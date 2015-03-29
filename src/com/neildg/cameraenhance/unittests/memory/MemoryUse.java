package com.neildg.cameraenhance.unittests.memory;

import com.neildg.cameraenhance.utils.ApplicationCore;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.util.Log;

public class MemoryUse {
	private final static String TAG = "CameraEnhance_MemoryUse";
	
	public static long getFreeMemory() {
		MemoryInfo mi = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager) ApplicationCore.getInstance().getAppContext().getSystemService(Activity.ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		
		long availableMegs = mi.availMem / 1048576L;
		return availableMegs;
	}
	
	public static void printMemoryUse() {
		Log.d(TAG, "Available memory: " +MemoryUse.getFreeMemory());
	}

}
