package com.pukka.ydepg.moudule.search.utils;

import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;

import java.util.List;
import java.util.Map;

public class ViewPage2MainEpgUtil {

    //返回当前界面的静态数据，用于去重
    public static List<String> getPageStaticContentIdList(ReflectRelativeLayout rootView){
        PHMFragment mFragment = null;
           mFragment = rootView.getFragment();
        if (null != mFragment){
            return mFragment.getContentIdList();
        }else{
            return null;
        }
    }

    public static String getElementExtra(Element mElement,String key){
        Map<String, String> extraData = mElement.getExtraData();
        if (null != extraData){
            return extraData.get(key);
        }
        return null;
    }

    private static String appId = "recom001";

    public static void setId(String id){
        appId = id;
    }

    public static String getId(){
        return appId;
    }

}
