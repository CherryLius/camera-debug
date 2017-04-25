package com.cherry.library.ui.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.cherry.library.ui.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/13.
 */

public class CountDownView extends View {

    private static final String TAG = "CountDownView";

    private int mWidth, mHeight;
    private float mRadius;
    private float mIndicateRadius = 12;
    private int mStrokeWidth = 8;
    private float mStartAngle = 270;
    private float mAngle = 90;
    private int mCircleColor = Color.RED;
    private int mTextColor = Color.WHITE;

    private Paint mPaint;
    private TextPaint mTextPaint;

    private RectF mOval;

    private long mTotalTime = 0;
    private long mCurrentTime = 0;

    private String mTimeString;

    private Disposable mDisposable;
    private ValueAnimator mAnimator;
    private SimpleDateFormat mFormatter;

    private Listener mListener;

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        if (ta != null) {
            mStrokeWidth = ta.getDimensionPixelOffset(R.styleable.CountDownView_strokeWidth, mStrokeWidth);
            mCircleColor = ta.getColor(R.styleable.CountDownView_circleColor, mCircleColor);
            mTextColor = ta.getColor(R.styleable.CountDownView_textColor, mTextColor);
            mStartAngle = ta.getFloat(R.styleable.CountDownView_startAngle, mStartAngle);
            mAngle = ta.getFloat(R.styleable.CountDownView_angle, mAngle);
            mRadius = ta.getDimensionPixelOffset(R.styleable.CountDownView_radius, 0);
            mIndicateRadius = ta.getDimension(R.styleable.CountDownView_indicatorRadius, mIndicateRadius);
            ta.recycle();
        }
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.DITHER_FLAG
                | Paint.FILTER_BITMAP_FLAG);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mCircleColor);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.density = getResources().getDisplayMetrics().density;
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(mTextColor);
        mTimeString = "";
        mOval = new RectF();
        mFormatter = new SimpleDateFormat("HH:mm:ss");
        mFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize((int) (mRadius * 1.2f), widthMeasureSpec);
        int height = getDefaultSize((int) (mRadius * 1.2f), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        int centerW = mWidth / 2;
        int centerH = mHeight / 2;
        mRadius = (Math.min(mWidth, mHeight) - (mIndicateRadius * 2)) / 2.0f;
        mOval.left = centerW - mRadius;
        mOval.top = centerH - mRadius;
        mOval.right = centerW + mRadius;
        mOval.bottom = centerH + mRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getVisibility() != View.VISIBLE)
            return;
        int centerW = mWidth / 2;
        int centerH = mHeight / 2;

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(centerW, centerH, mRadius, mPaint);

        mPaint.setColor(mCircleColor);

        canvas.drawArc(mOval, mStartAngle, -mAngle, false, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        float delta = 90 + mAngle;
        double xOffset = mRadius * Math.cos(Math.toRadians(delta));
        double yOffset = mRadius * Math.sin(Math.toRadians(delta));
        float cx = (float) (centerW + xOffset);
        float cy = (float) (centerH - yOffset);
        canvas.drawCircle(cx, cy, mIndicateRadius, mPaint);

        int textSize = (int) (mRadius * 2 / mTimeString.length());
        mTextPaint.setTextSize(textSize);
        int baseY = (int) (centerH - (mTextPaint.descent() + mTextPaint.ascent()) / 2);
        canvas.drawText(mTimeString, 0, mTimeString.length(), centerW, baseY, mTextPaint);
    }

    public void setCountDownTime(int hour, int minute, int second) {
        mTotalTime = hour * 60 * 60 + minute * 60 + second;
        Log.d(TAG, "mTotalTime=" + mTotalTime);
    }

    public void start() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(mTotalTime + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return mTotalTime - aLong;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe " + d);
                        mDisposable = d;
                        if (mCurrentTime == 0) {
                            createAnimator();
                            mAnimator.start();
                            if (mListener != null)
                                mListener.onStart();
                        }
                    }

                    @Override
                    public void onNext(Long aLong) {
                        mCurrentTime = aLong;
                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                        calendar.setTimeInMillis(aLong * 1000);
                        mTimeString = mFormatter.format(calendar.getTime());
                        Log.d(TAG, "onNext=" + calendar.get(Calendar.SECOND)
                                + "," + calendar.get(Calendar.MINUTE));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError=" + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                        postInvalidate();
                        if (mListener != null)
                            mListener.onComplete();
                    }
                });

    }

    public void stop() {
        if (mDisposable != null && !mDisposable.isDisposed())
            mDisposable.dispose();
        if (mAnimator != null && mAnimator.isRunning())
            mAnimator.cancel();
        mDisposable = null;
        mAnimator = null;
        mCurrentTime = 0;
        mTotalTime = 0;
    }

    public void pause() {
        if (mDisposable != null && !mDisposable.isDisposed())
            mDisposable.dispose();
        if (mAnimator != null && mAnimator.isRunning())
            mAnimator.pause();
    }

    public void resume() {
        if (mCurrentTime > 0) {
            mTotalTime = mCurrentTime - 1;
            start();
        }
        if (mAnimator != null && mAnimator.isPaused())
            mAnimator.resume();
    }

    public void setListener(Listener l) {
        mListener = l;
    }

    private void createAnimator() {
        mAnimator = ValueAnimator.ofFloat(0, 1).setDuration(mTotalTime * 1000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                mAngle = 360 * val;
                postInvalidate();
            }
        });
    }

    public interface Listener {
        void onStart();

        void onComplete();
    }
}
