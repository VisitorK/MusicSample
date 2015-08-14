package com.bktmkd.music;

import com.bktmkd.music.MainActivity.ProgressReceiver;
import com.bktmkd.musicdb.MusicDBAdapter;
import com.bktmkd.musicservice.MusicPlyerService;
import com.bktmkd.musicservice.MusicPlyerService.MusicSampleBinder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MusicListActivity extends Activity {
	private MusicDBAdapter dbAdapter;
	private int currentMusic;
	private int currentPosition;
	private int currentMax;
	private ProgressBar bar;
	private ProgressReceiver progressReceiver;
	private MusicSampleBinder musicSampleBinder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_musiclist);
		super.onCreate(savedInstanceState);
		connectToMusicPlayerService();
		registerReceiver();
		ListView list=(ListView)findViewById(R.id.listView1);
		dbAdapter=new MusicDBAdapter(this);
		dbAdapter.getReadableDatabase();
		Cursor musicCursor=null;
		if(dbAdapter.queryALL().getCount()<1)
		{
		musicCursor=this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[]{MediaStore.Audio.Media.TITLE,
						MediaStore.Audio.Media.DURATION,
						MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media._ID,
						MediaStore.Audio.Media.DISPLAY_NAME,
						MediaStore.Audio.Media.DATA}, 
				null, null, null);
		if(musicCursor.moveToFirst())
		{
		for (int i = 0; i < musicCursor.getCount(); i++) {
			musicCursor.moveToPosition(i);
			ContentValues values = new ContentValues();
			values.put("TITLE", musicCursor.getString(0));
			values.put("DURATION", musicCursor.getString(1));
			values.put("ARTIST", musicCursor.getString(2));
			values.put("_MusicID", musicCursor.getString(3));
			values.put("DISPLAY_NAME", musicCursor.getString(4));
			values.put("DATA", musicCursor.getString(5));
			dbAdapter.insert(values);
		}
		}
		}
		MusicBaseAdapter adapter=new MusicBaseAdapter(MusicListActivity.this,dbAdapter);
		list.setAdapter(adapter);
		bar=(ProgressBar)findViewById(R.id.progressBar2);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				TextView tv=(TextView)arg1.findViewById(R.id.title);
				Cursor cursor=dbAdapter.query(String.valueOf(tv.getText()));
				if(cursor.moveToFirst())
				{
					musicSampleBinder.startPlay(cursor.getInt(0), 0);
					}
			}
		});
	}
	

	class ProgressReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (MusicPlyerService.ACTION_UPDATE_PROGRESS.equals(action)) {
				int progress = intent.getIntExtra(MusicPlyerService.ACTION_UPDATE_PROGRESS, 0);
				if (progress > 0) {
					currentPosition = progress;
					bar.setProgress(progress / 1000);
				}
			} else if (MusicPlyerService.ACTION_UPDATE_CURRENT_MUSIC.equals(action)) {
				currentMusic = intent.getIntExtra(MusicPlyerService.ACTION_UPDATE_CURRENT_MUSIC, 0);
			
			} else if (MusicPlyerService.ACTION_UPDATE_DURATION.equals(action)) {
				currentMax = intent.getIntExtra(MusicPlyerService.ACTION_UPDATE_DURATION, 0);
				bar.setMax(currentMax / 1000);
			}
		}

	}
	private ServiceConnection serviceConnection = new ServiceConnection() {
		public void onServiceDisconnected(ComponentName name) {
		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			musicSampleBinder = (MusicSampleBinder) service;
		}
	};
	private void connectToMusicPlayerService() {
		Intent intent = new Intent(MusicListActivity.this, MusicPlyerService.class);
		bindService(intent, serviceConnection, BIND_AUTO_CREATE);
	}

	private void registerReceiver() {
		progressReceiver = new ProgressReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(MusicPlyerService.ACTION_UPDATE_PROGRESS);
		intentFilter.addAction(MusicPlyerService.ACTION_UPDATE_DURATION);
		intentFilter.addAction(MusicPlyerService.ACTION_UPDATE_CURRENT_MUSIC);
		registerReceiver(progressReceiver, intentFilter);
	}

}
