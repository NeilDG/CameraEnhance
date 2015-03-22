/**
 * 
 */
package com.neildg.cameraenhance.processing;

import com.neildg.cameraenhance.processing.workers.ImageWorker;
import com.neildg.cameraenhance.utils.notifications.NotificationCenter;
import com.neildg.cameraenhance.utils.notifications.NotificationListener;
import com.neildg.cameraenhance.utils.notifications.Notifications;
import com.neildg.cameraenhance.utils.notifications.Parameters;

import android.util.Log;

/**
 * Starts whatever image processing interface was thrown here.
 * @author NeilDG
 *
 */
public class ProcessorDispatcher implements NotificationListener {
	public final static String TAG = "CameraEnhance_ProcessorDispatcher";
	
	private static ProcessorDispatcher sharedInstance = null;
	public static ProcessorDispatcher getInstance() {
		/*if(sharedInstance == null) {
			sharedInstance = new ProcessorDispatcher();
		}*/
		Log.e(TAG, "Processor dispatcher is not yet initialized!");
		return sharedInstance;
	}
	
	private IImageProcessor imageProcessingTask;
	private ImageWorker imageWorker;
	
	private ProcessorDispatcher() {
		NotificationCenter.getInstance().addObserver(Notifications.ON_IMAGE_PROCESSING_STARTED, this);
	}
	
	public static void initialize() {
		sharedInstance = new ProcessorDispatcher();
	}
	
	public static void destroy() {
		NotificationCenter.getInstance().removeObserver(Notifications.ON_IMAGE_PROCESSING_STARTED, sharedInstance);
		sharedInstance.imageProcessingTask = null;
		sharedInstance.imageWorker = null;
	}
	
	public void attachImageProcessor(IImageProcessor imageProcessor) {
		this.imageProcessingTask = imageProcessor;
	}
	
	public IImageProcessor getImageProcessor() {
		return this.imageProcessingTask;
	}
	
	private void startProcessing() {
		if(this.imageProcessingTask != null) {
			this.imageWorker = new ImageWorker();
			this.imageWorker.attachImageProcessor(this.imageProcessingTask);
			this.imageWorker.start();
		}
		else {
			Log.e(TAG, "Cannot start image processing. There is no attached image processing task!");
		}
	}
	
	@Override
	public void onNotify(String notificationString, Parameters params) {
		if(notificationString == Notifications.ON_IMAGE_PROCESSING_STARTED) {
			this.startProcessing();
		}
	}
	
	
}
