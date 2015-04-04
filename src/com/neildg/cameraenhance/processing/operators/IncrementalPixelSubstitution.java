/**
 * 
 */
package com.neildg.cameraenhance.processing.operators;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * Incremental version of pixel substitution
 * @author NeilDG
 *
 */
public class IncrementalPixelSubstitution extends PixelSubstitution {
	private final static String TAG = "CameraEnhance_IncrementalPixelSubstitution";
	
	private float increasingConstant = 0.0f;
	
	public IncrementalPixelSubstitution(Mat lowResMatrix, Mat sharpenedMatrix, float increasingConstant, int iteration) {
		super(lowResMatrix, sharpenedMatrix, 0);
		this.increasingConstant = (increasingConstant * iteration) + 1.0f; //the basis of the upsampled factor
	}
	
	@Override
	public Mat perform() {
		//this.outputMatrix.convertTo(this.outputMatrix, CvType.CV_64FC3);
		
		//ROW = height
		//COL = width
		//replaces pixel from L based on upsampled factor
		for(int row = 0; row < this.inputMatrix.height(); row++) {
			for(int col = 0; col < this.inputMatrix.width(); col++) {
				double[] pixelData = this.inputMatrix.get(row, col);
				
				/*double[] pixelData = new double[3];
				pixelData[BLUE_INDEX] = 0;
				pixelData[RED_INDEX] = 0;
				pixelData[GREEN_INDEX] = 0;*/
				
				int rowToReplace = (int)(row * this.increasingConstant);
				int colToReplace = (int)(col * this.increasingConstant);
				
				if(rowToReplace < this.outputMatrix.height() && colToReplace < this.outputMatrix.width()) {
					//Log.d(TAG, "Replaced row: " +rowToReplace+ " Col: " +colToReplace+ " PixelData R: " +pixelData[RED_INDEX]+ " G: " +pixelData[GREEN_INDEX]+ " B: " +pixelData[BLUE_INDEX]);
					this.outputMatrix.put(rowToReplace, colToReplace, pixelData);
				}
			}
		}
		
		
		return this.outputMatrix;
	}
}
