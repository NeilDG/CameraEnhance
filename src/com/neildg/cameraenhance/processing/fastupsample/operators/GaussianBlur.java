/**
 * 
 */
package com.neildg.cameraenhance.processing.fastupsample.operators;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Component that performs the gaussian blur
 * @author NeilDG
 *
 */
public class GaussianBlur extends BaseOperator {
	private final static String TAG = "CameraEnhance_GaussianBlur";
	
	private Size kernelSize;
	private double sigmaX = 0.0;
	private double sigmaY = 0.0;
	
	public GaussianBlur(Mat inputMatrix) {
		this.inputMatrix = inputMatrix;
	}
	
	public void setParameters(int kernelWidth, int kernelHeight, double sigmaX, double sigmaY) {
		this.kernelSize = new Size(kernelWidth, kernelHeight);
		this.sigmaX = sigmaX;
		this.sigmaY = sigmaY;
		
		this.outputMatrix = new Mat();
	}

	@Override
	public Mat perform() {
		Imgproc.GaussianBlur(this.inputMatrix, this.outputMatrix, this.kernelSize, this.sigmaX, this.sigmaY);
		
		this.inputMatrix.release();
		this.inputMatrix = null;
		
		return this.outputMatrix;
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
		
		if(this.kernelSize != null) {
			this.kernelSize = null;
		}
	}
}
