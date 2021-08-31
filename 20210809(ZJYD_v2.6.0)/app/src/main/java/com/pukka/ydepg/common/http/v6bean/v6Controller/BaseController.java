package com.pukka.ydepg.common.http.v6bean.v6Controller;

import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.report.error.ErrorInfoReport;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.errorWindowUtil.OTTErrorWindowUtils;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by liudo on 2018/2/27.
 */
public class BaseController {
    private final String TAG = this.getClass().getSimpleName();

    protected RxAppCompatActivity rxAppCompatActivity;

    /**
     * 处理有返回结的错误
     *
     * @param errorCode     返回错误码
     * @param interfaceName 接口名，从error.json里面查找，匹配对用户友好的错误提示，如果不提示传null。
     */
    protected void handleError(String errorCode, String interfaceName) {
        //上报错误码到探针中间件
        ErrorInfoReport.getInstance().reportErrorCode(interfaceName,errorCode);

        //超时错误更新token,不弹错误提示;其他错误弹错误提示
        if (CommonUtil.isSessionError(errorCode)) {
            reportTokenTimeOut();
        } else {
            OTTErrorWindowUtils.getErrorInfoFromPbs(interfaceName,errorCode,null);
        }
    }

    /**
     *
     * 专门处理session out
     * @param errorCode
     */
    void handleError(String errorCode){
        //上报错误码到探针中间件 这里修改不便,且场景稀少,就暂时不上报了
        //ErrorInfoReport.getInstance().reportErrorCode(errorCode);
        if (CommonUtil.isSessionError(errorCode)) {  //超时后处理
            reportTokenTimeOut();
        }
    }

    /**
     * 报告token失效
     */
    private void reportTokenTimeOut() {
        SuperLog.error(TAG,"SessionTimeOut,begin to reportTokenStatus(UpdateToken)");
        new Thread(() -> AuthenticateManager.getInstance().reportTokenStatus()).start();
    }
}
