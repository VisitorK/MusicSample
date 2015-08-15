package com.bktmkd.music;

import java.util.ArrayList;
import com.bktmkd.musicdb.MusicModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicBaseAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<MusicModel> data;

	public MusicBaseAdapter(Context context, ArrayList<MusicModel> list) {
		super();
		this.data = list;
		this.mInflater = LayoutInflater.from(context);
	}

	public int getCount() {

		return this.data.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.item_music, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.txt = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.img.setBackgroundResource(R.drawable.musicico);
		holder.txt.setText(this.data.get(position).getTITLE());

		return convertView;
	}

	public final class ViewHolder {
		public ImageView img;
		public TextView txt;
	}

}
