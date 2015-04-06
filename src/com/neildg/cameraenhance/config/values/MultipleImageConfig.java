/**
 * 
 */
package com.neildg.cameraenhance.config.values;


/**
 * Implemention of config for multiple image processing
 * @author NeilDG
 *
 */
public class MultipleImageConfig extends BaseConfig {
	
	@Override
	protected void configure() {
		this.imageLimit = 10;
		this.shutterDelay = 100;
		
		this.defaultWidth = 300;
		this.defaultHeight = 300;
	}
	
}
