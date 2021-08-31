package com.pukka.ydepg.launcher.ui.template;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.Player;
import com.pukka.ydepg.GlideApp;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.v6bean.V6Constant;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.PlayVodBean;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.GetVODDetailResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayChannelResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.report.ubd.scene.UBDSwitch;
import com.pukka.ydepg.common.utils.GlideUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.customui.tv.PlayViewWindow;
import com.pukka.ydepg.event.VideoLivePlayTimeEvent;
import com.pukka.ydepg.inf.IPlayListener;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.GroupElement;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.livetv.utils.LiveTVCacheUtil;
import com.pukka.ydepg.moudule.livetv.utils.LiveUtils;
import com.pukka.ydepg.moudule.player.node.Schedule;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;
import com.pukka.ydepg.moudule.player.ui.OnDemandVideoActivity;
import com.pukka.ydepg.moudule.player.util.PlayerUtils;
import com.pukka.ydepg.view.PlayView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class VideoLiveTemplate extends PHMTemplate implements View.OnFocusChangeListener, View.OnClickListener {
    private static final String TAG = "VideoTemplate";
    private static final String CONTENT_TYPE = "ContentType";
    private static final String CONTENT_ID = "ContentId";
    private static final String CONTENT_MEDIA_ID = "MeidaId";
    private static final String CONTENT_NAME = "VideoTemplate";
    private static final String CONTENT_ACTION_URL = "ACTIONURL";
    public static final String CONTENT_TYPE_LIVE = "CHANNEL";
    public static final String CONTENT_TYPE_VOD = "STATICVOD";
    public static final String FORCE_DEFAULT_DATA = "false";
    private OnFocusChangeListener onFocusChangeListener;
    private Context context;
    private int playState = 0;
    private PlayViewWindow videoView;//左侧播放窗口
    private RelativeLayout videoContainer;
    private ImageView ivPoster;
    private ImageView ivFullScreen;//视频播放窗口右下角全屏小图片，展示10S

    private Map<Integer,String> mPlayUrlMap = new HashMap<>();

    //用于处理播放失败时，循环一遍处理
    private Map<Integer,Boolean> mIsCanPlay = new HashMap<>();

    private List<Map<String,String>> mDataMap = new ArrayList<>();
    private Map<Integer,VODDetail> mVodDetailMap = new HashMap<>();

    //跳转到VOD时需要用到
    private String mVodPlayUrl = "";

    @SuppressLint("UseSparseArrays")
    private Map<Integer, String> urls = new HashMap<>();
    private List<RelativeLayout> views = new ArrayList<>();
    private int queryPosition = 0;//请求第几个VOD地址
    private int playPostion = -1;//当前正在播放第几个
    private int viewPosition;//当前模板在页面中的位置
    private boolean isUrlsReady = false;
    private static final int QUERY_URL_SUCCESS = 1110;
    private static final int QUERY_URLS_FINISHED = 1111;
    private long playTime = 0;
    private boolean isFirstAttached = true;
    private static final int QUERY_FAILED = 1112;

    private Group mGroup;
    private List<Element> mElements;

    //落焦默认500毫秒开始播放
    private int mAutoPlaymauTime = 500;

    //=1或者为null，落焦播放；=2代表自动播放
    private String mPlayType;

    //是否播放完成
    private boolean isPlayComplete = false;

    private boolean mCanPlay = false;//是否可以播放

    private Timer timer = null;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public VideoLiveTemplate(Context context) {
        super(context);
    }

    public VideoLiveTemplate(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoLiveTemplate(Context context, int layoutId, PHMFragment fragment, OnFocusChangeListener onFocusChangeListener) {
        super(context);
        this.context = context;
        this.layoutId = layoutId;
        this.fragment = fragment;
        this.onFocusChangeListener = onFocusChangeListener;
        setFocusable(false);
        initView(context, layoutId);
        stopPlayAndShowPostImg();
    }

    @Override
    public View getFirstView() {
        return videoContainer;
    }

    protected void initView(Context context, int layoutId) {
        super.initView(context, layoutId);
        ivPoster = (ImageView) findViewById(R.id.iv_pan_l1_r4_left_poster);
        ivFullScreen = (ImageView) findViewById(R.id.iv_full_screen);
        videoView = (PlayViewWindow) findViewById(R.id.pan_l1_r4_video);
        initVideoView();
        views.add((RelativeLayout) findViewById(R.id.rl_pan_l1_r4_item0));
        views.add((RelativeLayout) findViewById(R.id.rl_pan_l1_r4_item1));
        views.add((RelativeLayout) findViewById(R.id.rl_pan_l1_r4_item2));
        views.add((RelativeLayout) findViewById(R.id.rl_pan_l1_r4_item3));
        videoContainer = (RelativeLayout) findViewById(R.id.pan_l1_r4_left);
        videoContainer.setOnClickListener(this);

        if (!TextUtils.isEmpty(SessionService.getInstance().getSession().getTerminalConfigurationValue("mixAutoPlayTime"))){
            mAutoPlaymauTime = Integer.parseInt(SessionService.getInstance().getSession().getTerminalConfigurationValue("mixAutoPlayTime"));
        }
    }

    private void initVideoView(){
        videoView.setResizeMode(PlayView.RESIZE_MODE_AUTO);
        videoView.setFocusable(false);

        videoView.setOnPlayCallback(new IPlayListener() {
            @Override
            public void onPlayState(int playbackState) {
                playState = playbackState;
                if (playbackState == Player.STATE_READY){
                    if (null != getFragment()){
                        getFragment().setIsShowVideoView(true);
                    }
                }else if (playbackState == Player.STATE_ENDED){
                    if (null != getFragment()){
                        getFragment().setIsShowVideoView(false);
                    }
                }
                /*if (canPlayOrPause()) {
                    ivPoster.setVisibility(INVISIBLE);
                }*/
            }

            @Override
            public void onPrepared(int  Videotype) { }

            @Override
            public void onRelease() {
                playState = 0;
            }

            @Override
            public void onPlayError(String msg, int errorCode, int playerType) {

                if (mIsCanPlay.get(playPostion) != null && !mIsCanPlay.get(playPostion)){
                    return;
                }

                mIsCanPlay.put(playPostion,false);
                playNextVideo();
            }

            @Override
            public void onPlayCompleted() {
                isPlayComplete = true;
                playNextVideo();
            }

            @Override
            public void onDetached(long time) {
                fragment.removeVideoTemplate(viewPosition);
                fragment.addPlayPosition(viewPosition, playPostion);
                fragment.addPlayTime(viewPosition, time);
                //fragment.addUrls(mChannelPlayUrl);
                videoView.releasePlayer();

                cancleTimer();
            }

            @Override
            public void onAttached() {
                fragment.addFunctionTemplate(viewPosition, VideoLiveTemplate.this);
                playTime = fragment.getPlayTimeByPosition(viewPosition);
                playPostion = fragment.getPlayPosition(viewPosition);
                isFirstAttached = false;
            }

            @Override
            public void onTryPlayForH5() {

            }

            @Override
            public void onAdVideoEnd() {

            }
        });
    }

    public void onFragmentPause() {
        mVideoTemplateHandler.removeCallbacksAndMessages(null);
        playHandler.removeCallbacksAndMessages(null);

        playTime = videoView.getCurrentPosition();
        fragment.addPlayPosition(viewPosition, playPostion);
        videoView.releasePlayer();

        cancleTimer();
    }

    public void onFragmentResume() {

        //首页视频窗口轮播
        //从其他界面返回，初始化播放器
        if (null != videoView){
            videoView.initVideoView();
            initVideoView();
        }

        if (isAutoPlay()){
            if (null != videoView && PlayerUtils.isVisibleALL(ivPoster)){
                getPlayUrlByPosition(playPostion == -1 ? 0 : playPostion);
            }
        }else{
            if (fragment.getUserVisibleHint() && playPostion != -1) {
                startPlayVideo();
            }
        }
    }

    private void setFlagForNavFocus(){
        (OTTApplication.getContext().getMainActivity()).getLauncherFocusHelper().setIsVideoRequest(true);
    }

    private void playNextVideo() {
        setFlagForNavFocus();
        playPostion++;
        if (playPostion >= 4){
            playPostion = 0;
        }
        playTime = 0;
        setPlayIcon(playPostion);
        if (null != mPlayUrlMap && mPlayUrlMap.size() > 0) {
            if (TextUtils.isEmpty(mPlayUrlMap.get(playPostion))) {
                queryUrl(playPostion);
            }else{
                startPlay(mPlayUrlMap.get(playPostion));
            }
        }
    }

    public boolean isViewVisiable() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) fragment.getRecyclerViewTV().getLayoutManager();
        int position = linearLayoutManager.getPosition(VideoLiveTemplate.this);
        if(position==-1){
            return false;
        }
        int firstVisiablePosition = linearLayoutManager.findFirstVisibleItemPosition();
        int lastVisiablePosition = linearLayoutManager.findLastVisibleItemPosition();
        return position <= lastVisiablePosition && position >= firstVisiablePosition;
    }

    @Override
    public void setElementData(GroupElement groupElement, int i) {
        this.mGroup = groupElement.getGroup();
    }


    @SuppressLint("SetTextI18n")
    public void bindSubjectVOD(List<Element> elements, int position) {
        if (elements == null)return;
        this.mElements = elements;
        setIsCanPlay(true);
        // 第一次初始化如果在顶部不会触发onAttach需要手动添加
        fragment.addFunctionTemplate(position, VideoLiveTemplate.this);
        queryPosition = 0;
        viewPosition = position;
        //mChannelPlayUrl = fragment.getChannelUrl();
        playTime = fragment.getPlayTimeByPosition(viewPosition);
        playPostion = fragment.getPlayPosition(viewPosition);

        mDataMap = new ArrayList<>();

        /**
         * 读取Element中的ActionUrl-->contentID，拿到频道ID，
         * 通过频道ID去所有频道中find  Schedule，将Schedule放入list集合
         * */
        for (int i = 0; i < elements.size(); i++) {
            mIsCanPlay.put(i,true);
            if (TextUtils.isEmpty(elements.get(i).getElementDataList().get(0).getElementAction().getActionURL()))break;
            String contentType = ZJVRoute.getContentValue(elements.get(i).getElementDataList().get(0).getElementAction().getActionURL(),
                    ZJVRoute.ActionUrlKeyType.CONTENT_TYPE);
            String contentId = ZJVRoute.getContentValue(elements.get(i).getElementDataList().get(0).getElementAction().getActionURL(),
                    ZJVRoute.ActionUrlKeyType.CONTENT_ID);
            String forceDefaultDate = elements.get(i).getForceDefaultData();
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put(CONTENT_ACTION_URL,elements.get(i).getElementDataList().get(0).getElementAction().getActionURL());
            hashMap.put(CONTENT_TYPE,contentType);
            hashMap.put(CONTENT_ID,contentId);
            hashMap.put(FORCE_DEFAULT_DATA,forceDefaultDate);
            if (contentType.equals(CONTENT_TYPE_LIVE)){
                Schedule schedule = LiveUtils.findScheduleById(contentId);
                if (null != schedule) {
                    hashMap.put(CONTENT_MEDIA_ID,schedule.getMediaID());
                    hashMap.put(CONTENT_NAME,schedule.getName());
                    setTextName(i,hashMap.get(CONTENT_NAME));
                    mDataMap.add(hashMap);
                    /*if (i == 0){
                        getChannelUrl(hashMap,i,false,true);
                    }*/
                }
            }else if (contentType.equals(CONTENT_TYPE_VOD)){
                mDataMap.add(hashMap);
                getVODDetail(contentId,i,hashMap);
            }
        }
        //queryUrl(0);
    }

    private void queryUrl(int index){
        if (mDataMap.size() > 4 || index > 3 || index >= mDataMap.size())return;
        if (mDataMap.get(index).get(CONTENT_TYPE).equals(CONTENT_TYPE_LIVE)){
            getChannelUrl((HashMap<String, String>) mDataMap.get(index),index,true,false);
        }else if (mDataMap.get(index).get(CONTENT_TYPE).equals(CONTENT_TYPE_VOD)){
            getVODUrl(mVodDetailMap.get(index),index,true,false);
        }
    }

    //设置标题
    private void setTextName(int index,String name){
        RelativeLayout view = views.get(index);
        view.setOnFocusChangeListener(this);
        view.setTag(index);
        view.setOnClickListener(this);
        TextView textView = (TextView) view.getChildAt(1);
        if (!TextUtils.isEmpty(name)) {
            textView.setText(name);
        } else {
            textView.setText("");
        }
    }

    /**
     * 加载海报图片
     *
     * @param vod
     */
    private void setPoster(VOD vod) {
        if (VISIBLE != ivPoster.getVisibility()) {
            ivPoster.setVisibility(VISIBLE);
        }
        Picture picture = vod.getPicture();
        String imgUrl = "";
        if (null != picture) {
            List<String> adList = picture.getAds();
            if (adList != null && adList.size() > 0) {
                imgUrl = adList.get(0).toString();
            }
            //如果上面都没取到，则去posters去取
            if (TextUtils.isEmpty(imgUrl)) {
                //DebugLog.debug(TAG, "海报");
                List<String> posterList = picture.getPosters();
                if (posterList != null && posterList.size() > 0) {
                    imgUrl = posterList.get(0).toString();
                }
            }
        }
        RequestOptions options  = new RequestOptions()
                .placeholder(R.drawable.default_poster_bg)
                .error(R.drawable.default_poster_bg);

        Glide.with(context).load(imgUrl).apply(options).into(ivPoster);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        onFocusChangeListener.onFocusChange(v, hasFocus);
        if (hasFocus) {
            ((RelativeLayout) v).getChildAt(1).setSelected(true);
        } else {
            ((RelativeLayout) v).getChildAt(1).setSelected(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (R.id.pan_l1_r4_left == v.getId() && playPostion != -1) {
            if (null == mDataMap || mDataMap.size() <= playPostion)return;
            Map<String, String> map = mDataMap.get(playPostion);
            if (null != map && !TextUtils.isEmpty(map.get(CONTENT_TYPE))) {
                //跳转到直播
                if (map.get(CONTENT_TYPE).equals(CONTENT_TYPE_LIVE)) {
                    Intent intent = new Intent(getContext(), LiveTVActivity.class);
                    //记录准备播放的频道ID和mediaID
                    if (!TextUtils.isEmpty(map.get(CONTENT_ID)) && !TextUtils.isEmpty(map.get(CONTENT_MEDIA_ID))) {

                        if (!TextUtils.isEmpty(ZJVRoute.getContentValue(map.get(CONTENT_ACTION_URL), ZJVRoute.ActionUrlKeyType.CHANNEL_MINI_TYPE))
                                && ZJVRoute.getContentValue(map.get(CONTENT_ACTION_URL), ZJVRoute.ActionUrlKeyType.CHANNEL_MINI_TYPE).equals("-1")){
                            //true:直播只播放一个频道，不能切台、不显示MiniEpg
                            intent.putExtra(LiveTVActivity.CHANNEL_FINAL, true);
                            //用于临时记录直播播放固定频道的channelID、MediaId
                            LiveDataHolder.get().setChannelAndMediaId(map.get(CONTENT_ID), map.get(CONTENT_MEDIA_ID));
                            LiveDataHolder.get().setIsShowingSkip(true);
                        }else{
                            //本地没有缓存的频道信息,不传,后面有做默认处理
                            LiveTVCacheUtil.getInstance().recordPlayChannelInfo(map.get(CONTENT_ID), map.get(CONTENT_MEDIA_ID));
                        }

                    } else {
                        //contentId或者media为空，则代表播放失败。不进行跳转
                        return;
                    }
                    intent.putExtra(LiveTVActivity.VIDEO_TYPE, LiveTVActivity.VIDEO_TYPE_LIVETV);
                    getContext().startActivity(intent);

                    UBDSwitch.getInstance().recordMainOnclick(mDataMap.get(playPostion).get(CONTENT_ACTION_URL),mElements.get(playPostion),mGroup,null,null);

                } else if (map.get(CONTENT_TYPE).equals(CONTENT_TYPE_VOD)) {
                    //跳转到VOD
                    if (TextUtils.isEmpty(map.get(CONTENT_ID)) || TextUtils.isEmpty(map.get(CONTENT_NAME))
                            //|| TextUtils.isEmpty(mPlayUrlList.get(playPostion)))
                            || null == mPlayUrlMap.get(playPostion) || TextUtils.isEmpty(mPlayUrlMap.get(playPostion)))
                        return;
                    Intent intent = new Intent(getContext(), OnDemandVideoActivity.class);
                    PlayVodBean bean = new PlayVodBean();
                    bean.setVodId(map.get(CONTENT_ID));
                    bean.setVodName(map.get(CONTENT_NAME));
                    bean.setPlayUrl(mPlayUrlMap.get(playPostion));
//                    String vodBeanStr = JsonParse.object2String(bean);
//                    intent.putExtra(OnDemandVideoActivity.PLAY_VOD_BEAN, vodBeanStr);
                    LiveDataHolder.get().setPlayVodBean(JsonParse.object2String(bean));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);

                    UBDSwitch.getInstance().recordMainOnclick("",mElements.get(playPostion),mGroup,mVodDetailMap.get(playPostion),null);
                }
            }

        }else if (isAutoPlay()){
            playPostion = (int) v.getTag();
            fragment.addPlayPosition(viewPosition, playPostion);
            setIsCanPlay(true);
            setPlayIcon(playPostion);
            if (null != mPlayUrlMap && playPostion < mPlayUrlMap.size() && !TextUtils.isEmpty(mPlayUrlMap.get(playPostion))) {
                startPlay(mPlayUrlMap.get(playPostion));
            } else if (playPostion < mDataMap.size()){
                if (mDataMap.get(playPostion).get(CONTENT_TYPE).equals(CONTENT_TYPE_LIVE)) {
                    getChannelUrl((HashMap<String, String>) mDataMap.get(playPostion), playPostion, false, true);
                } else if (mDataMap.get(playPostion).get(CONTENT_TYPE).equals(CONTENT_TYPE_VOD)) {
                    getVODUrl(mVodDetailMap.get(playPostion), playPostion, false, true);
                }
            }
        }
    }

    private void hideAllIcon() {
        for (int i = 0; i < views.size(); i++) {
            (views.get(i)).getChildAt(0).setVisibility(INVISIBLE);
        }
    }

    private void startPlay(String url) {

        if (fragment.getActivity() instanceof MainActivity && !((MainActivity)fragment.getActivity()).isStartPicFinished()){
            return;
        }

        //延时isViewVisiable()返回
        videoView.releasePlayer();
        videoView.setResizeMode(PlayView.RESIZE_MODE_FIT);

        if(playHandler.hasMessages(1)){
            playHandler.removeMessages(1);
        }
        Message msg = new Message();
        msg.what = 1;
        msg.obj = url;
        playHandler.sendMessageDelayed(msg,300);

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //控件不可见时不播放
                if (!isViewVisiable() || !PlayerUtils.isVisibleALL(ivPoster)) {
                    return;
                }
                *//*videoView.releasePlayer();
                videoView.setResizeMode(PlayView.RESIZE_MODE_FIT);*//*
                if(null!=fragment&&!fragment.isNotCanPlayNow())
                {
                    if (fragment.getUserVisibleHint())
                    {
                        if (!TextUtils.isEmpty(url))
                        {
                            //setViewFocusPlayNext();
                            videoView.startPlay(url);
                        }
                        else
                        {
                            playNextVideo();
                        }
                    }
                }
            }
        },300);*/
    }

    private void setPlayIcon(int position) {
        hideAllIcon();
        if (position < views.size()){
            views.get(position).getChildAt(0).setVisibility(VISIBLE);
            setViewFocusPlayNext();
        }
    }

    private Handler mVideoTemplateHandler = new Handler();

    @Subscribe
    public void onEvent(VideoLivePlayTimeEvent event){

        if (event.getLayoutId() != -1){
            mCanPlay = true;
            mVideoTemplateHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!mCanPlay)return;
                    TextView textView = (TextView) ((RelativeLayout)views.get(getViewFocusIndex(event.getLayoutId()))).getChildAt(1);
                    if (TextUtils.isEmpty(textView.getText())){
                        if (null != ivPoster && ivPoster.getVisibility() == GONE){
                            stopPlayAndShowPostImg();
                        }else if (null != videoView){
                            videoView.releasePlayer();
                        }
                    }
                    if (playPostion != getViewFocusIndex(event.getLayoutId()) && !TextUtils.isEmpty(textView.getText())){
                        //startPlayAndHideShowPostImg();
                        getPlayUrlByPosition(getViewFocusIndex(event.getLayoutId()));
                    }
                }
            },mAutoPlaymauTime);
        }else{
            mCanPlay = false;
            if (null != ivPoster && ivPoster.getVisibility() == GONE){
                stopPlayAndShowPostImg();
            }else if (null != videoView){
                videoView.releasePlayer();
            }
        }
    }

    public void scrollEndToPlay(){
        //如果不是全屏显示，则展示海报，并return
        if (!PlayerUtils.isVisibleALL(ivPoster)){
            if (videoView.getVisibility() == VISIBLE){
                stopPlayAndShowPostImg();
            }
            return;
        }else if (videoView.getVisibility() == VISIBLE){//如果是全屏显示，切正在播放，return
            return;
        }
        if (isAutoPlay()){
            getPlayUrlByPosition(playPostion == -1 ? 0 : playPostion);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        mPlayType = SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.MAIN_VIDEO_PLAY_TYPE);
        SuperLog.debug(TAG,"mPlayType="+mPlayType+"(=1或者为null，落焦播放；=2代表自动播放)");
        if (null != getFragment()){
            getFragment().setVideoLiveTemplate(this);
            getFragment().setIsShowVideoView(true);
        }
        if(isAutoPlay()){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null != videoView && PlayerUtils.isVisibleALL(ivPoster)){
                        getPlayUrlByPosition(playPostion == -1 ? 0 : playPostion);
                    }
                }
            },300);

        }else{
            setPlayIcon(playPostion == -1 ? 0 : playPostion);
            stopPlayAndShowPostImg();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        mVideoTemplateHandler.removeCallbacksAndMessages(null);
        playHandler.removeCallbacksAndMessages(null);
        if (null != videoView){
            videoView.releasePlayer();
        }
        if (null != getFragment()){
            getFragment().setVideoLiveTemplate(null);
            getFragment().setIsShowVideoView(false);
        }
        super.onDetachedFromWindow();
    }

    @Override
    public View getLastView() {
        return views.get(3);
    }

    public void startPlayVideo() {

        if (null == mPlayUrlMap || mPlayUrlMap.size() == 0) {
            return;
        //}else if (playPostion >= mPlayUrlMap.size())playPostion=0;
        }else if (!mPlayUrlMap.containsKey(playPostion))playPostion=0;
        setPlayIcon(playPostion);
        startPlay(mPlayUrlMap.get(playPostion));
        startTimeFullScreenIv();

    }

    //直播鉴权，获取直播、播放地址
    //private void getChannelUrl(int position)
    private void getChannelUrl(HashMap<String,String> hashMap,int position,boolean isGoOn,boolean isPlay)
    {
        PlayChannelRequest playChannelRequest = new PlayChannelRequest();
        playChannelRequest.setChannelID(hashMap.get(CONTENT_ID));
        playChannelRequest.setMediaID(hashMap.get(CONTENT_MEDIA_ID));
        playChannelRequest.setBusinessType(V6Constant.ChannelURLType.BTV);
        playChannelRequest.setURLFormat("1");//HLS
        fragment.getPresenter().playChannel(playChannelRequest, new
                RxCallBack<PlayChannelResponse>(HttpConstant.PLAYCHANNEL, getContext())
        {
            @Override
            public void onSuccess(PlayChannelResponse playChannelResponse)
            {
                String playUrl = StringUtils.splicingPlayUrl(playChannelResponse.getPlayURL());
                mPlayUrlMap.put(position,playUrl);
                //播放
                if (isAutoPlay()){
                    if (null != videoView && PlayerUtils.isVisibleALL(ivPoster)){
                        if (position == playPostion || isPlay) {//只有第一个自动播放
                            setPlayIcon(position);
                            startPlay(playUrl);
                        }
                    }
                }else{
                    startPlay(playUrl);
                }
            }

            @Override
            public void onFail(Throwable e)
            {
                /*if (isGoOn)
                queryUrl(position+1);*/
            }
        });
    }

    /**
     */
    private void getVODDetail(String vodId,int position,HashMap<String,String> hashMap) {
        fragment.getPresenter().getVODDetail(vodId, new RxCallBack<GetVODDetailResponse>(HttpConstant.GETVODDETAIL, getContext()) {
            @Override
            public void onSuccess(GetVODDetailResponse getVODDetailResponse) {
                if (null == getVODDetailResponse || null == getVODDetailResponse.getVODDetail())return;
                setTextName(position,getVODDetailResponse.getVODDetail().getName());
                if (null != hashMap){
                    hashMap.put(CONTENT_NAME,getVODDetailResponse.getVODDetail().getName());
                    if (mDataMap.size() > position){
                        mDataMap.remove(position);
                    }
                    if (position > mDataMap.size()){
                        mDataMap.add(hashMap);
                    }else{
                        mDataMap.add(position,hashMap);
                    }
                }
                mVodDetailMap.put(position,getVODDetailResponse.getVODDetail());
                if (position == playPostion)
                getVODUrl(getVODDetailResponse.getVODDetail(),position,false,false);
            }

            @Override
            public void onFail(Throwable e) {
                SuperLog.debug(TAG, "getVODDetail failed :" + e.toString());
            }
        });


    }

    private void getVODUrl(VODDetail detail,int position,boolean isGoOn,boolean isPlay) {
        if (null != detail) {
            //List<VODMediaFile> vodMediaFiles = detail.getMediaFiles();
            VODDetail vodDetail = null;
            if (detail.getVODType().equals("1")) {//电视剧
                vodDetail = playEpisodes(detail);
                mVodDetailMap.put(position, detail);
            }
            fragment.getPresenter().getPlayUrl(null == vodDetail ? detail : vodDetail, new RxCallBack<PlayVODResponse>(HttpConstant.PLAYVOD, getContext()) {
                @Override
                public void onSuccess(PlayVODResponse playVODResponse) {
                    mVodPlayUrl = StringUtils.splicingPlayUrl(playVODResponse.getPlayURL());
                    mPlayUrlMap.put(position, mVodPlayUrl);
                    //播放
                    if (isAutoPlay()) {
                        if (null != videoView && PlayerUtils.isVisibleALL(ivPoster)){
                            if (position == playPostion || isPlay) {//只有第一个自动播放
                                setPlayIcon(position);
                                startPlay(mVodPlayUrl);
                            }
                        }
                    }else{
                        startPlay(mVodPlayUrl);
                    }
                }

                @Override
                public void onFail(Throwable e) {
                /*if (isGoOn)
                    queryUrl(position + 1);*/
                }
            });
        }
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

    //用于处理播放失败时，循环一遍处理
    private void setIsCanPlay(boolean b){
        for(Integer key : mIsCanPlay.keySet()){
            mIsCanPlay.put(key,b);
        }
    }

    //根据落焦Id 确定播放第几个
    private int getViewFocusIndex(int id){
        if (id == R.id.rl_pan_l1_r4_item0){
            return 0;
        }else if (id == R.id.rl_pan_l1_r4_item1){
            return 1;
        }else if (id == R.id.rl_pan_l1_r4_item2){
            return 2;
        }else if (id == R.id.rl_pan_l1_r4_item3){
            return 3;
        }
        return -1;
    }

    public int getPlayPostion(){
        return playPostion < 0 ? 0 : playPostion;
    }

    private void getPlayUrlByPosition(int position){
        startPlayAndHideShowPostImg();
        playPostion = position;
        fragment.addPlayPosition(viewPosition, playPostion);
        setIsCanPlay(true);
        setPlayIcon(playPostion);
        if (null != mPlayUrlMap && playPostion < mPlayUrlMap.size() && !TextUtils.isEmpty(mPlayUrlMap.get(playPostion))) {
            startPlay(mPlayUrlMap.get(playPostion));
        } else if (playPostion < mDataMap.size()){
            if (mDataMap.get(playPostion).get(CONTENT_TYPE).equals(CONTENT_TYPE_LIVE)) {
                getChannelUrl((HashMap<String, String>) mDataMap.get(playPostion), playPostion, false, true);
            } else if (mDataMap.get(playPostion).get(CONTENT_TYPE).equals(CONTENT_TYPE_VOD)) {
                getVODUrl(mVodDetailMap.get(playPostion), playPostion, false, true);
            }
        }
    }

    private void stopPlayAndShowPostImg(){

        ivPoster.setVisibility(VISIBLE);
        setImagePost();
        //playPostion = -1;

        ivFullScreen.setVisibility(View.GONE);
        if (null != videoView){
            videoView.setVisibility(GONE);
            videoView.releasePlayer();
        }

        cancleTimer();
    }

    private void startPlayAndHideShowPostImg(){
        if (null != videoView){
            videoView.setVisibility(VISIBLE);
        }
        ivPoster.setVisibility(GONE);
    }
    int position = 0;
    //展示海报
    private void setImagePost(){
        if (playPostion != -1){
            position = playPostion;
        }else if (playPostion == -1){
            position = 0;
        }
        if (mDataMap.size() > 4 || position > 3 || position >= mDataMap.size() || position == -1)return;
        if (mDataMap.get(position).get(CONTENT_TYPE).equals(CONTENT_TYPE_LIVE) ||
                (!TextUtils.isEmpty(mDataMap.get(position).get(FORCE_DEFAULT_DATA)) && mDataMap.get(position).get(FORCE_DEFAULT_DATA).equals("true"))){
            setImagePostStatic(position);
        }else if (mDataMap.get(position).get(CONTENT_TYPE).equals(CONTENT_TYPE_VOD)){
            if (null != mVodDetailMap && mVodDetailMap.containsKey(position))
            setPoster(mVodDetailMap.get(position));
        }
    }

    private void setImagePostStatic(int position){
        if (null == mElements || mElements.size() == 0 || position == -1 || position >= mElements.size())return;
        String contentURL = mElements.get(position).getElementDataList().get(0).getContentURL();
        RequestOptions options  = new RequestOptions()
                .placeholder(R.drawable.default_poster_bg)
                .error(R.drawable.default_poster_bg);

        if (!TextUtils.isEmpty(contentURL)) {
            if (contentURL.contains("/") || contentURL.contains("http")) {
                try {
                    String launcherLink = SharedPreferenceUtil.getInstance().getLauncherLink();
                    if (!TextUtils.isEmpty(launcherLink)) {
                        contentURL = "http://" + AuthenticateManager.getInstance().getUserInfo()
                                .getIP() + ":" + AuthenticateManager.getInstance().getUserInfo()
                                .getPort() + launcherLink + GlideUtil.getUrl(contentURL);
                        Glide.with(context).load(contentURL).apply(options).into(ivPoster);
                    }
                } catch (Exception ex) {
                    SuperLog.error(TAG,ex);
                }
            }
        }else{
            Glide.with(context).load(contentURL).apply(options).into(ivPoster);
        }
    }

    //视频播放完成，播放下一视频，焦点框跟着移动
    private void setViewFocusPlayNext(){
        if (isPlayComplete){
            isPlayComplete = false;
            if (null != videoContainer && !videoContainer.hasFocus() && null != views && null != views.get(playPostion) && !views.get(playPostion).hasFocus()){
                views.get(playPostion).requestFocus();
            }
        }
    }

    //解决全屏提示常驻显示
    //private Handler handler = new Handler();

    private void startTimeFullScreenIv(){
        if (null == ivFullScreen || ivFullScreen.getVisibility() == VISIBLE)return;
        ivFullScreen.setVisibility(VISIBLE);
        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (null != ivFullScreen){
                    ivFullScreen.setVisibility(GONE);
                }
            }
        },10000);*/
    }

    private void cancleTimer(){
        //handler.removeCallbacksAndMessages(null);
        if(null != ivFullScreen && ivFullScreen.getVisibility() == VISIBLE){
            ivFullScreen.setVisibility(GONE);
        }
    }

    public void hasFocusToPlay(int layoutId,boolean isPlay,boolean isNewFocus){
        if (isAutoPlay()){
            return;
        }
        if (isPlay){
            mCanPlay = true;
            mVideoTemplateHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!mCanPlay)return;
                    TextView textView = (TextView) ((RelativeLayout)views.get(getViewFocusIndex(layoutId))).getChildAt(1);
                    if (TextUtils.isEmpty(textView.getText())){
                        mCanPlay = false;
                        if (null != ivPoster && ivPoster.getVisibility() == GONE){
                            stopPlayAndShowPostImg();
                        }else if (null != videoView){
                            videoView.releasePlayer();
                        }
                    }
                    if ((playPostion != getViewFocusIndex(layoutId) || ivPoster.getVisibility() == VISIBLE) && !TextUtils.isEmpty(textView.getText())){
                        cancleTimer();
                        //startPlayAndHideShowPostImg();
                        if (playHandler.hasMessages(0)){
                            playHandler.removeMessages(0);
                        }
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = layoutId;
                        playHandler.sendMessageDelayed(msg,300);
                        //getPlayUrlByPosition(getViewFocusIndex(layoutId));
                    }else{
                        cancleTimer();
                    }
                }
            },mAutoPlaymauTime);
        }else if (layoutId == R.id.pan_l1_r4_left && isNewFocus){
            if (mCanPlay){
                cancleTimer();
                startTimeFullScreenIv();
            }
        }else{
            mCanPlay = false;

            if (playHandler.hasMessages(0)){
                SuperLog.debug(TAG,"playHandler.hasMessages(0)"+playHandler.hasMessages(0));
                playHandler.removeMessages(0);
            }

            if (null != ivPoster && ivPoster.getVisibility() == GONE){
                stopPlayAndShowPostImg();
            }else if (null != videoView){
                videoView.releasePlayer();
            }
            cancleTimer();
        }
    }

    //true:自动播放;false:落焦播放
    private boolean isAutoPlay(){
        if (!TextUtils.isEmpty(mPlayType) && mPlayType.equalsIgnoreCase("2")){
            return true;
        }else{
            return false;
        }
    }

    //解决从第一个快速按下键，焦点落在第三个，播放的是第二个
    @SuppressLint("HandlerLeak")
    private Handler playHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){

                getPlayUrlByPosition(getViewFocusIndex((Integer) msg.obj));

            }else if (msg.what == 1){

                //控件不可见时不播放
                if (!isViewVisiable() || !PlayerUtils.isVisibleALL(ivPoster)) {
                    return;
                }
                if(null!=fragment&&!fragment.isNotCanPlayNow())
                {
                    if (fragment.getUserVisibleHint())
                    {
                        if (!TextUtils.isEmpty((String)msg.obj))
                        {
                            //setViewFocusPlayNext();
                            videoView.startPlay((String)msg.obj);
                        }
                        else
                        {
                            playNextVideo();
                        }
                    }
                }
            }
        }
    };

}
