package com.neildg.cameraenhance.ui;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomDrawerLayout extends DrawerLayout {

	public CustomDrawerLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public CustomDrawerLayout(Context context, AttributeSet attrs) {
		super(context,attrs);
	}
	
	public CustomDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
	    // TODO Auto-generated method stub
	    try {
	        return super.onInterceptTouchEvent(arg0);
	    } catch (Exception e) {
	        // TODO Auto-generated catch block

	        e.printStackTrace();
	        return false;
	    }
	}

}
