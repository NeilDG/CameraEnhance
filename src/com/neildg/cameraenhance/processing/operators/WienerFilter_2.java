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
		
		Mat singleChannelMat = new Mat(this.inputMatrix.rows(), this.inputMatrix.cols(), this.inputMatrix.type());
		Imgproc.cvtColor(this.inputMatrix, singleChannelMat, Imgproc.COLOR_BGR2GRAY);
		singleChannelMat.convertTo(singleChannelMat, CvType.CV_64FC2);
		this.outputMatrix = this.getDFT(singleChannelMat);
		
		return this.outputMatrix;
	}
	
	/**
	 * Gets a visualized DFT of the input image. TODO: convert as util class
	 * @param singleChannel
	 * @return
	 */
	private Mat getDFT(Mat singleChannel) {

	    int m = Core.getOptimalDFTSize(this.inputMatrix.rows());
	    int n = Core.getOptimalDFTSize(this.inputMatrix.cols()); // on the border
	                                                    // add zero
	                                                    // values
	                                                    // Imgproc.copyMakeBorder(image1,
	                                                    // padded, 0, m -
	                                                    // image1.rows(), 0, n

	    Mat padded = new Mat(new Size(n, m), CvType.CV_64FC2); // expand input
	                                                            // image to
	                                                            // optimal size
	    
	    Imgproc.copyMakeBorder(singleChannel, padded, 0, m - singleChannel.rows(), 0,
	            n - singleChannel.cols(), Imgproc.BORDER_CONSTANT);
	    
	    List<Mat> planes = new ArrayList<Mat>();
	    planes.add(padded);
	    planes.add(Mat.zeros(padded.rows(), padded.cols(), CvType.CV_64FC1));
	    
	    for(Mat mat : planes) {
	    	Log.d(TAG, "Depth: " +mat.depth()+ " Size: " +mat.size().width +" X " +mat.size().height);
	    }

	    Mat complexI = Mat.zeros(padded.rows(), padded.cols(), CvType.CV_64FC2);

	    Mat complexI2 = Mat
	            .zeros(padded.rows(), padded.cols(), CvType.CV_64FC2);

	    Core.merge(planes, complexI); // Add to the expanded another plane with
	                                    // zeros

	    Core.dft(complexI, complexI2); // this way the result may fit in the
	                                    // source matrix

	    // compute the magnitude and switch to logarithmic scale
	    // => log(1 + sqrt(Re(DFT(I))^2 + Im(DFT(I))^2))
	    Core.split(complexI2, planes); // planes[0] = Re(DFT(I), planes[1] =
	                                    // Im(DFT(I))

	    Mat mag = new Mat(planes.get(0).size(), planes.get(0).type());

	    Core.magnitude(planes.get(0), planes.get(1), mag);// planes[0]
	                                                        // =
	                                                        // magnitude

	    Mat magI = mag;
	    Mat magI2 = new Mat(magI.size(), magI.type());
	    Mat magI3 = new Mat(magI.size(), magI.type());
	    Mat magI4 = new Mat(magI.size(), magI.type());
	    Mat magI5 = new Mat(magI.size(), magI.type());

	    Core.add(magI, Mat.ones(padded.rows(), padded.cols(), CvType.CV_64FC1),
	            magI2); // switch to logarithmic scale
	    Core.log(magI2, magI3);

	    Mat crop = new Mat(magI3, new Rect(0, 0, magI3.cols() & -2,
	            magI3.rows() & -2));

	    magI4 = crop.clone();

	    // rearrange the quadrants of Fourier image so that the origin is at the
	    // image center
	    int cx = magI4.cols() / 2;
	    int cy = magI4.rows() / 2;

	    Rect q0Rect = new Rect(0, 0, cx, cy);
	    Rect q1Rect = new Rect(cx, 0, cx, cy);
	    Rect q2Rect = new Rect(0, cy, cx, cy);
	    Rect q3Rect = new Rect(cx, cy, cx, cy);

	    Mat q0 = new Mat(magI4, q0Rect); // Top-Left - Create a ROI per quadrant
	    Mat q1 = new Mat(magI4, q1Rect); // Top-Right
	    Mat q2 = new Mat(magI4, q2Rect); // Bottom-Left
	    Mat q3 = new Mat(magI4, q3Rect); // Bottom-Right

	    Mat tmp = new Mat(); // swap quadrants (Top-Left with Bottom-Right)
	    q0.copyTo(tmp);
	    q3.copyTo(q0);
	    tmp.copyTo(q3);

	    q1.copyTo(tmp); // swap quadrant (Top-Right with Bottom-Left)
	    q2.copyTo(q1);
	    tmp.copyTo(q2);

	    Core.normalize(magI4, magI5, 0, 255, Core.NORM_MINMAX);

	    Mat realResult = new Mat(magI5.size(), CvType.CV_8UC1);

	    magI5.convertTo(realResult, CvType.CV_8UC1);

	    return realResult;
	}

}
