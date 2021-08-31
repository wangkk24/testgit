package com.pukka.ydepg.common.extview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.utils.uiutil.Drawables;


public class LinearLayoutExt extends LinearLayout {
    public LinearLayoutExt(Context context) {
        super(context);
    }

    public LinearLayoutExt(Context context, AttributeSet attrs) {
        super(context, attrs);
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
