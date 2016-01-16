/**
 * 
 */
package com.neildg.cameraenhance.processing.operators;

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
import com.neildg.cameraenhance.config.values.DefaultConfigValues;
import com.neildg.cameraenhance.images.ImageDataStorage;
import com.neildg.cameraenhance.thumbnail.BitmapDecoder;

/**
 * Handles the initial upsampling with a bicubic interpolation
 * @author NeilDG
 *
 */
public class UpsampleInterpolate extends BaseOperator {
	private final static String TAG = "CameraEnhance_InitialUpSampler";
	
	private byte[] originalImage;
	private int upSampleFactor = 0;
	
	public UpsampleInterpolate(int upSampleFactor) {
		this.originalImage = ImageDataStorage.getInstance().loadOriginalImage();
		this.upSampleFactor = upSampleFactor;
	}
	
	public int getUpSampleFactor() {
		return this.upSampleFactor;
	}
	

	/**
	 * Starts upsampling using bicubic interpolation
	 */
	@Override
	public Mat perform() {
		//use the original image and actual camera size, not the shutter size
		//since we'll only process one image, use the original (higher resolution) instead of the image sequences
		int width = CameraManager.getInstance().getActualCameraSize().width;
		int height = CameraManager.getInstance().getActualCameraSize().height;
		
		this.inputMatrix = ImageDataStorage.getInstance().loadMatFormOfOriginalImage();
		Log.d(TAG, "Width: " +width+ " Height: " +height);
		
		//MAT is represented as row-major format. Therefore, it's height X width
		if(this.outputMatrix == null) {
			this.outputMatrix = new Mat(this.inputMatrix.height() * this.upSampleFactor, this.inputMatrix.width() * this.upSampleFactor, this.inputMatrix.type());
		}
		
		Imgproc.resize(this.inputMatrix, this.outputMatrix, this.outputMatrix.size(), 0, 0, Imgproc.INTER_CUBIC);

		return this.outputMatrix;
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
		
		if(this.originalImage != null) {
			this.originalImage = null;
		}
	}
}
