package bktmkd.android.music;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import bktmkd.android.music.R;
import bktmkd.android.services.MusicPlyerService;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	private ImageButton btnplay;
	private boolean PlayFlag=false;
    public static ProgressBar bar;
 public static   MusicHandler musicHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar=(ProgressBar)findViewById(R.id.progressBar1);
        musicHandler=new MusicHandler();
        Intent intent=getIntent();
       if(intent.hasExtra("DATA")&&intent.hasExtra("TITLE"))
       {
    	   
    	//   String TITLE = intent.getStringExtra("TITLE");   
    	   String DATA = intent.getStringExtra("DATA");    
    	   Toast.makeText(this, DATA, Toast.LENGTH_LONG).show();
       }
 
        btnplay=(ImageButton)findViewById(R.id.btnplay);
        MusicBroadCastReceive bll=new MusicBroadCastReceive();
        IntentFilter filter=new IntentFilter("bktmkd.android.services.duration");
        registerReceiver(bll, filter);
        btnplay.setOnClickListener(new OnClickListener() {
       	 Intent intentSV = new Intent(MainActivity.this, MusicPlyerService.class); 
			public void onClick(View v) {
			if(PlayFlag)
			{
				btnplay.setBackgroundResource(R.drawable.play);
				stopService(intentSV);
			}
			else
			{
				  btnplay.setBackgroundResource(R.drawable.pause);
			      startService(intentSV);  
			}
			PlayFlag=!PlayFlag;
			}
		});
        ImageButton btn1=(ImageButton)findViewById(R.id.btnoperate);
        btn1.setOnClickListener(new OnClickListener() {	
			public void onClick(View v) {
			startActivity(new Intent(MainActivity.this, MusicListActivity.class));
			}
		});
    }
    final ServiceConnection con=new ServiceConnection() {
		
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			   Toast.makeText(MainActivity.this, "MusicServiceActivity onSeviceDisconnected"
	                    , Toast.LENGTH_SHORT).show();
		}
		
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			 Toast.makeText(MainActivity.this, "MusicServiceActivity onSeviceDisconnected"
	                    , Toast.LENGTH_SHORT).show();
		}
	};
}