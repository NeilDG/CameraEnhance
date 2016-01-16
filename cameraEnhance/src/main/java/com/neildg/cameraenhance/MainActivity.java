package com.neildg.cameraenhance;

import java.io.File;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.neildg.cameraenhance.camera.CameraManager;
import com.neildg.cameraenhance.camera.CameraPreview;
import com.neildg.cameraenhance.camera.DrawingView;
import com.neildg.cameraenhance.io.ImageWriter;
import com.neildg.cameraenhance.processing.ProcessorDispatcher;
import com.neildg.cameraenhance.processing.TestImageProcessor;
import com.neildg.cameraenhance.thumbnail.ThumbnailView;
import com.neildg.cameraenhance.ui.DialogImageChooser;
import com.neildg.cameraenhance.ui.ProgressDialogHandler;
import com.neildg.cameraenhance.unittests.TestExecutioner;
import com.neildg.cameraenhance.utils.ApplicationCore;


public class MainActivity extends Activity implements DialogImageChooser.NoticeDialogListener{

	private final static String TAG = "CameraEnhance_MainActivity";
	
	private CameraPreview cameraPreview;
	private ThumbnailView thumbnailView;
	
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
	    @Override
	    public void onManagerConnected(int status) {
	        switch (status) {
	            case LoaderCallbackInterface.SUCCESS:
	            {
	                Log.i(TAG, "OpenCV loaded successfully");
	            } break;
	            default:
	            	
	            {
	                super.onManagerConnected(status);
	            } break;
	        }
	    }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		ApplicationCore.initialize(this);
		CameraManager.initialize();
		
		this.initializeButtons();
	}
	
	private void initializeButtons() {
		ImageButton rotateCameraBtn = (ImageButton) this.findViewById(R.id.btn_rotate);
		rotateCameraBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Camera deviceCamera = CameraManager.getInstance().swapCamera();
				MainActivity.this.cameraPreview.updateCameraSource(deviceCamera);
			}
		});
		
		ImageButton captureBtn = (ImageButton) this.findViewById(R.id.btn_capture);
		captureBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CameraManager.getInstance().capture();
			}
		});
		
		ImageButton optionsBtn = (ImageButton) this.findViewById(R.id.btn_options);
		optionsBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent viewImageIntent = new Intent(MainActivity.this, ImageViewActivity.class);
				startActivity(viewImageIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public void onResume() {
	    super.onResume();
	    OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);
	    ProgressDialogHandler.initialize(this);
	    ProcessorDispatcher.initialize();
	    ImageWriter.initialize(this);
	    
	    Camera deviceCamera = CameraManager.getInstance().requestCamera();
		this.cameraPreview = (CameraPreview) this.findViewById(R.id.camera_surface_view);
		this.cameraPreview.assignCamera(deviceCamera);
		CameraManager.getInstance().setCameraPreview(this.cameraPreview);
		
		DrawingView drawingView = (DrawingView) this.findViewById(R.id.camera_drawing_view);
		this.cameraPreview.assignDrawingView(drawingView);
		
		ImageButton thumbnailButton = (ImageButton) this.findViewById(R.id.btn_image_preview);
		this.thumbnailView = new ThumbnailView(thumbnailButton, this);
		this.thumbnailView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogImageChooser imageChooser = new DialogImageChooser();
				imageChooser.show(MainActivity.this.getFragmentManager(), "Image Chooser");
			}
		});
		
		ImageButton optionsBtn = (ImageButton) this.findViewById(R.id.btn_options);
		optionsBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TestExecutioner.getInstance().startMemoryTest();
			}
		});
	    
		//for unit testing
		//TestExecutioner.getInstance().startTestSeries();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		CameraManager.getInstance().closeCamera();
		ProgressDialogHandler.destroy();
		ProcessorDispatcher.destroy();
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		ImageWriter.destroy();
	}

	@Override
	public void onDialogPositionClick(DialogImageChooser dialog, int position) {
		Log.d(TAG, "Click on " +position);
		if(position == 0) {
			//use intent to view original image
			File imageFile = new File(ImageWriter.getInstance().getFilePath() + "/" + ImageWriter.ORIGINAL_IMAGE_NAME);
			if(imageFile.exists()) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setDataAndType(Uri.fromFile(imageFile), "image/jpeg");
				startActivity(i);
			}
		}
		else if(position == 1) {
			//use intent to view processed image
			File imageFile = new File(ImageWriter.getInstance().getFilePath() + "/" + ImageWriter.PROCESSED_IMAGE_NAME);
			if(imageFile.exists()) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setDataAndType(Uri.fromFile(imageFile), "image/jpeg");
				startActivity(i);
			}
		}
	}

}
