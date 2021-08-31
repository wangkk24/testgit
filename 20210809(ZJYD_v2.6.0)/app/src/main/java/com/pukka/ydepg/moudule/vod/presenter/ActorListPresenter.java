package com.pukka.ydepg.moudule.vod.presenter;

import android.text.TextUtils;

import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.SearchContentCallBack;
import com.pukka.ydepg.common.http.v6bean.v6Controller.SearchController;
import com.pukka.ydepg.common.http.v6bean.v6node.CastDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6request.GetCastDetailRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SearchContentRequest;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.search.presenter.BasePresenter;
import com.pukka.ydepg.moudule.vod.view.ActorDataView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author: ld
 * @date: 2017-12-19
 */
public class ActorListPresenter extends BasePresenter implements Presenter {
    private ActorDataView mActorDataView;
    private SearchController mSearchContentControl;

    public ActorListPresenter(RxAppCompatActivity rxAppCompatActivity) {
        mSearchContentControl = new SearchController(rxAppCompatActivity);
    }

    public void setActorDataView(ActorDataView actorDataView) {
        this.mActorDataView = actorDataView;
    }

    public void loadActorList(String offset,String count,String searchkey, String contentType, String searchScope) {
        SearchContentRequest request = new SearchContentRequest();
        request.setCount(count);
        request.setOffset(offset);
        request.setSearchKey(searchkey);

        List<String> contentTypes = new ArrayList<>();
        contentTypes.add(contentType);
        request.setContentTypes(contentTypes);

        List<String> searchScopes = new ArrayList<>();
        searchScopes.add(searchScope);
        request.setSearchScopes(searchScopes);

        List<String> sortTypes = new ArrayList<>();
        sortTypes.add("PRODUCE_DATE:DESC");
        request.setSortType(sortTypes);

        String categoryId2 = CommonUtil.getConfigValue("vod_subject_id");
        if (!TextUtils.isEmpty(categoryId2)) {
            request.setSubjectId(categoryId2);
        }

        mSearchContentControl.searchContent(request,mSearchContentCallback,true);
    }

    public void getCastDetails(List<String> castDetails){
        GetCastDetailRequest getCastDetailRequest=new GetCastDetailRequest();
        getCastDetailRequest.setCastIds(castDetails);
        mSearchContentControl.getCastDetail(getCastDetailRequest,mSearchContentCallback);
    }

    @Override
    public void resume() {}

    @Override
    public void pause() {}

    @Override
    public void destroy() {}

    private SearchContentCallBack mSearchContentCallback= new SearchContentCallBack() {
        @Override
        public void searchContentSuccess(int total, List<Content> contents,String key) {
            if (contents != null && contents.size() != 0) {
                mActorDataView.showActorContent(contents,total);
            }else {
                if(mActorDataView.getLoadNum()==0){
                    mActorDataView.showNoContent();
                }
            }
        }

        @Override
        public void searchContentFail(String key) { }

        @Override
        public void getCastDetailSuccess(List<CastDetail> mCastDetails) {
            if(null!=mActorDataView){
                mActorDataView.getCastDetail(mCastDetails);
            }
        }

        @Override
        public void getCastDetailFail() {
            SuperLog.error("ActorListPresenter","getCastDetailFail");
        }
    };

    public void showFocusNum(String s) {
        mActorDataView.showFocusNum(s);
    }
}