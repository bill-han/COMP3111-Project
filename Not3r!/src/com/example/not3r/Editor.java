package com.example.not3r;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class Editor extends Activity {

	private EditText editing;
	private NoteDatabase mDBHelper;

	//===========
	private boolean checkSave = false;
	
	//===========
	private long rowId;
	private int intId ;
	private SQLiteDatabase db;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		editing = (EditText) findViewById(R.id.editor);
		mDBHelper = new NoteDatabase(this);
		
		//===========
        Bundle bundle = this.getIntent().getExtras();
		rowId = bundle.getLong("rowId");
		intId = (int)rowId;
		
		db = mDBHelper.getReadableDatabase();
		Cursor cs = db.rawQuery("SELECT content FROM note WHERE id="+ intId, null);
		cs.moveToFirst();
		String contentStr =cs.getString(0);
		editing.setText(contentStr);
		editing.requestFocus();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.editor_actions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (editing.getText().toString().length() > 0) {
				mDBHelper.update(intId, "red", editing.getText().toString());
				checkSave = true;
				Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
			}
			else 
			{
				mDBHelper.delete(intId);
			}
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.share:
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent
					.putExtra(Intent.EXTRA_TEXT, editing.getText().toString());
			sendIntent.setType("text/plain");
			startActivity(Intent.createChooser(sendIntent, ""));
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onStop() {
		super.onStop();
		if(checkSave == false)
			if (editing.getText().toString().length() > 0) {
				mDBHelper.update(intId, "red", editing.getText().toString());
				checkSave = true;
				Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
			}
			else 
			{
				mDBHelper.delete(intId);
			}
	}

}
