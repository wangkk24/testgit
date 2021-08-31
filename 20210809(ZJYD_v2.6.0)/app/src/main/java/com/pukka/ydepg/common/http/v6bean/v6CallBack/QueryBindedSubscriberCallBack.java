package com.pukka.ydepg.common.http.v6bean.v6CallBack;

import com.pukka.ydepg.common.errorcode.ErrorMessage;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryBindedSubscriberResponse;

/**
 * 查询绑定列表回调
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6CallBack.QueryBindedSubscriberCallBack.java
 * @author:xj
 * @date: 2018-01-09 17:58
 */

public interface QueryBindedSubscriberCallBack {
    void qeryBindedSubscriberSuccess(QueryBindedSubscriberResponse queryBindedSubscriberResponse);
    void qeryBindedSubscriberFailed(ErrorMessage message);
}
