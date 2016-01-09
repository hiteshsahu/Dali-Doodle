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
            //X??? 10???
            canvas.translate(10, 0);
            //????canvas ??? Notice ???translate? drawARGB????!!
            canvas.drawARGB(111, 156, 11, 44);
            // ????
            canvas.drawPoint(10, 10, mPointPaint);

            //X??? 50???
            canvas.translate(50, 0);
            //??? Notice ???translate(10,0) ?? translate(50,0) ????!
            //Notice ?? ????????(0,0),????????????(60,0)!!
            canvas.drawLine(0, 0, 0, 300, mLinePaint);
            canvas.drawLine(0, 300, 300, 300, mLinePaint);
            //??????
//            canvas.drawRect(mArcRectF, mRectFPaint);
//            canvas.drawRect(1, 2, 3, 4, mRectFPaint);

//            canvas.translate(300, 0);
//            canvas.drawRoundRect(mArcRectF,0,20,mRectFPaint);

//            canvas.translate(300, 0);
//            canvas.drawRoundRect(mArcRectF,50,70,mRectFPaint);

            //????????????
            //????????,??????(???)
            /**
             * Notice  ?? ?????:
             *
             *            270°(-90°)
             *            |
             *            |
             *            |
             * 180°       |
             * ___________|____________ 0°
             *           /|\a
             *          /b| \
             *         /  |  \
             *        /   |   \
             *       /    90°(-270°)
             *
             *
             *  startAngle ?????????,?0 ??(a)
             *  sweepAngle ????????(b)   Notice ??360°,??????.
             *  ?? a ?90 b ?22 ??????? ??????? 90°?,?????112°?
             *
             *  useCenter true ??????
             *
             *  http://ww3.sinaimg.cn/large/98900c07gw1ewtt438wdej20e10c8aa7.jpg
             */
            // ???? ????
            canvas.drawArc(mArcRectF, 90, 22, true, mArcPaint);
            // ?????
            canvas.drawArc(mArcRectF, 122, 33, false, mArcPaint);

//            canvas.drawArc(1,1,1,1,2,2,false,mArcPaint);


//            //??? ??fill(??)
            mArcPaint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(mArcRectF, 166, 44, true, mArcPaint);
//

            canvas.restore();
            canvas.translate(0, 300);
            //??360°,??????,??useCenter?false
            mArcPaint.setStrokeWidth(20);
            mArcPaint.setStyle(Paint.Style.FILL_AND_STROKE);//???????????
            canvas.drawArc(mArcRectF, 22, 393, false, mArcPaint);

            canvas.translate(330, 100);
            //?? ????,?? ??????
            mCirclePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(50, 50, 50, mCirclePaint);
            mCirclePaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(0, 0, 100, 100, mCirclePaint);

            canvas.translate(400, 0);
            mOvalRectF.set(0, 30, 267, 222);//??
            canvas.drawOval(mOvalRectF, mOvalPaint);
//            canvas.drawOval(1, 1, 1, 1, mOvalPaint);
            canvas.translate(0, 300);
            mOvalRectF.set(0, 0, 200, 200);//??????
            canvas.drawOval(mOvalRectF, mOvalPaint);

            canvas.translate(0, 300);
            canvas.drawCircle(50, 50, 100, mOvalPaint);
        }
    }