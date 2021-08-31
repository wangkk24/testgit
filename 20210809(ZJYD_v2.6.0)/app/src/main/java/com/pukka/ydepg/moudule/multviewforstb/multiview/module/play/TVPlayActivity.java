package com.pukka.ydepg.moudule.multviewforstb.multiview.module.play;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.huawei.cloudplayer.sdk.HCPConfig;
import com.huawei.cloudplayer.sdk.HCPGlobalConfig;
import com.huawei.cloudplayer.sdk.HCPMediaType;
import com.huawei.cloudplayer.sdk.HuaweiCloudMultiPlayer4Stb;
import com.huawei.cloudplayer.sdk.HuaweiPlayerFactory;
import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayMultiMediaVODResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ShowBuyControl;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.multviewforstb.multiview.TVApplication;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.config.KPIConstant;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.config.MultiViewContant;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.main.bean.AllVideoConfig;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.main.bean.ViewSize;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.view.LoadingTextView;
import com.pukka.ydepg.moudule.multviewforstb.multiview.util.GSonUtil;
import com.pukka.ydepg.moudule.vod.activity.BrowseTVPlayFragment;
import com.pukka.ydepg.moudule.vod.playerController.VodPlayerControllerView;
import com.pukka.ydepg.moudule.vod.presenter.VoddetailEpsiodePresenter;
import com.pukka.ydepg.moudule.vod.utils.BrowseEpsiodesUtils;
import com.pukka.ydepg.moudule.vod.utils.MultViewUtils;
import com.pukka.ydepg.moudule.vod.utils.VoddetailEpsiodesUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
//import com.bumptech.glide.request.animation.GlideAnimation;

public class TVPlayActivity extends TVPlayBaseActivity implements
        HuaweiCloudMultiPlayer4Stb.OnStbBufferingUpdateListener,
        HuaweiCloudMultiPlayer4Stb.OnStbCompletionListener,
        HuaweiCloudMultiPlayer4Stb.OnStbErrorListener,
        HuaweiCloudMultiPlayer4Stb.OnStbPreparedListener, View.OnFocusChangeListener, HuaweiCloudMultiPlayer4Stb.OnStbInfoListener {

    private SurfaceView surfaceview01;
    private SurfaceView surfaceview02;
    private SurfaceView surfaceview03;
    private SurfaceView surfaceview04;
    /**
     * 选中高亮框
     */
    private View forgroundView;

    /**
     * 多机位播放器
     */
    HuaweiCloudMultiPlayer4Stb multiViewStbPlayer;
    /**
     * 当前界面的父布局
     */
    private FrameLayout rootView;

    /**
     * 机位数量，目前根据UI设计，固定为4
     */
    private final int GATE_HEAVY_NUM = 4;

    private AllVideoConfig allVideoConfig;

    /**
     * 是否是一拖三模式
     */
    private boolean isOneBigThreeSmall;


    private LoadingTextView loadingView;

    /**
     * 防暴力点击时间戳，只针对1拖3下，切换小屏幕
     */
    private long switchTimeMillis;

    /**
     * 切换窗口防暴力点击时间阀，经测试，机顶盒过热情况下，500ms也无法阻止
     */
    private final int SWITCH_TIME = 500;

    /**
     * 播放背景
     */
    private String playBackgroundUrl = "";

    /**
     * 4个屏幕对应的布局参数
     */
    private FrameLayout.LayoutParams layoutParams1, layoutParams2, layoutParams3, layoutParams4, layoutParamsFull;

    /**
     * 非海思接口，1拖3，切换机位左边小屏缓冲布局参数
     */
    private FrameLayout.LayoutParams layoutParamsLeftSmall = null;
    /**
     * 非海思接口，存储0-4位置和对应的SurfaceView
     */
    private HashMap<Integer, View> layoutParamsHashMap = null;

    /**
     * 是否是海思接口
     */
    private boolean isHs = false;

    /**
     * 非海思接口机顶盒下，切换View位置是否切换成功
     */
    boolean screenChangeSuccess = true;

    String mVODID;

    BrowseEpsiodesUtils mBrowseEpsiodesUtils = new BrowseEpsiodesUtils();

    private Handler mhandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugLog.info(TAG, KPIConstant.KPI_MULTIVIEW + "onCreate Activity");
        if (getIntent() != null) {
            mVODID = getIntent().getStringExtra("CONTENTID");
            String videoJson = getIntent().getStringExtra(PlayConstant.EXTRA);
            DebugLog.info(TAG, "[onCreate] videoJson = " + videoJson);
            Gson gson = new Gson();
            isFromEPGGo2There = TextUtils.isEmpty(videoJson);
            DebugLog.info(TAG, "[initPlayer] isFromEPGGo2There : " + isFromEPGGo2There);
            if (isFromEPGGo2There) {
                allVideoConfig = GSonUtil.getAllVideoConfig(getIntent());
            } else {
                try {
                    allVideoConfig = gson.fromJson(videoJson, AllVideoConfig.class);
                } catch (Exception e) {
                    DebugLog.error(TAG, e.toString());
                }
            }
            DebugLog.info(TAG, "allVideoConfig is null : " + (allVideoConfig == null));

        }
        if (allVideoConfig != null) {
            int multiAngleMode = allVideoConfig.getViewtype();
            if (allVideoConfig.getPicture() != null) {
                playBackgroundUrl = allVideoConfig.getPicture().getPlayBackground();
            }
            isOneBigThreeSmall = multiAngleMode == 0;
        }

        DebugLog.debug(TAG, "[onCreate] isOneBigThreeSmall = " + isOneBigThreeSmall);
        setContentView(R.layout.activity_play);
        initCtrlView(R.id.playcontrolview);
        forgroundView = findViewById(R.id.forgroud);
        rootView = findViewById(R.id.root_view);
        downloadSpeed = findViewById(R.id.progress);
        loadingView = findViewById(R.id.loading_view);
        logcatViewStub = findViewById(R.id.logcat);

        onBufferingUpdate = false;
        mhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == BrowseTVPlayFragment.EPISODE_PLAY) {
//切换子集
                    Bundle data = msg.getData();
//                    bundle.putString("SitcomNO", episode.getSitcomNO());
//                    bundle.putString("EpisodesId", episode.getVOD().getID());
//                    bundle.putString("MediaId", episode.getVOD().getMediaFiles().get(0).getID());
//                    bundle.putString("elapseTime", episode.getVOD().getMediaFiles().get(0).getElapseTime());
//                    bundle.putSerializable("episodeVod",episode.getVOD());
                    //子集id
                    String episodesId = (String) data.get("EpisodesId");
                    //子集媒资id
                    String mediaIdId = (String) data.get("MediaId");

                    ShowBuyControl showBuyControl = new ShowBuyControl(TVPlayActivity.this);
                    showBuyControl.setCallBack(new ShowBuyControl.ShowBuyTagCallBack() {
                        @Override
                        public void showBuyTag(int showBuy) {

                        }
                    });
                    showBuyControl.setCallBack(new ShowBuyControl.ShowMultViewResponseCallBack() {
                        @Override
                        public void showBuyTag(PlayVODResponse response) {

                        }

                        @Override
                        public void showBuyTags(PlayMultiMediaVODResponse response) {

                        }
                    });

                    PlayVODRequest playVODRequest = new PlayVODRequest();
                    playVODRequest.setVODID(episodesId);
                    playVODRequest.setMediaID(mediaIdId);
                    playVODRequest.setURLFormat("1");
                    playVODRequest.setIsReturnProduct("1");
                    showBuyControl.playVod(playVODRequest);
                }
            }
        };
    }

    /**
     * 根据当前需要展示的View的模式，或者尺寸，进行View的创建
     */
    private void initPlayView() {
        surfaceview01 = new SurfaceView(TVPlayActivity.this);
        surfaceview01.setId(R.id.surfaceview_1);
        surfaceview01.setFocusable(true);

        surfaceview02 = new SurfaceView(TVPlayActivity.this);
        surfaceview02.setId(R.id.surfaceview_2);
        surfaceview02.setFocusable(true);

        surfaceview03 = new SurfaceView(TVPlayActivity.this);
        surfaceview03.setId(R.id.surfaceview_3);
        surfaceview03.setFocusable(true);

        surfaceview04 = new SurfaceView(TVPlayActivity.this);
        surfaceview04.setId(R.id.surfaceview_4);
        surfaceview04.setFocusable(true);

        setViewNextFocus(surfaceview01, surfaceview02, surfaceview03, surfaceview04);

        FrameLayout.LayoutParams forgroudLayoutParams;

        layoutParamsFull = new FrameLayout.LayoutParams(phoneWidth, phoneHeight);

        if (isOneBigThreeSmall) {
            int smallHeight = (phoneHeight - MultiViewContant.MARGIN * 4) / 3;
            int smallWidth = smallHeight * 16 / 9;
            int marginBig = phoneWidth - smallWidth;

            int bigWidth = marginBig - MultiViewContant.MARGIN;
            int bigHeight = bigWidth * 9 / 16;
            int marginBigTop = (phoneHeight - bigHeight) / 2;

            if (!isHs) {
                layoutParamsLeftSmall = new FrameLayout.LayoutParams(smallWidth, smallHeight);
                layoutParamsLeftSmall.setMargins(phoneWidth, 0, 0, 0);
            }

            forgroudLayoutParams = new FrameLayout.LayoutParams(bigWidth, bigHeight);
            forgroudLayoutParams.setMargins(0, marginBigTop, 0, 0);

            layoutParams1 = new FrameLayout.LayoutParams(bigWidth, bigHeight);
            layoutParams1.setMargins(0, marginBigTop, 0, 0);

            layoutParams2 = new FrameLayout.LayoutParams(smallWidth, smallHeight);
            layoutParams2.setMargins(marginBig, MultiViewContant.MARGIN, 0, 0);
            layoutParams2.setMarginStart(marginBig);

            layoutParams3 = new FrameLayout.LayoutParams(smallWidth, smallHeight);
            layoutParams3.setMargins(marginBig, MultiViewContant.MARGIN * 2 + smallHeight, 0, 0);
            layoutParams3.setMarginStart(marginBig);

            layoutParams4 = new FrameLayout.LayoutParams(smallWidth, smallHeight);
            layoutParams4.setMargins(marginBig, MultiViewContant.MARGIN * 3 + smallHeight * 2, 0, 0);
            layoutParams4.setMarginStart(marginBig);
        } else {
            int aveHeight = (phoneHeight - MultiViewContant.MARGIN) / 2;
            int aveWidth = (phoneWidth - MultiViewContant.MARGIN) / 2;

            final int goldHeight = aveWidth * 9 / 16;
            final int goldWidth = aveHeight * 16 / 9;
            if (goldHeight < aveHeight) {
                aveHeight = goldHeight;
            } else {
                aveWidth = goldWidth;
            }

            forgroudLayoutParams = new FrameLayout.LayoutParams(aveWidth, aveHeight);
            forgroudLayoutParams.setMargins(0, 0, 0, 0);

            layoutParams1 = new FrameLayout.LayoutParams(aveWidth, aveHeight);
            layoutParams1.setMargins(0, 0, 0, 0);

            layoutParams2 = new FrameLayout.LayoutParams(aveWidth, aveHeight);
            layoutParams2.setMargins(aveWidth + MultiViewContant.MARGIN, 0, 0, 0);
            layoutParams2.setMarginStart(aveWidth + MultiViewContant.MARGIN);

            layoutParams3 = new FrameLayout.LayoutParams(aveWidth, aveHeight);
            layoutParams3.setMargins(0, aveHeight + MultiViewContant.MARGIN, 0, 0);

            layoutParams4 = new FrameLayout.LayoutParams(aveWidth, aveHeight);
            layoutParams4.setMargins(MultiViewContant.MARGIN + aveWidth, MultiViewContant.MARGIN + aveHeight, 0, 0);
            layoutParams4.setMarginStart(MultiViewContant.MARGIN + aveWidth);
        }

        if (allVideoConfig != null && allVideoConfig.getViewSizes() != null) {
            FrameLayout.LayoutParams[] layoutParamsArray = {layoutParams1, layoutParams2, layoutParams3, layoutParams4};

            List<ViewSize> viewSizes = allVideoConfig.getViewSizes();
            if (viewSizes != null && !viewSizes.isEmpty()) {
                for (int i = 0; i < (viewSizes.size() > layoutParamsArray.length ? layoutParamsArray.length : viewSizes.size()); i++) {
                    ViewSize viewSize = viewSizes.get(i);
                    FrameLayout.LayoutParams layoutParams = layoutParamsArray[i];
                    layoutParams.width = viewSize.getWidth();
                    layoutParams.height = viewSize.getHeight();
                    layoutParams.setMargins(viewSize.getLeft(), viewSize.getTop(), 0, 0);
                    layoutParams.setMarginStart(viewSize.getLeft());
                    if (i == 0) {
                        forgroudLayoutParams.width = viewSize.getWidth();
                        forgroudLayoutParams.height = viewSize.getHeight();
                        forgroudLayoutParams.setMargins(viewSize.getLeft(), viewSize.getTop(), 0, 0);
                        forgroudLayoutParams.setMarginStart(viewSize.getLeft());
                    }
                }
            }
        }

        surfaceview01.setLayoutParams(layoutParams1);
        surfaceview02.setLayoutParams(layoutParams2);
        surfaceview03.setLayoutParams(layoutParams3);
        surfaceview04.setLayoutParams(layoutParams4);


        forgroundView.setLayoutParams(forgroudLayoutParams);

        rootView.addView(surfaceview01, 0);
        rootView.addView(surfaceview02, 1);
        rootView.addView(surfaceview03, 2);
        rootView.addView(surfaceview04, 3);

        surfaceview01.setOnFocusChangeListener(this);
        surfaceview02.setOnFocusChangeListener(this);
        surfaceview03.setOnFocusChangeListener(this);
        surfaceview04.setOnFocusChangeListener(this);


        if (!isHs) {
            layoutParamsHashMap = new HashMap<>();
            layoutParamsHashMap.put(0, surfaceview01);
            layoutParamsHashMap.put(1, surfaceview02);
            layoutParamsHashMap.put(2, surfaceview03);
            layoutParamsHashMap.put(3, surfaceview04);
        }

        surfaceview01.requestFocus();

        initPlayer();
    }

    /**
     * 初始化播放器，并进行播放
     */
    protected void initPlayer() {
        DebugLog.info(TAG, KPIConstant.KPI_MULTIVIEW + "initPlayer");
        multiViewStbPlayer = HuaweiPlayerFactory.createMultiViewStbPlayer(this, allVideoConfig != null ? allVideoConfig.getResourcePlayURL().size() : GATE_HEAVY_NUM);
        HuaweiPlayerFactory.setConsoleLog(hcpLogLevel);
        HuaweiPlayerFactory.setFileLog(hcpLogLevel, TVApplication.LOG_FILE_PATH);

        //开启crash捕获开关
        HuaweiPlayerFactory.setGlobalConfig(HCPGlobalConfig.CRASH_POLICY, crashPolicyConfig);
        multiViewStbPlayer.setOnInfoListener(this);
        multiViewStbPlayer.setOnBufferingUpdateListener(this);
        multiViewStbPlayer.setOnCompletionListener(this);
        multiViewStbPlayer.setOnErrorListener(this);
        multiViewStbPlayer.setOnPreparedListener(this);
        currentState = PlayState.NULL;
        if (allVideoConfig != null) {
            if (showLogcat) {
                initLogcatRecyclerView();
                showJson(allVideoConfig.toString());
            }
            DebugLog.info(TAG, "[initPlayer] videoConfig=" + allVideoConfig.toString());
            if (allVideoConfig.getResourcePlayURL() == null || allVideoConfig.getResourcePlayURL().isEmpty()) {
                showToast("playUrl is null or empty ：" + allVideoConfig.getResourcePlayURL());
                return;
            }
            AllVideoConfig videoConfig = allVideoConfig;
            HCPConfig hcpConfig = new HCPConfig();
            hcpConfig.setMediaType((isLive = videoConfig.getMediaType() == 1) ? HCPMediaType.MEDIA_TYPE_LIVE : HCPMediaType.MEDIA_TYPE_VOD);
            hcpConfig.setAutoStart(true);
            hcpConfig.setPanoramic(false);
//            if ("1".equals(allVideoConfig.getEnforceProgressiveScanning())) {
            DebugLog.info(TAG, "[initPlayer] setProgressiveScanFlag true");
            hcpConfig.setProgressiveScanFlag(true);
//            }
            if (!isLive) {
                hcpConfig.setBookmark(videoConfig.getBookmark());
            }
            DebugLog.info(TAG, KPIConstant.KPI_MULTIVIEW + "setConfig");
            multiViewStbPlayer.setConfig(hcpConfig);

            SurfaceView[] surfaceViews = {surfaceview01, surfaceview02, surfaceview03, surfaceview04};
            List<String> urls = new ArrayList<>();
            for (String url : allVideoConfig.getResourcePlayURL()) {
                urls.add(url.replace("\"", "").replace("\"", "").trim());
                DebugLog.debug(TAG, "[initPlayer] replace url =" + url);
            }
            surfaceViews = Arrays.copyOf(surfaceViews, videoConfig.getResourcePlayURL().size());
            String[] playUrls = urls.toArray(new String[0]);
            DebugLog.debug(TAG, "[initPlayer] playUrls=" + Arrays.toString(playUrls));
            DebugLog.info(TAG, KPIConstant.KPI_MULTIVIEW + "addView");
            multiViewStbPlayer.addView(surfaceViews, playUrls);
            multiViewStbPlayer.start();
            DebugLog.info(TAG, KPIConstant.KPI_MULTIVIEW + "isLive " + isLive);
            //是否展示网速信息
            downloadSpeed.setVisibility(isShowSpeed ? View.VISIBLE : View.GONE);
            if (isShowSpeed) {
                uiHandler.sendMessage(uiHandler.obtainMessage(PlayConstant.WHAT_UPDATE_SPEED, String.valueOf(System.currentTimeMillis())));
            }
            if (!isLive) {
                uiHandler.sendEmptyMessage(PlayConstant.WHAT_UPDATE_TIME);
            }
        } else {
            showToast(getString(R.string.play_error_tip_param));
            DebugLog.error(TAG, "[initPlayer] allVideoConfig==null");
        }
    }


    @Override
    protected void seekTo(long time) {

        DebugLog.info(TAG, "[seekTo] time=" + time + " currentPlayPos=" + currentPlayPos);
        addLog("[seekTo] time=" + time + " currentPlayPos=" + currentPlayPos, false);
        //去除刷新UI通知，播放器在seek后，获取的当前时间点还是seek前位置，导致进度条刷新会出现回退现象
        if (null != uiHandler) {
            uiHandler.removeMessages(PlayConstant.WHAT_UPDATE_TIME);
        }

        if (null != multiViewStbPlayer) {
            multiViewStbPlayer.seek(time);
        }
    }

    @Override
    protected void showOrHideControl(boolean show) {

    }

    @Override
    protected boolean playerMuteChange() {
        if (multiViewStbPlayer != null) {
            multiViewStbPlayer.setMute(!multiViewStbPlayer.isMute());
            showToast(multiViewStbPlayer.isMute() ? getString(R.string.play_mute) : getString(R.string.play_mute_dis));
            return true;
        }
        return false;
    }

    @Override
    protected void playerPlayOrPause(boolean toPlay) {
        if (multiViewStbPlayer == null) {
            return;
        }
        if (toPlay) {
            multiViewStbPlayer.start();
        } else {
            multiViewStbPlayer.pause();
        }
    }

    @Override
    protected void dpadLeftDown(boolean leftDown, KeyEvent event) {

    }

    @Override
    protected void dpadLRUp() {

    }

    /**
     * 进入大屏
     */
    private void go2Full(View surfaceView) {
        if (surfaceView == null) {
            DebugLog.error(TAG, "[go2Full] surfaceView == null");
            return;
        }
        DebugLog.info(TAG, KPIConstant.KPI_MULTIVIEW + "go2Full start");
        addLog("[go2Full]", false);
        fullScreenMode = true;
        forgroundView.setVisibility(View.GONE);

        if (isHs) {
            surfaceview02.setVisibility(View.GONE);
            surfaceview02.setFocusable(false);
            surfaceview03.setVisibility(View.GONE);
            surfaceview03.setFocusable(false);
            surfaceview04.setVisibility(View.GONE);
            surfaceview04.setFocusable(false);
        } else {
            multiViewStbPlayer.setFullScreen(true);
            for (int i = 0; i < GATE_HEAVY_NUM; i++) {
                View view = layoutParamsHashMap.get(i);
                if (view == null) {
                    continue;
                }
                if (surfaceView != view) {
                    view.setVisibility(View.GONE);
                    view.setFocusable(false);
                }
            }
        }

        DebugLog.info(TAG, "[go2Full] doPositionChange");
        surfaceView.setLayoutParams(layoutParamsFull);
        surfaceView.clearFocus();
        surfaceView.setFocusable(false);
        DebugLog.info(TAG, KPIConstant.KPI_MULTIVIEW + "go2Full end");
    }

    /**
     * 退出大屏
     */
    private void outFull() {
        if (surfaceview01 == null) {
            return;
        }
        DebugLog.info(TAG, KPIConstant.KPI_MULTIVIEW + "outFull start");
        addLog("[outFull]", false);
        fullScreenMode = false;
        DebugLog.info(TAG, "[outFull] doPositionChange");

        if (isHs) {
            surfaceview01.setLayoutParams(layoutParams1);
            surfaceview01.setVisibility(View.VISIBLE);
            surfaceview01.setFocusable(true);
            surfaceview02.setVisibility(View.VISIBLE);
            surfaceview02.setFocusable(true);
            surfaceview03.setVisibility(View.VISIBLE);
            surfaceview03.setFocusable(true);
            surfaceview04.setVisibility(View.VISIBLE);
            surfaceview04.setFocusable(true);
            surfaceview01.requestFocus();
        } else {
            for (int i = 0; i < GATE_HEAVY_NUM; i++) {
                View view = layoutParamsHashMap.get(i);
                if (view == null) {
                    continue;
                }
                view.setVisibility(View.VISIBLE);
                view.setFocusable(true);
                if (i == 0) {
                    view.setLayoutParams(layoutParams1);
                    view.requestFocus();
                }
            }
            multiViewStbPlayer.setFullScreen(false);
        }

        DebugLog.info(TAG, KPIConstant.KPI_MULTIVIEW + "outFull end");
        hideCtrlUI();
        forgroundView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBufferingUpdate(int i) {
        DebugLog.debug(TAG, "[onBufferingUpdate] buffer=" + i);
        if (i < 100) {
            onBufferingUpdate = false;
            if (loadingView != null) {
                loadingView.onBufferingUpdate(i);
            }
        } else {
            isFirstBufferSuccess = true;
            if (loadingView != null) {
                loadingView.stopBufferingUpdate();
            }
        }

        if (i >= 100 && null != uiHandler && !onBufferingUpdate) {
            onBufferingUpdate = true;
            //用于seek后刷新UI进度条，播放器在seek后，获取的当前时间点还是seek前位置，导致进度条刷新会出现回退现象
            uiHandler.removeMessages(PlayConstant.WHAT_UPDATE_TIME);
            uiHandler.sendEmptyMessage(PlayConstant.WHAT_UPDATE_TIME);
        }
    }

    @Override
    public void onCompletion() {
        DebugLog.debug(TAG, "[onCompletion]");
        addLog("[onCompletion]", false);
        showToast(getString(R.string.play_placomplete));
        if (isFromEPGGo2There) {
            allVideoConfig.setBookmark(multiViewStbPlayer.getCurrentPosition());
            showExitDialog(this, allVideoConfig);
        } else {
            finish();
        }
    }

    @Override
    public void onError(int i, String s) {
        DebugLog.error(TAG, "[onError] code=" + i, " msg=" + s);
        addLog("[onError] code=" + i + " msg=" + s, true);
        showToast("[onError] " + i + " " + s);
        if (uiHandler != null) {
            uiHandler.removeMessages(PlayConstant.WHAT_UPDATE_TIME);
            uiHandler.removeMessages(PlayConstant.WHAT_UPDATE_SPEED);
        }
        if (loadingView != null) {
            loadingView.stopBufferingUpdate();
        }
        isFirstBufferSuccess = false;
    }

    @Override
    public void onPrepared() {
        DebugLog.info(TAG, "[onPrepared]");
        addLog("[onPrepared]", false);
        currentState = PlayState.PLAYING;
    }


    @Override
    public void onFocusChange(final View v, boolean hasFocus) {
        if (!hasFocus)
            return;

        v.post(new Runnable() {
            @Override
            public void run() {
                DebugLog.info(TAG, "[onFocusChange] id=" + v.getId() + " fullScreenMode=" + fullScreenMode);
                if (fullScreenMode) {
                    return;
                }
                if (v.getId() == surfaceview01.getId()) {
                    changeViewLayoutParam(forgroundView, surfaceview01);
                } else if (v.getId() == surfaceview02.getId()) {
                    changeViewLayoutParam(forgroundView, surfaceview02);
                } else if (v.getId() == surfaceview03.getId()) {
                    changeViewLayoutParam(forgroundView, surfaceview03);
                } else if (v.getId() == surfaceview04.getId()) {
                    changeViewLayoutParam(forgroundView, surfaceview04);
                }

                if (forgroundView.getVisibility() == View.GONE) {
                    forgroundView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 改变View的layoutParams
     *
     * @param needChangeView 需要改变参数的View
     * @param targetView     目标view
     */
    private void changeViewLayoutParam(View needChangeView, View targetView) {
        if (needChangeView == null || targetView == null) {
            DebugLog.error(TAG, "[changeViewLayoutParam] needChangeView == null || targetView == null");
            return;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) needChangeView.getLayoutParams();
        layoutParams.width = targetView.getWidth();
        layoutParams.height = targetView.getHeight();
        layoutParams.setMargins(targetView.getLeft(), targetView.getTop(), 0, 0);
        layoutParams.setMarginStart(targetView.getLeft());
        needChangeView.setLayoutParams(layoutParams);
    }

    @Override
    public void onBackPressed() {
        DebugLog.info(TAG, "[onBackPressed]");
        addLog("[onBackPressed]", false);
        if (fullScreenMode) {
            DebugLog.info(TAG, "[onBackPressed] outFull");
            outFull();
            return;
        }
        if (isFromEPGGo2There) {
            allVideoConfig.setBookmark(multiViewStbPlayer.getCurrentPosition());
            DebugLog.info(TAG, "[onBackPressed] showExitDialog");
            showExitDialog(this, allVideoConfig);
            return;
        }
        DebugLog.info(TAG, "[onBackPressed] PlayState.STOP");
        currentState = PlayState.STOP;
        if (null != multiViewStbPlayer) {
            multiViewStbPlayer.stop();
        }
        super.onBackPressed();
    }

    @Override
    protected void mediaDestroy() {
        addLog("[mediaDestroy]", false);
        if (uiHandler != null) {
            uiHandler.removeMessages(PlayConstant.WHAT_UPDATE_TIME);
            uiHandler.removeMessages(PlayConstant.WHAT_UPDATE_SPEED);
        }

        if (multiViewStbPlayer != null) {
            DebugLog.info(TAG, "[mediaDestroy] destroy PlayState.STOP");
            isFirstBufferSuccess = false;
            currentState = PlayState.STOP;
            multiViewStbPlayer.destroy();
            multiViewStbPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (multiViewStbPlayer != null) {
            DebugLog.info(TAG, "[onPause] currentState=" + currentState);
            if (currentState == PlayState.STOP) {
                //如果当前的状态是停止状态，说明是退出播放操作,已经调用过stop操作
//                multiViewStbPlayer.stop();
            } else {
                multiViewStbPlayer.suspend();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (multiViewStbPlayer != null && onCreate) {
            DebugLog.info(TAG, "[onResume] resume");
            boolean screenParamChanged = checkScreenParamChanged();
            if (!screenParamChanged) {
                multiViewStbPlayer.resume();
            }
        }
        onCreate = true;
    }

    @Override
    protected boolean canClickPauseBtn() {
        return fullScreenMode && !isLive;
    }

    @Override
    protected boolean canClickSeekBtn() {
        return fullScreenMode;
    }

    @Override
    protected void updatePlayerTime() {
        if (multiViewStbPlayer != null) {
            currentPlayPos = multiViewStbPlayer.getCurrentPosition();
            duration = multiViewStbPlayer.getDuration();
            bufferedLength = multiViewStbPlayer.getBufferedLength();
        }
    }

    @Override
    protected long getTotalDownloadByteNum() {
        return multiViewStbPlayer == null ? 0 : multiViewStbPlayer.getTotalDownloadByteNum();
    }

    @Override
    protected boolean playFreeVideo() {
        return true;
    }

    @Override
    protected void getPhoneWHSuccess() {

        if (!TextUtils.isEmpty(playBackgroundUrl)) {
            Glide.with(this)
                    .load(playBackgroundUrl)
                    .dontAnimate()
                    .override(phoneWidth, phoneHeight)
                    .thumbnail(0.8f)
                    .placeholder(R.color.playBg)
                    .error(R.color.playBg).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    if (rootView != null) {
                        rootView.setBackground(resource);
                    }
                    return false;
                }
            });
//                    .into(new ViewTarget<View,GlideDrawable>(rootView) {
//                        @Override
//                        public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                            if(rootView != null){
//                                rootView.setBackground(glideDrawable.getCurrent());
//                            }
//                        }
//                    });
        }

        initPlayView();
    }

    /**
     * 检测屏幕的分辨率，或者屏幕缩放比例是否改变
     * <p>
     * 如果改变了，需要重新进入播放界面。
     *
     * @return
     */
    private boolean checkScreenParamChanged() {
        if (multiViewStbPlayer != null) {
            boolean screenParamChange = multiViewStbPlayer.isScreenParamChanged();
            ;
            DebugLog.info(TAG, "[checkScreenParamChanged] screenParamChange=" + screenParamChange);
            addLog("[checkScreenParamChanged] screenParamChange=" + screenParamChange, false);
            if (screenParamChange) {
                //屏幕发生了变化，需要主动停止播放，并且提示退出后用户重新播放。
                multiViewStbPlayer.stop();
                AlertDialog.Builder builder = new AlertDialog.Builder(TVPlayActivity.this);
                builder.setCancelable(false);
                builder.setMessage(getString(R.string.screen_change_tip));
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            mediaDestroy();
                            Thread.sleep(150);
                            if (isFromEPGGo2There) {
                                allVideoConfig.setBookmark(multiViewStbPlayer.getCurrentPosition());
                                Intent intent = new Intent();
                                intent.putExtra(PlayConstant.RETURN_URL, allVideoConfig.toJson());
                                setResult(RESULT_OK, intent);
                            }
                            finish();
                        } catch (InterruptedException e) {
                            DebugLog.error(TAG, e.getMessage());
                        }
                    }
                });
                builder.create().show();
            }
            return screenParamChange;
        }
        return false;
    }

    @Override
    protected void refreshZoomUI() {

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        DebugLog.debug(TAG, "[onKeyUp] keyCode = " + keyCode);

        if (!isFirstBufferSuccess) {
            DebugLog.debug(TAG, "[onKeyUp] isFirstBufferSuccess = false");
            return super.onKeyUp(keyCode, event);
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER && !fullScreenMode) {
            if (null == multiViewStbPlayer) {
                DebugLog.error(TAG, "[onClick] null == multiViewStbPlayer");
                addLog("[onClick] null == multiViewStbPlayer", true);
                return super.onKeyUp(keyCode, event);
            }

            long currentTimeMillis = System.currentTimeMillis();

            if (currentTimeMillis - switchTimeMillis < SWITCH_TIME) {
                return super.onKeyUp(keyCode, event);
            }
            switchTimeMillis = currentTimeMillis;

            if (!isHs) {

                if (!screenChangeSuccess) {
                    DebugLog.debug(TAG, "[onClick] go2Full screenChangeSuccess=" + screenChangeSuccess);
                    return true;
                }

                int currentFocusPos = 0;//当前聚焦的view位置
                for (int i = 0; i < GATE_HEAVY_NUM; i++) {
                    View view = layoutParamsHashMap.get(i);
                    if (view != null && view.isFocused()) {
                        currentFocusPos = i;
                        break;
                    }
                }
                final View pos1View = layoutParamsHashMap.get(0);
                final View pos2View = layoutParamsHashMap.get(1);
                final View pos3View = layoutParamsHashMap.get(2);
                final View pos4View = layoutParamsHashMap.get(3);

                if (pos1View == null || pos2View == null || pos3View == null || pos4View == null) {
                    DebugLog.error(TAG, "[onClick] go2Full pos1View == null || pos2View == null || pos3View == null || pos4View == null");
                    return true;
                }

                if (currentFocusPos == 0) {
                    DebugLog.info(TAG, "[onClick] go2Full isFirstBufferSuccess=" + isFirstBufferSuccess);
                    if (!isFirstBufferSuccess) {
                        return true;
                    }
                    go2Full(pos1View);
                    return true;
                }

                DebugLog.info(TAG, KPIConstant.KPI_MULTIVIEW + "setBaseDisplay start");
                //第2个位置
                if (currentFocusPos == 1) {
                    pos1View.setLayoutParams(layoutParams2);
                    pos1View.requestFocus();
                    screenChangeSuccess = false;
                    layoutParamsHashMap.put(0, pos2View);
                    layoutParamsHashMap.put(1, pos1View);
                    setViewNextFocus(layoutParamsHashMap.get(0), layoutParamsHashMap.get(1), layoutParamsHashMap.get(2), layoutParamsHashMap.get(3));
                    if (!isOneBigThreeSmall) {
                        pos2View.setLayoutParams(layoutParams1);
                        screenChangeSuccess = true;
                        return true;
                    }
                    pos2View.setLayoutParams(layoutParamsLeftSmall);
                    pos2View.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (pos2View != null) {
                                pos2View.setLayoutParams(layoutParams1);
                                screenChangeSuccess = true;
                                DebugLog.info(TAG, KPIConstant.KPI_MULTIVIEW + "setBaseDisplay end");
                            }
                        }
                    }, PlayConstant.NOMAL_SWITCH_SCREEN_DELAY_TIME);
                    multiViewStbPlayer.setBaseDisplay(1);
                    return true;
                }

                //第3个位置
                if (currentFocusPos == 2) {
                    pos1View.setLayoutParams(layoutParams3);
                    pos1View.requestFocus();
                    screenChangeSuccess = false;
                    layoutParamsHashMap.put(0, pos3View);
                    layoutParamsHashMap.put(2, pos1View);
                    setViewNextFocus(layoutParamsHashMap.get(0), layoutParamsHashMap.get(1), layoutParamsHashMap.get(2), layoutParamsHashMap.get(3));
                    if (!isOneBigThreeSmall) {
                        pos3View.setLayoutParams(layoutParams1);
                        screenChangeSuccess = true;
                        return true;
                    }
                    pos3View.setLayoutParams(layoutParamsLeftSmall);
                    pos3View.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (pos3View != null) {
                                pos3View.setLayoutParams(layoutParams1);
                                screenChangeSuccess = true;
                                DebugLog.info(TAG, KPIConstant.KPI_MULTIVIEW + "setBaseDisplay end");
                            }
                        }
                    }, PlayConstant.NOMAL_SWITCH_SCREEN_DELAY_TIME);
                    multiViewStbPlayer.setBaseDisplay(2);
                    return true;
                }

                //第4个位置
                pos1View.setLayoutParams(layoutParams4);
                pos1View.requestFocus();
                screenChangeSuccess = false;
                layoutParamsHashMap.put(0, pos4View);
                layoutParamsHashMap.put(3, pos1View);
                setViewNextFocus(layoutParamsHashMap.get(0), layoutParamsHashMap.get(1), layoutParamsHashMap.get(2), layoutParamsHashMap.get(3));
                if (!isOneBigThreeSmall) {
                    pos4View.setLayoutParams(layoutParams1);
                    screenChangeSuccess = true;
                    return true;
                }
                pos4View.setLayoutParams(layoutParamsLeftSmall);
                pos4View.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pos4View != null) {
                            pos4View.setLayoutParams(layoutParams1);
                            screenChangeSuccess = true;
                            DebugLog.info(TAG, KPIConstant.KPI_MULTIVIEW + "setBaseDisplay end");
                        }
                    }
                }, PlayConstant.NOMAL_SWITCH_SCREEN_DELAY_TIME);
                multiViewStbPlayer.setBaseDisplay(3);
                return true;
            }

            if (surfaceview02.isFocused()) {
                DebugLog.info(TAG, KPIConstant.KPI_MULTIVIEW + "setBaseDisplay(1)");
                multiViewStbPlayer.setBaseDisplay(1);
                DebugLog.info(TAG, "[onClick] setBaseDisplay(1) time consuming=" + (System.currentTimeMillis() - currentTimeMillis) + "ms");
            } else if (surfaceview03.isFocused()) {
                DebugLog.info(TAG, KPIConstant.KPI_MULTIVIEW + "setBaseDisplay(2)");
                multiViewStbPlayer.setBaseDisplay(2);
                DebugLog.info(TAG, "[onClick] setBaseDisplay(2) time consuming=" + (System.currentTimeMillis() - currentTimeMillis) + "ms");
            } else if (surfaceview04.isFocused()) {
                DebugLog.info(TAG, KPIConstant.KPI_MULTIVIEW + "setBaseDisplay(3)");
                multiViewStbPlayer.setBaseDisplay(3);
                DebugLog.info(TAG, "[onClick] setBaseDisplay(3) time consuming=" + (System.currentTimeMillis() - currentTimeMillis) + "ms");
            } else {
                DebugLog.info(TAG, "[onClick] go2Full isFirstBufferSuccess=" + isFirstBufferSuccess);
                if (!isFirstBufferSuccess) {
                    return true;
                }
                go2Full(surfaceview01);
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 设置View的下一个焦点View
     *
     * @param pos1
     * @param pos2
     * @param pos3
     * @param pos4
     */
    private void setViewNextFocus(View pos1, View pos2, View pos3, View pos4) {
        if (pos1 == null || pos2 == null || pos3 == null || pos4 == null) {
            DebugLog.error(TAG, "[setViewNextFocus] pos1 == null || pos2 == null || pos3 == null || pos4 == null");
            return;
        }
        pos1.setNextFocusRightId(pos2.getId());

        pos2.setNextFocusLeftId(pos1.getId());
        pos2.setNextFocusDownId(pos3.getId());
        pos2.setNextFocusUpId(pos4.getId());

        pos3.setNextFocusLeftId(pos1.getId());
        pos3.setNextFocusDownId(pos4.getId());
        pos3.setNextFocusUpId(pos2.getId());

        pos4.setNextFocusLeftId(pos1.getId());
        pos4.setNextFocusDownId(pos2.getId());
        pos4.setNextFocusUpId(pos3.getId());
    }

    @Override
    public void onInfo(int i, Object o) {

        if (o == null) {
            return;
        }
        //收到机顶盒类型上报事件817
        //0：非机顶盒，1：海思，2：非海思
        if (i == PlayConstant.UPDATE_STB_TYPE) {
            isHs = 1 == (int) o;
        }
    }

    private void getSimpleEpsiodes() {
//        MultViewUtils.showEpiodes(mVODID, this, mhandler);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
                getSimpleEpsiodes();
                break;
        }
        return super.onKeyDown(keyCode, event);

    }
}
