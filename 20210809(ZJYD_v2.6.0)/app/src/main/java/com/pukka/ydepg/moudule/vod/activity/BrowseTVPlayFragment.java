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
package com.pukka.ydepg.moudule.vod.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.iflytek.ads.IAdvertisingService;
import com.iflytek.ads.IVideoControl;
import com.iflytek.xiri.Feedback;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.VODInfo;
import com.pukka.ydepg.common.ad.AdConstant;
import com.pukka.ydepg.common.ad.AdManager;
import com.pukka.ydepg.common.ad.AdUtil;
import com.pukka.ydepg.common.ad.IADListener;
import com.pukka.ydepg.common.ad.ui.CountDownRelativeLayouyt;
import com.pukka.ydepg.common.http.bean.node.EpisodesBean;
import com.pukka.ydepg.common.http.bean.node.XmppMessage;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.CreateBookmarkCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.PlayURLCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODDetailCallBack;
import com.pukka.ydepg.common.http.v6bean.v6node.AuthorizeResult;
import com.pukka.ydepg.common.http.v6bean.v6node.BookMarkSwitchs;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.PlayVodBean;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertContent;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertVideo;
import com.pukka.ydepg.common.http.v6bean.v6request.CreateBookmarkRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QuerySubjectDetailRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.ReportVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaService;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Play;
import com.pukka.ydepg.common.report.ubd.scene.UBDPlay;
import com.pukka.ydepg.common.utils.CornersTransform;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.PlayerAttriUtil;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.customui.PlayBackViewPopwindow;
import com.pukka.ydepg.customui.VodEpisodesView;
import com.pukka.ydepg.customui.VodSettingView;
import com.pukka.ydepg.event.FinishPlayUrlEvent;
import com.pukka.ydepg.event.OnDemandBackEvent;
import com.pukka.ydepg.event.PlayUrlEvent;
import com.pukka.ydepg.event.PlayerSkipChangeEvent;
import com.pukka.ydepg.event.PlayerSpeedChangeEvent;
import com.pukka.ydepg.inf.IPlayListener;
import com.pukka.ydepg.inf.IPlayState;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.mvp.contact.VideoPlayContact;
import com.pukka.ydepg.launcher.mvp.presenter.VideoPlayPresenter;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.base.BasePlayFragment;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.mytv.ZjYdUniAndPhonePayActivty;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewProductOrderActivity;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.JumpToH5OrderUtils;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;
import com.pukka.ydepg.moudule.player.ui.OnDemandVideoActivity;
import com.pukka.ydepg.moudule.player.util.OffScreenUtils;
import com.pukka.ydepg.moudule.player.util.RemoteKeyEvent;
import com.pukka.ydepg.moudule.player.util.ViewkeyCode;
import com.pukka.ydepg.moudule.vod.cache.VodDetailCacheService;
import com.pukka.ydepg.moudule.vod.cache.VoddetailUtil;
import com.pukka.ydepg.moudule.vod.presenter.BookmarkEvent;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.voice.VoiceVodListener;
import com.pukka.ydepg.moudule.voice.XiriVoiceVodUtil;
import com.pukka.ydepg.util.EventLogger;
import com.pukka.ydepg.util.PlayUtil;
import com.pukka.ydepg.view.PlayView;
import com.pukka.ydepg.xmpp.XmppManager;
import com.pukka.ydepg.xmpp.bean.XmppSuccessEvent;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * VOD点播视频播放界面,只能通过点播入口进来播放视频
 *
 * @author liudong Email： liudong@easier.cn
 * @version : 1.0
 * @Title: BrowseTVPlayFragment
 * @Package com.pukka.ydepg.moudule.browse
 * @date 2017/12/21 11:08
 */
public class BrowseTVPlayFragment extends BasePlayFragment<VideoPlayContact.IVideoPlayPresenter>
        implements IPlayListener, PlayURLCallBack, VoiceVodListener, VODDetailCallBack, VideoPlayContact.IVideoPlayView, IVideoControl {

    private final String TAG = this.getClass().getName();

    //退出播放，销毁当前播放器
    public static final int BACK_PLAY = 11111;

    public static final int NEXT_PLAY = 11112;

    public static final int EPISODE_PLAY = 11113;

    public static final int HIDE_TRY_SEE_HINT = 11114;

    public static final int UPDATE_DURATION = 11115;

    public static final int HIDE_MARKETING_CONTENT = 11116;

    public static final int AIDL_ON_VIDEO_PAUSE = 11117;

    public static final int AIDL_ON_VIDEO_REPLAY = 11118;

    private List<Float> speedList = new ArrayList<>();

    //ssp广告的relationID,用于话单上报
    private String sspAdvertRelationID;

    /**
     * 播放器根布局
     */
    @BindView(R.id.fm_ondemand_container)
    FrameLayout mOnDemandContainer;

    /**
     * 点播名称
     */
    @BindView(R.id.tv_subtitle)
    TextView vodTitletv;

    /**
     * 试看布局
     */
    @BindView(R.id.tryseelayout)
    LinearLayout tryseelayout;

    /**
     * 试看布局
     */
    @BindView(R.id.seehinttv)
    TextView seehinttv;

    /**
     * 营销内容容器
     */
    @BindView(R.id.marketing_content_container)
    LinearLayout marketingContentContainer;

    @BindView(R.id.marketing_content)
    ImageView marketingContent;

    @BindView(R.id.advert_hint)
    CountDownRelativeLayouyt advert_hint;

    /**
     * 暂停以及keyback的时候都会弹出的popwindow
     */
    private PlayBackViewPopwindow mPlayBackView;

    /**
     * 当前视频播放状态
     */
    private int mCurrentPlayState = -1;

    /**
     * 是否是电影，0为电影，1为电视剧
     */
    private String isFilm = "0";

    //点播播放对象
    private PlayVodBean playVodBean;

    //播放器url
    private String videoPath;

    //当前剧集数
    private String sitcomNO = "";

    //VOD对象的id
    private String subContentId = "";

    //影片vodId
    private String videoId = "";
    //广告播放是否结束
    private boolean isAdPlayEnd;
    private int tailDuration;
    private boolean isSkipStart;
    //广告播放是不是点击了返回键
    private boolean isadKeyBack;

    //=1:从首页视频窗口进入此界面，屏蔽遥控器上键
    private int mPlayFrom = 0;

    public static void setEpisodesBean(EpisodesBean episodesBean) {
        BrowseTVPlayFragment.episodesBean = episodesBean;
    }

    private static EpisodesBean episodesBean;

    //是否是回看
    private boolean isLookBack = false;

    //剧集list
    private List<Episode> episodeDetailList = new ArrayList<>();

    //选集弹出框
    private VodEpisodesView vodEpisodesView;
    private VodSettingView vodSettingView;

    //播放是否结束
    private boolean isComplete;

    //播放鉴权接口
    private DetailPresenter mDetailPresenter;

    //总时长
    private long totalTimes;

    private long currentTimes;

    /**
     * 初始化Handler
     */
    private OnDemandHandler mHandler = new OnDemandHandler(this);

    //语音监听
    private XiriVoiceVodUtil mXiriVoiceVodUtil;

    private XmppMessage xmppmessage;

    private OffScreenUtils mOffScreenUtils;

    //连续快进
    private long rightTime;

    //连续快退
    private long leftTime;

    //快进速度控制
    private int countForward;

    //是否是试看状态 1：试看
    private int tryToSeeFlag;

    //媒体播放时长
    private String elapseTime = "0";

    private int tryToSeeTime = 5 * 60;

    //第一次保存传过来的书签，切换子集不需要再从书签播放
    private boolean isFirstFlag = true;
    private boolean isFirstStartFlag = true;

    /**
     * 点播上报需要栏目id
     */
    private String subjectId;

    /**
     * 点播上报需要产品id
     */
    private String productId;

    /**
     * 点播上报vodId
     */

    private String reportVodId;
    /**
     * 点播上报MediaId
     */
    private String reportMediaId;


    private String newReportVodId;

    private String newReportMediaId;
    /**
     * 回看是否播放结束了,重新拉起播放器
     */
    private boolean isLookBackPlayEnd;

    private String fatherPrice;

    /**
     * 判断是否按了快进快退按钮 0未播放，1正常播放
     */
    private int forwardAndBackRate;
    /**
     * 当前播放的mediaId;
     */
    private String currentMediaId;
    /**
     * 是否试看订购成功
     */
    private boolean isTrySeeSubscribeSucess;

    /**
     * 子集的扩展参数
     */
    private List<NamedParameter> epsiodeCustomFields;

    private int vodTime;

    /**
     * voddetail详情
     */
    private VODDetail mVODDetail;

    private String trailVideoMarketingImageURL;

    /**
     * 当前正在播放vod系统分配的内容Code。
     */
    private String contentCode;

    /**
     * 当前正在播放的url,甩屏需要。
     */
    private String currentUrl;

    /**
     * UBD的action
     */
    private String action = UBDConstant.ACTION.PLAY_NORMAL;

    private boolean isNeedBackToStart = false;

    private boolean isNeedShowSkipBottomRecord = true;

    private boolean isNeedToSkip = false;

    private int speed = 1;

    private boolean isXiriChange = false;

    private boolean isSeekStart = false;

    private HomeKeyReceiver homeKeyReceiver;

    private PowerOffReceiver powerOffReceiver;

    private boolean canSetSpeed = true;

    private boolean canSetSkip = true;

    private String lastPlayID;
    private String lastPlayUrl;

    private Episode currentEpisode;

    @Override
    public <Z> LifecycleTransformer<Z> bindToLife() {
        return bindUntilEvent(FragmentEvent.DESTROY_VIEW);
    }

    @Override
    public void onChannelColumn() {

    }

    @Override
    public void onChannelIndex(int index) {

    }

    @Override
    public void getChannelPlayKey(String playKey) {

    }

    @Override
    public void getProductID(String productID) {

    }

    /**
     * 防止内存泄漏
     */
    private static class OnDemandHandler extends Handler {

        private WeakReference<BrowseTVPlayFragment> mReference;

        OnDemandHandler(BrowseTVPlayFragment fragment) {
            this.mReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BrowseTVPlayFragment tvPlayFragment;
            if (null != mReference && null != (tvPlayFragment = mReference.get())) {
                switch (msg.what) {
                    case RemoteKeyEvent.VOD_FAST_FORWARD:
                        tvPlayFragment.mPlayView.fastForward();
                        tvPlayFragment.mPlayView.showControlView(true);
                        break;
                    case RemoteKeyEvent.VOD_FAST_REWIND:
                        tvPlayFragment.mPlayView.rewind();
                        tvPlayFragment.mPlayView.showControlView(true);
                        break;
                    case ViewkeyCode.VIEW_KEY_BACK_TV_PLAY:
                        if (null != tvPlayFragment.mPlayBackView && !tvPlayFragment.mPlayBackView.isShowing()) {
                            tvPlayFragment.mPlayBackView.dismiss();
                        }
                        if (!tvPlayFragment.getActivity().isDestroyed()) {
                            tvPlayFragment.backPlay();
                        }
                        break;
                    case ViewkeyCode.VIEW_KEY_CONTINUE_PLAY:
                        tvPlayFragment.reportBookmark(BookMarkSwitchs.REPLAY);
                        if (tvPlayFragment.isCanReplayByTrySee()) {
                            tvPlayFragment.mPlayView.resumePlay();
                            tvPlayFragment.mPlayView.showControlView(true);
                            tvPlayFragment.vodInfo.setPlayState(4);
                            try {
                                Log.e("ADS_AdvertisingBinder", "VIEW_KEY_CONTINUE_PLAY1");
                                tvPlayFragment.iAdvertisingService.playStateChange(JsonParse.object2String(tvPlayFragment.vodInfo), 2, (int) tvPlayFragment.mPlayView.getCurrentPosition() / 1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (hasMessages(UPDATE_DURATION)) {
                                removeMessages(UPDATE_DURATION);
                            }
                            sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
                        }
                        break;
                    case ViewkeyCode.VIEW_KEY_DETAIL_PLAY:
                        break;
                    case ViewkeyCode.VIEW_KEY_REPLAY:
                        tvPlayFragment.reportBookmark(BookMarkSwitchs.REPLAY);
                        if (tvPlayFragment.isLookBackPlayEnd) {
                            tvPlayFragment.mPlayView.rePlay();
                            tvPlayFragment.isLookBackPlayEnd = false;
                        } else {
                            tvPlayFragment.mPlayView.seekTo(0);
                            tvPlayFragment.mPlayView.resumePlay();
                        }
                        if (null != tvPlayFragment.mPlayBackView) {
                            tvPlayFragment.mPlayBackView.dismiss();
                        }
                        tvPlayFragment.mPlayView.showControlView(true);
                        tvPlayFragment.vodInfo.setPlayState(4);
                        try {
                            Log.e("ADS_AdvertisingBinder", "VIEW_KEY_REPLAY2");
                            tvPlayFragment.iAdvertisingService.playStateChange(JsonParse.object2String(tvPlayFragment.vodInfo), 2, (int) tvPlayFragment.mPlayView.getCurrentPosition() / 1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case ViewkeyCode.VIEW_KEY_EXIT:
                        if (!tvPlayFragment.getActivity().isDestroyed()) {
                            tvPlayFragment.backPlay();
                        }
                        break;
                    case NEXT_PLAY:
                        tvPlayFragment.reportBookmark(BookMarkSwitchs.SWITCH);
                        tvPlayFragment.handlerNextPlay();
                        break;
                    case BACK_PLAY:
                        tvPlayFragment.backPlay();
                        break;
                    case EPISODE_PLAY:
                        tvPlayFragment.episondePlay(msg);
                        break;
                    case HIDE_TRY_SEE_HINT:
                        tvPlayFragment.tryseelayout.setVisibility(View.GONE);
                        tvPlayFragment.marketingContentContainer.setVisibility(View.GONE);
                        break;
                    case UPDATE_DURATION:
                        if (tvPlayFragment.tryToSeeFlag == 1) {
                            long currentPosition = tvPlayFragment.mPlayView.getCurrentPosition();
                            if (hasMessages(UPDATE_DURATION)) {
                                removeMessages(UPDATE_DURATION);
                            }
                            if (currentPosition * 1L >= tvPlayFragment.tryToSeeTime * 1000L) {
                                tvPlayFragment.pauseTrySee();
                            } else {
                                sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
                                String content = tvPlayFragment.seehinttv.getText().toString();
                                if (content.contains(tvPlayFragment.getString(R.string.trysee_finish_hint))) {
                                    tvPlayFragment.tryseelayout.setVisibility(View.GONE);
                                    tvPlayFragment.marketingContentContainer.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            if (hasMessages(UPDATE_DURATION)) {
                                removeMessages(UPDATE_DURATION);
                            }
                        }
                        break;
                    case HIDE_MARKETING_CONTENT:
                        tvPlayFragment.marketingContentContainer.setVisibility(View.GONE);
                        break;
                    case AIDL_ON_VIDEO_PAUSE:
                        if (!tvPlayFragment.mPlayBackView.isShowing() && tvPlayFragment.mPlayView.isPlaying() && tvPlayFragment.mPlayView.getDuration() > 0) {
                            tvPlayFragment.mPlayBackView.showPlayBack(tvPlayFragment.getView(), false, null);
                        } else if (!tvPlayFragment.mPlayView.isPlaying() && tvPlayFragment.mPlayBackView.isShowing()) {//解决MG100Vod播放界面点击ok键暂停-进设置-调节分辨率-返回到播控界面-点击ok键重新播放dialog未消失问题
                            tvPlayFragment.mPlayBackView.dismiss();
                        }

                        tvPlayFragment.reportBookmark(BookMarkSwitchs.PAUSE);
                        tvPlayFragment.vodInfo.setPlayState(3);
                        try {
                            Log.e("ADS_AdvertisingBinder", "AIDL_ON_VIDEO_PAUSE3");
                            tvPlayFragment.iAdvertisingService.playStateChange(JsonParse.object2String(tvPlayFragment.vodInfo), 2, (int) tvPlayFragment.mPlayView.getCurrentPosition() / 1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tvPlayFragment.mPlayView.pausePlay();
                        tvPlayFragment.mPlayView.showControlView(true);
                        break;
                    case AIDL_ON_VIDEO_REPLAY:
                        if (!tvPlayFragment.mPlayBackView.isShowing() && tvPlayFragment.mPlayView.isPlaying() && tvPlayFragment.mPlayView.getDuration() > 0) {
                            tvPlayFragment.mPlayBackView.showPlayBack(tvPlayFragment.getView(), false, null);
                        } else if (!tvPlayFragment.mPlayView.isPlaying() && tvPlayFragment.mPlayBackView.isShowing()) {//解决MG100Vod播放界面点击ok键暂停-进设置-调节分辨率-返回到播控界面-点击ok键重新播放dialog未消失问题
                            tvPlayFragment.mPlayBackView.dismiss();
                        }
                        tvPlayFragment.reportBookmark(BookMarkSwitchs.REPLAY);
                        tvPlayFragment.vodInfo.setPlayState(4);

                        try {
                            Log.e("ADS_AdvertisingBinder", "AIDL_ON_VIDEO_REPLAY3");
                            tvPlayFragment.iAdvertisingService.playStateChange(JsonParse.object2String(tvPlayFragment.vodInfo), 2, (int) tvPlayFragment.mPlayView.getCurrentPosition() / 1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tvPlayFragment.mPlayView.resumePlay();
                        tvPlayFragment.mPlayView.showControlView(true);
                        break;
                    default:
                        break;
                }
            }
        }
    }


    public void updatePlayer(XmppMessage mXmppMessage) {
        isFirstFlag = true;
        isFirstStartFlag = true;
        if (null != mPlayView) {
            mPlayView.releasePlayer();
        }
        if (null != mXmppMessage) {
            xmppmessage = mXmppMessage;
            mOffScreenUtils = new OffScreenUtils(xmppmessage, (BaseActivity) getActivity(), offScreenListener);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initDate();
        if (isNeedStartXiriAidl()) {
            try {
                Intent intent = new Intent("com.iflytek.ads.action.ADVERTISEMENT");
                getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        homeKeyReceiver = new HomeKeyReceiver();
        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        //安全扫描修改
        // [ 问题描述： ] 攻击窗口是由不安全的使用动态注册的广播导致的，在存在攻击窗口的情况下，
        //  第三方应用程序可以发送伪造消息，控制、欺骗或导致目标程序崩溃。
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(homeKeyReceiver, homeFilter);
//        getActivity().registerReceiver(homeKeyReceiver, homeFilter);

        powerOffReceiver = new PowerOffReceiver();
        IntentFilter powerFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);

        //安全扫描修改
        // [ 问题描述： ] 攻击窗口是由不安全的使用动态注册的广播导致的，在存在攻击窗口的情况下，
        //  第三方应用程序可以发送伪造消息，控制、欺骗或导致目标程序崩溃。
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(powerOffReceiver, powerFilter);
//        getActivity().registerReceiver(powerOffReceiver, powerFilter);
        return view;
    }

    //免费观看结束后操作
    public void pauseTrySee() {
        mPlayView.seekTo(tryToSeeTime * 1000);
        mPlayView.pausePlay();
        String timeStr = getTrySeeTime(tryToSeeTime);
        seehinttv.setText(timeStr + getString(R.string.trysee_finish_hint));
        seehinttv.setHighlightColor(getActivity().getResources().getColor(android.R.color
                .transparent));
        mHandler.removeMessages(HIDE_TRY_SEE_HINT);
        mHandler.removeMessages(HIDE_MARKETING_CONTENT);
        tryseelayout.setVisibility(View.VISIBLE);
        marketingContentContainer.setVisibility(View.VISIBLE);

    }

    public void initDate() {
        speedList.clear();
        String speeds = SessionService.getInstance().getSession().getTerminalConfigurationValue("vod_speed_list");
        if (TextUtils.isEmpty(speeds)) {
            Log.e(TAG, "TextUtils.isEmpty(speeds)");
            speedList.add(0.8f);
            speedList.add(1.0f);
            speedList.add(1.25f);
            speedList.add(1.5f);
            speedList.add(2.0f);
        } else {
            try {
                String[] speedarray = speeds.split(",");
                if (speedarray != null && speedarray.length == 5) {
                    Log.e(TAG, "speedarray != null && speedarray.length == 5");
                    for (String speed : speedarray) {
                        speedList.add(Float.parseFloat(speed));
                    }
                } else {
                    Log.e(TAG, "speedarray != null && speedarray.length == 5 !!!");
                    speedList.add(0.8f);
                    speedList.add(1.0f);
                    speedList.add(1.25f);
                    speedList.add(1.5f);
                    speedList.add(2.0f);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                speedList.add(0.8f);
                speedList.add(1.0f);
                speedList.add(1.25f);
                speedList.add(1.5f);
                speedList.add(2.0f);
            }
        }
        Serializable serializable = getArguments().getSerializable(XmppManager.XML_MESSAGE);
        if (null != serializable) {
            xmppmessage = (XmppMessage) serializable;
        }
        mDetailPresenter = new DetailPresenter((BaseActivity) getActivity());

        mPlayBackView = new PlayBackViewPopwindow(getActivity(), mHandler, -1);
        //设置允许自动播放,为false的话则是不自动播放
        mPlayView.setShouldAutoPlay(true);
        mPlayView.setOnPlayCallback(this);
        String canSetSkipStr = SessionService.getInstance().getSession().getTerminalConfigurationVOD_SKIP_SWITCH();
        if (TextUtils.isEmpty(canSetSkipStr) || canSetSkipStr.equals("1") || canSetSkipStr.equals("0")) {
            canSetSkip = false;
        } else {
            canSetSkip = true;
        }

        mXiriVoiceVodUtil = new XiriVoiceVodUtil(getActivity(), this, new Feedback(getActivity()));
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SuperLog.debug(TAG, "onActivityCreated");
        if (null != xmppmessage) {
            mOffScreenUtils = new OffScreenUtils(xmppmessage, (BaseActivity) getActivity(), offScreenListener);
        } else {
            getIntentInfo(null);
        }
        initTrailVideoMarketingImage();
    }


    private OffScreenUtils.OffScreenListener offScreenListener = new OffScreenUtils.OffScreenListener() {
        @Override
        public void offScreenSuccess(PlayVodBean playVodBean) {
            getIntentInfo(playVodBean);
            if (null != mPlayBackView && mPlayBackView.isShowing()) {
                mPlayBackView.dismiss();
            }
        }

        @Override
        public void offScreenFail(VODDetail mVODDetail) {

        }

    };

    public void getIntentInfo(PlayVodBean mPlayVodBean) {
        Bundle bundle = getArguments();

        mPlayFrom = bundle.getInt(OnDemandVideoActivity.IS_FROM_WINDOW_PLAY,0);

        isLookBack = bundle.getBoolean(BasePlayFragment.ISPLAYBACK);
        if (null != mPlayVodBean) {
            playVodBean = mPlayVodBean;
        } else {
            String playVodBeanStr = "";
            if (!TextUtils.isEmpty(bundle.getString(OnDemandVideoActivity.PLAY_VOD_BEAN))) {
                //解决H5界面调用会看崩溃问题(只有H5界面会传此参数)
                playVodBeanStr = bundle.getString(OnDemandVideoActivity.PLAY_VOD_BEAN);
            } else {
                playVodBeanStr = LiveDataHolder.get().getPlayVodBean();
            }
            SuperLog.info(TAG, "playVodBeanStr=" + playVodBeanStr);
            if (null != playVodBeanStr && !playVodBeanStr.equals("")) {
                playVodBean = JsonParse.json2Object(playVodBeanStr, PlayVodBean.class);
            }
        }
        if (playVodBean != null) {
            if (!PlayerAttriUtil.isEmpty(playVodBean.getPlayUrl())) {
                videoPath = playVodBean.getPlayUrl();

            }

            isXiriChange = TextUtils.equals("1", playVodBean.getIsXiri());
            playVodBean.setIsXiri("0");
            lastPlayUrl = playVodBean.getLastPlayUrl();
            playVodBean.setLastPlayUrl(null);
            lastPlayID = playVodBean.getLastPlayID();
            playVodBean.setLastPlayID(null);
            if (!PlayerAttriUtil.isEmpty(playVodBean.getSitcomNO())) {
                sitcomNO = playVodBean.getSitcomNO();
                subContentId = playVodBean.getEpisodeId();

            }
            currentMediaId = playVodBean.getMediaId();
            if (!PlayerAttriUtil.isEmpty(playVodBean.getVodId())) {
                videoId = playVodBean.getVodId();

                isFilm = playVodBean.getIsFilm();
                if (episodesBean != null) {
                    this.episodeDetailList = episodesBean.getEpisodeList();
                }
            }
            if (isFromXmpp()) {
                EpgToast.showToast(getActivity(), "来自咪咕爱看用户" + StringUtils.getEncryptionNumber(xmppmessage.getActionSource()) + "甩屏发起");
            }
            tryToSeeFlag = playVodBean.getTryToSeeFlag();
            productId = playVodBean.getProductId();
            subjectId = playVodBean.getSubjectID();
            reportMediaId = playVodBean.getMediaId();
            if (!TextUtils.isEmpty(playVodBean.getEpisodeId())) {
                reportVodId = playVodBean.getEpisodeId();
            } else {
                reportVodId = playVodBean.getVodId();
            }

            vodInfo.setVid(reportVodId);

            if (!PlayerAttriUtil.isEmpty(playVodBean.getElapseTime())) {
                this.elapseTime = playVodBean.getElapseTime();
            }

            String detailStr = playVodBean.getDetailStr();
            if (!TextUtils.isEmpty(detailStr)) {
                mVODDetail = JsonParse.json2Object(detailStr, VODDetail.class);
                if (null != mVODDetail) {
                    fatherPrice = mVODDetail.getPrice();
                    contentCode = mVODDetail.getCode();
                }
            }
        }

        initVideoView(videoPath);
        if (mPlayVodBean == null && !TextUtils.isEmpty(lastPlayID)) {
            VODInfo vodInfo = new VODInfo();
            vodInfo.setVid(lastPlayID);
            vodInfo.setDstVid(reportVodId);
            vodInfo.setVideoFromUrl(lastPlayUrl);
            vodInfo.setVideoUrl(videoPath);
            vodInfo.setPlayState(9);
            Log.e("geptstde", JsonParse.object2String(vodInfo));
            try {
                if (isXiriChange) {
                    Log.e("ADS_AdvertisingBinder", "7");
                    iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 1, (int) mPlayView.getCurrentPosition() / 1000);
                } else {
                    Log.e("ADS_AdvertisingBinder", "8");
                    iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 2, (int) mPlayView.getCurrentPosition() / 1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getTrySeeTime(int tryToSeeTime) {
        StringBuffer buffer = new StringBuffer();
        int minute = tryToSeeTime / 60;
        int second = tryToSeeTime % 60;
        if (minute == 0) {
            return buffer.append(second).append(getResources().getString(R.string.second)).toString();
        } else {
            if (second == 0) {
                return buffer.append(minute).append(getResources().getString(R.string.miunte)).toString();
            } else {
                return buffer.append(minute).append(getResources().getString(R.string.miunte)).append(second).append(getResources().getString(R.string.second)).toString();
            }
        }
    }

    @Override
    protected void initPresenter() {
        presenter = new VideoPlayPresenter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void initTrailVideoMarketingImage() {
        trailVideoMarketingImageURL = OTTApplication.getContext().getVideoTrialMarketingContentImageURL();
        SuperLog.debug(TAG, String.format("The cached marketing content image url is: %s", trailVideoMarketingImageURL));
        if (TextUtils.isEmpty(trailVideoMarketingImageURL)) {
            // 获取终端配置参数：配置营销海报的栏目的ID
            String videoTrialMarketingContentSubjectId = SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.VIDEO_TRIAL_MARKETING_CONTENT_SUBJECT_ID);

            SuperLog.info2SD(TAG, String.format("The Marketing content subject id is: %s", videoTrialMarketingContentSubjectId));
            if (!TextUtils.isEmpty(videoTrialMarketingContentSubjectId)) {

                List<String> subjectIds = new ArrayList<String>();
                subjectIds.add(videoTrialMarketingContentSubjectId);

                QuerySubjectDetailRequest marketingContentRequest = new QuerySubjectDetailRequest();
                marketingContentRequest.setSubjectIds(subjectIds);

                presenter.initVideoTrailMarketingContent(marketingContentRequest, getContext());
            }
        } else {
            initVideoTrailMarketingContent(trailVideoMarketingImageURL);
        }
    }

    public void initVideoView(String videoPath) {
        SuperLog.debug(TAG, "initVideoView");

        vodInfo.setVid(reportVodId);
        vodInfo.setDstVid(newReportVodId);
        vodInfo.setVideoFromUrl(currentUrl);
        vodInfo.setVideoUrl(videoPath);
        //前贴广告
        AdvertVideo mAdvertVideo = playVodBean.getAdvertVideo();
        currentUrl = videoPath;
        if (null != playVodBean && !TextUtils.isEmpty(playVodBean.getVodType()) && playVodBean.getVodType().equals(Content.PROGRAM)) {
            mPlayView.setResizeMode(PlayView.RESIZE_MODE_FIT);
        }
        if (tryToSeeFlag == 1) {
            mPlayView.setTryToSeeFlag(tryToSeeFlag);
            if (null != mVODDetail) {
                if (null != epsiodeCustomFields) {
                    tryToSeeTime = VodDetailCacheService.getPreviewDuration(mVODDetail.getCustomFields(), epsiodeCustomFields, vodTime);
                } else {
                    tryToSeeTime = mVODDetail.getPreviewDuration();
                    if (tryToSeeTime == -1) {
                        tryToSeeTime = VodDetailCacheService.getPreviewDuration(vodTime);
                    }
                }
            } else {
                String trySee = SessionService.getInstance().getSession().getTerminalConfigurationValue("vod_watch_validTime");
                if (!TextUtils.isEmpty(trySee)) {
                    tryToSeeTime = Integer.parseInt(trySee);
                }
            }
            mPlayView.setTryToSeeTime(tryToSeeTime * 1000);
            String timeStr = getTrySeeTime(tryToSeeTime);
            seehinttv.setText(String.format(getResources().getString(R.string.notice_free_trial_play), timeStr));
            seehinttv.setHighlightColor(getActivity().getResources().getColor(android.R.color
                    .transparent));

        } else {
            if (mHandler.hasMessages(HIDE_TRY_SEE_HINT)) {
                mHandler.removeMessages(HIDE_TRY_SEE_HINT);
                mHandler.removeMessages(HIDE_MARKETING_CONTENT);
            }
            if (mHandler.hasMessages(UPDATE_DURATION)) {
                mHandler.removeMessages(HIDE_TRY_SEE_HINT);
            }
            //设置播放器是否试看
            mPlayView.setTryToSeeFlag(0);
            tryseelayout.setVisibility(View.GONE);
            marketingContentContainer.setVisibility(View.GONE);
        }
        mPlayView.releasePlayer();
        if (null != playVodBean && !PlayerAttriUtil.isEmpty(playVodBean.getBookmark()) && isFirstFlag) {
            if (tryToSeeFlag == 1) {
                //甩屏过来书签超过可看范围
                //mPlayView.setSkipStart(false);
                long bookMark = Long.parseLong(playVodBean.getBookmark());
                if (bookMark >= tryToSeeTime) {
                    //&&playVodBean.getTryToSeeFlag()==1
                    mPlayView.startPlay(videoPath, (mAdvertVideo != null) ? mAdvertVideo.getCurl() : null, (tryToSeeTime - 1) * 1000);
                } else {
                    mPlayView.startPlay(videoPath, (mAdvertVideo != null) ? mAdvertVideo.getCurl() : null, Long.parseLong(playVodBean.getBookmark()) * 1000);
                }
            } else {
//                mPlayView.startPlay(videoPath, Long.parseLong(playVodBean.getBookmark()) * 1000);

//                boolean isSkipStart = SharedPreferenceUtil.getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "", true);
                isSkipStart = SharedPreferenceUtil.getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "", true);

                /*跳过片头片尾开关
                 * 0 为打开
                 * 1 为关闭
                 * 2 或者 不配置 或者 2 以外的值 为由用户自行设置
                 */
                String skipSwitch = SessionService.getInstance().getSession().getTerminalConfigurationVOD_SKIP_SWITCH();
                if (null != skipSwitch && skipSwitch.equals("0")) {
                    isSkipStart = true;
                } else if (null != skipSwitch && skipSwitch.equals("1")) {
                    isSkipStart = false;
                }

                if (!TextUtils.isEmpty(playVodBean.getVodType()) && TextUtils.equals(playVodBean.getVodType(), Content.PROGRAM)) {
                    isSkipStart = false;
                }
//                mPlayView.setSkipStart(isSkipStart);
                int headDuration = 0;
                tailDuration = 0;
                if (TextUtils.equals("0", isFilm) && mVODDetail != null) {
                    headDuration = mVODDetail.getMediaFiles().get(0).getHeadDuration();
                    tailDuration = mVODDetail.getMediaFiles().get(0).getTailDuration();
                    if (headDuration == 0 && tailDuration == 0) {
                        mPlayView.setNeedToSkip(false);
                        isNeedToSkip = false;
                    } else {
                        mPlayView.setNeedToSkip(true);
                        isNeedToSkip = true;
                    }
                } else {
                    if (episodeDetailList != null && episodeDetailList.size() > 0) {
                        for (Episode episode : episodeDetailList) {
                            if (TextUtils.equals(episode.getSitcomNO(), sitcomNO)) {
                                headDuration = episode.getVOD().getMediaFiles().get(0).getHeadDuration();
                                tailDuration = episode.getVOD().getMediaFiles().get(0).getTailDuration();
                                if (headDuration == 0 && tailDuration == 0) {
                                    mPlayView.setNeedToSkip(false);
                                    isNeedToSkip = false;
                                } else {
                                    mPlayView.setNeedToSkip(true);
                                    isNeedToSkip = true;
                                }
                                break;
                            }
                        }
                    }
                }
//                mPlayView.setEndTime(tailDuration * 1000);
                isNeedShowSkipBottomRecord = Long.parseLong(playVodBean.getBookmark()) < headDuration;
                mPlayView.startPlay(videoPath, (mAdvertVideo != null) ? mAdvertVideo.getCurl() : null, isSkipStart && isNeedShowSkipBottomRecord ? headDuration * 1000 : Long.parseLong(playVodBean.getBookmark()) * 1000);
            }
            isFirstFlag = false;
        } else {
//            mPlayView.startPlay(videoPath, 0);
            int headDuration = 0;
            tailDuration = 0;
            if (TextUtils.equals("0", isFilm) && mVODDetail != null) {
                headDuration = mVODDetail.getMediaFiles().get(0).getHeadDuration();
                tailDuration = mVODDetail.getMediaFiles().get(0).getTailDuration();
                if (headDuration == 0 && tailDuration == 0) {
                    mPlayView.setNeedToSkip(false);
                    isNeedToSkip = false;
                } else {
                    mPlayView.setNeedToSkip(true);
                    isNeedToSkip = true;
                }
            } else {
                if (episodeDetailList != null && episodeDetailList.size() > 0) {
                    for (Episode episode : episodeDetailList) {
                        if (TextUtils.equals(episode.getSitcomNO(), sitcomNO)) {
                            headDuration = episode.getVOD().getMediaFiles().get(0).getHeadDuration();
                            tailDuration = episode.getVOD().getMediaFiles().get(0).getTailDuration();
                            if (headDuration == 0 && tailDuration == 0) {
                                mPlayView.setNeedToSkip(false);
                                isNeedToSkip = false;
                            } else {
                                mPlayView.setNeedToSkip(true);
                                isNeedToSkip = true;
                            }
                            break;
                        }
                    }
                }
            }
            isSkipStart = SharedPreferenceUtil.getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "", true);

            /*跳过片头片尾开关
             * 0 为打开
             * 1 为关闭
             * 2 或者 不配置 或者 2 以外的值 为由用户自行设置
             */
            String skipSwitch = SessionService.getInstance().getSession().getTerminalConfigurationVOD_SKIP_SWITCH();
            if (null != skipSwitch && skipSwitch.equals("0")) {
                isSkipStart = true;
            } else if (null != skipSwitch && skipSwitch.equals("1")) {
                isSkipStart = false;
            }

            if (!TextUtils.isEmpty(playVodBean.getVodType()) && TextUtils.equals(playVodBean.getVodType(), Content.PROGRAM)) {
                isSkipStart = false;
            }
//            mPlayView.setSkipStart(isSkipStart);
//            mPlayView.setEndTime(tailDuration * 1000);
            isNeedShowSkipBottomRecord = true;
            mPlayView.startPlay(videoPath, (mAdvertVideo != null) ? mAdvertVideo.getCurl() : null, isSkipStart ? headDuration * 1000 : 0);
        }
        if (playVodBean != null) {
            if (!PlayerAttriUtil.isEmpty(playVodBean.getVodName())) {
                if (isNeedToSkip) {
                    vodTitletv.setText(playVodBean.getVodName());
                    mPlayView.setVodTitle(playVodBean.getVodName());
                    if (!TextUtils.isEmpty(sitcomNO)) {
                        vodTitletv.setText(playVodBean.getVodName() + "第" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "集" : "期"));
                        mPlayView.setVodTitle(playVodBean.getVodName() + "第" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "集" : "期"));
                    }
                    vodTitletv.setSelected(true);
                    mPlayView.getmRecord().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.T24_C23_Light_size));
                } else {
                    mPlayView.getmRecord().setText(playVodBean.getVodName());
                    mPlayView.setVodTitle(playVodBean.getVodName());
                    if (!TextUtils.isEmpty(sitcomNO)) {
                        mPlayView.getmRecord().setText(playVodBean.getVodName() + "第" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "集" : "期"));
                        mPlayView.setVodTitle(playVodBean.getVodName() + "第" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "集" : "期"));
                    }
                    mPlayView.getmRecord().setSelected(true);
                    mPlayView.getmRecord().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.T29_C21_Bold_size));
                }
            } else {
                SuperLog.debug(TAG, "playVodBean为空");
            }
        }

//        //定时隐藏控制栏
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mPlayView.showControlView(true);
//            }
//        }, 3000);


    }

    @Override
    public void onPause() {
        super.onPause();
        currentTimes = mPlayView.getCurrentPosition();
        mPlayView.pausePlay();
        mXiriVoiceVodUtil.stopXiri();
    }

    @Override
    public void onStop() {
        super.onStop();
        reportBookmark(BookMarkSwitchs.DESTORY);
    }

    @Override
    public void onResume() {
        //防止出现恢复播放,弹窗还在
        if (null == mPlayBackView || !mPlayBackView.isShowing()) {
            if (isCanReplayByTrySee()) {
                if (tryToSeeFlag == 1) {
                    mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
                }
                mPlayView.resumePlay();
            }
        }
        mXiriVoiceVodUtil.startXiri();
        super.onResume();
        //上报UBD
        VODDetail vodDetail = mVODDetail;
        if (null == vodDetail && null != playVodBean) {
            vodDetail = new VODDetail();
            vodDetail.setID(playVodBean.getVodId());
            vodDetail.setName(playVodBean.getVodName());
            vodDetail.setContentType(playVodBean.getVodType());
        }

        //播放上报,内含引流上报
        UBDPlay.record(vodDetail,
                subjectId,
                tryToSeeFlag == 1,
                playVodBean == null ? null : playVodBean.getRecommendType(),
                this.getActivity().getClass().getSimpleName());
    }

    @Subscribe
    public void onEvent(PlayUrlEvent event) {
        if (event.isTrySeeSubscribe()) {
            isTrySeeSubscribeSucess = true;
            //电影
            if ("0".equals(isFilm)) {
                PlayVODRequest mPlayVodRequest = new PlayVODRequest();
                mPlayVodRequest.setVODID(videoId);
                mPlayVodRequest.setMediaID(currentMediaId);
                mPlayVodRequest.setURLFormat("1");
                mPlayVodRequest.setIsReturnProduct("1");
                mDetailPresenter.playVOD(mPlayVodRequest, this, "0");
            } else {
                //电视剧
                action = UBDConstant.ACTION.PLAY_NORMAL;
                playVODAuthorize(subContentId, videoId, currentMediaId, "0");
            }
        }

    }


    /**
     * 监听遥控器按键事件,按下按键时的监听
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int codeValue = RemoteKeyEvent.getInstance().getVODKeyCodeValue(keyCode);
        SuperLog.debug(TAG, "onkeyDown->codeValue:" + codeValue);
        SuperLog.debug(TAG, "onkeyDown->iscurrentPlayAdvert:" + mPlayView.isCurrentPlayAdvert() + "|isPlaying:" + mPlayView.isPlaying());
        if (null != mPlayView && mPlayView.isCurrentPlayAdvert() && mPlayView.isPlaying()) {
            SuperLog.debug(TAG, "onkeyDown->advert playing");
            if (null != advert_hint && !advert_hint.isCanSkip()) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    isadKeyBack = true;
                    //广告结束上报
                    AdManager.getInstance().reportAdvertDuration(sspAdvertRelationID, mPlayView.getCurrentPosition());
                    mHandler.sendEmptyMessage(ViewkeyCode.VIEW_KEY_BACK_TV_PLAY);
                }
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                isadKeyBack = true;
                AdManager.getInstance().reportAdvertDuration(sspAdvertRelationID, mPlayView.getCurrentPosition());
                mPlayView.seekTo(mPlayView.getDuration());

            }
            return true;
        }
        if (codeValue == RemoteKeyEvent.MEDIA_PAUSE_PLAY || codeValue == KeyEvent.KEYCODE_MEDIA_STOP) {
            if (tryToSeeFlag == 0) {
                if (!mPlayBackView.isShowing() && mPlayView.isPlaying() && mPlayView.getDuration() > 0) {
                    mPlayBackView.showPlayBack(getView(), false, null);
                } else if (!mPlayView.isPlaying() && mPlayBackView.isShowing()) {//解决MG100Vod播放界面点击ok键暂停-进设置-调节分辨率-返回到播控界面-点击ok键重新播放dialog未消失问题
                    mPlayBackView.dismiss();
                }
                if (mPlayView.isPause()) {
                    reportBookmark(BookMarkSwitchs.REPLAY);
                    vodInfo.setPlayState(4);
                } else {
                    reportBookmark(BookMarkSwitchs.PAUSE);
                    vodInfo.setPlayState(3);
                }

                try {
                    Log.e("ADS_AdvertisingBinder", "3");
                    iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 2, (int) mPlayView.getCurrentPosition() / 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mPlayView.playerOrPause();
                mPlayView.showControlView(true);
            } else {
                if (null != playVodBean && !TextUtils.isEmpty(playVodBean.getAuthResult())) {
                    AuthorizeResult authorizeResult = JsonParse.json2Object(playVodBean.getAuthResult(), AuthorizeResult.class);
                    if (null != authorizeResult) {
                        List<Product> products = authorizeResult.getPricedProducts();
                        if (SessionService.getInstance().getSession().isHotelUser()) {
                            if (products == null || products.size() == 0) {
                                EpgToast.showToast(getActivity(), getResources().getString(R.string.notice_no_orderable_product));
                                return true;
                            }
                            for (int i = products.size() - 1; i >= 0; i--) {
                                Product mProductInfo = products.get(i);
                                if (!StringUtils.isSubscribeByDay(mProductInfo)) {
                                    products.remove(i);
                                }

                            }
                            if (products == null || products.size() == 0) {
                                EpgToast.showToast(getActivity(), getResources().getString(R.string.notice_no_orderable_product));
                                return true;
                            }
                            authorizeResult.setPricedProducts(products);

                        } else {
                            if (products == null || products.size() == 0) {
                                EpgToast.showToast(getActivity(), getResources().getString(R.string.notice_no_orderable_product));
                                return true;

                            }
                        }

                    } else {
                        EpgToast.showToast(getActivity(), getResources().getString(R.string.notice_no_orderable_product));
                        return true;

                    }

                    String needJumpToH5Order = SessionService.getInstance().getSession().getTerminalConfigurationNeedJumpToH5Order();
                    if (null != needJumpToH5Order && needJumpToH5Order.equals("1")) {
                        JumpToH5OrderUtils.getInstance().jumpToH5ORderFromOffScreen(authorizeResult.getPricedProducts(), getActivity(), null != xmppmessage, xmppmessage, mVODDetail);
                        //pbs点击上报
                        Log.i(TAG, "PbsUaService: "+mVODDetail.getID());
                        PbsUaService.report(Play.getPurchaseData(mVODDetail.getID()));
                    } else {
                        Intent intent = new Intent(getActivity(), NewProductOrderActivity.class);
                        intent.putExtra(NewProductOrderActivity.AUTHORIZE_RESULT, JsonParse.object2String(authorizeResult));
                        intent.putExtra(ZjYdUniAndPhonePayActivty.IS_TRY_SEE_SUBSCRIBE, true);
                        intent.putExtra(ZjYdUniAndPhonePayActivty.ISOFFSCREEN_SUBSCRIBE, null != xmppmessage);
                        intent.putExtra(ZjYdUniAndPhonePayActivty.VOD_DETAIL, VodUtil.getSimpleVoddetail(mVODDetail, 5 * 60));
                        if (null != xmppmessage) {
                            intent.putExtra(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE, xmppmessage);
                        }
                        getActivity().startActivity(intent);
                    }
                } else {
                    EpgToast.showToast(getActivity(), getResources().getString(R.string.notice_no_orderable_product));

                }
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            //只有在当前popwindow不可见,同时当前媒体资源状态是可以播放的,可以随时暂停和play
            if (!mPlayBackView.isShowing() && (((mCurrentPlayState == IPlayState.PLAY_STATE_HASMEDIA
                    || mCurrentPlayState == IPlayState.PLAY_STATE_BUFFERING))
                    && mPlayView.getDuration() > 0) && isCanReplayByTrySee()) {
                mPlayBackView.showPlayBack(getView(), true, new PlayBackViewPopwindow.OnKeyBackDismissListener() {
                    @Override
                    public void onDismiss() {
                        //1.8新需求，呼出返回弹框，按返回键，播放销毁
                        backPlay();
//                        finish();
//                        playBackListener();
                    }
                });
                reportBookmark(BookMarkSwitchs.PAUSE);
                mPlayView.pausePlay();
                mPlayView.showControlView(true);
                vodInfo.setPlayState(3);
                try {
                    Log.e("ADS_AdvertisingBinder", "4");
                    iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 2, (int) mPlayView.getCurrentPosition() / 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return false;
        } else if (keyCode == KeyEvent.KEYCODE_CHANNEL_UP || keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN) {
            if (!isLookBack) {
                // 频道+ || 频道-
                mReminderDialog.showRemiderDialog(getResources().getString(R.string.notice_sure_back_to_liveTv), getView());
            }
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP && mPlayFrom == 0) {
            if (!"0".equals(isFilm) && !isLookBack) {
                if (!PlayerAttriUtil.isEmpty(videoId)) {
                    vodEpisodesView = new VodEpisodesView((BaseActivity) getActivity(), episodeDetailList, videoId, mHandler, sitcomNO, fatherPrice, speed, canSetSpeed, canSetSkip);
                    vodEpisodesView.showEpisodes(mOnDemandContainer);
                } else {
                    vodEpisodesView = new VodEpisodesView((BaseActivity) getActivity(), episodeDetailList, "", mHandler, sitcomNO, fatherPrice, speed, canSetSpeed, canSetSkip);
                    vodEpisodesView.showEpisodes(mOnDemandContainer);
                }
            } else if ("0".equals(isFilm) && !(null != playVodBean && !TextUtils.isEmpty(playVodBean.getVodType()) && TextUtils.equals(playVodBean.getVodType(), Content.PROGRAM))) {
                //既不支持设置跳过片头片尾也不支持设置倍速时摁上不显示设置页面
                if (canSetSkip || canSetSpeed) {
                    vodSettingView = new VodSettingView((BaseActivity) getActivity(), speed, canSetSpeed, canSetSkip);
                    vodSettingView.showEpisodes(mOnDemandContainer);
                }
            }
        } else if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            if (!isLookBack && null != mDetailPresenter && !TextUtils.isEmpty(videoId)) {
                mDetailPresenter.getVODDetail(videoId, this);
            }
        } else if ((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_PAGE_DOWN) && isCanReplayByTrySee()) {
            if (!isSeekStart) {
                isSeekStart = true;
                vodInfo.setPlayState(5);
                try {
                    Log.e("ADS_AdvertisingBinder", "start forward");
                    iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 2, (int) mPlayView.getCurrentPosition() / 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //快进
            SuperLog.debug(TAG, "keycode_drap_right");
            countForward += 1;//快进速度控制
            if (totalTimes <= 0) {
                return false;
            }
            if (leftTime > 0) {
                currentTimes = currentTimes - leftTime;
                leftTime = 0;
            }

            if (countForward < 3) {
                rightTime = rightTime + 10000;
                forwardAndBackRate = 1;
            } else if (countForward < 5) {
                forwardAndBackRate = 2;
                rightTime = rightTime + 20000;
            } else if (countForward < 7) {
                forwardAndBackRate = 4;
                rightTime = rightTime + 30000;
            } else if (countForward < 9) {
                forwardAndBackRate = 8;
                rightTime = rightTime + 40000;
            } else if (countForward < 11) {
                forwardAndBackRate = 16;
                rightTime = rightTime + 50000;
            } else {
                forwardAndBackRate = 32;
                rightTime = rightTime + 60000;
            }
            int currentPosition = (int) mPlayView.getCurrentPosition();
            if (tryToSeeFlag == 1) {
                //试看5min
                if ((currentPosition + rightTime) * 1L >= tryToSeeTime * 1000L) {
                    //退出播放页面
                    mPlayView.dragProgress(tryToSeeTime * 1000);
                    //超过5min钟
                    rightTime = tryToSeeTime * 1000L - currentPosition;
                } else {
                    mPlayView.dragProgress((int) (currentPosition + rightTime));
                }
            } else {
                if (currentPosition + rightTime >= totalTimes) {
                    mPlayView.dragProgress((int) totalTimes);
                } else {
                    mPlayView.dragProgress((int) (currentPosition + rightTime));
                }

            }
            mPlayView.showControlView(true);
        } else if ((keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_PAGE_UP)) {
            if (!isSeekStart) {
                isSeekStart = true;
                vodInfo.setPlayState(7);
                try {
                    Log.e("ADS_AdvertisingBinder", "start backforward");
                    iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 2, (int) mPlayView.getCurrentPosition() / 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (isNeedBackToStart) {
                seekTo(0);
            } else {
                //快退
                countForward += 1;//快退速度控制
                if (totalTimes <= 0) {
                    return false;
                }
                if (rightTime > 0) {
                    currentTimes = currentTimes + rightTime;
                    rightTime = 0;
                }
                if (countForward < 3) {
                    leftTime = leftTime + 10000;
                    forwardAndBackRate = 1;
                } else if (3 <= countForward && countForward < 5) {
                    leftTime = leftTime + 20000;
                    forwardAndBackRate = -2;
                } else if (5 <= countForward && countForward < 7) {
                    leftTime = leftTime + 30000;
                    forwardAndBackRate = -4;
                } else if (7 <= countForward && countForward < 9) {
                    leftTime = leftTime + 40000;
                    forwardAndBackRate = -8;
                } else if (9 <= countForward && countForward < 11) {
                    leftTime = leftTime + 50000;
                    forwardAndBackRate = -16;
                } else {
                    forwardAndBackRate = -32;
                    leftTime = leftTime + 60000;
                }
                mPlayView.showControlView(true);
                int currentPosition = (int) mPlayView.getCurrentPosition();
                if (currentPosition - leftTime <= 0) {
                    mPlayView.dragProgress(0);
                } else {
                    mPlayView.dragProgress((int) (currentPosition - leftTime));
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 监听遥控器按键事件,抬起按键时的监听
     */
    @Override
    public void onKeyUp(int keyCode, KeyEvent event) {
        super.onKeyUp(keyCode, event);
        SuperLog.debug(TAG, "rightTime:" + rightTime + "|leftTime:" + leftTime);
        if (null != mPlayView && mPlayView.isCurrentPlayAdvert() && mPlayView.isPlaying()) {
            return;
        }
        countForward = 0;
        isSeekStart = false;
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {
            keyForWard(rightTime / 1000);
            forwardAndBackRate = 1;
            vodInfo.setPlayState(6);
            try {
                Log.e("ADS_AdvertisingBinder", "end forward");
                iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 2, (int) mPlayView.getCurrentPosition() / 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            reportBookmark(BookMarkSwitchs.FASTFORWARD);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_PAGE_UP) {
            keyBackForward(leftTime / 1000);
            forwardAndBackRate = 1;
            vodInfo.setPlayState(8);
            try {
                Log.e("ADS_AdvertisingBinder", "end backforward");
                iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 2, (int) mPlayView.getCurrentPosition() / 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            reportBookmark(BookMarkSwitchs.FASTBACK);
        }
        mPlayView.restartUpdateProgress();
    }

    /**
     * 播放状态的回调监听
     */
    @Override
    public void onPlayState(int playbackState) {
        mCurrentPlayState = playbackState;
        SuperLog.debug(TAG, "playbackState:" + EventLogger.getStateString(playbackState));
        if (playbackState == IPlayState.PLAY_STATE_HASMEDIA) {
            totalTimes = mPlayView.getDuration();
            OTTApplication.getContext().setIsRefreshPbsEpg(true);
//            if(preparebg.getVisibility()==View.VISIBLE){
//                preparebg.setVisibility(View.GONE);
//            }
//            if (totalTimes <= 0&&!elapseTime.equals("0")) {
//                mPlayView.fixedDuration(Integer.parseInt(elapseTime) * 1000);
//            }
        }
    }

    @Override
    public void onPrepared(int videoType) {
        if (videoType == PlayUtil.VideoType.ADVERT) {
            isadKeyBack = false;
            isAdPlayEnd = false;
            advert_hint.setVisibility(View.VISIBLE);
            advert_hint.setTotalTimeAndStart(mPlayView.getDuration() / 1000,true);
            advert_hint.setPlayView(mPlayView);
        } else if (videoType == PlayUtil.VideoType.VOD) {
            //广告结束隐藏倒计时布局
            isAdPlayEnd = true;
            advert_hint.hide();
            advert_hint.setVisibility(View.GONE);
//            //广告结束上报
//            AdManager.getInstance().reportAdvertDuration(sspAdvertRelationID);
            if (tryToSeeFlag == 1) {
                tryseelayout.setVisibility(View.VISIBLE);
                mHandler.sendEmptyMessageDelayed(HIDE_TRY_SEE_HINT, 10 * 1000);
                marketingContentContainer.setVisibility(View.VISIBLE);
                mHandler.sendEmptyMessageDelayed(HIDE_MARKETING_CONTENT, 10 * 1000);
                mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
            } else {
                //跳过片头片尾放在这边，为了解决右前贴广告的时候播放界面异常的问题
                mPlayView.setSkipStart(isSkipStart);
                mPlayView.setEndTime(tailDuration * 1000);
            }
            if (!TextUtils.isEmpty(vodInfo.getDstVid())) {
                vodInfo.setVid(vodInfo.getDstVid());
                vodInfo.setDstVid(null);
            }
            vodInfo.setPlayState(1);
            Log.e("geptstd", JsonParse.object2String(vodInfo));
            try {
                if (isXiriChange) {
                    Log.e("ADS_AdvertisingBinder", "5");
                    iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 1, (int) mPlayView.getCurrentPosition() / 1000);
                } else {
                    Log.e("ADS_AdvertisingBinder", "6");
                    iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 2, (int) mPlayView.getCurrentPosition() / 1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            isXiriChange = false;
            if (timerTask != null) {
                timerTask.cancel();
                timerTask = null;
            }
            if (timer != null) {
                timer.cancel();
                timer = null;
            }

            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (mPlayView != null && mPlayView.isPlaying()) {
                            iAdvertisingService.showAd(reportVodId, (int) mPlayView.getCurrentPosition() / 1000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            timer = new Timer();
            timer.schedule(timerTask, 1000, 1000);

            mPlayView.setFirstShowTopRecord(true);
            mPlayView.setSpeed(switchSpeed());
            canSetSpeed = mPlayView.canSetSpeed(switchSpeed());
            Log.e("gwptest", "canSetSpeed is " + canSetSpeed);
            setRecordText(true);
            if (!"0".equals(isFilm) && !isLookBack) {
                mPlayView.setmBottomRecord();
            }
            mPlayView.showControlView(true);
            if (null != playVodBean && !PlayerAttriUtil.isEmpty(playVodBean.getBookmark()) && isFirstStartFlag && Long.parseLong(playVodBean.getBookmark()) > 0) {
                isFirstStartFlag = false;
                showBackToStartToast();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideBackToStartToast();
                    }
                }, 5000);
            }
            if (tryToSeeFlag == 0) {
                reportVod(ReportVODRequest.START_PLAY);
            }
            reportBookmark(BookMarkSwitchs.START);
        }

    }

    @Override
    public void onRelease() {
        mPlayView.setControllViewState(View.GONE, true);
        if (tryToSeeFlag == 0) {
            reportVod(ReportVODRequest.FINISH_PLAY);
        }
        if (!TextUtils.isEmpty(newReportVodId)) {
            reportVodId = newReportVodId;
            reportMediaId = newReportMediaId;
        }
    }

    /**
     * 播放出错
     */
    @Override
    public void onPlayError(String msg, int errorCode, int playerType) {
        if (!isFromXmpp() && !getIsXmpp()) {
            EpgToast.showToast(OTTApplication.getContext(), msg);
        }
    }

    /**
     * 播放结束
     */
    @Override
    public void onPlayCompleted() {
        if (isLookBack) {
            isLookBackPlayEnd = true;
            mPlayView.showControlView(true);
            if (!mPlayBackView.isShowing()) {
                mPlayBackView.showPlayBack(mOnDemandContainer, false, new PlayBackViewPopwindow.OnKeyBackDismissListener() {
                    @Override
                    public void onDismiss() {
                        playBackListener();
                    }
                }, true);
                mPlayBackView.setOutsideTouchable(false);
            }
            if (mPlayView.isPlaying()) {
                mPlayView.pausePlay();
            }
        } else {
            //是否是电视剧  0为电影
            if (isFilm.equals("0")) {
                isComplete = true;
                mHandler.sendEmptyMessage(BACK_PLAY);
            } else {
                mHandler.sendEmptyMessage(NEXT_PLAY);
            }

        }


    }

    @Override
    public void onDetached(long time) {

    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onTryPlayForH5() {

    }

    @Override
    public void onAdVideoEnd() {
        //广告结束上报
        if (!isadKeyBack) {
            if (mPlayView != null && mPlayView.isCurrentPlayAdvert())
                AdManager.getInstance().reportAdvertDuration(sspAdvertRelationID, mPlayView.getCurrentPosition());
        }
    }

    public void playBackListener() {
        SuperLog.debug(TAG, "playBackListener:tryToSeeTime:" + tryToSeeTime * 1000 + "|CurrentPosition:" + mPlayView.getCurrentPosition());
        if (isCanReplayByTrySee()) {
            mPlayView.resumePlay();
        } else {
            if (mHandler.hasMessages(UPDATE_DURATION)) {
                mHandler.removeMessages(UPDATE_DURATION);
            }
            mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
        }
    }

    /**
     * 返回直播确认按钮
     */
    @Override
    public void onConfirm() {
        super.onConfirm();
        //先销毁当前界面
        getActivity().finish();
        //跳转到直播界面..........
        Intent intent = new Intent(getActivity(), LiveTVActivity.class);
        intent.putExtra(LiveTVActivity.VIDEO_TYPE, LiveTVActivity.VIDEO_TYPE_LIVETV);
        startActivity(intent);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_ondemand;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SuperLog.debug(TAG, "createBookmark->currentTimes:" + currentTimes);
        if (null != mPlayView) {
            currentTimes = mPlayView.getCurrentPosition();
            mPlayView.onDestory();
        }
        EventBus.getDefault().post(new OnDemandBackEvent(currentUrl, reportVodId));
        mHandler.removeCallbacksAndMessages(null);
        if (null != mPlayBackView && mPlayBackView.isShowing()) {
            mPlayBackView.dismiss();
        }
        if (null != mReminderDialog) {
            mReminderDialog.dismiss();
        }
        SuperLog.info(TAG, "onDestroyView()=====");
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        if (timer != null) {
            timer.cancel();
            timer.cancel();
        }

        try {
            getActivity().unbindService(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (homeKeyReceiver != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(homeKeyReceiver);
        }

        if (powerOffReceiver != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(powerOffReceiver);
        }

    }

    private void reportBookmark(String reportType) {
        if (mPlayView != null) {
            currentTimes = mPlayView.getCurrentPosition();
        }
        String switchs = SessionService.getInstance().getSession().getTerminalConfigurationValue("add_bookmark_switchs");
        BookMarkSwitchs bookMarkSwitchs = null;
        if (!TextUtils.isEmpty(switchs)) {
            bookMarkSwitchs = JsonParse.json2Object(switchs, BookMarkSwitchs.class);
        }

        if (TextUtils.equals(reportType, BookMarkSwitchs.DESTORY) || TextUtils.equals(reportType, BookMarkSwitchs.QUIT) || (bookMarkSwitchs != null && TextUtils.equals("0", bookMarkSwitchs.getBookmarkSwitchsValue(reportType)))) {
            //彻底收回播放器资源
            if (!PlayerAttriUtil.isEmpty(videoId) && playVodBean != null && !PlayerAttriUtil.isEmpty(playVodBean.getVodType())) {
                long currentSecond = 0;
                if (!isComplete) {
                    currentSecond = currentTimes / 1000;
                }

                Log.d(TAG, "bookmark episodeId:" + playVodBean.getEpisodeId() + "|currentTimes:" + currentTimes + "|totalTimes:" + totalTimes + "|tryToSeeFlag:" + tryToSeeFlag);
                if (tryToSeeFlag == 0) {
                    if (currentSecond > 0 && totalTimes - currentTimes > 5000) {
                        if (playVodBean != null && !TextUtils.isEmpty(playVodBean.getVodType()) && !playVodBean.getVodType().equals(Content.PROGRAM)) {
                            Bookmark bookMarkItem = new Bookmark();
                            bookMarkItem.setBookmarkType(playVodBean.getVodType());
                            bookMarkItem.setItemID(videoId);
                            bookMarkItem.setRangeTime(currentSecond + "");
                            // 非0的时候是电视剧
                            if (!TextUtils.isEmpty(isFilm)) {
                                if (!isFilm.equals("0")) {
                                    bookMarkItem.setSubContentID(subContentId);
                                    bookMarkItem.setSubContentType("VOD");
                                    bookMarkItem.setSitcomNO(sitcomNO);
                                }
                            }
                            // 银河的VOD场景
                            if (!TextUtils.isEmpty(playVodBean.getFatherVODId())) {
                                bookMarkItem.setItemID(playVodBean.getFatherVODId());
                                bookMarkItem.setSubContentID(videoId);
                                bookMarkItem.setSubContentType("VOD");
                                bookMarkItem.setSitcomNO(playVodBean.getFatherSitcomNO());
                            }
                            SuperLog.debug(TAG, "create bookmark--->" + "videoId:" + videoId + ",epsodeId:" + playVodBean.getEpisodeId() + ",sitcomNO:" + sitcomNO + ",currentSecond:" + currentSecond);
                            ArrayList<Bookmark> bookmarks = new ArrayList<>();
                            bookmarks.add(bookMarkItem);
                            createBookmark(bookmarks);
                        }
                    } else {
                        if (playVodBean != null && !TextUtils.isEmpty(playVodBean.getVodType()) && !playVodBean.getVodType().equals(Content.PROGRAM)) {
                            Bookmark bookMarkItem = new Bookmark();
                            bookMarkItem.setBookmarkType(playVodBean.getVodType());
                            bookMarkItem.setItemID(videoId);
                            bookMarkItem.setRangeTime("1");
                            if (!TextUtils.isEmpty(isFilm)) {
                                if (!isFilm.equals("0")) {
                                    bookMarkItem.setSubContentID(subContentId);
                                    bookMarkItem.setSubContentType("VOD");
                                    bookMarkItem.setSitcomNO(sitcomNO);
                                }
                            }
                            // 银河的VOD场景
                            if (!TextUtils.isEmpty(playVodBean.getFatherVODId())) {
                                bookMarkItem.setItemID(playVodBean.getFatherVODId());
                                bookMarkItem.setSubContentID(videoId);
                                bookMarkItem.setSubContentType("VOD");
                                bookMarkItem.setSitcomNO(playVodBean.getFatherSitcomNO());
                            }
                            SuperLog.debug(TAG, "create bookmark--->" + "videoId:" + videoId + ",epsodeId:" + playVodBean.getEpisodeId() + ",sitcomNO:" + sitcomNO + ",currentSecond:" + currentSecond);
                            ArrayList<Bookmark> bookmarks = new ArrayList<>();
                            bookmarks.add(bookMarkItem);
                            createBookmark(bookmarks);
                        }
                    }
                }
            }
        }
    }

    public void createBookmark(ArrayList<Bookmark> bookmarks) {
        CreateBookmarkRequest request = new CreateBookmarkRequest();
        List<String> str = new ArrayList<>();
        str.add("1");
        NamedParameter np = new NamedParameter("playFromTerminal", str);
        NamedParameter childNp = new NamedParameter(Constant.BOOKMARK_CHILD_MODE, str);
        List<NamedParameter> customFields = new ArrayList<>();
        customFields.add(np);
        if (VoddetailUtil.getInstance().isChildVod(mVODDetail)) {
            customFields.add(childNp);
        }
        for (Bookmark bookmark : bookmarks) {
            bookmark.setCustomFields(customFields);
        }
        request.setBookmarks(bookmarks);
        mDetailPresenter.createBookmark(request, createBookmarkCallBack);
    }

    CreateBookmarkCallBack createBookmarkCallBack = new CreateBookmarkCallBack() {
        @Override
        public void createBookmarkSuccess() {
            SuperLog.debug(TAG, "createBookmarkSuccess");
            if (playVodBean != null && tryToSeeFlag == 0) {
                playVodBean.setSitcomNO(sitcomNO);
                playVodBean.setEpisodeId(subContentId);
                if (currentTimes != 0) {
                    long currentSecond = 0;
                    if (!isComplete) {
                        currentSecond = currentTimes / 1000;
                    }
                    playVodBean.setBookmark(currentSecond + "");
                }
                SuperLog.debug(TAG, "event send Bookmark refresh");
                EventBus.getDefault().post(new BookmarkEvent(JsonParse.object2String(playVodBean)));
            }

        }

        @Override
        public void createBookmarkFail() {
            SuperLog.error(TAG, "createBookmarkFail");
        }
    };

    //点播鉴权
    public void playVODAuthorize(String vodId, String seriesId, String mediaId, String elapseTime) {
        SuperLog.error(TAG, "PlayVODAuthorize vodId" + vodId + "seriesId" + seriesId + "mediaId" + mediaId);
        PlayVODRequest playVODRequest = new PlayVODRequest();

        if (!TextUtils.isEmpty(seriesId)) {
            playVODRequest.setSeriesID(seriesId);
        }
        if (!TextUtils.isEmpty(vodId)) {
            playVODRequest.setVODID(vodId);
        }
        if (!TextUtils.isEmpty(mediaId)) {
            playVODRequest.setMediaID(mediaId);
            currentMediaId = mediaId;
        }
        playVODRequest.setURLFormat("1");

        mDetailPresenter.playVOD(playVODRequest, this, elapseTime);
    }

    //连续剧鉴权后走这里,非连续剧不走此回调
    @Override
    public void getVODPlayUrlSuccess(String url, String bookmark, String productId, String elapseTime) {
        SuperLog.debug(TAG, "playUrl is " + url);
        //如果从订购转过来销毁订购列表
        playVodBean.setBookmark(null);
        this.productId = productId;
        this.elapseTime = elapseTime;
        tryToSeeFlag = 0;
        if (isTrySeeSubscribeSucess) {
            tryseelayout.setVisibility(View.GONE);
            marketingContentContainer.setVisibility(View.GONE);
            playVodBean.setTryToSeeFlag(0);
            mPlayView.setTryToSeeFlag(0);
            if (mHandler.hasMessages(UPDATE_DURATION)) {
                mHandler.removeMessages(UPDATE_DURATION);
            }
            isTrySeeSubscribeSucess = false;
            EventBus.getDefault().post(new FinishPlayUrlEvent());
        } else {
            EventBus.getDefault().post(new FinishPlayUrlEvent());
            if (!PlayerAttriUtil.isEmpty(url)) {
                videoPath = url;
                currentEpisode = getCurrentEpisode();
                //请求时，先清除缓存
                playVodBean.setAdvertVideo(null);
                AdManager.getInstance().queryAdvertContent(AdConstant.AdClassify.VIDEO, new IADListener() {
                    @Override
                    public void onSuccess(List<AdvertContent> listAdvertContent) {
                        playVodBean.setAdvertVideo(listAdvertContent.get(0).getVideo());
                        sspAdvertRelationID = listAdvertContent.get(0).getRelationID();
                        initVideoView(videoPath);
                    }

                    @Override
                    public void onFail() {
                        initVideoView(videoPath);

                    }
                }, AdUtil.getVodInfoForEpisode(mVODDetail, currentEpisode.getVOD()));

                vodInfo.setPlayState(9);
                Log.e("geptstde", JsonParse.object2String(vodInfo));
                try {
                    if (isXiriChange) {
                        Log.e("ADS_AdvertisingBinder", "7");
                        iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 1, (int) mPlayView.getCurrentPosition() / 1000);
                    } else {
                        Log.e("ADS_AdvertisingBinder", "8");
                        iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 2, (int) mPlayView.getCurrentPosition() / 1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                EpgToast.showToast(getActivity(), getResources().getString(R.string.notice_play_url_is_empty));

            }
        }
        UBDPlay.recordSwitchEpisode(mVODDetail, subContentId, action);
    }

    public void reportVod(int action) {
        if (null != mDetailPresenter) {
            mDetailPresenter.reportVod(action, reportVodId, reportMediaId, subjectId, productId);
        }

    }

    @Override
    public void getChannelPlayUrlSuccess(String url, String attachedPlayURL, String bookmark) {

    }

    @Override
    public void playFail() {

    }

    @Override
    public void playFail(PlayVODResponse response) {
        String returnCode = response.getResult().getRetCode();
        if (!TextUtils.isEmpty(returnCode) && returnCode.equals("146021014")) {
            EpgToast.showToast(getActivity(), getResources().getString(R.string.notice_location_is_limited));
            return;
        }
        EventBus.getDefault().post(new FinishPlayUrlEvent());
        SuperLog.debug(TAG, "playFail->returnCode:" + returnCode);
        if (!TextUtils.isEmpty(returnCode) && (returnCode.equals("144020008") || returnCode.equals("504") || returnCode.equals("114020006"))) {
            String playUrl = response.getPlayURL();
            if (playVodBean != null) {
                if (!TextUtils.isEmpty(playUrl)) {
                    playVodBean.setPlayUrl(playUrl);
                    if (!TextUtils.isEmpty(sitcomNO) && !TextUtils.isEmpty(subContentId)) {
                        //电视剧
                        playVodBean.setSitcomNO(sitcomNO);
                        playVodBean.setEpisodeId(subContentId);
                    }
                    playVodBean.setTryToSeeFlag(1);// 1 试看  鉴权不通过
                    playVodBean.setAuthResult(JsonParse.object2String(response.getAuthorizeResult()));

                    vodInfo.setVid(reportVodId);
                    vodInfo.setDstVid(newReportVodId);
                    vodInfo.setVideoFromUrl(currentUrl);
                    vodInfo.setVideoUrl(playUrl);
                    vodInfo.setPlayState(9);
                    Log.e("geptstde", JsonParse.object2String(vodInfo));
                    try {
                        if (isXiriChange) {
                            Log.e("ADS_AdvertisingBinder", "7");
                            iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 1, (int) mPlayView.getCurrentPosition() / 1000);
                        } else {
                            Log.e("ADS_AdvertisingBinder", "8");
                            iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 2, (int) mPlayView.getCurrentPosition() / 1000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    currentEpisode = getCurrentEpisode();
                    //请求时，先清理缓存
                    playVodBean.setAdvertVideo(null);
                    AdManager.getInstance().queryAdvertContent(AdConstant.AdClassify.VIDEO, new IADListener() {
                        @Override
                        public void onSuccess(List<AdvertContent> listAdvertContent) {
                            playVodBean.setAdvertVideo(listAdvertContent.get(0).getVideo());
                            getIntentInfo(playVodBean);
                        }

                        @Override
                        public void onFail() {
                            getIntentInfo(playVodBean);
                        }
                    }, AdUtil.getVodInfoForEpisode(mVODDetail, currentEpisode.getVOD()));

                } else {
                    EpgToast.showToast(getActivity(), getResources().getString(R.string.notice_not_allowed_free_trial_forNow));
                }
                UBDPlay.recordSwitchEpisode(mVODDetail, subContentId, UBDConstant.ACTION.PLAY_AUTO);
            } else {
                AuthorizeResult authorizeResult = response.getAuthorizeResult();
                if (null != authorizeResult) {
                    List<Product> products = authorizeResult.getPricedProducts();
                    if (SessionService.getInstance().getSession().isHotelUser()) {
                        if (products == null || products.size() == 0) {
                            EpgToast.showToast(getActivity(), R.string.notice_no_orderable_product);
                            return;
                        }
                        for (int i = products.size() - 1; i >= 0; i--) {
                            Product mProductInfo = products.get(i);
                            if (!StringUtils.isSubscribeByDay(mProductInfo)) {
                                products.remove(i);
                            }

                        }
                        if (products == null || products.size() == 0) {
                            EpgToast.showToast(getActivity(), R.string.notice_no_orderable_product);
                            return;
                        }
                        authorizeResult.setPricedProducts(products);

                    } else {
                        if (products == null || products.size() == 0) {
                            EpgToast.showToast(getActivity(), R.string.notice_no_orderable_product);
                            return;
                        }
                    }

                } else {
                    EpgToast.showToast(getActivity(), R.string.notice_no_orderable_product);
                    return;
                }

                String needJumpToH5Order = SessionService.getInstance().getSession().getTerminalConfigurationNeedJumpToH5Order();
                if (null != needJumpToH5Order && needJumpToH5Order.equals("1")) {
                    JumpToH5OrderUtils.getInstance().jumpToH5OrderFromTrySee(authorizeResult.getPricedProducts(), getActivity(), false, false, null, mVODDetail);
                    //pbs点击上报
                    Log.i(TAG, "PbsUaService: "+mVODDetail.getID());
                    PbsUaService.report(Play.getPurchaseData(mVODDetail.getID()));
                } else {
                    String authResult = JsonParse.object2String(authorizeResult);
                    Intent intent = new Intent(getActivity(), NewProductOrderActivity.class);
                    intent.putExtra(NewProductOrderActivity.AUTHORIZE_RESULT, authResult);
                    intent.putExtra(ZjYdUniAndPhonePayActivty.IS_TRY_SEE_SUBSCRIBE, true);
                    intent.putExtra(ZjYdUniAndPhonePayActivty.VOD_DETAIL, VodUtil.getSimpleVoddetail(mVODDetail, 5 * 60));
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
            }
        } else if (!TextUtils.isEmpty(returnCode) && returnCode.equals("144020000")) {
            EpgToast.showToast(getActivity(), getResources().getString(R.string.notice_no_orderable_product));
            switchAutherateFail();
        } else {
            EpgToast.showToast(getActivity(), getResources().getString(R.string.notice_media_auth_failed));
            switchAutherateFail();
        }

    }

    @Override
    public void onPlaycancel() {

    }

    @Override
    public void getVODDownloadUrlSuccess(String vodID, String url, String postURL, String switchNum, String name) {

    }

    @Override
    public void getVODDownloadUrlFailed(String vodID, String episodeID) {

    }


    public void switchAutherateFail() {
        if (null != playVodBean && !PlayerAttriUtil.isEmpty(playVodBean.getVodName())) {
            vodTitletv.setText(playVodBean.getVodName());
            if ((!TextUtils.isEmpty(sitcomNO) && !TextUtils.isEmpty(subContentId))) {
                vodTitletv.setText(playVodBean.getVodName() + "第" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "集" : "期"));
                tryToSeeFlag = 1;
            }
            vodTitletv.setSelected(true);
        }
    }

    //语音播放
    @Override
    public void play() {
        //语音命令问题修改，如果当前播放广告，并且不在可跳过时间，则不执行操作。
        if (null != advert_hint && !isAdPlayEnd && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        if (null == mPlayView) {
            return;
        }
        if (isCanReplayByTrySee()) {
            if (tryToSeeFlag == 1) {
                mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
            }
            if (mPlayView.isPlaying()) {
                return;
            }
            mPlayView.playerOrPause();
            mPlayView.showControlView(true);
            if (mPlayBackView != null && mPlayBackView.isShowing()) {
                mPlayBackView.dismiss();
            }
        }
        vodInfo.setPlayState(4);
        try {
            Log.e("ADS_AdvertisingBinder", "9");
            iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 1, (int) mPlayView.getCurrentPosition() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //语音暂停
    @Override
    public void pause() {
        //语音命令问题修改，如果当前播放广告，并且不在可跳过时间，则不执行操作。
        if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        if (null != mPlayView) {
            if (!mPlayView.isPlaying()) {
                return;
            }
            if (!mPlayBackView.isShowing() && mPlayView.isPlaying() && mPlayView.getDuration() > 0) {
                mPlayBackView.showPlayBack(getView(), false, null);
            }
            mPlayView.playerOrPause();
            mPlayView.showControlView(true);
        }
        vodInfo.setPlayState(3);
        try {
            Log.e("ADS_AdvertisingBinder", "10");
            iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 1, (int) mPlayView.getCurrentPosition() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //语音快进多少秒
    @Override
    public void forWard(long time) {
        //语音命令跳过问题修改，如果当前播放广告，并且不在可跳过时间，则不执行操作。
        if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        vodInfo.setPlayState(5);
        try {
            Log.e("ADS_AdvertisingBinder", "11");
            iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 1, (int) mPlayView.getCurrentPosition() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mPlayBackView != null && mPlayBackView.isShowing()) {
            mPlayBackView.dismiss();
        }

        if (tryToSeeFlag == 1) {
            mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
            // JIRA问题单【ZJYDEPGAPP-801】 DELETE START
//            if (isCanReplayByTrySee()) {
//                time = mPlayView.getCurrentPosition() / 1000 + time;
//                if (time > tryToSeeTime) {
//                    time = tryToSeeTime - mPlayView.getCurrentPosition() / 1000 - 3;
//                }
//            }
            // JIRA问题单【ZJYDEPGAPP-801】 DELETE END
        }
        mPlayView.showControlView(true);
        mPlayView.fastForward(time * 1000);
        if (!mPlayView.isPlaying() && mCurrentPlayState == IPlayState.PLAY_STATE_HASMEDIA && isCanReplayByTrySee()) {
            mPlayView.resumePlay();
        }
        rightTime = 0;
        vodInfo.setPlayState(6);
        try {
            Log.e("ADS_AdvertisingBinder", "12");
            iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 1, (int) mPlayView.getCurrentPosition() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void keyForWard(long time) {
        if (mPlayBackView != null && mPlayBackView.isShowing()) {
            mPlayBackView.dismiss();
        }

        if (tryToSeeFlag == 1) {
            mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
            // JIRA问题单【ZJYDEPGAPP-801】 DELETE START
//            if (isCanReplayByTrySee()) {
//                time = mPlayView.getCurrentPosition() / 1000 + time;
//                if (time > tryToSeeTime) {
//                    time = tryToSeeTime - mPlayView.getCurrentPosition() / 1000 - 3;
//                }
//            }
            // JIRA问题单【ZJYDEPGAPP-801】 DELETE END
        }
        mPlayView.showControlView(true);
        mPlayView.fastForward(time * 1000);
        if (!mPlayView.isPlaying() && mCurrentPlayState == IPlayState.PLAY_STATE_HASMEDIA && isCanReplayByTrySee()) {
            mPlayView.resumePlay();
        }
        rightTime = 0;
    }

    //语音后退多少秒
    @Override
    public void backForward(long time) {
        //语音命令快退问题修改，如果当前播放广告，并且不在可跳过时间，则不执行操作。
        if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        vodInfo.setPlayState(7);
        try {
            Log.e("ADS_AdvertisingBinder", "13");
            iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 1, (int) mPlayView.getCurrentPosition() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mPlayBackView != null && mPlayBackView.isShowing()) {
            mPlayBackView.dismiss();
        }
        if (tryToSeeFlag == 1) {
            mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
        }
        mPlayView.showControlView(true);
        mPlayView.rewind(time * 1000);
        if (!mPlayView.isPlaying() && mCurrentPlayState == IPlayState.PLAY_STATE_HASMEDIA && isCanReplayByTrySee()) {
            mPlayView.resumePlay();
        }
        leftTime = 0;
        vodInfo.setPlayState(8);
        try {
            Log.e("ADS_AdvertisingBinder", "14");
            iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 1, (int) mPlayView.getCurrentPosition() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void keyBackForward(long time) {
        if (mPlayBackView != null && mPlayBackView.isShowing()) {
            mPlayBackView.dismiss();
        }
        if (tryToSeeFlag == 1) {
            mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
        }
        mPlayView.showControlView(true);
        mPlayView.rewind(time * 1000);
        if (!mPlayView.isPlaying() && mCurrentPlayState == IPlayState.PLAY_STATE_HASMEDIA && isCanReplayByTrySee()) {
            mPlayView.resumePlay();
        }
        leftTime = 0;
    }

    //语音结束播放
    @Override
    public void finish() {
        //语音命令问题修改，如果当前播放广告，并且不在可跳过时间，则不执行操作。
        if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        if (mPlayBackView != null) {
            mPlayBackView.setOnKeyBackDismissListener(null);
        }
        vodInfo.setPlayState(2);
        try {
            Log.e("ADS_AdvertisingBinder", "15");
            iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 1, (int) mPlayView.getCurrentPosition() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getActivity().finish();
    }
    //语音播放下一集

    @Override
    public void nextPlay() {
        //语音命令问题修改，如果当前播放广告，并且不在可跳过时间，则不执行操作。
        if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        SuperLog.debug(TAG, "OnDemand nextPlay" + sitcomNO);
        if (mPlayBackView != null) {
            if (mPlayBackView.isShowing()) {
                mPlayBackView.dismiss();
            }
        }
        if (episodeDetailList != null && episodeDetailList.size() != 0) {
            if (!TextUtils.isEmpty(sitcomNO)) {
                int sitNum = Integer.parseInt(sitcomNO);

                //电视剧取最后一集,综艺取第一集
                String lastNumStr = playVodBean.getIsReverse() == 0 ? episodeDetailList.get(episodeDetailList.size() - 1).getSitcomNO() : episodeDetailList.get(0).getSitcomNO();
                int lastNum = -1;
                if (!TextUtils.isEmpty(lastNumStr)) {
                    lastNum = Integer.parseInt(lastNumStr);
                }

                //电视剧取第一集,综艺取最后一集
                String firstNumStr = playVodBean.getIsReverse() == 0 ? episodeDetailList.get(0).getSitcomNO() : episodeDetailList.get(episodeDetailList.size() - 1).getSitcomNO();
                int firstNum = -1;
                if (!TextUtils.isEmpty(firstNumStr)) {
                    firstNum = Integer.parseInt(firstNumStr);
                }
                if (lastNum != -1) {
                    Log.e(TAG, "playVodBean.getIsReverse() is " + playVodBean.getIsReverse());
                    Log.e(TAG, "sitNum is " + sitNum);
                    Log.e(TAG, "firstNum is " + firstNum);
                    Log.e(TAG, "lastNum is " + lastNum);
                    Log.e(TAG, "playVodBean.getIsReverse() is " + playVodBean.getIsReverse());
                    if ((playVodBean.getIsReverse() == 0 && sitNum < lastNum) || (playVodBean.getIsReverse() == 1 && sitNum > firstNum)) {
                        for (int i = 0; i < episodeDetailList.size(); i++) {
                            Log.e(TAG, "episodeDetailList.get(" + i + ").getSitcomNO() is " + episodeDetailList.get(i).getSitcomNO());
                            Log.e(TAG, "lastNum is " + lastNum);
                            if (episodeDetailList.get(i).getSitcomNO().equals(sitcomNO)) {
                                isXiriChange = true;
                                Message message = Message.obtain();
                                message.what = EPISODE_PLAY;
                                Bundle bundle = new Bundle();
//                                Episode episode = playVodBean.getIsReverse() == 0 ? episodeDetailList.get(i + 1) : episodeDetailList.get(i - 1);
                                Episode episode = episodeDetailList.get(i + 1);
                                bundle.putString("SitcomNO", episode.getSitcomNO());
                                bundle.putString("EpisodesId", episode.getVOD().getID());
                                bundle.putString("MediaId", episode.getVOD().getMediaFiles().get(0).getID());
                                bundle.putSerializable("episodeVod", episode.getVOD());
                                epsiodeCustomFields = episode.getVOD().getCustomFields();
                                vodTime = VodDetailCacheService.getVodTime(episode.getVOD().getMediaFiles().get(0));
                                message.setData(bundle);
                                mHandler.sendMessage(message);
                            }
                        }
                    } else {
                        EpgToast.showToast(getActivity(), "已经是最后一集了！");
                    }
                }
            }
        }
    }

    //语音播放上一集
    @Override
    public void prevPlay() {
        //语音命令问题修改，如果当前播放广告，并且不在可跳过时间，则不执行操作。
        if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        SuperLog.debug("lx", "OnDemand prevPlay" + sitcomNO);
        if (mPlayBackView != null) {
            if (mPlayBackView.isShowing()) {
                mPlayBackView.dismiss();
            }
        }
        if (episodeDetailList != null && episodeDetailList.size() != 0) {
            if (!TextUtils.isEmpty(sitcomNO)) {
                int sitNum = Integer.parseInt(sitcomNO);

                //电视剧取最后一集,综艺取第一集
                String lastNumStr = playVodBean.getIsReverse() == 0 ? episodeDetailList.get(episodeDetailList.size() - 1).getSitcomNO() : episodeDetailList.get(0).getSitcomNO();
                int lastNum = -1;
                if (!TextUtils.isEmpty(lastNumStr)) {
                    lastNum = Integer.parseInt(lastNumStr);
                }

                //电视剧取第一集,综艺取最后一集
                String firstNumStr = playVodBean.getIsReverse() == 0 ? episodeDetailList.get(0).getSitcomNO() : episodeDetailList.get(episodeDetailList.size() - 1).getSitcomNO();
                int firstNum = -1;
                if (!TextUtils.isEmpty(firstNumStr)) {
                    firstNum = Integer.parseInt(firstNumStr);
                }
                if (firstNum != -1) {
                    Log.e(TAG, "playVodBean.getIsReverse() is " + playVodBean.getIsReverse());
                    Log.e(TAG, "sitNum is " + sitNum);
                    Log.e(TAG, "firstNum is " + firstNum);
                    Log.e(TAG, "lastNum is " + lastNum);
                    Log.e(TAG, "playVodBean.getIsReverse() is " + playVodBean.getIsReverse());
                    if ((playVodBean.getIsReverse() == 0 && sitNum > firstNum) || (playVodBean.getIsReverse() == 1 && sitNum < lastNum)) {
                        for (int i = 0; i < episodeDetailList.size(); i++) {

                            Log.e(TAG, "episodeDetailList.get(" + i + ").getSitcomNO() is " + episodeDetailList.get(i).getSitcomNO());
                            Log.e(TAG, "lastNum is " + lastNum);
                            if (episodeDetailList.get(i).getSitcomNO().equals(sitcomNO)) {
                                isXiriChange = true;
                                Message message = Message.obtain();
                                message.what = EPISODE_PLAY;
                                Bundle bundle = new Bundle();
//                                Episode episode = playVodBean.getIsReverse() == 0 ? episodeDetailList.get(i - 1) : episodeDetailList.get(i + 1);
                                Episode episode = episodeDetailList.get(i - 1);
                                bundle.putString("SitcomNO", episode.getSitcomNO());
                                bundle.putString("EpisodesId", episode.getVOD().getID());
                                bundle.putString("MediaId", episode.getVOD().getMediaFiles().get(0).getID());
                                bundle.putSerializable("episodeVod", episode.getVOD());
                                epsiodeCustomFields = episode.getVOD().getCustomFields();
                                vodTime = VodDetailCacheService.getVodTime(episode.getVOD().getMediaFiles().get(0));
                                message.setData(bundle);
                                mHandler.sendMessage(message);
                            }
                        }
                    } else {
                        EpgToast.showToast(getActivity(), "已经是第一集了！");
                    }
                }
            }
        }

    }

    //语音播放指定集数
    @Override
    public void indexPlay(int index) {
        //语音命令问题修改，如果当前播放广告，并且不在可跳过时间，则不执行操作。
        if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        if (mPlayBackView != null && mPlayBackView.isShowing()) {
            mPlayBackView.dismiss();
        }
        String sitNum = String.valueOf(index);
        if (episodeDetailList != null && episodeDetailList.size() > 0) {
            for (int i = 0; i < episodeDetailList.size(); i++) {
                if (episodeDetailList.get(i).getSitcomNO().equals(sitNum)) {
                    isXiriChange = true;
                    Message message = Message.obtain();
                    message.what = BrowseTVPlayFragment.EPISODE_PLAY;
                    Bundle bundle = new Bundle();
                    bundle.putString("SitcomNO", episodeDetailList.get(i).getSitcomNO());
                    bundle.putString("EpisodesId", episodeDetailList.get(i).getVOD().getID());
                    bundle.putString("MediaId", episodeDetailList.get(i).getVOD().getMediaFiles().get(0).getID());
                    bundle.putSerializable("episodeVod", episodeDetailList.get(i).getVOD());
                    epsiodeCustomFields = episodeDetailList.get(i).getVOD().getCustomFields();
                    vodTime = VodDetailCacheService.getVodTime(episodeDetailList.get(i).getVOD().getMediaFiles().get(0));
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                    return;
                }
            }
        }
        EpgToast.showToast(getActivity(), "集数不存在！");
    }

    //语音播放指定时间
    @Override
    public void seekTo(int position) {
        //语音命令问题修改，如果当前播放广告，并且不在可跳过时间，则不执行操作。
        if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        boolean isForward = mPlayView.getCurrentPosition() < position * 1000;
        if (isForward) {
            vodInfo.setPlayState(5);
        } else {
            vodInfo.setPlayState(7);
        }
        try {
            Log.e("ADS_AdvertisingBinder", "213 " + vodInfo.getPlayState());
            iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 1, (int) mPlayView.getCurrentPosition() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mPlayBackView != null && mPlayBackView.isShowing()) {
            mPlayBackView.dismiss();
        }
        if (tryToSeeFlag == 1) {
            mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
        }
        mPlayView.showControlView(true);
        mPlayView.seekTo(position * 1000);
        if (!mPlayView.isPlaying() && mCurrentPlayState == IPlayState.PLAY_STATE_HASMEDIA && isCanReplayByTrySee()) {
            mPlayView.resumePlay();
        }

        if (isForward) {
            vodInfo.setPlayState(6);
        } else {
            vodInfo.setPlayState(8);
        }
        try {
            Log.e("ADS_AdvertisingBinder", "214 " + vodInfo.getPlayState());
            iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 1, (int) mPlayView.getCurrentPosition() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //语音重新播放
    @Override
    public void rePlay() {
        //语音命令问题修改，如果当前播放广告，并且不在可跳过时间，则不执行操作。
        if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        if (mPlayBackView != null && mPlayBackView.isShowing()) {
            mPlayBackView.dismiss();
        }
        mPlayView.rePlay();
        mPlayView.showControlView(true);
    }

    @Override
    public void doSkipHistory() {
        getActivity().finish();
    }

    @Override
    public void playLastEpisode() {
        //语音命令问题修改，如果当前播放广告，并且不在可跳过时间，则不执行操作。
        //语音命令问题修改，如果当前播放广告，并且不在可跳过时间，则不执行操作。
        if (null != advert_hint && !isAdPlayEnd && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        SuperLog.debug(TAG, "OnDemand nextPlay" + sitcomNO);
        if (mPlayBackView != null) {
            if (mPlayBackView.isShowing()) {
                mPlayBackView.dismiss();
            }
        }
        if (episodeDetailList != null && episodeDetailList.size() != 0) {
            if (!TextUtils.isEmpty(sitcomNO)) {
                int sitNum = Integer.parseInt(sitcomNO);

                //电视剧取最后一集,综艺取第一集
                String lastNumStr = playVodBean.getIsReverse() == 0 ? episodeDetailList.get(episodeDetailList.size() - 1).getSitcomNO() : episodeDetailList.get(0).getSitcomNO();
                int lastNum = -1;
                if (!TextUtils.isEmpty(lastNumStr)) {
                    lastNum = Integer.parseInt(lastNumStr);
                }

                //电视剧取第一集,综艺取最后一集
                String firstNumStr = playVodBean.getIsReverse() == 0 ? episodeDetailList.get(0).getSitcomNO() : episodeDetailList.get(episodeDetailList.size() - 1).getSitcomNO();
                int firstNum = -1;
                if (!TextUtils.isEmpty(firstNumStr)) {
                    firstNum = Integer.parseInt(firstNumStr);
                }
                if (lastNum != -1) {
                    Log.e(TAG, "playVodBean.getIsReverse() is " + playVodBean.getIsReverse());
                    Log.e(TAG, "sitNum is " + sitNum);
                    Log.e(TAG, "firstNum is " + firstNum);
                    Log.e(TAG, "lastNum is " + lastNum);
                    Log.e(TAG, "playVodBean.getIsReverse() is " + playVodBean.getIsReverse());
                    if ((playVodBean.getIsReverse() == 0 && sitNum < lastNum) || (playVodBean.getIsReverse() == 1 && sitNum > firstNum)) {
//                        for (int i = 0; i < episodeDetailList.size(); i++) {
//                            Log.e(TAG, "episodeDetailList.get(" + i + ").getSitcomNO() is " + episodeDetailList.get(i).getSitcomNO());
//                            Log.e(TAG, "lastNum is " + lastNum);
//                            if (episodeDetailList.get(i).getSitcomNO().equals(sitcomNO)) {
                        isXiriChange = true;
                        Message message = Message.obtain();
                        message.what = EPISODE_PLAY;
                        Bundle bundle = new Bundle();
//                                Episode episode = playVodBean.getIsReverse() == 0 ? episodeDetailList.get(i + 1) : episodeDetailList.get(i - 1);
                        Episode episode = episodeDetailList.get(lastNum - 1);
                        bundle.putString("SitcomNO", episode.getSitcomNO());
                        bundle.putString("EpisodesId", episode.getVOD().getID());
                        bundle.putString("MediaId", episode.getVOD().getMediaFiles().get(0).getID());
                        bundle.putSerializable("episodeVod", episode.getVOD());
                        epsiodeCustomFields = episode.getVOD().getCustomFields();
                        vodTime = VodDetailCacheService.getVodTime(episode.getVOD().getMediaFiles().get(0));
                        message.setData(bundle);
                        mHandler.sendMessage(message);
//                            }
//                        }
                    } else {
                        EpgToast.showToast(getActivity(), "已经是最后一集了！");
                    }
                }
            }
        }
    }

    //退出播放
    public void backPlay() {
        SuperLog.debug(TAG, "BACK_PLAY");
        if (isNeedBackVoddetail()) {
            switchVoddetail();
        } else {
            getActivity().finish();
        }
        vodInfo.setPlayState(2);
        try {
            Log.e("ADS_AdvertisingBinder", "16");
            iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 2, (int) mPlayView.getCurrentPosition() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //播放子集
    public void episondePlay(Message msg) {
        String mediaId1 = "";
        String episodesId = "";
        Bundle episodeBundle = msg.getData();
        sitcomNO = episodeBundle.getString("SitcomNO");
        episodesId = episodeBundle.getString("EpisodesId");
        subContentId = episodesId;
        mediaId1 = episodeBundle.getString("MediaId");
        newReportVodId = episodesId;
        newReportMediaId = mediaId1;
        String elapseTime = episodeBundle.getString("elapseTime");
        if (null != episodeBundle.getSerializable("episodeVod")) {
            VOD vod = (VOD) episodeBundle.getSerializable("episodeVod");
            contentCode = vod.getCode();
            if (null != vod.getCustomFields()) {
                epsiodeCustomFields = vod.getCustomFields();
            }
            if (null != vod.getMediaFiles() && vod.getMediaFiles().size() > 0) {
                vodTime = VodDetailCacheService.getVodTime(vod.getMediaFiles().get(0));
            } else {
                vodTime = VodDetailCacheService.getVodTime(null);
            }
        }

        if (!PlayerAttriUtil.isEmpty(videoId) && !PlayerAttriUtil.isEmpty(episodesId) && !PlayerAttriUtil.isEmpty(mediaId1)) {
            action = UBDConstant.ACTION.PLAY_NORMAL;
            playVODAuthorize(episodesId, videoId, mediaId1, elapseTime);
        } else {
            EpgToast.showToast(getActivity(), "播放失败");
        }
    }

    public Episode getCurrentEpisode() {
        if (!PlayerAttriUtil.isEmpty(sitcomNO) && null != episodeDetailList) {
            for (int i = 0; i < episodeDetailList.size(); i++) {
                if (sitcomNO.equals(episodeDetailList.get(i).getSitcomNO())) {
                    return episodeDetailList.get(i);
                }
            }
        }
        return null;
    }

    //自动下集播放
    public void handlerNextPlay() {
        //播放下一级
        SuperLog.error(TAG, "NEXT_PLAY");
        int nextPlayPosition = 0;
        //电视剧剧集
        if (episodeDetailList != null && episodeDetailList.size() > 0) {
            if (!PlayerAttriUtil.isEmpty(sitcomNO)) {
                for (int i = 0; i < episodeDetailList.size(); i++) {
                    if (sitcomNO.equals(episodeDetailList.get(i).getSitcomNO())) {
                        nextPlayPosition = i;
                    }
                }
                if (nextPlayPosition == episodeDetailList.size() - 1) {
                    //最后一级，关闭播放器
                    isComplete = true;
                    mHandler.sendEmptyMessage(BACK_PLAY);
                } else {
                    //下一集
                    nextPlayPosition = nextPlayPosition + 1;
                    sitcomNO = episodeDetailList.get(nextPlayPosition).getSitcomNO();
                    subContentId = episodeDetailList.get(nextPlayPosition).getVOD().getID();
                    newReportVodId = subContentId;
                    newReportMediaId = episodeDetailList.get(nextPlayPosition).getVOD().getMediaFiles().get(0).getID();
                    currentMediaId = episodeDetailList.get(nextPlayPosition).getVOD().getMediaFiles().get(0).getID();
                    contentCode = episodeDetailList.get(nextPlayPosition).getVOD().getCode();
                    epsiodeCustomFields = episodeDetailList.get(nextPlayPosition).getVOD().getCustomFields();
                    vodTime = VodDetailCacheService.getVodTime(episodeDetailList.get(nextPlayPosition).getVOD().getMediaFiles().get(0));
                    if (!PlayerAttriUtil.isEmpty(videoId)) {
                        action = UBDConstant.ACTION.PLAY_AUTO;
                        playVODAuthorize(episodeDetailList.get(nextPlayPosition).getVOD().getID(), videoId, episodeDetailList.get(nextPlayPosition).getVOD().getMediaFiles().get(0).getID(), episodeDetailList.get(nextPlayPosition).getVOD().getMediaFiles().get(0).getElapseTime());
                    }
                }
            } else {
                mHandler.sendEmptyMessage(BACK_PLAY);
            }
        } else {
            mHandler.sendEmptyMessage(BACK_PLAY);
        }

    }

    @Override
    public void subscribeAuthoritize() {
        if (null != mOffScreenUtils) {
            if (null != mOffScreenUtils.getVodDetail())
                mDetailPresenter.playVOD(mOffScreenUtils.getVodDetail(), this);
        }
    }

    @Override
    public void getVODDetailSuccess(VODDetail vodDetail, String recmActionID, List<RecmContents> recmContents) {
        if (null != vodDetail) {
            mDetailPresenter.setCollection(vodDetail, vodDetail.getFavorite() != null);
        }
    }

    @Override
    public void getVODDetailFailed() {

    }

    @Override
    public void onError() {

    }

    //试看是否可以继续
    public boolean isCanReplayByTrySee() {
        SuperLog.debug(TAG, "isCanReplayByTrySee->tryToSeeFlag:" + tryToSeeFlag + "|tryToSeeTime:" + tryToSeeTime * 1000 + "|CurrentPosition:" + mPlayView.getCurrentPosition());
        return tryToSeeFlag != 1 || tryToSeeTime * 1000 > mPlayView.getCurrentPosition();
    }


    public boolean isFromXmpp() {
        if (!PlayerAttriUtil.isEmpty(playVodBean.getXmppFrom())) {
            SuperLog.debug(TAG, "onActivityCreated---->!PlayerAttriUtil.isEmpty(playVodBean.getXmppFrom())");
            if (null != xmppmessage && !TextUtils.isEmpty(xmppmessage.getActionSource())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 甩屏不提示
     *
     * @param event
     */
    @Subscribe
    public void xmppSuccess(XmppSuccessEvent event) {
        setIsXmpp(true);
    }


    /**
     * 甩屏时，发送播放状态给甩屏服务器
     *
     * @return true 正在播放
     */
    public boolean isVodPlayNow() {

        return null != mPlayView && mPlayView.isPlaying();

    }

    /**
     * 甩屏时，发送播放状态给甩屏服务器
     *
     * @return
     */
    public boolean isVodPause() {
        return null != mPlayView && mPlayView.isPause();
    }

    /**
     * 甩屏时，发送快进快退速率给服务器
     *
     * @return
     */
    public int getforwardAndBackRate() {
        if (!isVodPlayNow() && !isVodPause()) {
            forwardAndBackRate = 0;
        } else if ((isVodPlayNow() || isVodPause()) && forwardAndBackRate == 0) {
            forwardAndBackRate = 1;
        }
        return forwardAndBackRate;

    }

    /**
     * 甩屏时，获得当前播放位置(单位：秒）
     *
     * @return
     */
    public long getVodCurrentPosition() {
        return null == mPlayView ? 0 : (mPlayView.getCurrentPosition() / 1000);

    }

    public long getVodDuration() {
        return null == mPlayView ? 0 : (mPlayView.getDuration() / 1000);
    }

    /**
     * 甩屏， 0为播放，1为暂停
     *
     * @param state
     */
    public void playerOrPause(int state) {
        if (state == 1) {
            reportBookmark(BookMarkSwitchs.PAUSE);
            pause();
        } else {
            reportBookmark(BookMarkSwitchs.REPLAY);
            play();
        }
    }

    @Override
    public void initVideoTrailMarketingContent(String trailVideoMarketingImageURL) {
//        GlideApp.with(this.getActivity()).load(trailVideoMarketingImageURL).into(marketingContent);
        Context context = this.getActivity();
        RequestOptions options = new RequestOptions()
                .transform(new CornersTransform(8));

        Glide.with(context).load(trailVideoMarketingImageURL)
                .apply(options)
                .into(marketingContent);
    }

    public String getContentCode() {
        return contentCode;
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    private void showBackToStartToast() {
        isNeedBackToStart = true;
        mPlayView.showBackToStartToast(R.string.player_back_to_start);
    }

    private void hideBackToStartToast() {
        isNeedBackToStart = false;
        mPlayView.hideBackToStartToast();
    }

    private float switchSpeed() {
        if (speedList.size() > speed) {
            return speedList.get(speed);
        }
        return 1f;
    }

    @Subscribe
    public void onEvent(PlayerSpeedChangeEvent event) {
        this.speed = event.getSpeed();
        List<Float> speedList = event.getSpeedList();
        if (mPlayView != null) {
            mPlayView.setSpeed(speedList.get(speed));
        }
    }

    @Subscribe
    public void onEvent(PlayerSkipChangeEvent event) {
        if (mPlayView != null) {
            mPlayView.setSkipStart(SharedPreferenceUtil.getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "", true));
        }
    }


    /**
     * 甩屏，请求书签结束后，跳转详情页
     */
    public void switchVoddetail() {
        Bookmark bookMarkItem = null;
        if (tryToSeeFlag == 0 && !PlayerAttriUtil.isEmpty(videoId) && playVodBean != null && !PlayerAttriUtil.isEmpty(playVodBean.getVodType()) && !playVodBean.getVodType().equals(Content.PROGRAM)) {
            long currentSecond = 0;
            if (!isComplete) {
                currentSecond = currentTimes / 1000;
            }
            bookMarkItem = new Bookmark();
            bookMarkItem.setBookmarkType(playVodBean.getVodType());
            bookMarkItem.setItemID(videoId);
            if (currentSecond > 0 && totalTimes - currentTimes > 5000) {
                bookMarkItem.setRangeTime(currentSecond + "");
            } else {
                bookMarkItem.setRangeTime("1");
            }
            // 非0的时候是电视剧
            if (!TextUtils.isEmpty(isFilm)) {
                if (!isFilm.equals("0")) {
                    bookMarkItem.setSubContentID(subContentId);
                    bookMarkItem.setSubContentType("VOD");
                    bookMarkItem.setSitcomNO(sitcomNO);
                }
            }
            // 银河的VOD场景
            if (!TextUtils.isEmpty(playVodBean.getFatherVODId())) {
                bookMarkItem.setItemID(playVodBean.getFatherVODId());
                bookMarkItem.setSubContentID(videoId);
                bookMarkItem.setSubContentType("VOD");
                bookMarkItem.setSitcomNO(playVodBean.getFatherSitcomNO());
            }

        }
        String vodid = videoId;
        if (playVodBean != null && !TextUtils.isEmpty(playVodBean.getFatherVODId())) {
            vodid = playVodBean.getFatherVODId();
        }
        Intent intent = new Intent(getActivity(), NewVodDetailActivity.class);
        intent.putExtra(NewVodDetailActivity.VOD_ID, vodid);
        if (null != bookMarkItem) {
            intent.putExtra(NewVodDetailActivity.OLD_BOOKMARK, bookMarkItem);
        }
        getActivity().startActivity(intent);
        getActivity().finish();
    }


    public boolean isNeedBackVoddetail() {
        return null != xmppmessage && "1".equals(isFilm);
    }

    private void setRecordText(boolean isStart) {

        if (isNeedToSkip) {
            boolean isSkip = SharedPreferenceUtil.getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "", true);
            //是否展示提示
            boolean showTip = false;
            /*跳过片头片尾开关
             * 0 为打开
             * 1 为关闭
             * 2 或者 不配置 或者 2 以外的值 为由用户自行设置
             */
            String skipSwitch = SessionService.getInstance().getSession().getTerminalConfigurationVOD_SKIP_SWITCH();
            if (null != skipSwitch && skipSwitch.equals("0")) {
                isSkip = true;
            } else if (null != skipSwitch && skipSwitch.equals("1")) {
                isSkip = false;
            } else {
                showTip = true;
            }

            if (isNeedShowSkipBottomRecord) {
                if (isSkip) {
                    if (isStart) {
                        mPlayView.getmRecord().setText(R.string.player_skip_start);
                    } else {
                        mPlayView.getmRecord().setText(R.string.player_skip_end);
                    }
                } else {
                    if (showTip) {
                        if (isStart) {
                            mPlayView.getmRecord().setText(R.string.player_not_skip_start);
                        } else {
                            mPlayView.getmRecord().setText(R.string.player_not_skip_end);
                        }
                    } else {
                        mPlayView.getmRecord().setText("");
                    }
                }
            } else {
                mPlayView.getmRecord().setText("");
            }
            if (null != playVodBean && !TextUtils.isEmpty(playVodBean.getVodType()) && TextUtils.equals(playVodBean.getVodType(), Content.PROGRAM)) {
                mPlayView.getmSettingRecord().setText("");
            } else {
                if (mPlayFrom == 1){
                    mPlayView.getmSettingRecord().setText("");
                    mPlayView.getmSettingRecord().setVisibility(View.GONE);
                }else{
                    if (canSetSpeed || showTip) {
                        //跳过片头片尾设置和播放速度设置都没有的时候不展示右上角提示
                        mPlayView.getmSettingRecord().setText(R.string.player_setting_record);
                    } else {
                        mPlayView.getmSettingRecord().setText("");
                    }
                }
            }
            mPlayView.getmRecord().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.T24_C23_Light_size));
        } else {
            if (null != playVodBean && !TextUtils.isEmpty(playVodBean.getVodType()) && TextUtils.equals(playVodBean.getVodType(), Content.PROGRAM)) {
                mPlayView.getmSettingRecord().setText("");
            } else {
                if (mPlayFrom == 1){
                    mPlayView.getmSettingRecord().setText("");
                    mPlayView.getmSettingRecord().setVisibility(View.GONE);
                }else{
                    if (!canSetSpeed) {
                        //播放速度设置没有的时候不展示右上角提示
                        mPlayView.getmSettingRecord().setText("");
                    } else {
                        mPlayView.getmSettingRecord().setText(R.string.player_setting_record);
                    }
                }
            }
            if (playVodBean != null) {
                if (!PlayerAttriUtil.isEmpty(playVodBean.getVodName())) {
                    mPlayView.getmRecord().setText(playVodBean.getVodName());
                    if (!TextUtils.isEmpty(sitcomNO)) {
                        mPlayView.getmRecord().setText(playVodBean.getVodName() + "第" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "集" : "期"));
                    }
                } else {
                    SuperLog.debug(TAG, "playVodBean为空");
                }
            }
            mPlayView.getmRecord().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.T29_C21_Bold_size));
            vodTitletv.setVisibility(View.GONE);
        }
//        mPlayView.getmSettingRecord().setCompoundDrawables(Drawables.getInstance().getDrawable(getResources(), R.drawable.info_96), null, null, null);
    }

    public void onXmppBack() {
        backPlay();
    }

    private IAdvertisingService iAdvertisingService;

    private IBinder iAdvertisingBinder;

    private Timer timer;
    private TimerTask timerTask;
    private VODInfo vodInfo = new VODInfo();
    private IVideoControl iVideoControl = new Stub() {
        @Override
        public void onVideoPause() throws RemoteException {
            Log.e("ADSAdvertisingZJ", "onVideoPause");
            mHandler.sendEmptyMessage(AIDL_ON_VIDEO_PAUSE);
        }

        @Override
        public void onVideoReplay() throws RemoteException {
            Log.e("ADSAdvertisingZJ", "onVideoReplay");
            mHandler.sendEmptyMessage(AIDL_ON_VIDEO_REPLAY);
        }

        @Override
        public IBinder asBinder() {
            return this;
        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iAdvertisingService = IAdvertisingService.Stub.asInterface(service);
            iAdvertisingBinder = service;
            try {
                Log.e("ADSAdvertisingZJ", "setVideoControl is " + (iVideoControl == null));
                iAdvertisingService.setVideoControl(iVideoControl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iAdvertisingService = null;
        }
    };

    @Override
    public void onVideoPause() throws RemoteException {
        if (!mPlayBackView.isShowing() && mPlayView.isPlaying() && mPlayView.getDuration() > 0) {
            mPlayBackView.showPlayBack(getView(), false, null);
        } else if (!mPlayView.isPlaying() && mPlayBackView.isShowing()) {//解决MG100Vod播放界面点击ok键暂停-进设置-调节分辨率-返回到播控界面-点击ok键重新播放dialog未消失问题
            mPlayBackView.dismiss();
        }

        reportBookmark(BookMarkSwitchs.PAUSE);
        vodInfo.setPlayState(3);
        try {
            Log.e("ADS_AdvertisingBinder", "onVideoPause3");
            iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 2, (int) mPlayView.getCurrentPosition() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPlayView.pausePlay();
        mPlayView.showControlView(true);
    }

    @Override
    public void onVideoReplay() throws RemoteException {
        if (!mPlayBackView.isShowing() && mPlayView.isPlaying() && mPlayView.getDuration() > 0) {
            mPlayBackView.showPlayBack(getView(), false, null);
        } else if (!mPlayView.isPlaying() && mPlayBackView.isShowing()) {//解决MG100Vod播放界面点击ok键暂停-进设置-调节分辨率-返回到播控界面-点击ok键重新播放dialog未消失问题
            mPlayBackView.dismiss();
        }
        reportBookmark(BookMarkSwitchs.REPLAY);
        vodInfo.setPlayState(4);

        try {
            Log.e("ADS_AdvertisingBinder", "onVideoReplay3");
            iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 2, (int) mPlayView.getCurrentPosition() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPlayView.resumePlay();
        mPlayView.showControlView(true);
    }

    @Override
    public IBinder asBinder() {
        return iAdvertisingBinder;
    }

    private class HomeKeyReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            SuperLog.debug(TAG, "HomeKeyReceiver action:" + action);
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra("reason");
                if ("homekey".equals(reason) /*&& !isPlayingAd*/) {
                    vodInfo.setPlayState(2);
                    try {
                        Log.e("ADS_AdvertisingBinder", "18");
                        iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 2, (int) mPlayView.getCurrentPosition() / 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class PowerOffReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            SuperLog.info2SDDebug(TAG, "PowerOffReceiver action:" + action);
            if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                reportBookmark(BookMarkSwitchs.DESTORY);
            }
        }
    }

    private boolean isNeedStartXiriAidl() {
        String isStart = SessionService.getInstance().getSession().getTerminalConfigurationValue("vod_watch_start_xiri_aidl");
        return TextUtils.equals(isStart, "1");
//        return true;
    }

    public boolean isCurrentAdvertPlay() {
        if (null != mPlayView) {
            return mPlayView.isCurrentPlayAdvert();
        }
        return false;
    }
}