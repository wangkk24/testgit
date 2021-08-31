package com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.http;

import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.bean.ResultBean;

public class BaseResultCall implements ResultCallback {
    private static final String TAG = BaseResultCall.class.getSimpleName();


    @Override
    public void onSuccess(ResultBean response) {
        DebugLog.debug(TAG,response.toString());
    }

    @Override
    public void onError(String error) {
        DebugLog.error(TAG,error);
    }

    @Override
    public void onDownloadProgress(long totalSize, long downloadedSize) {
    }
}
