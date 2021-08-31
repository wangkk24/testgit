package com.pukka.ydepg.common.ad.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.ad.AdConstant;
import com.pukka.ydepg.common.ad.AdUtil;
import com.pukka.ydepg.view.PlayView;


//倒计时布局，仅实现倒计时和展示跳过字条的功能，跳过逻辑自行实现
public class CountDownRelativeLayouyt extends RelativeLayout {

    Context context;

    //跳过广告
    private TextView skipCountText;

    //倒计时布局
    private LinearLayout countDownlayout;

    //倒计时
    private TextView countDownText;

    //广告
    private ImageView adText;

    //回调
    private completeListener listener;

    //跳过广告字条是否展示，是否可以跳过广告
    private boolean canSkip = false;

    //是否需要展示跳过，默认需要
    private boolean needShowSkip = true;

    //是否需要展示倒计时，默认需要，不展示倒计时时仅展示"广告"字样
    private boolean needShowCountDown = true;

    //播放开始多少秒之后展示跳过，默认3秒，设置错误值时使用3秒
    private long SkipTime = 3;

    //用于计时的时间
    private long skipCountTime;

    //倒计时时间
    private long countTime = -10;

    private static final int  LOOP_FLAG =10000;

    private static final int  LOOP_FLAG_SKIP =20000;

    private PlayView playView;

    public CountDownRelativeLayouyt(Context context) {
        super(context);
        init(context,null);
    }

    public CountDownRelativeLayouyt(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public CountDownRelativeLayouyt(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        this.context = context;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.layout_count, this, true);

        this.skipCountText      = findViewById(R.id.text_skip_count);
        this.countDownlayout    = findViewById(R.id.text_count_layout);
        this.countDownText = findViewById(R.id.text_count_count);
        this.adText        = findViewById(R.id.text_ad);

        needShowCountDown = AdUtil.isShowAdTime(AdConstant.AdClassify.VIDEO);

        //获取允许跳过的间隔时间
        SkipTime = AdUtil.getSkipTime(AdConstant.AdClassify.VIDEO);
    }


    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOOP_FLAG:{
                    //展示倒计时
                    if (countTime == -10){
                        break;
                    }

                    if (playView.getDuration() != 0) {
                        countTime = (long) playView.getDuration() / 1000 - playView.getCurrentPosition() / 1000;
                    }
//                    countTime--;
                    if (countTime > 0){
                        if (needShowCountDown){
                            countDownText.setText(String.format("%1$ss    ", String.valueOf(countTime)));
                        }
                        sendEmptyMessageDelayed(LOOP_FLAG,500);
                    }else{
                        countComplete();
                    }

                    break;
                }
                case LOOP_FLAG_SKIP:{
                    skipCountTime--;
                    if (skipCountTime > 0){
                        skipCountText.setText(String.format("%1$ss后可以跳过", String.valueOf(skipCountTime)));
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

    private void countComplete(){
        if (null != listener){
            listener.complete();
        }
        countDownlayout.setVisibility(GONE);
        adText.setVisibility(GONE);
        skipCountText.setVisibility(GONE);
        playView = null;
        countTime = -10;
    }

    private void startCountDown(boolean showSkip){
        canSkip = false;
        skipCountTime = SkipTime;
        if (needShowCountDown){
            countDownlayout.setVisibility(VISIBLE);
            this.adText.setVisibility(GONE);
            this.countDownText.setText(String.format("%1$ss    ", String.valueOf(countTime)));
            mHandler.sendEmptyMessageDelayed(LOOP_FLAG,500);
        }else{
            countDownlayout.setVisibility(GONE);
            this.adText.setVisibility(VISIBLE);
            mHandler.sendEmptyMessageDelayed(LOOP_FLAG,500);
        }

        if (showSkip){
            skipCountText.setVisibility(VISIBLE);
        }else{
            skipCountText.setVisibility(INVISIBLE);
        }
        skipCountText.setText(String.format("%1$ss后可以跳过", String.valueOf(skipCountTime)));
        mHandler.sendEmptyMessageDelayed(LOOP_FLAG_SKIP,1000);
    }

    /*******************************************对外提供的方法********************************************/

    //设置倒计时起始时间并且开始倒计时
    public void setTotalTimeAndStart(long totalTime,boolean showSkip){
        if (totalTime > 0){
            this.countTime = totalTime;
            startCountDown(showSkip);
        }
    }

    //实时设置倒计时进度，vod播放卡顿可能导致倒计时不准确，不建议自动倒计时，可以放在进度回调里设置倒计时
    public void setCountText(long countTime){
        if (needShowCountDown){
            this.countTime = -10;
            this.countDownlayout.setVisibility(VISIBLE);
            this.countDownText.setVisibility(VISIBLE);
            this.countDownText.setText(String.format("%1$ss    ", String.valueOf(countTime)));
        }
    }

    //设置是否需要展示跳过，默认3秒后展示跳过
    public void setNeedShowSkip(boolean needShowSkip) {
        this.needShowSkip = needShowSkip;
    }

    //跳过广告字条是否展示，是否可以跳过广告
    public boolean isCanSkip() {
        return canSkip;
    }

    //当前的倒计时进度
    public long getCountTime() {
        return countTime;
    }

    //设置倒计时结束回调
    public void setListener(completeListener listener) {
        this.listener = listener;
    }

    //隐藏倒计时布局
    public void hide(){
        countDownlayout.setVisibility(GONE);
        adText.setVisibility(GONE);
        skipCountText.setVisibility(GONE);
        countTime = -10;
        playView = null;
        mHandler.removeMessages(LOOP_FLAG);
        mHandler.removeCallbacksAndMessages(null);
    }

    //设置右下角跳过提示状态 0隐藏 1展示
    public void setSkipCountTextState(int state){
        if (state == 0){
            skipCountText.setVisibility(INVISIBLE);
        }else if (state == 1){
            skipCountText.setVisibility(VISIBLE);
        }
    }

    /*******************************************对外提供的回调********************************************/
    //倒计时结束回调
    public interface completeListener{
        void complete();
    }

    public void setPlayView(PlayView playView) {
        this.playView = playView;
    }
}
