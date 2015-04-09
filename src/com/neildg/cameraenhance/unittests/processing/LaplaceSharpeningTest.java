package com.neildg.cameraenhance.unittests.processing;

import org.opencv.core.Mat;

import com.neildg.cameraenhance.images.ImageDataStorage;
import com.neildg.cameraenhance.processing.IImageProcessor;
import com.neildg.cameraenhance.processing.operators.LaplaceSharpening;
import com.neildg.cameraenhance.ui.ProgressDialogHandler;

/**
 * Test for laplace sharpening filter.
 * @author Patrick
 *
 */
public class LaplaceSharpeningTest implements IImageProcessor {
	private final static String TAG = "CameraEnhance_LaplaceSharpeningTest";
	
	private Mat originalMatrix;
	private Mat processingMatrix;
	
	private LaplaceSharpening laplaceSharp;
	
	@Override
	public void Preprocess() {
		this.originalMatrix = ImageDataStorage.getInstance().loadMatFormOfOriginalImage();
		this.processingMatrix = new Mat();
		this.originalMatrix.copyTo(this.processingMatrix);
	}

	@Override
	public void Process() {
		this.laplaceSharp = new LaplaceSharpening(this.processingMatrix, this.processingMatrix);
		this.processingMatrix = this.laplaceSharp.perform();
	}

	@Override
	public void PostProcess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPreProcessStarted() {
		ProgressDialogHandler.getInstance().showDialog("Laplace Test", "Processing...");
	}

	@Override
	public void onPreProcessFinished() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProcessStarted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProcessFinished() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPostProcessStarted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPostProcessFinished() {
		ProgressDialogHandler.getInstance().hideDialog();
	}

}
