package bktmkd.android.music;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bktmkd.android.db.DBAdapter;
import bktmkd.android.services.MusicPlyerService;

public class MusicListActivity extends Activity {
	private DBAdapter dbAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_musiclist);
		super.onCreate(savedInstanceState);
		ListView list=(ListView)findViewById(R.id.listView1);
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
				TextView tv=(TextView)arg1.findViewById(R.id.title);
				Cursor cursor=dbAdapter.query(String.valueOf(tv.getText()));
				if(cursor.moveToFirst())
				{
					Toast.makeText(MusicListActivity.this,cursor.getString(6), Toast.LENGTH_LONG).show();
					Intent intent=new Intent(MusicListActivity.this,MusicPlyerService.class);
					intent.putExtra("TITLE", cursor.getString(2));
					intent.putExtra("DATA", cursor.getString(6));
					startService(intent);
					finish();
					}
			}
		});
	}
}
