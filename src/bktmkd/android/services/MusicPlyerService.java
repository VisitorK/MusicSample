package bktmkd.android.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.R;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.animation.AnimationUtils;
import bktmkd.android.db.DBAdapter;
import bktmkd.android.music.MainActivity;
import bktmkd.android.musiclrc.MusicLrcContent;
import bktmkd.android.musiclrc.MusicLrcProcess;

public class MusicPlyerService extends Service {
	private static String TAG = "MusicService";
	private int ID = 1;
	public String title = "";
	private MediaPlayer mPlayer;
	private Timer mTimer;
	private MusicTimerTask mTimerTask;
	public final static String BROADCAST_COUNTER_DURATION = "bktmkd.android.services.duration";
	private MusicLrcProcess mLrcProcess; // 歌词处理
	private List<MusicLrcContent> lrcList = new ArrayList<MusicLrcContent>(); // 存放歌词列表对象
	private int index = 0;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		mPlayer.start();
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d(TAG, "CreateService");
		super.onCreate();
		mPlayer = new MediaPlayer();
		mTimer = new Timer(true);
		mTimerTask = new MusicTimerTask();
		mTimer.schedule(mTimerTask, 10, 500);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mPlayer.stop();
		mTimerTask.cancel();
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Intent musicIntent = intent;
		// 播放列表点击播放
		if (musicIntent.hasExtra("DATA") && musicIntent.hasExtra("TITLE")) {
			title = intent.getStringExtra("TITLE");
			ID = Integer.parseInt(intent.getStringExtra("ID"));
			String DATA = musicIntent.getStringExtra("DATA");
			mPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(DATA));
			mPlayer.setOnCompletionListener(new OnCompletionListener() {

				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					DBAdapter dbAdapter = new DBAdapter(MusicPlyerService.this);
					dbAdapter.getReadableDatabase();
					int count = dbAdapter.GetCount();
					// 如果播放的不是最后一首歌曲
					if (count > ID) {
						ID = ID + 1;
					} else {
						ID = 1;
					}
					Cursor c = dbAdapter.querybyID(ID);
					if (c.moveToFirst()) {
						title = c.getString(1);
						mPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(c.getString(6)));
						mPlayer.start();
					}
				}
			});
			mPlayer.start();

		}
		// 进度条拖动位置播放
		else if (!title.equals("") && musicIntent.hasExtra("PROGRESS")) {
			int progress = (int) (((double) (mPlayer.getDuration())) * (double) (musicIntent.getIntExtra("PROGRESS", 0))
					/ (double) 100);
			mPlayer.seekTo(progress);
		}
		// 暂停
		else if (musicIntent.hasExtra("STOP")) {
			mPlayer.pause();

		}
		// 播放
		else if (musicIntent.hasExtra("START")) {
			if (title.equals("")) {
				DBAdapter dbAdapter = new DBAdapter(MusicPlyerService.this);
				dbAdapter.getReadableDatabase();
				int count = dbAdapter.GetCount();
				// 如果播放的不是最后一首歌曲
				if (count > 0) {

					ID = 1;
				}
				Cursor c = dbAdapter.querybyID(ID);
				if (c.moveToFirst()) {
					title = c.getString(1);
					mPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(c.getString(6)));
					initLrc(c.getString(6));
					mPlayer.start();
				}
			} else {
				mPlayer.pause();
			}
	
		
		}
		// 上一曲
		else if (musicIntent.hasExtra("PRE")) {
			DBAdapter dbAdapter = new DBAdapter(MusicPlyerService.this);
			int count = dbAdapter.GetCount();
			dbAdapter.getReadableDatabase();
			if (title.equals("")) {

				// 如果播放的不是最后一首歌曲
				if (count > 0) {

					ID = 1;
				}
			} else {
				if (ID == 1) {
					ID = count;

				} else {
					ID = ID - 1;
				}

			}
			Cursor c = dbAdapter.querybyID(ID);
			if (c.moveToFirst()) {
				title = c.getString(1);
				mPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(c.getString(6)));
				mPlayer.start();
			}
		}
		// 下一曲
		else if (musicIntent.hasExtra("NEXT")) {
			DBAdapter dbAdapter = new DBAdapter(MusicPlyerService.this);
			int count = dbAdapter.GetCount();
			dbAdapter.getReadableDatabase();
			if (title.equals("")) {

				// 如果播放的不是最后一首歌曲
				if (count > 0) {

					ID = 1;
				}
			} else {
				if (ID == count) {
					ID = 1;

				} else {
					ID = ID + 1;
				}

			}
			Cursor c = dbAdapter.querybyID(ID);
			if (c.moveToFirst()) {
				title = c.getString(1);
				mPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(c.getString(6)));
				mPlayer.start();
			}
		}

	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		mPlayer.stop();
		return super.onUnbind(intent);
	}
	
	public void initLrc(String path) {
		mLrcProcess = new MusicLrcProcess();
		// 读取歌词文件
		mLrcProcess.readLRC(path);
		// 传回处理后的歌词文件
		lrcList = mLrcProcess.getLrcList();
	
		MainActivity.lrcView.setMusicLrcContent(lrcList);
	
		// 切换带动画显示歌词
	//	MainActivity.lrcView.setAnimation(AnimationUtils.loadAnimation(MusicPlyerService.this, android.R.anim.anticipate_interpolator));
		Log.d("bktmkd1111", "4243234");
		handler.post(mRunnable);
	}

	Handler handler = new Handler();

	Runnable mRunnable = new Runnable() {

		public void run() {
			MainActivity.lrcView.setIndex(lrcIndex());
			MainActivity.lrcView.invalidate();
			handler.postDelayed(mRunnable, 100);
		}
	};

	/**
	 * 根据时间获取歌词显示的索引值
	 * 
	 * @return
	 */
	public int lrcIndex() {
		int currentTime=0;
		int	duration=0 ;
		if (mPlayer.isPlaying()) {
			 currentTime = mPlayer.getCurrentPosition();
			 duration = mPlayer.getDuration();
		}
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

	public class MusicTimerTask extends TimerTask {

		@Override
		public boolean cancel() {
			// TODO Auto-generated method stub
			return super.cancel();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (!title.equals("")) {
				Intent intet = new Intent(BROADCAST_COUNTER_DURATION);
				intet.putExtra("DURATION", mPlayer.getDuration());
				intet.putExtra("CURRENTDURATION", mPlayer.getCurrentPosition());
				intet.putExtra("TITLE", title);
				// Log.d("sendBroadcast",
				// String.valueOf(mPlayer.getCurrentPosition()));
				sendBroadcast(intet);

			}
		}

		@Override
		public long scheduledExecutionTime() {
			// TODO Auto-generated method stub
			return super.scheduledExecutionTime();
		}

}
}
