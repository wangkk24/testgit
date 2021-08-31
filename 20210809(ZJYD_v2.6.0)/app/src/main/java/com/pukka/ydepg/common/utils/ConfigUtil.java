package com.pukka.ydepg.common.utils;

import android.content.Context;
import android.text.TextUtils;

import com.pukka.ydepg.common.AppConfig;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.fileutil.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 读取应用配置工具类
 *
 * @author yinxing 00176760
 */
public class ConfigUtil {
    private static final String TAG = "ConfigUtil";
    private static final String FILENAME = "config.json";
    private static final String AREA_CODE_JSON = "area_code.json";

    private static AppConfig appConfig;

    /**
     * 获取应用存储的配置信息
     */
    public static synchronized AppConfig getConfig(Context context) {
        if (null != appConfig) {
            return appConfig;
        }
        appConfig = readConfig(context);
        int currentVersion = CommonUtil.getVersionCode();
        if (appConfig.getVersionCode() < currentVersion) {
            deleteBackupConfigFile(context);
            appConfig = readConfig(context);
            appConfig.setVersionCode(currentVersion);
            saveToFile(context, appConfig);
        }
        return appConfig;
    }

    /**
     * 读取配置文件，优先读取备份文件，然后再读取apk里的
     */
    private static AppConfig readConfig(Context context) {
        InputStream is = null;
        AppConfig appConfig = null;
        try {
            String filePath = getBackupConfigFilePath(context);
            File file = new File(filePath);
            if (file.exists()) {
                SuperLog.debug(TAG, "get config from " + FileUtil.getCanonicalPath(file));
                is = new FileInputStream(file);
                String content = FileUtil.getContent(is);
                SuperLog.info(TAG, "config.json is " + content);
                if (!TextUtils.isEmpty(content)) {
                    appConfig = JsonParse.json2Object(content, AppConfig.class);
                }
                //如果内容有问题，读取assets中的文件
                if (null == appConfig || TextUtils.isEmpty(appConfig.getEdsURL())) {
                    SuperLog.info2SD(TAG, "config.json from sdcard file is null ,read assets");
                    if(!file.delete()){
                        SuperLog.error(TAG, "Backup config file delete failed.");
                    }
                    appConfig = readConfigFromAssets(context);
                }

            } else {
                appConfig = readConfigFromAssets(context);
            }


        } catch (IOException e) {
            SuperLog.error(TAG, e);
        } finally {
            if (is == null) {
                SuperLog.error("ConfigParam", "cannot found config file in assets");
            }
            closeInputStream(is);

        }
        return appConfig;
    }

    /**
     * 从assets中读取默认文件
     *
     * @param context
     * @return
     */
    private static AppConfig readConfigFromAssets(Context context) {
        SuperLog.debug(TAG, "get config from assets");
        AppConfig appConfig = null;
        InputStream is = null;
        try {
            is = context.getAssets().open(FILENAME);
            String content = FileUtil.getContent(is);
            SuperLog.info(TAG, "config.json is " + content);
            if (!TextUtils.isEmpty(content)) {
                appConfig = JsonParse.json2Object(content, AppConfig.class);
            }
        } catch (IOException e) {
            SuperLog.error(TAG,e);
        } finally {
            if (is == null) {
                SuperLog.error("ConfigParam", "cannot found config file in assets");
            }
            closeInputStream(is);
        }
        return appConfig;
    }


    public static String getPayAreaCodeFromAssets(Context context) {
        InputStream is = null;
        String content = null;
        try {
            SuperLog.debug(TAG, "get data from assets");
            is = context.getAssets().open(AREA_CODE_JSON);

            content = FileUtil.getContent(is);
        } catch (IOException e) {
            SuperLog.error(TAG, e);
        } finally {
            if (is == null) {
                SuperLog.error("FeaturedDefaultData", "Cannot found pay file [area_code.json] in assets");
            }
            closeInputStream(is);
        }

        return content;
    }

    private static void deleteBackupConfigFile(Context context) {
        File file = new File(getBackupConfigFilePath(context));
        if (file.exists()) {
            if (file.delete()) {
                SuperLog.debug(TAG, "file delete success");
            }
        }
    }

    /**
     * 保存配置信息到备份文件中
     */
    public static void saveToFile(Context context, AppConfig appConfig) {
        String content = JsonParse.object2String(appConfig);
        String filePath = getBackupConfigFilePath(context);
        FileUtil.saveContentToFile(filePath, content);
    }

    /**
     * 获取备份文件路径
     */
    private static String getBackupConfigFilePath(Context context) {
        return context.getFilesDir() + File.separator + FILENAME;
    }

    private static void closeInputStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
                is = null;
            } catch (IOException e) {
                SuperLog.error(TAG, e);
            }
        }
    }
}