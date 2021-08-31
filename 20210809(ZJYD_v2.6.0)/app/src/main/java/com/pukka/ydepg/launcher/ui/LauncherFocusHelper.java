package com.pukka.ydepg.launcher.ui;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.ImageViewExt;
import com.pukka.ydepg.common.extview.LinearLayoutExt;
import com.pukka.ydepg.common.extview.RelativeLayoutExt;
import com.pukka.ydepg.common.extview.ShimmerImageView;
import com.pukka.ydepg.common.http.v6bean.v6node.Navigate;
import com.pukka.ydepg.common.report.ubd.scene.UBDSwitch;
import com.pukka.ydepg.common.utils.DeviceInfo;
import com.pukka.ydepg.common.utils.GlideUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.customui.tv.autoscroll.AutoScrollViewPager;
import com.pukka.ydepg.customui.tv.bridge.RecyclerViewBridge;
import com.pukka.ydepg.customui.tv.widget.RecyclerViewTV;
import com.pukka.ydepg.event.AutoScrollViewEvent;
import com.pukka.ydepg.launcher.ui.fragment.FocusHelperCallBack;
import com.pukka.ydepg.launcher.ui.fragment.FocusInterceptor;
import com.pukka.ydepg.launcher.ui.fragment.MyPHMFragment;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.ui.template.MyListTemplate;
import com.pukka.ydepg.launcher.ui.template.VideoLiveTemplate;
import com.pukka.ydepg.launcher.util.FocusEffectWrapper;
import com.pukka.ydepg.launcher.util.Utils;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.mytv.utils.MessageDataHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * 处理焦点事件和显示效果,与LauncherActivity解耦
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.ui.LauncherFocusHelper.java
 * @date: 2017-12-20 10:14
 * @version: V1.0 处理焦点事件和显示效果,与LauncherActivity解耦
 */
public class LauncherFocusHelper implements FocusInterceptor {

    private FocusEffectWrapper mFocusEffectWrapper;  //边框效果控件
    private final MainActivity mActivity;
    private View mCurrentFocus;   //当前焦点
    private View mPreviousFocus; //上一个焦点
    private final String TAG = "LauncherFocusHelper";
    private boolean isiIntercept;
    private int mDirection;
    private static final int MIN_CLICK_DELAY = 300;
    private long lastClickTime;

    //焦点到达边界抖动flag
    private boolean isShaked = false;

    private View mFirstFocusView = null;

    private boolean isVideoRequest = false;

    public void setIsVideoRequest(boolean isVideoRequest){
        this.isVideoRequest = isVideoRequest;
    }

    public LauncherFocusHelper(MainActivity activity) {
        this.mActivity = activity;
        init();
    }

    private void init() {
        initListener();
        mFocusEffectWrapper = new FocusEffectWrapper.FocusEffectBuilder().effectNoDrawBridge(new RecyclerViewBridge()).mainUpView(mActivity.mMainUpView).build();
        //设置焦点拦截器
        mActivity.mViewpager.setInterceptor(this);
    }

    /**
     * 设置标题文字focus变化的效果
     *
     * @param focus    是否获取焦点
     * @param textView 标题栏文字
     */
    public void setFocusChangeEffect(boolean focus, TextView textView,int position) {
        if (!hasNavFocus()){
            setNavItemUnSelect(textView,0,null,null);
        }else if (focus && !mActivity.mIvSearch.hasFocus()) {
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            //textView.setTextColor(ContextCompat.getColor(mActivity, R.color.c21_color));
            textView.setTextColor(Utils.getNavTitleColor(position));
            textView.setSelected(true);//tab title 背景色设置
        } else {
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            //textView.setTextColor(ContextCompat.getColor(mActivity, R.color.c24_color));
            textView.setTextColor(Utils.getNavTitleColorNormal(position));
            textView.setSelected(false);//tab title 背景色设置
        }
    }

    private List<Navigate> mNavigates;

    private boolean isTvVisibility(TextView tv) {
        if (tv.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    /**
     * onfoon
     * 注册焦点监听事件
     */
    private void initListener() {
        //注册焦点全局变化监听
        mActivity.getWindow().getDecorView().getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                specialTtemplateFocusChange(oldFocus,newFocus);
            }
        });
        //焦点事件触发时，拦截焦点搜索，只对焦点到达边界起作用
        mActivity.mContainerLayout.setOnFocusSearchListener((focused, direction) -> {
            return onFocusSearch(focused, direction);
        });

        //导航栏获取或失去焦点切换效果
        mActivity.mNavRecyclerView.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                LinearLayoutExt llNavTitleLine = itemView.findViewById(R.id.ll_nav_title_line);
                TextView titleTv = (TextView) itemView.findViewById(R.id.tv_nav_title);
                ImageView imView = (ImageView) itemView.findViewById(R.id.im_nav_title);
                RelativeLayout rlNav = (RelativeLayout) itemView.findViewById(R.id.rl_nav);
                if (mActivity.mNavFoucsKeyEvent == KeyEvent.KEYCODE_DPAD_DOWN){
                    mActivity.focusManageEpgTop(false);
                    //焦点在导航栏上，按下键，焦点离开导航行栏,清除字体落焦效果
                    View firstChildFocusView = getFirstChildFocusView();
                    if (null != firstChildFocusView && firstChildFocusView.getId() == R.id.rl_topItem_container_0_11){
                        mFirstFocusView = firstChildFocusView;
                    }
                    if (isTvVisibility(titleTv)){
                        llNavTitleLine.setVisibility(View.VISIBLE);
                        titleTv.setSelected(false);
                    }else{//次落焦
                        loadNavImgBg(position,imView,titleTv,2,rlNav);
                    }
                }else if (mActivity.mNavFoucsKeyEvent == KeyEvent.KEYCODE_DPAD_UP){
                    //焦点在导航栏上，按上键键，焦点落到手动刷新上
                    mActivity.focusManageEpgTop(true);
                    //setNavItemUnSelect(titleTv,position,imView,rlNav);
                    if (isTvVisibility(titleTv)){
                        llNavTitleLine.setVisibility(View.VISIBLE);
                        titleTv.setSelected(false);
                    }else{//次落焦
                        loadNavImgBg(position,imView,titleTv,2,rlNav);
                    }
                }else{
                    if (isTvVisibility(titleTv)){
                        if (!isVideoRequest){
                            setFocusChangeEffect(true, titleTv,position);
                        }
                    }else{
                        loadNavImgBg(position,imView,titleTv,0,rlNav);
                    }
                }
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                if (mActivity.mIvSearch.hasFocus()) {
                    return;
                } else {
                    //为了解决两个导航栏同时有焦点效果，每次设置效果前，先将所有的导航栏清除焦点效果
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        View item = parent.getChildAt(i);
                        TextView titleTv = item.findViewById(R.id.tv_nav_title);
                        LinearLayoutExt llNavTitleLine = item.findViewById(R.id.ll_nav_title_line);
                        llNavTitleLine.setVisibility(View.GONE);
                        setFocusChangeEffect(false, titleTv,parent.getChildLayoutPosition(item));
                    }
                }
                TextView titleTv = itemView.findViewById(R.id.tv_nav_title);
                ImageView imView = itemView.findViewById(R.id.im_nav_title);
                RelativeLayout rlNav =  itemView.findViewById(R.id.rl_nav);

                if (!mActivity.getIsFirstShowFocus() || mActivity.mNavFoucsKeyEvent != KeyEvent.KEYCODE_DPAD_UP){
                    loadNavImgBg(position,imView,titleTv,1,rlNav);
                }else{
                    mActivity.setIsFirstShowFocus(false);
                }
                setFocusChangeEffect(true, titleTv,position);
                try {
                    //导航栏获焦后触发viewpager滚动
                    mActivity.mViewpager.setCurrentItem(position);

                    //导航栏切换记录位置,用于页面跳转时上报UBD
                    UBDSwitch.getInstance().setNavPosition(mActivity.currentPosition);

                    if (mActivity.mFragmentAdapter.size() > position && null != mActivity.mFragmentAdapter.get(position)) {
                        if (position >= 0) {
                            PHMFragment fragment = mActivity.mFragmentAdapter.get(position);
                            fragment.scrollToTop();
                        }
                    }
                } catch (Exception e) {
                    SuperLog.error(TAG,e);
                }

                mActivity.refreshPbsEpg(true);
            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {}
        });
    }

    private void setNavItemUnSelect(TextView titleTv,int position,ImageView imView,RelativeLayout rlNav){
        if (isTvVisibility(titleTv)){
            titleTv.setSelected(false);
        }
    }

    /**
     * 某些盒子 Q5、cm201z栏目下第一个模板为窄模板（rl_topItem_container_0_11），从导航栏上按下键无法落焦
     * 在栏目下第二个模板按上键应当落在第一个模板上（rl_topItem_container_0_11），未落，再次点击上键，焦点直接越过第一个模板，落在了导航栏上
     * */
    private void specialTtemplateFocusChange(View oldFocus,View newFocus) {
        View firstChildFocusView = getFirstChildFocusView();
        if (null == firstChildFocusView || (null != firstChildFocusView && firstChildFocusView.getId() != R.id.rl_topItem_container_0_11)){
            LauncherFocusHelper.this.onGlobalFocusChanged(oldFocus, newFocus);
        }else{
            if (mActivity.mNavFoucsKeyEvent == KeyEvent.KEYCODE_DPAD_UP && mDirection == KeyEvent.KEYCODE_DPAD_UP && null != newFocus && newFocus.getId() == R.id.tv_nav_parent
                    && null != oldFocus && oldFocus.getId() != R.id.rl_topItem_container_0_11 && oldFocus.getId() != R.id.tv_nav_parent
                    && null != firstChildFocusView && firstChildFocusView.getId() == R.id.rl_topItem_container_0_11){
                LauncherFocusHelper.this.onGlobalFocusChanged(oldFocus, firstChildFocusView);
                firstChildFocusView.requestFocus();
            }else{
                if (null == mFirstFocusView || null == oldFocus) {
                    LauncherFocusHelper.this.onGlobalFocusChanged(oldFocus, newFocus);
                } else {
                    LauncherFocusHelper.this.onGlobalFocusChanged(oldFocus, mFirstFocusView);
                    mFirstFocusView.requestFocus();
                    mFirstFocusView = null;
                }
            }
        }
    }

    /**
     * 只有在子view焦点到达边界，在某方向找不到焦点，才会向上一层寻找，触发此回调
     * 焦点拦截器，返回的view就是新焦点。返回null则继续用基类搜索焦点根据方向获取view。
     * 处理了以下事件
     * 1.当前焦点为search或者nav时 焦点向下移动选中下面fragment第一个item为焦点
     * 2.myFragment中search焦点向左移动切换到nav最后一栏并选中
     * 3.在viewpager上焦点向上移动为当前的导航栏item
     * 4.在导航栏右边边界移动焦点变为搜索栏，页面切换成第一页
     *
     * @param focused   当前焦点
     * @param direction 查找焦点的方向
     * @return 返回的焦点
     */
    public View onFocusSearch(View focused, int direction) {
        switch (direction) {
            case View.FOCUS_UP:
                mDirection = KeyEvent.KEYCODE_DPAD_UP;
                return onFocusUp();
            case View.FOCUS_LEFT:
                mDirection = KeyEvent.KEYCODE_DPAD_LEFT;
                return onFocusLeft();
            case View.FOCUS_DOWN:
                mDirection = KeyEvent.KEYCODE_DPAD_DOWN;
                return onFocusDown();
            case View.FOCUS_RIGHT:
                mDirection = KeyEvent.KEYCODE_DPAD_RIGHT;
                return onFocusRight();
        }
        return null;
    }

    /**
     * 处理viewpager焦点向上的情况
     *
     * @return
     */
    private View onFocusUp() {
        //在老人桌面不处理特殊向上事件
        if (null != mActivity.simpleEpgFragment) {
            return null;
        }

        //在儿童动漫桌面不处理特殊向上事件
        if (null != mActivity.childrenEpgFragment){
            return null;
        }

        //解决焦点再功能键和跑马灯上按上键焦点跑到导航栏上的问题
        if (mActivity.hasFocusLinearContent()){
            return null;
        }
        if (mActivity.hasFocusTvEpgScrollAds()){
            return mActivity.mTvEpgScrollAds;
        }
        if (mActivity.hasFocusEpgTopProfile()){
            return null;
        }

        //如果当前焦点方向向上且在myFragment上面,切换到我的导航栏。
        if (mActivity.mNavRecyclerView.getFocusedChild() == null && mActivity.mRiSearch.findFocus() == null) {
            mFocusEffectWrapper.clearEffectBridge(mCurrentFocus);
            return mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(mActivity.currentPosition);
        } else {
            return null;
        }
    }

    /**
     * 处理顶部搜索栏向左移动,返回焦点为导航栏最右边一栏
     *
     * @return
     */
    private View onFocusLeft() {
        if (mActivity.mRiSearch.findFocus() != null) {
            int count = mActivity.mNavRecyclerView.getAdapter().getItemCount();
            mActivity.mNavRecyclerView.getLayoutManager().scrollToPosition(count - 1);
            int index = count - 1;
            if (index < 0) {
                return mCurrentFocus;
            }
            mActivity.mViewpager.setCurrentItem(index);
            navItemGetFocus(index);
            return mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(count - 1);
        }
        return null;
    }

    /**
     * 处理焦点从顶部栏到fragment的向下事件
     *
     * @return
     */
    private View onFocusDown() {
        //如果之前焦点不在搜索栏和导航栏
        if (mCurrentFocus != mActivity.mIvSearch && mActivity.mNavRecyclerView.findFocus() == null) {
            return null;
        } else {  //让fragment第一个item获取焦点
            if (mCurrentFocus == mActivity.mIvSearch) { //如果当前焦点在导航栏，向下的时候设置我的标签为选中
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setMineSelected();
                    }
                },200);
            }
            //当首页viewpager正在滚动中时向下不响应，防止焦点错乱
            if (mActivity.mViewpager.getScrollState() == ViewPager.SCROLL_STATE_SETTLING) { //如果正在滚动
                return null;
            }
            return getFirstFocusView();
        }
    }

    /**
     * 处理导航栏边界焦点向右事件,也只有在边界会触发此事件。如果触发向右事件的是导航栏，那么把焦点设置为搜索栏
     *
     * @return
     */
    private View onFocusRight() {
        //如果当前焦点在导航栏最右边，焦点切换到搜索栏，并把tab切换到第一页
        if (mActivity.mNavRecyclerView.getFocusedChild() != null) {
            mActivity.mNavRecyclerView.getLayoutManager().scrollToPosition(0);
            mActivity.mViewpager.setCurrentItem(0);
            setNavTitleStatus(false, 0);
            return mActivity.mIvSearch;
        }
        return null;
    }

    /**
     * 获取当前tab页第一个获取焦点的view
     *
     * @return
     */
    private View getFirstFocusView() {
        if (mActivity.mFragmentAdapter.size() < mActivity.currentPosition) {
            return null;
        }
        Fragment fragment = mActivity.mFragmentAdapter.get(mActivity.currentPosition);
        if (fragment instanceof FocusHelperCallBack) {
            return ((FocusHelperCallBack) fragment).getFirstFocusView();
        } else {
            return null;
        }
    }
    private View getFirstChildFocusView() {
        if (null == mActivity.mFragmentAdapter || mActivity.mFragmentAdapter.size() == 0 || mActivity.mFragmentAdapter.size() <= mActivity.currentPosition || mActivity.currentPosition < 0) {
            return null;
        }
        if (mActivity.mFragmentAdapter.get(mActivity.currentPosition) instanceof MyPHMFragment){
            return null;
        }
        PHMFragment fragment = mActivity.mFragmentAdapter.get(mActivity.currentPosition);
        return fragment.getFirstChildView();
    }

    public View getFocusView(){
        return mCurrentFocus;
    }

    public void setFocusView(){
        onGlobalFocusChanged(mPreviousFocus,mCurrentFocus);
    }

    /**
     * 如果处于以下状态，手动设置焦点
     * 1.下页翻上页，上一页的焦点切换到最后一个item，与上一页翻下一页的时候保持一致
     * 2.MyFragment 列表焦点向上移动的时候，焦点返回当前选中的历史或者收藏
     * 3.MyFragment当前选中为收藏时候，焦点移动到列表让列表选中第一个选项
     *
     * @param oldFocus
     * @param newFocus
     */
    private void onGlobalFocusChanged(View oldFocus, View newFocus) {
        //解决 创维电视儿童版 焦点在边界处，按右键焦点跑上问题
        if (DeviceInfo.isSkyworth() && null != mActivity.childrenEpgFragment
                && mActivity.mNavFoucsKeyEvent == KeyEvent.KEYCODE_DPAD_RIGHT
                && null != oldFocus && null != newFocus && oldFocus.getParent() != newFocus.getParent()){
            return;
        }

        if (oldFocus != null) {
            mPreviousFocus = oldFocus;
        }
        if (newFocus != null) {
            mCurrentFocus = newFocus;
        }
        isVideoRequest = false;
        /*isShaked = false;
        ViewShakeUtils.getInstance().endHorizontalAnimator();*/

        if (mActivity.mNavRecyclerView.findFocus() != null || (null != newFocus && newFocus.getId() == R.id.iv_simple_search)) {
            mFocusEffectWrapper.clearEffectBridge(mPreviousFocus);
        }
        if (null != oldFocus && null != newFocus && ((oldFocus.getId() == R.id.rr_item_my_function && newFocus.getId() != R.id.rr_item_my_function)
            || (oldFocus.getId() == R.id.rr_item_my_function && newFocus.getId() == R.id.rr_item_my_function && newFocus.getParent() != oldFocus.getParent()))) {
            if (oldFocus.getParent() instanceof RecyclerView){
                RecyclerView parent = (RecyclerView) oldFocus.getParent();
                parent.getLayoutManager().scrollToPosition(0);
            }
        }

        if (null != oldFocus && oldFocus.getId() == R.id.rr_item_timeline && null != newFocus &&
                newFocus.getId() != R.id.rr_item_timeline) {
            RecyclerView parent = (RecyclerView) oldFocus.getParent().getParent();
            parent.getLayoutManager().scrollToPosition(0);
        }

        if (null != mActivity.childrenEpgFragment && null != oldFocus && oldFocus.getId() != R.id.rl_children_left && oldFocus.getId() != R.id.rl_children_middle
                && oldFocus.getId() != R.id.rl_rightItem_container02_01 && oldFocus.getId() != R.id.rl_rightItem_container03
                && oldFocus.getId() != R.id.rl_children_right01 && oldFocus.getId() != R.id.rl_rightItem_container04
                && oldFocus.getId() != R.id.rl_rightItem_container05 && oldFocus.getId() != R.id.rl_children_right && null != newFocus && (newFocus.getId() == R.id.rl_children_left || newFocus.getId() == R.id.rl_children_middle
                || newFocus.getId() == R.id.rl_children_right01 || newFocus.getId() == R.id.rl_children_right)){
            //儿童版焦点从下往上移，焦点落在第一个模板上时焦点错位问题
            View view = newFocus;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (view.hasFocus()){
                        refreshFocusEffect();
                    }
                }
            }, 600);
        }
        if (null != newFocus && (newFocus.getId() == R.id.rr_item_vertical_scroll || newFocus.getId() == R.id.rr_item_series_vertical_scroll || newFocus.getId() == R.id.rr_item_varietyshow_vertical_scroll)){//首页垂直滚动模板，隐藏统一mainupview,使用自己的焦点框
            mFocusEffectWrapper.setIsHideMainUpView(true);
            refreshFocusEffect();
        }else if (null != newFocus && newFocus.getId() == R.id.rr_item_timeline){//时间轴模板，隐藏统一mainupview,使用自己的焦点框
            mFocusEffectWrapper.setIsHideMainUpView(true);
            refreshFocusEffect();
        }else if (null != newFocus && newFocus.getId() == R.id.rr_item_my_function){//时间轴模板，隐藏统一mainupview,使用自己的焦点框
            mFocusEffectWrapper.setIsHideMainUpView(true);
            refreshFocusEffect();
        }else if (null != newFocus && newFocus.getId() == R.id.reflect_group_item){//自由编排桌面 焦点框处理
            mFocusEffectWrapper.setIsHideMainUpView(true);
            refreshFocusEffect();
        }else if (null != newFocus && (newFocus.getId() == R.id.tv_epg_function_item || newFocus.getId() == R.id.tv_main_refresh_btn || newFocus.getId() == R.id.tv_epg_scroll_ads)){//自由编排桌面 焦点框处理
            mFocusEffectWrapper.setIsHideMainUpView(true);
            refreshFocusEffect();
        }else if (null != newFocus && (newFocus.getId() == R.id.tv_change_user_btn || newFocus.getId() == R.id.tv_user_title_bg)){//切换账号按钮
            mFocusEffectWrapper.setIsHideMainUpView(true);
            refreshFocusEffect();
        }else if (null != newFocus && (newFocus.getId() == R.id.rl_item_profile_bookmark_list)){//多Profile界面观看记录落焦使用自己的焦点框
            mFocusEffectWrapper.setIsHideMainUpView(true);
            refreshFocusEffect();
        }else if (null != newFocus && (newFocus.getId() == R.id.iv_logo1)){//多Profile界面观看记录落焦使用自己的焦点框
            mFocusEffectWrapper.setIsHideMainUpView(true);
            refreshFocusEffect();
        }else {
            //刷新新老焦点效果
            if (newFocus != null) {
                mFocusEffectWrapper.setIsHideMainUpView(true);
                refreshFocusEffect();
            }
        }

        //停止光效效果
        stopShimmer(oldFocus,newFocus);

        //开始光效效果
        startShimmer(newFocus);
    }

    /**
     * 刷新焦点上的显示效果
     */
    private void refreshFocusEffect() {
        mFocusEffectWrapper.setIsHideMainUpView(true);
        if (mCurrentFocus == null) {
            return;
        }
        //Nav和搜索不显示边框
        if (mActivity.mNavRecyclerView.findFocus() != null || mActivity.mRiSearch.findFocus() != null || mCurrentFocus.getId() == R.id.iv_simple_search
                || mCurrentFocus.getId() == R.id.tv_nav_parent) {
            //2019.07.05解决页面第一个模板为横向滚动模板，焦点在Tab和模板之间切换焦点异常
            if (mActivity.mNavRecyclerView.findFocus() == null && mCurrentFocus.getId() == R.id.tv_nav_parent
                    && null != mPreviousFocus && mPreviousFocus.getId() == R.id.rr_item_my_function && null != mPreviousFocus.getParent()
                    && mPreviousFocus.getParent() instanceof RecyclerViewTV && ((RecyclerViewTV)mPreviousFocus.getParent()).getId() != R.id.rv_my_fragment_function){//排除不是我的下面常用功能模块
                navItemGetFocus(mActivity.currentPosition);
            }else if (mActivity.mNavRecyclerView.findFocus() == null && mCurrentFocus.getId() == R.id.tv_nav_parent
                    && null != mPreviousFocus && mActivity.mNavFoucsKeyEvent == KeyEvent.KEYCODE_DPAD_UP &&
                    (mPreviousFocus.getId() == R.id.rr_item_vertical_scroll || mPreviousFocus.getId() == R.id.rr_item_series_vertical_scroll || mPreviousFocus.getId() == R.id.rr_item_varietyshow_vertical_scroll)){
                //解决垂直滚动模板为顶部的一个模板时，按上键，焦点落不到tab导航栏上
                navItemGetFocus(mActivity.currentPosition);
            }else if (mActivity.mNavRecyclerView.findFocus() == null && mCurrentFocus.getId() == R.id.tv_nav_parent
                    && null != mPreviousFocus && mPreviousFocus.getId() == R.id.rr_item_timeline){
                //解决时间轴为顶部的一个模板时，按上键，焦点落不到tab导航栏上
                navItemGetFocus(mActivity.currentPosition);
            }
            return;
        }

        drawFocusEffect();
    }


    public void drawFocusEffect() {
        mFocusEffectWrapper.drawFocusEffect(mPreviousFocus, mCurrentFocus);
    }

    /**
     * 清除当前焦点效果
     */
    public void clearFocusEffect() {
        if (mCurrentFocus != null) {
            mFocusEffectWrapper.clearEffectBridge(mCurrentFocus);
        }
    }

    /**
     * 拦截焦点事件
     *
     * @param event
     * @param view
     * @return
     */
    @Override
    public boolean interceptFocus(KeyEvent event, View view) {
        mDirection = event.getKeyCode();
        switch (view.getId()) {
            //拦截首页viewpager中符合条件的按键事件
            case R.id.vp_viewpager:
                isiIntercept = interceptPageTurnFocus(event, view);
                return isiIntercept;
            //拦截我的页面符合条件的按键事件
            case R.id.rl_container:
                isiIntercept = interceptMyFocus(mDirection, view);
                return isiIntercept;
            //拦截动态桌面符合条件的按键事件
            case R.id.fl_container:
//                isiIntercept = interceptPHMFocus(event, view);
                return isiIntercept;
            case R.id.rl_main_lay:
                isiIntercept = interceptMainView(event, view);
                return isiIntercept;
        }
        return false;
    }

    private boolean interceptMainView(KeyEvent event, View view) {
        //------焦点在Tab上切换时，上报UBD参数
        int keycode = event.getKeyCode();
        mActivity.mNavFoucsKeyEvent = keycode;

        //在普通版，焦点从功能键和跑马灯上按下键落焦到导航栏的处理
        if (isMain() && (mActivity.hasFocusLinearContent() || mActivity.hasFocusTvEpgScrollAds() || mActivity.hasFocusEpgTopProfile()) && keycode == KeyEvent.KEYCODE_DPAD_DOWN){
            if (mActivity.currentPosition == -1){
                mActivity.currentPosition = 0;
            }
            navItemGetFocus(mActivity.currentPosition);
            return true;
        }

        if (keycode == KeyEvent.KEYCODE_DPAD_LEFT) {
            //焦点在首页刷新按钮上，拦截向左焦点移动事件
            if (mActivity.hasFocusTvEpgScrollAds()){
                return true;
            }
        }
        if (keycode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            //轮播：焦点再视频播放窗口上，按右键，落到播放的条目上
            if (null != mCurrentFocus && mCurrentFocus.getId() == R.id.pan_l1_r4_left){
                if (mCurrentFocus.getParent() instanceof VideoLiveTemplate){
                    VideoLiveTemplate parent = (VideoLiveTemplate) mCurrentFocus.getParent();
                    if (parent.getPlayPostion() == 0){
                        mCurrentFocus = parent.findViewById(R.id.rl_pan_l1_r4_item0);
                    }else if (parent.getPlayPostion() == 1){
                        mCurrentFocus = parent.findViewById(R.id.rl_pan_l1_r4_item1);
                    }else if (parent.getPlayPostion() == 2){
                        mCurrentFocus = parent.findViewById(R.id.rl_pan_l1_r4_item2);
                    }else if (parent.getPlayPostion() == 3){
                        mCurrentFocus = parent.findViewById(R.id.rl_pan_l1_r4_item3);
                    }
                    mCurrentFocus.requestFocus();
                    return true;
                }
            }

        }

        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP && mActivity.mNavRecyclerView.findFocus() != null
                && null != mActivity.mManualRefreshBtn && mActivity.mManualRefreshBtn.getVisibility() == View.VISIBLE) {
            return false;
        }else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP && mActivity.mNavRecyclerView.findFocus() != null
                && null != mActivity.mManualRefreshBtn &&  mActivity.mManualRefreshBtn.getVisibility() == View.GONE){
            return true;
        }
        if ((event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) && mActivity.mNavRecyclerView.findFocus() != null) {
            if (!canClick()) {
                return true;
            }
        }
        //处理模板加载失败模板没view的问题
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN && (null != mActivity.mNavRecyclerView.findFocus() || null != mActivity.mRiSearch.findFocus())) {
            try {
                PHMFragment phmFragment = (PHMFragment) mActivity.mFragmentAdapter.get(mActivity.currentPosition);
                if (phmFragment.isNoTemplate() || mActivity.mViewpager.getScrollState() == ViewPager.SCROLL_STATE_SETTLING) {
                    return true;
                }
            } catch (Exception e) {
                SuperLog.error(TAG,e);
                return true;
            }
        }
        return false;
    }

    private boolean isMineLastItem() {
        RecyclerView recyclerView = null;
        if (mCurrentFocus.getId() == R.id.rr_item_my_function && mCurrentFocus.getParent() instanceof RecyclerView) {
            recyclerView = (RecyclerView) mCurrentFocus.getParent();
            if (recyclerView.getLayoutManager().getItemCount() - 1 == recyclerView.getLayoutManager().getPosition(mCurrentFocus)) {
                return true;
            }
        }
        if (mCurrentFocus.getId() == R.id.rl_item_my_list) {
            recyclerView = (RecyclerView) mCurrentFocus.getParent().getParent();
            if (recyclerView.getLayoutManager().getItemCount() - 1 == recyclerView.getLayoutManager().getPosition((View) mCurrentFocus.getParent()) && recyclerView.getLayoutManager().getItemCount() < 5) {
                return true;
            }
        }
        if (mCurrentFocus.getId() == R.id.rl_item_profile_bookmark_list) {
            recyclerView = (RecyclerView) mCurrentFocus.getParent().getParent();
            if (recyclerView.getLayoutManager().getItemCount() < 5 && recyclerView.getLayoutManager().getItemCount() - 1 == recyclerView.getLayoutManager().getPosition((View) mCurrentFocus.getParent())) {
                return true;
            }
        }
        return false;
    }

    private boolean interceptPageTurnFocus(KeyEvent event, View v) {
        int keycode = event.getKeyCode();
        //判断翻页状态，如果是向左翻页
        if (keycode == KeyEvent.KEYCODE_DPAD_LEFT && isBorder((ViewGroup) v, mCurrentFocus, View.FOCUS_LEFT)) {

            /*if (!isShaked){
                isShaked = true;
                ViewShakeUtils.getInstance().setHorizontalShakeAnimator(getFocusView());
                ViewShakeUtils.getInstance().startVerticalShakeAnimator();
                return false;
            }else{
                //ViewShakeUtils.getInstance().endHorizontalAnimator();
                isShaked = false;
            }*/

            mActivity.resetMarginForTopView(-1);
            resetMarginForTopView();
            if (mActivity.currentPosition != 0) {
                int position = mActivity.currentPosition - 1;
                mActivity.mNavRecyclerView.setCurentKeyCode(KeyEvent.KEYCODE_DPAD_LEFT);
                setNavTitleStatus(false, mActivity.currentPosition);
                setNavImgStatus(false,mActivity.currentPosition);
                mActivity.mViewpager.setCurrentItem(position);
                navItemGetFocus(position);
                return true;
            } else {
                //第一页内容向左焦点返回到搜索
                clearFocusEffect();
                mActivity.mFragmentAdapter.get(0).scrollToTop();
                setFocusChangeEffect(false, mActivity.mNavRecyclerView.getChildAt(0).findViewById(R.id.tv_nav_title),0);

                mActivity.mIvSearch.requestFocus();
                return true;
            }
        }
        //判断翻页状态，如果是向右翻页
        if (keycode == KeyEvent.KEYCODE_DPAD_RIGHT && (isBorder((ViewGroup) v, mCurrentFocus, View.FOCUS_RIGHT) || isMineLastItem())) {
            mActivity.resetMarginForTopView(-1);
            resetMarginForTopView();
            if (mActivity.currentPosition < mActivity.mFragmentAdapter.size() - 1) {
                int position = mActivity.currentPosition + 1;
                mActivity.mNavRecyclerView.setCurentKeyCode(KeyEvent.KEYCODE_DPAD_RIGHT);
                setNavTitleStatus(false, mActivity.currentPosition);
                setNavImgStatus(false,mActivity.currentPosition);
                mActivity.mViewpager.setCurrentItem(position);
                navItemGetFocus(position);
                return true;
            } else if (mActivity.currentPosition == mActivity.mFragmentAdapter.size() - 1) {
                mFocusEffectWrapper.clearEffectBridge(mCurrentFocus);
                mActivity.mIvSearch.requestFocus();
                setNavTitleStatus(false, mActivity.currentPosition);
                mActivity.mViewpager.setCurrentItem(0);
                mActivity.searchIvHasFocus = true;//再最右侧tab下页面中的资源位按右键，然后再按下键，导航栏不展示问题
                mActivity.mNavRecyclerView.getLayoutManager().scrollToPosition(0);
                return true;
            }
        }
        return false;
    }


    /**
     * 判断是否是边界
     *
     * @param root
     * @param focused
     * @param direction
     * @return
     */
    public boolean isBorder(ViewGroup root, View focused, int direction) {

        //热播榜左右键 没有切换tab导航栏问题
        if (null != focused && focused.getParent() instanceof VerticalGridView && ((((VerticalGridView) focused.getParent()).getId() == R.id.rv_hot_rank01 && direction == View.FOCUS_LEFT)
        ||(((VerticalGridView) focused.getParent()).getId() == R.id.rv_hot_rank03 && direction == View.FOCUS_RIGHT))){
            return true;
        }

        View view = FocusFinder.getInstance().findNextFocus(root, focused,
                direction);
        if (direction == View.FOCUS_RIGHT && focused.getId() == R.id.rl_item_container_last){
            return true;
        }
        if (view == null || (view.getId() == R.id.rr_item_my_function && null != focused && focused.getId() != R.id.rr_item_my_function
                && focused.getId() != R.id.rl_userinfo_container) || view.getId() == R.id.tv_nav_parent) {
            return true;
        }else if (view.getId() == R.id.rr_item_timeline && null != focused && focused.getId() == R.id.rr_item_timeline){
            return false;
        }else if (null != focused && focused.getId() != R.id.rl_item_profile_bookmark_list && focused.getId() != R.id.rr_item_my_function
                && focused.getId() != R.id.rl_userinfo_container && focused.getId() != R.id.rl_item_my_list && focused.getId() != R.id.rt_moreItem){
            return FocusFinder.getInstance().findNextFocus(root, focused, direction).getParent() != focused.getParent();
        }else{
            return false;
        }
        /*return FocusFinder.getInstance().findNextFocus(root, focused,
                direction) == null;*/
    }


    /**
     * 拦截我的页面焦点事件
     *
     * @param keycode
     * @return
     */
    private boolean interceptMyFocus(int keycode, View rootView) {
        return false;
    }


    /**
     * 导航栏焦点切换时候的效果
     */
    public void onPageChange() {
        if (mActivity.mIvSearch.isFocused()) {
            return;
        }

        if (null != mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(mActivity.currentPosition) && null != mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(mActivity.currentPosition).findViewById(R.id.tv_nav_title)) {
            setFocusChangeEffect(true, (TextView) mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(mActivity.currentPosition).findViewById(R.id.tv_nav_title),mActivity.currentPosition);
        }
    }

    private boolean hasNavFocus(){
        return mActivity.mNavRecyclerView.hasFocus();
    }

    public void resetMarginForTopView(){
        if (null != mActivity.getTopView()){
            RelativeLayout topView = mActivity.getTopView();
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) topView.getLayoutParams();
            layoutParams.setMargins(0,0,0,0);
            topView.setLayoutParams(layoutParams);
        }
    }

    /**
     * 在fragment的时候返回键返回上一导航栏
     */
    public void onKeyBack() {
        resetMarginForTopView();
        //如果当前焦点不再导航栏上，返回到对应的导航栏，否则落焦到第一个获取焦点的导航上
        if (mActivity.mNavRecyclerView.findFocus() == null) {
            if (mCurrentFocus != null) {
                mFocusEffectWrapper.clearEffectBridge(mCurrentFocus);
            }
            View view = mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(mActivity.currentPosition);
            if (null != view)
                view.requestFocus();
            //滚动到顶部
            if (null != mActivity.mFragmentAdapter && mActivity.mFragmentAdapter.size()>0) {
                ((FocusHelperCallBack) mActivity.mFragmentAdapter.get(mActivity.currentPosition)).scrollToTop();
            }
        } else {
            navItemGetFocus(LauncherService.getInstance().getFirstIndex());
        }
    }

    private boolean canClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if (curClickTime - lastClickTime >= MIN_CLICK_DELAY) {
            flag = true;
            lastClickTime = curClickTime;
        }
        return flag;
    }

    /**
     * 顶部导航栏获取焦点
     *
     * @param position
     */
    public void navItemGetFocus(int position) {
        //主profile切子profile偶现崩溃-Invalid target position
        if (position < 0){
            return;
        }

        resetMarginForTopView();
        View navItem = mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(position);
        mFocusEffectWrapper.clearEffectBridge(mPreviousFocus);
        mActivity.scrollToItemForNavRecycleView(position);
        if (null != navItem) {
            navItem.requestFocus();
        } else {
            //mActivity.mNavRecyclerView.scrollToPosition(position);
             if (DeviceInfo.isM301H()){//解决M301H盒子按返回键回到默认落焦的导航栏。导航栏没有落焦效果问题（2421）
                 mActivity.mNavRecyclerView.postDelayed(() -> navItemGetFocus(position), 800);
             }else{
                 mActivity.mNavRecyclerView.postDelayed(() -> navItemGetFocus(position), 300);
             }
        }
    }

    /**
     * @param focus    是否是选中状态
     * @param position 在导航栏中的位置
     */
    private void setNavTitleStatus(boolean focus, int position) {
        if (null != mActivity && null != mActivity.mNavRecyclerView && null != mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(position)) {
            TextView tv = (TextView) mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.tv_nav_title);
            if (null != tv) {
                setFocusChangeEffect(focus, tv,position);
            }
        }
    }

    //焦点由搜索键向下，使"我的"选中 done by weicy 2020 0804
    private void setMineSelected(){
        TextView tv = null;
        try {
            tv = (TextView) mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(0).findViewById(R.id.tv_nav_title);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (null != tv){
            tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tv.setTextColor(Utils.getNavTitleColorNormal(0));
            tv.setSelected(false);//tab title 背景色设置
        }
    }

    //解决：焦点不在导航栏上时，在内容边缘按左键切换导航栏，导航栏背景图片为变成落焦前的图片
    private void setNavImgStatus(boolean focus, int position){
        if (null != mActivity && null != mActivity.mNavRecyclerView && null != mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(position)) {
            ImageView imView = (ImageView) mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.im_nav_title);
            TextView tv = (TextView) mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.tv_nav_title);
            RelativeLayout rlNav = (RelativeLayout) mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.rl_nav);
            if (null != imView) {
                loadNavImgBg(position,imView,tv,focus ? 1 : 0,rlNav);
            }
        }
    };

    //失去焦点，关闭海报光效
    private void stopShimmer(View oldFocusView,View newFocusView){
        if(oldFocusView instanceof ReflectRelativeLayout){
            ((ReflectRelativeLayout)oldFocusView).stopShimmer();
        }else if (oldFocusView instanceof RelativeLayout && null != ((RelativeLayout) oldFocusView).getChildAt(0)
                && ((RelativeLayout) oldFocusView).getChildAt(0) instanceof ReflectRelativeLayout){
            //解决首页点播自动播放-海报无光效
            ((ReflectRelativeLayout) ((RelativeLayout) oldFocusView).getChildAt(0)).stopShimmer();
        }else if (oldFocusView instanceof RelativeLayoutExt && null != ((RelativeLayoutExt) oldFocusView).getChildAt(0)
                && ((RelativeLayout) oldFocusView).getChildAt(0) instanceof ShimmerImageView){
            //解决播放历史和我的收藏无光效问题
            if (((ShimmerImageView) ((RelativeLayout) oldFocusView).getChildAt(0)).isCanStartShimmer()){
                ((ShimmerImageView) ((RelativeLayout) oldFocusView).getChildAt(0)).stopShimmer();
            }
        }else if (oldFocusView instanceof RelativeLayout && null != ((RelativeLayout) oldFocusView).getChildAt(0)
                && ((RelativeLayout) oldFocusView).getChildAt(0) instanceof AutoScrollViewPager){
            //解决滚动Banner海报无光效问题
            if (null != ((RelativeLayout) oldFocusView).getChildAt(0))
            EventBus.getDefault().post(new AutoScrollViewEvent(false));
        }

        /*if (oldFocusView instanceof RelativeLayout && null != ((RelativeLayout) oldFocusView).getChildAt(1)
                && ((RelativeLayout) oldFocusView).getChildAt(1) instanceof PlayViewWindow  && oldFocusView.getParent() instanceof MixVideoTemplate){
            //首页视频自动播放失去焦点停止播放
            //EventBus.getDefault().post(new MixAutoPlayTimeEvent(((RelativeLayout) oldFocusView).getChildAt(1).getId(),true));
            ((MixVideoTemplate)oldFocusView.getParent()).hasFocusToPlay(((RelativeLayout) oldFocusView).getChildAt(1).getId(),true);
        }else */if (oldFocusView instanceof RelativeLayout && (oldFocusView.getId() == R.id.pan_l1_r4_left || oldFocusView.getId() == R.id.rl_pan_l1_r4_item0
                || oldFocusView.getId() == R.id.rl_pan_l1_r4_item1 || oldFocusView.getId() == R.id.rl_pan_l1_r4_item2 || oldFocusView.getId() == R.id.rl_pan_l1_r4_item3)
                && (null == newFocusView || (null != newFocusView && newFocusView.getId() != R.id.rl_pan_l1_r4_item0 && newFocusView.getId() != R.id.rl_pan_l1_r4_item1
                && newFocusView.getId() != R.id.rl_pan_l1_r4_item2 && newFocusView.getId() != R.id.rl_pan_l1_r4_item3 && newFocusView.getId() != R.id.pan_l1_r4_left))){
            //判断条件：老焦点是属于视频轮播资源位，而新焦点不属于视频轮播，发送广播 停止播放
            //首页轮播视频窗口失去焦点停止播放
            //EventBus.getDefault().post(new VideoLivePlayTimeEvent(-1));
            ((VideoLiveTemplate)oldFocusView.getParent()).hasFocusToPlay(oldFocusView.getId(),false,false);
        }
    }

    //获得焦点 开始海报光效
    private void startShimmer(View newFocusView){
        if(newFocusView instanceof ReflectRelativeLayout){
            ((ReflectRelativeLayout)newFocusView).startShimmer();
        }else if (newFocusView instanceof RelativeLayout && null != ((RelativeLayout) newFocusView).getChildAt(0)
                && ((RelativeLayout) newFocusView).getChildAt(0) instanceof ReflectRelativeLayout && MessageDataHolder.get().getIsShimmerMix()){
            //解决首页点播自动播放-海报无光效
            ((ReflectRelativeLayout) ((RelativeLayout) newFocusView).getChildAt(0)).startShimmer();
        }else if (newFocusView instanceof RelativeLayoutExt && null != ((RelativeLayoutExt) newFocusView).getChildAt(0)
                && ((RelativeLayout) newFocusView).getChildAt(0) instanceof ShimmerImageView){
            if (newFocusView.getParent().getParent().getParent().getParent() instanceof MyListTemplate &&
                    ((MyListTemplate)newFocusView.getParent().getParent().getParent().getParent()).getTitleContent().equalsIgnoreCase(mActivity.getString(R.string.launcher_my_play_history))
                    && MessageDataHolder.get().getIsShimmerHistory()){
                //解决播放历史和我的收藏无光效问题
                if (((ShimmerImageView) ((RelativeLayout) newFocusView).getChildAt(0)).isCanStartShimmer()){
                    ((ShimmerImageView) ((RelativeLayout) newFocusView).getChildAt(0)).startShimmer();
                }
            }
            if (newFocusView.getParent().getParent().getParent().getParent() instanceof MyListTemplate &&
                    ((MyListTemplate)newFocusView.getParent().getParent().getParent().getParent()).getTitleContent().equalsIgnoreCase(mActivity.getString(R.string.launcher_my_collect))
                    && MessageDataHolder.get().getIsShimmerCollect()){
                if (((ShimmerImageView) ((RelativeLayout) newFocusView).getChildAt(0)).isCanStartShimmer()){
                    ((ShimmerImageView) ((RelativeLayout) newFocusView).getChildAt(0)).startShimmer();
                }
            }
        }else if (newFocusView instanceof RelativeLayout && null != ((RelativeLayout) newFocusView).getChildAt(0)
                && ((RelativeLayout) newFocusView).getChildAt(0) instanceof AutoScrollViewPager){
            //解决滚动Banner海报无光效问题
            if (null != ((RelativeLayout) newFocusView).getChildAt(0))
                EventBus.getDefault().post(new AutoScrollViewEvent(true));
        }

        /*if (newFocusView instanceof RelativeLayout && null != ((RelativeLayout) newFocusView).getChildAt(1)
                && ((RelativeLayout) newFocusView).getChildAt(1) instanceof PlayViewWindow && newFocusView.getParent() instanceof MixVideoTemplate){
            //首页视频窗口落焦自动播放
            //EventBus.getDefault().post(new MixAutoPlayTimeEvent(((RelativeLayout) newFocusView).getChildAt(1).getId(),false));
            ((MixVideoTemplate)newFocusView.getParent()).hasFocusToPlay(((RelativeLayout) newFocusView).getChildAt(1).getId(),false);
        }else */if (newFocusView instanceof RelativeLayout && (newFocusView.getId() == R.id.rl_pan_l1_r4_item0
            || newFocusView.getId() == R.id.rl_pan_l1_r4_item1 || newFocusView.getId() == R.id.rl_pan_l1_r4_item2 || newFocusView.getId() == R.id.rl_pan_l1_r4_item3
                || newFocusView.getId() == R.id.pan_l1_r4_left)){
            //首页轮播视频窗口落焦自动播放
            if (newFocusView.getId() == R.id.pan_l1_r4_left){
                ((VideoLiveTemplate)newFocusView.getParent()).hasFocusToPlay(newFocusView.getId(),false,true);
            }else{
                ((VideoLiveTemplate)newFocusView.getParent()).hasFocusToPlay(newFocusView.getId(),true,true);
            }
        }
    }

    /**
     * 加载导航栏tab背景
     * focusType = 0:无焦点；1:落焦；2：次落焦
     * */
    @SuppressLint("CheckResult")
    public void loadNavImgBg(int position,ImageView img,TextView titleTv, int focusType,RelativeLayout rlNav) {
        mNavigates = mActivity.getmNavigates();
        if (null != mNavigates && mNavigates.size() > 0 && null != mNavigates.get(position)
                && !TextUtils.isEmpty(mNavigates.get(position).getImage())
                && !TextUtils.isEmpty(mNavigates.get(position).getFocusImage())
                && !TextUtils.isEmpty(mNavigates.get(position).getSecondaryTitleImg())) {
            SuperLog.info2SD(TAG, "TabImageUrl=" + GlideUtil.getUrl(mNavigates.get(position).getImage())
                    + ";TabFocusImage=" + GlideUtil.getUrl(mNavigates.get(position).getFocusImage()
                    + ";TabSecondaryTitleImg=" + GlideUtil.getUrl(mNavigates.get(position).getSecondaryTitleImg() + ";focusType=" + focusType)));

            ImageViewExt imageViewExt = new ImageViewExt(mActivity);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = mActivity.getResources().getDimensionPixelOffset(R.dimen.launcher_nav_item_height);
            imageViewExt.setLayoutParams(params);
            imageViewExt.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageViewExt.setId(R.id.main_nav_bg_img);
            rlNav.addView(imageViewExt, 0);

            //设置有无焦点时默认图片
            if (focusType == 1 && !mActivity.mIvSearch.hasFocus()) {
                Glide.with(mActivity)
                        .load(mActivity.getImageUrl() + GlideUtil.getUrl(mNavigates.get(position).getFocusImage()))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (null != titleTv) {
                                    titleTv.setVisibility(View.VISIBLE);
                                }
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (null != titleTv) {
                                    titleTv.setVisibility(View.INVISIBLE);
                                }
                                GlideUtil.removeIvOfRl(rlNav);

                                //解决最后一个导航栏配置图片放大显示不全的问题---2020.05.21
                                if (position == mNavigates.size()-1){
                                    new Handler().postDelayed(new Runnable(){
                                        public void run() {
                                            mActivity.mNavRecyclerView.smoothScrollBy(resource.getIntrinsicWidth()/2,0);
                                        }
                                    }, 100);
                                }//---end
                                return false;
                            }
                        }).into(imageViewExt);
            } else if (focusType == 0){
                //无焦点
                Glide.with(mActivity)
                        .load(mActivity.getImageUrl() + GlideUtil.getUrl(mNavigates.get(position).getImage()))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (null != titleTv) {
                                    titleTv.setVisibility(View.VISIBLE);
                                }
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (null != titleTv) {
                                    titleTv.setVisibility(View.INVISIBLE);
                                }
                                GlideUtil.removeIvOfRl(rlNav);
                                return false;
                            }
                        }).into(imageViewExt);
            } else if (focusType == 2){
                //次落焦
                Glide.with(mActivity)
                        .load(mActivity.getImageUrl() + GlideUtil.getUrl(mNavigates.get(position).getSecondaryTitleImg()))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (null != titleTv) {
                                    titleTv.setVisibility(View.VISIBLE);
                                }
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (null != titleTv) {
                                    titleTv.setVisibility(View.INVISIBLE);
                                }
                                GlideUtil.removeIvOfRl(rlNav);
                                return false;
                            }
                        }).into(imageViewExt);
            }
        }
    }

    private boolean isMain(){
        return null == mActivity.childrenEpgFragment && null == mActivity.simpleEpgFragment;
    }
}