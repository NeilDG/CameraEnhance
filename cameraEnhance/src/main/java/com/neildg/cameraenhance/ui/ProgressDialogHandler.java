/**
 * 
 */
package com.neildg.cameraenhance.ui;

import com.neildg.cameraenhance.utils.notifications.NotificationCenter;
import com.neildg.cameraenhance.utils.notifications.NotificationListener;
import com.neildg.cameraenhance.utils.notifications.Parameters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

/**
 * Takes care of showing the progress dialog to the user
 * @author NeilDG
 *
 */
public class ProgressDialogHandler {
	public final static String TAG = "CameraEnhance_ProgressDialogHandler";
	
	private static ProgressDialogHandler sharedInstance = null;
	public static ProgressDialogHandler getInstance() {
		if(sharedInstance == null) {
			Log.e(TAG, "Progress dialog not yet initialized!");
		}
		
		return sharedInstance;
	}
	
	private ProgressDialog progressDialog;
	private Activity activity;
	
	public ProgressDialogHandler(Activity activity) {
		this.activity = activity;
	}
	
	public static void initialize(Activity activity) {
		sharedInstance = new ProgressDialogHandler(activity);
	}
	
	public static void destroy() {
		if(sharedInstance.progressDialog != null) {
			sharedInstance.progressDialog.dismiss();
			sharedInstance.progressDialog = null;
		}
		sharedInstance = null;
	}
	
	public void showDialog(final String title, final String message) {
		this.activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				sharedInstance.progressDialog = ProgressDialog.show(sharedInstance.activity, title, message);
				sharedInstance.progressDialog.setCancelable(false);
			}
		});
		
	}
	
	public void hideDialog() {
		if(this.progressDialog != null) {
			this.progressDialog.dismiss();
		}
	}
}
