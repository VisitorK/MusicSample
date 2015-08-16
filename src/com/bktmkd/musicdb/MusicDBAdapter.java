package com.bktmkd.musicdb;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;

public class MusicDBAdapter extends SQLiteOpenHelper {
	private final static int VERSION = 1;
	private final static String DB_NAME = "musicdata.db";
	private final static String TABLE_NAME = "music";
	private final static String CREATE_TBL = "create table music(_id integer primary key autoincrement, TITLE text, DURATION text, ARTIST text, _MusicID text,DISPLAY_NAME text,DATA text)";
	private SQLiteDatabase db;
	public static ArrayList<MusicModel> musicList;
	public static Cursor musicCursor;

	public static void LoadDataFromDB(Context context) {
		musicList = new ArrayList<MusicModel>();
		MusicDBAdapter dbAdapter = new MusicDBAdapter(context);
		dbAdapter.getReadableDatabase();
		if(musicCursor!=null&&!musicCursor.isClosed())
		{
			musicCursor.close();
		
		}
		musicCursor = dbAdapter.queryALL();
		musicList.clear();
		if (musicCursor.moveToFirst()) {
			MusicModel model = new MusicModel();
			for (int i = 0; i < musicCursor.getCount(); i++) {
				musicCursor.moveToPosition(i);
				model = null;
				model = new MusicModel();
				model.set_id(musicCursor.getInt(0));
				model.setTITLE(musicCursor.getString(1));
				model.setDURATION(musicCursor.getString(2));
				model.setARTIST(musicCursor.getString(3));
				model.set_MusicID(musicCursor.getString(4));
				model.setDISPLAY_NAME(musicCursor.getString(5));
				model.setDATA(musicCursor.getString(6));
				musicList.add(model);
			}
		}
		musicCursor.close();
		dbAdapter.close();
	}

	public static void RefreshData(Context context) {
		MusicDBAdapter dbAdapter = new MusicDBAdapter(context);
		dbAdapter.getReadableDatabase();
		dbAdapter.deleteAll();
		if(musicCursor!=null&&!musicCursor.isClosed())
		{
			musicCursor.close();
		}
		musicCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION,
						MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME,
						MediaStore.Audio.Media.DATA },
				null, null, null);
		if (musicCursor.moveToFirst()) {
			for (int i = 0; i < musicCursor.getCount(); i++) {
				musicCursor.moveToPosition(i);
				ContentValues values = new ContentValues();
				values.put("TITLE", musicCursor.getString(0));
				values.put("DURATION", musicCursor.getString(1));
				values.put("ARTIST", musicCursor.getString(2));
				values.put("_MusicID", musicCursor.getString(3));
				values.put("DISPLAY_NAME", musicCursor.getString(4));
				values.put("DATA", musicCursor.getString(5));
				dbAdapter.insert(values);
			}
		}
		musicCursor.close();
		dbAdapter.close();
		LoadDataFromDB(context);
	}

	// SQLiteOpenHelper�������Ҫ��һ�����캯��
	public MusicDBAdapter(Context context, String name, CursorFactory factory, int version) {
		// ����ͨ��super ���ø���Ĺ��캯��
		super(context, name, factory, version);
	}

	// ���ݿ�Ĺ��캯������������������
	public MusicDBAdapter(Context context, String name, int version) {
		this(context, name, null, version);
	}

	// ���ݿ�Ĺ��캯��������һ�������ģ� ���ݿ����ֺͰ汾�Ŷ�д����
	public MusicDBAdapter(Context context) {

		this(context, DB_NAME, null, VERSION);

	}

	// �ص���������һ�δ���ʱ�Ż���ô˺���������һ�����ݿ�
	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		db.execSQL(CREATE_TBL);
	}

	// �ص����������㹹��DBHelper�Ĵ��ݵ�Version��֮ǰ��Version���ô˺���
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	// ���뷽��
	public void insert(ContentValues values) {
		// ��ȡSQLiteDatabaseʵ��
		SQLiteDatabase db = getWritableDatabase();
		// �������ݿ���
		db.insert(TABLE_NAME, null, values);
		db.close();
	}

	// ��ѯ����
	public Cursor queryALL() {
		SQLiteDatabase db = getReadableDatabase();
		// ��ȡCursor
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null, null);
		return c;

	}

	public Cursor query(String musicTITLE) {

		SQLiteDatabase db = getReadableDatabase();
		// ��ȡCursor
		Cursor c = db.query(TABLE_NAME, null, "TITLE=?", new String[] { musicTITLE }, null, null, null, null);
		return c;
	}

	// �����ж���������
	public int GetCount() {
		SQLiteDatabase db = getReadableDatabase();
		// ��ȡCursor
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null, null);
		return c.getCount();
	}

	// ����ID��������
	public Cursor querybyID(int _id) {

		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from music where _id=" + _id, null);
		// Cursor c = db.query(TABLE_NAME, null, "_id="+_id, null, null, null,
		// null, null);
		// return c;
	}

	// ����Ψһ��ʶ_id ��ɾ������
	public void delete(int id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, "_id=?", new String[] { String.valueOf(id) });

	}

	public void deleteAll() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
		db.execSQL("DROP TABLE "+TABLE_NAME);
		db.execSQL(CREATE_TBL);
	}

	// �������ݿ������
	public void update(ContentValues values, String whereClause, String[] whereArgs) {
		SQLiteDatabase db = getWritableDatabase();
		db.update(TABLE_NAME, values, whereClause, whereArgs);
	}

	// �ر����ݿ�
	public void close() {
		if (db != null) {
			db.close();
		}
	}

}