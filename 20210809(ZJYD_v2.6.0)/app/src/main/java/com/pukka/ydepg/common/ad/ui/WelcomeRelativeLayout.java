package com.pukka.ydepg.common.ad.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.ad.AdConstant;
import com.pukka.ydepg.common.ad.AdManager;
import com.pukka.ydepg.common.ad.AdUtil;
import com.pukka.ydepg.common.ad.data.PicAdData;
import com.pukka.ydepg.common.extview.ImageViewExt;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertContent;
import com.pukka.ydepg.common.start.StartPictureManager;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.view.loadingball.MonIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//广告布局
public class WelcomeRelativeLayout extends RelativeLayout {
    private static final String TAG  = "WelcomeRelativeLayout";
    public final String TYPE_DISPLAY = "0";
    public final String TYPE_VIDEO   = "1";
    public final String TYPE_NULL    = "-1";

    Context context;

    private String showType = "-1";

    //图片广告
    private ImageViewExt imageViewExt;
    private ImageViewExt imageViewExtFront;

    //视频广告布局
    RelativeLayout playLayout;

    //视频广告
    private VideoView playView;

    //是否展示倒计时
    private boolean isShowCount;

    protected MonIndicator mLoadingBar;

    private TextView skipCountText;

    private LinearLayout countDownlayout;

    private TextView countDownText;

    private ImageView adText;

    private boolean isfirst = true;

    //是否可以跳过
    private boolean canSkip = false;

    //是否正在加载launcher广告
    private boolean isloadAd = false;

    //图片广告资源列表
    List<AdvertContent> advertContent = new ArrayList<>();

    //当前正在展示的广告
    AdvertContent nowAd;

    //视频广告回调
    PlayerListener playlistener;

    //图片展示回调
    CompleteListener listener;

    //倒计时时间
    private long countTime;

    //跳过倒计时时间
    private long skipCountTime = -10;

    //每张图片的展示时间
    private long countTimeEach;

    //广告观看后允许跳过的时间
    private long canSkipInterval = 3;

    private static final int  LOOP_FLAG =10000;

    private static final int  LOOP_FLAG_SKIP =20000;

    private static final int  LOOP_FLAG_PLAY =30000;

    private static final int  GET_VIDEO_DURATION =40000;

    public WelcomeRelativeLayout(Context context) {
        super(context);
        init(context,null);
    }

    public WelcomeRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public WelcomeRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOOP_FLAG:{
                    //展示倒计时
                    countTime--;
                    skipCountTime--;
                    if (countTime > 0){
                        if (!isfirst){
                            sendEmptyMessageDelayed(LOOP_FLAG,1000);
                        }
                    }else{
                        isloadAd = false;
                        complete();
                    }

                    //每个countTimeEach的间隔，更换广告
                    if (countTime == countTimeEach * advertContent.size() && countTime != 0){
                        SuperLog.info2SD(AdConstant.TAG,"Begin to load SSP banner AD(This progress may be looped sometimes).");
                        if (advertContent.size()>0){
                            nowAd = advertContent.get(0);
                            if (null != nowAd.getDisplay() && null != nowAd.getDisplay().getBanner() && !TextUtils.isEmpty(nowAd.getDisplay().getBanner().getImg())) {
                                //图片展示完之后展示标识
                                loadPicWithoutPlaceHolder(nowAd.getDisplay().getBanner().getImg(), new RequestListener() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                                        if (countDownlayout.getVisibility() != VISIBLE && adText.getVisibility() != VISIBLE){
                                            if (isShowCount){
                                                countDownText.setText(String.format("%1$ss    ", countTime));
                                            }
                                            if (isShowCount){
                                                countDownlayout.setVisibility(VISIBLE);
                                                adText.setVisibility(GONE);
                                            }else{
                                                countDownlayout.setVisibility(GONE);
                                                adText.setVisibility(VISIBLE);
                                            }
                                            if (isfirst){
                                                sendEmptyMessageDelayed(LOOP_FLAG,1000);
                                                isfirst = false;
                                            }
                                            skipCountText.setVisibility(VISIBLE);
                                            skipCountTime = canSkipInterval;
                                            skipCountText.setText(String.format("%1$ss后可以跳过", skipCountTime));
                                        }
                                        return false;
                                    }
                                });
                                //上报海报广告曝光
                                AdManager.getInstance().reportAdvert(nowAd, AdConstant.AdType.BANNER, AdConstant.ReportActionType.IMPRESSION);
                                advertContent.remove(0);
                            }else{
                                //数据错误，直接结束
                                isloadAd = false;
                                complete();
                            }
                        }else{
                            isloadAd = false;
                            //没有更多广告，结束
                            complete();
                        }
                    }

                    if (skipCountTime != -10 && skipCountTime != -11){

                        if (isShowCount){
                            countDownText.setText(String.format("%1$ss    ", countTime));
                        }

                        if (skipCountTime > 0){
                            skipCountText.setText(String.format("%1$ss后可以跳过", skipCountTime));
                        }else{
                            skipCountText.setText("按返回键跳过");
                            canSkip = true;
                        }

                    }
                    break;
                }

                //视频广告倒计时
                case LOOP_FLAG_PLAY: {

                    SuperLog.info2SD(AdConstant.TAG, "Begin to load SSP VIDEO AD(This progress may be looped sometimes).");
                    if (playView.getDuration() != 0 && playView.getDuration() != -1) {
                        if (playView.getCurrentPosition() == 0) {
                            countTime = (long) playView.getDuration() / 1000;
                        } else {
                            countTime = (long) playView.getDuration() / 1000 - playView.getCurrentPosition() / 1000;
                        }
                        if (countTime > 0) {
                            //展示倒计时
                            if (isShowCount) {
                                countDownText.setText(String.format("%1$ss     ", countTime));
                            }
                            sendEmptyMessageDelayed(LOOP_FLAG_PLAY, 200);
                        } else {
                            if (null != playlistener) {
                                playlistener.playComplete();
                            }
                        }
                        break;
                    }
                }



                //重新获取视频时长
                case GET_VIDEO_DURATION:{
                    if (countTime == -1){
                        //时长不为0
                        if (playView.getDuration() != 0){
                            String showStr = SessionService.getInstance().getSession().getTerminalConfigurationValue(AdConstant.BANNER_ISSHOW_COUNT);
                            imageViewExtFront.setVisibility(GONE);
                            if (null != showStr && showStr.equals("1")){
                                isShowCount = true;
                            }
                            if (isShowCount){
                                countDownlayout.setVisibility(VISIBLE);
                                adText.setVisibility(GONE);
                                //开始倒计时
                                mHandler.sendEmptyMessage(LOOP_FLAG_PLAY);
                            }else{
                                countDownlayout.setVisibility(GONE);
                                adText.setVisibility(VISIBLE);
                            }
                            skipCountTime = canSkipInterval;
                            skipCountText.setVisibility(VISIBLE);
                            skipCountText.setText(String.format("%1$ss后可以跳过", skipCountTime));
                            mHandler.sendEmptyMessageDelayed(LOOP_FLAG_SKIP,1000);
                        }
                    }
                    break;
                }

                //展示跳过
                case LOOP_FLAG_SKIP:{
                    skipCountTime--;
                    skipCountText.setVisibility(VISIBLE);
                    if (skipCountTime > 0){
                        skipCountText.setText(String.format("%1$ss后可以跳过", skipCountTime));
                        sendEmptyMessageDelayed(LOOP_FLAG_SKIP,1000);
                    }else{
                        skipCountText.setText("按返回键跳过");
                        canSkip = true;
                    }
                    break;
                }
            }
        }
    };

    private void init(Context context,AttributeSet attrs){
        this.context = context;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.layout_welcome, this, true);

        this.imageViewExt  = findViewById(R.id.startup_image_view);
        this.imageViewExtFront = findViewById(R.id.startup_image_view_front);
        loadDrawable(null);

        this.playLayout    = findViewById(R.id.welcome_play_layout);

        this.mLoadingBar   = findViewById(R.id.pb_loading_play);

        this.skipCountText      = findViewById(R.id.text_skip_count);
        countDownlayout    = findViewById(R.id.text_count_layout);
        this.countDownText = findViewById(R.id.text_count_count);
        this.adText        = findViewById(R.id.text_ad);
        this.playView      = findViewById(R.id.playview_welcome_play);
        playView.setBackground(imageViewExt.getDrawable());//解决视频资源加载过程中黑屏问题
        playView.setMediaController(null);
        playView.setZOrderOnTop(true);
        playView.setZOrderMediaOverlay(true);

        //获取允许跳过的间隔时间
        canSkipInterval = AdUtil.getSkipTime(AdConstant.AdClassify.START);

        playView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                SuperLog.info2SD(AdConstant.TAG,"VIDEO AD PLAY COMPLETE");
                playlistener.onComplete();
            }
        });
        playView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                SuperLog.info2SD(AdConstant.TAG,"VIDEO AD PLAY ERROR");
                playComplete();
                if (null != playlistener){
                    return playlistener.onError();
                }
                return false;
            }
        });
        playView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                skipCountTime = canSkipInterval;
                if (countTime == -1  && playView.getDuration() != -1){
                    if (playView.getDuration() != 0){
                        SuperLog.info2SD(AdConstant.TAG,"PERPARE TO PLAY VIDEO AD , get Duration Success");
                        countTime = (long) playView.getDuration()/1000;
                    }else{
                        SuperLog.info2SD(AdConstant.TAG,"PERPARE TO PLAY VIDEO AD , get Duration fail");
                        countTime = -1;
                        mHandler.sendEmptyMessageDelayed(GET_VIDEO_DURATION,1000);
                    }

                    if (countTime != -1){
                        Log.i(AdConstant.TAG, "onPrepared: 开始倒计时");
                        //获取到了视频时长，开始倒计时
                        isShowCount = AdUtil.isShowAdTime(AdConstant.AdClassify.START);

                        imageViewExtFront.setVisibility(GONE);

                        if (isShowCount){
                            countDownlayout.setVisibility(VISIBLE);
                            adText.setVisibility(GONE);
                            //开始倒计时
                            mHandler.sendEmptyMessage(LOOP_FLAG_PLAY);
                        }else{
                            countDownlayout.setVisibility(GONE);
                            adText.setVisibility(VISIBLE);
                            mHandler.sendEmptyMessage(LOOP_FLAG_PLAY);
                        }

                        //开始倒计时
                        skipCountText.setVisibility(VISIBLE);
                        skipCountText.setText(String.format("%1$ss后可以跳过", skipCountTime));
                        mHandler.sendEmptyMessageDelayed(LOOP_FLAG_SKIP,1000);
                    }
                }

                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START && mLoadingBar != null) {
                            mLoadingBar.setVisibility(View.VISIBLE);
                        }
                        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END && mLoadingBar != null) {
                            mLoadingBar.setVisibility(View.GONE);
                        }

                        return true;
                    }
                });

                if (null != playlistener){
                    //播放器准备完成,隐藏开机图片
                    //imageViewExtFront.setVisibility(GONE);
                    playlistener.onPrepare();
                }
            }
        });



    }

    //去除默认图，避免加载过程中出现默认图
    public void loadPicWithoutPlaceHolder(String url ,RequestListener listener){
        if (null != imageViewExt){
            //options默认图片解决加载网络广告图片过程中的黑屏现象
            RequestOptions options = new RequestOptions().placeholder(imageViewExt.getDrawable());
            Glide.with(context).load(url).apply(options).listener(listener).into(imageViewExt);
        }
    }

    public void loadDrawable(Drawable drawable){
        if (null != imageViewExt){
            if(drawable == null){
                //imageViewExt.setImageDrawable(drawable);
                StartPictureManager.loadStartPicture(context,imageViewExt);
                StartPictureManager.loadStartPicture(context,imageViewExtFront);
            } else {
                imageViewExt.setImageDrawable(drawable);
                imageViewExtFront.setImageDrawable(drawable);
            }
        }
    }

    //加载广告
    public void loadAd(List<AdvertContent> advertContent) {
        //开机广告不会混排,[视频/图片]二选一,以第一个广告资源的类型来判断开机广告类型,AdvertContent在接口响应处理中已经做过判空,此处直接使用即可
        AdvertContent content = advertContent.get(0);
        if (AdvertContent.AdvertType.DISPLAY.equals(content.getAdType())) {
            //图片广告(支持多张图片)
            SuperLog.info2SD(AdConstant.TAG,"Begin to load SSP [BANNER] ad, Banner number = " + advertContent.size());
            loadAds(advertContent);
        } else  { //非图片即视频广告,其他异常场景已经在AdManager中过滤,应用侧无需考虑
            String adURL = content.getVideo().getCurl();
            SuperLog.info2SD(AdConstant.TAG,"Begin to load SSP [VIDEO] ad,  Video URL = " + adURL);
            playAd(adURL,content);
        }
    }

    //加载图片广告
    private void loadAds(List<AdvertContent> advertContent){
        if (AdManager.getInstance().isPlayComplete()){
            return;
        }
        countTime = canSkipInterval;
        AdManager.getInstance().setPlayComplete(true);

        //先影藏布局，防止切换账号时展示异常
        countDownlayout.setVisibility(GONE);
        skipCountText.setVisibility(GONE);
        adText.setVisibility(GONE);

        showType = TYPE_DISPLAY;
        isloadAd = true;
        canSkip = false;
        this.advertContent.clear();
        this.advertContent.addAll(advertContent);
        String duration = SessionService.getInstance().getSession().getTerminalConfigurationValue(AdConstant.BANNER_DURATION);
        isShowCount = AdUtil.isShowAdTime(AdConstant.AdClassify.START);

        if (null == duration || duration.length() == 0){
            duration = "5";
        }
        try {
            countTimeEach = Long.parseLong(duration);
        }catch (Exception e){
            e.printStackTrace();
        }
        countTime = countTimeEach * advertContent.size() + 1;

        this.nowAd = this.advertContent.get(0);
        //开始倒计时
        mHandler.sendEmptyMessage(LOOP_FLAG);
//        //3秒后展示跳过
    }


    //图片广告点击事件
    public void Adclick(){
        PicAdData data = new PicAdData(nowAd, AdUtil.getClickUrl(nowAd));
        String clickURL = data.getClickUrl();
        if(!TextUtils.isEmpty(clickURL)){
            SuperLog.info2SD(TAG,"SSP AD pic click Url : " + clickURL);
            AdManager.getInstance().startAdPage(context,data.getContent(),AdConstant.AdType.BANNER,clickURL);
        }
    }


    //加载视频广告
    private void playAd(String path,AdvertContent content){
        imageViewExtFront.setVisibility(View.VISIBLE);//视频缓冲过程中前置开机图片,防止黑屏
        playLayout.setVisibility(View.VISIBLE);
        playLayout.setAlpha(1.0f);
        playView.setVisibility(View.VISIBLE);
        playView.setAlpha(1.0f);

        if (AdManager.getInstance().isPlayComplete()){
            return;
        }
        AdManager.getInstance().setPlayComplete(true);
        playView.setZOrderOnTop(true);
        playView.setZOrderMediaOverlay(true);
        SuperLog.info2SD(AdConstant.TAG,"start to play video ad");
        //先影藏布局，防止切换账号时展示异常
        countDownlayout.setVisibility(GONE);
        skipCountText.setVisibility(GONE);
        adText.setVisibility(GONE);

        showType = TYPE_VIDEO;
        isloadAd = true;
        canSkip = false;
        playView.setVideoPath(path);
        playView.start();
        //上报视频广告曝光
        AdManager.getInstance().reportAdvert(content, AdConstant.AdType.VIDEO,AdConstant.ReportActionType.IMPRESSION);

        countTime = -1;
    }

    public void clearUI(){
        countDownlayout.setVisibility(GONE);
        skipCountText.setVisibility(GONE);
        adText.setVisibility(GONE);

        isloadAd = false;
        canSkip = false;
        countTimeEach = 0;
        countTime = 0;
        loadDrawable(null);
        mHandler.removeCallbacksAndMessages(null);
    }

    public VideoView getPlayView() {
        return playView;
    }

    public RelativeLayout getPlayLayout() {
        return playLayout;
    }

    @Override
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
        playView.setAlpha(alpha);
    }

    public void setListener(CompleteListener listener) {
        this.listener = listener;
    }

    public boolean isCanSkip() {
        return canSkip;
    }

    public boolean isLoadAd() {
        return isloadAd;
    }

    public void complete(){

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                countDownlayout.setVisibility(GONE);
//                skipCountText.setVisibility(GONE);
//                adText.setVisibility(GONE);
//            }
//        },100);
        countDownlayout.setVisibility(GONE);
        skipCountText.setVisibility(GONE);
        adText.setVisibility(GONE);

        isloadAd = false;
        canSkip = false;
        countTimeEach = 0;
        countTime = 0;
        showType = TYPE_NULL;
        if (null != listener){
            listener.complete();
        }
        //清除加载的图片
        Glide.with(context).load("").into(imageViewExt);
        imageViewExt.setVisibility(INVISIBLE);
        mHandler.removeMessages(LOOP_FLAG);
        mHandler.removeMessages(LOOP_FLAG_PLAY);
        mHandler.removeCallbacksAndMessages(null);

        //手动回收播放器
        playView.stopPlayback();
    }

    public void playComplete(){

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                countDownlayout.setVisibility(GONE);
//                skipCountText.setVisibility(GONE);
//                adText.setVisibility(GONE);
//            }
//        },100);
        countDownlayout.setVisibility(GONE);
        skipCountText.setVisibility(GONE);
        adText.setVisibility(GONE);
        //清除播放器
        //playView.setVideoPath("");//cm201z盒子这里会报错
        playView.setAlpha(0);

        showType = TYPE_NULL;
        isloadAd = false;
        canSkip = false;
        countTimeEach = 0;
        countTime = 0;
        mHandler.removeMessages(LOOP_FLAG);
        mHandler.removeMessages(LOOP_FLAG_PLAY);
        mHandler.removeCallbacksAndMessages(null);

        //手动回收播放器
        playView.stopPlayback();
    }

    public String getShowType() {
        return showType;
    }

    public interface CompleteListener{
        void complete();
    }

    public void setPlayListener(PlayerListener listener) {
        this.playlistener = listener;
    }

    public interface PlayerListener {
        void    onPrepare();
        void    onComplete();
        void    playComplete();
        boolean onError();
    }

    public void frontGone(){
        imageViewExtFront.setVisibility(GONE);
    }

}