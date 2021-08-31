package com.pukka.ydepg.common.http.v6bean.v6Controller;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.bean.request.QueryDuplicateRequest;
import com.pukka.ydepg.common.http.bean.request.QueryRecommendRequest;
import com.pukka.ydepg.common.http.bean.response.QueryDuplicateResponse;
import com.pukka.ydepg.common.http.bean.response.QueryRecommendResponse;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.IPlayURLCallback;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODDetailCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODDuplicateCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODListCallBack;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.VODFilter;
import com.pukka.ydepg.common.http.v6bean.v6request.GetContentConfigRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.GetVODDetailRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QuerySubjectDetailRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODListBySubjectRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODSubjectListRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.GetContentConfigResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.GetVODDetailResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PBSRemixRecommendResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySubjectDetailResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryVODListBySubjectResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryVODSubjectListResponse;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.RxApiManager;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.vod.cache.VodDetailCacheService;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: VODListController.java
 * @author: yh
 * @date: 2017-04-25 17:55
 */
public class VODListController extends BaseController {

    private static final String TAG = "VODListController";

    private VODListCallBack vodListCallBack;

    /**
     * 可展示内容的CPID,多个CPID以逗号分隔
     */
    private String strCpIdList;

    private OkHttpClient client;

    //主线程Handler
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public VODListController(RxAppCompatActivity mRxAppCompatActivity) {
        this.rxAppCompatActivity=mRxAppCompatActivity;
        client=new OkHttpClient();
        getCpIdString();
    }

    public VODListController(final VODListCallBack vodListCallBack, RxAppCompatActivity mRxAppCompatActivity) {
        this.vodListCallBack = vodListCallBack;
        this.rxAppCompatActivity=mRxAppCompatActivity;
        client=new OkHttpClient();
        getCpIdString();
    }

    //获取界面要展示的VOD的cpID(cpID不等于这些值的VOD不展示)
    private void getCpIdString(){
        List<String> cpIdList = SessionService.getInstance().getSession().getTerminalConfigurationCpIDList();
        if( cpIdList != null ){
            for(String cpid : cpIdList){
                if(TextUtils.isEmpty(strCpIdList)){
                    strCpIdList = cpid ;
                }else{
                    strCpIdList = strCpIdList + "," + cpid;
                }
            }
        }
    }


//    public void queryVODListBySubjects(QueryVODListBySubjectRequest queryVODListBySubjectRequest,
//                                       ObservableTransformer<QueryVODListBySubjectResponse, QueryVODListBySubjectResponse> transformer,
//                                       final String subjectId,
//                                       VODListCallBack selfVodListCallBack) {
//
//        final VODListCallBack callback = selfVodListCallBack != null? selfVodListCallBack:vodListCallBack;
//        if( callback == null ){
//            SuperLog.error(TAG,"Network request callback is null, cancel the request.");
//            return;
//        }
//        OkHttpClient client = new OkHttpClient();
//        StringBuffer sb = new StringBuffer(HttpConstant.PBS_QUERY_VOD_URL).append("?");
//        sb.append("userVODListFilter=").append(SessionService.getInstance().getSession().getUserVODListFilter());
//        sb.append("&subjectID=").append(subjectId);
//        sb.append("&sortType=").append(queryVODListBySubjectRequest.getSortType());
//        sb.append("&count=").append(queryVODListBySubjectRequest.getCount());
//        sb.append("&offset=").append(queryVODListBySubjectRequest.getOffset());
////        sb.append("&genreName=").append(queryVODListBySubjectRequest.getVODFilter().getGenreIDs());
//        String vodFilter="";
//        if(null!=queryVODListBySubjectRequest.getVODFilter())
//        {
//            vodFilter= JsonParse.object2String(queryVODListBySubjectRequest.getVODFilter());
//        }
//        sb.append("&VODFilter=").append(vodFilter);
//        String jsessionid = SharedPreferenceUtil.getInstance().getSeesionId();
//        if (!jsessionid.contains("JSESSIONID=")) {
//            jsessionid = "JSESSIONID=" + jsessionid;
//        }
//        SuperLog.info2SD(TAG, "stb_remixRecommend Url=" + sb.toString());
//        Request request = new Request.Builder()
//                .header("Cookie", jsessionid)
//                .header("Set-Cookie", jsessionid + "; Path=/VSP/")
//                .header("User-Agent", "OTT-Android")
//                .header("authorization", SessionService.getInstance().getSession().getUserToken())
//                .header("EpgSession", jsessionid)
//                .header("Location", ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL())
//                .url(sb.toString())
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                SuperLog.error(TAG, e);
//                callback.queryVODListBySubjectFailed();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                SuperLog.info2SD(TAG, "stb_remixRecommend success,queryPBSRemixRecommend");
//                String body = response.body().string();
//                SuperLog.info2SD(TAG, "stb_remixRecommend responseData = " + body);
//                QueryVODListBySubjectResponse queryVODListBySubjectResponse = JsonParse.json2Object(body, QueryVODListBySubjectResponse.class);
//                if (queryVODListBySubjectResponse != null && queryVODListBySubjectResponse.getResult() != null && queryVODListBySubjectResponse.getResult().getRetCode()!=null) {
//                    Result result = queryVODListBySubjectResponse.getResult();
//                    if(Result.RETCODE_OK.equals(result.getRetCode())){
//                        String totalString = queryVODListBySubjectResponse.getTotal();
//                        int total;
//                        try {
//                            total = Integer.parseInt(totalString.trim());
//                        } catch (Exception e) {
//                            total = 0;
//                        }
//                        callback.queryVODListBySubjectSuccess(total, queryVODListBySubjectResponse.getVODs(),subjectId);
//                    }else{
//                        SuperLog.error(TAG, "[onNext]QueryVODListStcPropsBySubject code = " + result.getRetCode() + " message = " + result.getRetMsg());
//                        handleError(result.getRetCode(),HttpConstant.QUERYVODLISTSTCPROPSBYSUBJECT);
//                        callback.queryVODListBySubjectFailed();
//                    }
//                } else {
//                    callback.queryVODListBySubjectFailed();
//                }
//            }
//        });
//        if(!TextUtils.isEmpty(strCpIdList)){
//            if(queryVODListBySubjectRequest.getVODFilter()!=null){
//                queryVODListBySubjectRequest.getVODFilter().setCpId(strCpIdList);
//            } else {
//                VODFilter filter = new VODFilter();
//                filter.setCpId(strCpIdList);
//                queryVODListBySubjectRequest.setVODFilter(filter);
//            }
//        }
//    }

    //获取栏目下VOD列表
    public void queryVODListBySubject(
            QueryVODListBySubjectRequest queryVODListBySubjectRequest,
            ObservableTransformer<QueryVODListBySubjectResponse, QueryVODListBySubjectResponse> transformer,
            final String subjectId,
            VODListCallBack selfVodListCallBack)
    {
        final VODListCallBack callback = selfVodListCallBack != null? selfVodListCallBack:vodListCallBack;
        if( callback == null ){
            SuperLog.error(TAG,"Network request callback is null, cancel the request.");
            return;
        }

        RxCallBack<QueryVODListBySubjectResponse> rxCallBack = new RxCallBack<QueryVODListBySubjectResponse>(HttpConstant.QUERYVODLISTSTCPROPSBYSUBJECT, this.rxAppCompatActivity) {
            @Override
            public void onSuccess(QueryVODListBySubjectResponse queryVODListBySubjectResponse) {
                SuperLog.debug(TAG, "QueryVODListStcPropsBySubject successfully");
                if (queryVODListBySubjectResponse != null && queryVODListBySubjectResponse.getResult() != null && queryVODListBySubjectResponse.getResult().getRetCode()!=null) {
                    Result result = queryVODListBySubjectResponse.getResult();
                    if(Result.RETCODE_OK.equals(result.getRetCode())){
                        String totalString = queryVODListBySubjectResponse.getTotal();
                        int total;
                        try {
                            total = Integer.parseInt(totalString.trim());
                        } catch (Exception e) {
                            total = 0;
                        }
                        callback.queryVODListBySubjectSuccess(total, queryVODListBySubjectResponse.getVODs(),subjectId);
                    }else{
                        SuperLog.error(TAG, "[onNext]QueryVODListStcPropsBySubject code = " + result.getRetCode() + " message = " + result.getRetMsg());
                        handleError(result.getRetCode(),HttpConstant.QUERYVODLISTSTCPROPSBYSUBJECT);
                        callback.queryVODListBySubjectFailed();
                    }
                } else {
                    callback.queryVODListBySubjectFailed();
                }
            }

            @Override public void onFail(@NonNull Throwable e) {
                SuperLog.error(TAG, e);
                callback.queryVODListBySubjectFailed();
            }
        };

        if(!TextUtils.isEmpty(strCpIdList)){
            if(queryVODListBySubjectRequest.getVODFilter()!=null){
                queryVODListBySubjectRequest.getVODFilter().setCpId(strCpIdList);
            } else {
                VODFilter filter = new VODFilter();
                filter.setCpId(strCpIdList);
                queryVODListBySubjectRequest.setVODFilter(filter);
            }
        }

        //解决多次进详情，下来刷下，无响应的问题 不再使用.compose(transformer)
        String url = HttpUtil.getVspUrl(HttpConstant.QUERYVODLISTSTCPROPSBYSUBJECT) + HttpUtil.addUserVODListFilter();
        HttpApi.getInstance().getService().queryVODListBySubject(url, queryVODListBySubjectRequest)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxCallBack);
    }

    /**
     * 获取VOD栏目列表
     */
    public void queryVODSubjectList(QueryVODSubjectListRequest queryVODListBySubjectRequest, ObservableTransformer<QueryVODSubjectListResponse, QueryVODSubjectListResponse> transformer) {
        RxCallBack<QueryVODSubjectListResponse> rxCallBack = new RxCallBack<QueryVODSubjectListResponse>(HttpConstant.QUERYVODSUBJECTLIST, this.rxAppCompatActivity) {
            @Override
            public void onSuccess(QueryVODSubjectListResponse queryVODSubjectListResponse) {
                SuperLog.debug(TAG, "[onNext] QuerySubjectVODBySubjectIDEntry");
                if (queryVODSubjectListResponse != null && queryVODSubjectListResponse.getResult() != null) {
                    Result result = queryVODSubjectListResponse.getResult();
                    if (result != null && !TextUtils.isEmpty(result.getRetCode()) && !result.getRetCode().equals(Result.RETCODE_OK)) {
                        SuperLog.error(TAG, "[onNext] QuerySubjectVODBySubjectIDEntry code = " + result.getRetCode() + " message = " + result.getRetMsg());
                        handleError(result.getRetCode(),HttpConstant.QUERYVODSUBJECTLIST);
                    }

                    if (result != null && !TextUtils.isEmpty(result.getRetCode()) && result.getRetCode().equals(Result.RETCODE_OK) && vodListCallBack != null) {
                        String totalString = queryVODSubjectListResponse.getTotal();
                        int total = 0;
                        if (!TextUtils.isEmpty(totalString)) {
                            try {
                                total = Integer.parseInt(totalString.trim());
                            } catch (Exception e) {
                                total = 0;
                            }
                        }
                        vodListCallBack.queryVODSubjectListSuccess(total, queryVODSubjectListResponse.getSubjects());
                    } else {
                        if (vodListCallBack != null) {
                            vodListCallBack.queryVODSubjectListFailed();
                        }
                    }
                } else {
                    if (vodListCallBack != null) {
                        vodListCallBack.queryVODSubjectListFailed();
                    }
                }
            }

            @Override public void onFail(@NonNull Throwable e) {
                SuperLog.error(TAG, "[onError] QuerySubjectVODBySubjectIDEntry = " + e.toString());
                if (vodListCallBack != null) {
                    vodListCallBack.onError();
                }
            }
        };
        HttpApi.getInstance().getService().queryVODSubjectList(HttpUtil.getVspUrl(HttpConstant.QUERYVODSUBJECTLIST), queryVODListBySubjectRequest)
                .compose(transformer)
                .subscribe(rxCallBack);
    }

    /**
     * 获取内容配置
     */
    public void getContentConfig(GetContentConfigRequest getContentConfigRequest, ObservableTransformer<GetContentConfigResponse, GetContentConfigResponse> transformer) {
        HttpApi.getInstance().getService().getContentConfig(HttpUtil.getVspUrl(HttpConstant.GETCONTENTCONFIG), getContentConfigRequest)
            .compose(transformer)
            .subscribe(new RxCallBack<GetContentConfigResponse>(HttpConstant.GETCONTENTCONFIG, this.rxAppCompatActivity) {
                @Override
                public void onSuccess(GetContentConfigResponse getContentConfigResponse) {
                    SuperLog.debug(TAG, "[onNext] GetContentConfigEntry");
                    if (getContentConfigResponse != null && getContentConfigResponse.getResult() != null) {
                        VodDetailCacheService.getInstance().setGetContentConfigResponse(getContentConfigResponse);
                        Result result = getContentConfigResponse.getResult();
                        if (result != null && !TextUtils.isEmpty(result.getRetCode()) && !result.getRetCode().equals(Result.RETCODE_OK)) {
                            SuperLog.error(TAG, "[onNext] GetContentConfigEntry code = " + result.getRetCode() + " message = " + result.getRetMsg());
                                handleError(result.getRetCode(),HttpConstant.GETCONTENTCONFIG);
                            }

                            if (null!= result && !TextUtils.isEmpty(result.getRetCode()) && result.getRetCode().equals(Result.RETCODE_OK) && vodListCallBack != null) {
                                vodListCallBack.getContentConfigSuccess(getContentConfigResponse.getProduceZones(), getContentConfigResponse.getGenres());
                            } else {
                                if (vodListCallBack != null) {
                                    EpgToast.showToast(OTTApplication.getContext(), "服务暂不可用");
                                    vodListCallBack.getContentConfigFailed();
                                }
                            }
                        } else {
                            if (vodListCallBack != null) {
                                vodListCallBack.getContentConfigFailed();
                            }
                        }
                    }

                @Override
                public void onFail(@NonNull Throwable e) {
                    SuperLog.error(TAG, "[onError] GetContentConfigEntry = " + e.toString());
                    if (vodListCallBack != null) {
                        vodListCallBack.onError();
                    }
                }
            });
    }

    /**
     * 查询VOD详情
     */
    public void getVODDetail(GetVODDetailRequest getVODDetailRequest, ObservableTransformer<GetVODDetailResponse, GetVODDetailResponse> transformer, VODDetailCallBack vodDetailCallBack) {
        RxApiManager.get().cancelAll();
        RxCallBack<GetVODDetailResponse>  rxCallBack=new RxCallBack<GetVODDetailResponse>(HttpConstant.GETVODDETAIL, this.rxAppCompatActivity) {
            @Override
            public void onSuccess(GetVODDetailResponse getVODDetailResponse) {
                SuperLog.debug(TAG, "[onNext] GetVODDetailEntry");
                if (getVODDetailResponse != null && getVODDetailResponse.getResult() != null) {
                    Result result = getVODDetailResponse.getResult();
                    if (result != null && !TextUtils.isEmpty(result.getRetCode()) && !result.getRetCode().equals(Result.RETCODE_OK)) {
                        SuperLog.error(TAG, "[onNext] GetVODDetailEntry code = " + result.getRetCode() + " message = " + result.getRetMsg());
                        handleError(result.getRetCode(),HttpConstant.GETVODDETAIL);
                    }

                    if (null != result && !TextUtils.isEmpty(result.getRetCode()) && result.getRetCode().equals(Result.RETCODE_OK) && vodDetailCallBack != null) {
                        vodDetailCallBack.getVODDetailSuccess(getVODDetailResponse.getVODDetail(), getVODDetailResponse.getRecmActionID(), getVODDetailResponse.getRecmContents());
                    } else {
                        if (vodDetailCallBack != null) {
                            vodDetailCallBack.getVODDetailFailed();
                        }
                    }
                } else {
                    if (vodDetailCallBack != null) {
                        vodDetailCallBack.getVODDetailFailed();
                    }
                }
            }

            @Override public void onFail(@NonNull Throwable e) {
                SuperLog.error(TAG,e);
                if (vodDetailCallBack != null) {
                    vodDetailCallBack.onError();
                }
            }
        };
        HttpApi.getInstance().getService().getVODDetail(HttpUtil.getVspUrl(HttpConstant.GETVODDETAIL), getVODDetailRequest)
                .compose(transformer)
                .subscribe(rxCallBack);
    }

    /**
     * 历史，和收藏点击的时候，不弹出提示语需求
     * @param getVODDetailRequest
     * @param transformer
     * @param vodDetailCallBack
     */
    public void getVODDetailNotToastHint(GetVODDetailRequest getVODDetailRequest, ObservableTransformer<GetVODDetailResponse, GetVODDetailResponse> transformer, VODDetailCallBack vodDetailCallBack) {
        RxApiManager.get().cancelAll();
        RxCallBack<GetVODDetailResponse>  rxCallBack=new RxCallBack<GetVODDetailResponse>(HttpConstant.GETVODDETAIL, this.rxAppCompatActivity) {
            @Override
            public void onSuccess(GetVODDetailResponse getVODDetailResponse) {
                SuperLog.debug(TAG, "[onNext] GetVODDetailEntry");
                if (getVODDetailResponse != null && getVODDetailResponse.getResult() != null) {
                    Result result = getVODDetailResponse.getResult();
                    if (!TextUtils.isEmpty(result.getRetCode())
                        && result.getRetCode().equals(Result.RETCODE_OK)
                        && vodDetailCallBack != null) {

                        vodDetailCallBack.getVODDetailSuccess(
                            getVODDetailResponse.getVODDetail(),
                            getVODDetailResponse.getRecmActionID(),
                            getVODDetailResponse.getRecmContents());
                    } else {
                        if (vodDetailCallBack != null) {
                            vodDetailCallBack.getVODDetailFailed();
                        }
                    }
                } else {
                    if (vodDetailCallBack != null) {
                        vodDetailCallBack.getVODDetailFailed();
                    }
                }
            }

            @Override public void onFail(@NonNull Throwable e) {
                SuperLog.error(TAG, "[onError] GetVODDetailEntry = " + e.toString());
                if (vodDetailCallBack != null) {
                    vodDetailCallBack.onError();
                }
            }
        };
        HttpApi.getInstance().getService().getVODDetail(HttpUtil.getVspUrl(HttpConstant.GETVODDETAIL), getVODDetailRequest)
            .compose(transformer)
            .subscribe(rxCallBack);
    }

    /**
     * Subject的详情
     */
    public void querySubjectDetail(QuerySubjectDetailRequest queryRecmVODListRequest, ObservableTransformer<QuerySubjectDetailResponse, QuerySubjectDetailResponse> transformer) {
        HttpApi.getInstance().getService().QuerySubjectDetail(HttpUtil.getVspUrl(HttpConstant.QUERYSUBJECT_DETAIL), queryRecmVODListRequest)
            .compose(transformer)
            .subscribe(new RxCallBack<QuerySubjectDetailResponse>(HttpConstant.QUERYSUBJECT_DETAIL,this.rxAppCompatActivity) {
                @Override
                public void onSuccess(QuerySubjectDetailResponse response) {
                    if (null != vodListCallBack) {
                        Result result = response.getResult();
                        if (result != null && !TextUtils.isEmpty(result.getRetCode()) && !result.getRetCode().equals(Result.RETCODE_OK)) {
                            SuperLog.error(TAG, "[onNext] GetVODDetailEntry code = " + result.getRetCode() + " message = " + result.getRetMsg());
                                handleError(result.getRetCode(),HttpConstant.QUERYSUBJECT_DETAIL);
                                return;
                            }

                            if (null != response.getSubjectList() && response.getSubjectList().size() > 0) {
                                vodListCallBack.querySubjectDetailSuccess(response.getSubjectList().size(), response.getSubjectList());
                            }
                        }
                    }

                @Override
                public void onFail(@NonNull Throwable e) {}
            });
    }

    /**
     * Subject的详情
     */
    public void querySubjectDetail(QuerySubjectDetailRequest queryRecmVODListRequest, ObservableTransformer<QuerySubjectDetailResponse, QuerySubjectDetailResponse> transformer, VODListCallBack vodListCallBack) {
        HttpApi.getInstance().getService().QuerySubjectDetail(HttpUtil.getVspUrl(HttpConstant.QUERYSUBJECT_DETAIL), queryRecmVODListRequest)
            .compose(transformer)
            .subscribe(new RxCallBack<QuerySubjectDetailResponse>(HttpConstant.QUERYSUBJECT_DETAIL,this.rxAppCompatActivity) {
                @Override
                public void onSuccess(QuerySubjectDetailResponse response) {
                    if (null != vodListCallBack) {
                        Result result = response.getResult();
                        if (result != null && !TextUtils.isEmpty(result.getRetCode()) && !result.getRetCode().equals(Result.RETCODE_OK)) {
                            SuperLog.error(TAG, "[onNext] GetVODDetailEntry code = " + result.getRetCode() + " message = " + result.getRetMsg());
                            handleError(result.getRetCode(),HttpConstant.QUERYSUBJECT_DETAIL);
                            return;
                        }

                        if (null != response.getSubjectList() && response.getSubjectList().size() > 0) {
                            vodListCallBack.querySubjectDetailSuccess(response.getSubjectList().size(), response.getSubjectList());
                        }
                    }
                }

                @Override
                public void onFail(@NonNull Throwable e) {}
            });
    }

    /**
     * 查询推荐列表recommend
     */
    public void queryPBSRecommend(QueryRecommendRequest queryRecommendRequest, ObservableTransformer<QueryRecommendResponse, QueryRecommendResponse> transformer, VODListCallBack vodListCallBack) {
        StringBuilder sb = new StringBuilder(HttpConstant.PBS_URL).append("?");
        sb.append("offset=").append(queryRecommendRequest.getOffset());
        sb.append("&count=").append(queryRecommendRequest.getCount());
        sb.append("&userId=").append(AuthenticateManager.getInstance().getUserInfo().getUserId());
        sb.append("&scenarizedType=").append(queryRecommendRequest.getScenarizedType());
        if(!TextUtils.isEmpty(queryRecommendRequest.getProudctId())) {
            sb.append("&productId=").append(queryRecommendRequest.getProudctId());
        }else {
            sb.append("&vodId=").append(queryRecommendRequest.getVodId());
        }
        sb.append("&vt=").append(queryRecommendRequest.getVt());
        String jsessionid = SharedPreferenceUtil.getInstance().getSeesionId();
        if (!jsessionid.contains("JSESSIONID=")) {
            jsessionid = "JSESSIONID=" + jsessionid;
        }

        Request request=new Request.Builder()
            .header("Cookie", jsessionid)
            .header("Set-Cookie", jsessionid + "; Path=/VSP/")
            .header("User-Agent", "OTT-Android")
            .header("authorization",SessionService.getInstance().getSession().getUserToken())
            .header("EpgSession",jsessionid)
            .header("Location", ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL())
            .url(sb.toString())
            .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SuperLog.error(TAG,"queryPBSRecommend onFailure");
                SuperLog.error(TAG, e);
                if(null != vodListCallBack){
                    vodListCallBack.queryPSBRecommendFail();
                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body=response.body().string();
                if(response.isSuccessful()&&!TextUtils.isEmpty(body)){
                    String formBody = body.replaceAll("\\s*", "");
                    QueryRecommendResponse queryRecommendResponse = JsonParse.json2Object(formBody,QueryRecommendResponse.class);
                    if(null!=vodListCallBack){
                        if(null!=queryRecommendResponse) {
                            vodListCallBack.queryPSBRecommendSuccess(queryRecommendResponse.getTotalCount(), queryRecommendResponse.getVods());
                        }else{
                            vodListCallBack.queryPSBRecommendFail();
                        }
                    }
                }else{
                    if(null!=vodListCallBack){
                        vodListCallBack.queryPSBRecommendFail();
                    }
                }
            }
        });
    }

    /**
     * 获取智能推荐
     */
    public void queryPBSDuplicate(QueryDuplicateRequest queryDuplicateRequest,VODDuplicateCallBack callBack) {
        StringBuffer sb = new StringBuffer(HttpConstant.PBS_Duplicate_URL).append("?");
        sb.append("offset=").append(queryDuplicateRequest.getOffset());
        sb.append("&count=").append(queryDuplicateRequest.getCount());
        sb.append("&sceneId=").append(queryDuplicateRequest.getSceneId());
        if (null != queryDuplicateRequest.getContentIds() && queryDuplicateRequest.getContentIds().size() > 0) {
            List<String> contentIds = queryDuplicateRequest.getContentIds();
            sb.append("&contentIds=");
            for (int i = 0; i < contentIds.size(); i++) {
                String id = contentIds.get(i);
                sb.append(id);
                if (i != contentIds.size() - 1) {
                    sb.append(",");
                }
            }
        }

        sb.append("&vt=").append(queryDuplicateRequest.getVt());
        String jsessionid = SharedPreferenceUtil.getInstance().getSeesionId();
        if (!jsessionid.contains("JSESSIONID=")) {
            jsessionid = "JSESSIONID=" + jsessionid;
        }

        Request request = new Request.Builder()
                .header("Cookie", jsessionid)
                .header("Set-Cookie", jsessionid + "; Path=/VSP/")
                .header("User-Agent", "OTT-Android")
                .header("authorization",SessionService.getInstance().getSession().getUserToken())
                .header("EpgSession", jsessionid)
                .header("Location", ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL())
                .url(sb.toString())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SuperLog.error(TAG,"queryPBSDuplicate onFailure");
                SuperLog.error(TAG, e);
                //在主线程执行后续操作
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (null != callBack) {
                            callBack.queryDuplicateFail();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                //在主线程执行后续操作
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful() && !TextUtils.isEmpty(body)) {
                            String formBody = body.replaceAll("\\s*", "");
                            QueryDuplicateResponse queryDuplicateResponse = JsonParse.json2Object(formBody, QueryDuplicateResponse.class);
                            if (null != callBack) {
                                if (null != queryDuplicateResponse) {
                                    if (queryDuplicateRequest.getSceneId().equalsIgnoreCase(QueryDuplicateRequest.CONTENT_RECOMMEND)) {
                                        callBack.queryDuplicateSuccessByVODs(queryDuplicateResponse.getTotalCount(), queryDuplicateResponse.getVods(),queryDuplicateRequest.getSceneId());
                                    } else if (queryDuplicateRequest.getSceneId().equalsIgnoreCase(QueryDuplicateRequest.SPECIAL_RECOMMEND) || queryDuplicateRequest.getSceneId().equalsIgnoreCase(QueryDuplicateRequest.EVENT_RECOMMEND)) {
                                        callBack.queryDuplicateSuccessByTopics(queryDuplicateResponse.getTotalCount(), queryDuplicateResponse.getTopics(),queryDuplicateRequest.getSceneId());
                                    } else {
                                        callBack.queryDuplicateFail();
                                    }
                                } else {
                                    callBack.queryDuplicateFail();
                                }
                            }
                        } else {
                            if (null != callBack) {
                                callBack.queryDuplicateFail();
                            }
                        }
                    }
                });
            }
        });
    }
}