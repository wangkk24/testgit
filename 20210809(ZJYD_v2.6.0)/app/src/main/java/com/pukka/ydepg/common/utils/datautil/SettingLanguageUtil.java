package com.pukka.ydepg.common.utils.datautil;

import android.content.res.Configuration;
import android.content.res.Resources;


import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.UtilBase;

import java.util.Locale;

/**
 * Switch language
 *
 * @author zhouliyun n005689
 */
public final class SettingLanguageUtil
{
    private static final String TAG = SettingLanguageUtil.class.getSimpleName();
    
    private SettingLanguageUtil() { }

    /**
     * set the app default language
     * the language was set by user
     */
    public static void setUserSelectedLanguage() {
        String currentLanguage = getLanguage();
        SuperLog.debug(TAG, "currentLanguage = " + currentLanguage);
        Locale locale = new Locale(currentLanguage);
        Locale.setDefault(locale);
        Resources resource =  UtilBase.getApplicationContext().getResources();
        Configuration config = resource.getConfiguration();
        config.locale = new Locale(currentLanguage);
        resource.updateConfiguration(config, resource.getDisplayMetrics());
    }

    /**
     * get current app language
     * @return language name
     */
    public static String getLanguage() {
        return SharedPreferenceUtil.getInstance().getCurrentLanguage();
    }
}