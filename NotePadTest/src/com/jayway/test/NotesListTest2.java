package com.jayway.test;

import android.test.ActivityInstrumentationTestCase2;
import com.example.android.notepad.NotesList;
import com.jayway.android.robotium.solo.Solo;

import android.widget.AbsListView;
import junit.framework.AssertionFailedError;
import com.bitbar.recorder.extensions.ExtSolo;
import android.widget.EditText;

public class NotesListTest2 extends ActivityInstrumentationTestCase2<NotesList>
{

	private ExtSolo solo; // ExtSolo is an extension of Robotium Solo that helps collecting better test execution data during test runs

	public NotesListTest2()
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
			solo.sleep(2800);
			solo.clickOnMenuItem("Add note", true);
			solo.waitForActivity("NoteEditor");
			solo.sleep(2000);
			assertTrue(
					"Wait for edit text (id: com.example.android.notepad.R.id.note) failed.",
					solo.waitForView(
							solo.findViewById(com.example.android.notepad.R.id.note),
							20000, true));
			solo.clearEditText((EditText) solo
					.findViewById(com.example.android.notepad.R.id.note));
			solo.enterText((EditText) solo
					.findViewById(com.example.android.notepad.R.id.note),
					"\u65B9\u5F0F\u7684");
			solo.sleep(2300);
			//			assertTrue("Wait for menu item (text: Save) failed.",
			//					solo.waitForText("Save", 1, 20000));
			solo.clickOnMenuItem("Save", true);
			solo.waitForActivity("NotesList");
			solo.sleep(2000);
			assertTrue("Wait for list failed.",
					solo.waitForView(AbsListView.class, 1, 20000, true));
			solo.clickLongInList(1);
			boolean flag = solo.waitForLogMessage("@@@@", 10000);
			assertEquals("waitForLogMessage (text: delete) failed.", true, flag);
			solo.clickInList(2);
			solo.sleep(200);
			solo.goBack();
		}
		catch (AssertionFailedError e)
		{
			solo.fail("com.jayway.test.NotesListTest2.testRecorded_scr_fail", e);
			throw e;
		}
		catch (Exception e)
		{
			solo.fail("com.jayway.test.NotesListTest2.testRecorded_scr_fail", e);
			throw e;
		}
	}

	public void testAddNote() throws Exception
	{
		solo.clickOnMenuItem("Add note");
		//Assert that NoteEditor activity is opened
		solo.assertCurrentActivity("Expected NoteEditor activity", "NoteEditor");
		//In text field 0, add Note 1
		solo.enterText(0, "Note 1");
		solo.goBack();
		//Clicks on menu item
		solo.clickOnMenuItem("Add note");
		//In text field 0, add Note 2
		solo.enterText(0, "Note 2");
		//Go back to first activity named "NotesList"
		solo.goBackToActivity("NotesList");
		//Takes a screenshot and saves it in "/sdcard/Robotium-Screenshots/".
		solo.takeScreenshot();
		boolean expected = true;
		boolean actual = solo.searchText("Note 1") && solo.searchText("Note 2");
		//Assert that Note 1 & Note 2 are found
		assertEquals("Note 1 and/or Note 2 are not found", expected, actual);

	}

	public void testEditNote() throws Exception
	{
		// Click on the second list line
		solo.clickInList(2);
		// Change orientation of activity
		solo.setActivityOrientation(Solo.LANDSCAPE);
		// Change title
		solo.clickOnMenuItem("Edit title");
		//In first text field (0), add test
		solo.enterText(0, " test");
		solo.goBack();
		boolean expected = true;
		// (Regexp) case insensitive
		boolean actual = solo.waitForText("(?i).*?note 1 test");
		//Assert that Note 1 test is found
		assertEquals("Note 1 test is not found", expected, actual);

	}

	public void testRemoveNote() throws Exception
	{
		//(Regexp) case insensitive/text that contains "test"
		solo.clickOnText("(?i).*?test.*");
		//Delete Note 1 test
		solo.clickOnMenuItem("Delete");
		//Note 1 test & Note 2 should not be found
		boolean expected = false;
		boolean actual = solo.searchText("Note 1 test");
		//Assert that Note 1 test is not found
		assertEquals("Note 1 Test is found", expected, actual);
		solo.clickLongOnText("Note 2");
		//Clicks on Delete in the context menu
		solo.clickOnText("Delete");
		actual = solo.searchText("Note 2");
		//Assert that Note 2 is not found
		assertEquals("Note 2 is found", expected, actual);
	}

}
