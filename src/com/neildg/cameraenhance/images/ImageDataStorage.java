/**
 * 
 */
package com.neildg.cameraenhance.images;

import java.util.Hashtable;

import org.opencv.core.Mat;

import android.util.Log;

import com.neildg.cameraenhance.config.ConfigHandler;
import com.neildg.cameraenhance.config.values.BaseConfig;
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
	public static ImageDataStorage getInstance() {
		if(sharedInstance == null) {
			sharedInstance = new ImageDataStorage();
		}
		
		return sharedInstance;
	}
	
	private byte[] originalImageData;
	private Hashtable<Integer, byte[]> imageDataGroup;
	private byte[] processedImageData;
	
	private Mat originalImageMat;
	private Hashtable<Integer, Mat> imageMatGroup;
	
	private BaseConfig currentConfig;
	
	private ImageDataStorage() {
		this.currentConfig = ConfigHandler.getInstance().getCurrentConfig();
	}
	
	/**
	 * Loads the original image found in the album.
	 */
	public byte[] loadOriginalImage() {
		if(this.originalImageData == null) {
			this.originalImageData = ImageReader.getInstance().getBytesFromFile(ImageWriter.ORIGINAL_IMAGE_NAME);
		}
		
		return this.originalImageData;
	}
	
	/**
	 * Releases the original image. Call this when it is no longer needed
	 */
	public void releaseOriginalImage() {
		this.originalImageData = null;
		System.gc();
	}
	
	/**
	 * Loads a specified image sequence.
	 * Min = 0, Max = depends on setting
	 */
	public byte[] loadImageSequence(int sequenceNum) {
		String fileName = sequenceNum + ".jpg";
		
		if(this.imageDataGroup == null) {
			this.imageDataGroup = new Hashtable<Integer, byte[]>();
		}
		
		if(sequenceNum < this.currentConfig.getImageLimit()) {
			
			byte[] imageData = this.imageDataGroup.get(sequenceNum);
			if(imageData == null) {
				imageData = ImageReader.getInstance().getBytesFromFile(fileName);
				
				if(imageData != null) {
					this.imageDataGroup.put(sequenceNum, imageData);
				}
			}
			
			return imageData;
		}
		else {
			Log.e(TAG, "Exceeded sequence num! " +sequenceNum);
			return null;
		}
	}
	
	/**
	 * Releases the specified image sequence. Call this when the image frame is no longer needed
	 */
	public void releaseImageSequence(int sequenceNum) {
		if(this.imageDataGroup == null) {
			return;
		}
		
		if(this.imageDataGroup.containsKey(sequenceNum)) {
			this.imageDataGroup.remove(sequenceNum);
		}
		
		if(this.imageDataGroup.size() == 0) {
			this.imageDataGroup = null;
		}
		
		System.gc();
	}
	
	public void releaseAllImageSequences() {
		BaseConfig currentConfig = ConfigHandler.getInstance().getCurrentConfig();
		for(int i = 0; i < currentConfig.getImageLimit(); i++) {
			this.releaseImageSequence(i);
		}
	}
	
	/**
	 * Sets the processed image data and stored in runtime. This does not write it into file yet.
	 * Call storeProcessImageData to do so.
	 */
	public void setProcessImageData(byte[] processedImage) {
		this.processedImageData = processedImage;
	}
	
	/**
	 * Stores the processed image data into file and releases runtime memory.
	 */
	public void storeProcessImageData() {
		if(this.processedImageData != null) {
			ImageWriter.getInstance().saveProcessedImage(this.processedImageData);
			this.processedImageData = null;
			System.gc();
		}
		else {
			Log.e(TAG, "Processed image data does not exist!");
		}
	}
	
	/**
	 * Returns the processed image data. If it is null, it attempts to load it from file.
	 */
	public byte[] loadProcessedImageData() {
		if(this.processedImageData == null) {
			this.processedImageData = ImageReader.getInstance().getBytesFromFile(ImageWriter.PROCESSED_IMAGE_NAME);
			
			//if it's still null, then file is not existing yet
			if(this.processedImageData == null) {
				Log.e(TAG, "Processed image is not found in file!");
			}
		}
		
		return this.processedImageData;
	}
	
	public void releaseProcessedImageData() {
		this.processedImageData = null;
		System.gc();
	}
	
	/**
	 * Attempts to release all resources
	 */
	public void releaseAll() {
		this.releaseOriginalImage();
		this.releaseProcessedImageData();
		
		for(int i = 0; i < ConfigHandler.getInstance().getCurrentConfig().getImageLimit(); i++) {
			this.releaseImageSequence(i);
		}
		
		this.releaseMatOfOriginalImage();
		
		for(int i = 0; i < ConfigHandler.getInstance().getCurrentConfig().getImageLimit(); i++) {
			this.releaseMatOfImageSequence(i);
		}
	}
	
	/**
	 * Returns the mat form of the original image
	 */
	public Mat loadMatFormOfOriginalImage() {
		if(this.originalImageMat == null) {
			this.originalImageMat = ImageReader.getInstance().imReadOpenCV(ImageWriter.ORIGINAL_IMAGE_NAME);
		}
		Log.d(TAG, "Original Image mat: " +this.originalImageMat.depth());
		return this.originalImageMat;
	}
	
	/**
	 * Releases the mat form of original image. Frees up the memory if needed.
	 */
	public void releaseMatOfOriginalImage() {
		if(this.originalImageMat == null) {
			this.originalImageMat.release();
			this.originalImageMat = null;
		}
	}
	
	/**
	 * Loads a specified image sequence in mat form
	 * Min = 0, Max = depends on setting
	 */
	public Mat loadMatOfImageSequence(int sequenceNum) {
		String fileName = sequenceNum + ".jpg";
		
		if(this.imageMatGroup == null) {
			this.imageMatGroup = new Hashtable<Integer, Mat>();
		}
		
		if(sequenceNum < this.currentConfig.getImageLimit()) {
			
			Mat imageMat = this.imageMatGroup.get(sequenceNum);
			if(imageMat == null) {
				imageMat = ImageReader.getInstance().imReadOpenCV(fileName);
				
				if(imageMat != null) {
					this.imageMatGroup.put(sequenceNum, imageMat);
				}
			}
			
			return imageMat;
		}
		else {
			Log.e(TAG, "Exceeded sequence num! " +sequenceNum);
			return null;
		}
	}
	
	/**
	 * Releases the specified image sequence represented in MAT. Call this when the image frame is no longer needed
	 */
	public void releaseMatOfImageSequence(int sequenceNum) {
		if(this.imageMatGroup == null) {
			return;
		}
		
		if(this.imageMatGroup.containsKey(sequenceNum)) {
			this.imageMatGroup.remove(sequenceNum);
		}
		
		if(this.imageMatGroup.size() == 0) {
			this.imageMatGroup = null;
		}
		
		System.gc();
	}
}
