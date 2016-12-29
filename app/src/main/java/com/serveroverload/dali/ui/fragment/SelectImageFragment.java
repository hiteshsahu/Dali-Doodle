package com.serveroverload.dali.ui.fragment;

import com.serveroverload.dali.R;
import com.serveroverload.dali.helper.AppContants;
import com.serveroverload.dali.helper.Toaster;
import com.serveroverload.dali.helper.UriToUrl;
import com.serveroverload.dali.helper.UtilFunctions;
import com.serveroverload.dali.helper.UtilFunctions.AnimationType;
import com.serveroverload.dali.ui.SampleActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class SelectImageFragment extends Fragment {

	private Animation animation;
	private RelativeLayout top_holder;
	private RelativeLayout bottom_holder;
	private RelativeLayout step_number;
	private Uri imageUri;
	private boolean click_status = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.select_image_fragment, container, false);

		top_holder = (RelativeLayout) rootView.findViewById(R.id.top_holder);
		bottom_holder = (RelativeLayout) rootView.findViewById(R.id.bottom_holder);
		step_number = (RelativeLayout) rootView.findViewById(R.id.step_number);

		top_holder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				flyOut("displayCamera");

			}
		});

		bottom_holder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				flyOut("displayGallery");

			}
		});

		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

					UtilFunctions.switchContent(R.id.frag_root, UtilFunctions.HOME_FRAGMENT_TAG,
							(SampleActivity) getActivity(), AnimationType.SLIDE_RIGHT);

				}
				return true;
			}
		});

		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		flyIn();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
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

		ImageEditorFragment imageEditor = new ImageEditorFragment();

		Bundle bundle = new Bundle();
		bundle.putInt(AppContants.EXTRA_KEY_IMAGE_SOURCE, source_id);
		bundle.putString(AppContants.EXTRA_KEY_IMAGE_URL, imageUri.toString());

		imageEditor.setArguments(bundle);

		UtilFunctions.switchFragmentWithAnimation(R.id.frag_root, imageEditor, (SampleActivity) getActivity(), null,
				AnimationType.SLIDE_LEFT);
	}

	private void flyOut(final String method_name) {
		if (click_status) {
			click_status = false;

			animation = AnimationUtils.loadAnimation(getActivity(), R.anim.step_number_back);
			step_number.startAnimation(animation);

			animation = AnimationUtils.loadAnimation(getActivity(), R.anim.holder_top_back);
			top_holder.startAnimation(animation);

			animation = AnimationUtils.loadAnimation(getActivity(), R.anim.holder_bottom_back);
			bottom_holder.startAnimation(animation);

			animation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation arg0) {
				}

				@Override
				public void onAnimationRepeat(Animation arg0) {
				}

				@Override
				public void onAnimationEnd(Animation arg0) {
					// callMethod(method_name);

					if (method_name.equalsIgnoreCase("displayCamera")) {
						displayCamera();

					} else if (method_name.equalsIgnoreCase("displayGallery")) {
						displayGallery();
					}
				}
			});
		}
	}

	private void flyIn() {
		click_status = true;

		animation = AnimationUtils.loadAnimation(getActivity(), R.anim.holder_top);
		top_holder.startAnimation(animation);

		animation = AnimationUtils.loadAnimation(getActivity(), R.anim.holder_bottom);
		bottom_holder.startAnimation(animation);

		animation = AnimationUtils.loadAnimation(getActivity(), R.anim.step_number);
		step_number.startAnimation(animation);
	}

	public static Fragment newInstance() {
		// TODO Auto-generated method stub
		return new SelectImageFragment();
	}

}
