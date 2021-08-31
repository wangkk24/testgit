package com.pukka.ydepg.launcher.util;

import android.graphics.RectF;
import android.view.View;
import android.view.ViewParent;

import androidx.viewpager.widget.ViewPager;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.customui.tv.bridge.EffectNoDrawBridge;
import com.pukka.ydepg.customui.tv.widget.MainUpView;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;
import com.pukka.ydepg.moudule.search.utils.ScreenUtil;


/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.util.FocusEffectWrapper.java
 * @date: 2017-12-29 16:03
 * @version: V1.0 描述当前版本功能
 */
public class FocusEffectWrapper {
    private EffectNoDrawBridge mEffectNoDrawBridge;
    private MainUpView mMainUpView;
    private RectF rectF;

    //首页垂直滚动特俗模板，不用共用的MainUpView,,用item的焦点白框
    private boolean isHideMainUpView = true;

    public FocusEffectWrapper(EffectNoDrawBridge effectNoDrawBridge, MainUpView mainUpView, RectF rectF) {
        this.mEffectNoDrawBridge = effectNoDrawBridge;
        this.mMainUpView = mainUpView;
        this.rectF = rectF;
        initFocusEffect();
    }

    private void initFocusEffect() {
        if (mMainUpView == null) {
            throw new NullPointerException();
        }
        if (mEffectNoDrawBridge == null) {
            throw new NullPointerException();
        }
        if (rectF == null) {
            //首页焦点框效果--白色
            rectF = new RectF(ScreenUtil.getDimension(OTTApplication.getContext(), R.dimen.order_center_line_height),
                    ScreenUtil.getDimension(OTTApplication.getContext(), R.dimen.order_center_line_height),
                    ScreenUtil.getDimension(OTTApplication.getContext(), R.dimen.order_center_line_height),
                    ScreenUtil.getDimension(OTTApplication.getContext(), R.dimen.order_center_line_height)
            );
        }
        mMainUpView.setEffectBridge(mEffectNoDrawBridge);
        mEffectNoDrawBridge.setDrawUpRectPadding(rectF);
        mEffectNoDrawBridge.setVisibleWidget(true);
    }
    public void setIsHideMainUpView(boolean b) {
        isHideMainUpView = b;
    }

    public void drawFocusEffect(View oldFocus, View newFocus) {
        if (newFocus == null) {
            return;
        }
        clearEffectBridge(oldFocus);
        if (isHideMainUpView){
            hideMainUpView();
        }else{
            mEffectNoDrawBridge.setVisibleWidget(false);
        }

        /*异型海报要求新焦点是异形类型需要将焦点至于内容后面*/
        boolean isNewFocusSpecialPoster = newFocus instanceof ReflectRelativeLayout && ((ReflectRelativeLayout) newFocus).getPosterIsAlienType();
        if (isNewFocusSpecialPoster) {
            ViewParent viewParent = newFocus.getParent().getParent().getParent().getParent();
            if(viewParent instanceof ViewPager){
                ((ViewPager) viewParent).bringToFront();
            }
        } else {
            mMainUpView.bringToFront();
        }

        mEffectNoDrawBridge.setUpRectResource(R.drawable.btn_border_focus_radio); //首页焦点框效果--白色

        mEffectNoDrawBridge.clearAnimator(); // 清除之前的动画.
        mEffectNoDrawBridge.setTranDurAnimTime(0); // 避免边框从其它地方跑出来.
        if (isVideoWindow(newFocus)) {
            //视频播放窗口不放大
            mMainUpView.setFocusView(newFocus, oldFocus, 1.0f);
        } else {
            mMainUpView.setFocusView(newFocus, oldFocus, 1.06f);
        }
    }

    private boolean isVideoWindow(View newFocus) {
        if (newFocus instanceof ReflectRelativeLayout && ((ReflectRelativeLayout)newFocus).isVideoWinDow()){
            return true;
        }else{
            return R.id.rl_mix_item1_parent == newFocus.getId() || R.id.rl_mix_item2_parent == newFocus.getId() ||
                    R.id.pan_l1_r4_left == newFocus.getId() || R.id.rl_simple_epg_group2 == newFocus.getId()
                    || R.id.rl_mix_item3_parent == newFocus.getId() || R.id.tv_main_refresh_btn == newFocus.getId()
                    || R.id.rr_item_vertical_scroll == newFocus.getId()
                    || R.id.rr_item_series_vertical_scroll == newFocus.getId() || R.id.rr_item_varietyshow_vertical_scroll == newFocus.getId()
                    || R.id.tv_epg_scroll_ads == newFocus.getId() || R.id.tv_epg_function_item == newFocus.getId() || R.id.tv_change_user_btn == newFocus.getId()
                    || R.id.tv_user_title_bg == newFocus.getId() || R.id.iv_logo1 == newFocus.getId();
        }
    }

    /**
     * 清除焦点效果
     *
     * @param view
     */
    public void clearEffectBridge(View view) {
        mMainUpView.setUnFocusView(view);//将原焦点框缩小
        mEffectNoDrawBridge.setVisibleWidget(true);
    }

    public void hideMainUpView(){
        mEffectNoDrawBridge.setVisibleWidget(isHideMainUpView);
        isHideMainUpView = false;
    }

    public static class FocusEffectBuilder {
        private EffectNoDrawBridge innerEffect;
        private MainUpView innerMainUpView;
        private RectF rectF;

        public FocusEffectBuilder effectNoDrawBridge(EffectNoDrawBridge effectNoDrawBridge) {
            this.innerEffect = effectNoDrawBridge;
            return this;
        }

        public FocusEffectBuilder mainUpView(MainUpView mainUpView) {
            this.innerMainUpView = mainUpView;
            return this;
        }

        public FocusEffectBuilder padding(RectF rectF) {
            this.rectF = rectF;
            return this;
        }

        public FocusEffectWrapper build() {
            return new FocusEffectWrapper(innerEffect, innerMainUpView, rectF);
        }
    }
}