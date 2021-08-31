package com.pukka.ydepg.common.extview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.utils.uiutil.Drawables;

/**
 * Created by hasee on 2017/4/24.
 */

public class ViewExt extends View {
    public ViewExt(Context context) {
        super(context);
    }

    public ViewExt(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init4WhiteLabel(attrs);
    }

    public ViewExt(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init4WhiteLabel(attrs);
    }
    private void init4WhiteLabel(AttributeSet attrs) {
        if (!this.isInEditMode() && OTTApplication.getContext().isNeedLoadResource()) {
            String resourceName = "";
            int resourceId = -1;
            int count = attrs.getAttributeCount();
            for (int i = 0; i < count; i++) {
                resourceName = attrs.getAttributeName(i);
                resourceId = attrs.getAttributeResourceValue(i, -1);
                if (-1 != resourceId && "background".equals(resourceName)) {
                    Drawables.getInstance().setDrawable(this, resourceId, true);
                }
            }
        }
    }

}
