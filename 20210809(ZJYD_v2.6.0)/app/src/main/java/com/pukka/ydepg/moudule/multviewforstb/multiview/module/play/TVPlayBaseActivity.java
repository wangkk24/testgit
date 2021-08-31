package com.pukka.ydepg.moudule.multviewforstb.multiview.module.play;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.cloudplayer.sdk.HCPLogLevel;
import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.R;
import com.pukka.ydepg.moudule.multviewforstb.multiview.TVApplication;
import com.pukka.ydepg.moudule.multviewforstb.multiview.logcat.LogcatAdapter;
import com.pukka.ydepg.moudule.multviewforstb.multiview.logcat.LogcatItem;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.main.BaseActivity;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.main.bean.AllVideoConfig;
import com.pukka.ydepg.moudule.multviewforstb.multiview.util.ScreenUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class TVPlayBaseActivity extends BaseActivity implements View.OnFocusChangeListener {

    protected static final String TAG = "TVPlayActivity";

    /**
     * 播控UI
     */
    public PlayControlView playControlView;

    /**
     * seek点与当前时间的差值 毫秒
     */
    protected int seekMillisecond = 0;

    /**
     * 当前播放时间点
     */
    protected long currentPlayPos;
    /**
     * 当前影片的总时长
     */
    protected long duration;
    /**
     * 当前播放已缓存的时长
     */
    protected int bufferedLength;

    /**
     * 是否已经oncreate
     */
    protected boolean onCreate;
    /**
     * 是否是直播片源
     */
    protected boolean isLive;

    /**
     * 当前播放状态
     */
    protected PlayState currentState = PlayState.NULL;

    /**
     * 屏幕高度
     */
    protected int phoneHeight;
    /**
     * 屏幕宽度
     */
    protected int phoneWidth;
    /**
     * 当前是否是全屏状态
     */
    protected boolean fullScreenMode = false;

    protected AlertDialog.Builder builder = null;

    /**
     * BufferingUpdate 100%判断
     */
    protected boolean onBufferingUpdate;

    /**
     * 下载速度显示
     */
    protected TextView downloadSpeed;

    /**
     * 上一次总下载数：字节
     */
    protected long lastTotalDownloadByteNum = 0;

    private Toast mToast;

    /**
     * 起播是否已经缓冲到100
     */
    protected boolean isFirstBufferSuccess;

    /**
     * 下载速度格式化
     */
    protected DecimalFormat decimalFormat = new DecimalFormat("0.00");
    protected boolean isBack;

    protected void initCtrlView(int playControlViewResID) {
        playControlView = findViewById(playControlViewResID);

    }

    /**
     * 用于展示UI的logcat显示
     */
    private RecyclerView logcatRecyclerView;
    private TextView logcatJson;
    private LogcatAdapter logcatAdapter;
    private volatile List<LogcatItem> logcatList = new ArrayList<>();
    protected ViewStub logcatViewStub;

    /**
     * 是否展示网速信息
     */
    protected boolean isShowSpeed;

    /**
     * 是否展示json数据和UI日志
     */
    protected boolean showLogcat;

    /**
     * 是否显示自由视角机位位置的圆圈视图
     */
    protected boolean showCameraView;

    /**
     * 播放器日志级别
     */
    protected HCPLogLevel hcpLogLevel = HCPLogLevel.HCP_LOG_LEVEL_DEBUG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPlaySDKLogLevel();
        isFirstBufferSuccess = false;
        if (getIntent() != null) {
            isShowSpeed = getIntent().getBooleanExtra(PlayConstant.TAG_ISSHOWSPEED, false);
            showLogcat = getIntent().getBooleanExtra(PlayConstant.TAG_SHOWLOGCAT, false);
            showCameraView = getIntent().getBooleanExtra(PlayConstant.TAG_SHOWCAMERAVIEW, true);
        }
    }

    /**
     * 获取本地存储的播放器日志级别
     */
    private void initPlaySDKLogLevel() {
        int logLevel = TVApplication.getInstance().getLogLevel();
        switch (logLevel) {
            case DebugLog.DEBUG:
                hcpLogLevel = HCPLogLevel.HCP_LOG_LEVEL_DEBUG;
                break;
//            case DebugLog.INFO:
//                hcpLogLevel = HCPLogLevel.HCP_LOG_LEVEL_INFO;
//                break;
//            case DebugLog.ERROR:
//                hcpLogLevel = HCPLogLevel.HCP_LOG_LEVEL_ERROR;
//                break;
//            case DebugLog.OFF:
//                hcpLogLevel = HCPLogLevel.HCP_LOG_LEVEL_DISABLE;
//                break;
        }
    }

    /**
     * 这个只是从epg拉起，并退出当前播放场景才会弹出这个提示框
     *
     * @param context     上下文对象
     * @param videoConfig AllVideoConfig
     */
    protected void showExitDialog(Context context, AllVideoConfig videoConfig) {
        DebugLog.info(TAG, "[showExitDialog]");
        final String returnUrl = videoConfig.toJson();
        try {
            if (builder == null) {
                builder = new AlertDialog.Builder(context);
            }
            builder.setTitle(getString(R.string.tip_exit)).setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        mediaDestroy();
                        Thread.sleep(150);
                        Intent intent = new Intent();
                        intent.putExtra(PlayConstant.RETURN_URL, returnUrl);
                        setResult(RESULT_OK, intent);
                        finish();
                        //调用系统API结束进程
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    } catch (InterruptedException e) {
                        DebugLog.error(TAG, e.getMessage());
                    }
                }
            }).create().show();
        } catch (Exception e) {
            addLog("[showExitDialog] error " + e.getMessage(), true);
            DebugLog.error(TAG, "[showExitDialog] error " + e.getMessage());
        }
    }

    /**
     * 事件处理
     */
    protected Handler uiHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case PlayConstant.WHAT_UPDATE_TIME:
                    if (isLive) {
                        break;
                    }
                    if (seekMillisecond <= 0) {
                        updatePlayerTime();
                        ctrlUISetTime(currentPlayPos, duration, (int) (currentPlayPos + bufferedLength));
                    }
                    uiHandler.sendEmptyMessageDelayed(PlayConstant.WHAT_UPDATE_TIME, PlayConstant.UPDATETIME_OFFSET);
                    break;
                case PlayConstant.WHAT_HIDE_TIME:
                    hideCtrlUI();
                    break;
                case PlayConstant.WHAT_UPDATE_SPEED:
                    long totalDownloadByteNum = getTotalDownloadByteNum();
                    DebugLog.debug(TAG, "[uiHandler] getTotalDownloadByteNum=" + totalDownloadByteNum);
                    long offset = totalDownloadByteNum - lastTotalDownloadByteNum;
                    lastTotalDownloadByteNum = totalDownloadByteNum;
                    //上一次时间
                    long lastTimeMillis = Long.parseLong(String.valueOf(msg.obj));
                    //两次时间差（秒）
                    float timeOffset = (System.currentTimeMillis() - lastTimeMillis) * 0.001f;
                    //最终每秒字节
                    float realOffset = offset / timeOffset;
                    String speedMsg = "0KB/s";
                    if (offset > 0 && offset < 1000000) {
                        speedMsg = decimalFormat.format(realOffset * 0.001f) + "KB/s";
                    } else if (offset >= 1000000) {
                        speedMsg = decimalFormat.format(realOffset * 0.000001f) + "MB/s";
                    }
                    if (downloadSpeed != null) {
                        downloadSpeed.setText(speedMsg);
                    }
                    DebugLog.info(TAG, "[initPlayer] speedMsg=" + speedMsg);
                    Message message = uiHandler.obtainMessage(PlayConstant.WHAT_UPDATE_SPEED);
                    message.obj = String.valueOf(System.currentTimeMillis());
                    uiHandler.sendMessageDelayed(message, PlayConstant.UPDATETIME_OFFSET);
                    break;
                case PlayConstant.WHAT_DO_ZOOM:
                    refreshZoomUI();
                    break;
                case PlayConstant.WHAT_UPDATE_LOGCAT:
                    if (logcatAdapter != null && logcatRecyclerView != null) {
                        logcatAdapter.notifyDataSetChanged();
                        logcatRecyclerView.scrollToPosition(logcatList.size() - 1);
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && phoneWidth == 0) {
            int screenHeight = ScreenUtil.getScreenHeigt(this);
            int screenWidth = ScreenUtil.getScreenWidth(this);
            phoneWidth = Math.max(screenHeight, screenWidth);
            phoneHeight = Math.min(screenHeight, screenWidth);
            getPhoneWHSuccess();
        }
    }

    protected abstract void initPlayer();

    protected abstract void seekTo(long time);

    protected abstract void showOrHideControl(boolean show);

    protected abstract boolean playerMuteChange();

    protected abstract void playerPlayOrPause(boolean toPlay);

    protected abstract void dpadLeftDown(boolean leftDown, KeyEvent event);

    protected abstract void dpadLRUp();

    protected abstract void mediaDestroy();

    protected abstract boolean canClickPauseBtn();

    protected abstract boolean canClickSeekBtn();

    protected abstract void updatePlayerTime();

    protected abstract long getTotalDownloadByteNum();

    protected abstract boolean playFreeVideo();

    protected abstract void getPhoneWHSuccess();

    protected abstract void refreshZoomUI();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!onBufferingUpdate) {
            //缓冲状态下,播放器sdk底层不响应此事件，所以UI在此处做好拦截
            DebugLog.debug(TAG, "[onKeyDown] onBufferingUpdate=false");
            addLog("[onKeyDown] onBufferingUpdate=false", false);
            return super.onKeyDown(keyCode, event);
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                DebugLog.debug(TAG, "KEYCODE_DPAD_LEFT DOWN");
                if (ctrlUIIsShowing() && canClickSeekBtn()) {
                    tryChangeSeekTime(true, event.getRepeatCount() == 0);
                    showCtrlUI();
                } else {
                    dpadLeftDown(true, event);
                }
                break;
            case KeyEvent.KEYCODE_MEDIA_REWIND:
                DebugLog.debug(TAG, "KEYCODE_MEDIA_REWIND DOWN");
                if (canClickSeekBtn()) {
                    tryChangeSeekTime(true, event.getRepeatCount() == 0);
                    showCtrlUI();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                DebugLog.debug(TAG, "KEYCODE_DPAD_RIGHT DOWN");
                if (ctrlUIIsShowing() && canClickSeekBtn()) {
                    tryChangeSeekTime(false, event.getRepeatCount() == 0);
                    showCtrlUI();
                } else {
                    dpadLeftDown(false, event);
                }
                break;
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                DebugLog.debug(TAG, "KEYCODE_MEDIA_FAST_FORWARD DOWN");
                if (canClickSeekBtn()) {
                    tryChangeSeekTime(false, event.getRepeatCount() == 0);
                    showCtrlUI();
                }
                break;

        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 尝试记录seek时间的改变
     *
     * @param leftDown
     */
    private void tryChangeSeekTime(boolean leftDown, boolean onceTime) {
        isBack = leftDown;
        if (leftDown) {
            seekMillisecond -= onceTime ? PlayConstant.SEEKOFFSET : 1000;
            if (Math.abs(seekMillisecond) >= currentPlayPos) {
                seekMillisecond = (int) -currentPlayPos;
            }
        } else {
            seekMillisecond += onceTime ? PlayConstant.SEEKOFFSET : 1000;
            if ((seekMillisecond + currentPlayPos) >= duration) {
                seekMillisecond = (int) (duration - currentPlayPos);
            }
        }
        ctrlUISetTime(currentPlayPos + seekMillisecond, duration, (int) (currentPlayPos + bufferedLength));
    }

    /**
     * 尝试进行seek操作
     *
     * @return 是否执行了seek操作
     */
    private boolean tryDoSeek() {
        if (seekMillisecond != 0) {
            DebugLog.debug(TAG, "[onKeyUp] seek " + (currentPlayPos + seekMillisecond));
            seekTo(currentPlayPos = (currentPlayPos + seekMillisecond));
            seekMillisecond = 0;
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean used = false;
        if (!onBufferingUpdate && keyCode != KeyEvent.KEYCODE_DPAD_CENTER) {
            //缓冲状态下,播放器sdk底层不响应此事件，所以UI在此处做好拦截
            DebugLog.debug(TAG, "[onKeyUp] onBufferingUpdate=false");
            addLog("[onKeyUp] onBufferingUpdate=false", false);
            return super.onKeyUp(keyCode, event);
        }
        switch (keyCode) {
            //模拟器测试时键盘中的的Enter键，模拟ok键（推荐TV开发中使用蓝叠模拟器）
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                DebugLog.debug(TAG, "KEYCODE_DPAD_LEFT|KEYCODE_DPAD_RIGHT UP " + seekMillisecond);
                if (!tryDoSeek()) {
                    dpadLRUp();
                }
                break;

            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
            case KeyEvent.KEYCODE_MEDIA_REWIND:
                DebugLog.debug(TAG, "KEYCODE_MEDIA_FAST_FORWARD|KEYCODE_MEDIA_REWIND UP " + seekMillisecond);
                tryDoSeek();
                break;
            case KeyEvent.KEYCODE_MENU:
                DebugLog.debug(TAG, "KEYCODE_MENU");
                if (null == playControlView || !fullScreenMode) {
                    DebugLog.info(TAG, "null == playControlView || fullScreenMode = false");
                    break;
                }
                if (ctrlUIIsShowing()) {
                    hideCtrlUI();
                } else {
                    ctrlUIDoPause(currentState == PlayState.PAUSE);
                    showCtrlUI();
                }
                break;

            case KeyEvent.KEYCODE_MUTE:
                DebugLog.debug(TAG, "KEYCODE_MUTE");
                used = playerMuteChange();
                break;
            case KeyEvent.KEYCODE_VOLUME_MUTE:
                DebugLog.debug(TAG, "KEYCODE_VOLUME_MUTE");
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                DebugLog.debug(TAG, "KEYCODE_DPAD_CENTER");
                if (!canClickPauseBtn()) {
                    break;
                }
                if (currentState == PlayState.PLAYING) {
                    playerPlayOrPause(false);
                    currentState = PlayState.PAUSE;
                    ctrlUIDoPause(true);
                    DebugLog.info(TAG, "[onClick] pause");
                } else if (currentState == PlayState.PAUSE) {
                    playerPlayOrPause(true);
                    currentState = PlayState.PLAYING;
                    ctrlUIDoPause(false);
                    DebugLog.info(TAG, "[onClick] start");
                }
                showCtrlUI();
                break;
            default:
                break;
        }

        return used || super.onKeyUp(keyCode, event);
    }


    protected boolean ctrlUIIsShowing() {
        return playControlView != null && playControlView.getVisibility() == View.VISIBLE;
    }

    protected void showCtrlUI() {
        if (!ctrlUIIsShowing()) {
            playControlView.setVisibility(View.VISIBLE);
            playControlView.setFocusable(true);
            playControlView.requestFocus();
            showOrHideControl(true);
        }
        if (null != uiHandler) {
            uiHandler.removeMessages(PlayConstant.WHAT_HIDE_TIME);
            uiHandler.sendEmptyMessageDelayed(PlayConstant.WHAT_HIDE_TIME, 5000);
        }
    }

    protected void hideCtrlUI() {
        if (ctrlUIIsShowing()) {
            playControlView.setVisibility(View.GONE);
            playControlView.setFocusable(false);
        }
        showOrHideControl(false);
        if (null != uiHandler) {
            uiHandler.removeMessages(PlayConstant.WHAT_HIDE_TIME);
        }
    }

    protected void ctrlUIDoPause(boolean doPause) {
        if (playControlView != null) {
            playControlView.doPause(doPause);
        }
    }

    protected void ctrlUISetTime(long currentTime, long duration, int bufferedLength) {
        if (playControlView != null) {
            playControlView.setTime(currentTime, duration, bufferedLength);
        }
    }

    /**
     * 初始化日志相关View
     */
    protected void initLogcatRecyclerView() {
        if (logcatViewStub == null || logcatRecyclerView != null)
            return;
        View inflate = logcatViewStub.inflate();
        logcatJson = inflate.findViewById(R.id.json_text);
        logcatRecyclerView = inflate.findViewById(R.id.logcat_recyclerview);
        logcatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        logcatAdapter = new LogcatAdapter();
        logcatAdapter.setLogcatList(logcatList);
        logcatRecyclerView.setAdapter(logcatAdapter);
    }

    /**
     * 用于显示UI的Logcat
     *
     * @param logcat
     */
    protected void addLog(String logcat, boolean isError) {
        if (TextUtils.isEmpty(logcat) || logcatRecyclerView == null)
            return;
        logcatList.add(new LogcatItem(logcat, isError));
        if (Looper.getMainLooper() == Looper.myLooper()) {
            logcatAdapter.notifyDataSetChanged();
            logcatRecyclerView.scrollToPosition(logcatList.size() - 1);
        } else {
            uiHandler.sendEmptyMessage(PlayConstant.WHAT_UPDATE_LOGCAT);
        }
    }

    /**
     * 用于显示配置的Json
     *
     * @param json
     */
    protected void showJson(String json) {
        if (TextUtils.isEmpty(json) || logcatJson == null)
            return;
        logcatJson.setText(json);
    }

    /**
     * 统一管理toast
     *
     * @param msg
     */
    protected void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
