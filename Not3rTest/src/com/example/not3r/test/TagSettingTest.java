package com.example.not3r.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.example.not3r.TagSetting;
import com.robotium.solo.Solo;

public class TagSettingTest extends
		ActivityInstrumentationTestCase2<TagSetting> {

	private Solo solo;

	public TagSettingTest() {
		super(TagSetting.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	public void testActivity() {
		solo.assertCurrentActivity("Testing TagSetting", TagSetting.class);
	}

	public void testChangeTag01() {
		EditText tag0 = (EditText) solo.getView(com.example.not3r.R.id.tag0);
		solo.clearEditText(tag0);
		solo.enterText(tag0, "Test");
		solo.clickOnView(solo.getView(com.example.not3r.R.id.save));
		solo.clearEditText(tag0);
		solo.enterText(tag0, "Personal");
		solo.clickOnView(solo.getView(com.example.not3r.R.id.save));
	}

	public void testChangeTag02() {
		EditText tag1 = (EditText) solo.getView(com.example.not3r.R.id.tag1);
		solo.clearEditText(tag1);
		solo.enterText(tag1, "Test");
		solo.clickOnView(solo.getView(com.example.not3r.R.id.save));
		solo.clearEditText(tag1);
		solo.enterText(tag1, "Home");
		solo.clickOnView(solo.getView(com.example.not3r.R.id.save));
	}

	public void testChangeTag03() {
		EditText tag2 = (EditText) solo.getView(com.example.not3r.R.id.tag2);
		solo.clearEditText(tag2);
		solo.enterText(tag2, "Test");
		solo.clickOnView(solo.getView(com.example.not3r.R.id.save));
		solo.clearEditText(tag2);
		solo.enterText(tag2, "Work");
		solo.clickOnView(solo.getView(com.example.not3r.R.id.save));
	}

	public void testChangeTag04() {
		EditText tag3 = (EditText) solo.getView(com.example.not3r.R.id.tag3);
		solo.clearEditText(tag3);
		solo.enterText(tag3, "Test");
		solo.clickOnView(solo.getView(com.example.not3r.R.id.save));
		solo.clearEditText(tag3);
		solo.enterText(tag3, "Others");
		solo.clickOnView(solo.getView(com.example.not3r.R.id.save));
	}
}
