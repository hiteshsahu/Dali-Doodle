package com.serveroverload.dali.colorbox;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;

/**
 * The Util Class PaintBox.
 */
public class GradientBox {

	/** The screen half width. */
	private int SCREEN_HALF_WIDTH;

	/** The screen half height. */
	private int SCREEN_HALF_HEIGHT;

	/** The screen width. */
	private int screenWidth;

	/** The screen height. */
	private int screenHeight;

	int shaderColor0 = Color.RED;
	int shaderColor1 = Color.BLUE;

	public static final int RADIEL_GRADIENT = 0;
	public static final int LINER_GRADIENT = 1;
	public static final int SWEEP_GRADIENT = 2;
	public static final int COMPOSE_GRADIENT = 4;

	public Paint getShader(int gradientType, Paint paint) {

		switch (gradientType) {
		case RADIEL_GRADIENT:

			RadialGradient gradient = new RadialGradient(SCREEN_HALF_WIDTH,
					SCREEN_HALF_HEIGHT, SCREEN_HALF_WIDTH, 0xFFFFFFFF,
					0xFF000000, android.graphics.Shader.TileMode.REPEAT);
			paint.setDither(true);
			paint.setShader(gradient);

			return paint;

		case LINER_GRADIENT:

			paint.setAntiAlias(true);
			Shader linearGradientShader;

			linearGradientShader = new LinearGradient(0, 0, screenWidth,
					screenHeight, shaderColor1, shaderColor0,
					Shader.TileMode.MIRROR);

			paint.setShader(linearGradientShader);

			linearGradientShader = new LinearGradient(SCREEN_HALF_WIDTH,
					SCREEN_HALF_HEIGHT, SCREEN_HALF_WIDTH + SCREEN_HALF_WIDTH
							/ 4, SCREEN_HALF_HEIGHT + SCREEN_HALF_HEIGHT / 4,
					shaderColor0, shaderColor1, Shader.TileMode.MIRROR);

			paint.setShader(linearGradientShader);

			return paint;

		case SWEEP_GRADIENT:

			paint.setAntiAlias(true);
			paint.setShader(new SweepGradient(SCREEN_HALF_WIDTH,
					SCREEN_HALF_HEIGHT, shaderColor0, shaderColor1));

			return paint;

		case COMPOSE_GRADIENT:

			RadialGradient radial_gradient = new RadialGradient(
					SCREEN_HALF_WIDTH, SCREEN_HALF_HEIGHT, SCREEN_HALF_WIDTH,
					0xFFFFFFFF, 0x00FFFFFF,
					android.graphics.Shader.TileMode.CLAMP);

			int morecolors[] = new int[13];
			float hsv[] = new float[3];
			hsv[1] = 1;
			hsv[2] = 1;
			for (int i = 0; i < 12; i++) {
				hsv[0] = (360 / 12) * i;
				morecolors[i] = Color.HSVToColor(hsv);
			}
			morecolors[12] = morecolors[0];

			SweepGradient sweep_gradient = new SweepGradient(SCREEN_HALF_WIDTH,
					SCREEN_HALF_HEIGHT, morecolors, null);

			ComposeShader shader = new ComposeShader(sweep_gradient,
					radial_gradient, PorterDuff.Mode.SRC_OVER);

			paint.setDither(true);
			paint.setShader(shader);

			return paint;

		default:
			break;
		}
		return paint;

	}

	/**
	 * Inits the paint box.
	 *
	 * @param screenWidth
	 *            the screen width
	 * @param screenHeight
	 *            the screen height
	 * @param SCREEN_HALF_WIDTH
	 *            the screen half width
	 * @param SCREEN_HALF_HEIGHT
	 *            the screen half height
	 */
	public void initPaintBox(int screenWidth, int screenHeight,
			int SCREEN_HALF_WIDTH, int SCREEN_HALF_HEIGHT) {

		this.SCREEN_HALF_HEIGHT = SCREEN_HALF_HEIGHT;
		this.SCREEN_HALF_WIDTH = SCREEN_HALF_WIDTH;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

	}

	/**
	 * Gets the radiel gradient.
	 *
	 * @return the radiel gradient
	 */
	public Paint getRadielGradient() {

		RadialGradient gradient = new RadialGradient(SCREEN_HALF_WIDTH,
				SCREEN_HALF_HEIGHT, SCREEN_HALF_WIDTH, 0xFFFFFFFF, 0xFF000000,
				android.graphics.Shader.TileMode.REPEAT);

		Paint radielGradientPaint = new Paint();
		radielGradientPaint.setDither(true);
		radielGradientPaint.setShader(gradient);

		return radielGradientPaint;

	}

	/**
	 * Gets the better radient gradient.
	 *
	 * @return the better radient gradient
	 */
	public Paint getBetterRadientGradient() {
		// More colors

		int colors[] = new int[] { Color.BLUE, Color.YELLOW, Color.RED,
				Color.GREEN, Color.MAGENTA, Color.WHITE };

		RadialGradient gradient1 = new RadialGradient(SCREEN_HALF_WIDTH,
				SCREEN_HALF_HEIGHT, SCREEN_HALF_WIDTH, colors, null,
				Shader.TileMode.MIRROR);

		Paint radielGradientPaint = new Paint();
		radielGradientPaint.setDither(true);
		radielGradientPaint.setShader(gradient1);

		return radielGradientPaint;
	}

	/**
	 * Gets the sweep gradient.
	 *
	 * @return the sweep gradient
	 */
	public Paint getSweepGradient() {
		int[] mColors = new int[] { 0xFFFF0000,// red
				0xFFFFFF00,// yellow
				0xFF00FF00,// green
				0xFF00FFFF,// cyan
				0xFF0000FF,// blue
				0xFFFF00FF,// magenta
				0xFFFF0000,// red
		};
		Paint sweepGradientPaint = new Paint();
		Shader shaderHue = new SweepGradient(SCREEN_HALF_WIDTH,
				SCREEN_HALF_HEIGHT, mColors, null);
		sweepGradientPaint.setStyle(Paint.Style.FILL);
		sweepGradientPaint.setShader(shaderHue);

		return sweepGradientPaint;
	}

	/**
	 * Gets the compose shader.
	 *
	 * @return the compose shader
	 */
	public Paint getComposeShader() {
		RadialGradient radial_gradient = new RadialGradient(SCREEN_HALF_WIDTH,
				SCREEN_HALF_HEIGHT, SCREEN_HALF_WIDTH, 0xFFFFFFFF, 0x00FFFFFF,
				android.graphics.Shader.TileMode.CLAMP);

		int morecolors[] = new int[13];
		float hsv[] = new float[3];
		hsv[1] = 1;
		hsv[2] = 1;
		for (int i = 0; i < 12; i++) {
			hsv[0] = (360 / 12) * i;
			morecolors[i] = Color.HSVToColor(hsv);
		}
		morecolors[12] = morecolors[0];

		SweepGradient sweep_gradient = new SweepGradient(SCREEN_HALF_WIDTH,
				SCREEN_HALF_HEIGHT, morecolors, null);

		ComposeShader shader = new ComposeShader(sweep_gradient,
				radial_gradient, PorterDuff.Mode.SRC_OVER);

		Paint advancePaint = new Paint();

		advancePaint.setDither(true);
		advancePaint.setShader(shader);

		return advancePaint;
	}

	/**
	 * Gets the linear gradient.
	 *
	 * @param canv
	 *            the canv
	 * @return the linear gradient
	 */
	public Paint getLinearGradient(final Canvas canv) {
		Paint BackPaint = new Paint();

		BackPaint.setStyle(Paint.Style.FILL);
		BackPaint.setColor(Color.BLACK);

		canv.drawRect(0, 0, screenWidth, screenHeight, BackPaint);

		Paint linearGradientPaint = new Paint();
		linearGradientPaint.setStyle(Paint.Style.FILL);

		int shaderColor0 = Color.RED;
		int shaderColor1 = Color.BLUE;
		linearGradientPaint.setAntiAlias(true);
		Shader linearGradientShader;

		linearGradientShader = new LinearGradient(0, 0, screenWidth,
				screenHeight, shaderColor1, shaderColor0,
				Shader.TileMode.MIRROR);

		linearGradientPaint.setShader(linearGradientShader);

		canv.drawRect(0, 0, screenWidth, screenHeight, linearGradientPaint);

		linearGradientShader = new LinearGradient(SCREEN_HALF_WIDTH,
				SCREEN_HALF_HEIGHT, SCREEN_HALF_WIDTH + SCREEN_HALF_WIDTH / 4,
				SCREEN_HALF_HEIGHT + SCREEN_HALF_HEIGHT / 4, shaderColor0,
				shaderColor1, Shader.TileMode.MIRROR);

		linearGradientPaint.setShader(linearGradientShader);
		return linearGradientPaint;
	}

	/**
	 * Draw text with sadow.
	 *
	 * @param canvas
	 *            the canvas
	 * @param icon
	 *            the icon
	 */
	public void drawTextWithSadow(Canvas canvas, Bitmap icon) {
		Paint shadowPaint = new Paint();
		shadowPaint.setAntiAlias(true);
		shadowPaint.setColor(Color.WHITE);
		shadowPaint.setTextSize(45.0f);
		shadowPaint.setStrokeWidth(2.0f);
		shadowPaint.setStyle(Paint.Style.STROKE);
		shadowPaint.setShadowLayer(5.0f, 10.0f, 10.0f, Color.BLACK);

		// for android:minSdkVersion="11"
		// setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint);

		canvas.drawText("Canvas is Awesome !! ", 50, 200, shadowPaint);
		canvas.drawBitmap(icon, SCREEN_HALF_WIDTH, SCREEN_HALF_HEIGHT,
				shadowPaint);

	}

	/**
	 * Gets the simple sweep paint.
	 *
	 * @return the simple sweep paint
	 */
	public Paint getSimpleSweepPaint() {
		Paint MyPaint = new Paint();
		MyPaint.setStyle(Paint.Style.FILL);

		int shaderColor0 = Color.RED;
		int shaderColor1 = Color.BLUE;

		MyPaint.setAntiAlias(true);
		MyPaint.setShader(new SweepGradient(SCREEN_HALF_WIDTH,
				SCREEN_HALF_HEIGHT, shaderColor0, shaderColor1));

		return MyPaint;

	}

	/**
	 * Gets the better radiel gradient.
	 *
	 * @param canvas
	 *            the canvas
	 * @return the better radiel gradient
	 */
	public Paint getBetterRadielGradient(Canvas canvas) {

		Paint BackPaint = new Paint();

		BackPaint.setStyle(Paint.Style.FILL);
		BackPaint.setColor(Color.BLACK);

		canvas.drawRect(0, 0, screenWidth, screenHeight, BackPaint);

		Paint MyPaint = new Paint();
		MyPaint.setStyle(Paint.Style.FILL);

		float shaderCx = 0;
		float shaderCy = 0;
		float shaderRadius = screenWidth;
		int shaderColor0 = Color.WHITE;
		int shaderColor1 = Color.BLACK;
		MyPaint.setAntiAlias(true);
		Shader radialGradientShader;

		radialGradientShader = new RadialGradient(shaderCx, shaderCy,
				shaderRadius, shaderColor0, shaderColor1,
				Shader.TileMode.MIRROR);

		MyPaint.setShader(radialGradientShader);
		canvas.drawRect(0, 0, screenWidth, screenHeight, MyPaint);

		shaderRadius = SCREEN_HALF_WIDTH;
		shaderColor0 = Color.RED;
		shaderColor1 = Color.BLUE;

		radialGradientShader = new RadialGradient(shaderCx, shaderCy,
				shaderRadius, shaderColor0, shaderColor1,
				Shader.TileMode.MIRROR);

		MyPaint.setShader(radialGradientShader);
		// canvas.drawCircle(SCREEN_HALF_WIDTH, SCREEN_HALF_HEIGHT,
		// SCREEN_HALF_WIDTH, MyPaint);

		return MyPaint;

	}

	/**
	 * Gets the rain bow dhader.
	 *
	 * @return the rain bow dhader
	 */
	public android.graphics.Paint getRainBowDhader() {

		Paint rainboxPaint = new Paint();

		int[] rainbow = getRainbowColors();
		Shader shader = new LinearGradient(0, 0, 0, screenWidth, rainbow, null,
				Shader.TileMode.MIRROR);

		Matrix matrix = new Matrix();
		matrix.setRotate(90);
		shader.setLocalMatrix(matrix);

		rainboxPaint.setShader(shader);

		return rainboxPaint;
	}

	/**
	 * Gets the rainbow colors.
	 *
	 * @return the rainbow colors
	 */
	private int[] getRainbowColors() {
		return new int[] { Color.RED, Color.YELLOW,

		Color.GREEN, Color.BLUE,

		Color.RED, Color.MAGENTA };
	}

}
