package com.pukka.ydepg.common.upgrade.download.self;

import android.content.Context;

import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.v6bean.v6request.UpgradeRequest;
import com.pukka.ydepg.common.upgrade.download.DownloaderFactory;
import com.pukka.ydepg.common.upgrade.download.IDownloadListener;
import com.pukka.ydepg.common.upgrade.download.IDownloader;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class SelfDownloader implements IDownloader {

    private final static String TAG = SelfDownloader.class.getSimpleName();

    private IDownloadListener listener;

    private Context context;

    public SelfDownloader(IDownloadListener listener,Context context) {
        this.listener = listener;
        this.context  = context;
    }

    @SuppressWarnings("CheckResult")
    @Override
    public void download(String url) {
        HttpApi.getInstance().getService().downloadLauncher(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        responseBody -> {
                            SuperLog.info2SD(TAG, "Download [" + url + "] response<GET request> successfully.");
                            downloadFile(responseBody, DownloaderFactory.getDownloadFileName(url));
                        },
                        throwable -> {
                            //下载升级apk失败,升级结束
                            SuperLog.error(TAG, throwable);
                            listener.onFail();
                        }
                );
    }

    @SuppressWarnings("CheckResult")
    @Override
    public void download(String url, UpgradeRequest request) {
        HttpApi.getInstance().getService().upgrade(url,request.getRequest())
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                responseBody -> {
                    SuperLog.info2SD(TAG, "Download [" + url + "] response<POST request> successfully.");
                    downloadFile(responseBody, request.getFILENAME());
                },
                throwable -> {
                    //下载升级apk失败,升级结束
                    SuperLog.error(TAG, throwable);
                    listener.onFail();
                }
            );
    }

    private void downloadFile(ResponseBody responseBody,String fileName){
        InputStream      is  = null;//输入流
        FileOutputStream fos = null;//输出流
        try {
            is = responseBody.byteStream();//获取输入流
            long total = responseBody.contentLength();//获取文件大小
            File file = new File(getDownloadPath(context), fileName);// 设置路径
            SuperLog.info2SD(TAG,"Download APK path is : "+file.getAbsolutePath()+" Size="+total);
            if (is != null) {
                //fos = new FileOutputStream(file);

                //给下载的apk文件增加其他用户读权限,否则调用pm安装命令会失败,报错Failure [INSTALL_FAILED_INVALID_APK]
                //但是此权限在Android7.0后有问题，被声明为过时的，运行时会报错java.lang.SecurityException: MODE_WORLD_READABLE no longer supported
                //TODO 此处尚未解决请注意
                fos = context.openFileOutput(fileName,context.MODE_WORLD_READABLE);

                byte[] buf = new byte[1024];
                int ch;
                int process = 0;
                while ((ch = is.read(buf)) != -1) {
                    fos.write(buf, 0, ch);
                    process += ch;
                    float percentProgress = (float) process / (float) total * 100f;
                    listener.onProgress((int)percentProgress,100); //通知下载进度更新(total是实际大小)
                }
            }

            // 下载完成
            if (fos != null) {
                fos.flush();
                fos.close();
            }
            listener.onComplete(file.getAbsolutePath());
        } catch (Exception e) {
            SuperLog.error(TAG,e);
            listener.onFail();
        } finally {
            try {
                if (is != null){
                    is.close();
                }
            } catch (IOException e) {
                SuperLog.error(TAG,e);
            }
            try {
                if (fos != null){
                    fos.close();
                }
            } catch (IOException e) {
                SuperLog.error(TAG,e);
            }
        }
    }

    //下载文件路径,/data/data/包名/files
    private String getDownloadPath(Context context){
        return context.getFilesDir().getAbsolutePath();
    }
}