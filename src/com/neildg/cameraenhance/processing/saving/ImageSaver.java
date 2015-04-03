/**
 * 
 */
package com.neildg.cameraenhance.processing.saving;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import com.neildg.cameraenhance.images.ImageDataStorage;
import com.neildg.cameraenhance.io.ImageWriter;

/**
 * Component that saves that encodes the image given a matrix
 * @author NeilDG
 *
 */
public class ImageSaver {
	private final static String TAG = "CameraEnhance_ImageSaver";
	
	private Mat matrixToSave;
	
	public ImageSaver(Mat inputMatrix) {
		this.matrixToSave = inputMatrix;
	}
	
	/**
	 * Converts the matrix into its image form and saves it as PROCESSED image into the SDCARD
	 */
	public void encodeAndSave() {
		MatOfByte matOfByte = new MatOfByte();
		Highgui.imencode(".jpg", this.matrixToSave, matOfByte);
		
		ImageDataStorage.getInstance().setProcessImageData(matOfByte.toArray());
		ImageDataStorage.getInstance().storeProcessImageData();
		
		this.matrixToSave.release();
		this.matrixToSave = null;
		
		matOfByte.release();
		matOfByte = null;
	}
	
	/**
	 * Converts the matrix into its image form and saves it using a given filename
	 * @param fileName
	 */
	public void encodeAndSave(String fileName) {
		MatOfByte matOfByte = new MatOfByte();
		Highgui.imencode(".jpg", this.matrixToSave, matOfByte);
		
		ImageWriter.getInstance().saveSpecifiedImage(matOfByte.toArray(), fileName);
		
		matOfByte.release();
		matOfByte = null;
	}
}
