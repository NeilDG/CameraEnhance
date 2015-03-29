/**
 * 
 */
package com.neildg.cameraenhance.unittests.processing;

import com.neildg.cameraenhance.processing.ProcessorDispatcher;
import com.neildg.cameraenhance.unittests.IUnitTest;
import com.neildg.cameraenhance.utils.notifications.NotificationCenter;
import com.neildg.cameraenhance.utils.notifications.Notifications;

/**
 * Test for image processing
 * @author NeilDG
 *
 */
public class ImageProcessingTest implements IUnitTest {
	private final static String TAG = "CameraEnhance_ImageProcessingTest";
	
	private ImageProcessor01 testImageProcessing;
	
	public ImageProcessingTest() {
		this.testImageProcessing = new ImageProcessor01();
	}
	
	@Override
	public void startTest() {
		ProcessorDispatcher.getInstance().attachImageProcessor(this.testImageProcessing);
		NotificationCenter.getInstance().postNotification(Notifications.ON_IMAGE_PROCESSING_STARTED);
	}
}
