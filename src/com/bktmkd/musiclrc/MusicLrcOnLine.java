package com.bktmkd.musiclrc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bktmkd.music.MainActivity;
import com.bktmkd.musicdb.MusicDBAdapter;
import com.bktmkd.musicservice.MusicPlyerService;

import android.os.AsyncTask;
import android.util.Log;
import android.view.animation.AnimationUtils;

public class MusicLrcOnLine extends AsyncTask<String,Integer,Boolean>{
        private boolean hasonlinelrc=false;
	@Override
	protected Boolean doInBackground(String... params) {
		hasonlinelrc=WriteLRC(params[0].toString(), params[1].toString(),params[2].toString());
		return true;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		Log.d("bktmkd", String.valueOf(values[0]));
	//	MainActivity.musicTitle.setText(String.valueOf(values[0]));
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if(hasonlinelrc){
	MainActivity.musicProcess = new MusicLrcProcess();  
        //读取歌词文件  
	MainActivity.musicProcess.readLRC(MusicDBAdapter.musicList.get(MainActivity.currentMusic).getDATA(),MusicDBAdapter.musicList.get(MainActivity.currentMusic).getTITLE(),MusicDBAdapter.musicList.get(MainActivity.currentMusic).getARTIST());  
        //传回处理后的歌词文件  
	MainActivity.lrcList = MainActivity.musicProcess.getLrcList();  
	MainActivity.lrcView.setMusicLrcContent(MainActivity.lrcList);  
        //切换带动画显示歌词  
	}
	}
	private static MusicLrcOnLine instance;
	public static final String queryLrcAPI = "http://geci.me/api/lyric/";

	public static MusicLrcOnLine getInstance() {
		if (instance == null) {
			instance = new MusicLrcOnLine();
		}
		return instance;
	}

	public String getQueryLrcURL(String title,String artist) {
		return queryLrcAPI + Encode(title)+"/"+Encode(artist);
		
	}

	// 歌手，歌曲名中的空格进行转码
	public String Encode(String str) {

		try {
			return URLEncoder.encode(str.trim(), "utf-8");
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

		return str;

	}

	// 获取歌词文件地址
	public String getLrcURL(String title,String artist) {

		String queryLrcURLStr = getQueryLrcURL(title,artist);
		Log.d("bktmkd", queryLrcURLStr);
		String lrcurl = "";
		try {
			URL url = new URL(queryLrcURLStr);
			URLConnection urlConnection = url.openConnection();
			urlConnection.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String temp;
			while ((temp = in.readLine()) != null) {
				sb.append(temp);
			}
			Log.d("bktmkd", sb.toString());
			JSONObject jObject = new JSONObject(sb.toString());
			int count = jObject.getInt("count");
			int index = count == 0 ? 0 : 0;
			JSONArray jArray = jObject.getJSONArray("result");
			JSONObject obj = jArray.getJSONObject(index);
			lrcurl = obj.getString("lrc");
			Log.d("bktmkd1", lrcurl);
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return lrcurl;

	}

	// 歌词文件网络地址，歌词文件本地地址
	public boolean WriteLRC(String title, String lrcPath,String artist) {
		boolean flag = false;
		String urlPath = getLrcURL(title,artist);

		try {
			URL url = new URL(urlPath);
			URLConnection urlConnection = url.openConnection();
			urlConnection.connect();
			HttpURLConnection httpConn = (HttpURLConnection) urlConnection;
			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader bf = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
				PrintWriter out = new PrintWriter(
						new BufferedWriter(new OutputStreamWriter(new FileOutputStream(lrcPath))));
				char c[] = new char[256];
				@SuppressWarnings("unused")
				int temp = -1;
				while ((temp = bf.read()) != -1) {
					bf.read(c);
					out.write(c);
				}
				bf.close();
				out.close();
				flag = true;

			}
		} catch (MalformedURLException e) {

		} catch (IOException e) {

			e.printStackTrace();
		}

		return flag;

	}
}
