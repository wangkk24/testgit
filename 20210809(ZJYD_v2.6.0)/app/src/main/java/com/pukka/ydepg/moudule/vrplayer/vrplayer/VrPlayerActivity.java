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
 * ????????????
 *
 * @author l00477311
 * @since 2020-08-05
 */
public class VrPlayerActivity extends MD360PlayerActivity {
    private static final String TAG = "VrPlayerActivity";

    /**
     * ??????????????????
     */
    private int mCurrentBufferPercentage = 0;

    private MDVRLibrary mMDVRLibrary;

    private MediaPlayerWrapper mMediaPlayerWrapper = new MediaPlayerWrapper();

    private boolean isPause = false;

    private int orignPitch = -90; // ???????????????-90???

    private MediaController mpController;

    private FovController fovController;

    private boolean isLongPress = false;

    private int menuShowTime = AreaSpecialProperties.getMenuShowTime();

    private long lastBackKeyTime = 0; // ????????????????????????

    private long lastPauseKeyTime = 0; // ????????????????????????

    private boolean onBackground = false; // ???????????????

    private int lastPlayTime = 0; // ????????????????????????????????????

    private float lastPicth = orignPitch; // ?????????????????????????????????????????????

    private float lastYaw = 0; // ?????????????????????????????????????????????

    private float lastNearScale = 0; // ???????????????????????????

    private boolean backKeyHashDown = false; // ?????????????????????????????????Activity??????????????????????????????????????????

    private int mVideoDuration = 0;


    //???????????????????????????
    private boolean isVideoPrepared = false;


    //??????????????????
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
                    //???????????????????????????????????????
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
        // ?????????3D????????????????????????????????????
        if (getVideoBean().getResourceType() == 5 || getVideoBean().getResourceType() == 6) {
            set3DProjectionMode(getVideoBean().getType3D());
        }
        // ??????????????????
        mHandler.sendEmptyMessageDelayed(MessageType.MenuControlMessage.MESSAGE_HIDE_MENU, 1000 * menuShowTime);
        // ???????????????
        registerLogics();
        LogUtil.logI(TAG, "Exit onCreate " + this.toString());
        mEpsiodesHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == BrowseTVPlayFragment.EPISODE_PLAY) {
//????????????
                    Bundle data = msg.getData();
//                    bundle.putString("SitcomNO", episode.getSitcomNO());
//                    bundle.putString("EpisodesId", episode.getVOD().getID());
//                    bundle.putString("MediaId", episode.getVOD().getMediaFiles().get(0).getID());
//                    bundle.putString("elapseTime", episode.getVOD().getMediaFiles().get(0).getElapseTime());
//                    bundle.putSerializable("episodeVod",episode.getVOD());
                    //??????id
                    String episodesId = (String) data.get("EpisodesId");
                    //????????????id
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
        //??????????????????
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

//???????????????
        showEpiodes(mVODID, this, mEpsiodesHandler);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.logI(TAG, "Enter onNewIntent" + this.toString());
        VideoBean videoBean = CommonTools.parseVideoInfoByIntent(intent);
        if (videoBean == null || getVideoBean().equals(videoBean)) { // ????????????????????????????????????Activity???????????????
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
        if (onBackground) { // ?????????????????????
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
        mMediaPlayerWrapper.reset(); // ????????????????????????
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
                .displayMode(MDVRLibrary.DISPLAY_MODE_NORMAL) // ??????360?????????
                .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_TOUCH) // ??????
                .projectionMode(MDVRLibrary.PROJECTION_MODE_SPHERE) // ???????????????????????????
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
            if (lastPlayTime > AreaSpecialProperties.getSingleSeekTime()) { // ???????????????????????????????????????seek??????????????????seek?????????????????????
                Message msg = new Message();
                msg.what = MessageType.MPControlMessage.MESSAGE_MPCONTROL_SEEK_TO;
                msg.arg1 = lastPlayTime;
                mpController.sendMessage(msg);
            }
            if (isPause) {
                mMediaPlayerWrapper.pause();
                // ????????????pause/resume????????????????????????????????????
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
            if (!isPause) { // seek?????????????????????seek???????????????
                mMediaPlayerWrapper.resume();
            }
            mHandler.sendEmptyMessage(MessageType.MenuControlMessage.MESSAGE_SHOW_PROGRESS); // ???????????????
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

    // ?????????????????????
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
        LogUtil.logI(TAG, "Enter onKeyDown???keyCode = " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                backKeyHashDown = true;
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:

            case KeyEvent.KEYCODE_DPAD_CENTER:  // ????????????
                handlerPauseKey();
                return true;
            //MARK:TODO -- ????????????+???- ?????????????????????
//            case KeyEvent.KEYCODE_CHANNEL_UP: // ??????+
//            case KeyEvent.KEYCODE_CHANNEL_DOWN: // ??????-


            case KeyEvent.KEYCODE_MEDIA_REWIND: // ??????
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD: // ??????
                event.startTracking(); // ???????????????????????????????????????onKeyLongPress
                if (event.getRepeatCount() == 0) { // ???????????????
                    isLongPress = false;
                    prepareSeekOrSwitch(keyCode);
                }
                return true;
//            case KeyEvent.KEYCODE_DPAD_UP:
//            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                //MARK:TODO -- ?????????????????????    ????????????????????????seek????????? ??????????????????????????????


//                if (mCurrentBufferPercentage == 0) {
//                    return true;
//                }
                if (isVideoPrepared == false || null == touchRl) {
                    return true;
                }

                //??????????????????????????????
                if (touchRl.getVisibility() == View.VISIBLE) {
                    //??????????????? ??????seek??????
                    event.startTracking(); // ???????????????????????????????????????onKeyLongPress
                    if (event.getRepeatCount() == 0) { // ???????????????
                        isLongPress = false;
                        prepareSeekOrSwitch(keyCode);
                    }
                    return true;
                } else {
                    //?????????????????????????????????????????????
                    // fov????????????
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
//????????????????????????????????????
                        showView(mRootView);

                    }
                } else {

                    // ??????????????????
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
            case KeyEvent.KEYCODE_2: //??????2??????
            case KeyEvent.KEYCODE_4: //??????4??????
            case KeyEvent.KEYCODE_6: //??????6??????
            case KeyEvent.KEYCODE_8: //??????8??????

//                if (mCurrentBufferPercentage == 0) {
//                    return true;
//                }
                if (isVideoPrepared == false) {
                    return true;
                }
                // fov????????????
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

            //MARK:TODO -- ????????????+???- ?????????????????????
//            case KeyEvent.KEYCODE_CHANNEL_UP:

            //MARK:TODO -- ????????? ????????????????????????????????? ????????? ???????????????????????? seek
            case KeyEvent.KEYCODE_DPAD_RIGHT: //??????
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
                //?????????  ???????????????????????????????????????????????????
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && touchRl.getVisibility() != View.VISIBLE) {
                    //????????????
                    handleNumberKey(keyCode, KEY_LONG_PRESS);
                    return true;
                }


                if (getVideoBean().getMediaType() == Constant.VOD) { // ??????seek???????????????????????????
                    // ????????????
                    mMediaPlayerWrapper.pause();
                    mHandler.sendEmptyMessage(MessageType.MenuControlMessage.MESSAGE_DRAG_SEEKBAR_FORWOARD);
                } else { // ????????????
                    // todo
                }
                isLongPress = true;
                return true;
            //MARK:TODO -- ????????????+???- ?????????????????????
//            case KeyEvent.KEYCODE_CHANNEL_DOWN:

            case KeyEvent.KEYCODE_DPAD_LEFT://??????
            case KeyEvent.KEYCODE_MEDIA_REWIND:
//                if (mCurrentBufferPercentage == 0) {
//                    return true;
//                }

                if (isVideoPrepared == false || null == touchRl) {
                    return true;
                }


                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && touchRl.getVisibility() != View.VISIBLE) {
                    //????????????
                    handleNumberKey(keyCode, KEY_LONG_PRESS);
                    return true;
                }

                if (getVideoBean().getMediaType() == Constant.VOD) { // ??????seek???????????????????????????
                    // ????????????
                    mMediaPlayerWrapper.pause();
                    mHandler.sendEmptyMessage(MessageType.MenuControlMessage.MESSAGE_DRAG_SEEKBAR_REWIND);
                } else { // ????????????
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

            //MARK:TODO -- ??????
            case KeyEvent.KEYCODE_2: //??????2??????
            case KeyEvent.KEYCODE_4: //??????4??????
            case KeyEvent.KEYCODE_6: //??????6??????
            case KeyEvent.KEYCODE_8: //??????8??????
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
                //MARK:TODO -- ???????????????   -- ??????????????????????????????????????????  ????????????????????????????????? ??????Alert???
                if (!backKeyHashDown) {
                    return true;
                }

                backKeyHashDown = false;
                if (mMediaPlayerWrapper.hasPrePared() && !fovController.isFovInDefault(orignPitch)) {
                    // ??????????????????
                    Message message = new Message();
                    message.what = MessageType.FovControlMessage.MESSAGE_FOVCONTROL_RESET;
                    message.arg1 = orignPitch;
                    fovController.sendMessage(message);
                    return true;
                }


                //MARK:TODO -- ???????????????2,5???????????????????????????????????????Toast?????????
                //???????????? ???????????????????????????????????????

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("?????????");
                builder.setMessage("??????????????????");

                //??????????????????
                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
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
                //??????????????????
                builder.setPositiveButton("??????", null);
                //???????????????
                builder.show();
                return true;


            //MARK:TODO - ????????????+???- ?????????????????????
//            case KeyEvent.KEYCODE_CHANNEL_UP:
//            case KeyEvent.KEYCODE_CHANNEL_DOWN:


            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
            case KeyEvent.KEYCODE_MEDIA_REWIND: // seek???
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
            //MARK: TODO -- ???????????????????????????????????? ??????   ?????????????????????  seek
            //
            case KeyEvent.KEYCODE_DPAD_RIGHT: //??????
            case KeyEvent.KEYCODE_DPAD_LEFT: //??????
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
                    //????????????
                    handleNumberKey(keyCode, KEY_UP);
                    return true;
                }


//            case KeyEvent.KEYCODE_DPAD_UP:
//            case KeyEvent.KEYCODE_DPAD_DOWN:
//            case KeyEvent.KEYCODE_DPAD_LEFT:
//            case KeyEvent.KEYCODE_DPAD_RIGHT:
//                if (isLongPress) {
//                    fovController.stopFovCruise();
//                } else { // ????????????
//                    fovController.singleFovRotate(keyCode);
//                }
//                isLongPress = false;
//                return true;
                //MARK: TODO -- ????????????
            case KeyEvent.KEYCODE_MENU:
                if (null == touchRl) {
                    LogUtil.logE(TAG, "null == touchRl");
                    break;
                }

                //??????????????????????????????
                if (touchRl.getVisibility() == View.VISIBLE) {
                    //????????????????????????????????????
                    hideMenu();
                } else {
                    //???????????????????????????????????????
                    //???????????????????????????????????????????????????5????????????????????????????????????5????????????????????????
                    mHandler.removeMessages(MessageType.MenuControlMessage.MESSAGE_HIDE_MENU);
                    showMenu();
                    mHandler.sendEmptyMessageDelayed(MessageType.MenuControlMessage.MESSAGE_HIDE_MENU, 1000 * 5);// 5??????????????????
                }
                break;

            //MARK:TODO -- ????????????
            case KeyEvent.KEYCODE_2: //??????2??????
            case KeyEvent.KEYCODE_4: //??????4??????
            case KeyEvent.KEYCODE_6: //??????6??????
            case KeyEvent.KEYCODE_8: //??????8??????
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
        if (getVideoBean().getMediaType() == Constant.VOD) { // ??????seek??????
            mHandler.removeMessages(MessageType.MenuControlMessage.MESSAGE_SHOW_PROGRESS); // ?????????????????????
            // ????????????
            mHandler.removeMessages(MessageType.MenuControlMessage.MESSAGE_HIDE_MENU);
            showMenu();
        } else { // ??????switch????????????
            // todo
        }
    }


    // ??????seek?????????UP??????
    private void handlerSeekLongKeyUp() {
        if (getVideoBean().getMediaType() == Constant.VOD) { // ????????????seek
            LogUtil.logI(TAG, "seek long key up");
            mHandler.removeMessages(MessageType.MenuControlMessage.MESSAGE_DRAG_SEEKBAR_FORWOARD); // ??????????????????
            mHandler.removeMessages(MessageType.MenuControlMessage.MESSAGE_DRAG_SEEKBAR_REWIND);
            if (isPause) { // ???????????????seek???resume??????seek
                return;
            }
            mpController.seekBySeekBarTime(getPlaySeekbar());
        } else { // ????????????
            // todo
        }
    }

    // ??????seek?????????UP??????
    private void handlerSeekShortKeyUp(int deta) {
        if (getVideoBean().getMediaType() == Constant.VOD) { // ????????????seek
            dragPlaySeekBar(deta, "second");
            if (!isPause) { // ???pause??????seek
                mpController.seekBySeekBarTime(getPlaySeekbar());
            }
        } else { // ????????????
            // todo
        }
    }

    // ???????????????
    private void handlerPauseKey() {
        long currentTimeMillis = System.currentTimeMillis();
        long interval = currentTimeMillis - lastPauseKeyTime;
        if (interval < 500 || !mMediaPlayerWrapper.hasPrePared()) {
            LogUtil.logI(TAG, "press pause Key too frequently, ignore???hasPrepared = " + mMediaPlayerWrapper.hasPrePared());
            return;
        }
        lastPauseKeyTime = currentTimeMillis;
        mHandler.removeMessages(MessageType.MenuControlMessage.MESSAGE_HIDE_MENU);
        showMenu();
        mHandler.sendEmptyMessageDelayed(MessageType.MenuControlMessage.MESSAGE_HIDE_MENU, 1000 * menuShowTime);// 3??????????????????
        if (getVideoBean().getMediaType() == Constant.BTV) { // ???????????????
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
            // ????????????????????????????????????????????????????????????
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

    // ???????????????
    private void dragPlaySeekBar(int deta, String unit) {
        LogUtil.logI(TAG, "dragPlayerSeekBar, deta =" + deta + ",unit =" + unit);
        int curPostion = getPlaySeekbar().getProgress();
        long postion = 0;
        if (unit.equals("percent")) { // ?????????,?????????????????????
            postion = ((long) curPostion + deta) * mVideoDuration / 1000;
        } else { // ?????? second
            postion = (long) curPostion * mVideoDuration / 1000 + deta * 1000;
        }
        postion = postion > mVideoDuration ? mVideoDuration : postion;
        postion = postion < 0 ? 0 : postion;
        getPlaySeekbar().setProgress((int) postion * 1000 / mVideoDuration);
        getCurrentTimeTv().setText(CommonTools.generateTime(postion));
    }

    //MARK:TODO
    //???????????????  2???4???6???8
    private void handleNumberKey(int keyCode, int upLongDown) {

        if (upLongDown == KEY_UP) {
            //??????????????????????????????
            if (isLongPress) {
                fovController.stopFovCruise();
            } else { // ????????????
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    //????????? ????????? ????????????????????? ??????????????? 6???
                    keyCode = KeyEvent.KEYCODE_6;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    //??????????????? ?????? ?????? ??????????????? ??????????????? 4???
                    keyCode = KeyEvent.KEYCODE_4;
                }

                fovController.singleFovRotate(keyCode);
            }
            isLongPress = false;
        } else if (upLongDown == KEY_LONG_PRESS) {
            //??????????????????????????????
            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                keyCode = KeyEvent.KEYCODE_6;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                keyCode = KeyEvent.KEYCODE_4;
            }
            fovController.startFovCruise(keyCode);
            isLongPress = true;
        } else if (upLongDown == KEY_DOWN) {
//            //???????????????????????????
//            // fov????????????
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


        /*????????????????????????
         * 0 ?????????
         * 1 ?????????
         * 2 ?????? ????????? ?????? 2 ???????????? ????????????????????????
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