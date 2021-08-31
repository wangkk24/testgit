package com.pukka.ydepg.moudule.vrplayer.vrplayer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.asha.vrlib.MD360Director;
import com.asha.vrlib.MD360DirectorFactory;
import com.asha.vrlib.MDVRLibrary;
import com.pukka.ydepg.common.http.v6bean.v6node.BookMarkSwitchs;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayMultiMediaVODResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ShowBuyControl;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.play.freecamera.FreeCameraPlayActivity;
import com.pukka.ydepg.moudule.vod.activity.BrowseTVPlayFragment;
import com.pukka.ydepg.moudule.vod.playerController.VodPlayerControllerView;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.vod.presenter.VoddetailEpsiodePresenter;
import com.pukka.ydepg.moudule.vod.utils.BrowseEpsiodesUtils;
import com.pukka.ydepg.moudule.vod.utils.MultViewUtils;
import com.pukka.ydepg.moudule.vod.utils.ReportBookmarkForMultUtils;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.global.AreaSpecialProperties;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.global.Constant;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.global.MessageType;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.logic.FovController;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.logic.MediaController;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.module.VideoBean;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.utils.CommonTools;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.utils.LogUtil;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.view.MD360PlayerActivity;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.view.MediaPlayerWrapper;


import java.util.Locale;

/**
 * 功能描述
 *
 * @author l00477311
 * @since 2020-08-05
 */
public class VrPlayerActivity extends MD360PlayerActivity {
    private static final String TAG = "VrPlayerActivity";

    /**
     * 播放缓冲监听
     */
    private int mCurrentBufferPercentage = 0;

    private MDVRLibrary mMDVRLibrary;

    private MediaPlayerWrapper mMediaPlayerWrapper = new MediaPlayerWrapper();

    private boolean isPause = false;

    private int orignPitch = -90; // 初始角度为-90；

    private MediaController mpController;

    private FovController fovController;

    private boolean isLongPress = false;

    private int menuShowTime = AreaSpecialProperties.getMenuShowTime();

    private long lastBackKeyTime = 0; // 上次按返回键时间

    private long lastPauseKeyTime = 0; // 上次按暂停键时间

    private boolean onBackground = false; // 是否在后台

    private int lastPlayTime = 0; // 退后台时，视频播放的位置

    private float lastPicth = orignPitch; // 退后台时，水平方向视角偏移角度

    private float lastYaw = 0; // 退后台时，垂直方向视角偏移角度

    private float lastNearScale = 0; // 退后台时，镜头远近

    private boolean backKeyHashDown = false; // 如果按下返回键不是在此Activity中，就不处理返回键的松开事件

    private int mVideoDuration = 0;


    //视频是否已经准备好
    private boolean isVideoPrepared = false;


    //跳过片头片尾
    private boolean canSetSkip;
    private int headDuration;
    private int tailDuration;
    String mVODID;
    private Handler mEpsiodesHandler;
    private VOD mVod;
    private ReportBookmarkForMultUtils mReportBookmarkForMultUtils;

    private BrowseEpsiodesUtils mBrowseEpsiodesUtils = new BrowseEpsiodesUtils();

    private VodPlayerControllerView vodPlayerControllerView;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessageType.MenuControlMessage.MESSAGE_SHOW_PROGRESS:
                    if (getVideoBean().getMediaType() == Constant.BTV) {
                        break;
                    }
                    long pos = syncProgress();
                    msg = obtainMessage(MessageType.MenuControlMessage.MESSAGE_SHOW_PROGRESS);
                    sendMessageDelayed(msg, 1000 - (pos % 1000));
                    break;
                case MessageType.MenuControlMessage.MESSAGE_HIDE_MENU:
                    hideMenu();
                    break;
                case MessageType.MenuControlMessage.MESSAGE_PLAY_NEXT:
                    mpController.playNext(getVideoBean());
                    finish();

                    break;
                case MessageType.MenuControlMessage.MESSAGE_DRAG_SEEKBAR_FORWOARD:
                    dragPlaySeekBar(AreaSpecialProperties.getLongSeekSpeed() / 2, "percent");
                    msg = obtainMessage(MessageType.MenuControlMessage.MESSAGE_DRAG_SEEKBAR_FORWOARD);
                    sendMessageDelayed(msg, Constant.LOOP_SETP_TIME);
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
                        mReportBookmarkForMultUtils.reportBookmark(BookMarkSwitchs.FASTFORWARD, mVODID, "VOD", mMediaPlayerWrapper.getCurrentPosition(), isFilm, mBrowseEpsiodesUtils.getSelesctedEpisode().getSitcomNO(), mBrowseEpsiodesUtils.getSelesctedEpisode().getVOD().getID(), mMediaPlayerWrapper.getDuration(), false, mBrowseEpsiodesUtils.getmVoddetail());
                    }
                    break;
                case MessageType.MenuControlMessage.MESSAGE_DRAG_SEEKBAR_REWIND:
                    dragPlaySeekBar(-AreaSpecialProperties.getLongSeekSpeed() / 2, "percent");
                    msg = obtainMessage(MessageType.MenuControlMessage.MESSAGE_DRAG_SEEKBAR_REWIND);
                    sendMessageDelayed(msg, Constant.LOOP_SETP_TIME);
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
                        mReportBookmarkForMultUtils.reportBookmark(BookMarkSwitchs.FASTBACK, mVODID, "VOD", mMediaPlayerWrapper.getCurrentPosition(), isFilm, mBrowseEpsiodesUtils.getSelesctedEpisode().getSitcomNO(), mBrowseEpsiodesUtils.getSelesctedEpisode().getVOD().getID(), mMediaPlayerWrapper.getDuration(), false, mBrowseEpsiodesUtils.getmVoddetail());
                    }

                    break;
                case MessageType.MenuControlMessage.MESSAGE_START_PLYAER:
                    mMediaPlayerWrapper.openRemoteFile(getVideoBean().getResourcePlayURL().get(0));
                    mMediaPlayerWrapper.prepare();
                    //此处应该加载跳过片头的处理
                    if(needSkip())
                    {
                        mMediaPlayerWrapper.setSeekHead(headDuration * 1000);
                    }
                    break;
                default:
                    break;
            }
        }
    };


    //MARK:TODO -- MrLee
    private static final int KEY_UP = 0x0001;
    private static final int KEY_LONG_PRESS = 0x0002;
    private static final int KEY_DOWN = 0x0003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.logI(TAG, "Enter onCreate " + this.toString());
        super.onCreate(savedInstanceState);
        if (getVideoBean().getResourceType() == Constant.TWO_DIM_180) {
            orignPitch = 0;
            lastPicth = orignPitch;
        }
        if (getIntent() != null) {
            mVODID = getIntent().getStringExtra("CONTENTID");
            mVod = getIntent().getParcelableExtra("VOD");
        }
        mReportBookmarkForMultUtils = new ReportBookmarkForMultUtils(new DetailPresenter(this));
        initMediaPlayer();
        mMDVRLibrary = getVRLibrary();
        // 只有是3D的时候，才区分上下和左右
        if (getVideoBean().getResourceType() == 5 || getVideoBean().getResourceType() == 6) {
            set3DProjectionMode(getVideoBean().getType3D());
        }
        // 自动隐藏菜单
        mHandler.sendEmptyMessageDelayed(MessageType.MenuControlMessage.MESSAGE_HIDE_MENU, 1000 * menuShowTime);
        // 注册控制器
        registerLogics();
        LogUtil.logI(TAG, "Exit onCreate " + this.toString());
        mEpsiodesHandler = new Handler() {
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
                    ShowBuyControl showBuyControl = new ShowBuyControl(VrPlayerActivity.this);
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
        //初始化进度条
        if(null!=getPlaySeekbar())
        {
            getPlaySeekbar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (needSkip()) {
                        if (mMediaPlayerWrapper.getDuration() - tailDuration * 1000 <= mMediaPlayerWrapper.getCurrentPosition()) {
                            mMediaPlayerWrapper.seek(mMediaPlayerWrapper.getDuration());
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

//初始化一次
        showEpiodes(mVODID, this, mEpsiodesHandler);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.logI(TAG, "Enter onNewIntent" + this.toString());
        VideoBean videoBean = CommonTools.parseVideoInfoByIntent(intent);
        if (videoBean == null || getVideoBean().equals(videoBean)) { // 播放地址不一致，销毁当前Activity，重新创建
            return;
        }
        finish();
        startVideo(this, videoBean);
        LogUtil.logI(TAG, "Exit onNewIntent" + this.toString());
    }

    @Override
    protected void onStart() {
        LogUtil.logI(TAG, "Enter onStart " + this.toString());
        super.onStart();
        if (onBackground) { // 从后台切到前台
            mHandler.sendEmptyMessage(MessageType.MenuControlMessage.MESSAGE_START_PLYAER);
        }
        onBackground = false;
        LogUtil.logI(TAG, "Exit onStart " + this.toString());
    }

    @Override
    protected void onResume() {
        LogUtil.logI(TAG, "Enter onResume" + this.toString());
        super.onResume();
        LogUtil.logI(TAG, "Exit onResume" + this.toString());
    }

    @Override
    protected void onPause() {
        LogUtil.logI(TAG, "Enter onPause " + this.toString());
        super.onPause();
        LogUtil.logI(TAG, "Exit onPause " + this.toString());
    }

    @Override
    protected void onStop() {
        LogUtil.logI(TAG, "Enter onStop " + this.toString());
        super.onStop();
        resetPlay();
        onBackground = true;
        busy();
        LogUtil.logI(TAG, "Exit onStop " + this.toString());
    }

    private void resetPlay() {
        mHandler.removeMessages(MessageType.MenuControlMessage.MESSAGE_SHOW_PROGRESS);
        mHandler.removeMessages(MessageType.MenuControlMessage.MESSAGE_START_PLYAER);
        lastPlayTime = mMediaPlayerWrapper.getCurrentPosition();
        if (mMediaPlayerWrapper.hasPrePared()) {
            lastPicth = mMDVRLibrary.updateCamera().getPitch();
            lastYaw = mMDVRLibrary.updateCamera().getYaw();
            lastNearScale = mMDVRLibrary.updateCamera().getNearScale();
        }
        mMediaPlayerWrapper.reset(); // 退后台重置播放器
    }

    @Override
    protected void onDestroy() {
        LogUtil.logI(TAG, "Enter onDestroy " + this.toString());
        super.onDestroy();
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
            mReportBookmarkForMultUtils.reportBookmark(BookMarkSwitchs.DESTORY, mVODID, "VOD", mMediaPlayerWrapper.getCurrentPosition(), isFilm, mBrowseEpsiodesUtils.getSelesctedEpisode().getSitcomNO(), mBrowseEpsiodesUtils.getSelesctedEpisode().getVOD().getID(), mMediaPlayerWrapper.getDuration(), false, mBrowseEpsiodesUtils.getmVoddetail());
        }
        mMediaPlayerWrapper.destroy();
        mHandler.removeCallbacksAndMessages(null);
        unregisterLogics();
        LogUtil.logI(TAG, "Exit onDestroy " + this.toString());
    }

    @Override
    protected MDVRLibrary createVRLibrary() {
        return MDVRLibrary.with(this)
                .displayMode(MDVRLibrary.DISPLAY_MODE_NORMAL) // 默认360度全景
                .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_TOUCH) // 触摸
                .projectionMode(MDVRLibrary.PROJECTION_MODE_SPHERE) // 投影模式：默认球面
                .asVideo(surface -> {
                    LogUtil.logI(TAG, "onSurfaceReady");
                    mMediaPlayerWrapper.setSurface(surface);
                })
                .ifNotSupport(mode -> {
                    String tip = mode == MDVRLibrary.INTERACTIVE_MODE_MOTION
                            ? "onNotSupport:MOTION" : "onNotSupport:" + String.valueOf(mode);
                    Toast.makeText(VrPlayerActivity.this, tip, Toast.LENGTH_SHORT).show();
                })
                .directorFactory(new MD360DirectorFactory() {
                    @Override
                    public MD360Director createDirector(int index) {
                        LogUtil.logI(TAG, "createDirector: " + index);
                        return MD360Director.builder().setPitch(0).build();
                    }
                })
                .build(getGlSurfaceView());
    }

    private void initMediaPlayer() {
        mMediaPlayerWrapper.init();
        mMediaPlayerWrapper.setPreparedListener(mp -> {
            LogUtil.logI(TAG, "mediaPlayer prepared, orignPitch = " + orignPitch);
            mMediaPlayerWrapper.setHasPrepared();
            mHandler.sendEmptyMessage(MessageType.MenuControlMessage.MESSAGE_SHOW_PROGRESS);
            cancelBusy();
            isVideoPrepared = true;
            if (getVRLibrary() != null) {
                getVRLibrary().notifyPlayerChanged();
                getVRLibrary().updateCamera().setPitch(lastPicth).setYaw(lastYaw).setNearScale(lastNearScale);
            }
            if (lastPlayTime > AreaSpecialProperties.getSingleSeekTime()) { // 上次退后台的播放时大于单次seek值，回前台时seek到上次播放时间
                Message msg = new Message();
                msg.what = MessageType.MPControlMessage.MESSAGE_MPCONTROL_SEEK_TO;
                msg.arg1 = lastPlayTime;
                mpController.sendMessage(msg);
            }
            if (isPause) {
                mMediaPlayerWrapper.pause();
                // 规避海思pause/resume出问题，暂停时重置播放器
                resetPlay();
            }
        });
        mMediaPlayerWrapper.setOnErrorListener((mp, what, extra) -> {
            String error = String.format(Locale.ROOT, "Play Error what=%d extra=%d", what, extra);
            Toast.makeText(VrPlayerActivity.this, error, Toast.LENGTH_SHORT).show();
            LogUtil.logI(TAG, error);
            return true;
        });
        mMediaPlayerWrapper.setOnVideoSizeChangedListener((mp, width, height) -> {
            getVRLibrary().onTextureResize(width, height);
        });
        mMediaPlayerWrapper.setOnInfoListener((mp, what, extra) -> {
            LogUtil.logI(TAG, "onInfo what = " + what);
            return false;
        });
        mMediaPlayerWrapper.setOnBufferingUpdateListener((mp, percent) -> {
            mCurrentBufferPercentage = percent;
            LogUtil.logI(TAG, "percent = " + percent);
        });
        mMediaPlayerWrapper.setOnCompletionListener(mp -> {
            LogUtil.logI(TAG, "video play compelet");
            mHandler.sendEmptyMessage(MessageType.MenuControlMessage.MESSAGE_PLAY_NEXT);
        });
        mMediaPlayerWrapper.setOnSeekCompleteListener(mp -> {
            LogUtil.logI(TAG, "seek Complete");
            if (!isPause) { // seek前是播放状态，seek后恢复播放
                mMediaPlayerWrapper.resume();
            }
            mHandler.sendEmptyMessage(MessageType.MenuControlMessage.MESSAGE_SHOW_PROGRESS); // 同步进度条
            mHandler.sendEmptyMessageDelayed(MessageType.MenuControlMessage.MESSAGE_HIDE_MENU, 1000 * menuShowTime);
        });
        mHandler.sendEmptyMessageDelayed(MessageType.MenuControlMessage.MESSAGE_START_PLYAER, Constant.LOOP_SETP_TIME * 2);
    }

    private void set3DProjectionMode(int mode) {
        if (mode == Constant.STEREO_HORIZONTAL) {
            mMDVRLibrary.switchProjectionMode(this, MDVRLibrary.PROJECTION_MODE_STEREO_SPHERE_HORIZONTAL);
        } else if (mode == Constant.STEREO_VERTICAL) {
            mMDVRLibrary.switchProjectionMode(this, MDVRLibrary.PROJECTION_MODE_STEREO_SPHERE_VERTICAL);
        }
    }

    private void registerLogics() {
        mpController = new MediaController();
        mpController.init(this, mMediaPlayerWrapper);
        fovController = new FovController();
        fovController.init(this, mMDVRLibrary);
    }

    private void unregisterLogics() {
        mpController.destroy();
        fovController.destroy();
    }

    // 同步播放进度条
    private int syncProgress() {
        int position = mMediaPlayerWrapper.getCurrentPosition();
        mVideoDuration = mMediaPlayerWrapper.getDuration();
        if (mVideoDuration > 0) {
            int pos = (int) 1000L * position / mVideoDuration;
            getPlaySeekbar().setProgress(pos);
        }
        getPlaySeekbar().setSecondaryProgress(mCurrentBufferPercentage * 10);

        getCurrentTimeTv().setText(CommonTools.generateTime(position));
        getEndTimeTv().setText(CommonTools.generateTime(mVideoDuration));
        return position;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtil.logI(TAG, "Enter onKeyDown，keyCode = " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                backKeyHashDown = true;
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:

            case KeyEvent.KEYCODE_DPAD_CENTER:  // 控制暂停
                handlerPauseKey();
                return true;
            //MARK:TODO -- 屏蔽频道+、- 进度条拖动功能
//            case KeyEvent.KEYCODE_CHANNEL_UP: // 频道+
//            case KeyEvent.KEYCODE_CHANNEL_DOWN: // 频道-


            case KeyEvent.KEYCODE_MEDIA_REWIND: // 快退
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD: // 快进
                event.startTracking(); // 只有执行了这行代码才会调用onKeyLongPress
                if (event.getRepeatCount() == 0) { // 按键第一次
                    isLongPress = false;
                    prepareSeekOrSwitch(keyCode);
                }
                return true;
//            case KeyEvent.KEYCODE_DPAD_UP:
//            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                //MARK:TODO -- 左键右键修改为    呼出进度条，可以seek操作， 进度条消失，可以旋转


//                if (mCurrentBufferPercentage == 0) {
//                    return true;
//                }
                if (isVideoPrepared == false || null == touchRl) {
                    return true;
                }

                //判断是否有进度条显示
                if (touchRl.getVisibility() == View.VISIBLE) {
                    //有进度条， 进行seek操作
                    event.startTracking(); // 只有执行了这行代码才会调用onKeyLongPress
                    if (event.getRepeatCount() == 0) { // 按键第一次
                        isLongPress = false;
                        prepareSeekOrSwitch(keyCode);
                    }
                    return true;
                } else {
                    //不显示进度条，可以进行旋转操作
                    // fov视角调整
                    event.startTracking();
                    if (event.getRepeatCount() == 0) {
                        isLongPress = false;
                        LogUtil.logI(TAG, "start Fov single control, pitch=" + mMDVRLibrary.updateCamera().getPitch()
                                + ", yaw=" + mMDVRLibrary.updateCamera().getYaw());
                    }
                    return true;
                }

//            case KeyEvent.KEYCODE_PAGE_UP:
//            case KeyEvent.KEYCODE_PAGE_DOWN:
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (null != mVod && !mVod.getVODType().equals("0") && !mVod.getVODType().equals("2")) {
                    if (touchRl.getVisibility() == View.VISIBLE) {
//如果进度条显示，显示剧集
                        showView(mRootView);

                    }
                } else {

                    // 镜头拉远拉近
                    float nearScale = mMDVRLibrary.updateCamera().getNearScale();
                    LogUtil.logI(TAG, "start camara lens control, nearScale=" + (1 - nearScale));
                    Message message = new Message();
                    message.what = MessageType.FovControlMessage.MESSAGE_FOVCONTROL_NEARCSCALE;
                    message.arg1 = keyCode;
                    fovController.sendMessage(message);
                }

                return true;


            //MARK:TODO -- MrLee
            //MARK:TODO
            case KeyEvent.KEYCODE_2: //按键2，上
            case KeyEvent.KEYCODE_4: //按键4，左
            case KeyEvent.KEYCODE_6: //按键6，右
            case KeyEvent.KEYCODE_8: //按键8，下

//                if (mCurrentBufferPercentage == 0) {
//                    return true;
//                }
                if (isVideoPrepared == false) {
                    return true;
                }
                // fov视角调整
                event.startTracking();
                if (event.getRepeatCount() == 0) {
                    isLongPress = false;
                    LogUtil.logI(TAG, "start Fov single control, pitch=" + mMDVRLibrary.updateCamera().getPitch()
                            + ", yaw=" + mMDVRLibrary.updateCamera().getYaw());
                }
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        LogUtil.logI(TAG, "Enter onKeyLongPress, keyCode = " + keyCode);
        switch (keyCode) {

            //MARK:TODO -- 屏蔽频道+、- 进度条拖动功能
//            case KeyEvent.KEYCODE_CHANNEL_UP:

            //MARK:TODO -- 右键， 没有进度条，则可以直接 旋转。 有进度条，则直接 seek
            case KeyEvent.KEYCODE_DPAD_RIGHT: //快进
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:

//                if (mCurrentBufferPercentage == 0) {
//                    return true;
//                }

                if (isVideoPrepared == false) {
                    return true;
                }
                if (null == touchRl) {
                    return true;
                }
                //如果是  右键，并且不显示进度条，则直接旋转
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && touchRl.getVisibility() != View.VISIBLE) {
                    //旋转操作
                    handleNumberKey(keyCode, KEY_LONG_PRESS);
                    return true;
                }


                if (getVideoBean().getMediaType() == Constant.VOD) { // 点播seek，长按键操作进度条
                    // 暂停播放
                    mMediaPlayerWrapper.pause();
                    mHandler.sendEmptyMessage(MessageType.MenuControlMessage.MESSAGE_DRAG_SEEKBAR_FORWOARD);
                } else { // 直播切台
                    // todo
                }
                isLongPress = true;
                return true;
            //MARK:TODO -- 屏蔽频道+、- 进度条拖动功能
//            case KeyEvent.KEYCODE_CHANNEL_DOWN:

            case KeyEvent.KEYCODE_DPAD_LEFT://快退
            case KeyEvent.KEYCODE_MEDIA_REWIND:
//                if (mCurrentBufferPercentage == 0) {
//                    return true;
//                }

                if (isVideoPrepared == false || null == touchRl) {
                    return true;
                }


                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && touchRl.getVisibility() != View.VISIBLE) {
                    //旋转操作
                    handleNumberKey(keyCode, KEY_LONG_PRESS);
                    return true;
                }

                if (getVideoBean().getMediaType() == Constant.VOD) { // 点播seek，长按键操作进度条
                    // 暂停播放
                    mMediaPlayerWrapper.pause();
                    mHandler.sendEmptyMessage(MessageType.MenuControlMessage.MESSAGE_DRAG_SEEKBAR_REWIND);
                } else { // 直播切台
                    // todo
                }
                isLongPress = true;
                return true;

//            case KeyEvent.KEYCODE_DPAD_UP:
//            case KeyEvent.KEYCODE_DPAD_DOWN:
//            case KeyEvent.KEYCODE_DPAD_LEFT:
//            case KeyEvent.KEYCODE_DPAD_RIGHT:
//                fovController.startFovCruise(keyCode);
//                isLongPress = true;
//                return true;

            //MARK:TODO -- 位移
            case KeyEvent.KEYCODE_2: //按键2，上
            case KeyEvent.KEYCODE_4: //按键4，左
            case KeyEvent.KEYCODE_6: //按键6，右
            case KeyEvent.KEYCODE_8: //按键8，下
//                fovController.startFovCruise(keyCode);
//                isLongPress = true;

//                if (mCurrentBufferPercentage == 0) {
//                    return true;
//                }
                if (isVideoPrepared == false) {
                    return true;
                }
                handleNumberKey(keyCode, KEY_LONG_PRESS);
                return true;
            default:
                break;
        }

        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        LogUtil.logI(TAG, "Enter onKeyUp, keyCode = " + keyCode);

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //MARK:TODO -- 修改返回键   -- 不再默认视角，回到默认视角，  如果是默认视角，则弹出 退出Alert框
                if (!backKeyHashDown) {
                    return true;
                }

                backKeyHashDown = false;
                if (mMediaPlayerWrapper.hasPrePared() && !fovController.isFovInDefault(orignPitch)) {
                    // 视角恢复原点
                    Message message = new Message();
                    message.what = MessageType.FovControlMessage.MESSAGE_FOVCONTROL_RESET;
                    message.arg1 = orignPitch;
                    fovController.sendMessage(message);
                    return true;
                }


                //MARK:TODO -- 原来功能：2,5秒之内，再次按返回键，弹出Toast提示。
                //修改为： 如果是默认视角则，弹出弹框

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示：");
                builder.setMessage("您确定退出？");

                //设置确定按钮
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VideoBean VideoBean = new VideoBean();
                        final String Url = VideoBean.Url();
                        Intent intent = new Intent();
                        intent.putExtra(Constant.URL, Url);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                //设置取消按钮
                builder.setPositiveButton("取消", null);
                //显示提示框
                builder.show();
                return true;


            //MARK:TODO - 屏蔽频道+、- 进度条拖动功能
//            case KeyEvent.KEYCODE_CHANNEL_UP:
//            case KeyEvent.KEYCODE_CHANNEL_DOWN:


            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
            case KeyEvent.KEYCODE_MEDIA_REWIND: // seek键
                if (isLongPress) {
                    handlerSeekLongKeyUp();
                } else {
                    boolean rewind = false;
//                    if (keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN || keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
//                        rewind = true;
//                    }
                    //MARK:TODO -- MrLee
                    if (keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
                        rewind = true;
                    }
                    int deta = AreaSpecialProperties.getSingleSeekTime() * (rewind ? -1 : 1);
                    handlerSeekShortKeyUp(deta);
                }
                isLongPress = false;
                return true;
            //MARK: TODO -- 功能修改，没进度条的时候 旋转   有进度条的时候  seek
            //
            case KeyEvent.KEYCODE_DPAD_RIGHT: //右键
            case KeyEvent.KEYCODE_DPAD_LEFT: //左键
//                if (mCurrentBufferPercentage == 0) {
//                    return true;
//                }
                if (isVideoPrepared == false || null == touchRl) {
                    return true;
                }

                if (touchRl.getVisibility() == View.VISIBLE) {
                    if (isLongPress) {
                        handlerSeekLongKeyUp();
                    } else {
                        boolean rewind = false;
//                    if (keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN || keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
//                        rewind = true;
//                    }
                        //MARK:TODO --
                        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            rewind = true;
                        }
                        int deta = AreaSpecialProperties.getSingleSeekTime() * (rewind ? -1 : 1);
                        handlerSeekShortKeyUp(deta);
                    }
                    isLongPress = false;
                    return true;
                } else {
                    //旋转操作
                    handleNumberKey(keyCode, KEY_UP);
                    return true;
                }


//            case KeyEvent.KEYCODE_DPAD_UP:
//            case KeyEvent.KEYCODE_DPAD_DOWN:
//            case KeyEvent.KEYCODE_DPAD_LEFT:
//            case KeyEvent.KEYCODE_DPAD_RIGHT:
//                if (isLongPress) {
//                    fovController.stopFovCruise();
//                } else { // 单次按键
//                    fovController.singleFovRotate(keyCode);
//                }
//                isLongPress = false;
//                return true;
                //MARK: TODO -- 呼出菜单
            case KeyEvent.KEYCODE_MENU:
                if (null == touchRl) {
                    LogUtil.logE(TAG, "null == touchRl");
                    break;
                }

                //判断是否有进度条显示
                if (touchRl.getVisibility() == View.VISIBLE) {
                    //进度条可见，则隐藏进度条
                    hideMenu();
                } else {
                    //进度条不可见，则显示进度条
                    //菜单键，呼出进度条，无操作情况下，5秒后隐藏进度条（如何判断5秒之内没有操作）
                    mHandler.removeMessages(MessageType.MenuControlMessage.MESSAGE_HIDE_MENU);
                    showMenu();
                    mHandler.sendEmptyMessageDelayed(MessageType.MenuControlMessage.MESSAGE_HIDE_MENU, 1000 * 5);// 5秒后隐藏菜单
                }
                break;

            //MARK:TODO -- 位移功能
            case KeyEvent.KEYCODE_2: //按键2，上
            case KeyEvent.KEYCODE_4: //按键4，左
            case KeyEvent.KEYCODE_6: //按键6，右
            case KeyEvent.KEYCODE_8: //按键8，下
//                if (mCurrentBufferPercentage == 0) {
//                    return true;
//                }
                if (isVideoPrepared == false) {
                    return true;
                }
                handleNumberKey(keyCode, KEY_UP);
                return true;
            default:
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void prepareSeekOrSwitch(int keyCode) {
        if (getVideoBean().getMediaType() == Constant.VOD) { // 点播seek操作
            mHandler.removeMessages(MessageType.MenuControlMessage.MESSAGE_SHOW_PROGRESS); // 停止同步进度条
            // 展示菜单
            mHandler.removeMessages(MessageType.MenuControlMessage.MESSAGE_HIDE_MENU);
            showMenu();
        } else { // 直播switch切台操作
            // todo
        }
    }


    // 处理seek长按键UP操作
    private void handlerSeekLongKeyUp() {
        if (getVideoBean().getMediaType() == Constant.VOD) { // 点播状态seek
            LogUtil.logI(TAG, "seek long key up");
            mHandler.removeMessages(MessageType.MenuControlMessage.MESSAGE_DRAG_SEEKBAR_FORWOARD); // 停止拖进度条
            mHandler.removeMessages(MessageType.MenuControlMessage.MESSAGE_DRAG_SEEKBAR_REWIND);
            if (isPause) { // 暂停状态下seek等resume时再seek
                return;
            }
            mpController.seekBySeekBarTime(getPlaySeekbar());
        } else { // 直播切台
            // todo
        }
    }

    // 处理seek短按键UP操作
    private void handlerSeekShortKeyUp(int deta) {
        if (getVideoBean().getMediaType() == Constant.VOD) { // 点播状态seek
            dragPlaySeekBar(deta, "second");
            if (!isPause) { // 非pause状态seek
                mpController.seekBySeekBarTime(getPlaySeekbar());
            }
        } else { // 直播切台
            // todo
        }
    }

    // 处理暂停键
    private void handlerPauseKey() {
        long currentTimeMillis = System.currentTimeMillis();
        long interval = currentTimeMillis - lastPauseKeyTime;
        if (interval < 500 || !mMediaPlayerWrapper.hasPrePared()) {
            LogUtil.logI(TAG, "press pause Key too frequently, ignore：hasPrepared = " + mMediaPlayerWrapper.hasPrePared());
            return;
        }
        lastPauseKeyTime = currentTimeMillis;
        mHandler.removeMessages(MessageType.MenuControlMessage.MESSAGE_HIDE_MENU);
        showMenu();
        mHandler.sendEmptyMessageDelayed(MessageType.MenuControlMessage.MESSAGE_HIDE_MENU, 1000 * menuShowTime);// 3秒后隐藏菜单
        if (getVideoBean().getMediaType() == Constant.BTV) { // 直播无暂停
            return;
        }
        if (mMediaPlayerWrapper.getPlayer().isPlaying()) {
            LogUtil.logI(TAG, "pause Key, puase player");
            mMediaPlayerWrapper.pause();
            showPauseIcon();
            resetPlay();
            isPause = true;
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
                    mReportBookmarkForMultUtils.reportBookmark(BookMarkSwitchs.PAUSE, mVODID, "VOD", mMediaPlayerWrapper.getCurrentPosition(), isFilm, mBrowseEpsiodesUtils.getSelesctedEpisode().getSitcomNO(), mBrowseEpsiodesUtils.getSelesctedEpisode().getVOD().getID(), mMediaPlayerWrapper.getDuration(), false, mBrowseEpsiodesUtils.getmVoddetail());
            }
        } else {
            LogUtil.logI(TAG, "pause Key, resume player");
            // 规避海思暂停恢复出问题，暂停恢复重新起播
            lastPlayTime = (int) ((getPlaySeekbar().getProgress() * mVideoDuration) / 1000);
            lastPicth = mMDVRLibrary.updateCamera().getPitch();
            lastYaw = mMDVRLibrary.updateCamera().getYaw();
            lastNearScale = mMDVRLibrary.updateCamera().getNearScale();
            mHandler.sendEmptyMessage(MessageType.MenuControlMessage.MESSAGE_START_PLYAER);
            isPause = false;
            hidePauseIcon();
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
                mReportBookmarkForMultUtils.reportBookmark(BookMarkSwitchs.START, mVODID, "VOD", mMediaPlayerWrapper.getCurrentPosition(), isFilm, mBrowseEpsiodesUtils.getSelesctedEpisode().getSitcomNO(), mBrowseEpsiodesUtils.getSelesctedEpisode().getVOD().getID(), mMediaPlayerWrapper.getDuration(), false, mBrowseEpsiodesUtils.getmVoddetail());
            }
        }
    }

    // 拖拽进度条
    private void dragPlaySeekBar(int deta, String unit) {
        LogUtil.logI(TAG, "dragPlayerSeekBar, deta =" + deta + ",unit =" + unit);
        int curPostion = getPlaySeekbar().getProgress();
        long postion = 0;
        if (unit.equals("percent")) { // 百分比,实际转成千分比
            postion = ((long) curPostion + deta) * mVideoDuration / 1000;
        } else { // 单位 second
            postion = (long) curPostion * mVideoDuration / 1000 + deta * 1000;
        }
        postion = postion > mVideoDuration ? mVideoDuration : postion;
        postion = postion < 0 ? 0 : postion;
        getPlaySeekbar().setProgress((int) postion * 1000 / mVideoDuration);
        getCurrentTimeTv().setText(CommonTools.generateTime(postion));
    }

    //MARK:TODO
    //处理数字键  2、4、6、8
    private void handleNumberKey(int keyCode, int upLongDown) {

        if (upLongDown == KEY_UP) {
            //处理按键抬起时的状态
            if (isLongPress) {
                fovController.stopFovCruise();
            } else { // 单次按键
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    //如果是 右键， 则将右键转换成 数字键盘的 6键
                    keyCode = KeyEvent.KEYCODE_6;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    //如果按键是 左键 则将 左键转换成 数字键盘的 4键
                    keyCode = KeyEvent.KEYCODE_4;
                }

                fovController.singleFovRotate(keyCode);
            }
            isLongPress = false;
        } else if (upLongDown == KEY_LONG_PRESS) {
            //处理按键长按时的状态
            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                keyCode = KeyEvent.KEYCODE_6;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                keyCode = KeyEvent.KEYCODE_4;
            }
            fovController.startFovCruise(keyCode);
            isLongPress = true;
        } else if (upLongDown == KEY_DOWN) {
//            //处理按键按下的状态
//            // fov视角调整
//            event.startTracking();
//            if (event.getRepeatCount() == 0) {
//                isLongPress = false;
//                LogUtil.logI(TAG, "start Fov single control, pitch=" + mMDVRLibrary.updateCamera().getPitch()
//                        + ", yaw=" + mMDVRLibrary.updateCamera().getYaw());
//            }
        }

    }

    public void showEpiodes(String mVODID, Context context, Handler handler) {
        vodPlayerControllerView = null;
        mBrowseEpsiodesUtils.getSimpleVod(mVODID, new VoddetailEpsiodePresenter.GetSimpleVodCallback() {
            @Override
            public void getSimpleVodSuccess(VODDetail vodDetail) {
                vodPlayerControllerView = new VodPlayerControllerView(context, false, false, vodDetail, mBrowseEpsiodesUtils, handler, vodDetail.getPrice(), 1f);
            }

            @Override
            public void getSimpleVodFail() {
                SuperLog.info2SD(TAG, "showEpiodes getSimpleVodFail");
            }
        });
    }

    public void showView(View ParentView) {
        if (vodPlayerControllerView != null) {
            vodPlayerControllerView.showList(ParentView);
        }
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
}