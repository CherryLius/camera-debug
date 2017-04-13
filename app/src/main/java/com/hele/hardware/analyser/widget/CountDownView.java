package com.hele.hardware.analyser.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/4/13.
 */

public class CountDownView extends View {

    private int mWidth, mHeight;
    private float mRadius;
    private float mIndicateRadius = 10;
    private float mStrokeWidth = 8;
    private float mStartAngle = 270;
    private float mAngle = 90;


    private Paint mPaint;

    public CountDownView(Context context) {
        super(context);
        init();
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mStrokeWidth = 8;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.DITHER_FLAG
                | Paint.FILTER_BITMAP_FLAG);
        mPaint.setStrokeWidth(mStrokeWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = (Math.min(mWidth, mHeight) - (mIndicateRadius * 2)) / 2.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerW = mWidth / 2;
        int centerH = mHeight / 2;

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(centerW, centerH, mRadius, mPaint);

        mPaint.setColor(Color.RED);
        RectF oval = new RectF(centerW - mRadius, centerH - mRadius,
                centerW + mRadius, centerH + mRadius);
        canvas.drawArc(oval, mStartAngle, -mAngle, false, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        float delta = mStartAngle - mAngle;
        double xOffset = mRadius * Math.cos(Math.toRadians(delta));
        double yOffset = mRadius * Math.sin(Math.toRadians(delta));
        float cx = (float) (centerW + xOffset);
        float cy = (float) (centerH - yOffset);
        canvas.drawCircle(cx, cy, mIndicateRadius, mPaint);
    }
}
