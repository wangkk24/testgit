package com.pukka.ydepg.common.constant;


/**
 * 白标定制使用的常量
 *
 * @author wanghaiting wwx190216
 * @date 2015/8/13
 */
public class Constant {
    /**
     * 白标资源下载路径
     */

    public static final String TAG = "ZJYD";

    public static final String RESOURCE_STRINGS_PATH   = "/string/";

    public static final String RESOURCE_COLOR_PATH     = "/resource.rc";

    public static final String RESOURCE_IMAGE_PATH     = "/img/";

    public static final String IMAGE_SUFFIX            = ".png";

    public static final String NINE_PATCH_IMAGE_SUFFIX = ".9";

    public static final String ORDERING_SWITCH         = "orderingSwitch";

    public static final String SKYWORTH                = "Skyworth";

    public static final String HISENSE                 = "hisense";

    public static final String MGV2000                 = "MGV2000";
    /**
     * 连接超时时间
     */
    public static final int TIMEOUT_MILLIS = 20;

    /**
     * 读取超时时间
     */
    public static final int READ_TIMEOUT = 60;

    public enum DesktopType{
        NORMAL,
        SIMPLE,
        CHILD
    }
}