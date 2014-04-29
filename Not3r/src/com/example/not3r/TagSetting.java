package com.example.not3r;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TagSetting extends Activity {

	private LinearLayout layout;
	private EditText[] tag = new EditText[4];
	private Button save;

	private SharedPreferences sharedPref;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tag_setting);
		layout = (LinearLayout) findViewById(R.id.tag_setting_layout);

		GradientDrawable gdDrawable = (GradientDrawable) getResources()
				.getDrawable(R.drawable.corners_bg);
		sharedPref = this.getPreferences(MODE_PRIVATE);
		editor = sharedPref.edit();

		tag[0] = (EditText) layout.findViewById(R.id.tag0);
		gdDrawable.setColor(Color.parseColor(Not3rDB.LIGHTBLUE));
		tag[0].setBackgroundResource(R.drawable.corners_bg);
		tag[0].setText(sharedPref.getString("0", "Personal"));

		tag[1] = (EditText) layout.findViewById(R.id.tag1);
		gdDrawable.setColor(Color.parseColor(Not3rDB.LIGHTGREEN));
		tag[1].setBackgroundResource(R.drawable.corners_bg);
		tag[1].setText(sharedPref.getString("1", "Home"));

		tag[2] = (EditText) layout.findViewById(R.id.tag2);
		gdDrawable.setColor(Color.parseColor(Not3rDB.YELLOW));
		tag[2].setBackgroundResource(R.drawable.corners_bg);
		tag[2].setText(sharedPref.getString("2", "Work"));

		tag[3] = (EditText) layout.findViewById(R.id.tag3);
		gdDrawable.setColor(Color.parseColor(Not3rDB.PINK));
		tag[3].setBackgroundResource(R.drawable.corners_bg);
		tag[3].setText(sharedPref.getString("3", "Others"));

		save = (Button) layout.findViewById(R.id.save);
		gdDrawable.setColor(Color.parseColor("#888888"));
		save.setBackgroundResource(R.drawable.corners_bg);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveTags();
			}
		});
	}

	@Override
	public void onBackPressed() {
		NavUtils.navigateUpFromSameTask(this);
	}

	public void saveTags() {
		Not3rList.tabs[2] = Editor.tags[0] = tag[0].getText().toString();
		Not3rList.tabs[3] = Editor.tags[1] = tag[1].getText().toString();
		Not3rList.tabs[4] = Editor.tags[2] = tag[2].getText().toString();
		Not3rList.tabs[5] = Editor.tags[3] = tag[3].getText().toString();
		editor.putString("0", tag[0].getText().toString());
		editor.putString("1", tag[1].getText().toString());
		editor.putString("2", tag[2].getText().toString());
		editor.putString("3", tag[3].getText().toString());
		editor.commit();
		Toast.makeText(this, "Tags saved", Toast.LENGTH_SHORT).show();
		onBackPressed();
	}
}
