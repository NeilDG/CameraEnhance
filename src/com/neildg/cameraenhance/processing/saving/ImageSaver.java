/**
 * 
 */
package com.neildg.cameraenhance.processing.saving;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import com.neildg.cameraenhance.images.ImageDataStorage;
import com.neildg.cameraenhance.io.ImageWriter;

/**
 * Component that saves that encodes the image given a matrix
 * @author NeilDG
 *
 */
public class ImageSaver {
	private final static String TAG = "CameraEnhance_ImageSaver";
	
	
	public ImageSaver() {
		
	}
	
	/**
	 * Converts the matrix into its image form and saves it as PROCESSED image into the SDCARD
	 */
	public static void encodeAndSaveAsProcessed(Mat matrixToSave) {
		MatOfByte matOfByte = new MatOfByte();
		Highgui.imencode(".jpg", matrixToSave, matOfByte);
		
		ImageDataStorage.getInstance().setProcessImageData(matOfByte.toArray());
		ImageDataStorage.getInstance().storeProcessImageData();
		
		matOfByte.release();
		matOfByte = null;
	}
	
	/**
	 * Converts the matrix into its image form and saves it using a given filename and AS IS in image type.
	 * @param fileName
	 */
	public static void encodeAndSave(Mat matrixToSave, String fileName) {
		MatOfByte matOfByte = new MatOfByte();
		Highgui.imencode(".jpg", matrixToSave, matOfByte);
		
		ImageWriter.getInstance().saveSpecifiedImage(matOfByte.toArray(), fileName);
		
		matOfByte.release();
		matOfByte = null;
	}
	
	/**
	 * Converts the matrix into its image form (in YUV format) and saves it as RGB format. 
	 * @param matrixToSave
	 * @param fileName
	 * @param colorType
	 */
	public static void encodeAndSaveAsRGB(Mat matrixToSave, String fileName) {
		Imgproc.cvtColor(matrixToSave, matrixToSave, Imgproc.COLOR_YUV2BGR);
		ImageSaver.encodeAndSave(matrixToSave, fileName);
		Imgproc.cvtColor(matrixToSave, matrixToSave, Imgproc.COLOR_BGR2YUV);
	}
}
