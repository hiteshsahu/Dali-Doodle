package com.serveroverload.dali.ui.fragment;

import java.io.File;
import java.io.IOException;

import com.bumptech.glide.Glide;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.serveroverload.dali.R;
import com.serveroverload.dali.adapter.ExtendedViewPager;
import com.serveroverload.dali.helper.AppContants;
import com.serveroverload.dali.helper.DiskUtil;
import com.serveroverload.dali.helper.UtilFunctions;
import com.serveroverload.dali.helper.UtilFunctions.AnimationType;
import com.serveroverload.dali.ui.SampleActivity;
import com.serveroverload.dali.ui.customeview.TouchImageView;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FullScreenPagerFragment extends Fragment {

	private static final String PAGER_POSITION = "PagerPosition";
	private static int currentPagePosition = 0;
	private ExtendedViewPager mViewPager;
	private TouchImageAdapter touchImageAdapter;

	/**
	 * Step 1: Download and set up v4 support library:
	 * http://developer.android.com/tools/support-library/setup.html Step 2:
	 * Create ExtendedViewPager wrapper which calls
	 * TouchImageView.canScrollHorizontallyFroyo Step 3: ExtendedViewPager is a
	 * custom view and must be referred to by its full package name in XML Step
	 * 4: Write TouchImageAdapter, located below Step 5. The ViewPager in the
	 * XML should be ExtendedViewPager
	 */

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.doodle_gallery, container, false);
		if (null != getArguments()) {
			currentPagePosition = getArguments().getInt(PAGER_POSITION);
		}

		// setContentView(R.layout.activity_viewpager_example);
		mViewPager = (ExtendedViewPager) rootView.findViewById(R.id.view_pager);

		touchImageAdapter = new TouchImageAdapter();
		mViewPager.setAdapter(touchImageAdapter);

		mViewPager.setCurrentItem(currentPagePosition);

		rootView.findViewById(R.id.edit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// AppContants.doodleImageURL = DiskUtil.getListOfDoodles(
				// false).get(mViewPager.getCurrentItem());

				// UtilFunctions.switchContent(R.id.frag_root,
				// UtilFunctions.EDIT_IMAGE_FRAGMENT,
				// getActivity(), AnimationType.SLIDE_LEFT);

				Bundle bundle = new Bundle();
				bundle.putInt(AppContants.EXTRA_KEY_IMAGE_SOURCE, 2);
				bundle.putString(AppContants.EXTRA_KEY_IMAGE_URL,
						DiskUtil.getListOfDoodles(true).get(mViewPager.getCurrentItem()));

				ImageEditorFragment imageEditor = new ImageEditorFragment();

				imageEditor.setArguments(bundle);

				UtilFunctions.switchFragmentWithAnimation(R.id.frag_root, imageEditor, (SampleActivity) getActivity(),
						"ImageEditorFragment", AnimationType.SLIDE_LEFT);
			}
		});

		rootView.findViewById(R.id.info).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				File file = new File(DiskUtil.getListOfDoodles(false).get(mViewPager.getCurrentItem()));

				// 1. Instantiate an AlertDialog.Builder with its
				// constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});
				builder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
					}
				});

				StringBuilder str = new StringBuilder();

				// 2. Chain together various setter methods to set the
				// dialog characteristics

				// 3. Get the AlertDialog from create()

				// There are multiple ways to get a Metadata object for
				// a file

				//
				// SCENARIO 1: UNKNOWN FILE TYPE
				//
				// This is the most generic approach. It will
				// transparently determine the file type and invoke the
				// appropriate
				// readers. In most cases, this is the most appropriate
				// usage. This will handle JPEG, TIFF, GIF, BMP and RAW
				// (CRW/CR2/NEF/RW2/ORF) files and extract whatever
				// metadata is available and understood.
				//
				try {
					Metadata metadata = ImageMetadataReader.readMetadata(file);

					for (Directory directory : metadata.getDirectories()) {

						//
						// Each Directory stores values in Tag objects
						//
						for (Tag tag : directory.getTags()) {
							System.out.println(tag);

							str.append(tag.toString());
							str.append("\n");

						}

						//
						// Each Directory may also contain error
						// messages
						//
						if (directory.hasErrors()) {
							for (String error : directory.getErrors()) {
								System.err.println("ERROR: " + error);
							}
						}

					}

				} catch (ImageProcessingException e) {
					// handle exception
				} catch (IOException e) {
					// handle exception
				}

				builder.setMessage(str.toString()).setTitle("Image Info");

				AlertDialog dialog = builder.create();

				dialog.show();

			}
		});

		rootView.findViewById(R.id.canvas).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AppContants.doodleImageURL = DiskUtil.getListOfDoodles(true).get(mViewPager.getCurrentItem());

				UtilFunctions.switchFragmentWithAnimation(R.id.frag_root, DoodleFragment.newInstance(), getActivity(),
						UtilFunctions.DOODLE_FRAGMENT, AnimationType.SLIDE_RIGHT);

			}
		});

		rootView.findViewById(R.id.delete).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

//				// Set up the projection (we only need the ID)
//				String[] projection = { MediaStore.Images.Media._ID };
//
//				// Match on the file path
//				String selection = MediaStore.Images.Media.DATA + " = ?";
//				String[] selectionArgs = new String[] {
//						new File(DiskUtil.getListOfDoodles(true).get(mViewPager.getCurrentItem())).getAbsolutePath() };
//
//				// Query for the ID of the media matching the file path
//				Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//				ContentResolver contentResolver = getActivity().getContentResolver();
//				Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
//				if (c.moveToFirst()) {
//					// We found the ID. Deleting the item via the content
//					// provider will also remove the file
//					long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
//					Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
//					contentResolver.delete(deleteUri, null, null);
//					Toast.makeText(getActivity(), "Deleted", 500).show();
//				} else {
//					Toast.makeText(getActivity(), "Failed to Delete", 500).show();
//				}
//				c.close();

				File f = new File(DiskUtil.getListOfDoodles(true).get(mViewPager.getCurrentItem()));
				if (f.delete()) {

					touchImageAdapter.notifyDataSetChanged();

					getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
							Uri.fromFile(new File(DiskUtil.getListOfDoodles(true).get(mViewPager.getCurrentItem())))));

					Toast.makeText(getActivity(), "Deleted", 500).show();
				} else {
					Toast.makeText(getActivity(), "Failed to Delete", 500).show();
				}
			}
		});

		rootView.findViewById(R.id.share).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent share = new Intent(Intent.ACTION_SEND);
				share.setType("image/jpeg");
				share.putExtra(Intent.EXTRA_STREAM,
						Uri.parse(DiskUtil.getListOfDoodles(true).get(mViewPager.getCurrentItem())));
				startActivity(Intent.createChooser(share, "Share Image"));

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

	static class TouchImageAdapter extends PagerAdapter {

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return DiskUtil.getListOfDoodles(false).size();
		}

		@Override
		public int getItemPosition(Object object) {

			return super.getItemPosition(object);
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			TouchImageView img = new TouchImageView(container.getContext());
			// img.setImageResource(images[position]);

			currentPagePosition = position;

			Glide.with(container.getContext()).load(DiskUtil.getListOfDoodles(false).get(position)).crossFade(500)
					.into(img);

			container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

			return img;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	public static Fragment newInstance(int position) {
		Bundle bundle = new Bundle();
		bundle.putInt(PAGER_POSITION, position);
		FullScreenPagerFragment fullScreenPagerFragment = new FullScreenPagerFragment();
		fullScreenPagerFragment.setArguments(bundle);

		return fullScreenPagerFragment;
	}
}