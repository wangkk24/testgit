package com.pukka.ydepg.common.extview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

@SuppressLint("AppCompatCustomView")
public class MarqueeEpgTextView extends TextView {
    /**
     * 是否停止滚动
     */
    private boolean mStopMarquee;
    private String mText;
    private float mCoordinateX;
    private float mTextWidth;
    private float mTextHeight;
    private float mParentViewWidth;
    private float mParentViewHeight;

    private float baseLineY;

    public MarqueeEpgTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setText(String text, int parentViewWidth, int parentViewHeight) {
        this.mText = text;
        mTextWidth = getPaint().measureText(mText);
        Paint.FontMetrics fm = getPaint().getFontMetrics();
        mTextHeight = (int) (Math.ceil(fm.descent - fm.ascent));
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        //windowWith = displayMetrics.widthPixels;
        mParentViewWidth = parentViewWidth;
        mParentViewHeight = parentViewHeight;
        baseLineY = parentViewHeight / 2 + Math.abs(getPaint().ascent() + getPaint().descent()) / 2;
        //SuperLog.debug("MarqueeEpgTextView", "mParentViewWidth=" + mParentViewWidth + "--baseLineY=" + baseLineY + ";mParentViewHeight=" + mParentViewHeight + ";mTextHeight=" + mTextHeight + ";getPaint().ascent() + getPaint().descent()=" + getPaint().ascent() + getPaint().descent());
        if (mHandler.hasMessages(0))
            mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }


    @SuppressLint("NewApi")
    @Override
    protected void onAttachedToWindow() {
        mStopMarquee = false;
        if (!(mText == null || mText.isEmpty()))
            mHandler.sendEmptyMessageDelayed(0, 1000);
        super.onAttachedToWindow();
    }


    @Override
    protected void onDetachedFromWindow() {
        mStopMarquee = true;
        if (mHandler.hasMessages(0))
            mHandler.removeMessages(0);
        super.onDetachedFromWindow();
    }


    @SuppressLint("NewApi")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!(mText == null || mText.isEmpty()))
            canvas.drawText(mText, mCoordinateX, baseLineY + 1, getPaint());//mTextHeight-1
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (mCoordinateX < 0 && Math.abs(mCoordinateX) > mTextWidth) {
                        mCoordinateX = mParentViewWidth;
                    } else {
                        mCoordinateX -= 1;
                    }
                    invalidate();
                    sendEmptyMessageDelayed(0, 30);
                    break;
            }
        }
    };
}
