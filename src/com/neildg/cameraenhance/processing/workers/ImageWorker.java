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
		this.imageProcessor.onPreProcessStarted();
		this.imageProcessor.Preprocess();
		this.imageProcessor.onPreProcessFinished();
		
		//processing stage
		this.imageProcessor.onProcessStarted();
		this.imageProcessor.Process();
		this.imageProcessor.onProcessFinished();
		
		//postprocessing stage
		this.imageProcessor.onPostProcessStarted();
		this.imageProcessor.PostProcess();
		this.imageProcessor.onPostProcessFinished();
		
		//notify that image processing is finished
		NotificationCenter.getInstance().postNotification(Notifications.ON_IMAGE_PROCESSING_FINISHED);
	}
}
