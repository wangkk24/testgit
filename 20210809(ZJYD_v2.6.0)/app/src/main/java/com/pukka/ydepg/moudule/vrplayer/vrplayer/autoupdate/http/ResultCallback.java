package com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate.http;


import com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate.bean.ResultBean;

public interface ResultCallback {
    void onSuccess(ResultBean response);

    void onError(String error);

    void onDownloadProgress(long totalSize, long downloadedSize);
}
