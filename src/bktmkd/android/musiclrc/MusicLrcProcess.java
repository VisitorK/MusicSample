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

	// ��ȡ��Ƶ���
	public MusicLrcProcess() {
		lrcList = new ArrayList<MusicLrcContent>();
		mLrcContent = new MusicLrcContent();
	}

	// ����Ƶ��ͬλ�û�ȡ��Ƶ���
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
				// ���롰@���ַ�
				String splitLrcData[] = s.split("@");
				if (splitLrcData.length > 1) {
					mLrcContent.setLrcStr(splitLrcData[1]);
					// ������ȡ�ø�����ʱ��
					int lrcTime = timertoStr(splitLrcData[0]);
					mLrcContent.setLrcTime(lrcTime);
					// ��ӽ��б�����
					lrcList.add(mLrcContent);
					// �´���������ݶ���
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
		// ����ʱ����
		int minute = Integer.parseInt(timeData[0]);
		int second = Integer.parseInt(timeData[1]);
		int millisecond = Integer.parseInt(timeData[2]);
		// ������һ������һ�е�ʱ��ת��Ϊ������
		int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
		return currentTime;
	}

	public List<MusicLrcContent> getLrcList() {
		return lrcList;
	}

}
