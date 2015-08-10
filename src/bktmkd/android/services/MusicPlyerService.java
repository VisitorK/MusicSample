package bktmkd.android.services;

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
		   Toast.makeText(getApplicationContext(),  musicIntent.getStringExtra("DATA"), Toast.LENGTH_LONG).show();
		   Log.d("bktmkd",  musicIntent.getStringExtra("TITLE"));
		

	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		mPlayer.stop();
		return super.onUnbind(intent);
	}

}
