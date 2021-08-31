package com.pukka.ydepg.common.http.v6bean.v6Controller;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.AddFavoCatalogCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.CreateBookmarkCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.CreateContentScoreCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.CreateFavoriteCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.DeleteBookmarkCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.DeleteFavoCatalogCallback;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.DeleteFavoriteCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.GetRelatedContentCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.QueryBookmarkCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.QueryFavoCatalogCallback;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.QueryFavoriteCallBack;
import com.pukka.ydepg.common.http.v6bean.v6node.BookmarkItem;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6request.AddFavoCatalogRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.CreateBookmarkRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.CreateContentScoreRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.CreateFavoriteRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.DeleteBookmarkRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.DeleteFavoCatalogRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.DeleteFavoriteRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.GetRelatedContentRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryBookmarkRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryFavoCatalogRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryFavoriteRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.AddFavoCatalogResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.CreateBookmarkResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.CreateContentScoreResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.CreateFavoriteResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.DeleteBookmarkResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.DeleteFavoCatalogResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.DeleteFavoriteResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.GetRelatedContentResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryBookmarkResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryFavoCatalogResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryFavoriteResponse;
import com.pukka.ydepg.common.report.ubd.scene.UBDOther;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.IsTV;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.util.RxCallBack;

import com.pukka.ydepg.service.NtpTimeService;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: PersonalizedController.java
 * @author: yh
 * @date: 2017-04-26 09:13
 */
public class PersonalizedController extends BaseController {

    private static final String TAG = "PersonalizedController";

    private String baseUrl = null;

    public PersonalizedController(final RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity = rxAppCompatActivity;
    }

    private String getBaseUrl(){
        if(baseUrl == null){
            baseUrl = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()+ HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3;
        }
        return baseUrl;
    }

    //新增收藏
    public void createFavorite(
            CreateFavoriteRequest  createFavoriteRequest,
            CreateFavoriteCallBack createFavoriteCallBack,
            ObservableTransformer<CreateFavoriteResponse,CreateFavoriteResponse> transformer,String recommendType) {

        HttpApi.getInstance().getService().createFavorite(getBaseUrl()+HttpConstant.CREATEFAVORITE, createFavoriteRequest)
                .compose(transformer)
                .subscribe(new RxCallBack<CreateFavoriteResponse>(HttpConstant.CREATEFAVORITE, this.rxAppCompatActivity) {
                    @Override
                    public void onSuccess(CreateFavoriteResponse createFavoriteResponse) {
                        SuperLog.debug(TAG, "[onNext] CreateFavoriteEntity");
                        if (createFavoriteResponse != null
                                && createFavoriteResponse.getResult() != null) {

                            Result result = createFavoriteResponse.getResult();

                            if (result != null
                                    && !TextUtils.isEmpty(result.getRetCode())
                                    && !result.getRetCode().equals(Result.RETCODE_OK)) {
                                SuperLog.error(TAG, "[onNext] CreateFavoriteEntity code = " + result.getRetCode()
                                        + " message = " + result.getRetMsg());
                                if(result.getRetCode().equals("145020002")){
                                    EpgToast.showToast(rxAppCompatActivity,rxAppCompatActivity.getResources().getString(R.string.collect_error));
                                    OTTApplication.getContext().setFavoCatalogID("");
                                }else {
                                    handleError(result.getRetCode(), HttpConstant.CREATEFAVORITE);
                                }
                            }

                            if (result != null && !TextUtils.isEmpty(result.getRetCode())
                                    && result.getRetCode().equals(Result.RETCODE_OK)
                                    && createFavoriteCallBack != null) {
                                String preVersion = createFavoriteResponse.getPreVersion();
                                String version = createFavoriteResponse.getVersion();
                                if (TextUtils.isEmpty(version)) {
                                    version = preVersion;
                                }
                                if (!TextUtils.isEmpty(version)) {
                                    SharedPreferenceUtil.getInstance().
                                            updateLocalVersion(SharedPreferenceUtil.Key.FAVORITE_VERSION, version);
                                }
                                createFavoriteCallBack.createFavoriteSuccess();
                                UBDOther.recordCreateFavorite(createFavoriteRequest.getFavorites().get(0).getContentID(),recommendType);
                            }
                        } else {
                            if (createFavoriteCallBack != null) {
                                createFavoriteCallBack.createFavoriteFail();
                            }
                        }
                    }

                    @Override
                    public void onFail(@NonNull Throwable e) {
                        SuperLog.error(TAG, "[onError] CreateFavoriteEntity = " + e.toString());
                        if (createFavoriteCallBack != null) {
                            createFavoriteCallBack.createFavoriteFail();
                        }
                    }
                });
    }

    //删除收藏
    @SuppressLint("CheckResult")
    public void deleteFavorite(
            DeleteFavoriteRequest  deleteFavoriteRequest,
            DeleteFavoriteCallBack deleteFavoriteCallBack,
            ObservableTransformer<DeleteFavoriteResponse, DeleteFavoriteResponse> transformer) {

        HttpApi.getInstance().getService().deleteFavorite(getBaseUrl()+HttpConstant.DELETEFAVORITE, deleteFavoriteRequest)
                .compose(transformer)
                .subscribe(deleteFavoriteResponse -> {
                    Result result = deleteFavoriteResponse.getResult();
                    if (null != result && TextUtils.equals(result.getRetCode(), Result.RETCODE_OK)) {
                        String preVersion = deleteFavoriteResponse.getPreVersion();
                        String version = deleteFavoriteResponse.getVersion();
                        if (TextUtils.isEmpty(version)) {
                            version = preVersion;
                        }
                        if (!TextUtils.isEmpty(version)) {
                            SharedPreferenceUtil.getInstance().
                                    updateLocalVersion(SharedPreferenceUtil.Key.FAVORITE_VERSION, version);
                        }
                        if (null != deleteFavoriteCallBack)
                            deleteFavoriteCallBack.deleteFavoriteSuccess();
                    } else {
                        if (null != result){
                            handleError(result.getRetCode(),HttpConstant.DELETEFAVORITE);
                        }
                        if (deleteFavoriteCallBack != null) {
                            deleteFavoriteCallBack.deleteFavoriteFail();
                        }
                    }
                }, throwable -> {
                    if (deleteFavoriteCallBack != null) {
                        deleteFavoriteCallBack.deleteFavoriteFail();
                    }
                });
    }

    //查询收藏
    @SuppressLint("CheckResult")
    public void queryFavorite(
            QueryFavoriteRequest queryFavoriteRequest,
            QueryFavoriteCallBack queryFavoriteCallBack,
            ObservableTransformer<QueryFavoriteResponse, QueryFavoriteResponse> transformer) {

        HttpApi.getInstance().getService().queryFavorite(getBaseUrl()+HttpConstant.QUERYFAVORITE, queryFavoriteRequest)
                .compose(transformer)
                .subscribe(queryFavoriteResponse -> {
                    Result result = queryFavoriteResponse.getResult();
                    if (null != result && TextUtils.equals(result.getRetCode(), Result.RETCODE_OK)) {

                        String totalString = queryFavoriteResponse.getTotal();

                        int total = 0;

                        if (!TextUtils.isEmpty(totalString)) {
                            try {
                                total = Integer.parseInt(totalString.trim());
                            } catch (Exception e) {
                                total = 0;
                            }
                        }
                        String version = queryFavoriteResponse.getVersion();
                        if (!TextUtils.isEmpty(version)) {
                            SharedPreferenceUtil.getInstance().
                                    updateLocalVersion(SharedPreferenceUtil.Key.FAVORITE_VERSION, version);
                        }
                        if (null != queryFavoriteCallBack)
                            queryFavoriteCallBack.queryFavoriteSuccess(total, queryFavoriteResponse.getFavorites());

                    } else {
                        if(null!=result) {
                            handleError(result.getRetCode(), HttpConstant.QUERYFAVORITE);
                        }
                        if (queryFavoriteCallBack != null) {
                            queryFavoriteCallBack.queryFavoriteFail();
                        }
                    }

                }, throwable -> {
                    if (queryFavoriteCallBack != null) {
                        queryFavoriteCallBack.queryFavoriteFail();
                    }
                });
    }

    //新增书签
    public void createBookmark(
            CreateBookmarkRequest createBookmarkRequest,
            CreateBookmarkCallBack createBookmarkCallBack,
            ObservableTransformer<CreateBookmarkResponse, CreateBookmarkResponse> transformer) {

        RxCallBack<CreateBookmarkResponse> rxCallBack = new RxCallBack<CreateBookmarkResponse>(HttpConstant.CREATEBOOKMARK, this.rxAppCompatActivity) {
            @Override
            public void onSuccess(CreateBookmarkResponse createBookmarkResponse) {
                SuperLog.debug(TAG, "[onNext] CreateBookmarkEntity");

                if (createBookmarkResponse != null
                        && createBookmarkResponse.getResult() != null) {

                    Result result = createBookmarkResponse.getResult();

                    if (result != null
                            && !TextUtils.isEmpty(result.getRetCode())
                            && !result.getRetCode().equals(Result.RETCODE_OK)) {
                        SuperLog.error(TAG, "[onNext] CreateBookmarkEntity code = " + result.getRetCode()
                                + " message = " + result.getRetMsg());
                        handleError(result.getRetCode(),HttpConstant.CREATEBOOKMARK);
                    }

                    if (result != null && !TextUtils.isEmpty(result.getRetCode())
                            && result.getRetCode().equals(Result.RETCODE_OK)
                            && createBookmarkCallBack != null) {
                        createBookmarkCallBack.createBookmarkSuccess();
                    } else {
                        if (createBookmarkCallBack != null) {
                            createBookmarkCallBack.createBookmarkFail();
                        }
                    }
                } else {
                    if (createBookmarkCallBack != null) {
                        createBookmarkCallBack.createBookmarkFail();
                    }
                }
            }

            @Override
            public void onFail(@NonNull Throwable e) {
                SuperLog.error(TAG, "[onError] CreateBookmarkEntity = " + e.toString());
                if (createBookmarkCallBack != null) {
                    createBookmarkCallBack.createBookmarkFail();
                }
            }
        };
        HttpApi.getInstance().getService().createBookmark(getBaseUrl()+HttpConstant.CREATEBOOKMARK, createBookmarkRequest)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxCallBack);
    }

    //删除书签
    @SuppressLint("CheckResult")
    public void deleteBookMark(
            DeleteBookmarkRequest deleteBookmarkRequest,
            DeleteBookmarkCallBack deleteBookmarkCallBack,
            ObservableTransformer<DeleteBookmarkResponse, DeleteBookmarkResponse> transformer) {

        HttpApi.getInstance().getService().deleteBookmark(getBaseUrl()+HttpConstant.DELETEBOOKMARK,deleteBookmarkRequest)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deleteBookmarkResponse -> {
                    if (deleteBookmarkResponse != null
                            && deleteBookmarkResponse.getResult() != null) {

                        Result result = deleteBookmarkResponse.getResult();

                        if (result != null
                                && !TextUtils.isEmpty(result.getRetCode())
                                && !result.getRetCode().equals(Result.RETCODE_OK)) {
                            SuperLog.error(TAG, "[onNext] DeleteBookMarkEntity code = " + result.getRetCode()
                                    + " message = " + result.getRetMsg());
                            handleError(result.getRetCode(),HttpConstant.DELETEBOOKMARK);
                        }

                        if (result != null && !TextUtils.isEmpty(result.getRetCode())
                                && result.getRetCode().equals(Result.RETCODE_OK)
                                && deleteBookmarkCallBack != null) {
                            deleteBookmarkCallBack.deleteBookmarkSuccess();
                        } else {
                            if (deleteBookmarkCallBack != null) {
                                deleteBookmarkCallBack.deleteBookmarkFail();
                            }
                        }
                    }
                },throwable -> {
                    if (deleteBookmarkCallBack != null) {
                        deleteBookmarkCallBack.deleteBookmarkFail();
                    }
                });
    }

    //查询书签
    @SuppressLint("CheckResult")
    public void queryBookmark(
            QueryBookmarkRequest queryBookmarkRequest,
            QueryBookmarkCallBack queryBookmarkCallBack,
            ObservableTransformer<QueryBookmarkResponse, QueryBookmarkResponse> transformer) {

        HttpApi.getInstance().getService().queryBookmark(getBaseUrl()+HttpConstant.QUERYBOOKMARK, queryBookmarkRequest)
                .compose(transformer)
                .subscribe(queryBookmarkResponse -> {
                    if (queryBookmarkResponse != null
                            && queryBookmarkResponse.getResult() != null) {
                        Result result = queryBookmarkResponse.getResult();
                        if (result != null && !TextUtils.isEmpty(result.getRetCode())
                                && result.getRetCode().equals(Result.RETCODE_OK)
                                && queryBookmarkCallBack != null) {

                            String totalString = queryBookmarkResponse.getTotal();

                            int total = 0;

                            if (!TextUtils.isEmpty(totalString)) {
                                try {
                                    total = Integer.parseInt(totalString.trim());
                                } catch (Exception e) {
                                    total = 0;
                                }
                            }
                            String version = queryBookmarkResponse.getVersion();
                            if (!TextUtils.isEmpty(version)) {
                                SharedPreferenceUtil.getInstance().
                                        updateLocalVersion(SharedPreferenceUtil.Key.HISTORY_VERSION, version);
                            }
                            //儿童模式，只展示儿童模式书签
                            if(SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
                                if(null!=queryBookmarkResponse.getBookmarks()&&queryBookmarkResponse.getBookmarks().size()>0){
                                    List<BookmarkItem> items=new ArrayList<>();
                                    for(int i=0;i<queryBookmarkResponse.getBookmarks().size();i++){
                                        if(IsTV.isChildMode(queryBookmarkResponse.getBookmarks().get(i).getVOD())){
                                            items.add(queryBookmarkResponse.getBookmarks().get(i));
                                        }
                                    }
                                    if (queryBookmarkCallBack != null)
                                    {
                                        queryBookmarkCallBack.queryBookmarkSuccess(items.size(), items);
                                    }
                                }else{
                                    if (queryBookmarkCallBack != null) {
                                        queryBookmarkCallBack.queryBookmarkFail();
                                    }
                                }
                            }else{
                                if (queryBookmarkCallBack != null)
                                {
                                    queryBookmarkCallBack.queryBookmarkSuccess(total, queryBookmarkResponse.getBookmarks());
                                }
                            }
                        } else {
                            handleError(result.getRetCode(),HttpConstant.QUERYBOOKMARK);
                            if (queryBookmarkCallBack != null) {
                                queryBookmarkCallBack.queryBookmarkFail();
                            }

                        }
                    } else {
                        if (queryBookmarkCallBack != null) {
                            queryBookmarkCallBack.queryBookmarkFail();
                        }

                    }

                }, throwable -> {
                    if (queryBookmarkCallBack != null) {
                        queryBookmarkCallBack.queryBookmarkFail();
                    }
                });
    }

    //新增评分
    public void createContentScore(
            CreateContentScoreRequest createContentScoreRequest,
            CreateContentScoreCallBack createContentScoreCallBack,
            ObservableTransformer<CreateContentScoreResponse, CreateContentScoreResponse> transformer) {

        HttpApi.getInstance().getService().createContentScore(getBaseUrl()+HttpConstant.CREATECONTENTSCORE, createContentScoreRequest)
                .compose(transformer)
                .subscribe(new RxCallBack<CreateContentScoreResponse>(HttpConstant.CREATECONTENTSCORE, this.rxAppCompatActivity) {
                    @Override
                    public void onSuccess(CreateContentScoreResponse createContentScoreResponse) {
                        SuperLog.debug(TAG, "[onNext] CreateContentScoreEntity");

                        if (createContentScoreResponse != null
                                && createContentScoreResponse.getResult() != null) {

                            Result result = createContentScoreResponse.getResult();

                            if (result != null
                                    && !TextUtils.isEmpty(result.getRetCode())
                                    && !result.getRetCode().equals(Result.RETCODE_OK)) {
                                SuperLog.error(TAG, "[onNext] CreateContentScoreEntity code = " + result.getRetCode()
                                        + " message = " + result.getRetMsg());
                                handleError(result.getRetCode(),HttpConstant.CREATECONTENTSCORE);
                            }

                            if (result != null && !TextUtils.isEmpty(result.getRetCode())
                                    && result.getRetCode().equals(Result.RETCODE_OK)
                                    && createContentScoreCallBack != null) {

                                List<String> newScores = createContentScoreResponse.getNewScores();

                                List<Float> floatList = new ArrayList<Float>();

                                for (String newScore : newScores) {

                                    float score = 0f;

                                    if (!TextUtils.isEmpty(newScore)) {
                                        try {
                                            score = Float.parseFloat(newScore);
                                        } catch (Exception e) {
                                            score = 0f;
                                        }
                                    }

                                    floatList.add(score);
                                }

                                createContentScoreCallBack.createContentScoreSuccess(floatList);
                            } else {
                                if (createContentScoreCallBack != null) {
                                    createContentScoreCallBack.createContentScoreFail();
                                }
                            }
                        } else {
                            if (createContentScoreCallBack != null) {
                                createContentScoreCallBack.createContentScoreFail();
                            }
                        }
                    }

                    @Override
                    public void onFail(@NonNull Throwable e) {
                        SuperLog.error(TAG, "[onError] CreateContentScoreEntity = " + e.toString());
                        if (createContentScoreCallBack != null) {
                            createContentScoreCallBack.createContentScoreFail();
                        }
                    }
                });
    }

    //查询固移融合关联内容
    @SuppressLint("CheckResult")
    public void getRelatedContent(
            GetRelatedContentRequest getRelatedContentRequest,
            GetRelatedContentCallBack getRelatedContentCallBack,
            ObservableTransformer<GetRelatedContentResponse, GetRelatedContentResponse> transformer) {

        HttpApi.getInstance().getService().GetRelatedContent(getBaseUrl() + HttpConstant.GET_RELATED_CONTENT, getRelatedContentRequest)
                .compose(transformer)
                .subscribe(relatedContentResponse -> {
                    if (relatedContentResponse != null
                            && relatedContentResponse.getResult() != null) {
                        getRelatedContentCallBack.getRelatedContentSuccess(relatedContentResponse.getContents());
                    } else {
                        if (getRelatedContentCallBack != null) {
                            getRelatedContentCallBack.getRelatedContentFail();
                        }
                    }
                }, throwable -> {
                    if (getRelatedContentCallBack != null) {
                        getRelatedContentCallBack.getRelatedContentFail();
                    }
                });
    }

    //获取消息发送时间，格式yyyyMMddHHmmss
    public String getTimestamp() {
        String time = DateCalendarUtils.formatDate(NtpTimeService.queryNtpTime(), "yyyyMMddHHmmss");
        SuperLog.debug(TAG, "[getTimestamp] time = " + time);
        return time;
    }

    //新增收藏夹
    public void addFavoCatalog(
            AddFavoCatalogRequest  request,
            AddFavoCatalogCallBack callBack,
            ObservableTransformer<AddFavoCatalogResponse,AddFavoCatalogResponse> transformer) {

        HttpApi.getInstance().getService().addFavoCatalog(getBaseUrl()+HttpConstant.ADDFAVOCATALOG, request)
                .compose(transformer)
                .subscribe(new RxCallBack<AddFavoCatalogResponse>(HttpConstant.ADDFAVOCATALOG, this.rxAppCompatActivity) {
                    @Override
                    public void onSuccess(AddFavoCatalogResponse response) {
                        SuperLog.debug(TAG, "addFavoCatalog successfully.");
                        if(callBack == null){
                            return;
                        }

                        if (response != null && response.getResult() != null) {
                            Result result = response.getResult();
                            if (Result.RETCODE_OK.equals(result.getRetCode())) {
                                callBack.addFavoCatalogSuccess();
                            } else {
                                handleError(result.getRetCode(),HttpConstant.ADDFAVOCATALOG);
                                callBack.addFavoCatalogFail();
                            }
                        } else {
                            SuperLog.error(TAG, "addFavoCatalog failed because of response or result is empty.");
                            callBack.addFavoCatalogFail();
                        }
                    }

                    @Override
                    public void onFail(@NonNull Throwable e) {
                        SuperLog.error(TAG, e);
                        if (callBack != null) {
                            callBack.addFavoCatalogFail();
                        }
                    }
                });
    }

    //删除收藏夹
    public void deleteFavoCatalog(
            DeleteFavoCatalogRequest request,
            DeleteFavoCatalogCallback callBack,
            ObservableTransformer<DeleteFavoCatalogResponse,DeleteFavoCatalogResponse> transformer) {

        HttpApi.getInstance().getService().deleteFavoCatalog(getBaseUrl()+HttpConstant.DELETEFAVOCATALOG, request)
                .compose(transformer)
                .subscribe(new RxCallBack<DeleteFavoCatalogResponse>(HttpConstant.DELETEFAVOCATALOG, this.rxAppCompatActivity) {
                    @Override
                    public void onSuccess(DeleteFavoCatalogResponse response) {
                        SuperLog.debug(TAG, "deleteFavoCatalog successfully.");
                        if(callBack == null){
                            return;
                        }

                        if (response != null && response.getResult() != null) {
                            Result result = response.getResult();
                            if (Result.RETCODE_OK.equals(result.getRetCode())) {
                                callBack.deleteFavoCatalogSuccess();
                            } else {
                                handleError(result.getRetCode(),HttpConstant.DELETEFAVOCATALOG);
                                callBack.deleteFavoCatalogFail();
                            }
                        } else {
                            SuperLog.error(TAG, "delete FavoCatalog failed because of response or result is empty.");
                            callBack.deleteFavoCatalogFail();
                        }
                    }

                    @Override
                    public void onFail(@NonNull Throwable e) {
                        SuperLog.error(TAG, e);
                        if (callBack != null) {
                            callBack.deleteFavoCatalogFail();
                        }
                    }
                });
    }

    //查询收藏夹
    public void queryFavoCatalog(
            QueryFavoCatalogRequest request,
            QueryFavoCatalogCallback callBack,
            ObservableTransformer<QueryFavoCatalogResponse,QueryFavoCatalogResponse> transformer) {

        HttpApi.getInstance().getService().queryFavoCatalog(getBaseUrl()+HttpConstant.QUERYFAVOCATALOG, request)
                .compose(transformer)
                .subscribe(new RxCallBack<QueryFavoCatalogResponse>(HttpConstant.QUERYFAVOCATALOG, this.rxAppCompatActivity) {
                    @Override
                    public void onSuccess(QueryFavoCatalogResponse response) {
                        SuperLog.debug(TAG, "queryFavoCatalog successfully.");
                        if(callBack == null){
                            return;
                        }
                        if (response != null && response.getResult() != null) {
                            Result result = response.getResult();
                            if (Result.RETCODE_OK.equals(result.getRetCode())) {
                                callBack.queryFavoCatalogSuccess(response.getTotal(),response.getCatalogs());
                            } else {
                                handleError(result.getRetCode(),HttpConstant.QUERYFAVOCATALOG);
                                callBack.queryFavoCatalogFail();
                            }
                        } else {
                            SuperLog.error(TAG, "queryFavoCatalog failed because of response or result is empty.");
                            callBack.queryFavoCatalogFail();
                        }
                    }

                    @Override
                    public void onFail(@NonNull Throwable e) {
                        SuperLog.error(TAG, e);
                        if (callBack != null) {
                            callBack.queryFavoCatalogFail();
                        }
                    }
                });
    }
}