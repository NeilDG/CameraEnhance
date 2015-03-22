/**
 * 
 */
package com.neildg.cameraenhance.processing.workers;

import com.neildg.cameraenhance.processing.IImageProcessor;
import com.neildg.cameraenhance.utils.notifications.NotificationCenter;
import com.neildg.cameraenhance.utils.notifications.Notifications;

/**
 * Represents a generic worker thread to process an image
 * @author NeilDG
 *
 */
public class ImageWorker extends Thread {
	private final static String TAG = "CameraEnhance_AImageWorker";
	
	private IImageProcessor imageProcessor;
	
	public void attachImageProcessor(IImageProcessor imageProcessor) {
		this.imageProcessor = imageProcessor;
	}
	
	@Override
	public void run() {
		//preprocessing stage
		NotificationCenter.getInstance().postNotification(Notifications.ON_PRE_PROCESS_STARTED);
		this.imageProcessor.onPreProcessStarted();
		this.imageProcessor.Preprocess();
		this.imageProcessor.onPreProcessFinished();
		NotificationCenter.getInstance().postNotification(Notifications.ON_PRE_PROCESS_FINISHED);
		
		//processing stage
		NotificationCenter.getInstance().postNotification(Notifications.ON_PROCESS_STARTED);
		this.imageProcessor.onProcessStarted();
		this.imageProcessor.Process();
		this.imageProcessor.onProcessFinished();
		NotificationCenter.getInstance().postNotification(Notifications.ON_PROCESS_FINISHED);
		
		//postprocessing stage
		NotificationCenter.getInstance().postNotification(Notifications.ON_POST_PROCESS_STARTED);
		this.imageProcessor.onPostProcessStarted();
		this.imageProcessor.PostProcess();
		this.imageProcessor.onPostProcessFinished();
		NotificationCenter.getInstance().postNotification(Notifications.ON_POST_PROCESS_FINISHED);
	}
}
