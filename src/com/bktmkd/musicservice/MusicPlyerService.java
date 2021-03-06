package com.bktmkd.musicservice;

import java.util.ArrayList;
import java.util.List;

import com.bktmkd.music.MainActivity;
import com.bktmkd.music.R;
import com.bktmkd.musicdb.MusicDBAdapter;
import com.bktmkd.musicdb.MusicModel;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

public class MusicPlyerService extends Service {

	private static final String TAG = "com.bktmkd.music.NATURE_SERVICE";

	public static final String MUSICS = "com.bktmkd.music.MUSIC_LIST";

	public static final String NATURE_SERVICE = "com.bktmkd.music.NatureService";

	private MediaPlayer mediaPlayer;

	private boolean isPlaying = false;

	private Binder MusicSampleBinder = new MusicSampleBinder();

	private int currentMusic;
	private int currentPosition;

	private static final int updateProgress = 1;
	private static final int updateCurrentMusic = 2;
	private static final int updateDuration = 3;

	public static final String ACTION_UPDATE_PROGRESS = "com.bktmkd.music.UPDATE_PROGRESS";
	public static final String ACTION_UPDATE_DURATION = "com.bktmkd.music.UPDATE_DURATION";
	public static final String ACTION_UPDATE_CURRENT_MUSIC = "com.bktmkd.music.UPDATE_CURRENT_MUSIC";

	private Notification notification;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case updateProgress:
				toUpdateProgress();
				break;
			case updateDuration:
				toUpdateDuration();
				break;
			case updateCurrentMusic:
				toUpdateCurrentMusic();
				break;
			}
		}
	};

	// 更新进度广播
	private void toUpdateProgress() {
		if (mediaPlayer != null && isPlaying) {
			int progress = mediaPlayer.getCurrentPosition();
			Intent intent = new Intent();
			intent.setAction(ACTION_UPDATE_PROGRESS);
			intent.putExtra(ACTION_UPDATE_PROGRESS, progress);
			sendBroadcast(intent);
			handler.sendEmptyMessageDelayed(updateProgress, 1000);
		}
	}

	// 更新当前歌曲长度广播
	private void toUpdateDuration() {
		if (mediaPlayer != null) {
			int duration = mediaPlayer.getDuration();
			Intent intent = new Intent();
			intent.setAction(ACTION_UPDATE_DURATION);
			intent.putExtra(ACTION_UPDATE_DURATION, duration);
			sendBroadcast(intent);
		}
	}

	// 更新歌曲当前播放进度广播
	private void toUpdateCurrentMusic() {
		Intent intent = new Intent();
		intent.setAction(ACTION_UPDATE_CURRENT_MUSIC);
		intent.putExtra(ACTION_UPDATE_CURRENT_MUSIC, currentMusic);
		sendBroadcast(intent);
	}

	// service创建后执行
	public void onCreate() {
		initMediaPlayer();

		MusicDBAdapter.LoadDataFromDB(MusicPlyerService.this);
		if(MusicDBAdapter.musicList.size()==0)
		{
		MusicDBAdapter.RefreshData(MusicPlyerService.this);
		}
		Log.v(TAG, "OnCreate");
		super.onCreate();

		/*
		 * Intent intent = new Intent(this, MainActivity.class);
		 * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
		 * Intent.FLAG_ACTIVITY_SINGLE_TOP);
		 * 
		 * PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
		 * intent, 0); notification = new
		 * Notification.Builder(this).setTicker("Nature").setSmallIcon(R.
		 * drawable.music_app)
		 * .setContentTitle("Playing").setContentText(musicList.get(currentMusic
		 * ).getTITLE()) .setContentIntent(pendingIntent).getNotification();
		 * notification.flags |= Notification.FLAG_NO_CLEAR;
		 * 
		 * startForeground(1, notification);
		 */

	}

	// 停止service时执行
	public void onDestroy() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	/**
	 * 初始化service
	 */
	private void initMediaPlayer() {
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			public void onPrepared(MediaPlayer mp) {
				mediaPlayer.start();
				mediaPlayer.seekTo(currentPosition);
				handler.sendEmptyMessage(updateDuration);
			}
		});
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				if (isPlaying) {

					playNext();

				}

			}

		});
	}

	// 设置当前播放进度
	private void setCurrentMusic(int pCurrentMusic) {
		currentMusic = pCurrentMusic;
		handler.sendEmptyMessage(updateCurrentMusic);
	}

	//
	private void play(int currentMusic, int pCurrentPosition) {
		currentPosition = pCurrentPosition;
		setCurrentMusic(currentMusic);
		mediaPlayer.reset();
		try {
		
			mediaPlayer.setDataSource(MusicDBAdapter.musicList.get(currentMusic).getDATA());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.v(TAG, "[Play] Start Preparing at " + currentMusic);
		mediaPlayer.prepareAsync();
		handler.sendEmptyMessage(updateProgress);
		isPlaying = true;
	}

	private void stop() {
		mediaPlayer.stop();
		isPlaying = false;
	}

	private void playNext() {

		if (currentMusic + 1 == MusicDBAdapter.musicList.size()) {
			play(0, 0);
		} else {
			play(currentMusic + 1, 0);
		}

	}

	private void playPrevious() {

		if (currentMusic - 1 < 0) {
			play(MusicDBAdapter.musicList.size() - 1, 0);
		} else {
			play(currentMusic - 1, 0);
		}

	}

	@Override
	public IBinder onBind(Intent intent) {

		return MusicSampleBinder;
	}

	public class MusicSampleBinder extends Binder {

		public void startPlay(int currentMusic, int currentPosition) {

			play(currentMusic, currentPosition);
		}

		public void stopPlay() {
			stop();
		}

		public void toNext() {
			playNext();
		}

		public void toPrevious() {
			playPrevious();
		}

		public boolean isPlaying() {
			return isPlaying;
		}

		public void close()
		{
			onDestroy();
		}
		/**
		 * 更新提醒
		 */
		public void notifyActivity() {
			toUpdateCurrentMusic();
			toUpdateDuration();
		}

		/**
		 * Seekbar changes
		 * 
		 * @param progress
		 */
		public void changeProgress(int progress) {
			if (mediaPlayer != null) {
				currentPosition = progress * 1000;
				if (isPlaying) {
					mediaPlayer.seekTo(currentPosition);
				} else {
					play(currentMusic, currentPosition);
				}
			}
		}
	}

}