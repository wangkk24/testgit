package com.pukka.ydepg.moudule.vrplayer.vrplayer.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.global.Constant;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.module.Picture;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.module.VideoBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

/**
 * 功能描述 通用工具类
 *
 * @author l00477311
 * @since 2020-07-23
 */
public class CommonTools {
    private static final String TAG = "CommonTools";

    /**
     * 从intent中获取视频信息
     *
     * @param intent 入参
     * @return videoBean
     */
    public static VideoBean parseVideoInfoByIntent(Intent intent) {

        // 打印出实际传过来的extra信息
        String _debugTag = "VR_PLAYER_DEBUG_TAG";
        Bundle bundle = intent.getExtras();
        Set<String> bundleKeySet = bundle.keySet();
        for (String key : bundleKeySet) {
            Log.e(_debugTag, "parseVideoInfoByIntent: " + key + ">>>" + bundle.getString(key));
        }
        // 打印出实际传过来的extra信息

        try {
            // 将传参转换为JSONObject(params)
            JSONObject params = new JSONObject();

            Set<String> keys = bundle.keySet();
            for (String key : keys) {

                params.put(key, bundle.get(key));
            }

            String key = "[\"http:\\/\\/192.170.107.128\\/88888888\\/16\\/20200521\\/268436289\\/index.m3u8?rrsip=192.170.107.128&zoneoffset=0&servicetype=0&icpid=&accounttype=1&limitflux=-1&limitdur=-1&tenantId=8601&accountinfo=oA2DJ8uZBN392UthOC2727uX2GVOS%2FVRFMDML2u%2BJ7BX01CarGOlHRpko2A8fA8xyLYsXkXiXlSbEtjRc58x%2FTxpK34dmioqHZCOs1yi6Z6w9Id2G11bkAW%2FbnyQTwhn10578549e62ae6551240642b0e7d9ec3%3A20200903183839%3AUTC%2C10001000093431%2C173.8.0.235%2C20200903183839%2Ccp0010000017979%2C10001000093431%2C-1%2C1%2C0%2C%2C%2C1%2C1%2C%2C%2C1%2C10000001920069%2C2%2C10000001920069%2CA400E272302A%2C%2C%2C1%2C0%2CEND&GuardEncType=2&it=H4sIAAAAAAAAAE2OQQrCMBREb5NlaNTGZJGVIggSBatb-Ul-Y2naaFIFb28rXbic4b1hhgQW91sl1gJKzhwYzlbIV9IY5CU3iGIp68KSjE8d1YJYCKHpvY5u0q7nzY3JBWXrgsqCMkGqaXAXwKvix-pXZzDNYRTPmN6NReVyTd-QKXif0MPQxJ6eAnwuKcwIwWo-179CIMMUKsjtWJA75E3sHpDQHaL_caqGkJE8wLbgUUOHf94xufHEFwliwuntAAAA\"]";

            int resourceType = Integer.parseInt(TextUtils.isEmpty(intent.getStringExtra("ResourceType")) ? "3" : intent.getStringExtra("ResourceType"));
            DebugLog.info("打印出resourceType" + resourceType);
            String resource = intent.getStringExtra("ResourceType");
            DebugLog.info(resource);
            long bookmark = Integer.parseInt(TextUtils.isEmpty(intent.getStringExtra("Bookmark")) ? "1" : intent.getStringExtra("Bookmark"));
            String pictureJson = intent.getStringExtra("picture");
            Gson gson = new Gson();
            Picture picture = gson.fromJson(pictureJson, Picture.class);

            Log.e(_debugTag, "解析后的JSON:" + params.toString());

            // JSONObject(params)中的实际的ResourcePlayURL有问题 : ["http:\/\/192.170.107.128\/88888888\/16\/20200521\/268436289\/index.m3u8?rrsip=192.170.107.128&zoneoffset=0&servicetype=0&icpid=&accounttype=1&limitflux=-1&limitdur=-1&tenantId=8601&accountinfo=m71UzSFxnC3f17URiHAqMil4dPPX7LA30InL%2F9cWuOYcXZ%2F24U%2FN6WlYcjD9iIEvIZeo5RPEMdMvCtJRMVCUfFE6%2F2PKVXjDlV%2FILcus2BXY5OXMliuhBoFGJF0rN5Cw749800360349ad68ca8d8d39268f761f%3A20200903110347%3AUTC%2C10001000093431%2C173.8.0.235%2C20200903110347%2Ccp0010000017979%2C10001000093431%2C-1%2C1%2C0%2C%2C%2C1%2C1%2C%2C%2C1%2C10000001920069%2C2%2C10000001920069%2CA400E272302A%2C%2C%2C1%2C0%2CEND&GuardEncType=2&it=H4sIAAAAAAAAAE2Oyw6CMBRE_6bLpoC8Fl1pTExMNQHdmkt7qYRCsQUS_14gLFzO5JzJjA4kXk5cylhWWa4ilkaHKE-AxVlYYRpDwmSdIvH4EZaHRIIxTa-FVav2LI6vIA9pkDKaMxpkpFwHzwY0Zxsrpq5Ct4dFLNDNjUSufE1n8BS0dqhhbGxP7wa-D2d2hGC5n-snY8i4hhJ8uxTkDf5ouwEcqqvVG8drMB7JALIFjQI6_PNuTi0nfuClbRrtAAAA"]
            if (params.has("ResourcePlayURL") && params.getString("ResourcePlayURL").contains("[") && params.getString("ResourcePlayURL").contains("]")) {
                String unexpectedUrl = params.getString("ResourcePlayURL");
                unexpectedUrl = unexpectedUrl.replace("[", "");
                unexpectedUrl = unexpectedUrl.replace("]", "");
                unexpectedUrl = unexpectedUrl.replace("\"", "");
                unexpectedUrl = unexpectedUrl.replace("\\", "");

                Log.e(_debugTag, "处理后的字符串:" + params.toString());
                params.put("ResourcePlayURL", unexpectedUrl);

            }

            Log.e(_debugTag, "修复后的JSON:" + params.toString());
            String jString = params.toString();
            jString = jString.replaceAll("\\\\", "");
            Log.e(_debugTag, "最终转义JSON字符串:" + params.toString());

            JSONObject finalJObject = new JSONObject(jString);

            VideoBean _bean = new VideoBean();
            _bean.setContentID(finalJObject.getString("ContentID"));
            _bean.setMediaType(finalJObject.getInt("MediaType"));
            _bean.setResourceName(finalJObject.getString("ResourceName"));
            _bean.setResourcePlayURL(Arrays.asList(finalJObject.getString("ResourcePlayURL")));

            if (bundle.getString("ResourceType") == null){
                _bean.setResourceType(4);
            }else {
                _bean.setResourceType(finalJObject.getInt("ResourceType"));
            }
            _bean.setBookmark(finalJObject.getLong("Bookmark"));
            _bean.setReturnURL(finalJObject.getString("ReturnURL"));

            if (finalJObject.getInt("3DType") == 1){
                _bean.setType3D(2);
            }else if (finalJObject.getInt("3DType") == 2){
                _bean.setType3D(1);
            }
            Log.e(_debugTag, "最终VideoBean:" + _bean.toString());
            return _bean;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        Gson gson = new Gson();
        String json = intent.getStringExtra(Constant.VR_JSON);
        return gson.fromJson(json, VideoBean.class);

    }

    /**
     *
     *  根据intent启动Activity
     *
     * @param context 应用上下文
     * @param intent  用于启动Activity的intent对象
     */
    public static void startActivityByIntent(Context context, Intent intent) {
        if (context == null || intent == null) {
            LogUtil.logE(TAG, "context or intent is null");
            return;
        }
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            LogUtil.logE(TAG, intent.getClass().getName() + " is not found");
        }
    }

    /**
     * 视频时长格式化 00:00:00
     *
     * @param time
     * @return
     */
    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ? String.format(Locale.ROOT, "%02d:%02d:%02d", hours, minutes, seconds)
                : String.format(Locale.ROOT, "%02d:%02d", minutes, seconds);
    }


}