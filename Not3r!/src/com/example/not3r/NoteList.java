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
import android.widget.ListView;
import android.widget.SearchView;
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
	
	
	
	String columnsString = "SELECT id AS _id, color , content, lastmodifiedtime, important FROM note ";
	
	
	//==========
//	private EditText invisibleText;
	
	private SearchView sv ;
	private String searchString ="";
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
				R.layout.item_view, c, from, to, 0){
				@Override 
				public View getView(int position, View convertView, ViewGroup parent) {  
			 
				//get reference to the row
				View view = super.getView(position, convertView, parent); 
				//check for odd or even to set alternate colors to the row background
				long id = noteList.getItemIdAtPosition(position);
				Cursor cs = db.rawQuery("SELECT color FROM note WHERE id ="+id, null);
				cs.moveToFirst();
				
				String color = cs.getString(0);
				// testToast(color);
				if(color.equals("red")|| color.equals("RED"))
				{
					view.setBackgroundColor(Color.rgb(255, 0, 0));
				}
				else if(color.equals("green"))
				{
					view.setBackgroundColor(Color.rgb(0, 255, 0));
				}
				else if(color.equals("blue"))
				{
					view.setBackgroundColor(Color.rgb(0, 0, 255));
				}
				else
					view.setBackgroundColor(Color.rgb(255, 255, 255));

			   return view;  
			  }  
		};
		
		noteList = (ListView) findViewById(R.id.note_list);
		noteList.setAdapter(adapter);
		noteList.setOnCreateContextMenuListener(this);
		
		noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        public void onItemClick(AdapterView<?> av, View view, int position, long id) {

	    		Intent intent = new Intent(NoteList.this, Editor.class);

	    		Bundle bundle = new Bundle();
	    		bundle.putLong("rowId", id);
	    		intent.putExtras(bundle);
	    		
	    		startActivity(intent);
	    		
	        }
	    });
		
		
		//================
	//	invisibleText = (EditText)findViewById(R.id.myFilter);
	//	invisibleText.setVisibility(View.GONE);
			
		int red = 50;
		int green = 255;
		int blue = 40;
	//	testToast(Color.rgb(red, green, blue));
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
			searchList(searchString);
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
		case R.id.search:
			// search 
			
			sv.setOnQueryTextListener(new SearchView.OnQueryTextListener( ) {
			    @Override
			    public boolean   onQueryTextChange( String newText ) {
			    	searchString = newText;
	//		    	testToast(newText);
					searchList(newText);
			    	return false;
			    }

			    @Override
			    public boolean   onQueryTextSubmit(String query) {
					return false;
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

		//=============================
	    // Get the SearchView and set the searchable configuration
	//    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    sv = (SearchView) menu.findItem(R.id.search).getActionView();
	    
		return true;
	}

	public void newNote() {
		
		mDBHelper.insert("green", "");
		Cursor cs = db.rawQuery("SELECT MAX(id) FROM note", null);
		cs.moveToFirst();
		int intId = Integer.parseInt(cs.getString(0));
		
		Intent intent = new Intent(this, Editor.class);
		Bundle bundle = new Bundle();
		bundle.putLong("rowId", intId);
		intent.putExtras(bundle);
	//	Toast.makeText(this, "fsad" +cs.getString(0) , Toast.LENGTH_SHORT).show();
		
		startActivity(intent);
	}

	//======
/*	public void refreshList()
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
		searchList(searchString);
	}
	*/
	
	//============
	public void searchList(String s)
	{
		c = db.rawQuery(columnsString +" WHERE content LIKE '%" +s+"%' ORDER BY _id DESC",null);
		c.moveToFirst();
		String[] from = { NoteDatabase.COLUMN_CONTENT };
		int[] to = { R.id.item };
		adapter = new SimpleCursorAdapter(this,
				R.layout.item_view, c, from, to, 0){
			@Override 
			public View getView(int position, View convertView, ViewGroup parent) {  
		 
			//get reference to the row
			View view = super.getView(position, convertView, parent); 
			//check for odd or even to set alternate colors to the row background
			long id = noteList.getItemIdAtPosition(position);
			Cursor cs = db.rawQuery("SELECT color FROM note WHERE id ="+id, null);
			cs.moveToFirst();
			
			String color = cs.getString(0);
			// testToast(color);
			if(color.equals("red") || color.equals("RED") )
			{
				view.setBackgroundColor(Color.rgb(255, 0, 0));
			}
			else if(color.equals("green"))
			{
				view.setBackgroundColor(Color.rgb(0, 255, 0));
			}
			else if(color.equals("blue"))
			{
				view.setBackgroundColor(Color.rgb(0, 0, 255));
			}
			else
				view.setBackgroundColor(Color.rgb(255, 255, 255));

		   return view;  
		  }  
	};
		
		noteList = (ListView) findViewById(R.id.note_list);
		noteList.setAdapter(adapter);
		noteList.setOnCreateContextMenuListener(this);
		
		
	}
	
	public void testToast()
	{
		Toast.makeText(this, "fsad" , Toast.LENGTH_SHORT).show();
		
	}
	public void testToast(String s)
	{
		Toast.makeText(this, s , Toast.LENGTH_SHORT).show();
		
	}
	public void testToast(int s)
	{
		Toast.makeText(this, ""+s , Toast.LENGTH_SHORT).show();
		
	}
}
