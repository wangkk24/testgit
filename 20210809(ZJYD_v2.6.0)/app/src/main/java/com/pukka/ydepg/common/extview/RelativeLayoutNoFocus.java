package com.pukka.ydepg.common.extview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 不消费requestFocus的RelativeLayout容器
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.common.extview.RelativeLayoutNoFoucs.java
 * @date: 2017-12-15 17:41
 * @version: V1.0 不消费requestFocus的RelativeLayout容器
 */


public class RelativeLayoutNoFocus extends RelativeLayoutExt {
    public RelativeLayoutNoFocus(Context context) {
        super(context);
    }

    public RelativeLayoutNoFocus(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(false);
    }
}
