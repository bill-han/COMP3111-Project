package com.example.not3r;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Not3rList extends Activity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private Not3rDB mDBHelper;
	private SQLiteDatabase db;
	private Cursor c;

	private ListView noteList;
	private SearchView sv;

	private String[] menu = { "All", "Blue", "Green", "Yellow", "Red",
			"Important" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close);
		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, menu));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		getActionBar().setDisplayHomeAsUpEnabled(true);

		mDBHelper = new Not3rDB(this);
		db = mDBHelper.getWritableDatabase();
		filterList(Not3rDB.COLUMN_CONTENT, "");
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("Options");
		menu.add(0, 0, 0, "Delete");
		menu.add(0, 1, 0, "Share");
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
			filterList(Not3rDB.COLUMN_CONTENT, "");
			return true;
		case 1:
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, c.getString(2));
			sendIntent.setType("text/plain");
			startActivity(Intent.createChooser(sendIntent, ""));
			return true;
		}
		return super.onContextItemSelected(item);
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
		case R.id.search:
			sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

				@Override
				public boolean onQueryTextSubmit(String query) {
					return false;
				}

				@Override
				public boolean onQueryTextChange(String newText) {
					filterList(Not3rDB.COLUMN_CONTENT, newText);
					return true;
				}
			});
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_actions, menu);
		sv = (SearchView) menu.findItem(R.id.search).getActionView();
		return true;
	}

	public void newNote() {
		Intent intent = new Intent(this, Editor.class);
		startActivity(intent);
	}

	public void filterList(String selection, String keyword) {
		String[] columns = { Not3rDB.COLUMN_ID + " AS _id",
				Not3rDB.COLUMN_COLOR, Not3rDB.COLUMN_CONTENT,
				Not3rDB.COLUMN_TIME, Not3rDB.COLUMN_IMPORTANT };
		String orderBy = Not3rDB.COLUMN_TIME + " DESC";
		c = db.query(Not3rDB.TABLE_NAME, columns, selection + " LIKE ?",
				new String[] { "%" + keyword + "%" }, null, null, orderBy);
		c.moveToFirst();
		String[] from = { Not3rDB.COLUMN_CONTENT };
		int[] to = { R.id.main_list_item };

		noteList = (ListView) findViewById(R.id.note_list);
		noteList.setAdapter(new SimpleCursorAdapter(this,
				R.layout.main_list_item, c, from, to, 0) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				c.moveToPosition(position);
				view.setBackgroundColor(Color.parseColor(c.getString(1)));
				return view;
			}
		});
		noteList.setOnCreateContextMenuListener(this);
		noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(Not3rList.this, Editor.class);
				intent.putExtra("com.example.not3r.NOTE", id);
				startActivity(intent);
			}
		});
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		switch (position) {
		case 0:
			filterList(Not3rDB.COLUMN_COLOR, "");
			break;
		case 1:
			filterList(Not3rDB.COLUMN_COLOR, Note.LIGHTBLUE);
			break;
		case 2:
			filterList(Not3rDB.COLUMN_COLOR, Note.LIGHTGREEN);
			break;
		case 3:
			filterList(Not3rDB.COLUMN_COLOR, Note.YELLOW);
			break;
		case 4:
			filterList(Not3rDB.COLUMN_COLOR, Note.PINK);
			break;
		case 5:
			filterList(Not3rDB.COLUMN_IMPORTANT, "1");
			break;
		}
	}
}
