package bktmkd.android.services;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import bktmkd.android.db.DBAdapter;

public class MusicPlyerService extends Service {
	private static String TAG = "MusicService";
	private String ID="";
	public String title = "";
	private MediaPlayer mPlayer;
	private Timer mTimer;
	private MusicTimerTask mTimerTask;
	public final static String BROADCAST_COUNTER_DURATION = "bktmkd.android.services.duration";

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
		if (musicIntent.hasExtra("DATA") && musicIntent.hasExtra("TITLE")) {
			title = intent.getStringExtra("TITLE");
			ID=intent.getStringExtra("ID");
			String DATA = musicIntent.getStringExtra("DATA");
			mPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(DATA));
			 mPlayer.setOnCompletionListener(new OnCompletionListener() {
					
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
				    	DBAdapter	dbAdapter=new DBAdapter(MusicPlyerService.this);
						dbAdapter.getReadableDatabase();
					    Cursor cur=dbAdapter.queryALL();
					    Log.d("bktmkd1111111111111", ID);
					    if(cur.getCount()>Integer.parseInt(ID))
					    {
					    	ID=String.valueOf(Integer.parseInt(ID)+1);
					    	cur.moveToPosition(Integer.parseInt(ID)+1);
					    	title=cur.getString(1);
					    	mPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(cur.getString(6)));
							mPlayer.start();
					    }
					    else
					    {
					    	ID="1";
					    	cur.moveToFirst();
					    	title=cur.getString(1);
					    	mPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(cur.getString(6)));
							mPlayer.start();
					    }
					}
				});
			mPlayer.start();
			mTimer = new Timer(true);
			mTimerTask = new MusicTimerTask();
			mTimer.schedule(mTimerTask, 10, 500);
			Log.d("MusicService", musicIntent.getStringExtra("TITLE"));
		}
		if (!title.equals("") && musicIntent.hasExtra("PROGRESS")) {
			int progress = (int) (((double) (mPlayer.getDuration())) * (double) (musicIntent.getIntExtra("PROGRESS", 0))
					/ (double) 100);
			mPlayer.seekTo(progress);
		}

	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		mPlayer.stop();
		return super.onUnbind(intent);
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
			Intent intet = new Intent(BROADCAST_COUNTER_DURATION);
			intet.putExtra("DURATION", mPlayer.getDuration());
			intet.putExtra("CURRENTDURATION", mPlayer.getCurrentPosition());
			intet.putExtra("TITLE", title);
			sendBroadcast(intet);
		}

		@Override
		public long scheduledExecutionTime() {
			// TODO Auto-generated method stub
			return super.scheduledExecutionTime();
		}

	}

}
