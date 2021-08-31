package com.pukka.ydepg.common.utils;

import android.os.Build;
import android.text.TextUtils;

import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.Constant;

import java.util.List;


public class ChannelUtil {

    private static final String TAG = ChannelUtil.class.getSimpleName();

    //播放4K频道设备白名单，此列表中设备允许播放4K频道
    public static final String DEVICE_4K_CHANNEL_WHITELIST = "DEVICE_4K_CHANNEL_WHITELIST";

    //播放4K频道设备黑名单，此列表中设备不允许播放4K频道
    public static final String DEVICE_4K_CHANNEL_BLACKLIST = "DEVICE_4K_CHANNEL_BLACKLIST";

    //4K频道播放设备规则,0:使用白名单  1:使用黑名单
    public static final String DEVICE_4K_CHANNEL_RULE      = "DEVICE_4K_CHANNEL_RULE";

    private static String device = getDevice();

    public interface Rule{
        String WHITELIST = "0";
        String BLACKLIST = "1";
    }

    //当前机顶盒是否支持4K频道,判断逻辑独立于VOD能力的是否支持4K,使用不同的终端配置参数
    public static boolean isDeviceSupport4K(){
        String rule = CommonUtil.getConfigValue(DEVICE_4K_CHANNEL_RULE);
        boolean isDeviceSupport4K;
        if ( Rule.WHITELIST.equals(rule)){
            List<String> deviceCanPlay4K = CommonUtil.getListConfigValue(DEVICE_4K_CHANNEL_WHITELIST);
            isDeviceSupport4K = deviceCanPlay4K.contains(device);
        } else if ( Rule.BLACKLIST.equals(rule)){
            List<String> deviceCanNotPlay4K = CommonUtil.getListConfigValue(DEVICE_4K_CHANNEL_BLACKLIST);
            isDeviceSupport4K = !deviceCanNotPlay4K.contains(device);
        } else {
            //没有配置或配置错误，默认可以播放
            isDeviceSupport4K = true;
        }
        //SuperLog.info2SD(TAG,"Current device 4K Channel/TVOD(excluded VOD) capability : " + isDeviceSupport4K);
        return isDeviceSupport4K;
    }

    private static String getDevice(){
        String systemModel = DeviceInfo.getSystemInfo(Constant.DEVICE_RAW);
        if (TextUtils.isEmpty(systemModel) || "unknown".equals(systemModel)) {
            systemModel = Build.MODEL;
        }
        SuperLog.info2SD(TAG,"Current device is : " + systemModel);
        return systemModel;
    }
}
