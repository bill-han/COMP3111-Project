package com.example.not3r.test;

import android.test.AndroidTestCase;
import com.example.not3r.NoteDatabase;
import android.util.Log;
import java.util.List;
import java.util.Map;

public class Not3rDBTest extends AndroidTestCase {

	public Not3rDBTest() {
		// TODO Auto-generated constructor stub
	}
	
	public void createDB() {
		NoteDatabase helper = new NoteDatabase(getContext());
		helper.getWritableDatabase();
	}
	


}
