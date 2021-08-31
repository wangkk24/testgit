package com.pukka.ydepg.customui.tv.autoscroll;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.pukka.ydepg.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AutoScrollVerticalTextview extends TextSwitcher implements ViewSwitcher.ViewFactory {

    private static final int FLAG_START_AUTO_SCROLL = 0;
    private static final int FLAG_STOP_AUTO_SCROLL = 1;
    private long internalTime = 3L * 1000;
    private float mTextSize = 16;
    private int mPadding = 5;
    private int textColor = Color.BLACK;
    private boolean isFirstAttached = true;
    /**
     * @param textSize  字号
     * @param padding   内边距
     * @param textColor 字体颜色
     */
    public void setText(float textSize, int padding, int textColor) {
        mTextSize = textSize;
        mPadding = padding;
        this.textColor = textColor;
    }

    private OnItemClickListener itemClickListener;
    private Context mContext;
    private int currentId = -1;
    private ArrayList<String> textList;
    private MyHandler handler;

    public AutoScrollVerticalTextview(Context context) {
        this(context, null);
        mContext = context;
    }

    public AutoScrollVerticalTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        textList = new ArrayList<String>();
        handler = new MyHandler(this);
    }

    public void setAnimTime(long animDuration) {
        setFactory(this);
        setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom));
        setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_up));
    }

    private static class MyHandler extends Handler {
        private WeakReference<AutoScrollVerticalTextview> weakView;

        public MyHandler(AutoScrollVerticalTextview view) {
            weakView = new WeakReference<AutoScrollVerticalTextview>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            AutoScrollVerticalTextview verticalTextview=weakView.get();
            switch (msg.what) {
                case FLAG_START_AUTO_SCROLL:
                    if(null!=verticalTextview) {
                        if (verticalTextview.textList.size() > 0) {
                            verticalTextview.currentId++;
                            verticalTextview.currentId = verticalTextview.currentId % verticalTextview.textList.size();
                            verticalTextview.setText(verticalTextview.textList.get(verticalTextview.currentId));
                        }
                        sendEmptyMessageDelayed(FLAG_START_AUTO_SCROLL, verticalTextview.internalTime);
                    }
                    break;
                case FLAG_STOP_AUTO_SCROLL:
                    removeMessages(FLAG_START_AUTO_SCROLL);
                    break;
            }
        }
    }
    /**
     * 间隔时间
     *
     * @param time
     */
    public void setTextStillTime(final long time) {
        internalTime = time;
    }

    /**
     * 设置数据源
     *
     * @param titles
     */
    public void setTextList(List<String> titles) {
        textList.clear();
        textList.addAll(titles);
        currentId = -1;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isFirstAttached) {
            startAutoScroll();
        }
        isFirstAttached = false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoScroll();
    }

    /**
     * 开始滚动
     */
    public void startAutoScroll() {
        handler.sendEmptyMessage(FLAG_START_AUTO_SCROLL);
    }

    /**
     * 停止滚动
     */
    public void stopAutoScroll() {
        handler.sendEmptyMessage(FLAG_STOP_AUTO_SCROLL);
    }
    @Override
    public View makeView() {
        TextView t = new TextView(mContext);
        t.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
//        int w = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        this.measure(w, h);
//        int width = this.getMeasuredWidth();
//        int height = this.getMeasuredHeight();
        t.setMaxLines(3);
//        t.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ScreenUtil.getDimension(mContext, R.dimen.pan_scroll_vertical_text_height)));
        t.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        t.setPadding(mPadding, mPadding, mPadding, mPadding);
        t.setTextColor(textColor);
        t.setTextSize(mTextSize);
        t.setClickable(true);
        t.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null && textList.size() > 0 && currentId != -1) {
                    itemClickListener.onItemClick(currentId % textList.size());
                }
            }
        });
        return t;
    }

    /**
     * 设置点击事件监听
     *
     * @param itemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * 轮播文本点击监听器
     */
    public interface OnItemClickListener {
        /**
         * 点击回调
         *
         * @param position 当前点击ID
         */
        void onItemClick(int position);
    }

}