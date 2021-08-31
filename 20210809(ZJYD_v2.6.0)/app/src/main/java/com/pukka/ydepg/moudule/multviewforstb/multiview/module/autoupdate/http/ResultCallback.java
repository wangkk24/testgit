package com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.http;


import com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.bean.ResultBean;

public interface ResultCallback {
    void onSuccess(ResultBean response);

    void onError(String error);

    void onDownloadProgress(long totalSize, long downloadedSize);
}
