/**
 * 
 */
package com.neildg.cameraenhance.processing.fastupsample.operators;

import org.opencv.core.Mat;

/**
 * An implementation of a Wiener filter based from
 * https://www.ocf.berkeley.edu/~rayver/mm/code/cvWiener2/
 * @author NeilDG
 *
 */
public class WienerFilter extends BaseOperator {
	private final static String TAG = "CameraEnhance_WienerFilter";
	
	public WienerFilter(Mat inputMatrix, Mat outputMatrix) {
		this.inputMatrix = inputMatrix;
		this.outputMatrix = outputMatrix;
		
	}


	@Override
	public Mat perform() {
		// TODO Auto-generated method stub
		return null;
	}

}
