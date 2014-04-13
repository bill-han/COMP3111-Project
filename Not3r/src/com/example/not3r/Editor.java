package com.example.not3r;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Editor extends Activity {

	private LinearLayout layout;
	private EditText editing;
	private Not3rDB mDBHelper;
	private SQLiteDatabase db;
	private Cursor c;
	private long id;
	private String content = "",
			color = Note.colorSet[new Random().nextInt(4)];

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		layout = (LinearLayout) findViewById(R.id.layout);
		editing = (EditText) findViewById(R.id.editor);
		mDBHelper = new Not3rDB(this);

		id = this.getIntent().getLongExtra("com.example.not3r.NOTE", -1);
		if (id >= 0) {
			db = mDBHelper.getWritableDatabase();
			String sql = "SELECT * from " + Not3rDB.TABLE_NAME
					+ " WHERE id=" + id;
			c = db.rawQuery(sql, null);
			c.moveToFirst();
			color = c.getString(1);
			content = c.getString(2);
			editing.setText(content);
			editing.requestFocus();
		}
		layout.setBackgroundColor(Color.parseColor(color));
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
			onBackPressed();
			return true;
		case R.id.share:
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent
					.putExtra(Intent.EXTRA_TEXT, editing.getText().toString());
			sendIntent.setType("text/plain");
			startActivity(Intent.createChooser(sendIntent, ""));
			return true;
		case R.id.delete:
			new AlertDialog.Builder(this)
					.setTitle("Warning")
					.setMessage("Do you really want to delete this note?")
					.setPositiveButton("YES",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									editing.setText("");
									onBackPressed();
								}
							}).setNegativeButton("NO", null).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		String currentContent = editing.getText().toString();
		if (currentContent.length() > 0) {
			if (id < 0)
				mDBHelper.insert(color, currentContent);
			else
				mDBHelper.update(id, color, currentContent);
			Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
		} else if (id >= 0) {
			mDBHelper.delete(id);
			Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
		}
		NavUtils.navigateUpFromSameTask(this);
	}
}
