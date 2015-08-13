package bktmkd.android.music;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class MusicSearchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_musicsearch);
		findViewById(R.id.btnsearch1).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text=((EditText)findViewById(R.id.editText1)).getText().toString().trim();
			   //Òì²½ËÑË÷¸èÇú
				SearchMusicAnsy _SearchMusicAnsy=new SearchMusicAnsy();
				_SearchMusicAnsy.execute(text);
			}
		});
	}
	
	public class SearchMusicAnsy extends AsyncTask<String,Integer,Boolean>
	{
		String SearchText="";
       
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			SearchText=params[0].toString();			
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
	}
	

}
