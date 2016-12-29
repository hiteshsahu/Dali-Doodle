package com.serveroverload.dali.ui.fragment;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.serveroverload.dali.R;
import com.serveroverload.dali.helper.AppContants;
import com.serveroverload.dali.helper.BitmapLoader;
import com.serveroverload.dali.helper.BitmapProcessing;
import com.serveroverload.dali.helper.BitmapWriter;
import com.serveroverload.dali.helper.Toaster;
import com.serveroverload.dali.helper.UriToUrl;
import com.serveroverload.dali.helper.UtilFunctions;
import com.serveroverload.dali.helper.UtilFunctions.AnimationType;
import com.serveroverload.dali.ui.BackDialog;
import com.serveroverload.dali.ui.LoadingDialog;
import com.serveroverload.dali.ui.SampleActivity;
import com.serveroverload.dali.ui.customeview.TouchImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ImageEditorFragment extends Fragment {

	private static final String CBALANCE = "cbalance";
	// Supported Effects

	private static final String SATURATION = "saturation";
	private static final String BRIGHTNESS = "brightness";
	private static final String CONTRAST = "contrast";
	private static final String FLIP = "flip";
	private static final String BOOST = "boost";
	private static final String CDEPTH = "cdepth";
	private static final String GAMMA = "gamma";
	private static final String HUE = "hue";
	private static final String TINT = "tint";
	private static final String VIGNETTE = "vignette";
	private static final String SKETCH = "sketch";
	private static final String GAUSSIAN = "gaussian";
	private static final String EMBOSS = "emboss";
	private static final String SHARPEN = "sharpen";
	private static final String SEPIA = "sepia";
	private static final String NOISE = "noise";
	private static final String GRAYSCALE = "grayscale";
	private static final String INVERT = "invert";

	private Animation animation;
	private TouchImageView image_holder;
	private Bitmap last_bitmap;
	private int source_id;
	private Uri imageUri;
	private String imageUrl;
	private LinearLayout btn_holder;
	private ImageView undo_btn;
	private ImageView save_btn;
	private ArrayList<String> effects;
	private LinearLayout holder_target;
	private LinearLayout apply_set;
	private RelativeLayout toolbox;
	private String selected_effect;

	private LoadingDialog loading_dialog;

	private LinearLayout effect_box;

	private int tint_color = 0xFF1E8D24;

	private boolean flip_v = false;
	private boolean flip_h = false;

	private int boost_type = 1;

	private SeekBar hue_value;
	private TextView hue_label;
	private SeekBar sat_value;
	private TextView sat_label;

	private SeekBar bright_value;
	private TextView bright_label;
	private SeekBar cont_value;
	private TextView cont_label;

	private SeekBar gRed_value;
	private TextView gRed_label;
	private SeekBar gGreen_value;
	private TextView gGreen_label;
	private SeekBar gBlue_value;
	private TextView gBlue_label;

	private SeekBar bRed_value;
	private TextView bRed_label;
	private SeekBar bGreen_value;
	private TextView bGreen_label;
	private SeekBar bBlue_value;
	private TextView bBlue_label;

	private SeekBar rotate_value;
	private TextView rotate_label;

	private SeekBar boost_value;
	private TextView boost_label;

	private String outputURL = null;

	private SeekBar cdepth_value;
	private TextView cdepth_label;

	private boolean save_status = false;
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_photo, container, false);

		// setContentView(R.layout.activity_photo);

		setUpViews();

		// if(null!=AppContants.doodleImageURL)
		// {

		if (null != getArguments()) {

			// bundle.putInt(AppContants.EXTRA_KEY_IMAGE_SOURCE, source_id);
			// bundle.putString(AppContants.EXTRA_KEY_IMAGE_SOURCE,
			// imageUri.toString());
			// getArguments().getString(AppContants.EXTRA_KEY_IMAGE_SOURCE)
			// }

			// if (savedInstanceState == null) {
			source_id = getArguments().getInt(AppContants.EXTRA_KEY_IMAGE_SOURCE);
			imageUri = Uri.parse(getArguments().getString(AppContants.EXTRA_KEY_IMAGE_URL));
			effects = new ArrayList<String>();
			try {
				loadImage();

			} catch (Exception e) {

				Toaster.make(getActivity(), R.string.error_img_not_found);
				backToMain();
			}
			// } else {
			// effects = savedInstanceState
			// .getStringArrayList(AppContants.KEY_EFFECTS_LIST);
			// imageUrl = savedInstanceState.getString(AppContants.KEY_URL);
			// source_id = savedInstanceState.getInt(AppContants.KEY_SOURCE_ID);
			// setImage((Bitmap) savedInstanceState
			// .getParcelable(AppContants.KEY_BITMAP));
			// }

			if (effects.size() > 0) {
				btn_holder.setVisibility(View.VISIBLE);
				flyIn();
			} else {
				btn_holder.clearAnimation();
			}

			setUpEventsOnView();

		}

		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

					onBackPressed();
				}
				return true;
			}
		});

		return rootView;

	}

	private void setUpEventsOnView() {
		image_holder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggleToolbox();

			}
		});

		rootView.findViewById(R.id.boost).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleClickOnEfefct(BOOST);

			}
		});

		rootView.findViewById(R.id.brightness).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleClickOnEfefct(BRIGHTNESS);

			}
		});

		rootView.findViewById(R.id.cdepth).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleClickOnEfefct(CDEPTH);

			}
		});

		rootView.findViewById(R.id.cbalance).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleClickOnEfefct(CBALANCE);

			}
		});

		rootView.findViewById(R.id.contrast).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleClickOnEfefct(CONTRAST);

			}
		});

		rootView.findViewById(R.id.emboss).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleClickOnEfefct(EMBOSS);

			}
		});

		rootView.findViewById(R.id.flip).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleClickOnEfefct(FLIP);

			}
		});

		rootView.findViewById(R.id.gamma).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleClickOnEfefct(GAMMA);

			}
		});

		rootView.findViewById(R.id.gaussian).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleClickOnEfefct(GAUSSIAN);

			}
		});

		rootView.findViewById(R.id.hue).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleClickOnEfefct(HUE);

			}
		});

		rootView.findViewById(R.id.invert).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleClickOnEfefct(INVERT);

			}
		});

		rootView.findViewById(R.id.noise).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleClickOnEfefct(NOISE);

			}
		});

		rootView.findViewById(R.id.saturation).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleClickOnEfefct(SATURATION);

			}
		});

		rootView.findViewById(R.id.sepia).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleClickOnEfefct(SEPIA);

			}
		});

		rootView.findViewById(R.id.sharpen).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleClickOnEfefct(SHARPEN);

			}
		});

		rootView.findViewById(R.id.sketch).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleClickOnEfefct(SKETCH);

			}
		});

		rootView.findViewById(R.id.grayscale).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleClickOnEfefct(GRAYSCALE);

			}
		});

		rootView.findViewById(R.id.tint).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleClickOnEfefct(TINT);

			}
		});

		rootView.findViewById(R.id.vignette).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleClickOnEfefct(VIGNETTE);

			}
		});

		rootView.findViewById(R.id.save_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!loading_dialog.isShowing()) {
					hideEffectsBox(false);
					flyOut("saveImage");
				}

			}
		});

		rootView.findViewById(R.id.undo_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!loading_dialog.isShowing()) {
					flyOut("reloadImage");
				}

			}
		});

		rootView.findViewById(R.id.cancelSelectedEffect).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!loading_dialog.isShowing()) {
					setImage(last_bitmap.copy(last_bitmap.getConfig(), true));

					try {
						last_bitmap.recycle();
						last_bitmap = null;
					} catch (Exception e) {
					}

					hideEffectHolder();
				}

			}
		});

		rootView.findViewById(R.id.ic_accept).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				setSelectedEffect();

			}
		});

		rootView.findViewById(R.id.boost_red).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeBoostType(1);

			}
		});

		rootView.findViewById(R.id.boost_green).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeBoostType(2);

			}
		});

		rootView.findViewById(R.id.boost_blue).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeBoostType(3);

			}
		});

		rootView.findViewById(R.id.tint_shade_1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FF1E8D24");

			}
		});

		rootView.findViewById(R.id.tint_shade_2).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FF374D1F");

			}
		});

		rootView.findViewById(R.id.tint_shade_3).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FF04A639");

			}
		});

		rootView.findViewById(R.id.tint_shade_4).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FFA0D722");

			}
		});

		rootView.findViewById(R.id.tint_shade_5).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FFC41430");

			}
		});

		rootView.findViewById(R.id.tint_shade_6).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FFFF0000");

			}
		});

		rootView.findViewById(R.id.tint_shade_7).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FFFF3300");

			}

		});

		rootView.findViewById(R.id.tint_shade_8).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FFAD343A");

			}
		});

		rootView.findViewById(R.id.tint_shade_9).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FF7B2A26");

			}

		});

		rootView.findViewById(R.id.tint_shade_10).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FF9B162F");

			}

		});

		rootView.findViewById(R.id.tint_shade_11).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FFFF29E8");

			}

		});

		rootView.findViewById(R.id.tint_shade_12).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FF862B73");

			}

		});

		rootView.findViewById(R.id.tint_shade_13).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FF01A6A8");

			}

		});

		rootView.findViewById(R.id.tint_shade_14).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FF147C87");

			}

		});

		rootView.findViewById(R.id.tint_shade_15).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FF3C4056");

			}

		});

		rootView.findViewById(R.id.tint_shade_16).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FF0120C1");

			}

		});

		rootView.findViewById(R.id.tint_shade_17).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FFF3C538");

			}

		});

		rootView.findViewById(R.id.tint_shade_18).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FF0A1D19");

			}

		});

		rootView.findViewById(R.id.tint_shade_19).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FFF27B35");

			}

		});

		rootView.findViewById(R.id.tint_shade_20).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyTint("#FFF2B885");

			}

		});

		rootView.findViewById(R.id.flip_v).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onFlipV();

			}

		});

		rootView.findViewById(R.id.flip_h).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onFlipH();

			}

		});
	}

	// public void cancelSelectedEffect(View view) {
	// if (!loading_dialog.isShowing()) {
	// setImage(last_bitmap.copy(last_bitmap.getConfig(), true));
	//
	// try {
	// last_bitmap.recycle();
	// last_bitmap = null;
	// } catch (Exception e) {
	// }
	//
	// hideEffectHolder();
	// }
	// }

	//
	// public void onClickUndo(View view) {
	// if (!loading_dialog.isShowing()) {
	// flyOut("reloadImage");
	// }
	// }
	//
	// public void onClickSave(View view) {
	// if (!loading_dialog.isShowing()) {
	// hideEffectsBox(false);
	// flyOut("saveImage");
	// }
	// }
	//

	public void changeBoostType(int boostType) {
		boost_type = boostType;
		// boost_type = Integer.parseInt(view.getTag().toString());
		modifyBoostHolder();
		applyTempSelectedEffect();
	}

	private void hideLoading() {
		try {
			loading_dialog.dismiss();
		} catch (Exception e) {
		}
	}

	public void applyTint(String colorType) {
		if (tint_color != Color.parseColor(colorType)) {
			tint_color = Color.parseColor(colorType);
			applyTempSelectedEffect();
		}
	}

	public void onFlipV() {
		flip_v = !flip_v;
		applyTempSelectedEffect();
	}

	public void onFlipH() {
		flip_h = !flip_h;
		applyTempSelectedEffect();
	}

	public static Fragment newInstance() {
		// TODO Auto-generated method stub
		return new ImageEditorFragment();
	}

	public void handleClickOnEfefct(String effect) {
		if (!loading_dialog.isShowing()) {
			displayLoading();
			if (effects.size() == 0) {
				btn_holder.setVisibility(View.GONE);
			}

			handleEffect(effect);
		}
	}

	// public void onClickEffectButton(View view) {
	// if (!loading_dialog.isShowing()) {
	// displayLoading();
	// if (effects.size() == 0) {
	// btn_holder.setVisibility(View.GONE);
	// }
	//
	// handleEffect(view.getTag().toString());
	// }
	// }

	/**
	 * 
	 */
	public void setUpViews() {
		image_holder = (TouchImageView) rootView.findViewById(R.id.source_image);

		btn_holder = (LinearLayout) rootView.findViewById(R.id.btn_holder);
		undo_btn = (ImageView) rootView.findViewById(R.id.undo_btn);
		save_btn = (ImageView) rootView.findViewById(R.id.save_btn);

		loading_dialog = new LoadingDialog(getActivity());

		effect_box = (LinearLayout) rootView.findViewById(R.id.effects_holder);

		apply_set = (LinearLayout) rootView.findViewById(R.id.effect_set_box);

		toolbox = (RelativeLayout) rootView.findViewById(R.id.toolbox);

		hue_value = (SeekBar) rootView.findViewById(R.id.hue_value);
		hue_label = (TextView) rootView.findViewById(R.id.hue_label);
		sat_value = (SeekBar) rootView.findViewById(R.id.sat_value);
		sat_label = (TextView) rootView.findViewById(R.id.sat_label);

		bright_value = (SeekBar) rootView.findViewById(R.id.brightness_value);
		bright_label = (TextView) rootView.findViewById(R.id.brightness_label);
		cont_value = (SeekBar) rootView.findViewById(R.id.contrast_value);
		cont_label = (TextView) rootView.findViewById(R.id.contrast_label);

		gRed_value = (SeekBar) rootView.findViewById(R.id.gRed_value);
		gRed_label = (TextView) rootView.findViewById(R.id.gRed_label);
		gGreen_value = (SeekBar) rootView.findViewById(R.id.gGreen_value);
		gGreen_label = (TextView) rootView.findViewById(R.id.gGreen_label);
		gBlue_value = (SeekBar) rootView.findViewById(R.id.gBlue_value);
		gBlue_label = (TextView) rootView.findViewById(R.id.gBlue_label);

		bRed_value = (SeekBar) rootView.findViewById(R.id.bRed_value);
		bRed_label = (TextView) rootView.findViewById(R.id.bRed_label);
		bGreen_value = (SeekBar) rootView.findViewById(R.id.bGreen_value);
		bGreen_label = (TextView) rootView.findViewById(R.id.bGreen_label);
		bBlue_value = (SeekBar) rootView.findViewById(R.id.bBlue_value);
		bBlue_label = (TextView) rootView.findViewById(R.id.bBlue_label);

		rotate_value = (SeekBar) rootView.findViewById(R.id.rotate_value);
		rotate_label = (TextView) rootView.findViewById(R.id.rotate_label);

		boost_value = (SeekBar) rootView.findViewById(R.id.boost_value);
		boost_label = (TextView) rootView.findViewById(R.id.boost_label);

		cdepth_value = (SeekBar) rootView.findViewById(R.id.cdepth_value);
		cdepth_label = (TextView) rootView.findViewById(R.id.cdepth_label);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		// overridePendingTransition(0, 0);
		super.onStop();
	}

	public void toggleToolbox() {
		if (toolbox.getVisibility() == View.VISIBLE) {
			toolbox.setVisibility(View.GONE);
		} else {
			toolbox.setVisibility(View.VISIBLE);
		}
	}

	private void flyIn() {
		hideLoading();
		btn_holder.setVisibility(View.VISIBLE);
		if (effects.isEmpty()) {
			undo_btn.setVisibility(View.GONE);
			save_btn.setVisibility(View.GONE);
		} else {
			undo_btn.setVisibility(View.VISIBLE);
			save_btn.setVisibility(View.VISIBLE);
		}

		animation = AnimationUtils.loadAnimation(getActivity(), R.anim.holder_top_fast);
		btn_holder.startAnimation(animation);
	}

	private Bitmap bitmap() {
		try {
			return ((BitmapDrawable) image_holder.getDrawable()).getBitmap();
		} catch (Exception e) {
			return null;
		}
	}

	private void flyOut(final String method_name) {
		if (!loading_dialog.isShowing()) {
			displayLoading();
			try {
				if (method_name.equals("reloadImage")) {
					Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
					vibrator.vibrate(200);
				}
			} catch (Exception e) {
			}

			animation = AnimationUtils.loadAnimation(getActivity(), R.anim.holder_top_back_fast);
			btn_holder.startAnimation(animation);

			animation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation arg0) {
				}

				@Override
				public void onAnimationRepeat(Animation arg0) {
				}

				@Override
				public void onAnimationEnd(Animation arg0) {
					animation.setAnimationListener(null);
					btn_holder.clearAnimation();
					btn_holder.setVisibility(View.GONE);
					// callMethod(method_name);

					if (method_name.equalsIgnoreCase("reloadImage")) {
						reloadImage();
					} else if (method_name.equalsIgnoreCase("saveImage")) {

						saveImage();
					}
				}
			});
		}
	}

	// private void callMethod(String method_name) {
	// try {
	// Method method = ImageEditorFragment.class.getDeclaredMethod(method_name);
	// method.invoke(this, new Object[] {});
	// } catch (Exception e) {
	// }
	// }

	private void setImage(Bitmap bitmap) {
		hideLoading();
		try {
			if (bitmap != null) {
				image_holder.setImageBitmap(bitmap);
			} else {
				Toaster.make(getActivity(), R.string.error_img_not_found);
				backToMain();
			}
		} catch (Exception e) {
			Toaster.make(getActivity(), R.string.error_img_not_found);
			backToMain();
		}
	}

	public void setSelectedEffect() {
		if (!loading_dialog.isShowing()) {

			if (selected_effect.equals(TINT)) {
				selected_effect = selected_effect + "_#" + Integer.toHexString(tint_color);

			} else if (selected_effect.equals(BRIGHTNESS)) {
				if (bright_value.getProgress() == 200) {

					selected_effect = "";
					bright_value.setOnSeekBarChangeListener(null);

				} else {
					selected_effect = selected_effect + "_" + bright_value.getProgress();
				}

			} else if (selected_effect.equals(CONTRAST)) {
				if (cont_value.getProgress() == 50) {

					selected_effect = "";
					cont_value.setOnSeekBarChangeListener(null);

				} else {
					selected_effect = selected_effect + "_" + cont_value.getProgress();
				}
			} else if (selected_effect.equals(FLIP)) {
				if (rotate_value.getProgress() == 0 && !flip_v && !flip_h) {

					selected_effect = "";
					rotate_value.setOnSeekBarChangeListener(null);

				} else {
					selected_effect = selected_effect + "_" + (flip_h ? "t" : "f") + "_" + (flip_v ? "t" : "f") + "_"
							+ rotate_value.getProgress();
				}
			}

			else if (selected_effect.equals(HUE)) {
				selected_effect = selected_effect + "_" + hue_value.getProgress();
			} else if (selected_effect.equals(GAMMA)) {
				selected_effect = selected_effect + "_" + gRed_value.getProgress() + "_" + gGreen_value.getProgress()
						+ "_" + gBlue_value.getProgress();
			} else if (selected_effect.equals(CDEPTH)) {
				selected_effect = selected_effect + "_" + cdepth_value.getProgress();
			} else if (selected_effect.equals(CBALANCE)) {
				if (bRed_value.getProgress() == 100 && bGreen_value.getProgress() == 100
						&& bBlue_value.getProgress() == 100) {

					selected_effect = "";
					bRed_value.setOnSeekBarChangeListener(null);
					bGreen_value.setOnSeekBarChangeListener(null);
					bBlue_value.setOnSeekBarChangeListener(null);

				} else {
					selected_effect = selected_effect + "_" + bRed_value.getProgress() + "_"
							+ bGreen_value.getProgress() + "_" + bBlue_value.getProgress();
				}
			} else if (selected_effect.equals(BOOST)) {
				selected_effect = selected_effect + "_" + boost_type + "_" + boost_value.getProgress();
			}

			else if (selected_effect.equals(SATURATION)) {
				if (sat_value.getProgress() == 100) {
					selected_effect = "";
					sat_value.setOnSeekBarChangeListener(null);
				} else {
					selected_effect = selected_effect + "_" + sat_value.getProgress();
				}
			}

			applyEffect(selected_effect, true, false);
			hideEffectHolder();
		}
	}

	private void applyTempSelectedEffect() {
		applyEffect(selected_effect, false, true);
	}

	private void handleEffect(String effect) {
		ArrayList<String> noOptions = new ArrayList<String>();
		noOptions.add(INVERT);
		noOptions.add(GRAYSCALE);
		noOptions.add(NOISE);
		noOptions.add(SEPIA);
		noOptions.add(SHARPEN);
		noOptions.add(EMBOSS);
		noOptions.add(GAUSSIAN);
		noOptions.add(SKETCH);
		noOptions.add(VIGNETTE);

		if (noOptions.indexOf(effect) == -1) {
			last_bitmap = bitmap().copy(bitmap().getConfig(), false);
			selected_effect = effect;

			displayEffectHolder();
			// Set default if exists
			if (effect.equals(TINT) || effect.equals(HUE) || effect.equals(GAMMA) || effect.equals(CDEPTH)
					|| effect.equals(BOOST)) {
				applyTempSelectedEffect();
			}
		} else {
			applyEffect(effect, true, true);
		}

	}

	private void hideEffectHolder() {
		displayEffectsBox();
		if (selected_effect.startsWith(HUE)) {

			hue_value.setOnSeekBarChangeListener(null);

		} else if (selected_effect.startsWith(BRIGHTNESS)) {

			bright_value.setOnSeekBarChangeListener(null);

		} else if (selected_effect.startsWith(CONTRAST)) {

			cont_value.setOnSeekBarChangeListener(null);

		} else if (selected_effect.startsWith(FLIP)) {

			rotate_value.setOnSeekBarChangeListener(null);

		} else if (selected_effect.startsWith(GAMMA)) {

			gRed_value.setOnSeekBarChangeListener(null);
			gGreen_value.setOnSeekBarChangeListener(null);
			gBlue_value.setOnSeekBarChangeListener(null);

		} else if (selected_effect.startsWith(CDEPTH)) {

			cdepth_value.setOnSeekBarChangeListener(null);

		} else if (selected_effect.startsWith(GAMMA)) {

			bRed_value.setOnSeekBarChangeListener(null);
			bGreen_value.setOnSeekBarChangeListener(null);
			bBlue_value.setOnSeekBarChangeListener(null);

		} else if (selected_effect.startsWith(BOOST)) {

			boost_value.setOnSeekBarChangeListener(null);

		} else if (selected_effect.startsWith(SATURATION)) {

			sat_value.setOnSeekBarChangeListener(null);

		}

		animation = AnimationUtils.loadAnimation(getActivity(), R.anim.holder_bottom_back_fast);
		holder_target.startAnimation(animation);

		animation = AnimationUtils.loadAnimation(getActivity(), R.anim.holder_top_back_fast);
		apply_set.startAnimation(animation);

		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				animation.setAnimationListener(null);
				holder_target.clearAnimation();
				apply_set.clearAnimation();
				holder_target.setVisibility(View.GONE);
				apply_set.setVisibility(View.GONE);
				flyIn();
			}
		});
	}

	OnSeekBarChangeListener onHueChange = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			applyTempSelectedEffect();
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
		}

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			modifyHueHolder();
		}
	};

	OnSeekBarChangeListener onSatChange = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			applyTempSelectedEffect();
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
		}

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			modifySaturationHolder();
		}
	};

	OnSeekBarChangeListener onBrightChange = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			applyTempSelectedEffect();
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
		}

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			modifyBrightnessHolder();
		}
	};

	OnSeekBarChangeListener onCDepthChange = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			applyTempSelectedEffect();
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
		}

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			modifyCDepthHolder();
		}
	};

	OnSeekBarChangeListener onContChange = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			applyTempSelectedEffect();
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
		}

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			modifyContrastHolder();
		}
	};

	OnSeekBarChangeListener onRotateChange = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			applyTempSelectedEffect();
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
		}

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			modifyRotateHolder();
		}
	};

	OnSeekBarChangeListener onGRedChange = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			applyTempSelectedEffect();
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
		}

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			modifyGammaHolder();
		}
	};

	OnSeekBarChangeListener onGGreenChange = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			applyTempSelectedEffect();
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
		}

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			modifyGammaHolder();
		}
	};

	OnSeekBarChangeListener onBGreenChange = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			applyTempSelectedEffect();
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
		}

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			modifyBalanceHolder();
		}
	};

	OnSeekBarChangeListener onBRedChange = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			applyTempSelectedEffect();
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
		}

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			modifyBalanceHolder();
		}
	};

	OnSeekBarChangeListener onBBlueChange = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			applyTempSelectedEffect();
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
		}

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			modifyBalanceHolder();
		}
	};

	OnSeekBarChangeListener onGBlueChange = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			applyTempSelectedEffect();
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
		}

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			modifyGammaHolder();
		}
	};

	OnSeekBarChangeListener onBoostChange = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			applyTempSelectedEffect();
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
		}

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			modifyBoostHolder();
		}
	};

	private void displayEffectHolder() {
		hideEffectsBox(true);
		hideLoading();

		if (selected_effect.equals(HUE)) {

			hue_value.setProgress(180);
			modifyHueHolder();
			hue_value.setOnSeekBarChangeListener(onHueChange);

		} else if (selected_effect.equals(BRIGHTNESS)) {

			bright_value.setProgress(200);
			modifyBrightnessHolder();
			bright_value.setOnSeekBarChangeListener(onBrightChange);

		} else if (selected_effect.equals(CONTRAST)) {

			cont_value.setProgress(50);
			modifyContrastHolder();
			cont_value.setOnSeekBarChangeListener(onContChange);

		} else if (selected_effect.equals(FLIP)) {

			flip_h = false;
			flip_v = false;
			rotate_value.setProgress(0);
			modifyRotateHolder();
			rotate_value.setOnSeekBarChangeListener(onRotateChange);

		} else if (selected_effect.equals(GAMMA)) {

			gRed_value.setProgress(24);
			gGreen_value.setProgress(24);
			gBlue_value.setProgress(24);
			modifyGammaHolder();
			gRed_value.setOnSeekBarChangeListener(onGRedChange);
			gGreen_value.setOnSeekBarChangeListener(onGGreenChange);
			gBlue_value.setOnSeekBarChangeListener(onGBlueChange);

		} else if (selected_effect.equals(CDEPTH)) {

			cdepth_value.setProgress(1);
			modifyCDepthHolder();
			cdepth_value.setOnSeekBarChangeListener(onCDepthChange);

		} else if (selected_effect.equals(CBALANCE)) {

			bRed_value.setProgress(100);
			bGreen_value.setProgress(100);
			bBlue_value.setProgress(100);
			modifyBalanceHolder();
			bRed_value.setOnSeekBarChangeListener(onBRedChange);
			bGreen_value.setOnSeekBarChangeListener(onBGreenChange);
			bBlue_value.setOnSeekBarChangeListener(onBBlueChange);

		} else if (selected_effect.equals(BOOST)) {

			boost_type = 1;
			boost_value.setProgress(100);
			modifyBoostHolder();
			boost_value.setOnSeekBarChangeListener(onBoostChange);

		} else if (selected_effect.equals(SATURATION)) {

			sat_value.setProgress(100);
			modifySaturationHolder();
			sat_value.setOnSeekBarChangeListener(onSatChange);

		}

		holder_target = (LinearLayout) rootView.findViewById(
				getResources().getIdentifier("holder_" + selected_effect, "id", getActivity().getPackageName()));

		/*
		 * // SET MAX SIZE FOR TOOL BOX OF FILTER DisplayMetrics metrics =
		 * getResources().getDisplayMetrics();
		 * holder_target.getLayoutParams().width = Math.min(700,
		 * metrics.widthPixels); holder_target.requestLayout();
		 */

		holder_target.setVisibility(View.VISIBLE);
		apply_set.setVisibility(View.VISIBLE);

		animation = AnimationUtils.loadAnimation(getActivity(), R.anim.holder_bottom_fast);
		holder_target.startAnimation(animation);

		animation = AnimationUtils.loadAnimation(getActivity(), R.anim.holder_top_fast);
		apply_set.startAnimation(animation);
	}

	private void hideEffectsBox(boolean with_btn_holder) {
		if (with_btn_holder) {
			animation = AnimationUtils.loadAnimation(getActivity(), R.anim.holder_top_back_fast);
			btn_holder.startAnimation(animation);
		}

		animation = AnimationUtils.loadAnimation(getActivity(), R.anim.effect_box_back);
		effect_box.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				animation.setAnimationListener(null);
				effect_box.clearAnimation();
				effect_box.setVisibility(View.GONE);
			}
		});
	}

	private void displayEffectsBox() {
		effect_box.setVisibility(View.VISIBLE);
		animation = AnimationUtils.loadAnimation(getActivity(), R.anim.effect_box);
		effect_box.startAnimation(animation);
	}

	private void modifyRotateHolder() {
		rotate_label.setText(getString(R.string.rotate).replace(":value:", rotate_value.getProgress() + ""));
	}

	private void modifyBoostHolder() {
		int boost_label_id;
		if (boost_type == 1) {
			boost_label_id = R.string.redP;
		} else if (boost_type == 2) {
			boost_label_id = R.string.greenP;
		} else {
			boost_label_id = R.string.blueP;
		}
		boost_label.setText(getString(boost_label_id).replace(":value:", boost_value.getProgress() + ""));
	}

	private void modifyHueHolder() {
		hue_label.setText(getString(R.string.hue).replace(":value:", hue_value.getProgress() + ""));
	}

	private void modifySaturationHolder() {
		sat_label.setText(getString(R.string.sat).replace(":value:", sat_value.getProgress() + ""));
	}

	private void modifyCDepthHolder() {
		cdepth_label.setText(
				getString(R.string.cdepth).replace(":value:", (int) Math.pow(2, cdepth_value.getProgress() + 4) + ""));
	}

	private void modifyGammaHolder() {
		gRed_label.setText(
				getString(R.string.red).replace(":value:", ((double) (gRed_value.getProgress() + 2) / 10) + ""));
		gGreen_label.setText(
				getString(R.string.green).replace(":value:", ((double) (gGreen_value.getProgress() + 2) / 10) + ""));
		gBlue_label.setText(
				getString(R.string.blue).replace(":value:", ((double) (gBlue_value.getProgress() + 2) / 10) + ""));
	}

	private void modifyBalanceHolder() {
		bRed_label.setText(getString(R.string.redP).replace(":value:", (double) bRed_value.getProgress() + ""));
		bGreen_label.setText(getString(R.string.greenP).replace(":value:", (double) bGreen_value.getProgress() + ""));
		bBlue_label.setText(getString(R.string.blueP).replace(":value:", (double) bBlue_value.getProgress() + ""));
	}

	private void modifyBrightnessHolder() {
		bright_label.setText(getString(R.string.brightness).replace(":value:",
				((bright_value.getProgress() - 200) * 100 / 200) + ""));
	}

	private void modifyContrastHolder() {
		cont_label.setText(getString(R.string.contrast).replace(":value:", (cont_value.getProgress() - 50) + ""));
	}

	private void applyEffect(String effect, boolean set, boolean apply) {
		if (set) {
			effects.add(effect);
			try {
				last_bitmap.recycle();
				last_bitmap = null;
			} catch (Exception e) {
			}
		} else {
			setImage(last_bitmap.copy(last_bitmap.getConfig(), true));
		}
		if (apply) {
			new ApplyEffects(bitmap()).execute(effect);
		}
	}

	private class ApplyEffects extends AsyncTask<String, Void, Bitmap> {
		Bitmap bitmap;

		public ApplyEffects(Bitmap input_bitmap) {
			bitmap = input_bitmap.copy(input_bitmap.getConfig(), true);
			input_bitmap.recycle();
			input_bitmap = null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				image_holder.setImageBitmap(null);
			} catch (Exception e) {
			}
			if (!loading_dialog.isShowing()) {
				displayLoading();
			}
		}

		@Override
		protected Bitmap doInBackground(String... input_effects) {
			for (String effect : input_effects) {

				// Handle properties
				if (effect.startsWith(TINT) && effect.contains("_")) {

					String[] properties = effect.split("_");
					effect = properties[0];
					tint_color = Color.parseColor(properties[1]);

				} else if (effect.startsWith(HUE) && effect.contains("_")) {

					String[] properties = effect.split("_");
					effect = properties[0];
					hue_value.setProgress(Integer.parseInt(properties[1]));

				} else if (effect.startsWith(BRIGHTNESS) && effect.contains("_")) {

					String[] properties = effect.split("_");
					effect = properties[0];
					bright_value.setProgress(Integer.parseInt(properties[1]));

				} else if (effect.startsWith(CONTRAST) && effect.contains("_")) {

					String[] properties = effect.split("_");
					effect = properties[0];
					cont_value.setProgress(Integer.parseInt(properties[1]));

				} else if (effect.startsWith(FLIP) && effect.contains("_")) {

					String[] properties = effect.split("_");
					effect = properties[0];
					flip_h = (properties[1].equals("t"));
					flip_v = (properties[2].equals("t"));
					rotate_value.setProgress(Integer.parseInt(properties[3]));

				} else if (effect.startsWith(GAMMA) && effect.contains("_")) {

					String[] properties = effect.split("_");
					effect = properties[0];
					gRed_value.setProgress(Integer.parseInt(properties[1]));
					gGreen_value.setProgress(Integer.parseInt(properties[2]));
					gBlue_value.setProgress(Integer.parseInt(properties[3]));

				} else if (effect.startsWith(CDEPTH) && effect.contains("_")) {

					String[] properties = effect.split("_");
					effect = properties[0];
					cdepth_value.setProgress(Integer.parseInt(properties[1]));

				} else if (effect.startsWith(CBALANCE) && effect.contains("_")) {

					String[] properties = effect.split("_");
					effect = properties[0];
					bRed_value.setProgress(Integer.parseInt(properties[1]));
					bGreen_value.setProgress(Integer.parseInt(properties[2]));
					bBlue_value.setProgress(Integer.parseInt(properties[3]));

				} else if (effect.startsWith(BOOST) && effect.contains("_")) {

					String[] properties = effect.split("_");
					effect = properties[0];
					boost_type = Integer.parseInt(properties[1]);
					boost_value.setProgress(Integer.parseInt(properties[2]));

				} else if (effect.startsWith(SATURATION) && effect.contains("_")) {

					String[] properties = effect.split("_");
					effect = properties[0];
					sat_value.setProgress(Integer.parseInt(properties[1]));

				}

				// Apply Filters
				if (effect.equals(BRIGHTNESS)) {
					try {
						bitmap = BitmapProcessing.brightness(bitmap, bright_value.getProgress() - 200);
					} catch (Exception e) {
						Toaster.make(getActivity(), R.string.error_apply_effect);
					}
				} else if (effect.equals(CONTRAST)) {
					try {
						bitmap = BitmapProcessing.contrast(bitmap, cont_value.getProgress() - 50);
					} catch (Exception e) {
						Toaster.make(getActivity(), R.string.error_apply_effect);
					}
				} else if (effect.equals(NOISE)) {
					try {
						bitmap = BitmapProcessing.noise(bitmap);
					} catch (Exception e) {
						Toaster.make(getActivity(), R.string.error_apply_effect);
					}
				} else if (effect.equals(FLIP)) {
					try {
						bitmap = BitmapProcessing.rotate(BitmapProcessing.flip(bitmap, flip_h, flip_v),
								(float) rotate_value.getProgress());
					} catch (Exception e) {
						Toaster.make(getActivity(), R.string.error_apply_effect);
					}
				} else if (effect.equals(HUE)) {
					try {
						bitmap = BitmapProcessing.hue(bitmap, (float) hue_value.getProgress());
					} catch (Exception e) {
						Toaster.make(getActivity(), R.string.error_apply_effect);
					}
				} else if (effect.equals(TINT)) {
					try {
						bitmap = BitmapProcessing.tint(bitmap, tint_color);
					} catch (Exception e) {
						Toaster.make(getActivity(), R.string.error_apply_effect);
					}
				} else if (effect.equals(GAMMA)) {
					try {
						bitmap = BitmapProcessing.gamma(bitmap, (double) gRed_value.getProgress(),
								(double) gGreen_value.getProgress(), (double) gBlue_value.getProgress());
					} catch (Exception e) {
						Toaster.make(getActivity(), R.string.error_apply_effect);
					}
				} else if (effect.equals(INVERT)) {
					try {
						bitmap = BitmapProcessing.invert(bitmap);
					} catch (Exception e) {
						Toaster.make(getActivity(), R.string.error_apply_effect);
					}
				} else if (effect.equals(GRAYSCALE)) {
					try {
						bitmap = BitmapProcessing.grayscale(bitmap);
					} catch (Exception e) {
						Toaster.make(getActivity(), R.string.error_apply_effect);
					}
				} else if (effect.equals(SEPIA)) {
					try {
						bitmap = BitmapProcessing.sepia(bitmap);
					} catch (Exception e) {
						Toaster.make(getActivity(), R.string.error_apply_effect);
					}
				} else if (effect.equals(SHARPEN)) {
					try {
						bitmap = BitmapProcessing.sharpen(bitmap);
					} catch (Exception e) {
						Toaster.make(getActivity(), R.string.error_apply_effect);
					}
				} else if (effect.equals(GAUSSIAN)) {
					try {
						bitmap = BitmapProcessing.gaussian(bitmap);
					} catch (Exception e) {
						Toaster.make(getActivity(), R.string.error_apply_effect);
					}
				} else if (effect.equals(CBALANCE)) {
					try {
						bitmap = BitmapProcessing.cfilter(bitmap, (double) bRed_value.getProgress(),
								(double) bGreen_value.getProgress(), (double) bBlue_value.getProgress());
					} catch (Exception e) {
						Toaster.make(getActivity(), R.string.error_apply_effect);
					}
				} else if (effect.equals(CDEPTH)) {
					try {
						bitmap = BitmapProcessing.cdepth(bitmap, (int) Math.pow(2, 7 - cdepth_value.getProgress()));
					} catch (Exception e) {
						Toaster.make(getActivity(), R.string.error_apply_effect);
					}
				} else if (effect.equals(EMBOSS)) {
					try {
						bitmap = BitmapProcessing.emboss(bitmap);
					} catch (Exception e) {
						Toaster.make(getActivity(), R.string.error_apply_effect);
					}
				} else if (effect.equals(BOOST)) {
					try {
						bitmap = BitmapProcessing.boost(bitmap, boost_type, (float) boost_value.getProgress());
					} catch (Exception e) {
						Toaster.make(getActivity(), R.string.error_apply_effect);
					}
				} else if (effect.equals(SKETCH)) {
					try {
						bitmap = BitmapProcessing.sketch(BitmapProcessing.grayscale(bitmap));
					} catch (Exception e) {
						Toaster.make(getActivity(), R.string.error_apply_effect);
					}
				} else if (effect.equals(VIGNETTE)) {
					try {
						bitmap = BitmapProcessing.vignette(bitmap);
					} catch (Exception e) {
						Toaster.make(getActivity(), R.string.error_apply_effect);
					}
				} else if (effect.equals(SATURATION)) {
					try {
						bitmap = BitmapProcessing.saturation(bitmap, sat_value.getProgress());
					} catch (Exception e) {
						Toaster.make(getActivity(), R.string.error_apply_effect);
					}
				}
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (save_status) {

				int counter = 0;
				String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("."));

				File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

				File file = new File(path, "EffectsPro");

				try {
					file.mkdirs();
				} catch (Exception e) {
				}

				file = new File(path, "EffectsPro/" + fileName + ".jpg");

				while (file.exists()) {
					counter++;
					file = new File(path, "EffectsPro/" + fileName + "(" + counter + ").jpg");
				}

				outputURL = file.getAbsolutePath();

				new BitmapWriterWorker(file, bitmap).execute();
			} else {
				if (btn_holder.getVisibility() == View.GONE) {
					flyIn();
				}
				setImage(bitmap);
			}
		}
	}

	private class BitmapWriterWorker extends BitmapWriter {

		public BitmapWriterWorker(File input_file, Bitmap input_bitmap) {
			super(input_file, input_bitmap);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (result) {
				Toaster.make(getActivity(), getString(R.string.saved).replace(":url:", outputURL));
			} else {
				Toaster.make(getActivity(), R.string.save_failed);
			}
			backToMain();
		}

	}

	private void loadImage() throws Exception {
		BitmapWorkerTask bitmaporker = new BitmapWorkerTask();
		bitmaporker.execute();
	}

	private class BitmapWorkerTask extends AsyncTask<Void, Void, Bitmap> {
		DisplayMetrics metrics;
		BitmapLoader bitmapLoader;

		public BitmapWorkerTask() {
			metrics = getResources().getDisplayMetrics();
			imageUrl = UriToUrl.get(getActivity(), imageUri);
			bitmapLoader = new BitmapLoader();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			toolbox.setVisibility(View.GONE);
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(Void... arg0) {
			try {
				return bitmapLoader.load(getActivity(), new int[] { metrics.widthPixels, metrics.heightPixels },
						imageUrl);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap != null) {
				toolbox.setVisibility(View.VISIBLE);
				setImage(bitmap);
			} else {
				Toaster.make(getActivity(), R.string.error_img_not_found);
				backToMain();
			}
		}
	}

	public void onBackPressed() {
		if (apply_set.getVisibility() == View.VISIBLE) {
			cancelSelectedEffect();
		} else {
			displayBackDialog();
		}
	}

	private void cancelSelectedEffect() {
		if (!loading_dialog.isShowing()) {
			setImage(last_bitmap.copy(last_bitmap.getConfig(), true));

			try {
				last_bitmap.recycle();
				last_bitmap = null;
			} catch (Exception e) {
			}

			hideEffectHolder();
		}

	}

	private void displayBackDialog() {
		final BackDialog dialog = new BackDialog(getActivity());

		dialog.positive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				backToMain();
			}
		});

		dialog.negative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void recycleBitmap() {
		try {
			bitmap().recycle();
			image_holder.setImageBitmap(null);
		} catch (Exception e) {
		}
	}

	private void backToMain() {
		recycleBitmap();

		if (loading_dialog.isShowing()) {
			hideLoading();
		}

		UtilFunctions.switchContent(R.id.frag_root, UtilFunctions.SELECT_IMAGE_FRAGMENT, (SampleActivity) getActivity(),
				AnimationType.SLIDE_RIGHT);

		if (source_id == 1) {
			UriToUrl.deleteUri(getActivity(), imageUri);
		}

		if (save_status || source_id == 1) {
			UriToUrl.sendBroadcast(getActivity(), outputURL);
		}

		// finish();
	}

	@SuppressWarnings("unused")
	private void reloadImage() {
		new RevertEffects().execute();
	}

	private class RevertEffects extends AsyncTask<Void, Void, Bitmap> {
		@Override
		protected void onPreExecute() {
			image_holder.setVisibility(View.GONE);
			super.onPreExecute();
		}

		@Override
		protected Bitmap doInBackground(Void... arg0) {
			DisplayMetrics metrics = getResources().getDisplayMetrics();
			BitmapLoader bitmapLoader = new BitmapLoader();

			recycleBitmap();

			try {
				return bitmapLoader.load(getActivity(), new int[] { metrics.widthPixels, metrics.heightPixels },
						imageUrl);
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap != null) {
				image_holder.setVisibility(View.VISIBLE);

				if (effects.size() < 2) {
					effects = new ArrayList<String>();
					setImage(bitmap);
					flyIn();
				} else {
					effects.remove(effects.size() - 1);
					String[] effects_array = new String[effects.size()];
					effects_array = effects.toArray(effects_array);
					new ApplyEffects(bitmap).execute(effects_array);
				}
			} else {
				Toaster.make(getActivity(), R.string.error_img_not_found);
				backToMain();
			}
		}
	}

	@SuppressWarnings("unused")
	private void saveImage() {
		new recycleAllBitmaps().execute();
	}

	private class recycleAllBitmaps extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			recycleBitmap();

			ImageView imageView;
			int id;

			for (int i = 1; i < 20; i++) {
				id = getResources().getIdentifier("effect_img" + i, "id", getActivity().getPackageName());
				imageView = (ImageView) rootView.findViewById(id);

				try {
					((BitmapDrawable) imageView.getDrawable()).getBitmap().recycle();
					imageView.setImageBitmap(null);
				} catch (Exception e) {
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			save_status = true;

			ViewGroup root = (ViewGroup) toolbox.getRootView();
			root.removeView(toolbox);
			root.removeView(image_holder);

			System.gc();
			new OriginalImageLoader().execute();
		}

	}

	private class OriginalImageLoader extends AsyncTask<Void, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(Void... arg0) {
			BitmapLoader bitmapLoader = new BitmapLoader();
			try {
				return bitmapLoader.load(getActivity(), imageUrl);
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap != null) {
				String[] effects_array = new String[effects.size()];
				effects_array = effects.toArray(effects_array);
				new ApplyEffects(bitmap).execute(effects.toArray(effects_array));
			} else {
				Toaster.make(getActivity(), R.string.error_img_not_found);
				backToMain();
			}
		}

	}

	// @Override
	// protected void onSaveInstanceState(Bundle outState) {
	// super.onSaveInstanceState(outState);
	// outState.putString(AppContants.KEY_URL, imageUrl);
	// outState.putInt(AppContants.KEY_SOURCE_ID, source_id);
	// outState.putStringArrayList(AppContants.KEY_EFFECTS_LIST, effects);
	// outState.putParcelable(AppContants.KEY_BITMAP, bitmap());
	// }

	private void displayLoading() {
		loading_dialog.show();
	}

}
