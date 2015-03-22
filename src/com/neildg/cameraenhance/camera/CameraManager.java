/**
 * 
 */
package com.neildg.cameraenhance.camera;

import com.neildg.cameraenhance.capture.CaptureCallback;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

/**
 * Handles and controls the camera
 * @author NeilDG
 *
 */
public class CameraManager {
	private final static String TAG = "CameraEnhance_CameraManager";
	
	private static CameraManager sharedInstance = null;
	
	public static CameraManager getInstance() {
		if(sharedInstance == null) {
			Log.e(TAG, "CameraManager has not called initialized!");
		}
		return sharedInstance;
	}
	
	private Camera deviceCamera;
	private CameraPreview cameraPreview;
	
	private boolean isFrontCamera = false;
	private boolean safeToTakePicture = false;
	private CaptureCallback captureCallback = new CaptureCallback();
	
	private CameraManager() {
		
	}
	
	public static void initialize() {
		sharedInstance = new CameraManager();
	}
	
	public static boolean isCameraSupported(Context context) {
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // this device has a camera
	        return true;
	    } else {
	        // no camera on this device
	        return false;
	    }
	}
	
	public void grantCapturePermission() {
		this.safeToTakePicture = true;
	}
	
	public void restrictCapturePermission() {
		this.safeToTakePicture = false;
	}
	
	/*
	 * Requests for camera access and returns its instance
	 */
	public Camera requestCamera() {
		if(this.deviceCamera != null) {
			return this.deviceCamera;
		}
		else {
			try {
		        this.deviceCamera = Camera.open(); // attempt to get a Camera instance
		    }
		    catch (Exception e){
		       Log.e(TAG, "Camera is not available!");
		    }
			
			return this.deviceCamera;
		}
	}
	
	public void setCameraPreview(CameraPreview cameraPreview) {
		this.cameraPreview = cameraPreview;
	}
	
	public void refreshCameraPreview() {
		if(this.deviceCamera != null) {
			this.cameraPreview.updateCameraSource(this.deviceCamera);
		}
	}
	
	/*
	 * Requests for back camera access
	 */
	public Camera requestCameraBack() {
		if(this.deviceCamera != null) {
			this.deviceCamera.release();
			this.deviceCamera = null;
		}
		
		try {
			this.deviceCamera = Camera.open(0);
		}
		catch(Exception e) {
			Log.e(TAG, "Front camera is not available!");
		}
		
		return this.deviceCamera;
	}
	
	/*
	 * Requests for front camera access
	 */
	public Camera requestCameraFront() {
		if(this.deviceCamera != null) {
			this.deviceCamera.release();
			this.deviceCamera = null;
		}
		
		try {
			this.deviceCamera = Camera.open(1);
		}
		catch(Exception e) {
			Log.e(TAG, "Front camera is not available!");
		}
		
		return this.deviceCamera;
	}
	
	/*
	 * Swaps between the back and the front camera
	 */
	public Camera swapCamera() {
		if(this.isFrontCamera) {
			this.isFrontCamera = false;
			return this.requestCameraBack();
		}
		else {
			this.isFrontCamera = true;
			return this.requestCameraFront();
		}
	}
	
	/*
	 * Close the camera being used
	 */
	public void closeCamera() {
		if(this.deviceCamera != null) {
			this.deviceCamera.release();
			this.deviceCamera = null;
		}
	}
	
	/*
	 * Captures photo using the active camera
	 */
	public void capture() {
		if(this.deviceCamera != null && this.safeToTakePicture) {
			this.safeToTakePicture = false;
			this.deviceCamera.takePicture(null, null, this.captureCallback);
		}
	}
	
}
