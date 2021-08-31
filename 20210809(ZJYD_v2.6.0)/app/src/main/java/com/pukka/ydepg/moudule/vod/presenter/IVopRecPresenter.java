package com.pukka.ydepg.moudule.vod.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.common.http.bean.request.QueryRecommendRequest;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODListCallBack;
import com.pukka.ydepg.common.http.v6bean.v6Controller.VODListController;
import com.pukka.ydepg.common.http.v6bean.v6node.Genre;
import com.pukka.ydepg.common.http.v6bean.v6node.ProduceZone;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.SubjectVODList;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODListBySubjectRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PBSRemixRecommendResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.Recommend;
import com.pukka.ydepg.common.report.ubd.extension.RecommendData;
import com.pukka.ydepg.common.report.ubd.scene.UBDRecommend;
import com.pukka.ydepg.common.report.ubd.scene.UBDRecommendImpression;
import com.pukka.ydepg.common.toptool.EpgTopFunctionMenu;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.mvp.presenter.TabItemPresenter;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.search.presenter.BasePresenter;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.json.JSONObject;

import java.util.List;

import static com.pukka.ydepg.moudule.vod.utils.RemixRecommendUtil.APPPINEDID_CHILD;

public class IVopRecPresenter extends BasePresenter {
    private static final String TAG = "IVopRecPresenter";

    private VODDetail mVodDetail;

    private final RxAppCompatActivity rxAppCompatActivity;

    //用于查询智能推荐
    private final TabItemPresenter presenter = new TabItemPresenter();

    private GetChildModeRecCallback recCallback;

    private String jiutianTracker = "";

    public IVopRecPresenter(RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity = rxAppCompatActivity;
    }

    //请求儿童版详情页推荐数据
    public void getChildModeRecList(VODDetail vodDetail, String vodid, GetChildModeRecCallback callback){
        this.recCallback = callback;
        this.mVodDetail = vodDetail;
        jiutianTracker = "";
        presenter.queryPBSRemixRecommend(null, vodid, "", new EpgTopFunctionMenu.OnPBSRemixRecommendListener() {
            @Override
            public void getRemixRecommendData(PBSRemixRecommendResponse pbsRemixRecommendResponse) {
                Log.i(TAG, "getRemixRecommendData: ");
                if (null == recCallback){
                    return;
                }
                if (null != pbsRemixRecommendResponse && null != pbsRemixRecommendResponse.getRecommends() && pbsRemixRecommendResponse.getRecommends().size()>0){
                    Recommend recommend = pbsRemixRecommendResponse.getRecommends().get(0);
                    if (null != recommend.getVODs() && recommend.getVODs().size()>0){
                        List<VOD> vods;
                        //最多展示6个内容
                        if (recommend.getVODs().size() <= 6){
                            vods = recommend.getVODs();
                        }else{
                            vods = recommend.getVODs().subList(0,7);
                        }
                        UBDRecommendImpression.recommendType_child = String.valueOf(recommend.getIdentifyType());
                        UBDRecommendImpression.sceneId_child       = recommend.getSceneId();

                        if (!TextUtils.isEmpty(recommend.getDisplay_tracker())){
                            jiutianTracker = recommend.getDisplay_tracker();
                        }else{
                            jiutianTracker = "";
                        }

                        recCallback.getChildModeRecSuccess(vods,RecommendData.RecommendType.IVOP_TYPE,jiutianTracker);
                        return;
                    }
                }

                //获取不到IVOP智能推荐数据，走以前的逻辑
                getChildModeRecListOld();
            }

            @Override
            public void getRemixRecommendDataFail() {
                //获取不到IVOP智能推荐数据，走以前的逻辑
                getChildModeRecListOld();
            }
        },APPPINEDID_CHILD,"7");
    }

    private void queryPBSRecommend(String scenarizedType, String id, VODListCallBack mVODListCallBack, String count, String offset) {
        Log.i(TAG, "queryPBSRecommend: ");
        QueryRecommendRequest request = new QueryRecommendRequest();
        request.setCount(count);
        request.setOffset(offset);
        request.setScenarizedType(scenarizedType);
        if (QueryRecommendRequest.SecenarizedType.CONTENT_DETAIL.equals(scenarizedType)) {
            request.setVodId(id);
        } else {
            if (!TextUtils.isEmpty(id)) {
                request.setProudctId(id);
            }
        }
        request.setVt("9");
        VODListController vodListController = new VODListController(mVODListCallBack, rxAppCompatActivity);
        vodListController.queryPBSRecommend(request, compose(bindToLifecycle(rxAppCompatActivity)), mVODListCallBack);
    }

    //vod列表没有过滤条件请求
    private void loadMoviesContent(String subjectId, String offset, String count, VODListCallBack mVODListCallBack) {
        Log.i(TAG, "loadMoviesContent: ");
        QueryVODListBySubjectRequest request = new QueryVODListBySubjectRequest();
        request.setSubjectID(subjectId);
        request.setCount(count);
        request.setOffset(offset);
        VODListController vodListController = new VODListController(mVODListCallBack, rxAppCompatActivity);
        vodListController.queryVODListBySubject(request, compose(bindToLifecycle(rxAppCompatActivity)), subjectId,null);
    }

    //老的儿童版请求数据逻辑
    private void getChildModeRecListOld(){
        SuperLog.debug(TAG, "IVOP recommend vod is closed or get failed. Begin to get manual recommend vod.");
        //V2.5.0需求更改为IVOP请求推荐,如失败或关闭直接加载人工推荐,废弃CVI推荐
//        String isUseCVI = SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.USE_CVI_RECOMMEND);
//        if (!TextUtils.isEmpty(isUseCVI) && isUseCVI.equals("1")) {
//            queryPBSRecommend(QueryRecommendRequest.SecenarizedType.CONTENT_DETAIL, mVodDetail.getID(), mVODListCallBack, "7", "0");
//        } else {
            //开关指定加载人工推荐
            if (null != mVodDetail.getSubjectIDs() && mVodDetail.getSubjectIDs().size() > 0) {
                String subject = SessionService.getInstance().getSession().getTerminalConfigurationChildSubject(Constant.CHILD_MODE_RECOMMEND_SUBJECT, mVodDetail.getSubjectIDs());
                loadMoviesContent(subject, "0", "7", mVODListCallBack);
            } else {
                if (null != recCallback) {
                    SuperLog.debug(TAG, "Manual recommend vod is closed. There will be no recommend data.");
                    recCallback.getChildModeRecSuccess(null,RecommendData.RecommendType.HAND_TYPE,"");
                }
            }
//        }
    }

    VODListCallBack mVODListCallBack = new VODListCallBack() {
        @Override
        public void queryVODListBySubjectSuccess(int total, List<VOD> vodList, String subjectId) {
            if (null != recCallback) {
                recCallback.getChildModeRecSuccess(vodList,RecommendData.RecommendType.HAND_TYPE,jiutianTracker);
            }
        }

        @Override
        public void queryVODListBySubjectFailed() { }

        @Override
        public void queryVODSubjectListSuccess(int total, List<Subject> subjects) { }

        @Override
        public void queryVODSubjectListFailed() { }

        @Override
        public void querySubjectVODBySubjectIDSuccess(int total, List<SubjectVODList> subjectVODLists) { }

        @Override
        public void querySubjectVODBySubjectIDFailed() { }

        @Override
        public void getContentConfigSuccess(List<ProduceZone> produceZoneList, List<Genre> genreList) { }

        @Override
        public void getContentConfigFailed() { }

        @Override
        public void querySubjectDetailSuccess(int total, List<Subject> subjects) { }

        @Override
        public void queryPSBRecommendSuccess(int total, List<VOD> vodDetails) {
            //V2.5.0已经废弃CVI推荐
            if (null == recCallback){
                return;
            }
            if (null != vodDetails && vodDetails.size() > 5){
                recCallback.getChildModeRecSuccess(vodDetails,RecommendData.RecommendType.CVI_TYPE,jiutianTracker);
                return;
            }else{
                //没有获取到CVI推荐数据,加载人工推荐
                if (null != mVodDetail.getSubjectIDs() && mVodDetail.getSubjectIDs().size() > 0) {
                    String subject = SessionService.getInstance().getSession().getTerminalConfigurationChildSubject(Constant.CHILD_MODE_RECOMMEND_SUBJECT, mVodDetail.getSubjectIDs());
                    loadMoviesContent(subject, "0", "7", mVODListCallBack);
                    return;
                }
            }
            recCallback.getChildModeRecFail();
        }

        @Override
        public void queryPSBRecommendFail() {
            //获取CVI推荐数据失败，加载人工推荐数据
            if (null == recCallback){
                return;
            }
            if (null != mVodDetail.getSubjectIDs() && mVodDetail.getSubjectIDs().size() > 0) {
                String subject = SessionService.getInstance().getSession().getTerminalConfigurationChildSubject(Constant.CHILD_MODE_RECOMMEND_SUBJECT, mVodDetail.getSubjectIDs());
                loadMoviesContent(subject, "0", "7", mVODListCallBack);
                return;
            }
            recCallback.getChildModeRecFail();
        }

        @Override
        public void onError() { }
    };

    //获取儿童版推荐内容回调
    public interface GetChildModeRecCallback{
        void getChildModeRecSuccess(List<VOD> vods, String type,String tarcker);
        void getChildModeRecFail();
    }
}