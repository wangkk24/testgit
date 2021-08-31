package com.pukka.ydepg.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.pukka.ydepg.common.extview.ImageViewExt;
import com.pukka.ydepg.common.screensaver.view.ScreensaverDialog;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.util.AuthenticateManager;

public class GlideUtil {

    private static final String RESOURCE = "resource";

    private static final OTTRequestListener listener = new OTTRequestListener();

    public static void load(Context context, String url, RequestOptions options, GlideCallBack callBack, ImageView imageView){
        if (context instanceof FragmentActivity && !((FragmentActivity) context).isDestroyed()){
            listener.setGlideCallBack(callBack);
            Glide.with(context).load(url).apply(options).listener(listener).into(imageView);
        }
    }

    public static void load(Fragment fragment, String url, RequestOptions options, GlideCallBack callBack, ImageView imageView){
        listener.setGlideCallBack(callBack);
        Glide.with(fragment).load(url).apply(options).listener(listener).into(imageView);
    }

    public interface GlideCallBack{
        void onLoadFailed(GlideException e,Object model);
        void onResourceReady();
    }

    public static class OTTRequestListener implements RequestListener<Drawable> {

        private GlideCallBack callBack;

        void setGlideCallBack(GlideCallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            if( callBack != null ){
                callBack.onLoadFailed(e,model);
            }
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

            //2.4尝试解决首页：Canvas: trying to use a recycled bitmap android.graphics.Bitmap@314cf670问题
            if (resource instanceof BitmapDrawable && ((BitmapDrawable)resource).getBitmap().isRecycled()){
                SuperLog.debug("GlideUtil","Drawable Bitmap isRecycled = true");
                return true;
            }

            if( callBack != null ){
                callBack.onResourceReady();
            }
            return false;
        }
    }

    public static void removeIvOfRl(RelativeLayout rlNav){
        for (int i = 1; i < rlNav.getChildCount(); i++) {
            View view = rlNav.getChildAt(i);
            if (view instanceof ImageViewExt) {
                //移除
                rlNav.removeViewAt(i);
            }
        }
    }

    //使用PHS下发的json,图片路径前需要拼接"/"
    public static String getUrl(String url){
        if (!TextUtils.isEmpty(url) && url.contains(RESOURCE) && url.substring(0,8).equalsIgnoreCase(RESOURCE) ){
            return "/" + url;
        }else{
            return url;
        }
    }

    public static String getUrlForJsonPicAddress(String contentURL){
        String launcherLink = SharedPreferenceUtil.getInstance().getLauncherLink();
        if (!TextUtils.isEmpty(launcherLink)) {
            contentURL = "http://" + AuthenticateManager.getInstance().getUserInfo()
                    .getIP() + ":" + AuthenticateManager.getInstance().getUserInfo()
                    .getPort() + launcherLink + getUrl(contentURL);
        }else{
            SuperLog.info2SD("GlideUtil","launcherLink=null,加载组件标题图片失败");
        }
        return contentURL;
    }

    public static String getPHSImageUrl(){
        if (null != AuthenticateManager.getInstance().getUserInfo()) {
            return "http://" + AuthenticateManager.getInstance().getUserInfo().getIP()
                       + ":" + AuthenticateManager.getInstance().getUserInfo().getPort();
        } else {
            return "";
        }
    }

    //加载高清大图节约内存的Glide封装
    public static void load(Context context,String url,ImageView imageView,int errorResId){
        //如果imageView的尺寸为match_parent, 则通过getLayoutParams()获取到的宽高为-1
        //如果imageView不可见,               则通过imageView.getWidth()获取到的宽高为0
        int width  = imageView.getWidth()<=0? imageView.getLayoutParams().width :imageView.getWidth();
        int height = imageView.getHeight()<=0? imageView.getLayoutParams().height :imageView.getHeight();

        RequestOptions options = new RequestOptions().override(width,height);
        if(errorResId != -1){
            options = options.error(errorResId);
        }
        //使用CustomTarget加载来防止OOM done by weicy
        Glide.with(context)
            .load(url)
            .apply(options)
            .into(new CustomTarget<Drawable>(width,height) {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    imageView.setImageDrawable(resource);
                }

                @Override public void onLoadCleared(@Nullable Drawable placeholder) { }
            });
    }

    //加载高清大图节约内存的Glide封装
    public static void load(Context context, String url, ImageView imageView, int errorResId, OnGlideResourceReady resourceReadyListener){
        //如果imageView的尺寸为match_parent, 则通过getLayoutParams()获取到的宽高为-1
        //如果imageView不可见,               则通过imageView.getWidth()获取到的宽高为0
        int width  = imageView.getWidth()<=0? imageView.getLayoutParams().width :imageView.getWidth();
        int height = imageView.getHeight()<=0? imageView.getLayoutParams().height :imageView.getHeight();

        RequestOptions options = new RequestOptions().override(width,height);
        if(errorResId != -1){
            options = options.error(errorResId);
        }
        //使用CustomTarget加载来防止OOM done by weicy
        Glide.with(context)
            .load(url)
            .apply(options)
            .into(new CustomTarget<Drawable>(width,height) {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    imageView.setImageDrawable(resource);
                    if(resourceReadyListener != null){
                        resourceReadyListener.onReady();
                    }
                }

                @Override public void onLoadCleared(@Nullable Drawable placeholder) {}
            });
    }

    //节点指引加载图片
    @SuppressLint("CheckResult")
    public static void loadForResourceReadyEpgGuide(Context context, String url, ImageView imageView, int errorResId, OnGlideResourceReady resourceReadyListener) {

        RequestOptions options = new RequestOptions();
        if (errorResId != -1) {
            options = options.error(errorResId);
        }
        //使用CustomTarget加载来防止OOM done by weicy
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        SuperLog.info2SDDebug("MainActivity","loadBackBg___loadBackGroup()__onResourceReady,url="+url);
                        imageView.setImageDrawable(resource);
                        if (resourceReadyListener != null) {
                            resourceReadyListener.onReady();
                        }
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        SuperLog.info2SD("MainActivity","loadBackBg___loadBackGroup()__onLoadCleared,url="+url);
                    }
                });
    }

    //加载高清大图节约内存的Glide封装
    @SuppressLint("CheckResult")
    public static void loadForResourceReady(Context context, String url, ImageView imageView, int errorResId, OnGlideResourceReady resourceReadyListener) {

        RequestOptions options = new RequestOptions();
        if (errorResId != -1) {
            options = options.error(errorResId);
        }
        //使用CustomTarget加载来防止OOM done by weicy
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        SuperLog.info2SDDebug("MainActivity","loadBackBg___loadBackGroup()__onResourceReady,url="+url);
                        if (context instanceof MainActivity && url.equalsIgnoreCase(((MainActivity)context).getBgUrl())){
                            imageView.setImageDrawable(resource);
                        }
                        if (resourceReadyListener != null) {
                            resourceReadyListener.onReady();
                        }
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        SuperLog.info2SD("MainActivity","loadBackBg___loadBackGroup()__onLoadCleared,url="+url);
                    }
                });
    }

    //加载高清大图节约内存的Glide封装
    @SuppressLint("CheckResult")
    public static void loadForResourceReady(Context context, int resId, ImageView imageView, int errorResId, OnGlideResourceReady resourceReadyListener) {

        RequestOptions options = new RequestOptions();
        if (errorResId != -1) {
            options = options.error(errorResId);
        }
        //使用CustomTarget加载来防止OOM done by weicy
        Glide.with(context)
                .load(resId)
                .apply(options)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        if (context instanceof MainActivity && "1".equalsIgnoreCase(((MainActivity)context).getBgUrl())){
                            imageView.setImageDrawable(resource);
                        }
                        if (resourceReadyListener != null) {
                            resourceReadyListener.onReady();
                        }
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) { }
                });
    }

    public interface OnGlideResourceReady{
        void onReady();
    }
}