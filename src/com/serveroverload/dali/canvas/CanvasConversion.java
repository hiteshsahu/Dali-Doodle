package com.serveroverload.dali.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class CanvasConversion extends View {
	private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG
			| Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
			| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
			| Canvas.CLIP_TO_LAYER_SAVE_FLAG;
	private Paint mPaint;

	public CanvasConversion(Context context) {
		super(context);
		setFocusable(true);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
	}

//	@Override
//	protected void onDraw(Canvas canvas) {
//		canvas.drawColor(Color.WHITE);
//		canvas.translate(10, 10);
//		mPaint.setColor(Color.RED);
//		canvas.drawCircle(75, 75, 75, mPaint);
//		canvas.saveLayerAlpha(0, 0, 200, 200, 0x88, LAYER_FLAGS);
//		mPaint.setColor(Color.BLUE);
//		canvas.drawCircle(125, 125, 75, mPaint);
//		canvas.restore();
//	}
	
	@Override
    protected void onDraw (Canvas canvas) {
        canvas.translate (100, 100);
        canvas.drawColor (Color.RED); // you can see, the entire screen is still filled with red
                                                    
        canvas.drawRect ( new  Rect (-100, -100, 0, 0), new  Paint ()); // scaled
        canvas.scale (0.5f, 0.5f);
        canvas.drawRect ( new  Rect (0, 0, 100, 100), new  Paint ());
                                                    
        canvas.translate (200, 0);
        canvas.rotate (30);
        canvas.drawRect ( new  Rect (0, 0, 100, 100), new  Paint ()); // rotate the
                                                    
        canvas.translate (200, 0);
        canvas.skew (.5f, .5f); // distorted
        canvas.drawRect ( new  Rect (0, 0, 100, 100), new  Paint ());
        // Canvas.setMatrix (matrix); // Matrix are used later in yes.
    }
}