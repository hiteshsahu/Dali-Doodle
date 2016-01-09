package com.serveroverload.dali.ui.customeview;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.serveroverload.dali.R;
/**
* @author kince
* @category View??????
*
*/
public class WaterWaveView extends View {
     private Context mContext;
     private int mScreenWidth;
     private int mScreenHeight;
     private Paint mRingPaint;
     private Paint mCirclePaint;
     private Paint mWavePaint;
     private Paint linePaint;
     private Paint flowPaint;
     private Paint leftPaint;
     private int mRingSTROKEWidth = 15;
     private int mCircleSTROKEWidth = 2;
     private int mLineSTROKEWidth = 1;
     private int mCircleColor = Color.WHITE;
     private int mRingColor = Color.WHITE;
     private int mWaveColor = Color.WHITE;
     private Handler mHandler;
     private long c = 0L;
     private boolean mStarted = false;
     private final float f = 0.033F;
     private int mAlpha = 50;// ???
     private float mAmplitude = 10.0F; // ??
     private float mWateLevel = 0.5F;// ??(0~1)
     private Path mPath;
     private String flowNum = "1024M";
     private String flowLeft = "???";
     /**
     * @param context
     */
     public WaterWaveView(Context context) {
          super(context);
          // TODO Auto-generated constructor stub
          mContext = context;
          init(mContext);
     }
     /**
     * @param context
     * @param attrs
     */
     public WaterWaveView(Context context, AttributeSet attrs) {
          super(context, attrs);
          // TODO Auto-generated constructor stub
          mContext = context;
          init(mContext);
     }
     /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
     public WaterWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
          super(context, attrs, defStyleAttr);
          // TODO Auto-generated constructor stub
          mContext = context;
          init(mContext);
     }
     private void init(Context context) {
          mRingPaint = new Paint();
          mRingPaint.setColor(mRingColor);
          mRingPaint.setAlpha(50);
          mRingPaint.setStyle(Paint.Style.STROKE);
          mRingPaint.setAntiAlias(true);
          mRingPaint.setStrokeWidth(mRingSTROKEWidth);
          mCirclePaint = new Paint();
          mCirclePaint.setColor(mCircleColor);
          mCirclePaint.setStyle(Paint.Style.STROKE);
          mCirclePaint.setAntiAlias(true);
          mCirclePaint.setStrokeWidth(mCircleSTROKEWidth);
          linePaint = new Paint();
          linePaint.setColor(mCircleColor);
          linePaint.setStyle(Paint.Style.STROKE);
          linePaint.setAntiAlias(true);
          linePaint.setStrokeWidth(mLineSTROKEWidth);
          flowPaint = new Paint();
          flowPaint.setColor(mCircleColor);
          flowPaint.setStyle(Paint.Style.FILL);
          flowPaint.setAntiAlias(true);
          flowPaint.setTextSize(36);
          leftPaint = new Paint();
          leftPaint.setColor(mCircleColor);
          leftPaint.setStyle(Paint.Style.FILL);
          leftPaint.setAntiAlias(true);
          leftPaint.setTextSize(18);
          mWavePaint = new Paint();
          mWavePaint.setStrokeWidth(1.0F);
          mWavePaint.setColor(mWaveColor);
          mWavePaint.setAlpha(mAlpha);
          mPath = new Path();
          mHandler = new Handler() {
               @Override
               public void handleMessage(android.os.Message msg) {
                    if (msg.what == 0) {
                         invalidate();
                         if (mStarted) {
                              // ?????????????????
                              mHandler.sendEmptyMessageDelayed(0, 60L);
                         }
                    }
               }
          };
     }
     @Override
     protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
          int width = measure(widthMeasureSpec, true);
          int height = measure(heightMeasureSpec, false);
          if (width < height) {
               setMeasuredDimension(width, width);
          } else {
               setMeasuredDimension(height, height);
          }
     }
     /**
     * @category ??
     * @param measureSpec
     * @param isWidth
     * @return
     */
     private int measure(int measureSpec, boolean isWidth) {
          int result;
          int mode = MeasureSpec.getMode(measureSpec);
          int size = MeasureSpec.getSize(measureSpec);
          int padding = isWidth ? getPaddingLeft() + getPaddingRight()
                    : getPaddingTop() + getPaddingBottom();
          if (mode == MeasureSpec.EXACTLY) {
               result = size;
          } else {
               result = isWidth ? getSuggestedMinimumWidth()
                         : getSuggestedMinimumHeight();
               result += padding;
               if (mode == MeasureSpec.AT_MOST) {
                    if (isWidth) {
                         result = Math.max(result, size);
                    } else {
                         result = Math.min(result, size);
                    }
               }
          }
          return result;
     }
     @Override
     protected void onSizeChanged(int w, int h, int oldw, int oldh) {
          // TODO Auto-generated method stub
          super.onSizeChanged(w, h, oldw, oldh);
          mScreenWidth = w;
          mScreenHeight = h;
     }
     @Override
     protected void onDraw(Canvas canvas) {
          // TODO Auto-generated method stub
          super.onDraw(canvas);
          // ???????
          int width = getWidth();
          int height = getHeight();
          setBackgroundColor(Color.MAGENTA);
          canvas.drawCircle(mScreenWidth / 2, mScreenHeight / 2,
                    mScreenWidth / 4, mRingPaint);
          canvas.drawCircle(mScreenWidth / 2, mScreenHeight / 2, mScreenWidth / 4
                    - mRingSTROKEWidth / 2, mCirclePaint);
          canvas.drawLine(mScreenWidth * 3 / 8, mScreenHeight * 5 / 8,
                    mScreenWidth * 5 / 8, mScreenHeight * 5 / 8, linePaint);
          float num = flowPaint.measureText(flowNum);
          canvas.drawText(flowNum, mScreenWidth * 4 / 8 - num / 2,
                    mScreenHeight * 4 / 8, flowPaint);
          float left = leftPaint.measureText(flowLeft);
          canvas.drawText(flowLeft, mScreenWidth * 4 / 8 - left / 2,
                    mScreenHeight * 3 / 8, leftPaint);
          // ?????????startWave???,??????
          if ((!mStarted) || (mScreenWidth == 0) || (mScreenHeight == 0)) {
               RectF oval = new RectF(mScreenWidth / 4 + mRingSTROKEWidth / 2,
                         mScreenHeight / 4 + mRingSTROKEWidth / 2, mScreenWidth * 3
                                   / 4 - mRingSTROKEWidth / 2, mScreenHeight * 3 / 4
                                   - mRingSTROKEWidth / 2);// ?????????????
               canvas.drawArc(oval, 0, 180, true, mWavePaint);
               return;
          }
          // ??,?????????
          RectF oval = new RectF(mScreenWidth / 4 + mRingSTROKEWidth / 2,
                    mScreenHeight / 4 + mRingSTROKEWidth / 2 + mAmplitude * 2,
                    mScreenWidth * 3 / 4 - mRingSTROKEWidth / 2, mScreenHeight * 3
                              / 4 - mRingSTROKEWidth / 2);// ?????????????
          canvas.drawArc(oval, 0, 180, true, mWavePaint);
          if (this.c >= 8388607L) {
               this.c = 0L;
          }
          // ??onDraw?c????
          c = (1L + c);
          float f1 = mScreenHeight * (1.0F - mWateLevel);
          int top = (int) (f1 + mAmplitude);
          mPath.reset();
          int startX = mScreenWidth / 2 - mScreenWidth / 4 + mRingSTROKEWidth / 2;
          // ????
          while (startX < mScreenWidth / 2 + mScreenWidth / 4 - mRingSTROKEWidth
                    / 2) {
               int startY = (int) (f1 - mAmplitude
                         * Math.sin(Math.PI
                                   * (2.0F * (startX + this.c * width * this.f))
                                   / width));
               canvas.drawLine(startX, startY, startX, top, mWavePaint);
               startX++;
          }
          canvas.restore();
     }
     @Override
     public Parcelable onSaveInstanceState() {
          // Force our ancestor class to save its state
          Parcelable superState = super.onSaveInstanceState();
          SavedState ss = new SavedState(superState);
          ss.progress = (int) c;
          return ss;
     }
     @Override
     public void onRestoreInstanceState(Parcelable state) {
          SavedState ss = (SavedState) state;
          super.onRestoreInstanceState(ss.getSuperState());
          c = ss.progress;
     }
     @Override
     protected void onAttachedToWindow() {
          super.onAttachedToWindow();
          // ???????????unsupported operation exception
          this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
     }
     @Override
     protected void onDetachedFromWindow() {
          super.onDetachedFromWindow();
     }
     /**
     * @category ????
     */
     public void startWave() {
          if (!mStarted) {
               this.c = 0L;
               mStarted = true;
               this.mHandler.sendEmptyMessage(0);
          }
     }
     /**
     * @category ????
     */
     public void stopWave() {
          if (mStarted) {
               this.c = 0L;
               mStarted = false;
               this.mHandler.removeMessages(0);
          }
     }
     /**
     * @category ????
     */
     static class SavedState extends BaseSavedState {
          int progress;
          /**
          * Constructor called from {@link ProgressBar#onSaveInstanceState()}
          */
          SavedState(Parcelable superState) {
               super(superState);
          }
          /**
          * Constructor called from {@link #CREATOR}
          */
          private SavedState(Parcel in) {
               super(in);
               progress = in.readInt();
          }
          @Override
          public void writeToParcel(Parcel out, int flags) {
               super.writeToParcel(out, flags);
               out.writeInt(progress);
          }
          public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
               public SavedState createFromParcel(Parcel in) {
                    return new SavedState(in);
               }
               public SavedState[] newArray(int size) {
                    return new SavedState[size];
               }
          };
     }
}