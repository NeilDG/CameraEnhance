/**
 * 
 */
package com.neildg.cameraenhance.capture;

import java.util.ArrayList;

import android.util.Log;

/**
 * Stores captured images here for easier retrieval.
 * @author NeilDG
 *
 */
public class ImageDataStorage {
	private final static String TAG = "CameraEnhance_ImageDataStorage";
	
	private static ImageDataStorage sharedInstance = null;
	public static ImageDataStorage getInstance() {
		if(sharedInstance == null) {
			sharedInstance = new ImageDataStorage();
		}
		
		return sharedInstance;
	}
	
	public final static int MAX_IMAGE_TO_PROCESS = 10;
	
	private byte[] originalImageData;
	private ArrayList<byte[]> imageDataGroup;
	private byte[] processedImageData;
	
	private ImageDataStorage() {
		this.imageDataGroup = new ArrayList<byte[]>();
	}
	
	public void setOriginalImageData(byte[] imageData) {
		this.originalImageData = imageData;
	}
	
	public byte[] getOriginalImageData() {
		return this.originalImageData;
	}
	
	public void addImageDataToProcess(byte[] imageData) {
		if(this.imageDataGroup.size() < MAX_IMAGE_TO_PROCESS) {
			this.imageDataGroup.add(imageData);
		}
		else {
			Log.e(TAG, "You have exceeded the number of images to process!");
		}
	}
	
	public byte[] getImageDataAt(int index) {
		return this.imageDataGroup.get(index);
	}
	
	public int getImageToProcessSize() {
		return this.imageDataGroup.size();
	}
	
	public void setProcessedImageData(byte[] imageData) {
		this.processedImageData = imageData;
	}
	
	public byte[] getProcessedImageData() {
		return this.processedImageData;
	}
}
