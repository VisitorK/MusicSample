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
		MainActivity.startTime.setText(formatTime(b.getInt("CURRENTDURATION")));
	    //最终时间不相同则更新
		if(MainActivity.endTime.getText()!=formatTime(b.getInt("DURATION")))
		{
		MainActivity.endTime.setText(formatTime(b.getInt("DURATION")));
		}
		String title=b.getString("TITLE").length()>15?b.getString("TITLE").substring(0, 15)+"...":b.getString("TITLE");
		if(MainActivity.musicTitle.getText()!=title)
		{
			MainActivity.musicTitle.setText(title);
		}
		
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
	
	  public String formatTime(int time)
	    {
		  int s = time / 1000;// 秒
	        int m = s / 60;// 分
	        int add = s % 60;// 秒    
	        String con = "";
	        if (add > 10 && m > 10)
	            con = m + ":" + add;
	        else if (add < 10 && m > 10)
	            con = m + ":0" + add;
	        else if (m < 10&& add < 10)
	            con = "0" + m + ":0" + add;
	        else if (m > 10 && add < 10)
	            con = "" + m + ":0" + add;    
	        else if (m < 10 &&  add > 10)
	            con = "0" + m + ":" + add ;    
	        return con;
	    }
	 
	
	 
	

}
