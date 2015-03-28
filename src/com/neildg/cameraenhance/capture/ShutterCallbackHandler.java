/**
 * 
 */
package com.neildg.cameraenhance.capture;

import java.util.concurrent.Semaphore;

import com.neildg.cameraenhance.camera.CameraManager;
import com.neildg.cameraenhance.io.ImageWriter;
import com.neildg.cameraenhance.ui.ProgressDialogHandler;

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
	
	private long delayPerFrame = 0;
	
	private Semaphore waitSem;
	
	public ShutterCallbackHandler(long delay) {
		this.delayPerFrame = delay;
		this.waitSem = new Semaphore(0);
	}
	
	public float getDelayPerFrame() {
		return this.delayPerFrame;
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
		
		for(int i = 0; i < ImageDataStorage.MAX_IMAGE_TO_PROCESS; i++) {
			camera.setOneShotPreviewCallback(this);
			
			try {
				this.waitSem.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ImageWriter.getInstance().startWriting(); //start writing images
		ImageDataStorage.getInstance().release();
		
		ProgressDialogHandler.getInstance().hideDialog();
		CameraManager.getInstance().resetSettings();
	}
	
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		ImageDataStorage.getInstance().addImageDataToProcess(data);
		Log.d(TAG, "Saved image data");
		
		try {
			Thread.sleep(this.delayPerFrame);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.waitSem.release();
	}
	
}
