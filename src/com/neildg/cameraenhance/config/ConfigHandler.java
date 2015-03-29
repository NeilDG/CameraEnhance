/**
 * 
 */
package com.neildg.cameraenhance.config;

import android.util.Log;

import com.neildg.cameraenhance.config.values.BaseConfig;
import com.neildg.cameraenhance.config.values.MultipleImageConfig;

/**
 * Class that loads the specified config value
 * @author NeilDG
 *
 */
public class ConfigHandler {
	private final static String TAG = "CameraEnhance_ConfigHandler";
	
	private static ConfigHandler sharedInstance = null;
	public static ConfigHandler getInstance() {
		if(sharedInstance == null) {
			sharedInstance = new ConfigHandler();
		}
		
		return sharedInstance;
	}
	
	private BaseConfig assignedConfig;
	
	private ConfigHandler() {
		//change the configuration here
		this.assignedConfig = new MultipleImageConfig();
		
		Log.d(TAG, "Initialized config. Image limit:  "+this.assignedConfig.getImageLimit()+ " Shutter delay:" +this.assignedConfig.getShutterDelay());
	}
	
	public BaseConfig getCurrentConfig() {
		return this.assignedConfig;
	}
}
