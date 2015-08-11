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
	                  
	                //���롰@���ַ�  
	                String splitLrcData[] = s.split("@");  
	                if(splitLrcData.length > 1) {  
	                    mLrcContent.setLrcStr(splitLrcData[1]);  
	                      
	                    //������ȡ�ø�����ʱ��  
	                    int lrcTime = timertoStr(splitLrcData[0]);  
	                      
	                    mLrcContent.setLrcTime(lrcTime);  
	                      
	                    //��ӽ��б�����  
	                    lrcList.add(mLrcContent);  
	                      
	                    //�´���������ݶ���  
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
	   //����ʱ����
		int minute = Integer.parseInt(timeData[0]);  
        int second = Integer.parseInt(timeData[1]);  
        int millisecond = Integer.parseInt(timeData[2]);  
          
        //������һ������һ�е�ʱ��ת��Ϊ������  
        int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;  
        return currentTime;  
	}
	public List<MusicLrcContent> getLrcList()
	{
		return lrcList;
	}
	
}
