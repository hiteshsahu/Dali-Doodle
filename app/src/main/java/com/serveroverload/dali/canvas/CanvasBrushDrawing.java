package com.serveroverload.dali.canvas;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import com.serveroverload.dali.R;

public class CanvasBrushDrawing extends View {
    private Bitmap mBitmapBrush;
    private Vector2 mBitmapBrushDimensions;

    private List<Vector2> mPositions = new ArrayList<Vector2>(100);

    private static final class Vector2 {
        public Vector2(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public final float x;
        public final float y;
    }

    public CanvasBrushDrawing(Context context) {
        super(context);

// load your brush here
        mBitmapBrush = BitmapFactory.decodeResource(context.getResources(), R.drawable.texture_1);
        mBitmapBrushDimensions = new Vector2(mBitmapBrush.getWidth(), mBitmapBrush.getHeight());

        setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Vector2 pos : mPositions) {
            canvas.drawBitmap(mBitmapBrush, pos.x, pos.y, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action) {
        case MotionEvent.ACTION_MOVE:
            final float posX = event.getX();
            final float posY = event.getY();
            
            mPositions.clear();
            mPositions.add(new Vector2(posX - mBitmapBrushDimensions.x / 2, posY - mBitmapBrushDimensions.y / 2));
            invalidate();
        }

        return true;
    }
}