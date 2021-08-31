package com.pukka.ydepg.common.utils;

import android.util.Log;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.launcher.session.SessionService;

import java.util.List;

public class SubscriptControl {
    /**
     * Created by Weicy on 2019\12\23
     */
    //用于控制播放详情页清晰度角标

    private static final String TAG = "SubscriptControl";

    //真4k标识
    public static final String ZJ4KLEVEL = "zj4KLevel";

    private static List<String> listShowIds;

    private static List<String> getListShowIds() {
        if( listShowIds == null || listShowIds.isEmpty() ){
            try {
                return listShowIds = SessionService.getInstance().getSession().getTerminalConfigurationVODDETAIL_DEFINITION_DISPLAY();
            }catch (Exception e) {
                return null;
            }
        } else {
            return listShowIds;
        }
    }

    //终端配置参数控制是否展示清晰度角标标识
    public static boolean showSubscript(List<VODMediaFile> vodMediaFileList,VODDetail vodDetail){
        //是否展示清晰度标识
        boolean showDefinition;
        //终端配置参数获取显示的标识类型
        List<String> listShowIds = getListShowIds();
        Log.i(TAG, "showSubscript:  "+ listShowIds);

        if (null == listShowIds || listShowIds.size() == 0){
            return false;
        }

        List<NamedParameter> listNp = vodDetail.getCustomFields();

        //获取真4k标识
        List<String> zj4kLevel = CommonUtil.getCustomNamedParameterByKey(listNp, ZJ4KLEVEL);

        if (null != zj4kLevel && zj4kLevel.size()>0 && (zj4kLevel.contains("S")||zj4kLevel.contains("A")||zj4kLevel.contains("B"))){
            showDefinition = false;
            for (int i = 0; i < zj4kLevel.size(); i++) {
                if (listShowIds.contains(zj4kLevel.get(i))){
                    showDefinition = true;
                }
            }
            return showDefinition;
        }

        //真4k标识不存在的时候判断definition字段

        if (listShowIds.contains(vodMediaFileList.get(0).getDefinition())){
            showDefinition = true;
        }else{
            showDefinition = false;
        }

        return showDefinition;
    }

    //扩展参数判断是否是真4K内容
    public static boolean iszj4KVOD(VODDetail vodDetail){
        List<NamedParameter> listNp = vodDetail.getCustomFields();
        //获取真4k标识
        List<String> zj4kLevel = CommonUtil.getCustomNamedParameterByKey(listNp, ZJ4KLEVEL);
        Log.i(TAG, "showSubscript:  " + zj4kLevel);
        if (null != zj4kLevel && zj4kLevel.size()>0){
            Log.i(TAG, "showSubscript:  " + zj4kLevel.get(0));
        }
        if (null != zj4kLevel && zj4kLevel.size()>0 && (zj4kLevel.contains("S")||zj4kLevel.contains("A")||zj4kLevel.contains("B"))){
            return true;
        }else{
            return false;
        }

    }

}
