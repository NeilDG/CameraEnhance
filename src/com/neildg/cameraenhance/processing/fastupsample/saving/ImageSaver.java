/**
 * 
 */
package com.neildg.cameraenhance.processing.fastupsample.saving;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import com.neildg.cameraenhance.images.ImageDataStorage;

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
	 * Converts the matrix into its image form and saves it into the SDCARD
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
}
