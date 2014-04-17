package com.example.not3r.test;

import android.test.ActivityInstrumentationTestCase2;

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

}
