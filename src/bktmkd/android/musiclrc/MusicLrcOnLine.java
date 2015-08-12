package bktmkd.android.musiclrc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
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
import java.util.Random;

import org.apache.http.message.BufferedHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import bktmkd.android.music.MainActivity;
import bktmkd.android.services.MusicPlyerService;

public class MusicLrcOnLine extends AsyncTask{

	@Override
	protected Object doInBackground(Object... params) {

		WriteLRC(params[0].toString(), params[1].toString());
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		MusicPlyerService.DownLoadLRCSucess = true;
	}

	private static String TAG = "MuiscLrcOnLine";
	private static MusicLrcOnLine instance;
	public static final String lrcRootPath = Environment.getDownloadCacheDirectory().toString() + "/Lyrics/";
	public static final String queryLrcAPI = "http://geci.me/api/lyric/";

	public static MusicLrcOnLine getInstance() {
		if (instance == null) {
			instance = new MusicLrcOnLine();
		}
		return instance;
	}

	public String getQueryLrcURL(String title) {
		return queryLrcAPI + Encode(title);
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
	public String getLrcURL(String title) {

		String queryLrcURLStr = getQueryLrcURL(title);
		Log.d("bktmkd", queryLrcURLStr);
		String lrcurl = "";
		try {
			URL url = new URL(queryLrcURLStr);

			URLConnection urlConnection = url.openConnection();

			urlConnection.connect();
			Log.d("bktmkd", "aaaaaaaaaaaaaa");
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
	public boolean WriteLRC(String title, String lrcPath) {
		boolean flag = false;
		String urlPath = getLrcURL(title);

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
