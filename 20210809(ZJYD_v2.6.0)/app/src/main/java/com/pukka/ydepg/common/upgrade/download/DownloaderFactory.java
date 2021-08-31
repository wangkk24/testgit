package com.pukka.ydepg.common.upgrade.download;

import android.content.Context;

import com.pukka.ydepg.common.upgrade.download.self.SelfDownloader;
import com.pukka.ydepg.common.upgrade.download.yuansheng.AndroidDownloader;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

import static com.pukka.ydepg.common.upgrade.UpgradeManager.COMMON_TAG;

public class DownloaderFactory {

    //下载器类型
    public static final String SELF    = "self";     //自定义下载器
    public static final String ANDROID = "android";  //安卓原生DownloadManager下载器

    public static IDownloader getDownloader(String type,IDownloadListener dp,Context context){
        if(ANDROID.equalsIgnoreCase(type)){
            return new AndroidDownloader(dp,context);
        } else {
            return new SelfDownloader(dp,context);
        }
    }

    //生成本地保存的升级文件名
    //输入:http://117.148.130.74:33500/UPGRADE/jsp/ftp/guwangapk/ZJYD_v2.1.0.5.7_CODE374_20200311_release.apk
    //返回:ZJYD_v2.1.0.5.7_CODE374_20200311_release.apk
    public static String getDownloadFileName(String url){
        try{
            int pos = url.lastIndexOf("/");
            return url.substring(pos+1);
        }catch (Exception e){
            SuperLog.error(COMMON_TAG,e);
            return "ZJYD.apk";
        }
    }
}