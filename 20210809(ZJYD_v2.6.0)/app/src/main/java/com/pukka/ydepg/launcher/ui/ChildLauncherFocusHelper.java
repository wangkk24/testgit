package com.pukka.ydepg.launcher.ui;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.customui.tv.bridge.RecyclerViewBridge;
import com.pukka.ydepg.customui.tv.widget.RecyclerViewTV;
import com.pukka.ydepg.launcher.ui.fragment.FocusHelperCallBack;
import com.pukka.ydepg.launcher.ui.fragment.FocusInterceptor;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.util.FocusEffectWrapper;




public class ChildLauncherFocusHelper implements FocusInterceptor {
    private FocusEffectWrapper mFocusEffectWrapper;  //边框效果控件
    private ChildLauncherActivity mActivity;
    private View mCurrentFocus;   //当前焦点
    private View mPreviousFocus; //上一个焦点
    private final String TAG = "ChildLauncherFocusHelper";
    private boolean isiIntercept;
    private int mDirection;
    private static final int MIN_CLICK_DELAY = 300;
    private long lastClickTime;

    //每次进入二级页面，第一次焦点落焦，焦点延时落焦
    private boolean isFirstLoginChild = true;

    public ChildLauncherFocusHelper(ChildLauncherActivity activity) {
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
    public void setFocusChangeEffect(boolean focus, TextView textView) {
        if (focus) {
            textView.setTextColor(ContextCompat.getColor(mActivity, R.color.c21_color));
        } else {
            textView.setTextColor(ContextCompat.getColor(mActivity, R.color.c24_color));
        }
    }


    /**
     * 注册焦点监听事件
     */
    private void initListener() {
        //注册焦点全局变化监听
        mActivity.getWindow().getDecorView().getViewTreeObserver().addOnGlobalFocusChangeListener((oldFocus, newFocus) -> {
            onGlobalFocusChanged(oldFocus, newFocus);
        });
        //焦点事件触发时，拦截焦点搜索，只对焦点到达边界起作用
        mActivity.mContainerLayout.setOnFocusSearchListener((focused, direction) -> {
            return onFocusSearch(focused, direction);
        });

        //导航栏获取或失去焦点切换效果
        mActivity.mNavRecyclerView.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                TextView titleTv = (TextView) itemView.findViewById(R.id.tv_nav_title);
                if (mDirection != KeyEvent.KEYCODE_DPAD_DOWN) {
                    setFocusChangeEffect(false, titleTv);
                }
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                if (mActivity.mIvSearch.hasFocus()) {
                    return;
                }
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View item = parent.getChildAt(i);
                    TextView titleTv = (TextView) item.findViewById(R.id.tv_nav_title);
                    setFocusChangeEffect(false, titleTv);
                }
                TextView titleTv = (TextView) itemView.findViewById(R.id.tv_nav_title);
                setFocusChangeEffect(true, titleTv);
                try {
                    mActivity.mViewpager.setCurrentItem(position);
                    if (mActivity.mFragmentAdapter.size() > position && null != mActivity.mFragmentAdapter.get(position)) {
                        PHMFragment fragment = (PHMFragment) mActivity.mFragmentAdapter.get(position);
                        fragment.scrollToTop();
                    }

                } catch (Exception e) {
                    SuperLog.error(TAG,e);
                }
            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {

            }
        });
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
        //如果当前焦点方向向上且在myFragment上面,切换到我的导航栏。
        if (mActivity.mNavRecyclerView.getFocusedChild() == null && mActivity.mRiSearch.findFocus() == null) {
            if (isNavEmpty(mActivity.currentPosition)) {
                return mCurrentFocus;
            }
            mFocusEffectWrapper.clearEffectBridge(mCurrentFocus);
            return mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(mActivity.currentPosition);
        } else {
            return mCurrentFocus;
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
            if (isNavEmpty(count - 1)) {
                ((FocusHelperCallBack) mActivity.mFragmentAdapter.get(mActivity.currentPosition)).scrollToTop();
                return mActivity.mIvSearch;
            }
            mActivity.mNavRecyclerView.getLayoutManager().scrollToPosition(count - 1);
            int index = count - 1;
            if (index < 0) {
                return mCurrentFocus;
            }
            mActivity.mViewpager.setCurrentItem(index);
            //ScrollToPosition并不是立刻生效，延迟300ms让最后一个导航栏目获取焦点
            mActivity.mNavRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mActivity.mRiSearch.findFocus() != null && null != mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(count - 1)) {
                        mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(count - 1).requestFocus();
                    }
                }
            }, 300);
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
            if (mCurrentFocus == mActivity.mIvSearch && null != mActivity.mNavRecyclerView && null != mActivity.mNavRecyclerView.getChildAt(0)) { //如果当前焦点在导航栏，向下的时候设置我的标签为选中
                ((TextView) mActivity.mNavRecyclerView.getChildAt(0).findViewById(R.id.tv_nav_title)).setTextColor(ContextCompat.getColor(mActivity, R.color.c21_color));
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
        //如果焦点在搜索，且导航栏第一个栏目为空，则向右焦点不动
        if (mActivity.mRiSearch.findFocus() != null && isNavEmpty(0)) {
            ((FocusHelperCallBack) mActivity.mFragmentAdapter.get(mActivity.currentPosition)).scrollToTop();
            return mActivity.mIvSearch;
        }
        //如果当前焦点在导航栏最右边，焦点切换到搜索栏，并把tab切换到第一页
        if (mActivity.mNavRecyclerView.getFocusedChild() != null) {
            mActivity.mNavRecyclerView.getLayoutManager().scrollToPosition(0);
            mActivity.mViewpager.setCurrentItem(0);
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
        if (null != mActivity.mFragmentAdapter && mActivity.mFragmentAdapter.size() < mActivity.currentPosition) {
            return null;
        }
        Fragment fragment = mActivity.mFragmentAdapter.get(mActivity.currentPosition);
        if (fragment != null && fragment instanceof FocusHelperCallBack) {
            return ((FocusHelperCallBack) fragment).getFirstFocusView();
        } else {
            return null;
        }
    }

    public void setIsFirs(){
        this.isFirstLoginChild = false;
    }

    public View getFocusView(){
        return mCurrentFocus;
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

        if (oldFocus != null) {
            mPreviousFocus = oldFocus;
        }
        if (newFocus != null) {
            mCurrentFocus = newFocus;
        }

        if (isFirstLoginChild && null != newFocus && newFocus.getId() == R.id.rr_item_my_function){
            //newFocus.clearFocus();
            PHMFragment fragment = (PHMFragment) mActivity.mFragmentAdapter.get(0);
            fragment.scrollToTop();
            if (oldFocus != null) {
                mCurrentFocus = oldFocus;
                oldFocus.requestFocus();
            }
            mPreviousFocus = newFocus;
        }

        if (mActivity.mNavRecyclerView.findFocus() != null) {
            mFocusEffectWrapper.clearEffectBridge(mPreviousFocus);
        }
        //解决模板pannel_poster_horizontal_16配置两个相邻，焦点错位问题
        if (null != oldFocus && null != newFocus && newFocus.getId() == R.id.rr_item_my_function &&
                oldFocus.getId() == R.id.rr_item_my_function) {
            mFocusEffectWrapper.setIsHideMainUpView(true);
            refreshFocusEffect();
        }else if (null != newFocus && newFocus.getId() == R.id.rr_item_my_function){//时间轴模板，隐藏统一mainupview,使用自己的焦点框
            mFocusEffectWrapper.setIsHideMainUpView(true);
            refreshFocusEffect();
        }else if (null != newFocus && newFocus.getId() == R.id.reflect_group_item){//自由编排桌面 焦点框处理
            mFocusEffectWrapper.setIsHideMainUpView(true);
            refreshFocusEffect();
        }else if (null != newFocus && (newFocus.getId() == R.id.rr_item_vertical_scroll || newFocus.getId() == R.id.rr_item_series_vertical_scroll || newFocus.getId() == R.id.rr_item_varietyshow_vertical_scroll)){//首页垂直滚动模板，隐藏统一mainupview,使用自己的焦点框
            mFocusEffectWrapper.setIsHideMainUpView(true);
            refreshFocusEffect();
        }else if (null != newFocus && newFocus.getId() == R.id.rr_item_timeline){//时间轴模板，隐藏统一mainupview,使用自己的焦点框
            mFocusEffectWrapper.setIsHideMainUpView(true);
            refreshFocusEffect();
        }else if (null != newFocus && newFocus.getId() == R.id.vp_viewpager){//二级页面刷新时隐藏焦点框
            mFocusEffectWrapper.setIsHideMainUpView(true);
            refreshFocusEffect();
        }else if (newFocus != null) {//刷新新老焦点效果
            if (newFocus.getId() == R.id.tv_playHistory || newFocus.getId() == R.id.tv_collect) {
                clearFocusEffect();
            } else if (isFirstLoginChild){
                if (handler.hasMessages(1)){
                    handler.removeMessages(1);
                }
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessageDelayed(msg,400);
            }else {
                refreshFocusEffect();
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                refreshFocusEffect();
            }
        }
    };


    /**
     * 刷新焦点上的显示效果
     */
    private void refreshFocusEffect() {
        mFocusEffectWrapper.setIsHideMainUpView(true);
        isFirstLoginChild = false;
        if (mCurrentFocus == null) {
            return;
        }
        //Nav和搜索不显示边框
        if (mActivity.mNavRecyclerView.findFocus() != null || mActivity.mRiSearch.findFocus() != null) {
            return;
        }
        drawFocusEffect();
    }


    public void drawFocusEffect() {
        mFocusEffectWrapper.drawFocusEffect(mPreviousFocus, mCurrentFocus);
    }

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
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP && mActivity.mNavRecyclerView.findFocus() != null) {
            return true;
        }
        if ((event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) && mActivity.mNavRecyclerView.findFocus() != null) {
            if (!canClick()) {
                return true;
            }
        }
        //处理模板加载失败模板没view的问题
        if (mActivity.currentPosition != 0 && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN && mActivity.mNavRecyclerView.findFocus() != null
                && null != mActivity.mFragmentAdapter && mActivity.mFragmentAdapter.size() > mActivity.currentPosition) {
            PHMFragment phmFragment = (PHMFragment) mActivity.mFragmentAdapter.get(mActivity.currentPosition);
            if (null != phmFragment && phmFragment.isNoTemplate()) {
                return true;
            }
        }
        return false;
    }

    public boolean isNavEmpty(int position) {
        View view = mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(position);
        if (null != view && null != view.findViewById(R.id.tv_nav_title) && !TextUtils.isEmpty(((TextView) view.findViewById(R.id.tv_nav_title)).getText())) {
            return false;
        } else {
            return true;
        }
    }

    private boolean interceptPageTurnFocus(KeyEvent event, View v) {
        int keycode = event.getKeyCode();
        //判断翻页状态，如果是向左翻页
        if (keycode == KeyEvent.KEYCODE_DPAD_LEFT && mActivity.currentPosition != 0 && isBorder((ViewGroup) v, mCurrentFocus, View.FOCUS_LEFT)) {
            int position = mActivity.currentPosition - 1;
            mActivity.mNavRecyclerView.setCurentKeyCode(KeyEvent.KEYCODE_DPAD_LEFT);
            ((TextView) mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(mActivity.currentPosition).findViewById(R.id.tv_nav_title)).setTextColor(ContextCompat.getColor(mActivity, R.color.c24_color));
            mActivity.mViewpager.setCurrentItem(position);
            if (!isNavEmpty(position)) {
                navItemGetFocus(position);
            } else {
                mActivity.mViewpager.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 500);
            }
            return true;
        }
        //判断翻页状态，如果是向右翻页
        if (keycode == KeyEvent.KEYCODE_DPAD_RIGHT && isBorder((ViewGroup) v, mCurrentFocus, View.FOCUS_RIGHT)) {
            if (mActivity.currentPosition < mActivity.mFragmentAdapter.size() - 1) {
                int position = mActivity.currentPosition + 1;
                mActivity.mNavRecyclerView.setCurentKeyCode(KeyEvent.KEYCODE_DPAD_RIGHT);
                if (null == mActivity.mNavRecyclerView.getChildAt(position)) {
                    return true;
                }
                ((TextView) mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(mActivity.currentPosition).findViewById(R.id.tv_nav_title)).setTextColor(ContextCompat.getColor(mActivity, R.color.c24_color));
                mActivity.mViewpager.setCurrentItem(position);
                if (!isNavEmpty(position)) {
                    navItemGetFocus(position);
                }
                return true;
            } else if (mActivity.currentPosition == mActivity.mFragmentAdapter.size() - 1) {
//                mFocusEffectWrapper.clearEffectBridge(mCurrentFocus);
//                mActivity.mIvSearch.requestFocus();
//                if (!isNavEmpty(mActivity.currentPosition)) {
//                    ((TextView) mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(mActivity.currentPosition).findViewById(R.id.tv_nav_title)).setTextColor(ContextCompat.getColor(mActivity, R.color.c24_color));
//                }
//                mActivity.mViewpager.setCurrentItem(0);
//                mActivity.mNavRecyclerView.getLayoutManager().scrollToPosition(0);
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
    private boolean isBorder(ViewGroup root, View focused, int direction) {
        return FocusFinder.getInstance().findNextFocus(root, focused,
                direction) == null || (FocusFinder.getInstance().findNextFocus(root, focused,direction)).getParent() != focused.getParent();
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
        setFocusChangeEffect(true, (TextView) mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(mActivity.currentPosition).findViewById(R.id.tv_nav_title));
    }

    /**
     * 在fragment的时候返回键返回上一导航栏
     */
    public void onKeyBack() {
        if (mActivity.mNavRecyclerView.findFocus() == null) {
            if (mCurrentFocus != null) {
                mFocusEffectWrapper.clearEffectBridge(mCurrentFocus);
            }
            /*View view = mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(mActivity.currentPosition);
            if (isNavEmpty(mActivity.currentPosition)) {
                mActivity.mIvSearch.requestFocus();
            } else {
                view.requestFocus();
            }*/
            //滚动到顶部
            ((FocusHelperCallBack) mActivity.mFragmentAdapter.get(mActivity.currentPosition)).scrollToTop();
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
    private void navItemGetFocus(int position) {
        View navItem = mActivity.mNavRecyclerView.getLayoutManager().findViewByPosition(position);
        mFocusEffectWrapper.clearEffectBridge(mCurrentFocus);
        navItem.requestFocus();
    }
}


