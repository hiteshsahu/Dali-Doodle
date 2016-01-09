package com.serveroverload.dali.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class SpiderWave extends View {
	
	

	private static final int DEFAULT_WEB_COUNT = 6;
	private int count = 10; // Number of data
	private float angle = (float) (Math.PI * 2 / count);
	private float radius; // maximum radius grid
	private int centerX; // center X
	private int centerY; // center Y
	private String[] titles = { "a", "b", "c", "d", "e", "f", "c", "d", "e", "f" };
	private double[] data = { 100, 60, 60, 60, 100, 50, 10, 20 , 100, 50, 10, 20 }; // each
																	// dimension
																	// scores
	private float maxValue = 100; // maximum data
	private Paint mainPaint; // radar zone brush
	private Paint valuePaint; // data area brush
	private Paint textPaint;


	public SpiderWave(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
	}

	public SpiderWave(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public SpiderWave(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SpiderWave(Context context) {
		super(context);
		init();
	}

	private void init() {

		mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mainPaint.setColor(Color.BLACK);
		mainPaint.setStrokeWidth(5);
		mainPaint.setStyle(Paint.Style.STROKE);
		
		
		valuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		valuePaint.setColor(Color.BLUE);
		valuePaint.setStrokeWidth(5);
		valuePaint.setStyle(Paint.Style.STROKE);
		
		
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(Color.MAGENTA);
		textPaint.setStrokeWidth(5);
		textPaint.setStyle(Paint.Style.STROKE);
	}

	// text brush
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		radius = Math.min(h, w) / 2 * 0.9f;
		// Center coordinates
		centerX = w / 2;
		centerY = h / 2;
		postInvalidate();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		drawPolygon(canvas);

		drawLines(canvas);

		drawText(canvas);

		drawRegion(canvas);

		// draw(canvas);
	}

	private void drawPolygon(Canvas canvas) {
		Path path = new Path();
		float r = radius / (count - 1); // r is the spacing between the spider
										// silk
		for (int i = 1; i < count; i++) { // do not draw center
			float Curr = r * i; // current radius
			path.reset();
			for (int j = 0; j < count; j++) {
				if (j == 0) {
					path.moveTo(centerX + Curr, centerY);
				} else {
					// The radius, calculate the coordinates of each point on
					// the spider silk
					float x = (float) (centerX + Curr * Math.cos(angle * j));
					float y = (float) (centerY + Curr * Math.sin(angle * j));
					path.lineTo(x, y);
				}
			}
			path.close(); // close the path
			canvas.drawPath(path, mainPaint);
		}
	}

	private void drawLines(Canvas canvas) {
		Path path = new Path();
		for (int i = 0; i < count; i++) {
			path.reset();
			path.moveTo(centerX, centerY);
			float x = (float) (centerX + radius * Math.cos(angle * i));
			float y = (float) (centerY + radius * Math.sin(angle * i));
			path.lineTo(x, y);
			canvas.drawPath(path, mainPaint);
		}
	}

	private void drawText(Canvas canvas) {
		Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
		float fontHeight = fontMetrics.descent - fontMetrics.ascent;
		for (int i = 0; i < count; i++) {
			float x = (float) (centerX + (radius + fontHeight / 2)
					* Math.cos(angle * i));
			float y = (float) (centerY + (radius + fontHeight / 2)
					* Math.sin(angle * i));
			if (angle * i >= 0 && angle * i <= Math.PI / 2) { // the fourth
																// quadrant
				canvas.drawText(titles[i], x, y, textPaint);
			} else if (angle * i >= 3 * Math.PI / 2 && angle * i <= Math.PI * 2) { // the
																					// third
																					// quadrant
				canvas.drawText(titles[i], x, y, textPaint);
			} else if (angle * i > Math.PI / 2 && angle * i <= Math.PI) { // the
																			// second
																			// quadrant
				float dis = textPaint.measureText(titles[i]); // text length
				canvas.drawText(titles[i], x - dis, y, textPaint);
			} else if (angle * i >= Math.PI * angle && i < 3 * Math.PI / 2) { // first
																				// quadrant
				float dis = textPaint.measureText(titles[i]); // text length
				canvas.drawText(titles[i], x - dis, y, textPaint);
			}
		}
	}

	private void drawRegion(Canvas canvas) {
		Path path = new Path();
		valuePaint.setAlpha(255);
		for (int i = 0; i < count; i++) {
			double percent = data[i] / maxValue;
			float x = (float) (centerX + radius * Math.cos(angle * i) * percent);
			float y = (float) (centerY + radius * Math.sin(angle * i) * percent);
			if (i == 0) {
				path.moveTo(x, centerY);
			} else {
				path.lineTo(x, y);
			}
			// Draw a small dot
			canvas.drawCircle(x, y, 10, valuePaint);
		}
		valuePaint.setStyle(Paint.Style.STROKE);
		canvas.drawPath(path, valuePaint);
		valuePaint.setAlpha(127);
		// Draw a filled area
		valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
		canvas.drawPath(path, valuePaint);
	}

}