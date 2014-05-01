package com.example.not3r;

import java.util.Calendar;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Editor extends Activity {

	private LinearLayout layout;
	private EditText editing;
	private Not3rDB mDBHelper;
	private Cursor c;
	private long id;
	private String content = "", color = Not3rDB.LIGHTBLUE;
	private int importance = 0;

	public static String[] tags = { "Personal", "Home", "Work", "Others" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		layout = (LinearLayout) findViewById(R.id.editor_layout);
		editing = (EditText) findViewById(R.id.editor);
		mDBHelper = new Not3rDB(this);

		id = this.getIntent().getLongExtra("com.example.not3r.Note", -1);
		if (id >= 0) {
			c = mDBHelper.selectFromId(id);
			color = c.getString(1);
			content = c.getString(2);
			importance = c.getInt(4);
			editing.setText(content);
		}
		editing.requestFocus();
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
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editing.getWindowToken(), 0);
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.change_color:
			changeColor();
			return true;
		case R.id.set_importance:
			setImportance();
			return true;
		case R.id.set_reminder:
			setReminder();
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
				mDBHelper.insert(color, currentContent, importance);
			else
				mDBHelper.update(id, color, currentContent, importance);
			Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
		} else if (id >= 0) {
			mDBHelper.delete(id);
			Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
		}
		importance = 0;
		NavUtils.navigateUpFromSameTask(this);
	}

	public void changeColor() {
		View popupView = getLayoutInflater().inflate(R.layout.color_setting,
				null);
		final PopupWindow colorSetting = new PopupWindow(popupView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

		GradientDrawable gdDrawable = (GradientDrawable) getResources()
				.getDrawable(R.drawable.corners_bg);
		gdDrawable.setColor(Color.parseColor("#EEEEEE"));
		popupView.setBackgroundResource(R.drawable.corners_bg);
		colorSetting.setTouchable(true);
		colorSetting.setOutsideTouchable(true);
		colorSetting.setBackgroundDrawable(new BitmapDrawable(getResources(),
				(Bitmap) null));

		ListView colorList = (ListView) popupView.findViewById(R.id.color_list);
		colorList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				tags) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView view = (TextView) super.getView(position, convertView,
						parent);
				GradientDrawable gdDrawable = (GradientDrawable) getResources()
						.getDrawable(R.drawable.corners_bg);
				switch (position) {
				case 0:
					gdDrawable.setColor(Color.parseColor(Not3rDB.LIGHTBLUE));
					break;
				case 1:
					gdDrawable.setColor(Color.parseColor(Not3rDB.LIGHTGREEN));
					break;
				case 2:
					gdDrawable.setColor(Color.parseColor(Not3rDB.YELLOW));
					break;
				case 3:
					gdDrawable.setColor(Color.parseColor(Not3rDB.PINK));
					break;
				}
				view.setBackgroundResource(R.drawable.corners_bg);
				return view;
			}
		});
		colorList.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					color = Not3rDB.LIGHTBLUE;
					break;
				case 1:
					color = Not3rDB.LIGHTGREEN;
					break;
				case 2:
					color = Not3rDB.YELLOW;
					break;
				case 3:
					color = Not3rDB.PINK;
					break;
				}
				layout.setBackgroundColor(Color.parseColor(color));
				colorSetting.dismiss();
			}
		});

		colorSetting.showAtLocation(layout, Gravity.CENTER, 0, 0);
	}

	public void setReminder() {
		View popupView = getLayoutInflater().inflate(R.layout.reminder_setting,
				null);
		final PopupWindow reminderSetting = new PopupWindow(popupView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

		GradientDrawable gdDrawable = (GradientDrawable) getResources()
				.getDrawable(R.drawable.corners_bg);
		gdDrawable.setColor(Color.parseColor("#EEEEEE"));
		popupView.setBackgroundResource(R.drawable.corners_bg);
		reminderSetting.setTouchable(true);
		reminderSetting.setOutsideTouchable(true);
		reminderSetting.setBackgroundDrawable(new BitmapDrawable(
				getResources(), (Bitmap) null));

		DatePicker datePicker;
		TimePicker timePicker;
		Button confirm;
		final Calendar c = Calendar.getInstance();

		final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		datePicker = (DatePicker) popupView.findViewById(R.id.date_picker);
		datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH),
				new DatePicker.OnDateChangedListener() {
					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						c.set(year, monthOfYear, dayOfMonth);
					}
				});

		timePicker = (TimePicker) popupView.findViewById(R.id.time_picker);
		timePicker.setIs24HourView(true);
		timePicker
				.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
					@Override
					public void onTimeChanged(TimePicker view, int hourOfDay,
							int minute) {
						c.set(Calendar.MINUTE, minute);
						c.set(Calendar.HOUR_OF_DAY, hourOfDay);
					}
				});

		confirm = (Button) popupView.findViewById(R.id.confirm);
		gdDrawable.setColor(Color.parseColor("#B3D465"));
		confirm.setBackgroundResource(R.drawable.corners_bg);
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Editor.this,
						NotificationReceiver.class);
				intent.putExtra("com.example.not3r.Reminder", editing.getText()
						.toString());
				intent.putExtra("com.example.not3r.Note", id);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						Editor.this, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
						pendingIntent);
				reminderSetting.dismiss();
				Toast.makeText(Editor.this, "Reminder added",
						Toast.LENGTH_SHORT).show();
			}
		});

		reminderSetting.showAtLocation(layout, Gravity.CENTER, 0, 0);
	}

	public void setImportance() {
		importance ^= 1;
		if (importance == 1) {
			Toast.makeText(this, "This note is marked as important",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "This note is marked as unimportant",
					Toast.LENGTH_SHORT).show();
		}
	}
}
