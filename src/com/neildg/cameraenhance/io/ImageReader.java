/**
 * 
 */
package com.neildg.cameraenhance.io;

import java.io.File;

import android.content.Context;
import android.os.Environment;

/**
 * Reads images from external dir
 * @author NeilDG
 *
 */
public class ImageReader {
	private final static String TAG = "CameraEnhance_ImageReader";
	
	private static ImageReader sharedInstance = null;
	public static ImageReader getInstance() {
		/*if(sharedInstance == null) {
			sharedInstance = new ImageReader();
		}*/
		
		return sharedInstance;
	}
	
	private Context context;
	
	private ImageReader(Context context) {
		this.context = context;
	}
	
	public static void initialize(Context context) {
		sharedInstance = new ImageReader(context);
	}
	
	public static void destroy() {
		
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public boolean isAlbumDirExisting(int albumNumber) {
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + ImageWriter.ALBUM_NAME_PREFIX + albumNumber);
		
		return file.isDirectory() && file.exists();
	}
}
