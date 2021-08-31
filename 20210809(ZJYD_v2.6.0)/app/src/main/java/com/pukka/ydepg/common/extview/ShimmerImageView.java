package com.pukka.ydepg.common.extview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.utils.CornersTransform;
import com.pukka.ydepg.common.utils.GlideUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.animatorUtil.AnimatorUtil;
import com.pukka.ydepg.event.ShimmerCloseEvent;
import com.pukka.ydepg.moudule.search.utils.ScreenUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by guanwp on 19/1/21.
 */
public class ShimmerImageView extends AppCompatImageView {
    private Paint mPaint;
    private int mDx;
    private LinearGradient mLinearGradient;
    private ValueAnimator animator;
    private int[] colors = new int[]{0x00ffffff,0x99FFFFFF,0x00ffffff};
    private float[] colorSizes = new float[]{0.8f,0.9f,1f};
    private boolean canStartShimmer = false;

    private static final String TAG = ShimmerImageView.class.getSimpleName();

    private Context context;

    private String imageUrl = "";
    private boolean isLoad = true;

    private boolean isRecommend = false;

    public ShimmerImageView(Context context) {
        super(context);
        init(context);
    }

    public ShimmerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShimmerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setImageResourcesUrl(String imageUrl,boolean isLoad){
        this.imageUrl = imageUrl;
        this.isLoad = isLoad;
    }

    public void setIsRecommend(boolean isRecommend){
        this.isRecommend = isRecommend;
    }

    private void init(Context context){
        this.context = context;
        mPaint =new Paint();
        setScaleType(ScaleType.FIT_XY);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mLinearGradient = new LinearGradient(0, getMeasuredHeight() / 2, - getMeasuredHeight(), 0, colors,
                colorSizes,
                Shader.TileMode.CLAMP
        );
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!TextUtils.isEmpty(imageUrl) && isLoad){
            SuperLog.debug(TAG,"onAttachedToWindow_GlideUtil_loadUrl");
            RequestOptions options  = new RequestOptions().placeholder(context.getResources().getDrawable(R.drawable.default_poster_bg));
            options = options.transform(new CornersTransform(ScreenUtil.getDimensionF(context, R.dimen.my_moreItem_radius)));
            GlideUtil.load(context, imageUrl,options,null,this);
        }else{
            isLoad = true;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
        if (!isRecommend){
            try{
                //if(CommonUtil.getDeviceType().contains("M301H")){
                //解决M301H内存OOM问题
                Glide.with(getContext()).clear(this);
                //}
            } catch (Exception e){
                //TODO
                //No need to do anything here
                //解决 GlideApp.with(getContext()).clear(this);这句代码出现问题：
                // java.lang.IllegalArgumentException: You cannot start a load for a destroyed activity
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try{
            super.onDraw(canvas);
            if(canStartShimmer && mDx != 0) {

                Matrix matrix = new Matrix();
                matrix.setTranslate(mDx, 0);
                mLinearGradient.setLocalMatrix(matrix);
                mPaint.setShader(mLinearGradient);
                canvas.drawPaint(mPaint);
            }
        }catch (Exception e){
            SuperLog.error("ShimmerImageView",e);
        }
    }

    @Subscribe
    public void onEvent(ShimmerCloseEvent event) {
        stopShimmer();
    }

    public void startShimmer(){
        AnimatorUtil.resetDurationScaleIfDisable();
        canStartShimmer = true;

        animator = ValueAnimator.ofInt(0, getMeasuredWidth() + getMeasuredHeight() / 2 * 3);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(null!=animation&&null!=animation.getAnimatedValue()) {
                    mDx = (Integer) animation.getAnimatedValue();
                }
                postInvalidate();
            }
        });
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(0);

        animator.setDuration((getMeasuredWidth() + getMeasuredHeight() ) * 5000 / 1080 );
        animator.start();
    }

    public void stopShimmer(){
        if(animator != null) {
            animator.cancel();
            mDx = 0;
            postInvalidate();
        }
    }

    public boolean isCanStartShimmer() {
        return canStartShimmer;
    }

    public void setCanStartShimmer(boolean canStartShimmer) {
        this.canStartShimmer = canStartShimmer;
    }
}