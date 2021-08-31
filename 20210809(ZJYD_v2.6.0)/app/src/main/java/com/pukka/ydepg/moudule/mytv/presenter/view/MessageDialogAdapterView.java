package com.pukka.ydepg.moudule.mytv.presenter.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;

/**
 * Created by Eason on 2018/5/14.
 */

public class MessageDialogAdapterView extends LinearLayout{

    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.
            ItemFocusScale(FocusHighlight.ZOOM_FACTOR_SMALL);

    public MessageDialogAdapterView(Context context) {
        this(context, null);
    }

    public MessageDialogAdapterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageDialogAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override protected void onFocusChanged(boolean gainFocus,int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        /*if(null!=mFocusHighlight){
            mFocusHighlight.onItemFocused(this,gainFocus);
        }*/
        if(null!=mListener){
            mListener.onFocusChange(gainFocus);
        }
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        //mFocusHighlight.onInitializeView(this);
    }

    public void setFocusListener(FocusListener listener){
        mListener=listener;
    }

    private FocusListener mListener;

    public interface FocusListener{
        void onFocusChange(boolean hasFocus);
    }

}
