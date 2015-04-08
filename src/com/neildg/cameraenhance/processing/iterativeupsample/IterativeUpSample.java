package com.neildg.cameraenhance.processing.iterativeupsample;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.util.Log;

import com.neildg.cameraenhance.config.values.DefaultConfigValues;
import com.neildg.cameraenhance.images.ImageDataStorage;
import com.neildg.cameraenhance.processing.IImageProcessor;
import com.neildg.cameraenhance.processing.operators.Denoise;
import com.neildg.cameraenhance.processing.operators.Blur;
import com.neildg.cameraenhance.processing.operators.IncrementalPixelSubstitution;
import com.neildg.cameraenhance.processing.operators.IncrementalUpSample;
import com.neildg.cameraenhance.processing.operators.UnsharpenMask;
import com.neildg.cameraenhance.processing.operators.Blur.BlurType;
import com.neildg.cameraenhance.processing.saving.ImageSaver;
import com.neildg.cameraenhance.ui.ProgressDialogHandler;

/**
 * Iterative upsampling that slowly propagates the results.
 * @author Patrick
 *
 */
public class IterativeUpSample implements IImageProcessor {
	private final static String TAG = "CameraEnhance_PixelSubstitutionTest";
	
	public final static int Y_CHANNEL = 0;
	public final static int U_CHANNEL = 1;
	public final static int V_CHANNEL = 2;
	
	private final static float UP_SAMPLE_CONSTANT = 0.5f;
	
	private Mat originalMatrix;
	private Mat yuvMatrix;
	private ArrayList<Mat> splittedMatrix;
	
	private IncrementalUpSample upSampler;
	private UnsharpenMask unSharpMask;
	private Blur blur;
	private IncrementalPixelSubstitution pixelSub;
	
	@Override
	public void Preprocess() {
		this.originalMatrix = ImageDataStorage.getInstance().loadMatFormOfOriginalImage();
		
		//convert to YUV color space
		this.yuvMatrix = new Mat();
		Imgproc.cvtColor(this.originalMatrix, this.yuvMatrix, Imgproc.COLOR_BGR2YUV);
		
		this.splittedMatrix = new ArrayList<Mat>();
		Core.split(this.yuvMatrix, this.splittedMatrix);
	}

	@Override
	public void Process() {
		float upSampleFactor = 1.0f + UP_SAMPLE_CONSTANT;
		int numIterations = (int) Math.floor((DefaultConfigValues.UP_SAMPLE_FACTOR - 1.0f) / UP_SAMPLE_CONSTANT);
		
		Log.d(TAG, "Num iterations: " +numIterations);
		
		Mat beforeUpSampleMat = new Mat();
		this.splittedMatrix.get(Y_CHANNEL).copyTo(beforeUpSampleMat);
		
		for(int i = 0; i < numIterations; i++) {
			ProgressDialogHandler.getInstance().showDialog("Pixel Substitution Test", "Processing. Iteration: " +i+ " of " +(numIterations-1));
			this.upSampler = new IncrementalUpSample(this.originalMatrix, this.yuvMatrix, upSampleFactor);
			this.yuvMatrix = this.upSampler.perform();
			
			ImageSaver.encodeAndSaveAsRGB(this.yuvMatrix, "upsample_"+i);
			
			Core.split(this.yuvMatrix, this.splittedMatrix);
			
			Mat noPixelSubMat = new Mat();
			this.splittedMatrix.get(Y_CHANNEL).copyTo(noPixelSubMat);
			
			this.pixelSub = new IncrementalPixelSubstitution(beforeUpSampleMat, this.splittedMatrix.get(Y_CHANNEL));
			this.pixelSub.perform();
			
			ImageSaver.encodeAndSave(this.splittedMatrix.get(Y_CHANNEL), "pixelsub_"+i);
			
			this.unSharpMask = new UnsharpenMask(this.splittedMatrix.get(Y_CHANNEL), noPixelSubMat);
			this.unSharpMask.perform();
			
			this.splittedMatrix.get(Y_CHANNEL).copyTo(beforeUpSampleMat);
			
			Core.merge(this.splittedMatrix, this.yuvMatrix);
			
			upSampleFactor += UP_SAMPLE_CONSTANT;
			ProgressDialogHandler.getInstance().hideDialog();
		}
		
	}

	@Override
	public void PostProcess() {
		Imgproc.cvtColor(this.yuvMatrix, this.yuvMatrix, Imgproc.COLOR_YUV2BGR);
		ImageSaver.encodeAndSaveAsProcessed(this.yuvMatrix);
		
		this.originalMatrix.release(); this.originalMatrix = null;
		this.yuvMatrix.release(); this.yuvMatrix = null;
		
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
