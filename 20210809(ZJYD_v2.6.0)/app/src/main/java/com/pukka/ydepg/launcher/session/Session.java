package com.pukka.ydepg.launcher.session;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.pukka.ydepg.BuildConfig;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.bean.node.Marketing;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.Profile;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.node.Configuration;
import com.pukka.ydepg.launcher.bean.node.NamedParameter;
import com.pukka.ydepg.launcher.bean.response.QueryCustomizeConfigResponse;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.bean.UnsubscribeTip;
import com.pukka.ydepg.moudule.search.bean.SearchActorBean;
import com.pukka.ydepg.moudule.search.bean.SearchDataClassify;
import com.pukka.ydepg.moudule.search.bean.SearchSubjectBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * APP running data. All the data can be used in UI.
 * The class can save the running data of app,the all data will be stored in cache.
 * If the user logout the app,all the data will be cleared.
 */
public class Session {

    private static final String TAG = Session.class.getSimpleName();

    @SerializedName("csrfToken")
    private String csrfToken;

    @SerializedName("ntpDomain")
    private String ntpDomain = null;

    @SerializedName("ntpDomainBackup")
    private String ntpDomainBackup = null;

    @SerializedName("profileList")
    private List<Profile> profileList;

    @SerializedName("profile")
    private Profile profile;

    @SerializedName("userId")
    private String userId;

    @SerializedName("userToken")
    private String userToken;

    @SerializedName("queryCustomizeConfigResponse")
    private QueryCustomizeConfigResponse queryCustomizeConfigResponse;

    @SerializedName("mapConfigValue")
    private HashMap<String,String> mapConfigValue;

    @SerializedName("deviceId")
    private String deviceId;

    @SerializedName("profileSN")
    private String profileSN;

    @SerializedName("launcherLink")
    private String launcherLink;

    @SerializedName("userFilter")
    private String userFilter;

    @SerializedName("userVODListFilter")
    private String userVODListFilter;

    @SerializedName("cookie")
    private String cookie;

    @SerializedName("heartbit_interval")
    private String heartbitInterval;

    @SerializedName("accountName")
    private String accountName;

    @SerializedName("userGroup")
    private String userGroup;

    @SerializedName("marketProducts")
    private Map<String,Product> marketProducts = new HashMap<>();

    @SerializedName("BillID")
    private String billId;

    //??????????????????
    @SerializedName("userAreaCode")
    private String userAreaCode;

    public String getProfileSN() {
        return profileSN;
    }

    public void setProfileSN(String profileSN) {
        this.profileSN = profileSN;
    }

    public String getUserAreaCode() {
        return userAreaCode;
    }

    public void setUserAreaCode(String userAreaCode) {
        this.userAreaCode = userAreaCode;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public Map<String, Product> getMarketProducts() {
        return marketProducts;
    }

    public void setMarketProducts(Map<String, Product> marketProducts) {
        this.marketProducts = marketProducts;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public long getHeartbitInterval() {
        if (TextUtils.isEmpty(heartbitInterval)) {
            //???????????????15??????
            return 15 * 60L;
        } else {
            return Long.parseLong(heartbitInterval);
        }
    }

    public void setHeartbitInterval(String heartbitInterval) {
        this.heartbitInterval = heartbitInterval;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getNTPDomain() {
        return ntpDomain;
    }

    public void setNTPDomain(String ntpDomain) {
        this.ntpDomain = ntpDomain;
    }

    public String getNTPDomainBackup() {
        return ntpDomainBackup;
    }

    public void setNTPDomainBackup(String ntpDomainBackup) {
        this.ntpDomainBackup = ntpDomainBackup;
    }

    public List<Profile> getProfileList() {
        return profileList;
    }

    public void setProfileList(List<Profile> profileList) {
        this.profileList = profileList;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public QueryCustomizeConfigResponse getQueryCustomizeConfigResponse() {
        return queryCustomizeConfigResponse;
    }

    public void setQueryCustomizeConfigResponse(QueryCustomizeConfigResponse queryCustomizeConfigResponse) {
        this.queryCustomizeConfigResponse = queryCustomizeConfigResponse;
        parseConfigurationValueToMap(queryCustomizeConfigResponse);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserFilter() {
        return userFilter;
    }

    public void setUserFilter(String userFilter) {
        this.userFilter = userFilter;
    }

    public String getUserVODListFilter() {
        return userVODListFilter;
    }

    public void setUserVODListFilter(String userVODListFilter) {
        this.userVODListFilter = userVODListFilter;
    }

    public void setCSRFToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public String getCSRFToken() {
        return this.csrfToken;
    }

    public String getLauncherLink() {
        return launcherLink;
    }

    public void setLauncherLink(String launcherLink) {
        this.launcherLink = launcherLink;
    }

    private void parseConfigurationValueToMap(QueryCustomizeConfigResponse queryCustomizeConfigResponse){
        if (null == queryCustomizeConfigResponse) {
            return;
        }

        List<Configuration> configurationList = queryCustomizeConfigResponse.getConfigurationList();
        if (null == configurationList) {
            return;
        }

        mapConfigValue = new HashMap<>();
        for (Configuration configuration : configurationList) {
            if (Configuration.ConfigType.TERMINAL_CONFIGURATION.equals(configuration.getConfigType())) {
                List<NamedParameter> extensionFieldList = configuration.getExtensionFieldList();
                if (null == extensionFieldList) {
                    return;
                }
                for (NamedParameter namedParameter : extensionFieldList) {
                    mapConfigValue.put(namedParameter.getKey(),namedParameter.getFistItemFromValue());
                }
            }
        }
        SuperLog.info2SD(TAG,"Parse terminal configuration finished. Param details will be shown in debug version as followed.");
        if (BuildConfig.DEBUG) {
            for (String key : mapConfigValue.keySet()) {
                SuperLog.info2SD(TAG, "key=" + key + "\tvalue=" + mapConfigValue.get(key));
            }
        }

    }

    /**
     * get the value of configuration
     *
     * @param configType the configuration type
     * @param name       the name of configuration
     * @return String, the value of configuration'name
     * @see .Configuration.Key
     */
    private String getConfigurationValue(String configType, String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }

        if (null == queryCustomizeConfigResponse) {
            return null;
        }
        List<Configuration> configurationList = queryCustomizeConfigResponse.getConfigurationList();
        if (null == configurationList) {
            return null;
        }
        for (Configuration configuration : configurationList) {
            if (configType.equals(configuration.getConfigType())) {
                List<NamedParameter> extensionFieldList = configuration.getExtensionFieldList();
                if (null == extensionFieldList) {
                    return null;
                }
                for (NamedParameter namedParameter : extensionFieldList) {
                    if (name.equals(namedParameter.getKey())) {
                        String firstValue = namedParameter.getFistItemFromValue();
                        if (TextUtils.isEmpty(firstValue)) {
                            return null;
                        } else {
                            return firstValue;
                        }
                    }
                }
            }
        }
        return null;
    }

    public Configuration getServerConfiguration() {
        Configuration configuration = null;
        if (queryCustomizeConfigResponse != null) {
            List<Configuration> configurationList = queryCustomizeConfigResponse.getConfigurationList();
            if (!CollectionUtil.isEmpty(configurationList)) {
                for (int i = 0; i < configurationList.size(); i++) {
                    if (Configuration.ConfigType.VSP_SERVER_CONFIGURATION.equals(configurationList.get(i).getConfigType())) {
                        configuration = configurationList.get(i);
                        break;
                    }
                }
                return configuration;
            }
        }
        return configuration;
    }

    /**
     * get the value of terminal configuration
     *
     * @param name the name of configuration
     * @return String, the value of configuration'name
     * .Configuration.Key
     */
    public String getTerminalConfigurationValue(String name) {
        if(mapConfigValue == null){
            return null;
        }else{
            return mapConfigValue.get(name);
        }

        //return getConfigurationValue(Configuration.ConfigType.TERMINAL_CONFIGURATION, name);
    }

    public boolean isHotelUser() {
        if (TextUtils.isEmpty(userGroup)) {
            return false;
        } else {
            return userGroup.equals(getTerminalConfigurationValue("hotelUserGroup"));
        }
    }

    public String getTerminalConfigurationChildSubject(String keyName,List<String> subjects){
        String subjectsStr=getTerminalConfigurationValue(keyName);
        List<String> subjectsList=null;
        LinkedHashMap<String,String> subjectsMap=new LinkedHashMap<>();
        List<String> orderSubjects=new ArrayList<>();
        if (!TextUtils.isEmpty(subjectsStr)) {
            subjectsList = JsonParse.jsonToStringList(subjectsStr);
        }
        if(null!=subjectsList){
            for(int i=0;i<subjectsList.size();i++){
               String subjectStr= subjectsList.get(i);
               String[]  subjectIds= subjectStr.split("_");
               if(null!=subjectIds&&subjectIds.length>1){
                   subjectsMap.put(subjectIds[0],subjectIds[1]);
               }

            }

        }
        if(subjectsMap.size()>0){
            Set<String> keys=subjectsMap.keySet();
            for(String key:keys){
                for(int i=0;i<subjects.size();i++){
                    if(key.equals(subjects.get(i))){
                        orderSubjects.add(key);
                    }
                }
            }

        }
        if(orderSubjects.size()>0){
            return subjectsMap.get(orderSubjects.get(0));
        }
        return subjects.get(0);
    }

    public List<String> getTerminalConfigurationPreposeProductIDs() {
        List<String> productsIds = null;
        String productIdstr = getTerminalConfigurationValue(Constant.PREPOSE_PRODUCT_LIST);
        if (!TextUtils.isEmpty(productIdstr)) {
            productsIds = JsonParse.jsonToStringList(productIdstr);

        }
        return productsIds;
    }

    public List<String> getTerminalConfigurationPostPositionProductIDs() {
        List<String> productsIds = null;
        String productIdstr = getTerminalConfigurationValue(Constant.POSTPOSITION_PRODUCT_LIST);
        if (!TextUtils.isEmpty(productIdstr)) {
            productsIds = JsonParse.jsonToStringList(productIdstr);

        }
        return productsIds;
    }

    //??????????????????
    public List<Subject>  getTerminalConfigurationSettingFilterSubjects(){
        List<Subject> subjects=null;
        String subjectsStr = getTerminalConfigurationValue(Constant.CHILD_SETTING_FILTER_CATEGORY);
         if(!TextUtils.isEmpty(subjectsStr)){
            subjects=JsonParse.jsonToClassList(subjectsStr,Subject.class);
        }
        return subjects;
    }

    public List<String> getTerminalConfigurationSecondConfirmProductIDs() {
        List<String> productsIds = null;
        String productIdstr = getTerminalConfigurationValue(Constant.SECOND_CONFIRM_PRODUCT_LIST);
        if (!TextUtils.isEmpty(productIdstr)) {
            productsIds = JsonParse.jsonToStringList(productIdstr);

        }
        return productsIds;
    }

    public Map<String, String> getTerminalConfigurationCPAPKINFO() {
        Map<String, String> map = null;
        String cpAPKInfo = getTerminalConfigurationValue(Constant.CP_APK_INFO);
        if (!TextUtils.isEmpty(cpAPKInfo)) {
            map = JsonParse.jsonToMap(cpAPKInfo);
        }
        return map;
    }

    public List<String> getTerminalConfigurationSubjectIDS() {
        List<String> subjectIDs = new ArrayList<>();
        String terminal;

        //?????????
        terminal = getTerminalConfigurationValue("vod_series_subject_id");
        if (!TextUtils.isEmpty(terminal)) {
            subjectIDs.add(terminal);
        }
        //??????
        terminal = getTerminalConfigurationValue("vod_comic_subject_id");
        if (!TextUtils.isEmpty(terminal)) {
            subjectIDs.add(terminal);
        }
        //??????
        terminal = getTerminalConfigurationValue("vod_children_subject_id");
        if (!TextUtils.isEmpty(terminal)) {
            subjectIDs.add(terminal);
        }
        return subjectIDs;
    }

    /**
     * ??????????????????
     * @return ??????id
     */
    public List<String> getTerminalConfigurationVodReverseSubjectIDS() {
        List<String> subjectIDs = new ArrayList<>();
        String terminal;

        terminal = getTerminalConfigurationValue("vod_reverse_subject_id");
        if (!TextUtils.isEmpty(terminal)) {
            String[] splits = terminal.split(",");
            if(splits != null && splits.length > 0){
                for(String split:splits){
                    subjectIDs.add(split);
                }
            }
        }
        return subjectIDs;
    }

    /**
     * ??????????????????
     * @return ??????id
     */
    public List<String> getTerminalConfigurationVodNameSubjectIDS() {
        List<String> subjectIDs = new ArrayList<>();
        String terminal;

        terminal = getTerminalConfigurationValue("vod_name_subject_id");
        if (!TextUtils.isEmpty(terminal)) {
            String[] splits = terminal.split(",");
            if(splits != null && splits.length > 0){
                for(String split:splits){
                    subjectIDs.add(split);
                }
            }
        }
        return subjectIDs;
    }

    public List<String> getTerminalConfigurationNOSCOREDISPLAYEDSUBJECTIDS() {
        List<String> listNoShowIds = null;
        String noShowIdstr = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.NOSCOREDISPLAYEDSUBJECTIDS);
        if (!TextUtils.isEmpty(noShowIdstr)) {
            listNoShowIds  = JsonParse.jsonToStringList(noShowIdstr);

        }
        return listNoShowIds ;
    }

    public List<String> getTerminalConfigurationNOSCOREDISPLAYEDCMSTYPE() {
        List<String> listNoShowTypes = null;
        String noShowIdstr = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.NOSCOREDISPLAYEDCMSTYPE);
        if (!TextUtils.isEmpty(noShowIdstr)) {
            listNoShowTypes  = JsonParse.jsonToStringList(noShowIdstr);

        }
        return listNoShowTypes ;
    }

    //?????????????????????????????????url?????????url????????????SubjectID
    public String getTerminalConfigurationBASE_BUSINESS_SUBJCET_ID() {
        String subjectID = "";
        String subjectIDStr = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.BASE_BUSINESS_SUBJCET_ID);
        if (!TextUtils.isEmpty(subjectIDStr)) {
            subjectID = subjectIDStr;

        }
        return subjectID ;
    }

    public List<String> getTerminalConfigurationVODDETAIL_DEFINITION_DISPLAY() {
        List<String> listShowIds = null;
        String showIdstr = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.VODDETAIL_DEFINITION_DISPLAY);
        if (!TextUtils.isEmpty(showIdstr)) {
            listShowIds  = JsonParse.jsonToStringList(showIdstr);

        }
        return listShowIds ;
    }

    /*????????????????????????
     * 0 ?????????
     * 1 ?????????
     * 2 ????????????????????????
     */
    public String getTerminalConfigurationVOD_SKIP_SWITCH() {
        String Str = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.VOD_SKIP_SWITCH);
        if (null != Str){
            return Str;
        }else{
            return "";
        }
//        return "0";
    }

    public Map<String, Marketing> getTerminalConfigurationMarketing(){
        String marketingInfo = getTerminalConfigurationValue(Constant.MARKETING_INFO);
        HashMap<String ,Marketing> markets=null;
        if(!TextUtils.isEmpty(marketingInfo)){
            try {
                markets = JsonParse.json2Object(marketingInfo, new TypeToken<HashMap<String, Marketing>>(){}.getType());
            } catch (Exception e){
                SuperLog.error(TAG,e);
            }
        }
        return markets;
    }

    //whitelist_cpId???????????????cpid?????????????????????SearchContent???queryVodSubjectList??????????????????
    public List<String> getTerminalConfigurationCpIDList() {
        try{
            List<String> cpIdList = null;
            String cpIdStr = getTerminalConfigurationValue("whitelist_cpId");
            //SuperLog.info2SD(TAG,"TerminalConfigurationValue of [whitelist_cpId] is : " + cpIdStr);
            if (!TextUtils.isEmpty(cpIdStr)) {
                cpIdList  = JsonParse.jsonToStringList(cpIdStr);
            }
            return cpIdList ;
        }catch (Exception e){
            SuperLog.error(TAG,e);
            return null;
        }
    }

    public String getMiguCpID() {
        String miguCpID = getTerminalConfigurationValue("OTT_cpIdList");
        if (TextUtils.isEmpty(miguCpID)) {
            miguCpID = "000101";
        }
        return miguCpID;
    }

    public List<String> getTerminalConfigurationUnsupport4KDevice(){
        List<String> deviceList = null;
        String devices = getTerminalConfigurationValue(Constant.UNSUPPORT_4K_DEVICE);
        if(!TextUtils.isEmpty(devices)){
            deviceList= JsonParse.jsonToStringList(devices);
        }
        return  deviceList;
    }

    public Map<String, List<String>> getTerminalConfigurationProductPackageRelationship(){
        Map<String, List<String>> productPackageRelationshipList = null;
        String productPackageRelationships = getTerminalConfigurationValue(Constant.PRODUCT_PACKAGE_RELATIONSHIP);
        if(!TextUtils.isEmpty(productPackageRelationships)) {
            try {
                productPackageRelationshipList = JsonParse.json2Object(productPackageRelationships, new TypeToken<HashMap<String, List<String>>>()
                {}.getType());
            } catch (Exception e){
                SuperLog.error(TAG,e);
            }
        }
        return  productPackageRelationshipList;
    }

    public SearchDataClassify<String> getTerminalConfigurationHotKeys(){
        String hotKeys = getTerminalConfigurationValue(Constant.SEARCH_HOT_KEY);
        if(!TextUtils.isEmpty(hotKeys)){
            Type jsonType = new TypeToken<SearchDataClassify<String>>(){}.getType();
            return  JsonParse.json2Object(hotKeys,jsonType);
        }
        return null;
    }

    public SearchDataClassify<SearchActorBean> getTerminalConfigurationHotActors(){
        String hotActors = getTerminalConfigurationValue(Constant.SEARCH_HOT_ACTORS);
        if(!TextUtils.isEmpty(hotActors)){
            Type jsonType = new TypeToken<SearchDataClassify<SearchActorBean>>(){}.getType();
            return   JsonParse.json2Object(hotActors,jsonType);
        }
        return null;
    }

    public SearchDataClassify<SearchSubjectBean> getTerminalConfigurationSearchSubjects(){
        String searchSubjects = getTerminalConfigurationValue(Constant.SEARCH_SUBJECT_CLASSIFY);
        if(!TextUtils.isEmpty(searchSubjects)){
            Type jsonType = new TypeToken<SearchDataClassify<SearchSubjectBean>>(){}.getType();
            return JsonParse.json2Object(searchSubjects,jsonType);
        } else {
            return new SearchDataClassify<>();
        }
    }

    public SearchDataClassify<String> getTerminalConfigurationHotCategory(){
        String hotCategory = getTerminalConfigurationValue(Constant.SEARCH_HOT_CATEGORY);
        if(!TextUtils.isEmpty(hotCategory)){
            Type jsonType = new TypeToken<SearchDataClassify<String>>(){}.getType();
            return   JsonParse.json2Object(hotCategory,jsonType);
        }
        return null;
    }

    //??????????????????????????????
    public Map<String, String> getMessageFrequencyControl(){
        String MessageInfo = getTerminalConfigurationValue(Constant.MESSAGE_FREQUENCY_CONTROL);
        HashMap<String ,String> message=null;
        if(!TextUtils.isEmpty(MessageInfo)){
            try {
                message = JsonParse.json2Object(MessageInfo, new TypeToken<HashMap<String, String>>(){}.getType());
            } catch (Exception e){
                SuperLog.error(TAG,e);
            }
        }
        return message;
    }

    /*???????????????????????????
     * 0???????????????
     * 1???????????????
     * ????????????????????????????????????
     */

    public String getTerminalConfigurationOrderSecondConfirmFocus(){
        String Str = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.ORDER_SECOND_CONFIRM_FOCUS);
        return  Str;
    }

    public String getTerminalConfigurationOrderPhoneBillPayMinimumAmount(){
        String Str = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.ORDER_PHONEBILL_PAY_MINIMUM_AMOUNT);
        return  Str;
    }

    /*
     *???????????????????????????????????????????????????Toast??????????????????
     */
    public String getTerminalConfigurationOrderProductMutexInfo(){
        String str = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.ORDER_PRODUCT_MUTEX_INFO);
        return str;
    }

    /*
     *????????????????????????????????????????????????id
     */
    public String getTerminalConfigurationMyOrderUnshowList(){
        String str = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.MY_ORDER_UNSHOW_LIST);
        return str;
    }

    /*
     *??????????????????????????????????????????????????????????????????????????????
     */
    public Map<String,List<UnsubscribeTip>> getTerminalConfigurationUnsubscribeTips(){
        String str = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.UNSUBSCRIBE_TIPS);
//        str = "{\"600000617651\":[{\"offerid\":\"600000463006\",\"tip\":\"??????tip1\"},{\"offerid\":\"67890\",\"tip\":\"??????tip2\"}],\"600000581477\":[{\"offerid\":\"600000495366\",\"tip\":\"??????tip3\"},{\"offerid\":\"67890\",\"tip\":\"??????tip4\"}]}";
        if (!TextUtils.isEmpty(str)){
            Map<String ,List<UnsubscribeTip>> map = JsonParse.json2Object(str,new TypeToken<HashMap<String, List<UnsubscribeTip>>>() {}.getType());
            return map;
        }else{
            return null;
        }
    }

    /*
     *????????????????????????????????????id
     */
    public String getTerminalConfigurationGiftProductId(){
        String str = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.GIFT_PRODUCTID);
        return str;
    }

    /*
     *?????????????????????H5????????????
     */
    public String getTerminalConfigurationNeedJumpToH5Order(){
        String str = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.NEED_JUMP_TO_H5_ORDER);
//        return "0";
        return str;
    }

    /*
     *???????????????????????????
     */
    public String getTerminalConfigurationAvoidRepeatPaymentTime(){
        String str = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.AVOID_REPEAT_PAYMENT_TIME);
        return str;
    }

    /*
     *??????????????????????????????
     */
    public String getTerminalConfigurationGiftProductIdSwitch(){
        String str = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.GIFT_PRODUCTID_SWITCH);
        return str;
    }

    /*
     *4K?????????????????????
     */
    public String getTerminalConfigurationVod4kWarnningTip(){
        String str = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.VOD_4K_WARNNING_TIP);
        return str;
    }

    /*
     *???????????????????????????vod??????
     */
    public Map<String,String> getTerminalConfigurationNotShowCastListCmsType(){
        String str = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.NOT_SHOW_CAST_LIST_CMSTYPE);

        if (!TextUtils.isEmpty(str)){
            Map<String ,String> map = JsonParse.json2Object(str,new TypeToken<HashMap<String, String>>() {}.getType());
            if (null != map){
                return map;
            }
        }

        return null;
    }

    public String getTerminalConfigurationRecommendConfig(){
        String str = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.RECOMMEND_CONFIG);
        return str;
    }

    public String getTerminalConfigurationVoddetailRecommendRecent(){
        String str = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.VODDETAIL_RECOMMEND_RECENT);
        return str;
    }


    /*
     *??????????????????????????????????????????
     */
    public String getTerminalConfigurationUnsubscribeButtonConfig(){
        String str = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.UNSUBSCRIBE_BUTTON_CONFIG);
        return str;
    }

    //???????????????????????????????????????
    public String getTerminalConfigurationSuperScriptSwitch(){
        return getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.SUPERSCRIPT_SWIRCH);
    }

    //???????????????????????????????????????
    public boolean getTerminalConfigurationCanSetSpeed(){
        String str = getTerminalConfigurationValue(com.pukka.ydepg.common.http.v6bean.v6node.Configuration.Key.CAN_SET_SPEED_BLACK_LIST);
        Log.i(TAG, "getTerminalConfigurationCanSetSpeed: "+str);
        if(TextUtils.isEmpty(str)){
            return true;
        }
        String deviceType = CommonUtil.getDeviceType();
        Log.i(TAG, "getTerminalConfigurationCanSetSpeed: "+!str.contains(deviceType));
        return !str.contains(deviceType);
    }
}