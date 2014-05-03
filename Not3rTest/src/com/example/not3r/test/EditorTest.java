package com.example.not3r.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.example.not3r.Editor;
import com.robotium.solo.Solo;

public class EditorTest extends ActivityInstrumentationTestCase2<Editor> {

	private Solo solo;

	public EditorTest() {
		super(Editor.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	public void testaActivity() {
		solo.assertCurrentActivity("Testing Editor", Editor.class);
	}
	
	public void testbMenu() {
		solo.clickOnView(solo.getView(com.example.not3r.R.id.menu));
		solo.sleep(1000);
	}
	
	public void testcChangeColor() {
		EditText temp = (EditText)solo.getView(com.example.not3r.R.id.editor);
		solo.typeText(temp, "test");
		solo.clickOnView(solo.getView(com.example.not3r.R.id.menu));
		solo.clickInList(1);
		solo.clickInList(2);
	}
	
	public void testdSetReminder() {
		EditText temp = (EditText)solo.getView(com.example.not3r.R.id.editor);
		solo.typeText(temp, "test");
		solo.clickOnView(solo.getView(com.example.not3r.R.id.menu));
		solo.clickInList(3);
		solo.clickOnView(solo.getView(com.example.not3r.R.id.confirm));
	}
	
	public void testeInputWithStar() {
		EditText temp = (EditText)solo.getView(com.example.not3r.R.id.editor);
		solo.typeText(temp, "test");
		solo.clickOnView(solo.getView(com.example.not3r.R.id.menu));	//star
		solo.clickInList(2);
		solo.clickOnView(solo.getView(com.example.not3r.R.id.menu));	//unstar
		solo.clickInList(2);
		solo.clickOnView(solo.getView(com.example.not3r.R.id.menu));	//star
		solo.clickInList(2);
		solo.goBack();
		solo.sleep(1000);
	}

}
