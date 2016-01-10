package com.serveroverload.dali.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.serveroverload.dali.R;

public class DoodleFragment extends Fragment {

	public static Fragment newInstance() {
		return new DoodleFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.canvas_activity, container, false);
		Bundle bdl = getArguments();

		return v;
	}

}