package com.pukka.ydepg.moudule.vod.utils;

import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.http.v6bean.v6node.SingleMark;
import com.pukka.ydepg.common.http.v6bean.v6response.GetSTBSingleMarksResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySTBOrderInfoResponse;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.OwnChoose.OwnChoosePresenter;
import com.pukka.ydepg.moudule.vod.presenter.VodDetailPBSConfigPresenter;

import java.util.ArrayList;
import java.util.List;

public class VodDetailPBSConfigUtils {
    private static final String TAG = "VodDetailPBSConfigUtils";

    private String vodId = "";

    private VodDetailPBSConfigPresenter presenter;

    //回调
    private ConfigCallBack callBack;

    //请求订购信息完毕
    private boolean configOrderInfoDone = false;

    //请求角标信息完毕
    private boolean configMarksInfoDone = false;

    //请求订购信息成功
    public boolean configOrderInfoSuccess = false;

    //请求角标信息成功
    public boolean configMarksSuccess = false;

    private List<SingleMark> singleMarks = new ArrayList<>();

    private QuerySTBOrderInfoResponse querySTBOrderInfoResponse;

    //用于每次鉴权判断展示UI的response
    private QuerySTBOrderInfoResponse mPlayVodquerySTBOrderInfoResponse;

    OwnChoosePresenter mOwnPresenter = new OwnChoosePresenter();


    public VodDetailPBSConfigUtils(String vodId) {
        presenter = new VodDetailPBSConfigPresenter();
        this.vodId = vodId;
    }

    public void initConfig(ConfigCallBack callBack){
        this.callBack = callBack;
        configOrderInfo();
        configMarksInfo();
    }

    //请求角标信息
    private void configMarksInfo(){
        SuperLog.debug(TAG, "configMarksInfo: start getSTBSingleMarks");
        presenter.getSTBSingleMarks(vodId, new VodDetailPBSConfigPresenter.getSTBSingleMarksCallBack() {
            @Override
            public void getSTBSingleMarksSuccess(GetSTBSingleMarksResponse response) {
                SuperLog.debug(TAG, "vodDetailPBSConfigUtils: getSTBSingleMarks success");
                if (null != response){
                    singleMarks = response.getSingleMarks();
                    configMarksInfoDone = true;
                    configMarksSuccess = true;
                }else {
                    configMarksInfoDone = true;
                    configMarksSuccess = false;
                }

                configDone();
            }

            @Override
            public void getSTBSingleMarksFail() {
                SuperLog.debug(TAG, "vodDetailPBSConfigUtils: getSTBSingleMarks fail");
                configMarksInfoDone = true;
                configMarksSuccess = false;
                configDone();
            }
        });
    }

    //请求订购信息
    private void configOrderInfo(){
        SuperLog.debug(TAG, "configMarksInfo: start querySTBOrderInfo");
        String productIdStr = SessionService.getInstance().getSession().getTerminalConfigurationValue(Configuration.Key.CHOI_PRODUCT);
        List<String> productIds = new ArrayList<>();

        SuperLog.debug(TAG, "productIdStr: " + productIdStr);
        if (!TextUtils.isEmpty(productIdStr)) {
            try {
                productIds = JsonParse.jsonToStringList(productIdStr);
            } catch (Exception e) {
                SuperLog.error(TAG, e);
            }
        }

        String productList = "";
        if (productIds.size() > 0){
            for (int i = 0; i < productIds.size(); i++) {
                String productId = productIds.get(i);
                if (i != 0){
                    productList = productList + "," + productId;
                }else{
                    productList = productId;
                }
            }
        }
        if (!"".equals(productList)){
            presenter.querySTBOrderInfo(productList, new VodDetailPBSConfigPresenter.QuerySTBOrderInfoCallBack() {
                @Override
                public void querySTBOrderInfoSuccess(QuerySTBOrderInfoResponse response) {
                    SuperLog.debug(TAG, "configMarksInfo: querySTBOrderInfo success");
                    if (null != response){
                        querySTBOrderInfoResponse = response;
                        configOrderInfoDone = true;
                        configOrderInfoSuccess = true;
                    }else{
                        configOrderInfoDone = true;
                        configOrderInfoSuccess = false;
                    }
                    configDone();
                }

                @Override
                public void querySTBOrderInfoFail() {
                    SuperLog.debug(TAG, "configMarksInfo: querySTBOrderInfo fail");
                    configOrderInfoDone = true;
                    configOrderInfoSuccess = false;
                    configDone();
                }
            });
        }else{
            SuperLog.debug(TAG, "configMarksInfo: querySTBOrderInfo fail 未配置productids");
            configOrderInfoDone = true;
            configOrderInfoSuccess = false;
            configDone();
        }

    }

    //用playVod返回的产品id请求订购信息
    public void configOrderInfo(String productId,GetOrderInfoCallBack callBack){
        SuperLog.debug(TAG, "configMarksInfo: start querySTBOrderInfo");
        if (!"".equals(productId)){
            presenter.querySTBOrderInfo(productId, new VodDetailPBSConfigPresenter.QuerySTBOrderInfoCallBack() {
                @Override
                public void querySTBOrderInfoSuccess(QuerySTBOrderInfoResponse response) {
                    SuperLog.debug(TAG, "configMarksInfo: querySTBOrderInfo success");
                    mPlayVodquerySTBOrderInfoResponse = response;
                    callBack.getOrderInfoDone();
                }

                @Override
                public void querySTBOrderInfoFail() {
                    SuperLog.debug(TAG, "configMarksInfo: querySTBOrderInfo fail");
                    mPlayVodquerySTBOrderInfoResponse = null;
                    callBack.getOrderInfoDone();
                }
            });
        }else{
            SuperLog.debug(TAG, "configMarksInfo: querySTBOrderInfo fail");
            mPlayVodquerySTBOrderInfoResponse = null;
            configDone();
        }

    }

    private void configDone(){
        if (!configOrderInfoDone || !configMarksInfoDone){
            return;
        }
        callBack.configDone();
    }

    public List<SingleMark> getSingleMarks() {
        return singleMarks;
    }

    public QuerySTBOrderInfoResponse getQuerySTBOrderInfoResponse() {
        return querySTBOrderInfoResponse;
    }

    public QuerySTBOrderInfoResponse getmPlayVodquerySTBOrderInfoResponse() {
        return mPlayVodquerySTBOrderInfoResponse;
    }

    public boolean isConfigOrderInfoSuccess() {
        return configOrderInfoSuccess;
    }

    public interface GetOrderInfoCallBack{
        void getOrderInfoDone();
    }

    public interface ConfigCallBack{
        void configDone();
    }
}
