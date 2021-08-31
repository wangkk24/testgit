package com.pukka.ydepg.moudule.vod.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.moudule.mytv.MessageWebViewActivity;

import java.util.Objects;

/**
 * 作者：panjw on 2020/10/26 15:59
 * <p>
 * 角标广告需求
 * 邮箱：panjw@easier.cn
 */
public class VodCornerMarkerUtils {
    //开始插入广告的时间，默认20分钟支持可配置
    public static int START_PLAY_VOD_TIME = 1000 * 60 * 20;
    //内容播放过程中最大插入M次角标广告，其中角标广告最大播放次数支持可配，默认3次
    public static int PLAY_VOD_COUNT = 3;
    //广告自动关闭时间
    public static int CLOSE_AD_TIME = 1000 * 15;

    public static int FIRST_SHOW_AD_TIME =1000 * 60 * 20;

    //获取播放的间隔时间
    public static int getPlayVodAdTime() {
        if (!TextUtils.isEmpty(CommonUtil.getConfigValue(Constant.AdCornerMarker.INTERVAL_PLAY_TIME))) {
            return Integer.parseInt(Objects.requireNonNull(CommonUtil.getConfigValue(Constant.AdCornerMarker.INTERVAL_PLAY_TIME)));
        }
        return START_PLAY_VOD_TIME;
    }

    //获取播放的最大次数
    public static int getPlayVodCount() {
        if (!TextUtils.isEmpty(CommonUtil.getConfigValue(Constant.AdCornerMarker.MAX_PLAY_COUNT))) {
            return Integer.parseInt(Objects.requireNonNull(CommonUtil.getConfigValue(Constant.AdCornerMarker.MAX_PLAY_COUNT)));
        }
        return PLAY_VOD_COUNT;
    }

    //获取广告广告关闭时间
    public static int getAdCloseTime() {
        if (!TextUtils.isEmpty(CommonUtil.getConfigValue(Constant.AdCornerMarker.AD_CLOSE_TIME))) {
            return Integer.parseInt(Objects.requireNonNull(CommonUtil.getConfigValue(Constant.AdCornerMarker.AD_CLOSE_TIME)));
        }
        return CLOSE_AD_TIME;
    }

    //获取首次展示广告的时间
    public static int getFirstShowAdTime()
    {
        if(!TextUtils.isEmpty(CommonUtil.getConfigValue(Constant.AdCornerMarker.FIRST_SHOW_AD_TIME)))
        {
            return Integer.parseInt(Objects.requireNonNull(CommonUtil.getConfigValue(Constant.AdCornerMarker.FIRST_SHOW_AD_TIME)));
        }
        return FIRST_SHOW_AD_TIME;
    }
}