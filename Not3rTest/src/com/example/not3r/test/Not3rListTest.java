package com.example.not3r.test;

import android.test.ActivityInstrumentationTestCase2;

import com.example.not3r.Not3rList;
import com.robotium.solo.Solo;

public class Not3rListTest extends ActivityInstrumentationTestCase2<Not3rList> {

	private Solo solo;

	public Not3rListTest() {
		super(Not3rList.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	public void testStartActivity() {
		solo.assertCurrentActivity("Not starting Not3rList now!",
				Not3rList.class);
	}

	public void testNavigationDrawer() {
		solo.setNavigationDrawer(Solo.OPENED);
		solo.setNavigationDrawer(Solo.CLOSED);
	}

}
