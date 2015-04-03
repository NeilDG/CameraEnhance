package com.neildg.cameraenhance.processing.fastupsample;

import org.opencv.core.Mat;

import android.util.Log;

import com.neildg.cameraenhance.config.values.DefaultConfigValues;
import com.neildg.cameraenhance.images.ImageDataStorage;
import com.neildg.cameraenhance.processing.IImageProcessor;
import com.neildg.cameraenhance.processing.fastupsample.operators.GaussianBlur;
import com.neildg.cameraenhance.processing.fastupsample.operators.InitialUpSampler;
import com.neildg.cameraenhance.processing.fastupsample.operators.PixelSubstitution;
import com.neildg.cameraenhance.processing.fastupsample.operators.UnsharpenMask;
import com.neildg.cameraenhance.processing.fastupsample.saving.ImageSaver;
import com.neildg.cameraenhance.processing.psnr.PeakSNR;
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
	
	private InitialUpSampler upSampler;
	private GaussianBlur blurOperator;
	private UnsharpenMask unsharpMask;
	private PixelSubstitution pixelSubstitution;
	
	private ImageSaver imageSaver;
	
	private Mat upSampledMatrix;
	private Mat processingMatrix;
	
	public FastSampleProcessor() {
		
	}

	@Override
	public void Preprocess() {
		this.upSampler = new InitialUpSampler();
		this.processingMatrix = this.upSampler.perform();
		
		this.upSampledMatrix = new Mat();
		this.processingMatrix.copyTo(this.upSampledMatrix);
	}

	@Override
	public void Process() {
		
		for(int i = 0; i < MAX_ITERATIONS; i++) {
			
			ProgressDialogHandler.getInstance().showDialog("Refining", "Iteration count: " +i);
			
			//blur image
			this.blurOperator = new GaussianBlur(this.upSampledMatrix, this.processingMatrix);
			this.blurOperator.setParameters(13, 13, 1.5, 1.5);
			this.processingMatrix = this.blurOperator.perform();
			
			//save for checking
			ImageSaver tempSaver = new ImageSaver(this.processingMatrix);
			tempSaver.encodeAndSave("blurred_"+i);
			
			//deblur the image using unsharp mask
			this.unsharpMask = new UnsharpenMask(this.upSampledMatrix, this.processingMatrix);
			this.processingMatrix = this.unsharpMask.perform();
			
			//save for checking
			tempSaver = new ImageSaver(this.processingMatrix);
			tempSaver.encodeAndSave("sharpened_"+i);
			
			//perform pixel substitution
			Mat lowResMatrix = ImageDataStorage.getInstance().loadMatFormOfOriginalImage();
			this.pixelSubstitution = new PixelSubstitution(lowResMatrix, this.processingMatrix, DefaultConfigValues.UP_SAMPLE_FACTOR);
			this.processingMatrix = this.pixelSubstitution.perform();
			
			tempSaver = new ImageSaver(this.processingMatrix);
			tempSaver.encodeAndSave("pixel_replaced_"+i);
			
			/*this.upSampler.cleanup();
			this.blurOperator.cleanup();
			this.unsharpMask.cleanup();
			this.pixelSubstitution.cleanup();*/
			
			ProgressDialogHandler.getInstance().hideDialog();
		}
		
	}

	@Override
	public void PostProcess() {
		
		//Log.d(TAG, "PSNR: " +PeakSNR.getPSNR(this.blurOutputMatrix, this.sharpenedMatrix));
		
		this.imageSaver = new ImageSaver(this.processingMatrix);
		this.imageSaver.encodeAndSave();
		
		this.upSampler.cleanup();
		this.blurOperator.cleanup();
		this.unsharpMask.cleanup();
		this.pixelSubstitution.cleanup();
	}

	@Override
	public void onPreProcessStarted() {
		//ProgressDialogHandler.getInstance().showDialog("Initializing", "Upsampling image");
	}

	@Override
	public void onPreProcessFinished() {
		//ProgressDialogHandler.getInstance().hideDialog();
	}

	@Override
	public void onProcessStarted() {
		ProgressDialogHandler.getInstance().showDialog("Processing", "Refining image");
	}

	@Override
	public void onProcessFinished() {
		ProgressDialogHandler.getInstance().hideDialog();
	}

	@Override
	public void onPostProcessStarted() {
		ProgressDialogHandler.getInstance().showDialog("Post processing", "Encoding image");
	}

	@Override
	public void onPostProcessFinished() {
		ProgressDialogHandler.getInstance().hideDialog();
	}
}
