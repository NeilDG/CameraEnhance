/**
 * 
 */
package com.neildg.cameraenhance.processing.operators;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import android.util.Log;

/**
 * Incremental version of pixel substitution
 * @author NeilDG
 *
 */
public class IncrementalPixelSubstitution extends PixelSubstitution {
	private final static String TAG = "CameraEnhance_IncrementalPixelSubstitution";
	
	private float heightScale = 0.0f;
	private float widthScale = 0.0f;
	
	public IncrementalPixelSubstitution(Mat beforeMatrix, Mat outputMatrix) {
		super(beforeMatrix, outputMatrix, 0);
		
		//compute difference in resolution
		this.heightScale = this.outputMatrix.height() * 1.0f / this.inputMatrix.height();
		this.widthScale = this.outputMatrix.width() * 1.0f / this.inputMatrix.width();
		
		Log.d(TAG, "Width scale: " +this.widthScale+ " Height scale: " +this.heightScale);
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
				
				float rowFloat = row * this.heightScale;
				float colFloat = col * this.widthScale;
				
				float rowRemainder = rowFloat % 1;
				float colRemainder = colFloat % 1;
				
				int rowToReplace =(int) Math.floor(rowFloat);
				int colToReplace =(int) Math.floor(colFloat);
				
				if(rowRemainder == 0.0f && colRemainder == 0.0f && rowToReplace < this.outputMatrix.height() && colToReplace < this.outputMatrix.width()) {
					Log.d(TAG, "Replaced row: " +rowToReplace+ " Col: " +colToReplace);
					this.outputMatrix.put(rowToReplace, colToReplace, pixelData);
				}
			}
		}
		
		
		return this.outputMatrix;
	}
}
