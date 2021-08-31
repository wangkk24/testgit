package com.pukka.ydepg.customui.tv.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.DeviceInfo;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.ui.template.FreeLayoutTemplate;
import com.pukka.ydepg.launcher.ui.template.PHMTemplate;

/**
 * RecyclerView TV适配版本.
 * https://github.com/zhousuqiang/TvRecyclerView(参考源码)
 */
public class RecyclerViewTV extends RecyclerView {
    private static final String TAG = "RecyclerViewTV";

    public RecyclerViewTV(Context context) {
        this(context, null);
    }

    public RecyclerViewTV(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RecyclerViewTV(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    protected int position = 0;
    private OnItemListener mOnItemListener;
    private OnItemClickListener mOnItemClickListener; // item 单击事件.
    private ItemListener mItemListener;
    private int offset = -1;

    protected int screenWidth;
    protected int screenHeight;

    /**
     * 计算滚动的距离 用于替换this.computeVerticalScrollOffset()
     */
    private int scollOff = 0;
    private int scollOffScroll = 0;

    protected boolean isEndScrolling = false;
    private int scrollDy = -1;

    public int getScollOffScroll(){
        return scollOffScroll;
    }

    private void init(Context context) {
        this.screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        this.screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);//先分发给Child View进行处理，如果所有的Child View都没有处理，则自己再处理
        setHasFixedSize(true);
        setWillNotDraw(true);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setClipChildren(false);
        setClipToPadding(false);

        setClickable(false);
        setFocusable(false);
        setFocusableInTouchMode(false);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scollOffScroll -= dy;
            }
        });
        //
        mItemListener = new ItemListener() {
            /**
             * 子控件的点击事件
             * @param itemView
             */
            @Override
            public void onClick(View itemView) {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onItemClick(RecyclerViewTV.this, itemView, getChildLayoutPosition(itemView));
                }
            }

            /**
             * 子控件的焦点变动事件
             * @param itemView
             * @param hasFocus
             */
            @Override
            public void onFocusChange(View itemView, boolean hasFocus) {
                if (null != mOnItemListener) {
                    if (null != itemView) {
                        itemView.setSelected(hasFocus);
                        if (hasFocus) {
                            mOnItemListener.onItemSelected(RecyclerViewTV.this, itemView, getChildLayoutPosition(itemView));
                        } else {
                            mOnItemListener.onItemPreSelected(RecyclerViewTV.this, itemView, getChildLayoutPosition(itemView));
                        }
                    }
                }
            }
        };
    }

    /**
     * 由于调动该方法时onScroll不触发，手动赋值
     *
     * @param position
     */
    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(position);
        if (position == 0) {
            scollOff = 0;
            scollOffScroll = 0;
        }
    }

    @Override
    public View focusSearch(View focused, int direction) {
        return super.focusSearch(focused, direction);
    }

    private int curentKeyCode;

    public int getCurentKeyCode() {
        return this.curentKeyCode;
    }

    public void setCurentKeyCode(int code) {
        this.curentKeyCode = code;
    }

    @Override
    public void onChildAttachedToWindow(View child) {
        // 设置单击事件，修复.
        if (!child.hasOnClickListeners()) {
            child.setOnClickListener(mItemListener);
        }
        // 设置焦点事件，修复  一级栏目
        if (child.getOnFocusChangeListener() == null) {
            child.setOnFocusChangeListener(mItemListener);
        }
    }

    @Override
    public View focusSearch(int direction) {
        View nextFocusView = FocusFinder.getInstance().findNextFocus(this, this.findFocus(), direction);
        // 解决上个模板没展示在屏幕中时，按上键没有相应的问题
        if (nextFocusView == null && findFocus().getParent() instanceof PHMTemplate && direction == View.FOCUS_UP) {
            int position = getChildAdapterPosition((PHMTemplate) findFocus().getParent());
            if (position > 0) {
                PHMTemplate preTemplate = (PHMTemplate) recycler.getViewForPosition(position - 1);
                nextFocusView = preTemplate.getLastView();
                scrollToPosition(position - 1);
                findFocus().clearFocus();
                return nextFocusView;
            }
        }
        return super.focusSearch(findFocus(), direction);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override
    public boolean hasFocus() {
        return super.hasFocus();
    }

    @Override
    public boolean isInTouchMode() {
        // 解决4.4版本抢焦点的问题
        if (Build.VERSION.SDK_INT == 19) {
            return !(hasFocus() && !super.isInTouchMode());
        } else {
            return super.isInTouchMode();
        }
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (null != child) {
            super.requestChildFocus(child, focused);
        }
    }

    protected boolean isScrolling = false;

    public boolean isScrolling() {
        return this.isScrolling;
    }

    public void setIsScrolling(boolean isScrolling) {
        this.isScrolling = isScrolling;
    }

    private int moveX;
    private int moveY;

    //如果是特俗模板，小于15的滚动就取消
    private boolean isSpecialTmplat = false;

    public int getMoveX() {
        return this.moveX;
    }

    public int getMoveY() {
        return this.moveY;
    }

    Recycler recycler = null;

    private View mFocusView;
    private int mDirection = -1;
    public void setFocusViewAndDirection(View focusView,int keyCode) {
        this.mFocusView = focusView;
        if (keyCode != -1){
            this.mDirection = keyCode;
        }
    }

    private void navRequestFocus(View focusView){
        if (focusView.getId() == R.id.tv_nav_parent){
            OTTApplication.getContext().getMainActivity().requestNavItemFocus();
        }
    }

    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        this.moveY = this.moveX = 0;
        View focusView;
        if (curentKeyCode == -1 && mDirection == -1) {
            // TODO
            return false;
        }else if (curentKeyCode == -1){
            curentKeyCode = mDirection;
            mDirection = -1;
        }
        focusView = child.findFocus();
        if (null == focusView && null == mFocusView){
            return false;
        }else if (null == focusView){
            focusView = mFocusView;
            mFocusView = null;
        }
        // 视频模板特殊处理，确保屏幕能完整展示整个模板
        // 左侧两个小块堆叠的模板特殊处理，防止左右切换时上下滚动
        // 对超高模板PannelPoster_14特殊处理，防止临近模板滚出屏幕无法继续滚动
        if (focusView.getParent() instanceof PHMTemplate) {
            PHMTemplate template = (PHMTemplate) focusView.getParent();
            if (isSpecial(template)) {
                focusView = template;
            }
        }else if (focusView.getParent().getParent().getParent() instanceof PHMTemplate){
            PHMTemplate template = (PHMTemplate) focusView.getParent().getParent().getParent();
            if (isSpecial(template)) {
                focusView = template;
            }
        }else if (focusView.getParent().getParent().getParent().getParent() instanceof PHMTemplate){
            PHMTemplate template = (PHMTemplate) focusView.getParent().getParent().getParent().getParent();
            if (isSpecial(template)) {
                isSpecialTmplat = true;
                focusView = template;
            }
        }

        int[] focusViewLocation = new int[2];
        int[] recyclerviewLocation = new int[2];
        this.getLocationOnScreen(recyclerviewLocation);
        int offY = recyclerviewLocation[1] - this.getTop();
        focusView.getLocationOnScreen(focusViewLocation);
        int focusViewCenterY = focusViewLocation[1] - offY + focusView.getHeight() / 2;
        int focusViewCenterX = focusViewLocation[0] + focusView.getWidth() / 2;
        int centerScreenH = (screenHeight - offY) / 2;
        int centerScreenW = (screenWidth) / 2;
        int dx = 0, dy = 0;
        boolean isTopUp = false;
        switch (curentKeyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                //this.computeVerticalScrollOffset()计算移出屏幕距离不准确，修改为自己计算滚动距离 -------modified by luwm 2018-06-10
                scollOff = -this.computeVerticalScrollOffset();
                if (child instanceof FreeLayoutTemplate && getChildLayoutPosition(child) == 1) {
                    dy = scollOff;
                }else if (centerScreenH > focusViewCenterY && this.canScrollVertically(-1)) {
                    dy = focusViewCenterY - centerScreenH;
                    if (Math.abs(dy) > Math.abs(scollOff) || ((scollOff - dy) > 0 && (scollOff - dy) < 10)
                            || ((Math.abs(scollOff) - Math.abs(dy)) <= 52)) {
                        if (Math.abs(scollOff) > Math.abs(scollOffScroll)){
                            dy = scollOffScroll;
                        }else{
                            dy = scollOff;
                        }
                        isTopUp = true;
                    }else if (DeviceInfo.isSkyworth() && (Math.abs(dy) > Math.abs(scollOff) || ((scollOff - dy) > 0 && (scollOff - dy) < 10)
                            || ((Math.abs(scollOff) - Math.abs(dy)) <= 80))){
                        //解决创维电视焦点框错位问题
                        if (Math.abs(scollOff) > Math.abs(scollOffScroll)){
                            dy = scollOffScroll;
                        }else{
                            dy = scollOff;
                        }
                    }
                    //------
                    else if (Math.abs(dy) > Math.abs(scollOffScroll) || ((scollOffScroll - dy) > 0 && (scollOffScroll - dy) < 10)
                            || ((Math.abs(scollOffScroll) - Math.abs(dy)) <= 52)) {
                        if (Math.abs(scollOff) < 1500){
                            dy = scollOffScroll;
                            isTopUp = true;
                        }
                    }else if (DeviceInfo.isSkyworth() && (Math.abs(dy) > Math.abs(scollOffScroll) || ((scollOffScroll - dy) > 0 && (scollOffScroll - dy) < 10)
                            || ((Math.abs(scollOffScroll) - Math.abs(dy)) <= 80))){
                        //解决创维电视焦点框错位问题
                        if (Math.abs(scollOff) < 1300){
                            dy = scollOffScroll;
                        }
                    }
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (focusViewCenterY > centerScreenH && this.canScrollVertically(1)) {
                    dy = focusViewCenterY - centerScreenH;
                    int canScrollOffsetY = this.computeVerticalScrollRange() - this.computeVerticalScrollExtent() - this.computeVerticalScrollOffset();
                    if (dy <= canScrollOffsetY && dy < 200 && (canScrollOffsetY-dy)<dy){
                        //if(canScrollOffsetY<centerScreenH){//修复：如果最底部两个模板高度比较小，焦点框错位问题
                            dy=recyclerviewLocation[1];
                        //}
                    }else if (dy > canScrollOffsetY) {
                        dy = canScrollOffsetY;
                    }else if (canScrollOffsetY < 430){//修复：如果最底部两个模板高度比较小，焦点框错位问题
                        if (dy < 50){
                            dy=0;
                        }else{
                            dy-=50;
                        }
                    }else if ((canScrollOffsetY - dy) < 700 && (canScrollOffsetY - dy) > 205){//解决最后一个phmtem无法落焦问题
                        dy+=30;
                    }else if (DeviceInfo.isSkyworth() && dy == 437 && canScrollOffsetY == 579){
                        //修改创维电视 儿童版 特殊模板配置，在最底部焦点框错位问题
                        dy -= 65;
                    }else if (((canScrollOffsetY - dy)) < 200 && dy > 400){
                        dy-=50;
                    }
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (centerScreenW > focusViewCenterX && this.canScrollHorizontally(-1)) {
                    dx = focusViewCenterX - centerScreenW;
                    if (Math.abs(dx) > this.computeHorizontalScrollOffset()) {
                        dx = -this.computeHorizontalScrollOffset();
                    }
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (focusViewCenterX > centerScreenW && this.canScrollHorizontally(1)) {
                    dx = focusViewCenterX - centerScreenW;
                    int canScrollOffsetX = this.computeHorizontalScrollRange() - this.computeHorizontalScrollExtent() - this.computeHorizontalScrollOffset();
                    if (dx > canScrollOffsetX) {
                        dx = canScrollOffsetX;
                    }
                }
                break;
            default:
                break;
        }

        if (isSpecialTmplat){
            isSpecialTmplat = false;
            if (Math.abs(dy) < 20){
                dy = 0;
            }
        }

        if (dx == 0 && dy == 0) {
            this.curentKeyCode = -1;
            this.isScrolling = false;
            return false;
        }
        this.isScrolling = true;

        this.moveX = dx;
        this.moveY = dy;
        if (scrollListener != null) {
            scrollListener.startScroll(curentKeyCode);
        }
        this.curentKeyCode = -1;

        if (dy > 0 && getAdapter().getItemCount()-getChildLayoutPosition(child) < 3){
            scrollDy = dy;
            isEndScrolling = true;
        }

        if (immediate) {
            scrollBy(dx, dy);
        } else {
            smoothScrollBy(dx, dy);
        }

        if (isTopUp){
            navRequestFocus(focusView);
        }

        return true;
    }

    private boolean isSpecial(PHMTemplate template) {
        return template.getLayoutId() == R.layout.pannel_poster_1125 || template.getLayoutId() == R.layout.pannel_poster_1521
                || template.getLayoutId() == R.layout.pannel_poster_14 || template.getLayoutId() == R.layout.pannel_poster_l1_r4
                || template.getLayoutId() == R.layout.pannel_poster_1324 || template.getLayoutId() == R.layout.pannel_poster_1524
                || template.getLayoutId() == R.layout.pannel_poster_live_l1_r4 || template.getLayoutId() == R.layout.pannel_poster_0_1422
                || template.getLayoutId() == R.layout.pannel_poster_vertiacal_hot || template.getLayoutId() == R.layout.pannel_poster_024_142231_video
                || template.getLayoutId() == R.layout.pannel_poster_024_1422_video || template.getLayoutId() == R.layout.pannel_poster_024_13_video;
    }

    @Override
    public int getBaseline() {
        return offset;
    }

    @Override
    public void smoothScrollBy(int dx, int dy) {
        super.smoothScrollBy(dx, dy);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
    }

    /**
     * 改变子类的绘制顺序就要重写该方法
     * [放大的那个是最后 需要绘制的]
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        View view = getFocusedChild();
        if (null != view) {
            position = getChildAdapterPosition(view) - getFirstVisiblePosition();
            if (position < 0) {
                return i;
            } else {
                if (i == childCount - 1) {//这是最后一个需要刷新的item
                    if (position > i) {
                        position = i;
                    }
                    return position;
                }
                if (i == position) {//这是原本要在最后一个刷新的item
                    return childCount - 1;//放大的那个是最后 需要绘制的
                }
            }
        }
        return i;
    }

    public int getFirstVisiblePosition() {
        if (getChildCount() == 0)
            return 0;
        else
            return getChildLayoutPosition(getChildAt(0));
    }


    @Override
    public void onScrollStateChanged(int state) {
        if (state == SCROLL_STATE_IDLE) {
            offset = -1;
            final View focuse = getFocusedChild();
            if (null != mOnItemListener && null != focuse) {
                mOnItemListener.onReviseFocusFollow(this, focuse, getChildLayoutPosition(focuse));
            }
        }
        super.onScrollStateChanged(state);
    }

    private interface ItemListener extends OnClickListener, OnFocusChangeListener {}

    public interface OnItemListener {
        void onItemPreSelected(RecyclerViewTV parent, View itemView, int position);
        void onItemSelected(RecyclerViewTV parent, View itemView, int position);
        void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position);
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerViewTV parent, View itemView, int position);
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.mOnItemListener = onItemListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        View nextFocusView = null;
        switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN: {
                if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                    if (event.getRepeatCount() > 0) {
                        if (!isFastScroll && scrollListener != null) {
                            Log.d("wzh", "startScroll begin ....");
                            isFastScroll = true;
                            scrollListener.startScroll(event.getKeyCode());
                        }
                    }
                }
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_DPAD_UP:
                        nextFocusView = FocusFinder.getInstance().findNextFocus(this, findFocus(), FOCUS_UP);
                        if (nextFocusView != null && this.getId() != R.id.rv_viewPlayRecord) {
                            setFocusViewAndDirection(nextFocusView,event.getKeyCode());
                            curentKeyCode = event.getKeyCode();
                        }
                        break;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        nextFocusView = FocusFinder.getInstance().findNextFocus(this, findFocus(), FOCUS_DOWN);
                        if (nextFocusView != null) {
                            curentKeyCode = event.getKeyCode();
                        }
                        break;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        nextFocusView = FocusFinder.getInstance().findNextFocus(this, findFocus(), FOCUS_RIGHT);
                        if (nextFocusView != null) {
                            curentKeyCode = event.getKeyCode();
                        }
                        break;
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        nextFocusView = FocusFinder.getInstance().findNextFocus(this, findFocus(), FOCUS_LEFT);
                        if (nextFocusView != null) {
                            curentKeyCode = event.getKeyCode();
                        }
                        break;
                }
                break;
            }
            case KeyEvent.ACTION_UP:
                if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                    if (scrollListener != null) {
                        Log.d("wzh", "endScroll begin ....");
                        scrollListener.endScroll(event.getKeyCode());
                    }
                }
        }


        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                || keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return super.onInterceptTouchEvent(e);
    }

    private boolean isFastScroll;

    private ScrollListener scrollListener;

    public void setIsFastScroll(boolean isFastScroll) {
        this.isFastScroll = isFastScroll;
    }

    public boolean isFastScroll() {
        return this.isFastScroll;
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public interface ScrollListener {
        void startScroll(int keyCode);
        void endScroll(int keyCode);
    }

    @Override
    public void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
            SuperLog.error(TAG,"MyImageView  -> onDraw() Canvas: trying to use a recycled bitmap");
        }
    }
}