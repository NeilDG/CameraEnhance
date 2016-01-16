/**
 * 
 */
package com.neildg.cameraenhance.ui;

import java.util.Locale;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * An adapter for managing sections in tabs
 * @author NeilDG
 *
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	public SectionsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch(position) {
			case 0: return new ImagePreviewFragment();
			case 1: return new ProcessedImageFragment();
			case 2: return new ToolsFragment();
			default: return null;
		}
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			return "Image Preview";
		case 1:
			return "Processed Images";
		case 2:
			return "Tools";
		}
		return null;
	}
}
