package com.bktmkd.music;

import java.util.ArrayList;
import java.util.List;
import com.bktmkd.musicdb.MusicDBAdapter;
import com.bktmkd.musiclrc.MusicLrcContent;
import com.bktmkd.musiclrc.MusicLrcProcess;
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
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
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
	public static MusicLrcView lrcView;
	public static int currentMusic;
	private int currentPosition;
	private int currentMax;
	private ProgressReceiver progressReceiver;
	private MusicSampleBinder musicSampleBinder;
	public static MusicLrcProcess musicProcess;
	public static List<MusicLrcContent> lrcList = new ArrayList<MusicLrcContent>(); //存放歌词列表对象  
	public static int index = 0; 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		  menu.add(0, 1, 1, R.string.list).setIcon(R.drawable.musicico);  
		  menu.add(0, 2, 2, R.string.exit).setIcon(R.drawable.exit);  
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==2)
		{
			musicSampleBinder.close();
			this.finish();
		}
		if(item.getItemId()==1)
		{
			Intent intent = new Intent(MainActivity.this, MusicListActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

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
        if(MusicDBAdapter.musicList==null)
        {
        	MusicDBAdapter.LoadDataFromDB(this);
        }
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
					 lrcView.setIndex(lrcIndex(currentPosition,currentMax));  
			          lrcView.invalidate();  

					bar.setProgress(progress / 1000);
				}
			} else if (MusicPlyerService.ACTION_UPDATE_CURRENT_MUSIC.equals(action)) {
				currentMusic = intent.getIntExtra(MusicPlyerService.ACTION_UPDATE_CURRENT_MUSIC, 0);
				if (musicSampleBinder.isPlaying()) {
					btnstartstop.setBackgroundResource(R.drawable.pause);
				} 
				
				musicTitle.setText(MusicDBAdapter.musicList.get(currentMusic).getTITLE());
				musicProcess = new MusicLrcProcess();  
			        //读取歌词文件  
				musicProcess.readLRC(MusicDBAdapter.musicList.get(currentMusic).getDATA(),MusicDBAdapter.musicList.get(currentMusic).getTITLE(),MusicDBAdapter.musicList.get(currentMusic).getARTIST());  
			        //传回处理后的歌词文件  
			        lrcList = musicProcess.getLrcList();  
			        lrcView.setMusicLrcContent(lrcList);  
			        //切换带动画显示歌词  
			        lrcView.setAnimation(AnimationUtils.loadAnimation(MainActivity.this,android.R.anim.fade_in));  
			} else if (MusicPlyerService.ACTION_UPDATE_DURATION.equals(action)) {
				currentMax = intent.getIntExtra(MusicPlyerService.ACTION_UPDATE_DURATION, 0);
				endTime.setText(getTimeFromInt(currentMax));
				bar.setMax(currentMax / 1000);
			}
		}

	}
	public int lrcIndex(int currentTime,int duration) {  
	    if(currentTime < duration) {  
	        for (int i = 0; i < lrcList.size(); i++) {  
	            if (i < lrcList.size() - 1) {  
	                if (currentTime < lrcList.get(i).getLrcTime() && i == 0) {  
	                    index = i;  
	                }  
	                if (currentTime > lrcList.get(i).getLrcTime()  
	                        && currentTime < lrcList.get(i + 1).getLrcTime()) {  
	                    index = i;  
	                }  
	            }  
	            if (i == lrcList.size() - 1  
	                    && currentTime > lrcList.get(i).getLrcTime()) {  
	                index = i;  
	            }  
	        }  
	    }  
	    return index;  
	}  

	private void registerReceiver() {
		progressReceiver = new ProgressReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(MusicPlyerService.ACTION_UPDATE_PROGRESS);
		intentFilter.addAction(MusicPlyerService.ACTION_UPDATE_DURATION);
		intentFilter.addAction(MusicPlyerService.ACTION_UPDATE_CURRENT_MUSIC);
		registerReceiver(progressReceiver, intentFilter);
	}

	public String getTimeFromInt(int time) {
		if (time <= 0) {
			return "0:00";
		} else {
			int secondnd = (time / 1000) / 60;
			int million = (time / 1000) % 60;
			String f =secondnd>=10? String.valueOf(secondnd):"0" + String.valueOf(secondnd);
			String m = million >= 10 ? String.valueOf(million) : "0" + String.valueOf(million);
			return f + ":" + m;
		}
	}
}