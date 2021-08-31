package com.pukka.ydepg.common.extview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.pukka.ydepg.OTTApplication;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.uiutil.Drawables;


public class RelativeLayoutExt extends RelativeLayout {

    private ImageViewExt bgImageV = null;
    private Context context;

    public RelativeLayoutExt(Context context) {
        super(context);
    }

    public RelativeLayoutExt(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init4WhiteLabel(attrs);
        addFocusEffect();
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

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (null != bgImageV){
            if (gainFocus){
                bgImageV.bringToFront();
            }
            bgImageV.setSelected(gainFocus);
        }
    }

    public void addFocusEffect() {
        if (isNeedAddFocusEffect() && null == bgImageV){
            bgImageV = new ImageViewExt(context);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            bgImageV.setLayoutParams(params);
            bgImageV.setImageResource(R.drawable.select_ref_main);
            addView(bgImageV);
        }
    }

    private boolean isNeedAddFocusEffect(){
        if (getId() == R.id.rl_container_auto_scroll_tem || getId() == R.id.rl_simple_epg_group3 || getId() == R.id.rl_simple_epg_group2
            || getId() == R.id.rl_refresh || getId() == R.id.rl_setting || getId() == R.id.rl_filter || getId() == R.id.rl_children_epg || getId() == R.id.rl_epg
            || getId() == R.id.rl_simple_epg || getId() == R.id.rl_children_epg || getId() == R.id.rl_ordinary){
            return true;
        }else{
            return false;
        }
    }

}