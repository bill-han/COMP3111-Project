package com.example.not3r;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Not3rList extends Activity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private TextView tagSetting;

	private Not3rDB mDBHelper;
	private Cursor c;

	private ListView noteList;
	private SearchView sv;

	private static int currentTab = 0;

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

		loadDrawerList();
		mDrawerList.setItemChecked(currentTab, true);

		mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				currentTab = position;
				mDrawerList.setItemChecked(currentTab, true);
				filter(currentTab, "");
				mDrawerLayout.closeDrawers();
			}
		});

		tagSetting = (TextView) findViewById(R.id.tag_setting);
		tagSetting.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				R.drawable.ic_action_settings, 0);
		tagSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tagSetting.setSelected(true);
				mDrawerLayout.closeDrawers();
				Intent intent = new Intent(Not3rList.this, TagSetting.class);
				startActivity(intent);
			}
		});

		getActionBar().setDisplayHomeAsUpEnabled(true);

		mDBHelper = new Not3rDB(this);
		filter(currentTab, "");
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
			filter(currentTab, "");
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
					filter(currentTab, newText);
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

	public void loadDrawerList() {
		SharedPreferences sharedPref = this.getSharedPreferences(
				"com.example.not3r.TAG", MODE_PRIVATE);
		String[] tab = new String[6];
		tab[0] = "All";
		tab[1] = "Important";
		tab[2] = sharedPref.getString("0", "Personal");
		tab[3] = sharedPref.getString("1", "Home");
		tab[4] = sharedPref.getString("2", "Work");
		tab[5] = sharedPref.getString("3", "Others");

		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.list_item, tab) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView view = (TextView) super.getView(position, convertView,
						parent);
				view.setTextColor(Color.parseColor("#EEEEEE"));
				GradientDrawable gdDrawable = (GradientDrawable) getResources()
						.getDrawable(R.drawable.dot);
				switch (position) {
				case 0:
					break;
				case 1:
					view.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.ic_action_important, 0);
					break;
				case 2:
					gdDrawable.setColor(Color.parseColor(Not3rDB.LIGHTBLUE));
					view.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.dot, 0);
					break;
				case 3:
					gdDrawable.setColor(Color.parseColor(Not3rDB.LIGHTGREEN));
					view.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.dot, 0);
					break;
				case 4:
					gdDrawable.setColor(Color.parseColor(Not3rDB.YELLOW));
					view.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.dot, 0);
					break;
				case 5:
					gdDrawable.setColor(Color.parseColor(Not3rDB.PINK));
					view.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.dot, 0);
					break;
				}
				return view;
			}
		});
	}

	public void filter(int tab, String query) {
		switch (tab) {
		case 0:
			loadNot3rList(Not3rDB.COLUMN_COLOR, "%", query);
			break;
		case 1:
			loadNot3rList(Not3rDB.COLUMN_IMPORTANCE, "1", query);
			break;
		case 2:
			loadNot3rList(Not3rDB.COLUMN_COLOR, Not3rDB.LIGHTBLUE, query);
			break;
		case 3:
			loadNot3rList(Not3rDB.COLUMN_COLOR, Not3rDB.LIGHTGREEN, query);
			break;
		case 4:
			loadNot3rList(Not3rDB.COLUMN_COLOR, Not3rDB.YELLOW, query);
			break;
		case 5:
			loadNot3rList(Not3rDB.COLUMN_COLOR, Not3rDB.PINK, query);
			break;
		}
	}

	public void loadNot3rList(String selection, String arg, String query) {
		selection += " LIKE ?";
		String[] keyword = query.split(" ");
		String[] selectionArgs = new String[keyword.length + 1];
		selectionArgs[0] = arg;
		for (int i = 0; i < keyword.length; i++) {
			selection += " AND " + Not3rDB.COLUMN_CONTENT + " LIKE ?";
			selectionArgs[i + 1] = "%" + keyword[i] + "%";
		}

		c = mDBHelper.selectFrom(selection, selectionArgs);
		String[] from = { Not3rDB.COLUMN_CONTENT, Not3rDB.COLUMN_TIME,
				Not3rDB.COLUMN_IMPORTANCE };
		int[] to = { R.id.note_content, R.id.note_time, R.id.note_importance };

		noteList = (ListView) findViewById(R.id.note_list);
		noteList.setAdapter(new SimpleCursorAdapter(this, R.layout.note_item,
				c, from, to, 0) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				c.moveToPosition(position);
				GradientDrawable gdDrawable = (GradientDrawable) getResources()
						.getDrawable(R.drawable.corners_bg);
				gdDrawable.setColor(Color.parseColor(c.getString(1)));
				TextView timeView = (TextView) view
						.findViewById(R.id.note_time);
				String time = timeView.getText().toString(), current = new SimpleDateFormat(
						"yyyy-MM-dd", Locale.getDefault()).format(new Date());
				if (time.substring(0, 10).compareTo(current) < 0)
					timeView.setText(time.subSequence(5, 10));
				else
					timeView.setText(time.subSequence(11, 16));
				TextView starView = (TextView) view
						.findViewById(R.id.note_importance);
				starView.setText("");
				if (c.getInt(4) == 1)
					starView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.ic_action_important, 0);
				else
					starView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				view.setBackgroundResource(R.drawable.corners_bg);
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

	public void newNote() {
		Intent intent = new Intent(this, Editor.class);
		startActivity(intent);
	}
}
