package com.pukka.ydepg.launcher.ui.fragment;

import android.view.KeyEvent;
import android.view.View;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.ui.fragment.FocusInterceptor.java
 * @date: 2017-12-25 17:40
 * @version: V1.0 描述当前版本功能
 */


public interface FocusInterceptor {
   boolean interceptFocus(KeyEvent event, View view);
}
