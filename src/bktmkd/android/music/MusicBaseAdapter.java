package bktmkd.android.music;

import java.util.ArrayList;
import java.util.HashMap;

import android.R.integer;
import android.content.Context;
import android.provider.Contacts.ContactMethods;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicBaseAdapter extends BaseAdapter  {

	private LayoutInflater mInflater;
	public  MusicBaseAdapter(Context context)
	{
	     super();
		this.mInflater=LayoutInflater.from(context);
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return getData().size();
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
		if(convertView==null)
		{

			convertView=mInflater.inflate(R.layout.item_music, null);
			holder=new ViewHolder();
			holder.img=(ImageView)convertView.findViewById(R.id.img);
			holder.txt=(TextView)convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		}
		else
		{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.img.setBackgroundResource(Integer.parseInt(getData().get(position).get("img").toString()));
		holder.txt.setText(getData().get(position).get("text").toString());
		return null;
	}
	private ArrayList<HashMap<String,Object>> getData()
	{
		ArrayList<HashMap<String,Object>> listItem=new ArrayList<HashMap<String,Object>>();
		/**为动态数组添加数据*/     
		for(int i=0;i<30;i++)  
         {  
             HashMap<String, Object> map = new HashMap<String, Object>();  
             map.put("img", R.drawable.pause);  
             map.put("text", "这是第"+i+"行");  
             listItem.add(map);  
         } 
        return listItem;
	}
	public final class ViewHolder
	{
		public ImageView img;
		public TextView txt;
		}
	

}


