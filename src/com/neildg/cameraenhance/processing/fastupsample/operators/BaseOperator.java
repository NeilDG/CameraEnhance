package com.neildg.cameraenhance.processing.fastupsample.operators;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Represents a base image processing operator that has an input matrix and an output matrix
 * @author NeilDG
 *
 */
public abstract class BaseOperator {

	protected Mat inputMatrix;
	protected Mat outputMatrix;

	public BaseOperator() {
		super();
	}

	public abstract Mat perform();
	
	public Mat getOutputMatrix() {
		return this.outputMatrix;
	}

	public void cleanup() {
		if(this.inputMatrix != null) {
			this.inputMatrix.release();
			this.inputMatrix = null;
		}
		
		if(this.outputMatrix != null) {
			this.outputMatrix.release();
			this.outputMatrix = null;
		}
	}

}