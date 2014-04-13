package com.example.not3r.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.content.Context;

import com.example.not3r.Editor;
import com.example.not3r.NoteDatabase;
import com.example.not3r.NoteList;


public class Not3rActivityTest extends ActivityInstrumentationTestCase2<NoteList> 
{ 
	private NoteList mActivity;
	private Editor eActivity;
	private NoteDatabase dActivity;
//	Button buttonKilos, buttonPounds; 
//	EditText textKilos, textPounds;
	ListView noteList;
	SearchView sv ;
	//delta - the maximum delta between expected and actual for which both numbers are still considered equal. 
	private static final double DELTA = 1e-15; // constant for comparing doubles values
	
	public Not3rActivityTest() { 
		super(NoteList.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		//this method is called every time before any test execution super.setUp();
		mActivity = (NoteList) getActivity(); // get current activity
		//link the objects with the activity objects
		noteList = (ListView) mActivity 
				.findViewById(com.example.not3r.R.id.note_list);

	}
	
	@Override
	protected void tearDown() throws Exception {
		//this method is called every time after any test execution 
		// we want to clean the texts 

		super.tearDown();
	}
	

	@SmallTest
	public void testAddNote()
	{
		getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
	    getInstrumentation().invokeMenuActionSync(mActivity,
	    		com.example.not3r.R.id.add_note, 0);
	    sendKeys("T E S T ");
		String str1= "INPUT ERROR";
		mActivity = (NoteList) getActivity();
		View view = mActivity.findViewById(android.R.id.home);
		TouchUtils.tapView(this, view);
		mActivity = (NoteList) getActivity();
		int numChild = 0;

	    try{
	    	numChild = noteList.getCount();
		}
		catch (NumberFormatException e)
		{
			str1 = "";
		}
		assertEquals("checkAdd", 0,numChild);
	}
	
	
}