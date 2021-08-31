package com.pukka.ydepg.customui;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import androidx.core.view.ViewCompat;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class CommonTVRecycleView extends VerticalGridView {

  public CommonTVRecycleView(Context context) {
    this(context, null);
  }

  public CommonTVRecycleView(Context context, AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public CommonTVRecycleView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private boolean mSelectedItemCentered = true;
  private int mSelectedItemOffsetStart;
  private int mSelectedItemOffsetEnd;
  protected int position = 0;
  private OnItemClickListener mOnItemClickListener; // item 单击事件.
  private ItemListener mItemListener;
  private int offset = -1;
  protected int mAction ;
  protected int mKeyCode ;
  public int getAction() {
    return mAction;
  }

  public int getKeyCode() {
    return mKeyCode;
  }

  private void init() {
    setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
    setHasFixedSize(true);
    setWillNotDraw(true);
    setOverScrollMode(View.OVER_SCROLL_NEVER);
    setChildrenDrawingOrderEnabled(true);
    //
    setClipChildren(false);
    setClipToPadding(false);

    setClickable(false);
    setFocusable(true);
    setFocusableInTouchMode(true);
    //
    mItemListener = new ItemListener() {
      /**
       * 子控件的点击事件
       */
      @Override
      public void onClick(View itemView) {
        if (null != mOnItemClickListener) {
          mOnItemClickListener.onItemClick(CommonTVRecycleView.this, itemView, getChildLayoutPosition(itemView));
        }
      }

      /**
       * 子控件的焦点变动事件
       */
      @Override
      public void onFocusChange(View itemView, boolean hasFocus) {
      }
    };
  }

  private int getFreeWidth() {
    return getWidth() - getPaddingLeft() - getPaddingRight();
  }

  private int getFreeHeight() {
    return getHeight() - getPaddingTop() - getPaddingBottom();
  }

  @Override
  public void onChildAttachedToWindow(View child) {
    // 设置单击事件，修复.
    if (!child.hasOnClickListeners()) {
      child.setOnClickListener(mItemListener);
    }
    // 设置焦点事件，修复
    if (child.getOnFocusChangeListener() == null) {
      child.setOnFocusChangeListener(mItemListener);
    }
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
      if (mSelectedItemCentered) {
        mSelectedItemOffsetStart = !isVertical() ? (getFreeWidth() - child.getWidth()) : (getFreeHeight() - child.getHeight());
        mSelectedItemOffsetStart /= 2;
        mSelectedItemOffsetEnd = mSelectedItemOffsetStart;
      }
      super.requestChildFocus(child, focused);
    }
  }

  @Override
  public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
    final int parentLeft = getPaddingLeft();
    final int parentTop = getPaddingTop();
    final int parentRight = getWidth() - getPaddingRight();
    final int parentBottom = getHeight() - getPaddingBottom();

    final int childLeft = child.getLeft() + rect.left;
    final int childTop = child.getTop() + rect.top;

    //        final int childLeft = child.getLeft() + rect.left - child.getScrollX();
    //        final int childTop = child.getTop() + rect.top - child.getScrollY();

    final int childRight = childLeft + rect.width();
    final int childBottom = childTop + rect.height();

    final int offScreenLeft = Math.min(0, childLeft - parentLeft - mSelectedItemOffsetStart);
    final int offScreenTop = Math.min(0, childTop - parentTop - mSelectedItemOffsetStart);
    final int offScreenRight = Math.max(0, childRight - parentRight + mSelectedItemOffsetEnd);
    final int offScreenBottom = Math.max(0, childBottom - parentBottom + mSelectedItemOffsetEnd);

    final boolean canScrollHorizontal = getLayoutManager().canScrollHorizontally();
    final boolean canScrollVertical = getLayoutManager().canScrollVertically();

    // Favor the "start" layout direction over the end when bringing one side or the other
    // of a large rect into view. If we decide to bring in end because start is already
    // visible, limit the scroll such that start won't go out of bounds.
    final int dx;
    if (canScrollHorizontal) {
      if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
        dx = offScreenRight != 0 ? offScreenRight
            : Math.max(offScreenLeft, childRight - parentRight);
      } else {
        dx = offScreenLeft != 0 ? offScreenLeft
            : Math.min(childLeft - parentLeft, offScreenRight);
      }
    } else {
      dx = 0;
    }

    // Favor bringing the top into view over the bottom. If top is already visible and
    // we should scroll to make bottom visible, make sure top does not go out of bounds.
    final int dy;
    if (canScrollVertical) {
      dy = offScreenTop != 0 ? offScreenTop : Math.min(childTop - parentTop, offScreenBottom);
    } else {
      dy = 0;
    }
    if (cannotScrollForwardOrBackward(isVertical() ? dy : dx)) {
      offset = -1;
    } else {
      offset = isVertical() ? dy : dx;
      if (dx != 0 || dy != 0) {
        if (immediate) {
          scrollBy(dx, dy);
        } else {
          smoothScrollBy(dx, dy);
        }
        return true;
      }

    }

    // 重绘是为了选中item置顶，具体请参考getChildDrawingOrder方法
    postInvalidate();

    return false;
  }

  private boolean cannotScrollForwardOrBackward(int value) {
    return false;
  }

  @Override
  public int getBaseline() {
    return offset;
  }

  @Override
  public void smoothScrollBy(int dx, int dy) {
    // ViewFlinger --> smoothScrollBy(int dx, int dy, int duration, Interpolator interpolator)
    //  ViewFlinger --> run --> hresult = mLayout.scrollHorizontallyBy(dx, mRecycler, mState);
    // LinearLayoutManager --> scrollBy --> mOrientationHelper.offsetChildren(-scrolled);
    super.smoothScrollBy(dx, dy);
  }

  @Override
  public void setLayoutManager(LayoutManager layout) {
    super.setLayoutManager(layout);
  }

  /**
   * 判断是垂直，还是横向.
   */
  private boolean isVertical() {
    LinearLayoutManager layout = (LinearLayoutManager) getLayoutManager();
    return layout.getOrientation() == LinearLayoutManager.VERTICAL;
  }
  /**
   * 设置选中的Item居中
   */
  public void setSelectedItemAtCentered(boolean isCentered) {
    this.mSelectedItemCentered = isCentered;
  }

  @Override
  public void onScrollStateChanged(int state) {
    if (state == SCROLL_STATE_IDLE) {
      offset = -1;
    }
    super.onScrollStateChanged(state);
  }

  private interface ItemListener extends OnClickListener, OnFocusChangeListener {
  }

  public interface OnItemClickListener {
    void onItemClick(CommonTVRecycleView parent, View itemView, int position);
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    this.mOnItemClickListener = onItemClickListener;
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT
        || keyCode == KeyEvent.KEYCODE_DPAD_UP){
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent e) {
    return super.onInterceptTouchEvent(e);
  }

}
