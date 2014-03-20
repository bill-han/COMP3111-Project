package com.example.not3r;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		editing = (EditText) findViewById(R.id.editor);
		mDBHelper = new NoteDatabase(this);
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
				mDBHelper.insert("red", editing.getText().toString());
				checkSave = true;
				Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
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
				mDBHelper.insert("red", editing.getText().toString());
				checkSave = true;
				Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
			}
	}

}
