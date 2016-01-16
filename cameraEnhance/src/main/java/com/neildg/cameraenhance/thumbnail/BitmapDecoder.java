
package com.neildg.cameraenhance.thumbnail;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

/**
 * Class that aids in downsampling a bitmap for thumbnail images.
 * @author NeilDG
 *
 */
public class BitmapDecoder {
	private final static String TAG = "CameraEnhance_BitmapDownSampler";
	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }
    }

    return inSampleSize;
	}
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    options.inMutable = true;
	    options.inPurgeable = true;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	public static Bitmap decodeSampledBitmapFromByteArray(byte[] byteArray, int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    options.inMutable = true;
	    options.inPurgeable = true;
	    
	    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
	}
	
	public static Bitmap decodeActualBitmapFromByteArray(byte[] byteArray) {
		 final BitmapFactory.Options options = new BitmapFactory.Options();
		 options.inJustDecodeBounds = false;
		 options.inMutable = true;
		 options.inPurgeable = true;
		 options.inPreferredConfig = Config.ARGB_8888;
		 
		 return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
	}
	
	public static Bitmap decodeReusableBitmapFromByteArray(byte[] byteArray, Bitmap targetBitmap) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inMutable = true;
		options.inBitmap = targetBitmap;
		
		return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
	}
}
