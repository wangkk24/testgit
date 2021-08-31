package com.pukka.ydepg.common.utils;

import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.featured.bean.VodBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018\8\7 0007.
 */

public class ScoreControl {

    private static final String TAG = "ScoreControl";

    private static List<String> listNoShowIds;

    private static List<String> listNoShowTypes;

    private static List<String> getListNoShowIds() {
        if( listNoShowIds == null || listNoShowIds.isEmpty() ){
            try {
                return listNoShowIds = SessionService.getInstance().getSession().getTerminalConfigurationNOSCOREDISPLAYEDSUBJECTIDS();
            }catch (Exception e) {
                return null;
            }
        } else {
            return listNoShowIds;
        }
    }
    private static List<String> getlistNoShowTypes() {
        if( listNoShowTypes == null || listNoShowTypes.isEmpty() ){
            try {
                return listNoShowTypes = SessionService.getInstance().getSession().getTerminalConfigurationNOSCOREDISPLAYEDCMSTYPE();
            }catch (Exception e) {
                return null;
            }
        } else {
            return listNoShowTypes;
        }
    }

    public static boolean needShowScore(List<String> vodSubjectIds) {
        List<String> listNoShowIds = getListNoShowIds();
        if (null == vodSubjectIds || null == listNoShowIds) {
            return true;
        } else {
            for (String id : vodSubjectIds) {
                for (String showId : listNoShowIds) {
                    if (showId.equals(id)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    /*
    V2.1新需求，在原本判断是否显示评分的基础上，加上电影的过滤，电影显示，非电影不显示
     */
    public static boolean newNeedShowScore(VOD vod) {
        List<String> vodSubjectIds = vod.getSubjectIDs();
        List<String> listNoShowIds = getListNoShowIds();

        /*增加判断vod是不是电影的判断
              vodtype为0时为电影，显示，不为0时为非电影，不显示
             */

        String vodType = vod.getVODType();

        if (!TextUtils.isEmpty(vodType)){
            if (vodType.equals("0")){
                //vodtype为0,为电影，展示评分
                return true;
            }
        }

        if (null == vodSubjectIds || null == listNoShowIds) {
            return true;
        } else {
            for (String id : vodSubjectIds) {
                for (String showId : listNoShowIds) {
                    if (showId.equals(id)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public static boolean newNeedShowScore(VodBean vod) {
        List<String> vodSubjectIds = vod.getSubjectIds();
        List<String> listNoShowIds = getListNoShowIds();

        /*增加判断vod是不是电影的判断
              vodtype为0时为电影，显示，不为0时为非电影，不显示
             */
        String vodType = vod.getVODType();

        if (!TextUtils.isEmpty(vodType)){
            if (vodType.equals("0")){
                //vodtype为0,为电影，展示评分
                return true;
            }
        }

        if (null == vodSubjectIds || null == listNoShowIds) {
            return true;
        } else {
            for (String id : vodSubjectIds) {
                for (String showId : listNoShowIds) {
                    if (showId.equals(id)) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public static boolean newNeedShowScoreWithCMSType(VODDetail vod) {
        String cmsType = vod.getCmsType();
        List<String> listNoShowTypes = getlistNoShowTypes();


        //2.1补丁，去除电影类的判断
//        /*增加判断vod是不是电影的判断
//              vodtype为0时为电影，显示，不为0时为非电影，不显示
//             */
//        String vodType = vod.getVODType();
//
//        if (!TextUtils.isEmpty(vodType)){
//            if (vodType.equals("0")){
//                //vodtype为0,为电影，展示评分
//                return true;
//            }
//        }

        if (null == cmsType || cmsType.length() == 0 || null == listNoShowTypes) {
            return true;
        } else {
            for (String showType : listNoShowTypes) {
                if (cmsType.contains(showType)){
                    return false;
                }
            }

            return true;
        }
    }
}
