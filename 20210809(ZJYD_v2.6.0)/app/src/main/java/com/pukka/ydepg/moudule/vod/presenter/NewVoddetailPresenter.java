package com.pukka.ydepg.moudule.vod.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.SearchContentCallBack;
import com.pukka.ydepg.common.http.v6bean.v6Controller.SearchController;
import com.pukka.ydepg.common.http.v6bean.v6node.CastDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODFilter;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODListBySubjectRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SearchContentRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PBSRemixRecommendResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryVODListBySubjectResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.Recommend;
import com.pukka.ydepg.common.report.ubd.scene.UBDRecommendImpression;
import com.pukka.ydepg.common.toptool.EpgTopFunctionMenu;
import com.pukka.ydepg.launcher.mvp.presenter.TabItemPresenter;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.livetv.presenter.base.BasePresenter;
import com.pukka.ydepg.moudule.vod.utils.RemixRecommendUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;


public class NewVoddetailPresenter extends BasePresenter {

    private static final String TAG = "NewVoddetailPresenter";

    private static final String RECOMMEND_RECENT = "recommend_recent";

    private static final String RECOMMEND_GUESS = "recommend_guess";

    private List<VOD> recentList;

    private List<VOD> guessList;

    private String searchKey = "";

    GetRecommedCallback callback;
    private String vodid = "";

    //????????????????????????
    TabItemPresenter presenter = new TabItemPresenter();

    public NewVoddetailPresenter() {
        getCpIdString();
    }

    //??????vod??????????????????????????????
    public void getRecommend(String vodid, String searchKey,Context context,GetRecommedCallback callback){
        //??????????????????
        this.vodid = vodid;
        recentList = new ArrayList<>();
        guessList = new ArrayList<>();
        this.searchKey = searchKey;
        this.callback = callback;
        //??????????????????????????????
        getArtificialRecommend(RECOMMEND_RECENT,"4",context,callbackForRecent);
    }

    //?????????????????????????????????????????????????????????????????????????????????????????????
    private void getArtificialRecommend(String type,String count,Context context,GetArtificialRecommendCallback callback){
        QueryVODListBySubjectRequest request = new QueryVODListBySubjectRequest();

        String subjectID = "";
        if (type.equals(RECOMMEND_RECENT)){
            String configStr = SessionService.getInstance().getSession().getTerminalConfigurationVoddetailRecommendRecent();
            if (!TextUtils.isEmpty(configStr)){
                subjectID = configStr;
            }
        }else if (type.equals(RECOMMEND_GUESS)){
            String configStr = RemixRecommendUtil.getRemixRecommendSubjectid(RemixRecommendUtil.RemixRec_Voddetail);
            if (!TextUtils.isEmpty(configStr)){
                subjectID = configStr;
            }
        }

        if (!TextUtils.isEmpty(subjectID)){
            request.setSubjectID(subjectID);
            //??????UBD
            UBDRecommendImpression.sceneId_vod = subjectID;
            UBDRecommendImpression.recommendType_vod = "-1";
        }else{
            //?????????????????????????????????
            if (null != callback){
                callback.getArtificialRecommendFail(context);
            }
            //??????UBD?????????????????????????????????????????????
            UBDRecommendImpression.sceneId_vod = null;
            UBDRecommendImpression.recommendType_vod = null;
            return;
        }

        request.setCount(count);
        request.setOffset("0");

        if(!TextUtils.isEmpty(strCpIdList)){
            if(request.getVODFilter()!=null){
                request.getVODFilter().setCpId(strCpIdList);
            } else {
                VODFilter filter = new VODFilter();
                filter.setCpId(strCpIdList);
                request.setVODFilter(filter);
            }
        }

        String url = HttpUtil.getVspUrl(HttpConstant.QUERYVODLISTSTCPROPSBYSUBJECT) + HttpUtil.addUserVODListFilter();

        RxCallBack<QueryVODListBySubjectResponse> rxCallBack = new RxCallBack<QueryVODListBySubjectResponse>(url, OTTApplication.getContext()) {
            @Override
            public void onSuccess(QueryVODListBySubjectResponse response) {
                if (response != null && response.getResult() != null && response.getResult().getRetCode()!=null) {
                    Result result = response.getResult();
                    if(Result.RETCODE_OK.equals(result.getRetCode())){
                        //??????????????????????????????
                        if (null != callback){
                            callback.getArtificialRecommendSuccess(response.getVODs(),context);
                            return;
                        }
                    }
                }
                if (null != callback){
                    callback.getArtificialRecommendFail(context);
                }
            }

            @Override public void onFail(@NonNull Throwable e) {
                if (null != callback){
                    callback.getArtificialRecommendFail(context);
                }
            }
        };

        HttpApi.getInstance().getService().queryVODListBySubject(url,request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxCallBack);
    }

    //????????????????????????
    private void getVoddetailMixRecommend(String appointedId,EpgTopFunctionMenu.OnPBSRemixRecommendListener onPBSRemixRecommendListener){
        presenter.queryPBSRemixRecommend(null,vodid,"", onPBSRemixRecommendListener,appointedId,"6");
    }

    //????????????????????????
    private void getActorRecommend(Context context, String searchKey){
        if( callback == null ){
            return;
        }

        final List<Content> emptyActorRecommendList = new ArrayList<>();
        if (TextUtils.isEmpty(searchKey)){
            callback.getRecommendSuccess(recentList,guessList,emptyActorRecommendList);
            return;
        }

        SearchController controller = new SearchController((RxAppCompatActivity) context);

        SearchContentRequest request = new SearchContentRequest();
        request.setOffset("0");
        request.setCount("6");
        request.setSearchKey(searchKey);

        List<String> searchScopes = new ArrayList<>();
        searchScopes.add(SearchContentRequest.SearchScope.ACTOR);
        request.setSearchScopes(searchScopes);

        List<String> sortTypes = new ArrayList<>();
        //sortTypes.add(SearchContentRequest.SortType.CREATE_TIME_DESC);
        sortTypes.add(SearchContentRequest.SortType.START_TIME_DESC);
        sortTypes.add(SearchContentRequest.SortType.PLAY_TIMES_DESC);
        request.setSortType(sortTypes);

        List<String> contentTypeList = new ArrayList<>();
        contentTypeList.add("VOD");
        request.setContentTypes(contentTypeList);

        //Added by liuxia at 20201014,?????????????????????????????????????????????????????????,?????????????????????subjectID
        //??????????????????????????????,??????????????????ActorListPresenter.java?????????,??????????????????????????????????????????
        String categoryId2 = CommonUtil.getConfigValue("vod_subject_id");
        if (!TextUtils.isEmpty(categoryId2)) {
            request.setSubjectId(categoryId2);
        }

        controller.searchContent(request, new SearchContentCallBack() {
            @Override
            public void searchContentSuccess(int total, List<Content> contents, String key) {
                callback.getRecommendSuccess(recentList,guessList,contents);
            }

            @Override
            public void searchContentFail(String key) {
                callback.getRecommendSuccess(recentList,guessList,emptyActorRecommendList);
            }

            @Override
            public void getCastDetailSuccess(List<CastDetail> mCastDetails) {
                callback.getRecommendSuccess(recentList,guessList,emptyActorRecommendList);
            }

            @Override
            public void getCastDetailFail() {
                callback.getRecommendSuccess(recentList,guessList,emptyActorRecommendList);
            }
        },true);
    }

    private void getGuessRecommend(Context context){
        boolean needRemixRex = RemixRecommendUtil.getRemixRecommendSwitch(RemixRecommendUtil.RemixRec_Voddetail);
        if (needRemixRex){
            //????????????????????????
            getVoddetailMixRecommend(RemixRecommendUtil.APPPINEDID_VOD, new EpgTopFunctionMenu.OnPBSRemixRecommendListener() {
                @Override
                public void getRemixRecommendData(PBSRemixRecommendResponse pbsRemixRecommendResponse) {
                    if (null != pbsRemixRecommendResponse && null != pbsRemixRecommendResponse.getRecommends() && pbsRemixRecommendResponse.getRecommends().size()>0){
                        Recommend recommend = pbsRemixRecommendResponse.getRecommends().get(0);
                        if (null != recommend.getVODs() && recommend.getVODs().size()>0){
                            guessList = recommend.getVODs();
                            //??????UBD
                            UBDRecommendImpression.recommendType_vod = String.valueOf(recommend.getIdentifyType());
                            UBDRecommendImpression.sceneId_vod       = recommend.getSceneId();
                            //????????????????????????
                            getActorRecommend(context,searchKey);
                            return;
                        }
                    }
                    //?????????????????????????????????????????????
                    getArtificialRecommend(RECOMMEND_GUESS,"6",context,callbackForGuess);
                }

                @Override
                public void getRemixRecommendDataFail() {
                    //?????????????????????????????????????????????
                    getArtificialRecommend(RECOMMEND_GUESS,"6",context,callbackForGuess);
                }
            });
        }else{
            //????????????????????????
            getArtificialRecommend(RECOMMEND_GUESS,"6",context,callbackForGuess);
        }
    }

    //????????????
    private GetArtificialRecommendCallback callbackForRecent = new GetArtificialRecommendCallback() {
        @Override
        public void getArtificialRecommendSuccess(List<VOD> list,Context context) {
            if (null != list && list.size()>0){
                recentList = list;
            }
            //????????????????????????
            getGuessRecommend(context);
        }

        @Override
        public void getArtificialRecommendFail(Context context) {
            //????????????????????????
            getGuessRecommend(context);
        }
    };

    //????????????????????????????????????
    private GetArtificialRecommendCallback callbackForGuess = new GetArtificialRecommendCallback() {
        @Override
        public void getArtificialRecommendSuccess(List<VOD> list,Context context) {
            if (null != list && list.size()>0){
                guessList = list;
            }

            //??????????????????
            getActorRecommend(context,searchKey);
        }

        @Override
        public void getArtificialRecommendFail(Context context) {
            //??????????????????
            getActorRecommend(context,searchKey);
        }
    };

    public interface GetArtificialRecommendCallback{
        void getArtificialRecommendSuccess(List<VOD> list,Context context);
        void getArtificialRecommendFail(Context context);
    }

    public interface GetRecommedCallback{
        void getRecommendSuccess(List<VOD> recentList,List<VOD> recommendList,List<Content> actorRecommendList);
    }

    /**
     * ??????????????????CPID,??????CPID???????????????
     */
    private String strCpIdList;

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
}