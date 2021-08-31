package com.pukka.ydepg.common.http.v6bean.v6Controller;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.SearchContentCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.SuggestKeywordCallBack;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.SearchFilter;
import com.pukka.ydepg.common.http.v6bean.v6request.GetCastDetailRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SearchContentRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SuggestKeywordRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.SuggestKeywordResponse;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.errorWindowUtil.OTTErrorWindowUtils;
import com.pukka.ydepg.launcher.session.SessionService;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: SearchController.java
 * @author: yh
 * @date: 2017-04-26 09:13
 */
public class SearchController extends BaseController {

    private static final String TAG = SearchController.class.getSimpleName();

    //可展示搜索内容所归属的CPID,定义为静态常量防止构建多个SearchController对象时重复存储浪费内存
    private final static List<String> cpIdList = SessionService.getInstance().getSession().getTerminalConfigurationCpIDList();

    public SearchController(final RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity = rxAppCompatActivity;
    }

    @SuppressLint("CheckResult")
    public void getCastDetail(GetCastDetailRequest mGetCastDetailRequest, SearchContentCallBack searchContentCallBack){
        HttpApi.getInstance().getService().GetCastDetail(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.GET_CAST_DETAIL,mGetCastDetailRequest)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getCastDetailResponse -> {
                      if(null==getCastDetailResponse||null==getCastDetailResponse.getResult()){
                          handleError("1",HttpConstant.SEARCHCONTENT);
                          return;
                      }
                      Result result=getCastDetailResponse.getResult();
                      if(TextUtils.equals(result.getRetCode(),Result.RETCODE_OK)){
                          if (null != searchContentCallBack){
                              searchContentCallBack.getCastDetailSuccess(getCastDetailResponse.getCastDetails());
                          }
                      }else{
                          handleError(result.getRetCode(),HttpConstant.SEARCHCONTENT);
                          if (null != searchContentCallBack){
                              searchContentCallBack.getCastDetailFail();
                          }
                      }

                    },throwable -> {
                        if (null != searchContentCallBack){
                            searchContentCallBack.getCastDetailFail();
                        }
                    });


    }

    //搜索内容
    public void searchContent(SearchContentRequest searchContentRequest,SearchContentCallBack searchContentCallBack,boolean isFilterCpId) {
        if( searchContentCallBack == null || searchContentRequest == null){
            return;
        }

        if( isFilterCpId && cpIdList != null && cpIdList.size() >0 ){
            if(searchContentRequest.getFilter() == null ){
                SearchFilter filter = new SearchFilter();
                filter.setCpIDList(cpIdList);
                searchContentRequest.setFilter(filter);
            } else {
                searchContentRequest.getFilter().setCpIDList(cpIdList);
            }
        }

        HttpApi.getInstance().getService().searchContent(HttpUtil.getVspUrl(HttpConstant.SEARCHCONTENT),searchContentRequest)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchContentResponse -> {
            Result result = searchContentResponse.getResult();
            //测试代码
            //OTTErrorWindowUtils.getErrorInfoFromPbs(HttpConstant.SEARCHCONTENT,"144020000",null);
            if (null != result && TextUtils.equals(result.getRetCode(),Result.RETCODE_OK)){
                int total;
                try {
                    total = Integer.parseInt(searchContentResponse.getTotal().trim());
                } catch (Exception e) {
                    total = 0;
                }
                searchContentCallBack.searchContentSuccess(total, searchContentResponse.getContents(),searchContentRequest.getSearchKey());
            } else {
                if(null!=result) {
                    handleError(result.getRetCode(), HttpConstant.SEARCHCONTENT);
                }
                searchContentCallBack.searchContentFail(searchContentRequest.getSearchKey());
            }

        },throwable -> {
            SuperLog.error(TAG,throwable);
            searchContentCallBack.searchContentFail(searchContentRequest.getSearchKey());
        });
    }

    /**
     * 搜索关键词联想
     */
    @SuppressLint("CheckResult")
    public void suggestKeyword(SuggestKeywordRequest suggestKeywordRequest,
                               SuggestKeywordCallBack suggestKeywordCallBack,
                               ObservableTransformer<SuggestKeywordResponse,SuggestKeywordResponse> transformer) {
        if(suggestKeywordCallBack == null){
            return;
        }
        HttpApi.getInstance().getService().suggestKeyword(HttpUtil.getVspUrl(HttpConstant.SUGGESTKEYWORD),suggestKeywordRequest).compose(transformer).subscribe(
            suggestKeywordResponse -> {
                if (suggestKeywordResponse != null && suggestKeywordResponse.getResult() != null) {
                    Result result = suggestKeywordResponse.getResult();
                    if (!TextUtils.isEmpty(result.getRetCode()) && !result.getRetCode().equals(Result.RETCODE_OK)) {
                        handleError(result.getRetCode(),HttpConstant.SUGGESTKEYWORD);
                    }

                    if (!TextUtils.isEmpty(result.getRetCode()) && result.getRetCode().equals(Result.RETCODE_OK) ) {
                        List<String> suggests = suggestKeywordResponse.getSuggests();
                        suggestKeywordCallBack.suggestKeywordSuccess(suggests);
                    } else {
                        suggestKeywordCallBack.suggestKeywordFail();
                    }
                } else {
                    suggestKeywordCallBack.suggestKeywordFail();
                }
            },
            throwable -> {
                suggestKeywordCallBack.suggestKeywordFail();
            }
        );
    }
}