package com.serveroverload.dali.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.view.View;

public class CanvasClippingEffect extends View {

	private Paint mPaint;

	private Path mPath;

	public CanvasClippingEffect(Context context) {

		super(context);

		setFocusable(true);

		mPaint = new Paint();

		mPaint.setAntiAlias(true);

		mPaint.setStrokeWidth(6);

		mPaint.setTextSize(16);

		mPaint.setTextAlign(Paint.Align.RIGHT);

		mPath = new Path();

	}

	private void drawScene(Canvas canvas) {

		canvas.clipRect(0, 0, 160, 160);

		canvas.drawColor(Color.WHITE);

		mPaint.setColor(Color.RED);

		canvas.drawLine(0, 0, 160, 160, mPaint);

		mPaint.setColor(Color.GREEN);

		canvas.drawCircle(47, 113, 47, mPaint);

		mPaint.setColor(Color.BLUE);

		canvas.drawText("Clipping", 140, 30, mPaint);

	}

	@Override
	protected void onDraw(Canvas canvas) {

		boolean clipresult = false;

		canvas.drawColor(Color.GRAY);

		canvas.save();

		canvas.translate(10, 10);

		drawScene(canvas);

		canvas.restore();

		canvas.save();

		canvas.translate(180, 10);

		clipresult = canvas.clipRect(20, 20, 140, 140);

		clipresult = canvas.clipRect(50, 50, 110, 110, Region.Op.DIFFERENCE);

		drawScene(canvas);

		canvas.restore();

		canvas.save();

		canvas.translate(10, 200);

		canvas.clipRect(20, 20, 140, 140);

		mPath.reset();

		canvas.clipPath(mPath); // makes the clip empty

		mPath.addCircle(80, 80, 80, Path.Direction.CCW);

		canvas.clipPath(mPath, Region.Op.REPLACE);

		drawScene(canvas);

		canvas.restore();

		canvas.save();

		canvas.translate(180, 200);

		canvas.clipRect(10, 10, 100, 100);

		canvas.clipRect(80, 80, 170, 170, Region.Op.UNION);

		drawScene(canvas);

		canvas.restore();

		canvas.save();

		canvas.translate(10, 390);

		canvas.clipRect(0, 0, 80, 80);

		canvas.clipRect(40, 40, 120, 120, Region.Op.XOR);

		drawScene(canvas);

		canvas.restore();

		canvas.save();

		canvas.translate(180, 390);

		canvas.clipRect(0, 0, 80, 80);

		canvas.clipRect(40, 40, 120, 120, Region.Op.REVERSE_DIFFERENCE);

		drawScene(canvas);

		canvas.restore();

		canvas.save();

		canvas.translate(10, 580);

		canvas.clipRect(0, 0, 80, 80);

		canvas.clipRect(40, 40, 120, 120, Region.Op.INTERSECT);

		drawScene(canvas);

		canvas.restore();

	}

}