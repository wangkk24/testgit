package com.pukka.ydepg.launcher.ui.template;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.exoplayer2.Player;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.v6bean.V6Constant;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6node.PlayVodBean;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.GetVODDetailResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayChannelResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.report.ubd.extension.RecommendData;
import com.pukka.ydepg.common.report.ubd.scene.UBDSwitch;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.customui.tv.PlayViewWindow;
import com.pukka.ydepg.event.MixAutoPlayTimeEvent;
import com.pukka.ydepg.inf.IPlayListener;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.GroupElement;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.livetv.utils.LiveTVCacheUtil;
import com.pukka.ydepg.moudule.livetv.utils.LiveUtils;
import com.pukka.ydepg.moudule.mytv.utils.MessageDataHolder;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;
import com.pukka.ydepg.moudule.player.ui.OnDemandVideoActivity;
import com.pukka.ydepg.moudule.player.util.PlayerUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 视频播放资源位和海报资源位混搭模板
 *
 * @FileName: com.pukka.ydepg.launcher.ui.template.MixVideoTemplate.java
 * @author: luwm
 * @data: 2018-06-28 11:25
 * @Version V1.0 <描述当前版本功能>
 */
public class MixVideoTemplate extends PHMTemplate implements View.OnFocusChangeListener {

    private static final String TAG = MixVideoTemplate.class.getSimpleName();

    private String playUrl;
    private PlayViewWindow playView;
    //private Map<Integer, String> urls = new HashMap<>();
    private RelativeLayout parentViewLeft, parentViewRight,parentViewThree;

    private ReflectRelativeLayout mPostImageReflect;
    /**
     * 为空或者false则不播放
     * one_time:自动播放一次
     * true:自动无限无限播放
     * */
    private String mLoopType = "";

    //播放器当前进度
    private long currentPosition = 0;

    /**
     * 可以播放视频的资源位
     */
    private ReflectRelativeLayout videoContainer;

    private Group mGroup;
    private Element mElement;
    private List<Element> mElements;
    private int mNavIndex = 0;
    private int mPlayViewPosition = 0;

    private String contentType = "";
    private String contentId = "";
    private String mFourkContentId = "";//4KcontentID
    private String mSwitchingTime = "";//回看开始时间
    private String mStartTime = "";
    private String mEndTime = "";
    private String mediaId = "";
    private String actionUrl = "";
    private String name = "";
    private boolean isLive = false;//是直播还是点播
    private boolean mCanPlay = false;//是否可以播放

    //是否是回看，回看当点播处理
    private boolean isTVod = false;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'.00Z'");//20190128T150000.00z
    private SimpleDateFormat mDateFormatForJian8 = new SimpleDateFormat("yyyyMMddHHmmss");//20190128T150000.00z

    //落焦默认500毫秒开始播放
    private int mAutoPlaymauTime = 500;

    private boolean isBackFromFullScreenPlay = false;

    private int navIndex = 0;

    public MixVideoTemplate(Context context) {
        super(context);
    }

    public MixVideoTemplate(Context context, int layoutId, PHMFragment fragment, OnFocusChangeListener focusChangeListener) {
        super(context, layoutId, fragment, focusChangeListener);
    }

    //返回资源所在当前页面的位置
    public int getIndex(){
        return mNavIndex;
    }
    public void setIndex(int navIndex){
        this.mNavIndex = navIndex;
    }


    @Override
    protected void initView(Context context, int layoutId) {
        super.initView(context, layoutId);
        parentViewLeft = (RelativeLayout) findViewById(R.id.rl_mix_item1_parent);
        parentViewLeft.setOnClickListener((view) ->
                parentViewLeft.findViewById(R.id.rl_item_container01).performClick()
        );
        parentViewLeft.setBackgroundResource(R.drawable.select_vertical_scroll_item_bg);
        parentViewLeft.setPadding(context.getResources().getDimensionPixelSize(R.dimen.margin_2),context.getResources().getDimensionPixelSize(R.dimen.margin_2),
                context.getResources().getDimensionPixelSize(R.dimen.margin_2),context.getResources().getDimensionPixelSize(R.dimen.margin_2));
        if (layoutId == R.layout.pannel_poster_12_video){
            parentViewRight = (RelativeLayout) findViewById(R.id.rl_mix_item2_parent);
            parentViewRight.setOnClickListener((view) ->
                    parentViewRight.findViewById(R.id.rl_item_container02).performClick()
            );
            parentViewRight.setBackgroundResource(R.drawable.select_vertical_scroll_item_bg);
            parentViewRight.setPadding(context.getResources().getDimensionPixelSize(R.dimen.margin_2),context.getResources().getDimensionPixelSize(R.dimen.margin_2),
                    context.getResources().getDimensionPixelSize(R.dimen.margin_2),context.getResources().getDimensionPixelSize(R.dimen.margin_2));
        }
        if (layoutId == R.layout.pannel_poster_13_video){
            parentViewRight = (RelativeLayout) findViewById(R.id.rl_mix_item2_parent);
            parentViewRight.setOnClickListener((view) ->
                    parentViewRight.findViewById(R.id.rl_item_container02).performClick()
            );
            parentViewRight.setBackgroundResource(R.drawable.select_vertical_scroll_item_bg);
            parentViewRight.setPadding(context.getResources().getDimensionPixelSize(R.dimen.margin_2),context.getResources().getDimensionPixelSize(R.dimen.margin_2),
                    context.getResources().getDimensionPixelSize(R.dimen.margin_2),context.getResources().getDimensionPixelSize(R.dimen.margin_2));

            parentViewThree = (RelativeLayout) findViewById(R.id.rl_mix_item3_parent);
            parentViewThree.setOnClickListener((view) ->
                    parentViewThree.findViewById(R.id.rl_item_container03).performClick()
            );
            parentViewThree.setBackgroundResource(R.drawable.select_vertical_scroll_item_bg);
            parentViewThree.setPadding(context.getResources().getDimensionPixelSize(R.dimen.margin_2),context.getResources().getDimensionPixelSize(R.dimen.margin_2),
                    context.getResources().getDimensionPixelSize(R.dimen.margin_2),context.getResources().getDimensionPixelSize(R.dimen.margin_2));
        }

        if (!TextUtils.isEmpty(SessionService.getInstance().getSession()
                .getTerminalConfigurationValue("mixAutoPlayTime"))){
            mAutoPlaymauTime = Integer.parseInt(SessionService.getInstance().getSession()
                    .getTerminalConfigurationValue("mixAutoPlayTime"));
        }

        parentViewLeft.setOnFocusChangeListener(this);
        if (null != parentViewRight){
            parentViewLeft.setOnFocusChangeListener(this);
        }
        if (null != parentViewThree){
            parentViewThree.setOnFocusChangeListener(this);
        }
    }

    private List<VOD> mDataList;
    private DataLoaderAdapter mAdapter;

    @Override
    public <T> void bindSubjectVOD(List<T> dataList, DataLoaderAdapter adapter, int position) {
        super.bindSubjectVOD(dataList, adapter, position);
        mDataList = (List<VOD>) dataList;
        mAdapter = adapter;
        parseData(1);
    }

    //处理静态数据（点播和直播）；直播只支持静态
    private void setStaticData(ReflectRelativeLayout relativeLayout){
        navIndex = relativeLayout.getNavIndex();
        if (null != relativeLayout.getElementData()){
            actionUrl = relativeLayout.getElementData().getElementAction().getActionURL();
        }
        contentType = ZJVRoute.getContentValue(actionUrl,
                ZJVRoute.ActionUrlKeyType.CONTENT_TYPE);
        contentId = ZJVRoute.getContentValue(actionUrl,
                ZJVRoute.ActionUrlKeyType.CONTENT_ID);
        mFourkContentId = ZJVRoute.getContentValue(actionUrl,
                ZJVRoute.ActionUrlKeyType.FOURK_CONTENT_ID);
        mStartTime = ZJVRoute.getContentValue(actionUrl,
                ZJVRoute.ActionUrlKeyType.START_TIME);
        mEndTime = ZJVRoute.getContentValue(actionUrl,
                ZJVRoute.ActionUrlKeyType.END_TIME);
        name = relativeLayout.getTitleText();
        mSwitchingTime = ZJVRoute.getContentValue(actionUrl,
                ZJVRoute.ActionUrlKeyType.SWITCHING_TIME);

        if (!TextUtils.isEmpty(mStartTime) && !TextUtils.isEmpty(formatTime(mStartTime))){
            mStartTime = formatTime(mStartTime);
        }
        if (!TextUtils.isEmpty(mEndTime) && !TextUtils.isEmpty(formatTime(mEndTime))){
            mEndTime = formatTime(mEndTime);
        }

        SuperLog.info2SD(TAG,"SwitchingTime="+mSwitchingTime+";StartTime="+ZJVRoute.getContentValue(actionUrl,
                ZJVRoute.ActionUrlKeyType.START_TIME)+";EndTime="+ZJVRoute.getContentValue(actionUrl,
                ZJVRoute.ActionUrlKeyType.END_TIME)+
                ";formatTime(mStartTime)="+mStartTime+";formatTime(mEndTime)="+mEndTime+";------SwitchingTime正确格式：20181213T092741.00Z;actionUrl="+actionUrl+";VodUtil.isDeviceSupport4K()="+VodUtil.isDeviceSupport4K());
        if (contentType.equals(VideoLiveTemplate.CONTENT_TYPE_LIVE)){
            isLive = true;
            ChannelDetail channelDetail;
            //有4KcontentID 优先播放4K
            if (!TextUtils.isEmpty(mFourkContentId) && null != LiveUtils.findScheduleFromCanPlayById(mFourkContentId)){
                channelDetail = LiveUtils.findScheduleFromCanPlayById(mFourkContentId);
            }else{
                channelDetail = LiveUtils.findScheduleFromCanPlayById(contentId);
            }
            if (null != channelDetail) {
                mediaId = LiveUtils.parseMediaID(channelDetail);
                getChannelUrl(channelDetail.getID(),mediaId);
            }
        }else if (contentType.equals(VideoLiveTemplate.CONTENT_TYPE_VOD)){
            isLive = false;
            VOD currentVod = new VOD();
            if (VodUtil.isDeviceSupport4K() && !TextUtils.isEmpty(mFourkContentId)){
                currentVod.setID(mFourkContentId);
            }else{
                currentVod.setID(contentId);
            }
            getVODDetail(currentVod);
        }
    }

    //处理数据，进行播放
    public void parseData(int dataType){
        for (ReflectRelativeLayout relativeLayout : elementViews) {
            if (TextUtils.equals(relativeLayout.getExtraData().get(Constant.AUTO_PLAY), "true") ||
                    TextUtils.equals(relativeLayout.getExtraData().get(Constant.AUTO_PLAY), "one_time")) {
                mLoopType = relativeLayout.getExtraData().get(Constant.AUTO_PLAY);
                videoContainer = relativeLayout;
                setOnclick();

                if (null != relativeLayout.getExtraData() && !TextUtils.isEmpty(relativeLayout.getExtraData().get("Is_Shimmer")) &&
                        relativeLayout.getExtraData().get("Is_Shimmer").equals("false")){
                    MessageDataHolder.get().setIsShimmerMix(false);
                }

                if (null != relativeLayout.getVodData() && !TextUtils.isEmpty(relativeLayout.getVodData().getID())){
                    isLive = false;
                    //处理动态数据，点播；
                    playVod(relativeLayout.getVodData());
                }else if (null != relativeLayout.getElementData() && !TextUtils.isEmpty(relativeLayout.getElementData().getElementAction().getActionURL())){
                    //处理静态数据（直播或者点播）
                    setStaticData(relativeLayout);
                }else if (null != getFragment()){
                    getFragment().setMixVideoTemplate(mNavIndex,null);
                }
                return;
            }else if (null != getFragment()){
                getFragment().setMixVideoTemplate(mNavIndex,null);
            }
        }
    }

    @Override
    public void setElements(PHMFragment fragment) {

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {

            View view = ((ViewGroup) getChildAt(i)).getChildAt(0);
            if (view instanceof ReflectRelativeLayout) {
                addElementViews(view);
            }else if (view.getParent() instanceof ReflectRelativeLayout){
                addElementViews((View) view.getParent());
            }
        }
    }

    private void addElementViews(View view){
        view.setVisibility(VISIBLE);
        ((ReflectRelativeLayout) view).setFragment(fragment);
        if (onFocusChangeListener != null) {
            view.setOnFocusChangeListener(onFocusChangeListener);
        }
        elementViews.add((ReflectRelativeLayout) view);
    }

    @Override
    public void setElementData(GroupElement groupElement, int navIndex) {
        this.mElements = groupElement.getElement();
        this.mGroup = groupElement.getGroup();
        this.mNavIndex = navIndex;
        super.setElementData(groupElement, navIndex);
        if (null != getFragment()){
            getFragment().setMixVideoTemplate(mNavIndex,this);
        }
        if (mElements == null || mElements.size() == 0)return;
        if (navIndex > mElements.size()-1){
            this.mElement = mElements.get(mElements.size()-1);
        }else{
            this.mElement = mElements.get(navIndex);
        }

    }

    private void playVod(VOD vodData) {
        if (null != vodData) {
            if (!TextUtils.isEmpty(mFourkContentId)){
                vodData.setID(mFourkContentId);
            }
            getVODDetail(vodData);
        }
    }

    /**
     * @param vod
     */
    private void getVODDetail(VOD vod) {
        fragment.getPresenter().getVODDetail(vod.getID(), new RxCallBack<GetVODDetailResponse>(HttpConstant.GETVODDETAIL, getContext()) {
            @Override
            public void onSuccess(GetVODDetailResponse getVODDetailResponse) {
                if (null != getVODDetailResponse.getVODDetail())
                    getVODUrl(getVODDetailResponse.getVODDetail());
            }

            @Override
            public void onFail(Throwable e) {
                SuperLog.debug(TAG, "getVODDetail failed :" + e.toString());
            }
        });


    }

    private void getVODUrl(VODDetail detail) {
        if(null==detail){
            return;
        }
        VODDetail vodDetail = null;
        if (detail.getVODType().equals("1")) {//电视剧
            vodDetail = playEpisodes(detail);
        }
        List<VODMediaFile> vodMediaFiles = null;
        if (null != vodDetail){
            vodMediaFiles = vodDetail.getMediaFiles();
        }else{
            vodMediaFiles = detail.getMediaFiles();
        }
        if (vodMediaFiles == null || vodMediaFiles.size() == 0) {
            return;
        }
        fragment.getPresenter().getPlayUrl(null == vodDetail ? detail : vodDetail, new RxCallBack<PlayVODResponse>(HttpConstant.PLAYVOD, getContext()) {
            @Override
            public void onSuccess(PlayVODResponse playVODResponse) {
                if (!TextUtils.isEmpty(playVODResponse.getPlayURL())) {
                    initPlayView(true);
                    playUrl = StringUtils.splicingPlayUrl(playVODResponse.getPlayURL());
                    fragment.setUrl(mNavIndex,playUrl);
                    //落焦播放改为自动播放
                    startPlay(playUrl);
                }
            }

            @Override
            public void onFail(Throwable e) {
            }
        });
    }

    private void initPlayView(boolean isShowPost) {
        if (null == playView && null != videoContainer) {
            if (videoContainer.getId() == R.id.rl_item_container01) {
                mPlayViewPosition = 0;
                playView = (PlayViewWindow) parentViewLeft.findViewById(R.id.playview_mix_item1);
                mPostImageReflect = (ReflectRelativeLayout) parentViewLeft.findViewById(R.id.rl_item_container01);
            }else if (videoContainer.getId() == R.id.rl_item_container02){
                mPlayViewPosition = 1;
                playView = (PlayViewWindow) parentViewRight.findViewById(R.id.playview_mix_item2);
                mPostImageReflect = (ReflectRelativeLayout) parentViewLeft.findViewById(R.id.rl_item_container02);
            }else if (videoContainer.getId() == R.id.rl_item_container03){
                mPlayViewPosition = 2;
                playView = (PlayViewWindow) parentViewThree.findViewById(R.id.playview_mix_item3);
                mPostImageReflect = (ReflectRelativeLayout) parentViewLeft.findViewById(R.id.rl_item_container03);
            }else{
                SuperLog.error(TAG,"Unknown videoContainer id.");
            }
        }
        /*if (isShowPost){
            SuperLog.debug(TAG,"---initPlayView()-->playCompletedShowPost()");
            playCompletedShowPost();
        }*/

        //playView.setVisibility(VISIBLE);
        if(null == playView){
            SuperLog.error(TAG,"PlayView is null");
            return;
        }
        fragment.addFunctionTemplate(mNavIndex, MixVideoTemplate.this);
        playView.setOnPlayCallback(new IPlayListener() {
            @Override
            public void onPlayState(int playbackState) {
                if (playbackState == Player.STATE_READY){
                    if (null != getFragment() && null != videoContainer){
                        getFragment().setIsShowVideoView(true);
                    }
                }else if (playbackState == Player.STATE_ENDED){
                    if (null != getFragment() && null != videoContainer){
                        getFragment().setIsShowVideoView(false);
                    }
                }
                /*if (playbackState == Player.STATE_READY && !mCanPlay){
                    playCompletedShowPost();
                }*/
            }

            @Override
            public void onPrepared(int  Videotype) { }

            @Override
            public void onRelease() { }

            @Override
            public void onPlayError(String msg, int errorCode, int playerType) { }

            @Override
            public void onPlayCompleted() {
                //播放进度置为零
                currentPosition = 0;
                if (mLoopType.equals("true")){
                    //playView.seekTo(0);
                    playView.releasePlayer();
                    startPlay(playUrl);
                }else{
                    MessageDataHolder.get().setIsMixVideoPostImage(true);
                    playCompletedShowPost();
                }
            }

            @Override
            public void onDetached(long time)
            {
                playPauseOrDetached(time);
            }

            @Override
            public void onAttached() {
                SuperLog.debug(TAG,"---onAttached()---");
                //fragment.addFunctionTemplate(mNavIndex, MixVideoTemplate.this);
                //urls = fragment.getUrlsByPosition(mNavIndex);
                playUrl = fragment.getChannelUrl(mNavIndex);
                currentPosition = fragment.getPlayTimeByPosition(mNavIndex);
                isLive = fragment.getPlayType(mNavIndex);

                if (!TextUtils.isEmpty(fragment.getPlayOrNo(mNavIndex)) && !fragment.getPlayOrNo(mNavIndex).equals("-1")){
                    mLoopType = fragment.getPlayOrNo(mNavIndex);
                }

                startPlay(playUrl);

            }

            @Override
            public void onTryPlayForH5() { }

            @Override
            public void onAdVideoEnd() {

            }
        });
    }

    private Handler mVideoTemplateHandler = new Handler();

    @Subscribe
    public void onEvent(MixAutoPlayTimeEvent event){
        MessageDataHolder.get().setIsMixVideoPostImage(false);

        if (!fragment.getUserVisibleHint())return;

        if (null == playView){
            initPlayView(false);
        }
        if (event.getLayoutId() == playView.getId() && !event.isOldFocusViewId()){
            mCanPlay = true;
            mVideoTemplateHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    playAndHidePostImage();
                    startPlay(playUrl);
                }
            },mAutoPlaymauTime);
        }else if (event.getLayoutId() == playView.getId() && event.isOldFocusViewId()){
            mCanPlay = false;
            playPauseOrDetached(playView.getCurrentPosition());
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        SuperLog.debug(TAG,"visibility="+visibility);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        SuperLog.debug(TAG,"hasWindowFocus="+hasWindowFocus);
    }

    public void startPlay(String playURL) {

        if (TextUtils.isEmpty(playURL) || (fragment.getActivity() instanceof MainActivity && !((MainActivity) fragment.getActivity()).isStartPicFinished())) {
            return;
        }

        if (playHandler.hasMessages(1)) {
            playHandler.removeMessages(1);
        }
        Message msg = new Message();
        msg.what = 1;
        msg.obj = playURL;
        playHandler.sendMessageDelayed(msg, 1000);
    }

    @Override
    public View getFirstView() {
        if (!CollectionUtil.isEmpty(elementViews)){
            if (elementViews.get(0).getId() == R.id.rl_item_container01){
                return parentViewLeft;
            }else{
                return elementViews.get(0);
            }
        }
        return null;
    }

    //解决在首页按主页Home键，视频重头开始播放
    @Override
    public void onFragmentPause() {
        SuperLog.debug(TAG,"---onFragmentPause()---");
        MessageDataHolder.get().setIsMixVideoPostImage(false);
        mVideoTemplateHandler.removeCallbacksAndMessages(null);
        playHandler.removeCallbacksAndMessages(null);
        playCompletedShowPost();
        if (!isBackFromFullScreenPlay){
            mCanPlay = false;
        }
        if (null != playView) {
            playView.releasePlayer();
        }
    }

    @Override
    public void onFragmentResume() {
        SuperLog.debug(TAG,"---onFragmentResume()---");
        startPlay(playUrl);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        SuperLog.debug(TAG,"---onAttachedToWindow---");
        playCompletedShowPost();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        SuperLog.debug(TAG,"---onDetachedFromWindow---");
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        mVideoTemplateHandler.removeCallbacksAndMessages(null);
        playHandler.removeCallbacksAndMessages(null);
        if (null != playView){
            playView.releasePlayer();
        }
        if (null != getFragment()){
            getFragment().setIsShowVideoView(false);
        }
        super.onDetachedFromWindow();
    }

    //直播鉴权，获取直播、播放地址
    private void getChannelUrl(String contentId,String meidaId) {
        PlayChannelRequest playChannelRequest = new PlayChannelRequest();
        playChannelRequest.setChannelID(contentId);
        playChannelRequest.setMediaID(meidaId);
        playChannelRequest.setBusinessType(V6Constant.ChannelURLType.BTV);
        playChannelRequest.setURLFormat("1");//HLS
        fragment.getPresenter().playChannel(playChannelRequest, new
                RxCallBack<PlayChannelResponse>(HttpConstant.PLAYCHANNEL, getContext()) {
                    @Override
                    public void onSuccess(PlayChannelResponse playChannelResponse) {
                        if (!TextUtils.isEmpty(playChannelResponse.getPlayURL())) {
                            initPlayView(true);
                            playUrl = StringUtils.splicingPlayUrl(playChannelResponse.getPlayURL());
                            playUrl = setTvodPlayUrl(playUrl);
                            fragment.setUrl(mNavIndex,playUrl);
                            SuperLog.info2SD(TAG,"playUrl="+playUrl);
                            //urls.put(0, playUrl);
                            if (fragment.getUserVisibleHint()) {
                                startPlay(playUrl);
                            }
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                    }
                });
    }

    //播放电视剧
    private VODDetail playEpisodes(VODDetail detail) {
        List<Episode> episodes = detail.getEpisodes();
        Bookmark bookmark = detail.getBookmark();
        if (episodes != null && episodes.size() != 0) {
            if (bookmark != null) {
                for (Episode episode : episodes) {
                    if (episode.getSitcomNO().equals(bookmark.getSitcomNO())) {
                        detail.setMediaFiles(episode.getVOD().getMediaFiles());
                        detail.setID(episode.getVOD().getID());
                        return detail;
                    }
                }
            }
            detail.setMediaFiles(episodes.get(0).getVOD().getMediaFiles());
            detail.setID(episodes.get(0).getVOD().getID());
            return detail;
        }
        return null;
    }

    private void onClickStatic(){
        String sContentId = contentId;
        if (!TextUtils.isEmpty(mFourkContentId) && null != LiveUtils.findScheduleFromCanPlayById(mFourkContentId)){
            sContentId = mFourkContentId;
        }

        //跳转到直播
        if (contentType.equals(VideoLiveTemplate.CONTENT_TYPE_LIVE) && !isTVod) {
            Intent intent = new Intent(getContext(), LiveTVActivity.class);
            //记录准备播放的频道ID和mediaID
            if (!TextUtils.isEmpty(sContentId) && !TextUtils.isEmpty(mediaId)) {

                if (!TextUtils.isEmpty(ZJVRoute.getContentValue(actionUrl, ZJVRoute.ActionUrlKeyType.CHANNEL_MINI_TYPE))
                        && ZJVRoute.getContentValue(actionUrl, ZJVRoute.ActionUrlKeyType.CHANNEL_MINI_TYPE).equals("-1")){
                    //true:直播只播放一个频道，不能切台、不显示MiniEpg
                    intent.putExtra(LiveTVActivity.CHANNEL_FINAL, true);
                    //用于临时记录直播播放固定频道的channelID、MediaId
                    LiveDataHolder.get().setChannelAndMediaId(sContentId, mediaId);
                    LiveDataHolder.get().setIsShowingSkip(true);
                }else{
                    //本地没有缓存的频道信息,不传,后面有做默认处理
                    LiveTVCacheUtil.getInstance().recordPlayChannelInfo(sContentId, mediaId);
                }

            } else {
                //contentId或者media为空，则代表播放失败。不进行跳转
                return;
            }
            isBackFromFullScreenPlay = true;
            intent.putExtra(LiveTVActivity.VIDEO_TYPE, LiveTVActivity.VIDEO_TYPE_LIVETV);
            getContext().startActivity(intent);
        } else if (contentType.equals(VideoLiveTemplate.CONTENT_TYPE_VOD) || isTVod) {
            //跳转到VOD
            if (TextUtils.isEmpty(sContentId))
                return;
            isBackFromFullScreenPlay = true;
            currentPosition = playView == null ? 0 : playView.getCurrentPosition();
            Intent intent = new Intent(getContext(), OnDemandVideoActivity.class);
            PlayVodBean bean = new PlayVodBean();
            if (!isTVod){
                bean.setVodId(sContentId);
                bean.setVodName(videoContainer.getTitleText());
                bean.setBookmark(String.valueOf(currentPosition/1000));
            }else{
                bean.setVodName(videoContainer.getTitleText());
            }
            bean.setPlayUrl(playUrl);
            LiveDataHolder.get().setPlayVodBean(JsonParse.object2String(bean));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
        UBDSwitch.getInstance().recordMainOnclick(actionUrl,mElement,mGroup,null,null);
    }

    private void playPauseOrDetached(long playTime){
        if (null != fragment){
            fragment.removeVideoTemplate(mNavIndex);
            fragment.setUrl(mNavIndex,playUrl);
            fragment.setPlayOrNo(mNavIndex,mLoopType);
            fragment.addPlayTime(mNavIndex,playTime);
            fragment.setPlayType(mNavIndex,isLive);
        }

        playCompletedShowPost();
    }

    private void playCompletedShowPost(){
        if (null != mPostImageReflect && mPostImageReflect.getVisibility() == GONE){
            mPostImageReflect.setVisibility(View.VISIBLE);
            if (null != mElements  && mElements.size() > 0 && mPlayViewPosition < mElements.size() && null != mElements.get(mPlayViewPosition).getDefaultFocus()
                    && mElements.get(mPlayViewPosition).getDefaultFocus().equals("true")){
                mPostImageReflect.setElementData(mElements.get(mPlayViewPosition));
            }else if (null != mDataList  && mDataList.size() > 0 && mPlayViewPosition < mDataList.size()){
                mPostImageReflect.parseVOD(mAdapter, mDataList.get(mPlayViewPosition));
            }
        }
        if (null != playView){
            playView.setVisibility(View.GONE);
            playView.releasePlayer();
        }
    }

    private void playAndHidePostImage(){
        if (null != playView && playView.getVisibility() == GONE)
            playView.setVisibility(View.VISIBLE);
        if (null != mPostImageReflect && mPostImageReflect.getVisibility() == VISIBLE)
            mPostImageReflect.setVisibility(View.GONE);
        setOnclick();
    }

    //是否已经到了Switch Time时间播放回看
    private boolean isNewDateBeforeSwitchTime(String switchingTime){
        try {
            if (mDateFormat.parse(switchingTime).before(new Date())){
                return true;
            }
        } catch (ParseException e) {
            SuperLog.error(TAG,e);
        }
        return false;
    }

    //
    private void setOnclick(){
        if (null == videoContainer)return;
        videoContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(playUrl)) {
                    return;
                }
                mCanPlay = true;
                if (null != videoContainer.getVodData() && !isLive && !isTVod) {
                    isBackFromFullScreenPlay = true;
                    currentPosition = playView == null ? 0 : playView.getCurrentPosition();
                    VOD currentVod = videoContainer.getVodData();
                    Intent intent = new Intent(context, OnDemandVideoActivity.class);
                    PlayVodBean bean = new PlayVodBean();
                    bean.setVodId(currentVod.getID());
                    bean.setVodName(videoContainer.getTitleText());
                    bean.setPlayUrl(playUrl);
                    bean.setBookmark(String.valueOf(currentPosition / 1000));
//                    String vodBeanStr = JsonParse.object2String(bean);
//                    intent.putExtra(OnDemandVideoActivity.PLAY_VOD_BEAN, vodBeanStr);
                    LiveDataHolder.get().setPlayVodBean(JsonParse.object2String(bean));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                    UBDSwitch.getInstance().recordMainOnclick("", mElement, mGroup,currentVod,null);

                }else{
                    onClickStatic();
                }
            }
        });
    }

    private String setTvodPlayUrl(String playUrl){
        String url = "";
        if (isLive && ((!TextUtils.isEmpty(mSwitchingTime) && (mSwitchingTime.equals(mDateFormat.format(new Date())) || isNewDateBeforeSwitchTime(mSwitchingTime))
                && !TextUtils.isEmpty(mStartTime) && !TextUtils.isEmpty(mEndTime)) || (TextUtils.isEmpty(mSwitchingTime) && !TextUtils.isEmpty(mStartTime) && !TextUtils.isEmpty(mEndTime)))) {
            isTVod = true;
            if (!playUrl.contains(mStartTime) && playUrl.contains("index.m3u8?")){
                String substring = playUrl.substring(0, playUrl.indexOf("index.m3u8?"));
                String substring2 = playUrl.substring(playUrl.indexOf("index.m3u8?"), playUrl.length());
                String replace1="",replace2="",replace3="";
                if (substring2.contains("index.m3u8?")){
                    replace1 = substring2.replace("index.m3u8?", "");
                }else{
                    replace1 = substring2;
                }
                if (replace1.contains("servicetype=1")){
                    replace2 = replace1.replace("servicetype=1", "servicetype=3");
                }else{
                    replace2 = replace1;
                }
                if (substring.contains("/PLTV/")){
                    replace3 = substring.replace("/PLTV/", "/TVOD/");
                }else{
                    replace3 = substring;
                }
                url = replace3 + "index.m3u8?playseek=" + mStartTime + "-" + mEndTime + "&timezone=UTC&EnableTrick=1&recType=1" + replace2;
                return url;
            }else{
                return playUrl;
            }
        }
        return playUrl;
    }

    public void onToPlayFullScreenClick(){
        if (TextUtils.isEmpty(playUrl)) {
            return;
        }
        mCanPlay = true;
        if (null != videoContainer.getVodData() && !isLive && !isTVod){
            isBackFromFullScreenPlay = true;
            currentPosition = playView == null ? 0 : playView.getCurrentPosition();
            VOD currentVod = videoContainer.getVodData();
            Intent intent = new Intent(context, OnDemandVideoActivity.class);
            PlayVodBean bean = new PlayVodBean();
            bean.setVodId(currentVod.getID());
            bean.setVodName(videoContainer.getTitleText());
            bean.setPlayUrl(playUrl);
            bean.setBookmark(String.valueOf(currentPosition/1000));
//            String vodBeanStr = JsonParse.object2String(bean);
//            intent.putExtra(OnDemandVideoActivity.PLAY_VOD_BEAN, vodBeanStr);
            LiveDataHolder.get().setPlayVodBean(JsonParse.object2String(bean));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            UBDSwitch.getInstance().recordMainOnclick("",mElement,mGroup,currentVod,null);

        }else{
            onClickStatic();
        }
    }

    //拼接回看地址比实际时间晚八个小时。减去八个小时
    private String formatTime(String time){
        try {
            Date parse = mDateFormatForJian8.parse(time);
            long rightTime =parse.getTime() - 8 * 60 * 60 * 1000;
            return mDateFormatForJian8.format(rightTime);
        } catch (ParseException e) {
            SuperLog.error(TAG,e);
        }
        return "";
    }

    //解决从第一个快速按下键，焦点落在第三个，播放的是第二个
    @SuppressLint("HandlerLeak")
    private Handler playHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){

                SuperLog.debug(TAG,"!fragment.getUserVisibleHint()="+!fragment.getUserVisibleHint());
                if (null == playView || TextUtils.isEmpty((CharSequence) msg.obj)||null==fragment||!fragment.getUserVisibleHint()) {//getGlobalVisibleRect
                    return;
                }

                SuperLog.debug(TAG,"!PlayerUtils.isVisibleALL(mPostImageReflect)="+!PlayerUtils.isVisibleALL(mPostImageReflect));
                //如果不是全屏显示，则展示海报，并return
                if (!PlayerUtils.isVisibleALL(mPostImageReflect)){
                    if (null != playView && playView.getVisibility() == VISIBLE){
                        playCompletedShowPost();
                    }
                    return;
                }else if (null != playView && playView.getVisibility() == VISIBLE && playView.isPlaying()){//如果是全屏显示，切正在播放，return
                    return;
                }
                playAndHidePostImage();
                if (isLive){//播放直播
                    playUrl = setTvodPlayUrl((String) msg.obj);
                    playView.startPlay(playUrl);
                    SuperLog.debug(TAG,"channelurl="+playUrl);
                }else{//播放点播
                    playView.startPlay((String) msg.obj,currentPosition);
                    SuperLog.debug(TAG,"影片总时长playView.getDuration()=="+playView.getDuration()+";playURL="+(String) msg.obj);
                }
                // 第一次初始化如果在顶部不会触发onAttach需要手动添加
                //fragment.addFunctionTemplate(mNavIndex, MixVideoTemplate.this);
            }else if (msg.what == 0){
                startPlay(playUrl);
            }
        }
    };

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.rl_mix_item1_parent){
            setBg(hasFocus,parentViewLeft);
        }else if (v.getId() == R.id.rl_mix_item2_parent){
            setBg(hasFocus,parentViewRight);
        }else if (v.getId() == R.id.rl_mix_item3_parent){
            setBg(hasFocus,parentViewThree);
        }
    }
    private void setBg(boolean hasFocus,RelativeLayout relativeLayout){
        /*if (null != relativeLayout){
            if (hasFocus){
                relativeLayout.setPadding(context.getResources().getDimensionPixelSize(R.dimen.margin_2),context.getResources().getDimensionPixelSize(R.dimen.margin_2),
                        context.getResources().getDimensionPixelSize(R.dimen.margin_2),context.getResources().getDimensionPixelSize(R.dimen.margin_2));
            }else{
                relativeLayout.setPadding(0,0,0,0);
            }
        }*/
    }
}