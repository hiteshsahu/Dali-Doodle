package com.serveroverload.dali.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class CanvasLayering extends View {
	private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG
			| Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
			| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
			| Canvas.CLIP_TO_LAYER_SAVE_FLAG;
	private Paint mPaint;

	public CanvasLayering(Context context) {
		super(context);
		setFocusable(true);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		canvas.translate(10, 10);
		mPaint.setColor(Color.RED);
		canvas.drawCircle(75, 75, 75, mPaint);
		canvas.saveLayerAlpha(0, 0, 200, 200, 0x88, LAYER_FLAGS);
		mPaint.setColor(Color.BLUE);
		canvas.drawCircle(125, 125, 75, mPaint);
		canvas.restore();
	}
}