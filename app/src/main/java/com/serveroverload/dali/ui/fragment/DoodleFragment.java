package com.serveroverload.dali.ui.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import com.serveroverload.dali.R;
import com.serveroverload.dali.canvas.CanvasDrawElements;
import com.serveroverload.dali.canvas.CanvasDrawElements.Drawer;
import com.serveroverload.dali.canvas.CanvasDrawElements.Mode;
import com.serveroverload.dali.colorbox.GradientBox;
import com.serveroverload.dali.helper.AppContants;
import com.serveroverload.dali.helper.BitmapLoader;
import com.serveroverload.dali.helper.DiskUtil;
import com.serveroverload.dali.helper.UriToUrl;
import com.serveroverload.dali.helper.UtilFunctions;
import com.serveroverload.dali.helper.UtilFunctions.AnimationType;
import com.serveroverload.dali.ui.SampleActivity;
import com.serveroverload.dali.ui.customeview.ColorPickerDialog;
import com.serveroverload.dali.ui.customeview.ColorPickerDialog.OnColorSelectedListener;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class DoodleFragment extends Fragment {

	public static Fragment newInstance() {
		return new DoodleFragment();
	}

	private CanvasDrawElements canvas;
	private View rootView;

	TextView opacityValue, widthValue, paintMode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.doodle_fragment_new, container, false);

		SeekBar progressBar = (SeekBar) rootView.findViewById(R.id.progress_paint_size);

		SeekBar progressBar1 = (SeekBar) rootView.findViewById(R.id.progress_paint_opacity);

		canvas = (CanvasDrawElements) rootView.findViewById(R.id.doodle_container);

		loadBitMap();

		paintMode = (TextView) rootView.findViewById(R.id.current_paint_mode);

		widthValue = (TextView) rootView.findViewById(R.id.size_value);

		opacityValue = (TextView) rootView.findViewById(R.id.opacity_value);

		rootView.findViewById(R.id.btn_undo).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				canvas.undo();

			}
		});

		rootView.findViewById(R.id.btn_redo).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				canvas.redo();

			}
		});

		rootView.findViewById(R.id.btn_clear).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				canvas.clear();

			}
		});

		rootView.findViewById(R.id.eraser).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				canvas.setMode(Mode.ERASER);
				canvas.setDrawer(Drawer.PEN);

				paintMode.setText(getResources().getString(R.string.fa_eraser));

			}
		});

		rootView.findViewById(R.id.pencil).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				canvas.setMode(Mode.DRAW);
				canvas.setDrawer(Drawer.PEN);

				paintMode.setText(getResources().getString(R.string.fa_pencil));

			}
		});

		rootView.findViewById(R.id.text).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showTextVBoxDialog(getActivity());

				canvas.setMode(Mode.DRAW);
				canvas.setMode(Mode.TEXT);

				paintMode.setText(getResources().getString(R.string.fa_text_height));

			}
		});

		rootView.findViewById(R.id.line).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				canvas.setMode(Mode.DRAW);
				canvas.setDrawer(Drawer.LINE);

			}
		});

		rootView.findViewById(R.id.quadratic).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				canvas.setMode(Mode.DRAW);
				canvas.setDrawer(Drawer.QUADRATIC_BEZIER);

			}
		});

		rootView.findViewById(R.id.cubic).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				canvas.setMode(Mode.DRAW);
				canvas.setDrawer(Drawer.QUBIC_BEZIER);

			}
		});

		rootView.findViewById(R.id.circle).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				canvas.setMode(Mode.DRAW);
				canvas.setDrawer(Drawer.CIRCLE);

			}
		});

		rootView.findViewById(R.id.elipse).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				canvas.setMode(Mode.DRAW);
				canvas.setDrawer(Drawer.ELLIPSE);

			}
		});

		rootView.findViewById(R.id.rectangle).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				canvas.setMode(Mode.DRAW);
				canvas.setDrawer(Drawer.RECTANGLE);

			}
		});

		rootView.findViewById(R.id.poly).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				canvas.setMode(Mode.DRAW);
				canvas.setDrawer(Drawer.RECTANGLE);

			}
		});

		rootView.findViewById(R.id.radiel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				canvas.setGrdientStyle(GradientBox.RADIEL_GRADIENT);
				// canavas.setDrawer(Drawer.RECTANGLE);

			}
		});

		rootView.findViewById(R.id.logo).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				canvas.setMode(Mode.LOGO);
				// canavas.setDrawer(Drawer.RECTANGLE);

				paintMode.setText(getResources().getString(R.string.fa_image));

			}
		});

		rootView.findViewById(R.id.brush).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				canvas.setMode(Mode.LOGO);
				// canavas.setDrawer(Drawer.RECTANGLE);

				paintMode.setText(getResources().getString(R.string.fa_paint_brush));

			}
		});

		rootView.findViewById(R.id.btn_color).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showColorPickerDialogDemo();

			}
		});

		progressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

				if (canvas.getMode() == Mode.TEXT) {
					canvas.setFontSize(progress);
				} else {
					canvas.setPaintStrokeWidth(progress);
				}

				widthValue.setText(String.valueOf(progress) + "/500");

			}
		});

		progressBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

				canvas.setOpacity(progress);

				opacityValue.setText(String.valueOf(progress) + "/255");

			}
		});

		((SampleActivity) getActivity()).getSaveImage().setVisible(true);
		((SampleActivity) getActivity()).getSaveImage().setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {

				// save drawing
				canvas.setDrawingCacheEnabled(true);

				try {
					if (canvas.getDrawingCache().compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(
							new File(DiskUtil.getDoodleDirectory(), UUID.randomUUID().toString())))) {
						
						Toast.makeText(getActivity(), "Drawing saved to Gallery!", Toast.LENGTH_SHORT).show();
						
					} else {
						
						Toast.makeText(getActivity(), "Oops! Image could not be saved.", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {

					e.printStackTrace();
					Toast.makeText(getActivity(), "Oops! Image could not be saved.", Toast.LENGTH_SHORT).show();
					// Log.e("Error--------->", e.toString());
				}

				// String imgSaved =
				// MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
				// canvas.getDrawingCache(), UUID.randomUUID().toString() +
				// ".png", "drawing");
				//
				// if (imgSaved != null) {
				// Toast.makeText(getActivity(), "Drawing saved to Gallery!",
				// Toast.LENGTH_SHORT).show();
				// ;
				// } else {
				// Toast.makeText(getActivity(), "Oops! Image could not be
				// saved.", Toast.LENGTH_SHORT).show();
				// }

				canvas.destroyDrawingCache();

				return false;
			}
		});

		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

					showSaveAlert(getActivity());

				}
				return true;
			}
		});

		return rootView;
	}

	private void showTextVBoxDialog(final Context context) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setTitle("Add Text");
		alertDialog.setMessage("Enter Text");

		final EditText input = new EditText(context);
		// LinearLayout.LayoutParams lp = new
		// LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
		// LinearLayout.LayoutParams.WRAP_CONTENT);
		// input.setLayoutParams(lp);

		alertDialog.setView(input);
		alertDialog.setIcon(R.drawable.draw);

		alertDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			@TargetApi(Build.VERSION_CODES.GINGERBREAD)
			public void onClick(DialogInterface dialog, int which) {
				if (!input.getText().toString().isEmpty()) {

					canvas.setText(input.getText().toString());

				} else {
					Toast.makeText(context, "Empty Text", Toast.LENGTH_SHORT).show();
				}

				dialog.cancel();
			}

		});

		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}

		});

		alertDialog.show();
	}

	private void showColorPickerDialogDemo() {

		int initialColor = Color.WHITE;

		ColorPickerDialog colorPickerDialog = new ColorPickerDialog(getActivity(), initialColor,
				new OnColorSelectedListener() {

					@Override
					public void onColorSelected(int color) {

						canvas.setPaintStrokeColor(color);

						((TextView) rootView.findViewById(R.id.btn_color)).setTextColor(color);
					}

				});
		colorPickerDialog.show();

	}

	/**
	 * @return
	 * 
	 */
	public void loadBitMap() {
		if (null != AppContants.doodleImageURL) {
			BitmapLoader bitmapLoader = new BitmapLoader();
			try {
				canvas.drawBitmap(bitmapLoader.load(getActivity(),
						UriToUrl.get(getActivity(), Uri.parse(AppContants.doodleImageURL))));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	void showSaveAlert(final Context context) {
		AlertDialog.Builder saveDialog = new AlertDialog.Builder(context);
		saveDialog.setTitle("Save drawing");
		saveDialog.setMessage("Save drawing to device Gallery?");
		saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// save drawing

				canvas.setDrawingCacheEnabled(true);

				String imgSaved = MediaStore.Images.Media.insertImage(context.getContentResolver(),
						canvas.getDrawingCache(), UUID.randomUUID().toString() + ".png", "drawing");

				if (imgSaved != null) {
					Toast.makeText(context, "Drawing saved to Gallery!", Toast.LENGTH_SHORT).show();
					;
				} else {
					Toast.makeText(context, "Oops! Image could not be saved.", Toast.LENGTH_SHORT).show();
				}

				canvas.destroyDrawingCache();
			}
		});

		saveDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		saveDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				UtilFunctions.switchContent(R.id.frag_root, UtilFunctions.HOME_FRAGMENT_TAG,
						(SampleActivity) getActivity(), AnimationType.SLIDE_RIGHT);
				dialog.cancel();

			}
		});
		saveDialog.show();
	}

}