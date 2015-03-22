/**
 * 
 */
package com.neildg.cameraenhance.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.neildg.cameraenhance.capture.ImageDataStorage;
import com.neildg.cameraenhance.utils.notifications.NotificationCenter;
import com.neildg.cameraenhance.utils.notifications.NotificationListener;
import com.neildg.cameraenhance.utils.notifications.Notifications;
import com.neildg.cameraenhance.utils.notifications.Parameters;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * Writes images to external directory
 * @author NeilDG
 *
 */
public class ImageWriter implements NotificationListener {
	private final static String TAG = "CameraEnhance_ImageWriter";
	
	private static ImageWriter sharedInstance = null;
	public static ImageWriter getInstance() {
		/*if(sharedInstance == null) {
			sharedInstance = new ImageWriter();
		}*/
		
		return sharedInstance;
	}
	
	public final static String ALBUM_NAME_PREFIX = "/DIGIMAP_";
	public final static String ORIGINAL_IMAGE_NAME = "original.jpg";
	public final static String PROCESSED_IMAGE_NAME = "processed.jpg";
	
	private Context context;
	private int startingAlbum = 0;
	private String proposedPath;
	
	private ImageWriter(Context context) {
		this.context = context;
	}
	
	public static void initialize(Context context) {
		sharedInstance = new ImageWriter(context);
		NotificationCenter.getInstance().addObserver(Notifications.ON_POST_PROCESS_FINISHED, sharedInstance);
		
		//also initialize image reader
		ImageReader.initialize(context);
	}
	
	public static void destroy() {
		NotificationCenter.getInstance().removeObserver(Notifications.ON_POST_PROCESS_FINISHED, sharedInstance);
		
		//also destroy image reader
		ImageReader.destroy();
	}
	
	private void createNewAlbum() {
		//identify directory index first
		while(ImageReader.getInstance().isAlbumDirExisting(this.startingAlbum)) {
			this.startingAlbum++;
		}
		
		//create path
		this.proposedPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + ALBUM_NAME_PREFIX + this.startingAlbum;
		File filePath = new File(this.proposedPath);
		filePath.mkdirs();
		
		Log.d(TAG, "Image storage is set to: " +proposedPath);
	}
	
	/*
	 * Starts writing to the specified directory
	 */
	public void startWriting() {
		this.createNewAlbum();
		
		//save original image
		try {
			File originalImageFile = new File(this.proposedPath, ORIGINAL_IMAGE_NAME);
			
			FileOutputStream fos = new FileOutputStream(originalImageFile);
			byte[] imageData = ImageDataStorage.getInstance().getOriginalImageData();
			
			if(imageData != null) {
				fos.write(imageData);
				fos.close();
			}
			
		}
		catch(IOException e) {
			Log.e(TAG, "Error writing original image: " +e.getMessage());
		}
		
		//save processed image
		try {
			File processedImageFile = new File(this.proposedPath, PROCESSED_IMAGE_NAME);
			
			FileOutputStream fos = new FileOutputStream(processedImageFile);
			byte[] imageData = ImageDataStorage.getInstance().getProcessedImageData();
			
			if(imageData != null) {
				fos.write(imageData);
				fos.close();
			}
			
		}
		catch(IOException e) {
			Log.e(TAG, "Error writing original image: " +e.getMessage());
		}
		
	}

	@Override
	public void onNotify(String notificationString, Parameters params) {
		if(notificationString == Notifications.ON_POST_PROCESS_FINISHED) {
			this.startWriting();
		}
	}
}
