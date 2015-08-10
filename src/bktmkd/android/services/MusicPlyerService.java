package bktmkd.android.services;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class MusicPlyerService extends Service {
	private static String TAG = "MusicService";
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
		mPlayer=MediaPlayer.create(getApplicationContext(), bktmkd.android.music.R.raw.aaa);
	   mPlayer.setLooping(true);
		super.onCreate();
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
		Intent musicIntent=intent;
		   if(musicIntent.hasExtra("DATA")&&musicIntent.hasExtra("TITLE"))
	       {
	    	//   String TITLE = intent.getStringExtra("TITLE");   
	    	   String DATA = musicIntent.getStringExtra("DATA");    
	    	   Toast.makeText(this, DATA, Toast.LENGTH_LONG).show();
	    	   mPlayer=MediaPlayer.create(getApplicationContext(), Uri.parse(DATA));
	       }
		   mPlayer.start();
		   mTimer=new Timer(true);
		   mTimerTask=new MusicTimerTask();
		   mTimer.schedule(mTimerTask,10,500); 
		   Log.d("bktmkd",  musicIntent.getStringExtra("TITLE"));
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		mPlayer.stop();
		return super.onUnbind(intent);
	}
	public class MusicTimerTask extends TimerTask
	
	{

		@Override
		public boolean cancel() {
			// TODO Auto-generated method stub
			return super.cancel();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
		//	mPlayer.getDuration();
			Intent intet=new Intent(BROADCAST_COUNTER_DURATION);
			intet.putExtra("DURATION",mPlayer.getDuration());
			intet.putExtra("CURRENTDURATION", mPlayer.getCurrentPosition());
			sendBroadcast(intet);
			Log.d("bktmkd", String.valueOf(mPlayer.getCurrentPosition()));
			
		 
		}

		@Override
		public long scheduledExecutionTime() {
			// TODO Auto-generated method stub
			return super.scheduledExecutionTime();
		}
		
	}

}
