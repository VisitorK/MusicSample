package com.bktmkd.musicdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MusicDBAdapter extends SQLiteOpenHelper {	      
	private final static int VERSION = 1;
	private final static String DB_NAME = "musicdata.db";
	private final static String TABLE_NAME = "music";
	private final static String CREATE_TBL = "create table music(_id integer primary key autoincrement, TITLE text, DURATION text, ARTIST text, _MusicID text,DISPLAY_NAME text,DATA text)";
	private SQLiteDatabase db;

	//SQLiteOpenHelper�������Ҫ��һ�����캯��
	public MusicDBAdapter(Context context, String name, CursorFactory factory,int version) {
		//����ͨ��super ���ø���Ĺ��캯��
		super(context, name, factory, version);
	}
	
	//���ݿ�Ĺ��캯������������������
	public MusicDBAdapter(Context context, String name, int version){
		this(context, name, null, version);
	}
	
	//���ݿ�Ĺ��캯��������һ�������ģ� ���ݿ����ֺͰ汾�Ŷ�д����
	public MusicDBAdapter(Context context){

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
	public Cursor queryALL(){
		SQLiteDatabase db = getReadableDatabase();
		//��ȡCursor
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null, null);
		return c;
		
	}
	public Cursor query(String musicTITLE)
	{

		SQLiteDatabase db = getReadableDatabase();
		//��ȡCursor
		Cursor c = db.query(TABLE_NAME, null, "TITLE=?", new String[]{musicTITLE}, null, null, null, null);
		return c;	
	}
	//�����ж���������
	public int GetCount()
	{
		SQLiteDatabase db = getReadableDatabase();
		//��ȡCursor
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null, null);
		return c.getCount();
	}
	//����ID��������
	public Cursor querybyID(int _id)
	{

		SQLiteDatabase db = getReadableDatabase();
	    return	db.rawQuery("select * from music where _id="+_id, null);
	//	Cursor c = db.query(TABLE_NAME, null, "_id="+_id, null, null, null, null, null);
	//	return c;	
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