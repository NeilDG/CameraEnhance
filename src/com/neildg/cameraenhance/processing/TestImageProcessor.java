/**
 * 
 */
package com.neildg.cameraenhance.processing;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;

import com.neildg.cameraenhance.capture.ImageDataStorage;
import com.neildg.cameraenhance.thumbnail.BitmapDecoder;
import com.neildg.cameraenhance.ui.ProgressDialogHandler;

/**
 * An experiment image processing class using OPENCV
 * @author NeilDG
 *
 */
public class TestImageProcessor implements IImageProcessor {
	private final static String TAG = "CameraEnhance_TestImageProcessor";
	
	private Mat matrixImage;
	private Mat resizedMatrix;
	
	public TestImageProcessor() {
		
	}
	
	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#Preprocess()
	 */
	@Override
	public void Preprocess() {
		ProgressDialogHandler.getInstance().showDialog("Processing", "Processing image");
		
		byte[] originalImageData = ImageDataStorage.getInstance().getOriginalImageData();
		
		Bitmap bitmap = BitmapDecoder.decodeActualBitmapFromByteArray(originalImageData);
		this.matrixImage = new Mat (bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC1);
		this.resizedMatrix = new Mat(6120, 8160, CvType.CV_8UC1);
		
		Utils.bitmapToMat(bitmap, matrixImage);
		bitmap.recycle();
		
		Imgproc.resize(matrixImage, resizedMatrix, resizedMatrix.size(), 0, 0, Imgproc.INTER_LANCZOS4);
		
		//convert to byte form
		MatOfByte matOfByte = new MatOfByte();
		// encoding to png, so that your image does not lose information like with jpeg.
        Highgui.imencode(".png",this.resizedMatrix, matOfByte); 
        
		ImageDataStorage.getInstance().setProcessedImageData(matOfByte.toArray());
		
		this.matrixImage.release();
		this.resizedMatrix.release();
		
		ProgressDialogHandler.getInstance().hideDialog();
	}

	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#Process()
	 */
	@Override
	public void Process() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#PostProcess()
	 */
	@Override
	public void PostProcess() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#onPreProcessStarted()
	 */
	@Override
	public void onPreProcessStarted() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#onPreProcessFinished()
	 */
	@Override
	public void onPreProcessFinished() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#onProcessStarted()
	 */
	@Override
	public void onProcessStarted() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#onProcessFinished()
	 */
	@Override
	public void onProcessFinished() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#onPostProcessStarted()
	 */
	@Override
	public void onPostProcessStarted() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#onPostProcessFinished()
	 */
	@Override
	public void onPostProcessFinished() {
		// TODO Auto-generated method stub

	}

}
