/**
 * 
 */
package com.neildg.cameraenhance.processing.operators;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.util.Log;

/**
 * An implementation of a Wiener filter based from
 * https://www.ocf.berkeley.edu/~rayver/mm/code/cvWiener2/
 * @author NeilDG
 *
 */
public class WienerFilter extends BaseOperator {
	private final static String TAG = "CameraEnhance_WienerFilter";
	
	private Mat kernel;
	private int depth;
	
	public WienerFilter(Mat inputMatrix, Mat outputMatrix) {
		this.inputMatrix = inputMatrix;
		this.outputMatrix = outputMatrix;	
		
		this.inputMatrix.convertTo(this.inputMatrix, CvType.CV_32F);
		this.outputMatrix.convertTo(this.outputMatrix, CvType.CV_32F);
		this.depth = this.inputMatrix.depth();
	}
	
	public void setParameters(int kernelWidth, int kernelHeight) {
		this.kernel = new Mat(kernelWidth, kernelHeight, this.inputMatrix.type(), Scalar.all( 1.0 / (double) (kernelWidth * kernelHeight)));
	}


	@Override
	public Mat perform() {
		if(this.kernel == null) {
			Log.e(TAG, "Kernel not set! You must call setParameters()!");
			return null;
		}
		
		int nRows = this.kernel.height();
		int nCols = this.kernel.width();
		Scalar noisePower = null;
		
		//allocate tmp matrices
		Mat tmpMat1 = new Mat(this.inputMatrix.width(), this.inputMatrix.height(), this.inputMatrix.type());
		Mat tmpMat2 = new Mat(this.inputMatrix.width(), this.inputMatrix.height(), this.inputMatrix.type());
		Mat tmpMat3 = new Mat(this.inputMatrix.width(), this.inputMatrix.height(), this.inputMatrix.type());
		Mat tmpMat4 = new Mat(this.inputMatrix.width(), this.inputMatrix.height(), this.inputMatrix.type());
		
		Point point1 = new Point(nCols/2 ,nRows/2);
		
		//Local mean of input
		//cvFilter2D( srcMat, p_tmpMat1, p_kernel, cvPoint(nCols/2, nRows/2)); //localMean
		Imgproc.filter2D(this.inputMatrix, tmpMat1, this.depth, this.kernel, point1, 0.0);
		
		//Local variance of input
		//cvMul( srcMat, srcMat, p_tmpMat2);	//in^2
		//cvFilter2D( p_tmpMat2, p_tmpMat3, p_kernel, cvPoint(nCols/2, nRows/2));
		Core.multiply(this.inputMatrix, this.inputMatrix, tmpMat2);
		Imgproc.filter2D(tmpMat2, tmpMat3, this.depth, this.kernel, point1, 0.0);
		
		//Subtract off local_mean^2 from local variance
		//cvMul( p_tmpMat1, p_tmpMat1, p_tmpMat4 ); //localMean^2
		//cvSub( p_tmpMat3, p_tmpMat4, p_tmpMat3 ); //filter(in^2) - localMean^2 ==> localVariance
		Core.multiply(tmpMat1, tmpMat1, tmpMat4);
		Core.subtract(tmpMat3, tmpMat4, tmpMat3);
		
		//Estimate noise power
		noisePower = Core.mean(tmpMat3);
		
		// result = local_mean  + ( max(0, localVar - noise) ./ max(localVar, noise)) .* (in - local_mean)
		/*cvSub ( srcMat, p_tmpMat1, dstArr);		     //in - local_mean
		cvMaxS( p_tmpMat3, noise_power, p_tmpMat2 ); //max(localVar, noise)

		cvAddS( p_tmpMat3, cvScalar(-noise_power), p_tmpMat3 ); //localVar - noise
		cvMaxS( p_tmpMat3, 0, p_tmpMat3 ); // max(0, localVar - noise)

		cvDiv ( p_tmpMat3, p_tmpMat2, p_tmpMat3 );  //max(0, localVar-noise) / max(localVar, noise)

		cvMul ( p_tmpMat3, dstArr, dstArr );
		cvAdd ( dstArr, p_tmpMat1, dstArr );*/
		
		Core.subtract(this.inputMatrix, tmpMat1, this.outputMatrix);
		Core.max(tmpMat3, noisePower, tmpMat2);
		
		Scalar negativeNoisePower = new Scalar(-noisePower.val[0], -noisePower.val[1], -noisePower.val[2], -noisePower.val[3]);
		Core.add(tmpMat3, negativeNoisePower, tmpMat3);
		Core.max(tmpMat3, Mat.zeros(tmpMat3.size(), tmpMat3.type()), tmpMat3);
		
		Core.divide(tmpMat3, tmpMat2, tmpMat3);
		
		Core.multiply(tmpMat3, this.outputMatrix, this.outputMatrix);
		Core.add(this.outputMatrix, tmpMat1, this.outputMatrix);
		
		tmpMat1.release(); tmpMat1 = null;
		tmpMat2.release(); tmpMat2 = null;
		tmpMat3.release(); tmpMat3 = null;
		tmpMat4.release(); tmpMat4 = null;
	
		return this.outputMatrix;
	}

}
