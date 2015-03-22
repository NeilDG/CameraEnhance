/**
 * 
 */
package com.neildg.cameraenhance.thumbnail;

import com.neildg.cameraenhance.ImageViewActivity;
import com.neildg.cameraenhance.capture.CaptureCallback;
import com.neildg.cameraenhance.utils.notifications.NotificationCenter;
import com.neildg.cameraenhance.utils.notifications.NotificationListener;
import com.neildg.cameraenhance.utils.notifications.Notifications;
import com.neildg.cameraenhance.utils.notifications.Parameters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * Represents the thumbnail of a captured image from camera.
 * @author NeilDG
 *
 */
public class ThumbnailView implements NotificationListener {
	private final static String TAG = "CameraEnhance_ThumbnailView";
	
	private ImageButton thumbnailButton;
	private Activity mainActivity;
	
	public ThumbnailView(ImageButton thumbnailButton, Activity mainActivity) {
		this.thumbnailButton = thumbnailButton;
		this.mainActivity = mainActivity;
		this.initializeThumbnail();
		
		NotificationCenter.getInstance().addObserver(Notifications.ON_CREATE_THUMBNAIL, this);
	}
	
	private void initializeThumbnail() {
		this.thumbnailButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Intent viewImageIntent = new Intent(ThumbnailView.this.mainActivity, ImageViewActivity.class);
				//ThumbnailView.this.mainActivity.startActivity(viewImageIntent);
			}
		});
	}
	
	public void setOnClickListener(OnClickListener onClick) {
		this.thumbnailButton.setOnClickListener(onClick);
	}
	
	private void setThumbnail(byte[] byteArray) {
		Bitmap decodedBitmap = BitmapDecoder.decodeSampledBitmapFromByteArray(byteArray, this.thumbnailButton.getWidth(), this.thumbnailButton.getHeight());
		this.thumbnailButton.setImageBitmap(decodedBitmap);
	}

	@Override
	public void onNotify(String notificationString, Parameters params) {
		if(notificationString == Notifications.ON_CREATE_THUMBNAIL) {
			byte[] byteArray = (byte[]) params.getObjectExtra(CaptureCallback.CAPTURED_IMAGE_DATA_KEY, null);
			this.setThumbnail(byteArray);
		}
	}
}
