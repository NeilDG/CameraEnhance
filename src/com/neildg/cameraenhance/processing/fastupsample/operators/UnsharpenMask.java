/**
 * 
 */
package com.neildg.cameraenhance.processing.fastupsample.operators;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import android.util.Log;

/**
 * Represents an unsharpen mask operation
 * @author NeilDG
 *
 */
public class UnsharpenMask extends BaseOperator {
	private final static String TAG = "CameraEnhance_UnsharpenMask";
	
	
	public UnsharpenMask(Mat originalMatrix, Mat blurredMatrix) {
		this.inputMatrix = originalMatrix;
		this.outputMatrix = blurredMatrix;
		
		Log.d(TAG , "Input size: " +this.inputMatrix.size().area() + " Output size: " +this.outputMatrix.size().area());
	}
	
	@Override
	public Mat perform() {
		Core.addWeighted(this.inputMatrix, 1.5, this.outputMatrix, -0.5, 0, this.outputMatrix);
		
		return this.outputMatrix;
	}

}
