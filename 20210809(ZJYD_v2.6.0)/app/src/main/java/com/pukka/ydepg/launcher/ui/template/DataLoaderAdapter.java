package com.pukka.ydepg.launcher.ui.template;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;

/**
 * 模板数据加载基类
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.ui.template.DataLoaderAdapter.java
 * @date: 2018-02-01 10:22
 * @version: V1.0 描述当前版本功能
 */


public abstract class DataLoaderAdapter<T> {
    /**
     *
     * @param title      标题
     * @param subtitle   副标题
     * @param imageView  背景图片
     * @param t          数据
     */
    abstract public void loadData(TextView title, TextView subtitle, View mLayer, ImageView imageView, T t, ReflectRelativeLayout relativeLayout);
}
