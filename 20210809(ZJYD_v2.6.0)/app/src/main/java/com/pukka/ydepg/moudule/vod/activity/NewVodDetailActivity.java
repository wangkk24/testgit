package com.pukka.ydepg.moudule.vod.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.leanback.widget.BrowseFrameLayout;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.VerticalGridView;
import androidx.leanback.widget.Visibility;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iflytek.xiri.Feedback;
import com.iflytek.xiri.scene.ISceneListener;
import com.iflytek.xiri.scene.Scene;
import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.ad.AdConstant;
import com.pukka.ydepg.common.ad.AdManager;
import com.pukka.ydepg.common.extview.FrameLayoutExt;
import com.pukka.ydepg.common.extview.VerticalScrollTextView;
import com.pukka.ydepg.common.http.bean.node.XmppMessage;
import com.pukka.ydepg.common.http.v6bean.v6node.AlacarteChoosedContent;
import com.pukka.ydepg.common.http.v6bean.v6node.BookMarkSwitchs;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.Cast;
import com.pukka.ydepg.common.http.v6bean.v6node.CastRole;
import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.PlayVodBean;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.SingleMark;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertContent;
import com.pukka.ydepg.common.refresh.RefreshManager;
import com.pukka.ydepg.common.report.jiutian.JiutianService;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaConstant;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaService;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Detail;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Play;
import com.pukka.ydepg.common.report.ubd.scene.UBDAdvert;
import com.pukka.ydepg.common.report.ubd.scene.UBDRecommendImpression;
import com.pukka.ydepg.common.report.ubd.scene.UBDSwitch;
import com.pukka.ydepg.common.utils.ActivityStackControlUtil;
import com.pukka.ydepg.common.utils.ClickUtil;
import com.pukka.ydepg.common.utils.GlideUtil;
import com.pukka.ydepg.common.utils.HeartBeatUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.RxApiManager;
import com.pukka.ydepg.common.utils.ShowBuyControl;
import com.pukka.ydepg.common.utils.SubscriptControl;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.common.utils.TextUtil;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.DensityUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.customui.MiguQRViewPopWindow;
import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;
import com.pukka.ydepg.event.OnDemandBackEvent;
import com.pukka.ydepg.event.PlayUrlEvent;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.activity.TopicActivity;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.view.CustomRoundImageView;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.base.BasePlayFragment;
import com.pukka.ydepg.moudule.featured.view.CustomScrollView;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.mytv.MessageWebViewActivity;
import com.pukka.ydepg.moudule.mytv.NewMyMovieActivity;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.OwnChoose.OwnChooseEvent;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.OwnChoose.OwnChoosePresenter;
import com.pukka.ydepg.moudule.player.inf.ControlKeyDownListener;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;
import com.pukka.ydepg.moudule.player.ui.OnDemandVideoActivity;
import com.pukka.ydepg.moudule.player.util.RemoteKeyEvent;
import com.pukka.ydepg.moudule.vod.NewAdapter.NewChooseEpisodeAdapter;
import com.pukka.ydepg.moudule.vod.NewAdapter.NewChooseVarietyEpisodeAdapter;
import com.pukka.ydepg.moudule.vod.NewAdapter.NewTotalEpisodeAdapter;
import com.pukka.ydepg.moudule.vod.NewAdapter.NewVoddetailRecListAdapter;
import com.pukka.ydepg.moudule.vod.NewAdapter.VoddetailScrollEvent;
import com.pukka.ydepg.moudule.vod.adapter.ChooseEpisodeAdapter;
import com.pukka.ydepg.moudule.vod.adapter.NewCreditAdapter;
import com.pukka.ydepg.moudule.vod.cache.VoddetailUtil;
import com.pukka.ydepg.moudule.vod.presenter.BookmarkEvent;
import com.pukka.ydepg.moudule.vod.presenter.CollectionEvent;
import com.pukka.ydepg.moudule.vod.presenter.NewDetailPresenter;
import com.pukka.ydepg.moudule.vod.presenter.NewScoresEvent;
import com.pukka.ydepg.moudule.vod.presenter.VodDetailRecPresenter;
import com.pukka.ydepg.moudule.vod.presenter.VoddetailEpsiodePresenter;
import com.pukka.ydepg.moudule.vod.utils.DetailCommonUtils;
import com.pukka.ydepg.moudule.vod.utils.DetailVerticalScrollTextView;
import com.pukka.ydepg.moudule.vod.utils.MultViewUtils;
import com.pukka.ydepg.moudule.vod.utils.OrderConfigUtils;
import com.pukka.ydepg.moudule.vod.utils.RemixRecommendUtil;
import com.pukka.ydepg.moudule.vod.utils.ShowCastListUtils;
import com.pukka.ydepg.moudule.vod.utils.SpaceItemDecoration;
import com.pukka.ydepg.moudule.vod.utils.VodAdUtils;
import com.pukka.ydepg.moudule.vod.utils.VodDetailPBSConfigUtils;
import com.pukka.ydepg.moudule.vod.utils.VoddetailEpsiodesUtils;
import com.pukka.ydepg.moudule.vod.view.NewDetailDataView;
import com.pukka.ydepg.xmpp.XmppManager;
import com.pukka.ydepg.xmpp.bean.XmppConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewVodDetailActivity extends BaseActivity implements NewDetailDataView {

    public static final String TAG = "VodDetailActivity";

    public static final String VOD_ID = "vod_id";

    public static final String OLD_BOOKMARK = "oldBookmark";

    public static final String ORGIN_VOD = "orgin_vod";

    public static final String SUBJECT_ID = "subject_id";

    //???????????????????????????????????????????????????1
    public static final String NEED_FULL_WINDOW = "need_full_window";

    //???????????????Url????????????????????????????????????
    public static final String JIUTIAN_TRACKER_URL = "jiutian_tracker_url";

    //???????????????Url????????????????????????????????????
    public static final String JIUTIAN_ITEM_ID = "jiutian_item_id";

    public static final String RECOMMEND_TYPE = "recommend_type";

    private static final int RequestDetail = 1002;

    private static final int SET_BG = 1001;

    private static final int EPSIODE_REQUESTFOCUS = 1003;

    private static final int REFRESH_EPSIODE_CANCLICK = 1004;

    public static final String FROM_MAIN = "from_main";

    public static final String BUNDLE_UBD_RECOMMEND_TYEP = "recommendType";

    public static final String BUNDLE_UBD_SCENE_ID = "sceneId";

    public static final String BUNDLE_UBD_APP_POINTED_ID = "appPointedId";

    public static final String BUNDLE_PBS_RECOMMEND_TYEP = "pbs_recommendType";

    public static final String BUNDLE_PBS_SCENE_ID = "pbs_sceneId";

    public static final String BUNDLE_PBS_APP_POINTED_ID = "pbs_appPointedId";

    private String pbs_recommendType = "";

    private String pbs_sceneId = "";

    private String pbs_appPointedId = "";


    private boolean isFromMainActivity = false;

    private int activity_state;

    private final int activity_pause = 111111;

    //????????????
    private ControlKeyDownListener mKeyListener;

    //?????????fragment
    private NewBrowseTVPlayFragment onDemandTVPlayFragment;

    //???????????????????????????????????????
    private int windowCount = 0;

    //????????????????????????????????? 0?????? 1??????
    private int windowState = 0;

    //???????????????????????????????????????fragment?????????????????????????????????????????????????????????????????????
    private boolean canScroll = false;

    //????????????????????????????????????
    private boolean isFirstLoadDetail = true;

    //?????????????????????????????????
    private boolean needFresh = true;
    private boolean clickAd = false;

    //???????????????????????????
    private boolean alreadyShowPushToast = false;

    //?????????
    @BindView(R.id.detail_root_view)
    CustomScrollView detailRootView;

    //????????????????????????
    @BindView(R.id.detail_content_view)
    RelativeLayout detailContentView;

    //vod?????????
    @BindView(R.id.details_description_title)
    TextView mTitleText;

    //?????????????????????????????????
    @BindView(R.id.detail_pbs_mark_first)
    ImageView pbsMarkFirst;
    @BindView(R.id.detail_pbs_mark_second)
    ImageView pbsMarkSecond;
    @BindView(R.id.detail_pbs_mark_third)
    ImageView pbsMarkThird;

    //vod?????????
    @BindView(R.id.details_description_score)
    TextView mScoreText;

    //vod???????????????
    @BindView(R.id.details_description_score_text)
    TextView mScoreTextT;

    //vod???????????????
    @BindView(R.id.details_description_info)
    TextView mInfoText;

    //vod???????????????????????????????????????????????????
    @BindView(R.id.details_description_info_font)
    ImageView mInfoTextFont;

    //vod???????????????
    @BindView(R.id.details_descriptions)
    DetailVerticalScrollTextView mDescText;


    //???????????????
    @BindView(R.id.comefrom_layout)
    LinearLayout mComefromLayout;
    @BindView(R.id.details_comefrom_layout)
    LinearLayout mDataComefromLayout;
    @BindView(R.id.details_comefrom_source)
    TextView mDataComefromText;
    //???????????????,????????????
    @BindView(R.id.details_comefrom_JM)
    TextView mDataComefromTextJM;

    @BindView(R.id.details_comefrom_poster_layout)
    LinearLayout mDataComefromPosterLayout;
    @BindView(R.id.details_comefrom_poster_source)
    TextView mDataComefromPosterText;
    //???????????????,????????????
    @BindView(R.id.details_comefrom_poster_JM)
    TextView mDataComefromPosterTextJM;

    //??????
    @BindView(R.id.manban_layout)
    LinearLayout manbanLayout;
    //???????????????
    @BindView(R.id.detail_playview_menban)
    ImageView manbanPlayview;
    //???????????????
    @BindView(R.id.detail_poster_menban)
    ImageView manbanPoster;

    //4k???????????????mDataSourceText
    @BindView(R.id.details_4k_warnning)
    TextView m4KWarnning;

    //?????????????????????4k????????????
    @BindView(R.id.playview_4k_tips_layout)
    RelativeLayout mPlayview4KWarnningLayout;
    @BindView(R.id.playview_4k_tips)
    TextView mPlayview4KWarnning;

    //??????GridView
    @BindView(R.id.details_credits)
    HorizontalGridView mCreditList;

    //??????????????????
    @BindView(R.id.detail_root)
    RelativeLayout mBrowseFrameLayout;

    @BindView(R.id.total_episodes)
    HorizontalGridView total_episodes_GridView;

    @BindView(R.id.choose_episodes)
    HorizontalGridView chooseEpisodeList;

    @BindView(R.id.episodes_lly_icon_left)
    ImageView episodesIconLeft;

    @BindView(R.id.episodes_lly_icon_right)
    ImageView episodesIconRight;

    //??????????????????
    @BindView(R.id.detail_description_update)
    TextView updateText;

//    @BindView(R.id.detail_description_update_layout)
//    RelativeLayout updateTextLayout;
//
//    //??????????????????(?????????)
//    @BindView(R.id.detail_description_update_poster_layout)
//    RelativeLayout updateTextPosterLayout;
//    @BindView(R.id.detail_description_update_poster)
//    TextView updateTextPoster;

    @BindView(R.id.episodes_lly)
    RelativeLayout episodes_lly;

    //??????????????????
    @BindView(R.id.play_layout)
    RelativeLayout mplayRel;
    @BindView(R.id.play_layout_icon)
    ImageView mplayRelIcon;
    @BindView(R.id.play_layout_text)
    TextView mplayRelText;

    //??????????????????
    @BindView(R.id.favorate_layout)
    RelativeLayout col_rel;

    //??????icon
    @BindView(R.id.favorate_layout_icon)
    ImageView favIcon;

    //????????????
    @BindView(R.id.favorate_layout_text)
    TextView favText;

    //??????????????????
    @BindView(R.id.buy_layout)
    RelativeLayout buy_rel;

    //??????icon
    @BindView(R.id.buy_layout_icon)
    ImageView buyIcon;

    //????????????
    @BindView(R.id.buy_layout_text)
    TextView buyText;

    //????????????
    @BindView(R.id.advert)
    CustomRoundImageView advertImg;
    @BindView(R.id.advert_layout)
    RelativeLayout advertImgBg;

    //??????????????????
    @BindView(R.id.vod_playview_fullWindow)
    FrameLayout playViewFullWindow;

    //??????????????????
    @BindView(R.id.vod_playview_loading)
    RelativeLayout playViewloading;

    //?????????????????????
    @BindView(R.id.voddetail_playview_layout)
    RelativeLayout playViewLayout;

    @BindView(R.id.frame_playvideo)
    FrameLayoutExt playViewWindow;

    //???????????????tip
    @BindView(R.id.voddetail_playview_tip)
    ImageView playViewTip;

    //?????????????????????????????????tip
    @BindView(R.id.voddetail_playview_skip_tip)
    TextView playViewSkipTip;

    //???????????????????????????tip
    @BindView(R.id.preplay_trysee_tip_layout)
    RelativeLayout preplayTryseeTip;

    //vod??????
    @BindView(R.id.voddetail_playview_poster)
    ImageView playViewPoster;

    @BindView(R.id.detail_poster_right_icon)
    View mRightIcon;

    //HD
    @BindView(R.id.detail_poster_left_icon)
    ImageView mLeftIcon;

    //vip??????
    @BindView(R.id.voddetail_icon_vip)
    ImageView vipImageView;

    //??????????????????
    @BindView(R.id.voddetail_recent)
    LinearLayout recentLayout;
    @BindView(R.id.voddetail_recent_title)
    TextView recentTitle;
    @BindView(R.id.voddetail_recent_recycleview)
    VerticalGridView rencentRecycleview;

    //??????????????????
    @BindView(R.id.voddetail_guess)
    LinearLayout guessLayout;
    @BindView(R.id.voddetail_guess_title)
    TextView guessTitle;
    @BindView(R.id.voddetail_guess_recycleview)
    VerticalGridView guessRecycleview;

    //??????????????????
    @BindView(R.id.detail_list_layout)
    LinearLayout listLayout;
    //??????view,????????????????????????????????????????????????????????????
    @BindView(R.id.clear_view)
    View clearView;



    private List<List<String>> totalEpisodesCount;

    //??????????????????????????????
    private NewDetailPresenter mDetailPresenter;

    //???????????????
    private boolean isCollection;

    private VodDetailHandler mHandler;
    //????????????onResume???????????????vod??????
    private int requestNum = 0;

    //????????????????????????
    private long mLastOnClickTime = 0;
    //??????????????????
    private static final long VALID_TIME = 1000;

    private Scene mFocusScene;

    private Feedback mFeedback;


    private VODDetail mVODDetail;

    public VODDetail getmVODDetail() {
        return mVODDetail;
    }


    //????????????????????????????????????????????????
    private boolean isCanClick = true;

    //??????????????????????????????????????????
    private boolean isFirstLoad = true;

    //???????????????????????????????????????
    private boolean isNeedFullWindow = false;

    //???????????????url
    private String mJiutianTrackerUrl = "";

    //???????????????itemId
    private String mJiutianItemId = "";


    private NewChooseVarietyEpisodeAdapter varietyEpisodeAdapter;

    private NewChooseEpisodeAdapter chooseAdapter;


    private boolean isRefreshBookmark;

    private PlayVodBean playVodBean;

    private NewTotalEpisodeAdapter totalEpisodeAdapter;

    /**
     * ??????????????????????????????
     */
    private boolean isFromSubscribeNotTrySee;


    private String showSubscribedMark;

    private boolean is4KSource = false;

    /**
     * ??????????????????????????????
     */
    private Bookmark oldBookmark;

    //    private GridLayoutManager chooseEpisodeLayoutManager;
    private LinearLayoutManager chooseEpisodeLayoutManager;

    private static final long MIN_CLICK_INTERVAL = 350L;

    //??????????????????
    private boolean posterIsCreated;

    private static String topicId;

    private final FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(FocusHighlight.ZOOM_FACTOR_SMALL);


    //[UBD]????????????ID,?????????????????? ??????=>REC_BUY
    private String ubd_appPointedId;
    //[UBD]??????ID  ??????????????????????????????sceneId      ????????????????????????????????????subjectId
    private String ubd_sceneId;
    //[UBD]???????????? ??????????????????????????????identifyType ???????????????-1
    private String ubd_recommendType;

    public String getUbd_appPointedId() {
        return ubd_appPointedId;
    }

    public String getUbd_sceneId() {
        return ubd_sceneId;
    }

    public String getUbd_recommendType() {
        return ubd_recommendType;
    }

    private VoddetailEpsiodesUtils voddetailEpsiodesUtils;

    private static class VodDetailHandler extends Handler {

        private final WeakReference<NewVodDetailActivity> activity;

        public VodDetailHandler(NewVodDetailActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                if (null != activity && null != activity.get()) {
                    if (msg.what == RequestDetail) {
                        String vodId;
                        try {
                            vodId = activity.get().getIntent().getStringExtra(VOD_ID);
                            if (TextUtils.isEmpty(vodId) && null != activity.get().getIntent() && null != activity.get().getIntent().getData()) {
                                vodId = activity.get().getIntent().getData().getQueryParameter("vodid");
                            }

                            activity.get().voddetailEpsiodesUtils = new VoddetailEpsiodesUtils(vodId);
                            SuperLog.debug(TAG, "orderFinish: ????????????????????????");
                            activity.get().voddetailEpsiodesUtils.getSimpleVod(vodId, new VoddetailEpsiodePresenter.GetSimpleVodCallback() {
                                @Override
                                public void getSimpleVodSuccess(VODDetail vodDetail) {
                                    SuperLog.debug(TAG, "orderFinish: ??????????????????????????????");
                                    if (null == activity || null == activity.get()) {
                                        return;
                                    }
                                    activity.get().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (null != activity && null != activity.get() && null != activity.get().mDetailPresenter) {
                                                activity.get().mDetailPresenter.setVODDetail(vodDetail);
                                                activity.get().showDetail(vodDetail, "", null);
                                            }
                                        }
                                    });

                                }

                                @Override
                                public void getSimpleVodFail() {
                                }
                            });

                            activity.get().requestNum++;
                        } catch (Exception ex) {
                            SuperLog.error(TAG, ex);
                        }
                    } else if (msg.what == EPSIODE_REQUESTFOCUS) {
                        if (activity.get().chooseEpisodeList.getAdapter() instanceof NewChooseVarietyEpisodeAdapter) {
                            int position = ((NewChooseVarietyEpisodeAdapter) activity.get().chooseEpisodeList.getAdapter()).getBookmarkPosition();
                            if (position != -1) {
                                View view = activity.get().chooseEpisodeList.getLayoutManager().findViewByPosition(position);
                                if (view == null) {
                                    view = activity.get().chooseEpisodeList.findViewHolderForLayoutPosition(position).itemView;
                                }
                                view.setFocusable(true);
                                view.requestFocus();
                            }
                        } else if (activity.get().chooseEpisodeList.getAdapter() instanceof ChooseEpisodeAdapter) {
                            int position = ((ChooseEpisodeAdapter) activity.get().chooseEpisodeList.getAdapter()).getBookmarkPosition();
                            if (position != -1) {
                                View view = activity.get().chooseEpisodeList.getLayoutManager().findViewByPosition(position);
                                if (view == null) {
                                    view = activity.get().chooseEpisodeList.findViewHolderForLayoutPosition(position).itemView;
                                }
                                view.setFocusable(true);
                                view.requestFocus();
                            }
                        }


                    } else if (msg.what == REFRESH_EPSIODE_CANCLICK) {
                        activity.get().isCanClick = true;
                    }
                }
            } catch (Exception e) {
                SuperLog.error(TAG, e);
            }
        }
    }

    public void setOnControlKeyDownListener(ControlKeyDownListener listener) {
        this.mKeyListener = listener;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            savedInstanceState.remove("android:support:fragments");  //??????????????????Activity????????????android:fragments??? ????????????super.onCreate????????????????????????
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_voddetail_layout_new);
        mFeedback = new Feedback(this);
        setmUnBinder(ButterKnife.bind(this));
        mHandler = new VodDetailHandler(this);
        mDetailPresenter = new NewDetailPresenter(this);
        mDetailPresenter.setDetailDataView(this);
        SuperLog.debug(TAG, "onCreate->isClickEpisode:" + mDetailPresenter.isClickEpisode());
        getIntentInfo();
        String needFullwindowStr = getIntent().getStringExtra(NEED_FULL_WINDOW);
        if (!TextUtils.isEmpty(needFullwindowStr) && needFullwindowStr.equals("1")) {
            isNeedFullWindow = true;
        }

        String jiutianTrackerUrl = getIntent().getStringExtra(JIUTIAN_TRACKER_URL);
        if (!TextUtils.isEmpty(jiutianTrackerUrl)){
            this.mJiutianTrackerUrl = jiutianTrackerUrl;
        }else{
            this.mJiutianTrackerUrl = "";
        }

        String jiutianItemId = getIntent().getStringExtra(JIUTIAN_ITEM_ID);
        if (!TextUtils.isEmpty(jiutianItemId)){
            this.mJiutianItemId = jiutianItemId;
        }else{
            this.mJiutianItemId = "";
        }


        //???????????????????????????????????????
        handlerOldDetail();
        ActivityStackControlUtil.add(new WeakReference<>(this));
        initView();
        //???????????????????????????????????????vod,?????????????????????
        Serializable obj = null;
        Serializable bookobj = null;
        String subjectId = "";
        try {

            obj = getIntent().getSerializableExtra(ORGIN_VOD);
            bookobj = getIntent().getSerializableExtra(OLD_BOOKMARK);
            subjectId = getIntent().getStringExtra(SUBJECT_ID);

            ubd_sceneId = getIntent().getStringExtra(BUNDLE_UBD_SCENE_ID);
            ubd_appPointedId = getIntent().getStringExtra(BUNDLE_UBD_APP_POINTED_ID);
            ubd_recommendType = getIntent().getStringExtra(BUNDLE_UBD_RECOMMEND_TYEP);

            pbs_recommendType = getIntent().getStringExtra(BUNDLE_PBS_RECOMMEND_TYEP);
            pbs_sceneId = getIntent().getStringExtra(BUNDLE_PBS_SCENE_ID);
            pbs_appPointedId = getIntent().getStringExtra(BUNDLE_PBS_APP_POINTED_ID);

            isFromMainActivity = getIntent().getBooleanExtra(FROM_MAIN,false);

            showSubscribedMark = LauncherService.getInstance().getLauncher().getExtraData().get(Constant.SHOW_SUBSCRIBERD_MARK);
        } catch (Exception e) {
            SuperLog.error(TAG, e);
        }
        if (!TextUtils.isEmpty(subjectId)) {
            mDetailPresenter.setSubjectId(subjectId);
        }

        if (null != bookobj) {
            oldBookmark = (Bookmark) bookobj;
        }
        mDescText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mDescText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                } else {
                    mDescText.setEllipsize(TextUtils.TruncateAt.END);
                }
            }
        });

        View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mFocusHighlight != null) {
                    mFocusHighlight.onItemFocused(v, hasFocus);
                }
                if (hasFocus) {
                    Log.i(TAG, "onKeyDown: ????????????");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (null != detailRootView) {
                                detailRootView.scrollTo(0, 0);
                            }
                        }
                    }, 50);
                    if (null != onDemandTVPlayFragment && onDemandTVPlayFragment.alreadyStart) {
                        Log.i(TAG, "resumePlay:4 ");
                        onDemandTVPlayFragment.resumePlay();
                        playViewLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        ClickUtil.setInterval(TAG, MIN_CLICK_INTERVAL);

        if (mFocusHighlight != null) {
            mFocusHighlight.onInitializeView(mplayRel);
            mFocusHighlight.onInitializeView(col_rel);
            mFocusHighlight.onInitializeView(buy_rel);

            mplayRel.setOnFocusChangeListener(listener);
            col_rel.setOnFocusChangeListener(listener);
            buy_rel.setOnFocusChangeListener(listener);
        }

        playViewLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.i(TAG, "onKeyDown: ??????????????????");
                    if (isFinishing()) {
                        return;
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (null != detailRootView) {
                                detailRootView.scrollTo(0, 0);
                            }
                        }
                    }, 50);
                    if (null != onDemandTVPlayFragment && !onDemandTVPlayFragment.isPlayBackViewShowing()) {
                        if (activity_state != activity_pause) {
                            Log.i(TAG, "resumePlay:3 ");
                            onDemandTVPlayFragment.resumePlay();
                        }
                    }
                }
            }
        });

        playViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canScroll) {
                    return;
                }
                focusedCount = 0;
                switchWindow(1);
                //??????????????????
                playViewFullWindow.setVisibility(View.GONE);
            }
        });

        if (isNeedFullWindow) {
            //???????????????????????????????????????????????????
            playViewFullWindow.setVisibility(View.VISIBLE);
        }

        detailRootView.setOnScrollListener(new CustomScrollView.OnScrollListener() {
            @Override
            public void onScrolled(int l, int t, int oldl, int oldt) {
                int ScreenHeight = DensityUtil.getScreenHeight(NewVodDetailActivity.this);
                Log.i(TAG, "onScrolled: l " + l + " t " + t + " oldl " + oldl + " oldt " + oldt + " Y???" + (listLayout.getY() + (int) guessLayout.getY()) + " ScreenHeight: " + ScreenHeight);
                if (t > 0) {
                    if (null != onDemandTVPlayFragment && null != onDemandTVPlayFragment.mPlayView) {
                        onDemandTVPlayFragment.setSurfaceViewSize(0, 0);
                        playViewWindow.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (null != onDemandTVPlayFragment && null != onDemandTVPlayFragment.mPlayView) {
                        onDemandTVPlayFragment.setSurfaceViewSize(getResources().getDimension(R.dimen.new_voddetail_playview_width), getResources().getDimension(R.dimen.new_voddetail_playview_height));
                        playViewWindow.setVisibility(View.VISIBLE);
                    }
                }

                //????????????+???????????? > ?????????????????????Y???????????????????????????????????????????????????????????????
                if (t + ScreenHeight > (listLayout.getY() + (int) guessLayout.getY())) {
                    Log.i(TAG, "onScrolled: ?????????????????????");
                    //?????????????????????????????????????????????????????????
                    if (guessLayout.getVisibility() == View.VISIBLE) {
                        //?????????????????????????????????????????????
                        if (!isGuessRecord) {
                            //??????????????????
                            isGuessRecord = true;
                            PbsUaService.report(Detail.getRecommendData(PbsUaConstant.ActionType.IMPRESSION, mVODDetail.getID(), PbsUaConstant.AppointedId.GUESS, guessType, PbsUaConstant.sceneId_guess, UBDRecommendImpression.getRecommendContentIDs(guessList), guessIdentifyType));
                            if (!TextUtils.isEmpty(guessTracker)){
                                //??????????????????
                                SuperLog.debug(TAG,"jiutian display guess");
                                JiutianService.reportDisplayInDetail(guessTracker,JiutianService.getJiutianRecommendContentIDs(guessList),getJiutianItemId());
                            }
                        }
                    }
                }
            }
        });

        detailRootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        //?????????????????????????????????????????????
        playViewWindow.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (windowState == 1 && keyCode == KeyEvent.KEYCODE_DPAD_DOWN && null != onDemandTVPlayFragment) {
                    onDemandTVPlayFragment.onKeyDown(keyCode, event);
                    return true;
                }
                return false;
            }
        });
    }

    //???????????????????????????????????????,???????????????????????????
    private void handlerOldDetail() {
        List<WeakReference<Activity>> activityList = ActivityStackControlUtil.getActivityList();
        if (null != activityList && !activityList.isEmpty()) {
            for (int i = activityList.size() - 1; i >= 0; i--) {
                if (activityList.get(i).get() instanceof NewVodDetailActivity) {
                    NewVodDetailActivity activity = (NewVodDetailActivity) activityList.get(i).get();
                    if (activity.canPrePlay() || null != activity.onDemandTVPlayFragment) {
                        //TODO ??????????????????????????????release?????????????????????????????????????????????????????????????????????????????????
                        //TODO ???????????????release???????????????????????????????????????
                        if (null != activity.onDemandTVPlayFragment && null != activity.onDemandTVPlayFragment.mPlayView) {
                            activity.onDemandTVPlayFragment.mPlayView.pausePlay();
                            //???VOD???????????????????????????????????????????????????????????????onDestory????????????0???????????????????????????????????????
                            // ???onDestroy??????????????????????????????????????????onDestory????????????
                            activity.onDemandTVPlayFragment.reportBookmark(BookMarkSwitchs.DESTORY);
                            activity.onDemandTVPlayFragment.setSwitchVOD(true);
                            activity.onDemandTVPlayFragment.mPlayView.releasePlayer();
                        }
                        activity.finish();
                    }
                } else if (activityList.get(i).get() instanceof LiveTVActivity) {
                    LiveTVActivity activity = (LiveTVActivity) activityList.get(i).get();
                    activity.finish();
                }
            }
        }
    }

    //?????????????????????????????????super??????
    @Override
    @SuppressLint("MissingSuperCall")
    protected void onSaveInstanceState(Bundle outState) {
    }

    @SuppressLint("RestrictedApi")
    public void initView() {
        clearView.setNextFocusRightId(R.id.clear_view);
        mTitleText.setSelected(true);
        mDescText.setNextFocusDownId(R.id.play_rel);
        mDescText.setFocusable(false);
        mScoreText.setFocusable(true);
        mScoreText.setSelected(true);
        mScoreText.requestFocus();
        rencentRecycleview.addItemDecoration(new SpaceItemDecoration(NewVodDetailActivity.this, getResources().getDimensionPixelOffset(R.dimen.margin_7)));
        rencentRecycleview.setNumColumns(6);
        guessRecycleview.addItemDecoration(new SpaceItemDecoration(NewVodDetailActivity.this, getResources().getDimensionPixelOffset(R.dimen.margin_7)));
        guessRecycleview.setNumColumns(6);

    }

    //??????vod????????????????????????????????????????????????
    private void setButtonInfo() {
        if (mplayRel != null) {
            mplayRel.setVisibility(View.GONE);
        }

        if (col_rel != null) {
            col_rel.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        playViewLayout.setVisibility(View.VISIBLE);
        int activity_Resume = 111110;
        activity_state = activity_Resume;
        SuperLog.debug(TAG, "onResume->requestNum:" + requestNum);
        if (clickAd && null != onDemandTVPlayFragment) {
            onDemandTVPlayFragment.setSurfaceViewSize(getResources().getDimension(R.dimen.new_voddetail_playview_width), getResources().getDimension(R.dimen.new_voddetail_playview_height));
            clickAd = false;
        }
        if (needFresh) {
            //?????????????????????????????????????????????
            if (requestNum == 0) {
                mHandler.sendEmptyMessage(RequestDetail);
            }
            if (isRefreshBookmark && null != playVodBean && null != mVODDetail) {
                String vodType = mVODDetail.getVODType();
                if (vodType.equals("0") || vodType.equals("2") || mVODDetail.getEpisodes() == null || mVODDetail.getEpisodes().size() == 0) {
                    mDetailPresenter.setClickEpisode(false);
                } else {
                    mDetailPresenter.setClickEpisode(true);
                }

                refreshBookmark(playVodBean);
                isRefreshBookmark = false;
                playVodBean = null;
            }
        } else {
            needFresh = true;
        }


        startXiri();
        //??????????????????,????????????????????????????????????????????????????????????????????????????????????
        //UBDPlay.cachePlaySource(UBDSwitch.getInstance().getFromActivity());

        //????????????????????????????????????,???????????????????????????,??????????????????(??????????????????????????????,??????????????????????????????),??????????????????????????????,?????????????????????vodDetail??????,???????????????
        if (null != mVODDetail) {
            //?????????????????????????????????????????????????????????????????????,??????????????????????????????null
            UBDSwitch.getInstance().reportInVodDetailActivity(mVODDetail.getName(), mVODDetail.getID(), mVODDetail.getContentType(), null, null, null,false);
        }

        if (playViewWindow.getVisibility() == View.INVISIBLE) {
            playViewWindow.setVisibility(View.VISIBLE);
        }

        if (!isFromSubscribeNotTrySee && null != onDemandTVPlayFragment && onDemandTVPlayFragment.isCanReplayByTrySee()) {
            if (!(onDemandTVPlayFragment.isPlayBackViewShowing())) {
                Log.i(TAG, "resumePlay:2 ");
                if (!onDemandTVPlayFragment.isFromStop()) {
                    onDemandTVPlayFragment.resumePlay();
                }
            }
        }
    }

    @Override
    public Context context() {
        return this;
    }

    @Override
    public void showNoContent() {
    }

    @Override
    public void showError(String message) {
    }

    //??????vod???????????????
    @Override
    public void showDetail(final VODDetail vodDetail, String actionId, List<RecmContents> recmContentsList) {
        //TODO  ????????????demo?????????????????????
        if (null == listLayout) {
            return;
        }

        //??????????????????????????????????????????????????????
        if (null != oldBookmark) {
            vodDetail.setBookmark(oldBookmark);
        }
        mVODDetail = vodDetail;
//        initDetail(vodDetail);
        configDetail(vodDetail);

        if (!isFromSubscribeNotTrySee) {
            //?????????????????????
            VodDetailRecPresenter presenter = new VodDetailRecPresenter(mVODDetail);
            presenter.getVoddetailRecList(new VodDetailRecPresenter.GetRecCallback() {
                @Override
                public void getRecSuccess(List<VOD> relatedVods, List<VOD> guessVods, String relatedType, String guessType, String relatedIdentifyType, String guessTypeIdentifyType,String relatedTracker,String guessTracker) {
                    Log.i(TAG, "getRecSuccess: " + relatedVods.size());
                    Log.i(TAG, "getRecSuccess: " + guessVods.size());
                    Log.i(TAG, "getRecSuccess: null != relatedTracker "+ (!TextUtils.isEmpty(relatedTracker)));
                    Log.i(TAG, "getRecSuccess: null != guessTracker "+ (!TextUtils.isEmpty(guessTracker)));
                    if (null == recentLayout || null == guessLayout) {
                        return;
                    }
                    Log.i(TAG, "getRecSuccess: " + relatedVods.size());
                    Log.i(TAG, "getRecSuccess: " + guessVods.size());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String relatedSubjectId = "";
                            if ("0".equals(relatedType)) {
                                relatedSubjectId = PbsUaConstant.SceneId.RELATION;
                            } else {
                                relatedSubjectId = presenter.getRelatedSubjectId();
                            }
                            String guessSubjectId = "";
                            if ("0".equals(guessType)) {
                                guessSubjectId = PbsUaConstant.SceneId.GUESS;
                            } else {
                                guessSubjectId = presenter.getGuessSubjectId();
                            }
                            initListLayout(relatedVods, guessVods, relatedType, guessType, relatedSubjectId, guessSubjectId, relatedIdentifyType, guessTypeIdentifyType,relatedTracker,guessTracker);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (null == listLayout) {
                                        return;
                                    }
                                    float minHeight = getResources().getDimension(R.dimen.new_voddetail_list_height_min);
                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) listLayout.getLayoutParams();
                                    float height = listLayout.getMeasuredHeight();
                                    float maxHeight = getResources().getDimension(R.dimen.new_voddetail_list_height_max);
                                    Log.i(TAG, "onEvent:  " + height + " " + minHeight);
                                    if (height > minHeight && height < maxHeight) {
                                        Log.i(TAG, "showDetail: ???????????????");
                                        params.height = (int) getResources().getDimension(R.dimen.new_voddetail_list_height_max);
                                        listLayout.setLayoutParams(params);
                                    }
                                }
                            }, 100);
                        }
                    });
                }
            });


//            NewVoddetailPresenter presenter = new NewVoddetailPresenter();
//            presenter.getRecommend(mVODDetail.getID(),getFirstCast(mVODDetail), this,new NewVoddetailPresenter.GetRecommedCallback() {
//                @Override
//                public void getRecommendSuccess(List<VOD> recentList, List<VOD> recommendList, List<Content> actorRecommendList) {
//                    if (null == recentLayout || null == guessLayout){
//                        return;
//                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (recentList.size() <= 4 && recommendList.size() <= 6){
//                                initListLayout(recentList,recommendList,actorRecommendList);
//                            }else{
//                                List<VOD> recentListSub = new ArrayList<>();
//                                if (recentList.size() > 4){
//                                    recentListSub = recentList.subList(0,4);
//                                }else{
//                                    recentListSub = recentList;
//                                }
//                                List<VOD> recommendListSub = new ArrayList<>();
//                                if (recommendList.size() > 6){
//                                    recommendListSub = recommendList.subList(0,6);
//                                }else{
//                                    recommendListSub = recommendList;
//                                }
//                                initListLayout(recentListSub,recommendListSub,actorRecommendList);
//                            }
//
//                            Handler handler = new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (null == listLayout){
//                                        return;
//                                    }
//                                    float minHeight = getResources().getDimension(R.dimen.new_voddetail_list_height_min);
//                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) listLayout.getLayoutParams();
//                                    float height = listLayout.getMeasuredHeight();
//                                    Log.i(TAG, "onEvent:  "+ height);
//                                    float maxHeight = getResources().getDimension(R.dimen.new_voddetail_list_height_max);
//                                    if (height > minHeight && height < maxHeight){
//                                        Log.i(TAG, "showDetail: ???????????????");
//                                        params.height = (int) getResources().getDimension(R.dimen.new_voddetail_list_height_max);
//                                        listLayout.setLayoutParams(params);
//                                    }
//                                }
//                            },100);
//                        }
//                    });
//                }
//            });
        }

        if (TopicActivity.class.getSimpleName().equalsIgnoreCase(UBDSwitch.getInstance().getFromActivity())) {
            topicId = TopicActivity.getTopicId();
        }

        UBDSwitch.getInstance().reportInVodDetailActivity(vodDetail.getName(), vodDetail.getID(), vodDetail.getContentType(), ubd_recommendType, ubd_sceneId, ubd_appPointedId,isFromMainActivity);
    }

    public static String getTopicId() {
        return topicId;
    }

    //??????????????????
    private List<VOD> relatedList = new ArrayList<>();
    //??????????????????
    private List<VOD> guessList = new ArrayList<>();
    //?????????????????????????????????,????????????????????????
    private String guessSubjectId = "";
    private String guessType = "";
    private String guessIdentifyType = "";

    //??????????????????
    private boolean isGuessRecord = false;

    //???????????????trackerUrl ?????????????????????????????????
    private String relatedTracker = "";
    private String guessTracker = "";

    private void initListLayout(List<VOD> recentList, List<VOD> recommendList, String relatedType, String guessType, String relatedSubjectID, String guessSubjectID, String relatedIdentifyType, String guessTypeIdentifyType,String relatedTracker,String guessTracker) {
        if (isFinishing() || null == chooseEpisodeList) {
            return;
        }
        isGuessRecord = false;
        this.relatedList = recentList;
        this.guessList = recommendList;
        this.guessSubjectId = guessSubjectID;
        this.guessIdentifyType = guessTypeIdentifyType;
        this.guessType = guessType;
        this.relatedTracker = relatedTracker;
        this.guessTracker = guessTracker;
        int index = 0;
        if (chooseEpisodeList.getAdapter() != null) {
            index = 1;
        }

        index = 0;
        if (null != recentList && recentList.size() > 0) {
            //??????????????????
            recentLayout.setVisibility(View.VISIBLE);

            Typeface typeface = OTTApplication.getTypeFace();
            if (typeface != null) {
                recentTitle.setTypeface(typeface);
            }

//            LinearLayoutManager manager = new LinearLayoutManager(NewVodDetailActivity.this);
//            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
//            rencentRecycleview.setLayoutManager(manager);

            NewVoddetailRecListAdapter adapter = new NewVoddetailRecListAdapter(NewVodDetailActivity.this, recentList, index, hasEpisodes());
//            NewVoddetailListWithoutTitleAdapter adapter = new NewVoddetailListWithoutTitleAdapter(NewVodDetailActivity.this,recentList,index,hasEpisodes());
            adapter.setListenr(new NewVoddetailRecListAdapter.onClickListenr() {
                @Override
                public void onClick(boolean ismigu, VOD vod) {
                    //pbs??????
                    PbsUaService.report(Detail.getRecommendData(PbsUaConstant.ActionType.CLICK, vod.getID(), PbsUaConstant.AppointedId.RELATION, relatedType, PbsUaConstant.sceneId_rec, UBDRecommendImpression.getRecommendContentIDs(recentList), relatedIdentifyType));
                    if (ismigu) {
                        Log.i(TAG, "onClick: ?????????");
                        MiguQRViewPopWindow popWindow = new MiguQRViewPopWindow(NewVodDetailActivity.this, mVODDetail.getCode(), MiguQRViewPopWindow.mSearchResultType);
                        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.root);
                        popWindow.showPopupWindow(rootLayout);
                        return;
                    }

                    Intent intent = new Intent(NewVodDetailActivity.this, NewVodDetailActivity.class);
                    if (null != vod.getFeedback()){
                        if (!TextUtils.isEmpty(vod.getFeedback().getPlay_tracker())){
                            intent.putExtra(NewVodDetailActivity.JIUTIAN_TRACKER_URL, vod.getFeedback().getPlay_tracker());
                        }
                        if (!TextUtils.isEmpty(vod.getItemid())){
                            intent.putExtra(NewVodDetailActivity.JIUTIAN_ITEM_ID, vod.getItemid());
                        }
                        if (!TextUtils.isEmpty(vod.getFeedback().getClick_tracker())){
                            //??????????????????
                            SuperLog.debug(TAG,"jiutian click");
                            JiutianService.reportClick(vod.getFeedback().getClick_tracker());
                        }
                    }
                    intent.putExtra(NewVodDetailActivity.VOD_ID, vod.getID());
                    intent.putExtra(NewVodDetailActivity.BUNDLE_UBD_APP_POINTED_ID, RemixRecommendUtil.APPPINEDID_VOD);
                    intent.putExtra(NewVodDetailActivity.BUNDLE_UBD_RECOMMEND_TYEP, UBDRecommendImpression.recommendType_vod);
                    intent.putExtra(NewVodDetailActivity.BUNDLE_UBD_SCENE_ID, UBDRecommendImpression.sceneId_vod);

                    intent.putExtra(NewVodDetailActivity.BUNDLE_PBS_APP_POINTED_ID, VodDetailRecPresenter.RELATEDID);
                    intent.putExtra(NewVodDetailActivity.BUNDLE_PBS_RECOMMEND_TYEP, relatedType);
                    intent.putExtra(NewVodDetailActivity.BUNDLE_PBS_SCENE_ID, PbsUaConstant.sceneId_rec);
                    startActivity(intent);

                    finish();
                }
            });
            rencentRecycleview.setAdapter(adapter);

            index++;
        }

        if (null != recommendList && recommendList.size() > 0) {
            guessLayout.setVisibility(View.VISIBLE);

            Typeface typeface = OTTApplication.getTypeFace();
            if (typeface != null) {
                guessTitle.setTypeface(typeface);
            }

//            LinearLayoutManager manager = new LinearLayoutManager(NewVodDetailActivity.this);
//            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
//            guessRecycleview.setLayoutManager(manager);
            NewVoddetailRecListAdapter adapter = new NewVoddetailRecListAdapter(NewVodDetailActivity.this, recommendList, index, hasEpisodes());
            //?????????????????????/??????????????????
            String recommendContentIDs = UBDRecommendImpression.getRecommendContentIDs(recommendList);
            UBDRecommendImpression.record(RemixRecommendUtil.APPPINEDID_VOD, UBDRecommendImpression.sceneId_vod, recommendContentIDs, UBDRecommendImpression.recommendType_vod, null, mVODDetail.getID());
            adapter.setListenr(new NewVoddetailRecListAdapter.onClickListenr() {
                @Override
                public void onClick(boolean ismigu, VOD vod) {
                    //pbs??????
                    PbsUaService.report(Detail.getRecommendData(PbsUaConstant.ActionType.CLICK, vod.getID(), PbsUaConstant.AppointedId.GUESS, guessType, PbsUaConstant.sceneId_guess, UBDRecommendImpression.getRecommendContentIDs(recommendList), guessTypeIdentifyType));

                    if (ismigu) {
                        MiguQRViewPopWindow popWindow = new MiguQRViewPopWindow(NewVodDetailActivity.this, mVODDetail.getCode(), MiguQRViewPopWindow.mSearchResultType);
                        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.root);
                        popWindow.showPopupWindow(rootLayout);
                        return;
                    }

                    finish();

                    Intent intent = new Intent(NewVodDetailActivity.this, NewVodDetailActivity.class);

                    if (null != vod.getFeedback()){
                        if (!TextUtils.isEmpty(vod.getFeedback().getPlay_tracker())){
                            intent.putExtra(NewVodDetailActivity.JIUTIAN_TRACKER_URL, vod.getFeedback().getPlay_tracker());
                        }
                        if (!TextUtils.isEmpty(vod.getFeedback().getClick_tracker())){
                            //??????????????????
                            SuperLog.debug(TAG,"jiutian click");
                            JiutianService.reportClick(vod.getFeedback().getClick_tracker());
                        }
                        if (!TextUtils.isEmpty(vod.getItemid())){
                            intent.putExtra(NewVodDetailActivity.JIUTIAN_ITEM_ID, vod.getItemid());
                        }
                    }

                    intent.putExtra(NewVodDetailActivity.VOD_ID, vod.getID());
                    intent.putExtra(NewVodDetailActivity.BUNDLE_UBD_APP_POINTED_ID, RemixRecommendUtil.APPPINEDID_VOD);
                    intent.putExtra(NewVodDetailActivity.BUNDLE_UBD_RECOMMEND_TYEP, UBDRecommendImpression.recommendType_vod);
                    intent.putExtra(NewVodDetailActivity.BUNDLE_UBD_SCENE_ID, UBDRecommendImpression.sceneId_vod);

                    intent.putExtra(NewVodDetailActivity.BUNDLE_PBS_APP_POINTED_ID, VodDetailRecPresenter.GUESSID);
                    intent.putExtra(NewVodDetailActivity.BUNDLE_PBS_RECOMMEND_TYEP, guessType);
                    intent.putExtra(NewVodDetailActivity.BUNDLE_PBS_SCENE_ID, PbsUaConstant.sceneId_guess);
                    startActivity(intent);
                }
            });
            guessRecycleview.setAdapter(adapter);
            index++;
        }

        //PBS????????????
        //?????????????????????????????????????????????????????????????????????
        if (recentLayout.getVisibility() == View.VISIBLE && null != recentList && recentList.size() > 0) {
            //??????????????????????????????????????????
            PbsUaService.report(Detail.getRecommendData(PbsUaConstant.ActionType.IMPRESSION, mVODDetail.getID(), PbsUaConstant.AppointedId.RELATION, relatedType, PbsUaConstant.sceneId_rec, UBDRecommendImpression.getRecommendContentIDs(recentList), relatedIdentifyType));
            if (!TextUtils.isEmpty(this.relatedTracker)){
                //??????????????????
                SuperLog.debug(TAG,"jiutian display related");
                JiutianService.reportDisplayInDetail(this.relatedTracker,JiutianService.getJiutianRecommendContentIDs(recentList),getJiutianItemId());
            }
        } else if (guessLayout.getVisibility() == View.VISIBLE && null != recommendList && recommendList.size() > 0) {
            //??????????????????????????????????????????????????????????????????
            isGuessRecord = true;
            PbsUaService.report(Detail.getRecommendData(PbsUaConstant.ActionType.IMPRESSION, mVODDetail.getID(), PbsUaConstant.AppointedId.GUESS, guessType, PbsUaConstant.sceneId_guess, UBDRecommendImpression.getRecommendContentIDs(recommendList), relatedIdentifyType));
            if (!TextUtils.isEmpty(this.guessTracker)){
                //??????????????????
                SuperLog.debug(TAG,"jiutian display guess");
                JiutianService.reportDisplayInDetail(this.guessTracker,JiutianService.getJiutianRecommendContentIDs(recommendList),getJiutianItemId());
            }
        }
    }

    //???????????????vod????????????????????????
    private String getFirstCast(VODDetail vodDetail) {
        List<CastRole> castRoles = vodDetail.getCastRoles();
        Log.i(TAG, "getFirstCast:  " + JsonParse.object2String(castRoles));
        if (null != castRoles && castRoles.size() > 0) {
            for (int i = 0; i < castRoles.size(); i++) {
                CastRole castRole = castRoles.get(i);
                List<Cast> casts = castRole.getCasts();
                if (casts != null && casts.size() != 0) {
                    Cast cast = casts.get(0);
                    if (null != cast && !TextUtils.isEmpty(cast.getCastName())) {
                        return cast.getCastName();
                    }
                }
            }
        }
        return "";
    }

    public void authenticateVOd(VODDetail detail) {

        if (null == detail) {
            return;
        }
        setButtonInfo(detail);
        DetailCommonUtils.authenticateVOd(detail, mDetailPresenter, lastPlayUrl, lastPlayID, this);
    }

    private VodDetailPBSConfigUtils vodDetailPBSConfigUtils;

    //??????pbs?????????????????????????????????????????????
    public void configDetail(VOD vod){
        mInfoTextFont.setVisibility(View.GONE);

        vodDetailPBSConfigUtils = new VodDetailPBSConfigUtils(vod.getID());
        vodDetailPBSConfigUtils.initConfig(new VodDetailPBSConfigUtils.ConfigCallBack() {
            @Override
            public void configDone() {
                if (isFinishing()){
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //????????????
                        configMarks();
                        //??????????????????
                        configOrderBtn(vod);
                    }
                });
            }
        });

        initDetail(vod);
    }

    private void configMarks(){
        Log.i(TAG, "vodDetailPBSConfigUtils: "+(null != vodDetailPBSConfigUtils.getSingleMarks()));
        if (null != vodDetailPBSConfigUtils && null != vodDetailPBSConfigUtils.getSingleMarks()
                && vodDetailPBSConfigUtils.getSingleMarks().size() > 0){
            Log.i(TAG, "vodDetailPBSConfigUtils: "+vodDetailPBSConfigUtils.getSingleMarks().size());
            List<SingleMark> singleMarks = new ArrayList<>();
            if (vodDetailPBSConfigUtils.getSingleMarks().size() > 3){
                singleMarks = vodDetailPBSConfigUtils.getSingleMarks().subList(0,3);
            }else{
                singleMarks = vodDetailPBSConfigUtils.getSingleMarks();
            }

            for (int i = 0; i < singleMarks.size(); i++) {
                SingleMark singleMark = singleMarks.get(i);
                String url = singleMark.getIcon();
                switch (i){
                    case 0:{
                        pbsMarkFirst.setVisibility(View.VISIBLE);
                        GlideApp.with(NewVodDetailActivity.this).load(url).into(pbsMarkFirst);
                        mInfoTextFont.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 1:{
                        pbsMarkSecond.setVisibility(View.VISIBLE);
                        GlideApp.with(NewVodDetailActivity.this).load(url).into(pbsMarkSecond);
                        mInfoTextFont.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 2:{
                        pbsMarkThird.setVisibility(View.VISIBLE);
                        GlideApp.with(NewVodDetailActivity.this).load(url).into(pbsMarkThird);
                        mInfoTextFont.setVisibility(View.VISIBLE);
                        break;
                    }
                    default:break;
                }
            }
        }
    }

    private void configOrderBtn(VOD vod){
        if (null != buy_rel && vod instanceof VODDetail) {
            buy_rel.setFocusable(false);

            ShowBuyControl control = new ShowBuyControl(this);

            control.setCallBack(new ShowBuyControl.ShowBuyTagCallBack() {
                @Override
                public void showBuyTag(int showBuy) {
                    if (isFinishing() || null == buy_rel) {
                        return;
                    }
                    Log.i(TAG, "showBuyTag: getSuperScriptByType: detail");
                    switch (showBuy) {
                        case 0:
                            Log.i(TAG, "showBuyTag: 1");
                            buy_rel.setVisibility(View.VISIBLE);
                            buy_rel.setFocusable(true);
                            buy_rel.setEnabled(true);
                            buyIcon.setSelected(false);
                            buyText.setText("??????");
                            buyText.setTextColor(getResources().getColor(R.color.white_0));
                            col_rel.setNextFocusRightId(R.id.buy_rel);
                            break;
                        case 1:
                            buy_rel.setFocusable(false);
                            buy_rel.setVisibility(View.GONE);
                            break;
                        case 2:
                            buy_rel.setVisibility((TextUtils.isEmpty(showSubscribedMark) || "1".equals(showSubscribedMark)) ? View.VISIBLE : View.GONE);
                            buyText.setText("?????????");
                            buyText.setTextColor(getResources().getColor(R.color.voddetail_order_text_color_unenable));
                            buy_rel.setFocusable(false);
                            buy_rel.setEnabled(false);
                            buyIcon.setSelected(true);
                            buy_rel.setOnClickListener(null);
                            break;
                    }
                }
            });

            List<String> productIds = new ArrayList<>();
            String productIdStr = SessionService.getInstance().getSession().getTerminalConfigurationValue(Configuration.Key.CHOI_PRODUCT);

            Log.i(TAG, "ShowBuyControl: " + productIdStr);
            if (!TextUtils.isEmpty(productIdStr)) {
                try {
                    productIds = JsonParse.jsonToStringList(productIdStr);
                } catch (Exception e) {
                    SuperLog.error(TAG, e);
                }
            }
            boolean contains = false;

            if (null != vodDetailPBSConfigUtils && vodDetailPBSConfigUtils.isConfigOrderInfoSuccess()
                    && null != vodDetailPBSConfigUtils.getQuerySTBOrderInfoResponse()
                    && null != vodDetailPBSConfigUtils.getQuerySTBOrderInfoResponse().getSubProductList()){
                String str = JsonParse.object2String(vodDetailPBSConfigUtils.getQuerySTBOrderInfoResponse().getSubProductList());
                for (int i = 0; i < productIds.size(); i++) {
                    String productId = productIds.get(i);
                    if (null != str && str.contains(productId)){
                        contains = true;
                        break;
                    }
                }

                if (contains){
                    Log.i(TAG, "setButtonInfo: vodDetailPBSConfigUtils ?????????????????????");
                    buy_rel.setFocusable(false);
                    buy_rel.setVisibility(View.GONE);
                }else{
                    Log.i(TAG, "setButtonInfo: vodDetailPBSConfigUtils ??????????????????");
                    control.ShowBuyTag((VODDetail) vod);
                }
            }else{
                Log.i(TAG, "setButtonInfo: ??????QuerySTBOrderInfo????????????????????????");
                control.ShowBuyTagBySubscription((VODDetail) vod);
            }
        }

        if (advertImg.getVisibility() == View.VISIBLE && advertImgBg.getVisibility() == View.VISIBLE) {
            //???????????????????????????????????????????????????
            return;
        }

        VodAdUtils utils = new VodAdUtils();
        utils.getVodAd(NewVodDetailActivity.this, mDetailPresenter, mVODDetail, new VodAdUtils.VodAdCallBack() {
            @Override
            public void showAd(int state, String url, AdvertContent advertContent) {
//                state = VodAdUtils.AD_STATE_CAN_CLICK;
//                url = "http://117.148.130.129:7210/PHS/desktop/100343/assets/245aadef-917e-43ae-8502-0b3f4ffa5ab1.jpg";
                switch (state) {
                    case VodAdUtils.AD_STATE_NOT_SHOW: {
                        //??????????????????(????????????,1-??????SSP????????????????????????????????? 2-??????SSP????????????????????????????????????????????????????????????)
                        //??????????????????,???????????????
                        advertImg.setVisibility(View.GONE);
                        advertImgBg.setVisibility(View.GONE);
                        break;
                    }
                    case VodAdUtils.AD_STATE_CANT_CLICK: {
                        //?????????????????????????????????(????????????,1-?????????????????????????????? 2-SSP????????????????????????)
                        if (null == advertContent) {
                            //????????????????????????UBD
                            UBDAdvert.reportConfigBannerImpression(mVODDetail.getID(), mVODDetail.getName(), url, UBDConstant.ActionType.VOD_BANNER_IMPRESSION);
                        } else {
                            //SSP??????????????????SSP??????
                            AdManager.getInstance().reportAdvert(advertContent, AdConstant.AdType.BANNER, AdConstant.ReportActionType.IMPRESSION);
                        }
                        advertImgBg.setVisibility(View.VISIBLE);
                        advertImg.setVisibility(View.VISIBLE);
                        advertImgBg.setFocusable(false);
                        GlideApp.with(NewVodDetailActivity.this).load(url).into(advertImg);
                        break;
                    }
                    case VodAdUtils.AD_STATE_CAN_CLICK: {
                        //?????????????????????????????????(??????????????????,SSP???????????????????????????)
                        advertImg.setVisibility(View.VISIBLE);
                        GlideApp.with(NewVodDetailActivity.this).load(url).into(advertImg);
                        //SSP??????????????????SSP??????
                        AdManager.getInstance().reportAdvert(advertContent, AdConstant.AdType.BANNER, AdConstant.ReportActionType.IMPRESSION);
                        advertImgBg.setVisibility(View.VISIBLE);
                        advertImgBg.setFocusable(true);
                        advertImgBg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!canScroll) {
                                    return;
                                }

                                needFresh = false;
                                if (playViewWindow.getVisibility() == View.VISIBLE && null != onDemandTVPlayFragment) {
                                    clickAd = true;
                                    //????????????surface?????????????????????WebActivity?????????????????????
                                    onDemandTVPlayFragment.pausePlay();
                                    onDemandTVPlayFragment.setSurfaceViewSize(0, 0);
                                }

                                String clickUrl = advertContent.getDisplay().getBanner().getLink().getUrl();
                                Log.i(TAG, "Vod detail SSP banner advert click url is : " + clickUrl);
                                Intent intent = new Intent(NewVodDetailActivity.this, MessageWebViewActivity.class);
                                intent.putExtra(MessageWebViewActivity.LINKURL, clickUrl);
//                                                intent.putExtra("url", clickUrl);
                                startActivity(intent);
                                //SSP??????????????????SSP??????
                                AdManager.getInstance().reportAdvert(advertContent, AdConstant.AdType.BANNER, AdConstant.ReportActionType.CLICK);
                            }
                        });
                        advertImgBg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    advertImgBg.setSelected(true);
                                } else {
                                    advertImgBg.setSelected(false);
                                }
                            }
                        });
                        break;
                    }
                    default:
                }
            }
        });
    }

    //???????????????
    public void initDetail(VOD vod) {
        Log.i(TAG, "initDetail: ??????initdetail");
        playViewLayout.setVisibility(View.VISIBLE);
        detailContentView.setVisibility(View.VISIBLE);
        showPreplayTryseeTip(false);

        boolean canPrePlay = false;

        //?????????????????????
        boolean ismigu = false;
        if (vod instanceof VODDetail) {
            is4KSource = DetailCommonUtils.is4k((VODDetail) vod);
            canPrePlay = DetailCommonUtils.canPlay(is4KSource);
            //?????????????????????
            ismigu = VodUtil.isMiguVod(vod);
        }


        if (canPrePlay && !ismigu) {
            canClick = false;
            playViewWindow.setVisibility(View.VISIBLE);
            playViewTip.setVisibility(View.VISIBLE);
            playViewPoster.setVisibility(View.GONE);
            mLeftIcon.setVisibility(View.GONE);
            mRightIcon.setVisibility(View.GONE);

            playViewLayout.setFocusable(true);

//            mplayRel.setVisibility(View.GONE);
            mplayRel.setVisibility(View.VISIBLE);

            manbanPlayview.setVisibility(View.VISIBLE);
            manbanPoster.setVisibility(View.GONE);
            mDataComefromLayout.setVisibility(View.VISIBLE);
            mDataComefromPosterLayout.setVisibility(View.GONE);
            updateText.setVisibility(View.VISIBLE);
            updateText.setSelected(true);
        } else {
            canClick = true;
            if (vod instanceof VODDetail) {
                canScroll = true;
            }
            playViewWindow.setVisibility(View.GONE);
            playViewTip.setVisibility(View.GONE);
            playViewPoster.setVisibility(View.VISIBLE);
            mLeftIcon.setVisibility(View.VISIBLE);
            mRightIcon.setVisibility(View.VISIBLE);

            playViewLayout.setFocusable(false);

            mplayRel.setVisibility(View.VISIBLE);

            manbanPlayview.setVisibility(View.GONE);
            manbanPoster.setVisibility(View.VISIBLE);
            mDataComefromLayout.setVisibility(View.GONE);
            mDataComefromPosterLayout.setVisibility(View.VISIBLE);

            updateText.setVisibility(View.VISIBLE);
            updateText.setSelected(true);
        }

        manbanLayout.setVisibility(View.VISIBLE);
        mComefromLayout.setVisibility(View.VISIBLE);

        SuperLog.debug(TAG, "VOD ID : " + getIntent().getStringExtra(VOD_ID));
        String name = vod.getName();
        if (!TextUtils.isEmpty(name)) {
            mTitleText.setText(name);
        }

        if (DetailCommonUtils.needShowScore(vod)) {
            String score = vod.getAverageScore();
            if (TextUtils.isEmpty(score) || score.equals("0.0")) {
                score = "7.0";
            }
            if (!TextUtils.isEmpty(score)) {
//                if (!CommonUtil.getDeviceType().equals("EC6109M") && !CommonUtil.getDeviceType().equals("EC6110")){
//                    mScoreText.setTypeface(mScoreText.getTypeface(), Typeface.ITALIC);
//                }
                mScoreText.setText(score);
            }
            mScoreText.setVisibility(View.VISIBLE);
//            mScoreTextT.setVisibility(View.VISIBLE);
        } else {
            mScoreText.setVisibility(View.GONE);
//            mScoreTextT.setVisibility(View.GONE);
        }
        int line = 5;
        VODDetail vodDetail1 = (VODDetail) vod;
        List<CastRole> castRoles = vodDetail1.getCastRoles();
        boolean isShow = ShowCastListUtils.showCastList(mVODDetail);
        if (isShow && null != castRoles && castRoles.size() > 0) {
            //?????????????????????????????????
            line--;
            line--;
        }

        if (!VodUtil.canPlay(is4KSource) && !DetailCommonUtils.canPlayPre()) {
            //???4k???????????????????????????
            line--;
        }
        float density = 1f;
        float desitytem = getResources().getDisplayMetrics().density;
        if (desitytem > 0) {
            density = desitytem;
        }
        int singleLineHeight = 0;
        if (density == 1.0 || CommonUtil.isJMGODevice()) {
            singleLineHeight = (int) (getResources().getDimensionPixelSize(R.dimen.vod_describe_line_one_height_1));
        }
//        else if (CommonUtil.isJMGODevice()){
//            singleLineHeight = (int) (getResources().getDimensionPixelSize(R.dimen.vod_describe_line_one_height)/4*3);
//        }
        else {
            singleLineHeight = (int) (getResources().getDimensionPixelSize(R.dimen.vod_describe_line_one_height) / density);
        }

        int height = singleLineHeight * line;

        Log.i(TAG, "initDetail: " + singleLineHeight + " " + height + " " + density);

        mDescText.setLineCount(line);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mDescText.getLayoutParams();
        params.height = height;
        mDescText.setLayoutParams(params);

        String des = vod.getIntroduce();
        if (TextUtils.isEmpty(des)) {
            des = "????????????";
        }
        if (!TextUtils.isEmpty(des)) {
            String str1 = TextUtil.toDBC(des);
            if (TextUtils.isEmpty(str1)) {
                str1 = "????????????";
            }
            String updateTime = DetailCommonUtils.getUpdateTime(this, vod);

            if (!TextUtils.isEmpty(des)) {

                if (!str1.startsWith("??????")) {
                    str1 = "?????????" + str1;
                }

                if (!TextUtils.isEmpty(updateTime)) {
                    updateTime = updateTime.replace(" ", "");
                    Log.i(TAG, "initDetail: " + updateTime);
                    str1 = "(" + updateTime + ")" + "\n" + str1;
                }

                mDescText.setText(str1);
                mDescText.setFocusable(true);
            }
        }

        //?????? weicy

//        if (!canPrePlay) {
//            showLeftIcon(vod);
//        }

        //??????[??????][??????]
        Picture picture = vod.getPicture();
        if (picture != null && !posterIsCreated && !canPrePlay) {
            String poster = VoddetailUtil.getInstance().getVodBanner(picture);
            if (poster != null) {
                String bgUrl = VoddetailUtil.getInstance().getBackground(picture, poster);
                GlideUtil.load(this, poster, playViewPoster, R.drawable.default_poster238);
                posterIsCreated = true;
            }
        }

        if (vod instanceof VODDetail) {
            VODDetail vodDetail = (VODDetail) vod;
            showSubscriptAndCategoryInfo(vodDetail);
            setButtonListener(vodDetail);
            setButtonInfo(vodDetail);
            setVodEpsiodes(vodDetail);

            if (isFirstLoad) {
                if (canPrePlay) {
                    playViewLayout.requestFocus();
                    mplayRel.setFocusable(true);
                } else if (VodUtil.canPlay(is4KSource)) {
                    mplayRel.setFocusable(true);
                    mplayRel.requestFocus();
                } else {
                    col_rel.setFocusable(true);
                    col_rel.requestFocus();
                }
            }
        } else {
            setButtonInfo();
        }

        mScoreText.setSelected(false);
        mScoreText.setFocusable(false);

        //???????????????????????????
        Spanned updateStr = DetailCommonUtils.getVodUpdateInfo(vod);
        updateText.setText(updateStr);

        if (vod instanceof VODDetail) {
            //????????????????????????
            String text = DetailCommonUtils.getVodInterduce(this, vod);
            if (CommonUtil.isJMGODevice() && !TextUtils.isEmpty(text)) {
                mDataComefromTextJM.setText("???");
                mDataComefromText.setText(text);
                mDataComefromPosterTextJM.setText("???");
                mDataComefromPosterText.setText(text);
            } else {
                mDataComefromTextJM.setText("");
                mDataComefromText.setText(text);
                mDataComefromPosterTextJM.setText("");
                mDataComefromPosterText.setText(text);
            }

//            if (CommonUtil.isJMGODevice()){
////                DetailCommonUtils.setVodInterduce(this,mDataSourceText,vod);
//            }else{
//                mDataSourceText.setText(text);
//            }

            setFavoriteStatus(isCollection = vod.getFavorite() != null);
            //???????????????
            initCastRole(vod.getCastRoles(), vod.getSubjectIDs());
        }

        mplayRel.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (buy_rel.getVisibility() == View.VISIBLE && buy_rel.isFocusable()) {
                        buy_rel.requestFocus();
                    } else if (col_rel.getVisibility() == View.VISIBLE && col_rel.isFocusable()) {
                        col_rel.requestFocus();
                    }
                    return true;
                } else if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    if (episodes_lly.getVisibility() == View.VISIBLE) {
                        if (chooseEpisodeListRequestFocuse(false)) {
                            return true;
                        }
                    } else if (!canScroll) {
                        return true;
                    }
                }

                if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    if (playViewLayout.getVisibility() == View.VISIBLE && playViewLayout.isFocusable()) {
                        playViewLayout.requestFocus();
                        return true;
                    }
                    return true;
                }
                if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    if (mDescText.getVisibility() == View.VISIBLE && mDescText.isFocusable()) {
                        mDescText.requestFocus();
                        return true;
                    }
                }
                return false;
            }
        });

        buy_rel.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (col_rel.isFocusable()) {
                        col_rel.requestFocus();
                    }
                    return true;
                }

                if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    if (mplayRel.getVisibility() == View.VISIBLE && mplayRel.isFocusable()) {
                        mplayRel.requestFocus();
                    } else if (playViewLayout.getVisibility() == View.VISIBLE && playViewLayout.isFocusable()) {
                        playViewLayout.requestFocus();
                    }
                    return true;
                } else if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    if (episodes_lly.getVisibility() == View.VISIBLE) {
                        if (chooseEpisodeListRequestFocuse(false)) {
                            return true;
                        }
                    } else if (!canScroll) {
                        return true;
                    }
                }

                return false;
            }
        });
        advertImgBg.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    return true;
                } else if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    if (episodes_lly.getVisibility() == View.VISIBLE) {
                        if (chooseEpisodeListRequestFocuse(false)) {
                            return true;
                        }
                    } else if (!canScroll) {
                        return true;
                    }
                }
                return false;
            }
        });
        advertImgBg.setNextFocusLeftId(R.id.favorate_layout);

        col_rel.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (advertImgBg.getVisibility() == View.VISIBLE && advertImgBg.isFocusable()) {
                        advertImgBg.requestFocus();
                        return true;
                    }
                    return true;
                } else if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    if (episodes_lly.getVisibility() == View.VISIBLE) {
                        if (chooseEpisodeListRequestFocuse(false)) {
                            return true;
                        }
                    } else if (!canScroll) {
                        return true;
                    }
                }

                if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    if (buy_rel.getVisibility() == View.VISIBLE && buy_rel.isFocusable()) {
                        buy_rel.requestFocus();
                        return true;
                    }
                    if (mplayRel.getVisibility() == View.VISIBLE && mplayRel.isFocusable()) {
                        mplayRel.requestFocus();
                        return true;
                    } else if (playViewLayout.getVisibility() == View.VISIBLE && playViewLayout.isFocusable()) {
                        playViewLayout.requestFocus();
                        return true;
                    }
                    return true;
                }


                return false;
            }
        });


        playViewLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (windowState == 1 && KeyEvent.ACTION_DOWN == event.getAction()) {
                    return onKeyDown(keyCode, event);
                }

                if (windowState == 1 && KeyEvent.ACTION_UP == event.getAction()) {
                    return onKeyUp(keyCode, event);
                }

                if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (mplayRel.getVisibility() == View.VISIBLE && mplayRel.isFocusable()) {
                        mplayRel.requestFocus();
                        return true;
                    } else if (buy_rel.getVisibility() == View.VISIBLE && buy_rel.isFocusable()) {
                        buy_rel.requestFocus();
                        return true;
                    } else if (col_rel.getVisibility() == View.VISIBLE && col_rel.isFocusable()) {
                        col_rel.requestFocus();
                        return true;
                    }
                } else if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    if (episodes_lly.getVisibility() == View.VISIBLE) {
                        if (chooseEpisodeListRequestFocuse(false)) {
                            return true;
                        }
                    } else if (!canScroll) {
                        return true;
                    }
                } else if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    return true;
                }
                return false;
            }
        });

        mDescText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    if (playViewWindow.getVisibility() != View.VISIBLE) {
                        return true;
                    }
                } else if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    return true;
                }
                return false;
            }
        });

//        ???????????????
        if (vod instanceof VODDetail) {
            SuperLog.debug(TAG, "orderFinish: ?????????????????????????????????");
            if (playViewWindow.getVisibility() == View.VISIBLE && isFirstLoadDetail && !isNeedFullWindow) {
                isFirstLoadDetail = false;
                start((VODDetail) vod);
                SuperLog.debug(TAG, "orderFinish: ?????????????????????????????????1");
            } else if (isFromSubscribeNotTrySee) {
                SuperLog.debug(TAG, "orderFinish: ?????????????????????????????????2");
                start((VODDetail) vod);
                isFromSubscribeNotTrySee = false;
            } else if (isNeedFullWindow) {
                SuperLog.debug(TAG, "orderFinish: ?????????????????????????????????3");
                startPlay();
            }
        }

    }

    private void showLeftIcon(VOD vod) {
        String contentURL = SuperScriptUtil.getInstance().getSuperScriptByVod(vod, SuperScriptUtil.SCRIPT_DETAIL_ICON);
        if (!TextUtils.isEmpty(contentURL)) {
            if (contentURL.contains("/") || contentURL.contains("http")) {
                try {
                    String launcherLink = SharedPreferenceUtil.getInstance().getLauncherLink();
                    if (!TextUtils.isEmpty(launcherLink)) {
                        contentURL = "http://" + AuthenticateManager.getInstance().getUserInfo().getIP() + ":" + AuthenticateManager.getInstance().getUserInfo().getPort() + launcherLink + contentURL;
                        // ???????????????????????????????????????
                        contentURL = contentURL + "?version=" + SharedPreferenceUtil.getInstance().getLauncherVersion();
                        //SuperLog.debug(TAG, "launcherLink:" + contentURL);
                        mLeftIcon.setVisibility(View.VISIBLE);
                        SuperLog.info2SD("GlideLoadIPV6", "contentURL is " + contentURL);
                        GlideApp.with(this).load(contentURL).into(mLeftIcon);
                    } else {
                        mLeftIcon.setVisibility(View.GONE);
                    }
                } catch (Exception ex) {
                    mLeftIcon.setVisibility(View.GONE);
                    SuperLog.error(TAG, ex);
                }
            } else {
                mLeftIcon.setVisibility(View.GONE);
            }
        } else {
            mLeftIcon.setVisibility(View.GONE);
        }


    }

    public void setVodEpsiodes(VODDetail vodDetail) {
        Log.i(TAG, "?????????????????????????????? setVodEpsiodes: ");
        Log.i(TAG, "setVodEpsiodes: ");

        String vodType = vodDetail.getVODType();
        if (vodType.equals("0") || vodType.equals("2")) {
            episodes_lly.setVisibility(View.GONE);

        } else {
            chooseEpisodeList.setNumRows(1);
            episodes_lly.setVisibility(View.VISIBLE);
            if (vodType.equals("3") || DetailCommonUtils.isShowSerieslayout(vodDetail.getCmsType())) {
                mDetailPresenter.setIsSeries(0);
                if (DetailCommonUtils.isShowReverselayout(vodDetail.getCmsType())) {
                    mDetailPresenter.setIsReverse(1);
                } else {
                    mDetailPresenter.setIsReverse(0);
                }
                showEpisodes(mVODDetail);
            } else {
                mDetailPresenter.setIsSeries(1);
                if (DetailCommonUtils.isShowReverselayout(vodDetail.getCmsType())) {
                    mDetailPresenter.setIsReverse(1);
                } else {
                    mDetailPresenter.setIsReverse(0);
                }
                showVarietyEpisodes(mVODDetail);
            }

        }
        if (total_episodes_GridView.getVisibility() == View.VISIBLE && null != totalEpisodeAdapter) {
            buy_rel.setNextFocusDownId(R.id.total_episodes);
            col_rel.setNextFocusDownId(R.id.total_episodes);
            mplayRel.setNextFocusDownId(R.id.total_episodes);
            mplayRel.setNextFocusLeftId(R.id.play_layout);
            if (buy_rel.getVisibility() == View.GONE) {
                col_rel.setNextFocusRightId(R.id.total_episodes);
            }
            if (buy_rel.getVisibility() == View.VISIBLE) {
                if ("??????".equals(buyText.getText().toString())) {
                    buy_rel.setNextFocusRightId(R.id.total_episodes);
                } else {
                    col_rel.setNextFocusRightId(R.id.total_episodes);
                }
            }
            totalEpisodeAdapter.setOnkeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    //??????????????????????????????
                    if (!totalEpisodeAdapter.isCanMove()) {
                        return true;
                    }

                    if (KeyEvent.ACTION_DOWN == event.getAction()) {

                        if (ClickUtil.isFastDoubleClick(TAG)) {
                            return true;
                        }

                        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            if (chooseEpisodeListRequestFocuse(true)) {
                                return true;
                            }
                            return false;

                        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            HorizontalGridView parentView = (HorizontalGridView) v.getParent();
                            if (parentView.getChildAt(0) == v) {
                                int last = totalEpisodeAdapter.getItemCount() - 1;
                                parentView.scrollToPosition(last);
                                clearView.setFocusable(true);
                                clearView.requestFocus();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        NewTotalEpisodeAdapter.ViewHolder viewHolder = (NewTotalEpisodeAdapter.ViewHolder) parentView.findViewHolderForAdapterPosition(last);
                                        if (null != viewHolder){
                                            Log.i(TAG, "run: totalEpisodeAdapter request");
                                            viewHolder.itemView.requestFocus();
                                            clearView.setFocusable(false);
                                        }
                                    }
                                },200);
                                return true;
                            }
                        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            if (!canScroll) {
                                return true;
                            }
                        }else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                            int position = (int) v.getTag(R.id.voddetail_total_position);
                            HorizontalGridView parentView = (HorizontalGridView) v.getParent();
                            if (position == totalEpisodeAdapter.getItemCount() - 1){
                                parentView.scrollToPosition(0);
                                clearView.setFocusable(true);
                                clearView.requestFocus();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        NewTotalEpisodeAdapter.ViewHolder viewHolder = (NewTotalEpisodeAdapter.ViewHolder) parentView.findViewHolderForAdapterPosition(0);
                                        if (null != viewHolder){
                                            Log.i(TAG, "run: totalEpisodeAdapter request");
                                            viewHolder.itemView.requestFocus();
                                            clearView.setFocusable(false);
                                        }
                                    }
                                },200);
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });
        }
    }

    //??????????????????????????????????????????????????????
    public void setButtonListener(VODDetail vodDetail) {
        mplayRel.setTag(vodDetail);
        mplayRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canPrePlay()) {
                    if (!canScroll) {
                        return;
                    }
                    focusedCount = 1;
                    switchWindow(1);
                    //??????????????????
                    playViewFullWindow.setVisibility(View.GONE);
                    return;
                }

                //TODO ???????????????????????????????????????????????????
                try {
                    if (mLastOnClickTime != 0 && System.currentTimeMillis() - mLastOnClickTime < VALID_TIME) {
                        return;
                    }
                    mLastOnClickTime = System.currentTimeMillis();
                    if (!canClick) {
                        return;
                    }
                    canClick = false;
                    focusedCount = 1;
                    VODDetail detail = (VODDetail) v.getTag();
                    start(detail);

                } catch (Exception ex) {
                    SuperLog.error(TAG, ex);
                }

            }
        });
        col_rel.setTag(vodDetail);
        col_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onDemandTVPlayFragment.startPlay2();
                try {
                    VODDetail detail = (VODDetail) v.getTag();
                    mDetailPresenter.setCollection(detail, isCollection);
                } catch (Exception ex) {
                    SuperLog.error(TAG, ex);
                }

            }
        });
        buy_rel.setTag(vodDetail);
        buy_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canScroll) {
                    return;
                }
                try {
                    if (mLastOnClickTime != 0 && System.currentTimeMillis() - mLastOnClickTime < VALID_TIME) {
                        return;
                    }
                    mLastOnClickTime = System.currentTimeMillis();
                    mDetailPresenter.setButtonOrderOrSee(false);
                    VODDetail detail = (VODDetail) v.getTag();
                    if (VodUtil.isMiguVod(vodDetail)) {
//                        EpgToast.showToast(v.getContext(), "??????????????????app????????????!");
                        MiguQRViewPopWindow popWindow = new MiguQRViewPopWindow(v.getContext(), vodDetail.getCode(), MiguQRViewPopWindow.mSearchResultType);
                        popWindow.showPopupWindow(v);
                        return;
                    }
                    String vodType = detail.getVODType();
                    if (vodType.equals("0")) {
                        List<VODMediaFile> vodMediaFiles = detail.getMediaFiles();
                        if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
                            mDetailPresenter.setLastPlayUrl(lastPlayUrl);
                            mDetailPresenter.setLastPlayID(lastPlayID);
                            mDetailPresenter.playVOD(detail);
                            needFresh = false;
                        } else {
                            if (!HeartBeatUtil.getInstance().isSubscribedByPrice(detail, "")) {
                                EpgToast.showToast(NewVodDetailActivity.this, "???????????????????????????");
                            } else {
                                EpgToast.showToast(NewVodDetailActivity.this, "???????????????");
                            }
                            return;
                        }
                    } else {
                        List<Episode> episodes = detail.getEpisodes();
                        Bookmark bookmark = vodDetail.getBookmark();
                        if (bookmark != null && !TextUtils.isEmpty(bookmark.getSitcomNO())) {
                            SuperLog.debug(TAG, "set bookmark--->" + "videoId:" + bookmark.getItemID() + ",sitcomNO:" + bookmark.getSitcomNO() + ",breakpoint:" + bookmark.getRangeTime());
                        }
                        if (episodes != null && episodes.size() != 0) {
                            Episode playEpisode = getBuyEpsiode(episodes);
                            //?????????????????????
                            if (null == playEpisode) {
                                EpgToast.showToast(NewVodDetailActivity.this, R.string.notice_no_orderable_product);
                                return;
                            }
                            mDetailPresenter.setLastPlayUrl(lastPlayUrl);
                            mDetailPresenter.setLastPlayID(lastPlayID);
                            mDetailPresenter.playVOD(playEpisode);
                            needFresh = false;
                        } else {
                            EpgToast.showToast(NewVodDetailActivity.this, R.string.notice_no_orderable_product);
                            return;
                        }
                    }
                } catch (Exception ex) {
                    SuperLog.error(TAG, ex);
                }

            }
        });
    }

    private void start(VODDetail detail) {
        mDetailPresenter.setButtonOrderOrSee(true);
        if (VodUtil.isMiguVod(detail)) {
            MiguQRViewPopWindow popWindow = new MiguQRViewPopWindow(this, detail.getCode(), MiguQRViewPopWindow.mSearchResultType);
            RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.root);
            popWindow.showPopupWindow(rootLayout);
            return;
        }
        String vodType = detail.getVODType();
        if (vodType.equals("0")) {
            List<VODMediaFile> vodMediaFiles = detail.getMediaFiles();
            if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
                mDetailPresenter.setLastPlayUrl(lastPlayUrl);
                mDetailPresenter.setLastPlayID(lastPlayID);
                mDetailPresenter.playVOD(detail);
            } else {
                if (!HeartBeatUtil.getInstance().isSubscribedByPrice(detail, null == mVODDetail ? "" : mVODDetail.getPrice())) {
                    EpgToast.showToast(NewVodDetailActivity.this, "???????????????????????????");
                } else {
                    EpgToast.showToast(NewVodDetailActivity.this, "???????????????");
                }
                return;
            }
        } else {
            Bookmark bookmark = detail.getBookmark();
            String sitcomNo = "1";
            String zjSitcomNo = "";
            if (null != bookmark) {
                List<NamedParameter> listNp = bookmark.getCustomFields();
                List<String> vodCustomNamed = CommonUtil.getCustomNamedParameterByKey(listNp, Constant.DETAIL_ZJ_BOOKMARK);
                if (null != vodCustomNamed && vodCustomNamed.size() > 0) {
                    zjSitcomNo = vodCustomNamed.get(0);
                }
            }
            Log.i(TAG, "zjSitcomNo: " + zjSitcomNo);

            if (bookmark != null && !TextUtils.isEmpty(bookmark.getSitcomNO())) {
                SuperLog.debug(TAG, "set bookmark--->" + "videoId:" + bookmark.getItemID() + ",sitcomNO:" + bookmark.getSitcomNO() + ",breakpoint:" + bookmark.getRangeTime());
                sitcomNo = bookmark.getSitcomNO();
            } else if (!TextUtils.isEmpty(zjSitcomNo)) {
                sitcomNo = zjSitcomNo;
            } else {
                List<List<String>> lists = voddetailEpsiodesUtils.getSitcomNos();
                if (lists.size() > 0) {
                    sitcomNo = lists.get(0).get(0);
                } else {
                    EpgToast.showToast(NewVodDetailActivity.this, "???????????????????????????");
                    return;
                }

            }

            Log.i(TAG, "????????????????????????:  getEpisode " + System.currentTimeMillis());
            voddetailEpsiodesUtils.getEpisode(sitcomNo, new VoddetailEpsiodesUtils.GetEpisodeCallback() {
                @Override
                public void getEpisode(List<Episode> episodes, Episode episode) {
                    Log.i(TAG, "????????????????????????:  getEpisode Success " + System.currentTimeMillis());
                    mDetailPresenter.setLastPlayUrl(lastPlayUrl);
                    mDetailPresenter.setLastPlayID(lastPlayID);
                    mDetailPresenter.playVOD(episode);
                }

                @Override
                public void getEpisodeFail() {
                    EpgToast.showToast(NewVodDetailActivity.this, "???????????????????????????");
                }
            });
        }
    }


    public Episode getBuyEpsiode(List<Episode> episodes) {
        for (int i = 0; i < episodes.size(); i++) {
            if (!HeartBeatUtil.getInstance().isSubscribedByPrice(episodes.get(i).getVOD(), null == mVODDetail ? "" : mVODDetail.getPrice())) {
                return episodes.get(i);
            }
        }
        return null;
    }

    //??????????????????
    private void setFavoriteStatus(boolean isCollection) {
        if (isCollection) {
            favText.setText("??????");
            favIcon.setSelected(true);
        } else {
            favText.setText("??????");
            favIcon.setSelected(false);
        }
    }

    //??????????????????
    private void setButtonInfo(VODDetail vodDetail) {
        if (mplayRel != null && mplayRel.getVisibility() == View.VISIBLE) {
            mplayRel.setTag(vodDetail);
        }

        //????????????vip???????????????
        String urlStr = SuperScriptUtil.getInstance().getSuperScriptByVod(vodDetail, SuperScriptUtil.SCRIPT_DETAIL_ICON);
        if (!TextUtils.isEmpty(urlStr)) {
            GlideApp.with(NewVodDetailActivity.this).load(urlStr).into(vipImageView);
            vipImageView.setVisibility(View.VISIBLE);
            mInfoTextFont.setVisibility(View.VISIBLE);
        } else {
            vipImageView.setVisibility(View.GONE);
        }


        //????????????????????????????????????????????? weicy
        if (!VodUtil.canPlay(is4KSource)) {
            if (DetailCommonUtils.canPlayPre()) {
                mPlayview4KWarnningLayout.setVisibility(View.VISIBLE);
                mPlayview4KWarnning.setText(DetailCommonUtils.get4KWarnningtip(NewVodDetailActivity.this));
                m4KWarnning.setVisibility(View.GONE);
                m4KWarnning.setText("");
            } else {
                mPlayview4KWarnningLayout.setVisibility(View.GONE);
                mPlayview4KWarnning.setText("");
                m4KWarnning.setVisibility(View.VISIBLE);
                //??????4k?????????
                m4KWarnning.setText(DetailCommonUtils.get4KWarnningtip(NewVodDetailActivity.this));
            }
            mplayRel.setFocusable(false);
            mplayRel.setEnabled(false);
            mplayRelIcon.setSelected(false);
            GlideApp.with(this).load(R.drawable.detail_button_unplay).into(mplayRelIcon);
            mplayRelText.setTextColor(getResources().getColor(R.color.detail_btn_text_unable));
            buy_rel.setFocusable(false);
            buy_rel.setEnabled(false);
            buyIcon.setEnabled(false);
        } else {
            m4KWarnning.setVisibility(View.GONE);
            if (mplayRel.getVisibility() == View.VISIBLE) {
                mplayRel.setFocusable(true);
                mplayRel.setEnabled(true);
                mplayRelIcon.setSelected(true);
                GlideApp.with(this).load(R.drawable.detail_button_play).into(mplayRelIcon);
                mplayRelText.setTextColor(getResources().getColor(R.color.tVodProgramList_Content_Item_TextColor_Focus));
            }
        }

        if (col_rel != null) {
            col_rel.setVisibility(View.VISIBLE);
            col_rel.setTag(vodDetail);
            favText.setText("??????");
            favIcon.setSelected(false);

        }

    }

    @SuppressLint("RestrictedApi")
    public void showEpisodes(VODDetail detail) {
        if (null == detail.getEpisodes() || detail.getEpisodes().size() == 0) {
            return;
        }
        //????????????????????????
        Episode selectEpisode = voddetailEpsiodesUtils.getSelesctedEpisode();
        List<Episode> bookmarkEPisodes = voddetailEpsiodesUtils.getMarkEpisodes();
        List<List<String>> sitnums = voddetailEpsiodesUtils.getSitcomNos();
        List<String> bookmarkEPisodesCounts = new ArrayList<>();
        for (int i = 0; i < sitnums.size(); i++) {
            if (sitnums.get(i).get(0).equals(bookmarkEPisodes.get(0).getSitcomNO())) {
                bookmarkEPisodesCounts = sitnums.get(i);
            }
        }

        if (chooseEpisodeLayoutManager == null) {
            chooseEpisodeLayoutManager = new LinearLayoutManager(this);
            chooseEpisodeLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            chooseEpisodeList.setLayoutManager(chooseEpisodeLayoutManager);
        }
        if (voddetailEpsiodesUtils.getTotal() <= 10) {
            episodesIconLeft.setVisibility(View.GONE);
            episodesIconRight.setVisibility(View.GONE);
            chooseEpisodeList.setLayoutParams(DetailCommonUtils.getLayoutParam(voddetailEpsiodesUtils.getTotal(), chooseEpisodeList.getLayoutParams(), this));

            total_episodes_GridView.setVisibility(View.GONE);
            if (chooseAdapter == null) {
                chooseAdapter = new NewChooseEpisodeAdapter(bookmarkEPisodes, bookmarkEPisodesCounts, voddetailEpsiodesUtils, mDetailPresenter, this, is4KSource);
                chooseAdapter.setSelectEpisode(selectEpisode);
                chooseAdapter.setRecyclerView(chooseEpisodeList);
//                chooseEpisodeList.setHorizontalSpacing((int)getResources().getDimension(R.dimen.margin_12));
                chooseEpisodeList.setAdapter(chooseAdapter);
                chooseAdapter.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        //??????????????????????????????
                        if (!chooseAdapter.isCanMove()) {
                            return true;
                        }

                        if (KeyEvent.ACTION_DOWN == event.getAction()) {
                            int position = chooseEpisodeList.getChildPosition(v);
                            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                                if (playViewLayout.getVisibility() == View.VISIBLE && playViewLayout.isFocusable()) {
                                    playViewLayout.requestFocus();
                                } else if (mplayRel.getVisibility() == View.VISIBLE && mplayRel.isFocusable()) {
                                    mplayRel.requestFocus();
                                } else if (buy_rel.getVisibility() == View.VISIBLE && buy_rel.isFocusable()) {
                                    buy_rel.requestFocus();
                                } else {
                                    col_rel.requestFocus();
                                }
                                return true;
                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                                if (position == 0) {
                                    LinearLayoutManager manager = (LinearLayoutManager) chooseEpisodeList.getLayoutManager();
                                    View lastView = manager.findViewByPosition(manager.findLastVisibleItemPosition());
                                    if (null != lastView){
                                        lastView.requestFocus();
                                        clearView.setFocusable(false);
                                    }
                                    return true;
                                }
                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                                int total = chooseAdapter.getmEpisodeCountList().size();
                                if (total - 1 == position) {
                                    LinearLayoutManager manager = (LinearLayoutManager) chooseEpisodeList.getLayoutManager();
                                    View firstView = manager.findViewByPosition(0);
                                    if (null != firstView){
                                        firstView.requestFocus();
                                        clearView.setFocusable(false);
                                    }
                                    return true;
                                }
                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                                if (!canScroll) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });
            } else {
                chooseAdapter.setSelectEpisode(selectEpisode);
            }
        } else {
            episodesIconLeft.setVisibility(View.VISIBLE);
            episodesIconRight.setVisibility(View.VISIBLE);
            if (chooseAdapter == null) {
                totalEpisodesCount = voddetailEpsiodesUtils.getSitcomNos();
                selectEpisode = voddetailEpsiodesUtils.getSelesctedEpisode();
                bookmarkEPisodes = voddetailEpsiodesUtils.getMarkEpisodes();

                chooseAdapter = new NewChooseEpisodeAdapter(bookmarkEPisodes, bookmarkEPisodesCounts, voddetailEpsiodesUtils, mDetailPresenter, this, is4KSource);
                chooseAdapter.setSelectEpisode(selectEpisode);
                chooseAdapter.setRecyclerView(chooseEpisodeList);
//                chooseEpisodeList.setHorizontalSpacing((int)getResources().getDimension(R.dimen.margin_12));
                chooseEpisodeList.setAdapter(chooseAdapter);

                totalEpisodeAdapter = new NewTotalEpisodeAdapter(voddetailEpsiodesUtils.getSitcomNos(), chooseAdapter, 10, chooseEpisodeList);
                total_episodes_GridView.setAdapter(totalEpisodeAdapter);
                total_episodes_GridView.setHorizontalSpacing((int) getResources().getDimension(R.dimen.margin_12));
                int totalPosition = (int) (voddetailEpsiodesUtils.getTotalPosition());
                if (totalEpisodesCount.size() > totalPosition) {

                    chooseEpisodeList.setLayoutParams(DetailCommonUtils.getLayoutParam(totalEpisodesCount.get(totalPosition).size(), chooseEpisodeList.getLayoutParams(), this));
                    chooseAdapter.setDataSource(voddetailEpsiodesUtils.getMarkEpisodes());
                } else {
                    if (totalEpisodesCount.size() > 0) {

                        chooseEpisodeList.setLayoutParams(DetailCommonUtils.getLayoutParam(totalEpisodesCount.get(0).size(), chooseEpisodeList.getLayoutParams(), this));

                        chooseAdapter.setDataSource(voddetailEpsiodesUtils.getMarkEpisodes());
                    }
                }
                chooseAdapter.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        //??????????????????????????????
                        if (!chooseAdapter.isCanMove()) {
                            return true;
                        }

                        if (KeyEvent.ACTION_DOWN == event.getAction()) {
                            int position = chooseEpisodeList.getChildPosition(v);
                            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                                if (playViewLayout.getVisibility() == View.VISIBLE && playViewLayout.isFocusable()) {
                                    playViewLayout.requestFocus();
                                } else if (mplayRel.getVisibility() == View.VISIBLE && mplayRel.isFocusable()) {
                                    mplayRel.requestFocus();
                                } else if (buy_rel.getVisibility() == View.VISIBLE && buy_rel.isFocusable()) {
                                    buy_rel.requestFocus();
                                } else {
                                    col_rel.requestFocus();
                                }
                                return true;
                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                                if (position == 0) {
                                    //??????
                                    Log.i(TAG, "onKey: ?????????");

                                    if (!totalEpisodeAdapter.showPre()) {
                                        //???????????????
                                        return true;
                                    }
                                    total_episodes_GridView.setSelectedPosition(totalEpisodeAdapter.getSelectPosition());
                                    clearView.setFocusable(true);
                                    clearView.requestFocus();

                                    LinearLayoutManager manager = (LinearLayoutManager) chooseEpisodeList.getLayoutManager();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            View lastView = manager.findViewByPosition(manager.findLastVisibleItemPosition());
                                            if (null != lastView) {
                                                lastView.requestFocus();
                                                clearView.setFocusable(false);
                                            }
                                        }
                                    }, 200);

                                    return true;
                                }
                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

                                LinearLayoutManager manager = (LinearLayoutManager) chooseEpisodeList.getLayoutManager();
                                int lastPosition = manager.findLastVisibleItemPosition();
                                if (position == lastPosition) {
                                    //??????
                                    Log.i(TAG, "onKey: ????????????");

                                    if (!totalEpisodeAdapter.showNext()) {
                                        //???????????????
                                        return true;
                                    }
                                    total_episodes_GridView.setSelectedPosition(totalEpisodeAdapter.getSelectPosition());
                                    clearView.setFocusable(true);
                                    clearView.requestFocus();

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            View view = chooseEpisodeList.getChildAt(0);
                                            if (null != view) {
                                                view.requestFocus();
                                                clearView.setFocusable(false);
                                            }
                                        }
                                    }, 200);

                                    return true;
                                }

                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                                //?????????????????????
                                if (total_episodes_GridView.getVisibility() == View.VISIBLE && null != total_episodes_GridView.getAdapter()) {
                                    total_episodes_GridView.requestFocus();
                                    return true;
                                } else if (!canScroll) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });
                total_episodes_GridView.setSelectedPosition(totalPosition);
                totalEpisodeAdapter.setSelectPosition(totalPosition);
            } else {
                int totalPosition = (int) (voddetailEpsiodesUtils.getTotalPosition());
                if (totalEpisodesCount.size() > totalPosition) {

                    chooseEpisodeList.setLayoutParams(DetailCommonUtils.getLayoutParam(totalEpisodesCount.get(totalPosition).size(), chooseEpisodeList.getLayoutParams(), this));

                    chooseAdapter.setDataSource(voddetailEpsiodesUtils.getMarkEpisodes());
                } else {
                    if (totalEpisodesCount.size() > 0) {

                        chooseEpisodeList.setLayoutParams(DetailCommonUtils.getLayoutParam(totalEpisodesCount.get(0).size(), chooseEpisodeList.getLayoutParams(), this));

                        chooseAdapter.setDataSource(voddetailEpsiodesUtils.getMarkEpisodes());
                    }
                }
                total_episodes_GridView.setSelectedPosition(totalPosition);
                totalEpisodeAdapter.setSelectPosition(totalPosition);
                chooseAdapter.setSelectEpisode(selectEpisode);
            }
        }

        chooseAdapter.setClickListener(new NewChooseEpisodeAdapter.ClickListener() {
            @Override
            public void onClick(Episode episode, boolean isSelected, View view) {
                if (!VodUtil.canPlay(is4KSource)) {
                    return;
                }
                if (!isSelected) {
                    canScroll = false;
                }
                if (windowCount != 0 && canPrePlay()) {
                    //??????????????????????????????????????????????????????????????????
                    playViewLayout.setVisibility(View.VISIBLE);
                    detailRootView.scrollTo(0, 0);
                }
                focusedCount = 2;
                if (isSelected && canPrePlay()) {
                    if (!canScroll) {
                        return;
                    }
                    switchWindow(1);
                    //??????????????????
                    playViewFullWindow.setVisibility(View.GONE);
                } else {
                    if (windowState == 0) {
                        playViewloading.setVisibility(View.VISIBLE);
                        switchWindow(4);
                    }
                    playEpisode(episode);
                }
            }
        });
    }

    @SuppressLint("RestrictedApi")
    public void showVarietyEpisodes(VODDetail detail) {

        //????????????????????????
        Episode selectEpisode = voddetailEpsiodesUtils.getSelesctedEpisode();
        List<Episode> bookmarkEpisodes = voddetailEpsiodesUtils.getMarkEpisodes();
        List<List<String>> sitnums = voddetailEpsiodesUtils.getSitcomNos();
        List<String> bookmarkEPisodesCounts = new ArrayList<>();
        for (int i = 0; i < sitnums.size(); i++) {
            if (sitnums.get(i).get(0).equals(bookmarkEpisodes.get(0).getSitcomNO())) {
                bookmarkEPisodesCounts = sitnums.get(i);
            }
        }

        if (chooseEpisodeLayoutManager == null) {
            chooseEpisodeLayoutManager = new LinearLayoutManager(this);
            chooseEpisodeLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            chooseEpisodeList.setLayoutManager(chooseEpisodeLayoutManager);
        }

        if (voddetailEpsiodesUtils.getTotal() <= 5) {
            episodesIconLeft.setVisibility(View.GONE);
            episodesIconRight.setVisibility(View.GONE);
            total_episodes_GridView.setVisibility(View.GONE);

            chooseEpisodeList.setLayoutParams(DetailCommonUtils.getLayoutParamForVariety(voddetailEpsiodesUtils.getTotal(), chooseEpisodeList.getLayoutParams(), this));

            if (varietyEpisodeAdapter == null) {
                varietyEpisodeAdapter = new NewChooseVarietyEpisodeAdapter(voddetailEpsiodesUtils.getMarkEpisodes(), bookmarkEPisodesCounts, voddetailEpsiodesUtils, mDetailPresenter, this, is4KSource);
                varietyEpisodeAdapter.setSelectEpisode(selectEpisode);
                varietyEpisodeAdapter.setRecyclerView(chooseEpisodeList);
                chooseEpisodeList.setAdapter(varietyEpisodeAdapter);
//                chooseEpisodeList.setHorizontalSpacing((int)getResources().getDimension(R.dimen.margin_12));
                varietyEpisodeAdapter.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        //??????????????????????????????
                        if (!varietyEpisodeAdapter.isCanMove()) {
                            return true;
                        }

                        if (KeyEvent.ACTION_DOWN == event.getAction()) {
                            int position = chooseEpisodeList.getChildPosition(v);
                            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {

                                if (playViewLayout.getVisibility() == View.VISIBLE && playViewLayout.isFocusable()) {
                                    playViewLayout.requestFocus();
                                } else if (mplayRel.getVisibility() == View.VISIBLE && mplayRel.isFocusable()) {
                                    mplayRel.requestFocus();
                                } else if (buy_rel.getVisibility() == View.VISIBLE && buy_rel.isFocusable()) {
                                    buy_rel.requestFocus();
                                } else {
                                    col_rel.requestFocus();
                                }
                                return true;
                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {

                                if (position == 0) {
                                    LinearLayoutManager manager = (LinearLayoutManager) chooseEpisodeList.getLayoutManager();
                                    View lastView = manager.findViewByPosition(manager.findLastVisibleItemPosition());
                                    if (null != lastView){
                                        lastView.requestFocus();
                                        clearView.setFocusable(false);
                                    }
                                    return true;
                                }

                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                                Log.i(TAG, "onKey: " + chooseEpisodeList.getChildPosition(v));
                                if (null == varietyEpisodeAdapter.getmEpisodeCountList()) {
                                    return false;
                                }
                                int total = varietyEpisodeAdapter.getmEpisodeCountList().size();
                                if (total - 1 == position) {
                                    LinearLayoutManager manager = (LinearLayoutManager) chooseEpisodeList.getLayoutManager();
                                    View firstView = manager.findViewByPosition(0);
                                    if (null != firstView){
                                        firstView.requestFocus();
                                        clearView.setFocusable(false);
                                    }
                                    return true;
                                }
                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                                if (total_episodes_GridView.getVisibility() == View.VISIBLE && null != total_episodes_GridView.getAdapter()) {
                                    total_episodes_GridView.requestFocus();
                                    return true;
                                } else if (!canScroll) {
                                    return true;
                                }

                            }
                        }
                        return false;
                    }
                });
            } else {
                varietyEpisodeAdapter.setSelectEpisode(selectEpisode);
            }
        } else {
            episodesIconLeft.setVisibility(View.VISIBLE);
            episodesIconRight.setVisibility(View.VISIBLE);
            totalEpisodesCount = voddetailEpsiodesUtils.getSitcomNos();
            selectEpisode = voddetailEpsiodesUtils.getSelesctedEpisode();
            List<Episode> bookmarkEPisodes = voddetailEpsiodesUtils.getMarkEpisodes();

            if (varietyEpisodeAdapter == null) {
                varietyEpisodeAdapter = new NewChooseVarietyEpisodeAdapter(bookmarkEPisodes, bookmarkEPisodesCounts, voddetailEpsiodesUtils, mDetailPresenter, this, is4KSource);
                varietyEpisodeAdapter.setSelectEpisode(selectEpisode);
                varietyEpisodeAdapter.setRecyclerView(chooseEpisodeList);
//                chooseEpisodeList.setHorizontalSpacing((int)getResources().getDimension(R.dimen.margin_12));
                chooseEpisodeList.setAdapter(varietyEpisodeAdapter);
                totalEpisodeAdapter = new NewTotalEpisodeAdapter(voddetailEpsiodesUtils.getSitcomNos(), varietyEpisodeAdapter, 5, chooseEpisodeList);
                total_episodes_GridView.setAdapter(totalEpisodeAdapter);
                total_episodes_GridView.setHorizontalSpacing((int) getResources().getDimension(R.dimen.margin_12));
                //???????????????
                totalEpisodeAdapter.setTotal(voddetailEpsiodesUtils.getTotal());
                int totalPosition = (int) (voddetailEpsiodesUtils.getTotalPosition());
                if (totalEpisodesCount.size() > totalPosition) {

                    chooseEpisodeList.setLayoutParams(DetailCommonUtils.getLayoutParamForVariety(totalEpisodesCount.get(totalPosition).size(), chooseEpisodeList.getLayoutParams(), this));

                    varietyEpisodeAdapter.setDataSource(voddetailEpsiodesUtils.getMarkEpisodes());
                } else {
                    if (totalEpisodesCount.size() > 0) {

                        chooseEpisodeList.setLayoutParams(DetailCommonUtils.getLayoutParamForVariety(totalEpisodesCount.get(0).size(), chooseEpisodeList.getLayoutParams(), this));

                        varietyEpisodeAdapter.setDataSource(voddetailEpsiodesUtils.getMarkEpisodes());
                    }
                }
                varietyEpisodeAdapter.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        //??????????????????????????????
                        if (!varietyEpisodeAdapter.isCanMove()) {
                            return true;
                        }

                        if (KeyEvent.ACTION_DOWN == event.getAction()) {
                            int position = chooseEpisodeList.getChildPosition(v);
                            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {

                                if (playViewLayout.getVisibility() == View.VISIBLE && playViewLayout.isFocusable()) {
                                    playViewLayout.requestFocus();
                                } else if (mplayRel.getVisibility() == View.VISIBLE && mplayRel.isFocusable()) {
                                    mplayRel.requestFocus();
                                } else if (buy_rel.getVisibility() == View.VISIBLE && buy_rel.isFocusable()) {
                                    buy_rel.requestFocus();
                                } else {
                                    col_rel.requestFocus();
                                }
                                return true;

                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                                if (position == 0) {
                                    //??????
                                    Log.i(TAG, "onKey: ?????????");

                                    if (!totalEpisodeAdapter.showPre()) {
                                        //???????????????
                                        return true;
                                    }
                                    total_episodes_GridView.setSelectedPosition(totalEpisodeAdapter.getSelectPosition());
                                    clearView.setFocusable(true);
                                    clearView.requestFocus();

                                    LinearLayoutManager manager = (LinearLayoutManager) chooseEpisodeList.getLayoutManager();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            View lastView = manager.findViewByPosition(manager.findLastVisibleItemPosition());
                                            if (null != lastView) {
                                                lastView.requestFocus();
                                                clearView.setFocusable(false);
                                            }
                                        }
                                    }, 200);

                                    return true;
                                }

                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

                                LinearLayoutManager manager = (LinearLayoutManager) chooseEpisodeList.getLayoutManager();
                                int lastPosition = manager.findLastVisibleItemPosition();
                                if (position == lastPosition) {
                                    //???????????? ??????
                                    Log.i(TAG, "onKey: ????????????");

                                    if (!totalEpisodeAdapter.showNext()) {
                                        //???????????????
                                        return true;
                                    }
                                    total_episodes_GridView.setSelectedPosition(totalEpisodeAdapter.getSelectPosition());
                                    clearView.setFocusable(true);
                                    clearView.requestFocus();

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            View view = chooseEpisodeList.getChildAt(0);
                                            if (null != view) {
                                                view.requestFocus();
                                                clearView.setFocusable(false);
                                            }
                                        }
                                    }, 200);

                                    return true;
                                }

                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                                if (total_episodes_GridView.getVisibility() == View.VISIBLE && null != total_episodes_GridView.getAdapter()) {
                                    total_episodes_GridView.requestFocus();
                                    return true;
                                } else if (!canScroll) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });
                total_episodes_GridView.setSelectedPosition(totalPosition);
                totalEpisodeAdapter.setSelectPosition(totalPosition);
            } else {
                int totalPosition = (int) (voddetailEpsiodesUtils.getTotalPosition());
                if (totalEpisodesCount.size() > totalPosition) {

                    varietyEpisodeAdapter.setDataSource(voddetailEpsiodesUtils.getMarkEpisodes());
                } else {
                    if (totalEpisodesCount.size() > 0) {

                        chooseEpisodeList.setLayoutParams(DetailCommonUtils.getLayoutParamForVariety(totalEpisodesCount.get(0).size(), chooseEpisodeList.getLayoutParams(), this));

                        varietyEpisodeAdapter.setDataSource(voddetailEpsiodesUtils.getMarkEpisodes());
                    }
                }
                total_episodes_GridView.setSelectedPosition(totalPosition);
                totalEpisodeAdapter.setSelectPosition(totalPosition);
                varietyEpisodeAdapter.setSelectEpisode(selectEpisode);
            }
        }
        varietyEpisodeAdapter.setClickListener(new NewChooseVarietyEpisodeAdapter.ClickListener() {
            @Override
            public void onClick(Episode episode, boolean isSelected, View view) {
                if (!isSelected) {
                    canScroll = false;
                }
                if (windowCount != 0 && canPrePlay()) {
                    //??????????????????????????????????????????????????????????????????
                    playViewLayout.setVisibility(View.VISIBLE);
                    detailRootView.scrollTo(0, 0);
                }
                focusedCount = 2;
                if (isSelected && canPrePlay()) {
                    if (!canScroll) {
                        return;
                    }
                    switchWindow(1);
                    //??????????????????
                    playViewFullWindow.setVisibility(View.GONE);
                } else {
                    if (windowState == 0) {
                        playViewloading.setVisibility(View.VISIBLE);
                        switchWindow(4);
                    }
                    playEpisode(episode);
                }
            }
        });
    }

    //??????????????????  isUP???????????????????????????????????????
    private boolean chooseEpisodeListRequestFocuse(boolean isUP) {
        Log.i(TAG, "chooseEpisodeListRequestFocuse: isUp: "+isUP);
        LinearLayoutManager manager = (LinearLayoutManager) chooseEpisodeList.getLayoutManager();

        int selectedIndex = -1;
        if (null != varietyEpisodeAdapter){
            selectedIndex = varietyEpisodeAdapter.getSelectedPosition();
        }else if (null != chooseAdapter){
            selectedIndex = chooseAdapter.getSelectedPosition();
        }

        Log.i(TAG, "chooseEpisodeListRequestFocuse:selectedIndex "+selectedIndex);
        if (selectedIndex == -1){
            if (isUP){
                View view = chooseEpisodeList.getLayoutManager().findViewByPosition(0);
                Log.i(TAG, "chooseEpisodeListRequestFocuse: ?????????????????????????????????????????????" );
                if (null != view) {
                    view.requestFocus();
                    return true;
                }
            }else{
                //?????????????????????
                if (!chooseEpisodeListRequestFocuse()){
                    View view = chooseEpisodeList.getLayoutManager().findViewByPosition(0);
                    Log.i(TAG, "chooseEpisodeListRequestFocuse: ??????????????????????????????????????????????????????????????????" );
                    if (null != view) {
                        view.requestFocus();
                        return true;
                    }
                }else{
                    return true;
                }

            }
        }

        Log.i(TAG, "chooseEpisodeListRequestFocuse: ????????????????????????????????????");
        View view = chooseEpisodeList.getLayoutManager().findViewByPosition(selectedIndex);
        Log.i(TAG, "chooseEpisodeListRequestFocuse: " + selectedIndex + " " + (null == view));
        if (null != view) {
            view.requestFocus();
            return true;
        }

        //??????????????????????????????
        Log.i(TAG, "chooseEpisodeListRequestFocuse: ??????????????????????????????");
        view = chooseEpisodeList.getLayoutManager().findViewByPosition(0);
        Log.i(TAG, "chooseEpisodeListRequestFocuse: ?????????????????????????????????????????????" );
        if (null != view) {
            view.requestFocus();
            return true;
        }
        return false;
    }

    //
    private boolean chooseEpisodeListRequestFocuse() {
        Log.i(TAG, "chooseEpisodeListRequestFocuse: ?????????????????????");
        LinearLayoutManager manager = (LinearLayoutManager) chooseEpisodeList.getLayoutManager();
        if (null != varietyEpisodeAdapter) {
            int bookmarkPoistion = varietyEpisodeAdapter.getBookmarkPosition();
            Log.i(TAG, "chooseEpisodeListRequestFocuse: bookmarkPoistion "+ bookmarkPoistion);
            if (bookmarkPoistion > -1){
                View bookView = manager.findViewByPosition(bookmarkPoistion);
                if (null != bookView) {
                    bookView.requestFocus();
                    return true;
                }
            }

        } else if (null != chooseAdapter) {
            int bookmarkPoistion = chooseAdapter.getBookmarkPosition();
            Log.i(TAG, "chooseEpisodeListRequestFocuse: bookmarkPoistion "+ bookmarkPoistion);
            if (bookmarkPoistion > -1){
                View bookView = manager.findViewByPosition(bookmarkPoistion);
                Log.i(TAG, "chooseEpisodeListRequestFocuse: 3 "+(null != bookView));
                if (null != bookView) {
                    bookView.requestFocus();
                    return true;
                }
            }
        }

        return false;






//        Log.i(TAG, "chooseEpisodeListRequestFocuse: ?????????????????????");
//        LinearLayoutManager manager = (LinearLayoutManager) chooseEpisodeList.getLayoutManager();
//        if (null != manager) {
//            View firstView = manager.findViewByPosition(0);
//            View LastView = manager.findViewByPosition(manager.findLastVisibleItemPosition());
//            Log.i(TAG, "chooseEpisodeListRequestFocuse:  1 "+(null != firstView ) + " "+ (null != LastView));
//            if (null != firstView && null != LastView) {
//                Episode firstEpisode = (Episode) firstView.getTag();
//                Episode LastEpisode = (Episode) LastView.getTag();
//                Log.i(TAG, "chooseEpisodeListRequestFocuse: 2 "+(null != firstEpisode)+ " "+ (null != voddetailEpsiodesUtils.getSelesctedEpisode() )+ " "+ (null != LastEpisode));
//                if (null != firstEpisode && null != voddetailEpsiodesUtils.getSelesctedEpisode() && null != LastEpisode) {
//                    String bookmarkSitcomNo = voddetailEpsiodesUtils.getSelesctedEpisode().getSitcomNO();
//                    String firstSitcomNo = firstEpisode.getSitcomNO();
//                    String lastSitcomNo = LastEpisode.getSitcomNO();
//
//                    int bookmark = Integer.valueOf(bookmarkSitcomNo);
//                    int first = Integer.valueOf(firstSitcomNo);
//                    int last = Integer.valueOf(lastSitcomNo);
//
//                    Log.i(TAG, "chooseEpisodeListRequestFocuse: bookmark ??? " + bookmark + " first "+ first + " last "+ last);
//
//                    if ((first <= bookmark && bookmark <= last) || (last <= bookmark && bookmark <= first)) {
//                        //????????????????????????
//                        if (null != varietyEpisodeAdapter) {
//                            int bookmarkPoistion = varietyEpisodeAdapter.getBookmarkPosition();
//                            View bookView = manager.findViewByPosition(bookmarkPoistion);
//                            if (null != bookView) {
//                                bookView.requestFocus();
//                                return true;
//                            }
//                        } else if (null != chooseAdapter) {
//                            int bookmarkPoistion = chooseAdapter.getBookmarkPosition();
//                            View bookView = manager.findViewByPosition(bookmarkPoistion);
//                            Log.i(TAG, "chooseEpisodeListRequestFocuse: 3 "+(null != bookView));
//                            if (null != bookView) {
//                                bookView.requestFocus();
//                                return true;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        Log.i(TAG, "chooseEpisodeListRequestFocuse: ?????????????????????  ?????????????????????");
//
//        return false;
    }

    //?????????????????????
    private void initCastRole(List<CastRole> castRoleList, List<String> subjectIds) {
        if (castRoleList != null && castRoleList.size() != 0) {
            boolean isShow = ShowCastListUtils.showCastList(mVODDetail);
            if (isShow) {
                NewCreditAdapter adapter = new NewCreditAdapter(castRoleList, subjectIds);
                adapter.setOnkeyListener(new View.OnKeyListener() {
                    //                    boolean canMove = true;
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
//                        if (!canMove){
//                            return true;
//                        }
//                        canMove = false;
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                canMove = true;
//                            }
//                        },300);
                        int position = (int) v.getTag(R.id.voddetail_cast_position);
                        if (position == 0 && KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            if (playViewLayout.getVisibility() == View.VISIBLE && playViewLayout.isFocusable()) {
                                playViewLayout.requestFocus();
                            }
                            return true;
                        }
                        return false;
                    }
                });
                adapter.setListenr(new NewCreditAdapter.onClickListenr() {
                    @Override
                    public void onClick() {
                        needFresh = false;
                    }
                });
                mCreditList.setVisibility(View.VISIBLE);
                mCreditList.setAdapter(adapter);
            } else {
                mCreditList.setVisibility(View.GONE);
            }
        } else {
            mCreditList.setVisibility(View.GONE);
        }
    }

    //????????????
    @Override
    public void showCollection(boolean isCollection) {
        this.isCollection = isCollection;
        setFavoriteStatus(this.isCollection);
    }

    //????????????????????????
    @Override
    public void setNewScore(List<Float> newScore) {
//        if (!CommonUtil.getDeviceType().equals("EC6109M")){
//            mScoreText.setTypeface(mScoreText.getTypeface(), Typeface.ITALIC);
//        }
        mScoreText.setText(String.valueOf(newScore.get(0)));
    }

    //?????????????????????????????????
    @Override
    public void showContentNotExit() {
        EpgToast.showToast(NewVodDetailActivity.this, "???????????????????????????");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //????????????????????????????????????
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            return super.onKeyDown(keyCode, event);
        }
        Log.i(TAG, "onKeyDown: ?????????activity");

        if (windowState == 0) {
            Log.i(TAG, "onKeyDown: ??????");
            //??????????????????
            if (!isCanClick) {
                SuperLog.debug(TAG, "onKeyDown->CAN NOT CLICK1");
                int codeValue = RemoteKeyEvent.getInstance().getKeyCodeValue(keyCode);
                if (keyCode != KeyEvent.KEYCODE_BACK && codeValue != RemoteKeyEvent.BTV && codeValue != RemoteKeyEvent.TVOD && codeValue != RemoteKeyEvent.VOD)
                    return true;
            }
            //??????????????????
            if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                if (null != col_rel) {
                    col_rel.performClick();
                }
                return true;
            }

            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (m4KWarnning.getVisibility() == View.VISIBLE && col_rel.hasFocus()) {
                    return true;
                }
            }

        } else if (windowState == 1) {
            //??????????????????
            boolean state = false;
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                state = true;
            }

            int codeValue = RemoteKeyEvent.getInstance().getKeyCodeValue(keyCode);
            if (codeValue != RemoteKeyEvent.BTV
                    && codeValue != RemoteKeyEvent.TVOD
                    && null != mKeyListener) {
                if (null != onDemandTVPlayFragment && onDemandTVPlayFragment.isCurrentAdvertPlay()) {
                    return mKeyListener.onKeyDown(keyCode, event);
                }
                mKeyListener.onKeyDown(keyCode, event);
            }

            //??????????????????????????????????????????
            if (state) {
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        //????????????????????????????????????
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            return super.onKeyDown(keyCode, event);
        }
        if (windowState == 1) {
            //?????????
            int codeValue = RemoteKeyEvent.getInstance().getKeyCodeValue(keyCode);
            if (codeValue != RemoteKeyEvent.BTV
                    && codeValue != RemoteKeyEvent.TVOD
                    && null != mKeyListener) {
                Log.i(TAG, "onKeyUp: ????????? codeValue???" + codeValue);
                mKeyListener.onKeyUp(keyCode, event);
            }
        }
        return super.onKeyUp(keyCode, event);
    }


    //??????????????????????????????
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PlayUrlEvent event) {
        SuperLog.debug(TAG, "get PlayUrlEvent");
        if (null != event && (event.isVODSubscribe() || event.isTrySeeSubscribe())) {
            if (!mDetailPresenter.isClickEpisode()) {
                isFirstLoad = true;
            }
            //?????????????????????
            if (null != onDemandTVPlayFragment) {
                if (canPrePlay()) {
                    SuperLog.debug(TAG, "orderFinish: ??????????????????????????????");
                    if (windowState != 0) {
                        SuperLog.debug(TAG, "orderFinish: ????????????????????????");
                        switchWindow(0);
                    }
                    getSupportFragmentManager().beginTransaction().remove(onDemandTVPlayFragment).commitAllowingStateLoss();
                    onDemandTVPlayFragment = null;
                } else {
                    SuperLog.debug(TAG, "orderFinish: ?????????????????????????????????");
                    switchWindow(3);
                }
            }
            //???????????????????????????????????????????????????
            playViewFullWindow.setVisibility(View.VISIBLE);
            isNeedFullWindow = true;
            mHandler.sendEmptyMessage(RequestDetail);
            if (event.isVODSubscribe() || event.isTrySeeSubscribe()) {
                isFromSubscribeNotTrySee = true;
            }
        }
    }

    //????????????????????????????????????????????????
    @Subscribe
    public void eventCollection(CollectionEvent collectionEvent) {
        SuperLog.debug(TAG, "event receiver collectionEvent change state");
        if (null != mVODDetail && mVODDetail.getID().equals(collectionEvent.getVodId())) {
            showCollection(collectionEvent.isCollection());
        }

    }

    //????????????????????????????????????
    @Subscribe
    public void eventBookmark(BookmarkEvent bookmarkEvent) {
        Log.i(TAG, "?????????????????????????????? eventBookmark: ");
        if (this.isFinishing()) {
            return;
        }
        if (null != bookmarkEvent && !TextUtils.isEmpty(bookmarkEvent.getPlayVodBean())) {
            PlayVodBean bean = JsonParse.json2Object(bookmarkEvent.getPlayVodBean(), PlayVodBean.class);
            if (null != bean && !TextUtils.isEmpty(bean.getVodId()) && null != mVODDetail && (bean.getVodId().equals(mVODDetail.getID()) || bean.getFatherVODId().equals(mVODDetail.getID()))) {
                Log.i(TAG, "eventBookmark: ??????");
                if (activity_state == activity_pause) {
                    isRefreshBookmark = true;
                    playVodBean = bean;
                } else {
                    refreshBookmark(bean);
                }
            }
        }
    }

    //?????????????????????????????????
    @Subscribe
    public void eventSetScore(NewScoresEvent newScoresEvent) {
        SuperLog.debug(TAG, " event receiver score refresh");
        if (null != newScoresEvent) {
            setNewScore(newScoresEvent.getNewScores());
        }

    }

    //?????????????????????
    @Subscribe
    public void onEvent(VoddetailScrollEvent event) {
        Log.i(TAG, "onEvent: " + event.getIndex());
        Handler handler = new Handler();

        if (event.getIndex() == 2) {
            if (null != onDemandTVPlayFragment) {
                onDemandTVPlayFragment.pausePlay();
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null != detailRootView) {
                        //??????????????????
                        detailRootView.smoothScrollTo(0, (int) listLayout.getY());
                        windowCount = 1;
                    }
                }
            }, 50);
        } else if (event.getIndex() == 3) {
            if (null != onDemandTVPlayFragment) {
                onDemandTVPlayFragment.pausePlay();
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null != detailRootView) {
                        //??????????????????
                        if (recentLayout.getVisibility() == View.VISIBLE) {
                            detailRootView.smoothScrollTo(0, (int) listLayout.getY() + (int) recentLayout.getY());
                        } else {
                            detailRootView.smoothScrollTo(0, (int) listLayout.getY() + (int) guessLayout.getY());
                        }

                        windowCount = 1;
                    }
                }
            }, 50);

        } else if (event.getIndex() == 4) {
            if (null != onDemandTVPlayFragment) {
                onDemandTVPlayFragment.pausePlay();
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //????????????
                    detailRootView.smoothScrollTo(0, 10000);
                    windowCount = 2;
                }
            }, 50);

        } else if (event.getIndex() == VoddetailScrollEvent.EpisodeListRequesetFocus) {
            //???????????????????????????????????????????????????
            if (episodes_lly.getVisibility() == View.VISIBLE) {
                if (total_episodes_GridView.getVisibility() == View.VISIBLE && null != total_episodes_GridView.getAdapter()) {
                    total_episodes_GridView.requestFocus();
                } else {
                    chooseEpisodeListRequestFocuse(true);
                }
            }
        } else if (event.getIndex() == VoddetailScrollEvent.PlayViewRequesetFocus) {
            if (null != onDemandTVPlayFragment) {
                Log.i(TAG, "resumePlay:1 ");
                onDemandTVPlayFragment.resumePlay();
                playViewLayout.setVisibility(View.VISIBLE);
            }
            //?????????????????????????????????????????????
            if (playViewLayout.getVisibility() == View.VISIBLE && playViewLayout.isFocusable()) {
                playViewLayout.requestFocus();
            } else if (mplayRel.getVisibility() == View.VISIBLE && mplayRel.isFocusable()) {
                mplayRel.requestFocus();
            } else if (col_rel.getVisibility() == View.VISIBLE && col_rel.isFocusable()) {
                col_rel.requestFocus();
            }
            windowCount = 0;
        }

//        if (event.getIndex() == 1 || event.getIndex() == 10){
//            if (null != onDemandTVPlayFragment){
//                onDemandTVPlayFragment.pausePlay();
////                playViewLayout.setVisibility(View.INVISIBLE);
//            }
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (null != detailRootView){
//                        //??????????????????
//                        detailRootView.smoothScrollTo(0, (int) listLayout.getY());
//                        windowCount = 1;
//                    }
//                }
//            }, 50);
//        }else if (event.getIndex() == 2){
//            if (null != onDemandTVPlayFragment){
//                onDemandTVPlayFragment.pausePlay();
////                playViewLayout.setVisibility(View.INVISIBLE);
//            }
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    //????????????
//                    detailRootView.smoothScrollTo(0,10000);
//                    windowCount = 2;
//                }
//            }, 50);
//        }else if (event.getIndex() == 0){
//            if (null != onDemandTVPlayFragment){
//                Log.i(TAG, "resumePlay:1 ");
//                onDemandTVPlayFragment.resumePlay();
//                playViewLayout.setVisibility(View.VISIBLE);
//            }
//            //?????????????????????????????????????????????
//            if (playViewLayout.getVisibility() == View.VISIBLE && playViewLayout.isFocusable()){
//                playViewLayout.requestFocus();
//            }else if (mplayRel.getVisibility() == View.VISIBLE && mplayRel.isFocusable()){
//                mplayRel.requestFocus();
//            }
////            else if (buy_rel.getVisibility() == View.VISIBLE && buy_rel.isFocusable()){
////                buy_rel.requestFocus();
////            }
//            else if (col_rel.getVisibility() == View.VISIBLE && col_rel.isFocusable()){
//                col_rel.requestFocus();
//            }
//            windowCount = 0;
//        }else if (event.getIndex() == VoddetailScrollEvent.EpisodeListRequesetFocus){
//            //???????????????????????????????????????????????????
//            if (episodes_lly.getVisibility() == View.VISIBLE && chooseEpisodeList.getVisibility() == View.VISIBLE && null != chooseEpisodeList.getAdapter()){
//
//                if (mVODDetail.getVODType().equals("3") || DetailCommonUtils.isShowSerieslayout(mVODDetail.getCmsType())) {
//                    //20??????????????????
//                    if (null != chooseEpisodeList.getLayoutManager() && null != chooseEpisodeList.getLayoutManager().findViewByPosition(10)){
//                        chooseEpisodeList.getLayoutManager().findViewByPosition(10).requestFocus();
//                    }else{
//                        chooseEpisodeList.requestFocus();
//                    }
//                } else {
//                    //8??????????????????
//                    if (null != chooseEpisodeList.getLayoutManager() && null != chooseEpisodeList.getLayoutManager().findViewByPosition(4)){
//                        chooseEpisodeList.getLayoutManager().findViewByPosition(4).requestFocus();
//                    }else{
//                        chooseEpisodeList.requestFocus();
//                    }
//                }
//
//            }
//        }
    }

    public void refreshBookmark(PlayVodBean bean) {
        if (mDetailPresenter.isFromClick()) {
            mDetailPresenter.setFromClick(false);
            return;
        }
        //???????????????????????????????????????????????????ANR
        if (null != mVODDetail && null != mVODDetail.getEpisodes() && mVODDetail.getEpisodes().size() > 0) {
            isCanClick = false;
        }
        SuperLog.debug(TAG, "onActivityResult bookmark--->" + "videoId:" + bean.getVodId() + ",epsodeId:" + bean.getEpisodeId() + ",sitcomNO:" + bean.getSitcomNO() + ",breakpoint:" + bean.getBookmark());
        Bookmark bookmark = new Bookmark();
        bookmark.setSitcomNO(bean.getSitcomNO());
        bookmark.setRangeTime(bean.getBookmark());
        bookmark.setSubContentID(bean.getEpisodeId());
        VODDetail vodDetail = mDetailPresenter.getVODDetail();
        vodDetail.setBookmark(bookmark);
        mDetailPresenter.setVODDetail(vodDetail);

        //?????????????????????????????????
        voddetailEpsiodesUtils.refreshBookMark(bookmark, new VoddetailEpsiodesUtils.GetEpisodeCallback() {
            @Override
            public void getEpisode(List<Episode> episodes, Episode episode) {

                voddetailEpsiodesUtils.setBookmark(episodes, episode);

                if (mplayRel != null && mplayRel.getVisibility() == View.VISIBLE) {
                    mplayRel.setTag(vodDetail);
                }
//                setButtonInfo(vodDetail);

                setFavoriteStatus(isCollection);
                //TODO
                if (mDetailPresenter.isClickEpisode()) {
                    if (chooseAdapter != null) {
                        chooseAdapter.setNeedRequestFocus(true);
                    }
                    if (varietyEpisodeAdapter != null) {
                        varietyEpisodeAdapter.setNeedRequestFocus(true);
                    }
                    setVodEpsiodes(vodDetail);
                    SuperLog.debug(TAG, "refreshBookmark->isClickEpisode:" + mDetailPresenter.isClickEpisode());
                } else {
                    setVodEpsiodes(vodDetail);
                }
                //???????????????????????????????????????????????????ANR
                if (null != mVODDetail && null != mVODDetail.getEpisodes() && mVODDetail.getEpisodes().size() > 0) {
                    mHandler.sendEmptyMessageDelayed(REFRESH_EPSIODE_CANCLICK, 21);
                }
            }

            @Override
            public void getEpisodeFail() {
                Log.i(TAG, "getEpisodeFail: ");
            }
        });
    }

    private BrowseFrameLayout.OnFocusSearchListener mOnFocusSearchListener = new BrowseFrameLayout.OnFocusSearchListener() {
        @Override
        public View onFocusSearch(View focused, int direction) {
            return null;
        }
    };

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause: voddetail");
        super.onPause();
        activity_state = activity_pause;
        mDescText.setSelected(false);
        stopXiri();

        if (null != onDemandTVPlayFragment) {
            onDemandTVPlayFragment.pausePlay();
            if (windowState == 0) {
                onDemandTVPlayFragment.setSurfaceViewSize(0, 0);
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null != playViewWindow && null != onDemandTVPlayFragment && DetailCommonUtils.canPlay(is4KSource)) {
                        playViewWindow.setVisibility(View.INVISIBLE);
                    }
                }
            }, 500);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SuperLog.debug(TAG, "onDestroy");
        RxApiManager.get().cancelAll();
        mVODDetail = null;
        mFocusScene = null;
        mHandler.removeCallbacksAndMessages(null);

        ActivityStackControlUtil.remove(new WeakReference<Activity>(this));
        topicId = null;

        if (null != onDemandTVPlayFragment) {
            onDemandTVPlayFragment.pausePlay();
        }
        onDemandTVPlayFragment = null;
    }

    private void startXiri() {
        SuperLog.info2SD(TAG, "VodDetailActivity startXiri");
        mFeedback = new Feedback(this);
        mFocusScene = new Scene(this);
        mFocusScene.init(new ISceneListener() {
            @Override
            public String onQuery() {
                return "{\"_scene\":\"com.pukka.ydepg.DETAIL\",\"_commands\":{\"key1\":[\"$W(collect)\"],\"key2\":[\"$W(cancel_collect)\"],\"key3\":[\"$W(play)\"],\"key4\":[\"$W(history)\"],\"key5\":[\"$P(_EPISODE)\"]},\"_fuzzy_words\":{\"collect\":[\"??????\"],\"cancel_collect\":[\"????????????\"],\"play\":[\"??????\"],\"history\":[\"????????????\", \"????????????\", \"????????????\"],\"_EPISODE\":[\"_EPISODE\"]}}";
//                return "{\"_scene\":\"com.pukka.ydepg.DETAIL\",\"_commands\":{\"key1\":[\"$W(collect)\"],\"key2\":[\"$W(cancel_collect)\"],\"key3\":[\"$W(play)\"],\"key4\":[\"$W(history)\"],\"key5\":[\"$W(_EPISODE)\"]},\"_fuzzy_words\":{\"collect\":[\"??????\"],\"cancel_collect\":[\"????????????\"],\"play\":[\"??????\"],\"history\":[\"????????????\", \"????????????\", \"????????????\"],\"_EPISODE\":[\"?????????\"]}}";
            }

            @Override
            public void onExecute(Intent intent) {
                RefreshManager.getInstance().getScreenPresenter().exit();
                Log.i(TAG, "onExecute: " + "??????scene???scene: " + intent.getStringExtra("_scene") + " command: " + intent.getStringExtra("_command ") + " action " + intent.getStringExtra("_action ") + " index: " + intent.getStringExtra("index "));
                mFeedback.begin(intent);
                if (intent.hasExtra("_scene") && intent.getStringExtra("_scene").equals("com.pukka.ydepg.DETAIL") && null != mDetailPresenter) {
                    if (intent.hasExtra("_command")) {
                        String command = intent.getStringExtra("_command");
                        SuperLog.info2SD(TAG, "_command" + command);
                        if ("key1".equals(command)) {
                            Log.i(TAG, "onExecute: ????????????");
                            mFeedback.feedback("??????", Feedback.EXECUTION);
                            if (isCollection) {//?????????????????????
                                EpgToast.showToast(NewVodDetailActivity.this, "????????????????????????");
                                return;
                            }
                            mDetailPresenter.setCollection(mVODDetail, isCollection);
                        } else if ("key2".equals(command)) {
                            mFeedback.feedback("????????????", Feedback.EXECUTION);
                            if (!isCollection) {
                                EpgToast.showToast(NewVodDetailActivity.this, "???????????????????????????");
                                return;
                            }
                            mDetailPresenter.setCollection(mVODDetail, isCollection);
                        } else if ("key3".equals(command)) {
                            if (canPrePlay() && null != onDemandTVPlayFragment) {
                                return;
                            }
                            voicePlayLastEpisode("??????", false);
                        } else if ("key4".equals(command)) {
                            if (null != intent && null != intent.getExtras() && intent.getExtras().getString("_rawtext") != null && (intent.getExtras().getString("_rawtext").equals("????????????") || intent.getExtras().getString("_rawtext").equals("??????????????????"))) {
                                voicePlayLastEpisode("????????????", true);
                            } else if (null != intent && null != intent.getExtras() && intent.getExtras().getString("_rawtext") != null && (intent.getExtras().getString("_rawtext").equals("????????????") || intent.getExtras().getString("_rawtext").equals("????????????"))) {
                                mFeedback.feedback("????????????", Feedback.EXECUTION);
                                Intent intent1 = new Intent(NewVodDetailActivity.this, NewMyMovieActivity.class);
                                intent1.putExtra("id", "1");
                                startActivity(intent1);
                            }
                        } else if ("key5".equals(command)) {
                            int index = intent.getExtras().getInt("index");
                            SuperLog.info2SD(TAG, "index =" + intent.getExtras().getInt("index"));
                            voicePlayEpisodeByIndex(index, intent);
                        }
                    }
                }
            }
        });
    }

    public void activityStartXiri() {
        startXiri();
    }

    public void activityStoptXiri() {
        stopXiri();
    }

    private void voicePlayLastEpisode(String tip, boolean isLastEpisode) {

        mFeedback.feedback(tip, Feedback.EXECUTION);
        if (mVODDetail == null) {
            return;
        }

        if (!VodUtil.canPlay(is4KSource)) {
            EpgToast.showToast(NewVodDetailActivity.this, NewVodDetailActivity.this.getResources().getString(R.string.details_4k_warnning));
            return;
        }
        String type = mVODDetail.getVODType();
        mDetailPresenter.setButtonOrderOrSee(true);
        if (!TextUtils.isEmpty(type)) {
            if (type.equals("0")) {//????????????
                List<VODMediaFile> vodMediaFiles = mVODDetail.getMediaFiles();
                if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
                    mDetailPresenter.setXiri(true);
                    mDetailPresenter.setLastPlayUrl(lastPlayUrl);
                    mDetailPresenter.setLastPlayID(lastPlayID);
                    mDetailPresenter.playVOD(mVODDetail);
                } else {
                    if (!HeartBeatUtil.getInstance().isSubscribedByPrice(mVODDetail, "")) {
                        EpgToast.showToast(NewVodDetailActivity.this, "???????????????????????????");
                    } else {
                        EpgToast.showToast(NewVodDetailActivity.this, "???????????????");
                    }
                    return;
                }
            } else {//???????????????
                List<String> episodes = voddetailEpsiodesUtils.getmEpisodesCount();
                Bookmark bookmark = mVODDetail.getBookmark();
                if (bookmark != null && !TextUtils.isEmpty(bookmark.getSitcomNO())) {
                    SuperLog.debug(TAG, "set bookmark--->" + "videoId:" + bookmark.getItemID() + ",sitcomNO:" + bookmark.getSitcomNO() + ",breakpoint:" + bookmark.getRangeTime());
                }
                if (episodes != null && episodes.size() != 0) {
                    Episode playEpisode = null;

                    VoddetailEpsiodesUtils.GetEpisodeCallback callback = new VoddetailEpsiodesUtils.GetEpisodeCallback() {
                        @Override
                        public void getEpisode(List<Episode> episodes, Episode episode) {
//                            mDetailPresenter.setXiri(true);
//                            mDetailPresenter.setLastPlayUrl(lastPlayUrl);
//                            mDetailPresenter.setLastPlayID(lastPlayID);
//                            mDetailPresenter.playVOD(episode);
                            playEpisode(episode);
                        }

                        @Override
                        public void getEpisodeFail() {
                            EpgToast.showToast(NewVodDetailActivity.this, "??????????????????!");
                        }
                    };


                    if (isLastEpisode) {
                        SuperLog.info2SD("countcount,", "mVODDetail.getVodNum()=" + mVODDetail.getVodNum() + "----episodes.size() - 1=" + (episodes.size() - 1));
                        if (Integer.valueOf(mVODDetail.getVodNum()) == episodes.size()) {
                            voddetailEpsiodesUtils.getEpisode(episodes.size() + "", callback);
                        } else {
                            EpgToast.showToast(NewVodDetailActivity.this, "??????????????????!");
                            return;
                        }
                    } else {
                        if (bookmark != null && !TextUtils.isEmpty(bookmark.getSitcomNO())) {

                            voddetailEpsiodesUtils.getEpisode(bookmark.getSitcomNO(), callback);

                        } else {
                            if (voddetailEpsiodesUtils.isPositive()) {
                                //???????????????
                                voddetailEpsiodesUtils.getEpisode("1", callback);
                            } else {
                                //??????????????????
                                voddetailEpsiodesUtils.getEpisode(episodes.size() + "", callback);
                            }

                        }
                    }
                } else {
                    EpgToast.showToast(NewVodDetailActivity.this, "???????????????????????????");
                    return;
                }
            }
        }
    }

    private void voicePlayEpisodeByIndex(int index, Intent intent) {
        Log.i(TAG, "VoddetailEpsiodesUtils: ?????????????????? " + index);

        mFeedback.feedback("??????????????????", Feedback.EXECUTION);
        if (mVODDetail == null) {
            return;
        }

        if (!VodUtil.canPlay(is4KSource)) {
            EpgToast.showToast(NewVodDetailActivity.this, NewVodDetailActivity.this.getResources().getString(R.string.details_4k_warnning));
            return;
        }

        String type = mVODDetail.getVODType();
        mDetailPresenter.setButtonOrderOrSee(true);
        String sitNum = String.valueOf(index);
        if (!TextUtils.isEmpty(type)) {
            SuperLog.info2SD(TAG, "type=" + type);
            if (type.equals("0")) {//????????????
                EpgToast.showToast(this, "??????????????????");
            } else {//???????????????
                Bundle bundle = intent.getExtras();
                String action = bundle.getString("_action");
                if (index == 0 && (action.equals(ACTION_NEXT) || action.equals(ACTION_PREV))) {
                    if (action.equals(ACTION_NEXT)) {//?????????
                        voddetailEpsiodesUtils.getNextOrPreEpisode(onDemandTVPlayFragment.sitcomNO, true, new VoddetailEpsiodesUtils.GetEpisodeCallback() {
                            @Override
                            public void getEpisode(List<Episode> episodes, Episode episode) {
                                SuperLog.info2SD(TAG, "playEpisode=" + episode);
//                                mDetailPresenter.setXiri(true);
//                                mDetailPresenter.setLastPlayUrl(lastPlayUrl);
//                                mDetailPresenter.setLastPlayID(lastPlayID);
//                                mDetailPresenter.playVOD(episode);
                                playEpisode(episode);
                            }

                            @Override
                            public void getEpisodeFail() {
                                Log.i(TAG, "VoddetailEpsiodesUtils: ??????");
                                EpgToast.showToast(NewVodDetailActivity.this, "???????????????");
                            }
                        });
//                        onDemandTVPlayFragment.nextPlay();
                    } else if (action.equals(ACTION_PREV)) {//?????????
//                        onDemandTVPlayFragment.prevPlay();
                        voddetailEpsiodesUtils.getNextOrPreEpisode(onDemandTVPlayFragment.sitcomNO, false, new VoddetailEpsiodesUtils.GetEpisodeCallback() {
                            @Override
                            public void getEpisode(List<Episode> episodes, Episode episode) {
                                SuperLog.info2SD(TAG, "playEpisode=" + episode);
//                                mDetailPresenter.setXiri(true);
//                                mDetailPresenter.setLastPlayUrl(lastPlayUrl);
//                                mDetailPresenter.setLastPlayID(lastPlayID);
//                                mDetailPresenter.playVOD(episode);
                                playEpisode(episode);
                            }

                            @Override
                            public void getEpisodeFail() {
                                Log.i(TAG, "VoddetailEpsiodesUtils: ??????");
                                EpgToast.showToast(NewVodDetailActivity.this, "???????????????");
                            }
                        });
                    } else {
                        EpgToast.showToast(NewVodDetailActivity.this, "???????????????");
                    }
                } else if (index != 0) {
                    voddetailEpsiodesUtils.getEpisode(sitNum, new VoddetailEpsiodesUtils.GetEpisodeCallback() {
                        @Override
                        public void getEpisode(List<Episode> episodes, Episode episode) {
                            SuperLog.info2SD(TAG, "playEpisode=" + episode);
//                        mDetailPresenter.setXiri(true);
//                        mDetailPresenter.setLastPlayUrl(lastPlayUrl);
//                        mDetailPresenter.setLastPlayID(lastPlayID);
//                        mDetailPresenter.playVOD(episode);
                            playEpisode(episode);
                        }

                        @Override
                        public void getEpisodeFail() {
                            Log.i(TAG, "VoddetailEpsiodesUtils: ??????");
                            EpgToast.showToast(NewVodDetailActivity.this, "???????????????");
                        }
                    });
                } else {
                    EpgToast.showToast(NewVodDetailActivity.this, "???????????????");
                }
//                List<Episode> episodes = mVODDetail.getEpisodes();
//                Log.i(TAG, "voicePlayEpisodeByIndex:  "+ episodes.size() + " index "+index);
//                if (episodes != null && episodes.size() >= index && index > 0) {
//
//                    for (int i = 0; i < episodes.size(); i++) {
//                        if (episodes.get(i).getSitcomNO().equals(sitNum)) {
//                            Episode playEpisode = episodes.get(i);
//                            if (null != playEpisode) {
//                                SuperLog.info2SD(TAG, "playEpisode=" + playEpisode);
//                                mDetailPresenter.setXiri(true);
//                                mDetailPresenter.setLastPlayUrl(lastPlayUrl);
//                                mDetailPresenter.setLastPlayID(lastPlayID);
//                                mDetailPresenter.playVOD(playEpisode);
//                                return;
//                            }
//                        }
//                    }
//                    EpgToast.showToast(NewVodDetailActivity.this, "???????????????");
//
//                } else if (index == 0){
//                    Bundle bundle = intent.getExtras();
//                    String action = bundle.getString("_action");
//                    if (action.equals(ACTION_NEXT)) {//?????????
//                        onDemandTVPlayFragment.nextPlay();
//                    } else if (action.equals(ACTION_PREV)) {//?????????
//                        onDemandTVPlayFragment.prevPlay();
//                    }else{
//                        EpgToast.showToast(NewVodDetailActivity.this, "???????????????");
//                    }
//                }else {
//                    EpgToast.showToast(NewVodDetailActivity.this, "???????????????");
//                }
            }
        }
    }

    private static final String ACTION_NEXT = "NEXT";//?????????
    private static final String ACTION_PREV = "PREV";//?????????

    //????????????????????????????????????????????????
    private void showSubscriptAndCategoryInfo(VODDetail vodDetail) {
        is4KSource = false;
        String info = DetailCommonUtils.getVodCategoryInfo(vodDetail);
        List<VODMediaFile> vodMediaFileList = null;
        String vodType = vodDetail.getVODType();
        //???????????????????????????
        if ("1".equals(vodType) || "3".equals(vodType)) {
            List<Episode> episodes = mVODDetail.getEpisodes();
            if (null != episodes && episodes.size() > 0 && null != episodes.get(0).getVOD()) {
                vodMediaFileList = episodes.get(0).getVOD().getMediaFiles();
            }
        } else {
            //?????????????????????
            vodMediaFileList = vodDetail.getMediaFiles();
        }
        if (vodMediaFileList != null && vodMediaFileList.size() != 0) {
            String definition = vodMediaFileList.get(0).getDefinition();
            //?????????????????????????????????????????????????????????
            if (!SubscriptControl.showSubscript(vodMediaFileList, mVODDetail)) {
//                definitionView.setVisibility(View.GONE);
                mInfoText.setText(info);
            } else {
                String definitionStr = getDefinition(definition);
                if (!TextUtils.isEmpty(definitionStr)){
                    if (!TextUtils.isEmpty(info)){
                        mInfoText.setText(definitionStr + " | "+info);
                    }else{
                        mInfoText.setText(definitionStr);
                    }
                }else{
                    mInfoText.setText(info);
                }
            }

            if ("2".equals(definition) && mRightIcon.getVisibility() == View.VISIBLE) {
                mRightIcon.setBackgroundResource(R.drawable.details_right_4k_icon);
            }
        }else{
            mInfoText.setText(info);
        }
    }

    private void stopXiri() {
        SuperLog.info2SD(TAG, "VodDetailActivity stopXiri");
        if (mFocusScene != null) {
            try {
                mFocusScene.release();
            } catch (IllegalArgumentException e) {
                SuperLog.info2SD(TAG,"mFocusScene Exception");
                e.getMessage();
            }

        }
    }

    private String lastPlayUrl;
    private String lastPlayID;

    @Subscribe
    public void onEvent(OnDemandBackEvent event) {
        lastPlayUrl = event.getUrl();
        lastPlayID = event.getId();
        if (chooseAdapter != null) {
            chooseAdapter.setLastPlayID(lastPlayID);
            chooseAdapter.setLastPlayUrl(lastPlayUrl);
        }
        if (varietyEpisodeAdapter != null) {
            varietyEpisodeAdapter.setLastPlayID(lastPlayID);
            varietyEpisodeAdapter.setLastPlayUrl(lastPlayUrl);
        }
    }


    public VodDetailPBSConfigUtils getVodDetailPBSConfigUtils() {
        return vodDetailPBSConfigUtils;
    }
    /****************************************??????????????????******************************************/

    /**
     * ?????????Fragment TAG
     */
    private static final String ONDEMAND_TAG = "ON_DEMAND";

    private XmppMessage mXmppMessage;

    //????????????
    @Override
    public void startPlay() {
        if (null != OrderConfigUtils.getInstance().getEvent() && OrderConfigUtils.getInstance().needShowPopWindow()){
            //???????????????????????????
            String contentId = "";
            if (mVODDetail.getVODType().equals("0") || mVODDetail.getVODType().equals("2")) {
                contentId = mVODDetail.getCode();
            } else if (null != voddetailEpsiodesUtils.getSelesctedEpisode()) {
                contentId = voddetailEpsiodesUtils.getSelesctedEpisode().getVOD().getCode();
            }
            OrderConfigUtils.getInstance().config(contentId, new OrderConfigUtils.ConfigCallBack() {
                @Override
                public void configDone() {
                    if (isFinishing()){
                        return;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            vodStartPlay();
                        }
                    });
                }
            });
        }else{
            vodStartPlay();
        }
    }

    private void vodStartPlay(){
        SuperLog.debug(TAG, "orderFinish: playvod?????????????????????");
        if (isFinishing()) {
            return;
        }
        //??????pbs????????????
        String actionType = "";
        if (TextUtils.isEmpty(pbs_appPointedId)) {
            //?????????????????????????????????????????????
            actionType = PbsUaConstant.ActionType.NORMAL_PLAY;
        } else {
            actionType = PbsUaConstant.ActionType.RECOMMEND_PLAY;
        }
        String subContentId = "";
        if (episodes_lly.getVisibility() == View.VISIBLE && null != voddetailEpsiodesUtils.getSelesctedEpisode()) {
            //?????????
            subContentId = voddetailEpsiodesUtils.getSelesctedEpisode().getVOD().getID();
        }
        PbsUaService.report(Play.getPlayData(actionType, mVODDetail.getID(), pbs_appPointedId, pbs_recommendType, pbs_sceneId, subContentId));
        //????????????????????????????????????????????????????????????
        pbs_appPointedId = "";
        pbs_recommendType = "";
        pbs_sceneId = "";
        resetPlayButtonState();
        if (MultViewUtils.getMultType(mVODDetail, voddetailEpsiodesUtils, this, true)) {
            boolean canPrePlay = DetailCommonUtils.canPlay(is4KSource);
            if (!canPrePlay) {
                SuperLog.debug(TAG, "orderFinish: ?????????????????????");
                if (null == playViewFullWindow) {
                    return;
                }
                playViewFullWindow.setVisibility(View.VISIBLE);
                if (null != onDemandTVPlayFragment) {
                    switchWindow(3);
                }
                Bundle bundle = new Bundle();
                bundle.putBoolean(BasePlayFragment.ISPLAYBACK, getIntent().getBooleanExtra(BasePlayFragment.ISPLAYBACK, false));
                bundle.putSerializable(XmppManager.XML_MESSAGE, mXmppMessage);

                onDemandTVPlayFragment = new NewBrowseTVPlayFragment();
                onDemandTVPlayFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.vod_playview_fullWindow, onDemandTVPlayFragment, ONDEMAND_TAG).commitAllowingStateLoss();
                onDemandTVPlayFragment.setFullWindow(true);
                if (isNeedFullWindow) {
                    onDemandTVPlayFragment.fitSize(1);
                }

                //???????????????????????????????????????
                windowState = 1;
                detailRootView.setVisibility(View.GONE);
                playViewFullWindow.setFocusable(true);
                playViewFullWindow.requestFocus();

            } else {
                SuperLog.debug(TAG, "orderFinish: ????????? initFragment");
                if (null == playViewTip) {
                    return;
                }
                if (windowState == 1) {
                    playViewTip.setVisibility(View.GONE);
                    manbanLayout.setVisibility(View.GONE);
                    mComefromLayout.setVisibility(View.GONE);
                } else if (windowState == 0) {
                    playViewTip.setVisibility(View.VISIBLE);
                    manbanLayout.setVisibility(View.VISIBLE);
                    mComefromLayout.setVisibility(View.VISIBLE);
                }
                if (null == onDemandTVPlayFragment) {
                    initFragment();
                } else {
//                onDemandTVPlayFragment.pausePlay();
                    getSupportFragmentManager().beginTransaction().remove(onDemandTVPlayFragment).commitAllowingStateLoss();
                    onDemandTVPlayFragment = null;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!isFinishing()) {
                                initFragment();
                            }
                        }
                    }, 200);
                }
            }
        }
    }



    private void initFragment() {
        Log.i(TAG, "initFragment: initFragment");

        Bundle bundle = new Bundle();
        bundle.putBoolean(BasePlayFragment.ISPLAYBACK, getIntent().getBooleanExtra(BasePlayFragment.ISPLAYBACK, false));
        //??????H5??????????????????????????????(??????H5?????????????????????)
        if (!TextUtils.isEmpty(getIntent().getStringExtra(OnDemandVideoActivity.PLAY_VOD_BEAN))) {
            bundle.putString(OnDemandVideoActivity.PLAY_VOD_BEAN, getIntent().getStringExtra(OnDemandVideoActivity.PLAY_VOD_BEAN));
        }
        bundle.putSerializable(XmppManager.XML_MESSAGE, mXmppMessage);

        onDemandTVPlayFragment = new NewBrowseTVPlayFragment();
        onDemandTVPlayFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_playvideo, onDemandTVPlayFragment, ONDEMAND_TAG).commitAllowingStateLoss();
//        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ONDEMAND_TAG);
//        if (null == fragment) {
//            onDemandTVPlayFragment = (NewBrowseTVPlayFragment) Fragment.instantiate(this, NewBrowseTVPlayFragment.class.getName(), bundle);
//            getSupportFragmentManager().beginTransaction().replace(R.id.frame_playvideo, onDemandTVPlayFragment, ONDEMAND_TAG).commitAllowingStateLoss();
//        } else {
//            getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    initFragment();
//                }
//            },200);
//        }
        if (isNeedFullWindow) {
            //??????????????????
//            playViewLayout.performClick();
            performFullScreen();

//            onDemandTVPlayFragment.fitSize(1);
        } else {
            playViewFullWindow.setVisibility(View.GONE);
        }
    }

    //???????????????????????????
    private void performFullScreen() {
        if (isFinishing()) {
            return;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (canScroll) {
                    switchWindow(1);
                    //??????????????????
                    playViewFullWindow.setVisibility(View.GONE);
                } else {
                    performFullScreen();
                }
            }
        }, 100);
    }

    //????????????????????????????????????????????????
    public void onPlayerPrepare() {
        canScroll = true;
        if (canPrePlay() && !isNeedFullWindow) {
            playViewFullWindow.setVisibility(View.GONE);
        }
        playViewloading.setVisibility(View.GONE);
    }

    //??????????????? 0?????? 1?????? 3????????????  4?????????????????????
    public void switchWindow(int state) {

        if (null == onDemandTVPlayFragment) {
            return;
        }

        if (null == onDemandTVPlayFragment.getmPlayView()) {
            return;
        }

        if (state == 1 || state == 4) {
            windowState = 1;

            showPreplayTryseeTip(false);
            playViewTip.setVisibility(View.GONE);
            manbanLayout.setVisibility(View.GONE);
            mComefromLayout.setVisibility(View.GONE);
            playViewLayout.setPadding(0, 0, 0, 0);
            RelativeLayout.LayoutParams parentParams = (RelativeLayout.LayoutParams) playViewLayout.getLayoutParams();
            parentParams.setMargins(0, 0, 0, 0);
            playViewLayout.setLayoutParams(parentParams);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playViewWindow.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            params.width = (int) getResources().getDimension(R.dimen.window_width);
            params.height = (int) getResources().getDimension(R.dimen.window_height);
            playViewWindow.setLayoutParams(params);
            if (null != onDemandTVPlayFragment) {
                if (state == 1){
                    onDemandTVPlayFragment.fitSize(1);
                }else{
                    //??????????????????????????????????????????????????????????????????
                    onDemandTVPlayFragment.fitSize(4);
                }

            }


            playViewSkipTip.setVisibility(View.GONE);
            //???????????????????????????????????????
            detailContentView.setVisibility(View.GONE);
            listLayout.setVisibility(View.GONE);
            playViewWindow.setFocusable(true);
            playViewWindow.requestFocus();
        } else if (state == 0) {
            resetPlayButtonState();
            windowState = 0;
            //?????????????????????????????????????????????
            isNeedFullWindow = false;
            if (!onDemandTVPlayFragment.isCanReplayByTrySee() && onDemandTVPlayFragment.getmPlayView().isPause()){
                showPreplayTryseeTip(true);
            }

            //????????????????????????????????????UI

            if (preplayTryseeTip.getVisibility() != View.VISIBLE){
                playViewTip.setVisibility(View.VISIBLE);
                manbanLayout.setVisibility(View.VISIBLE);
            }
            mComefromLayout.setVisibility(View.VISIBLE);

            if (playViewFullWindow.getVisibility() == View.VISIBLE) {
                playViewFullWindow.setVisibility(View.GONE);
            }
            RelativeLayout.LayoutParams parentParams = (RelativeLayout.LayoutParams) playViewLayout.getLayoutParams();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playViewWindow.getLayoutParams();
            int marginTop = (int) getResources().getDimension(R.dimen.margin_65);
            int marginStart = (int) getResources().getDimension(R.dimen.margin_72_1);
            parentParams.setMargins(marginStart, marginTop, 0, 0);
            int padding = (int) getResources().getDimension(R.dimen.margin_2);
            playViewLayout.setPadding(padding, padding, padding, padding);
            playViewLayout.setLayoutParams(parentParams);
            params.width = (int) getResources().getDimension(R.dimen.new_voddetail_playview_width);
            params.height = (int) getResources().getDimension(R.dimen.new_voddetail_playview_height);
            playViewWindow.setLayoutParams(params);
            onDemandTVPlayFragment.fitSize(0);

            detailContentView.setVisibility(View.VISIBLE);
            listLayout.setVisibility(View.VISIBLE);
            playViewWindow.setFocusable(false);
//            playViewLayout.setFocusable(true);
//            playViewLayout.requestFocus();


            backToWindowRequestFocus();
        } else if (state == 3) {
            windowState = 0;
            getSupportFragmentManager().beginTransaction().remove(onDemandTVPlayFragment).commitAllowingStateLoss();
            onDemandTVPlayFragment = null;
            detailRootView.setVisibility(View.VISIBLE);
            playViewFullWindow.setVisibility(View.GONE);
            detailRootView.scrollTo(0, 0);
            backToWindowRequestFocus();
        }

    }

    //???????????????????????? 0?????????1??????
    public void showSkipToast(int state) {
        Log.i(TAG, "showSkipToast:  " + onDemandTVPlayFragment.isNeedToSkip);

        if (null == onDemandTVPlayFragment) {
            return;
        }

        playViewSkipTip.setVisibility(View.VISIBLE);
        if (state == 0) {
            playViewSkipTip.setText(R.string.player_skip_start);
        } else if (state == 1) {
            playViewSkipTip.setText(R.string.player_skip_end);
        }
        //????????????????????????
        mComefromLayout.setVisibility(View.GONE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && null != playViewSkipTip) {
                    playViewSkipTip.setText("");
                    playViewSkipTip.setVisibility(View.GONE);
                }

                if (windowState == 0) {
                    if (!isFinishing() && null != mComefromLayout) {
                        mComefromLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        }, 3000);
    }

    //??????????????????????????????????????????
    public boolean isSkipToastShowing() {
        Log.i(TAG, "isSkipToastShowing: " + (playViewSkipTip.getVisibility() == View.VISIBLE));
        return playViewSkipTip.getVisibility() == View.VISIBLE;
    }

    /*************************************************??????????????????************************************/

    public void getIntentInfo() {
        Serializable xmlInfo = getIntent().getSerializableExtra(XmppManager.XML_MESSAGE);
        if (null != xmlInfo) {
            mXmppMessage = (XmppMessage) xmlInfo;
        }
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @return
     */

    private boolean isVodPlaying() {
        return null != onDemandTVPlayFragment && onDemandTVPlayFragment.isVodPlayNow();
    }

    /**
     * ??????????????????????????????????????????
     *
     * @return
     */

    private boolean isVodPause() {
        return null != onDemandTVPlayFragment && onDemandTVPlayFragment.isVodPause();
    }

    /**
     * ????????????????????????????????????(???????????????
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
     * ???????????????seekto
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
     * ????????????????????????????????????????????????
     *
     * @return 0????????????1???????????????2,4,8,16,32???????????????-2,-4,-8,-16,-32????????????
     */

    public int getFastSpeed() {
        if (null == onDemandTVPlayFragment) {
            return 0;
        }
        return onDemandTVPlayFragment.getforwardAndBackRate();
    }

    /**
     * ?????????1?????????0??????
     *
     * @param state
     */
    public void playerOrPause(int state) {
        if (null != onDemandTVPlayFragment) {
            onDemandTVPlayFragment.playerOrPause(state);
        }
    }

    /**
     * ?????????????????????url,???????????????
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

    public boolean canPrePlay() {
        return DetailCommonUtils.canPlay(is4KSource);
    }

    public boolean isPlaying() {
        if (windowState == 1) {
            //????????????,?????????????????????
            return true;
        } else {
            if (!canPrePlay()) {
                //????????????????????????????????????????????????
                return false;
            } else {
                if (null != onDemandTVPlayFragment) {
                    //?????????????????????,?????????????????????????????????
                    return onDemandTVPlayFragment.isVodPlayNow();
                } else {
                    //?????????????????????,??????????????????
                    return false;
                }
            }
        }
    }

    //??????????????????
    private void playEpisode(Episode episode) {
        OrderConfigUtils.getInstance().clear();
        Log.i(TAG, "?????????????????????????????? playEpisode: ");
        if (null != onDemandTVPlayFragment) {
            NewBrowseTVPlayFragment.OnDemandHandler handler = onDemandTVPlayFragment.getmHandler();
            if (null != handler) {
                Message message = Message.obtain();
                message.what = BrowseTVPlayFragment.EPISODE_PLAY;
                Bundle bundle = new Bundle();
                bundle.putString("SitcomNO", episode.getSitcomNO());
                bundle.putString("EpisodesId", episode.getVOD().getID());
                bundle.putString("MediaId", episode.getVOD().getMediaFiles().get(0).getID());
                bundle.putString("elapseTime", episode.getVOD().getMediaFiles().get(0).getElapseTime());
                bundle.putSerializable("episodeVod", episode.getVOD());
                message.setData(bundle);
                onDemandTVPlayFragment.getmHandler().sendMessage(message);
            }
        } else {
            mDetailPresenter.setFromClick(true);
            mDetailPresenter.setButtonOrderOrSee(true);
            mDetailPresenter.setLastPlayUrl(lastPlayUrl);
            mDetailPresenter.setLastPlayID(lastPlayID);
            mDetailPresenter.playVOD(episode);
        }
    }

    private boolean canClick = true;

    //??????????????????????????? ?????????????????? ????????????????????????????????????????????????????????????????????????????????????????????????????????????
    public void resetPlayButtonState() {
        if (null != chooseAdapter) {
            chooseAdapter.resetState();
        }
        if (null != varietyEpisodeAdapter) {
            varietyEpisodeAdapter.resetState();
        }
        canClick = true;
    }

    private boolean hasEpisodes() {
        return (episodes_lly.getVisibility() == View.VISIBLE && chooseEpisodeList.getVisibility() == View.VISIBLE && null != chooseEpisodeList.getAdapter());
    }

    public boolean isAlreadyShowPushToast() {
        return alreadyShowPushToast;
    }

    public void setAlreadyShowPushToast(boolean alreadyShowPushToast) {
        this.alreadyShowPushToast = alreadyShowPushToast;
    }

    public NewBrowseTVPlayFragment getOnDemandTVPlayFragment() {
        return onDemandTVPlayFragment;
    }

    public String getPbs_recommendType() {
        return pbs_recommendType;
    }

    public void setPbs_recommendType(String pbs_recommendType) {
        this.pbs_recommendType = pbs_recommendType;
    }

    public String getPbs_sceneId() {
        return pbs_sceneId;
    }

    public void setPbs_sceneId(String pbs_sceneId) {
        this.pbs_sceneId = pbs_sceneId;
    }

    public String getPbs_appPointedId() {
        return pbs_appPointedId;
    }

    public void setPbs_appPointedId(String pbs_appPointedId) {
        this.pbs_appPointedId = pbs_appPointedId;
    }

    //????????????????????????????????????????????? -1????????? 0????????????  1????????????  2????????????
    private int focusedCount = -1;

    //?????????????????????????????????????????????
    public void backToWindowRequestFocus() {
        if (focusedCount == 0) {
            //??????????????????
            playViewLayout.requestFocus();
        } else if (focusedCount == 1) {
            //??????????????????
            mplayRel.requestFocus();
        } else if (focusedCount == 2) {
            //??????????????????
            if (chooseAdapter != null) {
                chooseAdapter.setNeedRequestFocus(true);
            }
            if (varietyEpisodeAdapter != null) {
                varietyEpisodeAdapter.setNeedRequestFocus(true);
            }
            if (mVODDetail.getVODType().equals("3") || DetailCommonUtils.isShowSerieslayout(mVODDetail.getCmsType())) {
                mDetailPresenter.setIsSeries(0);
                if (DetailCommonUtils.isShowReverselayout(mVODDetail.getCmsType())) {
                    mDetailPresenter.setIsReverse(1);
                } else {
                    mDetailPresenter.setIsReverse(0);
                }
                showEpisodes(mVODDetail);
            } else {
                mDetailPresenter.setIsSeries(1);
                if (DetailCommonUtils.isShowReverselayout(mVODDetail.getCmsType())) {
                    mDetailPresenter.setIsReverse(1);
                } else {
                    mDetailPresenter.setIsReverse(0);
                }
                showVarietyEpisodes(mVODDetail);
            }
        } else {
            if (canPrePlay()) {
                playViewLayout.requestFocus();
            } else if (mplayRel.getVisibility() == View.VISIBLE && mplayRel.isFocusable()) {
                mplayRel.requestFocus();
            } else {
                col_rel.requestFocus();
            }
        }

        //???????????????
        focusedCount = -1;
    }

    private String getDefinition(String definition){
        //??????????????????????????????4K??????

        if (SubscriptControl.iszj4KVOD(mVODDetail)) {
            is4KSource = true;
            return "???4K";
        }

        if ("2".equals(definition)) {
            is4KSource = true;
            return "4K";
        }
        is4KSource = false;
        return "";
    }

    public String getmJiutianTrackerUrl() {
        return mJiutianTrackerUrl;
    }

    //??????/???????????????????????????????????????
    public void showPreplayTryseeTip(boolean isShow){
        Log.i(TAG, "showPrplayTryseeTip: "+isShow);
        if (isShow){
            //????????????????????????
            preplayTryseeTip.setVisibility(View.VISIBLE);
            //?????????????????????ok???????????????
            playViewTip.setVisibility(View.GONE);
            //????????????
            manbanLayout.setVisibility(View.GONE);

        }else{
            //????????????????????????
            preplayTryseeTip.setVisibility(View.GONE);
            //?????????????????????ok???????????????
            playViewTip.setVisibility(View.VISIBLE);
            //????????????
            manbanLayout.setVisibility(View.VISIBLE);
        }


    }

    //??????????????????????????????itemId
    public String getJiutianItemId(){
        if (!TextUtils.isEmpty(mJiutianItemId)){
            //?????????intent?????????itemid
            return mJiutianItemId;
        }else{
            //??????????????????vod?????????id???cpid??????
            return (mVODDetail.getCpId() + "|"+ mVODDetail.getID());
        }
    }
}