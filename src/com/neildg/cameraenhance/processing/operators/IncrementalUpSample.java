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
	
	private int upSampleFactor = 0;
	
	private int newWidth = 0;
	private int newHeight = 0;
	
	public IncrementalUpSample(Mat originalMatrix, Mat inputMatrix, int upSampleFactor) {
		this.inputMatrix = inputMatrix;
		this.upSampleFactor = upSampleFactor;
		this.newHeight = originalMatrix.height() * this.upSampleFactor;
		this.newWidth = originalMatrix.width() * this.upSampleFactor;
	}
	
	@Override
	public Mat perform() {
		
		this.outputMatrix = new Mat(this.newHeight, this.newWidth, this.inputMatrix.type());
		
		Imgproc.resize(this.inputMatrix, this.outputMatrix, this.outputMatrix.size(), 0, 0, Imgproc.INTER_CUBIC);
		return this.outputMatrix;
	}

}
