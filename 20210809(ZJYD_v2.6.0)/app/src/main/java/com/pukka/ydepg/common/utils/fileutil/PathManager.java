package com.pukka.ydepg.common.utils.fileutil;

import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.utils.DeviceInfo;
import com.pukka.ydepg.common.utils.UtilBase;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

import java.io.File;

/**
 * App统一路径管理
 * 负责debug、release版本的区分
 * 含data/data、外部sdcard区分
 * app中不同功能组件目录管理，日志、升级、白标、缓存等
 */
public class PathManager
{
    private static final String TAG = PathManager.class.getSimpleName();

    private static String APP_ROOT_PATH;//文件路径管理中的基础路径

    static
    {
        //创维设备即使有MOUNTED权限判断也是没有，因此直接写即可
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || DeviceInfo.isSkyworth()) {
            APP_ROOT_PATH = Environment.getExternalStorageDirectory().getPath();      //SD卡已挂载，日志路径/sdcard
        } else {
            APP_ROOT_PATH = OTTApplication.getContext().getFilesDir().getPath(); //SD卡未挂载，日志路径/data/data/com.pukka/ydepg/files
        }
        APP_ROOT_PATH = APP_ROOT_PATH + File.separator + "YSPLOG" + File.separator;
    }

    /**
     * 获取日志存储路径
     * as:/mnt/sdcard/OTT/LOG/
     *
     * @return String
     */
    public static String getLogPath() {
        String absolutePath = APP_ROOT_PATH + "LOG" + File.separator;
        autoCreatePath(absolutePath);
        Log.d(TAG,"ZJYD log path = " + absolutePath);
        return absolutePath;
    }

    /**
     * video download path
     * as:/mnt/sdcard/OTT/DownLoad/
     *
     * @return String absolute path
     */
    public static String getDownLoadPath() {
        String absolutePath = APP_ROOT_PATH + "DownLoad" + File.separator;
        autoCreatePath(absolutePath);
        return absolutePath;
    }

    /**
     * peel resource
     * @return
     */
    public static String getPeelResourcePath() {
        String absolutePath = APP_ROOT_PATH + "PeelResource" ;
        autoCreatePath(absolutePath);
        return absolutePath;
    }

    /**
     * auto create the file's path
     */
    private static void autoCreatePath(String path) {
        if(TextUtils.isEmpty(path)) {
            return;
        }
        File file = new File(path);
        if(file.exists()) {
            return;
        }
        boolean result = file.mkdir();
        Log.i(TAG,"mkdir [" + path + "] result=" + result);
    }
}