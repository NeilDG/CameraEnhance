/**
 * 
 */
package com.neildg.cameraenhance.processing.operators;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.util.Log;

import com.neildg.cameraenhance.processing.psnr.PeakSNR;
import com.neildg.cameraenhance.processing.saving.ImageSaver;

/**
 * Wiener filter implementation usind DFT and based on deconvolution.py
 * https://github.com/Itseez/opencv/blob/master/samples/python2/deconvolution.py
 * @author NeilDG
 *
 */
public class WienerFilter_2 extends BaseOperator {
	private final static String TAG = "CameraEnhance_WienerFilter2";
	
	private int angle = 0;
	private int diameter = 0;
	
	public WienerFilter_2(Mat inputMatrix, Mat outputMatrix) {
		this.inputMatrix = inputMatrix;
		this.outputMatrix = outputMatrix;
	}
	
	public void setParameters(int angle, int diameter) {
		this.angle = angle;
		this.diameter = diameter;
	}
	
	private Mat createMotionKernel() {
		Mat kern = Mat.ones(1, this.diameter, this.inputMatrix.type());
		
		double cosine = Math.cos(this.angle);
		double sin = Math.sin(this.angle);
		
		Mat transMatrix = new Mat(2, 3, this.inputMatrix.type());
		transMatrix.put(0, 0, cosine);
		transMatrix.put(0, 1, -sin);
		transMatrix.put(0, 2, 0.0);
		transMatrix.put(1, 0, sin);
		transMatrix.put(1, 1, cosine);
		transMatrix.put(1, 2, 0.0);
		
		double sz2 = Math.floor(65.0 / 2);
		
		//TODO unfinished
		
		return kern;
	}
	
	private Mat createDefocusKernel() {
		Mat kern = Mat.zeros(65, 65, CvType.CV_8U); //65 is the default size of the defocus kernel
		Core.circle(kern, new Point(65,65), this.diameter, new Scalar(255,255,255,255), -1, Core.LINE_AA, 1);
		
		/*ImageSaver.encodeAndSave(kern, "circle");
		
		for(int row = 0; row < kern.rows(); row++) {
			for(int col = 0; col < kern.cols(); col++) {
				double value = kern.get(row, col)[0];
				value /= 255.0;
				kern.put(row, col, value);
			}
		}*/
		
		return kern;
	}
	
	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.operators.BaseOperator#perform()
	 */
	@Override
	public Mat perform() {
		Mat pointSpreadFunc = this.createDefocusKernel();
		ImageSaver.encodeAndSave(pointSpreadFunc, "psf");
		
		//Imgproc.cvtColor(this.inputMatrix, this.inputMatrix, Imgproc.COLOR_BGR2GRAY);
		Log.d(TAG, "Num channels of input: " +this.inputMatrix.channels());
		Log.d(TAG, "num channels of output: " +this.outputMatrix.channels());
		
		//this.inputMatrix.convertTo(this.inputMatrix, CvType.CV_64FC1);
		//this.outputMatrix.convertTo(this.outputMatrix, CvType.CV_64FC1);
		
		//Core.dft(this.inputMatrix, this.outputMatrix);
		
		ImageDFT imageDFT = new ImageDFT(this.inputMatrix);
		this.outputMatrix = imageDFT.getVisualizedDFT();
		ImageSaver.encodeAndSave(this.outputMatrix, "dft_visual");
		
		this.outputMatrix = imageDFT.perform();
		
		Scalar psfSumScalar = Core.sumElems(pointSpreadFunc);
		Log.d(TAG, "Sum elems: " +psfSumScalar.val[0] + " " +psfSumScalar.val[1]);
		
		Mat psfPad = Mat.zeros(this.inputMatrix.rows(), this.inputMatrix.cols(), CvType.CV_64FC2);
		int kh = psfPad.rows() - 1;
		int kw = psfPad.cols() - 1;
		for(int row = 0; row < psfPad.rows(); row++) {
			for(int col = 0; col < psfPad.cols(); col++) {
				psfPad.put(row, col, psfSumScalar.val[0], psfSumScalar.val[1]);
			}
		}
		//psfPad.put(kh, kw, psfSumScalar.val[0], psfSumScalar.val[1]);
		
		Mat PSF = new Mat();
		Mat PSF2 = new Mat();
		Core.dft(psfPad, PSF, Core.DFT_COMPLEX_OUTPUT, kh);
		Log.d(TAG, "Sample PSF: " +PSF.get(kh, kw)[0]);
		
		Scalar twoScalar = new Scalar(2,2);
		Core.multiply(PSF, twoScalar, PSF2);
		double PSF2SumElems = Core.sumElems(PSF2).val[0];
		double PSF2AndNoise = PSF2SumElems + 20.12;
		Log.d(TAG, "Sample PSF after scalar: " +PSF2.get(kh, kw)[0]);
		
		Mat iPSF = new Mat();
		Core.divide(PSF2AndNoise, PSF, iPSF);
		Log.d(TAG, "iPSF: " +iPSF.get(kh, kw)[0]);
		
		Mat RES = new Mat();
		Core.mulSpectrums(this.outputMatrix, iPSF, RES, Core.DFT_COMPLEX_OUTPUT);
		Core.idft(RES, this.outputMatrix, Core.DFT_REAL_OUTPUT, 0);
		

		return this.outputMatrix;
	}
	
	

}
