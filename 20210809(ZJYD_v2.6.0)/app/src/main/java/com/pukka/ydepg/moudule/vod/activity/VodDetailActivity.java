package com.pukka.ydepg.moudule.vod.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.widget.BrowseFrameLayout;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.iflytek.xiri.Feedback;
import com.iflytek.xiri.scene.ISceneListener;
import com.iflytek.xiri.scene.Scene;
import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.RelativeLayoutExt;
import com.pukka.ydepg.common.extview.VerticalScrollTextView;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.BrotherSeasonVOD;
import com.pukka.ydepg.common.http.v6bean.v6node.CastRole;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.Genre;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.PlayVodBean;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertContent;
import com.pukka.ydepg.common.refresh.RefreshManager;
import com.pukka.ydepg.common.report.ubd.scene.UBDPlay;
import com.pukka.ydepg.common.report.ubd.scene.UBDSwitch;
import com.pukka.ydepg.common.utils.FastBlur;
import com.pukka.ydepg.common.utils.GlideUtil;
import com.pukka.ydepg.common.utils.HeartBeatUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.RxApiManager;
import com.pukka.ydepg.common.utils.ScoreControl;
import com.pukka.ydepg.common.utils.ShowBuyControl;
import com.pukka.ydepg.common.utils.SubscriptControl;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.common.utils.TextUtil;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.customui.MiguQRViewPopWindow;
import com.pukka.ydepg.event.OnDemandBackEvent;
import com.pukka.ydepg.event.PlayUrlEvent;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.activity.TopicActivity;
import com.pukka.ydepg.launcher.ui.fragment.WebActivity;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.mytv.NewMyMovieActivity;
import com.pukka.ydepg.moudule.player.util.RemoteKeyEvent;
import com.pukka.ydepg.moudule.vod.adapter.ChooseEpisodeAdapter;
import com.pukka.ydepg.moudule.vod.adapter.ChooseVarietyEpisodeAdapter;
import com.pukka.ydepg.moudule.vod.adapter.CreditAdapter;
import com.pukka.ydepg.moudule.vod.adapter.DetailsRecommendAdapter;
import com.pukka.ydepg.moudule.vod.adapter.SeriesAdapter;
import com.pukka.ydepg.moudule.vod.adapter.StillAdapter;
import com.pukka.ydepg.moudule.vod.adapter.TotalEpisodeAdapter;
import com.pukka.ydepg.moudule.vod.adapter.TrailersAdapter;
import com.pukka.ydepg.moudule.vod.cache.VoddetailUtil;
import com.pukka.ydepg.moudule.vod.presenter.BookmarkEvent;
import com.pukka.ydepg.moudule.vod.presenter.CollectionEvent;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.vod.presenter.NewScoresEvent;
import com.pukka.ydepg.moudule.vod.utils.ShowCastListUtils;
import com.pukka.ydepg.moudule.vod.utils.VodAdUtils;
import com.pukka.ydepg.moudule.vod.view.VodDetailDataView;

import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ??????????????????
 *
 * @FileName: VodDetailActivity.java
 * @author: ld
 * @date: 2017-12-19
 */

public class VodDetailActivity extends BaseActivity implements VodDetailDataView {

    public static final String TAG = "VodDetailActivity";

    public static final String VOD_ID = "vod_id";

    public static final String OLD_BOOKMARK = "oldBookmark";

    public static final String ORGIN_VOD = "orgin_vod";

    public static final String SUBJECT_ID = "subject_id";

    public static final String RECOMMEND_TYPE = "recommend_type";

    private static final int RequestDetail = 1002;

    private static final int SET_BG = 1001;

    private static final int EPSIODE_REQUESTFOCUS = 1003;

    private static final int REFRESH_EPSIODE_CANCLICK = 1004;

    private int activity_state;

    private final int activity_Resume = 111110;

    private final int activity_pause = 111111;

    private static String topicId;

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
    //    @BindView(R.id.details_descriptions_scroll)
//    ScrollView mDesScrollView;
    //VOD??????
    @BindView(R.id.details_poster)
    ImageView mPosterImg;
    //HD
    @BindView(R.id.detail_poster_right_icon)
    View mRightIcon;
    //HD
    @BindView(R.id.detail_poster_left_icon)
    ImageView mLeftIcon;
    //???????????????
    @BindView(R.id.details_data_source)
    TextView mDataSourceText;
    //4k???????????????
    @BindView(R.id.details_4k_warnning)
    TextView m4KWarnning;
    //????????????
    @BindView(R.id.details_trailers_view)
    RelativeLayout mTrailersLayout;
    //??????GridView
    @BindView(R.id.details_trailers)
    HorizontalGridView mTrailerList;
    //????????????
    @BindView(R.id.details_credits_view)
    RelativeLayout mCreditsLayout;
    //??????GridView
    @BindView(R.id.details_credits)
    HorizontalGridView mCreditList;
    //????????????
    @BindView(R.id.details_still_view)
    RelativeLayout mStillLayout;
    //??????GridView
    @BindView(R.id.details_still)
    HorizontalGridView mStillList;
    //????????????
    @BindView(R.id.details_recommend_view)
    RelativeLayout mRecommendLayout;
    //??????GridView
    @BindView(R.id.details_recommend)
    VerticalGridView mRecommendList;
    //??????????????????
    @BindView(R.id.detail_root)
    BrowseFrameLayout mBrowseFrameLayout;
    //?????????
    @BindView(R.id.details_progress)
    ProgressBar detailsProgress;


    @BindView(R.id.details_buttons)
    LinearLayout details_buttons;


    //????????????????????????
    @BindView(R.id.play_buy_tv)
    TextView mBuyButton;

    @BindView(R.id.play_mark)
    ImageView mPlayMark;

    @BindView(R.id.play_rel)
    RelativeLayout mplayRel;

    @BindView(R.id.buybutton)
    TextView mVipButton;

    @BindView(R.id.vip_mark)
    ImageView mVipMark;

    //??????
    @BindView(R.id.col_rel)
    RelativeLayout col_rel;

    @BindView(R.id.colimg)
    ImageView colimg;

    @BindView(R.id.colbutton)
    TextView mCollectButton;

    //??????
    @BindView(R.id.buy_rel)
    RelativeLayout buy_rel;

    @BindView(R.id.total_episodes)
    HorizontalGridView total_episodes_GridView;

    @BindView(R.id.choose_episodes)
    RecyclerView chooseEpisodeList;

    @BindView(R.id.episodes_lly)
    LinearLayout episodes_lly;

    @BindView(R.id.details_credits_title)
    TextView details_credits_title;


    @BindView(R.id.vipimg)
    ImageView vipimg;

    /**
     * ????????????
     */
    @BindView(R.id.advert)
    ImageView advertImg;
    @BindView(R.id.advert_bg)
    RelativeLayoutExt advertImgBg;


    private List<List<Episode>> totalEpisodes;

    //??????????????????????????????
    private DetailPresenter mDetailPresenter;
    //    //??????????????????
//    private ProgressBarManager mProgressBarManager;
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

//    //??????
//    private TextView mScoreButton;

    private Scene mFocusScene;

    private Feedback mFeedback;

    private VODDetail mVODDetail;

    //????????????????????????????????????????????????
    private boolean isCanClick = true;

    //??????????????????????????????????????????
    private boolean isfirstload = true;

    //??????????????????????????????????????????????????????
    private boolean isfirstloadbookmark = true;


    private ChooseVarietyEpisodeAdapter varietyEpisodeAdapter;

    private ChooseEpisodeAdapter chooseAdapter;


    private boolean isRefreshBookmark;

    private PlayVodBean playVodBean;

    private TotalEpisodeAdapter totalEpisodeAdapter;

    /**
     * ??????????????????????????????
     */
    private boolean isFromSubscribeNotTrySee;


    private String showSubscribedMark;

    private boolean is4KSource = false;

    private String recommendType;

    /**
     * ??????????????????????????????
     */
    private Bookmark oldBookmark;

    private GridLayoutManager chooseEpisodeLayoutManager;


    private static class VodDetailHandler extends Handler {

        private WeakReference<VodDetailActivity> activity;

        public VodDetailHandler(VodDetailActivity activity) {
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
                            if (!TextUtils.isEmpty(vodId) && activity.get().mDetailPresenter != null) {
                                activity.get().mDetailPresenter.getVODDetail(vodId);
                            }
                            activity.get().requestNum++;
                        } catch (Exception ex) {
                            SuperLog.error(TAG, ex);
                        }
                    } else if (msg.what == SET_BG) {
                        if (null != msg.obj) {
                            Bitmap mBitmap = (Bitmap) msg.obj;
                            if (activity.get().mImgBb != null) {
                                activity.get().mImgBb.setImageBitmap(mBitmap);
                            }
                            if (activity.get().mImgShaw.getVisibility() != View.VISIBLE) {
                                activity.get().mImgShaw.setVisibility(View.VISIBLE);
                            }
                        }
                    } else if (msg.what == EPSIODE_REQUESTFOCUS) {
                        if (activity.get().chooseEpisodeList.getAdapter() instanceof ChooseVarietyEpisodeAdapter) {
                            int position = ((ChooseVarietyEpisodeAdapter) activity.get().chooseEpisodeList.getAdapter()).getBookmarkPosition();
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
                        } else if (activity.get().chooseEpisodeList.getAdapter() instanceof ChooseEpisodeAdapter) {
                            int position = ((ChooseEpisodeAdapter) activity.get().chooseEpisodeList.getAdapter()).getBookmarkPosition();
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


                    } else if (msg.what == REFRESH_EPSIODE_CANCLICK) {
                        activity.get().isCanClick = true;
                    }
                }
            } catch (Exception e) {
                SuperLog.error(TAG, e);
            } finally {

            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voddetail_layout);
        mFeedback = new Feedback(this);
        setmUnBinder(ButterKnife.bind(this));
        mHandler = new VodDetailHandler(this);
        mDetailPresenter = new DetailPresenter(this);
        mDetailPresenter.setDetailDataView(this);
        SuperLog.debug(TAG, "onCreate->isClickEpisode:" + mDetailPresenter.isClickEpisode());
//        initProgressBar();
        initView();
        //???????????????????????????????????????vod,?????????????????????
        Serializable obj = null;
        Serializable bookobj = null;
        String subjectId = "";
        try {
            subjectId = getIntent().getStringExtra(SUBJECT_ID);
            obj = getIntent().getSerializableExtra(ORGIN_VOD);
            bookobj = getIntent().getSerializableExtra(OLD_BOOKMARK);
        } catch (Exception e) {
            SuperLog.error(TAG, e);
        }
        if (!TextUtils.isEmpty(subjectId)) {
            mDetailPresenter.setSubjectId(subjectId);
        }
        if (null != obj && obj instanceof VOD) {
            VOD vod = (VOD) obj;
            initDetail(vod);
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
        mDescText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    return true;
                }
                return false;
            }
        });
    }

    @SuppressLint("RestrictedApi")
    public void initView() {
        showSubscribedMark = LauncherService.getInstance().getLauncher().getExtraData().get(Constant.SHOW_SUBSCRIBERD_MARK);
        mStillList.setFocusScrollStrategy(HorizontalGridView.FOCUS_SCROLL_ITEM);
        mRecommendList.setNumColumns(6);
        mBrowseFrameLayout.setOnFocusSearchListener(mOnFocusSearchListener);
        mTitleText.setSelected(true);
        mVideoInfoImg2.setVisibility(View.GONE);
        recommendType = getIntent().getStringExtra(VodDetailActivity.RECOMMEND_TYPE);
        mDescText.setNextFocusDownId(R.id.play_rel);
        mDescText.setFocusable(false);
        mScoreText.setFocusable(true);
        mScoreText.setSelected(true);
        mScoreText.requestFocus();
    }


    //??????vod????????????????????????????????????????????????
    private void setButtonInfo() {
        if (mplayRel != null) {
            mplayRel.setVisibility(View.GONE);
        }

        if (col_rel != null) {
            col_rel.setVisibility(View.GONE);
        }
        if (buy_rel != null) {
            buy_rel.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity_state = activity_Resume;
        SuperLog.debug(TAG, "onResume->requestNum:" + requestNum);
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

        startXiri();
        //??????????????????
        UBDPlay.cachePlaySource(UBDSwitch.getInstance().getFromActivity());

        //????????????????????????????????????,???????????????????????????,??????????????????(??????????????????????????????,??????????????????????????????),??????????????????????????????,?????????????????????vodDetail??????,???????????????
        if (null != mVODDetail) {
            UBDSwitch.getInstance().reportInVodDetailActivity(mVODDetail.getName(), mVODDetail.getID(), mVODDetail.getContentType(), recommendType);
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

    //??????vod???????????????
    @Override
    public void showDetail(final VODDetail vodDetail, String actionId, List<RecmContents> recmContentsList) {
        if (isfirstloadbookmark && null != oldBookmark) {
            vodDetail.setBookmark(oldBookmark);
        }
        mVODDetail = vodDetail;
        if (isFromSubscribeNotTrySee) {
            authenticateVOd(mVODDetail);
            isFromSubscribeNotTrySee = false;
        }
        initDetail(vodDetail);
        initRecommend(recmContentsList);
        if (!TextUtils.isEmpty(recommendType)) {
            mDetailPresenter.setRecommendType(recommendType);//?????????UBD??????
        }

        if (TopicActivity.class.getSimpleName().equalsIgnoreCase(UBDSwitch.getInstance().getFromActivity())){
            topicId = TopicActivity.getTopicId();
        }

        UBDSwitch.getInstance().reportInVodDetailActivity(vodDetail.getName(), vodDetail.getID(), vodDetail.getContentType(), recommendType);
    }

    public static String getTopicId(){
        return topicId;
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
                    mDetailPresenter.setLastPlayUrl(lastPlayUrl);
                    mDetailPresenter.setLastPlayID(lastPlayID);
                    mDetailPresenter.playVOD(detail);
                } else {
                    if (!detail.getIsSubscribed().equals("1")) {
                        EpgToast.showToast(VodDetailActivity.this, "???????????????????????????");
                    } else {
                        EpgToast.showToast(VodDetailActivity.this, "???????????????");
                    }
                }
            } else {
                Episode playEpisode = null;
                if (null != mDetailPresenter.getEpisode()) {
                    playEpisode = mDetailPresenter.getEpisode();
                } else {
                    List<Episode> episodes = detail.getEpisodes();
                    Bookmark bookmark = detail.getBookmark();
                    if (bookmark != null) {
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
                    mDetailPresenter.setLastPlayUrl(lastPlayUrl);
                    mDetailPresenter.setLastPlayID(lastPlayID);
                    mDetailPresenter.playVOD(playEpisode);
                }
            }
        }
    }

    //???????????????
    public void initDetail(VOD vod) {
        SuperLog.debug(TAG, "VOD ID : " + getIntent().getStringExtra(VOD_ID));
        String name = vod.getName();
        if (!TextUtils.isEmpty(name)) {
            mTitleText.setText(name);
        }
        //?????? vod.getAverageScore()
        String score = vod.getAverageScore();
        if (null != vod) {
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
        }
        //??????  vod.getIntroduce()

        final String des = vod.getIntroduce();
        if (!TextUtils.isEmpty(des)) {
            String str1 = TextUtil.toDBC(des);
            mDescText.setText(str1);
            mDescText.setFocusable(true);
        }

        //??????[??????][??????]
        Picture picture = vod.getPicture();
        if (picture != null && !posterIsCreated) {
            String poster = VoddetailUtil.getInstance().getVodBanner(picture);
            if (poster != null) {
                String bgUrl = VoddetailUtil.getInstance().getBackground(picture, poster);
                GlideUtil.load(this, poster, mPosterImg, R.drawable.default_poster238);
                loadBackgroundImg(bgUrl);
            }
        }

        if (vod instanceof VODDetail) {
            mPosterImg.setVisibility(View.VISIBLE);
        }
        String info = "";
        String totalTime;
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
        showLeftIcon(vod);
//        setButtonStaticByVodType(vodType);
        if (vod instanceof VODDetail) {
            VODDetail vodDetail = (VODDetail) vod;
            showSubscript(vodDetail);
            setButtonListener(vodDetail);
            setButtonInfo(vodDetail);
            setVodEpsiodes(vodDetail);
            if (isfirstload) {
                if (VodUtil.canPlay(is4KSource)) {
                    mplayRel.setFocusable(true);
                    mplayRel.requestFocus();
                } else {
                    col_rel.setFocusable(true);
                    col_rel.requestFocus();
                }
                isfirstload = false;
            }
            if (mDetailPresenter.isClickEpisode()) {
                mPosterImg.setFocusable(true);
                mPosterImg.requestFocus();
            }
        } else {
            setButtonInfo();
        }


        mScoreText.setSelected(false);
        mScoreText.setFocusable(false);
        String produceDate = vod.getProduceDate();
        if (!TextUtils.isEmpty(produceDate)) {
            if (produceDate.length() > 4) {
                info = info + (TextUtils.isEmpty(info) ? "" : " | ") + produceDate.substring(0, 4);
            }
        }
        List<Genre> genres = vod.getGenres();
        if (genres != null && genres.size() != 0) {
            for (int i = 0; i < genres.size(); i++) {
                Genre genre = genres.get(i);
                String genreName = genre.getGenreName();
                if (!TextUtils.isEmpty(genreName)) {
                    info = info + (TextUtils.isEmpty(info) ? "" : " | ") + genreName;
                }
            }
        }

        //?????????????????????????????????
        //vod.getMaxSitcomNO();
        String maxNum = vod.getMaxSitcomNO();
        // vod.getVodNum()
        String vodNum = vod.getVodNum();
        String text = "";
        if (!TextUtils.isEmpty(maxNum) && !TextUtils.isEmpty(vodNum)) {

            if (Integer.parseInt(vodNum) == Integer.parseInt(maxNum)) {
                info = info + (TextUtils.isEmpty(info) ? "" : " | ") + " ???" + maxNum + (isShowSerieslayout(vod.getCmsType()) ? "???" : "???");
            } else {
                info = info + (TextUtils.isEmpty(info) ? "" : " | ") + " ????????????" + maxNum + (isShowSerieslayout(vod.getCmsType()) ? "???" : "???");
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
        if (!VodUtil.canPlay(is4KSource)) {
            m4KWarnning.setVisibility(View.VISIBLE);
            //??????????????????
            String m4KWarnningtip = SessionService.getInstance().getSession().getTerminalConfigurationVod4kWarnningTip();
            if (!TextUtils.isEmpty(m4KWarnningtip)) {
                m4KWarnning.setText(m4KWarnningtip);
            } else {
                m4KWarnning.setText(getResources().getString(R.string.details_source_data_dot) + getResources().getString(R.string.details_4k_warnning));
            }
            mplayRel.setFocusable(false);
            mplayRel.setBackgroundResource(android.R.color.transparent);
            buy_rel.setFocusable(false);
            buy_rel.setBackgroundResource(android.R.color.transparent);
            mPlayMark.setVisibility(View.VISIBLE);
            mVipMark.setVisibility(View.VISIBLE);
        } else {
            mPlayMark.setVisibility(View.GONE);
            mVipMark.setVisibility(View.GONE);
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
        setFavoriteStatus(isCollection = vod.getFavorite() != null);
        //???????????????
        initCastRole(vod.getCastRoles(), vod.getSubjectIDs());
        if (picture != null) {
            initStills(picture.getStills());
        }

        buy_rel.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (advertImgBg.isFocusable()) {
                        advertImgBg.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });
        advertImgBg.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    return true;
                }

                if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                    if (total_episodes_GridView.getVisibility() == View.VISIBLE){
                        total_episodes_GridView.requestFocus();
                        return true;
                    }
                }
                return false;
            }
        });

        col_rel.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && (buy_rel.getVisibility() == View.GONE || !buy_rel.isFocusable())) {
                    return true;
                }
                return false;
            }
        });
    }

    private void showLeftIcon(VOD vod) {
        String contentURL = SuperScriptUtil.getInstance().getSuperScriptByVod(vod, false);
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
                        Glide.with(this).load(contentURL).into(mLeftIcon);
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

        String vodType = vodDetail.getVODType();
        if (vodType.equals("0") || vodType.equals("2")) {
            episodes_lly.setVisibility(View.GONE);

        } else {
            episodes_lly.setVisibility(View.VISIBLE);
            if (vodType.equals("3") || isShowSerieslayout(vodDetail.getCmsType())) {
                mDetailPresenter.setIsSeries(0);
                if (isShowReverselayout(vodDetail.getCmsType())) {
                    mDetailPresenter.setIsReverse(1);
                } else {
                    mDetailPresenter.setIsReverse(0);
                }
                showEpisodes(vodDetail);
            } else {
                mDetailPresenter.setIsSeries(1);
                if (isShowReverselayout(vodDetail.getCmsType())) {
                    mDetailPresenter.setIsReverse(1);
                } else {
                    mDetailPresenter.setIsReverse(0);
                }
                showVarietyEpisodes(vodDetail);
            }

        }
        if (total_episodes_GridView.getVisibility() == View.VISIBLE && null != totalEpisodeAdapter) {
            buy_rel.setNextFocusDownId(R.id.total_episodes);
            col_rel.setNextFocusDownId(R.id.total_episodes);
            mplayRel.setNextFocusDownId(R.id.total_episodes);
            if (buy_rel.getVisibility() == View.GONE) {
                col_rel.setNextFocusRightId(R.id.total_episodes);
            }
            if (buy_rel.getVisibility() == View.VISIBLE) {
                if ("??????".equals(mVipButton.getText().toString())) {
                    buy_rel.setNextFocusRightId(R.id.total_episodes);
                } else {
                    col_rel.setNextFocusRightId(R.id.total_episodes);
                }
            }
            totalEpisodeAdapter.setOnkeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (KeyEvent.ACTION_DOWN == event.getAction()) {
                        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            int position = chooseEpisodeList.getChildPosition(v);
                            if (position >= 3) {
                                SuperLog.debug(TAG, "KEYCODE_DPAD_UP->button requestFocus");
                                if (advertImgBg.getVisibility() == View.VISIBLE && advertImgBg.isFocusable()){
                                    Log.i(TAG, "onKey: 1");
                                    advertImgBg.requestFocus();
                                }else if (buy_rel.getVisibility() == View.VISIBLE && "??????".equals(mVipButton.getText().toString())) {
                                    Log.i(TAG, "onKey: 2");
                                    buy_rel.setFocusable(true);
                                    buy_rel.requestFocus();
                                } else {
                                    Log.i(TAG, "onKey: 3");
                                    col_rel.setFocusable(true);
                                    col_rel.requestFocus();
                                }
                                return true;
                            }
                        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            HorizontalGridView parentView = (HorizontalGridView) v.getParent();
                            if (parentView.getChildAt(0) == v) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });
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


    //??????????????????????????????????????????????????????
    public void setButtonListener(VODDetail vodDetail) {
        mplayRel.setTag(vodDetail);
        mplayRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO ???????????????????????????????????????????????????
                try {
                    if (mLastOnClickTime != 0 && System.currentTimeMillis() - mLastOnClickTime < VALID_TIME) {
                        return;
                    }
                    mLastOnClickTime = System.currentTimeMillis();
                    mDetailPresenter.setButtonOrderOrSee(true);
                    VODDetail detail = (VODDetail) v.getTag();
                    if (VodUtil.isMiguVod(vodDetail)) {
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
                        } else {
                            if (!HeartBeatUtil.getInstance().isSubscribedByPrice(detail, null == mVODDetail ? "" : mVODDetail.getPrice())) {
                                EpgToast.showToast(VodDetailActivity.this, "???????????????????????????");
                            } else {
                                EpgToast.showToast(VodDetailActivity.this, "???????????????");
                            }
                            return;
                        }
                    } else {
                        List<Episode> episodes = detail.getEpisodes();
                        Bookmark bookmark = vodDetail.getBookmark();
                        if (bookmark != null) {
                            SuperLog.debug(TAG, "set bookmark--->" + "videoId:" + bookmark.getItemID() + ",sitcomNO:" + bookmark.getSitcomNO() + ",breakpoint:" + bookmark.getRangeTime());
                        }
                        if (episodes != null && episodes.size() != 0) {
                            Episode playEpisode = null;
                            if (bookmark != null && !TextUtils.isEmpty(bookmark.getSitcomNO())) {
                                for (Episode episode : episodes) {
                                    if (bookmark.getSitcomNO().equals(episode.getSitcomNO())) {
                                        playEpisode = episode;
                                    }
                                }
                            } else {
                                playEpisode = episodes.get(0);
                            }
                            if (null != playEpisode) {
                                mDetailPresenter.setLastPlayUrl(lastPlayUrl);
                                mDetailPresenter.setLastPlayID(lastPlayID);
                                mDetailPresenter.playVOD(playEpisode);
                            }
                        } else {
                            EpgToast.showToast(VodDetailActivity.this, "???????????????????????????");
                            return;
                        }
                    }
                } catch (Exception ex) {
                    SuperLog.error(TAG, ex);
                }

            }
        });
        col_rel.setTag(vodDetail);
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
        buy_rel.setTag(vodDetail);
        buy_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        } else {
                            if (!HeartBeatUtil.getInstance().isSubscribedByPrice(detail, "")) {
                                EpgToast.showToast(VodDetailActivity.this, "???????????????????????????");
                            } else {
                                EpgToast.showToast(VodDetailActivity.this, "???????????????");
                            }
                            return;
                        }
                    } else {
                        List<Episode> episodes = detail.getEpisodes();
                        Bookmark bookmark = vodDetail.getBookmark();
                        if (bookmark != null) {
                            SuperLog.debug(TAG, "set bookmark--->" + "videoId:" + bookmark.getItemID() + ",sitcomNO:" + bookmark.getSitcomNO() + ",breakpoint:" + bookmark.getRangeTime());
                        }
                        if (episodes != null && episodes.size() != 0) {
                            Episode playEpisode = getBuyEpsiode(episodes);
                            //?????????????????????
                            if (null == playEpisode) {
                                EpgToast.showToast(VodDetailActivity.this, R.string.notice_no_orderable_product);
                                return;
                            }
                            mDetailPresenter.setLastPlayUrl(lastPlayUrl);
                            mDetailPresenter.setLastPlayID(lastPlayID);
                            mDetailPresenter.playVOD(playEpisode);
                        } else {
                            EpgToast.showToast(VodDetailActivity.this, R.string.notice_no_orderable_product);
                            return;
                        }
                    }
                } catch (Exception ex) {
                    SuperLog.error(TAG, ex);
                }

            }
        });

        initClipfiles(vodDetail.getClipfiles(), vodDetail.getID());
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
            mCollectButton.setText("??????");
            colimg.setImageDrawable(getResources().getDrawable(R.drawable.detail_favorited_icon));
        } else {
            mCollectButton.setText("??????");
            colimg.setImageDrawable(getResources().getDrawable(R.drawable.details_button_favorite_icon));
        }
    }

    //?????????????????????
    @SuppressLint("RestrictedApi")
    private void showSeries(VODDetail detail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.dialog));
        final AlertDialog dialog = builder.create();
        dialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //????????????
        dialog.getWindow().setAttributes(lp);
        dialog.setContentView(R.layout.dialog_detail_series);
        VerticalGridView seriesList = (VerticalGridView) dialog.getWindow().findViewById(R.id.series_list);
        List<BrotherSeasonVOD> brotherSeasonVODList = detail.getBrotherSeasonVODs();
        if (brotherSeasonVODList != null && brotherSeasonVODList.size() != 0) {
            seriesList.setAdapter(new SeriesAdapter(brotherSeasonVODList, dialog));
            for (int i = 0; i < brotherSeasonVODList.size(); i++) {
                BrotherSeasonVOD brotherSeasonVOD = brotherSeasonVODList.get(i);
                VOD vod = brotherSeasonVOD.getVOD();
                if (vod != null && detail.getID().equals(vod.getID())) {
                    seriesList.setSelectedPosition(i);
                }
            }
        }
    }

    //??????????????????
    private void setButtonInfo(VODDetail vodDetail) {
        details_buttons.setVisibility(View.VISIBLE);
        if (mplayRel != null) {
            mplayRel.setVisibility(View.VISIBLE);
            mplayRel.setTag(vodDetail);
            //????????????????????????
            if (!HeartBeatUtil.getInstance().isSubscribedByPrice(vodDetail, null == mVODDetail ? "" : mVODDetail.getPrice()) && vodDetail.getVODType().equals("0")) {
                SuperLog.debug(TAG, "???????????????????????????");
                mBuyButton.setText("??????");

                //?????????????????????
            } else if (!HeartBeatUtil.getInstance().isSubscribedByPrice(vodDetail, null == mVODDetail ? "" : mVODDetail.getPrice()) && !vodDetail.getVODType().equals("0")) {
                //???????????????
                if (null != vodDetail.getBookmark() && !TextUtils.isEmpty(vodDetail.getBookmark().getSubContentID())) {
                    List<Episode> episodes = vodDetail.getEpisodes();
                    Episode selectEpisode = null;
                    if (null != episodes && episodes.size() > 0) {
                        for (int i = 0; i < episodes.size(); i++) {
                            Episode episode = episodes.get(i);
                            if (vodDetail.getBookmark().getSubContentID().equals(episode.getVOD().getID())) {
                                selectEpisode = episode;
                            }
                        }
                    }
                    if (null != selectEpisode && HeartBeatUtil.getInstance().isSubscribedByPrice(selectEpisode.getVOD(), null == mVODDetail ? "" : mVODDetail.getPrice())) {
                        mBuyButton.setText("???" + selectEpisode.getSitcomNO() + (isShowSerieslayout(vodDetail.getCmsType()) ? "???" : "???"));
                    } else {
                        mBuyButton.setText("??????");
                    }
                    //??????????????????
                } else {
                    mBuyButton.setText("??????");
                }
                //??????
            } else {
                SuperLog.debug(TAG, "???????????????????????????");
                mBuyButton.setText("??????");

                /**
                 * ?????????????????????
                 */
                buy_rel.setOnClickListener(null);
                Bookmark bookmark = vodDetail.getBookmark();
                if (bookmark != null && !TextUtils.isEmpty(bookmark.getSitcomNO())) {
                    mBuyButton.setText("???" + bookmark.getSitcomNO() + (isShowSerieslayout(vodDetail.getCmsType()) ? "???" : "???"));
                }
            }
        }
        if (null != buy_rel) {
            buy_rel.setFocusable(false);

            ShowBuyControl control = new ShowBuyControl(this);

            control.setCallBack(new ShowBuyControl.ShowBuyTagCallBack() {
                @Override
                public void showBuyTag(int showBuy) {
                    switch (showBuy) {
                        case 0:
                            buy_rel.setVisibility(View.VISIBLE);
                            buy_rel.setFocusable(true);
                            vipimg.setImageDrawable(getResources().getDrawable(R.drawable.details_button_vip));
                            mVipButton.setText("??????");
                            col_rel.setNextFocusRightId(R.id.buy_rel);
                            break;
                        case 1:
                            buy_rel.setFocusable(false);
                            buy_rel.setVisibility(View.GONE);
                            break;
                        case 2:
                            buy_rel.setVisibility((TextUtils.isEmpty(showSubscribedMark) || "1".equals(showSubscribedMark)) ? View.VISIBLE : View.GONE);
                            mVipButton.setText("?????????");
                            vipimg.setImageDrawable(getResources().getDrawable(R.drawable.detail_button_orderedvip));
                            buy_rel.setFocusable(false);
                            buy_rel.setOnClickListener(null);
                            break;
                    }

                    //??????????????????
                    if (showBuy == 0) {
                        VodAdUtils utils = new VodAdUtils();
                        utils.getVodAd(VodDetailActivity.this,mDetailPresenter, mVODDetail, new VodAdUtils.VodAdCallBack() {
                            @Override
                            public void showAd(int state, String url, AdvertContent advertContent) {
                                switch (state) {
                                    case VodAdUtils.AD_STATE_NOT_SHOW: {
                                        //??????????????????
                                        advertImg.setVisibility(View.GONE);
                                        advertImgBg.setVisibility(View.GONE);
                                        break;
                                    }
                                    case VodAdUtils.AD_STATE_CANT_CLICK: {
                                        //?????????????????????????????????
                                        advertImgBg.setVisibility(View.VISIBLE);
                                        advertImg.setVisibility(View.VISIBLE);
                                        advertImgBg.setFocusable(false);
                                        SuperLog.info2SD("GlideLoadIPV6", "advertisementUrl is " + url);
                                        Glide.with(VodDetailActivity.this).load(url).into(advertImg);
                                        break;
                                    }
                                    case VodAdUtils.AD_STATE_CAN_CLICK: {
                                        //?????????????????????????????????
                                        advertImgBg.setVisibility(View.VISIBLE);
                                        advertImg.setVisibility(View.VISIBLE);
                                        advertImgBg.setFocusable(true);
                                        SuperLog.info2SD("GlideLoadIPV6", "advertisementUrl is " + url);
                                        Glide.with(VodDetailActivity.this).load(url).into(advertImg);
                                        advertImgBg.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(VodDetailActivity.this, WebActivity.class);
                                                String clickUrl = advertContent.getDisplay().getBanner().getLink().getUrl();
                                                intent.putExtra("url", clickUrl);
                                                startActivity(intent);
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
                    } else {
                        advertImg.setVisibility(View.GONE);
                    }
                }
            });

            control.ShowBuyTag(vodDetail);

//            int type=HeartBeatUtil.getInstance().isShowBuyTag(vodDetail);
        }

        if (col_rel != null) {
            col_rel.setVisibility(View.VISIBLE);
            col_rel.setTag(vodDetail);
            mCollectButton.setText("??????");
            colimg.setImageDrawable(getResources().getDrawable(R.drawable.details_button_favorite_icon));

        }

    }

    @SuppressLint("RestrictedApi")
    public void showEpisodes(VODDetail detail) {
        List<Episode> episodes = detail.getEpisodes();
        int bookNum = 0;
        Episode selectEpisode = null;
        if (episodes != null && episodes.size() != 0) {
            if (episodes.size() > 1 && Integer.parseInt(episodes.get(0).getSitcomNO()) < Integer.parseInt(episodes.get(1).getSitcomNO()) && isShowReverselayout(detail.getCmsType())) {
                Collections.reverse(episodes);
            }
            Bookmark bookmark = detail.getBookmark();
            if (bookmark != null) {
                for (int i = 0; i < episodes.size(); i++) {
                    Episode episode = episodes.get(i);
                    if (bookmark.getSubContentID().equals(episode.getVOD().getID())) {
                        bookNum = i;
                        selectEpisode = episode;
                    }
                }
            }
            if (chooseEpisodeLayoutManager == null) {
                chooseEpisodeLayoutManager = new GridLayoutManager(this, 10, GridLayoutManager.VERTICAL, false);
                chooseEpisodeList.setLayoutManager(chooseEpisodeLayoutManager);
            }
            if (episodes.size() <= 20) {

                ViewGroup.LayoutParams layoutParams = chooseEpisodeList.getLayoutParams();
                if (episodes.size() > 10) {
                    layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_100dp);
                } else {
                    layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_55dp);
                }
                chooseEpisodeList.setLayoutParams(layoutParams);

                total_episodes_GridView.setVisibility(View.GONE);
                if (chooseAdapter == null) {
                    chooseAdapter = new ChooseEpisodeAdapter(episodes, mDetailPresenter);
                    chooseAdapter.setSelectEpisode(selectEpisode);
                    chooseEpisodeList.setAdapter(chooseAdapter);
                    chooseAdapter.setIs4KSource(is4KSource);
                    chooseAdapter.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (KeyEvent.ACTION_DOWN == event.getAction()) {
                                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                                    int position = chooseEpisodeList.getChildPosition(v);
                                    if (position > 6 && position < 10) {
                                        if (buy_rel.getVisibility() == View.VISIBLE && "??????".equals(mVipButton.getText().toString())) {
                                            buy_rel.setFocusable(true);
                                            buy_rel.requestFocus();
                                        } else {
                                            col_rel.setFocusable(true);
                                            col_rel.requestFocus();
                                        }
                                        return true;
                                    }
                                } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                                    int position = chooseEpisodeList.getChildPosition(v);
                                    int line = (position / 10) + 1;
                                    if (position == (line - 1) * 10) {
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
                if (chooseAdapter == null) {
                    totalEpisodes = new ArrayList<>();
                    List<Episode> episodeList = null;
                    for (int i = 0; i < episodes.size(); i++) {
                        if (i % 20 == 0) {
                            if (episodeList != null) {
                                totalEpisodes.add(episodeList);
                            }
                            episodeList = new ArrayList<>();
                        }
                        if (null != episodeList) {
                            episodeList.add(episodes.get(i));
                            if (i == (episodes.size() - 1)) {
                                totalEpisodes.add(episodeList);
                            }
                        }
                    }
                    chooseAdapter = new ChooseEpisodeAdapter(new ArrayList<Episode>(), mDetailPresenter);
                    chooseAdapter.setSelectEpisode(selectEpisode);
                    chooseEpisodeList.setAdapter(chooseAdapter);
                    chooseAdapter.setIs4KSource(is4KSource);

                    totalEpisodeAdapter = new TotalEpisodeAdapter(totalEpisodes, chooseAdapter, 20, false, chooseEpisodeList);
                    total_episodes_GridView.setAdapter(totalEpisodeAdapter);
                    int totalPosition = (int) (bookNum / 20f);
                    if (totalEpisodes.size() > totalPosition) {

                        ViewGroup.LayoutParams layoutParams = chooseEpisodeList.getLayoutParams();
                        if (totalEpisodes.get(totalPosition).size() > 10) {
                            layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_100dp);
                        } else {
                            layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_55dp);
                        }
                        chooseEpisodeList.setLayoutParams(layoutParams);

                        chooseAdapter.setDataSource(totalEpisodes.get(totalPosition));
                    } else {
                        if (totalEpisodes.size() > 0) {

                            ViewGroup.LayoutParams layoutParams = chooseEpisodeList.getLayoutParams();
                            if (totalEpisodes.get(0).size() > 10) {
                                layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_100dp);
                            } else {
                                layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_55dp);
                            }
                            chooseEpisodeList.setLayoutParams(layoutParams);

                            chooseAdapter.setDataSource(totalEpisodes.get(0));
                        }
                    }
                    chooseAdapter.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (KeyEvent.ACTION_DOWN == event.getAction()) {
                                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                                    int position = chooseEpisodeList.getChildPosition(v);
                                    if (position >= 0 && position < 10) {
                                        SuperLog.debug(TAG, "KEYCODE_DPAD_UP->total_episodes_GridView requestFocus");

                                        total_episodes_GridView.requestFocus();
                                        return true;
                                    }
                                } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                                    int position = chooseEpisodeList.getChildPosition(v);
                                    int line = (position / 10) + 1;
                                    if (position == (line - 1) * 10) {
                                        return true;
                                    }

                                }
                            }
                            return false;
                        }
                    });
                    total_episodes_GridView.setSelectedPosition(totalPosition);
                } else {
                    int totalPosition = (int) (bookNum / 20f);
                    if (totalEpisodes.size() > totalPosition) {

                        ViewGroup.LayoutParams layoutParams = chooseEpisodeList.getLayoutParams();
                        if (totalEpisodes.get(totalPosition).size() > 10) {
                            layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_100dp);
                        } else {
                            layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_55dp);
                        }
                        chooseEpisodeList.setLayoutParams(layoutParams);

                        chooseAdapter.setDataSource(totalEpisodes.get(totalPosition));
                    } else {
                        if (totalEpisodes.size() > 0) {

                            ViewGroup.LayoutParams layoutParams = chooseEpisodeList.getLayoutParams();
                            if (totalEpisodes.get(0).size() > 10) {
                                layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_100dp);
                            } else {
                                layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_55dp);
                            }
                            chooseEpisodeList.setLayoutParams(layoutParams);

                            chooseAdapter.setDataSource(totalEpisodes.get(0));
                        }
                    }
                    total_episodes_GridView.setSelectedPosition(totalPosition);
                    totalEpisodeAdapter.setSelectPosition(totalPosition);
                    chooseAdapter.setSelectEpisode(selectEpisode);
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    public void showVarietyEpisodes(VODDetail detail) {
        List<Episode> episodes = detail.getEpisodes();
        int bookNum = 0;
        Episode selectEpisode = null;
        if (episodes != null && episodes.size() != 0) {
            if (episodes.size() > 1 && Integer.parseInt(episodes.get(0).getSitcomNO()) < Integer.parseInt(episodes.get(1).getSitcomNO()) && isShowReverselayout(detail.getCmsType())) {
                Collections.reverse(episodes);
            }
            Bookmark bookmark = detail.getBookmark();
            if (bookmark != null) {
                for (int i = 0; i < episodes.size(); i++) {
                    Episode episode = episodes.get(i);
                    if (bookmark.getSubContentID().equals(episode.getVOD().getID())) {
                        bookNum = i;
                        selectEpisode = episode;
                    }
                }
            }
            if (chooseEpisodeLayoutManager == null) {
                chooseEpisodeLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
                chooseEpisodeList.setLayoutManager(chooseEpisodeLayoutManager);
            }

            if (episodes.size() <= 9) {
                total_episodes_GridView.setVisibility(View.GONE);

                ViewGroup.LayoutParams pm = chooseEpisodeList.getLayoutParams();
                if (episodes.size() <= 3) {
                    pm.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_55dp);
                } else if (6 >= episodes.size() && episodes.size() > 3) {
                    pm.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_120dp);
                } else {
                    pm.height = getResources().getDimensionPixelOffset(R.dimen.details_varietys_view_height);
                }
                chooseEpisodeList.setLayoutParams(pm);

                if (varietyEpisodeAdapter == null) {
                    varietyEpisodeAdapter = new ChooseVarietyEpisodeAdapter(episodes, mDetailPresenter);
                    varietyEpisodeAdapter.setSelectEpisode(selectEpisode);
                    chooseEpisodeList.setAdapter(varietyEpisodeAdapter);
                    varietyEpisodeAdapter.setIs4KSource(is4KSource);
                    varietyEpisodeAdapter.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (KeyEvent.ACTION_DOWN == event.getAction()) {
                                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                                    int position = chooseEpisodeList.getChildPosition(v);
                                    if (position == 2) {
                                        if (buy_rel.getVisibility() == View.VISIBLE && "??????".equals(mVipButton.getText().toString())) {
                                            buy_rel.setFocusable(true);
                                            buy_rel.requestFocus();
                                        } else {
                                            col_rel.setFocusable(true);
                                            col_rel.requestFocus();
                                        }
                                        return true;
                                    }
                                } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                                    int position = chooseEpisodeList.getChildPosition(v);
                                    int line = (position / 3) + 1;
                                    if (position == (line - 1) * 3) {
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
                if (varietyEpisodeAdapter == null) {
                    totalEpisodes = new ArrayList<>();
                    List<Episode> episodeList = null;
                    for (int i = 0; i < episodes.size(); i++) {
                        if (i % 9 == 0) {
                            if (episodeList != null) {
                                totalEpisodes.add(episodeList);
                            }
                            episodeList = new ArrayList<>();
                        }
                        if (null != episodeList) {
                            episodeList.add(episodes.get(i));
                            if (i == (episodes.size() - 1)) {
                                totalEpisodes.add(episodeList);
                            }
                        }
                    }

                    varietyEpisodeAdapter = new ChooseVarietyEpisodeAdapter(new ArrayList<Episode>(), mDetailPresenter);
                    varietyEpisodeAdapter.setSelectEpisode(selectEpisode);
                    chooseEpisodeList.setAdapter(varietyEpisodeAdapter);
                    varietyEpisodeAdapter.setIs4KSource(is4KSource);
                    totalEpisodeAdapter = new TotalEpisodeAdapter(totalEpisodes, varietyEpisodeAdapter, 9, false, chooseEpisodeList);
                    total_episodes_GridView.setAdapter(totalEpisodeAdapter);
                    //???????????????
                    totalEpisodeAdapter.setTotal(episodes.size());
                    int totalPosition = (int) (bookNum / 9f);
                    if (totalEpisodes.size() > totalPosition) {

                        ViewGroup.LayoutParams pm = chooseEpisodeList.getLayoutParams();
                        if (totalEpisodes.get(totalPosition).size() <= 3) {
                            pm.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_55dp);
                        } else if (6 >= totalEpisodes.get(totalPosition).size() && totalEpisodes.get(totalPosition).size() > 3) {
                            pm.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_120dp);
                        } else {
                            pm.height = getResources().getDimensionPixelOffset(R.dimen.details_varietys_view_height);
                        }
                        chooseEpisodeList.setLayoutParams(pm);

                        varietyEpisodeAdapter.setDataSource(totalEpisodes.get(totalPosition));
                    } else {
                        if (totalEpisodes.size() > 0) {

                            ViewGroup.LayoutParams pm = chooseEpisodeList.getLayoutParams();
                            if (totalEpisodes.get(0).size() <= 3) {
                                pm.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_55dp);
                            } else if (6 >= totalEpisodes.get(0).size() && totalEpisodes.get(0).size() > 3) {
                                pm.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_120dp);
                            } else {
                                pm.height = getResources().getDimensionPixelOffset(R.dimen.details_varietys_view_height);
                            }
                            chooseEpisodeList.setLayoutParams(pm);

                            varietyEpisodeAdapter.setDataSource(totalEpisodes.get(0));
                        }
                    }
                    varietyEpisodeAdapter.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (KeyEvent.ACTION_DOWN == event.getAction()) {
                                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                                    int position = chooseEpisodeList.getChildPosition(v);
                                    if (position >= 0 && position < 3) {
                                        SuperLog.debug(TAG, "KEYCODE_DPAD_UP->total_episodes_GridView requestFocus");

                                        total_episodes_GridView.requestFocus();
                                        return true;
                                    }
                                } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                                    int position = chooseEpisodeList.getChildPosition(v);
                                    int line = (position / 3) + 1;
                                    if (position == (line - 1) * 3) {
                                        return true;
                                    }

                                }
                            }
                            return false;
                        }
                    });
                    total_episodes_GridView.setSelectedPosition(totalPosition);
                } else {
                    int totalPosition = (int) (bookNum / 9f);
                    if (totalEpisodes.size() > totalPosition) {

                        ViewGroup.LayoutParams pm = chooseEpisodeList.getLayoutParams();
                        if (totalEpisodes.get(totalPosition).size() <= 3) {
                            pm.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_55dp);
                        } else if (6 >= totalEpisodes.get(totalPosition).size() && totalEpisodes.get(totalPosition).size() > 3) {
                            pm.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_120dp);
                        } else {
                            pm.height = getResources().getDimensionPixelOffset(R.dimen.details_varietys_view_height);
                        }
                        chooseEpisodeList.setLayoutParams(pm);

                        varietyEpisodeAdapter.setDataSource(totalEpisodes.get(totalPosition));
                    } else {
                        if (totalEpisodes.size() > 0) {

                            ViewGroup.LayoutParams pm = chooseEpisodeList.getLayoutParams();
                            if (totalEpisodes.get(0).size() <= 3) {
                                pm.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_55dp);
                            } else if (6 >= totalEpisodes.get(0).size() && totalEpisodes.get(0).size() > 3) {
                                pm.height = getResources().getDimensionPixelOffset(R.dimen.featured_detail_120dp);
                            } else {
                                pm.height = getResources().getDimensionPixelOffset(R.dimen.details_varietys_view_height);
                            }
                            chooseEpisodeList.setLayoutParams(pm);

                            varietyEpisodeAdapter.setDataSource(totalEpisodes.get(0));
                        }
                    }
                    total_episodes_GridView.setSelectedPosition(totalPosition);
                    totalEpisodeAdapter.setSelectPosition(totalPosition);
                    varietyEpisodeAdapter.setSelectEpisode(selectEpisode);
                }
            }
        }
    }


    //????????????
    private void showScoreView(final VODDetail detail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.dialog));
        final AlertDialog dialog = builder.create();
        dialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //????????????
        dialog.getWindow().setAttributes(lp);
        dialog.setContentView(R.layout.dialog_detail_score);

        final TextView scoreText = (TextView) dialog.getWindow().findViewById(R.id.score_text);
        final RatingBar scoreRatingBar = (RatingBar) dialog.getWindow().findViewById(R.id.score_rating_bar);
        scoreRatingBar.setMax(10);

        String averageScore = detail.getAverageScore();
        if (averageScore.equals("0.0")) {
            averageScore = "7.0";
        }
        final float intScore = Float.parseFloat(averageScore);
        scoreText.setText(averageScore);
        scoreRatingBar.setRating(intScore / 2);
        final TextView commntButton = (TextView) dialog.getWindow().findViewById(R.id.score_commit_button);
        scoreRatingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    float score = scoreRatingBar.getRating() * 2;
                    if ((intScore == score) || (score == 0.0)) {
                        return;
                    }
                    mDetailPresenter.commitScore(score, detail);
                    dialog.dismiss();
                } catch (Exception ex) {
                    SuperLog.error(TAG, ex);
                }
            }
        });
        scoreRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (intScore == (rating * 2) || rating == 0.0) {
                    commntButton.setTextColor(getResources().getColor(R.color.c24_color));
                } else {
                    commntButton.setTextColor(getResources().getColor(R.color.c21_color));
                }
                scoreText.setText((rating * 2) + "");
            }
        });
    }

    //????????????????????????????????????
    private void initRecommend(List<RecmContents> recmContentsList) {
        if (recmContentsList != null && recmContentsList.size() != 0) {
            mRecommendLayout.setVisibility(View.VISIBLE);
            for (RecmContents recmContents : recmContentsList) {
                if (recmContents.getRecmContentType().equals("VOD")) {
                    List<VOD> vods = recmContents.getRecmVODs();
                    if (vods != null && vods.size() != 0) {
                        mRecommendList.setAdapter(new DetailsRecommendAdapter(vods, null));
                    }
                }
            }
        } else {
            mRecommendLayout.setVisibility(View.GONE);
        }
    }

    //????????????
    private void initStills(List<String> stillList) {
        if (stillList != null && stillList.size() != 0) {
            mStillLayout.setVisibility(View.VISIBLE);
            mStillList.setAdapter(new StillAdapter((ArrayList<String>) stillList));
        } else {
            mStillLayout.setVisibility(View.GONE);
        }
    }

    //?????????????????????
    private void initCastRole(List<CastRole> castRoleList, List<String> subjectIds) {
        if (castRoleList != null && castRoleList.size() != 0) {
            if (null != mVODDetail && ("0".equals(mVODDetail.getVODType()) || "2".equals(mVODDetail.getVODType()))) {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.topMargin = getResources().getDimensionPixelOffset(R.dimen.margin_30);
                lp.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.margin_20);
                lp.leftMargin = getResources().getDimensionPixelOffset(R.dimen.details_view_margin);
                details_credits_title.setLayoutParams(lp);
            }
            boolean isShow = ShowCastListUtils.showCastList(mVODDetail);
            if (isShow) {
                mCreditsLayout.setVisibility(View.VISIBLE);
                mCreditList.setAdapter(new CreditAdapter(castRoleList, subjectIds));
            } else {
                mCreditsLayout.setVisibility(View.GONE);
            }
        } else {
            mCreditsLayout.setVisibility(View.GONE);
        }
    }

    //???????????????
    private void initClipfiles(List<VODMediaFile> clipFileList, String vodID) {
        if (clipFileList != null && clipFileList.size() != 0) {
            mTrailersLayout.setVisibility(View.VISIBLE);
            mTrailerList.setAdapter(new TrailersAdapter(clipFileList, mDetailPresenter, vodID));
        } else {
            mTrailersLayout.setVisibility(View.GONE);
        }
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
        EpgToast.showToast(VodDetailActivity.this, "???????????????????????????");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        SuperLog.debug("onKeyDown", "onKeyDown");
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
        return super.onKeyDown(keyCode, event);
    }

    //??????????????????????????????
    @Subscribe
    public void onEvent(PlayUrlEvent event) {
        SuperLog.debug(TAG, "get PlayUrlEvent");
        if (null != event && (event.isVODSubscribe() || event.isTrySeeSubscribe())) {
            //???????????????????????????
//            Product mProduct=new Product();
//            mProduct.setID(event.getProductId());
//            //????????????????????????
//            UBDTool.getInstance().recordUBDPurchase(mProduct,mVODDetail==null?"":mVODDetail.getID(), PurchaseData.SUCCESS);
            if (!mDetailPresenter.isClickEpisode()) {
                isfirstload = true;
            }
            mHandler.sendEmptyMessage(RequestDetail);
            if (event.isVODSubscribe()) {
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
        SuperLog.debug(TAG, " event receiver Bookmark refresh");
        if (!TextUtils.isEmpty(bookmarkEvent.getPlayVodBean())) {
            PlayVodBean bean = JsonParse.json2Object(bookmarkEvent.getPlayVodBean(), PlayVodBean.class);
            if (null != bean && !TextUtils.isEmpty(bean.getVodId()) && null != mVODDetail && bean.getVodId().equals(mVODDetail.getID())) {
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

    public void refreshBookmark(PlayVodBean bean) {
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

        setButtonInfo(vodDetail);
        setFavoriteStatus(isCollection);
        //TODO
        if (mDetailPresenter.isClickEpisode()) {
//            mPosterImg.setFocusable(true);
//            mPosterImg.requestFocus();
            if (chooseAdapter != null) {
                chooseAdapter.setNeedRequestFocus(true);
            }
            if (varietyEpisodeAdapter != null) {
                varietyEpisodeAdapter.setNeedRequestFocus(true);
            }
            setVodEpsiodes(vodDetail);
//            mHandler.sendEmptyMessageDelayed(EPSIODE_REQUESTFOCUS, 20);
//            mDetailPresenter.setClickEpisode(false);
            SuperLog.debug(TAG, "refreshBookmark->isClickEpisode:" + mDetailPresenter.isClickEpisode());
        } else {
            setVodEpsiodes(vodDetail);
        }
        //???????????????????????????????????????????????????ANR
        if (null != mVODDetail && null != mVODDetail.getEpisodes() && mVODDetail.getEpisodes().size() > 0) {
            mHandler.sendEmptyMessageDelayed(REFRESH_EPSIODE_CANCLICK, 21);
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
            Glide.with(VodDetailActivity.this).load(bgUrl).override(mPosterImg.getLayoutParams().width, mPosterImg.getLayoutParams().height).into(new CustomTarget<Drawable>(mPosterImg.getLayoutParams().width, mPosterImg.getLayoutParams().height) {
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

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        activity_state = activity_pause;
        mDescText.setSelected(false);
        stopXiri();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SuperLog.debug(TAG, "onDestroy");
        RxApiManager.get().cancelAll();
        mVODDetail = null;
        mFocusScene = null;
        mHandler.removeCallbacksAndMessages(null);
        topicId = null;
    }

    private void startXiri() {
        mFocusScene = new Scene(this);
        mFocusScene.init(new ISceneListener() {
            @Override
            public String onQuery() {
                return "{\"_scene\":\"com.pukka.ydepg.DETAIL\",\"_commands\":{\"key1\":[\"$W(collect)\"],\"key2\":[\"$W(cancel_collect)\"],\"key3\":[\"$W(play)\"],\"key4\":[\"$W(history)\"],\"key5\":[\"$P(_EPISODE)\"]},\"_fuzzy_words\":{\"collect\":[\"??????\"],\"cancel_collect\":[\"????????????\"],\"play\":[\"??????\"],\"history\":[\"????????????\", \"????????????\", \"????????????\"]}}";
            }

            @Override
            public void onExecute(Intent intent) {
                RefreshManager.getInstance().getScreenPresenter().exit();
                mFeedback.begin(intent);
                if (intent.hasExtra("_scene") && intent.getStringExtra("_scene").equals("com.pukka.ydepg.DETAIL") && null != mDetailPresenter) {
                    if (intent.hasExtra("_command")) {
                        String command = intent.getStringExtra("_command");
                        SuperLog.info2SD(TAG, "_command" + command);
                        if ("key1".equals(command)) {
                            mFeedback.feedback("??????", Feedback.EXECUTION);
                            if (isCollection) {//?????????????????????
                                EpgToast.showToast(VodDetailActivity.this, "????????????????????????");
                                return;
                            }
                            mDetailPresenter.setCollection(mVODDetail, isCollection);
                        } else if ("key2".equals(command)) {
                            mFeedback.feedback("????????????", Feedback.EXECUTION);
                            if (!isCollection) {
                                EpgToast.showToast(VodDetailActivity.this, "???????????????????????????");
                                return;
                            }
                            mDetailPresenter.setCollection(mVODDetail, isCollection);
                        } else if ("key3".equals(command)) {
//                           SuperLog.info2SD("countcount","index ="+intent.getExtras().getInt("index"));
                            voicePlayLastEpisode("??????", false);
//                            mFeedback.feedback("??????", Feedback.EXECUTION);
//                            if (mVODDetail == null) {
//                                return;
//                            }
//
//                            if (!VodUtil.canPlay(is4KSource)) {
//                                EpgToast.showToast(VodDetailActivity.this, VodDetailActivity.this.getResources().getString(R.string.details_4k_warnning));
//                                return;
//                            }
//                            String type = mVODDetail.getVODType();
//                            mDetailPresenter.setButtonOrderOrSee(true);
//                            if (!TextUtils.isEmpty(type)) {
//                                if (type.equals("0")) {//????????????
//                                    List<VODMediaFile> vodMediaFiles = mVODDetail.getMediaFiles();
//                                    if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
//                                        mDetailPresenter.setXiri(true);
//                                        mDetailPresenter.setLastPlayUrl(lastPlayUrl);
//                                        mDetailPresenter.setLastPlayID(lastPlayID);
//                                        mDetailPresenter.playVOD(mVODDetail);
//                                    } else {
//                                        if (!HeartBeatUtil.getInstance().isSubscribedByPrice(mVODDetail, "")) {
//                                            EpgToast.showToast(VodDetailActivity.this, "???????????????????????????");
//                                        } else {
//                                            EpgToast.showToast(VodDetailActivity.this, "???????????????");
//                                        }
//                                        return;
//                                    }
//                                } else {//???????????????
//                                    List<Episode> episodes = mVODDetail.getEpisodes();
//                                    Bookmark bookmark = mVODDetail.getBookmark();
//                                    if (bookmark != null) {
//                                        SuperLog.debug(TAG, "set bookmark--->" + "videoId:" + bookmark.getItemID() + ",sitcomNO:" + bookmark.getSitcomNO() + ",breakpoint:" + bookmark.getRangeTime());
//                                    }
//                                    if (episodes != null && episodes.size() != 0) {
//                                        Episode playEpisode = null;
//                                        if (bookmark != null && !TextUtils.isEmpty(bookmark.getSitcomNO())) {
//                                            for (Episode episode : episodes) {
//                                                if (bookmark.getSitcomNO().equals(episode.getSitcomNO())) {
//                                                    playEpisode = episode;
//                                                }
//                                            }
//                                        } else {
//                                            playEpisode = episodes.get(0);
//                                        }
//                                        if (null != playEpisode) {
//                                            mDetailPresenter.setXiri(true);
//                                            mDetailPresenter.setLastPlayUrl(lastPlayUrl);
//                                            mDetailPresenter.setLastPlayID(lastPlayID);
//                                            mDetailPresenter.playVOD(playEpisode);
//                                        }
//                                    } else {
//                                        EpgToast.showToast(VodDetailActivity.this, "???????????????????????????");
//                                        return;
//                                    }
//                                }
//                            }
                        } else if ("key4".equals(command)) {
                            if (null != intent && null != intent.getExtras() && intent.getExtras().getString("_rawtext") != null && intent.getExtras().getString("_rawtext").equals("????????????")) {
                                voicePlayLastEpisode("????????????", true);
                            } else {
                                mFeedback.feedback("????????????", Feedback.EXECUTION);
                                Intent intent1 = new Intent(VodDetailActivity.this, NewMyMovieActivity.class);
                                intent1.putExtra("id", "1");
                                startActivity(intent1);
                            }
                        } else if ("key5".equals(command)) {
                            int index = intent.getExtras().getInt("index");
                            SuperLog.info2SD(TAG, "index =" + intent.getExtras().getInt("index"));
                            voicePlayEpisodeByIndex(index);
                        }
                    }
                }
            }
        });
    }

    private void voicePlayLastEpisode(String tip, boolean isLastEpisode) {

        mFeedback.feedback(tip, Feedback.EXECUTION);
        if (mVODDetail == null) {
            return;
        }

        if (!VodUtil.canPlay(is4KSource)) {
            EpgToast.showToast(VodDetailActivity.this, VodDetailActivity.this.getResources().getString(R.string.details_4k_warnning));
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
                        EpgToast.showToast(VodDetailActivity.this, "???????????????????????????");
                    } else {
                        EpgToast.showToast(VodDetailActivity.this, "???????????????");
                    }
                    return;
                }
            } else {//???????????????
                List<Episode> episodes = mVODDetail.getEpisodes();
                Bookmark bookmark = mVODDetail.getBookmark();
                if (bookmark != null) {
                    SuperLog.debug(TAG, "set bookmark--->" + "videoId:" + bookmark.getItemID() + ",sitcomNO:" + bookmark.getSitcomNO() + ",breakpoint:" + bookmark.getRangeTime());
                }
                if (episodes != null && episodes.size() != 0) {
                    Episode playEpisode = null;
                    if (isLastEpisode) {
                        SuperLog.info2SD("countcount,", "mVODDetail.getVodNum()=" + mVODDetail.getVodNum() + "----episodes.size() - 1=" + (episodes.size() - 1));
                        if (Integer.valueOf(mVODDetail.getVodNum()) == episodes.size()) {
                            playEpisode = episodes.get(episodes.size() - 1);
                        } else {
                            EpgToast.showToast(VodDetailActivity.this, "??????????????????!");
                            return;
                        }
                    } else {
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

                    if (null != playEpisode) {
                        mDetailPresenter.setXiri(true);
                        mDetailPresenter.setLastPlayUrl(lastPlayUrl);
                        mDetailPresenter.setLastPlayID(lastPlayID);
                        mDetailPresenter.playVOD(playEpisode);
                    }
                } else {
                    EpgToast.showToast(VodDetailActivity.this, "???????????????????????????");
                    return;
                }
            }
        }
    }


    private void voicePlayEpisodeByIndex(int index) {

        mFeedback.feedback("??????????????????", Feedback.EXECUTION);
        if (mVODDetail == null) {
            return;
        }

        if (!VodUtil.canPlay(is4KSource)) {
            EpgToast.showToast(VodDetailActivity.this, VodDetailActivity.this.getResources().getString(R.string.details_4k_warnning));
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
                List<Episode> episodes = mVODDetail.getEpisodes();
                if (episodes != null && episodes.size() >= index && index > 0) {

                    for (int i = 0; i < episodes.size(); i++) {
                        if (episodes.get(i).getSitcomNO().equals(sitNum)) {
                            Episode playEpisode = episodes.get(i);
                            if (null != playEpisode) {
                                SuperLog.info2SD(TAG, "playEpisode=" + playEpisode);
                                mDetailPresenter.setXiri(true);
                                mDetailPresenter.setLastPlayUrl(lastPlayUrl);
                                mDetailPresenter.setLastPlayID(lastPlayID);
                                mDetailPresenter.playVOD(playEpisode);
                                return;
                            }
                        }
                    }
                    EpgToast.showToast(VodDetailActivity.this, "???????????????");

                } else {
                    EpgToast.showToast(VodDetailActivity.this, "???????????????");
                }
            }
        }
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

//            //?????????????????????????????????????????????????????????
            if (!SubscriptControl.showSubscript(vodMediaFileList, mVODDetail)) {
                mVideoInfoImg1.setVisibility(View.INVISIBLE);
                return;
            } else {
                mVideoInfoImg1.setVisibility(View.VISIBLE);
            }

            String definition = vodMediaFileList.get(0).getDefinition();

            if ("2".equals(definition)) {
                mRightIcon.setBackgroundResource(R.drawable.details_right_4k_icon);
            }

            //??????????????????????????????4K??????
            if (SubscriptControl.iszj4KVOD(mVODDetail)) {
                is4KSource = true;
                mVideoInfoImg1.setImageResource(R.drawable.z4k_normal);
                return;
            }

            if ("0".equals(definition)) {
                mVideoInfoImg1.setImageResource(R.drawable.detail_sd_icon);
            } else if ("1".equals(definition)) {
                mVideoInfoImg1.setImageResource(R.drawable.details_hd_icon);
//                mRightIcon.setBackgroundResource(R.drawable.details_right_hd_icon);
            } else if ("2".equals(definition)) {
                is4KSource = true;
                mVideoInfoImg1.setImageResource(R.drawable.details_4k_icon);
            }
        }
    }

    private void stopXiri() {
        mFocusScene.release();
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

}
