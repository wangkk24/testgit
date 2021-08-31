package com.pukka.ydepg.common.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.profile.adapter.ConvertDataFromPbsToEpg;
import com.pukka.ydepg.common.profile.data.ProfileCustomization;
import com.pukka.ydepg.common.profile.data.ProfileStatus;
import com.pukka.ydepg.common.utils.GlideUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.ui.fragment.WebActivity;
import com.pukka.ydepg.moudule.featured.view.LauncherService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProfileManager {

    public final static String TAG = ProfileManager.class.getSimpleName();

    //开机管理Profile页面 profile状态接口返回1/3时  modified by liuxia at 20201106 as chengqiao's request
    public static final String URL_PROFILE_STARTUP = "http://aikanvod.miguvideo.com:8858/pvideo/p/AC_switchkinLogin.jsp";

    //开机管理Profile页面 profile状态接口返回2时
    public static final String URL_PROFILE_MANAGE  = "http://aikanvod.miguvideo.com:8858/pvideo/p/AC_kinshipaccount.jsp";

    //切换Profile页面,点击profile名时
    public static final String URL_PROFILE_SELECT  = "http://aikanvod.miguvideo.com:8858/pvideo/p/AC_switchkinshipaccount.jsp";

    //更新Profile昵称页面,点击切换profile时
    public static final String URL_PROFILE_MODIFY  = "http://aikanvod.miguvideo.com:8858/pvideo/p/AC_kinshipaChangeName.jsp";

    //获取开机时PROFILE展示状态
    private static final String URL_GET_PROFILE_STATUS = "http://aikanvod.miguvideo.com:8858/pvideo/p/i_kinshipaccountLogin.jsp?user=guest&vt=9";

    //家庭多帐号自动创建子帐号
    private static final String URL_ADD_SUB_PROFILE = "http://aikanvod.miguvideo.com:8858/pvideo/p/i_AddkinshiAuto.jsp?user=guest&vt=9";

    //获取对应Profile的推荐(爱看栏目)页面数据
    private static final String URL_GET_PROFILE_CUSTOM_DATA = "http://aikanvod.miguvideo.com:8858/pvideo/p/stb_profile.jsp?nodeId=catauto2000149345&vt=9";
    //private static final String URL_GET_PROFILE_CUSTOM_DATA = "http://aikanvod.miguvideo.com:8858/pvideo/t/stb_profile.jsp?nodeId=catauto2000149345&vt=9";

    public static final int PROFILE_REQUEST_CODE   = 100;

    public static final int PROFILE_RESULT_CODE    = 102;

    private static ProfileStatus profileInfo = new ProfileStatus("7");

    //Profile相关操作之前时Profile的ID(子/主),用于进行Profile相关操作之后判断Profile类型是否发生了改变
    private static String lastProfileID = null;

    //是否切换账号,切换账号后重新允许查询Profile
    public static boolean isChangeUser = true;

    //判断开机Profile流程是否执行过,用于保证此段逻辑只执行一遍
    public static boolean isProfileFinished = false;


    public static ProfileStatus getProfileInfo() {
        return profileInfo;
    }

    public static void startProfilePage(Context context, String url, boolean forResult){
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        if(forResult){
            ((Activity)context).startActivityForResult(intent,PROFILE_REQUEST_CODE);
        } else {
            context.startActivity(intent);
        }
    }

    public static boolean isProfileChange(){
        boolean result;
        if(TextUtils.isEmpty(lastProfileID)){
            //上次操作profileID为空,为首次登陆操作,认为发生Profile切换
            result = true;
        } else {
            result = !lastProfileID.equals(ProfileManager.getProfileInfo().getProfileId());
        }
        lastProfileID = ProfileManager.getProfileInfo().getProfileId();
        SuperLog.info2SD(TAG,"Profile has changed = " + result);
        return result;
    }

    @SuppressWarnings("CheckResult")
    private static void getSubProfileRecommendData(LauncherService.OnQueryLauncherListener listener,boolean isFresh){
        HttpApi.getInstance().getService().sendGetRequest(URL_GET_PROFILE_CUSTOM_DATA)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())//最后须回调主页面,因此切换到主线程,后续可优化为convertDataFromPbsToEpg中listener.onVersionChange为主线程调用即可
            .subscribe(
                responseBody -> {
                    SuperLog.info2SD(TAG, "GetSubProfileRecommendData successfully.");
                    String response = responseBody.string();
                    ProfileCustomization profileCustomization = JsonParse.json2Object(response, ProfileCustomization.class);
                    if (profileCustomization == null) {
                        //如果子profile无爱看页面，将上一个子profile返回的爱看页面移除
                        ConvertDataFromPbsToEpg.convertDataFromPbsToEpg(null,listener,isFresh);
                        SuperLog.error(TAG,"GetSubProfileRecommendData failed");
                    } else {
                        ConvertDataFromPbsToEpg.convertDataFromPbsToEpg(profileCustomization,listener,isFresh);
                    }
                },
                throwable -> {
                    //下载升级apk失败,升级结束
                    SuperLog.error(TAG,throwable);
                    SuperLog.error(TAG,"GetSubProfileRecommendData exception");
                    ConvertDataFromPbsToEpg.convertDataFromPbsToEpg(null,listener,isFresh);
                }
            );
    }


    //result取值为1————配置显示+存在子账号           进入H5(新)切换账号界面   展示Profile信息UI
    //result取值为2————配置显示+不存在子账号          进入H5家庭成员管理界面   展示Profile信息UI
    //result取值为3————配置不显示+存在子账号          进入H5(新)切换账号界面   展示Profile信息UI
    //result取值为4————配置不显示+不存在子账号        直接进入精选页面         展示Profile信息UI
    //result取值为5————停用Profile功能(PBS返回)     直接进入精选页面         不展示Profile信息UI
    //其他值———————————停用Profile功能(EPG设置)     直接进入精选页面         不展示Profile信息UI
    //context对象必须为MainActivity
    private static void startProfile(ProfileStatus status,Context context){
        SuperLog.info2SD(TAG,"QueryProfile status = " + status.getResult());
        profileInfo.setResult(status.getResult());
        switch (status.getResult()){
            case "1":
            case "3":
                SuperLog.info2SD(TAG,"Status=1/3, show Profile [Select] Page.");
                startProfilePage(context,URL_PROFILE_STARTUP,true);
                break;
            case "2":
                SuperLog.info2SD(TAG,"Status=2, show Profile [Manager] Page.");
                startProfilePage(context,URL_PROFILE_MANAGE,true);
                break;
                //1/2/3时启动WebActivity页面,页面关闭后会调用onActivityResult,进而调用onProfileUIFinished()保证流程同步化
            case "4":
                SuperLog.info2SD(TAG,"Status=4, no need to show Profile Page.");
                profileInfo.setProfileId(status.getProfileId());
                profileInfo.setProfileType(status.getProfileType());
                profileInfo.setProfileName(status.getProfileName());
                profileInfo.setIcon(status.getIcon());
                //此分支没有打开Profile的PBS页面,因此不会回调MainActivity的onActivityResult方法,因此需要手动触发一下,更新页面相关Profile信息
                ((MainActivity) context).dismissProfilePage();
                //4时在dismissProfilePage()中会调用onActivityResult,进而调用onProfileUIFinished()保证流程同步化
                break;
            case "5":
                //不启用Profile
                SuperLog.info2SD(TAG,"Status=5, close Profile function.");
                //统一Profile功能出口,使开机流程同步化
                ((MainActivity) context).onProfileUIFinished(false);
                break;
            default:
                //不启用Profile
                SuperLog.error(TAG,"Unknown QueryProfile status, close Profile function.");
                //统一Profile功能出口,使开机流程同步化
                ((MainActivity) context).onProfileUIFinished(false);
                break;
        }
    }

    //获取子Profile的爱看栏目页面数据
    public static void getSubProfileSubjectData(LauncherService.OnQueryLauncherListener listener,boolean isFresh){
        // 判断条件 Profile发生了改变 且 当前Profile是子Profile
        if(ProfileManager.isProfileChange()) {
            ProfileManager.getSubProfileRecommendData(listener,isFresh);
        } else {
            SuperLog.info2SD(TAG,"Profile does not change. No need to query Aikan subject data.");
        }
    }

    public static void getRefreshProfiljectData(LauncherService.OnQueryLauncherListener listener,boolean isFresh){
        // 判断条件 Profile发生了改变 且 当前Profile是子Profile
        if (isFresh){
            ProfileManager.getSubProfileRecommendData(listener,isFresh);
        }
    }

    @SuppressWarnings("CheckResult")
    //开机时向PBS请求Profile状态,根据此状态走对应的Profile展示流程
    //context对象必须为MainActivity
    public static void getStartupProfileStatus(Context context){
        isChangeUser = false;
        HttpApi.getInstance().getService().sendGetRequest(URL_GET_PROFILE_STATUS)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())//主线程
            .subscribe(
                responseBody -> {
                    SuperLog.info2SD(TAG, "Get profile status from PBS successfully.");
                    String response = responseBody.string();
                    ProfileStatus profileStatus = JsonParse.json2Object(response,ProfileStatus.class);
                    if(profileStatus == null || TextUtils.isEmpty(profileStatus.getResult())){
                        SuperLog.error(TAG,"Get Profile status failed, set status=6");
                        profileStatus = new ProfileStatus("6");
                    }
                    ProfileManager.startProfile(profileStatus,context);
                },
                throwable -> {
                    //请求Profile状态失败,关闭Profile相关功能
                    SuperLog.error(TAG,throwable);
                    SuperLog.error(TAG,"QueryProfile exception, set status=7");
                    ProfileStatus profileStatus = new ProfileStatus("7");
                    ProfileManager.startProfile(profileStatus,context);
                }
            );

        //(20201028)浙江移动手机视频项目FRS 开机自动创建子帐号的需求
        HttpApi.getInstance().getService().sendGetRequest(URL_ADD_SUB_PROFILE)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())//子线程
                .subscribe(
                        responseBody -> {
                            SuperLog.info2SD(TAG,"Send add sub profile request successfully.");
                        },
                        throwable -> {
                            SuperLog.error(TAG,throwable);
                        }
                );;
    }

    //更新Profile昵称
    public static void updateProfileAlias(TextView textView, ImageView iconView){
        textView.setText(ProfileManager.getProfileInfo().getProfileName());
        GlideUtil.load(OTTApplication.getContext().getMainActivity(),ProfileManager.getProfileInfo().getIcon(),iconView,-1);
    }

    public static void onProfileUpdate(String id,String type,String alias,String headIcon) {
        SuperLog.info2SD(ProfileManager.TAG,"Profile info changed, detail info is as followed:>>>" +
                "\n\ttype = " + type  + "(0:Main 1:Sub)" +
                "\n\tname = " + alias +
                "\n\tid   = " + id    +
                "\n\ticon = " + headIcon);
        ProfileManager.getProfileInfo().setProfileId(id);
        ProfileManager.getProfileInfo().setProfileType(type);
        ProfileManager.getProfileInfo().setProfileName(alias);
        ProfileManager.getProfileInfo().setIcon(headIcon);
    }

    //判断当前栏目是否是爱看栏目
    public static boolean isAiKanFragment(){
        if(!SharedPreferenceUtil.getInstance().getIsSimpleEpg()
                &&!SharedPreferenceUtil.getInstance().getIsChildrenEpg()
                && OTTApplication.getContext().getMainActivity().getCurrentPosition() >= 0
                && OTTApplication.getContext().getMainActivity().getCurrentPosition() < LauncherService.getInstance().getLauncher().getNavigateList().size()
                && ConvertDataFromPbsToEpg.AIKAN_NAV_ID.equals(LauncherService.getInstance().getLauncher().getNavigateList().get(OTTApplication.getContext().getMainActivity().getCurrentPosition()).getId())){
            return true;
        } else {
            return false;
        }
    }
}