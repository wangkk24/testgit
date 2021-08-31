package com.pukka.ydepg.moudule.vrplayer.vrplayer.utils;

import com.huawei.ott.sdk.log.DebugLog;

/**
 * 功能描述: 日志工具类
 *
 * @author l00477311
 * @since 2020-07-29
 */
public class LogUtil {
    private static final String PREFIX = "VRPLAYER_";

    public static void logI(String tag, String msg) {
        DebugLog.info(PREFIX + tag, msg);
    }

    public static void logD(String tag, String msg) {
        DebugLog.debug(PREFIX + tag, msg);
    }

//    public static void logW(String tag, String msg) { Log.w(PREFIX + tag, msg); }

    public static void logE(String tag, String msg) {
        DebugLog.error(PREFIX + tag, msg);
    }
}
