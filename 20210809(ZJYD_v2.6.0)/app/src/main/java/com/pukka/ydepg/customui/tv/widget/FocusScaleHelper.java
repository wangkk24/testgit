package com.pukka.ydepg.customui.tv.widget;

import android.animation.TimeAnimator;
import android.content.res.Resources;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.pukka.ydepg.R;

import static com.pukka.ydepg.customui.tv.widget.FocusHighlight.ZOOM_FACTOR_NONE;
import static com.pukka.ydepg.customui.tv.widget.FocusHighlight.ZOOM_FACTOR_SMALL;
import static com.pukka.ydepg.customui.tv.widget.FocusHighlight.ZOOM_FACTOR_XSMALL;
import static com.pukka.ydepg.customui.tv.widget.FocusHighlight.ZOOM_FACTOR_MEDIUM;
import static com.pukka.ydepg.customui.tv.widget.FocusHighlight.ZOOM_FACTOR_LARGE;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2016, by jiaxing, ChengDu, All rights reserved.
 * -----------------------------------------------------------------
 */

public class FocusScaleHelper {


    public static boolean isValidZoomIndex(int zoomIndex) {
        return zoomIndex == ZOOM_FACTOR_NONE || getResId(zoomIndex) > 0;
    }

    public static int getResId(int zoomIndex) {
        switch (zoomIndex) {
            case ZOOM_FACTOR_SMALL:
                return R.fraction.lb_focus_zoom_factor_small;
            case ZOOM_FACTOR_XSMALL:
                return R.fraction.lb_focus_zoom_factor_xsmall;
            case ZOOM_FACTOR_MEDIUM:
                return R.fraction.lb_focus_zoom_factor_medium;
            case ZOOM_FACTOR_LARGE:
                return R.fraction.lb_focus_zoom_factor_large;
            default:
                return 0;
        }
    }

    public static class ItemFocusScale implements FocusHighlightHandler {
        private static final int DURATION_MS = 150;

        private int mScaleIndex;

        public ItemFocusScale(int zoomIndex) {
            if (!isValidZoomIndex(zoomIndex)) {
                throw new IllegalArgumentException("Unhandled zoom index");
            }
            mScaleIndex = zoomIndex;
        }

        private float getScale(Resources res) {
            return mScaleIndex == ZOOM_FACTOR_NONE ? 1f :
                    res.getFraction(getResId(mScaleIndex), 1, 1);
        }

        @Override
        public void onItemFocused(View view, boolean hasFocus) {
            getOrCreateAnimator(view).animateFocus(hasFocus, false);
        }

        @Override
        public void onInitializeView(View view) {
            getOrCreateAnimator(view).animateFocus(false, true);
        }

        private FocusAnimator getOrCreateAnimator(View view) {
            FocusAnimator animator = (FocusAnimator) view.getTag(R.id.lb_focus_animator);
            if (animator == null) {
                animator = new FocusAnimator(
                        view, getScale(view.getResources()), DURATION_MS);
                view.setTag(R.id.lb_focus_animator, animator);
            }
            return animator;
        }

    }

    public static class FocusAnimator implements TimeAnimator.TimeListener {
        private final View mView;
        private final int mDuration;
        private final float mScaleDiff;
        private float mFocusLevel = 0f;
        private float mFocusLevelStart;
        private float mFocusLevelDelta;
        private final TimeAnimator mAnimator = new TimeAnimator();
        private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

        public void animateFocus(boolean select, boolean immediate) {
            endAnimation();
            final float end = select ? 1 : 0;
            if (immediate) {
                setFocusLevel(end);
            } else if (mFocusLevel != end) {
                mFocusLevelStart = mFocusLevel;
                mFocusLevelDelta = end - mFocusLevelStart;
                mAnimator.start();
            }
        }

        public FocusAnimator(View view, float scale, int duration) {
            mView = view;
            mDuration = duration;
            mScaleDiff = scale - 1f;
            mAnimator.setTimeListener(this);
        }

        public void setFocusLevel(float level) {
            mFocusLevel = level;
            float scale = 1f + mScaleDiff * level;
            mView.setScaleX(scale);
            mView.setScaleY(scale);
        }

        float getFocusLevel() {
            return mFocusLevel;
        }

        void endAnimation() {
            mAnimator.end();
        }

        @Override
        public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
            float fraction;
            if (totalTime >= mDuration) {
                fraction = 1;
                mAnimator.end();
            } else {
                fraction = (float) (totalTime / (double) mDuration);
            }
            if (mInterpolator != null) {
                fraction = mInterpolator.getInterpolation(fraction);
            }
            setFocusLevel(mFocusLevelStart + fraction * mFocusLevelDelta);
        }
    }
}
