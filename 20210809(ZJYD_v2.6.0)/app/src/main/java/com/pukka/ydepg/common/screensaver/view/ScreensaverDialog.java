package com.pukka.ydepg.common.screensaver.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.screensaver.ScreenConstant;
import com.pukka.ydepg.common.screensaver.ScreenContract;
import com.pukka.ydepg.common.screensaver.model.ScreenAdvertContent;
import com.pukka.ydepg.common.utils.GlideUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

public class ScreensaverDialog extends Dialog implements ScreenContract.IView {

    private ImageView imageBanner;

    private ImageView imageFlag;

    //支持跳转的屏保右下键展示的操作提示文字
    private ImageView imageClick;

    private ScreenContract.IPresenter screenPresenter;

    private ScreenAdvertContent advertContent;

    private Handler handler = new Handler(Looper.getMainLooper());

    private Runnable runnable = new SwitchScreenSaverRunnable();

    private boolean isFirstShow = true;

    public ScreensaverDialog(@NonNull Context context, ScreenContract.IPresenter presenter) {
        //使用Style创建Dialog,风格:全屏/
        super(context,R.style.dialog_screensaver);
        screenPresenter = presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_screensaver);

        initView();
        //初始化完成后会回调onWindowFocusChanged(true),启动屏保循环播放,无需手动调用
        //showSaverBanner(screenPresenter.getNextAdvert());
    }

    private void initView() {
        // 让Dialog全屏显示
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);

        imageFlag   = findViewById(R.id.advert_flag);
        imageClick  = findViewById(R.id.advert_click);
        imageBanner = findViewById(R.id.banner);
        imageBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击广告跳转
                //Tester.test(12);//12就是KeyEvent.KEYCODE_5
                screenPresenter.gotoAdPage(advertContent);
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //屏保结束
                handler.removeCallbacks(runnable); //关闭屏保轮播图片
                screenPresenter.onSaverDismiss();
            }
        });
    }

    //系统SDK原生回调
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            if(isFirstShow){
                isFirstShow = false;
                //Dialog创建并显示的时候会获取焦点,启动时立即展示第一张屏保,并启动循环
                showSaverBanner(screenPresenter.getNextAdvert());
            } else {
                //从设置页面返回,重新计时后展示下一张
                handler.postDelayed(runnable,screenPresenter.getBannerInterval());
            }
        } else {
            //按设置键会失去焦点时停止图片循环(关闭dialog并不会走此方法)
            handler.removeCallbacks(runnable);
        }
    }

    private class SwitchScreenSaverRunnable implements Runnable{
        @Override
        public void run() {
            //展示下一张屏保广告
            showSaverBanner(screenPresenter.getNextAdvert());
        }
    }

    //ScreenContract.IView中接口S
    @Override
    public void showSaverBanner(ScreenAdvertContent advertContent) {
        if(advertContent == null || TextUtils.isEmpty(advertContent.getBannerUrl())){
            this.close();
        }
        SuperLog.debug(ScreenConstant.TAG,"Show next screensaver banner");
        //延时(配置的时间)后展示下一张屏保广告
        handler.postDelayed(runnable,screenPresenter.getBannerInterval());
        //使用view.post()方式才能正确的获取到View的宽高,Glide加载图片时使用宽高裁剪,可以避免图片过大导致的OOM
        imageBanner.post(new Runnable() {
            @Override
            public void run() {
                if( isActivityDestroyed() ){
                    return;
                }
                //通过回调方式加载图片能保证图片展示实时性,防止网络图片异步加载慢,和广告信息不同步
                GlideUtil.load(getContext(), advertContent.getBannerUrl(), imageBanner, -1, new GlideUtil.OnGlideResourceReady() {
                    @Override
                    public void onReady() {
                        //SSP平台广告要展示广告标签,否则不展示
                        if(ScreenAdvertContent.TYPE_SSP.equals(advertContent.getType())){
                            imageFlag.setVisibility(View.VISIBLE);
                        } else {
                            imageFlag.setVisibility(View.GONE);
                        }
                        imageBanner.requestFocus();
                        ScreensaverDialog.this.advertContent = advertContent;//更新广告内容,保证跳转内容和广告展示内容一致对应
                        //支持跳转的广告要展示操作提示
                        if(screenPresenter.isSupportClick(advertContent)){
                            imageClick.setVisibility(View.VISIBLE);
                        } else {
                            imageClick.setVisibility(View.GONE);
                        }


                        //通知Presenter当前图片已展示
                        screenPresenter.onShowSingleAdvert(advertContent);
                    }
                });
            }
        });
    }

    private boolean isActivityDestroyed(){
        //通过主题创建的Dialog,其内部保存的context对象不是构造函数传入的,具体可以参见Dialog构造函数源码
        if( getContext() instanceof ContextWrapper ){
            Context context = ((ContextWrapper) getContext()).getBaseContext();
            if(context instanceof Activity){
                Activity activity = (Activity) context;
                return activity.isDestroyed() || activity.isFinishing();
            }
        }
        return false;
    }

    //ScreenContract.IView中接口
    @Override
    public void open(Context context) {
        SuperLog.info2SD(ScreenConstant.TAG,"Start to show screensaver. Context="+context.getClass().getSimpleName());
        this.show();
        //->onCreate
    }

    //ScreenContract.IView中接口
    //通过setOnDismissListener(new OnDismissListener()会回调presenter中的onSaverDismiss()
    @Override
    public void close() {
        this.dismiss();
    }

    @Override
    public boolean isShow() {
        return isShowing();
    }
}