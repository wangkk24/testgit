package com.pukka.ydepg.moudule.vrplayer.vrplayer.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.asha.vrlib.MDVRLibrary;
import com.google.gson.Gson;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.BaseActivity;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.VrPlayerActivity;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.global.Constant;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.module.VideoBean;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.utils.CommonTools;

/**
 * 功能描述
 *
 * @author l00477311
 * @since 2020-07-02
 */
public abstract class MD360PlayerActivity extends BaseActivity {
    private static final String TAG = "MD360PlayerActivity";

    // 菜单的区域

    //MARK:TODO -- 将 private 修改为 public
    public RelativeLayout touchRl;


    // 进度条
    private LinearLayout playTimeBar;
    private TextView endTimeTv; //总时长
    private TextView currentTimeTv; // 当前时长
    private SeekBar playSeekbar; // 视频播放进度

    private TextView titleTv;  // 视频标题
    private ProgressBar busyProgress; // 加载
    private ImageView pauseIcon; // 暂停标志

    private MDVRLibrary mVRLibrary;

    private GLSurfaceView mGlSurfaceView;

    private VideoBean mVideoBean; // 视频信息
    private static String mContentId;
    private static VOD mVod;
    public FrameLayout mRootView;

    public static void startVideo(Context context, VideoBean videoBean) {
        start(context, videoBean, VrPlayerActivity.class);
    }

    private static void start(Context context, VideoBean videoBean, Class<? extends Activity> clz) {

        Intent intent = new Intent(context, clz);
        Gson gson = new Gson();
        intent.putExtra(Constant.VR_JSON, gson.toJson(videoBean));
        intent.putExtra("CONTENTID", mContentId);
        intent.putExtra("VOD", mVod);
        CommonTools.startActivityByIntent(context, intent);
    }

    public static void setExtra(String contentId, VOD vod) {
        mContentId = contentId;
        mVod = vod;
    }

    abstract protected MDVRLibrary createVRLibrary();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            isFromEPG = TextUtils.isEmpty(getIntent().getStringExtra(Constant.VR_JSON));
        }

        // get video info
        mVideoBean = CommonTools.parseVideoInfoByIntent(getIntent());
        // set content view
        setContentView(R.layout.activity_md_using_surface_view);

        initView();

        // init VR Library
        mVRLibrary = createVRLibrary();
    }

    public MDVRLibrary getVRLibrary() {
        return mVRLibrary;
    }

    public GLSurfaceView getGlSurfaceView() {
        return mGlSurfaceView;
    }

    public VideoBean getVideoBean() {
        return mVideoBean;
    }

    public void setVideoBean(VideoBean videoBean) {
        this.mVideoBean = videoBean;
    }

    protected TextView getEndTimeTv() {
        return endTimeTv;
    }

    protected TextView getCurrentTimeTv() {
        return currentTimeTv;
    }

    protected SeekBar getPlaySeekbar() {
        return playSeekbar;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVRLibrary.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVRLibrary.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVRLibrary.onDestroy();
    }

    public void cancelBusy() {
        busyProgress.setVisibility(View.GONE);
    }

    public void busy() {
        busyProgress.setVisibility(View.VISIBLE);
    }

    public void showPauseIcon() {
        pauseIcon.setVisibility(View.VISIBLE);
    }

    public void hidePauseIcon() {
        pauseIcon.setVisibility(View.GONE);
    }

    /**
     * 展示菜单
     */
    public void showMenu() {
        touchRl.setVisibility(View.VISIBLE);
    }

    public void hideMenu() {
        touchRl.setVisibility(View.GONE);
    }

    //初始化页面菜单功能
    private void initView() {
        mRootView =findViewById(R.id.framelayout_gl);
        touchRl = (RelativeLayout) findViewById(R.id.rl_touch_menu);
        touchRl.setVisibility(View.VISIBLE); // 开始时显示菜单
        titleTv = (TextView) findViewById(R.id.tv_vr_title);
        titleTv.setText(mVideoBean.getResourceName());

        playTimeBar = (LinearLayout) findViewById(R.id.tv_play_time_progress);
        if (mVideoBean.getMediaType() == Constant.VOD) {
            playTimeBar.setVisibility(View.VISIBLE); // 点播显示进度条
        }
        endTimeTv = (TextView) findViewById(R.id.tv_end_play_time);
        currentTimeTv = (TextView) findViewById(R.id.tv_current_play_time);

        playSeekbar = (SeekBar) findViewById(R.id.seekbar_play_video);
        playSeekbar.setMax(1000);
        playSeekbar.setFocusable(false);

        mGlSurfaceView = (GLSurfaceView) findViewById(R.id.gl_view);

        busyProgress = (ProgressBar) findViewById(R.id.progress);
        pauseIcon = findViewById(R.id.iv_pause);
    }
}
