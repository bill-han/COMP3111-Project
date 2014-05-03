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

	public void testaActivity() {
		solo.assertCurrentActivity("Testing Not3rList", Not3rList.class);
	}
	
	public void testbSearch() {
		solo.clickOnView(solo.getView(com.example.not3r.R.id.search));
		solo.typeText(0, "t");
	}
	
	public void testcNavigationDrawer_star() {
		solo.setNavigationDrawer(Solo.OPENED);
		solo.clickOnText("Important");
		solo.setNavigationDrawer(Solo.CLOSED);
	}
	
	public void testdDelete() {
		solo.clickLongInList(1);
		solo.clickInList(1);
	}
	
	public void testeTagSetting() {
		solo.setNavigationDrawer(Solo.OPENED);
		solo.clickOnText("Tag setting");
		solo.clearEditText(2);
		solo.enterText(2, "test");
		solo.clearEditText(2);
		solo.enterText(2, "Work");
		solo.clickOnText("Save");
		//solo.setNavigationDrawer(Solo.CLOSED);
	}
	
}
