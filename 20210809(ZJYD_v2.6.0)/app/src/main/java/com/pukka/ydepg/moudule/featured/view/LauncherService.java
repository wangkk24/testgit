package com.pukka.ydepg.moudule.featured.view;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.pukka.ydepg.BuildConfig;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6node.Launcher;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Navigate;
import com.pukka.ydepg.common.http.v6bean.v6node.Page;
import com.pukka.ydepg.common.http.v6bean.v6node.ResStrategyData;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.StrategyData;
import com.pukka.ydepg.common.http.v6bean.v6request.BatchGetResStrategyDataRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.GetLatestResourcesRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryLauncherRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.BatchGetResStrategyDataResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.GetLatestResourcesResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.LoginPHMRouteResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryPHMLauncherListRequest;
import com.pukka.ydepg.common.profile.adapter.ConvertDataFromPbsToEpg;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaService;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Desktop;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.DeviceInfo;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.OTTFormat;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.fileutil.FileUtil;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.event.ShowRefreshNotifyEvent;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.GroupElement;
import com.pukka.ydepg.launcher.http.LoginNetApi;
import com.pukka.ydepg.launcher.mvp.contact.LauncherContact;
import com.pukka.ydepg.launcher.mvp.presenter.ChildLauncherPresenter;
import com.pukka.ydepg.launcher.mvp.presenter.TabItemPresenter;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.ComparatorIndex;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.launcher.util.SwitchProfileEvent;
import com.pukka.ydepg.moudule.mytv.utils.MessageDataHolder;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


/**
 * ??????launcher?????????????????????
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.moudule.featured.view.LauncherService.java
 * @date: 2017-12-15 10:10
 * @version: V1.0 ????????????????????????
 */
public class LauncherService {
    private static final String TAG = LauncherService.class.getSimpleName();
    private static final String FILE_NAME  = "launcher.json";
    private static final String USE_PHS_SERVER = "1";//??????????????????????????????????????????????????????PHS?????????
    private static String SAVED_DIR;
    private static String SAVED_FILE;
    private static volatile LauncherService sInstance;
    private Launcher mLauncher;
    private Map<String, Launcher> childLauncherMap = new HashMap<>();//????????????launcher??????
    private Map<String, ArrayList<List<GroupElement>>> childGroupElements = new HashMap<>();
    private List<GroupElement> simpleEpgData = new ArrayList<>();
    private List<GroupElement> childrenEpgData = new ArrayList<>();
    /**
     * ????????????id??????????????????????????????
     */
    private Map<String, String> additionElementMap = new HashMap<>();
    //????????????????????????app?????????????????????app?????????????????????
    private Map<String, Element> navApkExtraDataMap = new HashMap<>();
    private int mFirstIndex = 0;//???????????????????????????
    private int mMineIndex = -1;
    private int mSimpleEpgIndex = -1;
    private int mChildrenEpgIndex = -1;
    private int m4KEpgIndex = -1;
    private int mInterval = 900;  //????????????????????????
    private ArrayList<List<GroupElement>> mGroupElements = new ArrayList<>();
    private String mCurrentVersion;
    private String mCurrentResourceVersion = "0000000000000";//VSP?????????????????????????????????????????????????????????????????????
    private Navigate navSimpleEpg;
    private Navigate navChildrenEpg;
    private Map<String, StrategyData> mapResourceData = new HashMap<>();
    /**
     * ?????????????????????
     */
    private RxCallBack<String> mCheckObserver;
    private RxCallBack<String> mRefreshObserver;
    private RxCallBack<String> stringObservable;

    /**
     * ??????????????????????????????????????????????????????
     * launcherLink????????????????????????????????????launcher.jason???????????????
     * */
    private String launcherLink = "";
    private String launcherJsonLink = "";

    /*???????????????true:??????false??????
    * ??????????????????
    * 1?????????????????????????????????????????????????????????
    * 2???????????????????????????????????????????????????????????????????????????????????????version??????????????????????????????launcher??????????????????????????????fragment???loaddata??????????????????fragment??????????????????
    * */
    private boolean mIsOpenRefresh = true;

    public Navigate getNavigateChild() {
        return navigateChild;
    }

    public Navigate getNavigateSimple() {
        return navigateSimple;
    }

    private Navigate navigateChild  = null;
    private Navigate navigateSimple = null;

    private TabItemPresenter presenter = new TabItemPresenter();

    static {
        SAVED_DIR = OTTApplication.getCachePath();
        SAVED_FILE = SAVED_DIR + FILE_NAME;
    }

    public int getMineIndex() {
        return mMineIndex;
    }

    public static LauncherService getInstance() {
        if (sInstance == null) {
            synchronized (LauncherService.class) {
                if (sInstance == null) {
                    sInstance = new LauncherService();
                }
            }
        }
        return sInstance;
    }

    public Map<String, String> getAdditionElementMap() {
        return additionElementMap;
    }
    public Map<String, Element> getnavApkExtraDataMap() {
        return navApkExtraDataMap;
    }

    public int getFirstIndex() {
        int firstIndex = mFirstIndex;
        if(SharedPreferenceUtil.getInstance().getIsSimpleEpg()){
            firstIndex = UBDConstant.NavPosition.SIMPLE;
        }
        if(SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
            firstIndex = UBDConstant.NavPosition.CHILD;
        }
        SuperLog.debug(TAG, "mFirstIndex:" + mFirstIndex);
        return firstIndex;
    }
    public int getFirstIndexForNormal() {
        return mFirstIndex;
    }
    public int getSimpleEpgIndex() {
        return mSimpleEpgIndex;
    }

    public void setSimpleEpgIndex(int index) {
        mSimpleEpgIndex = index;
    }

    public void setNavSimpleEpg(Navigate mine) {
        this.navSimpleEpg = mine;
    }

    public void setNavChildrenEpg(Navigate mine) {
        this.navChildrenEpg = mine;
    }

    private LauncherService() { }

    public void queryLauncherLogin(LauncherContact.ILauncherPresenter callback,Context context){
        // 1 ??????????????????PHS???????????????LoginPHSRoute
        // 0 ??????????????? ???????????????PHS???????????????????????????PHM??????
        String launcherServer = CommonUtil.getConfigValue(Constant.QUERY_PHM_LAUNCHER_LIST);
        //TODO ????????????PHS?????????
        //launcherServer = USE_PHS_SERVER;
        SuperLog.info(TAG,"Launcher server is (1:PHS 0/other:PHM) = " + launcherServer);
        if (USE_PHS_SERVER.equals(launcherServer)){
            LauncherService.getInstance().loginPHMRoute(callback, context);
        } else {
            LauncherService.getInstance().firstCheckUpdate(callback, context);
        }
    }

    //??????launcher?????????????????????
    private String getExtraValue(String key) {
        Map<String, String> extraData = null;
        if (null != getLauncher()){
            extraData = getLauncher().getExtraData();
        }
        if (null != extraData) {
            return extraData.get(key);
        } else {
            return null;
        }
    }

    public String getNavIdMine() {
        return getExtraValue(Constant.NAV_MINE);
    }

    private String getNavIdFocused() {
        return getExtraValue(Constant.NAV_FOCUS);
    }

    public String getSimpleEpgBg() {
        if (null != navSimpleEpg && null != navSimpleEpg.getPageList() && navSimpleEpg.getPageList().size() > 0) {
            return navSimpleEpg.getPageList().get(0).getBackground();
        } else {
            return "";
        }
    }

    public String getChildrenEpgBg() {
        if (null != navChildrenEpg && null != navChildrenEpg.getPageList() && navChildrenEpg.getPageList().size() > 0) {
            return navChildrenEpg.getPageList().get(0).getBackground();
        } else {
            return "";
        }
    }

    public String getNavIdSimpleEpg() {
        return getExtraValue(Constant.NAV_SIMPLE_EPG);
    }

    public String getNavIdChildrenEpg() {
        return getExtraValue(Constant.NAV_CHILDREN_EPG);
    }

    public String getNavId4K() {
        return getExtraValue(Constant.NAV_4K_EPG);
    }

    private boolean isFileExist() {
        File file = new File(SAVED_FILE);
        return file.exists();
    }

    /**
     * get the content of launcher dynamic resource
     * from the json file every time.
     *
     * @return
     */
    public String getFileContent() {
        File file = new File(SAVED_FILE);
        String fileContent = "";
        if (file.exists()) {
            fileContent = FileUtil.readFileByLines(SAVED_FILE);
            SuperLog.debug(TAG, "load local json file address=" + FileUtil.getCanonicalPath(file));
        } else {
            InputStream inputStream = null;
            try {
                SuperLog.debug(TAG, "load local json file address=" + FILE_NAME);
                inputStream = OTTApplication.getContext().getAssets().open(FILE_NAME);
                fileContent = FileUtil.getContent(inputStream);
            } catch (IOException e) {
                SuperLog.error(TAG, e);
            } finally {
                FileUtil.closeInputStream(inputStream);
            }
        }
        return fileContent;
    }

    //????????????
    public void parseLauncher() {
        Launcher launcher = JsonParse.json2Object(getFileContent(), Launcher.class);
        setLauncher(launcher);
    }

    private void buildAdditionMap() {
        //??????????????????
        if (null == mLauncher || null == mLauncher.getAdditionElement())return;

        for (Element element : mLauncher.getAdditionElement()) {
            if ("1".equals(element.getType()) && !CollectionUtil.isEmpty(element
                    .getElementDataList())) {
                if (!TextUtils.isEmpty(element.getElementDataList().get
                        (0).getContentURL())){
                    additionElementMap.put(element.getId(), element.getElementDataList().get(0)
                            .getContentURL());
                }else{
                    additionElementMap.put(element.getId(), "");
                }
            }
            if (!TextUtils.isEmpty(element.getId())){
                navApkExtraDataMap.put(element.getId(),element);
            }
        }
    }

    public void setLauncher(Launcher launcher) {
        this.mLauncher = launcher;
        buildAdditionMap();//????????????id?????????????????????
    }

    public void addChildLauncher(String pageId, Launcher launcher) {
        childLauncherMap.put(pageId, launcher);
    }

    /**
     * ??????????????????id??????????????????launcher
     *
     * @param pageId
     * @return
     */
    public Launcher getChildLauncher(String pageId) {
        if (childLauncherMap.containsKey(pageId)) {
            return childLauncherMap.get(pageId);
        } else {
            return null;
        }
    }

    public void addChildGroupElements(String pageId, ArrayList<List<GroupElement>> groupElements) {
        childGroupElements.put(pageId, groupElements);
    }

    /**
     * ??????????????????groupElement??????
     * @param pageId
     * @return
     */
    public ArrayList<List<GroupElement>> getChildGroupElement(String pageId) {
        if (childGroupElements.containsKey(pageId)) {
            return childGroupElements.get(pageId);
        } else {
            return null;
        }
    }

    public void setGroupElements(ArrayList<List<GroupElement>> groupElements) {
        this.mGroupElements = groupElements;
    }

    private void addPbsEpgData(){
        SuperLog.info2SD(TAG,"after parseGroupElements and to addPbsEpgData");
        if (null != ConvertDataFromPbsToEpg.getNavigate() && null != ConvertDataFromPbsToEpg.getGroupElementList()){
            getLauncher().getNavigateList().add(0,ConvertDataFromPbsToEpg.getNavigate());
            mGroupElements.add(mFirstIndex+1,ConvertDataFromPbsToEpg.getGroupElementList());
            ConvertDataFromPbsToEpg.setIsHadAddPbsEpg(true);
            updateFirstIndex();
            SuperLog.info2SD(TAG,"addPbsEpgData success");
        }
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public Launcher getLauncher() {
        if (mLauncher == null) {
            parseLauncher();
        }
        return mLauncher;
    }

    /**
     * ??????group
     *
     * @return
     */
    public ArrayList<List<GroupElement>> getGroupElements() {
        return mGroupElements;
    }

    /**
     * ????????????Epg???????????????????????????
     *
     * @return
     */
    public List<GroupElement> getSimpleEpgData() {
        return simpleEpgData;
    }
    public List<GroupElement> getChildrenEpgData() {
        return childrenEpgData;
    }

    /**
     * ??????????????????group???element??????
     */
    public void parseGroupElements() {
        //????????????????????????
        mMineIndex = -1;
        mFirstIndex = 0;
        Navigate simpleNav = null;
        Navigate childrenNav = null;
        Navigate fourKNav = null;
        childrenEpgData = new ArrayList<>();
        ArrayList<List<GroupElement>> tempGroupElements = new ArrayList<>();
        Launcher launcher = getLauncher();
        if (launcher != null) {
            List<Navigate> navigates = launcher.getNavigateList();
            int navCount = navigates.size();
            for (int i = 0; i < navCount; i++) {
                if (TextUtils.equals(getNavIdMine(), navigates.get(i).getId())) {
                    mMineIndex = i;
                }
                if (TextUtils.equals(getNavIdSimpleEpg(), navigates.get(i).getId())) {
                    mSimpleEpgIndex = i;
                    simpleNav = navigates.get(i);
                    navigateSimple = simpleNav;
                }
                if (TextUtils.equals(getNavIdChildrenEpg(), navigates.get(i).getId())) {
                    mChildrenEpgIndex = i;
                    childrenNav = navigates.get(i);
                    navigateChild = childrenNav;
                }
                if (TextUtils.equals(getNavId4K(), navigates.get(i).getId())) {
                    m4KEpgIndex = i;
                    fourKNav = navigates.get(i);
                }
                /*if (TextUtils.equals(getNavIdFocused(), navigates.get(i).getId())) {
                    mFirstIndex = i;
                }*/
                List<Page> pageList = launcher.getNavigateList().get(i).getPageList();
                if (CollectionUtil.isEmpty(pageList)) {
                    tempGroupElements.add(new ArrayList<GroupElement>());
                    continue;
                }
                List<Element> elements = pageList.get(0).getElementList();
                List<Group> groups = launcher.getGroupList();
                if (CollectionUtil.isEmpty(elements) || CollectionUtil.isEmpty(groups)) {
                    tempGroupElements.add(new ArrayList<GroupElement>());
                    continue;
                }
                List<GroupElement> groupElementList = getGroupElements(elements, groups,navigates.get(i).getPageList().get(0).getId());
                tempGroupElements.add(groupElementList);
            }
            SuperLog.info2SD(TAG, "Focused navId :" + getNavIdFocused() + ", Mine navId:" +
                    getNavIdMine() + ", Mine navIndex:" + mMineIndex + ", First focused navIndex:" + mFirstIndex);
            setGroupElements(tempGroupElements);

            //??????GroupElement?????????????????????epg????????????EPG??????????????????
            if (-1 != mSimpleEpgIndex && null != simpleNav && mGroupElements.size() > mSimpleEpgIndex) {
                simpleEpgData = mGroupElements.remove(mSimpleEpgIndex);
            }
            if (-1 != mChildrenEpgIndex && null != childrenNav && mGroupElements.size() > mChildrenEpgIndex) {
                childrenEpgData = mGroupElements.remove(mChildrenEpgIndex);
            }
            //4K?????? ???????????????????????????4K??????????????????4K????????????
            if (!VodUtil.isDeviceSupport4K() && -1 != m4KEpgIndex && null != simpleNav && mGroupElements.size() > m4KEpgIndex) {
                mGroupElements.remove(m4KEpgIndex);
            }

            //for???????????????navigateList?????????????????????????????????4K??????
            if (navigates.size() > 0){
                //??????Tab??????????????????epg
                if (null != simpleNav){
                    navigates.remove(simpleNav);
                    setNavSimpleEpg(simpleNav);
                }
                //??????Tab?????????????????????epg
                if (null != childrenNav){
                    navigates.remove(childrenNav);
                    setNavChildrenEpg(childrenNav);
                }
                //???????????????4K?????????Tab????????????4K????????????
                if (!VodUtil.isDeviceSupport4K() && null != fourKNav){
                    navigates.remove(fourKNav);
                }
                for (int i= 0;i < navigates.size();i++){
                    if (TextUtils.equals(getNavIdFocused(), navigates.get(i).getId())) {
                        mFirstIndex = i;
                    }
                }
            }

            addPbsEpgData();
        }
    }

    //?????????????????????
    public void updateFirstIndex(){
        for (int i = 0; i < getLauncher().getNavigateList().size(); i++) {
            if (TextUtils.equals(getNavIdFocused(), getLauncher().getNavigateList().get(i).getId())) {
                mFirstIndex = i;
            }
        }
    }

    /**
     * ????????????GroupElements
     */
    private List<GroupElement> getGroupElements(List<Element> elements, List<Group> groups,String pageId) {
        List<GroupElement> groupElements = new ArrayList<>();
        int count = groups.size();
        //????????????????????????groupid??????
        List<String> groupidList = new ArrayList<>();
        int elementSize = elements.size();
        for (int i = 0; i < elementSize; i++) {
            String groupid = elements.get(i).getGroupID();
            if (!groupidList.contains(groupid)) {
                groupidList.add(groupid);
            }
        }
        //????????????group ??????????????????groupElement??????
        for (int j = 0; j < count; j++) {
            String groupid = groups.get(j).getId();
            if (groupidList.contains(groupid) && pageId.equalsIgnoreCase(groups.get(j).getControlInfo().getPageId())) {
                GroupElement groupElement = new GroupElement();
                //String controlInfo = groups.get(j).getControlInfo().getControlId();
                //????????????????????????????????????
                //if (!TextUtils.isEmpty(controlInfo)) {// && TemplateFactory.getsResourceIdMap().containsKey(controlInfo)
                groupElement.setGroup(groups.get(j));
                List<Element> elementsListTemp = new ArrayList<>();
                for (int i = 0; i < elementSize; i++) {
                    if (elements.get(i).getGroupID().equals(groups.get(j).getId())) {
                        elementsListTemp.add(elements.get(i));
                    }
                }
                Collections.sort(elementsListTemp, new ComparatorIndex());
                groupElement.setElement(elementsListTemp);
                groupElements.add(groupElement);
                //}

            }
        }
        return groupElements;
    }

    /*public void setQueryLauncherType(int keyCode){
        if (keyCode == KeyEvent.KEYCODE_1){
            queryPhmLauncher = "1";
            SharedPreferenceUtil.getInstance().saveLauncherLink("-1");
            SharedPreferenceUtil.getInstance().saveLauncherLinkJson("-1");
        }else if (keyCode == KeyEvent.KEYCODE_0){
            queryPhmLauncher = "0";
            SharedPreferenceUtil.getInstance().saveLauncherLink("-1");
            SharedPreferenceUtil.getInstance().saveLauncherLinkJson("-1");
        }
    }*/

    public String getPhsUrl(){
        return mPhsURLs;
    }
    public String getQueryPhmLauncher(){
        return queryPhmLauncher;
    }
    public List<NamedParameter> getUserAttribute(){
        return mUserAttribute;
    }

    private String queryPhmLauncher = "";
    private String mPhsHttpsURLs;
    private String mPhsURLs;
    private List<NamedParameter> mUserAttribute;
    private synchronized Observable<String> queryRemoteLauncher(LauncherContact.ILauncherPresenter launcherPresenter, Context context,boolean isSwitchProfile) {
        queryPhmLauncher = CommonUtil.getConfigValue(Constant.QUERY_PHM_LAUNCHER_LIST);
        /**
         * 1 ??????????????? ????????????PHS???????????????QueryPHMLauncherList
         * 0   ???????????????PHS?????????????????????????????????
         * */
        if (!TextUtils.isEmpty(queryPhmLauncher) && queryPhmLauncher.equalsIgnoreCase("1")){
            SuperLog.debug(TAG,"[queryRemoteLauncher] use phs queryPhmLauncher="+queryPhmLauncher+";mPhsURLs="+mPhsURLs);
            //??????PHS???????????????????????????querylauncher?????????????????????LoginPHMRoute???????????????null??????????????????json?????????
            if(!TextUtils.isEmpty(mPhsURLs)){
                return queryPHMLauncherList(launcherPresenter,context);
            }else{
                queryLauncherFail(launcherPresenter,context,isSwitchProfile);
                return null;
            }
        }else if (TextUtils.isEmpty(queryPhmLauncher) || queryPhmLauncher.equalsIgnoreCase("0")){
            SuperLog.debug(TAG,"[queryRemoteLauncher] use queryLauncher="+queryPhmLauncher);
            return queryLauncher(launcherPresenter,context);
        }
        queryLauncherFail(launcherPresenter,context,isSwitchProfile);
        return null;
    }

    private void loginPHMRoute(LauncherContact.ILauncherPresenter launcherPresenter, Context context){

        RxCallBack<LoginPHMRouteResponse> callBack = new RxCallBack<LoginPHMRouteResponse>(HttpConstant.LOGIN_PHS_ROUTE,context) {
            @Override
            public void onSuccess(LoginPHMRouteResponse loginPHMRouteResponse) {
                String retCode = loginPHMRouteResponse.getResult().getRetCode();
                SuperLog.info(TAG,"loginPHMRoute onSuccess and retCode="+loginPHMRouteResponse.getResult().getRetCode());
                if (TextUtils.equals(retCode, Result.RETCODE_OK)
                        && null != loginPHMRouteResponse.getPhsHttpsURLs() && loginPHMRouteResponse.getPhsHttpsURLs().size() > 0
                        && null != loginPHMRouteResponse.getPhsURLs() && loginPHMRouteResponse.getPhsURLs().size() > 0) {
                    mPhsHttpsURLs = loginPHMRouteResponse.getPhsHttpsURLs().get(0);
                    mPhsURLs = loginPHMRouteResponse.getPhsURLs().get(0);
                    mUserAttribute = loginPHMRouteResponse.getUserAttribute();
                    //????????????
                    //??????????????????
                }
                SuperLog.info(TAG,"loginPHMRoute onSuccess and mPhsHttpsURLs="+mPhsHttpsURLs +";mPhsURLs = "+mPhsURLs);
                firstCheckUpdate(launcherPresenter,context);
            }

            @Override
            public void onFail(Throwable e) {
                SuperLog.error(TAG,e);
                SuperLog.info2SD(TAG,"Login PHS route failed, do the original logic to get Desktop file(launcher.json).");
                firstCheckUpdate(launcherPresenter,context);
            }
        };

        HttpApi.getInstance().getService().loginPHMRoute(HttpUtil.getVspUrl(HttpConstant.LOGIN_PHS_ROUTE))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }

    private synchronized Observable<String> queryPHMLauncherList(LauncherContact.ILauncherPresenter launcherPresenter, Context context){
        SimpleDateFormat mFormatTime = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String systemModel = DeviceInfo.getSystemInfo(Constant.DEVICE_RAW);
        String versionName = "";
        if (TextUtils.isEmpty(systemModel) || "unknown".equals(systemModel)) {
            systemModel = Build.MODEL;
        }
        try {
            String version = CommonUtil.getVersionName();
            if(BuildConfig.DEBUG && !TextUtils.isEmpty(version) && version.contains(" ")){
                versionName = version.substring(0,version.indexOf(" "));
            } else{
                versionName = version;
            }
        } catch (Exception e) {
            SuperLog.error(TAG,e);
        }
        QueryPHMLauncherListRequest request = new QueryPHMLauncherListRequest();
        request.setUserAttribute(mUserAttribute);
        request.setDeviceModel("STB");
        request.setUserToken(AuthenticateManager.getInstance().getLocalToken());
        if (!TextUtils.isEmpty(versionName) && !TextUtils.isEmpty(systemModel)){
            request.setTerminalVersion(versionName+"_"+systemModel);
        }else{
            request.setTerminalVersion("-1");
        }
        /*String json = "{\"result\":{\"retCode\":\"000000000\",\"retMsg\":\"success\"},\"interval\":900,\"desktopInfos\":[{\"launcherLink\":\"/video/PHS/desktop/100174/5bf446dc0b4169bf69f152baf6fec0a6145846ea8445d22e18b9b8f0fb3e23e9.json\",\"desktopID\":\"100174\"}]}";
        QueryPHMLauncherListResponse responseTem = JsonParse.json2Object(json,QueryPHMLauncherListResponse.class);*/
        return LoginNetApi.getInstance().getService().queryPHMLauncherList(mPhsURLs + HttpConstant.QUERYPHMLAUNCHERLIST, request).filter(queryLauncherResponse -> {
            String retCode = queryLauncherResponse.getResult().getRetCode();
            if (TextUtils.equals(retCode, Result.RETCODE_OK)) {
                SuperLog.info2SD(TAG,"[Login-10]Get queryPHMLauncherList response. Details is as followed:>>>"+queryLauncherResponse.toString()
                        +"\n\t--------------------------------------------------------------"
                        +"\n\t(local)Version                 =" + SharedPreferenceUtil.getInstance().getLauncherVersion()
                        +"\n\t(local)CurrentResourcesVersion =" + mCurrentResourceVersion
                        +"\n\t(local)LauncherLink            =" + SharedPreferenceUtil.getInstance().getLauncherLink());
                return true;
            } else {
                SuperLog.info2SD(TAG,"queryPHMLauncherList fail,retCode = " + retCode + ", and load local json");
                if (launcherPresenter != null) {
                    launcherPresenter.loadLauncher(context);
                }
                //??????????????????????????????token???????????????
                if ("157031001".equals(retCode)) {
                    SuperLog.error(TAG, "queryPHMLauncherList response 157031001, so getTokenTotal() again");
                    AuthenticateManager.getInstance().getTokenTotal();
                }
                checkAndUpdate(context,-1);
                return false;
            }
        }).filter(response -> { //???????????????????????????????????????????????????????????????????????????
            String desktopId       = "";
            /*String resourceVersion = "";
            String getResourceUrl  = "";
            String batchUrl        = "";*/
            if (null != response && null != response.getDesktopInfos() && response.getDesktopInfos().size() > 0){
                desktopId = response.getDesktopInfos().get(0).getDesktopID();
                launcherLink = response.getDesktopInfos().get(0).getLauncherLink();
                SuperLog.debug(TAG,"launcherLink="+launcherLink);
            }
            launcherJsonLink = removeLauncherLinkNodeId(launcherLink);
            //?????????????????????????????????????????????????????????????????????queryLauncher?????????????????????????????????????????????
            //???queryLauncher?????????????????????????????????????????????????????????
            /*if (!TextUtils.isEmpty(getResourceUrl) && !TextUtils.isEmpty(batchUrl)
                    && OTTFormat.convertLong(resourceVersion) != OTTFormat.convertLong(mCurrentResourceVersion)) {
                SuperLog.debug(TAG,"getLatestResources????????????   "+"NewResourceVersion="+resourceVersion+";OldResourceVersion="+"mCurrentResourceVersion"+";desktopId="+desktopId);
                getLatestResources(getResourceUrl, batchUrl, desktopId, mCurrentResourceVersion, context);
                mCurrentResourceVersion = resourceVersion;
            }*/
            //Session???????????????1???????????????????????????2????????????????????????3????????????????????????????????????
            String refreshLauncherTime = SessionService.getInstance().getSession().getTerminalConfigurationValue("refresh_launcher_time");
            if (TextUtils.isEmpty(refreshLauncherTime)){
                refreshLauncherTime="30";   //Default 30 min
            }else if (refreshLauncherTime.equals("-1")){
                mIsOpenRefresh = false;
            }

            //??????Launcher.json?????????1:launcher.json??????????????? 2:?????????launcherlink?????????????????????
            boolean needUpdate = !isFileExist()//|| null == launcher
                    || TextUtils.isEmpty(SharedPreferenceUtil.getInstance().getLauncherLinkJson())
                    || !TextUtils.equals(launcherJsonLink,SharedPreferenceUtil.getInstance().getLauncherLinkJson());

            SuperLog.info2SD(TAG, "[LOGIN-11]LauncherNeedUpdate = [" + needUpdate + "] Details is as followed:>>>"
                    + "\n\tLocal launcher file exist              = " + isFileExist()
                    + "\n\tLocal LauncherLinkJson is          = " + SharedPreferenceUtil.getInstance().getLauncherLinkJson()
                    + "\n\tlauncherJsonLink = " + launcherJsonLink);

            if (needUpdate) {
                SharedPreferenceUtil.getInstance().setRefreshLauncherTime(mFormatTime.format(new Date()));
                mCurrentVersion = desktopId;
                mCurrentResourceVersion = desktopId;
                removeAllChildLauncher();
                //launcherLink = response.getDesktopInfos().get(0).getLauncherLink();
                if (!TextUtils.isEmpty(launcherLink)) {
                    SharedPreferenceUtil.getInstance().saveLauncherLinkJson(launcherJsonLink);
                    if (launcherJsonLink.toLowerCase(Locale.getDefault()).endsWith(".json")) {
                        String launcherLink = launcherJsonLink.substring(0, launcherJsonLink.lastIndexOf("/"));
                        if (!TextUtils.isEmpty(launcherLink)) {
                            //??????link?????????
                            //AppAplication.getApplication().setLauncherLink(tempLauncherUrl);
                            SuperLog.info2SD(TAG, "[LauncherNewLink]=" + launcherLink + "\t[oldLauncherLink]=" + SharedPreferenceUtil.getInstance().getLauncherLink());
                            SharedPreferenceUtil.getInstance().saveLauncherNewLink(launcherLink);
                            SharedPreferenceUtil.getInstance().saveLauncherLink(launcherLink);
                        }
                    }
                    synchronized (this){
                        mInterval = response.getInterval();
                    }
                    return true;//?????????launcher.json
                }
            } else {
                SuperLog.debug(TAG,"???????????????json");
                mCurrentVersion = null; //?????????launcher.json,????????????
            }

            if (launcherPresenter != null) {
                launcherPresenter.loadLauncher(context);
            }
            checkAndUpdate(context,-1);
            return false;  //?????????launcher.json,????????????
        }).flatMap(queryLauncherResponse -> {  //??????launcher.json
            String launcherLinkTem = "";
            if (null != queryLauncherResponse.getDesktopInfos()
                    && queryLauncherResponse.getDesktopInfos().size() > 0){
                launcherLinkTem = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + queryLauncherResponse.getDesktopInfos().get(0).getLauncherLink();
            }
            SuperLog.info2SD(TAG, "[LOGIN-option1(After step11)]Start download launcher.json. URL=" + launcherLinkTem);
            return downLoadLauncher(launcherLinkTem, launcherPresenter, context);
        });
    }

    private synchronized Observable<String> queryLauncher(LauncherContact.ILauncherPresenter launcherPresenter, Context context){
        SimpleDateFormat mFormatTime = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String systemModel = DeviceInfo.getSystemInfo(Constant.DEVICE_RAW);
        String versionName = "";
        if (TextUtils.isEmpty(systemModel) || "unknown".equals(systemModel)) {
            systemModel = Build.MODEL;
        }
        try {
            String version = context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
            if(BuildConfig.DEBUG){
                versionName = version.substring(0,version.indexOf(" "));
            } else{
                versionName = version;
            }
        } catch (Exception e) {
            SuperLog.error(TAG,e);
        }

        QueryLauncherRequest request = new QueryLauncherRequest();
        request.setDeviceModel("2");
        request.setTerminalVersion(CommonUtil.getTerminalVersion(versionName,systemModel));
        request.setUserToken(SessionService.getInstance().getSession().getUserToken());
        return LoginNetApi.getInstance().getService().queryLuancher(HttpUtil.getVspUrl(HttpConstant.QUERYLAUNCHER), request).filter(queryLauncherResponse -> {
            String retCode = queryLauncherResponse.getResult().getRetCode();
            if (TextUtils.equals(retCode, Result.RETCODE_OK)) {
                SuperLog.info2SD(TAG,"[Login-10]Get QueryLauncher response. Details is as followed:>>>"+queryLauncherResponse.toString()
                        +"\n\t--------------------------------------------------------------"
                        +"\n\t(local)Version                 =" + SharedPreferenceUtil.getInstance().getLauncherVersion()
                        +"\n\t(local)CurrentResourcesVersion =" + mCurrentResourceVersion
                        +"\n\t(local)LauncherLink            =" + SharedPreferenceUtil.getInstance().getLauncherLink());
                return true;
            } else {
                SuperLog.info2SD(TAG,"queryLauncher fail,retCode = " + retCode + ", and load local json");
                if (launcherPresenter != null) {
                    launcherPresenter.loadLauncher(context);
                }
                //??????????????????????????????token???????????????
                if ("157031001".equals(retCode)) {
                    SuperLog.error(TAG, "QueryLauncher response 157031001, so getTokenTotal() again");
                    AuthenticateManager.getInstance().getTokenTotal();
                }
                checkAndUpdate(context,-1);
                return false;
            }
        }).filter(response -> { //???????????????????????????????????????????????????????????????????????????
            String desktopId       = response.getDesktopID();
            String resourceVersion = response.getCurrentResourcesVersion();
            String getResourceUrl  = response.getGetLatestResourcesURL();
            String batchUrl        = response.getBatchGetResStrategyDataURL();
            SuperLog.info2SDDebug(TAG,"desktopId="+desktopId+";resourceVersion="+resourceVersion+";getResourceUrl="+getResourceUrl+";batchUrl="+batchUrl);

            SharedPreferenceUtil.getInstance().saveLauncherVersionForChild(response.getVersion());
            SharedPreferenceUtil.getInstance().saveLauncherDeskTopIdForChild(desktopId);

            //?????????????????????????????????????????????????????????????????????queryLauncher?????????????????????????????????????????????
            //???queryLauncher?????????????????????????????????????????????????????????
            if (!TextUtils.isEmpty(getResourceUrl) && !TextUtils.isEmpty(batchUrl)
                    && OTTFormat.convertLong(resourceVersion) != OTTFormat.convertLong(mCurrentResourceVersion)) {
                SuperLog.debug(TAG,"getLatestResources????????????   "+"NewResourceVersion="+resourceVersion+";OldResourceVersion="+"mCurrentResourceVersion"+";desktopId="+desktopId);
                getLatestResources(getResourceUrl, batchUrl, desktopId, mCurrentResourceVersion, context);
                mCurrentResourceVersion = resourceVersion;
            }
            //Session???????????????1???????????????????????????2????????????????????????3????????????????????????????????????
            String refreshLauncherTime = SessionService.getInstance().getSession().getTerminalConfigurationValue("refresh_launcher_time");
            if (TextUtils.isEmpty(refreshLauncherTime)){
                refreshLauncherTime="30";   //Default 30 min
            }else if (refreshLauncherTime.equals("-1")){
                mIsOpenRefresh = false;
            }

            //??????Launcher.json?????????1:launcher.json??????????????? 2:????????????????????? 3????????????????????????????????????????????????????????????
            boolean needUpdate = !isFileExist() //|| null == launcher
                    || !TextUtils.equals(response.getVersion(),SharedPreferenceUtil.getInstance().getLauncherVersion())
                    || ( mIsOpenRefresh && (TextUtils.isEmpty(SharedPreferenceUtil.getInstance().getRefreshLauncherTime())
                    || (new Date().getTime() - mFormatTime.parse(SharedPreferenceUtil.getInstance().getRefreshLauncherTime()).getTime())
                    > Long.parseLong(refreshLauncherTime)*60*1000));

            SuperLog.info2SD(TAG, "[LOGIN-11]LauncherNeedUpdate = [" + needUpdate + "] Details is as followed:>>>"
                    + "\n\tLocal launcher file exist              = " + isFileExist()
                    + "\n\tLast refresh launcher time is          = " + SharedPreferenceUtil.getInstance().getRefreshLauncherTime()
                    + "\n\tRefresh launcher interval is(Unit:min) = " + refreshLauncherTime);

            if (needUpdate) {
                SharedPreferenceUtil.getInstance().setRefreshLauncherTime(mFormatTime.format(new Date()));
                mCurrentVersion = response.getVersion();
                removeAllChildLauncher();
                launcherLink = response.getLauncherLink();
                if (!TextUtils.isEmpty(launcherLink)) {
                    if (launcherLink.toLowerCase(Locale.getDefault()).endsWith(".json")) {
                        launcherLink = launcherLink.substring(0, launcherLink.lastIndexOf("/"));
                        if (!TextUtils.isEmpty(launcherLink)) {
                            //??????link?????????
                            //AppAplication.getApplication().setLauncherLink(tempLauncherUrl);
                            SuperLog.info2SD(TAG, "[LauncherNewLink]=" + response.getLauncherLink() + "\t[oldLauncherLink]=" + SharedPreferenceUtil.getInstance().getLauncherLink());
                            SharedPreferenceUtil.getInstance().saveLauncherNewLink(launcherLink);
                        }
                    }
                    synchronized (this){
                        mInterval = Integer.parseInt(response.getInterval());
                    }
                    return true;//?????????launcher.json
                }
            } else {
                mCurrentVersion = null; //?????????launcher.json,????????????
            }

            if (launcherPresenter != null) {
                launcherPresenter.loadLauncher(context);
            }
            checkAndUpdate(context,-1);
            return false;  //?????????launcher.json,????????????
        }).flatMap(queryLauncherResponse -> {  //??????launcher.json
            String launcherLink = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + queryLauncherResponse.getLauncherLink();
            SuperLog.info2SD(TAG, "[LOGIN-option1(After step11)]Start download launcher.json. URL=" + launcherLink);
            return downLoadLauncher(launcherLink, launcherPresenter, context);
        });
    }

    /**
     * ??????launcher.json
     *
     * @param url
     * @return
     */
    private Observable<String> downLoadLauncher(String url, LauncherContact.ILauncherPresenter
            launcherPresenter, Context context) {
        if (null != mChildPresent && !TextUtils.isEmpty(mChildPageId)){
            mChildPresent.queryLauncher(mChildPageId,false,context,false);
        }
        Desktop.setStartDownLoadTime(System.currentTimeMillis());
        return HttpApi.getInstance().getService().downloadLauncher(url).map(new Function<ResponseBody, String>() {
            @Override
            public String apply(ResponseBody responseBody) {
                try{
                    return responseBody.source().readUtf8();
                }catch (Exception e){
                    //????????????????????????json??????,???????????????,????????????????????????
                    SuperLog.info2SD(TAG, "Read json from server failed.");
                    return "";
                }
            }
        }).filter(content -> {
            Desktop.setEndDownLoadTime(System.currentTimeMillis());
            if (!TextUtils.isEmpty(content)) {
                Desktop.setDownloadState("1");
                SuperLog.info2SD(TAG, "[LOGIN-option2]Download launcher file succeeded. Number of string=" + content.length());
                return true;
            } else {
                //?????????,????????????????????????
                Desktop.setDownloadState("0");
                SuperLog.error(TAG, "The size of downloaded launcher file is 0. Begin to load default launcher.json");
                if (launcherPresenter != null) {
                    launcherPresenter.loadLauncher(context);
                }
                checkAndUpdate(context,-1);
                return false;
            }
        }).doOnError(new Consumer<Throwable>() {
            //????????????Launcher.json???????????????404?????????,?????????,???????????????????????????
            @Override
            public void accept(Throwable throwable) {
                SuperLog.error(TAG,throwable);
                if (launcherPresenter != null) {
                    launcherPresenter.loadLauncher(context);
                }
                checkAndUpdate(context,-1);
            }
        });
    }

    public void firstCheckUpdate(LauncherContact.ILauncherPresenter launcherPresenter, Context context) {
        SuperLog.info2SD(TAG, "[Login-9]????????????launcher");
        stringObservable = new FirstCheckUpdate(launcherPresenter, context);
        Observable.timer(0, TimeUnit.SECONDS)
                .flatMap(aLong -> { return queryRemoteLauncher(launcherPresenter, context,false); })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(stringObservable);
    }

    //??????QueryLauncher?????????????????????launcher.json
    private void queryLauncherFail(LauncherContact.ILauncherPresenter launcherPresenter,Context context,boolean isSwitchProfile){
        SuperLog.debug(TAG,"[queryLauncherFail] use local json,???????????????????????????????????????????????????");
        Launcher launcher = JsonParse.json2Object(getFileContent(), Launcher.class);
        if (null != launcher) {
            setLauncher(launcher);
        }
        if (null != launcher) {
            if (null == launcherPresenter){
                SuperLog.info2SD(TAG, "load local Launcher.json,to parseGroupElements(),and to load desktop");
                parseGroupElements();
                SharedPreferenceUtil.getInstance().saveLauncherUpdate(true);
                removeAllChildLauncher();//??????????????????????????????????????????????????????
                MessageDataHolder.get().setRefreshLauncherData(false);
                SwitchProfileEvent event = new SwitchProfileEvent();
                event.setSwitchProfile(isSwitchProfile);
                EventBus.getDefault().post(event);//??????
            }else{
                SuperLog.info2SD(TAG, "load local Launcher.json,and to load desktop");
                //??????????????????
                launcherPresenter.loadLauncher(context);
            }
            checkAndUpdate(context,-1);
        } else {
            //?????????????????????-1???????????????????????????
            SuperLog.info2SD(TAG, "load local Launcher.json, but launcher is null,and again request queryLauncher");
            SharedPreferenceUtil.getInstance().saveLauncherVersion("-1");
            MessageDataHolder.get().setRefreshLauncherData(true);
            checkAndUpdate(context,0);
        }
    }

    public class FirstCheckUpdate extends RxCallBack<String>{
        private Context context;
        private LauncherContact.ILauncherPresenter launcherPresenter;

        public FirstCheckUpdate(LauncherContact.ILauncherPresenter launcherPresenter, Context context){
            super(context);
            this.context = context;
            this.launcherPresenter = launcherPresenter;
        }
        @Override
        public void onSuccess(@NonNull String content) {
            Launcher launcher = JsonParse.json2Object(content, Launcher.class);
            if (null != launcher) {
                Desktop.setAnalyseState("1");
                setLauncher(launcher);
                SuperLog.info2SD(TAG, "Launcher json file cache path : " + SAVED_FILE);
                File dir = new File(SAVED_DIR);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                //??????json??????
                FileUtil.saveContentToFile(SAVED_FILE, content);
                //?????????????????????
                if (!TextUtils.isEmpty(mCurrentVersion)) {
                    SharedPreferenceUtil.getInstance().saveLauncherVersion(mCurrentVersion);
                    // ????????????????????????launcher??????????????????????????????????????????
                    SharedPreferenceUtil.getInstance().saveLauncherUpdate(false);
                    mCurrentVersion = null;
                }
            }else{
                Desktop.setAnalyseState("0");
                //2.6 ?????????????????? ?????????????????????json
                SuperLog.info2SD(TAG, "firstCheckUpdate and launcher is null,load local json");
                queryLauncherFail(launcherPresenter, context,false);

                //??????????????????launcher??????????????????
                PbsUaService.report(Desktop.getDestopErrorData());
            }

            if (launcherPresenter != null && null != launcher) {
                SuperLog.info2SD(TAG, "firstCheckUpdate launcherPresenter and launcher is no null");
                //??????????????????
                launcherPresenter.loadLauncher(context);
                checkAndUpdate(context, -1);
            } else {
                //?????????????????????-1???????????????????????????
                SuperLog.info2SD(TAG, "firstCheckUpdate launcherPresenter is null or launcher is null???queryLauncher next time");
                checkAndUpdate(context, -1);
            }
        }

        @Override
        public void onFail(@NonNull Throwable e) {
            queryLauncherFail(launcherPresenter, context,false);
        }
    }

    public class CheckAndNeedToUpdate extends RxCallBack<String> {

        private Context context;

        public CheckAndNeedToUpdate(Context context){
            super(context);
            this.context = context;
        }

        @Override
        public void onSuccess(@NonNull String content) {
            Launcher launcher = JsonParse.json2Object(content, Launcher.class);
            if (null != launcher) {
                FileUtil.saveContentToFile(SAVED_FILE,content);
                setLauncher(launcher);
                //?????????????????????
                if (!TextUtils.isEmpty(mCurrentVersion)) {
                    SharedPreferenceUtil.getInstance().saveLauncherVersion(mCurrentVersion);
                    mCurrentVersion = null;
                }
                parseGroupElements();
                SharedPreferenceUtil.getInstance().saveLauncherUpdate(true);
                removeAllChildLauncher();//??????????????????????????????????????????????????????
                if (MessageDataHolder.get().getRefreshLauncherData()){//???????????????launcher?????????????????????
                    MessageDataHolder.get().setRefreshLauncherData(false);
                    SwitchProfileEvent event = new SwitchProfileEvent();
                    event.setSwitchProfile(false);
                    EventBus.getDefault().post(event);//??????
                }else{//???????????????launcher????????????????????????????????????????????????????????????????????????????????????????????????????????????launcher???????????????????????????
                    EventBus.getDefault().post(new ShowRefreshNotifyEvent());//??????
                }
            }else{
                //??????????????????launcher??????????????????
                PbsUaService.report(Desktop.getDestopErrorData());
            }
            SuperLog.debug(TAG, "????????????");
            checkAndUpdate(context,-1);
        }

        @Override
        public void onFail(@NonNull Throwable e) {
            SuperLog.error(TAG, e);
        }
    };
    /**
     * ??????????????????launcher
     */
    public synchronized void checkAndUpdate(Context context,int templateTime) {
        stopCheckUpdate();
        mCheckObserver = new CheckAndNeedToUpdate(context);
        int interval = (templateTime != -1) ? templateTime : mInterval;
        SuperLog.info2SD(TAG,"[LOGIN-12(Maybe in other process)]Start timer to parseLauncher for next time. Interval(unit:s)="+interval);
        Observable.timer(interval, TimeUnit.SECONDS)
                .flatMap(aLong -> { return queryRemoteLauncher(null, context,false); })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(mCheckObserver);
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????json??????
     * ????????????json?????????????????????????????????????????????.json??????
     */
    private void removeAllChildLauncher() {
        childLauncherMap.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonPath = OTTApplication.getCachePath();
                File jsonDir = new File(jsonPath);
                File[] files = jsonDir.listFiles();
                if (null != files) {
                    for (File jsonFile : files) {
                        String fileName = jsonFile.getName();
                        Pattern pattern = Pattern.compile("[0-9]*");
                        Matcher isNum = pattern.matcher(fileName.charAt(0) + "");
                        if (isNum.matches() && fileName.endsWith(".json")) {
                            if(!jsonFile.delete()){
                                SuperLog.error(TAG,"Delete json file failed");
                            }
                        }
                    }
                }
            }
        }).start();

    }

    /**
     * ??????launcher???????????????
     */
    public synchronized void stopCheckUpdate() {
        if (mCheckObserver != null) {
            mCheckObserver.dispose();
        }
    }

    public void checkAndUpdateAfterSwitch(Context context) {
        mCurrentResourceVersion = "0000000000000";
        SharedPreferenceUtil.getInstance().saveLauncherLink("-1");
        SharedPreferenceUtil.getInstance().saveLauncherLinkJson("-1");
        RxCallBack<String> checkObserver = new RxCallBack<String>(HttpConstant.QUERYLAUNCHER,context) {
            @Override
            public void onSuccess(@NonNull String content) {
                SuperLog.error(TAG, "afterswitch->checkAndUpdateAfterSwitch");
                Launcher launcher = JsonParse.json2Object(content, Launcher.class);
                if (null != launcher) {
                    FileUtil.saveContentToFile(SAVED_FILE,content);
                    setLauncher(launcher);
                    //?????????????????????
                    if (!TextUtils.isEmpty(mCurrentVersion)) {
                        SharedPreferenceUtil.getInstance().saveLauncherVersion(mCurrentVersion);
                        mCurrentVersion = null;
                    }
                    parseGroupElements();
                    //                    SharedPreferenceUtil.getInstance().saveLauncherUpdate
                    // (true);
                    removeAllChildLauncher();//??????????????????????????????????????????????????????
                    SwitchProfileEvent event = new SwitchProfileEvent();
                    event.setSwitchProfile(true);
                    EventBus.getDefault().post(event);
                }
                SuperLog.debug(TAG, "????????????");
            }

            @Override
            public void onFail(@NonNull Throwable e) {
                SuperLog.error(TAG, e);
            }
        };
        queryRemoteLauncher(null, context,true).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(checkObserver);

    }

    public void getLatestResources(String getResourceUrl, String batchUrl, String desktopId, String version, Context context) {
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + getResourceUrl;
        GetLatestResourcesRequest request = new GetLatestResourcesRequest();
        request.setDesktopID(desktopId);
        request.setUserToken(AuthenticateManager.getInstance().getLocalToken());
        request.setVersion(version);
        HttpApi.getInstance().getService().getLatestResources(url, request).filter(getLatestResourcesResponse -> {
            if (null != getLatestResourcesResponse && null != getLatestResourcesResponse.getResult()
                    && TextUtils.equals(Result.RETCODE_OK, getLatestResourcesResponse.getResult().getRetCode())) {
                if(!CollectionUtil.isEmpty(getLatestResourcesResponse.getResourceIDs())){
                    //???ResourceIDs???????????????????????????
                    return true;
                }
            }
            return false;
        }).flatMap((Function<GetLatestResourcesResponse, ObservableSource<BatchGetResStrategyDataResponse>>) getLatestResourcesResponse -> {
            List<String> resourceIds = getLatestResourcesResponse.getResourceIDs();
            return batchGetResStrategyData(batchUrl, resourceIds);
        }).subscribeOn(Schedulers.newThread()).subscribe(new RxCallBack<BatchGetResStrategyDataResponse>(batchUrl,context) {
            @Override
            public void onSuccess(BatchGetResStrategyDataResponse batchGetResStrategyDataResponse) {
                if (null != batchGetResStrategyDataResponse
                        && !CollectionUtil.isEmpty(batchGetResStrategyDataResponse.getResourceDatas())) {
                    mapResourceData = parseResourceData(batchGetResStrategyDataResponse.getResourceDatas());
                }
            }

            @Override
            public void onFail(Throwable e) {
                SuperLog.error(TAG, e);
            }
        });
    }

    private Observable<BatchGetResStrategyDataResponse> batchGetResStrategyData(String batchUrl, List<String> ids) {
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + batchUrl;
        BatchGetResStrategyDataRequest request = new BatchGetResStrategyDataRequest();
        request.setResourceIDs(ids);
        request.setUserToken(AuthenticateManager.getInstance().getLocalToken());
        return HttpApi.getInstance().getService().batchGetResStrategyData(url, request);
    }

    /**
     * ???????????????id????????????????????????map??????
     *
     * @param datas
     * @return
     */
    private Map<String, StrategyData> parseResourceData(List<StrategyData> datas) {
        Map<String, StrategyData> dataMap = new HashMap<>();
        for (StrategyData data : datas) {
            if (!CollectionUtil.isEmpty(data.getResStrategyDatas())) {
                dataMap.put(data.getResourceID(), data);
            }
        }
        return dataMap;
    }

    /**
     * ???????????????id?????????????????????????????????????????????
     *
     * @param id ?????????id
     * @return ????????????????????????
     */
    public ResStrategyData getResourceDataById(String id) {
        if (null != mapResourceData && mapResourceData.containsKey(id)) {
            StrategyData strategyData = mapResourceData.get(id);
            for (ResStrategyData data : strategyData.getResStrategyDatas()) {
                if (DateCalendarUtils.isTimeValid(data.getValidStartTime(), data.getValidEndTime())) {
                    return data;
                }
            }
        }
        return null;
    }

    //?????????????????????
    public String getDesktopVersion(){
        return mCurrentResourceVersion;
    }

    //??????Launcher?????????????????????
    public boolean isVersionNew(String serverLauncherVersion){
        String localLauncherVersion = SharedPreferenceUtil.getInstance().getLauncherVersion();
        SuperLog.info2SD(TAG,"Version[Local Launcher]=" + localLauncherVersion + "Version[PHS Server Launcher]="+serverLauncherVersion);
        return !SharedPreferenceUtil.getInstance().getLauncherVersion().equals(serverLauncherVersion);
    }
    public boolean isLauncherLinkNew(String launcherLink){
        launcherLink = removeLauncherLinkNodeId(launcherLink);
        if (TextUtils.isEmpty(launcherLink)){
            return false;
        }
        String localLauncherLink = SharedPreferenceUtil.getInstance().getLauncherLinkJson();
        if (TextUtils.isEmpty(localLauncherLink)){
            return true;
        }
        SuperLog.info2SD(TAG,"[localLauncherLink]=" + localLauncherLink + "[PHS launcherLink]="+launcherLink);
        return !TextUtils.equals(launcherLink,localLauncherLink);
    }

    //????????????launcher??????????????????????????????
    public boolean isChildLauncherLinkNew(String launcherLink,String pageId){
        launcherLink = removeLauncherLinkNodeId(launcherLink);
        if (TextUtils.isEmpty(launcherLink)){
            return false;
        }
        Map<String, String> linkMap = SharedPreferenceUtil.getInstance().getChildLauncherLink();
        if (null == linkMap || TextUtils.isEmpty(linkMap.get(pageId))){
            if (null == linkMap){
                linkMap = new HashMap<>();
            }
            linkMap.put(pageId,launcherLink);
            SharedPreferenceUtil.getInstance().saveChildLauncherLink(linkMap);
            return true;
        }
        SuperLog.info2SD(TAG,"[localLauncherLink]=" + linkMap.get(pageId) + "[PHS launcherLink]="+launcherLink);
        if (!TextUtils.equals(launcherLink,linkMap.get(pageId))){
            linkMap.put(pageId,launcherLink);
            SharedPreferenceUtil.getInstance().saveChildLauncherLink(linkMap);
            return true;
        }
        return false;
    }

    private void saveLauncherLink(String launcherLink){
        if (launcherLink.toLowerCase(Locale.getDefault()).endsWith(".json")) {
            launcherLink = launcherLink.substring(0, launcherLink.lastIndexOf("/"));
            if (!TextUtils.isEmpty(launcherLink)) {
                //??????link?????????
                //AppAplication.getApplication().setLauncherLink(tempLauncherUrl);
                SuperLog.info2SD(TAG, "[LauncherNewLink]=" + launcherLink + "\t[oldLauncherLink]=" + SharedPreferenceUtil.getInstance().getLauncherLink());
                SharedPreferenceUtil.getInstance().saveLauncherNewLink(launcherLink);
                SharedPreferenceUtil.getInstance().saveLauncherLink(launcherLink);
            }


        }
    }

    private String removeLauncherLinkNodeId(String launcherLink){
        if (!TextUtils.isEmpty(launcherLink) && launcherLink.contains("?")){
            launcherLink = launcherLink.substring(0,launcherLink.indexOf("?"));
        }
        SuperLog.debug(TAG,"launcherLink="+launcherLink);
        return launcherLink;
    }

    //????????????,????????????:?????????????????????,?????????????????????launcher?????????
    public void refreshLauncher(Context context,OnQueryLauncherListener listener,boolean isEpgRefresh){
        presenter.queryLauncher(context, new TabItemPresenter.OnQueryLauncherListener() {
            @Override
            public void onVersionChange(boolean change, String launcherName) {
                if (TextUtils.isEmpty(launcherName)){
                    listener.onVersionChange(false,false,isEpgRefresh);
                    SuperLog.debug(TAG,"[refreshLauncher] Return LauncherLink="+launcherName);
                    return;
                }
                listener.onVersionChange(change,false,isEpgRefresh);
                if(change){
                    MessageDataHolder.get().setRefreshLauncherData(true);
                    //??????
                    //LauncherService.getInstance().checkAndUpdate(context,0);
                    stopCheckUpdate();
                    mRefreshObserver = new CheckAndNeedToUpdate(context);
                    String launcherJsonLinkTem = removeLauncherLinkNodeId(launcherName);
                    SharedPreferenceUtil.getInstance().saveLauncherLinkJson(launcherJsonLinkTem);
                    saveLauncherLink(launcherJsonLinkTem);

                    String finalLauncherLinkTem = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + launcherName;

                    SuperLog.info2SD(TAG, "Refresh and Start download launcher.json. URL=" + finalLauncherLinkTem);
                    Observable.timer(0, TimeUnit.SECONDS)
                            .flatMap(aLong -> { return downLoadLauncher(finalLauncherLinkTem, null, context); })
                            .subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .subscribe(mRefreshObserver);
                }
            }
        });
    }

    public interface OnQueryLauncherListener{
        void onVersionChange(boolean change,boolean isOnlyRefreshPbsEpg,boolean isEpgRefresh);
    }

    //??????????????????json???????????????
    public void getChildLauncherLink(Context context, List<String> desktopIDs, TabItemPresenter.OnQueryLauncherListener listener) {
        presenter.queryPHMLauncherList(context, new TabItemPresenter.OnQueryLauncherListener() {
            @Override
            public void onVersionChange(boolean change, String launcherName) {
                listener.onVersionChange(change,launcherName);
                SuperLog.debug(TAG,"[getChildLauncherLink] childLauncherLink = "+launcherName);
            }
        }, false,desktopIDs);
    }

    //????????????????????????
    private String mChildPageId;
    private ChildLauncherPresenter mChildPresent;
    public void setRefreshChild(String pageId, ChildLauncherPresenter childLauncherPresenter){
        this.mChildPageId = pageId;
        this.mChildPresent = childLauncherPresenter;
    }

    public String getLauncherPictureLink(){
        String launcherLink = SharedPreferenceUtil.getInstance().getLauncherLink();
        if(TextUtils.isEmpty(launcherLink) || null == AuthenticateManager.getInstance().getUserInfo()){
            return null;
        } else {
            return "http://" + AuthenticateManager.getInstance().getUserInfo().getIP()+":"+AuthenticateManager.getInstance().getUserInfo().getPort()+launcherLink;
        }
    }
}