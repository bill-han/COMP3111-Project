package com.example.not3r;

import java.util.Calendar;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TimePicker;
import android.widget.Toast;

public class Editor extends Activity {

	private LinearLayout layout;
	private EditText editing;
	private Not3rDB mDBHelper;
	private SQLiteDatabase db;
	private Cursor c;
	private long id;
	private String content = "",
			color = Not3rDB.colorSet[new Random().nextInt(4)];

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
			String sql = "select * from " + Not3rDB.TABLE_NAME + " where _id="
					+ id;
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
		case R.id.color_change:
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
		case R.id.set_alarm:
			setAlarm();
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
	
	public void setAlarm() {
		View popupView = getLayoutInflater().inflate(R.layout.alarm, null);
		System.out.println(popupView);
		final PopupWindow alarm_setting = new PopupWindow(popupView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		alarm_setting.setTouchable(true);
		alarm_setting.setOutsideTouchable(true);
		alarm_setting.setBackgroundDrawable(new BitmapDrawable(getResources(),
				(Bitmap) null));

		DatePicker datePicker;
		TimePicker timePicker;
		Button confirm;
		Button cancel;
		final Calendar c = Calendar.getInstance();
		
		final AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

		datePicker = (DatePicker) popupView.findViewById(R.id.datePicker);
		datePicker.init(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),  
                new DatePicker.OnDateChangedListener() {  
                      
                    @Override  
                    public void onDateChanged(DatePicker view, int year, int monthOfYear,  
                            int dayOfMonth) { 
                        c.set(year,monthOfYear,dayOfMonth);  
                    }  
                });
		
		timePicker = (TimePicker) popupView.findViewById(R.id.timePicker);
		timePicker.setIs24HourView(true);  
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {  
              
            @Override  
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {  
                c.set(Calendar.MINUTE,minute);
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            }  
        });
        
        confirm = (Button) popupView.findViewById(R.id.setAlarm);
        confirm.setOnClickListener(new OnClickListener(){
        	@Override  
			public void onClick(View v) {
        		Intent intent = new Intent(Editor.this,AlarmReceiver.class); 
        		PendingIntent pendingIntent = PendingIntent.getBroadcast(Editor.this, 0, intent, 0);
        		
        		alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
				
			}
        });
        
        cancel = (Button) popupView.findViewById(R.id.disableAlarm);
        cancel.setOnClickListener(new OnClickListener(){
        	@Override  
            public void onClick(View v) {
        		Intent intent = new Intent(Editor.this,AlarmReceiver.class); 
        		PendingIntent pendingIntent = PendingIntent.getBroadcast(Editor.this, 0, intent, 0);
        		alarmManager.cancel(pendingIntent);
        	}
        });

		alarm_setting.showAtLocation(this.findViewById(R.id.set_alarm),
				Gravity.CENTER, 0, 0);
	}
	
}
