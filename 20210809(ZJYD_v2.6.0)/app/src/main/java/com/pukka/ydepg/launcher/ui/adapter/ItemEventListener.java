package com.pukka.ydepg.launcher.ui.adapter;

import android.view.View;

/**
 * Item的事件接口
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.ui.adapter.ItemEventListener.java
 * @date: 2017-12-19 10:02
 * @version: V1.0 Item的事件接口
 */
public interface ItemEventListener {
    /**
     * 点击事件
     * @param view
     */
    void onItemClick(View view,int position);
}