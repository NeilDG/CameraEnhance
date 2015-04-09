/**
 * 
 */
package com.neildg.cameraenhance.processing.operators;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Component that performs the gaussian blur
 * @author NeilDG
 *
 */
public class Blur extends BaseOperator {
	private final static String TAG = "CameraEnhance_GaussianBlur";
	
	public enum BlurType {
		GAUSSIAN,
		MEDIAN
	}
	
	private Size kernelSize;
	private double sigmaX = 0.0;
	private double sigmaY = 0.0;
	private BlurType blurType = BlurType.GAUSSIAN;
	
	public Blur(Mat inputMatrix, Mat outputMatrix) {
		//this.inputMatrix = new Mat();
		//inputMatrix.copyTo(this.inputMatrix);
		this.inputMatrix = inputMatrix;
		this.outputMatrix = outputMatrix;
	}
	
	public void setParameters(int kernelWidth, int kernelHeight, double sigmaX, double sigmaY, BlurType blurType) {
		this.kernelSize = new Size(kernelWidth, kernelHeight);
		this.sigmaX = sigmaX;
		this.sigmaY = sigmaY;
		this.blurType = blurType;
	}

	@Override
	public Mat perform() {
		if(this.blurType == BlurType.GAUSSIAN) {
			Imgproc.GaussianBlur(this.inputMatrix, this.outputMatrix, this.kernelSize, this.sigmaX, this.sigmaY);
		}
		else {
			this.outputMatrix.convertTo(this.outputMatrix, CvType.CV_8UC1);
			Imgproc.medianBlur(this.inputMatrix, this.outputMatrix, (int) this.kernelSize.width);
		}
		
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
