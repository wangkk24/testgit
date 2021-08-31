package com.pukka.ydepg.customui.tv.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * 自定义GridLayoutManager，修改RecyelerView焦点长按的BUG
 */
public class FocusGridLayoutManager extends GridLayoutManager {

    private static final String TAG = "FocusGridLayoutManager";
    /**
     * Constructor used when layout manager is set in XML by RecyclerView attribute
     * "layoutManager". If spanCount is not specified in the XML, it defaults to a
     * single column.
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     * @attr ref android.support.v7.recyclerview.R.styleable#RecyclerView_spanCount
     */
    public FocusGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Creates a vertical GridLayoutManager
     *
     * @param context   Current context, will be used to access resources.
     * @param spanCount The number of columns in the grid
     */
    public FocusGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    /**
     * @param context       Current context, will be used to access resources.
     * @param spanCount     The number of columns or rows in the grid
     * @param orientation   Layout orientation. Should be {@link #HORIZONTAL} or {@link
     *                      #VERTICAL}.
     * @param reverseLayout When set to true, layouts from end to start.
     */
    public FocusGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    /**
     * Return the current number of child views attached to the parent RecyclerView.
     * This does not include child views that were temporarily detached and/or scrapped.
     *
     * @return Number of attached children
     */
    @Override
    public int getChildCount() {
        return super.getChildCount();
    }

    /**
     * Return the child view at the given index
     *
     * @param index Index of child to return
     * @return Child view at index
     */
    @Override
    public View getChildAt(int index) {
        return super.getChildAt(index);
    }

    /**
     * Returns the number of items in the adapter bound to the parent RecyclerView.
     * @return The number of items in the bound adapter
     */
    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    /**
     * Returns the item View which has or contains focus.
     *
     * @return A direct child of RecyclerView which has focus or contains the focused child.
     */
    @Override
    public View getFocusedChild() {
        return super.getFocusedChild();
    }

    /**
     * Returns the adapter position of the item represented by the given View. This does not
     * contain any adapter changes that might have happened after the last layout.
     *
     * @param view The view to query
     * @return The adapter position of the item which is rendered by this View.
     */
    @Override
    public int getPosition(View view) {
        return super.getPosition(view);
    }

    /**
     * 获取列数
     * @return
     */
    @Override
    public int getSpanCount() {
        return super.getSpanCount();
    }

//    /**
//     * Called when searching for a focusable view in the given direction has failed for the current content of the RecyclerView.
//     * This is the LayoutManager's opportunity to populate views in the given direction to fulfill the request if it can.
//     * The LayoutManager should attach and return the view to be focused. The default implementation returns null.
//     * 防止当recyclerview上下滚动的时候焦点乱跳
//     * @param focused
//     * @param focusDirection
//     * @param recycler
//     * @param state
//     * @return
//     */
//    @Override
//    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
//
//        Log.i(TAG,"onFocusSearchFailed()");
//        // 解决向上滑动，需要重新绘制项，以防向上不显示隐藏的项
//        if(focusDirection ==  View.FOCUS_UP) {
//            View nextFocus = super.onFocusSearchFailed(focused, focusDirection, recycler, state);
//            if (nextFocus == null) {
//                Log.i(TAG,"nextFocus is null");
//                return null;
//            }
//        }
//        /**
//         * 获取当前焦点的位置
//         */
//        int fromPos = getPosition(focused);
//        Log.i(TAG,"fromPos=" + fromPos);
//        /**
//         * 获取我们希望的下一个焦点的位置
//         */
//        int nextPos = getNextViewPos(fromPos, focusDirection);
//        if(nextPos <=0){
//            nextPos = 0;
//        }
//        Log.i(TAG,"nextPos=" + nextPos);
//        return findViewByPosition(nextPos);
//
//    }

    //重写方法
    public View onInterceptFocusSearch(View focused, int direction) {
        int count = getItemCount();//获取item的总数
        int fromPos = getPosition(focused);//当前焦点的位置
        int lastVisibleItemPos = findLastVisibleItemPosition();//最新的已显示的Item的位置

        int nextPos = getNextViewPos(fromPos, direction);
        Log.e("whd", "onInterceptFocusSearch , fromPos = " + fromPos + " , count = " + count+" , lastVisibleItemPos = "+lastVisibleItemPos+"nextPos = "+ nextPos);
        if(fromPos < 0 || nextPos >= count ) {
            Log.e("whd", "fromPos < 0 || fromPos >= count");
            //如果下一个位置<0,或者超出item的总数，则返回当前的View，即焦点不动
            return focused;
        } else {
            //如果下一个位置大于最新的已显示的item，即下一个位置的View没有显示，则滑动到那个位置，让他显示，就可以获取焦点了
            if (nextPos > lastVisibleItemPos) {
                Log.e("whd", "nextPos > lastVisibleItemPos");
                scrollToPosition(fromPos);
//                scrollToPositionWithOffset(fromPos, 10);
            }
        }
        return super.onInterceptFocusSearch(focused, direction);
    }
//    @Override
//    public View onInterceptFocusSearch(View focused, int direction) {
//        Log.i(TAG,"onInterceptFocusSearch");
//        return super.onInterceptFocusSearch(focused, direction);
//        /**
//         * 获取当前焦点的位置
//         */
////        int fromPos = getPosition(focused);
////        Log.i(TAG,"fromPos=" + fromPos);
////        /**
////         * 获取我们希望的下一个焦点的位置
////         */
////        int nextPos = getNextViewPos(fromPos, direction);
////        Log.i(TAG,"nextPos=" + nextPos);
////        return findViewByPosition(nextPos);
//    }

    /**
     * Manually detect next view to focus.
     *
     * @param fromPos from what position start to seek.
     * @param direction in what direction start to seek. Your regular {@code View.FOCUS_*}.
     * @return adapter position of next view to focus. May be equal to {@code fromPos}.
     */
    protected int getNextViewPos(int fromPos, int direction) {
        int offset = calcOffsetToNextView(direction);
        Log.i(TAG,"offset=" + offset);
        if (hitBorder(fromPos, offset)) {
            return fromPos;
        }

        return fromPos + offset;
    }

    /**
     * Calculates position offset.
     *
     * @param direction regular {@code View.FOCUS_*}.
     * @return position offset according to {@code direction}.
     */
    protected int calcOffsetToNextView(int direction) {
        int spanCount = getSpanCount();
        int orientation = getOrientation();

        if (orientation == VERTICAL) {
            switch (direction) {
                case View.FOCUS_DOWN:
                    return spanCount;
                case View.FOCUS_UP:
                    return -spanCount;
                case View.FOCUS_RIGHT:
                    return 1;
                case View.FOCUS_LEFT:
                    return -1;
            }
        } else if (orientation == HORIZONTAL) {
            switch (direction) {
                case View.FOCUS_DOWN:
                    return 1;
                case View.FOCUS_UP:
                    return -1;
                case View.FOCUS_RIGHT:
                    return spanCount;
                case View.FOCUS_LEFT:
                    return -spanCount;
            }
        }

        return 0;
    }

    /**
     * Checks if we hit borders.
     *
     * @param from from what position.
     * @param offset offset to new position.
     * @return {@code true} if we hit border.
     */
    private boolean hitBorder(int from, int offset) {
        int spanCount = getSpanCount();

        if (Math.abs(offset) == 1) {
            int spanIndex = from % spanCount;
            int newSpanIndex = spanIndex + offset;
            return newSpanIndex < 0 || newSpanIndex >= spanCount;
        } else {
            int newPos = from + offset;
            return newPos < 0 && newPos >= spanCount;
        }
    }
}