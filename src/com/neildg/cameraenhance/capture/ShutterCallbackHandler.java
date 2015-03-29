/**
 * 
 */
package com.neildg.cameraenhance.capture;

import java.util.concurrent.Semaphore;

import com.neildg.cameraenhance.camera.CameraManager;
import com.neildg.cameraenhance.config.ConfigHandler;
import com.neildg.cameraenhance.config.values.BaseConfig;
import com.neildg.cameraenhance.io.ImageWriter;
import com.neildg.cameraenhance.ui.ProgressDialogHandler;
import com.neildg.cameraenhance.utils.notifications.NotificationCenter;
import com.neildg.cameraenhance.utils.notifications.Notifications;

import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;

/**
 * Simulates the burst mode of the camera via preview callback
 * @author NeilDG
 *
 */
public class ShutterCallbackHandler extends Thread implements PreviewCallback {
	private final static String TAG = "CameraEnhance_ShutterCallback";
	
	private Semaphore waitSem;
	private BaseConfig currentConfig;
	
	public ShutterCallbackHandler() {
		this.waitSem = new Semaphore(0);
		this.currentConfig = ConfigHandler.getInstance().getCurrentConfig();
	}
	
	@Override
	public void run() {
		ProgressDialogHandler.getInstance().showDialog("Taking pictures", "Do not move the device!");
		Camera camera = CameraManager.getInstance().requestCamera();
		CameraManager.getInstance().setupCameraForShutter();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(int i = 0; i < this.currentConfig.getImageLimit(); i++) {
			camera.setOneShotPreviewCallback(this);
			
			try {
				this.waitSem.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ImageWriter.getInstance().startWriting(); //start writing images
		ImageSequencesHolder.getInstance().release();
		
		ProgressDialogHandler.getInstance().hideDialog();
		CameraManager.getInstance().resetSettings();
		
		//start processing immediately
		NotificationCenter.getInstance().postNotification(Notifications.ON_IMAGE_PROCESSING_STARTED);
	}
	
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		ImageSequencesHolder.getInstance().addImageDataToProcess(data);
		Log.d(TAG, "Saved image data");
		
		try {
			Thread.sleep(this.currentConfig.getShutterDelay());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.waitSem.release();
	}
	
}
