package com.pukka.ydepg.customui.tv.bridge;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.customui.tv.utils.TVUtils;
import com.pukka.ydepg.customui.tv.widget.RecyclerViewTV;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;


public class RecyclerViewBridge extends EffectNoDrawBridge {
	private static final String TAG = "RecyclerViewBridge";
	private AnimatorSet mCurrentAnimatorSet;
	
	/**
	 * 重写边框移动函数.  flyWhiteBorder(toView, getMainUpView(), scaleX, scaleY);
	 */
	@Override
	public void flyWhiteBorder(final View focusView, View oldView, View moveView, final float scaleX, final float scaleY) {
		//获取内边距
		RectF paddingRect = getDrawUpRect();
		//新View的宽
		int newWidth = 0;
		//新View的高
		int newHeight = 0;
		//旧View的宽
		int oldWidth = 0;
		//旧View的高
		int oldHeight = 0;
		
		int newX = 0;
		int newY = 0;
        RecyclerView rv = null;
		if (focusView != null) {
			newWidth = (int) (focusView.getMeasuredWidth() * scaleX);
			newHeight = (int) (focusView.getMeasuredHeight() * scaleY);
			oldWidth = moveView.getMeasuredWidth();
			oldHeight = moveView.getMeasuredHeight();
			Rect fromRect = findLocationWithView(moveView);
			Rect toRect = findLocationWithToView(focusView);
			Log.i(TAG,"toRect=" + toRect.top +"," +toRect.bottom);

			// 处理 RecyclerView TV 上 移动边框跑偏的问题.
            if (null != focusView && focusView.getId() == R.id.rr_item_my_function) {
				if (oldView.getId() == focusView.getId() && focusView.getParent() instanceof RecyclerView) {
					rv = (RecyclerView) focusView.getParent();
				} else if (focusView.getParent().getParent().getParent().getParent() instanceof RecyclerView){
					rv = (RecyclerView) focusView.getParent().getParent().getParent().getParent();
				}
				final int offset = rv.getBaseline();
				if (offset != -1) {
					toRect.offset(rv.getLayoutManager().canScrollHorizontally() ? -offset : 0,
							rv.getLayoutManager().canScrollVertically() ? -offset : 0);
				}
            } else if(null!=focusView&&focusView.getId()==R.id.rl_item_my_list){
				rv = (RecyclerView) focusView.getParent().getParent().getParent().getParent().getParent();
				final int offset = rv.getBaseline();
				if (offset != -1) {
					toRect.offset(rv.getLayoutManager().canScrollHorizontally() ? -offset : 0,
							rv.getLayoutManager().canScrollVertically() ? -offset : 0);
				}
			}else if (null != focusView.getParent() && focusView.getParent() instanceof RecyclerView) {
                rv = (RecyclerView) focusView.getParent();
                final int offset = rv.getBaseline();
                if (offset != -1) {
                    toRect.offset(rv.getLayoutManager().canScrollHorizontally() ? -offset : 0,
                            rv.getLayoutManager().canScrollVertically() ? -offset : 0);
                }
            } else if (null != focusView.getParent().getParent() && focusView.getParent().getParent() instanceof RecyclerView) {
                rv = (RecyclerView) focusView.getParent().getParent();
                final int offset = rv.getBaseline();
                if (offset != -1) {
                    toRect.offset(rv.getLayoutManager().canScrollHorizontally() ? -offset : 0,
                            rv.getLayoutManager().canScrollVertically() ? -offset : 0);
                }
            } else if (null != focusView.getParent().getParent().getParent() && focusView.getParent().getParent().getParent() instanceof RecyclerView) {
                rv = (RecyclerView) focusView.getParent().getParent().getParent();
                final int offset = rv.getBaseline();
                if (offset != -1) {
                    toRect.offset(rv.getLayoutManager().canScrollHorizontally() ? -offset : 0,
                            rv.getLayoutManager().canScrollVertically() ? -offset : 0);
                }
            } else if (null != focusView.getParent().getParent().getParent().getParent() && focusView.getParent().getParent().getParent().getParent() instanceof RecyclerView) {
                rv = (RecyclerView) focusView.getParent().getParent().getParent().getParent();
                final int offset = rv.getBaseline();
                if (offset != -1) {
                    toRect.offset(rv.getLayoutManager().canScrollHorizontally() ? -offset : 0,
                            rv.getLayoutManager().canScrollVertically() ? -offset : 0);
                }
            }
			//
			//Log.i("DetailActivity","<<<<<<<<"+toRect.top);

			int x = toRect.left - fromRect.left - ((int)Math.rint(paddingRect.left));
			int y = toRect.top - fromRect.top - ((int)Math.rint(paddingRect.top));
			newX = x - Math.abs(focusView.getMeasuredWidth() - newWidth) / 2;
			newY = y - Math.abs(focusView.getMeasuredHeight() - newHeight) / 2;
			//
			newWidth += ((int)Math.rint(paddingRect.right) + (int)Math.rint(paddingRect.left));
			newHeight += ((int)Math.rint(paddingRect.bottom) + (int)Math.rint(paddingRect.top));
		}

		// 取消之前的动画.
		if (mCurrentAnimatorSet != null) {
			mCurrentAnimatorSet.cancel();
			mCurrentAnimatorSet = null;
		}
        if (rv != null && rv instanceof RecyclerViewTV)
        {
            if (((RecyclerViewTV) rv).isScrolling())
            {
                newX -= ((RecyclerViewTV) rv).getMoveX();
                newY -= ((RecyclerViewTV) rv).getMoveY();
            }
        }
		ObjectAnimator transAnimatorX = ObjectAnimator.ofFloat(moveView, "translationX", newX);
		ObjectAnimator transAnimatorY = ObjectAnimator.ofFloat(moveView, "translationY", newY);
		// BUG，因为缩放会造成图片失真(拉伸).
		// hailong.qiu 2016.02.26 修复 :)
		ObjectAnimator scaleXAnimator = ObjectAnimator.ofInt(new ScaleView(moveView), "width", oldWidth,
				(int) newWidth);
		ObjectAnimator scaleYAnimator = ObjectAnimator.ofInt(new ScaleView(moveView), "height", oldHeight,
				(int) newHeight);
		//
		AnimatorSet mAnimatorSet = new AnimatorSet();
		mAnimatorSet.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator);
		mAnimatorSet.setInterpolator(new DecelerateInterpolator(1));
		mAnimatorSet.setDuration(getTranDurAnimTime());
		mAnimatorSet.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				if (isVisibleWidget()) {
					getMainUpView().setVisibility(View.GONE);
				}
				if (getNewAnimatorListener() != null)
					getNewAnimatorListener().onAnimationStart(RecyclerViewBridge.this, focusView, animation);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				getMainUpView().setVisibility(isVisibleWidget() ? View.GONE : View.VISIBLE);
				if (getNewAnimatorListener() != null)
					getNewAnimatorListener().onAnimationEnd(RecyclerViewBridge.this, focusView, animation);
				// BUG:5.0系统边框错位.
				if (TVUtils.getSDKVersion() >= 21) {
					int newWidth = (int) (focusView.getMeasuredWidth() *
							scaleX);
					int newHeight = (int) (focusView.getMeasuredHeight() *
							scaleY);
					getMainUpView().getLayoutParams().width = newWidth + (int)getDrawUpRect().left+(int)getDrawUpRect().right;
					getMainUpView().getLayoutParams().height = newHeight + (int)getDrawUpRect().top + (int)getDrawUpRect().bottom;
					getMainUpView().requestLayout();
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
		mAnimatorSet.start();
		mCurrentAnimatorSet = mAnimatorSet;
	}

	public Rect findLocationWithToView(View view) {
		ViewGroup root = (ViewGroup) getMainUpView().getParent();
		Rect rect = new Rect();
		ViewGroup parent;
		Rect parentRect = new Rect();
        if (view.getId() == R.id.rr_item_my_function) {
            parent = (ViewGroup) view.getParent().getParent().getParent();
        } else if(view.getId()==R.id.rl_item_my_list){
        	parent = (ViewGroup) view.getParent().getParent().getParent().getParent();
		}else if(view instanceof ReflectRelativeLayout) {
            parent = (ViewGroup) view.getParent(); //phm的父容器宽度是充满全屏的
        }else{
            parent = (ViewGroup)view.getParent().getParent(); //我的需要获取父容器的父容器才和全屏等宽
            if(parent!= null && parent.getId() != R.id.rl_container){
                parent = null;
            }
        }
		if(parent == null){
			return findLocationWithView(view);
		}
		root.offsetDescendantRectToMyCoords(parent, parentRect);
		//父容器对于屏幕的偏离
		int left = parentRect.left;

		root.offsetDescendantRectToMyCoords(view, rect);
		//修正自身位置
		rect.left = rect.left - left;
		rect.right = rect.left + rect.width();
		return rect;
	}

}
