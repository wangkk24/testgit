package com.pukka.ydepg.moudule.vod.presenter;

import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.v6bean.v6response.GetSTBSingleMarksResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PBSRemixRecommendResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySTBOrderInfoResponse;
import com.pukka.ydepg.common.toptool.EpgTopFunctionMenu;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.session.SessionService;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VodDetailPBSConfigPresenter {

    private static final String TAG = "VodDetailPBSConfigPrese";

    private OkHttpClient client;

    public VodDetailPBSConfigPresenter() {
        this.client = new OkHttpClient();;
    }

    /*
     *查询订购信息
     *入参productList需用","将productId拼接
     */
    public void querySTBOrderInfo(String productList,QuerySTBOrderInfoCallBack callBack) {
        StringBuffer sb = new StringBuffer(HttpConstant.STB_ORDERINFO).append("?");
        sb.append("productIdList=").append(productList);
        sb.append("&vt=").append("9");
        String jsessionid = SharedPreferenceUtil.getInstance().getSeesionId();
        if (!jsessionid.contains("JSESSIONID=")) {
            jsessionid = "JSESSIONID=" + jsessionid;
        }
        SuperLog.debug(TAG,"stb_querySTBOrderInfo Url="+sb.toString());
        Request request = new Request.Builder()
                .header("Cookie", jsessionid)
                .header("Set-Cookie", jsessionid + "; Path=/VSP/")
                .header("User-Agent", "OTT-Android")
                .header("authorization", SessionService.getInstance().getSession().getUserToken())
                .header("EpgSession", jsessionid)
                .header("Location", ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL())
                .url(sb.toString())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SuperLog.error(TAG,"stb_querySTBOrderInfo fail");
                SuperLog.error(TAG, e);
                callBack.querySTBOrderInfoFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                SuperLog.debug(TAG,"stb_querySTBOrderInfo success");
                String body = response.body().string();
                SuperLog.debug(TAG,"stb_querySTBOrderInfo responseData = " + body);
                QuerySTBOrderInfoResponse querySTBOrderInfoResponse = JsonParse.json2Object(body, QuerySTBOrderInfoResponse.class);
                callBack.querySTBOrderInfoSuccess(querySTBOrderInfoResponse);
            }
        });
    }

    /*
     *内容关联的产品包角标查询接口
     *入参contentId为vodId
     */
    public void getSTBSingleMarks(String contentId,getSTBSingleMarksCallBack callBack) {
        StringBuffer sb = new StringBuffer(HttpConstant.STB_GET_SINGLE_MARKS).append("?");
        sb.append("vt=").append("9");
        sb.append("&contentId=").append(contentId);
        String jsessionid = SharedPreferenceUtil.getInstance().getSeesionId();
        if (!jsessionid.contains("JSESSIONID=")) {
            jsessionid = "JSESSIONID=" + jsessionid;
        }
        SuperLog.debug(TAG,"getSTBSingleMarks Url="+sb.toString());
        Request request = new Request.Builder()
                .header("Cookie", jsessionid)
                .header("Set-Cookie", jsessionid + "; Path=/VSP/")
                .header("User-Agent", "OTT-Android")
                .header("authorization", SessionService.getInstance().getSession().getUserToken())
                .header("EpgSession", jsessionid)
                .header("Location", ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL())
                .url(sb.toString())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SuperLog.error(TAG,"getSTBSingleMarks fail");
                SuperLog.error(TAG, e);
                callBack.getSTBSingleMarksFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                SuperLog.debug(TAG,"getSTBSingleMarks success");
                String body = response.body().string();
                SuperLog.debug(TAG,"getSTBSingleMarks responseData = " + body);
                GetSTBSingleMarksResponse getSTBSingleMarksResponsee = JsonParse.json2Object(body, GetSTBSingleMarksResponse.class);
                callBack.getSTBSingleMarksSuccess(getSTBSingleMarksResponsee);
            }
        });
    }

    public interface QuerySTBOrderInfoCallBack{
        void querySTBOrderInfoSuccess(QuerySTBOrderInfoResponse response);
        void querySTBOrderInfoFail();
    }

    public interface getSTBSingleMarksCallBack{
        void getSTBSingleMarksSuccess(GetSTBSingleMarksResponse response);
        void getSTBSingleMarksFail();
    }
}
