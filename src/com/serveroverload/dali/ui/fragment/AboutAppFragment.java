/**
 * 
 */
package com.serveroverload.dali.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.serveroverload.dali.R;

/**
 * @author 663918
 *
 */
public class AboutAppFragment extends Fragment {

	public static Fragment newInstance() {
		return new AboutAppFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.about_fragment, container, false);
		Bundle bdl = getArguments();

		return v;
	}

}
