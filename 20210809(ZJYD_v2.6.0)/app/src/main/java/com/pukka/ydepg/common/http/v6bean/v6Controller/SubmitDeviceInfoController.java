package com.pukka.ydepg.common.http.v6bean.v6Controller;

import android.content.Context;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.v6bean.v6request.SubmitDeviceInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.SubmitDeviceResponse;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.bean.node.Configuration;
import com.pukka.ydepg.launcher.session.Session;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.search.presenter.BasePresenter;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6Controller.SubmitDeviceInfoController.java
 * @author:xj
 * @date: 2018-01-30 10:31
 */
public class SubmitDeviceInfoController extends BasePresenter {
    private static final String TAG = "SubmitDeviceInfoController";

    public static void submitDeviceInfo(Context context) {
        SubmitDeviceInfoRequest request = new SubmitDeviceInfoRequest();
        request.setDeviceType("HUAWEI");
        Session session = SessionService.getInstance().getSession();
        Configuration configuration = session.getServerConfiguration();
        String xmppDomain = "";
        if (null != configuration){
            xmppDomain = configuration.getIMDomain();
        }
        String JID = session.getUserId() + "@" + xmppDomain + (TextUtils.isEmpty(session.getDeviceId()) ? "" : ("/" + session.getDeviceId()));
        request.setDeviceToken(JID);
        //SuperLog.debug(TAG, "submitDeviceInfo request" + JsonParse.object2String(request));
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.SUBMITDEVICEINFO;
        HttpApi.getInstance().getService().submitDeviceInfo(url, request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxCallBack<SubmitDeviceResponse>(HttpConstant.SUBMITDEVICEINFO, context) {
                    @Override
                    public void onSuccess(SubmitDeviceResponse submitDeviceResponse) {
                        //SuperLog.info2SD(TAG, "submitDeviceInfo onSuccess" + JsonParse.object2String(submitDeviceResponse));
                    }

                    @Override
                    public void onFail(Throwable e) {
                        SuperLog.error(TAG, e);
                    }
                });
    }
}
