/**
 * 
 */
package com.neildg.cameraenhance.unittests.processing;

import android.util.Log;

import com.neildg.cameraenhance.processing.IImageProcessor;

/**
 * Unit test for image processing
 * @author NeilDG
 *
 */
public class ImageProcessor01 implements IImageProcessor {
	private final static String TAG = "CameraEnhance_ImageProcessor01";
	
	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#Preprocess()
	 */
	@Override
	public void Preprocess() {
		Log.d(TAG, "Preprocessing");
	}

	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#Process()
	 */
	@Override
	public void Process() {
		Log.d(TAG, "Processing");
	}

	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#PostProcess()
	 */
	@Override
	public void PostProcess() {
		Log.d(TAG, "Postprocessing");
	}

	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#onPreProcessStarted()
	 */
	@Override
	public void onPreProcessStarted() {
		Log.d(TAG, "Pre process started");
	}

	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#onPreProcessFinished()
	 */
	@Override
	public void onPreProcessFinished() {
		Log.d(TAG, "Pre process finished");
	}

	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#onProcessStarted()
	 */
	@Override
	public void onProcessStarted() {
		Log.d(TAG, "Process started");
	}

	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#onProcessFinished()
	 */
	@Override
	public void onProcessFinished() {
		Log.d(TAG, "Process finished");
	}

	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#onPostProcessStarted()
	 */
	@Override
	public void onPostProcessStarted() {
		Log.d(TAG, "Post process started");
	}

	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#onPostProcessFinished()
	 */
	@Override
	public void onPostProcessFinished() {
		Log.d(TAG, "Post process finished");
	}

}
