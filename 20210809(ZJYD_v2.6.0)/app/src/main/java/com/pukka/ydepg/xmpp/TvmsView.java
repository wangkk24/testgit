package com.pukka.ydepg.xmpp;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;

import com.pukka.ydepg.R;
import com.pukka.ydepg.moudule.search.view.MessageView;

import java.util.LinkedList;

/**
 * TVMS view
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.xmpp.TvmsView.java
 * @author:xj
 * @date: 2018-01-18 09:31
 */

public class TvmsView  {
    private static TvmsView tvmsView;
    private TvmsView(){

    }
    public static TvmsView getInstance(Context context){
        if (null == tvmsView){
            tvmsView = new TvmsView();
            tvmsView.init(context);
        }
        return tvmsView;
    }
    LinkedList<String> messageList = new LinkedList<>();
    WindowManager mWM;
    MessageView tv;
    Runnable mRunable = new Runnable() {
        @Override
        public void run() {
            if (messageList.size() > 0) {
                String poll = messageList.pollFirst();
                setMessage(poll);
            }else {
                tv.removeCallbacks(mRunable);
                mWM.removeViewImmediate(tv);

            }
        }
    };

    WindowManager.LayoutParams params;


    private void init(Context context ){
        mWM = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = 600;//固定大小
        params.format = PixelFormat.TRANSLUCENT;
        params.windowAnimations = android.R.style.Animation_Toast;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
        params.gravity = Gravity.BOTTOM;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.tvms_layout, null);
        tv = (MessageView)v.findViewById(R.id.mess);
    }
    private void setMessageList(LinkedList<String> messages){
        String message = messages.pollFirst();
        setMessage(message);

    }
    public void addNewMessage(String message){
        if (!messageList.contains(message)){
            messageList.add(message);
            ViewParent parent = tv.getParent();
            if (null == parent)
            setMessageList(messageList);
        }


    }
    private void setMessage(String message){
        tv.setText(message);
        tv.startScroll();
        if (null == tv.getParent()) {
            mWM.addView(tv, params);
        }
        tv.postDelayed(mRunable,tv.getRndDuration());
    }

}
