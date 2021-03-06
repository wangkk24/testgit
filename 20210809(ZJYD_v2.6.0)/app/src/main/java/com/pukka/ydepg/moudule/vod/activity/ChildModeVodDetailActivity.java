package com.pukka.ydepg.moudule.vod.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.widget.BrowseFrameLayout;
import androidx.leanback.widget.HorizontalGridView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.iflytek.xiri.Feedback;
import com.iflytek.xiri.scene.ISceneListener;
import com.iflytek.xiri.scene.Scene;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.VerticalScrollTextView;
import com.pukka.ydepg.common.http.bean.node.XmppMessage;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.CreateBookmarkCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.DeleteBookmarkCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.PlayURLCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODDetailCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODListCallBack;
import com.pukka.ydepg.common.http.v6bean.v6node.AlacarteChoosedContent;
import com.pukka.ydepg.common.http.v6bean.v6node.AuthorizeResult;
import com.pukka.ydepg.common.http.v6bean.v6node.BookMarkSwitchs;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.BookmarkItem;
import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.Genre;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.PlayVodBean;
import com.pukka.ydepg.common.http.v6bean.v6node.ProduceZone;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.SubjectVODList;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6request.CreateBookmarkRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.DeleteBookmarkRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.ReportVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.refresh.RefreshManager;
import com.pukka.ydepg.common.report.jiutian.JiutianService;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.extension.RecommendData;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaService;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Play;
import com.pukka.ydepg.common.report.ubd.scene.UBDPlay;
import com.pukka.ydepg.common.report.ubd.scene.UBDRecommendImpression;
import com.pukka.ydepg.common.report.ubd.scene.UBDSwitch;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.FastBlur;
import com.pukka.ydepg.common.utils.HeartBeatUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.PlayerAttriUtil;
import com.pukka.ydepg.common.utils.RxApiManager;
import com.pukka.ydepg.common.utils.ScoreControl;
import com.pukka.ydepg.common.utils.ShowBuyControl;
import com.pukka.ydepg.common.utils.SubscriptControl;
import com.pukka.ydepg.common.utils.TextUtil;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.customui.MiguQRViewPopWindow;
import com.pukka.ydepg.customui.PlayBackViewPopwindow;
import com.pukka.ydepg.customui.VodEpisodesView;
import com.pukka.ydepg.customui.VodSettingView;
import com.pukka.ydepg.customui.dialog.FullScreenDialog;
import com.pukka.ydepg.customui.dialog.ParentSetCenterDialog;
import com.pukka.ydepg.event.FinishPlayUrlEvent;
import com.pukka.ydepg.event.PlayUrlEvent;
import com.pukka.ydepg.event.PlayerSkipChangeEvent;
import com.pukka.ydepg.event.PlayerSpeedChangeEvent;
import com.pukka.ydepg.inf.IPlayListener;
import com.pukka.ydepg.inf.IPlayState;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.fragment.FocusInterceptor;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.children.report.ChildrenConstant;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.mytv.ZjYdUniAndPhonePayActivty;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewProductOrderActivity;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.OwnChoose.OwnChooseEvent;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.OwnChoose.OwnChoosePresenter;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.JumpToH5OrderUtils;
import com.pukka.ydepg.moudule.player.util.RemoteKeyEvent;
import com.pukka.ydepg.moudule.player.util.ViewkeyCode;
import com.pukka.ydepg.moudule.vod.NewAdapter.NewChildTotalEpisodeAdapter;
import com.pukka.ydepg.moudule.vod.adapter.ChooseChildEpisodeAdapter;
import com.pukka.ydepg.moudule.vod.adapter.DetailsRecommendAdapter;
import com.pukka.ydepg.moudule.vod.cache.VodDetailCacheService;
import com.pukka.ydepg.moudule.vod.cache.VoddetailUtil;
import com.pukka.ydepg.moudule.vod.playerController.VodPlayerControllerView;
import com.pukka.ydepg.moudule.vod.presenter.BlockingEvent;
import com.pukka.ydepg.moudule.vod.presenter.CollectionEvent;
import com.pukka.ydepg.moudule.vod.presenter.DeblockingEvent;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.vod.presenter.IVopRecPresenter;
import com.pukka.ydepg.moudule.vod.presenter.NewScoresEvent;
import com.pukka.ydepg.moudule.vod.presenter.ResetBlockEvent;
import com.pukka.ydepg.moudule.vod.presenter.VoddetailEpsiodePresenter;
import com.pukka.ydepg.moudule.vod.utils.BrowseEpsiodesUtils;
import com.pukka.ydepg.moudule.vod.utils.OrderConfigUtils;
import com.pukka.ydepg.moudule.vod.utils.VodDetailPBSConfigUtils;
import com.pukka.ydepg.moudule.vod.utils.VoddetailEpsiodesUtils;
import com.pukka.ydepg.moudule.vod.view.AlacarteChoosePopWindow;
import com.pukka.ydepg.moudule.vod.view.FocusVerticalGridView;
import com.pukka.ydepg.moudule.vod.view.VodDetailDataView;
import com.pukka.ydepg.moudule.voice.VoiceVodListener;
import com.pukka.ydepg.moudule.voice.XiriVoiceVodUtil;
import com.pukka.ydepg.service.NtpTimeService;
import com.pukka.ydepg.util.EventLogger;
import com.pukka.ydepg.util.PlayUtil;
import com.pukka.ydepg.view.PlayView;
import com.pukka.ydepg.xmpp.XmppManager;
import com.pukka.ydepg.xmpp.bean.XmppSuccessEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pukka.ydepg.moudule.vod.utils.RemixRecommendUtil.APPPINEDID_CHILD;

/**
 * ??????????????????
 *
 * @FileName: VodDetailActivity.java
 * @author: ld
 * @date: 2017-12-19
 */
public class ChildModeVodDetailActivity extends BaseActivity implements VodDetailDataView, IPlayListener, PlayURLCallBack, VoiceVodListener, VODDetailCallBack {

    public static final String TAG = "ChildModeVodDetail";

    public static final String VOD_ID = "vod_id";

    public static final String ORGIN_VOD = "orgin_vod";

    public static final String SUBJECT_ID = "subject_id";

    private static final int RequestDetail = 1002;

    private static final int SET_BG = 1001;

    private static final int EPSIODE_REQUESTFOCUS = 1003;

    private static final int REFRESH_EPSIODE_CANCLICK = 1004;

    //????????????????????????????????????
    public static final int BACK_PLAY = 111112;

    public static final int NEXT_PLAY = 111113;

    //??????BrowseTVPlayFragment??????
    public static final int EPISODE_PLAY = 11113;

    public static final int HIDE_TRY_SEE_HINT = 111115;

    public static final int SWITCH_TRY_SEE_HINT = 111141;

    public static final int UPDATE_DURATION = 111116;

    public static final int HIDE_MARKETING_CONTENT = 111117;

    public static final int DECREMENT_REST_TIME = 111118;

    public static final int KEY_CLICK_RECORD = 111119;

    private List<Float> speedList = new ArrayList<>();

    //vod????????????
    @BindView(R.id.activity_detail_img_bg)
    ImageView mImgBb;
    //vod????????????????????????
    @BindView(R.id.activity_detail_shadow)
    ImageView mImgShaw;
    //vod?????????
    @BindView(R.id.details_description_title)
    TextView mTitleText;
    //vod?????????
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    //vod?????????????????????
    @BindView(R.id.details_description_star_layout)
    LinearLayout mStarLayout;
    //vod?????????
    @BindView(R.id.details_description_score)
    TextView mScoreText;
    //vod???????????????
    @BindView(R.id.details_description_info)
    TextView mInfoText;
    //SD
    @BindView(R.id.details_description_video_info1)
    ImageView mVideoInfoImg1;
    @BindView(R.id.details_description_video_info2)
    ImageView mVideoInfoImg2;
    //vod???????????????
    @BindView(R.id.details_descriptions)
    VerticalScrollTextView mDescText;


    //???????????????
    @BindView(R.id.details_data_source)
    TextView mDataSourceText;
    //4k???????????????
    @BindView(R.id.details_4k_warnning)
    TextView m4KWarnning;

    //????????????
    @BindView(R.id.details_recommend_title)
    TextView detailsRecommendtitle;
    //????????????
    @BindView(R.id.details_recommend_view)
    RelativeLayout mRecommendLayout;
    //??????GridView
    @BindView(R.id.details_recommend)
    FocusVerticalGridView mRecommendList;
    //??????????????????
    @BindView(R.id.detail_root)
    BrowseFrameLayout mBrowseFrameLayout;
    //?????????
    @BindView(R.id.details_progress)
    ProgressBar detailsProgress;

    //????????????
    @BindView(R.id.play_rel)
    RelativeLayout mplayRel;

    @BindView(R.id.full_img)
    ImageView fullImg;

    @BindView(R.id.full_tv)
    TextView fulltv;

    //??????
    @BindView(R.id.col_rel)
    RelativeLayout col_rel;

    @BindView(R.id.favo_img)
    ImageView favoImg;

    @BindView(R.id.favo_tv)
    TextView favotv;

    //??????
    @BindView(R.id.buy_rel)
    RelativeLayout buy_rel;

    @BindView(R.id.buy_img)
    ImageView buyImg;

    @BindView(R.id.buy_tv)
    TextView buytv;

    //??????????????????
    @BindView(R.id.play_mode_rel)
    RelativeLayout playModeRel;

    @BindView(R.id.play_mode_img)
    ImageView playModeImg;

    @BindView(R.id.play_mode_tv)
    TextView playModetv;

    //????????????
    @BindView(R.id.timing_rel)
    RelativeLayout timingrel;

    @BindView(R.id.timing_img)
    ImageView timingImg;

    @BindView(R.id.timing_tv)
    TextView timingtv;


    @BindView(R.id.total_episodes)
    HorizontalGridView total_episodes_GridView;

    @BindView(R.id.choose_episodes)
    RecyclerView chooseEpisodeList;

    @BindView(R.id.episodes_lly)
    LinearLayout episodes_lly;


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
     * ???????????????
     */

    private PlayView mPlayView;

    /**
     * ????????????
     */
    @BindView(R.id.advert)
    ImageView advertImg;

    @BindView(R.id.scrollview)
    ScrollView detailScrollView;

    @BindView(R.id.continue_play)
    LinearLayout contiunePlaylly;

    @BindView(R.id.continue_time)
    TextView continueTimetv;

    @BindView(R.id.play_complete)
    TextView playCompletetv;


    @BindView(R.id.activity_shadow_two)
    ImageView activity_shadow_two;

//    private List<List<Episode>> totalEpisodes;

    private View completeDialogView;

    //???????????????
    private boolean isCollection;

    private VodDetailHandler mHandler;
    //????????????onResume???????????????vod??????
    private int requestNum = 0;

    //?????????????????????
    private boolean posterIsCreated;
    //????????????????????????
    private long mLastOnClickTime = 0;
    //??????????????????
    private static final long VALID_TIME = 500;

    private Scene mFocusScene;

    private Feedback mFeedback;

    //????????????????????????????????????????????????
    private boolean isCanClick = true;

    private ChooseChildEpisodeAdapter chooseAdapter;

    private NewChildTotalEpisodeAdapter totalEpisodeAdapter;

    /**
     * ??????????????????????????????
     */
    private boolean isFromSubscribeNotTrySee;


    private String showSubscribedMark;

    private boolean is4KSource = false;

    /**
     * ????????????keyback????????????????????????popwindow
     */
    private PlayBackViewPopwindow mPlayBackView;

    /**
     * ????????????????????????
     */
    private int mCurrentPlayState = -1;

    /**
     * ??????????????????0????????????1????????????
     */
    private String isFilm = "0";

    //??????????????????
    private PlayVodBean playVodBean = new PlayVodBean();

    //?????????url
    private String videoPath;

    //???????????????
    private String sitcomNO = "";

    //??????????????????????????????
    private String oldsitcomNO = "";

    //VOD?????????id
    private String subContentId = "";

    //??????vodId
    private String videoId = "";

    //???????????????
    private boolean isLookBack = false;

    //??????list
//    private List<Episode> episodeDetailList = new ArrayList<>();

    //???????????????
    private VodEpisodesView vodEpisodesView;
    private VodSettingView vodSettingView;

    //??????????????????
    private boolean isCompelete;

    //??????????????????
    private DetailPresenter mDetailPresenter;

    //?????????
    private long totalTimes;

    private long currentTimes;


    //????????????
    private XiriVoiceVodUtil mXiriVoiceVodUtil;

    private XmppMessage xmppmessage;

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

    public long tryToSeeTime = 5 * 60;

    //???????????????????????????????????????????????????????????????????????????
    private boolean isFirstFlag = true;

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

    //[UBD]????????????ID,?????????????????? ???????????????????????????=>vodDetail001
    private String ubd_appPointedId;
    //[UBD]??????ID  ??????????????????????????????sceneId      CVI?????????1   ????????????????????????????????????subjectId
    private String ubd_sceneId;
    //[UBD]???????????? ??????????????????????????????identifyType CVI?????????-2  ???????????????-1
    private String ubd_recommendType;

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

    /**
     * value 0:>=90min
     * value 1:>=10min <90min
     * value 2:<10min
     */
    private int vodTime;

    /**
     * voddetail??????
     */
    private VODDetail mVODDetail;

    private boolean isXmpp;

    //?????????????????????
    private PopupWindow mComfirmPopwindow;

    //??????????????????
    private FullScreenDialog mCompletePopwindow;


    private PopupWindow mSecondConfirmPopwindow;

    private long restRemainderTime;

    private TextView singletimetv;


    private StringBuilder mFormatBuilder;

    private Formatter mFormatter;

    private String action = UBDConstant.ACTION.PLAY_NORMAL;

    private ImageView epsiodeImg;

    private int keyClickCount;

    private View currentFocusView;

    private boolean isDestory;

    private ParentSetCenterDialog dialogSingleTime;

    private List<VOD> recmContentsList;

    private boolean isFirstStartFlag = true;

    private boolean isNeedBackToStart = false;

    //???????????????????????????
    private int mTotalEpisodeSelectIndex = -1;

    private boolean isNeedShowSkipBottomRecord = true;

    private boolean isNeedToSkip = false;

    private int speed = 1;

    //????????????????????????
    private boolean canSetSpeed = true;

    //??????????????????????????????
    private boolean canSetSkip = true;

    //??????????????????????????????????????????????????????????????????
    private boolean canOnKey = false;

    private BrowseEpsiodesUtils epsiodesUtils;

    private VoddetailEpsiodesUtils voddetailEpsiodesUtils;
    
    //?????????????????????presenter
    private IVopRecPresenter iVopRecPresenter;

    public static class VodDetailHandler extends Handler {

        private WeakReference<ChildModeVodDetailActivity> activity;

        public VodDetailHandler(ChildModeVodDetailActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                if (activity != null && null != activity.get()) {
                    switch (msg.what) {
                        case RequestDetail:
                            String vodId;
                            try {
                                vodId = activity.get().getIntent().getStringExtra(VOD_ID);
                                if (TextUtils.isEmpty(vodId) && null != activity.get().getIntent() && null != activity.get().getIntent().getData()) {
                                    vodId = activity.get().getIntent().getData().getQueryParameter("vodid");
                                }
                                if (!TextUtils.isEmpty(vodId) && activity.get().mDetailPresenter != null) {
                                    activity.get().epsiodesUtils = new BrowseEpsiodesUtils();
                                    activity.get().epsiodesUtils.getSimpleVod(vodId, new VoddetailEpsiodePresenter.GetSimpleVodCallback() {
                                        @Override
                                        public void getSimpleVodSuccess(VODDetail vodDetail) { }

                                        @Override
                                        public void getSimpleVodFail() { }
                                    });

                                    activity.get().voddetailEpsiodesUtils = new VoddetailEpsiodesUtils(vodId);
                                    activity.get().voddetailEpsiodesUtils.setChildMode(true);
                                    activity.get().voddetailEpsiodesUtils.getSimpleVod(vodId, new VoddetailEpsiodePresenter.GetSimpleVodCallback() {
                                        @Override
                                        public void getSimpleVodSuccess(VODDetail vodDetail) {
                                            activity.get().mDetailPresenter.setVODDetail(vodDetail);
                                            activity.get().showDetail(vodDetail,"",new ArrayList<>());
                                        }

                                        @Override
                                        public void getSimpleVodFail() {}
                                    });
                                }
                                activity.get().requestNum++;
                            } catch (Exception ex) {
                                SuperLog.error(TAG, ex);
                            }
                            break;
                        case SET_BG:
                            if (null != msg.obj) {
                                Bitmap mBitmap = (Bitmap) msg.obj;
                                if (activity.get().mImgBb != null) {
                                    activity.get().mImgBb.setImageBitmap(mBitmap);
                                }
                            }
                            break;
                        case EPSIODE_REQUESTFOCUS:
                            if (activity.get().chooseEpisodeList.getAdapter() instanceof ChooseChildEpisodeAdapter) {
                                int position = ((ChooseChildEpisodeAdapter) activity.get().chooseEpisodeList.getAdapter()).getBookmarkPosition();
                                if (position != -1) {
                                    View view = activity.get().chooseEpisodeList.getLayoutManager().findViewByPosition(position);
                                    if (view == null) {
                                        view = activity.get().chooseEpisodeList.findViewHolderForLayoutPosition(position).itemView;
                                    }
                                    if (null != view) {
                                        view.setFocusable(true);
                                        view.requestFocus();
                                    }
                                }
                            }
                            activity.get().mTitleText.setFocusable(false);
                            break;
                        case REFRESH_EPSIODE_CANCLICK:
                            activity.get().isCanClick = true;
                            break;
                        case RemoteKeyEvent.VOD_FAST_FORWARD:
                            activity.get().mPlayView.fastForward();
                            activity.get().showControlView(true);
                            break;
                        case RemoteKeyEvent.VOD_FAST_REWIND:
                            activity.get().mPlayView.rewind();
                            activity.get().showControlView(true);
                            break;
                        case ViewkeyCode.VIEW_KEY_BACK_TV_PLAY:
                            if (null != activity.get().mPlayBackView && !activity.get().mPlayBackView.isShowing()) {
                                activity.get().mPlayBackView.dismiss();
                            }
                            if (null != activity.get().controllerview && activity.get().controllerview.isShowing()){
                                activity.get().controllerview.dismiss();
                            }
                            activity.get().finish();
                            break;
                        case ViewkeyCode.VIEW_KEY_CONTINUE_PLAY:
                            if (activity.get().isCanReplayByTrySee()) {
                                activity.get().resumePlay();
                                activity.get().startRecordPlayTime();
                                activity.get().showControlView(true);
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
                            if (activity.get().isLookBackPlayEnd) {
                                activity.get().mPlayView.rePlay();
                                activity.get().isLookBackPlayEnd = false;
                            } else {
                                activity.get().mPlayView.seekTo(0);
                                activity.get().resumePlay();

                            }
                            activity.get().startRecordPlayTime();
                            if (null != activity.get().mPlayBackView) {
                                activity.get().mPlayBackView.dismiss();
                            }
                            activity.get().showControlView(true);
                            break;
                        case ViewkeyCode.VIEW_KEY_EXIT:
                            if (!activity.get().isDestroyed()) {
                                activity.get().finish();
                            }
                            break;
                        case NEXT_PLAY:
                            activity.get().handlerNextPlay();
                            break;
                        case BACK_PLAY:
                            if (SharedPreferenceUtil.getInstance().isCirclePlay()) {
                                activity.get().getIntentInfo();
                            } else {
                                activity.get().playRelease();
                                activity.get().mImgBb.setVisibility(View.VISIBLE);
                                activity.get().showDetailDesc();
                            }
                            break;
                        case EPISODE_PLAY:
                            activity.get().episondePlay(msg);
                            break;
                        case HIDE_TRY_SEE_HINT:
                            activity.get().tryseelayout.setVisibility(View.GONE);
                            activity.get().tryseelayoutDown.setVisibility(View.GONE);
                            break;
                        case SWITCH_TRY_SEE_HINT:
                            activity.get().switchTrySeeHint();
                            break;
                        case UPDATE_DURATION:
                            if (activity.get().tryToSeeFlag == 1) {
                                long currentPosition = activity.get().mPlayView.getCurrentPosition();

                                if (activity.get().tryToSeeTime > 10 && currentPosition * 1L >= (activity.get().tryToSeeTime - 10 ) * 1000L && currentPosition * 1L < activity.get().tryToSeeTime * 1000L && !activity.get().alreadyReAuthorize){

                                    activity.get().reAuthorize();

                                }else{
                                    activity.get().alreadyReAuthorize = false;
                                }

                                if (hasMessages(UPDATE_DURATION)) {
                                    removeMessages(UPDATE_DURATION);
                                }
                                if (currentPosition >= activity.get().tryToSeeTime * 1000l) {
                                    activity.get().pauseTrySee();
                                } else {
                                    sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
                                    String content = activity.get().seehinttv.getText().toString();
                                    if (content.contains(activity.get().getString(R.string.trysee_finish_hint))) {
                                        activity.get().tryseelayout.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                if (hasMessages(UPDATE_DURATION)) {
                                    removeMessages(UPDATE_DURATION);
                                }
                            }
                            break;
                        case DECREMENT_REST_TIME:
                            if (activity.get().restRemainderTime <= 0) {
                                activity.get().autoDeblocking();
                            } else {
                                activity.get().restRemainderTime = activity.get().restRemainderTime - 1000;
                                if (activity.get().restRemainderTime > 0) {
                                    if (null != activity.get().singletimetv) {

                                        activity.get().singletimetv.setText(PlayUtil.getStringForTime(activity.get().mFormatBuilder, activity.get().mFormatter, activity.get().restRemainderTime));
                                    }
                                    activity.get().continueTimetv.setText(PlayUtil.getStringForTime(activity.get().mFormatBuilder, activity.get().mFormatter, activity.get().restRemainderTime));
                                    sendEmptyMessageDelayed(DECREMENT_REST_TIME, 1000);
                                } else {
                                    activity.get().autoDeblocking();
                                }

                            }
                            break;
                        case KEY_CLICK_RECORD:
                            SuperLog.debug(TAG, "keyClickCount:" + activity.get().keyClickCount);
                            if (activity.get().keyClickCount >= 10) {
                                activity.get().autoPlay();
                            } else {
                                activity.get().keyClickCount = activity.get().keyClickCount + 1;
                                sendEmptyMessageDelayed(KEY_CLICK_RECORD, 1000);
                            }
                            break;

                    }


                }
            } catch (Exception e) {
                SuperLog.error(TAG, e);
            } finally {}
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_mode_voddetail);
        setmUnBinder(ButterKnife.bind(this));
        //???????????????
        mFeedback = new Feedback(this);
        mXiriVoiceVodUtil = new XiriVoiceVodUtil(this, this, mFeedback);
        mHandler = new VodDetailHandler(this);
        mDetailPresenter = new DetailPresenter(this);
        mDetailPresenter.setDetailDataView(this);
        mDetailPresenter.setChildMode(true);
        mDetailPresenter.setPlayVodBean(playVodBean);
        initView();
        initDate();
    }


    public void autoDeblocking() {
        restRemainderTime = 0;
        if (VoddetailUtil.REST_BY_EPSIODE.equals(SharedPreferenceUtil.getInstance().getlockScreenType())) {
            VoddetailUtil.getInstance().setEpsiodeRest(false);
        }
        VoddetailUtil.getInstance().stopRestTime(SharedPreferenceUtil.getInstance().getlockScreenType());
        if (null != mCompletePopwindow && mCompletePopwindow.isShowing()) {
            mCompletePopwindow.dismiss();
        }
        secondComfirmRestMode();
        readyPlay();
    }


    //??????????????????
    public void handlerNextPlay() {
        isPrepared = false;
        tryseelayout.setVisibility(View.GONE);
        tryseelayoutDown.setVisibility(View.GONE);
        OrderConfigUtils.getInstance().clear();
        alacarteCode = "";
        //???????????????
        SuperLog.error(TAG, "NEXT_PLAY");
        int nextPlayPosition = 0;
        //???????????????
        voddetailEpsiodesUtils.getNextOrPreEpisode(sitcomNO, true, new VoddetailEpsiodesUtils.GetEpisodeCallback() {
            @Override
            public void getEpisode(List<Episode> episodes, Episode episode) {
                //?????????
                sitcomNO = episode.getSitcomNO();
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
                if (mBrowseFrameLayout.getVisibility() == View.VISIBLE) {
                    refreshBookmark();
                }
            }

            @Override
            public void getEpisodeFail() {
                if (SharedPreferenceUtil.getInstance().isCirclePlay()){
                    String firstPlayPosition = voddetailEpsiodesUtils.getSitcomNos().get(0).get(0);
                    voddetailEpsiodesUtils.getEpisode(firstPlayPosition, new VoddetailEpsiodesUtils.GetEpisodeCallback() {
                        @Override
                        public void getEpisode(List<Episode> episodes, Episode episode) {
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
                            if (mBrowseFrameLayout.getVisibility() == View.VISIBLE) {
                                refreshBookmark();
                            }
                        }

                        @Override
                        public void getEpisodeFail() {
                            isCompelete = true;
                            mHandler.sendEmptyMessage(BACK_PLAY);
                        }
                    });
                }else{
                    //??????????????????????????????
                    isCompelete = true;
                    mHandler.sendEmptyMessage(BACK_PLAY);
                }
            }
        });
    }

    //???????????????????????????
    public void pauseTrySee() {
        mPlayView.seekTo(tryToSeeTime * 1000);
        mPlayView.pausePlay();
        String timeStr = getTrySeeTime(tryToSeeTime);
        seehinttvDown.setText(String.format(getResources().getString(R.string.notice_free_trial_play_pause_new), timeStr));
        if (OrderConfigUtils.getInstance().needShowPopWindow()){
            if (OrderConfigUtils.getInstance().isAlacarte()){
                showAlacarteChoosePopWindow();
            }else{
                showOrderChoosePopWindow();
            }
        }
    }

    //?????????????????????url
    private String mJiutianTrackerUrl = "";
    //???????????????itemId
    private String mJiutianItemId = "";

    //????????????
    private void initDate() {
        VoddetailUtil.getInstance().initDate();

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

        //???????????????????????????????????????vod,?????????????????????
        Object obj = null;
        String subjectId = null;
        Serializable xmlInfo = null;
        try {
            obj = getIntent().getSerializableExtra(ORGIN_VOD);
            subjectId = getIntent().getStringExtra(SUBJECT_ID);
            xmlInfo = getIntent().getSerializableExtra(XmppManager.XML_MESSAGE);

            //??????????????????????????????????????????,????????????????????????
            ubd_sceneId       = getIntent().getStringExtra(NewVodDetailActivity.BUNDLE_UBD_SCENE_ID);
            ubd_appPointedId  = getIntent().getStringExtra(NewVodDetailActivity.BUNDLE_UBD_APP_POINTED_ID);
            ubd_recommendType = getIntent().getStringExtra(NewVodDetailActivity.BUNDLE_UBD_RECOMMEND_TYEP);

            String jiutianTrackerUrl = getIntent().getStringExtra(NewVodDetailActivity.JIUTIAN_TRACKER_URL);
            if (!TextUtils.isEmpty(jiutianTrackerUrl)){
                this.mJiutianTrackerUrl = jiutianTrackerUrl;
            }

            String jiutianItemId = getIntent().getStringExtra(NewVodDetailActivity.JIUTIAN_ITEM_ID);
            if (!TextUtils.isEmpty(jiutianItemId)){
                this.mJiutianItemId = jiutianItemId;
            }else{
                this.mJiutianItemId = "";
            }
        } catch (Exception e) {
            SuperLog.error(TAG, e);
        }
        resetTime();

        if (!TextUtils.isEmpty(subjectId)) {
            mDetailPresenter.setSubjectId(subjectId);
        }
        if (null != obj && obj instanceof VOD) {
            VOD vod = (VOD) obj;
            initDetail(vod);
        }

        if (null != xmlInfo && xmlInfo instanceof XmppMessage) {
            xmppmessage = (XmppMessage) xmlInfo;

        }

        mPlayBackView = new PlayBackViewPopwindow(this, mHandler, -1);
        //????????????????????????,???false???????????????????????????
        mPlayView.setShouldAutoPlay(true);
        mPlayView.setOnPlayCallback(this);

        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        VoddetailUtil.getInstance().setEpisodePollingListener(episodePollingListener);

        String canSetSkipStr = SessionService.getInstance().getSession().getTerminalConfigurationVOD_SKIP_SWITCH();
        if (canSetSkipStr.equals("1") || canSetSkipStr.equals("0")) {
            canSetSkip = false;
        } else {
            canSetSkip = true;
        }
    }

    private void resetTime() {
        //???????????????
        String yesterday = SharedPreferenceUtil.getInstance().getYesterdayTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date(NtpTimeService.queryNtpTime()));
        SuperLog.debug(TAG, "yesterday:" + yesterday + "|today:" + today + "|ntptime:" + NtpTimeService.queryNtpTime());
        if (TextUtils.isEmpty(yesterday) || !today.equals(yesterday)) {
            SharedPreferenceUtil.getInstance().setCurrentAllTime(0);
            SharedPreferenceUtil.getInstance().setYesterdayTime(today);
            VoddetailUtil.getInstance().initDate();
            VoddetailUtil.getInstance().resetAllTime();
            VoddetailUtil.getInstance().resetSingleTime();
            if (VoddetailUtil.REST_BY_TODAY.equals(SharedPreferenceUtil.getInstance().getlockScreenType())) {
                VoddetailUtil.getInstance().stopRestTime(SharedPreferenceUtil.getInstance().getlockScreenType());
            }
        }
    }

    @SuppressLint("RestrictedApi")
    public void initView() {
        showSubscribedMark = LauncherService.getInstance().getLauncher().getExtraData().get(Constant.SHOW_SUBSCRIBERD_MARK);
        mPlayView = (PlayView) findViewById(R.id.play_view);
        //????????????????????????
        mPlayView.setWindowState(1);
        mRecommendList.setNumColumns(6);
        mBrowseFrameLayout.setOnFocusSearchListener(mOnFocusSearchListener);
        mTitleText.setSelected(true);
        mVideoInfoImg2.setVisibility(View.GONE);
        mDescText.setFocusable(false);
        mScoreText.setFocusable(true);
        mScoreText.setSelected(true);
        mScoreText.requestFocus();

        buy_rel.setVisibility(View.GONE);
    }

    //??????vod????????????????????????????????????????????????
    private void setButtonInfo() {
        mplayRel.setVisibility(View.GONE);
        col_rel.setVisibility(View.GONE);
        buy_rel.setVisibility(View.GONE);
        timingrel.setVisibility(View.GONE);
        playModeRel.setVisibility(View.GONE);
    }

    /**
     * ??????10????????????????????????
     */
    public void autoPlay() {
        keyClickCount = 0;
        if ((null == mCompletePopwindow || !mCompletePopwindow.isShowing()) && (null == mComfirmPopwindow || !mComfirmPopwindow.isShowing()) && (null == mSecondConfirmPopwindow || !mSecondConfirmPopwindow.isShowing()) && VoddetailUtil.getInstance().getRestTime() <= 0 && getIsResume() && !isCompelete) {
            hideDetailDesc();
        } else {
            mHandler.sendEmptyMessageDelayed(KEY_CLICK_RECORD, 1000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SuperLog.debug(TAG, "onResume->requestNum:" + requestNum);
        if (requestNum == 0) {
            mHandler.sendEmptyMessage(RequestDetail);
        }

        startXiri();

        if (null == mPlayBackView || !mPlayBackView.isShowing()) {
            if (isCanReplayByTrySee()) {
                if (tryToSeeFlag == 1) {
                    mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
                }
                if (VoddetailUtil.getInstance().getRestTime() <= 0 && isPlayingState()) {
//                    mPlayView.resumePlay();
                    resumePlay();
                    startRecordPlayTime();
                }
            }
        }

    }

    @Override
    public Context context() {
        return this;
    }

    @Override
    public void showLoading() {
        isCanClick = false;
        detailsProgress.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        isCanClick = true;
        detailsProgress.setVisibility(View.GONE);
    }

    @Override
    public void showNoContent() { }

    @Override
    public void showError(String message) { }

    //?????????????????????tracker
    public String disPlayTracker = "";

    //??????vod???????????????
    @Override
    public void showDetail(final VODDetail vodDetail, String actionId, List<RecmContents> recmContentsList) {

        mVODDetail = vodDetail;
        mPlayView.setChildDetailMode(true);
        configDetail(vodDetail);

        //??????????????????
        iVopRecPresenter = new IVopRecPresenter(this);
        iVopRecPresenter.getChildModeRecList(mVODDetail, mVODDetail.getID(), new IVopRecPresenter.GetChildModeRecCallback() {
            @Override
            public void getChildModeRecSuccess(List<VOD> vods, String type,String tracker) {
                if (isDestory || isFinishing()){
                    return;
                }
                disPlayTracker = tracker;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initRecommend(vods,type);
                    }
                });

            }

            @Override
            public void getChildModeRecFail() {
                disPlayTracker = "";
                Log.i(TAG, "getChildModeRecFail: ");
            }
        });
        //??????????????????,readyPlay???????????????
        if (VodUtil.canPlay(is4KSource)) {
            secondComfirmRestMode();
            if (isCanPlay() && !(isFromSubscribeNotTrySee && isTrySeeSubscribeSucess)) {
                readyPlay();
                mHandler.sendEmptyMessageDelayed(KEY_CLICK_RECORD, 1000);
            }
        }
        isFromSubscribeNotTrySee = false;

        //??????????????????????????????????????????
        if(ubd_appPointedId != null){
            UBDSwitch.getInstance().reportInVodDetailActivityChild(vodDetail.getName(), vodDetail.getID(), vodDetail.getContentType(), ubd_recommendType,ubd_sceneId,ubd_appPointedId);
            //?????????????????????
            ubd_recommendType = null;
            ubd_sceneId = null;
            ubd_appPointedId = null;
        }
    }

    /**
     * ??????????????????,readyPlay???????????????
     */
    public void secondComfirmRestMode() {
        if (VoddetailUtil.getInstance().getRestTime() <= 0 && VoddetailUtil.getInstance().isEpsiodeRestTemp()) {
            VoddetailUtil.getInstance().setEpsiodeRest(true);
            VoddetailUtil.getInstance().setEpsiodeRestTemp(false);
        }
    }

    /**
     * ???????????????????????????
     */
    public void stopDecremntRestTime() {
        contiunePlaylly.setVisibility(View.GONE);
        playCompletetv.setVisibility(View.GONE);
        mHandler.removeMessages(DECREMENT_REST_TIME);
    }

    public boolean isCanPlay() {
        if (VoddetailUtil.getInstance().getRestTime() <= 0) {
            stopRest();
            return true;
        }
        boolean extraCondition = false;

        /**
         * 2019\09\19 Eason ????????????????????????????????????????????????????????????EPG-???????????????-???????????????????????????
         * ???????????? ?????????????????????????????????????????????????????????????????????????????????null????????????0.
         * */
        if (VoddetailUtil.REST_BY_EPSIODE.equals(SharedPreferenceUtil.getInstance().getlockScreenType()) && VoddetailUtil.getInstance().isEpsiodeRest()
                && !TextUtils.isEmpty(SharedPreferenceUtil.getInstance().getLockScreenPoint()) && !SharedPreferenceUtil.getInstance().getLockScreenPoint().equals("0")) {
            SuperLog.debug(TAG, "isEpsiodeRest is true to show contiunePlaylly");
            if (contiunePlaylly.getVisibility() == View.GONE) {
                contiunePlaylly.setVisibility(View.VISIBLE);
                playCompletetv.setVisibility(View.GONE);
                restRemainderTime = VoddetailUtil.getInstance().getRestTime();
                mHandler.removeMessages(DECREMENT_REST_TIME);
                mHandler.sendEmptyMessageDelayed(DECREMENT_REST_TIME, 1000);
            }
            extraCondition = true;
        }
        //--end


        if (((!VoddetailUtil.getInstance().isEpsiodeRest()) && (!VoddetailUtil.getInstance().isCanPlaySingle())) || (VoddetailUtil.getInstance().isEpsiodeRest() && (!VoddetailUtil.getInstance().isEspiodeCanPlay()))) {

            SuperLog.debug(TAG, "show----contiunePlaylly");

            if (contiunePlaylly.getVisibility() == View.GONE) {
                contiunePlaylly.setVisibility(View.VISIBLE);
                playCompletetv.setVisibility(View.GONE);
                restRemainderTime = VoddetailUtil.getInstance().getRestTime();
                mHandler.removeMessages(DECREMENT_REST_TIME);
                mHandler.sendEmptyMessageDelayed(DECREMENT_REST_TIME, 1000);
            }
            extraCondition = true;
        }
        if ((!VoddetailUtil.getInstance().isEpsiodeRest()) && (!VoddetailUtil.getInstance().isCanPlayToday())) {

            SuperLog.debug(TAG, "show----playCompletetv");

            if (playCompletetv.getVisibility() == View.GONE) {
                playCompletetv.setVisibility(View.VISIBLE);
                contiunePlaylly.setVisibility(View.GONE);
                mHandler.removeMessages(DECREMENT_REST_TIME);
            }
            extraCondition = true;
        }
        //?????????????????????????????????????????????????????????
        if (!extraCondition) {
            stopRest();
            return true;
        }
        return false;

    }


    public void stopRest() {
        stopDecremntRestTime();
        if (!TextUtils.isEmpty(SharedPreferenceUtil.getInstance().getlockScreenType())) {
            restRemainderTime = 0;
            if (VoddetailUtil.REST_BY_EPSIODE.equals(SharedPreferenceUtil.getInstance().getlockScreenType())) {
                VoddetailUtil.getInstance().setEpsiodeRest(false);
            }
            VoddetailUtil.getInstance().stopRestTime(SharedPreferenceUtil.getInstance().getlockScreenType());
            secondComfirmRestMode();
        }
    }


    public void readyPlay() {
        if (null == mVODDetail) {
            return;
        }
        if (mCurrentPlayState == IPlayState.PLAY_STATE_HASMEDIA && VoddetailUtil.getInstance().getRestTime() <= 0) {
            if (mBrowseFrameLayout.getVisibility() == View.GONE) {
                mImgBb.setVisibility(View.GONE);
            }
            stopDecremntRestTime();
            mHandler.removeMessages(KEY_CLICK_RECORD);
            mHandler.sendEmptyMessageDelayed(KEY_CLICK_RECORD, 1000);
            resumePlay();
            startRecordPlayTime();
            return;
        }
        isFirstFlag = true;
        try {
            mDetailPresenter.setButtonOrderOrSee(true);
            if (VodUtil.isMiguVod(mVODDetail)) {
                MiguQRViewPopWindow popWindow = new MiguQRViewPopWindow(this, mVODDetail.getCode(), MiguQRViewPopWindow.mSearchResultType);
                popWindow.showPopupWindow(mBrowseFrameLayout);
                return;
            }
            String vodType = mVODDetail.getVODType();
            if (vodType.equals("0")) {
                if (mVODDetail.getMediaFiles() != null && mVODDetail.getMediaFiles().size() != 0) {
                    Log.i(TAG, "currentVod: 1");
                    currentVod = mVODDetail;
                    mDetailPresenter.playVOD(mVODDetail);
                } else {
                    if (!HeartBeatUtil.getInstance().isSubscribedByPrice(mVODDetail, null == mVODDetail ? "" : mVODDetail.getPrice())) {
                        EpgToast.showToast(ChildModeVodDetailActivity.this, "???????????????????????????");
                    } else {
                        EpgToast.showToast(ChildModeVodDetailActivity.this, "???????????????");
                    }
                    return;
                }
            } else {
                List<Episode> episodes = mVODDetail.getEpisodes();
                Episode playEpisode = voddetailEpsiodesUtils.getSelesctedEpisode();
                Bookmark bookmark = mVODDetail.getBookmark();
                if (bookmark != null && !TextUtils.isEmpty(bookmark.getSitcomNO())) {
                    SuperLog.debug(TAG, "set bookmark--->" + "videoId:" + bookmark.getItemID() + ",sitcomNO:" + bookmark.getSitcomNO() + ",breakpoint:" + bookmark.getRangeTime());
                }
//                if (episodes != null && episodes.size() != 0) {
//                    Episode playEpisode = null;
//                    if (bookmark != null && !TextUtils.isEmpty(bookmark.getSitcomNO())) {
//                        for (Episode episode : episodes) {
//                            if (bookmark.getSitcomNO().equals(episode.getSitcomNO())) {
//                                playEpisode = episode;
//                            }
//                        }
//                    } else {
//                        playEpisode = episodes.get(0);
//                    }
//                    if (null != playEpisode) {
//                        Log.i(TAG, "currentVod: 2");
//                        currentVod = playEpisode.getVOD();
//                        mDetailPresenter.playVOD(playEpisode);
//                    }
//                } else {
//                    EpgToast.showToast(ChildModeVodDetailActivity.this, "???????????????????????????");
//                    return;
//                }
                if (null != playEpisode){
                    currentVod = playEpisode.getVOD();
                    mDetailPresenter.playVOD(playEpisode);
                }else{
                    EpgToast.showToast(ChildModeVodDetailActivity.this, "???????????????????????????");
                    return;
                }
            }
        } catch (Exception ex) {
            SuperLog.error(TAG, ex);
        }

    }


    public void authenticateVOd(VODDetail detail) {
        if (null != detail) {
            detail.setIsSubscribed("1");
            mDetailPresenter.setVODDetail(detail);
            setButtonInfo(detail);
            String vodType = detail.getVODType();
            if (vodType.equals("0")) {
                List<VODMediaFile> vodMediaFiles = detail.getMediaFiles();
                if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
                    mDetailPresenter.playVOD(detail);
                } else {
                    if (!detail.getIsSubscribed().equals("1")) {
                        EpgToast.showToast(ChildModeVodDetailActivity.this, "???????????????????????????");
                    } else {
                        EpgToast.showToast(ChildModeVodDetailActivity.this, "???????????????");
                    }
                }
            } else {
                Episode playEpisode = null;
                if (null != mDetailPresenter.getEpisode()) {
                    playEpisode = mDetailPresenter.getEpisode();
                } else {
                    List<Episode> episodes = detail.getEpisodes();
                    Bookmark bookmark = detail.getBookmark();
                    if (bookmark != null && !TextUtils.isEmpty(bookmark.getSitcomNO())) {
                        SuperLog.debug(TAG, "set bookmark--->" + "videoId:" + bookmark.getItemID() + ",sitcomNO:" + bookmark.getSitcomNO() + ",breakpoint:" + bookmark.getRangeTime());
                    }
                    if (episodes != null && episodes.size() != 0) {
                        if (bookmark != null && !TextUtils.isEmpty(bookmark.getSitcomNO())) {
                            for (Episode episode : episodes) {
                                if (bookmark.getSitcomNO().equals(episode.getSitcomNO())) {
                                    playEpisode = episode;
                                }
                            }
                        } else {
                            playEpisode = episodes.get(0);
                        }
                    }
                }
                if (null != playEpisode) {
                    mDetailPresenter.playVOD(playEpisode);
                }
            }
        }
    }

    //???????????????
    public void initDetail(VOD vod) {
        SuperLog.debug(TAG, "initDetail---->VODID:" + vod.getID());
        //??????????????????
        mTitleText.setText(TextUtils.isEmpty(vod.getName()) ? "" : vod.getName());
        //??????
        String score = vod.getAverageScore();
        //vod??????????????????????????????????????????????????????????????????????????????
        if (vod instanceof VODDetail) {
            VODDetail detail = (VODDetail) vod;
            if (ScoreControl.newNeedShowScoreWithCMSType(detail)) {
                if (TextUtils.isEmpty(score) || score.equals("0.0")) {
                    score = "7.0";
                }
                float intScore = Float.parseFloat(score);
                setScore(intScore);
                if (!TextUtils.isEmpty(score)) {
                    mScoreText.setText(score);
                }
                mStarLayout.setVisibility(View.VISIBLE);
                mScoreText.setVisibility(View.VISIBLE);
            } else {
                mScoreText.setVisibility(View.GONE);
                mStarLayout.setVisibility(View.GONE);
            }
        } else {
            mScoreText.setVisibility(View.GONE);
            mStarLayout.setVisibility(View.GONE);
        }


        //??????????????????
        setBackGroundImg(vod);
        //?????????????????????????????????
        if (vod instanceof VODDetail) {
            VODDetail vodDetail = (VODDetail) vod;
            //???????????????????????????
            showSubscript(vodDetail);
            setButtonInfo(vodDetail);
            setButtonListener(vodDetail);
            setVodEpsiodes(vodDetail);
            if (VodUtil.canPlay(is4KSource)) {
                mplayRel.setFocusable(true);
                if (!isFromSubscribeNotTrySee){
                    mplayRel.requestFocus();
                }
            } else {
                if (!isFromSubscribeNotTrySee){
                    col_rel.setFocusable(true);
                    col_rel.requestFocus();
                }
            }

        } else {
            setButtonInfo();
        }
        //??????????????????
        setDateSource(vod);
        mScoreText.setSelected(false);
        mScoreText.setFocusable(false);
        if (!VodUtil.canPlay(is4KSource)) {
            m4KWarnning.setVisibility(View.VISIBLE);
            m4KWarnning.setText(getResources().getString(R.string.details_source_data_dot) + getResources().getString(R.string.details_4k_warnning));
            mplayRel.setVisibility(View.GONE);
            buy_rel.setFocusable(false);
            col_rel.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (KeyEvent.ACTION_DOWN == event.getAction()) {
                        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            playModeRel.setFocusable(true);
                            playModeRel.requestFocus();
                            return true;
                        }
                    }
                    return false;
                }
            });

        }
        //???????????????
        setVodDesribe(vod);
        //??????????????????
        setFavoriteStatus(isCollection = vod.getFavorite() != null);
    }

    public void setDateSource(VOD vod) {
        String info = "";
        String totalTime = "";
        List<VODMediaFile> vodMediaFileList = vod.getMediaFiles();
        if (vodMediaFileList != null && vodMediaFileList.size() != 0) {
            VODMediaFile vodMediaFile = vodMediaFileList.get(0);
            if (null != vodMediaFile.getElapseTime()) {
                int time = Integer.parseInt(vodMediaFile.getElapseTime());
                if (time < 60) {
                    totalTime = time + "???";
                } else {
                    totalTime = (Integer.parseInt(vodMediaFile.getElapseTime()) / 60) + "??????";
                }
                if (!TextUtils.isEmpty(totalTime)) {
                    info = info + totalTime;
                }
            }
        }
        String produceDate = vod.getProduceDate();
        if (!TextUtils.isEmpty(produceDate)) {
            if (produceDate.length() > 4) {
                info = info + (TextUtils.isEmpty(info) ? "" : "  |  ") + produceDate.substring(0, 4);
            }
        }
        List<Genre> genres = vod.getGenres();
        if (genres != null && genres.size() != 0) {
            for (int i = 0; i < genres.size(); i++) {
                Genre genre = genres.get(i);
                String genreName = genre.getGenreName();
                if (!TextUtils.isEmpty(genreName)) {
                    info = info + (TextUtils.isEmpty(info) ? "" : "  |  ") + genreName;
                }
            }
        }

        //?????????????????????????????????
        String maxNum = vod.getMaxSitcomNO();
        String vodNum = vod.getVodNum();
        String text = "";
        if (!TextUtils.isEmpty(maxNum) && !TextUtils.isEmpty(vodNum)) {

            if (Integer.parseInt(vodNum) == Integer.parseInt(maxNum)) {
                info = info + (TextUtils.isEmpty(info) ? "" : "  |  ") + " ???" + maxNum + (isShowSerieslayout(vod.getCmsType()) ? "???" : "???");
            } else {
                info = info + (TextUtils.isEmpty(info) ? "" : "  |  ") + " ????????????" + maxNum + (isShowSerieslayout(vod.getCmsType()) ? "???" : "???");
            }
        }
        String cp = SessionService.getInstance().getSession().getTerminalConfigurationValue("cpId_" + vod.getCpId());
        if (!TextUtils.isEmpty(cp)) {
            if (TextUtils.isEmpty(text)) {
                text = getResources().getString(R.string.details_source_data_dot);
            }
//            text += " ????????????" + cp + "??????????????????";
            text = text + "???????????????"+cp;
        }
        List<NamedParameter> paramters = vod.getCustomFields();
        if (null != paramters && paramters.size() != 0) {
            StringBuffer sb = new StringBuffer(text);
            for (int i = 0; i < paramters.size(); i++) {
                if ("vodInfo".equals(paramters.get(i).getKey())) {
                    String firstValue = paramters.get(i).getFistItemFromValue();
                    if (!TextUtils.isEmpty(firstValue)) {
                        sb.append(" " + firstValue);
                    }
                }
            }
            text = sb.toString();
        }
        mDataSourceText.setText(text);

        mInfoText.setText(info);
    }


    /**
     * ??????????????????
     *
     * @param vod
     */
    public void setBackGroundImg(VOD vod) {
        Picture picture = vod.getPicture();
        if (picture != null && !posterIsCreated) {
            String bgUrl = null;
            List<String> bgUrls = picture.getBackgrounds();
            if (null != bgUrls && bgUrls.size() != 0) {
                bgUrl = bgUrls.get(0);
            }
            List<String> posters = picture.getPosters();
            if (posters != null && posters.size() != 0) {
                String poster = posters.get(0);
                if (!TextUtils.isEmpty(poster)) {
                    if (TextUtils.isEmpty(bgUrl)) {
                        bgUrl = poster;
                    }
                    if (!TextUtils.isEmpty(bgUrl)) {
                        loadBackgroundImg(bgUrl);
                    }
                }
            } else {
                List<String> iconList = picture.getIcons();
                if (iconList != null && iconList.size() != 0) {
                    String poster = iconList.get(0);
                    if (!TextUtils.isEmpty(poster)) {
                        if (TextUtils.isEmpty(bgUrl)) {
                            bgUrl = poster;
                        }
                        if (!TextUtils.isEmpty(bgUrl)) {
                            SuperLog.debug(TAG, "backgroundImg:" + bgUrl);
                            loadBackgroundImg(bgUrl);
                        }
                    }
                }
            }
        }
    }


    //????????????????????????
    public void setVodDesribe(VOD vod) {
        final String des = vod.getIntroduce();
        if (!TextUtils.isEmpty(des)) {
            String str1 = TextUtil.toDBC(des);
            mDescText.setText(str1);
            mDescText.setFocusable(true);
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
        if (mplayRel.getVisibility() == View.VISIBLE) {
            mDescText.setNextFocusUpId(R.id.play_rel);
        } else {
            mDescText.setNextFocusUpId(R.id.col_rel);
        }
        mDescText.setNextFocusRightId(R.id.details_descriptions);
    }


    public void setVodEpsiodes(VODDetail vodDetail) {
        String vodType = vodDetail.getVODType();
        if (vodType.equals("0") || vodType.equals("2")) {
            episodes_lly.setVisibility(View.GONE);
        } else {
            episodes_lly.setVisibility(View.VISIBLE);
            if (vodType.equals("3") || isShowSerieslayout(vodDetail.getCmsType())) {
                mDetailPresenter.setIsSeries(0);
            } else {
                mDetailPresenter.setIsSeries(1);
            }
            if (isShowReverselayout(vodDetail.getCmsType())) {
                mDetailPresenter.setIsReverse(1);
            } else {
                mDetailPresenter.setIsReverse(0);
            }
            showEpisodes(vodDetail);
        }

    }


    public boolean isShowSerieslayout(String cmsType) {
        boolean isSeries = true;
        List<String> terminalSubjectIds = SessionService.getInstance().getSession().getTerminalConfigurationVodNameSubjectIDS();
        if (null != terminalSubjectIds && terminalSubjectIds.size() > 0) {
            for (int i = 0; i < terminalSubjectIds.size(); i++) {
                if (terminalSubjectIds.get(i).equals(cmsType)) {
                    return false;
                }
            }
        }
        return isSeries;
    }

    public boolean isShowReverselayout(String cmsType) {
        boolean isSeries = false;
        List<String> terminalSubjectIds = SessionService.getInstance().getSession().getTerminalConfigurationVodReverseSubjectIDS();
        if (terminalSubjectIds != null && terminalSubjectIds.size() > 0) {
            for (int i = 0; i < terminalSubjectIds.size(); i++) {
                if (terminalSubjectIds.get(i).equals(cmsType)) {
                    return true;
                }
            }
        }
        return isSeries;
    }

    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()) {
                //????????????
                case R.id.play_rel:
                    fullImg.setImageDrawable(getResources().getDrawable(hasFocus ? R.drawable.full_screen_light : R.drawable.full_screen));
                    break;
                //????????????
                case R.id.col_rel:
                    favoImg.setImageDrawable(getResources().getDrawable(hasFocus ? R.drawable.child_collection_light : R.drawable.child_collection));
                    break;
                //????????????
                case R.id.buy_rel:
                    buyImg.setImageDrawable(getResources().getDrawable(hasFocus ? R.drawable.child_vip_light : R.drawable.child_vip));
                    break;
                //????????????
                case R.id.play_mode_rel:
                    if (SharedPreferenceUtil.getInstance().isCirclePlay()) {
                        playModeImg.setImageDrawable(getResources().getDrawable(hasFocus ? R.drawable.play_mode_open_light : R.drawable.play_mode_open));
                    } else {
                        playModeImg.setImageDrawable(getResources().getDrawable(hasFocus ? R.drawable.play_mode_close_light : R.drawable.play_mode_close));
                    }
                    break;
                //????????????
                case R.id.timing_rel:
                    timingImg.setImageDrawable(getResources().getDrawable(hasFocus ? R.drawable.timing_light : R.drawable.timing));
                    break;

            }
        }
    };


    public void hideDetailDesc() {
//        Animation  animation=  AnimationUtils.loadAnimation(this, R.anim.child_detail_hide);
//        mBrowseFrameLayout.setAnimation(animation);
        if (mBrowseFrameLayout.getVisibility() == View.VISIBLE) {
            currentFocusView = getCurrentFocus();
            if (null != chooseEpisodeList.findFocus() || null != total_episodes_GridView.findFocus()) {
                mDetailPresenter.setClickEpisode(true);
            } else {
                mDetailPresenter.setClickEpisode(false);
            }

            if (VoddetailUtil.getInstance().getRestTime() > 0) {
                showCompletePopwindow();
            }
            if (mImgBb.getVisibility() == View.VISIBLE && isPlayingState()) {
                mImgBb.setVisibility(View.GONE);
            }
            mBrowseFrameLayout.setVisibility(View.GONE);
            mImgShaw.setVisibility(View.GONE);
            activity_shadow_two.setVisibility(View.GONE);
        }
        mPlayView.setChildDetailMode(false);
        mHandler.removeMessages(KEY_CLICK_RECORD);

        if (null != playVodBean && !PlayerAttriUtil.isEmpty(playVodBean.getBookmark()) && isFirstStartFlag && Long.parseLong(playVodBean.getBookmark()) > 0) {
            isFirstStartFlag = false;
            if (!isNeedShowSkipBottomRecord){
                showBackToStartToast();
            }
//            setRecordText(true);
//            if (!"0".equals(isFilm) && !isLookBack) {
//                mPlayView.setmBottomRecord();
//            }
            if (!isLookBack){
                mPlayView.setmBottomRecordText(getBottomText());
            }
            showControlView(true);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideBackToStartToast();
                }
            }, 5000);
        }
        if (isFirstHideDetailDesc){
            setRecordText(true);
            isFirstHideDetailDesc = false;
        }

    }

    private boolean isFirstHideDetailDesc = true;

    public void showDetailDesc() {
        if (mBrowseFrameLayout.getVisibility() == View.GONE) {
            if (null != vodEpisodesView && vodEpisodesView.isShowing()) {
                vodEpisodesView.dismiss();
            }

            if (null != controllerview && controllerview.isShowing()){
                controllerview.dismiss();
            }

            if (mPlayView.getCurrentPosition() >= tryToSeeTime * 1000 && tryToSeeFlag == 1 && mImgBb.getVisibility() == View.GONE) {
                mImgBb.setVisibility(View.VISIBLE);
//                mImgShaw.setVisibility(View.VISIBLE);
            }
            mBrowseFrameLayout.setVisibility(View.VISIBLE);
            detailScrollView.scrollTo(0, 0);
            mPlayView.setChildDetailMode(true);
            if (null != recmContentsList && recmContentsList.size() > 0) {
                mImgShaw.setVisibility(View.VISIBLE);
            } else {
                activity_shadow_two.setVisibility(View.VISIBLE);
            }
            mHandler.removeMessages(KEY_CLICK_RECORD);
            mHandler.sendEmptyMessageDelayed(KEY_CLICK_RECORD, 1000);
        }
        if (!refreshBookmark()) {
            if (currentFocusView != null) {
                currentFocusView.setFocusable(true);
                currentFocusView.requestFocus();
            }
        }
        mDetailPresenter.setClickEpisode(false);
    }


    //??????????????????????????????????????????????????????
    public void setButtonListener(VODDetail vodDetail) {
        mplayRel.setOnFocusChangeListener(focusChangeListener);
        col_rel.setOnFocusChangeListener(focusChangeListener);
        playModeRel.setOnFocusChangeListener(focusChangeListener);
        timingrel.setOnFocusChangeListener(focusChangeListener);
        mplayRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDetailDesc();
            }
        });
        col_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    VODDetail detail = (VODDetail) v.getTag();
                    mDetailPresenter.setCollection(detail, isCollection);
                } catch (Exception ex) {
                    SuperLog.error(TAG, ex);
                }

            }
        });

        buy_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "buy_relonClick: 1");
                try {
                    if (mLastOnClickTime != 0 && System.currentTimeMillis() - mLastOnClickTime < VALID_TIME) {
                        Log.i(TAG, "buy_relonClick: 2");
                        return;
                    }
                    Log.i(TAG, "buy_relonClick: 3");
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
                            Log.i(TAG, "currentVod: 3");
                            currentVod = detail;
                            mDetailPresenter.playVOD(detail);
                            if (VoddetailUtil.getInstance().getRestTime() <= 0 && isPlayingState()) {
                                hideDetailDesc();
                            }
                        } else {
                            if (!HeartBeatUtil.getInstance().isSubscribedByPrice(detail, "")) {
                                EpgToast.showToast(ChildModeVodDetailActivity.this, "???????????????????????????");
                            } else {
                                EpgToast.showToast(ChildModeVodDetailActivity.this, "???????????????");
                            }
                            return;
                        }
                    } else {
//                        List<Episode> episodes = detail.getEpisodes();
                        List<Episode> episodes = voddetailEpsiodesUtils.getmVoddetail().getEpisodes();
                        Bookmark bookmark = vodDetail.getBookmark();
                        if (bookmark != null && !TextUtils.isEmpty(bookmark.getSitcomNO())) {
                            SuperLog.debug(TAG, "set bookmark--->" + "videoId:" + bookmark.getItemID() + ",sitcomNO:" + bookmark.getSitcomNO() + ",breakpoint:" + bookmark.getRangeTime());
                        }
                        if (episodes != null && episodes.size() != 0) {
                            Episode playEpisode = getBuyEpsiode(episodes);
                            //?????????????????????
                            if (null == playEpisode) {
                                EpgToast.showToast(ChildModeVodDetailActivity.this, R.string.notice_no_orderable_product);
                                return;
                            }
                            Log.i(TAG, "currentVod: 4");
                            currentVod = playEpisode.getVOD();
                            mDetailPresenter.playVOD(playEpisode);
                            if (VoddetailUtil.getInstance().getRestTime() <= 0 && isPlayingState()) {
                                hideDetailDesc();
                            }
                        } else {
                            EpgToast.showToast(ChildModeVodDetailActivity.this, R.string.notice_no_orderable_product);
                            return;
                        }
                    }
                } catch (Exception ex) {
                    SuperLog.error(TAG, ex);
                }

            }
        });


        //??????????????????
        playModeRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceUtil.getInstance().setCirclePlay(!SharedPreferenceUtil.getInstance().isCirclePlay());
                playModeImg.setImageDrawable(getResources().getDrawable(SharedPreferenceUtil.getInstance().isCirclePlay() ? R.drawable.play_mode_open_light : R.drawable.play_mode_close_light));
            }
        });
        //????????????
        timingrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimingRestDialog();
            }
        });

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
            favotv.setText("?????????");
        } else {
            favotv.setText("??????");
        }
    }

    //??????????????????
    private void setButtonInfo(VODDetail vodDetail) {
        mplayRel.setVisibility(View.VISIBLE);
        col_rel.setVisibility(View.VISIBLE);
        timingrel.setVisibility(View.VISIBLE);
        timingrel.setNextFocusRightId(R.id.timing_rel);
        mplayRel.setNextFocusDownId(R.id.details_descriptions);
        col_rel.setNextFocusDownId(R.id.details_descriptions);
        if (SharedPreferenceUtil.getInstance().isCirclePlay()) {
            playModeImg.setImageDrawable(getResources().getDrawable(R.drawable.play_mode_open));
        } else {
            playModeImg.setImageDrawable(getResources().getDrawable(R.drawable.play_mode_close));
        }
        playModeRel.setVisibility(View.VISIBLE);

        mplayRel.setTag(vodDetail);
        col_rel.setTag(vodDetail);
        buy_rel.setTag(vodDetail);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getResources().getDimensionPixelOffset(R.dimen.advert_width), getResources().getDimensionPixelOffset(R.dimen.margin_65));
        lp.leftMargin = getResources().getDimensionPixelOffset(R.dimen.order_margin_right);
        advertImg.setLayoutParams(lp);
        String advertisementUrl = OTTApplication.getContext().getChildVodDetailAdvertisementURL();
        if (!TextUtils.isEmpty(advertisementUrl)) {
            advertImg.setVisibility(View.VISIBLE);
            SuperLog.info2SD("GlideLoadIPV6", "advertisementUrl is " + advertisementUrl);
            Glide.with(ChildModeVodDetailActivity.this).load(advertisementUrl).into(advertImg);
        } else {
            String id = SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.VOD_DETAIL_AdVERT_SUBJECT_ID);
            if (!TextUtils.isEmpty(id)) {
                mDetailPresenter.getAdvertisementSubjectDetail(id, mVODListCallBack);
            }
        }

        //?????????????????????????????????

        ShowBuyControl control = new ShowBuyControl(this);
        control.setCallBack(new ShowBuyControl.ShowBuyTagCallBack() {
            @Override
            public void showBuyTag(int showBuy) {
                switch (showBuy) {
                    case 0:
                        Log.i(TAG, "showBuyTag: 1");
                        buy_rel.setVisibility(View.VISIBLE);
                        buy_rel.setFocusable(true);
                        buy_rel.setEnabled(true);
                        mplayRel.setNextFocusRightId(R.id.buy_rel);
                        col_rel.setNextFocusLeftId(R.id.buy_rel);
                        buyImg.setEnabled(true);
                        buy_rel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                buyImg.setSelected(hasFocus);
                            }
                        });
                        break;
                    case 1:
                        buy_rel.setFocusable(false);
                        buy_rel.setVisibility(View.GONE);
                        break;
                    case 2:
                        buy_rel.setVisibility((TextUtils.isEmpty(showSubscribedMark) || "1".equals(showSubscribedMark)) ? View.VISIBLE : View.GONE);
                        buy_rel.setFocusable(false);
                        buy_rel.setEnabled(false);
                        buy_rel.setOnClickListener(null);
                        buyImg.setEnabled(false);
                        mplayRel.setNextFocusRightId(R.id.col_rel);
                        col_rel.setNextFocusLeftId(R.id.play_rel);
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
                && null != vodDetailPBSConfigUtils.getQuerySTBOrderInfoResponse().getSubProductList() ){
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
                control.ShowBuyTag(vodDetail);
            }
        }else{
            Log.i(TAG, "setButtonInfo: ??????QuerySTBOrderInfo????????????????????????");
            control.ShowBuyTagBySubscription(vodDetail);
        }
    }

    private Episode selectEpisode = null;

    @SuppressLint("RestrictedApi")
    public void showEpisodes(VODDetail detail) {
        //???????????????
        List<String> mEpisodesCount = voddetailEpsiodesUtils.getmEpisodesCount();
        //????????????
        selectEpisode = voddetailEpsiodesUtils.getSelesctedEpisode();
        //????????????????????????
        List<Episode> bookmarkEPisodes = voddetailEpsiodesUtils.getMarkEpisodes();
        if (mEpisodesCount != null && mEpisodesCount.size() != 0) {
            List<List<String>> sitnums = voddetailEpsiodesUtils.getSitcomNos();
            //??????????????????
            List<String> bookmarkEPisodesCounts = new ArrayList<>();
            for (int i = 0; i < sitnums.size(); i++) {
                if (sitnums.get(i).get(0).equals(bookmarkEPisodes.get(0).getSitcomNO())){
                    bookmarkEPisodesCounts = sitnums.get(i);
                }
            }
            chooseEpisodeList.setItemAnimator(null);

            chooseEpisodeList.setLayoutManager(new GridLayoutManager(this, 13, GridLayoutManager.VERTICAL, false));
            if (mEpisodesCount.size() <= 13) {
                total_episodes_GridView.setVisibility(View.GONE);
                chooseAdapter = new ChooseChildEpisodeAdapter(mHandler,bookmarkEPisodes, mDetailPresenter, this,voddetailEpsiodesUtils,bookmarkEPisodesCounts);
                chooseAdapter.setSelectEpisode(selectEpisode);
                chooseAdapter.setIs4KSource(is4KSource);
                chooseAdapter.setOnkeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (KeyEvent.ACTION_DOWN == event.getAction()) {
                            keyClickCount = 0;
                            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                                /*int position = chooseEpisodeList.getChildPosition(v);
                                if (position > 6 && position < 10) {
                                    if (buy_rel.getVisibility() == View.VISIBLE) {
                                        buy_rel.setFocusable(true);
                                        buy_rel.requestFocus();
                                    } else {
                                        col_rel.setFocusable(true);
                                        col_rel.requestFocus();
                                    }
                                    return true;
                                }*/
                                mDescText.requestFocus();
                                return true;
                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                                int position = chooseEpisodeList.getChildPosition(v);
                                int line = (position / 13) + 1;
                                if (position == (line - 1) * 13) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });
                chooseEpisodeList.setAdapter(chooseAdapter);
            } else {
                int totalPosition = (int) (voddetailEpsiodesUtils.getTotalPosition());
                List<List<String>> sitNum = voddetailEpsiodesUtils.getSitcomNos();
                chooseAdapter = new ChooseChildEpisodeAdapter(mHandler,bookmarkEPisodes, mDetailPresenter, this,voddetailEpsiodesUtils,bookmarkEPisodesCounts);
                chooseAdapter.setSelectEpisode(selectEpisode);
                chooseAdapter.setIs4KSource(is4KSource);
                totalEpisodeAdapter = new NewChildTotalEpisodeAdapter(sitNum, chooseAdapter, 13, true, chooseEpisodeList);
                total_episodes_GridView.setAdapter(totalEpisodeAdapter);
                mTotalEpisodeSelectIndex = totalPosition;
                totalEpisodeAdapter.setSelectPosition(totalPosition);
                if (sitNum.size() > totalPosition) {
                    chooseAdapter.setDataEpisodesSource(sitNum.get(totalPosition));
                } else {
                    if (sitNum.size() > 0) {
                        chooseAdapter.setDataEpisodesSource(sitNum.get(0));
                    }
                }
                chooseAdapter.setOnkeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (KeyEvent.ACTION_DOWN == event.getAction()) {
                            keyClickCount = 0;
                            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                                int position = chooseEpisodeList.getChildPosition(v);
                                int line = (position / 13) + 1;
                                if (position == (line - 1) * 13) {
                                    return true;
                                }

                            }
                            if (keyCode == KeyEvent.KEYCODE_DPAD_UP && mDescText.isFocusable()) {
                                mDescText.requestFocus();
                                return true;
                            }
                            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && total_episodes_GridView.getVisibility() == View.VISIBLE) {
                                View viewTem = null;
                                if (mTotalEpisodeSelectIndex != -1) {
                                    viewTem = total_episodes_GridView.getLayoutManager().findViewByPosition(mTotalEpisodeSelectIndex);
                                }
                                if (null != viewTem) {
                                    viewTem.setFocusable(true);
                                    viewTem.requestFocus();
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });
                chooseEpisodeList.setAdapter(chooseAdapter);
                total_episodes_GridView.setSelectedPosition(totalPosition);

                totalEpisodeAdapter.setOnkeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (KeyEvent.ACTION_DOWN == event.getAction()) {
                            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                                if (chooseAdapter.getEpisodeList().size() == 0){
                                    //?????????????????????????????????
                                    return true;
                                }
                                View viewTem = null;
                                if (chooseAdapter.getEpisodeList().contains(selectEpisode)) {
                                    viewTem = chooseEpisodeList.getLayoutManager().findViewByPosition(chooseAdapter.getEpisodeList().indexOf(selectEpisode));
                                } else {
                                    return false;
                                }
                                if (null != viewTem) {
                                    viewTem.setFocusable(true);
                                    viewTem.requestFocus();
                                    return true;
                                }
                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                                if (mTotalEpisodeSelectIndex > 0) {
                                    mTotalEpisodeSelectIndex--;
                                }
                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                                if (sitNum.size() > mTotalEpisodeSelectIndex) {
                                    mTotalEpisodeSelectIndex++;
                                }
                            }
                        }
                        return false;
                    }
                });

            }
        } else {
            total_episodes_GridView.setVisibility(View.GONE);
        }
        //??????????????????????????????????????????
        Episode finalSelectEpisode = selectEpisode;
        mDescText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (KeyEvent.ACTION_DOWN == event.getAction()) {
                    keyClickCount = 0;
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        if (null != chooseAdapter.getEpisodeList()) {
                            View view = null;
                            if (chooseAdapter.getEpisodeList().contains(finalSelectEpisode)) {
                                view = chooseEpisodeList.getLayoutManager().findViewByPosition(chooseAdapter.getEpisodeList().indexOf(finalSelectEpisode));
                            } else {
                                view = chooseEpisodeList.getLayoutManager().findViewByPosition(0);
                            }
                            if (null != view) {
                                view.setFocusable(true);
                                view.requestFocus();
                                return true;
                            }
                        }

                    }
                }
                return false;
            }
        });
    }


    //????????????????????????????????????
    private void initRecommend(List<VOD> recmContentsList, String recommendType) {
        if (recmContentsList != null && recmContentsList.size() != 0) {
            for (int i = recmContentsList.size() - 1; i >= 0; i--) {
                if (null == recmContentsList.get(i) || mVODDetail.getID().equals(recmContentsList.get(i).getID())) {
                    recmContentsList.remove(i);
                }
            }
            if (recmContentsList.size() > 6) {
                recmContentsList.remove(recmContentsList.size() - 1);
            }

            recordRecommend(recommendType,recmContentsList); //??????????????????????????????
            this.recmContentsList = recmContentsList;
            ViewGroup.LayoutParams lp = mRecommendList.getLayoutParams();
            lp.height = recmContentsList.size() > 6 ? getResources().getDimensionPixelOffset(R.dimen.pan_height_546) : getResources().getDimensionPixelOffset(R.dimen.search_keyboard_height);
            mRecommendList.setLayoutParams(lp);
            mRecommendLayout.setVisibility(View.VISIBLE);
            detailsRecommendtitle.setText("??????" + mVODDetail.getName() + "???????????????");
            DetailsRecommendAdapter detailsRecommendAdapter = new DetailsRecommendAdapter(recmContentsList, recommendType);
            detailsRecommendAdapter.setDetailsRecommendListener(new DetailsRecommendAdapter.DetailsRecommendListener() {
                @Override
                public void onFocus(boolean hasFocus) {
                    if (hasFocus) {
                        scrollToBottom(detailScrollView, mBrowseFrameLayout);
                    }
                }
            });
            mRecommendList.setAdapter(detailsRecommendAdapter);

            //??????????????????
            if (!TextUtils.isEmpty(disPlayTracker) && !TextUtils.isEmpty(mVODDetail.getItemid())){
                SuperLog.debug(TAG,"jiutian display child");
                JiutianService.reportDisplayInDetail(disPlayTracker,JiutianService.getJiutianRecommendContentIDs(recmContentsList),getJiutianItemId());
            }


            activity_shadow_two.setVisibility(View.GONE);
            mImgShaw.setVisibility(View.VISIBLE);
            mRecommendList.setInterceptor(new FocusInterceptor() {
                @Override
                public boolean interceptFocus(KeyEvent event, View view) {
                    keyClickCount = 0;
                    int keycode = event.getKeyCode();
                    if (keycode == KeyEvent.KEYCODE_DPAD_RIGHT && mRecommendList.findFocus() != null && isBorder((ViewGroup) view, mRecommendList.findFocus(), View.FOCUS_RIGHT)) {
                        mRecommendList.findFocus().requestFocus();
                        return true;
                    }
                    if (keycode == KeyEvent.KEYCODE_DPAD_LEFT && mRecommendList.findFocus() != null && isBorder((ViewGroup) view, mRecommendList.findFocus(), View.FOCUS_LEFT)) {
                        mRecommendList.findFocus().requestFocus();
                        return true;
                    }

                    if (keycode == KeyEvent.KEYCODE_DPAD_UP && mRecommendList.findFocus() != null) {
                        detailScrollView.scrollTo(0, 0);
                        detailScrollView.smoothScrollTo(0, 0);
                        //??????????????????????????????????????????
                        Episode finalSelectEpisode = selectEpisode;
                        if (totalEpisodeAdapter != null && totalEpisodeAdapter.getItemCount() > 0) {
                            View viewTem = null;
                            if (mTotalEpisodeSelectIndex != -1) {
                                viewTem = total_episodes_GridView.getLayoutManager().findViewByPosition(mTotalEpisodeSelectIndex);
                            }
                            if (null != viewTem) {
                                viewTem.setFocusable(true);
                                viewTem.requestFocus();
                                return true;
                            }
                            return false;
                        } else if (null != chooseAdapter && null != chooseAdapter.getEpisodeList()) {
                            View viewTem = null;
                            if (chooseAdapter.getEpisodeList().contains(finalSelectEpisode)) {
                                viewTem = chooseEpisodeList.getLayoutManager().findViewByPosition(chooseAdapter.getEpisodeList().indexOf(finalSelectEpisode));
                            } else {
                                viewTem = chooseEpisodeList.getLayoutManager().findViewByPosition(0);
                            }
                            if (null != viewTem) {
                                viewTem.setFocusable(true);
                                viewTem.requestFocus();
                                return true;
                            }
                        } else {
                            mDescText.requestFocus();
                        }
                        return true;
                    }
                    return false;
                }
            });
        } else {
            mRecommendLayout.setVisibility(View.GONE);
        }
    }

    //????????????????????????
    private void recordRecommend(String recommendType,List<VOD> listVod){
        if(CollectionUtil.isEmpty(listVod)){
            return;
        }

        switch (recommendType){
            //V2.5.0???????????????IVOP???????????????,??????CVI??????
//            case RecommendData.RecommendType.CVI_TYPE:
//                UBDRecommendImpression.sceneId_child       = QueryRecommendRequest.SecenarizedType.CONTENT_DETAIL;
//                UBDRecommendImpression.recommendType_child = "-2";
//                break;
            case RecommendData.RecommendType.HAND_TYPE:
                UBDRecommendImpression.sceneId_child       = SessionService.getInstance().getSession().getTerminalConfigurationChildSubject(Constant.CHILD_MODE_RECOMMEND_SUBJECT, mVODDetail.getSubjectIDs());;
                UBDRecommendImpression.recommendType_child = "-1";
                break;
            case RecommendData.RecommendType.IVOP_TYPE:
                //UBDRecommendImpression.sceneId_child       = "???????????????sceneId";
                //UBDRecommendImpression.recommendType_child = "???????????????identifyType";
                break;
            default:
                return;
        }

        String recVods = UBDRecommendImpression.getRecommendContentIDs(listVod);
        UBDRecommendImpression.record(
                APPPINEDID_CHILD,
                UBDRecommendImpression.sceneId_child,
                recVods,
                UBDRecommendImpression.recommendType_child,
                null,
                mVODDetail.getID());
    }


    /**
     * ?????????????????????
     *
     * @param root
     * @param focused
     * @param direction
     * @return
     */
    private boolean isBorder(ViewGroup root, View focused, int direction) {
        return FocusFinder.getInstance().findNextFocus(root, focused,
                direction) == null ? true : false;
    }


    //????????????
    private void setScore(float intScore) {
        ratingBar.setVisibility(View.VISIBLE);
        ratingBar.setFocusable(false);
        ratingBar.setMax(10);
        ratingBar.setRating((float) Math.ceil(intScore) / 2);
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
        mScoreText.setText(String.valueOf(newScore.get(0)));
        setScore(newScore.get(0));

    }

    //?????????????????????????????????
    @Override
    public void showContentNotExit() {
        EpgToast.showToast(ChildModeVodDetailActivity.this, "???????????????????????????");
    }

    private boolean avoidmutiClick = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown: ");
        keyClickCount = 0;
        if (isAdmitOperationPlayControl()) {
            int codeValue = RemoteKeyEvent.getInstance().getVODKeyCodeValue(keyCode);
            SuperLog.debug(TAG, "onkeyDown->codeValue:" + codeValue);
            if (codeValue == RemoteKeyEvent.MEDIA_PAUSE_PLAY || codeValue == KeyEvent.KEYCODE_MEDIA_STOP) {
                Log.i(TAG, "onKeyDown: play");
                if (tryToSeeFlag == 0) {
                    Log.i(TAG, "onKeyDown: play trytoseeflag0");
                    if (!mPlayBackView.isShowing() && mPlayView.isPlaying() && mPlayView.getDuration() > 0) {
                        mPlayBackView.showPlayBack(mOnDemandContainer, false, null);
                    } else if (!mPlayView.isPlaying() && mPlayBackView.isShowing()) {//??????MG100Vod??????????????????ok?????????-?????????-???????????????-?????????????????????-??????ok???????????????dialog???????????????
                        mPlayBackView.dismiss();
                    }
                    if (mPlayView.isPlaying()) {
                        stopAllTiming();
                    } else {
                        startRecordPlayTime();
                    }
                    mPlayView.playerOrPause();
                    showControlView(true);
                } else {
                    if (avoidmutiClick){
                        return true;
                    }

                    avoidmutiClick = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            avoidmutiClick = false;
                        }
                    },2500);
                    
                    Log.i(TAG, "onKeyDown: play trytoseeflag1" + canOnKey);
                    if (!canOnKey){
                        return true;
                    }
                    if (OrderConfigUtils.getInstance().needShowPopWindow() &&
                    OrderConfigUtils.getInstance().isAlacarte() && mPlayView.getCurrentPosition() > 1000 && (mPlayView.getCurrentPosition() - 1000) < tryToSeeTime * 1000){
                        Log.i(TAG, "onKeyDown: play 1");
                        mPlayView.pausePlay();
                        showAlacarteChoosePopWindow();
                        return true;
                    }else if (OrderConfigUtils.getInstance().needShowPopWindow() &&
                            OrderConfigUtils.getInstance().isAlacarte()  && mPlayView.getCurrentPosition() < 1000){
                        Log.i(TAG, "onKeyDown: play 2");
                        mPlayView.pausePlay();
                        showAlacarteChoosePopWindow();
                        return true;
                    }

                    mPlayView.pausePlay();
                    showOrderChoosePopWindow();
                }
            } else if (keyCode == KeyEvent.KEYCODE_BACK) {
                //???????????????popwindow?????????,????????????????????????????????????????????????,?????????????????????play
                if (null != mCompletePopwindow && mCompletePopwindow.isShowing()) {
                    mCompletePopwindow.dismiss();
                }
                showDetailDesc();

                return false;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
//                if (!"0".equals(isFilm) && !isLookBack) {
//                    if (!PlayerAttriUtil.isEmpty(videoId)) {
//                        vodEpisodesView = new VodEpisodesView(this, episodeDetailList, videoId, mHandler, sitcomNO, fatherPrice, speed, canSetSpeed, canSetSkip);
//                        vodEpisodesView.showEpisodes(mOnDemandContainer);
//                    } else {
//                        vodEpisodesView = new VodEpisodesView(this, episodeDetailList, "", mHandler, sitcomNO, fatherPrice, speed, canSetSpeed, canSetSkip);
//                        vodEpisodesView.showEpisodes(mOnDemandContainer);
//                    }
//                } else if ("0".equals(isFilm) && !(null != playVodBean && !TextUtils.isEmpty(playVodBean.getVodType()) && TextUtils.equals(playVodBean.getVodType(), Content.PROGRAM))) {
//                    //??????????????????????????????????????????????????????????????????????????????????????????
//                    if (canSetSkip || canSetSpeed) {
//                        vodSettingView = new VodSettingView(this, speed, canSetSpeed, canSetSkip);
//                        vodSettingView.showEpisodes(mOnDemandContainer);
//                    }
//                }
            } else if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                if (!isLookBack && null != mDetailPresenter && !TextUtils.isEmpty(videoId)) {
                    mDetailPresenter.getVODDetail(videoId, this);
                }
            } else if ((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_PAGE_DOWN) && isCanReplayByTrySee()) {
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
                long currentPosition = mPlayView.getCurrentPosition();
                if (tryToSeeFlag == 1) {
                    //??????5min
                    if (currentPosition + rightTime >= tryToSeeTime * 1000L) {
                        //??????????????????
                        mPlayView.dragProgress((int) (tryToSeeTime * 1000));
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
                if (isNeedBackToStart) {
                    boolean skipOpen = SharedPreferenceUtil.getInstance().getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "",true);
                    if (skipOpen && headDuration > 0){
                        seekTo(headDuration);
                    }else{
                        seekTo(0);
                    }

                    seekTo(0);
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
            }else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                if (isLookBack || null == playVodBean || TextUtils.isEmpty(playVodBean.getVodType()) || TextUtils.equals(playVodBean.getVodType(), Content.PROGRAM)){
                return true;
            }

            if ((null != this.controllerview && this.controllerview.isShowing())){
                //???????????????
                return true;
            }

            boolean canshow = false;
            String vodType = mVODDetail.getVODType();
            if (!vodType.equals("0") && !vodType.equals("2")){
                //?????????
                canshow = true;
            }
            if (canSetSkip){
                canshow = true;
            }
            if (canSetSpeed){
                canshow = true;
            }
            if (!canshow){
                //???????????????????????????
                return true;
            }

            if (!avoidAlreadyShowDown){
                avoidAlreadyShowDown = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        avoidAlreadyShowDown = false;
                    }
                },3000);
                if (null != epsiodesUtils){
                    if (!TextUtils.isEmpty(sitcomNO)){
                        epsiodesUtils.refresh(sitcomNO, new BrowseEpsiodesUtils.GetEpisodeCallback() {
                            @Override
                            public void getEpisode(List<Episode> episodes, Episode episode) {
                                VodPlayerControllerView view = new VodPlayerControllerView(ChildModeVodDetailActivity.this,canSetSpeed,canSetSkip,mVODDetail,epsiodesUtils,mHandler,fatherPrice,switchSpeed());
                                controllerview = view;
                                view.showList(mOnDemandContainer);
                                mPlayView.hideControlView();
                            }

                            @Override
                            public void getEpisodeFail() {

                            }
                        });
                    }else{
                        VodPlayerControllerView view = new VodPlayerControllerView(ChildModeVodDetailActivity.this,canSetSpeed,canSetSkip,mVODDetail,epsiodesUtils,mHandler,fatherPrice,switchSpeed());
                        controllerview = view;
                        view.showList(mOnDemandContainer);
                        mPlayView.hideControlView();
                    }
                }else{
                    epsiodesUtils = new BrowseEpsiodesUtils(mVODDetail.getID());
                    epsiodesUtils.getSimpleVod(mVODDetail.getID(), new VoddetailEpsiodePresenter.GetSimpleVodCallback() {
                        @Override
                        public void getSimpleVodSuccess(VODDetail vodDetail) {
                            Log.i(TAG, "getSimpleVodSuccess: "+sitcomNO);
                            if (!TextUtils.isEmpty(sitcomNO)){
                                //????????????????????????????????????????????????????????????????????????????????????
                                epsiodesUtils.refresh(sitcomNO, new BrowseEpsiodesUtils.GetEpisodeCallback() {
                                    @Override
                                    public void getEpisode(List<Episode> episodes, Episode episode) {
                                        VodPlayerControllerView view = new VodPlayerControllerView(ChildModeVodDetailActivity.this,canSetSpeed,canSetSkip,mVODDetail,epsiodesUtils,mHandler,fatherPrice,switchSpeed());
                                        controllerview = view;
                                        view.showList(mOnDemandContainer);
                                        mPlayView.hideControlView();
                                    }

                                    @Override
                                    public void getEpisodeFail() {

                                    }
                                });
                                return;
                            }

                            VodPlayerControllerView view = new VodPlayerControllerView(ChildModeVodDetailActivity.this,canSetSpeed,canSetSkip,mVODDetail,epsiodesUtils,mHandler,fatherPrice,switchSpeed());
                            controllerview = view;
                            view.showList(mOnDemandContainer);
                            mPlayView.hideControlView();
                        }

                        @Override
                        public void getSimpleVodFail() {

                        }
                    });
                }
            }
            return true;
            }
        } else {
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
        }
        return super.onKeyDown(keyCode, event);
    }

    private VodPlayerControllerView controllerview;
    //??????????????????????????????
    private boolean avoidAlreadyShowDown = false;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        SuperLog.debug(TAG, "rightTime:" + rightTime + "|leftTime:" + leftTime);
        if (isAdmitOperationPlayControl()) {
            countForward = 0;
            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {
                forWard(rightTime / 1000);
                forwardAndBackRate = 1;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_PAGE_UP) {
                backForward(leftTime / 1000);
                forwardAndBackRate = 1;
            }
            mPlayView.restartUpdateProgress();
        }
        return super.onKeyUp(keyCode, event);
    }

    //??????????????????????????????
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PlayUrlEvent event) {
        SuperLog.debug(TAG, "get PlayUrlEvent");
        //?????????????????????????????????
//        Product mProduct=new Product();
//        mProduct.setID(event.getProductId());
//            //????????????????????????
//        UBDTool.getInstance().
//                recordUBDPurchase(mProduct,mVODDetail==null?"":mVODDetail.getID(), PurchaseData.SUCCESS);

        isFromSubscribeNotTrySee = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               hideDetailDesc();
            }
        },500);

        if (tryToSeeFlag == 1) {
            isTrySeeSubscribeSucess = true;
            //??????
            if ("0".equals(isFilm)) {
                PlayVODRequest mPlayVodRequest = new PlayVODRequest();
                mPlayVodRequest.setVODID(videoId);
                mPlayVodRequest.setMediaID(currentMediaId);
                mPlayVodRequest.setURLFormat("1");
                mPlayVodRequest.setIsReturnProduct("1");
                mDetailPresenter.playVOD(mPlayVodRequest, this, playVodBean == null ? "0" : playVodBean.getElapseTime());
            } else {
                //?????????
                action = UBDConstant.ACTION.PLAY_NORMAL;
                playVODAuthorize(subContentId, videoId, currentMediaId, playVodBean == null ? "0" : playVodBean.getElapseTime());
            }
        } else {
            EventBus.getDefault().post(new FinishPlayUrlEvent());
        }
//        tryToSeeFlag = 0;
//        mPlayView.setTryToSeeFlag(tryToSeeFlag);
        mHandler.sendEmptyMessage(RequestDetail);


    }

    //?????????????????????
    public String alacarteCode = "";

    //????????????????????????????????????????????????
    @Subscribe
    public void eventCollection(CollectionEvent collectionEvent) {
        SuperLog.debug(TAG, "event receiver collectionEvent change state");
        if (null != mVODDetail && mVODDetail.getID().equals(collectionEvent.getVodId())) {
            showCollection(collectionEvent.isCollection());
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


    @Subscribe
    public void onEvent(ResetBlockEvent resetBlockEvent) {
        if (!VoddetailUtil.REST_BY_EPSIODE.equals(SharedPreferenceUtil.getInstance().getlockScreenType())) {
            if (resetBlockEvent.getType().equals(VoddetailUtil.REST_SETTING_ALL) && isPlayingState() && !VoddetailUtil.getInstance().isEpsiodeRest()) {
                VoddetailUtil.getInstance().startAllRecordTime();
                VoddetailUtil.getInstance().startSingleRecordTime();
            }
            if ((null != mCompletePopwindow && mCompletePopwindow.isShowing()) || contiunePlaylly.getVisibility() == View.VISIBLE || playCompletetv.getVisibility() == View.VISIBLE) {
                secondComfirmRestMode();
                readyPlay();
            }
        }
    }


    /**
     * ????????????????????????
     *
     * @param event
     */
    @Subscribe
    public void eventDeblocking(DeblockingEvent event) {
        if (!TextUtils.isEmpty(event.getType())) {
            switch (event.getType()) {
                case VoddetailUtil.IS_OPEN_EPSIODE_PLAY:
                    if (null != mComfirmPopwindow && null != epsiodeImg) {
                        VoddetailUtil.getInstance().stopEpsiodeRecordTime();
                        VoddetailUtil.getInstance().setEpsiodeRest(false);
                        VoddetailUtil.getInstance().setEpsiodeRestTemp(false);
                        epsiodeImg.setImageDrawable(getResources().getDrawable(R.drawable.unchoose_light));
                        SuperLog.error(TAG, "eventDeblocking->mCurrentPlayState:" + mCurrentPlayState);
                        if (isPlayingState() && VoddetailUtil.getInstance().getRestTime() <= 0) {
                            startRecordPlayTime();
                        }
                        mComfirmPopwindow.dismiss();
                        timingtv.setText("????????????");
                    }
                    if (!(VoddetailUtil.getInstance().getRestTime() > 0 && VoddetailUtil.REST_BY_EPSIODE.equals(SharedPreferenceUtil.getInstance().getlockScreenType()))) {
                        break;
                    }
                case VoddetailUtil.REST_BY_SINGLE:
                case VoddetailUtil.REST_BY_TODAY:
                case VoddetailUtil.REST_BY_EPSIODE:
                    VoddetailUtil.getInstance().stopRestTime(event.getType());
                    if (VoddetailUtil.REST_BY_EPSIODE.equals(event.getType())) {
                        VoddetailUtil.getInstance().stopEpsiodeRecordTime();
                        VoddetailUtil.getInstance().setEpsiodeRest(false);
                        VoddetailUtil.getInstance().resetSingleTime();
                    }
                    if (null != mCompletePopwindow && mCompletePopwindow.isShowing()) {
                        mCompletePopwindow.dismiss();
                    }
                    contiunePlaylly.setVisibility(View.GONE);
                    playCompletetv.setVisibility(View.GONE);
                    mHandler.removeMessages(DECREMENT_REST_TIME);
                    secondComfirmRestMode();
                    readyPlay();
                    break;
            }


        }

    }


    @Subscribe
    public void eventBlocking(BlockingEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                VoddetailUtil.getInstance().startRestTimeDecrement(event.getType());
                if (!TextUtils.isEmpty(event.getType())) {
//                    mImgShaw.setVisibility(View.VISIBLE);
                    if (isPlayingState()) {
                        mPlayView.pausePlay();
                        stopAllTiming();
                    } else {
                        mImgBb.setVisibility(View.VISIBLE);
                        playRelease();
                    }
                    if (mBrowseFrameLayout.getVisibility() == View.GONE) {
                        showCompletePopwindow();
                    }
                    switch (event.getType()) {
                        case VoddetailUtil.REST_BY_EPSIODE:
                        case VoddetailUtil.REST_BY_SINGLE:
                            if (contiunePlaylly.getVisibility() == View.GONE) {
                                contiunePlaylly.setVisibility(View.VISIBLE);
                                playCompletetv.setVisibility(View.GONE);
                                restRemainderTime = VoddetailUtil.getInstance().getRestTime();
                                mHandler.removeMessages(DECREMENT_REST_TIME);
                                mHandler.sendEmptyMessageDelayed(DECREMENT_REST_TIME, 1000);
                            }
                            break;
                        case VoddetailUtil.REST_BY_TODAY:
                            if (playCompletetv.getVisibility() == View.GONE) {
                                playCompletetv.setVisibility(View.VISIBLE);
                                contiunePlaylly.setVisibility(View.GONE);
                                mHandler.removeMessages(DECREMENT_REST_TIME);
                            }
                            break;
                    }


                }
            }
        });

    }


    public boolean refreshBookmark() {
        //???????????????????????????????????????????????????ANR
        if (null != mVODDetail && !TextUtils.isEmpty(sitcomNO) && (TextUtils.isEmpty(oldsitcomNO) || !oldsitcomNO.equals(sitcomNO))) {
            String vodType = mVODDetail.getVODType();
            if (null != chooseEpisodeList.findFocus() || null != total_episodes_GridView.findFocus()) {
                mDetailPresenter.setClickEpisode(true);
            }
            if (vodType.equals("0") || vodType.equals("2") || mVODDetail.getEpisodes() == null || mVODDetail.getEpisodes().size() == 0) {
                mDetailPresenter.setClickEpisode(false);
            }
            oldsitcomNO = sitcomNO;
            if (null != mVODDetail && null != mVODDetail.getEpisodes() && mVODDetail.getEpisodes().size() > 0) {
                isCanClick = false;
            }
            SuperLog.debug(TAG, "onActivityResult bookmark--->" + ",epsodeId:" + subContentId + ",sitcomNO:" + sitcomNO);
            Bookmark bookmark = new Bookmark();
            bookmark.setSitcomNO(sitcomNO);
            bookmark.setRangeTime("1");
            bookmark.setSubContentID(subContentId);
            VODDetail vodDetail = mDetailPresenter.getVODDetail();
            vodDetail.setBookmark(bookmark);
            Log.i(TAG, "setbookmark1: "+bookmark.getSitcomNO());
            mVODDetail.setBookmark(bookmark);
            mDetailPresenter.setVODDetail(vodDetail);
            setButtonInfo(vodDetail);
            setFavoriteStatus(isCollection);
            if (mDetailPresenter.isClickEpisode()) {
                mTitleText.setFocusable(true);
                mTitleText.requestFocus();
                setVodEpsiodes(vodDetail);
                mHandler.sendEmptyMessageDelayed(EPSIODE_REQUESTFOCUS, 20);
                mDetailPresenter.setClickEpisode(false);
                SuperLog.debug(TAG, "refreshBookmark->isClickEpisode:" + mDetailPresenter.isClickEpisode());
            } else {
                setVodEpsiodes(vodDetail);
                if (null != mVODDetail && null != mVODDetail.getEpisodes() && mVODDetail.getEpisodes().size() > 0) {
                    mHandler.sendEmptyMessageDelayed(REFRESH_EPSIODE_CANCLICK, 21);
                }
                return false;
            }
            //???????????????????????????????????????????????????ANR
            if (null != mVODDetail && null != mVODDetail.getEpisodes() && mVODDetail.getEpisodes().size() > 0) {
                mHandler.sendEmptyMessageDelayed(REFRESH_EPSIODE_CANCLICK, 21);
            }
            return true;
        } else {
            return false;
        }
    }


    private BrowseFrameLayout.OnFocusSearchListener mOnFocusSearchListener = new BrowseFrameLayout.OnFocusSearchListener() {
        @Override
        public View onFocusSearch(View focused, int direction) {
            return null;
        }
    };

    //???????????????????????????
    public void loadBackgroundImg(String bgUrl) {
        if (!this.isFinishing()) {
            posterIsCreated = true;
            SuperLog.info2SD("GlideLoadIPV6", "bgUrl is " + bgUrl);
            Glide.with(ChildModeVodDetailActivity.this).load(bgUrl).into(new SimpleTarget<Drawable>() {

                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    if (resource != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                BitmapDrawable db = (BitmapDrawable) resource;
                                Bitmap bitmap = FastBlur.blur(db.getBitmap(), 5.0f, 5.0f);
                                Message msg = Message.obtain();
                                msg.obj = bitmap;
                                msg.what = SET_BG;
                                mHandler.sendMessage(msg);
                            }
                        }).start();
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentTimes = mPlayView.getCurrentPosition();
        mPlayView.pausePlay();
        stopAllTiming();
        stopXiri();
    }


    @Override
    protected void onDestroy() {
        OrderConfigUtils.getInstance().clear();
        isDestory = true;
        super.onDestroy();
        SuperLog.debug(TAG, "onDestroy");
        RxApiManager.get().cancelAll();
        VoddetailUtil.getInstance().storeDate();
        mHandler.removeCallbacksAndMessages(null);
        VoddetailUtil.getInstance().setEpisodePollingListener(null);
//        ?????????????????????
        playRelease();
        mVODDetail = null;
        mFocusScene = null;


    }

    public void playRelease() {
        SuperLog.info(TAG, "onDestroyView()=====");
        //???????????????????????????
        if (null != mPlayView) {
            currentTimes = mPlayView.getCurrentPosition();
        }
        if (!PlayerAttriUtil.isEmpty(videoId) && playVodBean != null && !PlayerAttriUtil.isEmpty(playVodBean.getVodType())) {
            long currentSecond = currentTimes / 1000;
            Log.d(TAG, "bookmark episodeId:" + playVodBean.getEpisodeId() + "|currentTimes:" + currentTimes + "|totalTimes:" + totalTimes + "|tryToSeeFlag:" + tryToSeeFlag);
            if (tryToSeeFlag == 0) {
                if (currentSecond > 0) {
                    if (playVodBean != null && !TextUtils.isEmpty(playVodBean.getVodType()) && !playVodBean.getVodType().equals(Content.PROGRAM)) {
                        Bookmark bookMarkItem = new Bookmark();
                        bookMarkItem.setBookmarkType(playVodBean.getVodType());
                        bookMarkItem.setItemID(videoId);
                        bookMarkItem.setRangeTime(currentSecond + "");
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
                        bookMarkItem.setRangeTime("1");
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
            }
        }
        SuperLog.error(TAG, "mPlayView:" + mPlayView);
        if (null != mPlayView) {
            mPlayView.onDestory();
        }
        if (null != mPlayBackView && mPlayBackView.isShowing()) {
            mPlayBackView.dismiss();
        }

        if (null != controllerview && controllerview.isShowing()){
            controllerview.dismiss();
        }
    }

    public void deleteBookMark() {
        DeleteBookmarkRequest request = new DeleteBookmarkRequest();
        List<String> typeList = new ArrayList<>();
        List<String> idList = new ArrayList<>();
        typeList.add(BookmarkItem.BookmarkType.VOD);
        idList.add(mVODDetail.getID());
        request.setBookmarkTypes(typeList);
        request.setItemIDs(idList);
        mDetailPresenter.deleteBookmark(request, deleteBookmarkCallBack);

    }

    DeleteBookmarkCallBack deleteBookmarkCallBack = new DeleteBookmarkCallBack() {
        @Override
        public void deleteBookmarkSuccess() {

        }

        @Override
        public void deleteBookmarkFail() {

        }
    };

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
        if (bookmarks.size()>0){
            mVODDetail.setBookmark(bookmarks.get(0));
            Log.i(TAG, "setbookmark2: "+bookmarks.get(0).getSitcomNO());
        }

        request.setBookmarks(bookmarks);
        mDetailPresenter.createBookmark(request, createBookmarkCallBack);
    }

    CreateBookmarkCallBack createBookmarkCallBack = new CreateBookmarkCallBack() {
        @Override
        public void createBookmarkSuccess() {
            SuperLog.debug(TAG, "createBookmarkSuccess->currentTimes" + currentTimes);
            if (playVodBean != null && tryToSeeFlag == 0) {
                playVodBean.setSitcomNO(sitcomNO);
                playVodBean.setEpisodeId(subContentId);
                long currentSecond = 0;
                if (currentTimes != 0) {
                    if (!isCompelete) {
                        currentSecond = currentTimes / 1000;
                    }
                    playVodBean.setBookmark(currentSecond + "");
                }
                if (null != mVODDetail) {
                    Bookmark bookmark = new Bookmark();
                    bookmark.setSitcomNO(sitcomNO);
                    bookmark.setSubContentID(subContentId);
                    bookmark.setRangeTime(currentSecond + "");
                    Log.i(TAG, "createBookmarkSuccess: 1");
                    mVODDetail.setBookmark(bookmark);
                    if (null != mDetailPresenter)
                        mDetailPresenter.setVODDetail(mVODDetail);
                }
            }

        }

        @Override
        public void createBookmarkFail() {
            SuperLog.debug(TAG, "createBookmarkFail");
        }

    };

    private void startXiri() {
        mFocusScene = new Scene(this);
        mFocusScene.init(new ISceneListener() {
            @Override
            public String onQuery() {
                return
                        "{\"_scene\":\"com.pukka.ydepg.DETAIL\",\"_commands\":{\"key1\":[\"$W(collect)\"],\"key2\":[\"$W(cancel_collect)\"],\"key4\":[\"??????\",\"??????\"],\"key5\":[$P(_PLAY)],\"key6\":[$P(_EPISODE)],\"key7\":[\"????????????\",\"????????????\"],\"key8\":[\"????????????\"]},\"_fuzzy_words\":{\"collect\":[\"??????\"],\"cancel_collect\":[\"????????????\"],\"play\":[\"??????\"]}}";
            }

            @Override
            public void onExecute(Intent intent) {
                RefreshManager.getInstance().getScreenPresenter().exit();
                mFeedback.begin(intent);
                if (intent.hasExtra("_scene") && intent.getStringExtra("_scene").equals("com.pukka.ydepg.DETAIL") && null != mDetailPresenter) {
                    if (intent.hasExtra("_command")) {
                        String command = intent.getStringExtra("_command");
                        if ("key1".equals(command)) {
                            mFeedback.feedback("??????", Feedback.EXECUTION);
                            if (isCollection) {//?????????????????????
                                EpgToast.showToast(ChildModeVodDetailActivity.this, "????????????????????????");
                                return;
                            }
                            mDetailPresenter.setCollection(mVODDetail, isCollection);
                        } else if ("key2".equals(command)) {
                            mFeedback.feedback("????????????", Feedback.EXECUTION);
                            if (!isCollection) {
                                EpgToast.showToast(ChildModeVodDetailActivity.this, "???????????????????????????");
                                return;
                            }
                            mDetailPresenter.setCollection(mVODDetail, isCollection);
                        } else if ("key4".equals(command)) {
                            ChildModeVodDetailActivity.this.finish();
                            mFeedback.feedback("??????", Feedback.EXECUTION);
                        } else if ("key5".equals(command)) {
                            mXiriVoiceVodUtil.doPlayAction(intent.getExtras());
                        } else if ("key6".equals(command)) {
                            mXiriVoiceVodUtil.doEpisodeAction(intent.getExtras());
                        } else if ("key7".equals(command)) {
                            mXiriVoiceVodUtil.doSkipHistory(intent.getExtras());
                        } else if ("key8".equals(command)) {
                            playLastEpisodeLocal();
                        }
                    }
                }
            }
        });
    }

    //???????????????????????????
    private void showSubscript(VODDetail vodDetail) {
        is4KSource = false;
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

            //?????????????????????????????????????????????????????????
            if (!SubscriptControl.showSubscript(vodMediaFileList, mVODDetail)) {
                mVideoInfoImg1.setVisibility(View.INVISIBLE);
                return;
            } else {
                mVideoInfoImg1.setVisibility(View.VISIBLE);
            }

            //??????????????????????????????4K??????
            if (SubscriptControl.iszj4KVOD(mVODDetail)) {
                is4KSource = true;
                mVideoInfoImg1.setImageResource(R.drawable.z4k_child);
                return;
            }

            String definition = vodMediaFileList.get(0).getDefinition();
            if ("0".equals(definition)) {
                mVideoInfoImg1.setImageResource(R.drawable.child_sd);
            } else if ("1".equals(definition)) {
                mVideoInfoImg1.setImageResource(R.drawable.child_hd);
                //                mRightIcon.setBackgroundResource(R.drawable.details_right_hd_icon);
            } else if ("2".equals(definition)) {
                is4KSource = true;
                mVideoInfoImg1.setImageResource(R.drawable.child_4k);
            }
        }
    }

    private void stopXiri() {
        mFocusScene.release();
    }


    @Override
    public void play() {
        if (VoddetailUtil.getInstance().getRestTime() > 0) {
            return;
        }
        if (isCanReplayByTrySee()) {
            if (tryToSeeFlag == 1) {
                mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
            }
            if (null != mPlayView) {
                if (mPlayView.isPlaying()) {
                    return;
                }
                mPlayView.playerOrPause();
                startRecordPlayTime();
                showControlView(true);
            }
            if (mPlayBackView != null && mPlayBackView.isShowing()) {
                mPlayBackView.dismiss();
            }
        }
    }

    @Override
    public void pause() {
        if (VoddetailUtil.getInstance().getRestTime() > 0) {
            return;
        }
        if (null != mPlayView) {
            if (!mPlayView.isPlaying()) {
                return;
            }
            if (!mPlayBackView.isShowing() && mPlayView.isPlaying() && mPlayView.getDuration() > 0 && mBrowseFrameLayout.getVisibility() != View.VISIBLE) {
                mPlayBackView.showPlayBack(mOnDemandContainer, false, null);
            }
            mPlayView.playerOrPause();
            stopAllTiming();
            showControlView(true);
        }
    }

    @Override
    public void forWard(long time) {
        if (VoddetailUtil.getInstance().getRestTime() > 0) {
            rightTime = 0;
            return;
        }
        if (mPlayBackView != null && mPlayBackView.isShowing()) {
            mPlayBackView.dismiss();
        }

        if (tryToSeeFlag == 1) {
            mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
        }
        showControlView(true);
        mPlayView.fastForward(time * 1000);
        if (!mPlayView.isPlaying() && mCurrentPlayState == IPlayState.PLAY_STATE_HASMEDIA && isCanReplayByTrySee()) {
            mPlayView.resumePlay();
        }
        rightTime = 0;
    }

    @Override
    public void backForward(long time) {
        if (VoddetailUtil.getInstance().getRestTime() > 0) {
            rightTime = 0;
            return;
        }
        if (mPlayBackView != null && mPlayBackView.isShowing()) {
            mPlayBackView.dismiss();
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

    @Override
    public void nextPlay() {
        if (VoddetailUtil.getInstance().getRestTime() > 0) {
            rightTime = 0;
            return;
        }
        SuperLog.debug(TAG, "OnDemand nextPlay" + sitcomNO);
        if (mPlayBackView != null) {
            if (mPlayBackView.isShowing()) {
                mPlayBackView.dismiss();
            }
        }
        handlerNextPlay();
//        if (episodeDetailList != null && episodeDetailList.size() != 0) {
//            if (!TextUtils.isEmpty(sitcomNO)) {
//                int sitNum = Integer.parseInt(sitcomNO);
//                //????????????????????????,??????????????????
//                String lastNumStr = playVodBean.getIsReverse() == 0 ? episodeDetailList.get(episodeDetailList.size() - 1).getSitcomNO() : episodeDetailList.get(0).getSitcomNO();
//                int lastNum = -1;
//                if (!TextUtils.isEmpty(lastNumStr)) {
//                    lastNum = Integer.parseInt(lastNumStr);
//                }
//                if (lastNum != -1) {
//                    if (sitNum < lastNum) {
//                        for (int i = 0; i < episodeDetailList.size(); i++) {
//                            if (episodeDetailList.get(i).getSitcomNO().equals(sitcomNO)) {
//                                Message message = Message.obtain();
//                                message.what = EPISODE_PLAY;
//                                Bundle bundle = new Bundle();
//                                Episode episode = playVodBean.getIsReverse() == 0 ? episodeDetailList.get(i + 1) : episodeDetailList.get(i - 1);
//                                bundle.putString("SitcomNO", episode.getSitcomNO());
//                                bundle.putString("EpisodesId", episode.getVOD().getID());
//                                bundle.putString("MediaId", episode.getVOD().getMediaFiles().get(0).getID());
//                                bundle.putSerializable("episodeVod", episode.getVOD());
//                                epsiodeCustomFields = episode.getVOD().getCustomFields();
//                                vodTime = VodDetailCacheService.getVodTime(episode.getVOD().getMediaFiles().get(0));
//                                message.setData(bundle);
//                                mHandler.sendMessage(message);
//                            }
//                        }
//                    } else {
//                        EpgToast.showToast(this, "???????????????????????????");
//                    }
//                }
//            }
//        }
    }

    @Override
    public void prevPlay() {
        if (VoddetailUtil.getInstance().getRestTime() > 0) {
            rightTime = 0;
            return;
        }
        SuperLog.debug("lx", "OnDemand prevPlay" + sitcomNO);
        if (mPlayBackView != null) {
            if (mPlayBackView.isShowing()) {
                mPlayBackView.dismiss();
            }
        }

        //???????????????
        voddetailEpsiodesUtils.getNextOrPreEpisode(sitcomNO, false, new VoddetailEpsiodesUtils.GetEpisodeCallback() {
            @Override
            public void getEpisode(List<Episode> episodes, Episode episode) {
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
                if (mBrowseFrameLayout.getVisibility() == View.VISIBLE) {
                    refreshBookmark();
                }
            }

            @Override
            public void getEpisodeFail() {
                EpgToast.showToast(ChildModeVodDetailActivity.this, "????????????????????????");
            }
        });


//        if (episodeDetailList != null && episodeDetailList.size() != 0) {
//            if (!TextUtils.isEmpty(sitcomNO)) {
//                int sitNum = Integer.parseInt(sitcomNO);
//                //?????????????????????,?????????????????????
//                String firstNumStr = playVodBean.getIsReverse() == 0 ? episodeDetailList.get(0).getSitcomNO() : episodeDetailList.get(episodeDetailList.size() - 1).getSitcomNO();
//                int firstNum = -1;
//                if (!TextUtils.isEmpty(firstNumStr)) {
//                    firstNum = Integer.parseInt(firstNumStr);
//                }
//                if (firstNum != -1) {
//                    if (sitNum > firstNum) {
//                        for (int i = 0; i < episodeDetailList.size(); i++) {
//                            if (episodeDetailList.get(i).getSitcomNO().equals(sitcomNO)) {
//                                Message message = Message.obtain();
//                                message.what = EPISODE_PLAY;
//                                Bundle bundle = new Bundle();
//                                Episode episode = playVodBean.getIsReverse() == 0 ? episodeDetailList.get(i - 1) : episodeDetailList.get(i + 1);
//                                bundle.putString("SitcomNO", episode.getSitcomNO());
//                                bundle.putString("EpisodesId", episode.getVOD().getID());
//                                bundle.putString("MediaId", episode.getVOD().getMediaFiles().get(0).getID());
//                                bundle.putSerializable("episodeVod", episode.getVOD());
//                                epsiodeCustomFields = episode.getVOD().getCustomFields();
//                                vodTime = VodDetailCacheService.getVodTime(episode.getVOD().getMediaFiles().get(0));
//                                message.setData(bundle);
//                                mHandler.sendMessage(message);
//                            }
//                        }
//                    } else {
//                        EpgToast.showToast(this, "????????????????????????");
//                    }
//                }
//            }
//        }

    }

    @Override
    public void indexPlay(int index) {
        if (VoddetailUtil.getInstance().getRestTime() > 0) {
            rightTime = 0;
            return;
        }
        if (mPlayBackView != null && mPlayBackView.isShowing()) {
            mPlayBackView.dismiss();
        }

        tryseelayout.setVisibility(View.GONE);
        tryseelayoutDown.setVisibility(View.GONE);
        OrderConfigUtils.getInstance().clear();

        isPrepared = false;
        alacarteCode = "";
        voddetailEpsiodesUtils.getEpisode(index + "", new VoddetailEpsiodesUtils.GetEpisodeCallback() {
            @Override
            public void getEpisode(List<Episode> episodes, Episode episode) {
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

            }
        });

    }

    @Override
    public void seekTo(int position) {
        if (VoddetailUtil.getInstance().getRestTime() > 0) {
            rightTime = 0;
            return;
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

    }

    @Override
    public void rePlay() {
        if (VoddetailUtil.getInstance().getRestTime() > 0) {
            rightTime = 0;
            return;
        }
        if (mPlayBackView != null && mPlayBackView.isShowing()) {
            mPlayBackView.dismiss();
        }
        mPlayView.rePlay();
        showControlView(true);
    }

    @Override
    public void doSkipHistory() {
    }

    @Override
    public void playLastEpisode() {
        EpgToast.showLongToast(this, "???????????????????????????");

    }

    public void playLastEpisodeLocal() {
        if (VoddetailUtil.getInstance().getRestTime() > 0) {
            rightTime = 0;
            return;
        }
        if (mPlayBackView != null && mPlayBackView.isShowing()) {
            mPlayBackView.dismiss();
        }
        List<List<String>> sitNums = voddetailEpsiodesUtils.getSitcomNos();
        List<String> lastNums = sitNums.get(sitNums.size() - 1 );
        String lastNum = lastNums.get(lastNums.size() - 1);
        voddetailEpsiodesUtils.getEpisode(lastNum, new VoddetailEpsiodesUtils.GetEpisodeCallback() {
            @Override
            public void getEpisode(List<Episode> episodes, Episode episode) {
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
                EpgToast.showToast(ChildModeVodDetailActivity.this, "??????????????????!");
            }
        });


//        if (episodeDetailList != null && episodeDetailList.size() > 0) {
//            SuperLog.info2SD(TAG, "mVODDetail.getVodNum=" + mVODDetail.getVodNum() + "--->episodeDetailList.size()=" + episodeDetailList.size());
//            if (Integer.valueOf(mVODDetail.getVodNum()) == episodeDetailList.size()) {
//                int index = episodeDetailList.size() - 1;
////                if (episodeDetailList.get(i).getSitcomNO().equals(sitNum)) {
//                Message message = Message.obtain();
//                message.what = BrowseTVPlayFragment.EPISODE_PLAY;
//                Bundle bundle = new Bundle();
//                bundle.putString("SitcomNO", episodeDetailList.get(index).getSitcomNO());
//                bundle.putString("EpisodesId", episodeDetailList.get(index).getVOD().getID());
//                bundle.putString("MediaId", episodeDetailList.get(index).getVOD().getMediaFiles().get(0).getID());
//                bundle.putSerializable("episodeVod", episodeDetailList.get(index).getVOD());
//                epsiodeCustomFields = episodeDetailList.get(index).getVOD().getCustomFields();
//                vodTime = VodDetailCacheService.getVodTime(episodeDetailList.get(index).getVOD().getMediaFiles().get(0));
//                message.setData(bundle);
//                mHandler.sendMessage(message);
////                }
//            } else {
//                EpgToast.showToast(this, "??????????????????!");
//            }
//        }

    }

    @Override
    public void getVODPlayUrlSuccess(String url, String bookmark, String productId, String
            elapseTime) {

        isPrepared = false;
        OwnChooseEvent event = new OwnChooseEvent(false);
        OrderConfigUtils.getInstance().setEvent(event);

        //??????????????????????????????????????????
        this.productId = productId;
        this.elapseTime = elapseTime;
        if (null != playVodBean) {
            playVodBean.setElapseTime(elapseTime);
            playVodBean.setTryToSeeFlag(0);
        }
        tryToSeeFlag = 0;
        if (isTrySeeSubscribeSucess) {
            tryseelayout.setVisibility(View.GONE);
            tryseelayoutDown.setVisibility(View.GONE);
            mPlayView.setTryToSeeFlag(0);
            if (mHandler.hasMessages(UPDATE_DURATION)) {
                mHandler.removeMessages(UPDATE_DURATION);
            }
            isTrySeeSubscribeSucess = false;
        } else {
            if (!PlayerAttriUtil.isEmpty(url)) {
                videoPath = url;
                initVideoView(videoPath);
            } else {
                EpgToast.showToast(this, getResources().getString(R.string.notice_play_url_is_empty));

            }
        }
        EventBus.getDefault().post(new FinishPlayUrlEvent());
        UBDPlay.recordSwitchEpisode(mVODDetail, subContentId, action);
    }

    @Override
    public void getChannelPlayUrlSuccess(String url, String attachedPlayURL, String bookmark) {
    }

    @Override
    public void playFail() {
    }

    @Override
    public void playFail(PlayVODResponse response) {
        tryToSeeFlag = 1;
        isTrySeeSubscribeSucess = false;
        String returnCode = response.getResult().getRetCode();
        if (!TextUtils.isEmpty(returnCode) && returnCode.equals("146021014")) {
            EpgToast.showToast(this, getResources().getString(R.string.notice_location_is_limited));
            return;
        }
        OwnChooseEvent event = new OwnChooseEvent(true);
        if (null != response.getAuthorizeResult() && null != response.getAuthorizeResult().getPricedProducts()
                && response.getAuthorizeResult().getPricedProducts().size() > 0){
            event.setPricedProducts(response.getAuthorizeResult().getPricedProducts());
        }
        OrderConfigUtils.getInstance().setEvent(event);

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
                    playVodBean.setAuthResult(JsonParse.object2String(response.getAuthorizeResult()));
                    getIntentInfo();
                } else {
                    EpgToast.showToast(this, getResources().getString(R.string.notice_not_allowed_free_trial_forNow));
                }
                UBDPlay.recordSwitchEpisode(mVODDetail, subContentId, UBDConstant.ACTION.PLAY_PREVIEW);
            } else {
                AuthorizeResult authorizeResult = response.getAuthorizeResult();
                if (null != authorizeResult) {
                    List<Product> products = authorizeResult.getPricedProducts();
                    if (SessionService.getInstance().getSession().isHotelUser()) {
                        if (products == null || products.size() == 0) {
                            EpgToast.showToast(this, R.string.notice_no_orderable_product);
                            return;
                        }
                        for (int i = products.size() - 1; i >= 0; i--) {
                            Product mProductInfo = products.get(i);
                            if (!StringUtils.isSubscribeByDay(mProductInfo)) {
                                products.remove(i);
                            }

                        }
                        if (products == null || products.isEmpty()) {
                            EpgToast.showToast(this, R.string.notice_no_orderable_product);
                            return;
                        }
                        authorizeResult.setPricedProducts(products);

                    } else {
                        if (products == null || products.isEmpty()) {
                            EpgToast.showToast(this, R.string.notice_no_orderable_product);
                            return;
                        }
                    }

                } else {
                    EpgToast.showToast(this, R.string.notice_no_orderable_product);
                    return;
                }
                String authResult = JsonParse.object2String(authorizeResult);
                String needJumpToH5Order = SessionService.getInstance().getSession().getTerminalConfigurationNeedJumpToH5Order();
                if (null != needJumpToH5Order && needJumpToH5Order.equals("1")) {
                    JumpToH5OrderUtils.getInstance().jumpToH5OrderFromTrySee(authorizeResult.getPricedProducts(), this, false, false, null, mVODDetail);
                    //pbs????????????
                    Log.i(TAG, "PbsUaService: "+mVODDetail.getID());
                    PbsUaService.report(Play.getPurchaseData(mVODDetail.getID()));
                } else {
                    Intent intent = new Intent(this, NewProductOrderActivity.class);
                    intent.putExtra(NewProductOrderActivity.AUTHORIZE_RESULT, authResult);
                    intent.putExtra(ZjYdUniAndPhonePayActivty.VOD_DETAIL, VodUtil.getSimpleVoddetail(mVODDetail, 5 * 60));
                    intent.putExtra(ZjYdUniAndPhonePayActivty.IS_TRY_SEE_SUBSCRIBE, true);
                    startActivity(intent);
                    finish();
                }
            }
        } else if (!TextUtils.isEmpty(returnCode) && returnCode.equals("144020000")) {
            EpgToast.showToast(this, getResources().getString(R.string.notice_no_orderable_product));
            switchAutherateFail();
        }else {
            EpgToast.showToast(this, getResources().getString(R.string.notice_media_auth_failed));
            switchAutherateFail();
        }
    }

    public void switchAutherateFail() {
        if (null != playVodBean && !PlayerAttriUtil.isEmpty(playVodBean.getVodName())) {
//            vodTitletv.setText(playVodBean.getVodName());
//            if ((!TextUtils.isEmpty(sitcomNO) && !TextUtils.isEmpty(subContentId))) {
////                vodTitletv.setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//                mPlayView.setVodTitle(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//                mPlayView.getmRecord().setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//                mPlayView.getmRecord().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.T29_C21_Bold_size));
//                tryToSeeFlag = 1;
//            }
//            vodTitletv.setSelected(true);

            //????????????????????????
            mPlayView.getmTitleUp().setText(playVodBean.getVodName());
            if (!TextUtils.isEmpty(sitcomNO)) {
                mPlayView.getmTitleUp().setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
            }
            mPlayView.getmTitleUp().setSelected(true);
            mPlayView.getmTitleUp().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.margin_32));
        }
    }

    @Override
    public void onPlaycancel() {

    }

    @Override
    public void getVODDownloadUrlSuccess(String vodID, String url, String postURL, String
            switchNum, String name) {

    }

    @Override
    public void getVODDownloadUrlFailed(String vodID, String episodeID) {

    }

    @Override
    public void onPlayState(int playbackState) {
        mCurrentPlayState = playbackState;
        SuperLog.debug(TAG, "playbackState:" + EventLogger.getStateString(playbackState));
        if (playbackState == IPlayState.PLAY_STATE_HASMEDIA && null != mPlayView) {
            totalTimes = mPlayView.getDuration();
        }
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    public boolean isAdmitOperationPlayControl() {
        if (null == mBrowseFrameLayout){
            return false;
        }
        return mBrowseFrameLayout.getVisibility() == View.GONE;
    }

    private boolean isPrepared = false;

    //?????????????????????????????????????????????????????????
    private boolean isJiutianReport = false;
    //????????????????????????
    private long startPoint = 0;

    @Override
    public void onPrepared(int Videotype) {
        mPlayView.resetControlState();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                canOnKey = true;
            }
        },1200);
        isPrepared = true;

        isJiutianReport = false;
        if (!TextUtils.isEmpty(mJiutianTrackerUrl)){
            startPoint = System.currentTimeMillis();
            SuperLog.debug(TAG,"jiutian play startpoint "+startPoint);
            //????????????????????????tracker,??????????????????
            JiutianService.reportPlay(mJiutianTrackerUrl);
        }

        mPlayView.setSpeed(switchSpeed());
        if (null != controllerview && controllerview.isShowing()){
            controllerview.dismiss();
        }
        boolean canSetSpeedConfig = SessionService.getInstance().getSession().getTerminalConfigurationCanSetSpeed();
        if (canSetSpeedConfig && mPlayView.canSetSpeed(switchSpeed())){
            canSetSpeed = true;
        }else{
            canSetSpeed = false;
        }
//        canSetSpeed = mPlayView.canSetSpeed(switchSpeed());
        mPlayView.setFirstShowTopRecord(true);
        if (isAdmitOperationPlayControl()) {
            showControlView(true);
        }
        mImgBb.setVisibility(View.GONE);
//        mImgShaw.setVisibility(View.GONE);
        if (tryToSeeFlag == 0) {
            reportVod(ReportVODRequest.START_PLAY);

        }
        startRecordPlayTime();

        if (!(mBrowseFrameLayout.getVisibility() == View.VISIBLE)){
            setRecordText(true);
        }else{
            isFirstHideDetailDesc = true;
        }

        if (tryToSeeFlag == 1) {
            if (OrderConfigUtils.getInstance().needShowPopWindow() && !OrderConfigUtils.getInstance().isAlacarte()){
                String timeStr = getTrySeeTime(tryToSeeTime);
                seehinttv.setText(String.format(getResources().getString(R.string.notice_free_trial_play_new), timeStr));
                seehinttvDown.setText(String.format(getResources().getString(R.string.notice_free_trial_play_new), timeStr));
                seehinttv.setHighlightColor(getResources().getColor(android.R.color
                        .transparent));
                tryseelayout.setVisibility(View.VISIBLE);
                mHandler.sendEmptyMessageDelayed(SWITCH_TRY_SEE_HINT, 5 * 1000);
                mHandler.sendEmptyMessageDelayed(HIDE_MARKETING_CONTENT, 10 * 1000);
            }
        }

        reportBookmark(BookMarkSwitchs.START);
    }

    @Override
    public void onRelease() {

        if (!TextUtils.isEmpty(mJiutianTrackerUrl) && !isJiutianReport){
            long duration = System.currentTimeMillis() - startPoint;
            SuperLog.debug(TAG,"jiutian play end point "+System.currentTimeMillis() + " duration "+ duration);
            if (startPoint > 0 && duration > 0){
                JiutianService.reportPlayend(mJiutianTrackerUrl,duration);
            }
            isJiutianReport = true;
        }

        mPlayView.setControllViewState(View.GONE, true);
        if (tryToSeeFlag == 0) {
            reportVod(ReportVODRequest.FINISH_PLAY);
        }
        if (!TextUtils.isEmpty(newReportVodId)) {
            reportVodId = newReportVodId;
            reportMediaId = newReportMediaId;
        }
        stopAllTiming();


    }

    private void reportBookmark(String reportType) {
        String switchs = SessionService.getInstance().getSession().getTerminalConfigurationValue("add_bookmark_switchs");
        BookMarkSwitchs bookMarkSwitchs = null;
        if (!TextUtils.isEmpty(switchs)) {
            bookMarkSwitchs = JsonParse.json2Object(switchs, BookMarkSwitchs.class);
        }

        if (TextUtils.equals(reportType, BookMarkSwitchs.START) || TextUtils.equals(reportType, BookMarkSwitchs.DESTORY) || TextUtils.equals(reportType, BookMarkSwitchs.QUIT) || (bookMarkSwitchs != null && TextUtils.equals("0", bookMarkSwitchs.getBookmarkSwitchsValue(reportType)))) {
            //???????????????????????????
            if (!PlayerAttriUtil.isEmpty(videoId) && playVodBean != null && !PlayerAttriUtil.isEmpty(playVodBean.getVodType())) {
                long currentSecond = 0;
                if (!isCompelete) {
                    currentSecond = currentTimes / 1000;
                }

                Log.d(TAG, "bookmark episodeId:" + playVodBean.getEpisodeId() + "|currentTimes:" + currentTimes + "|totalTimes:" + totalTimes + "|tryToSeeFlag:" + tryToSeeFlag);

                if (currentSecond > 0 && totalTimes - currentTimes > 5000) {
                    if (playVodBean != null && !TextUtils.isEmpty(playVodBean.getVodType()) && !playVodBean.getVodType().equals(Content.PROGRAM)) {
                        Bookmark bookMarkItem = new Bookmark();
                        bookMarkItem.setBookmarkType(playVodBean.getVodType());
                        bookMarkItem.setItemID(videoId);
                        if (tryToSeeFlag == 0) {
                            bookMarkItem.setRangeTime(currentSecond + "");
                        }else{
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
                        bookMarkItem.setRangeTime("1");
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
            }
        }
    }


    public void reportVod(int action) {
        if (null != mDetailPresenter) {
            mDetailPresenter.reportVod(action, reportVodId, reportMediaId, subjectId, productId);
        }

    }

    @Override
    public void onPlayError(String msg, int errorCode, int playerType) {
        if (!isFromXmpp() && !isXmpp && !TextUtils.isEmpty(msg)) {
            EpgToast.showToast(OTTApplication.getContext(), msg);
        }
    }

    @Override
    public void onPlayCompleted() {

        if (!TextUtils.isEmpty(mJiutianTrackerUrl) && !isJiutianReport){
            long duration = System.currentTimeMillis() - startPoint;
            SuperLog.debug(TAG,"jiutian play end point "+System.currentTimeMillis() + " duration "+ duration);
            if (startPoint > 0 && duration > 0){
                JiutianService.reportPlayend(mJiutianTrackerUrl,duration);
            }
            isJiutianReport = true;
        }

        //??????????????????  0?????????
        if (isFilm.equals("0")) {
            isCompelete = true;
            mHandler.sendEmptyMessage(BACK_PLAY);
        } else {
            mHandler.sendEmptyMessage(NEXT_PLAY);
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

    //????????????????????????
    public boolean isCanReplayByTrySee() {
        if (null != mPlayView) {
            SuperLog.debug(TAG, "isCanReplayByTrySee->tryToSeeFlag:" + tryToSeeFlag + "|tryToSeeTime:" + tryToSeeTime * 1000 + "|CurrentPosition:" + mPlayView.getCurrentPosition());
            return tryToSeeFlag != 1 || tryToSeeTime * 1000 > mPlayView.getCurrentPosition();
        } else {
            return false;
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

    }

    public void getIntentInfo() {
        String contentCode = "";
        if (mVODDetail.getVODType().equals("0") || mVODDetail.getVODType().equals("2") ){
            contentCode = mVODDetail.getCode();
        }else if (null != currentVod){
            contentCode = currentVod.getCode();
        }

        OrderConfigUtils.getInstance().config(contentCode, new OrderConfigUtils.ConfigCallBack() {
            @Override
            public void configDone() {

                if (isFinishing()){
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getIntentInfoAfterConfig();
                    }
                });
            }
        });
    }

    private void getIntentInfoAfterConfig(){
        if (playVodBean != null && !PlayerAttriUtil.isEmpty(playVodBean.getPlayUrl())) {
            if (!PlayerAttriUtil.isEmpty(playVodBean.getPlayUrl())) {
                videoPath = playVodBean.getPlayUrl();

            }

            if (!PlayerAttriUtil.isEmpty(playVodBean.getSitcomNO())) {
                sitcomNO = playVodBean.getSitcomNO();
                subContentId = playVodBean.getEpisodeId();

            }
            currentMediaId = playVodBean.getMediaId();
            if (!PlayerAttriUtil.isEmpty(playVodBean.getVodId())) {
                videoId = playVodBean.getVodId();
                isFilm = playVodBean.getIsFilm();
            }
            if (isFromXmpp()) {
                EpgToast.showToast(this, "????????????????????????" + StringUtils.getEncryptionNumber(xmppmessage.getActionSource()) + "????????????");
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

            if (!PlayerAttriUtil.isEmpty(playVodBean.getElapseTime())) {
                this.elapseTime = playVodBean.getElapseTime();
            }

            String detailStr = playVodBean.getDetailStr();
            if (!TextUtils.isEmpty(detailStr)) {
                VODDetail vODDetail = JsonParse.json2Object(detailStr, VODDetail.class);
                if (null != vODDetail) {
                    fatherPrice = vODDetail.getPrice();
                }
            }
        }
        initVideoView(videoPath);
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

    public void initVideoView(String videoPath) {
        //TODO
        if (isCanPlay()) {
            isCompelete = false;
            if (null != mCompletePopwindow && mCompletePopwindow.isShowing()) {
                mCompletePopwindow.dismiss();
            }
            if (contiunePlaylly.getVisibility() == View.VISIBLE) {
                contiunePlaylly.setVisibility(View.GONE);
            }
            if (playCompletetv.getVisibility() == View.VISIBLE) {
                playCompletetv.setVisibility(View.GONE);
            }

            if (null != playVodBean && !TextUtils.isEmpty(playVodBean.getVodType()) && playVodBean.getVodType().equals(Content.PROGRAM)) {
                mPlayView.setResizeMode(PlayView.RESIZE_MODE_FIT);
            }
            if (tryToSeeFlag == 1) {
                mPlayView.setTryToSeeFlag(tryToSeeFlag);
                String detailStr = null;
                if (null != playVodBean) {
                    detailStr = playVodBean.getDetailStr();
                }
                VODDetail vodDetail = null;
                if (!TextUtils.isEmpty(detailStr)) {
                    vodDetail = JsonParse.json2Object(detailStr, VODDetail.class);
                }
                if (null != epsiodeCustomFields) {
                    tryToSeeTime = VodDetailCacheService.getPreviewDuration(mVODDetail.getCustomFields(), epsiodeCustomFields, vodTime);
                } else if (null != vodDetail) {
                    tryToSeeTime = vodDetail.getPreviewDuration();
                    if (tryToSeeTime == -1) {
                        tryToSeeTime = VodDetailCacheService.getPreviewDuration(vodTime);
                    }
                } else {
                    String trySee = SessionService.getInstance().getSession().getTerminalConfigurationValue("vod_watch_validTime");
                    if (!TextUtils.isEmpty(trySee)) {
                        tryToSeeTime = Integer.parseInt(trySee);
                    }
                }

                Log.i(TAG, "initVideoView: tryToSeeTime"+tryToSeeTime);
                mPlayView.setTryToSeeTime((int) (tryToSeeTime * 1000));
                String timeStr = getTrySeeTime(tryToSeeTime);
                seehinttv.setText(String.format(getResources().getString(R.string.notice_free_trial_play_new), timeStr));
                seehinttv.setHighlightColor(getResources().getColor(android.R.color
                        .transparent));
//                tryseelayout.setVisibility(View.VISIBLE);
//                mHandler.sendEmptyMessageDelayed(HIDE_TRY_SEE_HINT, 10 * 1000);
//                mHandler.sendEmptyMessageDelayed(HIDE_MARKETING_CONTENT, 10 * 1000);
                mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
            } else {
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
                tryseelayoutDown.setVisibility(View.GONE);
            }
            mPlayView.releasePlayer();
            mHandler.removeMessages(KEY_CLICK_RECORD);
            mHandler.sendEmptyMessageDelayed(KEY_CLICK_RECORD, 1000);
            if (isFirstFlag) {
                if (null != playVodBean && !PlayerAttriUtil.isEmpty(playVodBean.getBookmark())) {
                    SuperLog.debug(TAG, "initVideoView->bookmark:" + playVodBean.getBookmark());
                    if (tryToSeeFlag == 1) {
                        //????????????????????????????????????
                        long bookMark = Long.parseLong(playVodBean.getBookmark());
                        //????????????????????????
                        mPlayView.setWindowState(1);
                        if (bookMark >= tryToSeeTime) {
                            mPlayView.startPlay(videoPath, (tryToSeeTime - 1) * 1000);
                        } else {
                            mPlayView.startPlay(videoPath, Long.parseLong(playVodBean.getBookmark()) * 1000);
                        }
                    } else {
//                        mPlayView.startPlay(videoPath, Long.parseLong(playVodBean.getBookmark()) * 1000);
                        boolean isSkipStart = SharedPreferenceUtil.getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "", true);

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
                        mPlayView.setSkipStart(isSkipStart);
                        headDuration = 0;
                        tailDuration = 0;
                        if (TextUtils.equals("0", isFilm)) {
                            headDuration = mVODDetail.getMediaFiles().get(0).getHeadDuration();
                            tailDuration = mVODDetail.getMediaFiles().get(0).getTailDuration();
                            if (tailDuration == 0) {
                                mPlayView.setNeedToSkip(false);
                                isNeedToSkip = false;
                            } else {
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
                            voddetailEpsiodesUtils.getEpisode(sitcomNO, new VoddetailEpsiodesUtils.GetEpisodeCallback() {
                                @Override
                                public void getEpisode(List<Episode> episodes, Episode episode) {
                                    headDuration = episode.getVOD().getMediaFiles().get(0).getHeadDuration();
                                    tailDuration = episode.getVOD().getMediaFiles().get(0).getTailDuration();
                                    if (tailDuration == 0) {
                                        mPlayView.setNeedToSkip(false);
                                        isNeedToSkip = false;
                                        canSetSkip = false;
                                    } else {
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

                                @Override
                                public void getEpisodeFail() {

                                }
                            });
//                            if (episodeDetailList != null && episodeDetailList.size() > 0) {
//                                for (Episode episode : episodeDetailList) {
//                                    if (TextUtils.equals(episode.getSitcomNO(), sitcomNO)) {
//                                        headDuration = episode.getVOD().getMediaFiles().get(0).getHeadDuration();
//                                        tailDuration = episode.getVOD().getMediaFiles().get(0).getTailDuration();
//                                        if (headDuration == 0 && tailDuration == 0) {
//                                            mPlayView.setNeedToSkip(false);
//                                            isNeedToSkip = false;
//                                        } else {
//                                            mPlayView.setNeedToSkip(true);
//                                            isNeedToSkip = true;
//                                        }
//                                        break;
//                                    }
//                                }
//                            }
                        }
                        mPlayView.setEndTime(tailDuration * 1000);
                        isNeedShowSkipBottomRecord = Long.parseLong(playVodBean.getBookmark()) < headDuration;
                        //????????????????????????
                        mPlayView.setWindowState(1);
                        mPlayView.startPlay(videoPath, isSkipStart && isNeedShowSkipBottomRecord ? headDuration * 1000 : Long.parseLong(playVodBean.getBookmark()) * 1000);
                    }
                } else {
//                    mPlayView.startPlay(videoPath, 0);
                    headDuration = 0;
                    tailDuration = 0;
                    if (TextUtils.equals("0", isFilm)) {
                        headDuration = mVODDetail.getMediaFiles().get(0).getHeadDuration();
                        tailDuration = mVODDetail.getMediaFiles().get(0).getTailDuration();
                        if (tailDuration == 0) {
                            mPlayView.setNeedToSkip(false);
                            isNeedToSkip = false;
                        } else {
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
                        voddetailEpsiodesUtils.getEpisode(sitcomNO, new VoddetailEpsiodesUtils.GetEpisodeCallback() {
                            @Override
                            public void getEpisode(List<Episode> episodes, Episode episode) {
                                headDuration = episode.getVOD().getMediaFiles().get(0).getHeadDuration();
                                tailDuration = episode.getVOD().getMediaFiles().get(0).getTailDuration();
                                if (tailDuration == 0) {
                                    mPlayView.setNeedToSkip(false);
                                    isNeedToSkip = false;
                                } else {
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

                            @Override
                            public void getEpisodeFail() {

                            }
                        });

//                        if (episodeDetailList != null && episodeDetailList.size() > 0) {
//                            for (Episode episode : episodeDetailList) {
//                                if (TextUtils.equals(episode.getSitcomNO(), sitcomNO)) {
//                                    headDuration = episode.getVOD().getMediaFiles().get(0).getHeadDuration();
//                                    tailDuration = episode.getVOD().getMediaFiles().get(0).getTailDuration();
//                                    if (headDuration == 0 && tailDuration == 0) {
//                                        mPlayView.setNeedToSkip(false);
//                                        isNeedToSkip = false;
//                                    } else {
//                                        mPlayView.setNeedToSkip(true);
//                                        isNeedToSkip = true;
//                                    }
//                                    break;
//                                }
//                            }
//                        }
                    }
                    boolean isSkipStart = SharedPreferenceUtil.getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "", true);

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
                    mPlayView.setSkipStart(isSkipStart);
                    mPlayView.setEndTime(tailDuration * 1000);
                    isNeedShowSkipBottomRecord = true;
                    //????????????????????????
                    mPlayView.setWindowState(1);
                    mPlayView.startPlay(videoPath, isSkipStart ? headDuration * 1000 : 0);
                }
                UBDPlay.record(mVODDetail, subjectId, tryToSeeFlag == 1, playVodBean == null ? null : playVodBean.getRecommendType(), this.getClass().getSimpleName());
                isFirstFlag = false;
            } else {
                headDuration = 0;
                tailDuration = 0;
                if (TextUtils.equals("0", isFilm)) {
                    headDuration = mVODDetail.getMediaFiles().get(0).getHeadDuration();
                    tailDuration = mVODDetail.getMediaFiles().get(0).getTailDuration();
                    if (tailDuration == 0) {
                        mPlayView.setNeedToSkip(false);
                        isNeedToSkip = false;
                    } else {
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
                    voddetailEpsiodesUtils.getEpisode(sitcomNO, new VoddetailEpsiodesUtils.GetEpisodeCallback() {
                        @Override
                        public void getEpisode(List<Episode> episodes, Episode episode) {
                            headDuration = episode.getVOD().getMediaFiles().get(0).getHeadDuration();
                            tailDuration = episode.getVOD().getMediaFiles().get(0).getTailDuration();
                            if (tailDuration == 0) {
                                mPlayView.setNeedToSkip(false);
                                isNeedToSkip = false;
                            } else {
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

                        @Override
                        public void getEpisodeFail() {

                        }
                    });

//                    if (episodeDetailList != null && episodeDetailList.size() > 0) {
//                        for (Episode episode : episodeDetailList) {
//                            if (TextUtils.equals(episode.getSitcomNO(), sitcomNO)) {
//                                headDuration = episode.getVOD().getMediaFiles().get(0).getHeadDuration();
//                                tailDuration = episode.getVOD().getMediaFiles().get(0).getTailDuration();
//                                if (headDuration == 0 && tailDuration == 0) {
//                                    mPlayView.setNeedToSkip(false);
//                                    isNeedToSkip = false;
//                                } else {
//                                    mPlayView.setNeedToSkip(true);
//                                    isNeedToSkip = true;
//                                }
//                                break;
//                            }
//                        }
//                    }
                }
                boolean isSkipStart = SharedPreferenceUtil.getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "", true);

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
                mPlayView.setSkipStart(isSkipStart);
                mPlayView.setEndTime(tailDuration * 1000);
                isNeedShowSkipBottomRecord = true;
                //????????????????????????
                mPlayView.setWindowState(1);
                mPlayView.startPlay(videoPath, isSkipStart ? headDuration * 1000 : 0);
            }
            if (playVodBean != null) {
                if (!PlayerAttriUtil.isEmpty(playVodBean.getVodName())) {

                    //????????????????????????
                    mPlayView.getmTitleUp().setText(playVodBean.getVodName());
                    if (!TextUtils.isEmpty(sitcomNO)) {
                        mPlayView.getmTitleUp().setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
                    }
                    mPlayView.getmTitleUp().setSelected(true);
                    mPlayView.getmTitleUp().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.margin_32));

//                    //?????????????????????????????????????????????
//                    mPlayView.getmRecord().setText(playVodBean.getVodName());
//                    mPlayView.setVodTitle(playVodBean.getVodName());
//                    if (!TextUtils.isEmpty(sitcomNO)) {
//                        mPlayView.setVodTitle(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//                        mPlayView.getmRecord().setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//                    }
//                    mPlayView.getmRecord().setSelected(true);
//                    mPlayView.getmRecord().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.T29_C21_Bold_size));


//                    if (isNeedToSkip) {
//                        vodTitletv.setText(playVodBean.getVodName());
//                        mPlayView.setVodTitle(playVodBean.getVodName());
//                        if (!TextUtils.isEmpty(sitcomNO)) {
//                            vodTitletv.setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//                            mPlayView.setVodTitle(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//                        }
//                        vodTitletv.setSelected(true);
//                        mPlayView.getmRecord().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.T24_C23_Light_size));
//                    } else {
//                        mPlayView.getmRecord().setText(playVodBean.getVodName());
//                        mPlayView.setVodTitle(playVodBean.getVodName());
//                        if (!TextUtils.isEmpty(sitcomNO)) {
//                            mPlayView.setVodTitle(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//                            mPlayView.getmRecord().setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//                        }
//                        mPlayView.getmRecord().setSelected(true);
//                        mPlayView.getmRecord().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.T29_C21_Bold_size));
//                    }
                } else {
                    SuperLog.debug(TAG, "playVodBean??????");
                }
            }
        }
    }

    public String getTrySeeTime(long tryToSeeTime) {
        StringBuffer buffer = new StringBuffer();
        long minute = tryToSeeTime / 60;
        long second = tryToSeeTime % 60;
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


    /**
     * ???????????????
     *
     * @param event
     */
    @Subscribe
    public void xmppSuccess(XmppSuccessEvent event) {
        isXmpp = true;
    }

    //???????????????vod??????
    private VOD currentVod ;

    //????????????
    public void episondePlay(Message msg) {
        tryseelayout.setVisibility(View.GONE);
        tryseelayoutDown.setVisibility(View.GONE);
        OrderConfigUtils.getInstance().clear();
        isPrepared = false;
        alacarteCode = "";
        Log.i(TAG, "episondePlay: ");
        canOnKey = false;
        String mediaId1 = "";
        String episodesId = "";
        Bundle episodeBundle = msg.getData();
        sitcomNO = episodeBundle.getString("SitcomNO");
        Log.i(TAG, "episondePlay: ");
        if (null != epsiodesUtils){
            epsiodesUtils.refresh(sitcomNO);
        }
        if (null != voddetailEpsiodesUtils){
            voddetailEpsiodesUtils.refresh(sitcomNO);
        }
        episodesId = episodeBundle.getString("EpisodesId");
        subContentId = episodesId;
        mediaId1 = episodeBundle.getString("MediaId");
        newReportVodId = episodesId;
        newReportMediaId = mediaId1;

        if (null != episodeBundle.getSerializable("episodeVod")) {
            VOD vod = (VOD) episodeBundle.getSerializable("episodeVod");
            Log.i(TAG, "currentVod: 5");
            currentVod = vod;
            if (null != vod.getCustomFields()) {
                epsiodeCustomFields = vod.getCustomFields();
            }
            elapseTime = vod.getMediaFiles().get(0).getElapseTime();
            vodTime = VodDetailCacheService.getVodTime(vod.getMediaFiles().get(0));
        }

        if (!PlayerAttriUtil.isEmpty(videoId) && !PlayerAttriUtil.isEmpty(episodesId) && !PlayerAttriUtil.isEmpty(mediaId1)) {
            action = UBDConstant.ACTION.PLAY_NORMAL;
            //??????????????????????????????
            playVODAuthorize(episodesId, videoId, mediaId1, elapseTime);
        } else {
            EpgToast.showToast(this, "????????????");
        }
        if (mBrowseFrameLayout.getVisibility() == View.VISIBLE) {
            refreshBookmark();
        }
    }

    //????????????
    public void playVODAuthorize(String vodId, String seriesId, String mediaId, String elapseTime) {
        isPrepared = false;
        alacarteCode = "";
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

    @Override
    public void getVODDetailSuccess(VODDetail vodDetail, String recmActionID, List<RecmContents>
            recmContents) {
        if (null != vodDetail) {
            mDetailPresenter.setCollection(vodDetail, vodDetail.getFavorite() != null);
        }
    }

    @Override
    public void getVODDetailFailed() {
    }

    @Override
    public void onError() {
        mImgBb.setVisibility(View.VISIBLE);
        showDetailDesc();
    }

    VODListCallBack mVODListCallBack = new VODListCallBack() {
        @Override
        public void queryVODListBySubjectSuccess(int total, List<VOD> vodList, String subjectId) {
            if (!isDestory) {
                initRecommend(vodList, RecommendData.RecommendType.HAND_TYPE);
            }
        }

        @Override
        public void queryVODListBySubjectFailed() { }

        @Override
        public void queryVODSubjectListSuccess(int total, List<Subject> subjects) { }

        @Override
        public void queryVODSubjectListFailed() {}

        @Override
        public void querySubjectVODBySubjectIDSuccess(int total, List<SubjectVODList> subjectVODLists) { }

        @Override
        public void querySubjectVODBySubjectIDFailed() { }

        @Override
        public void getContentConfigSuccess(List<ProduceZone> produceZoneList, List<Genre> genreList) { }

        @Override
        public void getContentConfigFailed() { }

        @Override
        public void querySubjectDetailSuccess(int total, List<Subject> subjects) {
            //??????????????????,???IVopRecPresenter???????????????,??????????????????????????????????????????,??????????????????
            if (null != subjects && !subjects.isEmpty()) {
                advertImg.setVisibility(View.VISIBLE);
                Picture picture = subjects.get(0).getPicture();
                if (picture != null) {
                    List<String> posters = picture.getPosters();
                    if (posters != null && posters.size() > 0) {
                        String ImageURL = posters.get(0);
                        SuperLog.info2SD("GlideLoadIPV6", "ImageURL is " + ImageURL);
                        Glide.with(ChildModeVodDetailActivity.this).load(ImageURL).into(advertImg);
                        OTTApplication.getContext().setChildVodDetailAdvertisementURL(ImageURL);
                    }
                }

            }
        }

        @Override
        public void queryPSBRecommendSuccess(int total, List<VOD> vodDetails) {
            //??????????????????,???IVopRecPresenter???????????????,??????????????????????????????????????????,??????????????????
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isDestory) {
                        if (null != vodDetails && vodDetails.size() > 5) {
                            initRecommend(vodDetails, RecommendData.RecommendType.CVI_TYPE);
                        } else {
                            if (null != mVODDetail.getSubjectIDs() && mVODDetail.getSubjectIDs().size() > 0) {
                                String subject = SessionService.getInstance().getSession().getTerminalConfigurationChildSubject(Constant.CHILD_MODE_RECOMMEND_SUBJECT, mVODDetail.getSubjectIDs());
                                mDetailPresenter.loadMoviesContent(subject, "0", "7", mVODListCallBack);
                            }
                        }
                    }
                }
            });
        }

        @Override
        public void queryPSBRecommendFail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null != mVODDetail && null != mVODDetail.getSubjectIDs() && mVODDetail.getSubjectIDs().size() > 0 && !isDestory) {
                        String subject = SessionService.getInstance().getSession().getTerminalConfigurationChildSubject(Constant.CHILD_MODE_RECOMMEND_SUBJECT, mVODDetail.getSubjectIDs());
                        mDetailPresenter.loadMoviesContent(subject, "0", "7", mVODListCallBack);
                    }
                }
            });
        }

        @Override
        public void onError() { }
    };

    /**
     * ????????????????????????
     */
    public void showTimingRestDialog() {
        View comfirmView = LayoutInflater.from(this).inflate(R.layout.window_pip_timing_rest, null);
        epsiodeImg = (ImageView) comfirmView.findViewById(R.id.epsiode_choose);
        ImageView parentSettingImg = (ImageView) comfirmView.findViewById(R.id.parent_choose);
        RelativeLayout epsiodeRel = (RelativeLayout) comfirmView.findViewById(R.id.epsiode_rest);
        RelativeLayout parentSettingRel = (RelativeLayout) comfirmView.findViewById(R.id.parent_setting);
        epsiodeImg.setImageDrawable(getResources().getDrawable(VoddetailUtil.getInstance().isEpsiodeRest() ? R.drawable.choosen_normal : R.drawable.unchoose_normal));
        epsiodeRel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (VoddetailUtil.getInstance().isEpsiodeRest() || VoddetailUtil.getInstance().isEpsiodeRestTemp()) {
                    epsiodeImg.setImageDrawable(getResources().getDrawable(hasFocus ? R.drawable.choosen_light : R.drawable.choosen_normal));
                } else {
                    epsiodeImg.setImageDrawable(getResources().getDrawable(hasFocus ? R.drawable.unchoose_light : R.drawable.unchoose_normal));
                }
            }
        });

        epsiodeRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VoddetailUtil.getInstance().isEpsiodeRest() || VoddetailUtil.getInstance().isEpsiodeRestTemp()) {
                    dialogSingleTime = new ParentSetCenterDialog(ChildModeVodDetailActivity.this, ChildrenConstant.VIEWTYPE.UNLOCK);
                    setParentSetCenterDialogListener();

                    if (isPlayingState()) {
                        mPlayView.pausePlay();
                        stopAllTiming();
                    }
                    dialogSingleTime.setTypeForToActivity(VoddetailUtil.IS_OPEN_EPSIODE_PLAY);
                    dialogSingleTime.show();
                } else {
                    epsiodeImg.setImageDrawable(getResources().getDrawable(R.drawable.choosen_light));
                    SuperLog.error(TAG, "onClick->mCurrentPlayState:" + mCurrentPlayState);
                    if (isPlayingState() && VoddetailUtil.getInstance().getRestTime() <= 0) {
                        VoddetailUtil.getInstance().setEpsiodeRest(true);
                        startRecordPlayTime();
                    } else {
                        VoddetailUtil.getInstance().setEpsiodeRestTemp(true);
                    }
                    mComfirmPopwindow.dismiss();
                }
            }
        });
        parentSettingRel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                parentSettingImg.setImageDrawable(getResources().getDrawable(hasFocus ? R.drawable.unchoose_light : R.drawable.unchoose_normal));

            }
        });
        parentSettingRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSingleTime = new ParentSetCenterDialog(ChildModeVodDetailActivity.this, ChildrenConstant.VIEWTYPE.UNLOCK);
                dialogSingleTime.setTypeForToActivity(ZJVRoute.LauncherElementContentType.VOD_DETAIL_SWITCH_PARENT);
                setParentSetCenterDialogListener();
                if (isPlayingState()) {
                    mPlayView.pausePlay();
                    stopAllTiming();
                }
                dialogSingleTime.show();
                mComfirmPopwindow.dismiss();
            }
        });
        mComfirmPopwindow = new PopupWindow(comfirmView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mComfirmPopwindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mComfirmPopwindow.setBackgroundDrawable(dw);
        if (!mComfirmPopwindow.isShowing()) {
            mComfirmPopwindow.showAtLocation(mBrowseFrameLayout, Gravity.CENTER, 0, 0);

        }

    }

    public boolean isPlayingState() {

        return mCurrentPlayState == IPlayState.PLAY_STATE_HASMEDIA || mCurrentPlayState == IPlayState.PLAY_STATE_BUFFERING;

    }


    /**
     * ????????????dialog????????????
     */
    public void setParentSetCenterDialogListener() {
        if (null != dialogSingleTime) {
            dialogSingleTime.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    ParentSetCenterDialog parentSetCenterDialog = (ParentSetCenterDialog) dialog;
                    if (!parentSetCenterDialog.isOpenParentControl()) {
                        readyPlay();
                    }


                }
            });
        }
    }


    public void showSecondConfirmPopwindow() {
        if (mSecondConfirmPopwindow == null || !mSecondConfirmPopwindow.isShowing()) {
            View comfirmView = LayoutInflater.from(this).inflate(R.layout.window_pip_child_second_confirm, null);
            TextView secondConfirmtv = (TextView) comfirmView.findViewById(R.id.confirm_tv);
            TextView secondBacktv = (TextView) comfirmView.findViewById(R.id.backtv);
            TextView secondContenttv = (TextView) comfirmView.findViewById(R.id.content);
            if (VoddetailUtil.REST_BY_TODAY.equals(SharedPreferenceUtil.getInstance().getlockScreenType())) {
                secondContenttv.setText("????????????????????????????????????????????????");
            } else {
                secondContenttv.setText("???????????????????????????????????????");
            }
            mSecondConfirmPopwindow = new PopupWindow(comfirmView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mSecondConfirmPopwindow.setFocusable(true);
            ColorDrawable dw = new ColorDrawable(0x00000000);
            mSecondConfirmPopwindow.setBackgroundDrawable(dw);
            secondConfirmtv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogSingleTime = new ParentSetCenterDialog(ChildModeVodDetailActivity.this, ChildrenConstant.VIEWTYPE.UNLOCK);
                    String type = VoddetailUtil.REST_BY_SINGLE;
                    if (!VoddetailUtil.getInstance().isCanPlayToday()) {
                        type = VoddetailUtil.REST_BY_TODAY;
                    }
                    if (VoddetailUtil.getInstance().isEpsiodeRest()) {
                        type = VoddetailUtil.REST_BY_EPSIODE;
                    }
                    setParentSetCenterDialogListener();
                    if (isPlayingState()) {
                        mPlayView.pausePlay();
                        stopAllTiming();
                    }
                    dialogSingleTime.setTypeForToActivity(type);
                    dialogSingleTime.show();
                    mSecondConfirmPopwindow.dismiss();
                }
            });
            secondBacktv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSecondConfirmPopwindow.dismiss();
                }
            });
            if (!mSecondConfirmPopwindow.isShowing() && null != completeDialogView) {
                mSecondConfirmPopwindow.showAtLocation(completeDialogView, Gravity.CENTER, 0, 0);
            }
            secondConfirmtv.setFocusable(true);
            secondBacktv.setFocusable(true);
            secondConfirmtv.requestFocus();


        }
    }


    /**
     * ??????????????????
     */
    public void showCompletePopwindow() {
        if (mCompletePopwindow == null || !mCompletePopwindow.isShowing()) {
            if (null != vodEpisodesView && vodEpisodesView.isShowing()) {
                vodEpisodesView.dismiss();
            }

            if (null != controllerview && controllerview.isShowing()){
                controllerview.dismiss();
            }

            mCompletePopwindow = new FullScreenDialog(this);
            completeDialogView = LayoutInflater.from(this).inflate(R.layout.window_pip_complete_today, null);
            mCompletePopwindow.setContentView(completeDialogView);
            TextView todayhinttv = (TextView) completeDialogView.findViewById(R.id.today_completetv);
            TextView singlehinttv = (TextView) completeDialogView.findViewById(R.id.single_completetv);
            singletimetv = (TextView) completeDialogView.findViewById(R.id.single_timetv);
            TextView deblockingtv = (TextView) completeDialogView.findViewById(R.id.deblockingtv);
            TextView backtv = (TextView) completeDialogView.findViewById(R.id.backtv);
            TextView singletv = (TextView) completeDialogView.findViewById(R.id.single_tv);
            TextView todaytv = (TextView) completeDialogView.findViewById(R.id.today_tv);
            if (((!VoddetailUtil.getInstance().isEpsiodeRest()) && (!VoddetailUtil.getInstance().isCanPlaySingle())) || (VoddetailUtil.getInstance().isEpsiodeRest() && (!VoddetailUtil.getInstance().isEspiodeCanPlay()))) {
                todayhinttv.setVisibility(View.GONE);
                singlehinttv.setVisibility(View.VISIBLE);
                singletv.setVisibility(View.VISIBLE);
                todaytv.setVisibility(View.GONE);
                singletimetv.setVisibility(View.VISIBLE);
                restRemainderTime = VoddetailUtil.getInstance().getRestTime();
                singletimetv.setText(PlayUtil.getStringForTime(mFormatBuilder, mFormatter, restRemainderTime));
                mHandler.removeMessages(DECREMENT_REST_TIME);
                mHandler.sendEmptyMessageDelayed(DECREMENT_REST_TIME, 1000);
            }
            if ((!VoddetailUtil.getInstance().isEpsiodeRest()) && (!VoddetailUtil.getInstance().isCanPlayToday())) {
                todayhinttv.setVisibility(View.VISIBLE);
                singlehinttv.setVisibility(View.GONE);
                singletimetv.setVisibility(View.GONE);
                singletv.setVisibility(View.GONE);
                todaytv.setVisibility(View.VISIBLE);
                mHandler.removeMessages(DECREMENT_REST_TIME);
            }
            backtv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCompletePopwindow.dismiss();
                }
            });
            deblockingtv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSecondConfirmPopwindow();

                }
            });


            if (!mCompletePopwindow.isShowing()) {
                mCompletePopwindow.show();
            }
            deblockingtv.setFocusable(true);
            backtv.setFocusable(true);
            deblockingtv.requestFocus();
            mCompletePopwindow.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (!VoddetailUtil.getInstance().isCanPlaySingle() || !VoddetailUtil.getInstance().isCanPlayToday() || (VoddetailUtil.getInstance().isEpsiodeRest() && !VoddetailUtil.getInstance().isEspiodeCanPlay())) {
                        showDetailDesc();
                    }
                }
            });
        }
    }

    /**
     * ???????????????????????????,??????:???
     *
     * @return
     */
    public long getCurrentElapseTime() {
        long DEFAULT_TIME = 300;
        long currentTime = mPlayView.getCurrentPosition() / 1000;
        if (VoddetailUtil.getInstance().getEpsiodePlayTime() != 0) {
            return VoddetailUtil.getInstance().getEpsiodePlayTime() / 1000;
        }
        if (null == mDetailPresenter || null == mVODDetail) {
            return DEFAULT_TIME;
        }
        if (!TextUtils.isEmpty(mDetailPresenter.getElapseTime())) {
            return Long.valueOf(mDetailPresenter.getElapseTime()) - currentTime;
        }

        if (mVODDetail.getVODType().equals("0")) {
            List<VODMediaFile> vodMediaFiles = mVODDetail.getMediaFiles();
            if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
                return Long.valueOf(vodMediaFiles.get(0).getElapseTime()) - currentTime;
            }
        } else {
            List<Episode> episodes = mVODDetail.getEpisodes();
            Episode playEpisode = voddetailEpsiodesUtils.getSelesctedEpisode();
            Bookmark bookmark = mVODDetail.getBookmark();
//            if (bookmark != null && episodes != null && episodes.size() != 0) {
//                Episode playEpisode = null;
//                if (bookmark != null && !TextUtils.isEmpty(bookmark.getSitcomNO())) {
//                    for (Episode episode : episodes) {
//                        if (bookmark.getSitcomNO().equals(episode.getSitcomNO())) {
//                            playEpisode = episode;
//                        }
//                    }
//                } else {
//                    playEpisode = episodes.get(0);
//                }
//
//                if (null != playEpisode && null != playEpisode.getVOD() && null != playEpisode.getVOD().getMediaFiles() && playEpisode.getVOD().getMediaFiles().size() > 0) {
//                    return Long.valueOf(playEpisode.getVOD().getMediaFiles().get(0).getElapseTime()) - currentTime;
//                }
//            }
            if (null != playEpisode && null != playEpisode.getVOD() && null != playEpisode.getVOD().getMediaFiles() && playEpisode.getVOD().getMediaFiles().size() > 0) {
                return Long.valueOf(playEpisode.getVOD().getMediaFiles().get(0).getElapseTime()) - currentTime;
            }
        }
        return DEFAULT_TIME;
    }

    /**
     * ????????????
     */
    public void startRecordPlayTime() {
        if (VoddetailUtil.getInstance().getRestTime() > 0) {
            return;
        }
        if (VoddetailUtil.getInstance().isEpsiodeRest()) {
            VoddetailUtil.getInstance().setEpsiodeAllTime(getCurrentElapseTime());
            VoddetailUtil.getInstance().startEpsiodeRecordTime();
            VoddetailUtil.getInstance().stopSingleRecordTime();
            VoddetailUtil.getInstance().stopAllRecordTime();
        } else {
            VoddetailUtil.getInstance().startSingleRecordTime();
            VoddetailUtil.getInstance().startAllRecordTime();
            VoddetailUtil.getInstance().stopEpsiodeRecordTime();
        }
    }

    /**
     * ??????????????????
     */
    public void stopAllTiming() {
        VoddetailUtil.getInstance().stopEpsiodeRecordTime();
        VoddetailUtil.getInstance().stopAllRecordTime();
        VoddetailUtil.getInstance().stopSingleRecordTime();
    }


    VoddetailUtil.EpisodePollingListener episodePollingListener = new VoddetailUtil.EpisodePollingListener() {
        @Override
        public void handleEpisode(long episodeTime) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (episodeTime > 0 && VoddetailUtil.getInstance().isEpsiodeRest() && VoddetailUtil.getInstance().getRestTime() <= 0) {
                        timingtv.setText(PlayUtil.getStringForTime(mFormatBuilder, mFormatter, episodeTime));
                    } else {
                        timingtv.setText("????????????");
                    }
                }
            });
        }

        @Override
        public void stopEpisode() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if ((!VoddetailUtil.getInstance().isEpsiodeRest()) || VoddetailUtil.getInstance().getRestTime() > 0 || (isCompelete && "0".equals(mVODDetail.getContentType()))) {
                        timingtv.setText("????????????");
                    }
                }
            });

        }
    };

    private void showBackToStartToast() {
        isNeedBackToStart = true;
//        mPlayView.showBackToStartToast(R.string.player_back_to_start);
        if (null != getContinueText()){
            mPlayView.showToast("",getContinueText());
        }
    }

    private void hideBackToStartToast() {
        isNeedBackToStart = false;
//        mPlayView.hideBackToStartToast();
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
        if (mPlayView != null) {
            mPlayView.setSpeed(speedList.get(speed));

            //????????????????????????
            mPlayView.setmBottomRecordText(getBottomText());
            if (!getBottomText().equals("")){
                mPlayView.setDownArrowVisible(true);
            }
            //??????????????????Toast
            showControlView(true);
            mPlayView.showToast("",getSpeedText());

            resumePlay();
        }
    }

    @Subscribe
    public void onEvent(PlayerSkipChangeEvent event) {
        if (mPlayView != null) {
            mPlayView.setSkipStart(SharedPreferenceUtil.getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "", true));
        }
    }

    private void setRecordText(boolean isStart) {

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
                        mPlayView.showToast("?????????????????????",null);
//                        mPlayView.getmRecord().setText(R.string.player_skip_start);
                    } else {
                        mPlayView.showToast("????????????????????????",null);
//                        mPlayView.getmRecord().setText(R.string.player_skip_end);
                    }
                } else {
                    //???????????????????????????
//                    if (showTip) {
//                        if (isStart) {
//                            mPlayView.getmRecord().setText(R.string.player_not_skip_start);
//                        } else {
//                            mPlayView.getmRecord().setText(R.string.player_not_skip_end);
//                        }
//                    } else {
//                        mPlayView.getmRecord().setText("");
//                    }
                }
            } else {
                mPlayView.getmRecord().setText("");
            }
            if (null != playVodBean && !TextUtils.isEmpty(playVodBean.getVodType()) && TextUtils.equals(playVodBean.getVodType(), Content.PROGRAM)) {
                mPlayView.getmSettingRecord().setText("");
            } else {
                //??????????????????????????????????????????
                mPlayView.getmSettingRecord().setText("");
//                if (canSetSpeed || showTip) {
//                    //???????????????????????????????????????????????????????????????????????????????????????
//                    mPlayView.getmSettingRecord().setText(R.string.player_setting_record);
//                } else {
//                    mPlayView.getmSettingRecord().setText("");
//                }
            }
            mPlayView.getmRecord().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.T24_C23_Light_size));
        } else {
            if (null != playVodBean && !TextUtils.isEmpty(playVodBean.getVodType()) && TextUtils.equals(playVodBean.getVodType(), Content.PROGRAM)) {
                mPlayView.getmSettingRecord().setText("");
            } else {
                //??????????????????????????????????????????
                mPlayView.getmSettingRecord().setText("");
//                if (!canSetSpeed) {
//                    //?????????????????????????????????????????????????????????
//                    mPlayView.getmSettingRecord().setText("");
//                } else {
//                    mPlayView.getmSettingRecord().setText(R.string.player_setting_record);
//                }
            }
            if (playVodBean != null) {
                if (!PlayerAttriUtil.isEmpty(playVodBean.getVodName())) {

                    //????????????????????????
                    mPlayView.getmTitleUp().setText(playVodBean.getVodName());
                    if (!TextUtils.isEmpty(sitcomNO)) {
                        mPlayView.getmTitleUp().setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
                    }
                    mPlayView.getmTitleUp().setSelected(true);
                    mPlayView.getmTitleUp().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.margin_32));

//                    mPlayView.getmRecord().setText(playVodBean.getVodName());
//                    if (!TextUtils.isEmpty(sitcomNO)) {
//                        mPlayView.getmRecord().setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//                    }
                } else {
                    SuperLog.debug(TAG, "playVodBean??????");
                }
            }
//            mPlayView.getmRecord().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.T29_C21_Bold_size));
            vodTitletv.setVisibility(View.GONE);
        }
//        mPlayView.getmSettingRecord().setCompoundDrawables(Drawables.getInstance().getDrawable(getResources(), R.drawable.info_96), null, null, null);
    }

    private void scrollToBottom(final ScrollView scroll, final View inner) {

        Handler mHandler = new Handler();

        mHandler.post(new Runnable() {
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }
                int offset = inner.getMeasuredHeight() - scroll.getHeight();
                if (offset < 0) {
                    offset = 0;
                }

                scroll.scrollTo(0, offset);
                scroll.smoothScrollTo(0, offset);
            }
        });
    }

    private SpannableString getContinueText(){

        String string = "???????????????";
        if (null != playVodBean && null != playVodBean.getBookmark() && playVodBean.getBookmark().length() > 0 ){
            int second = Integer.valueOf(playVodBean.getBookmark());
            if (second >= 60 ){
                int min = second / 60;
                int secondLeft = second % 60;
                if (min >= 60){
                    int hour = min / 60;
                    int minLeft = min % 60;
                    string = string + hour + "???" + minLeft + "???" + secondLeft +"?????????";
                }else{
                    string = string + min + "???" + secondLeft + "?????????";
                }
            }else{
                string = string + second + "?????????";
            }

            String endString = "????????????????????????";

            string = string + endString;

            SpannableString spanString = new SpannableString(string);
            ForegroundColorSpan spanColor = new ForegroundColorSpan(this.getResources().getColor(R.color.controller_toast_text_color));
            spanString.setSpan(spanColor,spanString.length() - 8,spanString.length() - 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            return spanString;
        }
        return null;
    }

    //???????????????????????????
    private String getBottomText(){
        if (isLookBack){
            return "";
        }

        String str = "";
        if (null != mVODDetail){

            String vodType = mVODDetail.getVODType();
            if (! (vodType.equals("0") || vodType.equals("2"))) {
                str = str + "??????        ";
            }

            if (canSetSpeed){
                str = str + "????????????   (" + switchSpeed() + "" +")        ";
            }

            if (canSetSkip){
//                boolean skipOpen = SharedPreferenceUtil.getInstance().getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "",true);
//                if (skipOpen){
//                    str = str + "????????????????????????";
//                }else{
//                    str = str + "????????????????????????";
//                }
                str = str + "????????????";
            }

            return str;


        }else{
            return "";

        }
    }

    public void showControlView(boolean isDelayHide){
        boolean showDownArror = false;
        String vodType = mVODDetail.getVODType();
        if (!vodType.equals("0") && !vodType.equals("2")){
            //?????????
            showDownArror = true;
        }
        if (canSetSkip){
            showDownArror = true;
        }
        if (canSetSpeed){
            showDownArror = true;
        }


//        mPlayView.getmRecord().setVisibility(View.VISIBLE);
//        mPlayView.getmRecord().setText(playVodBean.getVodName());
//        mPlayView.setVodTitle(playVodBean.getVodName());
//        if (!TextUtils.isEmpty(sitcomNO)) {
//            mPlayView.getmRecord().setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//            mPlayView.setVodTitle(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
//        }
//        mPlayView.getmRecord().setSelected(true);
//        mPlayView.getmRecord().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.margin_32));

        //????????????????????????
        mPlayView.getmTitleUp().setVisibility(View.VISIBLE);
        mPlayView.getmTitleUp().setText(playVodBean.getVodName());
        if (!TextUtils.isEmpty(sitcomNO)) {
            mPlayView.getmTitleUp().setText(playVodBean.getVodName() + "???" + sitcomNO + (playVodBean.getIsSeries() == 0 ? "???" : "???"));
        }
        mPlayView.getmTitleUp().setSelected(true);
        mPlayView.getmTitleUp().setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.margin_32));


        mPlayView.showControlView(isDelayHide);
        if (headDuration > 0 || tailDuration > 0){
            setSkipNode();
        }
        mPlayView.setmBottomRecordText(getBottomText());
        mPlayView.setDownArrowVisible(showDownArror);
    }

    private int headDuration = 0;
    private int tailDuration = 0;

    //??????????????????????????????
    private void setSkipNode(){
        Log.i(TAG, "setNode setSkipNode: "+headDuration+ " " + tailDuration + " "+ mPlayView.getDuration());
        if (headDuration == 0 && tailDuration == 0){
            return;
        }
        //?????????
        long totalDuration = mPlayView.getDuration();

        if (headDuration != 0 && totalDuration != 0){
            float headPercent = headDuration * 1000 * 1000 /totalDuration;

            mPlayView.setNode(0,headPercent);
        }

        if (tailDuration != 0 && totalDuration != 0){
            float tailPercent = tailDuration * 1000 * 1000 /totalDuration;
            mPlayView.setNode(1,tailPercent);
        }



    }

    private SpannableString getSpeedText(){
        String string = "?????????"+switchSpeed()+"??????";
        SpannableString spanString = new SpannableString(string);
        ForegroundColorSpan spanColor = new ForegroundColorSpan(getResources().getColor(R.color.controller_toast_text_color));
        spanString.setSpan(spanColor,3,spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    public void resumePlay() {
        if (isCanReplayByTrySee()) {
            if (null != mPlayView) {
                mPlayView.resumePlay();
            }
            if (tryToSeeFlag == 1) {
                mHandler.sendEmptyMessageDelayed(UPDATE_DURATION, 1000);
            }
        }
    }


    private AlacarteChoosePopWindow alacarteChoosePopwindow;
//    //??????????????????????????????
//    private void getSimpleVodForAlacarte(){
//        if (null != controllerview && controllerview.isShowing()) {
//            controllerview.dismiss();
//        }
//        epsiodesUtils.getSimpleVod(mVODDetail.getID(), new VoddetailEpsiodePresenter.GetSimpleVodCallback() {
//            @Override
//            public void getSimpleVodSuccess(VODDetail vodDetail) {
//                if (TextUtils.isEmpty(sitcomNO)){
//                    showAlacarteChoosePopWindow(alacarteChooseDismissListener);
//                }else{
//                    epsiodesUtils.refresh(sitcomNO, new BrowseEpsiodesUtils.GetEpisodeCallback() {
//                        @Override
//                        public void getEpisode(List<Episode> episodes, Episode episode) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    showAlacarteChoosePopWindow(alacarteChooseDismissListener);
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void getEpisodeFail() {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void getSimpleVodFail() {
//
//            }
//        });
//    }

    private void showAlacarteChoosePopWindow() {
        if (null != alacarteChoosePopwindow && alacarteChoosePopwindow.isShowing()){
            //??????????????????
            return;
        }
        alacarteChoosePopwindow = new AlacarteChoosePopWindow(ChildModeVodDetailActivity.this,true);
        alacarteChoosePopwindow.setCallback(new AlacarteChoosePopWindow.AddOwnChooseCallback() {
            @Override
            public void addOwnChoose() {
                String contentId = "";
                if (mVODDetail.getVODType().equals("0") || mVODDetail.getVODType().equals("2")){
                    contentId = mVODDetail.getCode();
                }else if (null != voddetailEpsiodesUtils.getSelesctedEpisode()){
                    contentId = voddetailEpsiodesUtils.getSelesctedEpisode().getVOD().getCode();
                }

                OrderConfigUtils.getInstance().getPresenter().addAlacarteChooseContent(contentId, ChildModeVodDetailActivity.this, new OwnChoosePresenter.AddAlacarteChooseContentCallBack() {
                    @Override
                    public void addAlacarteChooseContentSuccsee() {
                        Log.i(TAG, "AlacarteChoose: addSuccess");
                        if (isFinishing()){
                            return;
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                alreadyReAuthorize = false;
                                reAuthorize();
                            }
                        },200);
                    }

                    @Override
                    public void addAlacarteChooseContentFail(String descirbe) {
                        if (isFinishing()){
                            return;
                        }
                        Toast.makeText(ChildModeVodDetailActivity.this, descirbe, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void cancelChoose() {
                alacarteChoosePopwindow.dismiss();
                resumePlay();
//                goToOrder();
            }

            @Override
            public void upgradeChoose() {
                jumpToOrder();
            }

            @Override
            public void backChoose() {
                alacarteChoosePopwindow.dismiss2();
                if (mPlayView.getCurrentPosition() > 1000 && (mPlayView.getCurrentPosition() - 1000) > tryToSeeTime * 1000){
                    mPlayView.seekTo(tryToSeeTime * 1000);
                    mPlayView.pausePlay();
//                    stopAllTiming();
//                    String timeStr = getTrySeeTime(tryToSeeTime);
//                    seehinttv.setText(timeStr + getString(R.string.trysee_finish_hint));
//                    seehinttv.setHighlightColor(getResources().getColor(android.R.color.transparent));
//                    mHandler.removeMessages(HIDE_TRY_SEE_HINT);
//                    mHandler.removeMessages(HIDE_MARKETING_CONTENT);
//                    tryseelayout.setVisibility(View.VISIBLE);
                }else{
                    resumePlay();
                }
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

    private void showOrderChoosePopWindow() {
        tryseelayoutDown.setVisibility(View.GONE);
        tryseelayout.setVisibility(View.GONE);
        if (null != alacarteChoosePopwindow && alacarteChoosePopwindow.isShowing()){
            //??????????????????
            return;
        }
        alacarteChoosePopwindow = new AlacarteChoosePopWindow(ChildModeVodDetailActivity.this,false);
        alacarteChoosePopwindow.setCallback(new AlacarteChoosePopWindow.AddOwnChooseCallback() {
            @Override
            public void addOwnChoose() {

            }

            @Override
            public void cancelChoose() {
                alacarteChoosePopwindow.dismiss();
                resumePlay();
                tryseelayoutDown.setVisibility(View.VISIBLE);
//                goToOrder();
            }

            @Override
            public void upgradeChoose() {
                jumpToOrder();
                tryseelayoutDown.setVisibility(View.VISIBLE);
            }

            @Override
            public void backChoose() {
                alacarteChoosePopwindow.dismiss2();
                if (mPlayView.getCurrentPosition() > 1000 && (mPlayView.getCurrentPosition() - 1000) > tryToSeeTime * 1000){
                    mPlayView.seekTo(tryToSeeTime * 1000);
                    mPlayView.pausePlay();
                }else{
                    resumePlay();
                }
                tryseelayoutDown.setVisibility(View.VISIBLE);
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

    //????????????????????????????????????
    private ShowBuyControl showBuyControl;
    //???????????????????????????????????????
    public boolean alreadyReAuthorize = false;

    //?????????????????????
    public void reAuthorize(){
        Log.i(TAG, "AlacarteChoose: reAuthorize");

        if (alreadyReAuthorize){
            return;
        }

        alreadyReAuthorize = true;
        if (isFinishing()){
            return;
        }

        showBuyControl = new ShowBuyControl(this);
        showBuyControl.setCallBack(new ShowBuyControl.ShowBuyTagCallBack() {
            @Override
            public void showBuyTag(int showBuy) {
                Log.i(TAG, "AlacarteChoose: reAuthorize showBuyTag "+showBuy);
                if (showBuy == 2){
                    if (isFinishing()){
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
                    tryseelayoutDown.setVisibility(View.GONE);
                    resumePlay();

                }
            }
        });


        PlayVODRequest mPlayVodRequest = new PlayVODRequest();
        //??????
        if ("0".equals(isFilm)) {
            Log.i(TAG, "AlacarteChoose: reAuthorize ??????");
            if (!TextUtils.isEmpty(videoId)){
                mPlayVodRequest.setVODID(videoId);
            }
            if (!TextUtils.isEmpty(currentMediaId)){
                mPlayVodRequest.setMediaID(currentMediaId);
            }

            mPlayVodRequest.setURLFormat("1");
            mPlayVodRequest.setIsReturnProduct("1");
            showBuyControl.playVod(mPlayVodRequest);
        } else {
            Log.i(TAG, "AlacarteChoose: reAuthorize ?????????");
            //?????????
            voddetailEpsiodesUtils.getEpisode(sitcomNO, new VoddetailEpsiodesUtils.GetEpisodeCallback() {
                @Override
                public void getEpisode(List<Episode> episodes, Episode episode) {
                    if (isFinishing()){
                        return;
                    }
                    if (null != episode){
                        mPlayVodRequest.setSeriesID(mVODDetail.getID());
                        mPlayVodRequest.setVODID(episode.getVOD().getID());
                        mPlayVodRequest.setMediaID(episode.getVOD().getMediaFiles().get(0).getID());
                        mPlayVodRequest.setURLFormat("1");
                        mPlayVodRequest.setIsReturnProduct("1");
                        Log.i(TAG, "AlacarteChoose: reAuthorize ????????? refresh");
                        showBuyControl.playVod(mPlayVodRequest);
                    }
                }

                @Override
                public void getEpisodeFail() {

                }
            });
        }
    }

    //???????????????????????????
    public void showTrySeeText(){
        //?????????????????????
//        String timeStr = getTrySeeTime(tryToSeeTime);
//        String totalStr = ownPresenter.total;
//        String str = "??????VIP??????????????????"+totalStr+"??????????????????????????????"+timeStr+"?????????????????????????????????";
//        seehinttv.setText(str);
//        seehinttv.setHighlightColor(getResources().getColor(android.R.color
//                .transparent));
//        tryseelayout.setVisibility(View.VISIBLE);
//        mHandler.sendEmptyMessageDelayed(HIDE_TRY_SEE_HINT, 10 * 1000);
//        mHandler.sendEmptyMessageDelayed(HIDE_MARKETING_CONTENT, 10 * 1000);
    }

    public void showTrySeeTextAlacarteComplete(){
        //?????????????????????
//        String timeStr = getTrySeeTime(tryToSeeTime);
//        String totalStr = ownPresenter.total;
//        String str = "??????VIP??????????????????0??????????????????????????????"+timeStr+"?????????????????????????????????";
//        seehinttv.setText(str);
//        seehinttv.setHighlightColor(getResources().getColor(android.R.color
//                .transparent));
//        tryseelayout.setVisibility(View.VISIBLE);
//        mHandler.sendEmptyMessageDelayed(HIDE_TRY_SEE_HINT, 10 * 1000);
//        mHandler.sendEmptyMessageDelayed(HIDE_MARKETING_CONTENT, 10 * 1000);
    }

    private void jumpToOrder(){
        if (null != playVodBean && !TextUtils.isEmpty(playVodBean.getAuthResult())) {
            AuthorizeResult authorizeResult = JsonParse.json2Object(playVodBean.getAuthResult(), AuthorizeResult.class);
            if (null != authorizeResult) {
                List<Product> products = authorizeResult.getPricedProducts();
                if (SessionService.getInstance().getSession().isHotelUser()) {
                    if (products == null || products.size() == 0) {
                        EpgToast.showToast(this, getResources().getString(R.string.notice_no_orderable_product));
                        return;
                    }
                    for (int i = products.size() - 1; i >= 0; i--) {
                        Product mProductInfo = products.get(i);
                        if (!StringUtils.isSubscribeByDay(mProductInfo)) {
                            products.remove(i);
                        }

                    }
                    if (products == null || products.isEmpty()) {
                        EpgToast.showToast(this, getResources().getString(R.string.notice_no_orderable_product));
                        return;
                    }
                    authorizeResult.setPricedProducts(products);

                } else {
                    if (products == null || products.isEmpty()) {
                        EpgToast.showToast(this, getResources().getString(R.string.notice_no_orderable_product));
                        return;
                    }
                }

            } else {
                EpgToast.showToast(this, getResources().getString(R.string.notice_no_orderable_product));
                return;
            }

            String needJumpToH5Order = SessionService.getInstance().getSession().getTerminalConfigurationNeedJumpToH5Order();
            if (null != needJumpToH5Order && needJumpToH5Order.equals("1")) {
                JumpToH5OrderUtils.getInstance().jumpToH5ORderFromOffScreen(authorizeResult.getPricedProducts(), this, null != xmppmessage, xmppmessage, mVODDetail);
                //pbs????????????
                Log.i(TAG, "PbsUaService: "+mVODDetail.getID());
                PbsUaService.report(Play.getPurchaseData(mVODDetail.getID()));
            } else {
                Intent intent = new Intent(this, NewProductOrderActivity.class);
                intent.putExtra(NewProductOrderActivity.AUTHORIZE_RESULT, JsonParse.object2String(authorizeResult));
                intent.putExtra(ZjYdUniAndPhonePayActivty.IS_TRY_SEE_SUBSCRIBE, true);
                intent.putExtra(ZjYdUniAndPhonePayActivty.ISOFFSCREEN_SUBSCRIBE, null != xmppmessage);
                intent.putExtra(ZjYdUniAndPhonePayActivty.VOD_DETAIL, VodUtil.getSimpleVoddetail(mVODDetail, 5 * 60));
                if (null != xmppmessage) {
                    intent.putExtra(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE, xmppmessage);
                }
                startActivity(intent);
            }
        } else {
            EpgToast.showToast(this, getResources().getString(R.string.notice_no_orderable_product));
        }
    }

    private VodDetailPBSConfigUtils vodDetailPBSConfigUtils;

    //??????pbs?????????????????????????????????????????????
    public void configDetail(VOD vod){
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
                        initDetail(vod);
                    }
                });
            }
        });
    }

    //??????5??????????????????????????????
    public void switchTrySeeHint(){
        tryseelayout.setVisibility(View.GONE);

        if (null == alacarteChoosePopwindow || !alacarteChoosePopwindow.isShowing()){
            tryseelayoutDown.setVisibility(View.VISIBLE);
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