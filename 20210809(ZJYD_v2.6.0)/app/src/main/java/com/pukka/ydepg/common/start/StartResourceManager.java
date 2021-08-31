package com.pukka.ydepg.common.start;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.DownLoaderStartMedia;
import com.pukka.ydepg.common.http.DownloaderStartSource;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.StartPicture;
import com.pukka.ydepg.common.http.v6bean.v6node.StartPictureConfig;
import com.pukka.ydepg.common.http.v6bean.v6request.DownloadVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.GetStartPictureRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.DownloadVODResponse;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.fileutil.FileUtil;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StartResourceManager {

    //下载开机资源
    @SuppressLint("CheckResult")
    public void getStartResource() {
        GetStartPictureRequest request = new GetStartPictureRequest();
        request.setAction(GetStartPictureRequest.ACTION);
        request.setDeviceModel("STB");
        request.setPicType(GetStartPictureRequest.ANY);
        HttpApi.getInstance().getService().getStartPicture(HttpUtil.getVspUrl(HttpConstant.GET_START_PICTURE), request).subscribe(getStartPictureResponse -> {
            if (null != getStartPictureResponse && null != getStartPictureResponse.getResult()) {
                String retCode = getStartPictureResponse.getResult().getRetCode();
                if (TextUtils.equals(retCode, Result.RETCODE_OK) || TextUtils.equals(retCode, Result.RETCODE_OK_TWO)) {
                    String resourceUrl  = getStartPictureResponse.getUrl();
                    String resourceName = getStartPictureResponse.getPicturename();
                    String localResourceName = SharedPreferenceUtil.getInstance().getStartResource();
                    SuperLog.info2SDSecurity(Constant.TAG, "\n\tResourceUrl  = " + resourceUrl+" \n\tNetwork ResourceName = "+resourceName + "\n\tLocal   ResourceName = "+localResourceName);
                    //[网络资源包URL]和[网络资源包名]均不为空进入下载解压逻辑
                    if (!TextUtils.isEmpty(resourceUrl) && !TextUtils.isEmpty(resourceName)) {
                        if( !resourceName.equals(localResourceName)) {
                            //[本地资源包名] != [网络资源包名],需要下载
                            downloadStartResource(resourceUrl, resourceName);
                        } else if (!SharedPreferenceUtil.getInstance().getUnZipStartResource()){
                            //[本地资源包名] == [网络资源包名] 且上一次解压网络资源包失败则重新解压
                            boolean unZipResult = FileUtil.unZip(OTTApplication.getCachePath() + resourceName + ".zip", OTTApplication.getCachePath() + resourceName);
                            if(unZipResult){
                                FileUtil.unZip(OTTApplication.getCachePath() + SharedPreferenceUtil.getInstance().getStartResource() + "/" + DownloaderStartSource.STB_BOOT_FILE_NAME, DownloaderStartSource.STB_BOOT_FILE_DIR);
                                downloadStartVideo(OTTApplication.getCachePath() + resourceName);
                            }
                            SharedPreferenceUtil.getInstance().saveUnZipStartResource(unZipResult);
                        }
                    } else {
                        SuperLog.info2SD(Constant.TAG, "resourceUrl or resourceName is null, start picture will be cleared.");

                        //判断目录文件是否存在
                        if(!TextUtils.isEmpty(SharedPreferenceUtil.getInstance().getPreStartResource())) {
                            File file = new File(OTTApplication.getCachePath() + SharedPreferenceUtil.getInstance().getPreStartResource());
                            if ( file.exists() ) {
                                SuperLog.info2SD(Constant.TAG, "need delete file, path = " + OTTApplication.getCachePath() + SharedPreferenceUtil.getInstance().getPreStartResource());
                                FileUtil.deleteDir(OTTApplication.getCachePath() + SharedPreferenceUtil.getInstance().getPreStartResource());
                            }
                            //判断目录文件是否存在
                            File zipFile = new File(OTTApplication.getCachePath() + SharedPreferenceUtil.getInstance().getPreStartResource() + ".zip");
                            if (zipFile.exists()) {
                                SuperLog.info2SD(Constant.TAG, "need delete file, path = " + OTTApplication.getCachePath() + SharedPreferenceUtil.getInstance().getPreStartResource() + ".zip");
                                FileUtil.deleteDir(OTTApplication.getCachePath() + SharedPreferenceUtil.getInstance().getPreStartResource() + ".zip");
                            }
                        }
                        SharedPreferenceUtil.getInstance().saveUnZipStartResource(true);
                        SharedPreferenceUtil.getInstance().savePreStartResource(SharedPreferenceUtil.getInstance().getStartResource());
                        SharedPreferenceUtil.getInstance().saveStartResource(resourceName);
                    }
                } else {
                    if("157051001".equals(retCode)){
                        SuperLog.info2SD(Constant.TAG, "resourceUrl is null, start picture will be cleared.");

                        //判断目录文件是否存在
                        if(!TextUtils.isEmpty(SharedPreferenceUtil.getInstance().getPreStartResource())) {
                            File file = new File(OTTApplication.getCachePath() + SharedPreferenceUtil.getInstance().getPreStartResource());
                            if (!file.exists()) {
                            } else {
                                SuperLog.info2SD(Constant.TAG, "need delete file, path = " + OTTApplication.getCachePath() + SharedPreferenceUtil.getInstance().getPreStartResource());
                                FileUtil.deleteDir(OTTApplication.getCachePath() + SharedPreferenceUtil.getInstance().getPreStartResource());
                            }
                            //判断目录文件是否存在
                            File zipFile = new File(OTTApplication.getCachePath() + SharedPreferenceUtil.getInstance().getPreStartResource() + ".zip");
                            if (!zipFile.exists()) {
                            } else {
                                SuperLog.info2SD(Constant.TAG, "need delete file, path = " + OTTApplication.getCachePath() + SharedPreferenceUtil.getInstance().getPreStartResource() + ".zip");
                                FileUtil.deleteDir(OTTApplication.getCachePath() + SharedPreferenceUtil.getInstance().getPreStartResource() + ".zip");
                            }
                        }
                        SharedPreferenceUtil.getInstance().saveUnZipStartResource(true);
                        SharedPreferenceUtil.getInstance().savePreStartResource(SharedPreferenceUtil.getInstance().getStartResource());
                        SharedPreferenceUtil.getInstance().saveStartResource("");
                    }
                }
            }
        });
        SuperLog.info2SD(Constant.TAG,"[LOGIN-6(异步)]Send GetStartPicture finished.");
    }

    private void downloadStartResource(String resourceUrl, String resourceName){
        DownloaderStartSource task = new DownloaderStartSource(resourceUrl, resourceName);
        task.execute();
    }

    private void downloadStartVideo(String startSourceDir){
        SuperLog.debug(Constant.TAG, "downloadStartVideo");
        String startConfigPath = startSourceDir + "/" + DownloaderStartSource.START_CONFIG;
        String startConfigJson = FileUtil.getJsonStrFromUrl(startConfigPath);
        SuperLog.debug(Constant.TAG, "startConfigJson is " + startConfigJson);
        StartPictureConfig startPictureConfig = JsonParse.json2Object(startConfigJson, StartPictureConfig.class);
        if (startPictureConfig != null && startPictureConfig.getPicture() != null && startPictureConfig.getPicture().size() > 0) {
            List<String> vodIDs = new ArrayList<>();
            for(StartPicture startPicture:startPictureConfig.getPicture()){
                if("1".equals(startPicture.getType()) && !vodIDs.contains(startPicture.getVodID())){
                    vodIDs.add(startPicture.getVodID());
                    DownloadVODRequest downloadVODRequest = new DownloadVODRequest();
                    downloadVODRequest.setVODID(startPicture.getVodID());
                    List<String> mediaIDs = new ArrayList<>();
                    mediaIDs.add(startPicture.getMediaID());
                    downloadVODRequest.setMediaIDs(mediaIDs);
                    getVODPlayUrlEPGAndDownload(downloadVODRequest, startPicture.getVodID(), startSourceDir);
                }
            }
        }
    }

    private void getVODPlayUrlEPGAndDownload(DownloadVODRequest downloadVODRequest, String vodID, String startSourceDir) {
        SuperLog.debug(Constant.TAG, "getVODPlayUrlEPGAndDownload");
        HttpApi.getInstance().getService().downloadVOD(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.DOWNLOADVOD, downloadVODRequest)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DownloadVODResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(DownloadVODResponse downloadVODResponse) {
                        SuperLog.debug(Constant.TAG,"getVODPlayUrlEPG onSuccess");
                        if (downloadVODResponse != null
                                && downloadVODResponse.getResult() != null) {

                            Result result = downloadVODResponse.getResult();
                            if (result != null
                                    && !TextUtils.isEmpty(result.getRetCode())
                                    && result.getRetCode().equals(Result.RETCODE_OK)
                                    && downloadVODResponse.getDownloadURLs() != null && downloadVODResponse.getDownloadURLs().size() > 0) {

                                DownLoaderStartMedia task = new DownLoaderStartMedia(downloadVODResponse.getDownloadURLs().get(0), startSourceDir, vodID);
                                task.execute();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });

    }
}
