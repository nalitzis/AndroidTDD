package com.google.android.testing.mocktest.test;

import sample.ClassToMock;
import junit.framework.TestCase;

import com.google.android.testing.mocking.AndroidMock;
import com.google.android.testing.mocking.UsesMocks;

public class MockingTest extends TestCase {
  @UsesMocks(ClassToMock.class)
  public void testMocks() throws ClassNotFoundException {
	  
	  ClassToMock ctm =  new ClassToMock();
	  assertNull(ctm.getNextInt(2));
	  assertNull(ctm.getString());
	  
	  //create the mock object
    ClassToMock myMockObject = AndroidMock.createMock(ClassToMock.class);
    //define its behaviour
    AndroidMock.expect(myMockObject.getString()).andReturn("Woohoo");
    AndroidMock.expect(myMockObject.getNextInt(2)).andReturn(42);
    //now the mock object is ready:
    AndroidMock.replay(myMockObject);
    
    assertEquals("Woohoo", myMockObject.getString());
    assertEquals(42, myMockObject.getNextInt(2));
    //the tests are finished
    AndroidMock.verify(myMockObject);
  }
}