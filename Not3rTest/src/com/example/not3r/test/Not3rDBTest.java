package com.example.not3r.test;

import android.test.AndroidTestCase;
<<<<<<< HEAD

import com.example.not3r.Not3rDB;
=======
import com.example.not3r.NoteDatabase;
import android.util.Log;
import java.util.List;
import java.util.Map;
>>>>>>> FETCH_HEAD

public class Not3rDBTest extends AndroidTestCase {

	public Not3rDBTest() {
		// TODO Auto-generated constructor stub
	}
<<<<<<< HEAD

	public void createDB() {
		Not3rDB helper = new Not3rDB(getContext());
		helper.getWritableDatabase();
	}
=======
	
	public void createDB() {
		NoteDatabase helper = new NoteDatabase(getContext());
		helper.getWritableDatabase();
	}
	

>>>>>>> FETCH_HEAD

}
