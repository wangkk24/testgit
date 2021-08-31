package com.pukka.ydepg.moudule.vod.utils;

import android.text.TextUtils;

import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.session.SessionService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ShowCastListUtils {
    private static final String TAG = "ShowCastListUtils";

    private final static String SWICTH = "switch";
    private final static String LIST = "list";

    //是否展示演职员列表
    public static boolean showCastList(VODDetail vodDetail){
        //终端参数不配置或者配置错误时，默认展示
        if (!configExist()){
            return true;
        }
        //判断终端参数
        Map<String,String> ShowCastMap = SessionService.getInstance().getSession().getTerminalConfigurationNotShowCastListCmsType();
        boolean isOpen = isOpen(ShowCastMap);

        if (isOpen){
            //开关为打开时

            //影片没有cmsType时
            if (null == vodDetail || TextUtils.isEmpty(vodDetail.getCmsType())){
                return false;
            }

            List<String> list = getCastList(ShowCastMap);
            if (null != list && list.size()>0){
                for (int i = 0; i < list.size(); i++) {
                    String str = list.get(i);
                    if (!"|".equals(str) && vodDetail.getCmsType().contains(str)){
                        return true;
                    }
                }
                return false;
            }else{
                return false;
            }
        }else{
            //开关为关闭

            //影片没有cmsType时
            if (null == vodDetail || TextUtils.isEmpty(vodDetail.getCmsType())){
                return true;
            }

            List<String> list = getCastList(ShowCastMap);
            if (null != list && list.size()>0){
                for (int i = 0; i < list.size(); i++) {
                    String str = list.get(i);
                    if (!"|".equals(str) && vodDetail.getCmsType().contains(str)){
                        return false;
                    }
                }
                return true;
            }else{
               return true;
            }
        }

    }

    //获取开关状态
    private static boolean isOpen(Map<String,String> map){
        //获取开关是否打开
        String isOpenStr = map.get(SWICTH);
        if (TextUtils.isEmpty(isOpenStr)){
            //为空默认开关关闭
            return false;
        }else{
            if ("0".equals(isOpenStr)){
                return false;
            }else{
                return true;
            }
        }
    }

    //获取类别列表
    private static List<String> getCastList(Map<String,String> map){
        String listStr = map.get(LIST);
        if (TextUtils.isEmpty(listStr)){
            return null;
        }else{
            List<String> list = null;
            try {
                list = Arrays.asList(listStr.split("\\|"));
            }catch (Exception e){
                SuperLog.error(TAG,e);
            }
            return list;
        }
    }

    //终端参数是否配置
    public static boolean configExist(){
        Map<String,String> ShowCastMap = SessionService.getInstance().getSession().getTerminalConfigurationNotShowCastListCmsType();
        if (null == ShowCastMap){
            return false;
        }

        if (TextUtils.isEmpty(ShowCastMap.get(LIST)) && TextUtils.isEmpty(ShowCastMap.get(SWICTH))){
            return false;
        }

        return true;
    }






}
