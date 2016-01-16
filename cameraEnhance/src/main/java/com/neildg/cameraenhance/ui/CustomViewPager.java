package com.neildg.cameraenhance.ui;

/** Custom your own ViewPager to extends support ViewPager. java source: */
/** Created by azi on 2013-6-21.  */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends android.support.v4.view.ViewPager {

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
