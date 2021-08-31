package com.pukka.ydepg.launcher.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.viewpager.widget.ViewPager;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.ui.fragment.FocusInterceptor;
import com.pukka.ydepg.launcher.ui.fragment.FocusObserver;

import java.lang.reflect.Field;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.view.ViewPagerFocusExt.java
 * @date: 2017-12-29 17:55
 * @version: V1.0 描述当前版本功能
 */
public class ViewPagerFocusExt extends ViewPager implements FocusObserver {

    private FocusInterceptor mIntercept;

    public ViewPagerFocusExt(Context context) {
        super(context);
    }

    public ViewPagerFocusExt(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setInterceptor(FocusInterceptor focusInterceptor) {
        if(mIntercept == null) {
            this.mIntercept = focusInterceptor;
        }
    }

    public int getScrollState(){
        try {
            Field field = ViewPager.class.getDeclaredField("mScrollState");
            field.setAccessible(true);
            int state = (int)field.get(this);
            SuperLog.debug(getClass().getSimpleName(),"state:" + state);
            return state;
        } catch (Exception e) {
            SuperLog.error("ViewPagerFocusExt",e);
            return -1;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //dispatchKeyEvent通常会调用2次，一次down一次up，调用up的时候焦点已经变成了切换后的焦点，所以要过滤掉up事件
        return (event.getAction() == KeyEvent.ACTION_DOWN&&mIntercept!=null&&mIntercept.interceptFocus(event,this))||super.dispatchKeyEvent(event);
    }
}