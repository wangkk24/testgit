package com.pukka.ydepg.common.upgrade.download;

import com.pukka.ydepg.common.http.v6bean.v6request.UpgradeRequest;

public interface IDownloader{
    //开始下载
    //URL:下载文件地址,形如 http://117.148.130.74:33500/UPGRADE/jsp/ftp/guwangapk/ZJYD_v2.1.0.5.7_CODE374_20200311_release.apk
    //GET请求方式下载
    void download(String url);

    //POST请求方式下载
    void download(String url, UpgradeRequest request);
}
