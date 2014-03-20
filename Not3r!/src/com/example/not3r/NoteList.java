package com.example.not3r;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class NoteList extends Activity {

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	private NoteDatabase mDBHelper;
	private SQLiteDatabase db;
	private Cursor c;

	//=========
	private SimpleCursorAdapter adapter;
	private ListView noteList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close);
		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		mDBHelper = new NoteDatabase(this);
		db = mDBHelper.getReadableDatabase();

		String[] columns = { NoteDatabase.COLUMN_ID + " AS _id",
				NoteDatabase.COLUMN_COLOR, NoteDatabase.COLUMN_CONTENT,
				NoteDatabase.COLUMN_TIME, NoteDatabase.COLUMN_IMPORTANT };
		String sortOrder = NoteDatabase.COLUMN_ID + " DESC";
		c = db.query(NoteDatabase.TABLE_NAME, columns, null, null, null,
				null, sortOrder);
		c.moveToFirst();
		String[] from = { NoteDatabase.COLUMN_CONTENT };
		int[] to = { R.id.item };
		adapter = new SimpleCursorAdapter(this,
				R.layout.item_view, c, from, to, 0);
		
		noteList = (ListView) findViewById(R.id.note_list);
		noteList.setAdapter(adapter);
		noteList.setOnCreateContextMenuListener(this);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		menu.add(0, 0, 0, "Delete");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		c.moveToPosition(info.position);
		switch (item.getItemId()) {
		case 0:
			mDBHelper.delete(c.getInt(0));
			Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
			//=============
			refreshList();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...
		switch (item.getItemId()) {
		case R.id.add_note:
			newNote();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_actions, menu);
		return true;
	}

	public void newNote() {
		Intent intent = new Intent(this, Editor.class);
		startActivity(intent);
	}

	//======
	public void refreshList()
	{
		String[] columns = { NoteDatabase.COLUMN_ID + " AS _id",
				NoteDatabase.COLUMN_COLOR, NoteDatabase.COLUMN_CONTENT,
				NoteDatabase.COLUMN_TIME, NoteDatabase.COLUMN_IMPORTANT };
		String sortOrder = NoteDatabase.COLUMN_ID + " DESC";
		c = db.query(NoteDatabase.TABLE_NAME, columns, null, null, null,
				null, sortOrder);
		c.moveToFirst();
		String[] from = { NoteDatabase.COLUMN_CONTENT };
		int[] to = { R.id.item };
		adapter = new SimpleCursorAdapter(this,
				R.layout.item_view, c, from, to, 0);
		
		noteList = (ListView) findViewById(R.id.note_list);
		noteList.setAdapter(adapter);
		noteList.setOnCreateContextMenuListener(this);
	}
}
