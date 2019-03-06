package com.example.testtargetsample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "database.db";
	public static final int DATABASE_VERSION = 1;
	public static final String TABLE_USER = "user";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_PASSWORD = "password";
	
	private static DBHelper instance = null;
	
	public static DBHelper getInstance(Context context, String name, CursorFactory factory, int version) {
		if (instance == null) {
			instance = new DBHelper(context, name, factory, version);
		}
		return instance;
	}

	public DBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + TABLE_USER
				+ "(" + COLUMN_ID + " integer primary key autoincrement, "
				+ COLUMN_NAME + " varchar(50), "
				+ COLUMN_PASSWORD + " varchar(50));";
		db.execSQL(sql);
		
		sql = "insert into " + TABLE_USER + " (" + COLUMN_NAME + ", " + COLUMN_PASSWORD + ") values ('administrator', 'administrator')";
		db.execSQL(sql);
		sql = "insert into " + TABLE_USER + " (" + COLUMN_NAME + ", " + COLUMN_PASSWORD + ") values ('ronaldqinbiao', 'ronaldqinbiao')";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}






























