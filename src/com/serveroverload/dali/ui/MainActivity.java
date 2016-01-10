package com.serveroverload.dali.ui;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.serveroverload.dali.R;
import com.serveroverload.dali.canvas.CanvasDrawElements;
import com.serveroverload.dali.canvas.CanvasDrawElements.Drawer;
import com.serveroverload.dali.canvas.CanvasDrawElements.Mode;
import com.serveroverload.dali.canvas.CanvasGradientEffects;
import com.serveroverload.dali.canvas.CanvasPathEffect;
import com.serveroverload.dali.ui.customeview.ColorPickerDialog;
import com.serveroverload.dali.ui.customeview.ColorPickerDialog.OnColorSelectedListener;

public class MainActivity extends Activity {

	private CanvasDrawElements canvas = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_ACTION_BAR);

		
		// Select Gradient Mode
		final String[] gradientModes = new String[] { "BetterRadientGradient",
				"ComposeShader", "RadielGradient", "SweepGradient",
				"SimpleSweep", "RainBowDhader", "LinearGradient",
				"BetterRadielGradient" };
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getBaseContext(),
				android.R.layout.simple_spinner_dropdown_item, gradientModes);

		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		ActionBar.OnNavigationListener gradientNavigationListener = new OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int itemPosition,
					long itemId) {
				
			//	setContentView(new WaterWaveView(MainActivity.this));

				setContentView(new CanvasGradientEffects(MainActivity.this,
						itemPosition));

				return false;
			}
		};

//		ActionBar.OnNavigationListener canvasNavigationListener = new OnNavigationListener() {
//
//			@Override
//			public boolean onNavigationItemSelected(int itemPosition,
//					long itemId) {
//
//				setContentView(new SampleView(MainActivity.this,
//						itemPosition));
//
//				// Toast.makeText(getBaseContext(),
//				// "You selected : " + gradientModes[itemPosition],
//				// Toast.LENGTH_SHORT).show();
//				return false;
//			}
//		};

		/**
		 * Setting dropdown items and item navigation listener for the actionbar
		 */
		getActionBar().setListNavigationCallbacks(adapter,
				gradientNavigationListener);

		setContentView(new CanvasPathEffect(MainActivity.this));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_gradient) {

			setContentView(new CanvasGradientEffects(MainActivity.this, 0));

			return true;
		}

		else if (id == R.id.action_canvas) {

			CanvasDrawElements canvasView = new CanvasDrawElements(
					getApplicationContext());

			// //Text Mode
			//
			// canvasView.setMode(Mode.TEXT);
			//
			// canvasView.setDrawer(Drawer.PEN);

			// Draw Mode Circle

			// canvasView.setMode(Mode.DRAW);
			//
			// canvasView.setDrawer(Drawer.CIRCLE);

			// Draw Rectangles
			canvasView.setMode(Mode.DRAW);

			canvasView.setDrawer(Drawer.RECTANGLE);

			// Draw

			// canvasView.setMode(Mode.DRAW);
			//
			// canvasView.setDrawer(Drawer.ELLIPSE);

			// canvasView.setMode(Mode.DRAW);
			//
			// canvasView.setDrawer(Drawer.QUBIC_BEZIER);

			setContentView(canvasView);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public CanvasDrawElements getCanvas() {
		return this.canvas;
	}

	private void showColorPickerDialogDemo() {

		int initialColor = Color.WHITE;

		ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this,
				initialColor, new OnColorSelectedListener() {

					@Override
					public void onColorSelected(int color) {
						showToast(color);
					}

				});
		colorPickerDialog.show();

	}

	private void showToast(int color) {
		String rgbString = "R: " + Color.red(color) + " B: "
				+ Color.blue(color) + " G: " + Color.green(color);
		Toast.makeText(this, rgbString, Toast.LENGTH_SHORT).show();
	}
}
