package com.neildg.cameraenhance.unittests;

import com.neildg.cameraenhance.utils.notifications.NotificationCenter;
import com.neildg.cameraenhance.utils.notifications.Notifications;
import com.neildg.cameraenhance.utils.notifications.Parameters;

public class TestNotify {
	public final static String MY_ARRAY_KEY = "MY_ARRAY_KEY";
	
	private int[] testData;
	
	public TestNotify() {
		this.testData = new int[]{1,2,3,4,5};
		
		Parameters params = new Parameters();
		params.putExtra(MY_ARRAY_KEY, this.testData);
		
		//with parameters
		NotificationCenter.getInstance().postNotification(Notifications.ON_IMAGE_PROCESSING_STARTED, params);
		//no parameters
		NotificationCenter.getInstance().postNotification(Notifications.ON_IMAGE_PROCESSING_STARTED);
		
	}
}
