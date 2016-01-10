package com.serveroverload.dali.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.DocumentsContract.Root;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.serveroverload.dali.R;
import com.serveroverload.dali.canvas.CanvasDrawElements;
import com.serveroverload.dali.helper.AppContants;
import com.serveroverload.dali.helper.BitmapLoader;

public class DoodleFragment extends Fragment {

	public static Fragment newInstance() {
		return new DoodleFragment();
	}

	private CanvasDrawElements canavas;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.doodle_fragment, container,
				false);

		canavas = (CanvasDrawElements) rootView
				.findViewById(R.id.doodle_container);

		loadBitMap();
		
		return rootView;
	}

	/**
	 * @return
	 * 
	 */
	public void loadBitMap() {
		if (null != AppContants.doodleImageURL) {
			BitmapLoader bitmapLoader = new BitmapLoader();
			try {
				canavas.drawBitmap(bitmapLoader.load(getActivity(),
						AppContants.doodleImageURL));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}