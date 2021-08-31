package com.pukka.ydepg.moudule.multviewforstb.multiview.module.play.freecamera;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.huawei.cloudplayer.sdk.HCPConfig;
import com.huawei.cloudplayer.sdk.HCPGlobalConfig;
import com.huawei.cloudplayer.sdk.HCPMediaType;
import com.huawei.cloudplayer.sdk.HCPPicture;
import com.huawei.cloudplayer.sdk.HuaweiCloudPlayer;
import com.huawei.cloudplayer.sdk.HuaweiPlayerFactory;
import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.BookMarkSwitchs;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayMultiMediaVODResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.utils.ShowBuyControl;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.multviewforstb.multiview.TVApplication;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.config.KPIConstant;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.main.bean.AllVideoConfig;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.play.LandPlayControlView;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.play.PlayConstant;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.play.PlayState;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.play.TVPlayBaseActivity;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.view.AerialView;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.view.AngleView;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.view.LoadingTextView;
import com.pukka.ydepg.moudule.multviewforstb.multiview.util.GSonUtil;
import com.pukka.ydepg.moudule.multviewforstb.multiview.util.ScreenUtil;
import com.pukka.ydepg.moudule.vod.activity.BrowseTVPlayFragment;
import com.pukka.ydepg.moudule.vod.playerController.VodPlayerControllerView;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.vod.presenter.VoddetailEpsiodePresenter;
import com.pukka.ydepg.moudule.vod.utils.BrowseEpsiodesUtils;
import com.pukka.ydepg.moudule.vod.utils.MultViewUtils;
import com.pukka.ydepg.moudule.vod.utils.ReportBookmarkForMultUtils;

import java.util.Timer;
import java.util.TimerTask;


public class FreeCameraPlayActivity extends TVPlayBaseActivity implements
        HuaweiCloudPlayer.OnBufferingUpdateListener,
        HuaweiCloudPlayer.OnCompletionListener,
        HuaweiCloudPlayer.OnErrorListener,
        HuaweiCloudPlayer.OnPreparedListener,
        HuaweiCloudPlayer.OnCameraChangedListener {

    private HuaweiCloudPlayer huaweiCloudPlayer;

    private SurfaceView surfaceView;

    /**
     * 当前surfaceview状态
     */
    private boolean surfaceCreated;

    /**
     * 是否已经进行过播放
     */
    private boolean alreadyPrepareForUrl;

    private AllVideoConfig videoConfig;


    /**
     * 子弹时间标记
     */
    private boolean inStaticBulletTime;

    /**
     * 当前的缩放比例
     */
    private float currentScale = 1f;

    /**
     * 最大缩放
     */
    private final float MAX_SCALE = 3f;

    /**
     * 一次缩放偏移量
     */
    private final float MINI_SCALE_OFFSET = 0.1f;

    /**
     * 连续缩放最小偏移量
     */
    private final float MINI_CONTINUITY_SCALE_OFFSET = 0.016949f;

    /**
     * 一次位移偏移量
     */
    private final int MINI_MOVE_OFFSET = 40;

    /**
     * 连续位移最小偏移量
     */
    private final int MINI_CONTINUITY_MOVE_OFFSET = 10;

    /**
     * 鸟瞰图使用的View
     */
    private AerialView aerialView;
    /**
     * 自由时间View
     */
    private AngleView angleview;
    private ImageView aerialViewBg;

    /**
     * 是否已经进行过zoom
     */
    private boolean alreadyChangeZoom;

    /**
     * 当前位移偏移量
     */
    private int moveOffsetX;
    private int moveOffsetY;
    /**
     * 当前缩放偏移量
     */
    private float zoomOffset;

    /**
     * 长按放大缩放
     */
    private Timer mTimer;
    private TimerTask mTimerTask;

    /**
     * 长按调用zoom接口的时间间隔
     */
    private final int TERMINAL_ZOOM_TIME = 10;

    /**
     * 当前机位
     */
    private int cameraNum = -1;

    private LoadingTextView loadingView;

    /**
     * 是否处于停止旋转状态
     */
    protected boolean stopRotatingState = true;

    //跳过片头片尾
    private boolean canSetSkip;
    private int headDuration;
    private int tailDuration;
    String mVODID;
    private Handler mHandler;
    private VOD mVod;
    private FrameLayout mRootView;
    private ReportBookmarkForMultUtils mReportBookmarkForMultUtils;

    private BrowseEpsiodesUtils mBrowseEpsiodesUtils = new BrowseEpsiodesUtils();

    private VodPlayerControllerView vodPlayerControllerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_free);
        mReportBookmarkForMultUtils = new ReportBookmarkForMultUtils(new DetailPresenter(this));
        mVODID = getIntent().getStringExtra("CONTENTID");
        mVod = getIntent().getParcelableExtra("VOD");
        DebugLog.info(TAG, KPIConstant.KPI_FREEVIEW + "onCreate Activity");
        initCtrlView(R.id.playcontrolview);
        fullScreenMode = true;
        surfaceView = findViewById(R.id.surfaceview);
        downloadSpeed = findViewById(R.id.progress);
        aerialView = findViewById(R.id.aerialview);
        angleview = findViewById(R.id.angleview);
        mRootView = findViewById(R.id.root_view);
        if (!showCameraView) {
            angleview.setVisibility(View.GONE);
        }
        aerialViewBg = findViewById(R.id.aerialview_bg);
        Glide.with(this)
                .load(R.drawable.background)
                .thumbnail(0.8f)
                .override(ScreenUtil.dp2px(this, getResources().getDimension(R.dimen.aerialView_width)),
                        ScreenUtil.dp2px(this, getResources().getDimension(R.dimen.aerialView_height)))
                .into(aerialViewBg);
        loadingView = findViewById(R.id.loading_view);
        logcatViewStub = findViewById(R.id.logcat);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                DebugLog.info(TAG, "[surfaceCreated]");
                surfaceCreated = true;
                if (!alreadyPrepareForUrl) {
                    alreadyPrepareForUrl = true;
                    playFreeVideo();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                DebugLog.info(TAG, "[surfaceChanged]");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                DebugLog.info(TAG, "[surfaceDestroyed]");
                surfaceCreated = false;
            }
        });


        mHandler = new Handler() {
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
                    mVod = (VOD) data.getSerializable("episodeVod");
                    ShowBuyControl showBuyControl = new ShowBuyControl(FreeCameraPlayActivity.this);
                    showBuyControl.setCallBack(new ShowBuyControl.ShowBuyTagCallBack() {
                        @Override
                        public void showBuyTag(int showBuy) {

                        }
                    });
                    showBuyControl.setCallBack(new ShowBuyControl.ShowMultViewResponseCallBack() {
                        @Override
                        public void showBuyTag(PlayVODResponse response) {
                            MultViewUtils.refresh((String) data.get("SitcomNO"));
                            //url

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
//初始化一次
        showEpiodes(mVODID, this, mHandler);

        onBufferingUpdate = false;
        initPlayer();

        initLongPress();
        if (null != playControlView) {
            if (playControlView instanceof LandPlayControlView) {
//                playControlView = (LandPlayControlView) playControlView;
                ((LandPlayControlView) playControlView).getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (needSkip()) {
                            if (huaweiCloudPlayer.getDuration() - tailDuration * 1000 <= huaweiCloudPlayer.getCurrentPosition()) {
                                huaweiCloudPlayer.seek(huaweiCloudPlayer.getDuration());
                            }
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }
        }
    }

    private void initLongPress() {

    }

    private boolean needSkip() {
        headDuration = mVod.getMediaFiles().get(0).getHeadDuration();
        tailDuration = mVod.getMediaFiles().get(0).getTailDuration();


        /*跳过片头片尾开关
         * 0 为打开
         * 1 为关闭
         * 2 或者 不配置 或者 2 以外的值 为由用户自行设置
         */
        String skipSwitch = SessionService.getInstance().getSession().getTerminalConfigurationVOD_SKIP_SWITCH();
        if (null != skipSwitch && skipSwitch.equals("0")) {
            return true;
        } else if (null != skipSwitch && skipSwitch.equals("1")) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    protected void initPlayer() {
        DebugLog.debug(TAG, "[initPlayer]");
        DebugLog.info(TAG, KPIConstant.KPI_FREEVIEW + "createCommonPlayer");
        huaweiCloudPlayer = HuaweiPlayerFactory.createCommonPlayer(this);
        HuaweiPlayerFactory.setConsoleLog(hcpLogLevel);
        HuaweiPlayerFactory.setFileLog(hcpLogLevel, TVApplication.LOG_FILE_PATH);
        //开启crash捕获开关
        HuaweiPlayerFactory.setGlobalConfig(HCPGlobalConfig.CRASH_POLICY, crashPolicyConfig);
        huaweiCloudPlayer.setOnBufferingUpdateListener(this);
        huaweiCloudPlayer.setOnCompletionListener(this);
        huaweiCloudPlayer.setOnErrorListener(this);
        huaweiCloudPlayer.setOnPreparedListener(this);
        huaweiCloudPlayer.setOnCameraChangedListener(this);
        currentState = PlayState.NULL;
        String videoJson = getIntent().getStringExtra(PlayConstant.EXTRA);
        Gson gson = new Gson();
        isFromEPGGo2There = TextUtils.isEmpty(videoJson);
        DebugLog.info(TAG, "[initPlayer] isFromEPGGo2There : " + isFromEPGGo2There);
        if (isFromEPGGo2There) {
            videoConfig = GSonUtil.getAllVideoConfig(getIntent());
        } else {
            try {
                videoConfig = gson.fromJson(videoJson, AllVideoConfig.class);
            } catch (Exception e) {
                DebugLog.error(TAG, e.toString());
            }
        }
        DebugLog.info(TAG, "[initPlayer] videoConfig is null = " + (videoConfig == null));
        if (videoConfig != null) {

            if (showLogcat) {
                initLogcatRecyclerView();
                showJson(videoConfig.toString());
            }
            DebugLog.info(TAG, "[initPlayer] videoConfig=" + videoConfig.toString());
            //是否展示网速信息
            downloadSpeed.setVisibility(isShowSpeed ? View.VISIBLE : View.GONE);

            HCPConfig hcpConfig = new HCPConfig();
            hcpConfig.setMediaType((isLive = videoConfig.getMediaType() == 1) ? HCPMediaType.MEDIA_TYPE_LIVE : HCPMediaType.MEDIA_TYPE_VOD);
            hcpConfig.setPanoramic(videoConfig.getCameraDegree() == 360);
            hcpConfig.setMainCameraId(videoConfig.getMainCameraId());
            hcpConfig.setAutoStart(true);
            //这个是刘刚说的暂时先写死，防止有人忘记配置
//            if ("1".equals(videoConfig.getEnforceProgressiveScanning())) {
            DebugLog.info(TAG, "[initPlayer] setProgressiveScanFlag true");
            hcpConfig.setProgressiveScanFlag(true);
//            }
            if (!isLive) {
                hcpConfig.setBookmark(videoConfig.getBookmark());
            }
            DebugLog.info(TAG, KPIConstant.KPI_FREEVIEW + "setConfig");
            huaweiCloudPlayer.setConfig(hcpConfig);
            huaweiCloudPlayer.setDisplay(surfaceView);
            huaweiCloudPlayer.setLoop(videoConfig.isRecyclePlay());
            DebugLog.info(TAG, "surfaceCreated is Create " + surfaceCreated);
            inStaticBulletTime = false;
            DebugLog.info(TAG, KPIConstant.KPI_FREEVIEW + "isLive " + isLive);
            if (isShowSpeed) {
                uiHandler.sendMessage(uiHandler.obtainMessage(PlayConstant.WHAT_UPDATE_SPEED, String.valueOf(System.currentTimeMillis())));
            }
            if (!isLive) {
                uiHandler.sendEmptyMessage(PlayConstant.WHAT_UPDATE_TIME);
            }
        } else {
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

        if (null != huaweiCloudPlayer) {
            DebugLog.info(TAG, KPIConstant.KPI_FREEVIEW + "seek time=" + currentPlayPos);
            huaweiCloudPlayer.seek(time);
            huaweiCloudPlayer.start();
            currentState = PlayState.PLAYING;
            ctrlUIDoPause(false);
            if(null!=mReportBookmarkForMultUtils) {
                String isFilm = "";
                if(mBrowseEpsiodesUtils!=null)
                {
                    if(mBrowseEpsiodesUtils.getmVoddetail().getVODType().equals("0")||mBrowseEpsiodesUtils.getmVoddetail().getVODType().equals("2")){
                        isFilm="0";
                    }else
                    {
                        isFilm="1";
                    }

                }
                if(isBack)
                {
                    mReportBookmarkForMultUtils.reportBookmark(BookMarkSwitchs.FASTBACK, mVODID, "VOD", huaweiCloudPlayer.getCurrentPosition(), isFilm, mBrowseEpsiodesUtils.getSelesctedEpisode().getSitcomNO(), mBrowseEpsiodesUtils.getSelesctedEpisode().getVOD().getID(), huaweiCloudPlayer.getDuration(), false, mBrowseEpsiodesUtils.getmVoddetail());
                }else
                {
                    mReportBookmarkForMultUtils.reportBookmark(BookMarkSwitchs.FASTFORWARD, mVODID, "VOD", huaweiCloudPlayer.getCurrentPosition(), isFilm, mBrowseEpsiodesUtils.getSelesctedEpisode().getSitcomNO(), mBrowseEpsiodesUtils.getSelesctedEpisode().getVOD().getID(), huaweiCloudPlayer.getDuration(), false, mBrowseEpsiodesUtils.getmVoddetail());
                }
            }
        }
    }

    @Override
    protected void showOrHideControl(boolean show) {
        if (!showCameraView) {
            return;
        }
        if (show) {
            angleview.initAngle(videoConfig.getCameraNum(), videoConfig.getCameraDegree(), cameraNum == -1 ? videoConfig.getMainCameraId() : cameraNum);
            angleview.setVisibility(View.VISIBLE);
        } else if (stopRotatingState) {
            angleview.stopAni();
            angleview.setVisibility(View.GONE);
        }
    }

    @Override
    protected boolean playerMuteChange() {
        if (huaweiCloudPlayer != null) {
            huaweiCloudPlayer.setMute(!huaweiCloudPlayer.isMute());
            showToast(huaweiCloudPlayer.isMute() ? getString(R.string.play_mute) : getString(R.string.play_mute_dis));
            return true;
        }
        return false;
    }

    @Override
    protected void playerPlayOrPause(boolean toPlay) {
        if (huaweiCloudPlayer == null) {
            return;
        }
        DebugLog.info(TAG, "[playerPlayOrPause] isPlay " + toPlay);
        addLog("[playerPlayOrPause] isPlay " + toPlay, false);
        if (toPlay) {
            if(null!=mReportBookmarkForMultUtils) {
                String isFilm = "";
                if(mBrowseEpsiodesUtils!=null)
                {
                    if(mBrowseEpsiodesUtils.getmVoddetail().getVODType().equals("0")||mBrowseEpsiodesUtils.getmVoddetail().getVODType().equals("2")){
                        isFilm="0";
                    }else
                    {
                        isFilm="1";
                    }

                }
                mReportBookmarkForMultUtils.reportBookmark(BookMarkSwitchs.PAUSE, mVODID, "VOD", huaweiCloudPlayer.getCurrentPosition(), isFilm, mBrowseEpsiodesUtils.getSelesctedEpisode().getSitcomNO(), mBrowseEpsiodesUtils.getSelesctedEpisode().getVOD().getID(), huaweiCloudPlayer.getDuration(), false, mBrowseEpsiodesUtils.getmVoddetail());
            }
            stopStaticBulletTime();
        } else {
            go2StaticBulletTime();
            hideCtrlUI();
            showOrHideControl(false);
        }
    }

    @Override
    protected void dpadLeftDown(boolean leftDown, KeyEvent event) {
        int repeatCount = event.getRepeatCount();
        DebugLog.info(TAG, "[dpadLeftDown] leftDown=" + leftDown + " repeatCount=" + repeatCount);
        if (huaweiCloudPlayer != null && repeatCount == 0) {
            DebugLog.info(TAG, KPIConstant.KPI_FREEVIEW + "setCameraRotationTrack=" + (leftDown ? -1 : 1) + " dpadLeftDown");
            addLog("setRotationDirection=" + (leftDown ? -1 : 1), false);
            stopRotatingState = false;
            huaweiCloudPlayer.setRotationDirection(leftDown ? -1 : 1);
            if (!showCameraView) {
                return;
            }
            angleview.initAngle(videoConfig.getCameraNum(), videoConfig.getCameraDegree(), cameraNum == -1 ? videoConfig.getMainCameraId() : cameraNum);
            angleview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void dpadLRUp() {
        DebugLog.info(TAG, "[dpadLRUp] stopRotating");
        if (huaweiCloudPlayer != null) {
            DebugLog.info(TAG, KPIConstant.KPI_FREEVIEW + "stopRotating");
            addLog("[stopRotating]", false);
            stopRotatingState = true;
            huaweiCloudPlayer.stopRotating();
            if (null != uiHandler) {
                uiHandler.removeMessages(PlayConstant.WHAT_HIDE_TIME);
                uiHandler.sendEmptyMessageDelayed(PlayConstant.WHAT_HIDE_TIME, 3000);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DebugLog.info(TAG, "[onBackPressed]");
        addLog("[onBackPressed]", false);
        if (currentScale != 1f) {
            currentScale = 1f;
            DebugLog.info(TAG, "[onBackPressed] zoom");
            zoom(false, false);
            return;
        }

        if (inStaticBulletTime) {
            DebugLog.info(TAG, "[onBackPressed] stopStaticBulletTime");
            stopStaticBulletTime();
            return;
        }

        if (angleview != null && showCameraView) {
            angleview.stopAni();
            angleview.setVisibility(View.GONE);
        }

        if (isFromEPGGo2There) {
            DebugLog.info(TAG, "[onBackPressed] showExitDialog");
            videoConfig.setBookmark(huaweiCloudPlayer.getCurrentPosition());
            showExitDialog(this, videoConfig);
            return;
        }

        DebugLog.info(TAG, "[onBackPressed] PlayState.STOP");
        currentState = PlayState.STOP;
        if (null != huaweiCloudPlayer) {
            huaweiCloudPlayer.stop();
        }
        super.onBackPressed();
    }

    @Override
    protected void mediaDestroy() {

        if (null != mTimer) {
            mTimer.cancel();
        }
        if (angleview != null && showCameraView) {
            angleview.stopAni();
        }

        if (uiHandler != null) {
            uiHandler.removeMessages(PlayConstant.WHAT_UPDATE_TIME);
            uiHandler.removeMessages(PlayConstant.WHAT_UPDATE_SPEED);
            uiHandler.removeMessages(PlayConstant.WHAT_DO_ZOOM);
        }

        if (huaweiCloudPlayer != null) {
            if(null!=mReportBookmarkForMultUtils) {
                String isFilm = "";
                if(mBrowseEpsiodesUtils!=null)
                {
                    if(mBrowseEpsiodesUtils.getmVoddetail().getVODType().equals("0")||mBrowseEpsiodesUtils.getmVoddetail().getVODType().equals("2")){
                        isFilm="0";
                    }else
                    {
                        isFilm="1";
                    }

                }
                    mReportBookmarkForMultUtils.reportBookmark(BookMarkSwitchs.QUIT, mVODID, "VOD", huaweiCloudPlayer.getCurrentPosition(), isFilm, mBrowseEpsiodesUtils.getSelesctedEpisode().getSitcomNO(), mBrowseEpsiodesUtils.getSelesctedEpisode().getVOD().getID(), huaweiCloudPlayer.getDuration(), false, mBrowseEpsiodesUtils.getmVoddetail());
            }

            DebugLog.info(TAG, "[onBackPressed] PlayState.STOP");
            isFirstBufferSuccess = false;
            currentState = PlayState.STOP;
            huaweiCloudPlayer.destroy();
            alreadyPrepareForUrl = false;
            huaweiCloudPlayer = null;
        }
    }

    @Override
    public void onBufferingUpdate(HuaweiCloudPlayer huaweiCloudPlayer, int i) {
        DebugLog.info(TAG, "[onBufferingUpdate] " + i);
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
            DebugLog.info(TAG, "[onBufferingUpdate] 100  sendEmptyMessage WHAT_UPDATE_TIME");
            //用于seek后刷新UI进度条，播放器在seek后，获取的当前时间点还是seek前位置，导致进度条刷新会出现回退现象
            uiHandler.removeMessages(PlayConstant.WHAT_UPDATE_TIME);
            uiHandler.sendEmptyMessage(PlayConstant.WHAT_UPDATE_TIME);
        }
    }

    @Override
    public void onCompletion(HuaweiCloudPlayer huaweiCloudPlayer) {
        DebugLog.info(TAG, "[onCompletion]");
        addLog("[onCompletion]", false);
        showToast(getString(R.string.play_placomplete));
        if (isFromEPGGo2There) {
            videoConfig.setBookmark(huaweiCloudPlayer.getCurrentPosition());
            showExitDialog(this, videoConfig);
        } else {
            if (!videoConfig.isRecyclePlay()) {
                finish();
            }
        }
    }

    @Override
    public void onError(HuaweiCloudPlayer huaweiCloudPlayer, int i, String s) {
        DebugLog.error(TAG, "[onError] code=" + i + " msg=" + s);
        addLog("[onError] code=" + i + " msg=" + s, true);
        if (uiHandler != null) {
            uiHandler.removeMessages(PlayConstant.WHAT_UPDATE_TIME);
            uiHandler.removeMessages(PlayConstant.WHAT_UPDATE_SPEED);
            uiHandler.removeMessages(PlayConstant.WHAT_DO_ZOOM);
        }
        showToast("[onError] " + i + " " + s);
        if (loadingView != null) {
            loadingView.stopBufferingUpdate();
        }
    }

    @Override
    public void onPrepared(HuaweiCloudPlayer huaweiCloudPlayer) {
        DebugLog.info(TAG, "[onPrepared]");
        addLog("[onPrepared]", false);
        if (huaweiCloudPlayer != null) {
            if (Integer.parseInt(mVod.getBookmark().getRangeTime()) > headDuration) {
                huaweiCloudPlayer.seek(Integer.parseInt(mVod.getBookmark().getRangeTime()) * 1000);
            } else {
                if (needSkip()) {
                    huaweiCloudPlayer.seek(headDuration * 1000);
                }
            }
            if(null!=mReportBookmarkForMultUtils) {
                String isFilm = "";
                if(mBrowseEpsiodesUtils!=null)
                {
                    if(mBrowseEpsiodesUtils.getmVoddetail().getVODType().equals("0")||mBrowseEpsiodesUtils.getmVoddetail().getVODType().equals("2")){
                        isFilm="0";
                    }else
                    {
                        isFilm="1";
                    }

                }
                mReportBookmarkForMultUtils.reportBookmark(BookMarkSwitchs.START, mVODID, "VOD", huaweiCloudPlayer.getCurrentPosition(), isFilm, mBrowseEpsiodesUtils.getSelesctedEpisode().getSitcomNO(), mBrowseEpsiodesUtils.getSelesctedEpisode().getVOD().getID(), huaweiCloudPlayer.getDuration(), false, mBrowseEpsiodesUtils.getmVoddetail());
            }
            huaweiCloudPlayer.start();
        }
        currentState = PlayState.PLAYING;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (huaweiCloudPlayer != null) {
            if (currentState == PlayState.STOP) {
                DebugLog.info(TAG, "[onPause] stop");
                //如果当前的状态是停止状态，说明是退出播放操作,已经调用过stop操作
//                huaweiCloudPlayer.stop();
            } else {
                DebugLog.info(TAG, "[onPause] suspend");
                huaweiCloudPlayer.suspend();
                currentState = PlayState.PAUSE;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (huaweiCloudPlayer != null && onCreate) {
            DebugLog.info(TAG, "[onResume] resume");
            huaweiCloudPlayer.resume();
            if (!inStaticBulletTime) {
                currentState = PlayState.PLAYING;
            }
        }
        onCreate = true;
    }

    public void go2StaticBulletTime() {
        if (huaweiCloudPlayer != null) {
            showToast(getString(R.string.play_enter_bullettime));
            addLog(getString(R.string.play_enter_bullettime), false);
            DebugLog.info(TAG, KPIConstant.KPI_FREEVIEW + "startBulletTime");
            huaweiCloudPlayer.pause();
            inStaticBulletTime = true;
        }
    }

    public void stopStaticBulletTime() {
        if (huaweiCloudPlayer != null) {
            showToast(getString(R.string.play_out_bullettime));
            addLog(getString(R.string.play_out_bullettime), false);
            DebugLog.info(TAG, KPIConstant.KPI_FREEVIEW + "stopBulletTime");
            huaweiCloudPlayer.start();
            inStaticBulletTime = false;
            currentState = PlayState.PLAYING;
            ctrlUIDoPause(false);
        }
    }

    @Override
    protected boolean canClickPauseBtn() {
        return true;
    }

    @Override
    protected boolean canClickSeekBtn() {
        return !inStaticBulletTime;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!onBufferingUpdate) {
            //缓冲状态下,播放器sdk底层不响应此事件，所以UI在此处做好拦截
            return super.onKeyDown(keyCode, event);
        }
        boolean zoomChange = false;
        boolean moveChange = false;
        moveOffsetX = 0;
        moveOffsetY = 0;
        boolean repeatCount0 = event.getRepeatCount() == 0;
        boolean repeatCount1 = event.getRepeatCount() == 1;
        if (event.getRepeatCount() < 1) {
            alreadyChangeZoom = false;
            zoomOffset = 0;
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if (repeatCount1) {
                    zoomOffset = MINI_CONTINUITY_SCALE_OFFSET;
                    zoomChange = true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
//子集按下键弹出
                if (null != mVod && !mVod.getVODType().equals("0") && !mVod.getVODType().equals("2")) {
                    showView(mRootView);
                }
                if (repeatCount1) {
                    zoomOffset = -MINI_CONTINUITY_SCALE_OFFSET;
                    zoomChange = true;
                }

                break;
            case KeyEvent.KEYCODE_2:
                if (repeatCount0) {
                    break;
                }
                if (currentScale <= 1f) {
                    DebugLog.debug(TAG, "[onKeyUp] KEYCODE_2 currentScale=" + currentScale);
                    break;
                }
                moveOffsetY = (int) (MINI_CONTINUITY_MOVE_OFFSET * currentScale);
                moveChange = true;
                zoomChange = true;
                break;
            case KeyEvent.KEYCODE_8:
                if (repeatCount0) {
                    break;
                }
                if (currentScale <= 1f) {
                    DebugLog.debug(TAG, "[onKeyUp] KEYCODE_8 currentScale=" + currentScale);
                    break;
                }
                moveOffsetY = -(int) (MINI_CONTINUITY_MOVE_OFFSET * currentScale);
                moveChange = true;
                zoomChange = true;
                break;
            case KeyEvent.KEYCODE_4:
                if (repeatCount0) {
                    break;
                }
                if (currentScale <= 1f) {
                    DebugLog.debug(TAG, "[onKeyUp] KEYCODE_4 currentScale=" + currentScale);
                    break;
                }
                moveOffsetX = (int) (MINI_CONTINUITY_MOVE_OFFSET * currentScale);
                moveChange = true;
                zoomChange = true;
                break;
            case KeyEvent.KEYCODE_6:
                if (repeatCount0) {
                    break;
                }
                if (currentScale <= 1f) {
                    DebugLog.debug(TAG, "[onKeyUp] KEYCODE_6 currentScale=" + currentScale);
                    break;
                }
                moveOffsetX = -(int) (MINI_CONTINUITY_MOVE_OFFSET * currentScale);
                moveChange = true;
                zoomChange = true;
                break;
            case KeyEvent.KEYCODE_STAR:
//                if (repeatCount0 && huaweiCloudPlayer != null) {
//                    huaweiCloudPlayer.playFrameByFrame(1);
//                }
                break;
            case KeyEvent.KEYCODE_POUND:
//                if (repeatCount0 && huaweiCloudPlayer != null) {
//                    huaweiCloudPlayer.playFrameByFrame(-1);
//                }
                break;
        }
        if (zoomChange) {
            alreadyChangeZoom = true;
            zoom(moveChange, true);
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (!onBufferingUpdate) {
            //缓冲状态下,播放器sdk底层不响应此事件，所以UI在此处做好拦截
            return super.onKeyUp(keyCode, event);
        }
        boolean moveChange = false;
        boolean zoomChange = false;
        moveOffsetX = 0;
        moveOffsetY = 0;
        zoomOffset = 0;
        if (alreadyChangeZoom) {
            alreadyChangeZoom = false;
            endAutoZoom();
            return super.onKeyUp(keyCode, event);
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if (currentScale >= MAX_SCALE) {
                    DebugLog.info(TAG, "[onKeyUp] KEYCODE_PAGE_UP currentScale=" + currentScale);
                    break;
                }
                currentScale += MINI_SCALE_OFFSET;
                zoomOffset = MINI_SCALE_OFFSET;
                if (currentScale >= MAX_SCALE) {
                    currentScale = MAX_SCALE;
                }

                zoomChange = true;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (currentScale <= 1f) {
                    DebugLog.info(TAG, "[onKeyUp] KEYCODE_PAGE_DOWN currentScale=" + currentScale);
                    break;
                }
                currentScale -= MINI_SCALE_OFFSET;
                zoomOffset = -MINI_SCALE_OFFSET;
                if (currentScale <= 1f) {
                    currentScale = 1f;
                }

                zoomChange = true;
                break;
            case KeyEvent.KEYCODE_2:
                if (currentScale <= 1f) {
                    DebugLog.debug(TAG, "[onKeyDown] KEYCODE_2 currentScale=" + currentScale);
                    break;
                }
                moveOffsetY = (int) (MINI_MOVE_OFFSET * currentScale);
                moveChange = true;
                zoomChange = true;
                break;
            case KeyEvent.KEYCODE_8:
                if (currentScale <= 1f) {
                    DebugLog.debug(TAG, "[onKeyDown] KEYCODE_8 currentScale=" + currentScale);
                    break;
                }
                moveOffsetY = -(int) (MINI_MOVE_OFFSET * currentScale);
                moveChange = true;
                zoomChange = true;
                break;
            case KeyEvent.KEYCODE_4:
                if (currentScale <= 1f) {
                    DebugLog.debug(TAG, "[onKeyDown] KEYCODE_4 currentScale=" + currentScale);
                    break;
                }
                moveOffsetX = (int) (MINI_MOVE_OFFSET * currentScale);
                moveChange = true;
                zoomChange = true;
                break;
            case KeyEvent.KEYCODE_6:
                if (currentScale <= 1f) {
                    DebugLog.debug(TAG, "[onKeyDown] KEYCODE_6 currentScale=" + currentScale);
                    break;
                }
                moveOffsetX = -(int) (MINI_MOVE_OFFSET * currentScale);
                moveChange = true;
                zoomChange = true;
                break;
            case KeyEvent.KEYCODE_STAR:
            case KeyEvent.KEYCODE_POUND:
                if (huaweiCloudPlayer != null) {
//                    huaweiCloudPlayer.stopFrameByFrame();
                }
                break;
        }
        if (zoomChange) {
            zoom(moveChange, false);
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 进行缩放操作
     */
    private void zoom(boolean move, boolean isLongPressed) {

        if (currentScale > 1f || isLongPressed) {
            aerialViewBg.setVisibility(View.VISIBLE);
            aerialView.setVisibility(View.VISIBLE);
        } else {
            aerialViewBg.setVisibility(View.GONE);
            aerialView.setVisibility(View.GONE);
        }

        if (huaweiCloudPlayer != null) {
            DebugLog.debug(TAG, "[zoom] currentScale=" + currentScale);
            if (move) {
                DebugLog.info(TAG, KPIConstant.KPI_FREEVIEW + "move moveOffsetX=" + moveOffsetX + " moveOffsetY=" + moveOffsetY);
                addLog("move moveOffsetX=" + moveOffsetX + " moveOffsetY=" + moveOffsetY, false);
                huaweiCloudPlayer.move(moveOffsetX, moveOffsetY);
            } else {
                if (isLongPressed) {
                    beginAutoZoom();
                } else {
                    DebugLog.info(TAG, KPIConstant.KPI_FREEVIEW + "zoom X=" + (phoneWidth / 2) + " Y=" + (phoneHeight / 2) + " scaleOffset=" + zoomOffset + " isLongPressed=" + isLongPressed);
                    addLog("zoom X=" + (phoneWidth / 2) + " Y=" + (phoneHeight / 2) + " scaleOffset=" + zoomOffset + " isLongPressed=" + isLongPressed, false);
                    huaweiCloudPlayer.zoom(phoneWidth / 2, phoneHeight / 2, zoomOffset);
                }
            }

            HCPPicture currentPictureInfo = huaweiCloudPlayer.getCurrentPictureInfo();
            if (null != aerialView) {
                aerialView.updateData(currentPictureInfo.getMultiple(),
                        -currentPictureInfo.getX(), -currentPictureInfo.getY(),
                        phoneWidth, phoneHeight,
                        currentPictureInfo.getWidth(), currentPictureInfo.getHeight());
            }
        }
    }

    @Override
    protected void getPhoneWHSuccess() {

    }

    @Override
    protected void updatePlayerTime() {
        if (huaweiCloudPlayer != null) {
            currentPlayPos = huaweiCloudPlayer.getCurrentPosition();
            duration = huaweiCloudPlayer.getDuration();
            bufferedLength = huaweiCloudPlayer.getBufferedLength();
        }
    }

    /**
     * 检测view是否已经加载完成，并尝试进行播放
     *
     * @return
     */
    @Override
    protected boolean playFreeVideo() {
        if (surfaceCreated && huaweiCloudPlayer != null) {
            DebugLog.info(TAG, "[playFreeVideo] before prepareForUrl " + videoConfig.getResourcePlayURL().get(0));
            String prepareForUrl = videoConfig.getResourcePlayURL().get(0).replace("\"", "").replace("\"", "");
            DebugLog.info(TAG, KPIConstant.KPI_FREEVIEW + "prepareForUrl");
            huaweiCloudPlayer.prepareForUrl(prepareForUrl);
            return true;
        }
        return false;
    }

    @Override
    protected long getTotalDownloadByteNum() {
        return huaweiCloudPlayer == null ? 0 : huaweiCloudPlayer.getTotalDownloadByteNum();
    }

    /**
     * 进行长按缩放
     * 专门为机顶盒开发，handler、Animator等都不奏效，会很卡。
     * 此时的zoom接口，播放器会自动切换到主线程绘制Surface，但是UI只能使用handler更新UI，会卡顿。
     */
    private void beginAutoZoom() {
        endAutoZoom();
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                boolean shouldStop = false;
                boolean zoom = true;
                currentScale += zoomOffset;
                if (currentScale < 1f) {
                    zoomOffset = 0f;
                    currentScale = 1f;
                    shouldStop = true;
                }

                HCPPicture currentPictureInfo = huaweiCloudPlayer.getCurrentPictureInfo();

                if (currentScale >= MAX_SCALE) {
                    zoomOffset = MAX_SCALE - currentPictureInfo.getMultiple();
                    currentScale = MAX_SCALE;
                    shouldStop = true;
                    if (zoomOffset == 0) {
                        zoom = false;
                    }
                }
                if (zoom) {
                    DebugLog.info(TAG, KPIConstant.KPI_FREEVIEW + "zoom X=" + (phoneWidth / 2) + " Y=" + (phoneHeight / 2) + " scaleOffset=" + zoomOffset);
                    currentScale = huaweiCloudPlayer.zoom(phoneWidth / 2, phoneHeight / 2, zoomOffset);
                }
                uiHandler.sendEmptyMessage(PlayConstant.WHAT_DO_ZOOM);
                if (shouldStop) {
                    endAutoZoom();
                }
            }
        };
        mTimer.schedule(mTimerTask, 0, TERMINAL_ZOOM_TIME);
    }

    /**
     * 结束长按缩放
     */
    private void endAutoZoom() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    protected void refreshZoomUI() {
        if (huaweiCloudPlayer == null || aerialView == null) {
            return;
        }

        //如果缩小为1倍，
        if (currentScale <= 1f) {
            aerialViewBg.setVisibility(View.GONE);
            aerialView.setVisibility(View.GONE);
            return;
        }

        HCPPicture currentPictureInfo = huaweiCloudPlayer.getCurrentPictureInfo();
        aerialView.updateData(currentScale,
                -currentPictureInfo.getX(), -currentPictureInfo.getY(),
                phoneWidth, phoneHeight,
                currentPictureInfo.getWidth(), currentPictureInfo.getHeight());
    }

    @Override
    public void onCameraChanged(int cameraNum) {
        DebugLog.debug(TAG, "[onCameraChanged] cameraNum=" + cameraNum);
        addLog("[onCameraChanged] cameraNum=" + cameraNum, false);
        this.cameraNum = cameraNum;
        if (showCameraView && angleview.getVisibility() == View.VISIBLE) {
            angleview.updateData(cameraNum);
        }
    }

//    private void getSimpleEpsiodes() {
//        MultViewUtils.showEpiodes(mVODID, this, mhandler);
//    }

    public void showEpiodes(String mVODID, Context context, Handler handler) {
        vodPlayerControllerView = null;
        mBrowseEpsiodesUtils.getSimpleVod(mVODID, new VoddetailEpsiodePresenter.GetSimpleVodCallback() {
            @Override
            public void getSimpleVodSuccess(VODDetail vodDetail) {
                vodPlayerControllerView = new VodPlayerControllerView(context, false, false, vodDetail, mBrowseEpsiodesUtils, handler, vodDetail.getPrice(), 1f);
            }

            @Override
            public void getSimpleVodFail() {

            }
        });
    }

    public void showView(View ParentView) {
        if (vodPlayerControllerView != null) {
            vodPlayerControllerView.showList(ParentView);
        }
    }
}
