package bktmkd.android.music;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class MusicSearchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_musicsearch);
		findViewById(R.id.btnsearch1).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String text=((EditText)findViewById(R.id.editText1)).getText().toString().trim();
			   //Òì²½ËÑË÷¸èÇú
				SearchMusicAnsy _SearchMusicAnsy=new SearchMusicAnsy();
				_SearchMusicAnsy.execute(text);
			}
		});
	}
	
	public class SearchMusicAnsy extends AsyncTask<String,Integer,Boolean>
	{
		
		@Override
		protected Boolean doInBackground(String... params) {
			String	SearchText="http://box.zhangmen.baidu.com/x?op=12&count=1&title="+Encode(params[0])+"$$";
			try {
				URL url = new URL(SearchText);
				URLConnection urlConnection = url.openConnection();
				urlConnection.connect();
				BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String temp;
				while ((temp = in.readLine()) != null) {
					sb.append(temp);
				}
				@SuppressWarnings("unused")
				String result=sb.toString();
			try{
				ListView list=(ListView)findViewById(R.id.listviewsearch);
				//SAXParserFactory spf=SAXParserFactory.newInstance();
				//SAXParser saxParser=spf.newSAXParser();
				//saxParser.setProperty("http://xml.org/sax/features/namespaces",true);
				DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
				DocumentBuilder builder=factory.newDocumentBuilder();
				InputStream   inn   =   new   ByteArrayInputStream(sb.toString().getBytes("UTF-8"));  
				Document dom=builder.parse(inn);
				Element root=dom.getDocumentElement();
				NodeList items=root.getElementsByTagName("url");
				Log.d("bktmkd", sb.toString());
				for(int i=0;i<items.getLength();i++)
				{  
					Element currentNode = (Element) items.item(i);
					 NodeList childsNodes = currentNode.getChildNodes();
			
					String encodecontent=((Node) childsNodes.item(0)).getTextContent();
					String decodecontent=((Node) childsNodes.item(1)).getTextContent();
					String musicfile=decodecontent.split("\\?")[0];
					String sub=musicfile.split("\\.")[0];
					String url1=encodecontent.split(sub)[0];
					Log.d("bktmkd", encodecontent);
					Log.d("bktmkd", decodecontent);
					Log.d("bktmkd", "@@@@@@@@@@@@@@@@@@@@@@@@@"+sub);
					Log.d("bktmkd", url1);
					Log.d("bktmkd", url1+musicfile);

				}
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			} catch (MalformedURLException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			} 
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
	}
	public String Encode(String str) {

		try {
			return URLEncoder.encode(str.trim(), "utf-8");
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

		return str;

	}
	

}
