package com.cherry.library.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/4/20.
 */

public class DotLineView extends View {

    private int mDotRadius;
    private int mLineWidth;
    private int mLineHeight;

    private boolean mSelected;

    private int mSelectedColor;
    private int mNormalColor;

    private Paint mPaint;

    private int mWidth;
    private int mHeight;

    public DotLineView(Context context) {
        super(context);
        init();
    }

    public DotLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DotLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mDotRadius = 10;
        mLineWidth = 3;
        mLineHeight = 15;
        mSelectedColor = Color.RED;
        mSelected = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mSelected ? mSelectedColor : mNormalColor);
        canvas.drawCircle(mWidth / 2, mDotRadius, mDotRadius, mPaint);

        canvas.drawLine(mWidth / 2, 0, mWidth / 2, mHeight, mPaint);
    }

    public void setSelectedColor(int color) {
        mSelectedColor = color;
        postInvalidate();
    }

    public void setNormalColor(int color) {
        mNormalColor = color;
        postInvalidate();
    }

    public void setSelected(boolean flag) {
        mSelected = flag;
        postInvalidate();
    }
}
