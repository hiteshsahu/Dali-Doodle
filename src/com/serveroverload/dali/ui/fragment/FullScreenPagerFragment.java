package com.serveroverload.dali.ui.fragment;

import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import com.serveroverload.dali.ui.ImageEditor;
import com.serveroverload.dali.ui.SampleActivity;
import com.serveroverload.dali.ui.customeview.TouchImageView;

public class FullScreenPagerFragment extends Fragment {

	private static final String PAGER_POSITION = "PagerPosition";
	private static int currentPagePosition = 0;
	private ExtendedViewPager mViewPager;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.doodle_gallery, container,
				false);
		if (null != getArguments()) {
			currentPagePosition = getArguments().getInt(PAGER_POSITION);
		}

		// setContentView(R.layout.activity_viewpager_example);
		mViewPager = (ExtendedViewPager) rootView.findViewById(R.id.view_pager);
		mViewPager.setAdapter(new TouchImageAdapter());

		mViewPager.setCurrentItem(currentPagePosition);

		rootView.findViewById(R.id.edit).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						AppContants.doodleImageURL = DiskUtil.getListOfDoodles(
								false).get(mViewPager.getCurrentItem());

						UtilFunctions.switchContent(R.id.frag_root,
								UtilFunctions.EDIT_IMAGE_FRAGMENT,
								getActivity(), AnimationType.SLIDE_LEFT);
					}
				});

		rootView.findViewById(R.id.info).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						File file = new File(DiskUtil.getListOfDoodles(false)
								.get(mViewPager.getCurrentItem()));

						// 1. Instantiate an AlertDialog.Builder with its
						// constructor
						AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());

						builder.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

									}
								});
						builder.setNegativeButton("Done",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
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
							Metadata metadata = ImageMetadataReader
									.readMetadata(file);

							for (Directory directory : metadata
									.getDirectories()) {

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

						builder.setMessage(str.toString()).setTitle(
								"Image Info");

						AlertDialog dialog = builder.create();

						dialog.show();

					}
				});

		rootView.findViewById(R.id.canvas).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						AppContants.doodleImageURL = DiskUtil.getListOfDoodles(
								false).get(mViewPager.getCurrentItem());

						UtilFunctions.switchFragmentWithAnimation(
								R.id.frag_root, DoodleFragment.newInstance(),
								getActivity(), UtilFunctions.DOODLE_FRAGMENT,
								AnimationType.SLIDE_RIGHT);

					}
				});

		rootView.findViewById(R.id.share).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						// Bitmap icon = mBitmap;
						// Intent share = new Intent(Intent.ACTION_SEND);
						// share.setType("image/jpeg");
						//
						// ContentValues values = new ContentValues();
						// values.put(Images.Media.TITLE, "title");
						// values.put(Images.Media.MIME_TYPE, "image/jpeg");
						// Uri uri =
						// getActivity().getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,
						// values);
						//
						//
						// OutputStream outstream;
						// try {
						// outstream =
						// getActivity().getContentResolver().openOutputStream(uri);
						// icon.compress(Bitmap.CompressFormat.JPEG, 100,
						// outstream);
						// outstream.close();
						// } catch (Exception e) {
						// System.err.println(e.toString());
						// }
						//
						// share.putExtra(Intent.EXTRA_STREAM, uri);
						// startActivity(Intent.createChooser(share,
						// "Share Image"));

					}
				});

		return rootView;
	}

	static class TouchImageAdapter extends PagerAdapter {

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

			Glide.with(container.getContext())
					.load(DiskUtil.getListOfDoodles(false).get(position))
					.crossFade(500).into(img);

			container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);

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