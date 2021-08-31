package com.pukka.ydepg.moudule.voice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.DeviceInfo;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.livetv.utils.LiveTVCacheUtil;
import com.pukka.ydepg.moudule.livetv.utils.LiveUtils;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * description: 语音广播接收器
 *
 * @author fuqiang  Email： fuqiang@easier.cn
 * @version 1.0
 */
public class VoiceReceiver extends BroadcastReceiver {

    private static final String TAG = VoiceReceiver.class.getName();

    private static final String LIVETV_CMD = "com.iflytek.xiri.action.LIVE";

    private static final String CMD_SELECT = "select";

    private static final String INTENT_ACTION = "action";

    private static final String INTENT_CHANNEL_ID = "channelid";

    private static final String INTENT_MEDIA_ID = "mediaid";

    //切换到下一个频道命令
    private static final String CMD_NEXT = "next";

    //切换到上一个频道命令
    private static final String CMD_PREV = "prev";

    @Override
    public void onReceive(Context context, Intent intent) {
        SuperLog.error(TAG, "VoiceReceiver : onReceive");
        //正在显示开机导航页，拦截语音遥控
        if (LiveDataHolder.get().getIsShowingSkip())return;

        String action = intent.getAction();
        SuperLog.error(TAG, "VoiceReceiver :" + action);
        if (!TextUtils.isEmpty(action)) {
            if (action.equals(LIVETV_CMD)) {
                String actionValue = intent.getStringExtra(INTENT_ACTION);
                if (!TextUtils.isEmpty(actionValue)) {
                    switch (actionValue) {
                        case CMD_SELECT:
                            //频道ID
                            String channelID = intent.getStringExtra(INTENT_CHANNEL_ID);
                            //媒体资源ID
                            String mediaID = intent.getStringExtra(INTENT_MEDIA_ID);
                            SuperLog.error(TAG, "收到切台广播:[mediaID:" + mediaID + ",channelID:" + channelID + "]");
                            //两个参数都是非空才能继续下面操作
                            if (!TextUtils.isEmpty(channelID) && !TextUtils.isEmpty(mediaID)) {
                                SuperLog.debug(TAG, "收到切台广播:[mediaID:" + mediaID + ",channelID:" + channelID + "]");
                                //存储下当前频道ID和媒体资源ID
                                if(setPosition(channelID)){
                                    LiveTVCacheUtil.getInstance().recordPlayChannelInfo(channelID, mediaID);
                                    Activity current = OTTApplication.getContext().getCurrentActivity();
                                    if (current instanceof LiveTVActivity) {
                                        //如果当前是直播界面
                                        if(DeviceInfo.isForeground(OTTApplication.getContext())) {
                                            //EventBus在APP后台不起作用
                                            EventBus.getDefault().post(new VoiceEvent(VoiceEvent.SWITCH_CHANNEL));
                                        }else{
                                            EventBus.getDefault().post(new VoiceEvent(VoiceEvent.SELECT));
                                        }
                                    } else {
                                        EventBus.getDefault().post(new VoiceEvent(VoiceEvent.SELECT));
                                    }
                                }else{
                                    EpgToast.showLongToast(OTTApplication.getContext(),"当前用户无权限观看此频道");
                                }

                            }
                            break;
                        case CMD_NEXT:
                            SuperLog.info(TAG, "切换到下个频道");
                            EventBus.getDefault().post(new VoiceEvent(VoiceEvent.NEXT));
                            break;
                        case CMD_PREV:
                            SuperLog.info(TAG, "切换到上个频道");
                            EventBus.getDefault().post(new VoiceEvent(VoiceEvent.PREV));
                            break;
                    }
                }
            }
        } else {
            SuperLog.error(TAG, "VoiceReceiver Intent == null");
        }
    }

    public static boolean setPosition(String channelId) {
        List<ChannelDetail> channelPlay = LiveDataHolder.get().getChannelPlay();
        String mColumnId = "";
        List<Subject> saveSubjectList = LiveDataHolder.get().getSaveSubjectList();
        int channelIndex = parseChannelDetailIndex(channelId, channelPlay);
        if (channelIndex == -1) {
            return false;
        } else {
            ChannelDetail channelDetail = channelPlay.get(channelIndex);
            if (saveSubjectList != null && (saveSubjectList.size() == 1 || saveSubjectList.size() == 0)) {
                LiveDataHolder.get().setChannelSelectPosition(channelIndex);
            } else if (saveSubjectList != null && saveSubjectList.size() > 1){
                for (Subject subject : saveSubjectList) {
                    if (TextUtils.isEmpty(mColumnId)) {
                        mColumnId = subject.getID();
                    } else {
                        mColumnId = mColumnId + "," + subject.getID();
                    }
                }
                List<String> subjectIDs = channelDetail.getSubjectIDs();
                String[] split = mColumnId.split(",");
                boolean isGoOn = true;
                int indexPo = 0;
                for (int i = 0; i < split.length; i++) {
                    if (!isGoOn) break;
                    for (String subject : subjectIDs) {
                        if (split[i].equals(subject)) {
                            LiveDataHolder.get().setCulomnSelectPosition(i);
                            indexPo = i;
                            isGoOn = false;
                            break;
                        }
                    }
                }
                List<ChannelDetail> channelDetailList = LiveDataHolder.get().getSaveHashMap().get(split[indexPo]);
                if (channelDetailList != null && channelDetailList.size() > 0) {
                    for (int i = 0; i < channelDetailList.size(); i++) {
                        if (channelId.equals(channelDetailList.get(i).getID())) {
                            LiveDataHolder.get().setChannelSelectPosition(i);
                            break;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 解析频道号索引位置
     *
     * @param channelID 频道ID
     * @return 索引位置
     */
    public static int parseChannelDetailIndex(String channelID, List<ChannelDetail> allSchedules) {
        int allScheduleSize;
        int channelIndex = -1;
        if (null != allSchedules && (allScheduleSize = allSchedules.size()) > 0) {
            for (int index = 0; index < allScheduleSize; index++) {
                String channelId = allSchedules.get(index).getID();
                //遍历出当前频道ID所在的位置
                if (!TextUtils.isEmpty(channelId) && channelId.equals(channelID)) {
                    channelIndex = index;
                    break;
                }
            }
            if (channelIndex == -1) {
                for (int index = 0; index < allScheduleSize; index++) {
//                    String channelId = allSchedules.get(index).getID();
                    String channelId = CommonUtil.getCustomField(allSchedules.get(index).getCustomFields(), Constant.CHANNEL_ID);
                    //遍历出当前频道ID所在的位置
                    if (!TextUtils.isEmpty(channelId) && channelId.equals(channelID)) {
                        channelIndex = index;
                        break;
                    }
                }
            }
        }


//        for (ChannelDetail detail : listChannelDetail) {
//               String channelCode= CommonUtil.getCustomField(detail.getCustomFields(),"XmppForChannel");
//                if (channelCode.equalsIgnoreCase(mediaCode)) {
//                    chInfo.setChannelID(detail.getID());
//                    chInfo.setMediaID(LiveUtils.parseMediaID(detail));
//                    SuperLog.info2SD(TAG, "XMPP LiveTV CustomField info is: [ChannelID]=" + chInfo.getChannelID() + "\t[mediaID]=" + chInfo.getMediaID());
//                    break;
//                }
//            }
        return channelIndex;
    }
}