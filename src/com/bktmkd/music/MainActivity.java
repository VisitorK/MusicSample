package com.bktmkd.music;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.bktmkd.musicdb.MusicDBAdapter;
import com.bktmkd.musicdb.MusicModel;
import com.bktmkd.musiclrc.MusicLrcView;
import com.bktmkd.musicservice.MusicPlyerService;
import com.bktmkd.musicservice.MusicPlyerService.MusicSampleBinder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	private ImageButton btnstartstop;
	private ImageButton btnnext;
	private ImageButton btnprevious;
	private ImageButton btnList;
	private SeekBar bar;
	private TextView startTime;
	private TextView endTime;
	private TextView musicTitle;
	private MusicLrcView lrcView;
	private List<MusicModel> musicList = new ArrayList<MusicModel>();
	private int currentMusic;
	private int currentPosition;
	private int currentMax;
	private ProgressReceiver progressReceiver;
	private MusicSampleBinder musicSampleBinder;

	private ServiceConnection serviceConnection = new ServiceConnection() {
		public void onServiceDisconnected(ComponentName name) {
		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			musicSampleBinder = (MusicSampleBinder) service;
		}
	};

	private void connectToMusicPlayerService() {
		Intent intent = new Intent(MainActivity.this, MusicPlyerService.class);
		bindService(intent, serviceConnection, BIND_AUTO_CREATE);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MusicDBAdapter dbAdapter = new MusicDBAdapter(MainActivity.this);
		dbAdapter.getReadableDatabase();
		Cursor musicCursor = dbAdapter.queryALL();
		musicList.clear();
		if (musicCursor.moveToFirst()) {
			MusicModel model = new MusicModel();
			while (musicCursor.moveToNext()) {

				model.set_id(musicCursor.getInt(0));
				model.setTITLE(musicCursor.getString(1));
				model.setDURATION(musicCursor.getString(2));
				model.setARTIST(musicCursor.getString(3));
				model.set_MusicID(musicCursor.getString(4));
				model.setDISPLAY_NAME(musicCursor.getString(5));
				model.setDATA(musicCursor.getString(6));
				musicList.add(model);
			}
		}
		dbAdapter.close();
		connectToMusicPlayerService();
		bar = (SeekBar) findViewById(R.id.progressbar1);
		startTime = (TextView) findViewById(R.id.textView1);
		endTime = (TextView) findViewById(R.id.textView2);
		musicTitle = (TextView) findViewById(R.id.textViewtitle);
		lrcView = (MusicLrcView) findViewById(R.id.lrcShowView);
		btnstartstop = (ImageButton) findViewById(R.id.btnplay);
		btnnext = (ImageButton) findViewById(R.id.btnnext);
		btnprevious = (ImageButton) findViewById(R.id.btnpre);
		btnList = (ImageButton) findViewById(R.id.btnoperate);
		btnnext.setOnClickListener(this);
		btnprevious.setOnClickListener(this);
		btnstartstop.setOnClickListener(this);
		btnList.setOnClickListener(this);
		registerReceiver();
		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser) {
					musicSampleBinder.changeProgress(progress);
				}
			}
		});

	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnplay:
			play(currentMusic, R.id.btnplay);

			break;
		case R.id.btnnext:
			musicSampleBinder.toNext();
			break;
		case R.id.btnpre:
			musicSampleBinder.toPrevious();
			break;
		case R.id.btnoperate:
			Intent intent = new Intent(MainActivity.this, MusicListActivity.class);
			startActivity(intent);

		}
	}

	private void play(int position, int resId) {
		if (musicSampleBinder.isPlaying()) {
			musicSampleBinder.stopPlay();
			btnstartstop.setBackgroundResource(R.drawable.play);
		} else {
			musicSampleBinder.startPlay(position, currentPosition);
			btnstartstop.setBackgroundResource(R.drawable.pause);
		}
	}

	class ProgressReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (MusicPlyerService.ACTION_UPDATE_PROGRESS.equals(action)) {
				int progress = intent.getIntExtra(MusicPlyerService.ACTION_UPDATE_PROGRESS, 0);
				if (progress > 0) {
					currentPosition = progress;
					startTime.setText(getTimeFromInt(currentPosition));
					
					bar.setProgress(progress / 1000);
				}
			} else if (MusicPlyerService.ACTION_UPDATE_CURRENT_MUSIC.equals(action)) {
				currentMusic = intent.getIntExtra(MusicPlyerService.ACTION_UPDATE_CURRENT_MUSIC, 0);
				musicTitle.setText(musicList.get(currentMusic).getTITLE());
			} else if (MusicPlyerService.ACTION_UPDATE_DURATION.equals(action)) {
				currentMax = intent.getIntExtra(MusicPlyerService.ACTION_UPDATE_DURATION, 0);
		     	endTime.setText(getTimeFromInt(currentMax));
				bar.setMax(currentMax / 1000);
			}
		}

	}

	private void registerReceiver() {
		progressReceiver = new ProgressReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(MusicPlyerService.ACTION_UPDATE_PROGRESS);
		intentFilter.addAction(MusicPlyerService.ACTION_UPDATE_DURATION);
		intentFilter.addAction(MusicPlyerService.ACTION_UPDATE_CURRENT_MUSIC);
		registerReceiver(progressReceiver, intentFilter);
	}

	public   String getTimeFromInt(int time) {
		if (time <= 0)
		 {return "0:00";}
		else{
			int secondnd = (time/1000)/60;
			int million = (time / 1000) % 60;
			String f = String.valueOf(secondnd);
			String m = million >= 10? String.valueOf(million) : "0"+ String.valueOf(million);
			return f + ":" + m;
			}
	}
}