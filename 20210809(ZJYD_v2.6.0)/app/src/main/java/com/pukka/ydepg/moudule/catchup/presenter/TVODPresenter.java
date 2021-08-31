package com.pukka.ydepg.moudule.catchup.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryChannelStcPropsBySubjectRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryChannelSubjectListRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PBSRemixRecommendResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryChannelStcPropsBySubjectResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryChannelSubjectListResponse;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.RxApiManager;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.mvp.presenter.BasePresenter;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.catchup.common.Constant;
import com.pukka.ydepg.moudule.catchup.common.TVODDataUtil;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class TVODPresenter extends BasePresenter<TVODContract.View> implements TVODContract.Presenter {

    private static final String TAG = TVODPresenter.class.getName();

    private Context mContext;

    public TVODPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void queryChannelSubjectList() {
        QueryChannelSubjectListRequest queryChannelSubjectListRequest = new QueryChannelSubjectListRequest();
        // 获取终端参数:CatchUpTv父栏目id，用户获取catchUpTv子栏目的id
        String catchUpTvSubjectID = SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.TVOD_SUBJECT_ID);
        queryChannelSubjectListRequest.setSubjectID(catchUpTvSubjectID);
        queryChannelSubjectListRequest.setOffset("0");
        queryChannelSubjectListRequest.setCount("50");

        RxCallBack<QueryChannelSubjectListResponse> rxCallBack = new RxCallBack<QueryChannelSubjectListResponse>(HttpConstant.QUERYCHANNELSUBJECTLIST, mContext) {
            @Override
            public void onSuccess(QueryChannelSubjectListResponse queryChannelSubjectListResponse) {
                // TODO: 2021/5/7 回看栏目id
                if (null == mView) {
                    //mView=null说明是从心跳发起的本次请求调用,不需要回调UI,放到子线程执行提升性能
                    //本次修改由20190823张长顺反馈开机慢后分析增加：
                    //本次问题经定位原因是由于开机时第一次心跳回调到此方法后以下逻辑在Main执行,导致UI加载被阻塞,出现问题盒子型号6109，6110
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO: 2021/5/7 此处应该修改为pbs获取
//                            List<Subject> listAllSubject = getSubjectListFromResponse(queryChannelSubjectListResponse);\
                            queryPBSRemixRecommend(queryChannelSubjectListResponse);
//                            List<Subject> listAllSubject = getSubjectListFromResponse(queryChannelSubjectListResponse);
//
//                            TVODDataUtil.getInstance().setListSubject(TVODDataUtil.getInstance().getUserRealTVODSubjectID(listAllSubject));
//                            TVODDataUtil.getInstance().createCacheData();
                        }
                    }, "HeartBeatExecute").start();
                } else {
//                    List<Subject> listAllSubject = getSubjectListFromResponse(queryChannelSubjectListResponse);
//                    TVODDataUtil.getInstance().setListSubject(TVODDataUtil.getInstance().getUserRealTVODSubjectID(listAllSubject));
//                    TVODDataUtil.getInstance().createCacheData();
//                    mView.onQueryChannelSubjectListSuccess();
                    queryPBSRemixRecommend(queryChannelSubjectListResponse);
                }
            }

            @Override
            public void onFail(Throwable e) {
                SuperLog.error(TAG, e);
                if (null != mView)
                    mView.onQueryChannelSubjectListFailed();
            }
        };

        RxApiManager.get().add(HttpConstant.QUERYCHANNELSUBJECTLIST, rxCallBack);
        HttpApi.getInstance().getService().queryChannelSubjectList(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUERYCHANNELSUBJECTLIST, queryChannelSubjectListRequest)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxCallBack);
    }

    private List<Subject> getSubjectListFromResponse(QueryChannelSubjectListResponse queryChannelSubjectListResponse) {
        Result result = queryChannelSubjectListResponse.getResult();
        if (!TextUtils.isEmpty(result.getRetCode())
                && Result.RETCODE_OK.equals(result.getRetCode())
                && queryChannelSubjectListResponse.getSubjects() != null &&
                queryChannelSubjectListResponse.getSubjects().size() > 0) {
            return queryChannelSubjectListResponse.getSubjects();
        } else {
            return null;
        }
    }

    /**
     * 获取栏目下频道列表
     */
    @Override
    public void queryChannelStcPropsBySubject(String subjectID, int position) {
        QueryChannelStcPropsBySubjectRequest request = new QueryChannelStcPropsBySubjectRequest();
        request.setSubjectID(subjectID);
        request.setOffset("0");
        request.setCount("1000");
        request.setSortType("ORDERINDEX:DESC");

        RxCallBack<QueryChannelStcPropsBySubjectResponse> rxCallBack = new RxCallBack<QueryChannelStcPropsBySubjectResponse>(HttpConstant.QUERYCHANNELSTCPROPSBYSUBJECT, mContext) {
            @Override
            public void onSuccess(QueryChannelStcPropsBySubjectResponse response) {
                if (null != mView) {
                    if (null == response.getChannelDetails() || response.getChannelDetails().size() == 0) {
                        mView.onQueryChannelStcPropsBySubjectFailed();
                    } else {
                        mView.onQueryChannelStcPropsBySubjectSuccess(TVODDataUtil.getInstance().filter4KChannel(response.getChannelDetails()), position);
                    }
                }

            }

            @Override
            public void onFail(Throwable e) {
                if (null != mView) {
                    mView.onQueryChannelStcPropsBySubjectFailed();
                }
            }
        };

        RxApiManager.get().add(HttpConstant.QUERYCHANNELSUBJECTLIST, rxCallBack);
        HttpApi.getInstance().getService().queryChannelStcPropsBySubject(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUERYCHANNELSTCPROPSBYSUBJECT, request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxCallBack);
    }

    public void queryPBSRemixRecommend(QueryChannelSubjectListResponse queryChannelSubjectListResponse) {
        final StringBuilder[] builder = {new StringBuilder()};
        OkHttpClient client = new OkHttpClient();
        List<Subject> subjectList = new ArrayList<>();
        StringBuffer sb = new StringBuffer(HttpConstant.PBS_RemixRecommend_URL).append("?");
        sb.append("offset=").append("0");
        sb.append("&count=").append("100");
//    sb.append("&sceneType=").append("7");
//    sb.append("&stringURL=").append(CommonUtil.getConfigValue(Constant.LIVE_SUBJECT_ID));
//    sb.append("&appointedId=").append("live25");
        sb.append("&appointedIds=").append("rewatching25");
        sb.append("&vt=").append("9");
        String jsessionid = SharedPreferenceUtil.getInstance().getSeesionId();
        if (!jsessionid.contains("JSESSIONID=")) {
            jsessionid = "JSESSIONID=" + jsessionid;
        }
        SuperLog.debug(TAG, "stb_remixRecommend Url=" + sb.toString());
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
                SuperLog.error(TAG, "stb_remixRecommend fail,queryPBSRemixRecommend");
                SuperLog.error(TAG, e.getMessage());
                List<Subject> listAllSubject = getSubjectListFromResponse(queryChannelSubjectListResponse);
                TVODDataUtil.getInstance().setListSubject(TVODDataUtil.getInstance().getUserRealTVODSubjectID(listAllSubject));
                TVODDataUtil.getInstance().createCacheData();
                if (mView != null) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onQueryChannelSubjectListSuccess();
                        }
                    });

                }
//                onPBSRemixRecommendListener.getRemixRecommendDataFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                SuperLog.debug(TAG, "stb_remixRecommend success,queryPBSRemixRecommend");
                String body = response.body().string();
                SuperLog.debug(TAG, "stb_remixRecommend responseData = " + body);
                PBSRemixRecommendResponse pbsRemixRecommendResponse = JsonParse.json2Object(body, PBSRemixRecommendResponse.class);
                List<Subject> listAllSubject = getSubjectListFromResponse(queryChannelSubjectListResponse);
                if (null != pbsRemixRecommendResponse) {
                    if (null != pbsRemixRecommendResponse.getRecommends() && pbsRemixRecommendResponse.getRecommends().size() > 0) {
                        for (int i = 0; i < pbsRemixRecommendResponse.getRecommends().size(); i++) {
                            if (pbsRemixRecommendResponse.getRecommends().get(i).getOther() != null && pbsRemixRecommendResponse.getRecommends().get(i).getOther().size() > 0) {
                                for (int j = 0; j < pbsRemixRecommendResponse.getRecommends().get(i).getOther().size(); j++) {
                                    if (null != pbsRemixRecommendResponse.getRecommends().get(i).getOther().get(j).getStringURL() && !TextUtils.isEmpty(pbsRemixRecommendResponse.getRecommends().get(i).getOther().get(j).getStringURL())) {
                                        if (pbsRemixRecommendResponse.getRecommends().get(i).getSceneType().equals("9")) {

                                            String[] split = pbsRemixRecommendResponse.getRecommends().get(i).getOther().get(j).getStringURL().split(",");
                                            for (int k = 0; k < split.length; k++) {
                                                Subject subject = new Subject();
                                                subject.setID(split[k]);
                                                subjectList.add(subject);
                                            }
                                        }
                                    }

                                }
                            }
                        }
                        List<Subject> newSubjectList = new ArrayList<>();

                        if (listAllSubject.size() > 0) {
                            for (int i = 0; i < listAllSubject.size(); i++) {
                                if (subjectList != null && subjectList.size() > 0) {
                                    for (int j = 0; j < subjectList.size(); j++) {
                                        if (listAllSubject.get(i).getID().equals(subjectList.get(j).getID())) {
                                            //取合集
                                            newSubjectList.add(listAllSubject.get(i));
                                        }
                                    }
                                }
                            }
                        }

                        TVODDataUtil.getInstance().setListSubject(newSubjectList);
                        TVODDataUtil.getInstance().createCacheData();
                        if (mView != null) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mView.onQueryChannelSubjectListSuccess();
                                }
                            });

                        }
                    } else {
                        TVODDataUtil.getInstance().setListSubject(TVODDataUtil.getInstance().getUserRealTVODSubjectID(listAllSubject));
                        TVODDataUtil.getInstance().createCacheData();
                        if (mView != null) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mView.onQueryChannelSubjectListSuccess();
                                }
                            });

                        }
                    }
                } else {
                    TVODDataUtil.getInstance().setListSubject(TVODDataUtil.getInstance().getUserRealTVODSubjectID(listAllSubject));
                    TVODDataUtil.getInstance().createCacheData();
                    if (mView != null) {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mView.onQueryChannelSubjectListSuccess();
                            }
                        });

                    }
                }
            }
        });
    }
}
