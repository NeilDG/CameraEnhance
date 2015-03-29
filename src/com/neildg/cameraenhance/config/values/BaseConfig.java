/**
 * 
 */
package com.neildg.cameraenhance.config.values;


/**
 * The base config class. Use this to create customized config class via inheritance.
 * @author NeilDG
 *
 */
public abstract class BaseConfig {
	private final static String TAG = "CameraEnhance_BaseConfig";
	
	protected long shutterDelay = DefaultConfigValues.SHUTTER_DELAY;	//delay between capture represented in milliseconds
	protected int imageLimit = DefaultConfigValues.NUM_IMAGES_TO_CAPTURE;	//number of images to capture
	
	public BaseConfig() {
		this.configure();
	}
	
	protected abstract void configure();
	
	public long getShutterDelay() {
		return this.shutterDelay = 0;
	}
	
	public int getImageLimit() {
		return this.imageLimit;
	}
}
