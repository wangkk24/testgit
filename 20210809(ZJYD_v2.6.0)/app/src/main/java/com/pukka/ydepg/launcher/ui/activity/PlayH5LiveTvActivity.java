package com.pukka.ydepg.launcher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.inf.IPlayListener;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.view.PlayView;

public class PlayH5LiveTvActivity extends BaseActivity implements IPlayListener {

    private static final String TAG = PlayH5LiveTvActivity.class.getSimpleName();
    public static final String PLAY_URL = "playUrl";
    private PlayView mPlayView;
    private String mPlayUrl = "";
    private int mTryCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_livetv);
        mPlayView = (PlayView) findViewById(R.id.play_view);
        initListener();
    }

    private void initListener() {
        mPlayView.setShouldAutoPlay(true);
        mPlayView.setOnPlayCallback(this);
        mPlayView.setControllViewState(View.GONE, false);
        Intent intent = getIntent();
        String playUrl = intent.getStringExtra(PLAY_URL);
        if (!TextUtils.isEmpty(playUrl)) {
            mPlayView.startPlay(playUrl);
        }
    }

    @Override
    protected void onPause() {
        mPlayView.releasePlayer();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mPlayView.rePlay();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayView != null) {
            mPlayView.onDestory();
        }
    }

    @Override
    public void onPlayState(int playbackState) { }

    @Override
    public void onPrepared(int  Videotype) { }

    @Override
    public void onRelease() { }

    @Override
    public void onPlayError(String msg, int errorCode, int playerType) { }

    @Override
    public void onPlayCompleted() { }

    @Override
    public void onDetached(long time) { }

    @Override
    public void onAttached() { }

    @Override
    public void onTryPlayForH5() {
        if (null != mPlayView && mTryCount < 3 && !TextUtils.isEmpty(mPlayUrl)) {
            SuperLog.error(TAG, "mPlayView=" + mPlayView + ",mTryCount=" + mTryCount + ",mPlayUrl=" + mPlayUrl);
            mPlayView.releasePlayer();
            mPlayView.startPlay(mPlayUrl);
            mTryCount++;
        } else {
            SuperLog.error(TAG, "mTryCount=" + mTryCount);
            mTryCount = 0;
        }
    }

    @Override
    public void onAdVideoEnd() {

    }
}