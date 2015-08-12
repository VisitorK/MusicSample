package bktmkd.android.musiclrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MusicLrcProcess {

	private List<MusicLrcContent> lrcList;
	private MusicLrcContent mLrcContent;

	// 获取音频歌词
	public MusicLrcProcess() {
		lrcList = new ArrayList<MusicLrcContent>();
		mLrcContent = new MusicLrcContent();
	}

	// 在音频相同位置获取音频歌词
	@SuppressWarnings("unchecked")
	public String readLRC(String path,String title) {
		StringBuilder stringBuilder = new StringBuilder();
		File f = new File(path.replace(".mp3", ".lrc"));
		if(!f.exists())
		{
			MusicLrcOnLine _MusicLrcOnLine=new MusicLrcOnLine();
		    _MusicLrcOnLine.execute(title, path.replace(".mp3", ".lrc"));
		    
		}
		else
		{
		
		try {
			FileInputStream fis = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(fis, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while ((s = br.readLine()) != null) {
				s = s.replace("[", "");
				s = s.replace("]", "@");
				// 分离“@”字符
				String splitLrcData[] = s.split("@");
				if (splitLrcData.length > 1) {
					mLrcContent.setLrcStr(splitLrcData[1]);
					// 处理歌词取得歌曲的时间
					int lrcTime = timertoStr(splitLrcData[0]);
					mLrcContent.setLrcTime(lrcTime);
					// 添加进列表数组
					lrcList.add(mLrcContent);
					// 新创建歌词内容对象
					mLrcContent = new MusicLrcContent();
				}
			}
			br.close();
		} catch (Exception e) {

		}
		}
		return stringBuilder.toString();
	}

	public int timertoStr(String timeStr) {
		timeStr = timeStr.replace(":", ".");
		timeStr = timeStr.replace(".", "@");
		String timeData[] = timeStr.split("@");
		// 分离时分秒
		int minute = Integer.parseInt(timeData[0]);
		int second = Integer.parseInt(timeData[1]);
		int millisecond = Integer.parseInt(timeData[2]);
		// 计算上一行与下一行的时间转换为毫秒数
		int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
		return currentTime;
	}

	public List<MusicLrcContent> getLrcList() {
		return lrcList;
	}

}
