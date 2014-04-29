package com.example.not3r;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Not3rDB extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "not3r.db";
	private static int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "note";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_COLOR = "color";
	public static final String COLUMN_CONTENT = "content";
	public static final String COLUMN_TIME = "lastmodifiedtime";
	public static final String COLUMN_IMPORTANCE = "importance";

	public static final String BLUE = "#0299CC";
	public static final String GREEN = "#669902";
	public static final String ORANGE = "#FF8A04";
	public static final String RED = "#CC0302";
	public static final String LIGHTBLUE = "#A8DFF4";
	public static final String LIGHTGREEN = "#D3E992";
	public static final String YELLOW = "#FFECC0";
	public static final String PINK = "#FFAFAF";

	public Not3rDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + TABLE_NAME + " (" + COLUMN_ID
				+ " integer primary key, " + COLUMN_COLOR + " text, "
				+ COLUMN_CONTENT + " text, " + COLUMN_TIME + " text, "
				+ COLUMN_IMPORTANCE + " integer)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public void insert(String color, String content, int importance) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_COLOR, color);
		cv.put(COLUMN_CONTENT, content);
		cv.put(COLUMN_TIME,
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
						.format(new Date()));
		cv.put(COLUMN_IMPORTANCE, importance);
		db.insert(TABLE_NAME, null, cv);
	}

	public void delete(long id) {
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = COLUMN_ID + "=?";
		String[] whereArgs = { Long.toString(id) };
		db.delete(TABLE_NAME, whereClause, whereArgs);
	}

	public void update(long id, String color, String content, int importance) {
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = COLUMN_ID + "=?";
		String[] whereArgs = { Long.toString(id) };
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_COLOR, color);
		cv.put(COLUMN_CONTENT, content);
		cv.put(COLUMN_TIME,
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
						.format(new Date()));
		cv.put(COLUMN_IMPORTANCE, importance);
		db.update(TABLE_NAME, cv, whereClause, whereArgs);
	}

	public Cursor selectFromId(long id) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "select * from " + Not3rDB.TABLE_NAME + " where _id=" + id;
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return c;
	}

}
