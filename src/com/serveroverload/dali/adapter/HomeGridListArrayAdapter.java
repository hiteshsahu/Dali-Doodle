package com.serveroverload.dali.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.serveroverload.dali.R;
import com.serveroverload.dali.helper.UtilFunctions;
import com.serveroverload.dali.helper.UtilFunctions.AnimationType;
import com.serveroverload.dali.ui.SampleActivity;
import com.serveroverload.dali.ui.fragment.FullScreenPagerFragment;

// TODO: Auto-generated Javadoc
/**
 * The Class HomeGridListArrayAdapter.
 */
public class HomeGridListArrayAdapter extends ArrayAdapter<String>

{

	/**
	 * Instantiates a new home grid list array adapter.
	 *
	 * @param context
	 *            the context
	 * @param resource
	 *            the resource
	 * @param listOfProducts
	 *            the list of products
	 */
	public HomeGridListArrayAdapter(Context context, int resource,
			ArrayList<String> listOfProducts) {
		super(context, resource, listOfProducts);

		this.context = context;
		this.listOfProductsCategory = listOfProducts;
	}

	/** The context. */
	private final Context context;

	/** The list of products. */
	private final List<String> listOfProductsCategory;

	/** The holder. */
	private ViewHolder holder;

	/**
	 * The Class ViewHolder.
	 */
	private class ViewHolder {

		/** The recording name. */
		TextView categoryName, categoryDescription;
		ImageView rootbackGround;

		/** The current swipe layout. */
		// SwipeLayout currentSwipeLayout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.list_item_home_grid,
					parent, false);

			holder = new ViewHolder();

			holder.categoryName = (TextView) convertView
					.findViewById(R.id.item_name);

			holder.categoryName.setSelected(true);

			holder.categoryDescription = (TextView) convertView
					.findViewById(R.id.item_short_desc);

			holder.categoryDescription.setSelected(true);

			// holder.currentSwipeLayout = (SwipeLayout) convertView
			// .findViewById(R.id.swipe_layout_1);

			holder.rootbackGround = (ImageView) convertView
					.findViewById(R.id.imageView);

			// holder.currentSwipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
			//
			// holder.currentSwipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);
			//
			// LabelView label = new LabelView(context);
			// label.setText("20%");
			// label.setBackgroundColor(0xffE91E63);
			// label.setTargetView(convertView.findViewById(R.id.top_view_back),
			// 10, LabelView.Gravity.RIGHT_TOP);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//
				// UtilFunctions.switchContent(R.id.content_frame,
				// UtilFunctions.ITEM_DETAIL_FRAGMENT_TAG,
				// (FarmerzdenHomeActivity) context,
				// AnimationType.SLIDE_LEFT);
			}
		});

		Glide.with(context)
				.load((new File(listOfProductsCategory.get(position))))
				.crossFade().into(holder.rootbackGround);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				UtilFunctions.switchFragmentWithAnimation(R.id.frag_root,
						FullScreenPagerFragment.newInstance(position),
						(SampleActivity) getContext(),
						"FullScreenPagerFragment", AnimationType.SLIDE_RIGHT);

			}
		});

		return convertView;
	}

	// }
	/**
	 * Share item.
	 *
	 * @param position
	 *            the position
	 */
	//
	private void shareItem(int position) {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		// File archivo = new File(((HomeActivity) context).getRecordings().get(
		// position));
		// sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(archivo));
		context.startActivity(Intent.createChooser(sharingIntent,
				"Share with Friends"));

	}
}
