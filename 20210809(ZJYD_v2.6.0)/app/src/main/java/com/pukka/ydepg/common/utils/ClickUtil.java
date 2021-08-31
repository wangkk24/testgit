package com.pukka.ydepg.common.utils;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.service.NtpTimeService;

import java.util.HashMap;

public final class ClickUtil
{
    private static long DEFAULT_CLICK_INTERVAL = 200L;

    private static long lastClickTime;

    private static HashMap<String,Long> mapClickInterval = new HashMap<>();
    private ClickUtil() {}
    
    /**
     * 设置两次点击的间隔
     * @param time 毫秒
     */
    public static void setInterval(String tag,long time) {
        mapClickInterval.put(tag,time);
    }

    private static long getInterval(String tag){
        if(null == mapClickInterval.get(tag)){
            return DEFAULT_CLICK_INTERVAL;
        }else {
            return mapClickInterval.get(tag);
        }
    }


    /**
     * 判断是否快速点击
     *
     * @return TRUE 快速点击
     * @see [类、类#方法、类#成员]
     */
    public static boolean isFastDoubleClick(String tag) {
        long curTime = NtpTimeService.queryNtpTime();
        SuperLog.debug("lastClickTime","curTime = "+curTime+",  lastClickTime ="+lastClickTime);
        long timeSpace = curTime - lastClickTime;
        if (0 < timeSpace && timeSpace < getInterval(tag)) {
            return true;
        } else {
            lastClickTime = curTime;
            return false;
        }
    }
}