package com.serveroverload.dali.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
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
import com.serveroverload.dali.helper.DiskUtil;
import com.serveroverload.dali.helper.UtilFunctions;
import com.serveroverload.dali.helper.UtilFunctions.AnimationType;
import com.serveroverload.dali.ui.customeview.CardsEffect;
import com.serveroverload.dali.ui.customeview.JazzyGridView;

/**
 * @author 663918
 *
 */
public class HomeFragment extends Fragment implements OnRefreshListener {

	protected static final String TAG = HomeFragment.class.getSimpleName();

	String tagJSONReq = "reqCategoryTask";

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.home_fragment, container, false);

		getActivity().setTitle("Search");

		itemsGridView = (JazzyGridView) rootView
				.findViewById(R.id.listView_products);
		// progressBar = (ProgressBar) rootView.findViewById(R.id.loading_bar);

		itemsGridView.setTransitionEffect(new CardsEffect(24));

		swipeLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(HomeFragment.this);
		swipeLayout.setColorSchemeColors(android.R.color.holo_blue_bright,
				R.color.holo_green_light, R.color.holo_orange_light,
				R.color.holo_red_light);

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

		rootView.findViewById(R.id.add_new_doodle).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						UtilFunctions.switchContent(R.id.frag_root,
								UtilFunctions.DOODLE_FRAGMENT, getActivity(),
								AnimationType.SLIDE_DOWN);

					}
				});

		rootView.findViewById(R.id.start_doodling).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						UtilFunctions.switchContent(R.id.frag_root,
								UtilFunctions.DOODLE_FRAGMENT, getActivity(),
								AnimationType.SLIDE_DOWN);

					}
				});

		setUpGrid(rootView);
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_UP
						&& keyCode == KeyEvent.KEYCODE_BACK) {

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
			itemsGridView.setAdapter(new HomeGridListArrayAdapter(
					getActivity(), R.layout.list_item_home_grid, doodles));

		} else {

			rootView.findViewById(R.id.default_nodata).setVisibility(
					View.VISIBLE);
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

}
