package bktmkd.android.music;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class MusicHandler extends Handler {

	public MusicHandler(){
		
	}
	public MusicHandler(Looper L)
	{
		super(L);
	}
	@Override
	public void dispatchMessage(Message msg) {
		// TODO Auto-generated method stub
		super.dispatchMessage(msg);
	}

	@Override
	public String getMessageName(Message message) {
		// TODO Auto-generated method stub
		return super.getMessageName(message);
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		Bundle b=msg.getData();
		int currentProgress=(int)(((double)(b.getInt("CURRENTDURATION")))/((double)(b.getInt("DURATION")))*100);
		MainActivity.bar.setSecondaryProgress(currentProgress);
	}

	@Override
	public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
		// TODO Auto-generated method stub
		return super.sendMessageAtTime(msg, uptimeMillis);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	

}
