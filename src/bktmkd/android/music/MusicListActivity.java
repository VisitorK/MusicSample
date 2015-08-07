package bktmkd.android.music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import bktmkd.android.db.DBAdapter;

public class MusicListActivity extends Activity {

	private DBAdapter dbAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_musiclist);
		super.onCreate(savedInstanceState);
		ListView list=(ListView)findViewById(R.id.listView1);

		//SimpleAdapter adapter=new SimpleAdapter(this,getData(), R.layout.item_music,new String[]{"img","title"},new int[]{R.id.img,R.id.title});
       //list.setAdapter(adapter);
		dbAdapter=new DBAdapter(this);
		dbAdapter.getReadableDatabase();
		Cursor musicCursor=null;
		if(dbAdapter.queryALL().getCount()<1)
		{
		musicCursor=this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[]{MediaStore.Audio.Media.TITLE,
						MediaStore.Audio.Media.DURATION,
						MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media._ID,
						MediaStore.Audio.Media.DISPLAY_NAME,
						MediaStore.Audio.Media.DATA}, 
				null, null, null);
		
		if(musicCursor.moveToFirst())
		{
		for (int i = 0; i < musicCursor.getCount(); i++) {
			musicCursor.moveToPosition(i);
			ContentValues values = new ContentValues();
			values.put("TITLE", musicCursor.getString(0));
			values.put("DURATION", musicCursor.getString(1));
			values.put("ARTIST", musicCursor.getString(2));
			values.put("_MusicID", musicCursor.getString(3));
			values.put("DISPLAY_NAME", musicCursor.getString(4));
			values.put("DATA", musicCursor.getString(5));
			dbAdapter.insert(values);
		
		}
		}
		}
		MusicBaseAdapter adapter=new MusicBaseAdapter(MusicListActivity.this,dbAdapter);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				TextView tv=(TextView)arg1.findViewById(R.id.title);
				Cursor cursor=dbAdapter.query(String.valueOf(tv.getText()));
				if(cursor.moveToFirst())
				{
					Toast.makeText(MusicListActivity.this,cursor.getString(6), Toast.LENGTH_LONG).show();
				}
				
			}
			
		});
	
	}
}
