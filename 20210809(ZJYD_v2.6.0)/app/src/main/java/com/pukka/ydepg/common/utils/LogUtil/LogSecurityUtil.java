package com.pukka.ydepg.common.utils.LogUtil;

import com.pukka.ydepg.BuildConfig;

public class LogSecurityUtil {
    //[匹配内容] userToken='7kpR27kpR24IGLmm1eU5A6PyfHtQhQ1l'
    public final static String REGEX_TOKEN       = "userToken='[a-zA-z0-9_]*'";
    public final static String REPLACEMENT_TOKEN = "userToken=*******";

    //[匹配内容] subscriberID='7155036505'
    public final static String REGEX_SUBSCRIBER       = "subscriberID='[a-zA-z0-9]*'";
    public final static String REPLACEMENT_SUBSCRIBER = "subscriberID=*******";

    //[匹配内容] http://117.148.130.129:7350
    public final static String REGEX_HTTPURL       = ":\\/\\/[a-zA-z0-9.:]*";
    public final static String REPLACEMENT_HTTPURL = "://********";

    //路径遍历 漏洞修复
    public static String getSecurityLog(String log,String regex,String replacement) {
            return BuildConfig.DEBUG ? log : log.replaceAll(regex,replacement);
    }
}