/**
 * 
 */
package com.neildg.cameraenhance.processing.fastupsample;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.util.Log;

import com.neildg.cameraenhance.camera.CameraManager;
import com.neildg.cameraenhance.images.ImageDataStorage;
import com.neildg.cameraenhance.thumbnail.BitmapDecoder;

/**
 * Handles the initial upsampling.
 * @author NeilDG
 *
 */
public class InitialUpSampler {
	private final static String TAG = "CameraEnhance_InitialUpSampler";
	
	private byte[] originalImage;
	private int upSampleFactor = 4;
	
	private Mat originalMatrix;
	private Mat resizedMatrix;
	
	public InitialUpSampler() {
		this.originalImage = ImageDataStorage.getInstance().loadOriginalImage();
	}
	
	public int getUpSampleFactor() {
		return this.upSampleFactor;
	}
	
	/**
	 * Starts the upsampling using bicubic interpolation
	 */
	public void performUpSampling() {
		
		//use the original image and actual camera size, not the shutter size
		//since we'll only process one image, use the original (higher resolution) instead of the image sequences
		int width = CameraManager.getInstance().getActualCameraSize().width;
		int height = CameraManager.getInstance().getActualCameraSize().height;
		
		/*Bitmap bitmap = BitmapDecoder.decodeActualBitmapFromByteArray(this.originalImage);
		this.originalMatrix = new Mat();
		Utils.bitmapToMat(bitmap, this.originalMatrix);*/
		
		this.originalMatrix = ImageDataStorage.getInstance().loadMatFormOfOriginalImage();
		Log.d(TAG, "Width: " +width+ " Height: " +height);
		
		//MAT is represented as row-major format. Therefore, it's height X width
		this.resizedMatrix = new Mat(this.originalMatrix.height() * this.upSampleFactor, this.originalMatrix.width() * this.upSampleFactor, this.originalMatrix.type());
		Imgproc.resize(this.originalMatrix, this.resizedMatrix, this.resizedMatrix.size(), 0, 0, Imgproc.INTER_CUBIC);
		
		Size kernelSize = new Size(13, 13);
		Imgproc.GaussianBlur(this.resizedMatrix, this.resizedMatrix, kernelSize, 1.5, 1.5);
		
		//convert to byte form
		MatOfByte matOfByte = new MatOfByte();
        Highgui.imencode(".jpg",this.resizedMatrix, matOfByte);
        
        ImageDataStorage.getInstance().setProcessImageData(matOfByte.toArray());
        ImageDataStorage.getInstance().storeProcessImageData();
	}
}
