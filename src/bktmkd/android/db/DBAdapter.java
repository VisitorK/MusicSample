package bktmkd.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter extends SQLiteOpenHelper {	      
	private final static int VERSION = 1;
	private final static String DB_NAME = "phones.db";
	private final static String TABLE_NAME = "phone";
	private final static String CREATE_TBL = "create table phone(_id integer primary key autoincrement, name text, sex text, number text, desc text)";
	private SQLiteDatabase db;

	//SQLiteOpenHelper�������Ҫ��һ�����캯��
	public DBAdapter(Context context, String name, CursorFactory factory,int version) {
		//����ͨ��super ���ø���Ĺ��캯��
		super(context, name, factory, version);
	}
	
	//���ݿ�Ĺ��캯������������������
	public DBAdapter(Context context, String name, int version){
		this(context, name, null, version);
	}
	
	//���ݿ�Ĺ��캯��������һ�������ģ� ���ݿ����ֺͰ汾�Ŷ�д����
	public DBAdapter(Context context){
		this(context, DB_NAME, null, VERSION);
	}
	
    // �ص���������һ�δ���ʱ�Ż���ô˺���������һ�����ݿ�
	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		db.execSQL(CREATE_TBL);
	}

	//�ص����������㹹��DBHelper�Ĵ��ݵ�Version��֮ǰ��Version���ô˺���
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	

	}
	
	//���뷽��
	public void insert(ContentValues values){
		//��ȡSQLiteDatabaseʵ��
		SQLiteDatabase db = getWritableDatabase();
		//�������ݿ���
		db.insert(TABLE_NAME, null, values);
		db.close();
	}
	
	//��ѯ����
	public Cursor query(){
		SQLiteDatabase db = getReadableDatabase();
		//��ȡCursor
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null, null);
		return c;
		
	}
	
	//����Ψһ��ʶ_id  ��ɾ������
	public void delete(int id){
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, "_id=?", new String[]{String.valueOf(id)});
	}
	
	
	//�������ݿ������
	public void update(ContentValues values, String whereClause, String[]whereArgs){
		SQLiteDatabase db = getWritableDatabase();
		db.update(TABLE_NAME, values, whereClause, whereArgs);
	}
	
	//�ر����ݿ�
	public void close(){
		if(db != null){
			db.close();
		}
	}

}