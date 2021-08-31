package com.pukka.ydepg.common.http.v6bean.v6CallBack;

/**
 * 退出二维码认证状态回调
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6CallBack.QuitQrCodeCallBack.java
 * @author:xj
 * @date: 2018-01-10 15:08
 */

public interface QuitQrCodeCallBack {
    void quitQrCodeSuccess();
    void quitQrCodeFailed();
}
