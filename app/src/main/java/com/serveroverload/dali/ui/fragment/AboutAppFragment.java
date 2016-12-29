/**
 * 
 */
package com.serveroverload.dali.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.serveroverload.dali.R;
import com.serveroverload.dali.helper.UtilFunctions;
import com.serveroverload.dali.helper.UtilFunctions.AnimationType;
import com.serveroverload.dali.ui.SampleActivity;

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
		View rootView = inflater.inflate(R.layout.about_fragment, container, false);

		((TextView) rootView.findViewById(R.id.app_version_code)).setText(UtilFunctions.getVersion(getActivity()));

		rootView.findViewById(R.id.site).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.serveroverload.com"));
				startActivity(browserIntent);

			}
		});

		CheckBox submitCrashLogs = ((CheckBox) rootView.findViewById(R.id.checkbox_submit_logs));

		rootView.findViewById(R.id.mail_dev).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				emailIntent.setType("text/plain");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
						new String[] { "serveroverloadofficial@gmail.com" });
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hello There");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Add Message here");

				emailIntent.setType("message/rfc822");

				try {
					startActivity(Intent.createChooser(emailIntent, "Send email using..."));
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(getActivity(), "No email clients installed.", Toast.LENGTH_SHORT).show();
				}

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

}
