/**
 * 
 */
package com.neildg.cameraenhance.processing.fastupsample;

import org.opencv.core.Mat;

import com.neildg.cameraenhance.config.values.DefaultConfigValues;
import com.neildg.cameraenhance.images.ImageDataStorage;
import com.neildg.cameraenhance.processing.IImageProcessor;
import com.neildg.cameraenhance.processing.operators.Denoise;
import com.neildg.cameraenhance.processing.operators.IncrementalUpSample;
import com.neildg.cameraenhance.processing.operators.PixelSubstitution;
import com.neildg.cameraenhance.processing.saving.ImageSaver;
import com.neildg.cameraenhance.ui.ProgressDialogHandler;

/**
 * Upsample with denoising technique
 * @author Patrick
 *
 */
public class UpSampleDenoising implements IImageProcessor {
	private final static String TAG = "CameraEnhance_UpsampleDenoising";
	
	private final static int MAX_ITERATIONS = 16;
	
	private Mat originalMatrix;
	private Mat processingMatrix;
	
	private IncrementalUpSample upSampler;
	private Denoise deNoise;
	private PixelSubstitution pixelSubstitution;
	
	private float upSampleFactor = (float) (DefaultConfigValues.UP_SAMPLE_FACTOR * 1.0 / MAX_ITERATIONS);
	private float upSampleIteration = upSampleFactor + 1.0f;
	
	@Override
	public void Preprocess() {
		this.originalMatrix = ImageDataStorage.getInstance().loadMatFormOfOriginalImage();
		this.processingMatrix = new Mat();
		this.originalMatrix.copyTo(this.processingMatrix);
		
		
	}
	@Override
	public void Process() {
		for(int i = 0; i < MAX_ITERATIONS; i++) {
			ProgressDialogHandler.getInstance().showDialog("Upsample denoising", "Processing. Iteration: " +i);
			
			this.upSampler = new IncrementalUpSample(this.originalMatrix, this.processingMatrix, this.upSampleIteration);
			this.processingMatrix = this.upSampler.perform();
			ImageSaver.encodeAndSave(this.processingMatrix, "upsample_" +i);
			
			this.deNoise = new Denoise(this.processingMatrix, this.processingMatrix);
			this.processingMatrix = this.deNoise.perform();
			
			ImageSaver.encodeAndSave(this.processingMatrix, "denoised_" +i);
			
			//perform pixel substitution
			//Mat lowResMatrix = ImageDataStorage.getInstance().loadMatFormOfOriginalImage();
			//this.pixelSubstitution = new PixelSubstitution(lowResMatrix, this.processingMatrix, DefaultConfigValues.UP_SAMPLE_FACTOR);
			//this.processingMatrix = this.pixelSubstitution.perform();
			//ImageSaver.encodeAndSave(this.processingMatrix, "pixel_replaced_"+i);
			
			this.upSampleIteration += this.upSampleFactor;
			ProgressDialogHandler.getInstance().hideDialog();
		}
	}
	@Override
	public void PostProcess() {
		ImageSaver.encodeAndSaveAsProcessed(this.processingMatrix);
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
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPostProcessFinished() {
		
	}
}
