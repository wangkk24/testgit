package com.pukka.ydepg.moudule.vod.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.iflytek.ads.IAdvertisingService;
import com.iflytek.ads.IVideoControl;
import com.iflytek.xiri.Feedback;
import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.VODInfo;
import com.pukka.ydepg.common.ad.AdConstant;
import com.pukka.ydepg.common.ad.AdManager;
import com.pukka.ydepg.common.ad.AdUtil;
import com.pukka.ydepg.common.ad.IADListener;
import com.pukka.ydepg.common.ad.ui.CountDownRelativeLayouyt;
import com.pukka.ydepg.common.extview.ImageViewExt;
import com.pukka.ydepg.common.extview.RelativeLayoutExt;
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
import com.pukka.ydepg.common.report.jiutian.JiutianService;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaConstant;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaService;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Play;
import com.pukka.ydepg.common.report.ubd.scene.UBDPlay;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.CornersTransform;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.PlayerAttriUtil;
import com.pukka.ydepg.common.utils.ShowBuyControl;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.customui.VodSettingView;
import com.pukka.ydepg.event.FinishPlayUrlEvent;
import com.pukka.ydepg.event.OnDemandBackEvent;
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
import com.pukka.ydepg.moudule.mytv.MessageWebViewActivity;
import com.pukka.ydepg.moudule.mytv.ZjYdUniAndPhonePayActivty;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewProductOrderActivity;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.OwnChoose.OwnChooseEvent;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.OwnChoose.OwnChoosePresenter;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.JumpToH5OrderUtils;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;
import com.pukka.ydepg.moudule.player.ui.OnDemandVideoActivity;
import com.pukka.ydepg.moudule.player.util.OffScreenUtils;
import com.pukka.ydepg.moudule.player.util.RemoteKeyEvent;
import com.pukka.ydepg.moudule.player.util.ViewkeyCode;
import com.pukka.ydepg.moudule.vod.cache.VodDetailCacheService;
import com.pukka.ydepg.moudule.vod.cache.VoddetailUtil;
import com.pukka.ydepg.moudule.vod.playerController.VodPlayerControllerView;
import com.pukka.ydepg.moudule.vod.presenter.BookmarkEvent;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.vod.presenter.VoddetailEpsiodePresenter;
import com.pukka.ydepg.moudule.vod.utils.BrowseEpsiodesUtils;
import com.pukka.ydepg.moudule.vod.utils.OrderConfigUtils;
import com.pukka.ydepg.moudule.vod.utils.VodCornerMarkerUtils;
import com.pukka.ydepg.moudule.vod.utils.VodDetailPBSConfigUtils;
import com.pukka.ydepg.moudule.vod.view.AlacarteChoosePopWindow;
import com.pukka.ydepg.moudule.vod.view.NewPlayBackViewPopwindow;
import com.pukka.ydepg.moudule.vod.view.NewVodEpisodesView;
import com.pukka.ydepg.moudule.voice.VoiceVodListener;
import com.pukka.ydepg.moudule.voice.XiriVoiceVodUtil;
import com.pukka.ydepg.util.EventLogger;
import com.pukka.ydepg.util.PlayUtil;
import com.pukka.ydepg.view.PlayView;
import com.pukka.ydepg.xmpp.XmppManager;
import com.pukka.ydepg.xmpp.bean.XmppSuccessEvent;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public class NewBrowseTVPlayFragment extends BasePlayFragment<VideoPlayContact.IVideoPlayPresenter>
        implements IPlayListener, PlayURLCallBack, VoiceVodListener, VODDetailCallBack, VideoPlayContact.IVideoPlayView, IVideoControl {

    private final String TAG = this.getClass().getName();

    //????????????????????????????????????LiveDataHolder
    public static final int BACK_PLAY = 11111;

    public static final int NEXT_PLAY = 11112;

    public static final int EPISODE_PLAY = 11113;

    public static final int HIDE_TRY_SEE_HINT = 11114;

    public static final int SWITCH_TRY_SEE_HINT = 111141;

    public static final int UPDATE_DURATION = 11115;

    public static final int HIDE_MARKETING_CONTENT = 11116;

    public static final int AIDL_ON_VIDEO_PAUSE = 11117;

    public static final int AIDL_ON_VIDEO_REPLAY = 11118;
    public static final int SHOW_CONER_AD = 11119;
    public static final int HIDE_CONER_AD = 11120;

    //???VOD???????????????????????????????????????????????????????????????onDestory????????????0????????????????????????????????????????????????????????????
    // ???onDestroy??????????????????????????????????????????onDestory????????????
    public boolean isSwitchVOD = false;

    private List<Float> speedList = new ArrayList<>();

    //ssp?????????relationID,??????????????????
    private String sspAdvertRelationID;

    /**
     * ??????????????????
     */
    @BindView(R.id.fm_ondemand_container)
    FrameLayout mOnDemandContainer;

    /**
     * ????????????
     */
    @BindView(R.id.tv_subtitle)
    TextView vodTitletv;

    /**
     * ????????????
     */
    @BindView(R.id.tryseelayout)
    LinearLayout tryseelayout;

    /**
     * ????????????
     */
    @BindView(R.id.seehinttv)
    TextView seehinttv;

    /**
     * ?????????????????????
     */
    @BindView(R.id.tryseelayout_down)
    LinearLayout tryseelayoutDown;

    /**
     * ?????????????????????
     */
    @BindView(R.id.seehinttv_down)
    TextView seehinttvDown;

    /**
     * ??????????????????
     */
    @BindView(R.id.marketing_content_container)
    LinearLayout marketingContentContainer;

    @BindView(R.id.marketing_content)
    ImageView marketingContent;

    @BindView(R.id.advert_hint)
    CountDownRelativeLayouyt advert_hint;

    /**
     * ????????????keyback????????????????????????popwindow
     */
    private NewPlayBackViewPopwindow mPlayBackView;

    /**
     * ????????????????????????
     */
    private int mCurrentPlayState = -1;

    /**
     * ??????????????????????????? 0 ?????????1????????????????????????????????????
     */
    private int windowState = 0;

    /**
     * ???????????????????????????
     */
    private boolean isFirst = true;

    /**
     * ??????????????????????????????????????????????????????????????????????????????
     */
    private boolean isFullWindow = false;

    /**
     * ????????????????????????????????????????????????????????????????????????????????????????????????
     */
    private boolean isFromStop = false;

    /**
     * ??????????????????
     */
    public boolean alreadyStart = false;

    /**
     * ??????????????????0????????????1????????????
     */
    private String isFilm = "0";

    //??????????????????
    private PlayVodBean playVodBean;

    //?????????url
    private String videoPath;

    //???????????????
    public String sitcomNO = "";

    //VOD?????????id
    private String subContentId = "";

    //??????vodId
    private String videoId = "";
    //????????????????????????
    private boolean isAdPlayEnd;
    private int tailDuration;
    private boolean isSkipStart;

    //???????????????????????????????????????
    private boolean isadKeyBack;

    //???????????????url
    private String mLinkUrl = "";
    //??????????????????????????????????????????????????????????????????
    private boolean isFitOrPrepared;
    //??????????????????????????????
    public boolean isPrepared;
    private boolean isAdOnpause;
    private String mCopyLinkUrl;
    private AdvertContent mAdverContent;
    //?????????????????????????????????

    private BrowseEpsiodesUtils epsiodesUtils = new BrowseEpsiodesUtils();

    //???????????????
    private boolean isLookBack = false;

    //??????fragment
//    //??????list??????????????????????????????????????????BrowseEpisodeUtils???
//    private List<Episode> episodeDetailList = new ArrayList<>();

    //???????????????
    private NewVodEpisodesView vodEpisodesView;
    private VodSettingView vodSettingView;

    //??????????????????
    private boolean isComplete;

    //??????????????????
    private DetailPresenter mDetailPresenter;

    //?????????
    private long totalTimes;

    private long currentTimes;

    /**
     * ?????????Handler
     */
    private NewBrowseTVPlayFragment.OnDemandHandler mHandler = new NewBrowseTVPlayFragment.OnDemandHandler(this);

    public OnDemandHandler getmHandler() {
        return mHandler;
    }

    //????????????
    private XiriVoiceVodUtil mXiriVoiceVodUtil;

    private XmppMessage xmppmessage;

    private OffScreenUtils mOffScreenUtils;

    //????????????
    private long rightTime;

    //????????????
    private long leftTime;

    //??????????????????
    private int countForward;

    //????????????????????? 1?????????
    private int tryToSeeFlag;

    //??????????????????
    private String elapseTime = "0";

    private int tryToSeeTime = 5 * 60;

    //???????????????????????????????????????????????????????????????????????????
    private boolean isFirstFlag = true;
    private boolean isFirstStartFlag = true;

    /**
     * ????????????????????????id
     */
    private String subjectId;

    /**
     * ????????????????????????id
     */
    private String productId;

    /**
     * ????????????vodId
     */

    private String reportVodId;
    /**
     * ????????????MediaId
     */
    private String reportMediaId;


    private String newReportVodId;

    private String newReportMediaId;
    /**
     * ???????????????????????????,?????????????????????
     */
    private boolean isLookBackPlayEnd;

    private String fatherPrice;

    /**
     * ???????????????????????????????????? 0????????????1????????????
     */
    private int forwardAndBackRate;
    /**
     * ???????????????mediaId;
     */
    private String currentMediaId;
    /**
     * ????????????????????????
     */
    private boolean isTrySeeSubscribeSucess;

    /**
     * ?????????????????????
     */
    private List<NamedParameter> epsiodeCustomFields;

    private int vodTime;

    /**
     * voddetail??????
     */
    private VODDetail mVODDetail;

    private String trailVideoMarketingImageURL;

    /**
     * ??????????????????vod?????????????????????Code???
     */
    private String contentCode;

    /**
     * ?????????????????????url,???????????????
     */
    private String currentUrl;

    /**
     * UBD???action
     */
    private String action = UBDConstant.ACTION.PLAY_NORMAL;

    private boolean isNeedBackToStart = false;

    private boolean isNeedShowSkipBottomRecord = true;

    public boolean isNeedToSkip = false;

    private int speed = 1;

    private boolean isXiriChange = false;

    private boolean isSeekStart = false;

    private NewBrowseTVPlayFragment.HomeKeyReceiver homeKeyReceiver;

    private NewBrowseTVPlayFragment.PowerOffReceiver powerOffReceiver;

    private boolean canSetSpeed = true;

    private boolean canSetSkip = true;

    private String lastPlayID;
    private String lastPlayUrl;

    private Episode currentEpisode;
    @BindView(R.id.ad_img)
    ImageViewExt mAdImage;
    @BindView(R.id.ad_top_pic)
    ImageViewExt mAdImageTopPic;
    @BindView(R.id.ad_container)
    RelativeLayoutExt mAdContainer;
    private int mAdplayCount = VodCornerMarkerUtils.getPlayVodCount();

    private boolean isFirstShowAd=true;

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
     * ??????????????????
     */
    public static class OnDemandHandler extends Handler {

        private WeakReference<NewBrowseTVPlayFragment> mReference;

        OnDemandHandler(NewBrowseTVPlayFragment fragment) {
            this.mReference = new WeakReference<>(fragment);
        }

        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            NewBrowseTVPlayFragment tvPlayFragment;
            if (null != mReference && null != (tvPlayFragment = mReference.get())) {
                switch (msg.what) {
                    case RemoteKeyEvent.VOD_FAST_FORWARD:
                        tvPlayFragment.mPlayView.fastForward();
                        tvPlayFragment.showControlView(true);
                        break;
                    case RemoteKeyEvent.VOD_FAST_REWIND:
                        tvPlayFragment.mPlayView.rewind();
                        tvPlayFragment.showControlView(true);
                        break;
                    case ViewkeyCode.VIEW_KEY_BACK_TV_PLAY:
                        if (null != tvPlayFragment.mPlayBackView && !tvPlayFragment.mPlayBackView.isShowing()) {
                            tvPlayFragment.mPlayBackView.dismiss();
                        }
                        if (null != tvPlayFragment.controllerview && tvPlayFragment.controllerview.isShowing()) {
                            tvPlayFragment.controllerview.dismiss();
                        }
                        if (!tvPlayFragment.getActivity().isDestroyed()) {
                            tvPlayFragment.backPlay(false);
                        }
                        break;
                    case ViewkeyCode.VIEW_KEY_CONTINUE_PLAY:
                        tvPlayFragment.reportBookmark(BookMarkSwitchs.REPLAY);
                        if (tvPlayFragment.isCanReplayByTrySee()) {
                            Log.i("resumePlay7", "handleMessage: ");
                            tvPlayFragment.mPlayView.resumePlay();
                            tvPlayFragment.showControlView(true);
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
                            tvPlayFragment.isFromStop = true;
                            tvPlayFragment.mPlayView.rePlay();
                            tvPlayFragment.isLookBackPlayEnd = false;
                        } else {
                            tvPlayFragment.mPlayView.seekTo(0);
                            Log.i("resumePlay8", "handleMessage: ");
                            tvPlayFragment.mPlayView.resumePlay();
                            if (tvPlayFragment.tryToSeeFlag == 1) {
                                sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
                            }
                        }
                        if (null != tvPlayFragment.mPlayBackView) {
                            tvPlayFragment.mPlayBackView.dismiss();
                        }
                        if (null != tvPlayFragment.controllerview && tvPlayFragment.controllerview.isShowing()) {
                            tvPlayFragment.controllerview.dismiss();
                        }
                        tvPlayFragment.showControlView(true);
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
                            tvPlayFragment.backPlay(false);
                        }
                        break;
                    case NEXT_PLAY:
                        tvPlayFragment.reportBookmark(BookMarkSwitchs.SWITCH);
                        tvPlayFragment.handlerNextPlay();
                        break;
                    case BACK_PLAY:
                        tvPlayFragment.backPlay(true);
                        break;
                    case EPISODE_PLAY:
                        Log.i("??????????????????????????????", "handleMessage: episode play");
                        tvPlayFragment.isPrepared = false;
//                        tvPlayFragment.reportBookmark(BookMarkSwitchs.SWITCH);
                        tvPlayFragment.episondePlay(msg);
                        break;
                    case HIDE_TRY_SEE_HINT:
                        tvPlayFragment.tryseelayout.setVisibility(View.GONE);
                        tvPlayFragment.tryseelayoutDown.setVisibility(View.GONE);
                        tvPlayFragment.marketingContentContainer.setVisibility(View.GONE);
                        break;
                    case SWITCH_TRY_SEE_HINT:
                        tvPlayFragment.switchTrySeeHint();
                        break;
                    case UPDATE_DURATION:

                        if (null == tvPlayFragment.getActivity() || tvPlayFragment.getActivity().isFinishing()) {
                            return;
                        }

                        if (tvPlayFragment.tryToSeeFlag == 1) {
                            long currentPosition = tvPlayFragment.mPlayView.getCurrentPosition();
                            //??????????????????10?????????
                            if (tvPlayFragment.tryToSeeTime > 5 && currentPosition * 1L >= (tvPlayFragment.tryToSeeTime - 5) * 1000L && currentPosition * 1L < tvPlayFragment.tryToSeeTime * 1000L && !tvPlayFragment.alreadyReAuthorize) {

                                tvPlayFragment.reAuthorize();

                            } else {
                                tvPlayFragment.alreadyReAuthorize = false;
                            }
                            if (hasMessages(UPDATE_DURATION)) {
                                removeMessages(UPDATE_DURATION);
                            }
                            if (currentPosition * 1L >= tvPlayFragment.tryToSeeTime * 1000L) {
                                tvPlayFragment.pauseTrySee();
                            } else {
                                sendEmptyMessageDelayed(UPDATE_DURATION, 1001);
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
//                            tvPlayFragment.mPlayBackView.showPlayBack(tvPlayFragment.getView(), tvPlayFragment.hasNext(), null);
                        } else if (!tvPlayFragment.mPlayView.isPlaying() && tvPlayFragment.mPlayBackView.isShowing()) {//??????MG100Vod??????????????????ok?????????-?????????-???????????????-?????????????????????-??????ok???????????????dialog???????????????
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
                        tvPlayFragment.showControlView(true);
                        break;
                    case AIDL_ON_VIDEO_REPLAY:
                        if (!tvPlayFragment.mPlayBackView.isShowing() && tvPlayFragment.mPlayView.isPlaying() && tvPlayFragment.mPlayView.getDuration() > 0) {
//                            tvPlayFragment.mPlayBackView.showPlayBack(tvPlayFragment.getView(), tvPlayFragment.hasNext(), null);
                        } else if (!tvPlayFragment.mPlayView.isPlaying() && tvPlayFragment.mPlayBackView.isShowing()) {//??????MG100Vod??????????????????ok?????????-?????????-???????????????-?????????????????????-??????ok???????????????dialog???????????????
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
                        Log.i("resumePlay9", "handleMessage: ");
                        tvPlayFragment.mPlayView.resumePlay();
                        tvPlayFragment.showControlView(true);
                        if (tvPlayFragment.tryToSeeFlag == 1) {
                            sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
                        }
                        break;
                    case SHOW_CONER_AD:
                        Log.i(tvPlayFragment.TAG, "case SHOW_CONER_AD");
                        if (tvPlayFragment.mAdplayCount == 0) {
                            tvPlayFragment.mAdContainer.setVisibility(View.GONE);
                            tvPlayFragment.mLinkUrl = "";
                            //?????????????????????????????????runnable???????????????????????????runnable
                            removeMessages(SHOW_CONER_AD);
                            removeMessages(HIDE_CONER_AD);
                        } else {
//                            tvPlayFragment.getSSPAd();
                            if (null == tvPlayFragment.getActivity() || tvPlayFragment.getActivity().isFinishing()) {
                                return;
                            }
                            tvPlayFragment.mAdImageTopPic.setVisibility(View.GONE);
                            GlideApp.with(tvPlayFragment).load(R.drawable.transparent_drawable).into(tvPlayFragment.mAdImage);
                            tvPlayFragment.mAdContainer.setVisibility(View.VISIBLE);
                            tvPlayFragment.getSSPAd();
//                            tvPlayFragment.mCopyLinkUrl = tvPlayFragment.mLinkUrl;
//                            tvPlayFragment.mHandler.sendEmptyMessageDelayed(HIDE_CONER_AD, VodCornerMarkerUtils.getAdCloseTime());
//                            tvPlayFragment.mAdplayCount = tvPlayFragment.mAdplayCount - 1;
                        }
                        break;
                    case HIDE_CONER_AD:
                        Log.i(tvPlayFragment.TAG, "case HIDE_CONER_AD");
                        tvPlayFragment.mAdContainer.setVisibility(View.GONE);
                        tvPlayFragment.mLinkUrl = "";
                        //??????????????????????????????????????????
                        Log.i(tvPlayFragment.TAG, "case HIDE_CONER_AD---send SHOW_CONER_AD");
                        //??????????????????????????? ????????????????????????
                        sendEmptyMessageDelayed(SHOW_CONER_AD, VodCornerMarkerUtils.getPlayVodAdTime());
//                        tvPlayFragment.getSSPAd();
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
        homeKeyReceiver = new NewBrowseTVPlayFragment.HomeKeyReceiver();
        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        //??????????????????
        // [ ??????????????? ] ?????????????????????????????????????????????????????????????????????????????????????????????????????????
        //  ?????????????????????????????????????????????????????????????????????????????????????????????
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(homeKeyReceiver, homeFilter);
//        getActivity().registerReceiver(homeKeyReceiver, homeFilter);

        powerOffReceiver = new NewBrowseTVPlayFragment.PowerOffReceiver();
        IntentFilter powerFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);

        //??????????????????
        // [ ??????????????? ] ?????????????????????????????????????????????????????????????????????????????????????????????????????????
        //  ?????????????????????????????????????????????????????????????????????????????????????????????
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(powerOffReceiver, powerFilter);
//        getActivity().registerReceiver(powerOffReceiver, powerFilter);

        mPlayView.skipTipCallBack = new PlayView.ShowSkipTipCallBack() {
            @Override
            public void showSkip() {
                if (null != getActivity() && getActivity() instanceof NewVodDetailActivity && windowState == 0) {
                    NewVodDetailActivity activity = (NewVodDetailActivity) getActivity();
                    activity.showSkipToast(1);
                }
            }
        };

        return view;
    }

    //???????????????????????????
    public void pauseTrySee() {
        Log.i(TAG, "pauseTrySee: 1");

        //??????????????????
//        mPlayView.seekTo(tryToSeeTime * 1000);
        mPlayView.pausePlay();

        String timeStr = getTrySeeTime(tryToSeeTime);
        seehinttvDown.setText(String.format(getResources().getString(R.string.notice_free_trial_play_pause_new), timeStr));
        if (windowState == 1) {
            if (OrderConfigUtils.getInstance().needShowPopWindow()) {
                    if (OrderConfigUtils.getInstance().isAlacarte()){
                        showAlacarteChoosePopWindow();
                    }else{
                        tryseelayout.setVisibility(View.GONE);
                        tryseelayoutDown.setVisibility(View.GONE);
                        showOrderPopWindow();
                    }
                }
        }else{
            mActivity.showPreplayTryseeTip(true);
        }
    }

    private boolean isSwitchTrySeeHint = false;
    //??????5??????????????????????????????
    public void switchTrySeeHint(){
        tryseelayout.setVisibility(View.GONE);
        marketingContentContainer.setVisibility(View.GONE);

        if (null == alacarteChoosePopwindow || !alacarteChoosePopwindow.isShowing()){
            tryseelayoutDown.setVisibility(View.VISIBLE);
        }
    }

    public void initDate() {
//        mPlayView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!mPlayView.isCurrentPlayAdvert() && isPrepared && windowState == 1) {
//                    if (mPlayView.getControlViewState()) {
//                        mPlayView.hide();
//                    } else {
//                        mPlayView.showControlView(true);
//                    }
//                }
//            }
//        });
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

        mPlayBackView = new NewPlayBackViewPopwindow(getActivity(), mHandler, hasNext());
        //????????????????????????,???false???????????????????????????
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
            if (null != controllerview && controllerview.isShowing()) {
                controllerview.dismiss();
            }
        }

        @Override
        public void offScreenFail(VODDetail mVODDetail) {

        }

    };

    public void getIntentInfo(PlayVodBean mPlayVodBean) {
        Log.i(TAG, "????????????????????????: getIntentInfo" + System.currentTimeMillis());
        Log.i(TAG, "?????????????????????????????? getIntentInfo: ");
        Bundle bundle = getArguments();

        isLookBack = bundle.getBoolean(BasePlayFragment.ISPLAYBACK);
        if (null != mPlayVodBean) {
            playVodBean = mPlayVodBean;
        } else {
            String playVodBeanStr = "";
            if (!TextUtils.isEmpty(bundle.getString(OnDemandVideoActivity.PLAY_VOD_BEAN))) {
                //??????H5??????????????????????????????(??????H5?????????????????????)
                playVodBeanStr = bundle.getString(OnDemandVideoActivity.PLAY_VOD_BEAN);
            } else {
                playVodBeanStr = LiveDataHolder.get().getPlayVodBean();
            }

            SuperLog.info(TAG, "playVodBeanStr=" + playVodBeanStr);
            if (!TextUtils.isEmpty(playVodBeanStr)) {
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
//                if (episodesBean != null) {
//                    this.episodeDetailList = episodesBean.getEpisodeList();
//                }
            }
            if (isFromXmpp() && (getActivity() instanceof NewVodDetailActivity) && (!((NewVodDetailActivity) getActivity()).isAlreadyShowPushToast())) {
                ((NewVodDetailActivity) getActivity()).setAlreadyShowPushToast(true);
                EpgToast.showToast(getActivity(), "????????????????????????" + StringUtils.getEncryptionNumber(xmppmessage.getActionSource()) + "????????????");
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

        mPlayView.setWindowState(windowState);

//        NewVodDetailActivity activity = (NewVodDetailActivity) getActivity();
        String vodid = "";
        if (null != mActivity) {
            vodid = mActivity.getmVODDetail().getID();
        } else {
            if (!TextUtils.isEmpty(playVodBean.getFatherVODId())) {
                vodid = playVodBean.getFatherVODId();
            } else {
                vodid = playVodBean.getVodId();
            }
        }
        if (null == epsiodesUtils || TextUtils.isEmpty(epsiodesUtils.getVodid()) || !epsiodesUtils.getVodid().equals(vodid)) {
            epsiodesUtils = new BrowseEpsiodesUtils(vodid);
            epsiodesUtils.getSimpleVod(vodid, new VoddetailEpsiodePresenter.GetSimpleVodCallback() {
                @Override
                public void getSimpleVodSuccess(VODDetail vodDetail) {
                    if (null == epsiodesUtils || null == mActivity || mActivity.isFinishing()) {
                        Log.i(TAG, "getSimpleVodSuccess: ???????????????????????? 1 " + (null == epsiodesUtils)
                                + (null == mActivity) + (mActivity.isFinishing()));
                        return;
                    }
                    if (TextUtils.isEmpty(sitcomNO)) {
                        Log.i(TAG, "getSimpleVodSuccess: ???????????????????????? 2");
                        if (null != mActivity) {
                            Log.i(TAG, "getSimpleVodSuccess: ???????????????????????? 3");
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
                        return;
                    }
                    epsiodesUtils.getEpisode(sitcomNO, new BrowseEpsiodesUtils.GetEpisodeCallback() {
                        @Override
                        public void getEpisode(List<Episode> episodes, Episode episode) {
                            if (null == epsiodesUtils || null == mActivity || mActivity.isFinishing()) {
                                Log.i(TAG, "getSimpleVodSuccess: ???????????????????????? 4");
                                return;
                            }
                            if (null != mActivity) {
                                Log.i(TAG, "getSimpleVodSuccess: ???????????????????????? 5");
                                epsiodesUtils.refresh(sitcomNO);
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
                        }

                        @Override
                        public void getEpisodeFail() {

                        }
                    });
                }

                @Override
                public void getSimpleVodFail() {

                }
            });
        } else {
            Log.i(TAG, "getSimpleVodSuccess: ???????????????????????? 6");
            epsiodesUtils.refresh(sitcomNO);
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

    }

    public String getTrySeeTime(int tryToSeeTime) {
        StringBuffer buffer = new StringBuffer();
        int minute = tryToSeeTime / 60;
        int second = tryToSeeTime % 60;
        if (minute == 0) {
            return buffer.append(second).append("???").toString();
        } else {
            if (second == 0) {
                return buffer.append(minute).append("??????").toString();
            } else {
                return buffer.append(minute).append("??????").append(second).append("???").toString();
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
            // ?????????????????????????????????????????????????????????ID
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
        Log.i(TAG, "?????????????????????????????? initVideoView: ");
        //???????????????????????????????????????????????????
        resetDetailBtnState();
        Log.i(TAG, "????????????????????????: initVideoView" + System.currentTimeMillis());
        SuperLog.debug(TAG, "initVideoView");

        vodInfo.setVid(reportVodId);
        vodInfo.setDstVid(newReportVodId);
        vodInfo.setVideoFromUrl(currentUrl);
        vodInfo.setVideoUrl(videoPath);
        //????????????
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
//            tryToSeeTime = 99999;
            mPlayView.setTryToSeeTime(tryToSeeTime * 1000);
            String timeStr = getTrySeeTime(tryToSeeTime);
            seehinttv.setText(String.format(getResources().getString(R.string.notice_free_trial_play_new), timeStr));
            seehinttvDown.setText(String.format(getResources().getString(R.string.notice_free_trial_play_new), timeStr));
            seehinttv.setHighlightColor(getActivity().getResources().getColor(android.R.color
                    .transparent));

        } else {
            if (mHandler.hasMessages(HIDE_TRY_SEE_HINT)) {
                mHandler.removeMessages(HIDE_TRY_SEE_HINT);
                mHandler.removeMessages(HIDE_MARKETING_CONTENT);
            }
            if (mHandler.hasMessages(UPDATE_DURATION)) {
                mHandler.removeMessages(UPDATE_DURATION);
            }
            //???????????????????????????
            mPlayView.setTryToSeeFlag(0);
            tryseelayout.setVisibility(View.GONE);
            tryseelayoutDown.setVisibility(View.GONE);
            isSwitchTrySeeHint = false;
            tryseelayoutDown.setVisibility(View.GONE);
            marketingContentContainer.setVisibility(View.GONE);
        }
        mPlayView.releasePlayer();
        if (null != playVodBean && !PlayerAttriUtil.isEmpty(playVodBean.getBookmark()) && isFirstFlag) {
            if (tryToSeeFlag == 1) {
                //????????????????????????????????????
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

                /*????????????????????????
                 * 0 ?????????
                 * 1 ?????????
                 * 2 ?????? ????????? ?????? 2 ???????????? ????????????????????????
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
                this.headDuration = 0;
                tailDuration = 0;
                if (TextUtils.equals("0", isFilm) && mVODDetail != null) {
                    this.headDuration = mVODDetail.getMediaFiles().get(0).getHeadDuration();
                    this.tailDuration = mVODDetail.getMediaFiles().get(0).getTailDuration();

                    if (tailDuration == 0){
                        mPlayView.setNeedToSkip(false);
                        isNeedToSkip = false;
                    }else{
                        mPlayView.setNeedToSkip(true);
                        isNeedToSkip = true;
                    }

                    if (headDuration == 0 && tailDuration == 0) {
                        canSetSkip = false;
                    } else {
                        String canSetSkipStr = SessionService.getInstance().getSession().getTerminalConfigurationVOD_SKIP_SWITCH();
                        if (TextUtils.isEmpty(canSetSkipStr) || canSetSkipStr.equals("1") || canSetSkipStr.equals("0")) {
                            canSetSkip = false;
                        } else {
                            canSetSkip = true;
                        }
                    }
                } else {
                    Episode episode = epsiodesUtils.getSelesctedEpisode();
                    if (null != episode) {
                        headDuration = episode.getVOD().getMediaFiles().get(0).getHeadDuration();
                        tailDuration = episode.getVOD().getMediaFiles().get(0).getTailDuration();
                        if (tailDuration == 0){
                            mPlayView.setNeedToSkip(false);
                            isNeedToSkip = false;
                        }else{
                            mPlayView.setNeedToSkip(true);
                            isNeedToSkip = true;
                        }
                        if (headDuration == 0 && tailDuration == 0) {
                            canSetSkip = false;
                        } else {
                            String canSetSkipStr = SessionService.getInstance().getSession().getTerminalConfigurationVOD_SKIP_SWITCH();
                            if (TextUtils.isEmpty(canSetSkipStr) || canSetSkipStr.equals("1") || canSetSkipStr.equals("0")) {
                                canSetSkip = false;
                            } else {
                                canSetSkip = true;
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
            headDuration = 0;
            tailDuration = 0;
            if (TextUtils.equals("0", isFilm) && mVODDetail != null) {
                headDuration = mVODDetail.getMediaFiles().get(0).getHeadDuration();
                tailDuration = mVODDetail.getMediaFiles().get(0).getTailDuration();
                if (tailDuration == 0){
                    mPlayView.setNeedToSkip(false);
                    isNeedToSkip = false;
                }else{
                    mPlayView.setNeedToSkip(true);
                    isNeedToSkip = true;
                }
                if (headDuration == 0 && tailDuration == 0) {
                    canSetSkip = false;
                } else {
                    String canSetSkipStr = SessionService.getInstance().getSession().getTerminalConfigurationVOD_SKIP_SWITCH();
                    if (TextUtils.isEmpty(canSetSkipStr) || canSetSkipStr.equals("1") || canSetSkipStr.equals("0")) {
                        canSetSkip = false;
                    } else {
                        canSetSkip = true;
                    }
                }
            } else {
                Episode episode = epsiodesUtils.getSelesctedEpisode();
                if (null != episode) {
                    headDuration = episode.getVOD().getMediaFiles().get(0).getHeadDuration();
                    tailDuration = episode.getVOD().getMediaFiles().get(0).getTailDuration();
                    if (tailDuration == 0){
                        mPlayView.setNeedToSkip(false);
                        isNeedToSkip = false;
                    }else{
                        mPlayView.setNeedToSkip(true);
                        isNeedToSkip = true;
                    }
                    if (headDuration == 0 && tailDuration == 0) {
                        canSetSkip = false;
                    } else {
                        String canSetSkipStr = SessionService.getInstance().getSession().getTerminalConfigurationVOD_SKIP_SWITCH();
                        if (TextUtils.isEmpty(canSetSkipStr) || canSetSkipStr.equals("1") || canSetSkipStr.equals("0")) {
                            canSetSkip = false;
                        } else {
                            canSetSkip = true;
                        }
                    }
                } else {
                    mPlayView.setNeedToSkip(false);
                    isNeedToSkip = false;
                }

            }
            isSkipStart = SharedPreferenceUtil.getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "", true);

            /*????????????????????????
             * 0 ?????????
             * 1 ?????????
             * 2 ?????? ????????? ?????? 2 ???????????? ????????????????????????
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
//                if (isNeedToSkip) {
//                    vodTitletv.setText(playVodBean.getVodName());
//                    mPlayView.setVodTitle(playVodBean.getVodName());
//                    if (!TextUtils.isEmpty(sitcomNO)) {
//                        vodTitletv.setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//                        mPlayView.setVodTitle(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//                    }
//                    vodTitletv.setSelected(true);
//                    mPlayView.getmRecord().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.T24_C23_Light_size));
//                } else {
//                    mPlayView.getmRecord().setText(playVodBean.getVodName());
//                    mPlayView.setVodTitle(playVodBean.getVodName());
//                    if (!TextUtils.isEmpty(sitcomNO)) {
//                        mPlayView.getmRecord().setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//                        mPlayView.setVodTitle(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//                    }
//                    mPlayView.getmRecord().setSelected(true);
//                    mPlayView.getmRecord().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.T29_C21_Bold_size));
//                }

                //????????????????????????
                mPlayView.getmTitleUp().setText(playVodBean.getVodName());
                if (!TextUtils.isEmpty(sitcomNO)) {
                    mPlayView.getmTitleUp().setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
                }
                mPlayView.getmTitleUp().setSelected(true);
                mPlayView.getmTitleUp().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.margin_32));

//                mPlayView.getmRecord().setText(playVodBean.getVodName());
//                mPlayView.setVodTitle(playVodBean.getVodName());
//                if (!TextUtils.isEmpty(sitcomNO)) {
//                    mPlayView.getmRecord().setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//                    mPlayView.setVodTitle(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//                }
//                mPlayView.getmRecord().setSelected(true);
//                mPlayView.getmRecord().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.margin_32));

            } else {
                SuperLog.debug(TAG, "playVodBean??????");
            }
        }

//        //?????????????????????
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mPlayView.showControlView(true);
//            }
//        }, 3000);


    }

    @Override
    public void onPause() {
        if (isAdOnpause || AdUtil.enableSsp(AdConstant.AdClassify.CORNER)) {
            mLinkUrl = "";
            mAdContainer.setVisibility(View.GONE);
            mHandler.removeMessages(SHOW_CONER_AD);
            mHandler.removeMessages(HIDE_CONER_AD);
        }
        Log.i(TAG, "onPause: voddetail Browse");
        super.onPause();
        currentTimes = mPlayView.getCurrentPosition();
        Log.i(TAG, "currentTimes onPause: " + currentTimes);
        mPlayView.pausePlay();
//        if (windowState == 1){
//            mPlayView.showControlView(true);
//        }
        if (null != mXiriVoiceVodUtil) {
//            mXiriVoiceVodUtil.startXiri();
            mXiriVoiceVodUtil.stopXiri();
        }
    }

    //?????????????????????activity??????
    public void pausePlay() {
        currentTimes = mPlayView.getCurrentPosition();
        mPlayView.pausePlay();
//        mPlayView.playerOrPause();
//        mPlayView.showControlView(true);
    }

    //?????????????????????activity??????
    public void resumePlay() {
        if (isCanReplayByTrySee() && alreadyStart) {

            if (null != alacarteChoosePopwindow && alacarteChoosePopwindow.isShowing()){
                return;
            }

            if (null != mPlayView) {
                mPlayView.resumePlay();
            }
            if (tryToSeeFlag == 1) {
                mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
            }
            if (null != vodInfo) {
                vodInfo.setPlayState(4);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        reportBookmark(BookMarkSwitchs.DESTORY);
    }


    @Override
    public void onResume() {
        Log.i(TAG, "onResume----isAdOnpause=" + isAdOnpause + "---AdConstant.AdClassify.CORNER=" + AdUtil.enableSsp(AdConstant.AdClassify.CORNER) + "---tryToSeeFlag=" + tryToSeeFlag + "---windowState=" + windowState + "---isPrepared=" + isPrepared + "---mPlayView.isCurrentPlayAdvert()=" + mPlayView.isCurrentPlayAdvert());
        if (isAdOnpause || (AdUtil.enableSsp(AdConstant.AdClassify.CORNER) && tryToSeeFlag != 1 && windowState == 1 && isPrepared && !mPlayView.isCurrentPlayAdvert())) {
            Log.i(TAG, "onResume----SHOW_CONER_AD");
            if(isFirstShowAd)
            {
                mHandler.sendEmptyMessageDelayed(SHOW_CONER_AD, VodCornerMarkerUtils.getFirstShowAdTime());
                isFirstShowAd =false;
            }else
            {
                mHandler.sendEmptyMessageDelayed(SHOW_CONER_AD, VodCornerMarkerUtils.getPlayVodAdTime());
            }

            //??????????????????????????? ????????????????????????
//            getSSPAd();
            isAdOnpause = false;
        }
        //????????????????????????,????????????
        if (null == mPlayBackView || !mPlayBackView.isShowing()) {
            if (isCanReplayByTrySee()) {
                if (tryToSeeFlag == 1) {
                    mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
                }
                Log.i(TAG, "resumePlay:5 ");
                if (!isFromStop) {
                    resumePlay();
                }
            }
        }
        if (windowState == 1) {
            if (getActivity() instanceof NewVodDetailActivity) {
                NewVodDetailActivity newVodDetailActivity = (NewVodDetailActivity) getActivity();
//                if (!CommonUtil.IsVoice()) {
                newVodDetailActivity.activityStoptXiri();
//                }
            }
            if (null != mXiriVoiceVodUtil) {

                mXiriVoiceVodUtil.startXiri();
            }
        }
        super.onResume();
    }

    //UBD????????????,??????????????????
    private void reportPlayUBD() {

        Activity activity = getActivity();
        if (activity instanceof NewVodDetailActivity) {
            NewVodDetailActivity vodDetailActivity = (NewVodDetailActivity) activity;

            VODDetail vodDetail = mVODDetail;
            if (null == vodDetail && null != playVodBean) {
                vodDetail = new VODDetail();
                vodDetail.setID(playVodBean.getVodId());
                vodDetail.setName(playVodBean.getVodName());
                vodDetail.setContentType(playVodBean.getVodType());
            }

            // String fatherID = videoId;
            // String currentID = playVodBean

            UBDPlay.record(playVodBean.getVodId(),
                    playVodBean.getVodName(),
                    vodDetailActivity.getmVODDetail().getContentType(),
                    playVodBean.getEpisodeId(),
                    tryToSeeFlag == 1,
                    vodDetailActivity.getUbd_recommendType(),
                    vodDetailActivity.getUbd_sceneId(),
                    vodDetailActivity.getUbd_appPointedId());
        }
    }

    //??????????????????
    public boolean isPlayBackViewShowing() {
        Log.i(TAG, "NewPlayBackViewPopwindo: " + (null != mPlayBackView && mPlayBackView.isShowing()));
        return (null != mPlayBackView && mPlayBackView.isShowing());
    }

    //??????????????????????????????????????????????????????????????????
    private boolean canOnKey = false;

    /**
     * ???????????????????????????,????????????????????????
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
                    //??????????????????
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
                if (mAdContainer.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(mLinkUrl)) {
                    //????????????????????????????????????????????????
                    //??????????????????
                    isAdOnpause = true;
//                    mHandler.sendEmptyMessage(HIDE_CONER_AD);
                    mAdContainer.setVisibility(View.GONE);
                    Intent intent = new Intent(getActivity(), MessageWebViewActivity.class);
                    intent.putExtra(MessageWebViewActivity.LINKURL, mLinkUrl);
                    getActivity().startActivity(intent);
                    if (null != mAdverContent) {
                        AdManager.getInstance().reportAdvert(mAdverContent, AdConstant.AdType.BANNER, AdConstant.ReportActionType.CLICK);
                    }
                    //??????????????????
                    //????????????????????????
//                    AdManager.getInstance().reportAdvert(content, adType, AdConstant.ReportActionType.CLICK);
                } else {
                    if (!mPlayBackView.isShowing() && mPlayView.isPlaying() && mPlayView.getDuration() > 0) {
//                    mPlayBackView.showPlayBack(getView(), hasNext(), null);
                    } else if (!mPlayView.isPlaying() && mPlayBackView.isShowing()) {//??????MG100Vod??????????????????ok?????????-?????????-???????????????-?????????????????????-??????ok???????????????dialog???????????????
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
                    showControlView(true);
                }

            } else {
//                if (null != getActivity() && getActivity() instanceof NewVodDetailActivity &&((NewVodDetailActivity)((NewVodDetailActivity) getActivity())).isNeedShowAlacarteChoosePopWindow()){
//                    mPlayView.pausePlay();
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            showAlacarteChoosePopWindow(new PopupWindow.OnDismissListener() {
//                                @Override
//                                public void onDismiss() {
//                                    resumePlay();
//                                }
//                            });
//                        }
//                    },500);
//
//                    return true;
//                }
                if (!canOnKey) {
                    return true;
                }

                if (null != getActivity() && getActivity() instanceof NewVodDetailActivity) {
                    NewVodDetailActivity activity = (NewVodDetailActivity) getActivity();

                }

                if (OrderConfigUtils.getInstance().needShowPopWindow()
                        &&OrderConfigUtils.getInstance().isAlacarte()
                        && mPlayView.getCurrentPosition() > 1000
                        && (mPlayView.getCurrentPosition() - 1000) < tryToSeeTime * 1000) {
                    Log.i(TAG, "pauseTrySee: 2");
                    mPlayView.pausePlay();
                    showAlacarteChoosePopWindow();
                    return true;
                } else if (OrderConfigUtils.getInstance().needShowPopWindow()
                        &&OrderConfigUtils.getInstance().isAlacarte()
                        && mPlayView.getCurrentPosition() < 1000) {
                    Log.i(TAG, "pauseTrySee: 21");
                    mPlayView.pausePlay();
                    showAlacarteChoosePopWindow();
                    return true;
                }

                //???????????????????????????
                showOrderPopWindow();
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mAdContainer.getVisibility() == View.VISIBLE) {
                mLinkUrl = "";
                mHandler.removeMessages(HIDE_CONER_AD);
                mHandler.removeMessages(SHOW_CONER_AD);
                mHandler.sendEmptyMessage(HIDE_CONER_AD);
                Log.i(TAG, "KEYCODE_BACK----- HIDE_CONER_AD");
            } else {
                //???????????????popwindow?????????,????????????????????????????????????????????????,?????????????????????play
                if (!mPlayBackView.isShowing() && (((mCurrentPlayState == IPlayState.PLAY_STATE_HASMEDIA
                        || mCurrentPlayState == IPlayState.PLAY_STATE_BUFFERING))
                        && mPlayView.getDuration() > 0) && isCanReplayByTrySee()) {
                    mPlayBackView.showPlayBack(getView(), hasNext(), new NewPlayBackViewPopwindow.OnKeyBackDismissListener() {
                        @Override
                        public void onDismiss() {
                            //1.8????????????????????????????????????????????????????????????
                            backPlay(false);
//                        finish();
//                        playBackListener();
                        }
                    }, mVODDetail,getJiutianItemId());
                    reportBookmark(BookMarkSwitchs.PAUSE);
                    mPlayView.pausePlay();
                    //?????? ???????????????????????? 20101012
//                mPlayView.showControlView(true);
                    vodInfo.setPlayState(3);
                    try {
                        Log.e("ADS_AdvertisingBinder", "4");
                        iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 2, (int) mPlayView.getCurrentPosition() / 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (windowState == 1 && !isCanReplayByTrySee()) {
                    //??????????????????
                    backPlay(false);
//                return true;
                }
                return true;
            }


        } else if (keyCode == KeyEvent.KEYCODE_CHANNEL_UP || keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN) {
            if (!isLookBack) {
                // ??????+ || ??????-
                mReminderDialog.showRemiderDialog(getResources().getString(R.string.notice_sure_back_to_liveTv), getView());
            }
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {

//            //??????????????????
//            if ( null != vodEpisodesView && vodEpisodesView.isShowing() || ( null != vodSettingView && vodSettingView.isShowing())){
//                return true;
//            }
//            if (!"0".equals(isFilm) && !isLookBack) {
//                if (!PlayerAttriUtil.isEmpty(videoId)) {
//                    vodEpisodesView = new NewVodEpisodesView((BaseActivity) getActivity(), epsiodesUtils, videoId, mHandler, sitcomNO, fatherPrice, speed, canSetSpeed, canSetSkip);
//                    vodEpisodesView.showEpisodes(mOnDemandContainer);
//                } else {
//                    vodEpisodesView = new NewVodEpisodesView((BaseActivity) getActivity(), epsiodesUtils, "", mHandler, sitcomNO, fatherPrice, speed, canSetSpeed, canSetSkip);
//                    vodEpisodesView.showEpisodes(mOnDemandContainer);
//                }
//            } else if ("0".equals(isFilm) && !(null != playVodBean && !TextUtils.isEmpty(playVodBean.getVodType()) && TextUtils.equals(playVodBean.getVodType(), Content.PROGRAM))) {
//                //??????????????????????????????????????????????????????????????????????????????????????????
//                if (canSetSkip || canSetSpeed) {
//                    vodSettingView = new VodSettingView((BaseActivity) getActivity(), speed, canSetSpeed, canSetSkip);
//                    vodSettingView.showEpisodes(mOnDemandContainer);
//                }
//            }
        } else if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            if (!isLookBack && null != mDetailPresenter && !TextUtils.isEmpty(videoId)) {
                mDetailPresenter.getVODDetail(videoId, this);
            }
        } else if ((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_PAGE_DOWN) && isCanReplayByTrySee()) {
            Log.i(TAG, "onKeyDown: drap" + isSeekStart + " " + totalTimes);
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
            //??????
            SuperLog.debug(TAG, "keycode_drap_right");
            countForward += 1;//??????????????????
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
                //??????5min
                if ((currentPosition + rightTime) * 1L >= tryToSeeTime * 1000L) {
                    //??????????????????
                    mPlayView.dragProgress(tryToSeeTime * 1000);
                    //??????5min???
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
            showControlView(true);
        } else if ((keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_PAGE_UP)) {
            Log.i(TAG, "onKeyDown: " + isSeekStart + " " + totalTimes);
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
            Log.i(TAG, "onKeyDown: keycode_drap_right");

            if (isNeedBackToStart) {
                boolean skipOpen = SharedPreferenceUtil.getInstance().getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "", true);
                if (skipOpen && headDuration > 0) {
                    seekTo(headDuration);
                } else {
                    seekTo(0);
                }
            } else {
                //??????
                countForward += 1;//??????????????????
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
                showControlView(true);
                int currentPosition = (int) mPlayView.getCurrentPosition();
                if (currentPosition - leftTime <= 0) {
                    mPlayView.dragProgress(0);
                } else {
                    mPlayView.dragProgress((int) (currentPosition - leftTime));
                }
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

            if (isLookBack || null == playVodBean || TextUtils.isEmpty(playVodBean.getVodType()) || TextUtils.equals(playVodBean.getVodType(), Content.PROGRAM)) {
                return true;
            }

            if ((null != this.controllerview && this.controllerview.isShowing())) {
                //???????????????
                return true;
            }

            if (!alreadyStart){
                //???????????????????????????
                return true;
            }

            boolean canshow = false;

            if (null == epsiodesUtils.getmVoddetail()){
                return true;
            }

            String vodType = epsiodesUtils.getmVoddetail().getVODType();
            if (!vodType.equals("0") && !vodType.equals("2")) {
                //?????????
                canshow = true;
            }
            if (canSetSkip) {
                canshow = true;
            }
            if (canSetSpeed) {
                canshow = true;
            }
            if (!canshow) {
                //???????????????????????????
                return true;
            }

            if (!avoidAlreadyShowDown) {
                avoidAlreadyShowDown = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        avoidAlreadyShowDown = false;
                    }
                }, 500);
                VodPlayerControllerView view = new VodPlayerControllerView(getActivity(), canSetSpeed, canSetSkip, epsiodesUtils.getmVoddetail(), epsiodesUtils, mHandler, fatherPrice, switchSpeed());
                this.controllerview = view;
                view.showList(mOnDemandContainer);
                mPlayView.hideControlView();
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private VodPlayerControllerView controllerview;
    //??????????????????????????????
    private boolean avoidAlreadyShowDown = false;


    public boolean canOnKeyDown() {
        Log.i(TAG, "onKeyDown ?????????:  " + (null != mPlayBackView && mPlayBackView.isShowing()) + (null != vodEpisodesView && vodEpisodesView.isShowing()) + (null != vodSettingView && vodSettingView.isShowing()));
        return ((null != mPlayBackView && mPlayBackView.isShowing())
                || (null != vodEpisodesView && vodEpisodesView.isShowing())
                || (null != vodSettingView && vodSettingView.isShowing()));
    }

    /**
     * ???????????????????????????,????????????????????????
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
     * ???????????????????????????
     */
    @Override
    public void onPlayState(int playbackState) {
        mCurrentPlayState = playbackState;
        SuperLog.debug(TAG, "playbackState:" + EventLogger.getStateString(playbackState));
        Log.i(TAG, "onPlayState:  " + playbackState + " " + mPlayView.getDuration());
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                canOnKey = true;
            }
        }, 1200);


        Log.i(TAG, "?????????????????????????????? onPrepared: ");
        //???????????????????????????????????????????????????
        resetDetailBtnState();
        canComplete = true;
        //????????????????????????????????????
        mLinkUrl = "";
        mAdContainer.setVisibility(View.GONE);
        mHandler.removeMessages(SHOW_CONER_AD);
        mHandler.removeMessages(HIDE_CONER_AD);
        //?????????????????????
        if (AdUtil.enableSsp(AdConstant.AdClassify.CORNER)) {
            isFirstShowAd=true;
            mAdplayCount = VodCornerMarkerUtils.getPlayVodCount();
            mAdContainer.setVisibility(View.GONE);
        }
        alreadyStart = true;
        Log.i(TAG, "onPrepared:  replay");

        try {
//            NewVodDetailActivity activity = (NewVodDetailActivity) getActivity();
            mActivity.onPlayerPrepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != controllerview && controllerview.isShowing()) {
            controllerview.dismiss();
        }

        if (videoType == PlayUtil.VideoType.ADVERT) {
            //????????????
            isadKeyBack = false;
            isAdPlayEnd = false;
            isFromStop = false;
            advert_hint.setVisibility(View.VISIBLE);
            if (windowState == 0) {
                //????????????????????????????????????
                advert_hint.setTotalTimeAndStart(mPlayView.getDuration() / 1000, false);
            } else {
                //?????????????????????????????????????????????
                advert_hint.setTotalTimeAndStart(mPlayView.getDuration() / 1000, true);
            }
            advert_hint.setPlayView(mPlayView);
        } else if (videoType == PlayUtil.VideoType.VOD) {
            isJiutianReport = false;
            if (!TextUtils.isEmpty(mActivity.getmJiutianTrackerUrl())){
                startPoint = System.currentTimeMillis();
                SuperLog.debug(TAG,"jiutian play startpoint "+startPoint);
                //????????????????????????tracker,??????????????????
                JiutianService.reportPlay(mActivity.getmJiutianTrackerUrl());
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setSkipNode();
                }
            }, 300);
            Log.i(TAG, "onPrepared: onPrepared");
            isPrepared = true;
            isFirst = true;
            //????????????
            reportPlayUBD();

            mPlayView.resetControlState();

            if (windowState == 0) {
                //??????????????????
                mPlayView.setControllViewState(View.GONE, true);
            } else {
                mPlayView.setSurfaceViewSize(getResources().getDimension(R.dimen.window_width), getResources().getDimension(R.dimen.window_height));
            }


            //?????????????????????????????????
            isAdPlayEnd = true;
            advert_hint.hide();
            advert_hint.setVisibility(View.GONE);
            //??????????????????
//            AdManager.getInstance().reportAdvertDuration(sspAdvertRelationID);
            if (tryToSeeFlag == 1) {
                //???????????????????????????
                if (windowState == 1) {
                    if (OrderConfigUtils.getInstance().needShowPopWindow() && !OrderConfigUtils.getInstance().isAlacarte()) {
                        tryseelayout.setVisibility(View.VISIBLE);
                        mHandler.sendEmptyMessageDelayed(SWITCH_TRY_SEE_HINT, 5 * 1000);
                        marketingContentContainer.setVisibility(View.VISIBLE);
                        mHandler.sendEmptyMessageDelayed(HIDE_MARKETING_CONTENT, 10 * 1000);
                    }
                }
                mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
            } else {
                //????????????????????????????????????????????????????????????????????????????????????????????????
                mPlayView.setSkipStart(isSkipStart);
                mPlayView.setEndTime(tailDuration * 1000);

                //??????????????????????????????????????????????????????20????????????????????????????????????
                if (windowState == 1) {
                    //???????????????????????????
                    SuperLog.info2SD(TAG, "[onPrepared]corner is enable=" + AdUtil.enableSsp(AdConstant.AdClassify.CORNER));
                    //?????????fit??????????????????????????????
                    if (AdUtil.enableSsp(AdConstant.AdClassify.CORNER)) {
                        Log.i(TAG, "onPrepared----SHOW_CONER_AD");
                        if(isFirstShowAd)
                        {
                            mHandler.sendEmptyMessageDelayed(SHOW_CONER_AD, VodCornerMarkerUtils.getFirstShowAdTime());
                            isFirstShowAd =false;
                        }else
                        {
                            mHandler.sendEmptyMessageDelayed(SHOW_CONER_AD, VodCornerMarkerUtils.getPlayVodAdTime());
                        }
//                        mHandler.sendEmptyMessageDelayed(SHOW_CONER_AD, VodCornerMarkerUtils.getPlayVodAdTime());
                        //??????????????????????????? ????????????????????????
//                        getSSPAd();
                    }
                }
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
            boolean canSetSpeedConfig = SessionService.getInstance().getSession().getTerminalConfigurationCanSetSpeed();
            if (canSetSpeedConfig && mPlayView.canSetSpeed(switchSpeed())) {
                canSetSpeed = true;
            } else {
                canSetSpeed = false;
            }
//            canSetSpeed = mPlayView.canSetSpeed(switchSpeed());
            Log.e("gwptest", "canSetSpeed is " + canSetSpeed);
            if (!isFromStop) {
                setRecordText(true, true);
            }
            isFromStop = false;


            if (!"0".equals(isFilm) && !isLookBack && windowState == 1) {
                //?????????????????????
                //???????????????????????????????????????
//                mPlayView.setmBottomRecord();
                mPlayView.setmBottomRecordText(getBottomText());
                if (!getBottomText().equals("")) {
                    mPlayView.setDownArrowVisible(true);
                }
            }
            if (windowState == 0) {
                //?????????????????????
                mPlayView.hideControlView();
            } else if (windowState == 1) {
                //??????????????????
                showControlView(true);
            }

            if (null != playVodBean && !PlayerAttriUtil.isEmpty(playVodBean.getBookmark()) && isFirstStartFlag && Long.parseLong(playVodBean.getBookmark()) > 0 && windowState == 1) {
                //??????windowState??????
                if (isFirst) {
                    isFirst = false;
                    //??????????????????toast

                    if (!isNeedShowSkipBottomRecord) {
                        showBackToStartToast();
                    }
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideBackToStartToast();
                        }
                    }, 5000);
                }
            }

            if (windowState == 1 && isFirst) {
                isFirst = false;
            }

            isFirstStartFlag = false;
            if (tryToSeeFlag == 0) {
                reportVod(ReportVODRequest.START_PLAY);
            }
        } else {
            SuperLog.error(TAG, "Unknown videoType :" + videoType);
        }
        reportBookmark(BookMarkSwitchs.START);
    }

    //?????????????????????????????????????????????????????????
    private boolean isJiutianReport = false;
    //????????????????????????
    private long startPoint = 0;

    @Override
    public void onRelease() {
        Log.i(TAG, "onRelease: getEpisode");
        mPlayView.setControllViewState(View.GONE, true);
        if (tryToSeeFlag == 0) {
            reportVod(ReportVODRequest.FINISH_PLAY);
        }
        if (!TextUtils.isEmpty(newReportVodId)) {
            reportVodId = newReportVodId;
            reportMediaId = newReportMediaId;
        }

        if (!TextUtils.isEmpty(mActivity.getmJiutianTrackerUrl()) && !isJiutianReport){
            long duration = System.currentTimeMillis() - startPoint;
            if (startPoint > 0 && duration > 0){
                SuperLog.debug(TAG,"jiutian play end point "+System.currentTimeMillis() + " duration "+ duration);
                JiutianService.reportPlayend(mActivity.getmJiutianTrackerUrl(),duration);
                startPoint = 0;
            }
            isJiutianReport = true;
        }
    }

    /**
     * ????????????
     */
    @Override
    public void onPlayError(String msg, int errorCode, int playerType) {
        if (!isFromXmpp() && !getIsXmpp()) {
            EpgToast.showToast(OTTApplication.getContext(), msg);
        }
    }

    //??????????????????????????????????????????onPlayCompleted?????????????????????????????????
    private boolean canComplete = false;

    /**
     * ????????????
     */
    @Override
    public void onPlayCompleted() {
        Log.i(TAG, "getEpisode onPlayCompleted: ????????????");
        if (isLookBack) {
            isLookBackPlayEnd = true;
            showControlView(true);
            if (!mPlayBackView.isShowing()) {
                mPlayBackView.showPlayBack(mOnDemandContainer, hasNext(), new NewPlayBackViewPopwindow.OnKeyBackDismissListener() {
                    @Override
                    public void onDismiss() {
                        playBackListener();
                    }
                }, mVODDetail,getJiutianItemId());
                mPlayBackView.setOutsideTouchable(false);
            }
            if (mPlayView.isPlaying()) {
                mPlayView.pausePlay();
            }
        } else {
            if (!canComplete) {
                return;
            }
            canComplete = false;

            if (!TextUtils.isEmpty(mActivity.getmJiutianTrackerUrl()) && !isJiutianReport){
                long duration = System.currentTimeMillis() - startPoint;
                if (startPoint > 0 && duration > 0){
                    SuperLog.debug(TAG,"jiutian play end point "+System.currentTimeMillis() + " duration "+ duration);
                    JiutianService.reportPlayend(mActivity.getmJiutianTrackerUrl(),duration);
                    startPoint = 0;
                }
                isJiutianReport = true;
            }

//            mPlayView.pausePlay();
            //??????????????????  0?????????
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

    private NewVodDetailActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        mActivity = (NewVodDetailActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onTryPlayForH5() {

    }

    @Override
    public void onAdVideoEnd() {
        //??????????????????
        if (!isadKeyBack) {
            if (mPlayView != null && mPlayView.isCurrentPlayAdvert())
                AdManager.getInstance().reportAdvertDuration(sspAdvertRelationID, mPlayView.getCurrentPosition());
        }
    }


    public void playBackListener() {
        SuperLog.debug(TAG, "playBackListener:tryToSeeTime:" + tryToSeeTime * 1000 + "|CurrentPosition:" + mPlayView.getCurrentPosition());
        if (isCanReplayByTrySee()) {
            Log.i(TAG, "resumePlay:6 ");
            mPlayView.resumePlay();
            if (tryToSeeFlag == 1) {
                mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
            }
        } else {
            if (mHandler.hasMessages(UPDATE_DURATION)) {
                mHandler.removeMessages(UPDATE_DURATION);
            }
            mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
        }
    }

    /**
     * ????????????????????????
     */
    @Override
    public void onConfirm() {
        super.onConfirm();
        //?????????????????????
        getActivity().finish();
        //?????????????????????..........
        Intent intent = new Intent(getActivity(), LiveTVActivity.class);
        intent.putExtra(LiveTVActivity.VIDEO_TYPE, LiveTVActivity.VIDEO_TYPE_LIVETV);
        startActivity(intent);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_ondemand_new;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        epsiodesUtils = null;
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

        if (null != controllerview) {
            controllerview.dismiss();
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
            //????????????
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(homeKeyReceiver);
//            getActivity().unregisterReceiver(homeKeyReceiver);
        }

        if (powerOffReceiver != null) {
            //????????????
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(powerOffReceiver);
//            getActivity().unregisterReceiver(powerOffReceiver);
        }
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        //???????????????????????????
        OrderConfigUtils.getInstance().clear();
        super.onDestroy();
    }

    public void reportBookmark(String reportType) {

        if (isSwitchVOD) {
            return;
        }

        Log.i(TAG, "reportBookmark:  " + reportType);
        if (mPlayView != null) {
            currentTimes = mPlayView.getCurrentPosition();
        }
        String switchs = SessionService.getInstance().getSession().getTerminalConfigurationValue("add_bookmark_switchs");
        BookMarkSwitchs bookMarkSwitchs = null;
        if (!TextUtils.isEmpty(switchs)) {
            bookMarkSwitchs = JsonParse.json2Object(switchs, BookMarkSwitchs.class);
        }

        if (TextUtils.equals(reportType, BookMarkSwitchs.DESTORY) || TextUtils.equals(reportType, BookMarkSwitchs.QUIT) || (bookMarkSwitchs != null && TextUtils.equals("0", bookMarkSwitchs.getBookmarkSwitchsValue(reportType)))) {
            //???????????????????????????
            if (!PlayerAttriUtil.isEmpty(videoId) && playVodBean != null && !PlayerAttriUtil.isEmpty(playVodBean.getVodType())) {
                long currentSecond = 0;
                if (!isComplete) {
                    currentSecond = currentTimes / 1000;
                }

                Log.d(TAG, "bookmark episodeId:" + playVodBean.getEpisodeId() + "|currentTimes:" + currentTimes + "|totalTimes:" + totalTimes + "|tryToSeeFlag:" + tryToSeeFlag);
                if (tryToSeeFlag == 0 || (tryToSeeFlag == 1 && (TextUtils.equals(reportType, BookMarkSwitchs.DESTORY) || TextUtils.equals(reportType, BookMarkSwitchs.QUIT)))) {
                    if (currentSecond > 0 && totalTimes - currentTimes > 5000) {
                        if (playVodBean != null && !TextUtils.isEmpty(playVodBean.getVodType()) && !playVodBean.getVodType().equals(Content.PROGRAM)) {
                            Bookmark bookMarkItem = new Bookmark();
                            bookMarkItem.setBookmarkType(playVodBean.getVodType());
                            bookMarkItem.setItemID(videoId);
                            if (tryToSeeFlag == 0) {
                                Log.i(TAG, "1reportBookmark: currentSecond");
                                bookMarkItem.setRangeTime(currentSecond + "");
                            } else {
                                Log.i(TAG, "2reportBookmark: currentSecond");
                                bookMarkItem.setRangeTime("1");
                            }
                            // ???0?????????????????????
                            if (!TextUtils.isEmpty(isFilm)) {
                                if (!isFilm.equals("0")) {
                                    bookMarkItem.setSubContentID(subContentId);
                                    bookMarkItem.setSubContentType("VOD");
                                    bookMarkItem.setSitcomNO(sitcomNO);
                                }
                            }
                            // ?????????VOD??????
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
                            Log.i(TAG, "3reportBookmark: currentSecond");
                            if (tryToSeeFlag == 1) {
                                bookMarkItem.setRangeTime("1");
                            } else {
                                bookMarkItem.setRangeTime(currentSecond + "");
                            }

                            if (!TextUtils.isEmpty(isFilm)) {
                                if (!isFilm.equals("0")) {
                                    bookMarkItem.setSubContentID(subContentId);
                                    bookMarkItem.setSubContentType("VOD");
                                    bookMarkItem.setSitcomNO(sitcomNO);
                                }
                            }
                            // ?????????VOD??????
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
                } else {
                    //??????????????????vip????????????????????????????????????
                    playVodBean.setSitcomNO(sitcomNO);
                    playVodBean.setEpisodeId(subContentId);
                    SuperLog.debug(TAG, "event send Bookmark refresh");
                    if (null != mActivity && !mActivity.isFinishing()) {
                        EventBus.getDefault().post(new BookmarkEvent(JsonParse.object2String(playVodBean)));
                    }
                }
            }
        }
        if (TextUtils.equals(reportType, BookMarkSwitchs.START)) {
            //?????????????????????????????????????????????
            playVodBean.setSitcomNO(sitcomNO);
            playVodBean.setEpisodeId(subContentId);
            SuperLog.debug(TAG, "event send Bookmark refresh");
            if (null != mActivity && !mActivity.isFinishing()) {
                Log.i(TAG, "?????????????????????????????? reportBookmark: ");
                EventBus.getDefault().post(new BookmarkEvent(JsonParse.object2String(playVodBean)));
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

        // ???0?????????????????????
        if (!TextUtils.isEmpty(isFilm)) {
            if (!isFilm.equals("0")) {
                List<String> bookmark = new ArrayList<>();
                bookmark.add(sitcomNO);
                NamedParameter bookmarkNp = new NamedParameter(Constant.DETAIL_ZJ_BOOKMARK, bookmark);
                customFields.add(bookmarkNp);
            }
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
                Log.i(TAG, "createBookmarkSuccess:  " + sitcomNO + " " + subContentId + " " + playVodBean.getBookmark());
                if (null != mActivity && !mActivity.isFinishing()) {
                    EventBus.getDefault().post(new BookmarkEvent(JsonParse.object2String(playVodBean)));
                }

            }

        }

        @Override
        public void createBookmarkFail() {
            SuperLog.error(TAG, "createBookmarkFail");
        }
    };

    //????????????
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

        Log.i(TAG, "?????????????????????????????? playVODAuthorize: playVOD");
        mDetailPresenter.playVOD(playVODRequest, this, elapseTime);
    }

    //???????????????????????????,???????????????????????????
    @Override
    public void getVODPlayUrlSuccess(String url, String bookmark, String productId, String elapseTime) {
        Log.i(TAG, "?????????????????????????????? getVODPlayUrlSuccess: ");
        //??????????????????????????????????????????
        playVodBean.setBookmark(null);
        this.productId = productId;
        this.elapseTime = elapseTime;
        tryToSeeFlag = 0;
        OwnChooseEvent event = new OwnChooseEvent(false);
        OrderConfigUtils.getInstance().setEvent(event);
        if (isTrySeeSubscribeSucess) {
            tryseelayout.setVisibility(View.GONE);
            isSwitchTrySeeHint = false;
            tryseelayoutDown.setVisibility(View.GONE);
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
                //???????????????????????????
                playVodBean.setAdvertVideo(null);
                Log.i(TAG, "????????????????????????: " + System.currentTimeMillis());
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
        OwnChooseEvent event = new OwnChooseEvent(true);
        if (null != response.getAuthorizeResult() && null != response.getAuthorizeResult().getPricedProducts()
        && response.getAuthorizeResult().getPricedProducts().size() > 0){
            event.setPricedProducts(response.getAuthorizeResult().getPricedProducts());
        }
        OrderConfigUtils.getInstance().setEvent(event);

        OrderConfigUtils.getInstance().config(contentCode, new OrderConfigUtils.ConfigCallBack() {
            @Override
            public void configDone() {
                if (null != mActivity && !mActivity.isFinishing()){
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            playFailWithPbsConfig(response);
                        }
                    });
                }
            }
        });

    }

    //????????????????????????pbs?????????????????????
    private void playFailWithPbsConfig(PlayVODResponse response){
        Log.i(TAG, "?????????????????????????????? playFail: ");
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
                        //?????????
                        playVodBean.setSitcomNO(sitcomNO);
                        playVodBean.setEpisodeId(subContentId);
                    }
                    playVodBean.setTryToSeeFlag(1);// 1 ??????  ???????????????
                    //?????????????????????
                    playVodBean.setBookmark(null);
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
                    //???????????????????????????
                    playVodBean.setAdvertVideo(null);
                    Log.i(TAG, "????????????????????????: " + System.currentTimeMillis());
                    AdManager.getInstance().queryAdvertContent(AdConstant.AdClassify.VIDEO, new IADListener() {
                        @Override
                        public void onSuccess(List<AdvertContent> listAdvertContent) {
                            playVodBean.setAdvertVideo(listAdvertContent.get(0).getVideo());
                            //???????????????????????????????????????????????????
                            resetDetailBtnState();
                            getIntentInfo(playVodBean);
                        }

                        @Override
                        public void onFail() {
                            //???????????????????????????????????????????????????
                            resetDetailBtnState();
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
                    //pbs????????????
                    Log.i(TAG, "PbsUaService: " + mVODDetail.getID());
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

            //????????????????????????
            mPlayView.getmTitleUp().setText(playVodBean.getVodName());
            if (!TextUtils.isEmpty(sitcomNO)) {
                mPlayView.getmTitleUp().setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
            }

//            mPlayView.getmRecord().setText(playVodBean.getVodName());
//            if (!TextUtils.isEmpty(sitcomNO)) {
//                mPlayView.getmRecord().setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//            }



//            vodTitletv.setText(playVodBean.getVodName());
//            if ((!TextUtils.isEmpty(sitcomNO) && !TextUtils.isEmpty(subContentId))) {
//                vodTitletv.setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//                tryToSeeFlag = 1;
//            }
//            vodTitletv.setSelected(true);
        }
    }

    //????????????
    @Override
    public void play() {
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (null != advert_hint && !isAdPlayEnd && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        //?????????????????????
        if (windowState == 0) {
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
            showControlView(true);
            if (mPlayBackView != null && mPlayBackView.isShowing()) {
                mPlayBackView.dismiss();
            }
            if (null != controllerview && controllerview.isShowing()) {
                controllerview.dismiss();
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

    //????????????
    @Override
    public void pause() {
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        //?????????????????????
        if (windowState == 0) {
            return;
        }
        if (null != mPlayView) {
            if (!mPlayView.isPlaying()) {
                return;
            }
//            if (!mPlayBackView.isShowing() && mPlayView.isPlaying() && mPlayView.getDuration() > 0) {
//                mPlayBackView.showPlayBack(getView(), hasNext(), null);
//            }
            mPlayView.playerOrPause();
            showControlView(true);
        }
        vodInfo.setPlayState(3);
        try {
            Log.e("ADS_AdvertisingBinder", "10");
            iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 1, (int) mPlayView.getCurrentPosition() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //?????????????????????
    @Override
    public void forWard(long time) {
        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        //?????????????????????
        if (windowState == 0) {
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
        if (null != controllerview && controllerview.isShowing()) {
            controllerview.dismiss();
        }

        if (tryToSeeFlag == 1) {
            mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
            // JIRA????????????ZJYDEPGAPP-801??? DELETE START
//            if (isCanReplayByTrySee()) {
//                time = mPlayView.getCurrentPosition() / 1000 + time;
//                if (time > tryToSeeTime) {
//                    time = tryToSeeTime - mPlayView.getCurrentPosition() / 1000 - 3;
//                }
//            }
            // JIRA????????????ZJYDEPGAPP-801??? DELETE END
        }
        showControlView(true);
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
        if (null != controllerview && controllerview.isShowing()) {
            controllerview.dismiss();
        }

        if (tryToSeeFlag == 1) {
            mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
            // JIRA????????????ZJYDEPGAPP-801??? DELETE START
//            if (isCanReplayByTrySee()) {
//                time = mPlayView.getCurrentPosition() / 1000 + time;
//                if (time > tryToSeeTime) {
//                    time = tryToSeeTime - mPlayView.getCurrentPosition() / 1000 - 3;
//                }
//            }
            // JIRA????????????ZJYDEPGAPP-801??? DELETE END
        }
        showControlView(true);
        mPlayView.fastForward(time * 1000);
        if (!mPlayView.isPlaying() && mCurrentPlayState == IPlayState.PLAY_STATE_HASMEDIA && isCanReplayByTrySee()) {
            mPlayView.resumePlay();
        }
        rightTime = 0;
    }

    //?????????????????????
    @Override
    public void backForward(long time) {
        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        //?????????????????????
        if (windowState == 0) {
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
        if (null != controllerview && controllerview.isShowing()) {
            controllerview.dismiss();
        }
        if (tryToSeeFlag == 1) {
            mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
        }
        showControlView(true);
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
        if (null != controllerview && controllerview.isShowing()) {
            controllerview.dismiss();
        }
        if (tryToSeeFlag == 1) {
            mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
        }
        showControlView(true);
        mPlayView.rewind(time * 1000);
        if (!mPlayView.isPlaying() && mCurrentPlayState == IPlayState.PLAY_STATE_HASMEDIA && isCanReplayByTrySee()) {
            mPlayView.resumePlay();
        }
        leftTime = 0;
    }

    //??????????????????
    @Override
    public void finish() {
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        //?????????????????????
        if (windowState == 0) {
            return;
        } else {
            backPlay(false);
        }
//        if (mPlayBackView != null) {
//            mPlayBackView.setOnKeyBackDismissListener(null);
//        }
//        vodInfo.setPlayState(2);
//        try {
//            Log.e("ADS_AdvertisingBinder", "15");
//            iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 1, (int) mPlayView.getCurrentPosition() / 1000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        getActivity().finish();
    }
    //?????????????????????

    @Override
    public void nextPlay() {
        Log.i(TAG, "prevPlay: ?????? index = ?????????");
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        SuperLog.debug(TAG, "OnDemand nextPlay" + sitcomNO);
        if (mPlayBackView != null) {
            if (mPlayBackView.isShowing()) {
                mPlayBackView.dismiss();
            }
        }
        if (null != controllerview && controllerview.isShowing()) {
            controllerview.dismiss();
        }

        epsiodesUtils.getNextOrPreEpisode(sitcomNO, true, new BrowseEpsiodesUtils.GetEpisodeCallback() {
            @Override
            public void getEpisode(List<Episode> episodes, Episode episode) {
                isXiriChange = true;
                Message message = Message.obtain();
                message.what = EPISODE_PLAY;
                Bundle bundle = new Bundle();
                bundle.putString("SitcomNO", episode.getSitcomNO());
                bundle.putString("EpisodesId", episode.getVOD().getID());
                bundle.putString("MediaId", episode.getVOD().getMediaFiles().get(0).getID());
                bundle.putSerializable("episodeVod", episode.getVOD());
                epsiodeCustomFields = episode.getVOD().getCustomFields();
                vodTime = VodDetailCacheService.getVodTime(episode.getVOD().getMediaFiles().get(0));
                message.setData(bundle);
                mHandler.sendMessage(message);
            }

            @Override
            public void getEpisodeFail() {
                EpgToast.showToast(getActivity(), "???????????????????????????");
            }
        });
    }

    //?????????????????????
    @Override
    public void prevPlay() {
        Log.i(TAG, "prevPlay: ?????? index = ?????????");
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        SuperLog.debug("lx", "OnDemand prevPlay" + sitcomNO);
        if (mPlayBackView != null) {
            if (mPlayBackView.isShowing()) {
                mPlayBackView.dismiss();
            }
        }
        if (null != controllerview && controllerview.isShowing()) {
            controllerview.dismiss();
        }
        epsiodesUtils.getNextOrPreEpisode(sitcomNO, false, new BrowseEpsiodesUtils.GetEpisodeCallback() {
            @Override
            public void getEpisode(List<Episode> episodes, Episode episode) {
                isXiriChange = true;
                Message message = Message.obtain();
                message.what = EPISODE_PLAY;
                Bundle bundle = new Bundle();
                bundle.putString("SitcomNO", episode.getSitcomNO());
                bundle.putString("EpisodesId", episode.getVOD().getID());
                bundle.putString("MediaId", episode.getVOD().getMediaFiles().get(0).getID());
                bundle.putSerializable("episodeVod", episode.getVOD());
                epsiodeCustomFields = episode.getVOD().getCustomFields();
                vodTime = VodDetailCacheService.getVodTime(episode.getVOD().getMediaFiles().get(0));
                message.setData(bundle);
                mHandler.sendMessage(message);
            }

            @Override
            public void getEpisodeFail() {
                EpgToast.showToast(getActivity(), "???????????????????????????");
            }
        });

    }

    //????????????????????????
    @Override
    public void indexPlay(int index) {
        Log.i(TAG, "prevPlay: ?????????????????? index = " + index);
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        if (mPlayBackView != null && mPlayBackView.isShowing()) {
            mPlayBackView.dismiss();
        }
        String sitNum = String.valueOf(index);
        epsiodesUtils.getEpisode(sitNum, new BrowseEpsiodesUtils.GetEpisodeCallback() {
            @Override
            public void getEpisode(List<Episode> episodes, Episode episode) {
                isXiriChange = true;
                Message message = Message.obtain();
                message.what = BrowseTVPlayFragment.EPISODE_PLAY;
                Bundle bundle = new Bundle();
                bundle.putString("SitcomNO", episode.getSitcomNO());
                bundle.putString("EpisodesId", episode.getVOD().getID());
                bundle.putString("MediaId", episode.getVOD().getMediaFiles().get(0).getID());
                bundle.putSerializable("episodeVod", episode.getVOD());
                epsiodeCustomFields = episode.getVOD().getCustomFields();
                vodTime = VodDetailCacheService.getVodTime(episode.getVOD().getMediaFiles().get(0));
                message.setData(bundle);
                mHandler.sendMessage(message);
            }

            @Override
            public void getEpisodeFail() {
                EpgToast.showToast(getActivity(), "??????????????????");
            }
        });
    }

    //????????????????????????
    @Override
    public void seekTo(int position) {
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        //?????????????????????
        if (windowState == 0) {
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
        showControlView(true);
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

    //??????????????????
    @Override
    public void rePlay() {
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        //?????????????????????
        if (windowState == 0) {
            return;
        }
        if (mPlayBackView != null && mPlayBackView.isShowing()) {
            mPlayBackView.dismiss();
        }
        isFromStop = true;
        mPlayView.rePlay();
        showControlView(true);
    }

    @Override
    public void doSkipHistory() {
        if(getActivity()!=null)
        {
            getActivity().finish();
        }
    }

    @Override
    public void playLastEpisode() {
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (null != advert_hint && !isAdPlayEnd && advert_hint.getVisibility() == View.VISIBLE) {
            return;
        }
        SuperLog.debug(TAG, "OnDemand nextPlay" + sitcomNO);
        if (mPlayBackView != null) {
            if (mPlayBackView.isShowing()) {
                mPlayBackView.dismiss();
            }
        }
        epsiodesUtils.getLastEpisode(new BrowseEpsiodesUtils.GetEpisodeCallback() {
            @Override
            public void getEpisode(List<Episode> episodes, Episode episode) {
                isXiriChange = true;
                Message message = Message.obtain();
                message.what = EPISODE_PLAY;
                Bundle bundle = new Bundle();
                bundle.putString("SitcomNO", episode.getSitcomNO());
                bundle.putString("EpisodesId", episode.getVOD().getID());
                bundle.putString("MediaId", episode.getVOD().getMediaFiles().get(0).getID());
                bundle.putSerializable("episodeVod", episode.getVOD());
                epsiodeCustomFields = episode.getVOD().getCustomFields();
                vodTime = VodDetailCacheService.getVodTime(episode.getVOD().getMediaFiles().get(0));
                message.setData(bundle);
                mHandler.sendMessage(message);
            }

            @Override
            public void getEpisodeFail() {
                EpgToast.showToast(getActivity(), "??????????????????!");
            }
        });
    }

    //????????????
    public void backPlay(boolean isFromStop) {
        SuperLog.debug(TAG, "backPlay");

        Activity activity = getActivity();

        if (null != vodEpisodesView && vodEpisodesView.isShowing()) {
            vodEpisodesView.dismiss();
        }
        if (null != vodSettingView && vodSettingView.isShowing()) {
            vodSettingView.dismiss();
        }
        if (null != controllerview && controllerview.isShowing()) {
            controllerview.dismiss();
        }

        if (windowState == 0) {
            //????????????????????????
            this.isFromStop = true;
            if (isFromStop && this.mPlayView.isPlaying()) {
                this.mPlayView.seekTo(mPlayView.getDuration());
            }
        } else if (windowState == 1) {

            //?????????????????????????????????
            if (null != mXiriVoiceVodUtil) {
//                mXiriVoiceVodUtil.startXiri();
                mXiriVoiceVodUtil.stopXiri();
            }
            if (getActivity() instanceof NewVodDetailActivity) {
                NewVodDetailActivity newVodDetailActivity = (NewVodDetailActivity) getActivity();
//                if (!CommonUtil.IsVoice()) {
                newVodDetailActivity.activityStartXiri();
//                }
            }

            windowState = 0;
            mPlayView.setWindowState(0);
            if (activity instanceof NewVodDetailActivity) {
                if (isFullWindow) {
                    ((NewVodDetailActivity) activity).switchWindow(3);
                } else {
                    ((NewVodDetailActivity) activity).switchWindow(0);
                }

            }

            if (isFromStop) {
                Log.i(TAG, "backPlay: 4");
                this.isFromStop = true;
                if (isNeedToSkip) {
                    mPlayView.seekTo(mPlayView.getDuration());
                }
            } else {
                //??????0.5?????????????????????????????????????????????????????????,????????????????????????????????????
                Log.i(TAG, "isCanReplayByTrySee:  " + isCanReplayByTrySee());
                if (isCanReplayByTrySee()) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPlayView.resumePlay();
                            vodInfo.setPlayState(4);
                            if (tryToSeeFlag == 1) {
                                mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
                            }
                        }
                    }, 500);
                }
            }
        }

        try {
            Log.e("ADS_AdvertisingBinder", "16");
            iAdvertisingService.playStateChange(JsonParse.object2String(vodInfo), 2, (int) mPlayView.getCurrentPosition() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void exitPlay() {
        vodInfo.setPlayState(2);
    }


    //????????????
    public void episondePlay(Message msg) {
        tryseelayout.setVisibility(View.GONE);
        tryseelayoutDown.setVisibility(View.GONE);
        OrderConfigUtils.getInstance().clear();
        isFromStop = false;
        canOnKey = false;
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
        NewVodDetailActivity activity = (NewVodDetailActivity) getActivity();
        String pbs_appPointedId = activity.getPbs_appPointedId();
        String pbs_recommendType = activity.getPbs_recommendType();
        String pbs_sceneId = activity.getPbs_sceneId();
        //??????pbs????????????
        String actionType = "";
        if (TextUtils.isEmpty(pbs_appPointedId)) {
            //?????????????????????????????????????????????
            actionType = PbsUaConstant.ActionType.NORMAL_PLAY;
        } else {
            actionType = PbsUaConstant.ActionType.RECOMMEND_PLAY;
        }
        PbsUaService.report(Play.getPlayData(actionType, mVODDetail.getID(), pbs_appPointedId, pbs_recommendType, pbs_sceneId, subContentId));
        //????????????????????????????????????????????????????????????
        activity.setPbs_appPointedId("");
        activity.setPbs_recommendType("");
        activity.setPbs_sceneId("");

        if (!PlayerAttriUtil.isEmpty(videoId) && !PlayerAttriUtil.isEmpty(episodesId) && !PlayerAttriUtil.isEmpty(mediaId1)) {
            action = UBDConstant.ACTION.PLAY_NORMAL;
            playVODAuthorize(episodesId, videoId, mediaId1, elapseTime);
        } else {
            EpgToast.showToast(getActivity(), "????????????");
        }
    }

    public Episode getCurrentEpisode() {
        epsiodesUtils.refresh(sitcomNO);
        return epsiodesUtils.getSelesctedEpisode();
    }

    //??????????????????
    public boolean hasNext() {
        if (null != epsiodesUtils && !TextUtils.isEmpty(sitcomNO)) {
            return epsiodesUtils.hasNext(sitcomNO);
        } else {
            return false;
        }

    }

    //??????????????????
    public void handlerNextPlay() {
        isPrepared = false;
        Log.i(TAG, "getEpisode handlerNextPlay: " + sitcomNO);
        //???????????????
        SuperLog.error(TAG, "NEXT_PLAY");
        if (!PlayerAttriUtil.isEmpty(sitcomNO)) {

            epsiodesUtils.getNextOrPreEpisode(sitcomNO, true, new BrowseEpsiodesUtils.GetEpisodeCallback() {
                @Override
                public void getEpisode(List<Episode> episodes, Episode episode) {
                    Log.i(TAG, "getEpisode:  ??????");
                    //?????????
//                    sitcomNO = episode.getSitcomNO();
//                    subContentId = episode.getVOD().getID();
//                    newReportVodId = subContentId;
//                    newReportMediaId = episode.getVOD().getMediaFiles().get(0).getID();
//                    currentMediaId = episode.getVOD().getMediaFiles().get(0).getID();
//                    contentCode = episode.getVOD().getCode();
//                    epsiodeCustomFields = episode.getVOD().getCustomFields();
//                    vodTime = VodDetailCacheService.getVodTime(episode.getVOD().getMediaFiles().get(0));
//                    if (!PlayerAttriUtil.isEmpty(videoId)) {
//                        action = UBDConstant.ACTION.PLAY_AUTO;
//                        playVODAuthorize(episode.getVOD().getID(), videoId, episode.getVOD().getMediaFiles().get(0).getID(), episode.getVOD().getMediaFiles().get(0).getElapseTime());
//                    }

                    Message message = Message.obtain();
                    message.what = EPISODE_PLAY;
                    Bundle bundle = new Bundle();
                    bundle.putString("SitcomNO", episode.getSitcomNO());
                    bundle.putString("EpisodesId", episode.getVOD().getID());
                    bundle.putString("MediaId", episode.getVOD().getMediaFiles().get(0).getID());
                    bundle.putSerializable("episodeVod", episode.getVOD());
                    epsiodeCustomFields = episode.getVOD().getCustomFields();
                    vodTime = VodDetailCacheService.getVodTime(episode.getVOD().getMediaFiles().get(0));
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                }

                @Override
                public void getEpisodeFail() {
                    isComplete = true;
                    mHandler.sendEmptyMessage(BACK_PLAY);
                }
            });
        } else {
            isComplete = true;
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

    //????????????????????????
    public boolean isCanReplayByTrySee() {
        Log.i(TAG, "isCanReplayByTrySee: " + (null == mPlayView));
        if (null == mPlayView) {
            return false;
        }
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
     * ???????????????
     *
     * @param event
     */
    @Subscribe
    public void xmppSuccess(XmppSuccessEvent event) {
        setIsXmpp(true);
    }


    /**
     * ????????????????????????????????????????????????
     *
     * @return true ????????????
     */
    public boolean isVodPlayNow() {

        return null != mPlayView && mPlayView.isPlaying();

    }

    /**
     * ????????????????????????????????????????????????
     *
     * @return
     */
    public boolean isVodPause() {
        return null != mPlayView && mPlayView.isPause();
    }

    /**
     * ????????????????????????????????????????????????
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
     * ????????????????????????????????????(???????????????
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
     * ????????? 0????????????1?????????
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

        GlideApp.with(context).load(trailVideoMarketingImageURL)
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

        if (null == playVodBean || null == playVodBean.getBookmark() || playVodBean.getBookmark().length() == 0 || Long.parseLong(playVodBean.getBookmark()) <= 0) {
            return;
        }

        isNeedBackToStart = true;
//        mPlayView.showBackToStartToast(R.string.player_back_to_start);
        if (null != getContinueText()) {
            mPlayView.showToast("", getContinueText());
        }
    }

    private void hideBackToStartToast() {
        isNeedBackToStart = false;
        mPlayView.hideBackToStartToast();
        mPlayView.hideToast();
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
        Log.i(TAG, "onEvent: " + (null != mPlayView));
        if (mPlayView != null) {
            mPlayView.setSpeed(speedList.get(speed));

            //????????????????????????
            mPlayView.setmBottomRecordText(getBottomText());
            if (!getBottomText().equals("")) {
                mPlayView.setDownArrowVisible(true);
            }
            //??????????????????Toast
            showControlView(true);
            mPlayView.showToast("", getSpeedText());

            resumePlay();
        }
    }

    @Subscribe
    public void onEvent(PlayerSkipChangeEvent event) {
        if (mPlayView != null) {
            mPlayView.setSkipStart(SharedPreferenceUtil.getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "", true));
        }
    }


    /**
     * ????????????????????????????????????????????????
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
            // ???0?????????????????????
            if (!TextUtils.isEmpty(isFilm)) {
                if (!isFilm.equals("0")) {
                    bookMarkItem.setSubContentID(subContentId);
                    bookMarkItem.setSubContentType("VOD");
                    bookMarkItem.setSitcomNO(sitcomNO);
                }
            }
            // ?????????VOD??????
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

    private void setRecordText(boolean isStart, boolean isFromPrepared) {
        if (null == getActivity() || getActivity().isFinishing()) {
            return;
        }

        //????????????????????????????????????
        if (headDuration > 0) {
            boolean isSkip = SharedPreferenceUtil.getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "", true);
            //??????????????????
            boolean showTip = false;
            /*????????????????????????
             * 0 ?????????
             * 1 ?????????
             * 2 ?????? ????????? ?????? 2 ???????????? ????????????????????????
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
                        if (windowState == 1) {
                            NewVodDetailActivity activity = (NewVodDetailActivity) getActivity();
                            if (isFromPrepared || activity.isSkipToastShowing()) {
                                mPlayView.showToast("?????????????????????", null);
//                                mPlayView.getmRecord().setText(R.string.player_skip_start);
                            }
                        } else {
                            if (null != getActivity() && getActivity() instanceof NewVodDetailActivity) {
                                NewVodDetailActivity activity = (NewVodDetailActivity) getActivity();
                                activity.showSkipToast(0);
                            }
                        }

                    } else {
                        if (windowState == 1) {
                            NewVodDetailActivity activity = (NewVodDetailActivity) getActivity();
                            if (isFromPrepared || activity.isSkipToastShowing()) {
//                                mPlayView.getmRecord().setText(R.string.player_skip_end);
                                mPlayView.showToast("????????????????????????", null);
                            }
                        } else {
                            if (null != getActivity() && getActivity() instanceof NewVodDetailActivity) {
                                NewVodDetailActivity activity = (NewVodDetailActivity) getActivity();
                                activity.showSkipToast(1);
                            }
                        }

                    }
                } else {
                    if (showTip) {
                        if (windowState == 1) {
                            //??????????????????????????????????????????
                            mPlayView.getmRecord().setText("");
//                            if (isStart) {
//                                mPlayView.getmRecord().setText(R.string.player_not_skip_start);
//                            } else {
//                                mPlayView.getmRecord().setText(R.string.player_not_skip_end);
//                            }
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
                if (windowState == 1) {
                    //??????????????????????????????????????????
//                    if (canSetSpeed || showTip) {
//                        //???????????????????????????????????????????????????????????????????????????????????????
//                        mPlayView.getmSettingRecord().setText(R.string.player_setting_record);
//                    } else {
//                        mPlayView.getmSettingRecord().setText("");
//                    }
                    mPlayView.getmSettingRecord().setText("");
                }


            }
            mPlayView.getmRecord().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.margin_32));
        } else {
            if (null != playVodBean && !TextUtils.isEmpty(playVodBean.getVodType()) && TextUtils.equals(playVodBean.getVodType(), Content.PROGRAM)) {
                mPlayView.getmSettingRecord().setText("");
            } else {

                if (windowState == 1) {
                    //??????????????????????????????????????????
                    mPlayView.getmSettingRecord().setText("");
//                    if (!canSetSpeed) {
//                        //?????????????????????????????????????????????????????????
//                        mPlayView.getmSettingRecord().setText("");
//                    } else {
//                        mPlayView.getmSettingRecord().setText(R.string.player_setting_record);
//                    }
                }

            }
            if (playVodBean != null) {
                if (!PlayerAttriUtil.isEmpty(playVodBean.getVodName())) {

                    //????????????????????????
                    mPlayView.getmTitleUp().setText(playVodBean.getVodName());
                    if (!TextUtils.isEmpty(sitcomNO)) {
                        mPlayView.getmTitleUp().setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
                    }

//                    mPlayView.getmRecord().setText(playVodBean.getVodName());
//                    if (!TextUtils.isEmpty(sitcomNO)) {
//                        mPlayView.getmRecord().setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//                    }
                } else {
                    SuperLog.debug(TAG, "playVodBean??????");
                }
            }
            mPlayView.getmRecord().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.margin_32));
            vodTitletv.setVisibility(View.GONE);
        }
//        mPlayView.getmSettingRecord().setCompoundDrawables(Drawables.getInstance().getDrawable(getResources(), R.drawable.info_96), null, null, null);
    }

    public void onXmppBack() {
        backPlay(false);
    }

    private IAdvertisingService iAdvertisingService;

    private IBinder iAdvertisingBinder;

    private Timer timer;
    private TimerTask timerTask;
    private VODInfo vodInfo = new VODInfo();
    private IVideoControl iVideoControl = new IVideoControl.Stub() {
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
//            mPlayBackView.showPlayBack(getView(), hasNext(), null);
        } else if (!mPlayView.isPlaying() && mPlayBackView.isShowing()) {//??????MG100Vod??????????????????ok?????????-?????????-???????????????-?????????????????????-??????ok???????????????dialog???????????????
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
        showControlView(true);
    }

    @Override
    public void onVideoReplay() throws RemoteException {
        if (!mPlayBackView.isShowing() && mPlayView.isPlaying() && mPlayView.getDuration() > 0) {
//            mPlayBackView.showPlayBack(getView(), hasNext(), null);
        } else if (!mPlayView.isPlaying() && mPlayBackView.isShowing()) {//??????MG100Vod??????????????????ok?????????-?????????-???????????????-?????????????????????-??????ok???????????????dialog???????????????
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
        Log.i(TAG, "resumePlay:6 ");
        mPlayView.resumePlay();
        showControlView(true);
        if (tryToSeeFlag == 1) {
            mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
        }
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

    public void fitSize(int state) {
        if (state == 4){
            windowState = 1;
        }else{
            windowState = state;
        }

        if (null != mPlayView) {
            mPlayView.setWindowState(windowState);
        }
        if (null != mPlayView) {
            if (state == 0) {
                SuperLog.info2SD(TAG, "[backPlay]corner is enable=" + AdUtil.enableSsp(AdConstant.AdClassify.CORNER));
                if (AdUtil.enableSsp(AdConstant.AdClassify.CORNER)) {
                    mLinkUrl = "";
                    mAdContainer.setVisibility(View.GONE);
                    mHandler.removeMessages(SHOW_CONER_AD);
                    mHandler.removeMessages(HIDE_CONER_AD);
                    isFitOrPrepared = false;
                }
                if (null != mXiriVoiceVodUtil) {
//                    mXiriVoiceVodUtil.startXiri();
                    mXiriVoiceVodUtil.stopXiri();
                }
                Log.i(TAG, "windowState: 0");
                windowState = 0;
                //????????????
                if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) advert_hint.getLayoutParams();
                    params.width = (int) getResources().getDimension(R.dimen.new_voddetail_playview_width);
                    params.height = (int) getResources().getDimension(R.dimen.new_voddetail_playview_height);
                    advert_hint.setLayoutParams(params);
                    advert_hint.setSkipCountTextState(0);
                }

                //??????????????????????????????toast?????????
                if (isNeedBackToStart) {
                    hideBackToStartToast();
                }

                //???????????????????????????????????????
                if (tryseelayout.getVisibility() == View.VISIBLE) {
                    tryseelayout.setVisibility(View.GONE);
                    tryseelayoutDown.setVisibility(View.GONE);
                    marketingContentContainer.setVisibility(View.GONE);
                }

                mPlayView.setControllViewState(View.GONE, true);
                mPlayView.setSurfaceViewSize(getResources().getDimension(R.dimen.new_voddetail_playview_width), getResources().getDimension(R.dimen.new_voddetail_playview_height));

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPlayView.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                params.width = (int) getResources().getDimension(R.dimen.new_voddetail_playview_width);
                params.height = (int) getResources().getDimension(R.dimen.new_voddetail_playview_height);
                mPlayView.setLayoutParams(params);

            } else if (state == 1 || state == 4) {
                //??????????????????????????????????????????????????????????????????????????????
                SuperLog.info2SD(TAG, "[fitSize]corner is enable=" + AdUtil.enableSsp(AdConstant.AdClassify.CORNER) + "tryToSeeFlag=" + tryToSeeFlag + "--mPlayView.isCurrentPlayAdvert()=" + mPlayView.isCurrentPlayAdvert());
                if (!mPlayView.isCurrentPlayAdvert() && AdUtil.enableSsp(AdConstant.AdClassify.CORNER) && tryToSeeFlag == 0) {
                    //???????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    if (mAdplayCount > 0 && isPrepared && !mPlayView.isCurrentPlayAdvert() && tryToSeeFlag != 1) {
                        Log.i(TAG, "fitSize----SHOW_CONER_AD");
                        if(isFirstShowAd)
                        {
                            mHandler.sendEmptyMessageDelayed(SHOW_CONER_AD, VodCornerMarkerUtils.getFirstShowAdTime());
                            isFirstShowAd =false;
                        }else
                        {
                            mHandler.sendEmptyMessageDelayed(SHOW_CONER_AD, VodCornerMarkerUtils.getPlayVodAdTime());
                        }
//                        mHandler.sendEmptyMessageDelayed(SHOW_CONER_AD, VodCornerMarkerUtils.getPlayVodAdTime());
                        //??????????????????????????? ????????????????????????
//                        getSSPAd();
                    }
                }
                Log.i(TAG, "windowState: 1");
                windowState = 1;
                if (null != mXiriVoiceVodUtil) {
                    if (getActivity() instanceof NewVodDetailActivity) {
                        NewVodDetailActivity newVodDetailActivity = (NewVodDetailActivity) getActivity();
//                if (!CommonUtil.IsVoice()) {
                        newVodDetailActivity.activityStoptXiri();
//                }
                    }
                    mXiriVoiceVodUtil.startXiri();
                }
                //????????????
                if (null != advert_hint && advert_hint.getVisibility() == View.VISIBLE) {
                    advert_hint.setSkipCountTextState(1);
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) advert_hint.getLayoutParams();
                    params.width = (int) getResources().getDimension(R.dimen.window_width);
                    params.height = (int) getResources().getDimension(R.dimen.window_height);
                    advert_hint.setLayoutParams(params);
                }

                if (isFromStop) {
                    mPlayView.rePlay();
                }

                if (isFirst && (null == advert_hint || advert_hint.getVisibility() != View.VISIBLE) && isAdPlayEnd) {
                    //?????????????????????????????????????????????toast
                    isFirst = false;
                    if (!isNeedShowSkipBottomRecord) {
                        showBackToStartToast();
                    }

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //??????????????????????????????toast?????????
                            if (isNeedBackToStart) {
                                hideBackToStartToast();
                            }
                        }
                    }, 5000);

                    //?????????????????????????????????????????????
                    if (!"0".equals(isFilm) && !isLookBack) {
                        //?????????????????????
                        //?????????????????????????????????
//                        mPlayView.setmBottomRecord();
                        mPlayView.setmBottomRecordText(getBottomText());
                        if (!getBottomText().equals("")) {
                            mPlayView.setDownArrowVisible(true);
                        }
                    }

                    //?????????????????????????????????????????????????????????
                    if (!isFromStop) {
                        setRecordText(true, false);
                    }


                    //?????????????????????????????????????????????????????????
                    if (tryToSeeFlag == 1 && mPlayView.isPlaying()) {
                        if (OrderConfigUtils.getInstance().needShowPopWindow() && !OrderConfigUtils.getInstance().isAlacarte()){
                            if (!isSwitchTrySeeHint){
                                tryseelayout.setVisibility(View.VISIBLE);
                                mHandler.sendEmptyMessageDelayed(SWITCH_TRY_SEE_HINT, 5 * 1000);
                                marketingContentContainer.setVisibility(View.VISIBLE);
                                mHandler.sendEmptyMessageDelayed(HIDE_MARKETING_CONTENT, 10 * 1000);
                            }
                        }
                    }
                }

                if (isAdPlayEnd && (null == advert_hint || advert_hint.getVisibility() != View.VISIBLE)) {
                    showControlView(true);
                    if (!mPlayView.isPause() && isSwitchTrySeeHint){
                        tryseelayoutDown.setVisibility(View.VISIBLE);
                    }
                }

                //?????????????????????????????????????????????????????????
                if (tryToSeeFlag == 1 && mPlayView.isPause() && state != 4) {

                    if (OrderConfigUtils.getInstance().needShowPopWindow()) {

                        if (OrderConfigUtils.getInstance().isAlacarte()){
                            showAlacarteChoosePopWindow();
                        }else{
                            showOrderPopWindow();
                        }
                    }

                }

                mPlayView.setSurfaceViewSize(getResources().getDimension(R.dimen.window_width), getResources().getDimension(R.dimen.window_height));

                FrameLayout layout = (FrameLayout) mPlayView.getParent();
                FrameLayout.LayoutParams parentParams = (FrameLayout.LayoutParams) layout.getLayoutParams();
                parentParams.setMargins(0, 0, 0, 0);
                parentParams.width = (int) getResources().getDimension(R.dimen.window_width);
                parentParams.height = (int) getResources().getDimension(R.dimen.window_height);
                layout.setLayoutParams(parentParams);

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPlayView.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                params.width = (int) getResources().getDimension(R.dimen.window_width);
                params.height = (int) getResources().getDimension(R.dimen.window_height);
                mPlayView.setLayoutParams(params);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setSkipNode();
                    }
                }, 200);
            }
        }
    }

    //?????????super?????????????????????
    @SuppressLint("MissingSuperCall")
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }

    public interface ShowSkipTipCallBack {
        void showSkip();
    }

    public boolean isFullWindow() {
        return isFullWindow;
    }

    public void setFullWindow(boolean fullWindow) {
        isFullWindow = fullWindow;
        if (isFullWindow) {
            Log.i(TAG, "windowState: 3");
            windowState = 1;
            if (getActivity() instanceof NewVodDetailActivity) {
                NewVodDetailActivity newVodDetailActivity = (NewVodDetailActivity) getActivity();
//                if (!CommonUtil.IsVoice()) {
                newVodDetailActivity.activityStoptXiri();
//                }
            }
            if (null != mXiriVoiceVodUtil) {
                mXiriVoiceVodUtil.startXiri();
            }
//            mPlayView.setWindowState(1);
        }
    }

    public boolean isFromStop() {
        return isFromStop;
    }

    public void setSurfaceViewSize(float width, float height) {
        mPlayView.setSurfaceViewSize(width, height);
    }

    private void getSSPAd() {
        if (null != getActivity() && getActivity() instanceof NewVodDetailActivity) {
            NewVodDetailActivity activity = (NewVodDetailActivity) getActivity();
            AdManager.getInstance().queryAdvertContent(AdConstant.AdClassify.CORNER, new IADListener() {
                @Override
                public void onSuccess(List<AdvertContent> listAdvertContent) {
                    //???????????????activity??????????????????
                    if (null == activity || activity.isFinishing()) {
                        return;
                    }
                    //?????????????????????
                    if (!CollectionUtil.isEmpty(listAdvertContent)) {
                        SuperLog.info2SD(TAG, null == listAdvertContent.get(0) ? "null" : "listAdvertContent.get(0)=" + listAdvertContent.get(0));
                        if (null != listAdvertContent.get(0) && null != listAdvertContent.get(0).getDisplay() && null != listAdvertContent.get(0).getDisplay().getBanner() && null != listAdvertContent.get(0).getDisplay().getBanner().getImg() && !TextUtils.isEmpty(listAdvertContent.get(0).getDisplay().getBanner().getImg())) {
                            GlideApp.with(activity).load(listAdvertContent.get(0).getDisplay().getBanner().getImg()).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    SuperLog.info2SD(TAG, "load picture fail");
//                                    new Handler().post(new Runnable() {
//                                        @Override
//                                        public void run() {
                                    //??????????????????????????????????????????
                                    mLinkUrl = "";
                                    mHandler.sendEmptyMessageDelayed(HIDE_CONER_AD, VodCornerMarkerUtils.getAdCloseTime());
                                    mAdplayCount = mAdplayCount - 1;
//                                        }
//                                    });

//                                    GlideApp.with(activity).load(listAdvertContent.get(0).getDisplay().getBanner().getImg()).into(mAdImage);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    SuperLog.info2SD(TAG, "load picture success");
                                    mAdImageTopPic.setVisibility(View.VISIBLE);
                                    mAdverContent = listAdvertContent.get(0);
                                    AdManager.getInstance().reportAdvert(mAdverContent, AdConstant.AdType.BANNER, AdConstant.ReportActionType.IMPRESSION);
//                                    new Handler().post(new Runnable() {
//                                        @Override
//                                        public void run() {
                                    mHandler.sendEmptyMessageDelayed(HIDE_CONER_AD, VodCornerMarkerUtils.getAdCloseTime());
                                    mAdplayCount = mAdplayCount - 1;
//                                        }
//                                    });

                                    return false;
                                }
                            }).into(mAdImage);


//                            mAdContainer.setVisibility(View.VISIBLE);
//                            mHandler.sendEmptyMessageDelayed(HIDE_CONER_AD, VodCornerMarkerUtils.getAdCloseTime());
                            Log.i(TAG, "getSSPAd----- HIDE_CONER_AD");
                        } else {
//                            mAdContainer.setVisibility(View.GONE);
                            SuperLog.error(TAG, "queryAdvertContent imageUrl isEmpty");
                            return;
                        }
                        //???????????????????????????
                        if (null != listAdvertContent.get(0) && null != listAdvertContent.get(0).getDisplay() && null != listAdvertContent.get(0).getDisplay().getBanner() && null != listAdvertContent.get(0).getDisplay().getBanner().getLink() && !TextUtils.isEmpty(listAdvertContent.get(0).getDisplay().getBanner().getLink().getUrl())) {
                            SuperLog.info2SD(TAG, "mLinkUrl=" + listAdvertContent.get(0).getDisplay().getBanner().getLink().getUrl());
                            mLinkUrl = listAdvertContent.get(0).getDisplay().getBanner().getLink().getUrl();
                        } else {
                            mLinkUrl = "";
                        }
                    } else {
                        SuperLog.error(TAG, "queryAdvertContent  isEmpty");
                    }
                }

                @Override
                public void onFail() {
                    //??????????????????
                    SuperLog.error(TAG, "queryAdvertContent fail");
                    mHandler.sendEmptyMessageDelayed(HIDE_CONER_AD, VodCornerMarkerUtils.getAdCloseTime());
                    mAdplayCount = mAdplayCount - 1;

                }
            }, activity.getmVODDetail());
        }
    }

    public PlayView getmPlayView() {
        return mPlayView;
    }

    //???????????????????????????????????????????????????
    private void resetDetailBtnState() {
        if (null != getActivity() && getActivity() instanceof NewVodDetailActivity) {
            NewVodDetailActivity activity = (NewVodDetailActivity) getActivity();
            activity.resetPlayButtonState();
        }
    }

    //???????????????????????????
    private String getBottomText() {

        String str = "";
        if (null != epsiodesUtils.getmVoddetail()) {

            String vodType = epsiodesUtils.getmVoddetail().getVODType();
            if ((!vodType.equals("0")) && (!vodType.equals("2"))) {
                str = str + "??????        ";
            }

            if (canSetSpeed) {
                str = str + "????????????   (" + switchSpeed() + "" + ")        ";
            }

            if (canSetSkip) {
//                boolean skipOpen = SharedPreferenceUtil.getInstance().getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "",true);
//                if (skipOpen){
//                    str = str + "????????????????????????";
//                }else{
//                    str = str + "????????????????????????";
//                }
                str = str + "????????????";
            }

            return str;


        } else {
            return "";

        }
    }

    public void showControlView(boolean isDelayHide) {
        if (null == epsiodesUtils.getmVoddetail()){
            return;
        }
        //?????????????????????????????????????????????
        if (!alreadyStart){
            return;
        }

        boolean showDownArror = false;
        String vodType = epsiodesUtils.getmVoddetail().getVODType();
        if (!vodType.equals("0") && !vodType.equals("2")) {
            //?????????
            showDownArror = true;
        }
        if (canSetSkip) {
            showDownArror = true;
        }
        if (canSetSpeed) {
            showDownArror = true;
        }

        //????????????????????????
        mPlayView.getmTitleUp().setVisibility(View.VISIBLE);
        mPlayView.getmTitleUp().setText(playVodBean.getVodName());
        if (!TextUtils.isEmpty(sitcomNO)) {
            mPlayView.getmTitleUp().setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
        }
        mPlayView.getmTitleUp().setSelected(true);
        mPlayView.getmTitleUp().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.margin_32));

//        mPlayView.getmRecord().setVisibility(View.VISIBLE);
//        mPlayView.getmRecord().setText(playVodBean.getVodName());
//        mPlayView.setVodTitle(playVodBean.getVodName());
//        if (!TextUtils.isEmpty(sitcomNO)) {
//            mPlayView.getmRecord().setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//            mPlayView.setVodTitle(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//        }
//        mPlayView.getmRecord().setSelected(true);
//        mPlayView.getmRecord().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.margin_32));
        mPlayView.showControlView(isDelayHide);
        if (headDuration > 0 || tailDuration > 0) {
            setSkipNode();
        }
        mPlayView.setmBottomRecordText(getBottomText());
        mPlayView.setDownArrowVisible(showDownArror);
    }

    private SpannableString getContinueText() {

        String string = "???????????????";
        if (null != playVodBean && null != playVodBean.getBookmark() && playVodBean.getBookmark().length() > 0) {
            int second = Integer.valueOf(playVodBean.getBookmark());
            if (second >= 60) {
                int min = second / 60;
                int secondLeft = second % 60;
                if (min >= 60) {
                    int hour = min / 60;
                    int minLeft = min % 60;
                    string = string + hour + "???" + minLeft + "???" + secondLeft + "?????????";
                } else {
                    string = string + min + "???" + secondLeft + "?????????";
                }
            } else {
                string = string + second + "?????????";
            }

            String endString = "????????????????????????";

            string = string + endString;

            SpannableString spanString = new SpannableString(string);
            ForegroundColorSpan spanColor = new ForegroundColorSpan(getActivity().getResources().getColor(R.color.controller_toast_text_color));
            spanString.setSpan(spanColor, spanString.length() - 8, spanString.length() - 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            return spanString;
        }
        return null;
    }

    private SpannableString getSpeedText() {
        String string = "?????????" + switchSpeed() + "??????";
        SpannableString spanString = new SpannableString(string);
        ForegroundColorSpan spanColor = new ForegroundColorSpan(getActivity().getResources().getColor(R.color.controller_toast_text_color));
        spanString.setSpan(spanColor, 3, spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    int headDuration = 0;

    //??????????????????????????????
    private void setSkipNode() {
        Log.i(TAG, "setNode setSkipNode: " + headDuration + " " + tailDuration + " " + mPlayView.getDuration());
        if (headDuration == 0 && tailDuration == 0) {
            return;
        }
        //?????????
        long totalDuration = mPlayView.getDuration();

        if (headDuration != 0 && totalDuration != 0) {
            float headPercent = headDuration * 1000 * 1000 / totalDuration;

            mPlayView.setNode(0, headPercent);
        }

        if (tailDuration != 0 && totalDuration != 0) {
            float tailPercent = tailDuration * 1000 * 1000 / totalDuration;
            mPlayView.setNode(1, tailPercent);
        }


    }

    public boolean isSwitchVOD() {
        return isSwitchVOD;
    }

    public void setSwitchVOD(boolean switchVOD) {
        isSwitchVOD = switchVOD;
    }


    private AlacarteChoosePopWindow alacarteChoosePopwindow;

    //??????????????????????????????
    private void showAlacarteChoosePopWindow() {
        if (null != alacarteChoosePopwindow && alacarteChoosePopwindow.isShowing()) {
            //??????????????????
            return;
        }
        if (null != getActivity()) {
            mPlayView.pausePlay();
            if (getActivity() instanceof NewVodDetailActivity) {
                NewVodDetailActivity activity = (NewVodDetailActivity) getActivity();
                if (null != controllerview && controllerview.isShowing()) {
                    controllerview.dismiss();
                }

                alacarteChoosePopwindow = new AlacarteChoosePopWindow(activity,true);
                alacarteChoosePopwindow.setOwnChoose(true);
                alacarteChoosePopwindow.setCallback(new AlacarteChoosePopWindow.AddOwnChooseCallback() {
                    @Override
                    public void addOwnChoose() {
                        if (null != getActivity() && getActivity() instanceof NewVodDetailActivity) {
                            NewVodDetailActivity activity1 = (NewVodDetailActivity) getActivity();
                            String contentId = "";
                            if (epsiodesUtils.getmVoddetail().getVODType().equals("0") || epsiodesUtils.getmVoddetail().getVODType().equals("2")) {
                                contentId = epsiodesUtils.getmVoddetail().getCode();
                            } else if (null != epsiodesUtils.getSelesctedEpisode()) {
                                contentId = epsiodesUtils.getSelesctedEpisode().getVOD().getCode();
                            }

                            OrderConfigUtils.getInstance().getPresenter().addAlacarteChooseContent(contentId, activity1, new OwnChoosePresenter.AddAlacarteChooseContentCallBack() {
                                @Override
                                public void addAlacarteChooseContentSuccsee() {
                                    if (null == getActivity() || getActivity().isFinishing()) {
                                        return;
                                    }
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            alreadyReAuthorize = false;
                                            reAuthorize();
                                        }
                                    }, 200);
                                }

                                @Override
                                public void addAlacarteChooseContentFail(String descirbe) {
                                    if (null == getActivity() || getActivity().isFinishing()) {
                                        return;
                                    }
                                    Toast.makeText(activity1, "addAlacarteChooseContentFail: " + descirbe, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void cancelChoose() {
                        //goToOrder();
                        resumePlay();
                        alacarteChoosePopwindow.dismiss();
                    }

                    @Override
                    public void upgradeChoose() {
                        jumpToOrder();
                    }

                    @Override
                    public void backChoose() {
                        Log.i(TAG, "pausetrysee: " + mPlayView.getCurrentPosition() + tryToSeeTime);
                        alacarteChoosePopwindow.dismiss2();
                        resumePlay();
                    }
                });
                alacarteChoosePopwindow.showView(mOnDemandContainer);
                alacarteChoosePopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        alacarteChoosePopwindow = null;
                    }
                });
            }
        }
    }
    //????????????????????????
    private void showOrderPopWindow() {
        if (null != alacarteChoosePopwindow && alacarteChoosePopwindow.isShowing()) {
            //??????????????????
            return;
        }
        if (null != getActivity()) {
            mPlayView.pausePlay();
            tryseelayoutDown.setVisibility(View.GONE);
            tryseelayout.setVisibility(View.GONE);
            if (getActivity() instanceof NewVodDetailActivity) {
                NewVodDetailActivity activity = (NewVodDetailActivity) getActivity();
                VodDetailPBSConfigUtils utils = OrderConfigUtils.getInstance().getVodDetailPBSConfigUtils();
                if (null != controllerview && controllerview.isShowing()) {
                    controllerview.dismiss();
                }
                alacarteChoosePopwindow = new AlacarteChoosePopWindow(activity,false);
                alacarteChoosePopwindow.setOwnChoose(false);
                alacarteChoosePopwindow.setCallback(new AlacarteChoosePopWindow.AddOwnChooseCallback() {
                    @Override
                    public void addOwnChoose() {

                    }

                    @Override
                    public void cancelChoose() {
                        resumePlay();
                        tryseelayoutDown.setVisibility(View.VISIBLE);
                        alacarteChoosePopwindow.dismiss();
                    }

                    @Override
                    public void upgradeChoose() {
                        jumpToOrder();
                        tryseelayoutDown.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void backChoose() {
                        alacarteChoosePopwindow.dismiss2();
                        tryseelayoutDown.setVisibility(View.VISIBLE);
                        resumePlay();
                    }
                });
                alacarteChoosePopwindow.showView(mOnDemandContainer);
                alacarteChoosePopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        alacarteChoosePopwindow = null;
                    }
                });
            }
        }
    }

    private void goToOrder() {
        AuthorizeResult authorizeResult = JsonParse.json2Object(playVodBean.getAuthResult(), AuthorizeResult.class);
        if (null != authorizeResult) {
            List<Product> products = authorizeResult.getPricedProducts();
            if (SessionService.getInstance().getSession().isHotelUser()) {
                if (products == null || products.size() == 0) {
                    EpgToast.showToast(getActivity(), getResources().getString(R.string.notice_no_orderable_product));
                    return;
                }
                for (int i = products.size() - 1; i >= 0; i--) {
                    Product mProductInfo = products.get(i);
                    if (!StringUtils.isSubscribeByDay(mProductInfo)) {
                        products.remove(i);
                    }

                }
                if (products == null || products.size() == 0) {
                    EpgToast.showToast(getActivity(), getResources().getString(R.string.notice_no_orderable_product));
                    return;
                }
                authorizeResult.setPricedProducts(products);

            } else {
                if (products == null || products.size() == 0) {
                    EpgToast.showToast(getActivity(), getResources().getString(R.string.notice_no_orderable_product));
                    return;

                }
            }

        } else {
            EpgToast.showToast(getActivity(), getResources().getString(R.string.notice_no_orderable_product));
            return;

        }
        String needJumpToH5Order = SessionService.getInstance().getSession().getTerminalConfigurationNeedJumpToH5Order();
        if (null != needJumpToH5Order && needJumpToH5Order.equals("1")) {
            JumpToH5OrderUtils.getInstance().jumpToH5ORderFromOffScreen(authorizeResult.getPricedProducts(), getActivity(), null != xmppmessage, xmppmessage, mVODDetail);
            //pbs????????????
            Log.i(TAG, "PbsUaServ ice: " + mVODDetail.getID());
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
    }

    //????????????????????????????????????
    private ShowBuyControl showBuyControl;
    //???????????????????????????????????????
    private boolean alreadyReAuthorize = false;

    //?????????????????????
    public void reAuthorize() {

        if (alreadyReAuthorize) {
            return;
        }

        alreadyReAuthorize = true;
        if (null == getActivity()) {
            return;
        }

        showBuyControl = new ShowBuyControl((RxAppCompatActivity) getActivity());
        showBuyControl.setCallBack(new ShowBuyControl.ShowBuyTagCallBack() {
            @Override
            public void showBuyTag(int showBuy) {
                if (showBuy == 2) {
                    if (null == getActivity() || getActivity().isFinishing()) {
                        return;
                    }
                    //???????????????????????????
                    tryToSeeFlag = 0;
                    if (mHandler.hasMessages(HIDE_TRY_SEE_HINT)) {
                        mHandler.removeMessages(HIDE_TRY_SEE_HINT);
                        mHandler.removeMessages(HIDE_MARKETING_CONTENT);
                    }
                    if (mHandler.hasMessages(UPDATE_DURATION)) {
                        mHandler.removeMessages(HIDE_TRY_SEE_HINT);
                    }
                    //???????????????????????????
                    mPlayView.setTryToSeeFlag(0);
                    tryseelayout.setVisibility(View.GONE);
                    isSwitchTrySeeHint = false;
                    tryseelayoutDown.setVisibility(View.GONE);
                    marketingContentContainer.setVisibility(View.GONE);
                    resumePlay();
                }
            }
        });


        PlayVODRequest mPlayVodRequest = new PlayVODRequest();
        //??????
        if ("0".equals(isFilm)) {
            if (!TextUtils.isEmpty(videoId)) {
                mPlayVodRequest.setVODID(videoId);
            }
            if (!TextUtils.isEmpty(currentMediaId)) {
                mPlayVodRequest.setMediaID(currentMediaId);
            }

            mPlayVodRequest.setURLFormat("1");
            mPlayVodRequest.setIsReturnProduct("1");
        } else if (null != epsiodesUtils.getSelesctedEpisode()) {
            //?????????
            if (!TextUtils.isEmpty(epsiodesUtils.getmVoddetail().getID())) {
                mPlayVodRequest.setSeriesID(epsiodesUtils.getmVoddetail().getID());
            }
            if (null != epsiodesUtils.getSelesctedEpisode().getVOD()) {
                mPlayVodRequest.setVODID(epsiodesUtils.getSelesctedEpisode().getVOD().getID());
            }
            if (null != epsiodesUtils.getSelesctedEpisode().getVOD().getMediaFiles() && epsiodesUtils.getSelesctedEpisode().getVOD().getMediaFiles().size() > 0) {
                mPlayVodRequest.setMediaID(epsiodesUtils.getSelesctedEpisode().getVOD().getMediaFiles().get(0).getID());
            }
            mPlayVodRequest.setURLFormat("1");
            mPlayVodRequest.setIsReturnProduct("1");
        }

        showBuyControl.playVod(mPlayVodRequest);
    }

    //?????????????????????????????????
    public void showTrySeeText() {
        //?????????????????????

    }

    public void showTrySeeTextAlacarteComplete() {
        //?????????????????????

    }

    private void jumpToOrder() {
        Log.i(TAG, "jumpToOrder: ");
        if (null != playVodBean && !TextUtils.isEmpty(playVodBean.getAuthResult())) {
            AuthorizeResult authorizeResult = JsonParse.json2Object(playVodBean.getAuthResult(), AuthorizeResult.class);
            if (null != authorizeResult) {
                List<Product> products = authorizeResult.getPricedProducts();
                if (SessionService.getInstance().getSession().isHotelUser()) {
                    if (products == null || products.size() == 0) {
                        EpgToast.showToast(getActivity(), getResources().getString(R.string.notice_no_orderable_product));
                        return;
                    }
                    for (int i = products.size() - 1; i >= 0; i--) {
                        Product mProductInfo = products.get(i);
                        if (!StringUtils.isSubscribeByDay(mProductInfo)) {
                            products.remove(i);
                        }
                    }
                    if (products == null || products.size() == 0) {
                        EpgToast.showToast(getActivity(), getResources().getString(R.string.notice_no_orderable_product));
                        return;
                    }
                    authorizeResult.setPricedProducts(products);
                } else {
                    if (products == null || products.size() == 0) {
                        EpgToast.showToast(getActivity(), getResources().getString(R.string.notice_no_orderable_product));
                        return;
                    }
                }

            } else {
                EpgToast.showToast(getActivity(), getResources().getString(R.string.notice_no_orderable_product));
                return;

            }
            String needJumpToH5Order = SessionService.getInstance().getSession().getTerminalConfigurationNeedJumpToH5Order();
            if (null != needJumpToH5Order && needJumpToH5Order.equals("1")) {
                JumpToH5OrderUtils.getInstance().jumpToH5ORderFromOffScreen(authorizeResult.getPricedProducts(), getActivity(), null != xmppmessage, xmppmessage, mVODDetail);
                //pbs????????????
                Log.i(TAG, "PbsUaServ ice: " + mVODDetail.getID());
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

    //??????????????????????????????itemId
    public String getJiutianItemId(){
        if (null != mActivity && !TextUtils.isEmpty(mActivity.getJiutianItemId())){
            //?????????intent?????????itemid
            return mActivity.getJiutianItemId();
        }else{
            //??????????????????vod?????????id???cpid??????
            return (mVODDetail.getCpId() + "|"+ mVODDetail.getID());
        }
    }


}
