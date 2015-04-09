/**
 * 
 */
package com.neildg.cameraenhance.processing;

import com.neildg.cameraenhance.camera.CameraManager;
import com.neildg.cameraenhance.images.ImageDataStorage;
import com.neildg.cameraenhance.processing.fastupsample.FastSampleProcessor;
import com.neildg.cameraenhance.processing.fastupsample.UpSampleDenoising;
import com.neildg.cameraenhance.processing.iterativeupsample.IterativeUpSample;
import com.neildg.cameraenhance.processing.workers.ImageWorker;
import com.neildg.cameraenhance.unittests.processing.DenoisingTest;
import com.neildg.cameraenhance.unittests.processing.LaplaceSharpeningTest;
import com.neildg.cameraenhance.unittests.processing.WienerFilterTest;
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
		if(sharedInstance == null) {
			//sharedInstance = new ProcessorDispatcher();
			Log.e(TAG, "Processor dispatcher is not yet initialized!");
		}
		return sharedInstance;
	}
	
	private IImageProcessor imageProcessingTask;
	private ImageWorker imageWorker;
	
	private boolean processingOnGoing = false;
	
	private ProcessorDispatcher() {
		NotificationCenter.getInstance().addObserver(Notifications.ON_IMAGE_PROCESSING_STARTED, this);
		NotificationCenter.getInstance().addObserver(Notifications.ON_IMAGE_PROCESSING_FINISHED, this);
		
		//IMPORTANT: define image processing task here.
	    //this.attachImageProcessor(new FastSampleProcessor());
		//this.attachImageProcessor(new IterativeUpSampleProcessor());
		//this.attachImageProcessor(new LaplaceSharpeningTest());
		//this.attachImageProcessor(new UpSampleDenoising());
		this.attachImageProcessor(new IterativeUpSample());
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
		if(this.imageProcessingTask != null && this.processingOnGoing == false) {
			this.processingOnGoing = true;
			this.imageWorker = new ImageWorker();
			this.imageWorker.attachImageProcessor(this.imageProcessingTask);
			this.imageWorker.start();
			
			CameraManager.getInstance().closeCamera();
		}
		else if(this.processingOnGoing == true) {
			Log.e(TAG, "Cannot start image processing. An image processing task is already being executed!");
		}
		else {
			Log.e(TAG, "Cannot start image processing. There is no attached image processing task!");
		}
	}
	
	private void stopProcessing() {
		this.processingOnGoing = false;
		CameraManager.getInstance().requestCamera();
		CameraManager.getInstance().refreshCameraPreview();
		
		ImageDataStorage.getInstance().releaseAll();
	}
	
	@Override
	public void onNotify(String notificationString, Parameters params) {
		if(notificationString == Notifications.ON_IMAGE_PROCESSING_STARTED) {
			this.startProcessing();
		}
		else if(notificationString == Notifications.ON_IMAGE_PROCESSING_FINISHED) {
			this.stopProcessing();
		}
	}
	
	
}
