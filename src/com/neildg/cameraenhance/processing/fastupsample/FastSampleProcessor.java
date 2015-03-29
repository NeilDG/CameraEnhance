package com.neildg.cameraenhance.processing.fastupsample;

import android.util.Log;

import com.neildg.cameraenhance.processing.IImageProcessor;
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
	
	public FastSampleProcessor() {
		
	}

	@Override
	public void Preprocess() {
		this.upSampler = new InitialUpSampler();
	}

	@Override
	public void Process() {
		Log.d(TAG, "Initial up sampling!");
		this.upSampler.performUpSampling();
	}

	@Override
	public void PostProcess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPreProcessStarted() {
		ProgressDialogHandler.getInstance().showDialog("Processing", "Upsampling image");
	}

	@Override
	public void onPreProcessFinished() {
	}

	@Override
	public void onProcessStarted() {
		// TODO Auto-generated method stub
		
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
