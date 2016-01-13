package com.serveroverload.dali.ui.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;

import com.serveroverload.dali.R;
import com.serveroverload.dali.adapter.HomeGridListArrayAdapter;
import com.serveroverload.dali.helper.AppContants;
import com.serveroverload.dali.helper.DiskUtil;
import com.serveroverload.dali.helper.Toaster;
import com.serveroverload.dali.helper.UriToUrl;
import com.serveroverload.dali.helper.UtilFunctions;
import com.serveroverload.dali.helper.UtilFunctions.AnimationType;
import com.serveroverload.dali.ui.SampleActivity;
import com.serveroverload.dali.ui.customeview.CardsEffect;
import com.serveroverload.dali.ui.customeview.JazzyGridView;

/**
 * @author 663918
 *
 */
public class HomeFragment extends Fragment implements OnRefreshListener {

	protected static final String TAG = HomeFragment.class.getSimpleName();

	private Uri imageUri;

	/** The swipe layout. */
	private SwipeRefreshLayout swipeLayout;

	/** The double back to exit pressed once. */
	private boolean doubleBackToExitPressedOnce;

	/** The m handler. */
	private Handler mHandler = new Handler();

	/** The m runnable. */
	private final Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			doubleBackToExitPressedOnce = false;
		}
	};

	/** The items grid view. */
	private JazzyGridView itemsGridView;

	private ProgressBar progressBar;

	private HomeGridListArrayAdapter adapter;

	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.home_fragment, container, false);

		getActivity().setTitle("Search");

		itemsGridView = (JazzyGridView) rootView.findViewById(R.id.listView_products);
		// progressBar = (ProgressBar) rootView.findViewById(R.id.loading_bar);

		itemsGridView.setTransitionEffect(new CardsEffect(24));

		swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(HomeFragment.this);
		swipeLayout.setColorSchemeColors(android.R.color.holo_blue_bright, R.color.holo_green_light,
				R.color.holo_orange_light, R.color.holo_red_light);

		// itemsGridView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		//
		// UtilFunctions.switchContent(R.id.frag_root,
		// UtilFunctions.DETAIL_FRAGMENT_TAG, getActivity(),
		// AnimationType.SLIDE_DOWN);
		//
		// }
		// });

		rootView.findViewById(R.id.add_new_doodle).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				UtilFunctions.switchContent(R.id.frag_root, UtilFunctions.DOODLE_FRAGMENT, getActivity(),
						AnimationType.SLIDE_DOWN);

			}
		});

		rootView.findViewById(R.id.pick).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				displayGallery();

			}
		});

		rootView.findViewById(R.id.capture).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				displayCamera();

			}
		});

		rootView.findViewById(R.id.start_doodling).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				UtilFunctions.switchContent(R.id.frag_root, UtilFunctions.DOODLE_FRAGMENT, getActivity(),
						AnimationType.SLIDE_DOWN);

			}
		});

		setUpGrid(rootView);
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

					// UtilFunctions.switchContent(R.id.frag_root,
					// UtilFunctions.DETAIL_FRAGMENT_TAG, getActivity(),
					// AnimationType.SLIDE_DOWN);

				}
				return true;
			}
		});

		return rootView;
	}

	/**
	 * @param rootView
	 */
	public void setUpGrid(View rootView) {

		ArrayList<String> doodles = DiskUtil.getListOfDoodles(true);

		if (!doodles.isEmpty()) {

			// Gridview adapter
			rootView.findViewById(R.id.default_nodata).setVisibility(View.GONE);
			swipeLayout.setVisibility(View.VISIBLE);

			// setting grid view adapter
			itemsGridView
					.setAdapter(new HomeGridListArrayAdapter(getActivity(), R.layout.list_item_home_grid, doodles));

		} else {

			rootView.findViewById(R.id.default_nodata).setVisibility(View.VISIBLE);
			swipeLayout.setVisibility(View.GONE);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener#onRefresh
	 * ()
	 */
	@Override
	public void onRefresh() {

		swipeLayout.setRefreshing(false);

		setUpGrid(rootView);

	}

	public static Fragment newInstance() {
		return new HomeFragment();
	}

	private void displayGallery() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
				&& !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)) {
			Intent intent = new Intent();
			intent.setType("image/jpeg");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, AppContants.REQUEST_GALLERY);
		} else {
			Toaster.make(getActivity(), R.string.no_media);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == AppContants.REQUEST_CAMERA) {
			try {
				if (resultCode == Activity.RESULT_OK) {
					displayPhotoActivity(1);
				} else {
					UriToUrl.deleteUri(getActivity(), imageUri);
				}
			} catch (Exception e) {
				Toaster.make(getActivity(), R.string.error_img_not_found);
			}
		} else if (resultCode == Activity.RESULT_OK && requestCode == AppContants.REQUEST_GALLERY) {
			try {
				imageUri = data.getData();
				displayPhotoActivity(2);
			} catch (Exception e) {
				Toaster.make(getActivity(), R.string.error_img_not_found);
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void displayCamera() {
		imageUri = getOutputMediaFile();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, AppContants.REQUEST_CAMERA);
	}

	private Uri getOutputMediaFile() {
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, "Camera Pro");
		values.put(MediaStore.Images.Media.DESCRIPTION, "www.appsroid.org");
		return getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	}

	private void displayPhotoActivity(int source_id) {

		// ImageEditorFragment imageEditor = new ImageEditorFragment();
		//
		// Bundle bundle = new Bundle();
		// bundle.putInt(AppContants.EXTRA_KEY_IMAGE_SOURCE, source_id);
		// bundle.putString(AppContants.EXTRA_KEY_IMAGE_URL,
		// imageUri.toString());
		//
		// imageEditor.setArguments(bundle);

		// UtilFunctions.switchFragmentWithAnimation(R.id.frag_root,
		// imageEditor, (SampleActivity) getActivity(), null,
		// AnimationType.SLIDE_LEFT);

		AppContants.doodleImageURL = imageUri.toString();

		UtilFunctions.switchFragmentWithAnimation(R.id.frag_root, DoodleFragment.newInstance(), getActivity(),
				UtilFunctions.DOODLE_FRAGMENT, AnimationType.SLIDE_RIGHT);

	}

}
