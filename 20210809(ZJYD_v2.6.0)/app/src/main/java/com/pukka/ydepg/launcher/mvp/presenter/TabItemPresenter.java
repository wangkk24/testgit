package com.pukka.ydepg.launcher.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.bean.node.SubjectIDList;
import com.pukka.ydepg.common.http.bean.request.QueryBatchVODListBySubjectRequest;
import com.pukka.ydepg.common.http.bean.request.QueryVODListBySubjectConditionRequest;
import com.pukka.ydepg.common.http.bean.response.QueryBatchVODListBySubjectResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6request.GetVODDetailRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryBookmarkRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryFavoriteRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryLauncherRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QuerySubjectVODBySubjectIDRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.GetVODDetailResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PBSRemixRecommendResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayChannelResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryBookmarkResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryFavoriteResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryLauncherResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryPHMLauncherListRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryPHMLauncherListResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySubjectVODBySubjectIDResponse;
import com.pukka.ydepg.common.toptool.EpgTopFunctionMenu;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.GroupElement;
import com.pukka.ydepg.launcher.bean.node.SubjectVODLists;
import com.pukka.ydepg.launcher.bean.node.SubjectVodsList;
import com.pukka.ydepg.launcher.http.LoginNetApi;
import com.pukka.ydepg.launcher.mvp.contact.TabItemContact;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.featured.view.LauncherService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 动态模板
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.mvp.presenter.LauncherPresenter.java
 * @date: 2017-12-15 14:41
 * @version: V1.0 描述当前版本功能
 */
public class TabItemPresenter extends BasePresenter<TabItemContact.ITabItemView> implements TabItemContact.ITabItemPresenter {

    private static final String TAG = TabItemPresenter.class.getSimpleName();

    private static final String CNTARRANGE         = "CNTARRANGE";
    private static final String QUERY_COUNT_LIMIT  = "50"; //查询数量
    private static final String QUERY_OFFSET_LIMIT = "0"; //查询开始位置
    private static final String QUERY_CONTENT_TYPE = "VOD";

    //navID
    private String mNavID;

    private OkHttpClient client;

    //queryEpgHome开始请求的页数
    private int mOffset = 0;
    private int mCount = 5;

    private List<SubjectIDList> subjectIDLists;

    private int defaultTimes = 0;//queryEpgHomeVod 失败次数
    public TabItemPresenter() {
        client = new OkHttpClient();
    }

    public TabItemPresenter(TabItemContact.ITabItemView view) {mView = view;}

    public void setOffSet(){
        this.mOffset ++;
    }
    public int  getOffSet(){
        return mOffset;
    }
    public void resetOffSet(){
        this.mOffset = 0;
    }
    public int  getCount(){
        return mCount;
    }
    public int  getPosition(){
        return mOffset*mCount;
    }

    public List<SubjectIDList> getSubjectIDLists(){
        return subjectIDLists;
    }

    /**
     * Type3的数据现在只有type3，临时先这样处理
     */
    @Override
    public void queryHomeEpg(String navId, List<GroupElement> groupElements, Context context) {

        if (CollectionUtil.isEmpty(groupElements)) {// || (CollectionUtil.isEmpty(getSubjectIDLists(groupElements)) || getSubjectIDLists(groupElements).size() == 0)
            /*if (null != mView){
                mView.loadVODData(null);
            }*/
            SuperLog.error(TAG, "groupElements is empty");
            return;
        }
        this.mNavID = navId;
        QueryBatchVODListBySubjectRequest request = new QueryBatchVODListBySubjectRequest();
        List<QueryVODListBySubjectConditionRequest> requestList = new ArrayList<>();
        subjectIDLists = getSubjectIDLists(groupElements);
        for (int i = mOffset*mCount; i < subjectIDLists.size(); i++) {
            if (i > mOffset*mCount + 4){
                break;
            }
            QueryVODListBySubjectConditionRequest requestSub = new QueryVODListBySubjectConditionRequest();
            requestSub.setSortType(CNTARRANGE);  //按栏目下内容编排顺序排序
            requestSub.setCount(10);
            requestSub.setOffset(0);
            requestSub.setSubjectID(subjectIDLists.get(i).getSubjectID());
            requestList.add(requestSub);
        }
        request.setConditions(requestList);
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.BATCH_QUERY_VODLIST_BYSUBJECT;
        HttpApi.getInstance().getService().queryBatchVODListBySubject(url, request).compose(onCompose(null == mView ? null : mView.bindToLife())).subscribe(new RxCallBack<QueryBatchVODListBySubjectResponse>(mView, HttpConstant.BATCH_QUERY_VODLIST_BYSUBJECT, context) {
            @Override
            public void onError(Throwable e) {
                SuperLog.error(TAG, "queryEpgHomeVod onError");
                OTTApplication.getContext().setLastNavID(navId);
                OTTApplication.getContext().setGroupElements(groupElements);
                OTTApplication.getContext().setView(mView);
                super.onError(e);
            }

            @Override
            public void onSuccess(@NonNull QueryBatchVODListBySubjectResponse response) {
                defaultTimes = 0;
                SuperLog.info2SD(TAG, "queryEpgHomeVod successfully, navID=" + navId);
                if (mView != null && null != response.getSubjectVODLists()) {
                    mView.loadVODData(processResponseData(response.getSubjectVODLists()));
                    OTTApplication.getContext().setLastNavID("");
                    OTTApplication.getContext().setGroupElements(null);
                    OTTApplication.getContext().setView(null);
                } else {
                    SuperLog.error(TAG, "mView or SubjectVODLists is null, can not load VOD data!");
                    OTTApplication.getContext().setLastNavID(navId);
                    OTTApplication.getContext().setGroupElements(groupElements);
                    OTTApplication.getContext().setView(mView);
                }
            }

            @Override
            public void onFail(@NonNull Throwable e) {
                SuperLog.error(TAG, "queryEpgHomeVOD failed, navID=" + navId + "  exception: " + e);
                OTTApplication.getContext().setLastNavID(navId);
                OTTApplication.getContext().setGroupElements(groupElements);
                OTTApplication.getContext().setView(mView);
                defaultTimes++;
                //失败后最多重试三次，每次延时三秒执行
                if (defaultTimes < 4) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3L * 1000);
                                queryHomeEpg(navId, groupElements, context);
                            } catch (InterruptedException e1) {
                                SuperLog.error(TAG, "Interrupted!" + e);
                                Thread.currentThread().interrupt();
                            }
                        }
                    }).start();

                }
            }
        });
    }

    private List<SubjectVodsList> processResponseData(List<SubjectVODLists> list){
        List<SubjectVodsList> subList = new ArrayList<>();
        for (SubjectVODLists subjectVODLists : list){
            SubjectVodsList subjectVodsList = new SubjectVodsList();
            subjectVodsList.setSubjectID(subjectVODLists.getSubject().getID());
            subjectVodsList.setVodList(subjectVODLists.getVodList());
            subList.add(subjectVodsList);
        }
        return subList;
    }

    @Override
    public void getVODDetail(String vodId, RxCallBack<GetVODDetailResponse> callBack) {
        GetVODDetailRequest getVODDetailRequest = new GetVODDetailRequest();
        getVODDetailRequest.setVODID(vodId);
        HttpApi.getInstance().getService().getVODDetail(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.GETVODDETAIL, getVODDetailRequest)
                .compose(onCompose(null == mView ? null : mView.bindToLife()))
                .subscribe(callBack);
    }

    @Override
    public void getPlayUrl(VODDetail detail, RxCallBack<PlayVODResponse> callBack) {
        List<VODMediaFile> vodMediaFiles = detail.getMediaFiles();
        if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
            VODMediaFile vodMediaFile = vodMediaFiles.get(0);
            PlayVODRequest mPlayVodRequest = new PlayVODRequest();
            mPlayVodRequest.setVODID(detail.getID());
            mPlayVodRequest.setMediaID(vodMediaFile.getID());
            mPlayVodRequest.setURLFormat("1");
            mPlayVodRequest.setIsReturnProduct("1");

            HttpApi.getInstance().getService().playVOD(
                    ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.PLAYVOD, mPlayVodRequest)
                    .compose(onCompose(null == mView ? null : mView.bindToLife()))
                    .subscribe(callBack);
        }
    }

    public void queryBookMark(RxCallBack<QueryBookmarkResponse> callBack) {
        QueryBookmarkRequest request = new QueryBookmarkRequest();
        request.setOffset(QUERY_OFFSET_LIMIT);
        request.setCount(QUERY_COUNT_LIMIT);
        request.setSortType(Constant.SortType
                .UPDATE_TIME_DESC);
        String interfaceName = HttpConstant.QUERYBOOKMARK;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + interfaceName;
        HttpApi.getInstance().getService().queryBookmark(url, request).compose(onCompose(mView.bindToLife())).subscribe(callBack);
    }

    public void queryFavorite(RxCallBack<QueryFavoriteResponse> callBack) {
        List<String> contentTypeList = new ArrayList<>();
        contentTypeList.add(QUERY_CONTENT_TYPE);
        QueryFavoriteRequest request = new QueryFavoriteRequest();
        request.setContentTypes(contentTypeList);
        request.setOffset(QUERY_OFFSET_LIMIT);
        request.setCount(QUERY_COUNT_LIMIT);
        request.setSortType(Constant.SortType
                .SORTTYPE_FAVO_TIME_DESC);
        String interfaceName = HttpConstant.QUERYFAVORITE;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + interfaceName;
        HttpApi.getInstance().getService().queryFavorite(url, request).compose(onCompose(mView.bindToLife())).subscribe(callBack);
    }

    /**
     * 生成SubjectIDList，把需要查询的id和数量传入
     *
     * @param groupElements
     * @return
     */
    public List<SubjectIDList> getSubjectIDLists(List<GroupElement> groupElements) {
        List<SubjectIDList> subjectIDLists = new ArrayList<SubjectIDList>();
        int index = 0;
        for (GroupElement groupElement : groupElements) {
            String categoryCode = groupElement.getGroup().getCategoryCode();
            List<Element> elements = groupElement.getElement();
            boolean isLoad = false;
            for (Element element : elements) {
                //如果elements中有ForceDefaultData属性为false的元素，isLoad为true，否则所有数据都在本地获取
                if (TextUtils.equals(element.getForceDefaultData(), "false")) {
                    isLoad = true;
                    break;
                }
            }
            if (!isLoad) {
                groupElement.setDataIndex(-1);
            }
            if (!TextUtils.isEmpty(categoryCode)) {
                int size = elements.size();
                //如果elements中有元素需要加载，把id加入
                if (size > 0) {
                    if (isLoad) {
                        groupElement.setDataIndex(index);
                        SuperLog.debug(TAG, "load:" + "NavId:" + mNavID + "categoryCode:" + categoryCode);
                        subjectIDLists.add(new SubjectIDList(categoryCode, size));
                        index++;
                    }
                }
            } else {
                groupElement.setDataIndex(-1);
            }
        }
        return subjectIDLists;
    }

    public void playChannel(PlayChannelRequest playChannelRequest, RxCallBack<PlayChannelResponse> rxCallBack) {
        HttpApi.getInstance().getService().playChannel(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.PLAYCHANNEL, playChannelRequest)
                .compose(onCompose(null == mView ? null : mView.bindToLife()))
                .subscribe(rxCallBack);
    }


    public void queryLauncher(Context context, OnQueryLauncherListener listener) {
        String queryPhmLauncher = LauncherService.getInstance().getQueryPhmLauncher();
        if (!TextUtils.isEmpty(queryPhmLauncher) && queryPhmLauncher.equalsIgnoreCase("1")){
            SuperLog.debug(TAG,"refreshLauncher and [queryPHMLauncherList]");
            queryPHMLauncherList(context,listener,true,null);
        }else if (TextUtils.isEmpty(queryPhmLauncher) || queryPhmLauncher.equalsIgnoreCase("0")){
            SuperLog.debug(TAG,"refreshLauncher and [queryVSPLauncher];queryPhmLauncher="+queryPhmLauncher);
            queryVSPLauncher(context,listener);
        }
    }

    private void queryVSPLauncher(Context context, OnQueryLauncherListener listener){
        String systemModel = CommonUtil.getDeviceType();
        String versionName = CommonUtil.getVersionNameExcludeDebug();

        QueryLauncherRequest request = new QueryLauncherRequest();
        request.setTerminalVersion(CommonUtil.getTerminalVersion(versionName,systemModel));
        request.setUserToken(SessionService.getInstance().getSession().getUserToken());
        request.setDeviceModel("2");//2代表STB

        HttpApi.getInstance().getService().queryLauncher(HttpUtil.getVspUrl(HttpConstant.QUERYLAUNCHER), request)
                //.compose(onCompose(mView.bindToLife()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new RxCallBack<QueryLauncherResponse>(HttpConstant.QUERYLAUNCHER,context) {
                    @Override
                    public void onSuccess(QueryLauncherResponse queryLauncherResponse) {
                        String retCode = queryLauncherResponse.getResult().getRetCode();
                        if (TextUtils.equals(retCode, Result.RETCODE_OK)) {
                            if(LauncherService.getInstance().isVersionNew(queryLauncherResponse.getVersion())){
                                listener.onVersionChange(true,queryLauncherResponse.getLauncherLink());
                            } else {
                                listener.onVersionChange(false,null);
                            }
                        } else {
                            listener.onVersionChange(false,null);
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        SuperLog.error(TAG,e);
                        listener.onVersionChange(false,null);
                    }
                });
    }

    public void queryPHMLauncherList(Context context, OnQueryLauncherListener listener,boolean isMain,List<String> desktopIDs) {
        if (TextUtils.isEmpty(LauncherService.getInstance().getPhsUrl())){
            SuperLog.debug(TAG,"[refreshLauncher] mPhsURLs=null");
            listener.onVersionChange(false,null);
            return;
        }
        String systemModel = CommonUtil.getDeviceType();
        String versionName = CommonUtil.getVersionNameExcludeDebug();

        QueryPHMLauncherListRequest request = new QueryPHMLauncherListRequest();
        request.setTerminalVersion(CommonUtil.getTerminalVersion(versionName,systemModel));
        request.setUserToken(AuthenticateManager.getInstance().getLocalToken());
        request.setDeviceModel("STB");//2代表STB
        request.setUserAttribute(LauncherService.getInstance().getUserAttribute());
        if (!isMain){
            request.setDesktopIDs(desktopIDs);
        }

        LoginNetApi.getInstance().getService().queryPHMLauncherList(LauncherService.getInstance().getPhsUrl() + HttpConstant.QUERYPHMLAUNCHERLIST, request)
                //.compose(onCompose(mView.bindToLife()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new RxCallBack<QueryPHMLauncherListResponse>(HttpConstant.QUERYLAUNCHER,context) {
                    @Override
                    public void onSuccess(QueryPHMLauncherListResponse queryLauncherResponse) {
                        String retCode = queryLauncherResponse.getResult().getRetCode();
                        if (TextUtils.equals(retCode, Result.RETCODE_OK)) {
                            if(null != queryLauncherResponse.getDesktopInfos() && queryLauncherResponse.getDesktopInfos().size() > 0){
                                if (!isMain){
                                    if (LauncherService.getInstance().isChildLauncherLinkNew(queryLauncherResponse.getDesktopInfos().get(0).getLauncherLink(),desktopIDs.get(0))){
                                        listener.onVersionChange(true,queryLauncherResponse.getDesktopInfos().get(0).getLauncherLink());
                                        return;
                                    }else{
                                        listener.onVersionChange(false,queryLauncherResponse.getDesktopInfos().get(0).getLauncherLink());
                                        return;
                                    }
                                }else if (LauncherService.getInstance().isLauncherLinkNew(queryLauncherResponse.getDesktopInfos().get(0).getLauncherLink())){
                                    listener.onVersionChange(true,queryLauncherResponse.getDesktopInfos().get(0).getLauncherLink());
                                    return;
                                }
                            }
                        }
                        listener.onVersionChange(false,null);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        SuperLog.error(TAG,e);
                        listener.onVersionChange(false,null);
                    }
                });
    }

    //查询EPG top 功能键
    public void queryEpgTopFunctionMenuList(Context context,String subjectId,EpgTopFunctionMenu.OnQueryEPGFunctionListener queryEPGFunctionListener) {
        QuerySubjectVODBySubjectIDRequest request = new QuerySubjectVODBySubjectIDRequest();
        if (!TextUtils.isEmpty(subjectId)) {
            SuperLog.info2SDDebug(TAG,"查询功能键栏目id=="+subjectId);
            request.setSubjectID(subjectId);
        }else{
            SuperLog.info2SDDebug(TAG,"查询功能键栏目id=null,未在PHM扩展参数中配置");
            return;
        }
        request.setVODCount("0");
        request.setOffset("0");
        request.setSubjectCount("50");
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUERYSUBJECTVODBYSUBJECTID;
        HttpApi.getInstance().getService().querySubjectVODBySubjectID(url, request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new RxCallBack<QuerySubjectVODBySubjectIDResponse>(HttpConstant.QUERYSUBJECTVODBYSUBJECTID, context) {
            @Override
            public void onError(Throwable e) {
                SuperLog.error(TAG, "queryEpgTopFunctionMenuList onError and subjectId="+subjectId);
                queryEPGFunctionListener.getEPGFunctionDataFail();
                super.onError(e);
            }

            @Override
            public void onSuccess(@NonNull QuerySubjectVODBySubjectIDResponse response) {
                defaultTimes = 0;
                SuperLog.info2SD(TAG, "queryEpgTopFunctionMenuList successfully, and subjectId="+subjectId);
                if (queryEPGFunctionListener != null && null != response.getSubjectVODLists()) {
                    queryEPGFunctionListener.getEPGFunctionData(response.getSubjectVODLists());
                } else {
                    SuperLog.error(TAG, "queryEpgTopFunctionMenuList onError and subjectId="+subjectId);
                    queryEPGFunctionListener.getEPGFunctionDataFail();
                }
            }

            @Override
            public void onFail(@NonNull Throwable e) {
                SuperLog.error(TAG, "queryEpgTopFunctionMenuList failed, and subjectId="+subjectId + "  exception: " + e);
                queryEPGFunctionListener.getEPGFunctionDataFail();
            }
        });
    }

    public interface OnQueryLauncherListener{
        void onVersionChange(boolean change,String launcherName);
    }

    /**
     * 获取混合推荐接口
     * http://aikanvod.miguvideo.com:8858/pvideo/p/stb_remixRecommend.jsp?offset=0&count=6&appointedIds=recom001&contentIds=ID1,ID2,ID3,ID4,ID5&vt=9
     */
    public void queryPBSRemixRecommend(List<String> contentIds,EpgTopFunctionMenu.OnPBSRemixRecommendListener onPBSRemixRecommendListener,String appointedId,String count) {

        StringBuffer sb = new StringBuffer(HttpConstant.PBS_RemixRecommend_URL).append("?");
        sb.append("offset=").append("0");
        sb.append("&count=").append(count);
        sb.append("&appointedIds=").append(appointedId);
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
                .header("authorization",SessionService.getInstance().getSession().getUserToken())
                .header("EpgSession", jsessionid)
                .header("Location", ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL())
                .url(sb.toString())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SuperLog.error(TAG,"stb_remixRecommend fail,queryPBSRemixRecommend");
                SuperLog.error(TAG, e);
                onPBSRemixRecommendListener.getRemixRecommendDataFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                SuperLog.debug(TAG,"stb_remixRecommend success,queryPBSRemixRecommend");
                String body = response.body().string();
                SuperLog.info2SDDebug(TAG,"stb_remixRecommend responseData = " + body);
                PBSRemixRecommendResponse pbsRemixRecommendResponse = JsonParse.json2Object(body, PBSRemixRecommendResponse.class);
                //PBSRemixRecommendResponse
                onPBSRemixRecommendListener.getRemixRecommendData(pbsRemixRecommendResponse);
            }
        });
    }


    //给详情页和订购过渡页调用
    public void queryPBSRemixRecommend(List<String> contentIds,String vodId, String productId, EpgTopFunctionMenu.OnPBSRemixRecommendListener onPBSRemixRecommendListener,String appointedId,String count) {

        StringBuffer sb = new StringBuffer(HttpConstant.PBS_RemixRecommend_URL).append("?");
        sb.append("offset=").append("0");
        sb.append("&count=").append(count);
        if (!TextUtils.isEmpty(vodId)){
            sb.append("&vodId=").append(vodId);
        }
        if (!TextUtils.isEmpty(productId)){
            sb.append("&productId=").append(productId);
        }
        sb.append("&appointedIds=").append(appointedId);
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
                .header("authorization",SessionService.getInstance().getSession().getUserToken())
                .header("EpgSession", jsessionid)
                .header("Location", ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL())
                .url(sb.toString())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SuperLog.error(TAG,"stb_remixRecommend fail,queryPBSRemixRecommend");
                SuperLog.error(TAG, e);
                onPBSRemixRecommendListener.getRemixRecommendDataFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                SuperLog.debug(TAG,"stb_remixRecommend success,queryPBSRemixRecommend");
                String body = response.body().string();
                SuperLog.debug(TAG,"stb_remixRecommend responseData = " + body);
                PBSRemixRecommendResponse pbsRemixRecommendResponse = JsonParse.json2Object(body, PBSRemixRecommendResponse.class);
                //PBSRemixRecommendResponse
                onPBSRemixRecommendListener.getRemixRecommendData(pbsRemixRecommendResponse);
            }
        });
    }

}
