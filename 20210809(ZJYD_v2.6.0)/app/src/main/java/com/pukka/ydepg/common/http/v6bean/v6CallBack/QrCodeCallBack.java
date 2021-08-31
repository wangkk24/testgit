package com.pukka.ydepg.common.http.v6bean.v6CallBack;

import com.pukka.ydepg.common.http.v6bean.v6response.QrCodeAuthenticateResponse;

/**
 * 生成二维码回调
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6CallBack.QrCodeCallBack.java
 * @author:xj
 * @date: 2018-01-09 15:22
 */

public interface QrCodeCallBack {
    void qrCodeCallBackSuccess(QrCodeAuthenticateResponse authenticateResponse);
    void qrCodeCallBackFailed();
}
