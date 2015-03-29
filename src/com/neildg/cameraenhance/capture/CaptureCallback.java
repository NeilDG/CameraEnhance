/**
 * 
 */
package com.neildg.cameraenhance.capture;

import com.neildg.cameraenhance.camera.CameraManager;
import com.neildg.cameraenhance.utils.notifications.NotificationCenter;
import com.neildg.cameraenhance.utils.notifications.Notifications;
import com.neildg.cameraenhance.utils.notifications.Parameters;

import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;

/**
 * Implements the picture callback called by the camera upon capture of image
 * @author NeilDG
 *
 */
public class CaptureCallback implements PictureCallback {
	private final static String TAG = "CameraEnhance_CaptureCallback";
	
	public final static String CAPTURED_IMAGE_DATA_KEY = "CAPTURED_IMAGE_DATA_KEY";
	
	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		Log.d(TAG, "Picture taken!");
		
		Parameters params = new Parameters();
		params.putExtra(CAPTURED_IMAGE_DATA_KEY, data);
		
		ImageSequencesHolder.getInstance().setOriginalImageData(data);
		NotificationCenter.getInstance().postNotification(Notifications.ON_CREATE_THUMBNAIL, params);
		
		//update camera source to refresh preview
		CameraManager.getInstance().refreshCameraPreview();
		
		ShutterCallbackHandler shutterHandler = new ShutterCallbackHandler();
		shutterHandler.start();
		//NotificationCenter.getInstance().postNotification(Notifications.ON_IMAGE_PROCESSING_STARTED);
	}
	
	

}
