package com.pukka.ydepg.common.extview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;


/**
 * Created by liudo on 2018/4/14.
 */

public class RetainCursorEditText extends AppCompatEditText {

    private Rect mTempRect;

    private boolean isShowCursor;

    private boolean  isRetainFocus;


    public boolean isRetainFocus(){
        return isRetainFocus;
    }

    private Handler mHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case 0:
                   isShowCursor=true;
                   invalidate();
                   break;
               case 1:
                   isShowCursor=false;
                   invalidate();
                   removeMessages(0);
                   removeMessages(1);
                   sendEmptyMessageDelayed(0,500);
                   break;
           }
        }
    };

    public RetainCursorEditText(Context context) {
        super(context);
    }

    public RetainCursorEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RetainCursorEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       if(isShowCursor) {
           Paint paint = new Paint();
           paint.setColor(Color.WHITE);
           paint.setStrokeWidth(2);
           Layout layout = this.getLayout();
           int offset = this.getSelectionStart();
           int left = clampHorizontalPosition(null, layout.getPrimaryHorizontal(offset));
           canvas.drawLine(left + getPaddingLeft() + 2, 10, left + getPaddingLeft() + 2, getMeasuredHeight() - 10, paint);
           mHandler.removeMessages(0);
           mHandler.removeMessages(1);
           mHandler.sendEmptyMessageDelayed(1,500);
       }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    private int clampHorizontalPosition(@Nullable final Drawable drawable, float horizontal) {
        horizontal = Math.max(0.5f, horizontal - 0.5f);
        if (mTempRect == null) mTempRect = new Rect();

        int drawableWidth = 0;
        if (drawable != null) {
            drawable.getPadding(mTempRect);
            drawableWidth = drawable.getIntrinsicWidth();
        } else {
            mTempRect.setEmpty();
        }

        int scrollX = this.getScrollX();
        float horizontalDiff = horizontal - scrollX;
        int viewClippedWidth = this.getWidth() - this.getCompoundPaddingLeft()
                - this.getCompoundPaddingRight();

        final int left;
        if (horizontalDiff >= (viewClippedWidth - 1f)) {
            // at the rightmost position
            left = viewClippedWidth + scrollX - (drawableWidth - mTempRect.right);
        } else if (Math.abs(horizontalDiff) <= 1f ||
                (TextUtils.isEmpty(this.getText())
                        && (1024 * 1024 - scrollX) <= (viewClippedWidth + 1f)
                        && horizontal <= 1f)) {
            // at the leftmost position
            left = scrollX - mTempRect.left;
        } else {
            left = (int) horizontal - mTempRect.left;
        }
        return left;
    }

    public  void stopRetainCursor(){
        mHandler.removeMessages(0);
        mHandler.removeMessages(1);
        isShowCursor=false;
        isRetainFocus=false;
        invalidate();

    }

    public void startRetainCursor(){
        isRetainFocus=true;
        isShowCursor=true;
        invalidate();
    }
}
