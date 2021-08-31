package com.pukka.ydepg.common.http.v6bean.v6CallBack;

import com.pukka.ydepg.common.errorcode.ErrorMessage;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryQrCodeStatusResponse;

/**
 * 查询二维码状态回调
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6CallBack.QueryQrCodeStatusCallBack.java
 * @author:xj
 * @date: 2018-01-09 17:05
 */

public interface QueryQrCodeStatusCallBack {
    void queryQrCodeStatusSuccess(QueryQrCodeStatusResponse qrCodeStatusResponse);
    void queryQrCodeStatusFailed(ErrorMessage message);
}
