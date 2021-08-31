package com.pukka.ydepg.common.report.ubd.pbs;

import android.text.TextUtils;

import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.session.SessionService;

import java.util.Map;

import io.reactivex.schedulers.Schedulers;

//PBS user action analysis service
public class PbsUaService {
    @SuppressWarnings("checkResult")
    public static void report(Map<String,String> userData){
        String url = getPbsUserActionReportUrl(userData);
        HttpApi.getInstance().getService().sendGetRequest(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        response -> {
                            SuperLog.debug(PbsUaConstant.TAG, "Report user action to PBS successfully.Response message = " + response.string());
                        },
                        throwable -> {
                            SuperLog.error(PbsUaConstant.TAG,"Report user action to PBS exception.");
                            SuperLog.error(PbsUaConstant.TAG,throwable);
                        });
    }

    @SuppressWarnings("checkResult")
    public static void reportDesktop(Map<String,String> userData){
        String url = getPbsDesktopReportUrl(userData);
        SuperLog.info2SD(PbsUaConstant.TAG,"reportDesktop url = "+url);
        HttpApi.getInstance().getService().sendGetRequest(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        response -> {
                            SuperLog.debug(PbsUaConstant.TAG, "Report desktop info to PBS successfully.Response message = " + response.string());
                        },
                        throwable -> {
                            SuperLog.error(PbsUaConstant.TAG,"Report desktop info to PBS exception.");
                            SuperLog.error(PbsUaConstant.TAG,throwable);
                        });
    }

    //PBS接口地址如下：http://aikanvod.miguvideo.com/video/p/useraction.jsp?actiontype=99&key1=value&key2=value2&...keyN=valueN
    private static String getPbsUserActionReportUrl(Map<String,String> userData){
        //话单服务器地址
        StringBuilder url = new StringBuilder(PbsUaConstant.PBS_UBD_URL);

        //话单公共字段
        url.append("?userId=").append(SessionService.getInstance().getSession().getUserId());

        //话单定制字段
        for(String key:userData.keySet()){
            if(!TextUtils.isEmpty(userData.get(key))){
                url.append("&").append(key).append("=").append(userData.get(key));
            }
        }

        SuperLog.debug(PbsUaConstant.TAG,"Report user action to PBS url = " + url.toString());
        return url.toString();
    }

    //PBS接口地址如下：http://aikanvod.miguvideo.com/video/p/stb_getEPGInfo.jsp?actiontype=99&key1=value&key2=value2&...keyN=valueN
    private static String getPbsDesktopReportUrl(Map<String,String> userData){
        //话单服务器地址
        StringBuilder url = new StringBuilder(PbsUaConstant.PBS_DESKTOP_URL);

        //话单公共字段
        url.append("?userId=").append(SessionService.getInstance().getSession().getUserId());

        //话单定制字段
        for(String key:userData.keySet()){
            if(!TextUtils.isEmpty(userData.get(key))){
                url.append("&").append(key).append("=").append(userData.get(key));
            }
        }

        SuperLog.debug(PbsUaConstant.TAG,"Report desktop info to PBS url = " + url.toString());
        return url.toString();
    }
}