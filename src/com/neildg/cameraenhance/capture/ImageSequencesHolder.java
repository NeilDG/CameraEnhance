/**
 * 
 */
package com.neildg.cameraenhance.capture;

import java.util.ArrayList;

import android.os.Debug;
import android.util.Log;

/**
 * Image sequences from camera capture and the original image is stored here.
 * Once the capture is done. Memory is released. You cannot use this class to retrieve
 * sequences during the processing phase.
 * @author NeilDG
 *
 */
public class ImageSequencesHolder {
	private final static String TAG = "CameraEnhance_ImageDataStorage";
	
	private static ImageSequencesHolder sharedInstance = null;
	public static ImageSequencesHolder getInstance() {
		if(sharedInstance == null) {
			sharedInstance = new ImageSequencesHolder();
		}
		
		return sharedInstance;
	}
	
	public final static int MAX_IMAGE_TO_PROCESS = 10;
	
	private byte[] originalImageData;
	private ArrayList<byte[]> imageDataGroup;
	private byte[] processedImageData;
	
	private ImageSequencesHolder() {
		this.imageDataGroup = new ArrayList<byte[]>();
	}
	
	public void setOriginalImageData(byte[] imageData) {
		this.originalImageData = imageData;
	}
	
	public byte[] getOriginalImageData() {
		return this.originalImageData;
	}
	
	public void addImageDataToProcess(byte[] imageData) {
		if(this.imageDataGroup.size() <= MAX_IMAGE_TO_PROCESS) {
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
	
	/*
	 * Releases allocation of byte data for reuse. It is best to call this upon writing to file as images are already saved.
	 */
	public void release() {
		this.imageDataGroup.clear();
		this.originalImageData = null;
		this.processedImageData = null;
	}
}