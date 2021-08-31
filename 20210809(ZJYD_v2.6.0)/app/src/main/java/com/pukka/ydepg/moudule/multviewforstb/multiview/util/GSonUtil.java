package com.pukka.ydepg.moudule.multviewforstb.multiview.util;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.main.bean.AllVideoConfig;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.main.bean.Picture;

import java.util.Arrays;

public class GSonUtil {
    private static final String TAG = GSonUtil.class.getSimpleName();

    private static final String KEY_RESOURCENAME = "ResourceName";
    private static final String KEY_CONTENTID = "ContentID";
    private static final String KEY_RETURNURL = "ReturnURL";
    private static final String KEY_PICTURE = "picture";
    private static final String KEY_VIEWSIZES = "viewSizes";
    private static final String KEY_MEDIATYPE = "MediaType";
    private static final String KEY_VIEWTYPE = "Viewtype";
    private static final String KEY_BOOKMARK = "Bookmark";
    private static final String KEY_MAINCAMERAID = "mainCameraId";
    private static final String KEY_CAMERANUM = "CameraNum";
    private static final String KEY_CAMERADEGREE = "CameraDegree";
    private static final String KEY_RESOURCEPLAYURL = "ResourcePlayURL";

    public static AllVideoConfig getAllVideoConfig(Intent intent) {

        int mediaType = 2;
        int viewType = 0;
        long bookmark = 0;
        int mainCameraId = 0;
        int cameraNum = 0;
        int cameraDegree = 180;

        //打印所有传过来的数据
        Bundle bundle = intent.getExtras();
        if (null != bundle) {
            for (String key : bundle.keySet()) {
                DebugLog.info(TAG, "[getAllVideoConfig] key=" + key);
                Object data = bundle.get(key);
                if(data != null){
                    DebugLog.info(TAG, "[getAllVideoConfig] value=" + data + " " + bundle.get(key).getClass());
                }else{
                    DebugLog.error(TAG, "[getAllVideoConfig] value=null");
                }
            }
            //媒体类型
            Object oMediaType = bundle.get(KEY_MEDIATYPE);
            if (oMediaType instanceof Integer) {
                mediaType = (int) oMediaType;
            }else if (oMediaType != null) {
                try {
                    mediaType = Integer.parseInt(String.valueOf(oMediaType));
                } catch (Exception e) {
                    e.printStackTrace();
                    DebugLog.error(TAG, e.toString());
                }
            }

            //多机位的布局类型
            Object oViewType = bundle.get(KEY_VIEWTYPE);
            if (oViewType instanceof Integer) {
                viewType = (int) oViewType;
            }else if (oViewType != null) {
                try {
                    viewType = Integer.parseInt(String.valueOf(oViewType));
                } catch (Exception e) {
                    e.printStackTrace();
                    DebugLog.error(TAG, e.toString());
                }
            }

            //书签
            Object oBookmark = bundle.get(KEY_BOOKMARK);
            if (oBookmark instanceof Long) {
                bookmark = (long) oBookmark;
            }else if (oBookmark != null) {
                try {
                    bookmark = Long.parseLong(String.valueOf(oBookmark));
                } catch (Exception e) {
                    e.printStackTrace();
                    DebugLog.error(TAG, e.toString());
                }
            }

            //自由视角主机位ID
            Object oMainCameraId = bundle.get(KEY_MAINCAMERAID);
            if (oMainCameraId instanceof Integer) {
                mainCameraId = (int) oMainCameraId;
            }else if (oMainCameraId != null) {
                try {
                    mainCameraId = Integer.parseInt(String.valueOf(oMainCameraId));
                } catch (Exception e) {
                    e.printStackTrace();
                    DebugLog.error(TAG, e.toString());
                }
            }

            //自由视角总机位数量
            Object oCameraNum = bundle.get(KEY_CAMERANUM);
            if (oCameraNum instanceof Integer) {
                cameraNum = (int) oCameraNum;
            }else if (oCameraNum != null) {
                try {
                    cameraNum = Integer.parseInt(String.valueOf(oCameraNum));
                } catch (Exception e) {
                    e.printStackTrace();
                    DebugLog.error(TAG, e.toString());
                }
            }

            //自由视角的角度
            Object oCameraDegree = bundle.get(KEY_CAMERADEGREE);
            if (oCameraDegree instanceof Integer) {
                cameraDegree = (int) oCameraDegree;
            }else if (oCameraDegree != null) {
                try {
                    cameraDegree = Integer.parseInt(String.valueOf(oCameraDegree));
                } catch (Exception e) {
                    e.printStackTrace();
                    DebugLog.error(TAG, e.toString());
                }
            }

        }


        AllVideoConfig allVideoConfig = new AllVideoConfig();
        String resourceName = intent.getStringExtra(KEY_RESOURCENAME);
        String contentID = intent.getStringExtra(KEY_CONTENTID);
        String returnURL = intent.getStringExtra(KEY_RETURNURL);
        String pictureJson = intent.getStringExtra(KEY_PICTURE);
        String viewSizesJson = intent.getStringExtra(KEY_VIEWSIZES);
        DebugLog.info(TAG, "viewSizes = " + viewSizesJson);

        Picture picture = null;
        try {
            Gson gson = new Gson();
            picture = gson.fromJson(pictureJson, Picture.class);
        } catch (Exception e) {
            e.printStackTrace();
            DebugLog.error(TAG, e.toString());
        }

        String playURL = intent.getStringExtra(KEY_RESOURCEPLAYURL);
        DebugLog.info(TAG, "Original ResourcePlayURL = " + playURL);
        String enforceProgressiveScanning = intent.getStringExtra("enforceProgressiveScanning");
        if (!TextUtils.isEmpty(playURL)) {
            String resourcePlayUrl = playURL.replace("[", "").replace("]", "").replace("\\", "").replace("\"", "");
            DebugLog.info(TAG, "ResourcePlayURL = " + resourcePlayUrl);
            DebugLog.info(TAG, "Replace ResourcePlayURL = " + resourcePlayUrl);
            allVideoConfig.setResourcePlayURL(Arrays.asList(resourcePlayUrl.split(",")));
        }else{
            DebugLog.error(TAG, "ResourcePlayURL = null");
        }

        if (allVideoConfig.getResourcePlayURL().size() > 1) {
            allVideoConfig.setViewtype(viewType);
            allVideoConfig.setPicture(picture);
//                allVideoConfig.setViewSizes(gson.<List<ViewSize>>fromJson(viewSizesJson, new TypeToken<List<ViewSize>>(){}.getType()));
        } else {
            allVideoConfig.setCameraDegree(cameraDegree);
            allVideoConfig.setMainCameraId(mainCameraId);
            allVideoConfig.setCameraNum(cameraNum);
        }

        allVideoConfig.setBookmark(bookmark);
        allVideoConfig.setReturnURL(returnURL);
        allVideoConfig.setResourceName(resourceName);
        allVideoConfig.setContentID(contentID);
        allVideoConfig.setMediaType(mediaType);
        allVideoConfig.setEnforceProgressiveScanning(enforceProgressiveScanning);


        DebugLog.info(TAG, allVideoConfig != null ? allVideoConfig.toString() : "allVideoConfig is null");

        return allVideoConfig;
    }


}
