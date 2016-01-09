package com.serveroverload.dali.ui;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;

import com.serveroverload.dali.R;
import com.serveroverload.dali.canvas.CanvasView;
import com.serveroverload.dali.canvas.CanvasView.Drawer;
import com.serveroverload.dali.canvas.CanvasView.Mode;
import com.serveroverload.dali.canvas.CircularDragCanvasView;

public class MainActivity extends Activity {

	private CanvasView canvas = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_ACTION_BAR);

		/** An array of strings to populate dropdown list */
		final String[] gradientModes = new String[] { "BetterRadientGradient",
				"ComposeShader", "RadielGradient", "SweepGradient",
				"SimpleSweep", "RainBowDhader", "LinearGradient",
				"BetterRadielGradient" };

		final String[] vanvasModes = new String[] { "Text", "ComposeShader",
				"RadielGradient", "SweepGradient", "SimpleSweep",
				"RainBowDhader", "LinearGradient", "BetterRadielGradient" };

		/** Create an array adapter to populate dropdownlist */
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getBaseContext(),
				android.R.layout.simple_spinner_dropdown_item, gradientModes);

		/** Enabling dropdown list navigation for the action bar */
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		/** Defining Navigation listener */
		ActionBar.OnNavigationListener gradientNavigationListener = new OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int itemPosition,
					long itemId) {

				setContentView(new CircularDragCanvasView(MainActivity.this,
						itemPosition));

				// Toast.makeText(getBaseContext(),
				// "You selected : " + gradientModes[itemPosition],
				// Toast.LENGTH_SHORT).show();
				return false;
			}
		};

		ActionBar.OnNavigationListener canvasNavigationListener = new OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int itemPosition,
					long itemId) {

				setContentView(new CircularDragCanvasView(MainActivity.this,
						itemPosition));

				// Toast.makeText(getBaseContext(),
				// "You selected : " + gradientModes[itemPosition],
				// Toast.LENGTH_SHORT).show();
				return false;
			}
		};

		/**
		 * Setting dropdown items and item navigation listener for the actionbar
		 */
		getActionBar().setListNavigationCallbacks(adapter,
				gradientNavigationListener);

		setContentView(new CircularDragCanvasView(MainActivity.this, 0));

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

			setContentView(new CircularDragCanvasView(MainActivity.this, 0));

			return true;
		}

		else if (id == R.id.action_canvas) {

			CanvasView canvasView = new CanvasView(getApplicationContext());
			
//			//Text Mode
//
//			canvasView.setMode(Mode.TEXT);
//			
//			canvasView.setDrawer(Drawer.PEN);
			
			//Draw Mode Circle

//			canvasView.setMode(Mode.DRAW);
//			
//			canvasView.setDrawer(Drawer.CIRCLE);
			
			
			//Draw Rectangles
			canvasView.setMode(Mode.DRAW);
			
			canvasView.setDrawer(Drawer.RECTANGLE);
			
			//Draw 
			
//			canvasView.setMode(Mode.DRAW);
//			
//			canvasView.setDrawer(Drawer.ELLIPSE);
			
//	canvasView.setMode(Mode.DRAW);
//			
//			canvasView.setDrawer(Drawer.QUBIC_BEZIER);
			
			setContentView(canvasView);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public CanvasView getCanvas() {
		return this.canvas;
	}
}
