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
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID
				+ " INTEGER PRIMARY KEY, " + COLUMN_COLOR + " TEXT, "
				+ COLUMN_CONTENT + " TEXT, " + COLUMN_TIME + " TEXT, "
				+ COLUMN_IMPORTANCE + " INTEGER)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public void insert(String color, String content, int importance) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_COLOR, color);
		values.put(COLUMN_CONTENT, content);
		values.put(COLUMN_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault()).format(new Date()));
		values.put(COLUMN_IMPORTANCE, importance);
		db.insert(TABLE_NAME, null, values);
	}

	public void delete(long id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, COLUMN_ID + " = ?",
				new String[] { Long.toString(id) });
	}

	public void update(long id, String color, String content, int importance) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_COLOR, color);
		values.put(COLUMN_CONTENT, content);
		values.put(COLUMN_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault()).format(new Date()));
		values.put(COLUMN_IMPORTANCE, importance);
		db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
				new String[] { Long.toString(id) });
	}

	public Cursor selectFrom(String selection, String[] selectionArgs) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, null, selection, selectionArgs, null,
				null, COLUMN_TIME + " DESC");
		c.moveToFirst();
		return c;
	}

	public Cursor selectFrom(long id) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, null, COLUMN_ID + " = ?",
				new String[] { Long.toString(id) }, null, null, null);
		c.moveToFirst();
		return c;
	}

}
