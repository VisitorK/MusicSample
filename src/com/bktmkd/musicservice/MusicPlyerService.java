package com.bktmkd.musicservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.bktmkd.music.MainActivity;
import com.bktmkd.musicdb.MusicDBAdapter;
import com.bktmkd.musiclrc.MusicLrcContent;
import com.bktmkd.musiclrc.MusicLrcProcess;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class MusicPlyerService extends Service {
	private static String TAG = "MusicService";
	private int ID = 1;
	public String title = "";
	private MediaPlayer mPlayer;
	private Timer mTimer;
	private MusicTimerTask mTimerTask;
	public final static String BROADCAST_COUNTER_DURATION = "bktmkd.android.services.duration";
	private MusicLrcProcess mLrcProcess; // ��ʴ���
	public static boolean DownLoadLRCSucess = false;
	private List<MusicLrcContent> lrcList = new ArrayList<MusicLrcContent>(); // ��Ÿ���б����
	private int index = 0;
	private String path = "";

	@Override
	public IBinder onBind(Intent intent) {
		mPlayer.start();
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "CreateService");
		super.onCreate();
		mPlayer = new MediaPlayer();
		mPlayer.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				MusicDBAdapter dbAdapter = new MusicDBAdapter(MusicPlyerService.this);
				dbAdapter.getReadableDatabase();
				int count = dbAdapter.GetCount();
				// ������ŵĲ������һ�׸���
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
					dbAdapter.close();
				}
			}
		});
		mTimer = new Timer(true);
		mTimerTask = new MusicTimerTask();
		mTimer.schedule(mTimerTask, 10, 500);
	}

	@Override
	public void onDestroy() {
		mPlayer.stop();
		mTimerTask.cancel();
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Intent musicIntent = intent;
		// �����б�������
		if (musicIntent.hasExtra("DATA") && musicIntent.hasExtra("TITLE")) {
			title = intent.getStringExtra("TITLE");
			ID = Integer.parseInt(intent.getStringExtra("ID"));
			String DATA = musicIntent.getStringExtra("DATA");
			mPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(DATA));
			mPlayer.start();

		}
		// �������϶�λ�ò���
		else if (!title.equals("") && musicIntent.hasExtra("PROGRESS")) {
			int progress = (int) (((double) (mPlayer.getDuration())) * (double) (musicIntent.getIntExtra("PROGRESS", 0))
					/ (double) 100);
			mPlayer.seekTo(progress);
		}
		// ��ͣ
		else if (musicIntent.hasExtra("STOP")) {
			mPlayer.pause();

		}
		// ����
		else if (musicIntent.hasExtra("START")) {
			if (title.equals("")) {
				MusicDBAdapter dbAdapter = new MusicDBAdapter(MusicPlyerService.this);
				dbAdapter.getReadableDatabase();
				int count = dbAdapter.GetCount();
				// ������ŵĲ������һ�׸���
				if (count > 0) {

					ID = 1;
				}
				Cursor c = dbAdapter.querybyID(ID);
				if (c.moveToFirst()) {
					title = c.getString(1);
					mPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(c.getString(6)));
					initLrc(c.getString(6));
					mPlayer.start();
					dbAdapter.close();
				}
			} else {
				mPlayer.pause();
			}

		}
		// ��һ��
		else if (musicIntent.hasExtra("PRE")) {
			MusicDBAdapter dbAdapter = new MusicDBAdapter(MusicPlyerService.this);
			int count = dbAdapter.GetCount();
			dbAdapter.getReadableDatabase();
			if (title.equals("")) {
				// ������ŵĲ������һ�׸���
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
				mPlayer.reset();
				initLrc(c.getString(6));
				mPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(c.getString(6)));
				mPlayer.start();
				dbAdapter.close();
			}
		}
		// ��һ��
		else if (musicIntent.hasExtra("NEXT")) {
			MusicDBAdapter dbAdapter = new MusicDBAdapter(MusicPlyerService.this);
			int count = dbAdapter.GetCount();
			dbAdapter.getReadableDatabase();
			if (title.equals("")) {

				// ������ŵĲ������һ�׸���
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
				mPlayer.reset();
				initLrc(c.getString(6));
				mPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(c.getString(6)));
				mPlayer.start();
				dbAdapter.close();
			}
		}

	}

	@Override
	public boolean onUnbind(Intent intent) {
		mPlayer.stop();
		return super.onUnbind(intent);
	}

	public void initLrc(String path) {

		this.path = path;
		mLrcProcess = new MusicLrcProcess();

		// ��ȡ����ļ�
		mLrcProcess.readLRC(path, title);

		// ���ش����ĸ���ļ�
		lrcList = mLrcProcess.getLrcList();

		// ���ø��
		MainActivity.lrcView.setMusicLrcContent(lrcList);

		// �л���������ʾ���
		// MainActivity.lrcView.setAnimation(AnimationUtils.loadAnimation(MusicPlyerService.this,
		// android.R.anim.anticipate_interpolator));

		handler.post(mRunnable);
	}

	// ע�͵���һ�δ���
	Handler handler = new Handler();
	Runnable mRunnable = new Runnable() {

		public void run() {
			MainActivity.lrcView.setIndex(lrcIndex());
			MainActivity.lrcView.invalidate();
			handler.postDelayed(mRunnable, 100);
			if (DownLoadLRCSucess) {
				initLrc(path);
				DownLoadLRCSucess = false;
			}
		}
	};

	/**
	 * ����ʱ���ȡ�����ʾ������ֵ
	 * 
	 * @return ���ص�ǰ���ź���λ��
	 */
	public int lrcIndex() {
		int currentTime = 0;
		int duration = 0;
		if (mPlayer.isPlaying()) {
			currentTime = mPlayer.getCurrentPosition();
			duration = mPlayer.getDuration();
		}
		if (currentTime < duration) {
			for (int i = 0; i < lrcList.size(); i++) {
				if (i < lrcList.size() - 1) {
					if (currentTime < lrcList.get(i).getLrcTime() && i == 0) {
						index = i;
					}
					if (currentTime > lrcList.get(i).getLrcTime() && currentTime < lrcList.get(i + 1).getLrcTime()) {
						index = i;
					}
				}
				if (i == lrcList.size() - 1 && currentTime > lrcList.get(i).getLrcTime()) {
					index = i;
				}
			}
		}
		return index;
	}

	// ��ʱ��ҵ����ȡ��Ƶ��ǰ����λ�ã����㲥��ȥ
	public class MusicTimerTask extends TimerTask {

		@Override
		public boolean cancel() {
			return super.cancel();
		}

		@Override
		public void run() {
			if (!title.equals("")) {
				Intent intet = new Intent(BROADCAST_COUNTER_DURATION);
				intet.putExtra("DURATION", mPlayer.getDuration());
				intet.putExtra("CURRENTDURATION", mPlayer.getCurrentPosition());
				intet.putExtra("TITLE", title);
				sendBroadcast(intet);
			}
		}

		@Override
		public long scheduledExecutionTime() {
			return super.scheduledExecutionTime();
		}

	}
}
