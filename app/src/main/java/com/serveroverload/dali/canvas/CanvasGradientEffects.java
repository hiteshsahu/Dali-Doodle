package com.serveroverload.dali.canvas;

import java.util.HashSet;

import com.serveroverload.dali.colorbox.GradientBox;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * The Class CircularDragView.
 */
public class CanvasGradientEffects extends View {

	private static final int DRAG_CIRCLE_RADIUS = 50;

	private static final int DUMMY_CIRCLE_RADIUS = 80;

	private static final int SUM_DUMMYRADI_DRAG_RADI = DRAG_CIRCLE_RADIUS
			+ DUMMY_CIRCLE_RADIUS;

	/** The screen half width. */
	private int SCREEN_HALF_WIDTH;

	/** The screen half height. */
	private int SCREEN_HALF_HEIGHT;

	/** The Constant TAG. */
	private static final String TAG = CanvasGradientEffects.class
			.getSimpleName();

	/** The flag. */
	boolean flag = false;

	private Paint paintBoxMode;
	private int currentPaintMode;
	private Paint paint;

	/** Paint to draw circles. */
	private Paint mCirclePaint;

	/** The stroke paint. */
	Paint strokePaint;

	/** The Constant CIRCLES_LIMIT. */
	private static final int CIRCLES_LIMIT = 3;

	/** All available circles. */
	private HashSet<CircleArea> mCircles = new HashSet<CircleArea>(
			CIRCLES_LIMIT);

	/** The m circle pointer. */
	private SparseArray<CircleArea> mCirclePointer = new SparseArray<CircleArea>(
			CIRCLES_LIMIT);

	/** The screen width. */
	private int screenWidth;

	/** The screen height. */
	private int screenHeight;

	private GradientBox paintBox;

	/**
	 * Stores data about single circle.
	 */
	private static class CircleArea {

		/** The radius. */
		int radius;

		/** The center x. */
		int centerX;

		/** The center y. */
		int centerY;

		/**
		 * Instantiates a new circle area.
		 *
		 * @param centerX
		 *            the center x
		 * @param centerY
		 *            the center y
		 * @param radius
		 *            the radius
		 */
		CircleArea(int centerX, int centerY, int radius) {
			this.radius = radius;
			this.centerX = centerX;
			this.centerY = centerY;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Circle[" + centerX + ", " + centerY + ", " + radius + "]";
		}
	}

	/**
	 * Default constructor.
	 *
	 * @param context
	 *            {@link android.content.Context}
	 */
	public CanvasGradientEffects(final Context context, int paintMode) {
		super(context);

		this.currentPaintMode = paintMode;

		init(context, paintMode);
	}

	/**
	 * Inits the.
	 *
	 * @param context
	 *            the ct
	 */
	private void init(final Context context, int paintMode) {

		paintBox = new GradientBox();

		mCirclePaint = new Paint();
		mCirclePaint.setColor(Color.argb(255, 255, 205, 255));
		mCirclePaint.setStrokeWidth(40);
		mCirclePaint.setStyle(Paint.Style.FILL);

		strokePaint = new Paint();
		strokePaint.setColor(Color.argb(255, 255, 255, 255));
		strokePaint.setStyle(Paint.Style.STROKE);
		strokePaint.setStrokeWidth(3);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		screenWidth = MeasureSpec.getSize(widthMeasureSpec);
		screenHeight = MeasureSpec.getSize(heightMeasureSpec);

		SCREEN_HALF_WIDTH = screenWidth / 2;

		SCREEN_HALF_HEIGHT = screenHeight / 2;

		this.setMeasuredDimension(screenWidth, screenHeight);

		paintBox.initPaintBox(screenWidth, screenHeight, SCREEN_HALF_WIDTH,
				SCREEN_HALF_HEIGHT);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	public void onDraw(final Canvas canv) {

		Log.e(TAG, "drawCircle screenWidth  " + screenWidth + " screenHeight "
				+ screenHeight);

		if (null == paintBoxMode) {

			switch (currentPaintMode) {
			case 0:

				paintBoxMode = paintBox.getBetterRadientGradient();
				break;

			case 1:

				paintBoxMode = paintBox.getComposeShader();
				break;

			case 2:

				paintBoxMode = paintBox.getRadielGradient();
				break;

			case 3:

				paintBoxMode = paintBox.getSweepGradient();
				break;

			case 4:

				paintBoxMode = paintBox.getSimpleSweepPaint();
				break;

			case 5:

				paintBoxMode = paintBox.getRainBowDhader();
				break;

			case 6:

				paintBoxMode = paintBox.getLinearGradient(canv);
				break;

			case 7:

				paintBoxMode = paintBox.getBetterRadielGradient(canv);
				break;

			default:
				break;
			}
		}

		canv.drawCircle(SCREEN_HALF_WIDTH, SCREEN_HALF_HEIGHT,
				SCREEN_HALF_WIDTH, paintBoxMode);

		if (null == paint) {
			paint = new Paint();

		}

		paint.setStyle(Paint.Style.FILL);
		// canv.drawPaint(paint);
		paint.setColor(Color.BLACK);
		canv.drawCircle(SCREEN_HALF_WIDTH, SCREEN_HALF_HEIGHT,
				DUMMY_CIRCLE_RADIUS, paint);
		//
		// // Background
		paint.setStyle(Paint.Style.STROKE);
		// canv.drawPaint(paint);
		paint.setColor(Color.MAGENTA);
		canv.drawCircle(SCREEN_HALF_WIDTH, SCREEN_HALF_HEIGHT,
				SCREEN_HALF_WIDTH, paint);

		// Drawable d =
		// getResources().getDrawable(R.drawable.mood_wheel_black_bg);
		// d.draw(canv);

		for (CircleArea circle : mCircles) {

			canv.drawCircle(circle.centerX, circle.centerY, circle.radius,
					mCirclePaint);

			canv.drawCircle(circle.centerX, circle.centerY, circle.radius,
					strokePaint);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(@Nullable final MotionEvent event) {
		boolean handled = false;
		CircleArea touchedCircle;
		int xTouch;
		int yTouch;
		int pointerId;
		int actionIndex = event.getActionIndex();

		// get touch event coordinates and make transparent circle from it
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			// it's the first pointer, so clear all existing pointers data
			clearCirclePointer();

			xTouch = (int) event.getX(0);
			yTouch = (int) event.getY(0);

			// check if we've touched inside some circle
			touchedCircle = obtainTouchedCircle(xTouch, yTouch);
			touchedCircle.centerX = xTouch;
			touchedCircle.centerY = yTouch;
			mCirclePointer.put(event.getPointerId(0), touchedCircle);

			invalidate();
			handled = true;
			break;
		case MotionEvent.ACTION_MOVE:
			final int pointerCount = event.getPointerCount();

			for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
				// Some pointer has moved, search it by pointer id
				pointerId = event.getPointerId(actionIndex);

				xTouch = (int) event.getX(actionIndex);
				yTouch = (int) event.getY(actionIndex);

				Log.e(TAG, "xTouch" + xTouch + "  yTouch" + yTouch);

				touchedCircle = mCirclePointer.get(pointerId);

				if (null != touchedCircle) {

					if (xTouch <= DRAG_CIRCLE_RADIUS) {

						touchedCircle.centerX = DRAG_CIRCLE_RADIUS;

					}

					else if (xTouch >= SCREEN_HALF_WIDTH
							+ SCREEN_HALF_WIDTH
							* Math.cos(getAngle(event.getX(actionIndex),
									event.getY(actionIndex)))) {

						touchedCircle.centerX = (int) (SCREEN_HALF_WIDTH * Math
								.cos(getAngle(event.getX(actionIndex),
										event.getY(actionIndex))));

					} else if (xTouch >= screenWidth - DRAG_CIRCLE_RADIUS) {

						touchedCircle.centerX = screenWidth
								- DRAG_CIRCLE_RADIUS;

					} else {
						touchedCircle.centerX = xTouch;
					}

					if (yTouch <= DRAG_CIRCLE_RADIUS) {

						touchedCircle.centerY = DRAG_CIRCLE_RADIUS;

					} else if (yTouch >= SCREEN_HALF_WIDTH
							+ SCREEN_HALF_WIDTH
							* Math.sin(getAngle(event.getX(actionIndex),
									event.getY(actionIndex)))) {

						touchedCircle.centerY = (int) (SCREEN_HALF_WIDTH * Math
								.sin(getAngle(event.getX(actionIndex),
										event.getY(actionIndex))));

					}

					else if (yTouch >= screenHeight - DRAG_CIRCLE_RADIUS) {

						touchedCircle.centerY = screenHeight
								- DRAG_CIRCLE_RADIUS;

					} else {
						touchedCircle.centerY = yTouch;
					}

					// Outer circle

					if ((xTouch - SCREEN_HALF_WIDTH)
							* (xTouch - SCREEN_HALF_WIDTH)
							+ (yTouch - SCREEN_HALF_HEIGHT)
							* (yTouch - SCREEN_HALF_HEIGHT) > ((SCREEN_HALF_WIDTH - DRAG_CIRCLE_RADIUS) * (SCREEN_HALF_WIDTH - DRAG_CIRCLE_RADIUS)))
						;
					{
						Log.e(TAG, "@@@@@@@@@@@collided@@@@@@@@@@@ ");

						touchedCircle.centerY = (int) (SCREEN_HALF_HEIGHT + (SCREEN_HALF_WIDTH - DRAG_CIRCLE_RADIUS)
								* Math.sin(getAngle(event.getX(actionIndex),
										event.getY(actionIndex))));

						touchedCircle.centerX = (int) (SCREEN_HALF_WIDTH + (SCREEN_HALF_WIDTH - DRAG_CIRCLE_RADIUS)
								* Math.cos(getAngle(event.getX(actionIndex),
										event.getY(actionIndex))));
					}

					// Inner circle

					if ((xTouch - SCREEN_HALF_WIDTH)
							* (xTouch - SCREEN_HALF_WIDTH)
							+ (yTouch - SCREEN_HALF_HEIGHT)
							* (yTouch - SCREEN_HALF_HEIGHT) < (SUM_DUMMYRADI_DRAG_RADI * SUM_DUMMYRADI_DRAG_RADI)) {
						Log.e(TAG,
								"#############collided############ "
										+ SUM_DUMMYRADI_DRAG_RADI
										* Math.sin(getAngle(
												event.getX(actionIndex),
												event.getY(actionIndex))));

						touchedCircle.centerY = (int) (SCREEN_HALF_HEIGHT + SUM_DUMMYRADI_DRAG_RADI
								* Math.sin(getAngle(event.getX(actionIndex),
										event.getY(actionIndex))));

						touchedCircle.centerX = (int) (SCREEN_HALF_WIDTH + SUM_DUMMYRADI_DRAG_RADI
								* Math.cos(getAngle(event.getX(actionIndex),
										event.getY(actionIndex))));
					}

				}
			}
			invalidate();
			handled = true;
			break;

		case MotionEvent.ACTION_UP:

			Toast.makeText(
					getContext(),
					"Angle is "
							+ getAngle(event.getX(actionIndex),
									event.getY(actionIndex)), 500).show();
			clearCirclePointer();
			invalidate();
			handled = true;
			break;

		case MotionEvent.ACTION_POINTER_UP:
			// not general pointer was up
			pointerId = event.getPointerId(actionIndex);

			mCirclePointer.remove(pointerId);
			invalidate();
			handled = true;
			break;

		case MotionEvent.ACTION_CANCEL:
			handled = true;
			break;

		default:
			// do nothing
			break;
		}

		return super.onTouchEvent(event) || handled;
	}

	/**
	 * Clears all CircleArea - pointer id relations.
	 */
	private void clearCirclePointer() {
		mCirclePointer.clear();
	}

	/**
	 * Gets the angle.
	 *
	 * @param xValue
	 *            the x value
	 * @param yValue
	 *            the y value
	 * @return the angle
	 */
	public double getAngle(double xValue, double yValue) {

		double dx = xValue - SCREEN_HALF_WIDTH;
		// Minus to correct for coord re-mapping
		double dy = -(yValue - SCREEN_HALF_HEIGHT);

		double inRads = Math.atan2(dy, dx);

		// We need to map to cord system when 0 degree is at 3 O'clock, 270 at
		// 12 O'clock
		if (inRads < 0)
			inRads = Math.abs(inRads);
		else
			inRads = 2 * Math.PI - inRads;

		return inRads;

		// return Math.toDegrees(inRads);
	}

	/**
	 * Search and creates new (if needed) circle based on touch area.
	 *
	 * @param xTouch
	 *            int x of touch
	 * @param yTouch
	 *            int y of touch
	 * @return obtained {@link CircleArea}
	 */
	private CircleArea obtainTouchedCircle(final int xTouch, final int yTouch) {
		CircleArea touchedCircle = getTouchedCircle(xTouch, yTouch);

		if (null == touchedCircle) {
			touchedCircle = new CircleArea(xTouch, yTouch, DRAG_CIRCLE_RADIUS);

			if (mCircles.size() == CIRCLES_LIMIT) {
			}
			if (flag == false) {
				mCircles.add(touchedCircle);
				flag = true;
			}

		}

		return touchedCircle;
	}

	/**
	 * Determines touched circle.
	 *
	 * @param xTouch
	 *            int x touch coordinate
	 * @param yTouch
	 *            int y touch coordinate
	 * @return {@link CircleArea} touched circle or null if no circle has been
	 *         touched
	 */
	private CircleArea getTouchedCircle(final int xTouch, final int yTouch) {
		CircleArea touched = null;

		for (CircleArea circle : mCircles) {
			if ((circle.centerX - xTouch) * (circle.centerX - xTouch)
					+ (circle.centerY - yTouch) * (circle.centerY - yTouch) <= circle.radius
					* circle.radius) {
				touched = circle;
				break;
			}
		}

		return touched;
	}

}