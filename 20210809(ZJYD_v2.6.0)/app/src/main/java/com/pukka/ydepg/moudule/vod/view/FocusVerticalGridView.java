package com.pukka.ydepg.moudule.vod.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.leanback.widget.VerticalGridView;

import com.pukka.ydepg.launcher.ui.fragment.FocusInterceptor;
import com.pukka.ydepg.launcher.ui.fragment.FocusObserver;

/**
 * Created by liudo on 2018/3/15.
 */

public class FocusVerticalGridView extends VerticalGridView implements FocusObserver {
    private FocusInterceptor mIntercept;
    public FocusVerticalGridView(Context context) {
        super(context);
    }

    public FocusVerticalGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusVerticalGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public void setInterceptor(FocusInterceptor focusInterceptor) {
        if(mIntercept == null) {
            this.mIntercept = focusInterceptor;
        }
    }
    @SuppressLint("RestrictedApi")
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //dispatchKeyEvent通常会调用2次，一次down一次up，调用up的时候焦点已经变成了切换后的焦点，所以要过滤掉up事件
        return (event.getAction() ==KeyEvent.ACTION_DOWN&&mIntercept!=null&&mIntercept.interceptFocus(event,this))||super.dispatchKeyEvent(event);
    }
}
