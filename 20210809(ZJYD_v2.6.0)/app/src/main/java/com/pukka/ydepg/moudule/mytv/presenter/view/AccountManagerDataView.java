package com.pukka.ydepg.moudule.mytv.presenter.view;

import com.pukka.ydepg.common.errorcode.ErrorMessage;
import com.pukka.ydepg.common.http.v6bean.v6response.QrCodeAuthenticateResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryBindedSubscriberResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryQrCodeStatusResponse;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.moudule.mytv.presenter.view.AccountManagerDataView.java
 * @author:xj
 * @date: 2018-01-24 16:11
 */

public interface AccountManagerDataView {
    void qrCodeCallBackSuccess(QrCodeAuthenticateResponse authenticateResponse);

    void qrCodeCallBackFailed();

    void queryQrCodeStatusSuccess(QueryQrCodeStatusResponse qrCodeStatusResponse);

    void queryQrCodeStatusFailed(ErrorMessage message);

    void quitQrCodeSuccess();

    void quitQrCodeFailed();

    void qeryBindedSubscriberSuccess(QueryBindedSubscriberResponse queryBindedSubscriberResponse);

    void qeryBindedSubscriberFailed(ErrorMessage message);

    void unBindSubsrciberSuccess();

    void unBindSubsrciberFailed();
}
