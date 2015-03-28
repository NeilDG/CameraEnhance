/**
 * 
 */
package com.neildg.cameraenhance.processing;

import java.util.ArrayList;
import java.util.Hashtable;

import android.util.Log;

import com.neildg.cameraenhance.capture.ImageSequencesHolder;
import com.neildg.cameraenhance.io.ImageReader;
import com.neildg.cameraenhance.io.ImageWriter;

/**
 * Consists of byte data from images stored in the specified album.
 * Memory is allocated as soon as the required image is requested.
 * Developers would have to manually release images no longer being used.
 * @author NeilDG
 *
 */
public class ImageDataStorage {
	private final static String TAG = "CameraEnhance_ImageDataStorage";
	
	private static ImageDataStorage sharedInstance = null;
	public static ImageDataStorage getSharedInstance() {
		if(sharedInstance == null) {
			sharedInstance = new ImageDataStorage();
		}
		
		return sharedInstance;
	}
	
	private byte[] originalImageData;
	private Hashtable<Integer, byte[]> imageDataGroup;
	private byte[] processedImageData;
	
	private ImageDataStorage() {
		
	}
	
	/*
	 * Loads the original image found in the album.
	 */
	public byte[] loadOriginalImage() {
		if(this.originalImageData == null) {
			this.originalImageData = ImageReader.getInstance().getBytesFromFile(ImageWriter.ORIGINAL_IMAGE_NAME);
		}
		
		return this.originalImageData;
	}
	
	/*
	 * Releases the original image. Call this when it is no longer needed
	 */
	public void releaseOriginalImage() {
		this.originalImageData = null;
		System.gc();
	}
	
	/*
	 * Loads a specified image sequence.
	 * Min = 1, Max = depends on setting
	 */
	public byte[] loadImageSequence(int sequenceNum) {
		String fileName = sequenceNum + ".jpg";
		
		if(this.imageDataGroup == null) {
			this.imageDataGroup = new Hashtable<Integer, byte[]>();
		}
		
		if(sequenceNum <= ImageSequencesHolder.MAX_IMAGE_TO_PROCESS) {
			
			byte[] imageData = this.imageDataGroup.get(sequenceNum);
			if(imageData == null) {
				imageData = ImageReader.getInstance().getBytesFromFile(fileName);
				this.imageDataGroup.put(sequenceNum, imageData);
			}
			
			return imageData;
		}
		else {
			Log.e(TAG, "Exceeded sequence num! " +sequenceNum);
			return null;
		}
	}
	
	/*
	 * Releases the specified image sequence. Call this when the image frame is no longer needed
	 */
	public void releaseImageSequence(int sequenceNum) {
		if(this.imageDataGroup.containsKey(sequenceNum)) {
			this.imageDataGroup.remove(sequenceNum);
		}
		
		if(this.imageDataGroup.size() == 0) {
			this.imageDataGroup = null;
		}
		
		System.gc();
	}
}
