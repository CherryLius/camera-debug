package com.cherry.library.ui.view.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;

import com.cherry.library.ui.R;

/**
 * Created by Administrator on 2017/5/3.
 */

public class SectionItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private ISectionProvider mProvider;

    private Paint mPaint;
    private TextPaint mTextPaint;
    private Paint.FontMetrics mFontMetrics;

    private int mSectionGap;
    private int mTextSize;
    private int mTextPadding;

    private Rect mRect;

    public SectionItemDecoration(Context context, ISectionProvider provider) {
        mContext = context;
        mProvider = provider;
        init();
    }

    private void init() {
        mRect = new Rect();
        mSectionGap = mContext.getResources().getDimensionPixelSize(R.dimen.section_height);
        mTextPadding = mContext.getResources().getDimensionPixelSize(R.dimen.section_text_padding);
        mTextSize = mContext.getResources().getDimensionPixelSize(R.dimen.section_text_size);
        int textColor = ContextCompat.getColor(mContext, R.color.section_text_color);
        int backgroundColor = ContextCompat.getColor(mContext, R.color.section_background_color);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(backgroundColor);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(mTextSize);
        mFontMetrics = mTextPaint.getFontMetrics();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int top = isFirstSectionPosition(position) ? mSectionGap : 0;
        outRect.set(0, top, 0, 0);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int position = parent.getChildAdapterPosition(child);
            if (isFirstSectionPosition(position)) {
                final int top = child.getTop() - mSectionGap;
                final int bottom = child.getTop();
                drawSection(c, mProvider.getSectionTitle(position), resetRect(left, top, right, bottom));
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int position = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            if (isFirstSectionPosition(position + 1)) {
                int bottom = child.getBottom();
                if (bottom <= mSectionGap) {
                    drawSection(c, mProvider.getSectionTitle(position),
                            resetRect(left, bottom - mSectionGap, right, bottom));
                    return;
                }
            }
        }
        drawSection(c, mProvider.getSectionTitle(position),
                resetRect(left, parent.getPaddingTop(), right, mSectionGap));
    }

    private void drawSection(Canvas canvas, String text, Rect rect) {
        canvas.drawRect(rect, mPaint);
        float baseY = (mSectionGap - (mFontMetrics.ascent + mFontMetrics.descent)) / 2;
        canvas.drawText(text, rect.left + mTextPadding, rect.top + baseY, mTextPaint);
    }

    private boolean isFirstSectionPosition(int position) {
        if (position == 0) return true;
        long preSectionId = mProvider.getSectionId(position - 1);
        long sectionId = mProvider.getSectionId(position);
        return sectionId != -1 && preSectionId != sectionId;
    }

    private Rect resetRect(int left, int top, int right, int bottom) {
        mRect.left = left;
        mRect.top = top;
        mRect.right = right;
        mRect.bottom = bottom;
        return mRect;
    }

    public interface ISectionProvider {
        long getSectionId(int position);

        String getSectionTitle(int position);
    }
}
