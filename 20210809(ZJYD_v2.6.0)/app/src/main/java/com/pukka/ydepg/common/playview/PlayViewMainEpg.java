package com.pukka.ydepg.common.playview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.Player;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.extview.RelativeLayoutExt;
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
import com.pukka.ydepg.common.report.ubd.scene.UBDSwitch;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.customui.tv.PlayViewWindow;
import com.pukka.ydepg.inf.IPlayListener;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.ui.template.FreeLayoutTemplate;
import com.pukka.ydepg.launcher.ui.template.PHMTemplate;
import com.pukka.ydepg.launcher.ui.template.VideoLiveTemplate;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.livetv.utils.LiveTVCacheUtil;
import com.pukka.ydepg.moudule.livetv.utils.LiveUtils;
import com.pukka.ydepg.moudule.mytv.utils.MessageDataHolder;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;
import com.pukka.ydepg.moudule.player.ui.OnDemandVideoActivity;
import com.pukka.ydepg.moudule.player.util.PlayerUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.view.View.VISIBLE;

/**
 * ???????????????????????????????????????????????????
 * */
public class PlayViewMainEpg {

    private static final String TAG = PlayViewMainEpg.class.getSimpleName();

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'.00Z'");//20190128T150000.00z
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat mDateFormatForJian8 = new SimpleDateFormat("yyyyMMddHHmmss");//20190128T150000.00z

    private static final String ONE_TIME = "one_time";

    /**
     * ??????????????????
     * one_time:??????????????????
     * true:????????????????????????
     * */
    private String mLoopType = "one_time";

    //0:???????????????1:????????????
    private int mPlayMode = 0;

    //1=?????????2=??????;3=??????
    private String mPlayType = "";
    private static final String LIVE_TYPE = "1";
    private static final String VOD_TYPE = "2";
    private static final String CATCH_UP_TYPE = "3";

    //????????????
    private static final int PLAY_AUTO = 0;
    //????????????
    private static final int PLAY_FOCUS = 1;

    private PlayViewWindow mPlayView;
    private RelativeLayoutExt mRlPV;
    private Context mContext;
    private Group mGroup;
    private Element mElement;
    private PHMFragment mFragment;
    private ReflectRelativeLayout mReflectRelativeLayout;

    private String mStartTime;
    private String mEndTime;
    private String mSwitchingTime;
    private String contentType;
    private String contentId;
    private String mFourkContentId;
    private String mMediaId;
    private String mActionUrl;

    //????????????
    private String mPlayUrl;
    //VOD????????????
    private long mDuration = 0;

    //?????????????????????
    private boolean isOnclick = false;

    public PlayViewMainEpg(PlayViewWindow playViewWindow, RelativeLayoutExt rlPV,Context context, ReflectRelativeLayout reflectRelativeLayout, Group group, Element element, PHMFragment fragment) {
        mPlayView = playViewWindow;
        mRlPV = rlPV;
        mContext = context;
        mGroup = group;
        mElement = element;
        mFragment = fragment;
        mReflectRelativeLayout = reflectRelativeLayout;
        if (null != mFragment){
            mFragment.setVideoTemplate(this,mReflectRelativeLayout.getNavIndex());
        }
        initView();
        loadData();
    }

    private void initView() {
        mPlayView.setZOrderOnTop(true);
        if (!TextUtils.isEmpty(mReflectRelativeLayout.getExtraData().get(Constant.AUTO_PLAY))){
            mLoopType = mReflectRelativeLayout.getExtraData().get(Constant.AUTO_PLAY);
        }
        if (null != mReflectRelativeLayout.getExtraData() && mReflectRelativeLayout.getExtraData().containsKey(Constant.PLAY_MODE) && !TextUtils.isEmpty(mReflectRelativeLayout.getExtraData().get(Constant.PLAY_MODE))){
            mPlayMode = Integer.parseInt(mReflectRelativeLayout.getExtraData().get(Constant.PLAY_MODE));
        }
        SuperLog.debug(TAG,"mPlayMode = "+ mPlayMode);
        mPlayView.setOnPlayCallback(new IPlayListener() {
            @Override
            public void onPlayState(int playbackState) {
                SuperLog.debug(TAG,"[PlayCallback]....playbackState="+playbackState);
                //?????????????????????????????????????????????????????????????????????
                if (playbackState == Player.STATE_READY){
                    mFragment.setIsShowVideoView(true);

                }else if (playbackState == Player.STATE_ENDED){
                    mFragment.setIsShowVideoView(false);
                }else if (playbackState == Player.STATE_IDLE){
                    stopPlay(false);
                }
            }

            @Override
            public void onPrepared(int Videotype) {
                SuperLog.debug(TAG,"[PlayCallback]....onPrepared()__Videotype="+Videotype);
            }

            @Override
            public void onRelease() {
                SuperLog.debug(TAG,"[PlayCallback]....onRelease()");
            }

            @Override
            public void onPlayError(String msg, int errorCode, int playerType) {
                SuperLog.debug(TAG,"[PlayCallback]....onPlayError()__msg="+msg+";errorCode="+errorCode+";playerType="+playerType);
            }

            @Override
            public void onPlayCompleted() {
                SuperLog.debug(TAG,"[PlayCallback]....onPlayCompleted()");
                if (mLoopType.equals("true")){
                    mPlayView.releasePlayer();
                    startPlay();
                }else{
                    recordPlayCompleteForOnePlayModel();
                    stopPlay(false);
                }

                mDuration = 0;
                mFragment.recordDuration(mReflectRelativeLayout.getNavIndex(),mDuration);
            }

            @Override
            public void onDetached(long time) {
                //mFragment.recordDuration(mReflectRelativeLayout.getNavIndex(),mPlayView == null ? 0 : mPlayView.getCurrentPosition());
                SuperLog.debug(TAG,"[PlayCallback]....onDetached()---time="+time+";"+"duration="+(mPlayView == null ? 0 : mPlayView.getCurrentPosition()));
            }

            @Override
            public void onAttached() {
                SuperLog.debug(TAG,"[PlayCallback]....onAttached()");
            }

            @Override
            public void onTryPlayForH5() {

            }

            @Override
            public void onAdVideoEnd() {

            }
        });
    }

    //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????? ??????????????????
    private void recordPlayCompleteForOnePlayModel(){
        int position = mReflectRelativeLayout.getNavIndex();
        mFragment.setIsPlayCompleteMap(position,true);
    }

    private void loadData() {
        if (mReflectRelativeLayout.isDefaultData()){
            //????????????
            setStaticData();
        }else{
            //????????????
            playVod(mReflectRelativeLayout.getVodData());
        }
    }

    private void playVod(VOD vodData) {
        if (null != vodData) {
            /*if (!TextUtils.isEmpty(mFourkContentId)){
                vodData.setID(mFourkContentId);
            }*/
            getVODDetail(vodData);
        }else{
            SuperLog.error(TAG,"?????????????????????????????????????????????");
        }
    }

    //???????????????????????????????????????????????????????????????
    private void setStaticData(){
        //navIndex = mReflectRelativeLayout.getNavIndex();
        if (null == mReflectRelativeLayout.getElementData() || null == mReflectRelativeLayout.getElementData().getElementAction()
            || TextUtils.isEmpty(mReflectRelativeLayout.getElementData().getElementAction().getActionURL())){
            SuperLog.info2SD(TAG,"ActionURL=null,return");
            return;
        }
        mActionUrl = mReflectRelativeLayout.getElementData().getElementAction().getActionURL();
        //mActionUrl = "ContentCode=&ContentID=42329898&ContentType=CHANNEL&Action=View";
        //mActionUrl = "ContentCode=&ContentID=46707373&ContentType=STATICVOD&Action=View";//243857269
        //mActionUrl = "ContentCode=&ContentID=231907064&ContentType=STATICVOD&Action=View";//243857269
        contentType = ZJVRoute.getContentValue(mActionUrl,
                ZJVRoute.ActionUrlKeyType.CONTENT_TYPE);
        contentId = ZJVRoute.getContentValue(mActionUrl,
                ZJVRoute.ActionUrlKeyType.CONTENT_ID);
        mFourkContentId = ZJVRoute.getContentValue(mActionUrl,
                ZJVRoute.ActionUrlKeyType.FOURK_CONTENT_ID);
        mStartTime = ZJVRoute.getContentValue(mActionUrl,
                ZJVRoute.ActionUrlKeyType.START_TIME);
        mEndTime = ZJVRoute.getContentValue(mActionUrl,
                ZJVRoute.ActionUrlKeyType.END_TIME);
        String name = mReflectRelativeLayout.getTitleText();
        mSwitchingTime = ZJVRoute.getContentValue(mActionUrl,
                ZJVRoute.ActionUrlKeyType.SWITCHING_TIME);

        if (!TextUtils.isEmpty(mStartTime) && !TextUtils.isEmpty(formatTime(mStartTime))){
            mStartTime = formatTime(mStartTime);
        }
        if (!TextUtils.isEmpty(mEndTime) && !TextUtils.isEmpty(formatTime(mEndTime))){
            mEndTime = formatTime(mEndTime);
        }

        SuperLog.info2SD(TAG,"SwitchingTime="+mSwitchingTime+";StartTime="+ZJVRoute.getContentValue(mActionUrl,
                ZJVRoute.ActionUrlKeyType.START_TIME)+";EndTime="+ZJVRoute.getContentValue(mActionUrl,
                ZJVRoute.ActionUrlKeyType.END_TIME)+
                ";formatTime(mStartTime)="+mStartTime+";formatTime(mEndTime)="+mEndTime+";------SwitchingTime???????????????20181213T092741.00Z;actionUrl="+mActionUrl+";VodUtil.isDeviceSupport4K()="+ VodUtil.isDeviceSupport4K());
        if (contentType.equals(VideoLiveTemplate.CONTENT_TYPE_LIVE)){
            mPlayType = LIVE_TYPE;
            ChannelDetail channelDetail;
            //???4K_contentID ????????????4K
            if (!TextUtils.isEmpty(mFourkContentId) && null != LiveUtils.findScheduleFromCanPlayById(mFourkContentId)){
                channelDetail = LiveUtils.findScheduleFromCanPlayById(mFourkContentId);
            }else{
                channelDetail = LiveUtils.findScheduleFromCanPlayById(contentId);
            }
            if (null != channelDetail) {
                mMediaId = LiveUtils.parseMediaID(channelDetail);
                getChannelUrl(channelDetail.getID(),mMediaId);
            }
        }else if (contentType.equals(VideoLiveTemplate.CONTENT_TYPE_VOD)){
            mPlayType = VOD_TYPE;
            VOD currentVod = new VOD();
            if (VodUtil.isDeviceSupport4K() && !TextUtils.isEmpty(mFourkContentId)){
                currentVod.setID(mFourkContentId);
            }else{
                currentVod.setID(contentId);
            }
            getVODDetail(currentVod);
        }
    }

    //??????????????????????????????????????????
    private void getChannelUrl(String contentId,String meidaId) {
        PlayChannelRequest playChannelRequest = new PlayChannelRequest();
        playChannelRequest.setChannelID(contentId);
        playChannelRequest.setMediaID(meidaId);
        playChannelRequest.setBusinessType(V6Constant.ChannelURLType.BTV);
        playChannelRequest.setURLFormat("1");//HLS
        mFragment.getPresenter().playChannel(playChannelRequest, new
                RxCallBack<PlayChannelResponse>(HttpConstant.PLAYCHANNEL, mContext) {
                    @Override
                    public void onSuccess(PlayChannelResponse playChannelResponse) {
                        if (!TextUtils.isEmpty(playChannelResponse.getPlayURL())) {
                            mPlayUrl = StringUtils.splicingPlayUrl(playChannelResponse.getPlayURL());
                            mPlayUrl = setTvodPlayUrl(mPlayUrl);
                            mReflectRelativeLayout.setPlayUrl(mPlayUrl);
                            SuperLog.info2SD(TAG,"playUrl="+mPlayUrl);
                            if (mFragment.getUserVisibleHint()) {
                                startPlay();
                            }
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                    }
                });
    }

    /**
     * @param vod
     */
    private void getVODDetail(VOD vod) {
        mFragment.getPresenter().getVODDetail(vod.getID(), new RxCallBack<GetVODDetailResponse>(HttpConstant.GETVODDETAIL, mContext) {
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
        if (detail.getVODType().equals("1")) {//?????????
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
        mFragment.getPresenter().getPlayUrl(null == vodDetail ? detail : vodDetail, new RxCallBack<PlayVODResponse>(HttpConstant.PLAYVOD, mContext) {
            @Override
            public void onSuccess(PlayVODResponse playVODResponse) {
                if (!TextUtils.isEmpty(playVODResponse.getPlayURL())) {
                    mPlayUrl = StringUtils.splicingPlayUrl(playVODResponse.getPlayURL());
                    mReflectRelativeLayout.setPlayUrl(mPlayUrl);
                    SuperLog.info2SD(TAG,"playUrl="+mPlayUrl);
                    if (mFragment.getUserVisibleHint()) {
                        startPlay();
                    }
                }
            }

            @Override
            public void onFail(Throwable e) {
            }
        });
    }

    //???????????????
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

    private String setTvodPlayUrl(String playUrl){
        String url = "";
        if (mPlayType.equalsIgnoreCase(LIVE_TYPE) && ((!TextUtils.isEmpty(mSwitchingTime) && (mSwitchingTime.equals(mDateFormat.format(new Date())) || isNewDateBeforeSwitchTime(mSwitchingTime))
                && !TextUtils.isEmpty(mStartTime) && !TextUtils.isEmpty(mEndTime)) || (TextUtils.isEmpty(mSwitchingTime) && !TextUtils.isEmpty(mStartTime) && !TextUtils.isEmpty(mEndTime)))) {
            mPlayType = CATCH_UP_TYPE;
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

    public void startPlay() {

        if (mLoopType.equalsIgnoreCase(ONE_TIME) && mFragment.isPlayComplete(mReflectRelativeLayout.getNavIndex())){
            SuperLog.info2SD(TAG,"????????????????????????????????????????????????????????????????????????????????????");
            return;
        }

        mDuration = mFragment.getDurationByPosition(mReflectRelativeLayout.getNavIndex());
        mPlayUrl = mReflectRelativeLayout.getPlayUrl();

        if (TextUtils.isEmpty(mPlayUrl) || (mFragment.getActivity() instanceof MainActivity && !((MainActivity) mFragment.getActivity()).isStartPicFinished())) {
            SuperLog.info2SD(TAG,"???????????????????????????????????????????????????mPlayUrl???null;mPlayUrl="+mPlayUrl);
            return;
        }

        if (mPlayHandler.hasMessages(1)) {
            mPlayHandler.removeMessages(1);
        }
        Message msg = new Message();
        msg.what = 1;
        msg.obj = mPlayUrl;
        mPlayHandler.sendMessageDelayed(msg, 1000);
    }

    //?????????????????????????????????????????????????????????????????????????????????
    @SuppressLint("HandlerLeak")
    private Handler mPlayHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                if (null == mPlayView || TextUtils.isEmpty((CharSequence) msg.obj) || null==mFragment || !mFragment.getUserVisibleHint()) {//getGlobalVisibleRect
                    SuperLog.debug(TAG,"???????????????null == mPlayView?="+ (null == mPlayView) + ";TextUtils.isEmpty((CharSequence) msg.obj)?=" + TextUtils.isEmpty((CharSequence) msg.obj)
                    + ";null==mFragment?=" + (null==mFragment) +";!mFragment.getUserVisibleHint()?=" + !mFragment.getUserVisibleHint());
                    return;
                }

                if (!isVisibleALL()){
                    SuperLog.debug(TAG,"no full screen,don't play???if is playing,pause play and return.");
                    if (mPlayView.isPlaying()){
                        mFragment.recordDuration(mReflectRelativeLayout.getNavIndex(),mPlayView == null ? 0 : mPlayView.getCurrentPosition());
                        //mPlayView.playerOrPause();
                    }
                    if (mPlayView.getVisibility() == VISIBLE){
                        stopPlay(false);
                    }
                    return;
                }else if (mPlayView.getVisibility() == VISIBLE && mPlayView.isPlaying()){
                    //??????????????????????????????????????????return
                    SuperLog.debug(TAG,"full screen,but is playing,return");
                    return;
                }else if (mPlayMode == PLAY_FOCUS && !mReflectRelativeLayout.hasFocus()){
                    SuperLog.debug(TAG,"playMode is focus, no focus, don't play, return.");
                    return;
                }
                SuperLog.debug(TAG,"full screen, start play");
                mRlPV.setVisibility(VISIBLE);
                mPlayView.setVisibility(VISIBLE);
                if (null != mReflectRelativeLayout.getPosterImgView()){
                    mReflectRelativeLayout.getPosterImgView().setVisibility(View.GONE);
                }
                if (mPlayView.isPause()){//???????????????????????????
                    mPlayView.playerOrPause();
                }else if (mPlayType.equalsIgnoreCase(LIVE_TYPE)){//????????????
                    SuperLog.debug(TAG,"live playUrl="+msg.obj);
                    mPlayView.startPlay((String) msg.obj);
                }else{//????????????
                    SuperLog.debug(TAG,"play duration = "+ mDuration +";movie duration="+mPlayView.getDuration()+";vod playUrl ="+msg.obj);
                    mPlayView.startPlay((String) msg.obj,mDuration);
                }
            }
        }
    };

    //????????????????????????
    private boolean isVisibleALL(){
        SuperLog.debug(TAG,"PlayerUtils.isVisibleALL(mReflectRelativeLayout)="+PlayerUtils.isVisibleALL(mReflectRelativeLayout));
        if (PlayerUtils.isVisibleALL(mReflectRelativeLayout)){
            return true;
        }
        return false;
    }

    //???????????????????????????
    public void stopPlay(boolean isFocusChange){
        if (isFocusChange && mPlayMode == PLAY_AUTO){
            SuperLog.debug(TAG,"focusChange but playMode is auto, don't stopPlay, return.");
            return;
        }
        SuperLog.debug(TAG,"stopPlay()");
        if (null != mPlayView && mPlayView.getVisibility() == VISIBLE){
            SuperLog.debug(TAG,"playview Duration = "+mPlayView.getCurrentPosition());
            mFragment.recordDuration(mReflectRelativeLayout.getNavIndex(),mPlayView.getCurrentPosition());
            if (isOnclick){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isOnclick = false;
                        mPlayView.releasePlayer();
                        mRlPV.setVisibility(View.GONE);
                        mPlayView.setVisibility(View.GONE);
                        if (null != mReflectRelativeLayout.getPosterImgView()){
                            mReflectRelativeLayout.getPosterImgView().setVisibility(VISIBLE);
                        }
                    }
                },1000);
            }else{
                mPlayView.releasePlayer();
                mRlPV.setVisibility(View.GONE);
                mPlayView.setVisibility(View.GONE);
                if (null != mReflectRelativeLayout.getPosterImgView()){
                    mReflectRelativeLayout.getPosterImgView().setVisibility(VISIBLE);
                }
            }
        }
    }

    public void onClick(){
        if (TextUtils.isEmpty(mPlayUrl)) {
            SuperLog.debug(TAG,"?????????????????????????????????????????????????????????");
            return;
        }
        stopPlay(false);
        isOnclick = true;
        if (!mReflectRelativeLayout.isDefaultData() && null != mReflectRelativeLayout.getVodData()){
            mDuration = mPlayView == null ? 0 : mPlayView.getCurrentPosition();
            VOD currentVod = mReflectRelativeLayout.getVodData();
            Intent intent = new Intent(mContext, OnDemandVideoActivity.class);
            PlayVodBean bean = new PlayVodBean();
            bean.setVodId(currentVod.getID());
            bean.setVodName(mReflectRelativeLayout.getTitleText());
            bean.setPlayUrl(mPlayUrl);
            bean.setBookmark(String.valueOf(mDuration/1000));
//            String vodBeanStr = JsonParse.object2String(bean);
//            intent.putExtra(OnDemandVideoActivity.PLAY_VOD_BEAN, vodBeanStr);
            LiveDataHolder.get().setPlayVodBean(JsonParse.object2String(bean));
            intent.putExtra(OnDemandVideoActivity.IS_FROM_WINDOW_PLAY,1);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);

            UBDSwitch.getInstance().recordMainOnclick("",mElement,mGroup,currentVod,null);

        }else{
            onClickStatic();
        }
    }

    private void onClickStatic(){
        String sContentId = contentId;
        if (!TextUtils.isEmpty(mFourkContentId) && null != LiveUtils.findScheduleFromCanPlayById(mFourkContentId)){
            sContentId = mFourkContentId;
        }

        //???????????????
        if (contentType.equals(VideoLiveTemplate.CONTENT_TYPE_LIVE) && mPlayType.equalsIgnoreCase(LIVE_TYPE)) {
            Intent intent = new Intent(mContext, LiveTVActivity.class);
            //???????????????????????????ID???mediaID
            if (!TextUtils.isEmpty(sContentId) && !TextUtils.isEmpty(mMediaId)) {

                if (!TextUtils.isEmpty(ZJVRoute.getContentValue(mActionUrl, ZJVRoute.ActionUrlKeyType.CHANNEL_MINI_TYPE))
                        && ZJVRoute.getContentValue(mActionUrl, ZJVRoute.ActionUrlKeyType.CHANNEL_MINI_TYPE).equals("-1")){
                    //true:??????????????????????????????????????????????????????MiniEpg
                    intent.putExtra(LiveTVActivity.CHANNEL_FINAL, true);
                    //?????????????????????????????????????????????channelID???MediaId
                    LiveDataHolder.get().setChannelAndMediaId(sContentId, mMediaId);
                    LiveDataHolder.get().setIsShowingSkip(true);
                }else{
                    //?????????????????????????????????,??????,????????????????????????
                    LiveTVCacheUtil.getInstance().recordPlayChannelInfo(sContentId, mMediaId);
                }

            } else {
                //contentId??????media????????????????????????????????????????????????
                return;
            }
            intent.putExtra(LiveTVActivity.VIDEO_TYPE, LiveTVActivity.VIDEO_TYPE_LIVETV);
            mContext.startActivity(intent);
        } else if (contentType.equals(VideoLiveTemplate.CONTENT_TYPE_VOD) || mPlayType.equalsIgnoreCase(CATCH_UP_TYPE)) {
            //?????????VOD
            if (TextUtils.isEmpty(sContentId))
                return;
            mDuration = mPlayView == null ? 0 : mPlayView.getCurrentPosition();
            Intent intent = new Intent(mContext, OnDemandVideoActivity.class);
            PlayVodBean bean = new PlayVodBean();
            if (mPlayType.equalsIgnoreCase(VOD_TYPE)){
                bean.setVodId(sContentId);
                bean.setVodName(mReflectRelativeLayout.getTitleText());
                bean.setBookmark(String.valueOf(mDuration/1000));
            }else{
                bean.setVodName(mReflectRelativeLayout.getTitleText());
            }
            bean.setPlayUrl(mPlayUrl);
            LiveDataHolder.get().setPlayVodBean(JsonParse.object2String(bean));
            intent.putExtra(OnDemandVideoActivity.IS_FROM_WINDOW_PLAY,1);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
        UBDSwitch.getInstance().recordMainOnclick(mActionUrl,mElement,mGroup,null,null);
    }

    public void onResume(){
        SuperLog.debug(TAG,"onResume()");
        startPlay();
    }

    public void onPause(){
        SuperLog.debug(TAG,"onPause()");
        /*if (null != mFragment){
            mFragment.setVideoTemplate(null,mReflectRelativeLayout.getNavIndex());
        }*/
        mPlayHandler.removeCallbacksAndMessages(null);
        stopPlay(false);
    }

    //??????????????????Switch Time??????????????????
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

    //?????????????????????????????????????????????????????????????????????
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
}
