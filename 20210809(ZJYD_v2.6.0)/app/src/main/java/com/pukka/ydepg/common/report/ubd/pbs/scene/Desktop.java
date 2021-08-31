package com.pukka.ydepg.common.report.ubd.pbs.scene;

import android.text.TextUtils;

import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.fcc.MulticastTool;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaConstant;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.moudule.featured.view.LauncherService;

import java.util.HashMap;
import java.util.Map;

public class Desktop {

    private static String downloadState = "1";
    private static String downloadTime="0";
    private static long startDownLoadTime = 0,endDownLoadTime = 0;
    private static String analyseState = "1";

    //EPG解析新桌面JSON
    public static Map<String,String> getDestopErrorData(){
        Map<String,String> data = new HashMap<>();
        data.put("userID", SessionService.getInstance().getSession().getUserId());
        data.put("errorCode", "E999999");
        data.put("errorMsg","failedToParseTheJSON");
        data.put("boxModel",CommonUtil.getDeviceType());
        data.put("version", CommonUtil.getVersionName());
        data.put("STBID",CommonUtil.getSTBID());
        return data;
    }

    //EPG进入首页后上报stb_getEPGInfo.jsp的信息
    /*
        EPGVersion String M EPG实际版本号
        UserGroup  String M 用户组：ZJLogin返回的userGroup，枚举值——
                空或-1:商用
                700005:领导
                200001:测试
                1100:投影机
                11001:投影机演示
        LauncherVersion String M PHM桌面版本号：QueryLauncher返回的version，为时间戳。
                独立部署后在QueryPHMLauncherList返回version(已提需求)
        VSCIPv4         String O 认证APK返回的VSC IPv4 格式：IP:Port
        VSCIPv6         String O 认证APK返回的VSC IPv6 格式：[IP]:Port
        DesktopType     String M 界面类型，枚举值——0：常规 1：儿童 2：老年
        DownloadState   String O 下载结果:0：失败 1：成功
        DownloadSpan    String O 下载成功时实际时长，单位:秒
        AnalyseState    String O 解析结果：0：失败 1：成功
        LoadState       String M 加载结果：0：失败 1：成功
        LoadSpan        String O 加载成功时实际时长，单位:秒
        MulticastSwitchState String M 组播开关状态：0：关 1：开
        STBIPType       String M 机顶盒IP类型 0:IPV4 1:IPV6
        UserID          String M ZJLogin返回的ID
        STBID           String M 认证APK返回的STBID
    */
    public static Map<String,String> getLauncherData(
            String downloadState, //下载结果: 0：失败 1：成功
            String downloadTime,  //下载成功时实际时长，单位:秒
            String analyseState   //解析结果：0：失败 1：成功
    ){
        Map<String,String> data = new HashMap<>();
        data.put("EPGVersion", CommonUtil.getVersionName());
        data.put("UserGroup", SessionService.getInstance().getSession().getUserGroup());

        data.put("LauncherVersion", LauncherService.getInstance().getDesktopVersion());
        String ip   = AuthenticateManager.getInstance().getUserInfo().getIP();
        String port = AuthenticateManager.getInstance().getUserInfo().getPort();
        if(!TextUtils.isEmpty(ip) && ip.contains(":")){
            data.put("VSCIPv6",ip+":"+port);
            data.put("STBIPType","1");
        } else {
            data.put("VSCIPv4",ip+":"+port);
            data.put("STBIPType","0");
        }
        if (SharedPreferenceUtil.getInstance().getIsSimpleEpg()){
            data.put("DesktopType","2");
        }else if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
            data.put("DesktopType","1");
        }else{
            data.put("DesktopType","0");
        }
        data.put("DownloadState",downloadState);//下载结果:0：失败 1：成功
        data.put("DownloadSpan", downloadTime);//下载成功时实际时长，单位:秒
        data.put("AnalyseState", analyseState);//解析结果：0：失败 1：成功
        data.put("LoadState","1");             //加载结果：0：失败 1：成功
        data.put("LoadSpan", "1");             //加载成功时实际时长，单位:秒
        data.put("MulticastSwitchState", SharedPreferenceUtil.getInstance().getMulticastSwitch()?"1":"0");//组播开关状态：0：关 1：开

        //data.put("STBIPType",);//机顶盒IP类型 0:IPV4 1:IPV6
        //由接口统一处理，不需要单独添加
        //data.put("UserID", SessionService.getInstance().getSession().getUserId());//ZJLogin返回的ID
        data.put("STBID", CommonUtil.getSTBID());//认证APK返回的STBID

        return data;
    }

    public static String getDownloadState() {
        return downloadState;
    }

    public static void setDownloadState(String downloadState) {
        Desktop.downloadState = downloadState;
    }

    public static String getDownloadTime() {
        return String.valueOf((int)Math.ceil(((float)(endDownLoadTime-startDownLoadTime))/1000));
    }

    public static String getAnalyseState() {
        return analyseState;
    }

    public static void setAnalyseState(String analyseState) {
        Desktop.analyseState = analyseState;
    }

    public static void setStartDownLoadTime(long startDownLoadTime) {
        SuperLog.debug(PbsUaConstant.TAG,"startDownLoadTime = "+startDownLoadTime);
        Desktop.startDownLoadTime = startDownLoadTime;
    }

    public static void setEndDownLoadTime(long endDownLoadTime) {
        SuperLog.debug(PbsUaConstant.TAG,"endDownLoadTime = "+endDownLoadTime);
        Desktop.endDownLoadTime = endDownLoadTime;
    }

    /*详情页推荐曝光或点击
     * actionType    : [曝光]PbsUaConstant.ActionType.IMPRESSION  [点击]PbsUaConstant.ActionType.CLICK
     * contentId     : [曝光]当前浏览影片ID  [点击]用户点击影片ID
     * appointedId   : [相关推荐]relatedReco25 [猜你爱看]guessReco25
     * recommendType : [智能推荐]0 [人工推荐]1
     * sceneId       : [智能推荐]705猜你爱看 215相关推荐 [人工推荐]推荐栏目的ID
     * contentIds    : [曝光]推荐的影片ID列表，以逗号分隔  [点击]NA
     * identifyType  : [智能推荐]接口返回的identifyType值 [人工推荐]无此字段
     */
    public static Map<String,String> getRecommendData(
            String actionType,
            String contentId,
            String appointedId,
            String recommendType,
            String sceneId,
            String contentIds,
            String identifyType){
        Map<String,String> data = new HashMap<>();
        data.put("actionChannel", PbsUaConstant.ActionChannel.DESTOP);
        data.put("actionType", actionType);
        data.put("contentId",contentId);
        data.put("appointedId",appointedId);
        data.put("recommendType",recommendType);
        data.put("sceneId",sceneId);
        data.put("identifyType",identifyType);
        if(actionType.equals(PbsUaConstant.ActionType.IMPRESSION)){
            data.put("contentIds",contentIds);
        }
        return data;
    }
}
