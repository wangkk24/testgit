package com.pukka.ydepg.common.ad;

import android.text.TextUtils;
import android.util.Log;

import com.huawei.ott.sdk.encrypt.MsaSecurityStorage;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertContent;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertUser;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.Device;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.DistributionChannel;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.Event;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.UserIdentification;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryProductRequest;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.security.SHA256Util;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.AuthenticateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.reactivex.schedulers.Schedulers;

import static com.pukka.ydepg.common.ad.AdConstant.SSP_DEFAULT_SKIP_TIME;

public class AdUtil {

    private static final String TAG = "AdUtil";

    //当前登录用户是否是会员
    private static boolean isMember = false;

    //SSP广告能力总开关
    private static boolean isSspEnable(){
        String sspSwitch = CommonUtil.getConfigValue(AdConstant.SSP_SWITCH_ALL);
        return !AdConstant.SSP_CLOSE.equals(sspSwitch);
    }

    //SSP广告能力子场景开关,入参adClassify取值为@AdConstant.AdClassify  VIDEO/START/SCREEN/DETAIL
    //没配置默认为开
    private static boolean isSspSingleEnable(String adClassify){
//        //测试代码，强制打开贴片广告开关
//        if(AdConstant.AdClassify.SCREEN.equals(adClassify)){
//            return true;
//        }
        Map<String,String> mapSspSwitchSingle = CommonUtil.getMapConfigValue(AdConstant.SSP_SWITCH_SINGLE);
        if(mapSspSwitchSingle == null || mapSspSwitchSingle.isEmpty()){
            return true;
        }
        return !AdConstant.SSP_CLOSE.equals(mapSspSwitchSingle.get(adClassify));
    }

    //判断当前机顶盒是否支持SSP视频前贴广告
    private static boolean isStbSupportVideo(){
        List<String> stbBlacklist = CommonUtil.getListConfigValue(AdConstant.SSP_VIDEO_STB_BLACKLIST);
        if(stbBlacklist.contains("ALL")){
            return false;
        } else {
            return !stbBlacklist.contains(CommonUtil.getDeviceType());
        }
    }

    public static boolean enableSsp(String adClassify){
        //总开关关闭,跳过
        if(!isSspEnable()){
            SuperLog.info2SD(AdConstant.TAG,"SSP all switch is close, skip [" + adClassify + "] AD.");
            return false;
        }

        //子场景开关关闭,跳过
        if(!isSspSingleEnable(adClassify)){
            SuperLog.info2SD(AdConstant.TAG,"SSP sub switch is close, skip [" + adClassify + "] AD.");
            return false;
        }

        //视频贴片广告,判断是否是会员/机顶盒是否支持广告
        if( AdConstant.AdClassify.VIDEO.equals(adClassify)){
            //用户是会员跳过
            if(isMember){
                SuperLog.info2SD(AdConstant.TAG,"Current user is member, skip video AD.");
                return false;
            }
            //机顶盒不支持视频前贴广告,跳过
            if( !AdUtil.isStbSupportVideo() ){
                SuperLog.info2SD(AdConstant.TAG,"Current device is not support front video AD function, skip AD.");
                return false;
            }
        }
        return true;
    }

    //华为安全要求不允许明文存储URL
    private static String getAdvertUrl(){
        return MsaSecurityStorage.read(SharedPreferenceUtil.Key.ADVERT_URL+"");
        //return getStringData(Key.ADVERT_URL+"","");
    }

    private static String getMD5Mac(){
        try{
            String mac  = CommonUtil.getMac().replace(":","").toUpperCase();//去冒号转大写
            mac         = SHA256Util.Encrypt(mac,"MD5").toLowerCase();
            return mac;
        } catch (Exception e){
            SuperLog.error(TAG,e);
            return "";
        }
    }

    private static String getMD5AndroidID(){
        try{
            String androidID = CommonUtil.getAndroidID();
            androidID        = SHA256Util.Encrypt(androidID,"MD5").toLowerCase();
            return androidID;
        } catch (Exception e){
            SuperLog.error(TAG,e);
            return "";
        }
    }

    //获取SSP平台[广告请求]/[监测事件上报]URL
    static String getSspAdvertUrl(String requestName){
        String advertUrl;
        String adDomain = getAdvertUrl();
        if(!TextUtils.isEmpty( adDomain)){
            advertUrl = adDomain                 + AdConstant.ADVERT_URL_PATH;
        } else {
            advertUrl = AdConstant.ADVERT_DOMAIN + AdConstant.ADVERT_URL_PATH;
        }
        advertUrl += requestName;
        SuperLog.debug(AdConstant.TAG, "SSP AD request is : " + advertUrl);
        return advertUrl;
    }

    //获取第三方监测事件上报服务URL
    public static String getThirdReportUrl(AdvertContent content,String adType,String actionType){
        try{
            List<Event> listEvents;
            if(AdConstant.AdType.BANNER.equals(adType)){
                //海报广告
                listEvents = content.getDisplay().getEvents();
            } else if (AdConstant.AdType.VIDEO.equals(adType)){
                //视频广告
                listEvents = content.getVideo().getEvents();
            } else {
                return null;
            }
            for(Event event : listEvents){
                if(actionType.equals(event.getType())){
                    return event.getUrl();
                }
            }
        } catch (Exception e){
            SuperLog.error(AdConstant.TAG,e);
        }
        return null;
    }

    static String getRequestID(){
        Random random = new Random();
        String result="";
        for (int i=0;i<10;i++) {
            result += random.nextInt(10);
        }
        return "OTT_EPG_"+System.currentTimeMillis()+"_"+result;
    }

    //秒针监测样例，仅供参考，以广告请求返回消息中的URL为准：
    //http://g.dtv.cn.miaozhen.com/x/k=2008872&p=6vJR8&ns=__IP__&nx=__LAB__&m1=__ANDROIDID1__&m1a=__ANDROIDID__&m4=__AAID__&m6=__MAC1__&m6a=__MAC__&m6o=__M6O__&rt=2&nd=__DRA__&nt=__TIME__&ni=__IESID__&tdt=__TDT__&tdr=__TDR__&o=
    //国双监测样例：
    //https://i.gridsumdissector.com/v/?gscmd=impress&gid=gad_203_rm10pqr0&t=1&os=__OS__&if=__IDFA__&oid=__OPENUDID__&aid=__ANDROIDID__&oa=__OAID__&m=__MAC__&ip=__IP__&ts=__TS__&aaid=__AAID__&ua=__UA__&autorefresh=__AUTOREFRESH__&mid=__REQUESTID__
    static String setReportInfo(String url){
        //__MAC__	去除冒号分隔符的大写MAC地址取MD5摘要（秒针/国双）
        //__ANDROIDID__	Android ID取MD5摘要（秒针/国双）
        try{
            return url.replace("__MAC__",getMD5Mac()).replace("__ANDROIDID__",getMD5AndroidID());
        } catch (Exception e){
            SuperLog.error(AdConstant.TAG,e);
            return null;
        }
    }

    //随机播放开机广告 海报或者视频 0:海报 1:视频
    static String showBannerOrVideo(){
        return System.currentTimeMillis() % 2 == 0L ? AdConstant.AdType.BANNER:AdConstant.AdType.VIDEO;
    }








    //华为安全要求不允许明文存储URL,此方法务必在获取终端配置参数后调用
    public static void setAdvertUrl(){
        String advertUrl= CommonUtil.getConfigValue(AdConstant.ADVERT_URL_DOMAIN);
        if(!TextUtils.isEmpty(advertUrl)){
            MsaSecurityStorage.write(SharedPreferenceUtil.Key.ADVERT_URL+"",advertUrl);
            //putString(Key.ADVERT_URL+"",advertUrl);
        }
    }

    public static AdvertUser getUser(){
        UserIdentification userIdentification = new UserIdentification();
        userIdentification.setIdType(UserIdentification.IdType.USERID);
        //用户需要用MD5加密
        String md5User = SHA256Util.Encrypt(AuthenticateManager.getInstance().getUserInfo().getUserId(),"MD5");
        userIdentification.setIdValue(md5User);

        List<UserIdentification> userIdentificationList = new ArrayList<>();
        userIdentificationList.add(userIdentification);

        AdvertUser user = new AdvertUser();
        user.setUsers(userIdentificationList);
        return user;
    }

    public static String getClickUrl(AdvertContent content){
        try{
            return content.getDisplay().getBanner().getLink().getUrl();
        }catch (Exception e){
            SuperLog.error(AdConstant.TAG,e);
            return null;
        }
    }

    public static long getBannerAdvertShowTime(){
        String duration = SessionService.getInstance().getSession().getTerminalConfigurationValue(AdConstant.BANNER_DURATION);
        Log.i(TAG, "getBannerAdvertShowTime: "+duration);
        if(!TextUtils.isEmpty(duration)){
            return Long.parseLong(duration)*1000L;
        } else {
            return 5000L;
        }
    }

    public static Device getDevice(){
        Device device = new Device();
        device.setType("STB");
        //device.getMake()
        device.setModel(CommonUtil.getDeviceType());
        //device.setDidmd5();
        device.setDpidmd5(getMD5AndroidID());
        device.setMacmd5(getMD5Mac());
        return device;
    }

    public static DistributionChannel getChannel(){
        DistributionChannel channel = new DistributionChannel();
        channel.setId("OTT_EPG");
        channel.setName("EPG");
        channel.setType("APP");
        return channel;
    }

    //查询是否是SSP广告平台会员
    @SuppressWarnings("CheckResult")
    public static void querySspMember(){
        List<String> listMemberProductID = CommonUtil.getListConfigValue(AdConstant.SSP_MEMBER_LIST);
        if(CollectionUtil.isEmpty(listMemberProductID)){
            SuperLog.error(AdConstant.TAG,"Ad member of current user: [false]\t(Not configured member product)");
            return;
        }
        QueryProductRequest request = new QueryProductRequest();
        request.setQueryType(QueryProductRequest.QueryType.BY_IDLIST);
        request.setProductIds(listMemberProductID);
        HttpApi.getInstance().getService().queryProduct(HttpUtil.getVspUrl(HttpConstant.QUERY_PRODUCT), request)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(queryProductResponse->{
                if (null != queryProductResponse && null != queryProductResponse.getResult() && Result.RETCODE_OK.equals(queryProductResponse.getResult().getRetCode())) {
                    List<Product> products = queryProductResponse.getProductList();
                    if (!CollectionUtil.isEmpty(products)) {
                        for(Product product: products){
                            //1代表已订购
                            if("1".equals(product.getIsSubscribed())){
                                SuperLog.error(AdConstant.TAG,"Ad member of current user: [true]");
                                isMember = true;
                                return;
                            }
                        }
                    }
                }
                //查询失败认为是非会员
                isMember = false;
                SuperLog.error(AdConstant.TAG,"Ad member of current user: [false]\t(Not subscribed member product)");
            });
    }

    public static VOD getVodInfoForEpisode(VOD father,VOD episode){

        if(TextUtils.isEmpty(episode.getCmsType())){
            episode.setCmsType(father.getCmsType());
        }

        if(TextUtils.isEmpty(episode.getCode())){
            episode.setCode(father.getCode());
        }

        if(CollectionUtil.isEmpty(episode.getGenres())) {
            episode.setGenres(father.getGenres());
        }
        return episode;
    }

    //SSP广告能力子场景开关,入参adClassify取值为@AdConstant.AdClassify  VIDEO/START/SCREEN
    public static boolean isShowAdTime(String adClassify){
        boolean show = true;
        Map<String,String> mapShowAdTime = CommonUtil.getMapConfigValue(AdConstant.SSP_TIME_SWITCH_ALL);
        if(mapShowAdTime != null ){
            try{
                show = !AdConstant.SSP_CLOSE.equals(mapShowAdTime.get(adClassify));
            } catch (Exception e){
                SuperLog.error(TAG,e);
            }
        }
        return show;
    }

    //SSP广告能力子场景开关,入参adClassify取值为@AdConstant.AdClassify  VIDEO/START/SCREEN
    public static int getSkipTime(String adClassify){
        int skipTime = SSP_DEFAULT_SKIP_TIME;
        Map<String,String> mapSspSkipTime = CommonUtil.getMapConfigValue(AdConstant.SSP_SKIP_TIME_ALL);
        if(mapSspSkipTime != null ){
            try{
                skipTime = Integer.parseInt(mapSspSkipTime.get(adClassify));
            } catch (Exception e){
                SuperLog.error(TAG,e);
            }
        }
        return skipTime;
    }

    public static String getSSPAdvertValue(AdvertContent advertContent,String param){
        String value;
        try{
            switch (param){
                case "clickUrl":
                    value = advertContent.getDisplay().getBanner().getLink().getUrl();
                    break;
                case "banner":
                    value = advertContent.getDisplay().getBanner().getImg();
                    break;
                default:
                    value = null;
                    break;
            }
        }catch (Exception e){
            //SuperLog.error(TAG,e);
            value = null;
        }
        return value;
    }
}