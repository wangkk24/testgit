package com.pukka.ydepg.moudule.vrplayer.vrplayer.global;

import android.content.Context;
import android.text.TextUtils;


import com.pukka.ydepg.moudule.vrplayer.vrplayer.utils.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 功能描述
 *
 * @author l00477311
 * @since 2020-08-08
 */
public class AreaSpecialProperties {
    private static final String TAG = "AreaSpecialProperties";

    private static final String CONFIG_NAME = "config.ini";

    /**
     * 配置文件config.ini已经加载
     * true-已加载
     * false-未加载
     */
    private static boolean isInited = false;

    /**
     * 单次seek时间，单位秒
     */
    private static int singleSeekTime = 5;

    /**
     * 长按键seek速度，单位百分比/秒
     */
    private static int longSeekSpeed = 10;

    /**
     * 菜单出现后，展示多久后自动隐藏
     */
//    private static int menuShowTime = 3;
            //MARK:TODO -- 修改菜单出现后，展示多久后自动隐藏
    private static int menuShowTime = 5;
    /**
     * fov视角巡航速度
     */
    private static int fovCruiseSpeed = 30;

    /**
     * 单交按键，fov转动角度
     */
    private static int singleFovDelegate = 10;

    /**
     *
     * 加载config.ini文件内容
     *
     * @param context
     */
    public static void initResource(Context context) {
        if (context == null) {
            LogUtil.logE(TAG, "initResource: context is null");
        }
        if (isInited) {
            return;
        }
        LogUtil.logI(TAG, "AreaSpecialProperties initResource");
        Properties prop = new Properties();
        try (InputStream in = context.getAssets().open(CONFIG_NAME)) {
            prop.load(in);
            loadData(prop);
            isInited = true;
        } catch (IOException e) {
            LogUtil.logE(TAG, "can't load config.ini");
        }
    }

    private static void loadData(Properties prop) {
        singleSeekTime = getInt(prop, "singleSeekTime", 5);
        longSeekSpeed = getInt(prop, "longSeekSpeed", 10);
        menuShowTime = getInt(prop, "menuShowTime", 3);
        fovCruiseSpeed = getInt(prop, "fovCruiseSpeed", 30);
        singleFovDelegate = getInt(prop, "singleFovDelegate", 10);
    }

    private static int getInt(Properties prop, String key, int defaultVale) {
        String value = prop.getProperty(key);
        if (TextUtils.isEmpty(value) || !TextUtils.isDigitsOnly(value)) {
            return defaultVale;
        }
        return Integer.parseInt(value);
    }

    public static int getSingleSeekTime() {
        return singleSeekTime;
    }

    public static int getLongSeekSpeed() {
        return longSeekSpeed;
    }

    public static int getMenuShowTime() {
        return menuShowTime;
    }

    public static int getFovCruiseSpeed() {
        return fovCruiseSpeed;
    }

    public static int getSingleFovDelegate() {
        return singleFovDelegate;
    }
}
