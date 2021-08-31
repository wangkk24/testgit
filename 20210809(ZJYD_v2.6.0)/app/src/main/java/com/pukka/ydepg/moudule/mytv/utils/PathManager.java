package com.pukka.ydepg.moudule.mytv.utils;

import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.fileutil.FileUtil;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * App unified path management
 * Responsible for debug, release version distinction
 * Containing *** / ***, to distinguish between external sdcard
 * App in different functional components directory management, logging, upgrade, white label,
 * caching, etc.
 */
public class PathManager
{
    private static final String TAG = PathManager.class.getSimpleName();
    /**
     * Management of the file path base path
     */
    private static String APP_ROOT_PATH = "";

    static {
        File fileDir = null;
        if (OTTApplication.getContext() != null) {
            fileDir = OTTApplication.getContext().getFilesDir();
        }

        if (null != fileDir) {
            APP_ROOT_PATH = FileUtil.getCanonicalPath(fileDir) + File.separator;
        }
    }

    public static String getVSPCachePath()
    {
        String absolutePath = APP_ROOT_PATH + "Cache" + File.separator;
        autoCreatePath(absolutePath);
        return absolutePath;
    }

    /**
     * auto create the file's path
     */
    public static void autoCreatePath(String path)
    {
        if (OTTApplication.getContext() == null) {
            return;
        }

        if (TextUtils.isEmpty(path)) {
            return;
        }

        File file = FileUtils.getFile(path);
        if (null == file || file.exists()) {
            return;
        }

        boolean result = file.mkdirs();
        SuperLog.debug(TAG, "mkdirs result=" + result);
    }
}