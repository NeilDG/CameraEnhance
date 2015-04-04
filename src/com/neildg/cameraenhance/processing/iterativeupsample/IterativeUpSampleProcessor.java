/**
 * 
 */
package com.neildg.cameraenhance.processing.iterativeupsample;

import org.opencv.core.Mat;

import android.util.Log;

import com.neildg.cameraenhance.config.values.DefaultConfigValues;
import com.neildg.cameraenhance.images.ImageDataStorage;
import com.neildg.cameraenhance.processing.IImageProcessor;
import com.neildg.cameraenhance.processing.operators.GaussianBlur;
import com.neildg.cameraenhance.processing.operators.IncrementalPixelSubstitution;
import com.neildg.cameraenhance.processing.operators.IncrementalUpSample;
import com.neildg.cameraenhance.processing.operators.PixelSubstitution;
import com.neildg.cameraenhance.processing.operators.UnsharpenMask;
import com.neildg.cameraenhance.processing.operators.UpsampleInterpolate;
import com.neildg.cameraenhance.processing.operators.WienerFilter;
import com.neildg.cameraenhance.processing.saving.ImageSaver;
import com.neildg.cameraenhance.ui.ProgressDialogHandler;

/**
 * Own implementation of iterative upsample where we slowly increase the resolution and apply filtering per iteration.
 * @author NeilDG
 *
 */
public class IterativeUpSampleProcessor implements IImageProcessor {
	private final static String TAG = "CameraEnhance_IterativeUpSampleProcessor";
	
	private final static int MAX_ITERATIONS = 3;

	//private float increasingConstant = 0.0f;
	
	private IncrementalUpSample upSampler;
	private GaussianBlur blurOperator;
	private UnsharpenMask unsharpMask;
	//private WienerFilter wienerFilter;
	private PixelSubstitution pixelSubstitution;
	
	private Mat originalMatrix;
	private Mat processingMatrix;
	
	@Override
	public void Preprocess() {
		//compute increasing constant
		//this.increasingConstant = DefaultConfigValues.UP_SAMPLE_FACTOR * 1.0f / MAX_ITERATIONS;
		//Log.d(TAG, "Increasing constant is: " +this.increasingConstant);

		this.originalMatrix = ImageDataStorage.getInstance().loadMatFormOfOriginalImage();
		this.processingMatrix = new Mat();
		this.originalMatrix.copyTo(this.processingMatrix);
	}

	@Override
	public void Process() {
		
		for(int i = 0; i < MAX_ITERATIONS; i++) {
			ProgressDialogHandler.getInstance().showDialog("Refining", "Iteration count: " +i);
			
			//upsample the image
			this.upSampler = new IncrementalUpSample(this.originalMatrix, this.processingMatrix, (i+2));
			this.processingMatrix = this.upSampler.perform();
			
			//save for checking
			ImageSaver.encodeAndSave(this.processingMatrix,"bicubic_"+i);
			
			//blur image (convolution)
			Mat unblurredMatrix = new Mat();
			this.processingMatrix.copyTo(unblurredMatrix);
			
			this.blurOperator = new GaussianBlur(this.processingMatrix, this.processingMatrix);
			this.blurOperator.setParameters(13, 13, 1.5, 1.5);
			this.processingMatrix = this.blurOperator.perform();
			
			//save for checking
			ImageSaver.encodeAndSave(this.processingMatrix, "blurred_"+i);
			
			//deblur the image (deconvolution)
			//this.wienerFilter = new WienerFilter(this.processingMatrix, this.processingMatrix);
			//this.wienerFilter.setParameters(13, 13);
			//this.processingMatrix = this.wienerFilter.perform();
			this.unsharpMask = new UnsharpenMask(unblurredMatrix, this.processingMatrix);
			this.processingMatrix = this.unsharpMask.perform();
			unblurredMatrix.release(); unblurredMatrix = null;
			
			//save for checking
			ImageSaver.encodeAndSave(this.processingMatrix, "sharpened_"+i);
			
			//perform pixel substitution
			this.pixelSubstitution = new PixelSubstitution(this.originalMatrix, this.processingMatrix, (i+2));
			this.processingMatrix = this.pixelSubstitution.perform();
			
			ImageSaver.encodeAndSave(this.processingMatrix, "pixel_replaced_"+i);
			
			ProgressDialogHandler.getInstance().hideDialog();
		}
		
		ProgressDialogHandler.getInstance().showDialog("Refining", "Final touches using deconvolution/convolution.");
		
		//blur image (convolution)
		Mat unblurredMatrix = new Mat();
		this.processingMatrix.copyTo(unblurredMatrix);
		
		this.blurOperator = new GaussianBlur(this.processingMatrix, this.processingMatrix);
		this.blurOperator.setParameters(13, 13, 1.5, 1.5);
		this.processingMatrix = this.blurOperator.perform();
		
		//save for checking
		ImageSaver.encodeAndSave(this.processingMatrix, "blurred_final");
		
		//deblur the image (deconvolution)
		this.unsharpMask = new UnsharpenMask(unblurredMatrix, this.processingMatrix);
		this.processingMatrix = this.unsharpMask.perform();
		//this.wienerFilter = new WienerFilter(this.processingMatrix, this.processingMatrix);
		//this.wienerFilter.setParameters(13, 13);
		//this.processingMatrix = this.wienerFilter.perform();
		unblurredMatrix.release(); unblurredMatrix = null;
		
		//save for checking
		ImageSaver.encodeAndSave(this.processingMatrix, "sharpened_final");
		
		ProgressDialogHandler.getInstance().hideDialog();
	}

	@Override
	public void PostProcess() {
		ImageSaver.encodeAndSaveAsProcessed(this.processingMatrix);
		
		this.blurOperator.cleanup();	this.blurOperator = null;
		this.upSampler.cleanup();	this.upSampler = null;
		this.unsharpMask.cleanup();	this.unsharpMask = null;
		//this.wienerFilter.cleanup(); this.wienerFilter = null;
		this.pixelSubstitution.cleanup(); this.pixelSubstitution = null;
	}

	@Override
	public void onPreProcessStarted() {
		
	}

	@Override
	public void onPreProcessFinished() {
		
	}


	@Override
	public void onProcessStarted() {
		
	}


	@Override
	public void onProcessFinished() {
		
	}


	@Override
	public void onPostProcessStarted() {
		
	}


	@Override
	public void onPostProcessFinished() {
		
	}

}
