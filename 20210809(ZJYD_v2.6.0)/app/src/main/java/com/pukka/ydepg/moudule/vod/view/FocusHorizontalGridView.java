package com.pukka.ydepg.moudule.vod.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;

import androidx.leanback.widget.HorizontalGridView;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.ui.fragment.FocusInterceptor;
import com.pukka.ydepg.launcher.ui.fragment.FocusObserver;

/**
 * Created by liudo on 2018/7/16.
 */

public class FocusHorizontalGridView extends HorizontalGridView implements FocusObserver {
    private FocusInterceptor mIntercept;
    public FocusHorizontalGridView(Context context) {
        super(context);
    }

    public FocusHorizontalGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusHorizontalGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
        boolean i=(event.getAction() ==KeyEvent.ACTION_DOWN&&mIntercept!=null&&mIntercept.interceptFocus(event,this))||super.dispatchKeyEvent(event);
        return i;
    }
}
