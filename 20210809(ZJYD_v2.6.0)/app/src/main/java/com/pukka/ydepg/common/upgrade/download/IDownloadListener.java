package com.pukka.ydepg.common.upgrade.download;

public interface IDownloadListener {

    //下载完成
    void onComplete(String file);

    //下载失败
    void onFail();

    //下载进度通知
    void onProgress(int progress, int max);
}
