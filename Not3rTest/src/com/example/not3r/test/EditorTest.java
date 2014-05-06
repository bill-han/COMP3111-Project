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

	public void testActivity() {
		solo.assertCurrentActivity("Testing Editor", Editor.class);
	}

	public void testChangeColor01() {
		EditText editor = (EditText) solo
				.getView(com.example.not3r.R.id.editor);
		solo.typeText(editor, "test");
		solo.clickOnActionBarItem(com.example.not3r.R.id.menu);
		solo.clickOnActionBarItem(com.example.not3r.R.id.change_color);
		solo.clickInList(1);
	}

	public void testChangeColor02() {
		EditText editor = (EditText) solo
				.getView(com.example.not3r.R.id.editor);
		solo.typeText(editor, "test");
		solo.clickOnActionBarItem(com.example.not3r.R.id.menu);
		solo.clickOnActionBarItem(com.example.not3r.R.id.change_color);
		solo.clickInList(2);
	}

	public void testChangeColor03() {
		EditText editor = (EditText) solo
				.getView(com.example.not3r.R.id.editor);
		solo.typeText(editor, "test");
		solo.clickOnActionBarItem(com.example.not3r.R.id.menu);
		solo.clickOnActionBarItem(com.example.not3r.R.id.change_color);
		solo.clickInList(3);
	}

	public void testChangeColor04() {
		EditText editor = (EditText) solo
				.getView(com.example.not3r.R.id.editor);
		solo.typeText(editor, "test");
		solo.clickOnActionBarItem(com.example.not3r.R.id.menu);
		solo.clickOnActionBarItem(com.example.not3r.R.id.change_color);
		solo.clickInList(4);
	}

	public void testDelete() {
		EditText editor = (EditText) solo
				.getView(com.example.not3r.R.id.editor);
		solo.typeText(editor, "test");
		solo.clickOnActionBarItem(com.example.not3r.R.id.menu);
		solo.clickOnActionBarItem(com.example.not3r.R.id.delete);
	}

	public void testSetReminder() {
		EditText editor = (EditText) solo
				.getView(com.example.not3r.R.id.editor);
		solo.typeText(editor, "test");
		solo.clickOnActionBarItem(com.example.not3r.R.id.menu);
		solo.clickOnActionBarItem(com.example.not3r.R.id.set_reminder);
		solo.clickOnView(solo.getView(com.example.not3r.R.id.confirm));
	}

	public void testStar() {
		EditText editor = (EditText) solo
				.getView(com.example.not3r.R.id.editor);
		solo.typeText(editor, "test");
		solo.clickOnActionBarItem(com.example.not3r.R.id.menu);
		solo.clickOnActionBarItem(com.example.not3r.R.id.set_importance);
		solo.clickOnActionBarItem(com.example.not3r.R.id.menu);
		solo.clickOnActionBarItem(com.example.not3r.R.id.set_importance);
		solo.clickOnActionBarItem(com.example.not3r.R.id.menu);
		solo.clickOnActionBarItem(com.example.not3r.R.id.set_importance);
		solo.goBack();
	}

}
