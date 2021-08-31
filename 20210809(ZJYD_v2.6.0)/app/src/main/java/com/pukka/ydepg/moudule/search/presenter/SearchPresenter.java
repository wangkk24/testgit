package com.pukka.ydepg.moudule.search.presenter;

import com.pukka.ydepg.common.http.v6bean.v6CallBack.QueryHotSearchContentsListCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.SearchContentCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.SearchHotKeyCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.SuggestKeywordCallBack;
import com.pukka.ydepg.common.http.v6bean.v6Controller.SearchController;
import com.pukka.ydepg.common.http.v6bean.v6node.CastDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.SearchExcluder;
import com.pukka.ydepg.common.http.v6bean.v6node.SearchFilter;
import com.pukka.ydepg.common.http.v6bean.v6request.SearchContentRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SuggestKeywordRequest;
import com.pukka.ydepg.moudule.search.view.SearchDataView;
import com.pukka.ydepg.moudule.search.view.SearchResultDataView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.module.search.presenter.SearchPresenter.java
 * @author:xj
 * @date: 2018-01-23 15:15
 */
public class SearchPresenter extends BasePresenter implements SearchHotKeyCallBack, QueryHotSearchContentsListCallBack, SearchContentCallBack {

    //最大联想数目,需求【201910215199】要求为10
    private static final String MAX_SUGGEST_COUNT = "10";

    //一次搜索最大数目
    private static final String MAX_SEARCH_COUNT = "15";

    private RxAppCompatActivity mRxAppCompatActivity;

    //搜索activityView回调
    private SearchDataView mSearchDataView;

    //搜索结果回调
    private SearchResultDataView mSearchResultDataView;

    //请求控制器
    private SearchController mSearchController;

    public SearchPresenter(RxAppCompatActivity mRxAppCompatActivity) {
        this.mRxAppCompatActivity = mRxAppCompatActivity;
        mSearchController = new SearchController(mRxAppCompatActivity);
    }

    public void setmSearchDataView(SearchDataView mSearchDataView) {
        this.mSearchDataView = mSearchDataView;
    }

    public void setmSearchResultDataView(SearchResultDataView mSearchResultDataView) {
        this.mSearchResultDataView = mSearchResultDataView;
    }

    //联想
    public void suggestKeyWords(String searchKey, SuggestKeywordCallBack callBack){
        SuggestKeywordRequest suggestKeywordRequest = new SuggestKeywordRequest();
        suggestKeywordRequest.setCount(MAX_SUGGEST_COUNT);
        suggestKeywordRequest.setKey(searchKey);
        mSearchController.suggestKeyword(suggestKeywordRequest, callBack,compose(bindToLifecycle(mRxAppCompatActivity)));
    }

    //搜索
    public void searchContent(String subjectId, String searchKey, int offset, List<String> filterSubjectIDs, boolean isActor, boolean isMigu){
        SearchContentRequest request = new SearchContentRequest();
        request.setOffset(String.valueOf(offset));
        request.setCount(MAX_SEARCH_COUNT);
        request.setSearchKey(searchKey);
        request.setSubjectId(subjectId);

        List<String> searchScopes = new ArrayList<>();
        if(isActor) {
            searchScopes.add(SearchContentRequest.SearchScope.ACTOR);
        }else{
            searchScopes.add(SearchContentRequest.SearchScope.CONTENT_NAME);
            //排他条件
            SearchExcluder excluder = new SearchExcluder();
            excluder.setSubSitcomSearchFlag(0); //0: 不搜索连续剧子集,1: 搜索连续剧子集
            request.setExcluder(excluder);
        }
        request.setSearchScopes(searchScopes);

        List<String> sortTypes = new ArrayList<>();
        //sortTypes.add(SearchContentRequest.SortType.CREATE_TIME_DESC);
        sortTypes.add(SearchContentRequest.SortType.START_TIME_DESC);
        sortTypes.add(SearchContentRequest.SortType.PLAY_TIMES_DESC);
        request.setSortType(sortTypes);

        List<String> contentTypeList = new ArrayList<>();
        contentTypeList.add("VOD");
        request.setContentTypes(contentTypeList);

        if( filterSubjectIDs != null && !filterSubjectIDs.isEmpty()){
            SearchFilter sf = new SearchFilter();
            sf.setSubjectIDs(filterSubjectIDs);
            request.setFilter(sf);
        }

        //对于咪咕爱看的结果是移动网络的VOD内容,不需要根据CPID过滤结果
        mSearchController.searchContent(request,this,!isMigu);
    }

    @Override
    public void searchHotKeySuccess(int total, List<String> hotKeys) {
        if (null != mSearchDataView ){
            mSearchDataView.searchHotSuccess(total,hotKeys);
        }
    }

    @Override
    public void searchHotKeyFail() {
        if (null != mSearchDataView){
            mSearchDataView.searchHotKeyFail();
        }
    }

    @Override
    public void searchHotKeyCancle() { }

    @Override
    public void queryHotContentListSuccess(List<Content> contents) {
        if (null != mSearchDataView){
            mSearchDataView.queryHotContentListSuccess(contents);
        }
    }

    @Override
    public void queryHotContentListFailed() {
        if (null != mSearchDataView){
            mSearchDataView.queryHotContentListFailed();
        }
    }

    @Override
    public void queryHotContentListCancel() { }

    @Override
    public void searchContentSuccess(int total, List<Content> contents,String key) {
        if (null != mSearchResultDataView){
            mSearchResultDataView.searchContentSuccess(total,contents,key);
        }
    }

    @Override
    public void searchContentFail(String key) {
        if (null != mSearchResultDataView){
            mSearchResultDataView.searchContentFail(key);
        }
    }

    @Override
    public void getCastDetailSuccess(List<CastDetail> mCastDetails) { }

    @Override
    public void getCastDetailFail() { }
}