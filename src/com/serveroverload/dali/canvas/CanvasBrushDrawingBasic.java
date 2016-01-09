package com.serveroverload.dali.canvas;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

public class CanvasBrushDrawingBasic extends View {

	Paint paint;
	private ArrayList<PointF> graphics = new ArrayList<PointF>();
	PointF point;

	public CanvasBrushDrawingBasic(Context context) {
		super(context);
		paint = new Paint();
		paint.setColor(Color.YELLOW);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(3);

		setBackgroundColor(Color.BLACK);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		graphics.add(new PointF(event.getX(), event.getY()));

		invalidate();

		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		for (PointF point : graphics) {
			canvas.drawPoint(point.x, point.y, paint);
		}

	}
}