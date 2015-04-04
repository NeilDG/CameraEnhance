package com.neildg.cameraenhance.processing.fastupsample;

import org.opencv.core.Mat;

import android.util.Log;

import com.neildg.cameraenhance.config.values.DefaultConfigValues;
import com.neildg.cameraenhance.images.ImageDataStorage;
import com.neildg.cameraenhance.processing.IImageProcessor;
import com.neildg.cameraenhance.processing.operators.GaussianBlur;
import com.neildg.cameraenhance.processing.operators.PixelSubstitution;
import com.neildg.cameraenhance.processing.operators.UnsharpenMask;
import com.neildg.cameraenhance.processing.operators.UpsampleInterpolate;
import com.neildg.cameraenhance.processing.psnr.PeakSNR;
import com.neildg.cameraenhance.processing.saving.ImageSaver;
import com.neildg.cameraenhance.ui.ProgressDialogHandler;

/**
 * Base class for the fast upsampling image processor in
 * Shan, Q., Li, Z., Jia, J., Tang, C. 2008. Fast Image/Video Upsampling.
 * 
 * @author NeilDG
 */
public class FastSampleProcessor implements IImageProcessor {
	private final static String TAG = "CameraEnhance_FastSampleProcessor";
	
	private final static int MAX_ITERATIONS = 4;
	
	private UpsampleInterpolate upSampler;
	private GaussianBlur blurOperator;
	private UnsharpenMask unsharpMask;
	//private WienerFilter wienerFilter;
	private PixelSubstitution pixelSubstitution;
	
	private Mat upSampledMatrix;
	private Mat processingMatrix;
	
	public FastSampleProcessor() {
		
	}

	@Override
	public void Preprocess() {
		this.upSampler = new UpsampleInterpolate(DefaultConfigValues.UP_SAMPLE_FACTOR);
		this.upSampledMatrix = this.upSampler.perform();
		this.processingMatrix = new Mat();
		this.upSampledMatrix.copyTo(this.processingMatrix);
		
		//save for checking
		ImageSaver.encodeAndSave(this.upSampledMatrix, "bicubic");
	}

	@Override
	public void Process() {
		
		for(int i = 0; i < MAX_ITERATIONS; i++) {
			
			ProgressDialogHandler.getInstance().showDialog("Refining", "Iteration count: " +i);
			
			//blur image (convolution)
			this.blurOperator = new GaussianBlur(this.processingMatrix, this.processingMatrix);
			this.blurOperator.setParameters(13, 13, 1.5, 1.5);
			this.processingMatrix = this.blurOperator.perform();
			
			//save for checking
			ImageSaver.encodeAndSave(this.processingMatrix, "blurred_"+i);
			
			//deblur the image (deconvolution)
			this.unsharpMask = new UnsharpenMask(this.upSampledMatrix, this.processingMatrix);
			this.processingMatrix = this.unsharpMask.perform();
			//this.wienerFilter = new WienerFilter(this.processingMatrix, this.processingMatrix);
			//this.wienerFilter.setParameters(13, 13);
			//this.processingMatrix = this.wienerFilter.perform();
			
			//save for checking
			ImageSaver.encodeAndSave(this.processingMatrix, "sharpened_"+i);
			
			if(i == MAX_ITERATIONS - 1) {
				ProgressDialogHandler.getInstance().hideDialog();
				break;
			}
			
			//blur again
			this.blurOperator = new GaussianBlur(this.processingMatrix, this.processingMatrix);
			this.blurOperator.setParameters(13, 13, 1.5, 1.5);
			this.processingMatrix = this.blurOperator.perform();
			
			//save for checking
			ImageSaver.encodeAndSave(this.processingMatrix, "reblurred_"+i);
			
			//perform pixel substitution
			Mat lowResMatrix = ImageDataStorage.getInstance().loadMatFormOfOriginalImage();
			this.pixelSubstitution = new PixelSubstitution(lowResMatrix, this.processingMatrix, DefaultConfigValues.UP_SAMPLE_FACTOR);
			this.processingMatrix = this.pixelSubstitution.perform();
			
			ImageSaver.encodeAndSave(this.processingMatrix, "pixel_replaced_"+i);
			
			/*this.upSampler.cleanup();
			this.blurOperator.cleanup();
			this.unsharpMask.cleanup();
			this.pixelSubstitution.cleanup();*/
			
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
		
		//Log.d(TAG, "PSNR: " +PeakSNR.getPSNR(this.blurOutputMatrix, this.sharpenedMatrix));
		
		ImageSaver.encodeAndSaveAsProcessed(this.processingMatrix);
		
		this.upSampler.cleanup();
		this.blurOperator.cleanup();
		this.unsharpMask.cleanup();
		//this.wienerFilter.cleanup();
		this.pixelSubstitution.cleanup();
	}

	@Override
	public void onPreProcessStarted() {
		ProgressDialogHandler.getInstance().showDialog("Initializing", "Upsampling image");
	}

	@Override
	public void onPreProcessFinished() {
		ProgressDialogHandler.getInstance().hideDialog();
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
		ProgressDialogHandler.getInstance().hideDialog();
	}
}
