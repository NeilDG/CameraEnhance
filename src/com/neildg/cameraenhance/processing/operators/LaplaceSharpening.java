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
	
	/**
	 * Input matrix should be in RGB format. Output matrix becomes RGB format as well.
	 * @param inputMatrix
	 * @param outputMatrix
	 */
	public LaplaceSharpening(Mat inputMatrix, Mat outputMatrix) {
		this.inputMatrix = inputMatrix;
		this.outputMatrix = outputMatrix;
		
	}
	
	@Override
	public Mat perform() {
		//remove noise with gaussian
		Blur blur = new Blur(this.outputMatrix, this.outputMatrix);
		blur.setParameters(3, 3, 0, 0, BlurType.GAUSSIAN);
		blur.perform();

		//convert to grayscale
		Mat greyScaleMat = new Mat(this.inputMatrix.size(), this.inputMatrix.type());
		Mat rgbMat = new Mat(this.inputMatrix.size(), this.inputMatrix.type());
		Imgproc.cvtColor(this.outputMatrix, greyScaleMat, Imgproc.COLOR_BGR2GRAY);
		this.outputMatrix.copyTo(rgbMat);
		
		Imgproc.Laplacian(greyScaleMat, this.outputMatrix, CvType.CV_16S, 3, 1, 0);
		Core.convertScaleAbs(this.outputMatrix, this.outputMatrix);
		
		Imgproc.cvtColor(this.outputMatrix, this.outputMatrix, Imgproc.COLOR_GRAY2BGR);
		Core.addWeighted(this.outputMatrix, -0.25, rgbMat, 1.0, 0, this.outputMatrix);
		
		greyScaleMat.release(); greyScaleMat = null;
		rgbMat.release(); rgbMat = null;
		return this.outputMatrix;
	}

}
