package com.pukka.ydepg.xmpp;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.bean.node.XmppMessage;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.PlayURLCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODDetailCallBack;
import com.pukka.ydepg.common.http.v6bean.v6Controller.VODListController;
import com.pukka.ydepg.common.http.v6bean.v6node.BookMarkSwitchs;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.utils.ActivityStackControlUtil;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.uiutil.CpRoute;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.livetv.utils.LiveTVCacheUtil;
import com.pukka.ydepg.moudule.livetv.utils.LiveUtils;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;
import com.pukka.ydepg.moudule.player.ui.OnDemandVideoActivity;
import com.pukka.ydepg.moudule.player.util.OffScreenUtils;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.voice.VoiceEvent;
import com.pukka.ydepg.moudule.voice.VoiceReceiver;
import com.pukka.ydepg.xmpp.bean.PlayerStateMessage;
import com.pukka.ydepg.xmpp.bean.VolumeStateMessage;
import com.pukka.ydepg.xmpp.bean.XmppConstant;
import com.pukka.ydepg.xmpp.bean.XmppSuccessEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

import static android.content.Context.AUDIO_SERVICE;
import static android.media.AudioManager.FLAG_SHOW_UI;
import static com.pukka.ydepg.xmpp.bean.XmppConstant.MediaType;
import static com.pukka.ydepg.xmpp.bean.XmppConstant.MessageKey;
import static com.pukka.ydepg.xmpp.bean.XmppConstant.PlayBackState;
import static com.pukka.ydepg.xmpp.bean.XmppConstant.TrickPlayMode;

/**
 * Created by Liu on 2018/6/20.
 */
class PlayerControl {

    private static final String TAG = PlayerControl.class.getSimpleName();

    private HandXMPPMessage mHandXMPPMessage;

    PlayerControl(HandXMPPMessage mHandXMPPMessage) {
        this.mHandXMPPMessage = mHandXMPPMessage;
    }

    String getPlayerState() {
        PlayerStateMessage playerState = new PlayerStateMessage();
        Activity current = OTTApplication.getContext().getCurrentActivity();
        if (current instanceof LiveTVActivity) {
            //???????????????
            LiveTVActivity activity = (LiveTVActivity) current;
            playerState.setPlayBackState(PlayBackState.PLAY);
            //playerState.playerInstance
            playerState.setTrickPlayMode(TrickPlayMode.PLAY);
            //playerState.fastSpeed
            //playerState.playPosition
            playerState.setMediaType(MediaType.TYPE_CHANNEL);
            String[] ch = LiveTVCacheUtil.getInstance().getRecordPlayChannelInfo();
            if (null != ch) {
                playerState.setMediaCode(activity.getContentCode());
                playerState.setChanKey(Integer.parseInt(ch[0]));
            }
            //playerState.duration      =
            playerState.setPlayUrl(activity.getCurrentChannelUrl());

        } else if (current instanceof OnDemandVideoActivity) {
            //???????????????
            OnDemandVideoActivity activity = (OnDemandVideoActivity) current;
            playerState.setPlayBackState(activity.getPlayBackState());
            //playerState.playerInstance
            if (PlayBackState.PLAY == playerState.getPlayBackState()) {
                playerState.setTrickPlayMode(activity.getTrickPlayMode());
                if (TrickPlayMode.FAST_SPEED == activity.getTrickPlayMode()) {
                    playerState.setFastSpeed(activity.getFastSpeed());
                }
                playerState.setPlayPosition(activity.getVodCurrentPosition());
                playerState.setMediaType(MediaType.TYPE_VOD);
                playerState.setMediaCode(activity.getContentCode());
                //playerState.chanKey       =
                playerState.setDuration(activity.getVodDuration());
                playerState.setPlayUrl(activity.getCurrentVodPlayUrl());
            } else {
                //playerState.playerInstance=
                //playerState.trickPlayMode = TrickPlayMode.PLAY;
                //playerState.fastSpeed     =
                //playerState.playPosition  =
                //playerState.mediaType     = MediaType.TYPE_CHANNEL;
                //playerState.mediaCode     =
                //playerState.chanKey       =
                //playerState.duration      =
                //playerState.playUrl       =
            }
        } else  if (current instanceof NewVodDetailActivity){
            //?????????????????????
            NewVodDetailActivity activity = (NewVodDetailActivity) current;
            playerState.setPlayBackState(activity.getPlayBackState());
            //playerState.playerInstance
            if (PlayBackState.PLAY == playerState.getPlayBackState()) {
                playerState.setTrickPlayMode(activity.getTrickPlayMode());
                if (TrickPlayMode.FAST_SPEED == activity.getTrickPlayMode()) {
                    playerState.setFastSpeed(activity.getFastSpeed());
                }
                playerState.setPlayPosition(activity.getVodCurrentPosition());
                playerState.setMediaType(MediaType.TYPE_VOD);
                playerState.setMediaCode(activity.getContentCode());
                //playerState.chanKey       =
                playerState.setDuration(activity.getVodDuration());
                playerState.setPlayUrl(activity.getCurrentVodPlayUrl());
            }
        }else{
            //?????????????????????
            playerState.setPlayBackState(PlayBackState.NOT_PLAY);
            //playerState.playerInstance=
            //playerState.trickPlayMode = TrickPlayMode.PLAY;
            //playerState.fastSpeed     =
            //playerState.playPosition  =
            //playerState.mediaType     = MediaType.TYPE_CHANNEL;
            //playerState.mediaCode     =
            //playerState.chanKey       =
            //playerState.duration      =
            //playerState.playUrl       =
        }
        return JsonParse.object2String(playerState);
    }

    //XMPP??????
    void handlePullMessage(String contentCode, String handler, String jid) {
        if (null != mHandXMPPMessage) {
            PlayerStateMessage playerState = new PlayerStateMessage();
            Activity activity = OTTApplication.getContext().getCurrentActivity();
            if (TextUtils.isEmpty(contentCode) ||  !(activity instanceof RxAppCompatActivity)) {
                playerState.setPlayUrl("No contentCode error");
                mHandXMPPMessage.handleXmppMessage(XmppConstant.Operation.PULL_OPERATION, JsonParse.object2String(playerState), handler, jid);
            } else {
                DetailPresenter mDetailPresenter = new DetailPresenter((RxAppCompatActivity)activity);
                mDetailPresenter.getVODDetail(contentCode, new VODDetailCallBack() {
                    @Override
                    public void getVODDetailSuccess(VODDetail vodDetail, String recmActionID, List<RecmContents> recmContents) {
                        mDetailPresenter.setButtonOrderOrSee(true);
                        mDetailPresenter.setVODDetail(vodDetail);
                        mDetailPresenter.playVOD(vodDetail, new PlayURLCallBack() {
                            @Override
                            public void getVODPlayUrlSuccess(String url, String bookmark, String productId, String elapseTime) {
                                playerState.setPlayUrl(url);
                                mHandXMPPMessage.handleXmppMessage(XmppConstant.Operation.PULL_OPERATION, JsonParse.object2String(playerState), handler, jid);
                            }

                            @Override
                            public void getChannelPlayUrlSuccess(String url, String attachedPlayURL, String bookmark) { }

                            @Override
                            public void playFail() {
                                playerState.setPlayUrl("No contentCode error");
                                mHandXMPPMessage.handleXmppMessage(XmppConstant.Operation.PULL_OPERATION, JsonParse.object2String(playerState), handler, jid);
                            }

                            @Override
                            public void playFail(PlayVODResponse playVODResponse) {
                                playerState.setPlayUrl(playVODResponse.getResult().getRetMsg());
                                mHandXMPPMessage.handleXmppMessage(XmppConstant.Operation.PULL_OPERATION, JsonParse.object2String(playerState), handler, jid);
                            }

                            @Override public void onPlaycancel() { }

                            @Override public void getVODDownloadUrlSuccess(String vodID, String url, String postURL, String switchNum, String name) { }

                            @Override public void getVODDownloadUrlFailed(String vodID, String episodeID) { }
                        });
                    }

                    @Override
                    public void getVODDetailFailed() {
                        playerState.setPlayUrl("No contentCode error");
                        mHandXMPPMessage.handleXmppMessage(XmppConstant.Operation.PULL_OPERATION, JsonParse.object2String(playerState), handler, jid);
                    }

                    @Override
                    public void onError() {}
                });

            }
        }
    }

    void remoteSeek(int seekPosition) {
        SuperLog.info2SD(TAG, "XMPP seek position(unit:s)=" + seekPosition);
        Activity current = OTTApplication.getContext().getCurrentActivity();
        if (current instanceof OnDemandVideoActivity) {
            current.runOnUiThread(()-> ((OnDemandVideoActivity) current).seekTo(seekPosition));
        }else if (current instanceof NewVodDetailActivity){
            current.runOnUiThread(()-> ((NewVodDetailActivity) current).seekTo(seekPosition));
        }
    }

    void remoteControl(int trickPlayMode) {
        SuperLog.info2SD(TAG, "XMPP play control command is : " + trickPlayMode + "\t0:PLAY 1:PAUSE");
        Activity current = OTTApplication.getContext().getCurrentActivity();
        if (current instanceof OnDemandVideoActivity) {
            current.runOnUiThread(() -> ((OnDemandVideoActivity) current).playerOrPause(trickPlayMode));
        }else if (current instanceof NewVodDetailActivity){
            current.runOnUiThread(() -> ((NewVodDetailActivity) current).playerOrPause(trickPlayMode));
        }
    }

    //??????????????????D
    void handleRemotePlayVOD(JSONObject jsonObject) {
        /*
         * {???action???:???functionCall???,???functionType???:???startPlay???,???mediaCode???:???2???,???mediaType???:???2???
         * ,???playByBookMark???:???1???,???playByTime???:???180???,???actionSource???:???myPhone???,"delay":"2002-09-
         *  10T23:08:25Z","platform":"iptv"}
         */
        try {
            if (jsonObject.has(MessageKey.DELAY)) {
                //??????????????????
                TextUtils.isEmpty(jsonObject.getString(MessageKey.DELAY));
                return;
            }
            XmppMessage xmppMessage = new XmppMessage();
            xmppMessage.setMediaCode(jsonObject.getString(MessageKey.MEDIA_CODE));
            xmppMessage.setMediaType(jsonObject.getString(MessageKey.MEDIA_TYPE));
            xmppMessage.setPlayByBookMark(jsonObject.getString(MessageKey.PLAY_BY_BOOKMARK));
            xmppMessage.setPlayByTime(jsonObject.getString(MessageKey.PLAY_BY_TIME));
            xmppMessage.setActionSource(jsonObject.getString(MessageKey.ACTION_SOURCE));
            xmppMessage.setPlatform(jsonObject.getString(MessageKey.PLATFORM));

            List<WeakReference<Activity>> activityList = ActivityStackControlUtil.getActivityList();
            if (null != activityList && !activityList.isEmpty()) {
                for (int i = activityList.size() - 1; i >= 0; i--) {
                    if (activityList.get(i).get() instanceof OnDemandVideoActivity || activityList.get(i).get() instanceof NewVodDetailActivity) {
                        if (activityList.get(i).get() instanceof NewVodDetailActivity){
                            NewVodDetailActivity activity = (NewVodDetailActivity) activityList.get(i).get();
                            if (null != activity && null != activity.getOnDemandTVPlayFragment() && null != activity.getOnDemandTVPlayFragment().mPlayView){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        activity.getOnDemandTVPlayFragment().mPlayView.pausePlay();
                                        //???VOD???????????????????????????????????????????????????????????????onDestory????????????0???????????????????????????????????????
                                        // ???onDestroy??????????????????????????????????????????onDestory????????????
                                        activity.getOnDemandTVPlayFragment().reportBookmark(BookMarkSwitchs.DESTORY);
                                        activity.getOnDemandTVPlayFragment().setSwitchVOD(true);
                                        activity.getOnDemandTVPlayFragment().mPlayView.releasePlayer();
                                    }
                                });
                            }
                        }
                        activityList.get(i).get().finish();
                        break;
                    }
                }
            }

            //Activity current = OTTApplication.getContext().getCurrentActivity();
            Activity current = OTTApplication.getContext().getMainActivity();
            //?????????????????????????????????,?????????????????????????????????????????????
            OffScreenUtils.getSPVodDetail(jsonObject.getString(MessageKey.MEDIA_CODE), new VODListController((RxAppCompatActivity) current), (RxAppCompatActivity) current, new VODDetailCallBack() {
                @Override
                public void getVODDetailSuccess(VODDetail vodDetail, String recmActionID, List<RecmContents> recmContents) {
                    if(vodDetail == null){
                        SuperLog.error(TAG,"XMPP play vod getVodDetail failed.");
                        return;
                    }

                    if (!CollectionUtil.isEmpty(vodDetail.getSeries()) && "0".equals(vodDetail.getVODType())) {
                        //?????????     ???????????????????????????????????????id?????????????????????????????????id????????????cpid
                        String fatherId = vodDetail.getSeries().get(0).getVODID();//??????ID
                        OffScreenUtils.getSPVodDetail(fatherId, new VODListController((RxAppCompatActivity) current), (RxAppCompatActivity) current, new VODDetailCallBack() {
                            @Override
                            public void getVODDetailSuccess(VODDetail vodDetail, String recmActionID, List<RecmContents> recmContents) {
                                if (!TextUtils.isEmpty(vodDetail.getCpId()) && CpRoute.isCp(vodDetail.getCpId(), vodDetail.getCustomFields())) {
                                    CpRoute.goCp(vodDetail.getCustomFields());
                                } else {
                                    startSelfPlayer(current, xmppMessage,vodDetail,fatherId);
                                }
                            }

                            @Override
                            public void getVODDetailFailed() {
                                startSelfPlayer(current, xmppMessage,vodDetail,fatherId);
                            }

                            @Override public void onError() {
                                startSelfPlayer(current, xmppMessage,vodDetail,fatherId);
                            }
                        });
                    } else {
                        //????????????
                        if (!TextUtils.isEmpty(vodDetail.getCpId()) && CpRoute.isCp(vodDetail.getCpId(), vodDetail.getCustomFields())) {
                            CpRoute.goCp(vodDetail.getCustomFields());
                        } else {
                            startSelfPlayer(current, xmppMessage,vodDetail,"");
                        }
                    }
                }

                @Override
                public void getVODDetailFailed() {
                    SuperLog.info2SD(TAG, "get cpId failed");
                    startSelfPlayer(current, xmppMessage,null,"");
                }

                @Override
                public void onError() {
                    SuperLog.info2SD(TAG, "get cpId onError");
                    startSelfPlayer(current, xmppMessage,null,"");
                }
            });
        } catch (JSONException e) {
            SuperLog.info2SD(TAG, e.getMessage());
        }
    }

    private void startSelfPlayer(Activity current, XmppMessage xmppMessage, VODDetail vodDetail,String fatherid) {
        if (null != current) {
            EventBus.getDefault().post(new XmppSuccessEvent());
//            Intent intent = new Intent(current, OnDemandVideoActivity.class);
//            intent.putExtra("xmpp_message", xmppMessage);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            OTTApplication.getContext().startActivity(intent);

            Intent intent = new Intent(current, NewVodDetailActivity.class);
            intent.putExtra("xmpp_message", xmppMessage);
            intent.putExtra(NewVodDetailActivity.NEED_FULL_WINDOW,"1");

            if (!TextUtils.isEmpty(fatherid)){
                intent.putExtra(NewVodDetailActivity.VOD_ID,fatherid);
            }else{
                intent.putExtra(NewVodDetailActivity.VOD_ID,vodDetail.getID());
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            OTTApplication.getContext().startActivity(intent);
        }
    }

    //??????VR??????
    void playVR(JSONObject jsonObject) {
        try {
            if (jsonObject.has(MessageKey.DELAY)) {
                //??????????????????
                TextUtils.isEmpty(jsonObject.getString(MessageKey.DELAY));
                return;
            }
            XmppMessage xmppMessage = new XmppMessage();
            xmppMessage.setMediaCode(jsonObject.getString(MessageKey.MEDIA_CODE));
            xmppMessage.setMediaType(jsonObject.getString(MessageKey.MEDIA_TYPE));
            xmppMessage.setPlayByBookMark(jsonObject.getString(MessageKey.PLAY_BY_BOOKMARK));
            xmppMessage.setPlayByTime(jsonObject.getString(MessageKey.PLAY_BY_TIME));
            xmppMessage.setActionSource(jsonObject.getString(MessageKey.ACTION_SOURCE));
            xmppMessage.setPlatform(jsonObject.getString(MessageKey.PLATFORM));
            xmppMessage.setVideoType(jsonObject.getString(MessageKey.VIDEO_TYPE));
            xmppMessage.setPlayUrl(jsonObject.getString(MessageKey.PLAY_URL));

            List<WeakReference<Activity>> activityList = ActivityStackControlUtil.getActivityList();
            if (null != activityList && !activityList.isEmpty()) {
                for (int i = activityList.size() - 1; i >= 0; i--) {
                    if (activityList.get(i).get() instanceof OnDemandVideoActivity || activityList.get(i).get() instanceof NewVodDetailActivity) {
                        if (activityList.get(i).get() instanceof NewVodDetailActivity){
                            NewVodDetailActivity activity = (NewVodDetailActivity) activityList.get(i).get();
                            if (null != activity && null != activity.getOnDemandTVPlayFragment() && null != activity.getOnDemandTVPlayFragment().mPlayView){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        activity.getOnDemandTVPlayFragment().mPlayView.pausePlay();
                                        //???VOD???????????????????????????????????????????????????????????????onDestory????????????0???????????????????????????????????????
                                        // ???onDestroy??????????????????????????????????????????onDestory????????????
                                        activity.getOnDemandTVPlayFragment().reportBookmark(BookMarkSwitchs.DESTORY);
                                        activity.getOnDemandTVPlayFragment().setSwitchVOD(true);
                                        activity.getOnDemandTVPlayFragment().mPlayView.releasePlayer();
                                    }
                                });
                            }
                        }
                        activityList.get(i).get().finish();
                        break;
                    }
                }
            }

            Activity activity = OTTApplication.getContext().getCurrentActivity();
            if (null != activity) {
                EventBus.getDefault().post(new XmppSuccessEvent());
                Intent intent = new Intent(activity, OnDemandVideoActivity.class);
                intent.putExtra("xmpp_message", xmppMessage);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                OTTApplication.getContext().startActivity(intent);
            }
        } catch (JSONException e) {
            SuperLog.info2SD(TAG, e.getMessage());
        }
    }

    //??????????????????
    void handleRemotePlayChannel(JSONObject jsonObject) throws Exception {
        //??????????????????
        if (jsonObject.has(MessageKey.DELAY) && !TextUtils.isEmpty(jsonObject.getString(MessageKey.DELAY))) {


            SuperLog.info2SD(TAG, "This is an offline XMPP message and dismiss it.");
            return;
        }

        ChannelInfo chInfo = getChannelInfo(jsonObject.getString(MessageKey.MEDIA_CODE));
        if (!chInfo.hasChannelInfo()) {
            SuperLog.error(TAG, "There is no channel detail matching XMPP LiveTV play command:mediaCode=" + jsonObject.getString(MessageKey.MEDIA_CODE));
            return;
        }


        if (VoiceReceiver.setPosition(chInfo.getChannelID())) {
            LiveTVCacheUtil.getInstance().recordPlayChannelInfo(chInfo.getChannelID(), chInfo.getMediaID());
            Activity current = OTTApplication.getContext().getCurrentActivity();
            if (current instanceof LiveTVActivity) {
                //?????????????????????,???????????????????????????????????????????????????UI
                SuperLog.info2SD(TAG, "Switch to the channel XMPP pushed.");
                current.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(new VoiceEvent(VoiceEvent.SWITCH_CHANNEL));
                    }
                });
            } else {
                //????????????????????????(BaseActivity????????????????????????)
                SuperLog.info2SD(TAG, "Begin to play XMPP push channel.");
                EventBus.getDefault().post(new VoiceEvent(VoiceEvent.SELECT));
            }
        } else {
            SuperLog.info2SD(TAG, "Current STB user has no permission to watch this XMPP push channel.");
            if (null != OTTApplication.getContext().getCurrentActivity()) {
                //??????????????????Toast
                OTTApplication.getContext().getCurrentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EpgToast.showLongToast(OTTApplication.getContext(), "????????????????????????????????????");
                    }
                });
            }
        }
    }

    private ChannelInfo getChannelInfo(String mediaCode) {
        ChannelInfo chInfo = new ChannelInfo();
        SuperLog.info2SD(TAG, "<><><><><><><><><> get all channel info in xmpp");
        List<ChannelDetail> listChannelDetail = LiveDataHolder.get().getAllChannels();
        for (ChannelDetail detail : listChannelDetail) {
            if (detail.getCode().equalsIgnoreCase(mediaCode)) {
                chInfo.setChannelID(detail.getID());
                chInfo.setMediaID(LiveUtils.parseMediaID(detail));
                SuperLog.info2SD(TAG, "XMPP LiveTV info is: [ChannelID]=" + chInfo.getChannelID() + "\t[mediaID]=" + chInfo.getMediaID());
                break;
            }
        }
        //??????????????????????????????????????????
        if(!chInfo.hasChannelInfo())
        {
            for (ChannelDetail detail : listChannelDetail) {
               String channelCode= CommonUtil.getCustomField(detail.getCustomFields(), Constant.CHANNEL_CODE);
                if (channelCode.equalsIgnoreCase(mediaCode)) {
                    chInfo.setChannelID(detail.getID());
                    chInfo.setMediaID(LiveUtils.parseMediaID(detail));
                    SuperLog.info2SD(TAG, "XMPP LiveTV CustomField info is: [ChannelID]=" + chInfo.getChannelID() + "\t[mediaID]=" + chInfo.getMediaID());
                    break;
                }
            }
        }
        return chInfo;
    }

    private class ChannelInfo {
        private String channelID;
        private String mediaID;

        public String getChannelID() {
            return channelID;
        }

        public void setChannelID(String channelID) {
            this.channelID = channelID;
        }

        public String getMediaID() {
            return mediaID;
        }

        public void setMediaID(String mediaID) {
            this.mediaID = mediaID;
        }

        boolean hasChannelInfo() {
            if (TextUtils.isEmpty(mediaID) || TextUtils.isEmpty(channelID)) {
                return false;
            } else {
                return true;
            }
        }
    }

    String getPlayerVolume() {
        int volume = 0;
        try {
            AudioManager am = (AudioManager) OTTApplication.getContext().getSystemService(AUDIO_SERVICE);
            volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
            int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            if (100 != maxVolume) {
                volume = (volume * 100) / maxVolume;
            }
        } catch (Exception e) {
            SuperLog.error(TAG, e);
        }

        VolumeStateMessage volumeState = new VolumeStateMessage();
        volumeState.setCurrentVolume(String.valueOf(volume));
        return JsonParse.object2String(volumeState);
    }

    void setPlayerVolume(JSONObject jsonObject) {
        try {
            AudioManager am = (AudioManager) OTTApplication.getContext().getSystemService(AUDIO_SERVICE);
            int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int volume = jsonObject.getInt(MessageKey.NEW_VOLUME);
            //???????????????????????????????????????????????????100??????????????????????????????????????????????????????100,???????????????????????????
            int realVolume = volume * maxVolume / 100;
            am.setStreamVolume(AudioManager.STREAM_MUSIC, realVolume, FLAG_SHOW_UI);
        } catch (Exception e) {
            SuperLog.error(TAG, e);
        }
    }

    void exitPlay() {
        Activity current = OTTApplication.getContext().getCurrentActivity();
        if (current instanceof OnDemandVideoActivity) {
            current.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((OnDemandVideoActivity) current).onXmppBack();
                }
            });
        }
    }

    public interface HandXMPPMessage {
        void handleXmppMessage(int operation, String pullMessage, String handler, String jid);
    }
}