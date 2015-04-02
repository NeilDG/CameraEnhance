package com.neildg.cameraenhance.processing.fastupsample;

import org.opencv.core.Mat;

import android.util.Log;

import com.neildg.cameraenhance.processing.IImageProcessor;
import com.neildg.cameraenhance.processing.fastupsample.operators.GaussianBlur;
import com.neildg.cameraenhance.processing.fastupsample.operators.InitialUpSampler;
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
	
	private InitialUpSampler upSampler;
	private GaussianBlur blurOperator;
	private UnsharpenMask unsharpMask;
	
	private ImageSaver imageSaver;
	
	private Mat upSampledMatrix;
	private Mat blurOutputMatrix;
	private Mat sharpenedMatrix;
	
	
	public FastSampleProcessor() {
		
	}

	@Override
	public void Preprocess() {
		this.upSampler = new InitialUpSampler();
		this.upSampledMatrix = this.upSampler.perform();
		
	}

	@Override
	public void Process() {
		//blur the image
		this.blurOperator = new GaussianBlur(this.upSampledMatrix);
		this.blurOperator.setParameters(13, 13, 1.5, 1.5);
		this.blurOutputMatrix = this.blurOperator.perform();
		
		//deblur the image using unsharp mask
		this.unsharpMask = new UnsharpenMask(this.upSampledMatrix, this.blurOutputMatrix);
		this.sharpenedMatrix = this.unsharpMask.perform();
		
		//save for debug
		ImageSaver tempSaver = new ImageSaver(this.blurOutputMatrix);
		tempSaver.encodeAndSave("blurred");
		
		tempSaver = new ImageSaver(this.sharpenedMatrix);
		tempSaver.encodeAndSave("sharpened");
	}

	@Override
	public void PostProcess() {
		
		Log.d(TAG, "PSNR: " +PeakSNR.getPSNR(this.blurOutputMatrix, this.sharpenedMatrix));
		
		this.imageSaver = new ImageSaver(this.blurOutputMatrix);
		this.imageSaver.encodeAndSave();
		
		this.upSampler.cleanup();
		this.blurOperator.cleanup();
		this.unsharpMask.cleanup();
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
		ProgressDialogHandler.getInstance().showDialog("Processing", "Blurring image");
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
