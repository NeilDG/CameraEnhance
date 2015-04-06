package com.neildg.cameraenhance.unittests.processing;

import org.opencv.core.Mat;

import com.neildg.cameraenhance.images.ImageDataStorage;
import com.neildg.cameraenhance.processing.IImageProcessor;
import com.neildg.cameraenhance.processing.operators.Denoise;
import com.neildg.cameraenhance.processing.operators.IncrementalUpSample;
import com.neildg.cameraenhance.processing.operators.UpsampleInterpolate;
import com.neildg.cameraenhance.processing.saving.ImageSaver;
import com.neildg.cameraenhance.ui.ProgressDialogHandler;

/**
 * Testing for denoising component
 * @author Patrick
 *
 */
public class DenoisingTest implements IImageProcessor {
	private final static String TAG  = "CameraEnhance_DenoisingTest";
	
	private Mat originalMatrix;
	private Mat processingMatrix;
	
	private IncrementalUpSample upSampler;
	private Denoise deNoise;
	
	@Override
	public void Preprocess() {
		this.originalMatrix = ImageDataStorage.getInstance().loadMatFormOfOriginalImage();
		this.processingMatrix = new Mat();
		this.originalMatrix.copyTo(this.processingMatrix);
	}

	@Override
	public void Process() {
		this.upSampler = new IncrementalUpSample(this.originalMatrix, this.processingMatrix, 4);
		this.processingMatrix = this.upSampler.perform();
		
		ImageSaver.encodeAndSave(this.processingMatrix, "upsample");
		
		this.deNoise = new Denoise(this.processingMatrix, this.processingMatrix);
		this.processingMatrix = this.deNoise.perform();
		
		ImageSaver.encodeAndSave(this.processingMatrix, "denoised");
		
		
	}

	@Override
	public void PostProcess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPreProcessStarted() {
		ProgressDialogHandler.getInstance().showDialog("Denoising Test", "Processing");
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
