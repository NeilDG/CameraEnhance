/**
 * 
 */
package com.neildg.cameraenhance.unittests.processing;

import org.opencv.core.Mat;

import com.neildg.cameraenhance.images.ImageDataStorage;
import com.neildg.cameraenhance.processing.IImageProcessor;
import com.neildg.cameraenhance.processing.operators.GaussianBlur;
import com.neildg.cameraenhance.processing.operators.WienerFilter;
import com.neildg.cameraenhance.processing.operators.WienerFilter_2;
import com.neildg.cameraenhance.processing.saving.ImageSaver;
import com.neildg.cameraenhance.ui.ProgressDialogHandler;

/**
 * Testing for wiener filter module. Given a blurry image, it should output a nicer image.
 * @author NeilDG
 *
 */
public class WienerFilterTest implements IImageProcessor {
	private final static String TAG = "CameraEnhance_WienerFilterTest";
	
	private Mat originalMatrix;
	private Mat processingMatrix;
	
	private GaussianBlur blurOperator;
	private WienerFilter_2 wienerFilter;
	
	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#Preprocess()
	 */
	@Override
	public void Preprocess() {
		this.originalMatrix = ImageDataStorage.getInstance().loadMatFormOfOriginalImage();
		this.processingMatrix = new Mat();
		
		//create a blur sample
		this.blurOperator = new GaussianBlur(this.originalMatrix, this.processingMatrix);
		this.blurOperator.setParameters(13, 13, 1.5, 1.5);
		this.processingMatrix = this.blurOperator.perform();
		
		ImageSaver.encodeAndSave(this.processingMatrix, "blurred");
	}

	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#Process()
	 */
	@Override
	public void Process() {
		this.wienerFilter = new WienerFilter_2(this.processingMatrix, this.processingMatrix);
		this.wienerFilter.setParameters(0, 13);
		this.processingMatrix = this.wienerFilter.perform();
		
		ImageSaver.encodeAndSave(this.processingMatrix, "dft_13");
		
	}

	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#PostProcess()
	 */
	@Override
	public void PostProcess() {
		this.blurOperator.cleanup();
		this.wienerFilter.cleanup();
	}

	/* (non-Javadoc)
	 * @see com.neildg.cameraenhance.processing.IImageProcessor#onPreProcessStarted()
	 */
	@Override
	public void onPreProcessStarted() {
		ProgressDialogHandler.getInstance().showDialog("Processing", "Testing wiener filter.");
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
		ProgressDialogHandler.getInstance().hideDialog();
	}

}
