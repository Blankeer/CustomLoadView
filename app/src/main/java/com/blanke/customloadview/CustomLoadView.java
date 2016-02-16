package com.blanke.customloadview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;

/**
 * Created by Blanke on 16-2-15.
 */
public class CustomLoadView extends ProgressBar {
    private int mMinWidth = 48, mMinHeight = 48;
    private int r = 0;//半径
    private int mProgerss;
    private float oX = 0, oY = 0;//原点坐标
    private Paint mPreCirclePaint, mPreLinePaint, mCirclePaint;
    private int t = 0;
    private int mStrokeWidth = 12;

    public CustomLoadView(Context context) {
        this(context, null);
    }

    public CustomLoadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPreCirclePaint = new Paint();
        mPreCirclePaint.setColor(Color.parseColor("#11000000"));
        mPreCirclePaint.setStyle(Paint.Style.STROKE);
        mPreCirclePaint.setAntiAlias(true);
        mPreCirclePaint.setStrokeWidth(mStrokeWidth);
        mPreLinePaint = new Paint(mPreCirclePaint);
        mPreLinePaint.setColor(Color.WHITE);
        mCirclePaint = new Paint(mPreCirclePaint);
        mCirclePaint.setColor(Color.BLUE);

    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        if (wMode == MeasureSpec.AT_MOST) {
            w = mMinWidth;
        }
        if (hMode == MeasureSpec.AT_MOST) {
            h = mMinHeight;
        }
        r = Math.min(w, h) / 2 - mStrokeWidth;
        t = r / 2;
        w += getPaddingLeft() + getPaddingRight();
        h += getPaddingBottom() + getPaddingTop();
        oX = w * 0.5F;
        oY = h * 0.5F;
        setMeasuredDimension(w, h);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        mProgerss = getProgress();
        drawPreAnim(canvas, mProgerss);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void drawPreAnim(final Canvas canvas, final int k) {
        canvas.drawCircle(oX, oY, r, mPreCirclePaint);
        float temp;
        if (k >= 0 && k < 10) {
            temp = (float) (t * (10 - k) * 1.0 / 10);
            canvas.drawLine(oX, oY - temp, oX, oY + temp, mPreLinePaint);
            canvas.drawLine(oX - t, oY, oX, oY + t, mPreLinePaint);
            canvas.drawLine(oX + t, oY, oX, oY + t, mPreLinePaint);
        } else if (k < 20) {
//            canvas.drawLine(oX, oY - mStrokeWidth / 2, oX, oY + mStrokeWidth / 2, mPreLinePaint);
            canvas.drawCircle(oX, oY, mStrokeWidth * 0.5F, mPreLinePaint);
            temp = (float) (t * (20 - k) * 1.0 / 10);
            canvas.drawLine(oX - t, oY, oX, oY + temp, mPreLinePaint);
            canvas.drawLine(oX + t, oY, oX, oY + temp, mPreLinePaint);
        } else if (k <= 30) {
            canvas.drawLine(oX - t, oY, oX + t, oY, mPreLinePaint);
            temp = (float) (r * (30 - k) * 1.0 / 10);
//            canvas.drawRect(oX - mStrokeWidth * 0.5F, oY - mStrokeWidth * 0.5F - (r - temp), oX + mStrokeWidth * 0.5F, oY + mStrokeWidth * 0.5F - (r - temp), mPreLinePaint);
            canvas.drawCircle(oX, oY - (r - temp), mStrokeWidth * 0.5F, mPreLinePaint);
        } else if (k <= 100) {
//            canvas.drawLine(oX - t, oY, oX + t, oY, mPreLinePaint);
            temp = (float) (360 * (1 - (95 - k) * 1.0 / 65));
            canvas.drawArc(oX - r, oY - r, oX + r, oY + r, 270, temp, false, mCirclePaint);
            float temp2 = (float) (t * (1 - (95 - k) * 1.0 / 65));
            if (k < 95) {
                canvas.drawLine(oX - t, oY, oX, oY + temp2, mPreLinePaint);
                canvas.drawLine(oX + t, oY, oX, oY + temp2, mPreLinePaint);
            } else {
                temp = (float) (t * 0.5 * (1 - (100 - k) * 1.0 / 5));
                canvas.drawLine(oX - t, oY, oX, oY + t, mPreLinePaint);
                canvas.drawLine(oX + t + temp, oY - temp, oX, oY + t, mPreLinePaint);
            }
        }
    }
}
