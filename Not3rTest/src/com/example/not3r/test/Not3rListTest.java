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

	public void test00_Activity() {
		solo.assertCurrentActivity("Testing Not3rList", Not3rList.class);
	}

	public void test01_Search() {
		solo.clickOnView(solo.getView(com.example.not3r.R.id.search));
		solo.typeText(0, "t");
	}

	public void test02_Delete() {
		solo.clickLongOnText("test");
		solo.clickInList(1);
	}

	public void test03_NavigationDrawer01() {
		solo.setNavigationDrawer(Solo.OPENED);
		solo.clickOnText("All");
	}

	public void test04_NavigationDrawer02() {
		solo.setNavigationDrawer(Solo.OPENED);
		solo.clickOnText("Important");
	}

	public void test05_NavigationDrawer03() {
		solo.setNavigationDrawer(Solo.OPENED);
		solo.clickOnText("Personal");
	}

	public void test06_NavigationDrawer04() {
		solo.setNavigationDrawer(Solo.OPENED);
		solo.clickOnText("Home");
	}

	public void test07_NavigationDrawer05() {
		solo.setNavigationDrawer(Solo.OPENED);
		solo.clickOnText("Work");
	}

	public void test08_NavigationDrawer06() {
		solo.setNavigationDrawer(Solo.OPENED);
		solo.clickOnText("Others");
	}

	public void test09_AddNote() {
		solo.clickOnActionBarItem(com.example.not3r.R.id.add_note);
		solo.clickOnActionBarHomeButton();
	}

}
