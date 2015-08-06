package bktmkd.android.music;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.R.integer;
import android.content.Context;
import android.provider.Contacts.ContactMethods;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MusicBaseAdapter extends BaseAdapter  {

	private LayoutInflater mInflater;
    private	List<ObjectEntity> data;
	public  MusicBaseAdapter(Context context)
	{
	     super();
	     data=getData();
		this.mInflater=LayoutInflater.from(context);
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
		ViewHolder holder;
		ObjectEntity ob=data.get(position);
		if(convertView==null)
		{
         
			convertView=mInflater.inflate(R.layout.item_music, null);
		    holder=new ViewHolder();
			holder.img=(ImageView)convertView.findViewById(R.id.img);
			holder.txt=(TextView)convertView.findViewById(R.id.title);
			convertView.setTag(holder);
			   Log.d("bktmkd", "aaaaaaaaaaaaaa");
		}
		else
		{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.img.setBackgroundResource(ob.imgUrl);
		holder.txt.setText(ob.txt);
		return convertView;
	}
	private List<ObjectEntity> getData()
	{
		List<ObjectEntity> ListObject=new ArrayList<ObjectEntity>();
	boolean flag=false;
		for(int i=0;i<10000;i++)
		{
			ObjectEntity ob=new ObjectEntity();
			if(flag)
			{
			ob.imgUrl=R.drawable.pause;
			}
			else
			{
				ob.imgUrl=R.drawable.play;
			}
			flag=!flag;
			ob.txt=String.valueOf(i);
			ListObject.add(ob);
			
		}
		return ListObject;
		
			
		
	
	}
	public final class ViewHolder
	{
		public ImageView img;
		public TextView txt;
		}
	
	public class ObjectEntity {

	    public int imgUrl;
	    public String txt;
	}
}


