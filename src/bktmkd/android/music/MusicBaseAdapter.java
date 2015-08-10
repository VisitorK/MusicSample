package bktmkd.android.music;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import bktmkd.android.db.DBAdapter;

public class MusicBaseAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Cursor mycursor;
    private DBAdapter  dbAdapter;
	public MusicBaseAdapter(Context context,DBAdapter _dbAdapter) {
		super();
		this.dbAdapter=_dbAdapter;
		this.mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		this.mycursor=dbAdapter.queryALL();
		return this.mycursor.getCount();
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
		ViewHolder holder;
		this.mycursor.moveToPosition(position);
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.item_music, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.txt = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.img.setBackgroundResource(R.drawable.pause);
		holder.txt.setText(this.mycursor.getString(1));

		return convertView;
	}

	public final class ViewHolder {
		public ImageView img;
		public TextView txt;
	}

}
