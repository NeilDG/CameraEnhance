package com.neildg.cameraenhance.processing.iterativeupsample;

import org.opencv.core.Mat;

import android.util.Log;

import com.neildg.cameraenhance.config.values.DefaultConfigValues;
import com.neildg.cameraenhance.images.ImageDataStorage;
import com.neildg.cameraenhance.processing.IImageProcessor;
import com.neildg.cameraenhance.processing.operators.Denoise;
import com.neildg.cameraenhance.processing.operators.GaussianBlur;
import com.neildg.cameraenhance.processing.operators.IncrementalPixelSubstitution;
import com.neildg.cameraenhance.processing.operators.IncrementalUpSample;
import com.neildg.cameraenhance.processing.operators.UnsharpenMask;
import com.neildg.cameraenhance.processing.saving.ImageSaver;
import com.neildg.cameraenhance.ui.ProgressDialogHandler;

/**
 * Iterative upsampling that slowly propagates the results.
 * @author Patrick
 *
 */
public class IterativeUpSample implements IImageProcessor {
	private final static String TAG = "CameraEnhance_PixelSubstitutionTest";
	
	private final static float UP_SAMPLE_CONSTANT = 0.25f;
	
	private Mat originalMatrix;
	private Mat processingMatrix;
	
	private IncrementalUpSample upSampler;
	private UnsharpenMask unSharpMask;
	private Denoise denoise;
	private IncrementalPixelSubstitution pixelSub;
	
	@Override
	public void Preprocess() {
		this.originalMatrix = ImageDataStorage.getInstance().loadMatFormOfOriginalImage();
		this.processingMatrix = new Mat();
		this.originalMatrix.copyTo(this.processingMatrix);
	}

	@Override
	public void Process() {
		float upSampleFactor = 1.0f + UP_SAMPLE_CONSTANT;
		int numIterations = (int) Math.floor((DefaultConfigValues.UP_SAMPLE_FACTOR - 1.0f) / UP_SAMPLE_CONSTANT);
		
		Log.d(TAG, "Num iterations: " +numIterations);
		
		Mat beforeUpSampleMat = new Mat();
		this.originalMatrix.copyTo(beforeUpSampleMat);
		
		for(int i = 0; i < numIterations; i++) {
			ProgressDialogHandler.getInstance().showDialog("Pixel Substitution Test", "Processing. Iteration: " +i);
			this.upSampler = new IncrementalUpSample(this.originalMatrix, this.processingMatrix, upSampleFactor);
			this.processingMatrix = this.upSampler.perform();
			
			ImageSaver.encodeAndSave(this.processingMatrix, "upsample_"+i);
			
			Mat noPixelSubMat = new Mat();
			this.processingMatrix.copyTo(noPixelSubMat);
			
			this.pixelSub = new IncrementalPixelSubstitution(beforeUpSampleMat, this.processingMatrix);
			this.processingMatrix = this.pixelSub.perform();
			
			ImageSaver.encodeAndSave(this.processingMatrix, "pixelsub_"+i);
			
			
			this.unSharpMask = new UnsharpenMask(this.processingMatrix, noPixelSubMat);
			this.processingMatrix = this.unSharpMask.perform();
			this.processingMatrix.copyTo(beforeUpSampleMat);
			//noPixelSubMat.release(); noPixelSubMat = null;
			
			upSampleFactor += UP_SAMPLE_CONSTANT;
			ProgressDialogHandler.getInstance().hideDialog();
		}
		
	}

	@Override
	public void PostProcess() {

		ImageSaver.encodeAndSaveAsProcessed(this.processingMatrix);
		
		this.originalMatrix.release(); this.originalMatrix = null;
		this.processingMatrix.release(); this.processingMatrix = null;
		
		this.pixelSub.cleanup(); this.pixelSub = null;
		this.unSharpMask.cleanup(); this.unSharpMask = null;
		this.upSampler.cleanup(); this.upSampler = null;
	}

	@Override
	public void onPreProcessStarted() {
		
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
	}

	@Override
	public void onPostProcessFinished() {
		
	}

}
