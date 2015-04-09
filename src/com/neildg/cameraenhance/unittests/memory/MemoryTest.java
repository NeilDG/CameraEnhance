/**
 * 
 */
package com.neildg.cameraenhance.unittests.memory;

import android.util.Log;

import com.neildg.cameraenhance.config.ConfigHandler;
import com.neildg.cameraenhance.config.values.BaseConfig;
import com.neildg.cameraenhance.images.ImageDataStorage;
import com.neildg.cameraenhance.unittests.IUnitTest;

/**
 * Test for memory usage when loading image frames
 * @author NeilDG
 *
 */
public class MemoryTest implements IUnitTest {
	private final static String TAG = "CameraEnhance_MemoryTest";
	
	@Override
	public void startTest() {
		
		for(int i = 0; i < 10; i++) {
			byte[] originalImageData = ImageDataStorage.getInstance().loadOriginalImage();
			if(originalImageData != null) {
				Log.d(TAG, "Original image data length: "+originalImageData.length);
			}
			
			MemoryUse.printMemoryUse();
		}
		
		for(int i = 0; i < 10; i++) {
			byte[] processedImageData = ImageDataStorage.getInstance().loadProcessedImageData();
			if(processedImageData != null) {
				Log.d(TAG, "Processed image data length: "+processedImageData.length);
			}
			
			MemoryUse.printMemoryUse();
		}
		
		for(int i = 0; i < 10; i++) {
			BaseConfig currentConfig = ConfigHandler.getInstance().getCurrentConfig();
			
			for(int j = 0; j < currentConfig.getImageLimit(); j++) {
				byte[] imageSequence = ImageDataStorage.getInstance().loadImageSequence(j);
				if(imageSequence != null) {
					Log.d(TAG , "Image sequence length: " +imageSequence.length);
				}
				
				MemoryUse.printMemoryUse();
			}
		}
		
		ImageDataStorage.getInstance().releaseOriginalImage();
		ImageDataStorage.getInstance().releaseAllImageSequences();
		ImageDataStorage.getInstance().releaseProcessedImageData();
		MemoryUse.printMemoryUse();
		
	}

}
