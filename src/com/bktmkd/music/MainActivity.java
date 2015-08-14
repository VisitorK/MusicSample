package com.bktmkd.music;

import com.bktmkd.musiclrc.MusicLrcView;
import com.bktmkd.musicservice.MusicPlyerService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	public static ImageButton btnplay;
	public static boolean PlayFlag = false;
	public static SeekBar bar;
	public static TextView startTime;
	public static TextView endTime;
	public static TextView musicTitle;
	public static MusicHandler musicHandler;
	public static MusicLrcView lrcView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bar = (SeekBar) findViewById(R.id.progressbar1);
		startTime = (TextView) findViewById(R.id.textView1);
		endTime = (TextView) findViewById(R.id.textView2);
		musicTitle = (TextView) findViewById(R.id.textViewtitle);
		lrcView = (MusicLrcView) findViewById(R.id.lrcShowView);
		Animation mAnimationRight = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
		lrcView.setAnimation(mAnimationRight);

		findViewById(R.id.btnsearch).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
			
			}
		});
		musicHandler = new MusicHandler();
		Intent intent = getIntent();
		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@SuppressWarnings("unused")
			boolean flag = false;

			public void onStopTrackingTouch(SeekBar seekBar) {
				flag = false;
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				flag = true;
			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (flag = true) {

					Intent intent = new Intent(MainActivity.this, MusicPlyerService.class);
					intent.putExtra("PROGRESS", bar.getProgress());
					startService(intent);
					flag = false;
				}

			}
		});
		if (intent.hasExtra("DATA") && intent.hasExtra("TITLE")) {

			// String TITLE = intent.getStringExtra("TITLE");
			String DATA = intent.getStringExtra("DATA");
			Toast.makeText(this, DATA, Toast.LENGTH_LONG).show();
		}

		btnplay = (ImageButton) findViewById(R.id.btnplay);
		MusicBroadCastReceive bll = new MusicBroadCastReceive();
		IntentFilter filter = new IntentFilter("bktmkd.android.services.duration");
		registerReceiver(bll, filter);
		btnplay.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (PlayFlag) {
					btnplay.setBackgroundResource(R.drawable.play);
					Intent intentSV = new Intent(MainActivity.this, MusicPlyerService.class);
					intentSV.putExtra("STOP", "STOP");
					stopService(intentSV);
				} else {
					btnplay.setBackgroundResource(R.drawable.pause);
					Intent intentSV = new Intent(MainActivity.this, MusicPlyerService.class);
					intentSV.putExtra("START", "START");
					startService(intentSV);
				}
				PlayFlag = !PlayFlag;
			}
		});
		ImageButton btn1 = (ImageButton) findViewById(R.id.btnoperate);
		btn1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, MusicListActivity.class));
			}
		});
		// 上一曲
		findViewById(R.id.btnpre).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intentSV = new Intent(MainActivity.this, MusicPlyerService.class);
				intentSV.putExtra("PRE", "PRE");
				startService(intentSV);

			}
		});
		// 下一曲
		findViewById(R.id.btnnext).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intentSV = new Intent(MainActivity.this, MusicPlyerService.class);
				intentSV.putExtra("NEXT", "NEXT");
				startService(intentSV);
			}
		});
	}

	final ServiceConnection con = new ServiceConnection() {

		public void onServiceDisconnected(ComponentName name) {
			Toast.makeText(MainActivity.this, "MusicServiceActivity onSeviceDisconnected", Toast.LENGTH_SHORT).show();
		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			Toast.makeText(MainActivity.this, "MusicServiceActivity onSeviceDisconnected", Toast.LENGTH_SHORT).show();
		}
	};
}