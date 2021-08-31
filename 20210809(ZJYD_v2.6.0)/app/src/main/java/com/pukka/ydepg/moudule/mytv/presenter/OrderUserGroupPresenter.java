package com.pukka.ydepg.moudule.mytv.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.bean.response.QueryProductInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.CustomGroup;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6request.GetLabelsByUserIdRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryProductRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.GetLabelsByUserIdResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryProductResponse;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.mvp.presenter.BasePresenter;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.mytv.presenter.view.OrderCenterView;

import com.trello.rxlifecycle2.LifecycleTransformer;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderUserGroupPresenter extends BasePresenter {

    private static final String TAG = "OrderUserGroupPresenter";

    private OkHttpClient client;

    public OrderUserGroupPresenter() {
        client = new OkHttpClient();
    }

    public void getLabelsByUserId(String userId, GetUserGroupCallback callback){

        GetLabelsByUserIdRequest getLabelsByUserIdRequest = new GetLabelsByUserIdRequest();
        getLabelsByUserIdRequest.setVt(GetLabelsByUserIdRequest.VT);
        getLabelsByUserIdRequest.setSceneId(GetLabelsByUserIdRequest.SCENE_ID);
        getLabelsByUserIdRequest.setType(GetLabelsByUserIdRequest.TYPE);

        StringBuilder sb = new StringBuilder(HttpConstant.GET_LABELS_BY_USER_URL).append("?");
        sb.append("&type=").append(getLabelsByUserIdRequest.getType());
        sb.append("&sceneId=").append(getLabelsByUserIdRequest.getSceneId());
        sb.append("&vt=").append(getLabelsByUserIdRequest.getVt());
        String jsessionid = SharedPreferenceUtil.getInstance().getSeesionId();
        if (!jsessionid.contains("JSESSIONID=")) {
            jsessionid = "JSESSIONID=" + jsessionid;
        }

        Request request=new Request.Builder()
                .header("Cookie", jsessionid)
                .header("Set-Cookie", jsessionid + "; Path=/VSP/")
                .header("User-Agent", "OTT-Android")
                .header("authorization", SessionService.getInstance().getSession().getUserToken())
                .header("EpgSession",jsessionid)
                .header("Location", ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL())
                .url(sb.toString())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SuperLog.error(TAG,"getLabelsByUser onFailure");
                SuperLog.error(TAG, e);
                if(null != callback){
                    callback.getLabelsByUserIdFail();
                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body=response.body().string();
                Log.i(TAG, "onResponse:  "+ body);
                if(response.isSuccessful()&&!TextUtils.isEmpty(body)){
                    String formBody = body.replaceAll("\\s*", "");
                    GetLabelsByUserIdResponse getLabelsByUserIdResponse = JsonParse.json2Object(formBody,GetLabelsByUserIdResponse.class);
                    if (null != getLabelsByUserIdResponse){
                        List<CustomGroup> customGroups = getLabelsByUserIdResponse.getCustomGroups();
                        if(null != callback){
                            callback.getLabelsByUserIdSuccess(customGroups);
                        }
                    }else{
                        if(null != callback){
                            callback.getLabelsByUserIdFail();
                        }
                    }
                }else{
                    if(null != callback){
                        callback.getLabelsByUserIdFail();
                    }
                }
            }
        });

    }

    public interface GetUserGroupCallback{
        void getLabelsByUserIdSuccess(List<CustomGroup> customGroups);
        void getLabelsByUserIdFail();
    }
}
