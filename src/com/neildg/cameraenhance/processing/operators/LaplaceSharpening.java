package com.neildg.cameraenhance.processing.operators;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import com.neildg.cameraenhance.processing.operators.Blur.BlurType;
import com.neildg.cameraenhance.processing.saving.ImageSaver;

/**
 * Performs sharpening via laplace operator
 * @author Patrick
 *
 */
public class LaplaceSharpening extends BaseOperator {
	private final static String TAG = "CameraEnhance_LaplaceSharpening";
	
	public LaplaceSharpening(Mat inputMatrix, Mat outputMatrix) {
		this.inputMatrix = inputMatrix;
		this.outputMatrix = outputMatrix;
		
	}
	
	@Override
	public Mat perform() {
		//convert to RGB
		//Imgproc.cvtColor(this.inputMatrix, this.outputMatrix, Imgproc.COLOR_YUV2BGR);
		
		//remove noise with gaussian
		Blur blur = new Blur(this.outputMatrix, this.outputMatrix);
		blur.setParameters(3, 3, 0, 0, BlurType.GAUSSIAN);
		blur.perform();
		
		ImageSaver.encodeAndSave(this.outputMatrix, "before_laplacian");
		
		//convert to grayscale
		Mat greyScaleMat = new Mat(this.inputMatrix.size(), this.inputMatrix.type());
		Imgproc.cvtColor(this.outputMatrix, greyScaleMat, Imgproc.COLOR_BGR2GRAY);
		
		Imgproc.Laplacian(greyScaleMat, this.outputMatrix, CvType.CV_16S, 3, 1, 0);
		Core.convertScaleAbs(this.outputMatrix, this.outputMatrix);
	
		ImageSaver.encodeAndSave(this.outputMatrix, "laplacian");
		
		Core.add(this.outputMatrix, greyScaleMat, this.outputMatrix);
		
		ImageSaver.encodeAndSave(this.outputMatrix, "sharpened");
		
		return this.outputMatrix;
	}

}
