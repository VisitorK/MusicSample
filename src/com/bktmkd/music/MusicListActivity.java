package com.bktmkd.music;

import com.bktmkd.music.PullToRefreshListView.OnRefreshListener;
import com.bktmkd.musicdb.MusicDBAdapter;
import com.bktmkd.musicservice.MusicPlyerService;
import com.bktmkd.musicservice.MusicPlyerService.MusicSampleBinder;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MusicListActivity extends Activity {
	private int currentMusic;
	private int currentPosition;
	private int currentMax;
	private ProgressBar bar;
	private ProgressReceiver progressReceiver;
	private MusicSampleBinder musicSampleBinder;
	private PullToRefreshListView list;
	private MusicBaseAdapter adapter;
	private TextView TVcount;
	private TextView TVtitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_musiclist);
		super.onCreate(savedInstanceState);
		if (MusicDBAdapter.musicList == null) {
			MusicDBAdapter.LoadDataFromDB(this);

		}
		connectToMusicPlayerService();
		registerReceiver();
		list = (PullToRefreshListView) findViewById(R.id.listView1);
		adapter = new MusicBaseAdapter(this, MusicDBAdapter.musicList);
		TVcount = (TextView) findViewById(R.id.textViewcout);
		TVtitle = (TextView) findViewById(R.id.textViewcurrenttitle);
		TVcount.setText(String.valueOf(MusicDBAdapter.musicList.size()));
		list.setAdapter(adapter);
		bar = (ProgressBar) findViewById(R.id.progressBar2);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				TextView tv = (TextView) arg1.findViewById(R.id.title);
				for (int i = 0; i < MusicDBAdapter.musicList.size(); i++) {
					if (MusicDBAdapter.musicList.get(i).getTITLE() == String.valueOf(tv.getText())) {

						musicSampleBinder.startPlay(i, 0);
						break;
					}
				}
			}
		});
		list.setonRefreshListener(new OnRefreshListener() {

			public void onRefresh() {
				// TODO Auto-generated method stub
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
							MusicDBAdapter.RefreshData(MusicListActivity.this);
						} catch (Exception e) {
							e.printStackTrace();

						}

						return null;

					}

					protected void onPostExecute(Void result) {
						adapter = new MusicBaseAdapter(MusicListActivity.this, MusicDBAdapter.musicList);
						list.setAdapter(adapter);
						TVcount.setText(String.valueOf(MusicDBAdapter.musicList.size()));
						list.onRefreshComplete();

					}
				}.execute(null, null, null);
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
				TVtitle.setText(MusicDBAdapter.musicList.get(currentMusic).getTITLE());

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
