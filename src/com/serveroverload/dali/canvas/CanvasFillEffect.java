package com.serveroverload.dali.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

public class CanvasFillEffect extends View {

	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	private Path mpath;

	public CanvasFillEffect(Context context) {

		super(context);

		setFocusable(true);

		setFocusableInTouchMode(true);

		mpath = new Path();

		mpath.addCircle(40, 40, 45, Path.Direction.CCW);

		mpath.addCircle(80, 80, 45, Path.Direction.CCW);

	}

	private void showPath(Canvas canvas, int x, int y, Path.FillType ft,

	Paint paint) {

		canvas.save();

		canvas.translate(x, y);

		canvas.clipRect(0, 0, 120, 120);

		canvas.drawColor(Color.WHITE);

		mpath.setFillType(ft);

		canvas.drawPath(mpath, mPaint);

		canvas.restore();

	}

	@Override
	protected void onDraw(Canvas canvas) {

		Paint Paint = mPaint;

		canvas.drawColor(0xFFCCCCCC);

		canvas.translate(20, 20);

		Paint.setAntiAlias(true);

		showPath(canvas, 0, 0, Path.FillType.WINDING, Paint);

		showPath(canvas, 160, 0, Path.FillType.EVEN_ODD, Paint);

		showPath(canvas, 0, 160, Path.FillType.INVERSE_WINDING, Paint);

		showPath(canvas, 160, 160, Path.FillType.INVERSE_EVEN_ODD, Paint);

	}
}