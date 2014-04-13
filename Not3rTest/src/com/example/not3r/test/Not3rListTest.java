package com.example.not3r.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.not3r.Editor;
import com.example.not3r.Not3rDB;
import com.example.not3r.Not3rList;

public class Not3rListTest extends ActivityInstrumentationTestCase2<Not3rList> {
	private Not3rList mActivity;
	private Editor eActivity;
	private Not3rDB dActivity;

	ListView noteList;
	SearchView sv;

	public Not3rListTest() {
		super(Not3rList.class);
	}

	@Override
	protected void setUp() throws Exception {
		// this method is called every time before any test execution
		// super.setUp();
		mActivity = (Not3rList) getActivity(); // get current activity
		// link the objects with the activity objects
		noteList = (ListView) mActivity
				.findViewById(com.example.not3r.R.id.note_list);

	}

	@Override
	protected void tearDown() throws Exception {
		// this method is called every time after any test execution
		// we want to clean the texts

		super.tearDown();
	}

	@SmallTest
	public void testAddNote() {
		getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
		getInstrumentation().invokeMenuActionSync(mActivity,
				com.example.not3r.R.id.add_note, 0);
		sendKeys("T E S T ");
		String str1 = "INPUT ERROR";
		mActivity = (Not3rList) getActivity();
		View view = mActivity.findViewById(android.R.id.home);
		TouchUtils.tapView(this, view);
		mActivity = (Not3rList) getActivity();
		int numChild = 0;

		try {
			numChild = noteList.getCount();
		} catch (NumberFormatException e) {
			str1 = "";
		}
		assertEquals("checkAdd", 0, numChild);
	}

}