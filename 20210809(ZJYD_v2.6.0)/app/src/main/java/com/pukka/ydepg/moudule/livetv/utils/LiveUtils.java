package com.pukka.ydepg.moudule.livetv.utils;

import android.text.TextUtils;

import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.moudule.player.node.Schedule;
import com.pukka.ydepg.moudule.player.util.PlayerUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gentl on 2017/3/23.
 */

public class LiveUtils {
    /**
     * 查找Schedule
     */
    public static Schedule findScheduleById(String channelId) {
        if (TextUtils.isEmpty(channelId))return null;
        List<Schedule> schedules = LiveDataHolder.get().getAllSchedules();
        if (null != schedules) {
            for (Schedule schedule : schedules) {
                if (schedule.getId().equals(channelId)) {
                    return schedule;
                }
            }
        }
        return null;
    }

    /**
     * 根据channel id
     * 在用户可播放列表中
     * 查找Schedule
     */
    public static ChannelDetail findScheduleFromCanPlayById(String channelId) {
        if (TextUtils.isEmpty(channelId))return null;
        List<ChannelDetail> channels = LiveDataHolder.get().getChannelPlay();
        if (null != channels) {
            for (ChannelDetail channel : channels) {
                if (channel.getID().equals(channelId)) {
                    return channel;
                }
            }
        }
        return null;
    }

    /**
     * 根据频道号查找Schedule
     */
    public static Schedule findScheduleByChannelNO(String channelNO) {
        List<Schedule> schedules = LiveDataHolder.get().getAllSchedules();
        if (!TextUtils.isEmpty(channelNO) && null != schedules) {
            for (Schedule schedule : schedules) {
                String channelNo = schedule.getChannelNo();
                if (!TextUtils.isEmpty(channelNo) && channelNo.equals(channelNO)) {
                    return schedule;
                }
            }
        }
        return null;
    }

    /**
     * 查找Schedule
     */
    public static ChannelDetail findChannelById(String channelId) {
        List<ChannelDetail> schedules = LiveDataHolder.get().getChannelPlay();
        if (null != schedules) {
            for (ChannelDetail schedule : schedules) {
                if (schedule.getID().equals(channelId)) {
                    return schedule;
                }
            }
        }
        return null;
    }

    public static String parseChannelPic(Picture picture) {
        if (picture != null
                && picture.getChannelPics() != null
                && picture.getChannelPics().size() > 0) {
            return String.valueOf(picture.getChannelPics().get(0));
        }
        return "";
    }

    public static String parseMediaID(ChannelDetail channelDetail){
        //媒体ID
        String mediaId = null;
        if (channelDetail.getPhysicalChannels() != null && channelDetail.getPhysicalChannels().size() > 0) {
            if (channelDetail.getPhysicalChannels().size() == 1) {
                mediaId = channelDetail.getPhysicalChannels().get(0).getID();
            } else {
                //取[清晰度标识]最大的
                List<Integer> maxList = new ArrayList<>();
                for (int i = 0; i < channelDetail.getPhysicalChannels().size(); i++) {
                    maxList.add(Integer.parseInt(channelDetail.getPhysicalChannels().get(i).getDefinition()));
                }
                mediaId = channelDetail.getPhysicalChannels().get(maxList.indexOf(Collections.max(maxList))).getID();
            }
        }
        return mediaId;
    }

    public static Schedule parseSingleSchedule(ChannelDetail channelDetail) {
        //SuperLog.debug("parseSingleSchedule", "parseSingleSchedule");
        Schedule schedule = new Schedule();
        schedule.setId(channelDetail.getID());
        schedule.setMediaID(parseMediaID(channelDetail));
        schedule.setChannelNo(channelDetail.getChannelNO());
        schedule.setName(channelDetail.getName());
        schedule.setCode(channelDetail.getCode());
        schedule.setPoster(LiveUtils.parsePoster(channelDetail.getPicture()));
        //channel logo
        String logo = parseChannelPic(channelDetail.getPicture());
        if (channelDetail.getLogo() != null && !TextUtils.isEmpty(channelDetail.getLogo().getUrl())) {
            logo = channelDetail.getLogo().getUrl();
        }
        schedule.setChannelLogo(logo);
        schedule.setSupportBtv(PlayerUtils.supportBTV(channelDetail));
        schedule.setFavorite(channelDetail.getFavorite() != null);
        schedule.setPltvLength(PlayerUtils.getPLTVLength(channelDetail));
        schedule.setNumber((!TextUtils.isEmpty(channelDetail.getVisibility()) && TextUtils.isDigitsOnly(
                channelDetail.getVisibility())) ? Integer.parseInt(channelDetail.getVisibility()) : 0);
        if (channelDetail.getPhysicalChannels() != null
                && channelDetail.getPhysicalChannels().size() > 0) {
            schedule.setMediaID(channelDetail.getPhysicalChannels().get(0).getID());
        }
        if (channelDetail.getSubjectIDs() != null && channelDetail.getSubjectIDs().size() > 0) {
            for (String subject : channelDetail.getSubjectIDs()) {
                if (!TextUtils.isEmpty(subject) && TextUtils.isEmpty(schedule.getParentId())) {
                    schedule.setParentId(subject);
                } else {
                    schedule.setParentId(schedule.getParentId() + "," + subject);
                }
            }
        }
        if (channelDetail.getPicture() != null
                && channelDetail.getPicture().getChannelPics() != null
                && channelDetail.getPicture().getChannelPics().size() > 0) {
            schedule.setPicture(channelDetail.getPicture().getChannelPics().get(0));
        }
        schedule.setIsLocked(channelDetail.getIsLocked());
        return schedule;
    }

    /**
     * 解析海报
     */
    public static String parsePoster(Picture picture) {
        if (picture != null && picture.getPosters() != null && picture.getPosters().size() > 0) {
            return String.valueOf(picture.getPosters().get(0));
        }
        return "";
    }

    /**
     * 是否显示频道名称
     */
    public static boolean isShowChannelName() {
        String showChannelNameValue = com.pukka.ydepg.launcher.session.SessionService.getInstance()
                .getSession()
                .getTerminalConfigurationValue(Configuration.Key.IS_SHOWCHANNEL_NAME);
        if (TextUtils.isEmpty(showChannelNameValue)) {
            //没有配置,默认显示
            showChannelNameValue = "1";
        }
        //1表示展示频道名称,0表示不展示频道名称
        return "1".equals(showChannelNameValue);
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

            //此处也要遍历取终端参数
            if (channelIndex == -1) {
                for (int index = 0; index < allScheduleSize; index++) {
//                    String channelId = allSchedules.get(index).getID();
                    String channelId = CommonUtil.getCustomField(allSchedules.get(index).getCustomFields(), Constant.CHANNEL_ID);
                    //遍历出当前频道ID所在的位置
                    if (!TextUtils.isEmpty(channelId) && channelId.equals(channelId)) {
                        SuperLog.info2SD("LiveUtils","index="+index);
                        channelIndex = index;
                        break;
                    }
                }
            }
        }
        return channelIndex;
    }

    /**
     * 解析频道号索引位置
     *
     * @return 索引位置
     */
    public static int parseChannelIdIndex(String channelNo, List<ChannelDetail> allSchedules) {
        int allScheduleSize;
        int channelIndex = -1;
        if (null != allSchedules && (allScheduleSize = allSchedules.size()) > 0) {
            for (int index = 0; index < allScheduleSize; index++) {
                String channelId = allSchedules.get(index).getChannelNO();
                //遍历出当前频道ID所在的位置
                if (!TextUtils.isEmpty(channelId) && channelId.equals(channelNo)) {
                    channelIndex = index;
                    break;
                }
            }
        }
        return channelIndex;
    }
}