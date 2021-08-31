package com.pukka.ydepg.moudule.search.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.pukka.ydepg.launcher.ui.fragment.FocusInterceptor;
import com.pukka.ydepg.launcher.ui.fragment.FocusObserver;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.moudule.search.view.FocusRecyclerView.java
 * @date: 2018-03-01 09:20
 * @version: V1.0 描述当前版本功能
 */


public class FocusLinearLayout extends LinearLayout implements FocusObserver {
    private FocusInterceptor mIntercept;
    public FocusLinearLayout(Context context) {
        super(context);
    }

    public FocusLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setInterceptor(FocusInterceptor focusInterceptor) {
        if(mIntercept == null) {
            this.mIntercept = focusInterceptor;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //dispatchKeyEvent通常会调用2次，一次down一次up，调用up的时候焦点已经变成了切换后的焦点，所以要过滤掉up事件
        return (event.getAction() ==KeyEvent.ACTION_DOWN&&mIntercept!=null&&mIntercept.interceptFocus(event,this))||super.dispatchKeyEvent(event);
    }

}
