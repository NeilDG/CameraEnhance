package com.neildg.cameraenhance;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.neildg.cameraenhance.ui.SectionsPagerAdapter;

public class ImageViewActivity extends FragmentActivity implements
ActionBar.TabListener {


	
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);
		
		// Set up the action bar.
				final ActionBar actionBar = getActionBar();
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

				// Create the adapter that will return a fragment for each of the three
				// primary sections of the app.
				mSectionsPagerAdapter = new SectionsPagerAdapter(
						getSupportFragmentManager());

				// Set up the ViewPager with the sections adapter.
				mViewPager = (ViewPager) findViewById(R.id.pager);
				mViewPager.setAdapter(mSectionsPagerAdapter);

				// When swiping between different sections, select the corresponding
				// tab. We can also use ActionBar.Tab#select() to do this if we have
				// a reference to the Tab.
				mViewPager
						.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
							@Override
							public void onPageSelected(int position) {
								actionBar.setSelectedNavigationItem(position);
							}
						});
				
				this.mViewPager.setOffscreenPageLimit(3);

				// For each of the sections in the app, add a tab to the action bar.
				for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
					// Create a tab with text corresponding to the page title defined by
					// the adapter. Also specify this Activity object, which implements
					// the TabListener interface, as the callback (listener) for when
					// this tab is selected.
					actionBar.addTab(actionBar.newTab()
							.setText(mSectionsPagerAdapter.getPageTitle(i))
							.setTabListener(this));
				}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.image_view, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setTitle(CharSequence title) {
	    getActionBar().setTitle(title);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.action_image_preview: this.mViewPager.setCurrentItem(0); break;
			case R.id.action_processed_images: this.mViewPager.setCurrentItem(1); break;
			case R.id.action_settings: this.mViewPager.setCurrentItem(2); break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	

}
