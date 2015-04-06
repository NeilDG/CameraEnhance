/**
 * 
 */
package com.neildg.cameraenhance.processing.operators;

import org.opencv.core.Mat;
import org.opencv.photo.Photo;

/**
 * Class that handles denoising.
 * @author Patrick
 *
 */
public class Denoise extends BaseOperator {
	private final static String TAG = "CameraEnhance_Denoise";
	
	
	public Denoise(Mat inputMatrix, Mat outputMatrix) {
		this.inputMatrix = inputMatrix;
		this.outputMatrix = outputMatrix;
	}
	
	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.operators.BaseOperator#perform()
	 */
	@Override
	public Mat perform() {
		Photo.fastNlMeansDenoisingColored(this.inputMatrix, this.outputMatrix);
		return this.outputMatrix;
	}

}
