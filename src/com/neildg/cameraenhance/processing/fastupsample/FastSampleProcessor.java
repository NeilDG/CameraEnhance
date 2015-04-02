package com.neildg.cameraenhance.processing.fastupsample;

import org.opencv.core.Mat;

import android.util.Log;

import com.neildg.cameraenhance.processing.IImageProcessor;
import com.neildg.cameraenhance.processing.fastupsample.operators.GaussianBlur;
import com.neildg.cameraenhance.processing.fastupsample.operators.InitialUpSampler;
import com.neildg.cameraenhance.processing.fastupsample.saving.ImageSaver;
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
	
	private ImageSaver imageSaver;
	
	private Mat upSampledMatrix;
	private Mat blurOutputMatrix;
	
	
	public FastSampleProcessor() {
		
	}

	@Override
	public void Preprocess() {
		this.upSampler = new InitialUpSampler();
		this.upSampledMatrix = this.upSampler.perform();
		
		//this.upSampler.cleanup();
	}

	@Override
	public void Process() {
		this.blurOperator = new GaussianBlur(this.upSampledMatrix);
		this.blurOperator.setParameters(13, 13, 1.5, 1.5);
		this.blurOutputMatrix = this.blurOperator.perform();
		//this.blurOperator.cleanup();
	}

	@Override
	public void PostProcess() {
		this.imageSaver = new ImageSaver(this.blurOutputMatrix);
		this.imageSaver.encodeAndSave();
	}

	@Override
	public void onPreProcessStarted() {
		ProgressDialogHandler.getInstance().showDialog("Processing", "Upsampling image");
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPostProcessFinished() {
		// TODO Auto-generated method stub
		
	}
}
