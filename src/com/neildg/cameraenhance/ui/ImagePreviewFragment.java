package com.neildg.cameraenhance.ui;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.neildg.cameraenhance.R;
import com.neildg.cameraenhance.capture.ImageSequencesHolder;
import com.neildg.cameraenhance.photoview.PhotoViewAttacher;
import com.neildg.cameraenhance.processing.TestImageProcessor;
import com.neildg.cameraenhance.processing.workers.ImageWorker;
import com.neildg.cameraenhance.thumbnail.BitmapDecoder;
import com.neildg.cameraenhance.utils.notifications.NotificationCenter;
import com.neildg.cameraenhance.utils.notifications.NotificationListener;
import com.neildg.cameraenhance.utils.notifications.Notifications;
import com.neildg.cameraenhance.utils.notifications.Parameters;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class ImagePreviewFragment extends Fragment {

	private View parentView;
	private ImageView imageView;
	private PhotoViewAttacher photoAttacher;
	
	private ProgressDialog progressDialog;
	
	public ImagePreviewFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		this.parentView =  inflater.inflate(R.layout.fragment_image_preview, container, false);
		
		//this.initializeImageView();
		//this.startProcessingOfImage();
		
		return this.parentView;
	}
	
	private void initializeImageView() {
		this.imageView = (ImageView) this.parentView.findViewById(R.id.processed_image_view);
		
		Bitmap bitmap = BitmapDecoder.decodeActualBitmapFromByteArray(ImageSequencesHolder.getInstance().getOriginalImageData());
		this.imageView.setImageBitmap(bitmap);
		
		this.photoAttacher = new PhotoViewAttacher(this.imageView);
		this.photoAttacher.update();
	}
	
	private void startProcessingOfImage() {
		Bitmap oldBitmap = ((BitmapDrawable) this.imageView.getDrawable()).getBitmap();
		oldBitmap.recycle();
		oldBitmap = null;
		this.imageView.setImageBitmap(null);
		
		ImageWorker imageWorker = new ImageWorker();
		imageWorker.attachImageProcessor(new TestImageProcessor());
		imageWorker.start();
	}
	
	private void displayProcessedImage() {
		
		this.getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Bitmap bitmap = BitmapDecoder.decodeActualBitmapFromByteArray(ImageSequencesHolder.getInstance().getProcessedImageData());
				
				ImagePreviewFragment.this.imageView.setImageBitmap(bitmap);
				ImagePreviewFragment.this.photoAttacher.update();
			}
		});

	}
	
	private void showProgressDialog(final String title, final String message) {
		this.getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				ImagePreviewFragment.this.progressDialog = ProgressDialog.show(getActivity(), title, message);
				ImagePreviewFragment.this.progressDialog.setCancelable(false);
			}
		});
		
	}
	
	private void hideProgressDialog() {
		this.getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(ImagePreviewFragment.this.progressDialog != null) {
					ImagePreviewFragment.this.progressDialog.hide();
					ImagePreviewFragment.this.progressDialog = null;
				}
			}
		});
		
		
	}

}
