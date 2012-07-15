package com.android.example.spinner.test;

import java.util.List;

import com.android.example.spinner.NewActivity;
import com.android.example.spinner.SpinnerActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.app.Service;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class SpinnerActivityTest extends
		ActivityInstrumentationTestCase2<SpinnerActivity> {
	
	private SpinnerActivity mActivity;
	private Spinner mSpinner;
	private SpinnerAdapter mPlanetData;
	private int mPos;
	private String mSelection;
	
	private Button goTobutton;
	
	private ActivityMonitor activityMonitor;
	
	public static final int ADAPTER_COUNT = 9;
	public static final int INITIAL_POSITION = 0;
	public static final int TEST_POSITION = 5;
	public static final int TEST_STATE_DESTROY_POSITION = 2;
	public static final String TEST_STATE_DESTROY_SELECTION = "Earth";
	public static final int TEST_STATE_PAUSE_POSITION = 4;
	public static final String TEST_STATE_PAUSE_SELECTION = "Jupiter";
	
	 public SpinnerActivityTest() {
		    super(SpinnerActivity.class);
	 } // end of SpinnerActivityTest constructor definition

	 
	 
	 public void setUp() throws Exception{
		 super.setUp();
		// Add a monitor before we start the activity
			activityMonitor = new ActivityMonitor(NewActivity.class.getName(), null, false);
			getInstrumentation().addMonitor(activityMonitor);
			
		 this.setActivityInitialTouchMode(false); //disable finger touches on the UI
		 
		 mActivity = getActivity();
		 mSpinner = (Spinner)mActivity.findViewById(com.android.example.spinner.R.id.Spinner01);
		 mPlanetData = mSpinner.getAdapter();
		 
		 goTobutton = (Button)mActivity.findViewById(com.android.example.spinner.R.id.button1);
	 
	 }
	 
	 public void testPreconditions(){
		 assertTrue(mSpinner.getOnItemSelectedListener() != null);
		 assertTrue(mPlanetData != null);
		 assertEquals(mPlanetData.getCount(), ADAPTER_COUNT);
	 }
	 
	 public void testClickButton(){
		
		
		  mActivity.runOnUiThread(new Runnable() {
		    public void run() {
		      // click button and open next activity.
		      goTobutton.performClick();
		    }
		  });

		    
		 Activity activity = activityMonitor.waitForActivityWithTimeout(1 * 1000);
	     assertNotNull("Activity was not started", activity);
	     activity.finish();
	     //sendKeys(KeyEvent.KEYCODE_BACK);
	     
	 }
	 
	 public void testSpinnerUI(){
		 mActivity.runOnUiThread(
			      new Runnable() {
			        public void run() {
			          mSpinner.requestFocus();
			          mSpinner.setSelection(INITIAL_POSITION);
			        } // end of run() method definition
			      } // end of anonymous Runnable object instantiation
			    ); // end of invocation of runOnUiThread
		 
		 this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		    for (int i = 1; i <= TEST_POSITION; i++) {
		      this.sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
		    } // end of for loop

		 this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		 
		 mPos = mSpinner.getSelectedItemPosition();
		 mSelection = (String)mSpinner.getItemAtPosition(mPos);
		 
	 }
	 
	 public void testStateDestroy(){
		 mActivity.setSpinnerPosition(TEST_STATE_DESTROY_POSITION);
		 mActivity.setSpinnerSelection(TEST_STATE_DESTROY_SELECTION);
		 
		 //restart the activity
		 mActivity.finish();
		 mActivity = this.getActivity();
		 
		 int currentPosition = mActivity.getSpinnerPosition();
		 String currentSelection = mActivity.getSpinnerSelection();
		 
		 assertEquals(TEST_STATE_DESTROY_POSITION, currentPosition);
		 assertEquals(TEST_STATE_DESTROY_SELECTION, currentSelection);
	 }
	 
	 
	 @UiThreadTest
	 public void testStatePause(){
		 Instrumentation instrumentation = this.getInstrumentation();
		 
		 mActivity.setSpinnerPosition(TEST_STATE_PAUSE_POSITION);
		 mActivity.setSpinnerSelection(TEST_STATE_PAUSE_SELECTION);
		 
		 instrumentation.callActivityOnPause(mActivity);
		 
		 //force the spinner into a different state
		 mActivity.setSpinnerPosition(0);
		 mActivity.setSpinnerSelection("");
		 
		 instrumentation.callActivityOnResume(mActivity);
		 
		 int currentPosition = mActivity.getSpinnerPosition();
		 String currentSelection = mActivity.getSpinnerSelection();
		 
		 assertEquals(TEST_STATE_PAUSE_POSITION,currentPosition);
		 assertEquals(TEST_STATE_PAUSE_SELECTION,currentSelection);
	 }
}
