package com.pukka.ydepg.common.viewpage2.presenter;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.v6bean.v6response.PBSRemixRecommendResponse;
import com.pukka.ydepg.common.toptool.EpgTopFunctionMenu;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.viewpage2.ViewPage2MainEpgResponse;
import com.pukka.ydepg.common.viewpage2.view.ViewPage2MainEpg;
import com.pukka.ydepg.launcher.mvp.presenter.BasePresenter;
import com.pukka.ydepg.launcher.mvp.presenter.TabItemPresenter;
import com.pukka.ydepg.launcher.session.SessionService;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ViewPage2Presenter extends BasePresenter {
    private static final String TAG = ViewPage2Presenter.class.getSimpleName();
    private OkHttpClient client;

    public ViewPage2Presenter() {
        client = new OkHttpClient();
    }


    /**
     * http://aikanvod.miguvideo.com:8858/pvideo/p/stb_queryCarouselsById.jsp?&carouselId=recom001&contentIds=ID1,ID2,ID3,ID4,ID5&vt=9
     * 查询首页轮播智能推荐数据
     * */
    /*public void queryCarouselsById(List<String> contentIds, ViewPage2MainEpg.OnPBSRemixRecommendListener onPBSRemixRecommendListener, String carouselId) {

        StringBuffer sb = new StringBuffer(HttpConstant.PBS_QueryCarouselsById_URL).append("?");
        *//*sb.append("offset=").append("0");
        sb.append("&count=").append("6");*//*
        sb.append("&carouselId=").append(carouselId);
        if (null != contentIds && contentIds.size() > 0) {
            sb.append("&contentIds=");
            for (int i = 0; i < contentIds.size(); i++) {
                String id = contentIds.get(i);
                sb.append(id);
                if (i != contentIds.size() - 1) {
                    sb.append(",");
                }
            }
        }

        sb.append("&vt=").append("9");
        String jsessionid = SharedPreferenceUtil.getInstance().getSeesionId();
        if (!jsessionid.contains("JSESSIONID=")) {
            jsessionid = "JSESSIONID=" + jsessionid;
        }
        SuperLog.debug(TAG,"stb_remixRecommend Url="+sb.toString());
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
                SuperLog.error(TAG,"首页轮播接口 stb_queryCarouselsById fail");
                SuperLog.error(TAG, e);
                onPBSRemixRecommendListener.getqueryCarouselsByIdDataFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = response.body().string();
                SuperLog.debug(TAG,"首页轮播接口 stb_queryCarouselsById success and responseData = " + body);
                ViewPage2MainEpgResponse viewPage2MainEpgResponse = JsonParse.json2Object(body, ViewPage2MainEpgResponse.class);
                onPBSRemixRecommendListener.getQueryCarouselsByIdData(viewPage2MainEpgResponse);
            }
        });
    }*/

}
