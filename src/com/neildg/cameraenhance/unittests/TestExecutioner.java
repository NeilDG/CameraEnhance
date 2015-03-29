/**
 * 
 */
package com.neildg.cameraenhance.unittests;

import com.neildg.cameraenhance.unittests.memory.MemoryTest;
import com.neildg.cameraenhance.unittests.processing.ImageProcessingTest;

/**
 * Handles specified unit test procedures
 * @author NeilDG
 *
 */
public class TestExecutioner {
	private final static String TAG = "CameraEnhance_TestExecutioner";
	
	private static TestExecutioner sharedInstance = null;
	public static TestExecutioner getInstance() {
		if(sharedInstance == null) {
			sharedInstance = new TestExecutioner();
		}
		
		return sharedInstance;
	}
	
	private TestExecutioner() {
		
	}
	
	public void startTestSeries() {
		ImageProcessingTest imageProcessingTest = new ImageProcessingTest();
		imageProcessingTest.startTest();
	}

	public void startMemoryTest() {
		MemoryTest memoryTest = new MemoryTest();
		memoryTest.startTest();
	}
}
