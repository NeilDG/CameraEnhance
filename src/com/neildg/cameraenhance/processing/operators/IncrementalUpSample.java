/**
 * 
 */
package com.neildg.cameraenhance.processing.operators;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * @author NeilDG
 *
 */
public class IncrementalUpSample extends BaseOperator {
	private final static String TAG = "CameraEnhance_IncrementalUpSample";
	
	private float upSampleFactor = 0;
	
	private int newWidth = 0;
	private int newHeight = 0;
	
	/**
	 * 
	 * @param originalMatrix - the matrix of the original image
	 * @param inputMatrix - the input matrix to be resized.
	 * @param upSampleFactor - the upsample factor
	 */
	public IncrementalUpSample(Mat originalMatrix, Mat inputMatrix, float upSampleFactor) {
		this.inputMatrix = inputMatrix;
		this.upSampleFactor = upSampleFactor;
		this.newHeight = Math.round(originalMatrix.height() * this.upSampleFactor);
		this.newWidth = Math.round(originalMatrix.width() * this.upSampleFactor);
	}
	
	@Override
	public Mat perform() {
		
		this.outputMatrix = new Mat(this.newHeight, this.newWidth, this.inputMatrix.type());
		
		Imgproc.resize(this.inputMatrix, this.outputMatrix, this.outputMatrix.size(), 0, 0, Imgproc.INTER_CUBIC);
		return this.outputMatrix;
	}

}
