package com.pukka.ydepg.common.utils;

import android.util.Log;

import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.featured.bean.VodBean;
import com.pukka.ydepg.moudule.mytv.CollectionListFragment;

import java.util.List;

public class IsTV {

    private static List<String> valueStrs;

    public static boolean isTV(VOD vod) {
        if (hasLowCustomField(vod, Constant.PLAY_FROM_TERMINAL)) {
            if (null != valueStrs && valueStrs.contains("1")) {
                return true;
            } else{
                return false;
            }
        }
        return false;
//        else {
//            return isTvByMiguId(vod.getCpId());
//        }
    }

    public static boolean isMigu(VOD vod){
        if (hasLowCustomField(vod, Constant.PLAY_FROM_TERMINAL)) {
            if(null != valueStrs && valueStrs.contains("0")) {
                return true;
            }  else {
                return false;
            }
        } else {
            return VodUtil.isMiguVod(vod.getCpId());
        }
    }

    public static boolean isTVFar(VOD vod) {
        if (hasLowCustomFieldFar(vod, Constant.PLAY_FROM_TERMINAL)) {
            if (null != valueStrs && valueStrs.contains("1")) {
                return true;
            } else{
                return false;
            }
        }
        return false;
//        else {
//            return isTvByMiguId(vod.getCpId());
//        }
    }

    public static boolean isMiguFar(VOD vod){
        if (hasLowCustomFieldFar(vod, Constant.PLAY_FROM_TERMINAL)) {
            if(null != valueStrs && valueStrs.contains("0")) {
                return true;
            }  else {
                return false;
            }
        } else {
            return VodUtil.isMiguVod(vod.getCpId());
        }
    }

    //判断收藏记录是否是VR的 不是VR返回true,是VR返回false
    public static boolean isNotVRFar(VOD vod){
        List<NamedParameter> parameterList = null;
        if (null != vod.getFavorite()){
            parameterList = vod.getFavorite().getCustomFields();
        }
        if (null != parameterList){
            List<String> terminal = CommonUtil.getCustomNamedParameterByKey(parameterList, CollectionListFragment.PLAY_FROM_TERMINAL);
            if (null != terminal && terminal.size()>0){
                String teminalStr = terminal.get(0);
                if (teminalStr.equals("0") ||teminalStr.equals("1")){
                    return true;
                }
            }else{
                //没有扩展参数的时候看配置参数，包含cpID的话不是VR
                return VodUtil.isMiguVod(vod.getCpId());
            }
            return false;
        }else{
            //没有扩展参数的时候看配置参数，包含cpID的话不是VR
            return VodUtil.isMiguVod(vod.getCpId());
        }
    }
    //判断播放记录是否是VR的 不是VR返回true,是VR返回false
    public static boolean isNotVRBM(VOD vod){

        if (null == vod || null == vod.getBookmark()){
            return true;
        }

        List<NamedParameter> parameterList = vod.getBookmark().getCustomFields();
        if (null != parameterList){
            List<String> terminal = CommonUtil.getCustomNamedParameterByKey(parameterList, CollectionListFragment.PLAY_FROM_TERMINAL);
            if (null != terminal && terminal.size()>0){
                String teminalStr = terminal.get(0);
                if (teminalStr.equals("0") ||teminalStr.equals("1")){
                    return true;
                }
            }else{
                //没有扩展参数的时候看配置参数，包含cpID的话不是VR
                return VodUtil.isMiguVod(vod.getCpId());
            }
            return false;
        }else{
            //没有扩展参数的时候看配置参数，包含cpID的话不是VR
            return VodUtil.isMiguVod(vod.getCpId());
        }

    }

    public static boolean isNotVRBMForVODBean(VodBean vod){
        if (null == vod || null == vod.getBookmark()){
            return true;
        }
        List<NamedParameter> parameterList = vod.getBookmark().getCustomFields();
        if (null != parameterList){
            List<String> terminal = CommonUtil.getCustomNamedParameterByKey(parameterList, CollectionListFragment.PLAY_FROM_TERMINAL);
            if (null != terminal && terminal.size()>0){
                String teminalStr = terminal.get(0);
                if (teminalStr.equals("0") ||teminalStr.equals("1")){
                    return true;
                }
            }else{
                //没有扩展参数的时候看配置参数，包含cpID的话不是VR
                return VodUtil.isMiguVod(vod.getCpId());
            }
            return false;
        }else{
            //没有扩展参数的时候看配置参数，包含cpID的话不是VR
            return VodUtil.isMiguVod(vod.getCpId());
        }

    }




    public static boolean isChildMode(VOD vod){
      return  hasCustomField(vod,Constant.BOOKMARK_CHILD_MODE)&&null != valueStrs&&valueStrs.contains("1");
    }

    private static boolean hasCustomField(VOD vod,String key) {
        if (null == vod.getBookmark()) {
            return false;
        }

        List<NamedParameter> parameterList = vod.getBookmark().getCustomFields();
        if (null == parameterList) {
            return false;
        }

        for (NamedParameter parameter : parameterList) {
            if (parameter.getKey().equals(key)) {
                valueStrs = parameter.getValues();
                return true;
            }
        }
        return false;
    }

    private static boolean hasLowCustomField(VOD vod,String key) {
        if (null == vod.getBookmark()) {
            return false;
        }

        List<NamedParameter> parameterList = vod.getBookmark().getCustomFields();
        if (null == parameterList) {
            return false;
        }

        for (NamedParameter parameter : parameterList) {
            if (parameter.getKey().toLowerCase().equals(key)) {
                valueStrs = parameter.getValues();
                return true;
            }
        }
        return false;
    }

    private static boolean hasLowCustomFieldFar(VOD vod,String key) {
        if (null == vod.getFavorite()) {
            return false;
        }

        List<NamedParameter> parameterList = vod.getFavorite().getCustomFields();
        if (null == parameterList) {
            return false;
        }

        for (NamedParameter parameter : parameterList) {
            if (parameter.getKey().toLowerCase().equals(key)) {
                valueStrs = parameter.getValues();
                return true;
            }
        }
        return false;
    }


    private static boolean isTvByMiguId(String cpId){
        if (VodUtil.isMiguVod(cpId)) {
            return false;
        } else {
            return true;
        }
    }
}

