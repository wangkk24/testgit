package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.OwnChoose;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.bean.node.User;
import com.pukka.ydepg.common.http.v6bean.v6node.AlacarteChoosedContent;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6request.AddAlacarteChoosedContentRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.GetAlacarteChoosedContentsRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.AddAlacarteChoosedContentResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.GetAlacarteChoosedContentsResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryVODResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.VerifiedCodeResponse;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.livetv.presenter.base.BasePresenter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OwnChoosePresenter extends BasePresenter {
    private static final String TAG = "OwnChoosePresenter";

    //自选包产品包id
    private String productId = "";

    //自选包订购关系id
    private String subscriptionId = "";

    //自选集包剩余集数
    public String total = "";

    //添加剧集到自选集包中,入参为内容的外部id
    public void addAlacarteChooseContent(String contentId, Context context, AddAlacarteChooseContentCallBack callBack){
        AddAlacarteChoosedContentRequest request = new AddAlacarteChoosedContentRequest();
        request.setContentID(contentId);
        request.setProductID(productId);
        request.setSubscriptionID(subscriptionId);
        UserInfo mUserInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        if (null != mUserInfo)
        {
            request.setUserID(mUserInfo.getUserId());
            request.setAddUserID(mUserInfo.getUserId());
        }

        Log.i(TAG, "addAlacarteChooseContent: "+JsonParse.object2String(request));


        String interfaceName = HttpConstant.ADD_ALACARTE_CHOOSED_CONTENTS;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;

        RxCallBack<AddAlacarteChoosedContentResponse> rxCallBack = new RxCallBack<AddAlacarteChoosedContentResponse>(url, OTTApplication.getContext()){
            @Override
            public void onSuccess(AddAlacarteChoosedContentResponse response) {
                Log.i(TAG, "AddAlacarte onSuccess: "+ JsonParse.object2String(response));

                if (null != response && null != response.getResult()){
                    //添加成功
                    if (("0".equals(response.getResult().getRetCode())
                            || "0".equals(response.getResult().getReturnCode())
                            || "0".equals(response.getResult().getResultCode()))){
                        if (null != callBack){
                            callBack.addAlacarteChooseContentSuccsee();
                            return;
                        }
                    }else{
                        if (null != callBack){
                            callBack.addAlacarteChooseContentFail(response.getResult().getDescription());
                            return;
                        }
                    }
                }
                if (null != callBack){
                    callBack.addAlacarteChooseContentFail("添加失败");
                }
            }

            @Override
            public void onFail(Throwable e) {
                if (null != callBack){
                    callBack.addAlacarteChooseContentFail("添加失败");
                }
            }
        };
        HttpApi.getInstance().getService().addAlacarteChoosedContent(url,request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxCallBack);
    }

    //添加剧集到自选集包中,入参为内容的外部id
    public void getAlacarteChooseContent(String contentId, Context context, GetAlacarteChooseContentCallBack callBack){
        GetAlacarteChoosedContentsRequest request = new GetAlacarteChoosedContentsRequest();
        request.setContentID(contentId);
        request.setProductID(productId);
        UserInfo mUserInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        if (null != mUserInfo)
        {
            request.setUserID(mUserInfo.getUserId());
        }

        Log.i(TAG, "getAlacarteChooseContent: "+JsonParse.object2String(request));


        String interfaceName = HttpConstant.GET_ALACARTE_CHOOSED_CONTENTS;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;

        RxCallBack<GetAlacarteChoosedContentsResponse> rxCallBack = new RxCallBack<GetAlacarteChoosedContentsResponse>(url, OTTApplication.getContext()){
            @Override
            public void onSuccess(GetAlacarteChoosedContentsResponse response) {
                Log.i(TAG, "AddAlacarte onSuccess: "+ JsonParse.object2String(response));

                if (null != response && null != response.getResult() ){
                    //查询成功
                    if (("0".equals(response.getResult().getRetCode())
                            || "0".equals(response.getResult().getReturnCode())
                            || "0".equals(response.getResult().getResultCode()))
                            && null != response.getAlacarteChoosedContents() && response.getAlacarteChoosedContents().size() > 0){
                        if (null != callBack){
                            callBack.getAlacarteChooseContentSuccsee(response.getAlacarteChoosedContents());
                            return;
                        }
                    }else{
                        if (null != callBack){
                            callBack.getAlacarteChooseContentFail(response.getResult().getDescription());
                            return;
                        }
                    }
                }
                if (null != callBack){
                    callBack.getAlacarteChooseContentFail("查询失败");
                }
            }

            @Override
            public void onFail(Throwable e) {
                if (null != callBack){
                    callBack.getAlacarteChooseContentFail("查询失败");
                }
            }
        };
        HttpApi.getInstance().getService().getAlacarteChoosedContents(url,request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxCallBack);
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }


    public interface AddAlacarteChooseContentCallBack{
        void addAlacarteChooseContentSuccsee();
        void addAlacarteChooseContentFail(String descirbe);
    }

    public interface GetAlacarteChooseContentCallBack{
        void getAlacarteChooseContentSuccsee(List<AlacarteChoosedContent> alacarteChoosedContents);
        void getAlacarteChooseContentFail(String descirbe);
    }
}
