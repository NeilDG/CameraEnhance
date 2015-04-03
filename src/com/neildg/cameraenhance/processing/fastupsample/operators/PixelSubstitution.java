/**
 * 
 */
package com.neildg.cameraenhance.processing.fastupsample.operators;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import com.neildg.cameraenhance.images.ImageDataStorage;

import android.util.Log;

/**
 * Component that handles pixel substitution
 * @author NeilDG
 *
 */
public class PixelSubstitution extends BaseOperator {
	private final static String TAG = "CameraEnhance_PixelSubstitution";
	
	private final static int BLUE_INDEX = 0;
	private final static int GREEN_INDEX = 1;
	private final static int RED_INDEX = 2;
	
	private int upSampleFactor = 1;
	/**
	 * 
	 * @param lowResMatrix - the original low res image matrix
	 * @param sharpenedMatrix - the sharpened matrix after the deconvolution operator
	 * @param upSampleFactor - the upsampled factor of image
	 */
	public PixelSubstitution(Mat lowResMatrix, Mat sharpenedMatrix, int upSampleFactor) {
		this.inputMatrix = lowResMatrix;
		this.outputMatrix = sharpenedMatrix;
		this.upSampleFactor = upSampleFactor;
	}
	
	@Override
	public Mat perform() {
		this.outputMatrix.convertTo(this.outputMatrix, CvType.CV_64FC3);
		
		/*for(int i = 0; i < this.outputMatrix.height(); i++) {
			for(int j = 0; j < this.outputMatrix.width(); j++) {
				double[] pixelData = this.outputMatrix.get(i, j);
				Log.d(TAG, "Pixel R: " +pixelData[RED_INDEX]+ " G: " +pixelData[GREEN_INDEX] + " B: " +pixelData[BLUE_INDEX]);
			}
		}*/
		
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
				
				int rowToReplace = (row * this.upSampleFactor) + 1;
				int colToReplace = (col * this.upSampleFactor) + 1;
				
				if(rowToReplace < this.outputMatrix.height() && colToReplace < this.outputMatrix.width()) {
					//Log.d(TAG, "Replaced row: " +rowToReplace+ " Col: " +colToReplace+ " PixelData R: " +pixelData[RED_INDEX]+ " G: " +pixelData[GREEN_INDEX]+ " B: " +pixelData[BLUE_INDEX]);
					this.outputMatrix.put(rowToReplace, colToReplace, pixelData);
				}
			}
		}
		
		
		return this.outputMatrix;
	}

}
