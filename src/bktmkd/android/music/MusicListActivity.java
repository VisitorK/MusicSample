package bktmkd.android.music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MusicListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_musiclist);
		super.onCreate(savedInstanceState);
		ListView list=(ListView)findViewById(R.id.listView1);

		//SimpleAdapter adapter=new SimpleAdapter(this,getData(), R.layout.item_music,new String[]{"img","title"},new int[]{R.id.img,R.id.title});
       //list.setAdapter(adapter);
		Cursor c=this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media._ID,MediaStore.Audio.Media.DISPLAY_NAME,MediaStore.Audio.Media.DATA}, null, null, null);
		
		MusicBaseAdapter adapter=new MusicBaseAdapter(MusicListActivity.this,c);
		list.setAdapter(adapter);
	}
	private List<Map<String,Object>> getData()
	{
       List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
       Map<String,Object> map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "aaaa");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.play);
       map.put("title", "aaa3543a");
       list.add(map);
       
       
       map=new HashMap<String,Object>();
       map.put("img",R.drawable.pause);
       map.put("title", "esfsfsdaaaa");
       list.add(map);
       
       return list;
	}
	

}
