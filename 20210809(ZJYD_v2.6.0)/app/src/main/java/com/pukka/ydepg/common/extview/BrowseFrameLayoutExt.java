package com.pukka.ydepg.common.extview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.leanback.widget.BrowseFrameLayout;

/**
 * BrowseFrameLayout扩展类
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.common.extview.BrowseFrameLayoutExt.java
 * @date: 2017-12-15 10:38
 * @version: V1.0 BrowseFrameLayout扩展类
 */


public class BrowseFrameLayoutExt extends BrowseFrameLayout {
    public BrowseFrameLayoutExt(Context context) {
        super(context);
    }

    public BrowseFrameLayoutExt(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(false);
    }

    public BrowseFrameLayoutExt(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
