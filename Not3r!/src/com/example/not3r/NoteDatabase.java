package com.example.not3r;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDatabase extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "not3r.db";
	private static int DATABASE_VERSION = 3;
	public static final String TABLE_NAME = "note";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_COLOR = "color";
	public static final String COLUMN_CONTENT = "content";
	public static final String COLUMN_TIME = "lastmodifiedtime";
	public static final String COLUMN_IMPORTANT = "important";

	public NoteDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + TABLE_NAME + "(" + COLUMN_ID
				+ " integer primary key, " + COLUMN_COLOR + " text, "
				+ COLUMN_CONTENT + " text, " + COLUMN_TIME + " datetime, "
				+ COLUMN_IMPORTANT + " integer)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public void insert(String color, String content) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "insert into " + TABLE_NAME + "(" + COLUMN_COLOR + ", "
				+ COLUMN_CONTENT + ", " + COLUMN_TIME + ", " + COLUMN_IMPORTANT
				+ ") values('" + color + "','" + content
				+ "',datetime('now'),0)";
		db.execSQL(sql);
	}

	public void delete(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String whereClause = COLUMN_ID + "=?";
		String[] whereArgs = { Integer.toString(id) };
		db.delete(TABLE_NAME, whereClause, whereArgs);
	}

	public void update(int id, String color, String content) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "update " + TABLE_NAME + " set " + COLUMN_COLOR + "='"
				+ color + "', " + COLUMN_CONTENT + "='" + content + "', "
				+ COLUMN_TIME + "=datetime('now') where " + COLUMN_ID + "="
				+ Integer.toString(id);
		db.execSQL(sql);
	}

}
