package com.pukka.ydepg.customui.tv.widget;

import android.content.Context;
import android.graphics.Rect;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.ui.template.PHMTemplate;
import com.pukka.ydepg.launcher.ui.template.VideoTemplate;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;
import com.pukka.ydepg.moudule.search.utils.ScreenUtil;

/**
 * 因为快速长按焦点丢失问题.
 * Created by hailongqiu on 2016/8/25.
 */
public class TVLinearLayoutManager extends LinearLayoutManager {
    private static final String TAG = TVLinearLayoutManager.class.getSimpleName();
    private static final int MIN_LEFT = ScreenUtil.getDimension(OTTApplication.getContext(), R.dimen.pan_common_group_marginLeft);

    private RecyclerViewTV recyclerViewTV;

    private RecyclerView.Recycler recycler;

    public TVLinearLayoutManager(Context context, RecyclerViewTV recyclerViewTV) {
        super(context);
        this.recyclerViewTV = recyclerViewTV;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.recycler == null) {
            this.recycler = recycler;
        }
        try {
            super.onLayoutChildren(recycler, state);
        } catch (Exception e) {
            SuperLog.error(TAG,e);
        }
    }

    private int getKeyCode(int direction){
        if (direction == View.FOCUS_DOWN){
            return KeyEvent.KEYCODE_DPAD_DOWN;
        }else if (direction == View.FOCUS_UP){
            return KeyEvent.KEYCODE_DPAD_UP;
        }else if (direction == View.FOCUS_LEFT){
            return KeyEvent.KEYCODE_DPAD_LEFT;
        }else if (direction == View.FOCUS_RIGHT){
            return KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        return -1;
    }

    @Override
    public View onInterceptFocusSearch(View focused, int direction) {
        if (direction == View.FOCUS_UP && (recyclerViewTV.computeVerticalScrollOffset() == 0 || findFirstVisibleItemPosition() == 0)) {
            return super.onInterceptFocusSearch(focused, direction);
        }

        View nextFocusView = FocusFinder.getInstance().findNextFocus(recyclerViewTV, recyclerViewTV.findFocus(), direction);
        recyclerViewTV.setFocusViewAndDirection(nextFocusView,getKeyCode(direction));

        /**解决长条海报不落焦情况------------start
         * 焦点在第一个资源位上或者最后一个资源位上，按下键跳过窄海报模板，焦点落到下下个模板上
         * */
        if (!isVideoTemplate(focused) && !isVideoTemplate(nextFocusView) &&null != nextFocusView && nextFocusView.getParent() instanceof PHMTemplate && focused.getParent() instanceof PHMTemplate
                && Math.abs(((PHMTemplate) nextFocusView.getParent()).getNavIndex() - ((PHMTemplate) focused.getParent()).getNavIndex()) > 1) {
            View rightFocusView;
            if (focused.getLeft() == MIN_LEFT){
                rightFocusView = FocusFinder.getInstance().findNextFocus(recyclerViewTV, recyclerViewTV.findFocus(), View.FOCUS_RIGHT);
            }else{
                rightFocusView = FocusFinder.getInstance().findNextFocus(recyclerViewTV, recyclerViewTV.findFocus(), View.FOCUS_LEFT);
            }
            if (null != rightFocusView){
                if (null != FocusFinder.getInstance().findNextFocus(recyclerViewTV, rightFocusView, direction)){
                    return FocusFinder.getInstance().findNextFocus(recyclerViewTV, rightFocusView, direction);
                }else{
                    return nextFocusView;
                }
            }
        }//-------------------end

        if (direction == View.FOCUS_DOWN && nextFocusView != null) {
            if (focused.getLeft() < MIN_LEFT) {
                SuperLog.debug(TAG, "focused.getLeft()" + focused.getLeft());
                if (nextFocusView.getLeft() < MIN_LEFT) {
                    SuperLog.debug(TAG, "nextFocusView.getLeft()" + nextFocusView.getLeft());
                    return nextFocusView;
                }
            }
            /**
             * 左边一条滑下去
             */
            if ((FocusFinder.getInstance().findNextFocus(recyclerViewTV, focused, View.FOCUS_LEFT) == null && focused.getParent() != nextFocusView.getParent()) || focused.getParent() instanceof VideoTemplate) {

                PHMTemplate phmTemplate = null;
                if (nextFocusView.getId() == R.id.rl_item_my_list || nextFocusView.getId() == R.id.rr_item_timeline
                        || nextFocusView.getId() == R.id.rr_item_vertical_scroll || nextFocusView.getId() == R.id.rr_item_series_vertical_scroll || nextFocusView.getId() == R.id.rr_item_varietyshow_vertical_scroll) {
                    phmTemplate = (PHMTemplate) nextFocusView.getParent().getParent().getParent().getParent();
                } else if (nextFocusView.getId() == R.id.rr_item_my_function && nextFocusView.getParent().getParent().getParent() instanceof PHMTemplate) {
                    phmTemplate = (PHMTemplate) nextFocusView.getParent().getParent().getParent();
                }else if (focused.getId() != R.id.rt_moreItem && nextFocusView.getId() != R.id.rt_moreItem && nextFocusView.getParent() instanceof PHMTemplate){//观看历史--更多历史--按下键Crash
                    phmTemplate = (PHMTemplate) nextFocusView.getParent();
                }
                if (null != phmTemplate && focused.getParent() != phmTemplate && phmTemplate.getFirstView() != null) {
                    return phmTemplate.getFirstView();
                }
            }
        }

        if (nextFocusView == null) {
            return focused;
        }
        return super.onInterceptFocusSearch(focused, direction);
    }

    private boolean isVideoTemplate(View view){
        if (null != view && view.getId() != R.id.pan_l1_r4_left){
            return true;
        }
        return false;
    }

    @Override
    public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate, boolean focusedChildVisible) {
        if (recyclerViewTV.requestChildRectangleOnScreen(child,rect,immediate)){
            return true;
        }else{
            return super.requestChildRectangleOnScreen(parent, child, rect, immediate, focusedChildVisible);
        }
    }
}