package bktmkd.android.musiclrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MusicLrcProcess {

	private List<MusicLrcContent> lrcList;
	private MusicLrcContent mLrcContent;
	
	public MusicLrcProcess()
	{
		lrcList=new ArrayList<MusicLrcContent>();
		mLrcContent=new MusicLrcContent();
	}
	public String readLRC(String path)
	{
		StringBuilder stringBuilder=new StringBuilder();
		File f=new File(path.replace(".mp3", ".lrc"));
		try
		{
			FileInputStream fis=new FileInputStream(f);
			InputStreamReader isr=new InputStreamReader(fis,"utf-8");
			BufferedReader br=new BufferedReader(isr);
			String s="";
			while((s=br.readLine())!=null)
			{
				  s = s.replace("[", "");  
	                s = s.replace("]", "@");  
	                  
	                //分离“@”字符  
	                String splitLrcData[] = s.split("@");  
	                if(splitLrcData.length > 1) {  
	                    mLrcContent.setLrcStr(splitLrcData[1]);  
	                      
	                    //处理歌词取得歌曲的时间  
	                    int lrcTime = timertoStr(splitLrcData[0]);  
	                      
	                    mLrcContent.setLrcTime(lrcTime);  
	                      
	                    //添加进列表数组  
	                    lrcList.add(mLrcContent);  
	                      
	                    //新创建歌词内容对象  
	                    mLrcContent = new MusicLrcContent(); 
			}
		}
			br.close();
		}
		catch(Exception e)
		{
			
		}
		return stringBuilder.toString();
	}
	public int timertoStr(String timeStr)
	{
		timeStr=timeStr.replace(":", ".");
		timeStr=timeStr.replace(".", "@");
		String timeData[]=timeStr.split("@");
	   //分离时分秒
		int minute = Integer.parseInt(timeData[0]);  
        int second = Integer.parseInt(timeData[1]);  
        int millisecond = Integer.parseInt(timeData[2]);  
          
        //计算上一行与下一行的时间转换为毫秒数  
        int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;  
        return currentTime;  
	}
	public List<MusicLrcContent> getLrcList()
	{
		return lrcList;
	}
	
}
