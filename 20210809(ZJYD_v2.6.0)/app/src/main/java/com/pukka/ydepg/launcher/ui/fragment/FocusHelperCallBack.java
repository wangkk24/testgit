package com.pukka.ydepg.launcher.ui.fragment;

import android.view.View;

/**
 * 管理首页fragment内部焦点，如果需要的话
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.ui.fragment.FocusHelperCallBack.java
 * @date: 2017-12-20 11:16
 * @version: V1.0 描述当前版本功能
 */
public interface FocusHelperCallBack {
    /**
     * 给fragment第一个可以获取焦点的item设置焦点并返回
     * @return
     */
   View getFirstFocusView();
    /**
     * 滚动到顶部
     */
    void scrollToTop();
}