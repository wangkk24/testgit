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
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import androidx.fragment.app.Fragment;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.extview.LinearLayoutExt;
import com.pukka.ydepg.common.http.v6bean.v6node.BookMarkSwitchs;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6response.PBSRemixRecommendResponse;
import com.pukka.ydepg.common.utils.ActivityStackControlUtil;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.customui.focusView.TVGuidePopwindow;
import com.pukka.ydepg.event.DisableTVGuideKeyEvent;
import com.pukka.ydepg.event.ResponseKeyEvent;
import com.pukka.ydepg.event.TVGuideEvent;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.catchup.activity.CatchUpActivity;
import com.pukka.ydepg.moudule.catchup.common.TVODDataUtil;
import com.pukka.ydepg.moudule.catchup.presenter.TVODContract;
import com.pukka.ydepg.moudule.catchup.presenter.TVODPresenter;
import com.pukka.ydepg.moudule.livetv.adapter.BaseAdapter;
import com.pukka.ydepg.moudule.livetv.ui.LiveTVPlayFragment;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.player.inf.ControlKeyDownListener;
import com.pukka.ydepg.moudule.player.util.RemoteKeyEvent;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 直播视频播放器界面
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: LiveTVActivity
 * @Package com.pukka.ydepg.moudule.player
 * @date 2017/12/20 17:40
 */

public class LiveTVActivity extends BaseActivity implements TVGuidePopwindow.WindowDismissListener {

    private static final String TAG = "LiveTVActivity";

    /*
     * 直播分类所需的id
     * */
    public static final String KEY_CHANNEL_SUBJECTS = "ChannelSubjects";
    private LinearLayoutExt llContainer;

    public static final String KEY_SUBJECT_ID = "SubjectId";

    //1代表查询子栏目，非1代表不查
    public static final String KEY_QUERY_SUBJECT = "query_subject";

    /**
     * 1代表展示频道号，非1代表不展示
     * 频道号非channelNo,而是频道集合的position，从1开始，同时切台的频道号按照此postion，并非之前的channelNo,(勿影响之前原有逻辑)。
     */
    public static final String KEY_SHOW_CHANNEL_NO = "show_channel_no";

    /**
     * intent:key常量
     */
    public static final String VIDEO_TYPE = "VIDEO_TYPE";

    /**
     * 直播 Intent Type
     */
    public static final String VIDEO_TYPE_LIVETV = "LIVE_TV";

    /**
     * 回看（点播） Intent Type
     */
    public static final String VIDEO_TYPE_TVOD = "LIVE_TVOD";

    /**
     * 特定频道进入Intent Type
     */
    public static final String CHANNEL_FINAL = "CHANNEL_FINAL";

    /**
     * 特定栏目进入Intent Type
     */
    public static final String SUBJECT_FINAL = "SUBJECT_FINAL";

    /**
     * 特定栏目进入Intent Type
     */
    public static final String SUBJECT_FINAL_ID = "SUBJECT_FINAL_ID";

    /**
     * 延时显示TVGUIDE的msg.what
     */
    private static final int MSG_DELAY_SHOW_TVGUIDE = 0x98;

    /**
     * 延时关闭TVGUIDE的msg.what
     */
    private static final int DISMISS_TVGUIDE = 0x99;

    /**
     * 5秒不操作自动关闭TVGUIDE页
     */
    private static final int DELAY_DISMISS_TIME = 5000;


    private final int ACTIVITY_ONCREATE = 11111;

    private final int ACTIVITY_ONPAUSE = 11112;

    private final int ACTIVITY_ONSTOP = 11113;

    private final int ACTIVITY_ONDESTORY = 11114;

    private int currentState;

    //点击资源位，==true:只播放某一个频道，不能切台，不现实EPG。。。
    private boolean mIsOnlyChannel = false;

    private boolean isOnlySubject = false;

    private String mOnlySubjectID;

    /**
     * 当前跳转过来的是直播还是点播
     */
    private String mVideoType;

    //配置一个栏目id，播放界面只展示频道列表
    private String mSubjectId;

    //1代表查询子栏目，非1代表不查
    private String mIsQuerySubject;

    /**
     * 1代表展示频道号，非1代表不展示
     * 频道号非channelNo,而是频道集合的position，从1开始，同时切台的频道号按照此postion，并非之前的channelNo,(勿影响之前原有逻辑)。
     */
    private String mIsShowChannelNo;

    /**
     * 直播的Fragment TAG
     */
    private static final String LIVETV_TAG = "LIVE_TV";

    private ControlKeyDownListener mKeyListener;

    /**
     * TVGUIDE弹窗
     */
    private TVGuidePopwindow mTVGuideWindow;

    private LiveTVPlayFragment liveTVPlayFragment;

    /**
     * 特定栏目进入Intent Type
     */
    public static final String PLAY_URL_ONLY = "PLAYURLONLY";

    private String mOnlyPlayUrl;

    /**
     * Handler
     */
    private LiveHandler mHandler = new LiveHandler(this);

    public LiveTVPlayFragment getLiveTvPlayFragment() {
        return liveTVPlayFragment;
    }

    private static class LiveHandler extends Handler {

        private WeakReference<LiveTVActivity> mRefrence;

        LiveHandler(LiveTVActivity tvActivity) {
            mRefrence = new WeakReference<>(tvActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != mRefrence && null != mRefrence.get()) {
                LiveTVActivity tvActivity = mRefrence.get();
                if (msg.what == DISMISS_TVGUIDE) {
                    if (null != tvActivity.mTVGuideWindow) {
                        tvActivity.mTVGuideWindow.dismiss();
                    }
                } else if (msg.what == MSG_DELAY_SHOW_TVGUIDE) {
                    tvActivity.showTVGuide();
                }
            }
        }
    }

    public void setOnControlKeyDownListener(ControlKeyDownListener listener) {
        this.mKeyListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SuperLog.debug(TAG, "onCreate");
        currentState = ACTIVITY_ONCREATE;
        setContentView(R.layout.activity_playvideo);
//        TVODDataUtil.getInstance().createChannelMapData();
        //initUserColumnId();
        ButterKnife.bind(this);
        ActivityStackControlUtil.add(new WeakReference<Activity>(this));
        mIsOnlyChannel = getIntent().getBooleanExtra(CHANNEL_FINAL, false);
        isOnlySubject = getIntent().getBooleanExtra(SUBJECT_FINAL, false);
        mOnlySubjectID = getIntent().getStringExtra(SUBJECT_FINAL_ID);
        if (null != getIntent().getStringExtra(PLAY_URL_ONLY)) {
            mOnlyPlayUrl = getIntent().getStringExtra(PLAY_URL_ONLY);
        }
//        mOnlyPlayUrl = "http://hwltc.tv.cdn.zj.chinamobile.com/PLTV/88888888/224/3221232167/293144469.smil/index.m3u8?fmt=ts2hls&rrsip=hwltc.tv.cdn.zj.chinamobile.com&zoneoffset=0&servicetype=1&icpid=&limitflux=-1&limitdur=-1&tenantId=8601&accountinfo=%7E%7EV2.0%7EXN0vmGNxtlRQN6iNBD2Lbg%7E2XwL824wDaIJ1hAfU8g2p6llNKXf29K_OByGEPGKgJcEuFi1UaeW4OEJJP0UipUN%7EExtInfoWNHSPSTb%2B3AG0FnUkYLPMw%3D%3D%3A20210804022853%3AUTC%2C10001207013016%2C36.152.38.158%2C20210804022853%2CMGAYZ02%2C10001207013016%2C-1%2C0%2C1%2C%2C%2C2%2C600000439142%2C%2C%2C2%2C10000156513199%2C0%2C10000156513775%2C0042010030042010171158B42D06DD20%2C%2C%2C2%2C1%2C293144469%2CEND&GuardEncType=2&USERID=hwtest937&STBID=0042010030042010171158B42D06DD20&Multicast=0&profileid=main";
        initFragment();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //initUserColumnId();
        SuperLog.debug(TAG, "onNewIntent");
        if (null == intent || currentState == ACTIVITY_ONCREATE) return;
        setIntent(intent);
        EventBus.getDefault().post(new TVGuideEvent(false));
        initFragment();
    }

    private void initView() {
        //if (TextUtils.isEmpty(channelSubjectsID))return;
        llContainer = (LinearLayoutExt) findViewById(R.id.ll_container);
        mVideoType = getIntent().getStringExtra(VIDEO_TYPE);
        mSubjectId = getIntent().getStringExtra(KEY_SUBJECT_ID);

//        initChannelList();
//        queryPBSRemixRecommend();
//    mSubjectId = "catauto2000056025";
        mIsQuerySubject = getIntent().getStringExtra(KEY_QUERY_SUBJECT);
        mIsShowChannelNo = getIntent().getStringExtra(KEY_SHOW_CHANNEL_NO);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(LIVETV_TAG);
        if (null == fragment) {
            //点击BTV/TVOD都直接打开直播视频播放器界面
            Bundle bundle = new Bundle();
            bundle.putBoolean(LiveTVPlayFragment.ISPLAYBACK, mVideoType.equals(VIDEO_TYPE_TVOD));
            bundle.putBoolean(LiveTVActivity.CHANNEL_FINAL, mIsOnlyChannel);
            bundle.putString(LiveTVActivity.KEY_SUBJECT_ID, mSubjectId);
            bundle.putString(LiveTVActivity.KEY_QUERY_SUBJECT, mIsQuerySubject);
            bundle.putString(LiveTVActivity.KEY_SHOW_CHANNEL_NO, mIsShowChannelNo);
            bundle.putString(LiveTVActivity.PLAY_URL_ONLY, mOnlyPlayUrl);

//      bundle.putString(LiveTVActivity.KEY_SUBJECT_ID, "catauto2000056025");
//      bundle.putString(LiveTVActivity.KEY_QUERY_SUBJECT, "1");
//      bundle.putString(LiveTVActivity.KEY_SHOW_CHANNEL_NO, "0");

            liveTVPlayFragment = (LiveTVPlayFragment) Fragment.instantiate(this,
                    LiveTVPlayFragment.class.getName(), bundle);
//            getSupportFragmentManager().beginTransaction().replace(R.id.frame_playvideo,
//                    liveTVPlayFragment, LIVETV_TAG).commit();
            //长稳问题，commit改为commitAllowingStateLoss
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_playvideo,
                    liveTVPlayFragment, LIVETV_TAG).commitAllowingStateLoss();
        } else {
            mHandler.sendEmptyMessageDelayed(MSG_DELAY_SHOW_TVGUIDE, 500);
        }
    }


    private void initFragment() {
        if (havePlayerView()) {
            handlerOldDetail();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initView();
                        }
                    });
                }
            }, 2000);
        } else {
            initView();
        }
    }

    /**
     * 监听遥控器按键 Down事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean hasCode = false;
        int codeValue = RemoteKeyEvent.getInstance().getKeyCodeValue(keyCode);
        if (isShowTVGuide(keyCode)) {
            hasCode = true;
            showTVGuide();
        }
        if (codeValue == RemoteKeyEvent.BTV) {
            hasCode = true;
            showTVGuide();
        } else if (codeValue == RemoteKeyEvent.TVOD) {
            jumpToCatchUp();
        }
        if (!hasCode && null != mKeyListener) {
            mKeyListener.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean isShowTVGuide(int keyCode) {
        return (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) && !(null != liveTVPlayFragment && liveTVPlayFragment.isTSTVState());
    }

    /**
     * 监听遥控器key UP事件
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        int codeValue = RemoteKeyEvent.getInstance().getKeyCodeValue(keyCode);
        if (!isShowTVGuide(codeValue) && codeValue != RemoteKeyEvent.BTV
                && codeValue != RemoteKeyEvent.TVOD
                && null != mKeyListener) {
            mKeyListener.onKeyUp(keyCode, event);
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 展示TVGUIDE页
     */
    public void showTVGuide() {

        if (mIsOnlyChannel) {
            EventBus.getDefault().post(new DisableTVGuideKeyEvent(false));
            return;
        }

        if (null != mTVGuideWindow) {
            mTVGuideWindow.dismiss();
            mTVGuideWindow = null;
        }
        if ((null == mTVGuideWindow || !mTVGuideWindow.isShowing())) {
            //通过popwindow父布局尺寸判断其是否完成渲染，防止show Popwindow时父布局渲染未完成导致crash,crash信息如下：
            //android.view.WindowManager$BadTokenException: Unable to add window -- token null is not valid; is your activity running?
            if (llContainer != null && llContainer.getWidth() > 0) {
                EventBus.getDefault().post(new DisableTVGuideKeyEvent(true));

                mTVGuideWindow = new TVGuidePopwindow(this, this, mSubjectId, mIsQuerySubject, mIsShowChannelNo, liveTVPlayFragment != null ? liveTVPlayFragment.getCurrentChildSubjectID() : "");
                mTVGuideWindow.show(llContainer);
                delayDismissTVGuide();
            }

        }
    }

    //跳往回看界面
    public void jumpToCatchUp() {
        Intent intent = new Intent(this, CatchUpActivity.class);
        startActivity(intent);
    }

    /**
     * 用于判断用户是否对界面进行了操作
     * 刷新操作时间
     *
     * @param event event
     */
    @Subscribe
    public void onEvent(ResponseKeyEvent event) {
        delayDismissTVGuide();
    }

    /**
     * 显示和关闭TVGUIDE
     *
     * @param event event
     */
    @Subscribe
    public void onEvent(TVGuideEvent event) {
        if (event.isShow()) {
            showTVGuide();
        } else {
            if (null != mTVGuideWindow && mTVGuideWindow.isShowing()) {
                mTVGuideWindow.dismiss();
            }
        }
    }

    /**
     * 弹窗销毁回调
     */
    @Override
    public void onDismiss() {
        EventBus.getDefault().post(new DisableTVGuideKeyEvent(false));
        mTVGuideWindow = null;
        System.gc();
    }

    /**
     * 不做任何操作的时候,延时关闭TVGUIDE页
     */
    private void delayDismissTVGuide() {
        mHandler.removeMessages(DISMISS_TVGUIDE);
        mHandler.sendEmptyMessageDelayed(DISMISS_TVGUIDE, DELAY_DISMISS_TIME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveDataHolder.get().setIsShowingSkip(false);
        currentState = ACTIVITY_ONDESTORY;
        mHandler.removeCallbacksAndMessages(null);
        if (null != mTVGuideWindow && mTVGuideWindow.isShowing() && !isFinishing()) {
            mTVGuideWindow.dismiss();
        }
        ActivityStackControlUtil.remove(new WeakReference<Activity>(this));
    }

    @Override
    protected void onStop() {
        super.onStop();
        currentState = ACTIVITY_ONSTOP;
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentState = ACTIVITY_ONPAUSE;
    }

    public String getContentCode() {
        if (null == liveTVPlayFragment) {
            return null;
        }
        return liveTVPlayFragment.getContentCode();
    }

    /**
     * 当前正在播放的url,甩屏需要。
     */
    public String getCurrentChannelUrl() {
        if (null == liveTVPlayFragment) {
            return null;
        }
        return liveTVPlayFragment.getCurrentUrl();
    }

    //把之前支持预播放的页面关闭,防止出现多个播放器
    private void handlerOldDetail() {
        List<WeakReference<Activity>> activityList = ActivityStackControlUtil.getActivityList();
        if (null != activityList && !activityList.isEmpty()) {
            for (int i = activityList.size() - 1; i >= 0; i--) {
                if (activityList.get(i).get() instanceof NewVodDetailActivity) {
                    NewVodDetailActivity activity = (NewVodDetailActivity) activityList.get(i).get();
                    if (activity.canPrePlay() || null != activity.getOnDemandTVPlayFragment()) {
                        //TODO 坚果投影仪出现播放器release比较慢导致新的播放页出现没有画面，在播放上个片源的问题
                        //TODO 暂时暂停并release解决，后期看有没有其他方法
                        if (null != activity.getOnDemandTVPlayFragment() && null != activity.getOnDemandTVPlayFragment().mPlayView) {
                            activity.getOnDemandTVPlayFragment().mPlayView.pausePlay();
                            //是VOD之间进行切换的场景，由于提前释放播放器会在onDestory时书签为0，上报错误书签，这种场景下
                            // 在onDestroy之前，手动上报书签，之后拦截onDestory报的书签
                            activity.getOnDemandTVPlayFragment().reportBookmark(BookMarkSwitchs.DESTORY);
                            activity.getOnDemandTVPlayFragment().setSwitchVOD(true);
                            activity.getOnDemandTVPlayFragment().mPlayView.releasePlayer();
                        }
                        activity.finish();
                    }
                }
            }
        }
    }

    //堆栈中，是否存在播放页面
    private boolean havePlayerView() {
        List<WeakReference<Activity>> activityList = ActivityStackControlUtil.getActivityList();
        if (null != activityList && !activityList.isEmpty()) {
            for (int i = activityList.size() - 1; i >= 0; i--) {
                if (activityList.get(i).get() instanceof NewVodDetailActivity) {
                    NewVodDetailActivity activity = (NewVodDetailActivity) activityList.get(i).get();
                    if (activity.canPrePlay() || null != activity.getOnDemandTVPlayFragment()) {
                        //TODO 坚果投影仪出现播放器release比较慢导致新的播放页出现没有画面，在播放上个片源的问题
                        //TODO 暂时暂停并release解决，后期看有没有其他方法
                        return true;
                    }
                }
            }
        }
        return false;
    }

}