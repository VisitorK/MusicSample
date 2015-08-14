package bktmkd.android.music;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicSearchBaseAdapter extends BaseAdapter {
	private ArrayList<String> data;
	private Context context;
	private LayoutInflater layoutInflater;
	public MusicSearchBaseAdapter(Context context,ArrayList<String> data)
	{
		this.data=data;
		this.context=context;
		this.layoutInflater=LayoutInflater.from(context);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView==null)
		{
			convertView=layoutInflater.inflate(R.layout.item_music, null);
			holder=new ViewHolder();
			holder.img=(ImageView)convertView.findViewById(R.id.img);
			holder.title=(TextView)convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		}
		else
		{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.img.setBackgroundResource(R.drawable.musicico);
		holder.title.setText(data.get(position));
		return convertView;
	}
	
	public class ViewHolder
	{
		private ImageView img;
		private TextView title;
	}
	

}
