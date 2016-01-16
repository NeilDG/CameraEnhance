/**
 * 
 */
package com.neildg.cameraenhance.processing;

/**
 * An interface for an image processor that is executed by a thread
 * @author NeilDG
 *
 */
public interface IImageProcessor {
	public abstract void Preprocess();
	public abstract void Process();
	public abstract void PostProcess();
	
	public abstract void onPreProcessStarted();
	public abstract void onPreProcessFinished();
	public abstract void onProcessStarted();
	public abstract void onProcessFinished();
	public abstract void onPostProcessStarted();
	public abstract void onPostProcessFinished();
}
