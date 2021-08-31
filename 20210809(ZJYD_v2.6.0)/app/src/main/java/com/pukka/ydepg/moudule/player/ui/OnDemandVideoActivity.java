/*
 *Copyright (C) 2017 广州易杰科技, Inc.
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package com.pukka.ydepg.moudule.player.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;

import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.bean.node.XmppMessage;
import com.pukka.ydepg.common.utils.ActivityStackControlUtil;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.base.BasePlayFragment;
import com.pukka.ydepg.moudule.player.inf.ControlKeyDownListener;
import com.pukka.ydepg.moudule.player.util.RemoteKeyEvent;
import com.pukka.ydepg.moudule.vod.activity.BrowseTVPlayFragment;
import com.pukka.ydepg.xmpp.bean.XmppConstant;
import com.pukka.ydepg.xmpp.XmppManager;

import java.io.Serializable;
import java.lang.ref.WeakReference;

/**
 * 点播视频播放器界面
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: LiveTVActivity
 * @Package com.pukka.ydepg.moudule.player
 * @date 2017/12/20 17:40
 */
public class OnDemandVideoActivity extends BaseActivity {

    private static final String TAG = "LiveTVActivity";

    public static final String IS_FROM_WINDOW_PLAY = "is_from_window_play";

    /**
     * 点播视频播放界面需要的PlayVODBean: Intent信息
     */
    public static final String PLAY_VOD_BEAN = "playVodBean";

    /**
     * 点播的Fragment TAG
     */
    private static final String ONDEMAND_TAG = "ON_DEMAND";

    private ControlKeyDownListener mKeyListener;

    private XmppMessage mXmppMessage;

    private BrowseTVPlayFragment onDemandTVPlayFragment;

    //=1:从首页视频窗口进入此界面，屏蔽遥控器上键
    private int mPlayFrom = 0;

    public void setOnControlKeyDownListener(ControlKeyDownListener listener) {
        this.mKeyListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playvideo);
        ButterKnife.bind(this);
        getIntentInfo();
        initFragment();
        ActivityStackControlUtil.add(new WeakReference<Activity>(this));
    }

    public void getIntentInfo() {
        Serializable xmlInfo = getIntent().getSerializableExtra(XmppManager.XML_MESSAGE);
        if (null != xmlInfo) {
            mXmppMessage = (XmppMessage) xmlInfo;
        }
        mPlayFrom = getIntent().getIntExtra(IS_FROM_WINDOW_PLAY,0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (null == intent) return;
        setIntent(intent);
        getIntentInfo();
        initFragment();
    }

    private void initFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ONDEMAND_TAG);
        if (null == fragment) {
            Bundle bundle = new Bundle();
            bundle.putInt(IS_FROM_WINDOW_PLAY,mPlayFrom);
            bundle.putBoolean(BasePlayFragment.ISPLAYBACK, getIntent().getBooleanExtra(BasePlayFragment.ISPLAYBACK, false));
            //解决H5界面调用会看崩溃问题(只有H5界面会传此参数)
            if(!TextUtils.isEmpty(getIntent().getStringExtra(OnDemandVideoActivity.PLAY_VOD_BEAN)))
            {
                bundle.putString(OnDemandVideoActivity.PLAY_VOD_BEAN, getIntent().getStringExtra(OnDemandVideoActivity.PLAY_VOD_BEAN));
            }
            bundle.putSerializable(XmppManager.XML_MESSAGE, mXmppMessage);
            onDemandTVPlayFragment = (BrowseTVPlayFragment) Fragment.instantiate(this, BrowseTVPlayFragment.class.getName(), bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_playvideo, onDemandTVPlayFragment, ONDEMAND_TAG).commit();
        } else {
            onDemandTVPlayFragment = (BrowseTVPlayFragment) fragment;
            onDemandTVPlayFragment.updatePlayer(mXmppMessage);
        }
    }

    /**
     * 监听遥控器按键 Down事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int codeValue = RemoteKeyEvent.getInstance().getKeyCodeValue(keyCode);
        if (codeValue != RemoteKeyEvent.BTV
                && codeValue != RemoteKeyEvent.TVOD
                && null != mKeyListener) {
            if(null!=onDemandTVPlayFragment&&onDemandTVPlayFragment.isCurrentAdvertPlay()){
                return  mKeyListener.onKeyDown(keyCode, event);
            }
            mKeyListener.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 监听遥控器key UP事件
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        int codeValue = RemoteKeyEvent.getInstance().getKeyCodeValue(keyCode);
        if (codeValue != RemoteKeyEvent.BTV
                && codeValue != RemoteKeyEvent.TVOD
                && null != mKeyListener) {
            mKeyListener.onKeyUp(keyCode, event);
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStackControlUtil.remove(new WeakReference<Activity>(this));
    }

    /**
     * 甩屏发送消息用，当前点播正在播放
     *
     * @return
     */

    private boolean isVodPlaying() {
        return null != onDemandTVPlayFragment && onDemandTVPlayFragment.isVodPlayNow();
    }

    /**
     * 甩屏发送消息用，当前点播暂停
     *
     * @return
     */

    private boolean isVodPause() {
        return null != onDemandTVPlayFragment && onDemandTVPlayFragment.isVodPause();
    }

    /**
     * 甩屏时，获得当前播放位置(单位：秒）
     *
     * @return
     */
    public long getVodCurrentPosition() {
        if (null == onDemandTVPlayFragment) {
            return 0;
        }
        return onDemandTVPlayFragment.getVodCurrentPosition();
    }

    public String getContentCode() {
        if (null == onDemandTVPlayFragment) {
            return null;
        }
        return onDemandTVPlayFragment.getContentCode();
    }

    public long getVodDuration() {
        if (null == onDemandTVPlayFragment) {
            return 0;
        }
        return onDemandTVPlayFragment.getVodDuration();
    }

    /**
     * 支持甩屏的seekto
     *
     * @param position
     */

    public void seekTo(int position) {
        if (null != onDemandTVPlayFragment && (onDemandTVPlayFragment.isVodPlayNow() || onDemandTVPlayFragment.isVodPause())) {
            onDemandTVPlayFragment.seekTo(position);
        }
    }

    public int getPlayBackState() {
        if (!isVodPlaying() && !isVodPause()) {
            return XmppConstant.PlayBackState.NOT_PLAY;
        } else {
            return XmppConstant.PlayBackState.PLAY;
        }

    }

    public int getTrickPlayMode() {
        if (getFastSpeed() > 1 || getFastSpeed() < -1) {
            return XmppConstant.TrickPlayMode.FAST_SPEED;
        } else if (isVodPlaying()) {
            return XmppConstant.TrickPlayMode.PLAY;
        } else if (isVodPause()) {
            return XmppConstant.TrickPlayMode.PAUSE;
        }
        return -1;
    }

    /**
     * 甩屏发送消息用，当前点播快进快退
     *
     * @return 0未播放，1正常播放，2,4,8,16,32快进速率，-2,-4,-8,-16,-32后退速率
     */

    public int getFastSpeed() {
        if (null == onDemandTVPlayFragment) {
            return 0;
        }
        return onDemandTVPlayFragment.getforwardAndBackRate();
    }

    /**
     * 甩屏，1暂停，0播放
     *
     * @param state
     */
    public void playerOrPause(int state) {
        if (null != onDemandTVPlayFragment) {
            onDemandTVPlayFragment.playerOrPause(state);
        }
    }

    /**
     * 当前正在播放的url,甩屏需要。
     */
    public String getCurrentVodPlayUrl() {
        if (null == onDemandTVPlayFragment) {
            return null;
        }
        return onDemandTVPlayFragment.getCurrentUrl();
    }

    public void onXmppBack() {
        if (onDemandTVPlayFragment != null) {
            onDemandTVPlayFragment.onXmppBack();
        }
    }
}