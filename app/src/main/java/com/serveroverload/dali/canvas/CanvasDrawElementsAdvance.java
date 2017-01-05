package com.serveroverload.dali.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class CanvasDrawElementsAdvance extends View {

        private RectF mArcRectF;
        private RectF mOvalRectF;

        public CanvasDrawElementsAdvance(Context context) {
            super(context);
            init();
        }

        Paint mLinePaint;
        Paint mArcPaint;
        Paint mRectFPaint;
        Paint mCirclePaint;
        Paint mPointPaint;
        Paint mOvalPaint;

        private void init() {


            mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPointPaint.setColor(Color.parseColor("#000000"));
            mPointPaint.setStrokeWidth(15);

            mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLinePaint.setColor(Color.parseColor("#0000ff"));
            mLinePaint.setStrokeWidth(5);// stroke??

            mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mArcPaint.setColor(Color.parseColor("#00ff00"));

            mRectFPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mRectFPaint.setColor(Color.parseColor("#eeff00"));


            mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mCirclePaint.setColor(Color.parseColor("#443322"));

            mOvalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mOvalPaint.setColor(Color.parseColor("#88ddff"));

            //RectF rectangle ?????,??????????????
            mArcRectF = new RectF(0, 30, 267, 222);
            mOvalRectF = new RectF(0,0,300,200);
        }

        @Override
        protected void onDraw(Canvas canvas) {

            canvas.save();
            canvas.translate(10, 0);
            canvas.drawARGB(111, 156, 11, 44);
            // ????
            canvas.drawPoint(10, 10, mPointPaint);

            canvas.translate(50, 0);
            canvas.drawLine(0, 0, 0, 300, mLinePaint);
            canvas.drawLine(0, 300, 300, 300, mLinePaint);
            canvas.drawArc(mArcRectF, 90, 22, true, mArcPaint);
            // ?????
            canvas.drawArc(mArcRectF, 122, 33, false, mArcPaint);

            mArcPaint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(mArcRectF, 166, 44, true, mArcPaint);


            canvas.restore();
            canvas.translate(0, 300);
            mArcPaint.setStrokeWidth(20);
            mArcPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawArc(mArcRectF, 22, 393, false, mArcPaint);

            canvas.translate(330, 100);
            mCirclePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(50, 50, 50, mCirclePaint);
            mCirclePaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(0, 0, 100, 100, mCirclePaint);

            canvas.translate(400, 0);
            mOvalRectF.set(0, 30, 267, 222);
            canvas.drawOval(mOvalRectF, mOvalPaint);
            canvas.translate(0, 300);
            mOvalRectF.set(0, 0, 200, 200);
            canvas.drawOval(mOvalRectF, mOvalPaint);

            canvas.translate(0, 300);
            canvas.drawCircle(50, 50, 100, mOvalPaint);
        }
    }