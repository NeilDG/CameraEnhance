package com.neildg.cameraenhance.ui;

import com.neildg.cameraenhance.R;
import com.neildg.cameraenhance.R.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class ProcessedImageFragment extends Fragment {

	private View parentView;
	
	public ProcessedImageFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		this.parentView =  inflater.inflate(R.layout.fragment_processed_images, container, false);
		
		return this.parentView;
	}

}
