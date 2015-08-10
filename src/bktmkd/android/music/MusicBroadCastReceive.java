package bktmkd.android.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

public class MusicBroadCastReceive extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent inte=intent;
		if(inte.hasExtra("DURATION"))
		{
           //  Toast.makeText(context, String.valueOf(inte.getIntExtra("DURATION", 0)), Toast.LENGTH_LONG).show();	
			  Message message = new Message(); 	
			  Bundle b=new Bundle();
			  b.putInt("DURATION", inte.getIntExtra("DURATION", 0));
			  b.putInt("CURRENTDURATION", inte.getIntExtra("CURRENTDURATION", 0));
			  message.what=1;
			  message.setData(b);
			  MainActivity.musicHandler.sendMessage(message);
			
		}
	}
}
