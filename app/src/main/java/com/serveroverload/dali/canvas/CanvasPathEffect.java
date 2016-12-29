package com.serveroverload.dali.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.view.KeyEvent;
import android.view.View;

public class CanvasPathEffect extends View {
	private Paint mPaint;
	private Path mPath;
	private PathEffect[] mEffects;
	private int[] mColors;
	private float mPhase = 3;

	private static void makeEffects(PathEffect[] e, float phase) {
		e[0] = null; // no effect
		e[1] = new CornerPathEffect(10);
		e[2] = new DashPathEffect(new float[] { 10, 5, 5, 5 }, phase);
		e[3] = new PathDashPathEffect(makePathDash(), 12, phase,
				PathDashPathEffect.Style.MORPH);
		e[4] = new ComposePathEffect(e[2], e[1]);
		e[5] = new ComposePathEffect(e[3], e[1]);
	}

	public CanvasPathEffect(Context context) {
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(6);

		//mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

		mPath = makeFollowPath();

		mEffects = new PathEffect[6];

		mColors = new int[] { Color.BLACK, Color.RED, Color.BLUE, Color.GREEN,
				Color.MAGENTA, Color.BLACK };
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);

		RectF bounds = new RectF();
		mPath.computeBounds(bounds, false);
		canvas.translate(10 - bounds.left, 10 - bounds.top);

		makeEffects(mEffects, mPhase);
		invalidate();

		for (int i = 0; i < mEffects.length; i++) {
			mPaint.setPathEffect(mEffects[i]);
			mPaint.setColor(mColors[i]);
			canvas.drawPath(mPath, mPaint);
			canvas.translate(0, 28);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
			mPath = makeFollowPath();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private static Path makeFollowPath() {
		Path p = new Path();
		p.moveTo(0, 0);
		for (int i = 1; i <= 15; i++) {
			p.lineTo(i * 20, (float) Math.random() * 35);
		}
		return p;
	}

	private static Path makePathDash() {
		Path p = new Path();
		p.moveTo(-6, 4);
		p.lineTo(6, 4);
		p.lineTo(6, 3);
		p.lineTo(-6, 3);
		p.close();
		p.moveTo(-6, -4);
		p.lineTo(6, -4);
		p.lineTo(6, -3);
		p.lineTo(-6, -3);
		return p;
	}
}
