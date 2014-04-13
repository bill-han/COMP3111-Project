package com.example.not3r.test;

import android.test.AndroidTestCase;

import com.example.not3r.Not3rDB;

public class Not3rDBTest extends AndroidTestCase {

	public Not3rDBTest() {
		// TODO Auto-generated constructor stub
	}

	public void createDB() {
		Not3rDB helper = new Not3rDB(getContext());
		helper.getWritableDatabase();
	}

}
