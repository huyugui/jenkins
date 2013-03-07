package com.jayway.test;

import android.test.ActivityInstrumentationTestCase2;
import com.example.android.notepad.NotesList;
import android.widget.AbsListView;
import junit.framework.AssertionFailedError;
import com.bitbar.recorder.extensions.ExtSolo;
import android.widget.EditText;

public class NotesListTest1 extends ActivityInstrumentationTestCase2<NotesList>
{

	private ExtSolo solo; // ExtSolo is an extension of Robotium Solo that helps collecting better test execution data during test runs

	public NotesListTest1()
	{
		super(NotesList.class);
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		solo = new ExtSolo(getInstrumentation(), getActivity(), this.getClass()
				.getCanonicalName(), getName());
	}

	@Override
	public void tearDown() throws Exception
	{
		solo.finishOpenedActivities();
		solo.tearDown();
		super.tearDown();
	}

	public void testRecorded() throws Exception
	{
		try
		{
			solo.waitForActivity("NotesList");
			solo.sleep(4000);
			solo.clickOnMenuItem("Add note", true);
			solo.waitForActivity("NoteEditor");
			solo.sleep(5700);
			assertTrue(
					"Wait for edit text (id: com.example.android.notepad.R.id.note) failed.",
					solo.waitForView(
							solo.findViewById(com.example.android.notepad.R.id.note),
							20000, true));
			solo.clearEditText((EditText) solo
					.findViewById(com.example.android.notepad.R.id.note));
			solo.enterText((EditText) solo
					.findViewById(com.example.android.notepad.R.id.note),
					"\u73A9\u513F");
			solo.sleep(6300);
//			assertTrue("Wait for menu item (text: Save) failed.",
//					solo.waitForText("Save", 1, 50000));
			solo.clickOnMenuItem("Save", true);
			solo.waitForActivity("NotesList");
			solo.sleep(11000);
			assertTrue("Wait for list failed.",
					solo.waitForView(AbsListView.class, 1, 20000, true));
			solo.clickInList(2);
			solo.sleep(2200);
			solo.goBack();
		}
		catch (AssertionFailedError e)
		{
			solo.fail("com.jayway.test.NotesListTest1.testRecorded_scr_fail", e);
			throw e;
		}
		catch (Exception e)
		{
			solo.fail("com.jayway.test.NotesListTest1.testRecorded_scr_fail", e);
			throw e;
		}
	}

}
