package com.pukka.ydepg.launcher.util;

import com.pukka.ydepg.R;
import com.pukka.ydepg.launcher.ui.template.TemplateType;

import java.util.HashMap;
import java.util.Map;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.moudule.home.util.TempletFactory.java
 * @date: 2018-01-22 9:46
 * @version: V1.0 描述当前版本功能
 */
public class TemplateFactory {
    public static Map<String, Integer> getsResourceIdMap() {
        return sResourceIdMap;
    }

    private static Map<String,Integer> sResourceIdMap = new HashMap<>();
    static {
        sResourceIdMap.put(TemplateType.ENTRANCE_﻿TEMPLATE_01, R.layout.pannel_subject_nav_14);
        sResourceIdMap.put(TemplateType.ENTRANCE_﻿TEMPLATE_02, R.layout.pannel_subject_nav_15m);
        sResourceIdMap.put(TemplateType.ENTRANCE_﻿TEMPLATE_03, R.layout.pannel_subject_nav_16m);
        sResourceIdMap.put(TemplateType.ENTRANCE_﻿TEMPLATE_04, R.layout.pannel_subject_nav_13);
        sResourceIdMap.put(TemplateType.ENTRANCE_﻿TEMPLATE_05, R.layout.pannel_cp_navigation_14);
        sResourceIdMap.put(TemplateType.ENTRANCE_﻿TEMPLATE_06, R.layout.pannel_cp_navigation_15);
        sResourceIdMap.put(TemplateType.ENTRANCE_﻿TEMPLATE_07, R.layout.pannel_cp_navigation_16);
        sResourceIdMap.put(TemplateType.MULTIPLEX_﻿TEMPLATE_01, R.layout.pannel_poster_16);
        sResourceIdMap.put(TemplateType.MULTIPLEX_﻿TEMPLATE_02, R.layout.pannel_poster_13m);
        sResourceIdMap.put(TemplateType.SELECTION_﻿TEMPLATE_01, R.layout.pannel_poster_old_12);
        sResourceIdMap.put(TemplateType.SELECTION_﻿TEMPLATE_02, R.layout.pannel_poster_13);
        sResourceIdMap.put(TemplateType.SELECTION_﻿TEMPLATE_03, R.layout.pannel_poster_1223);
        sResourceIdMap.put(TemplateType.RECOMMEND_﻿TEMPLATE_01, R.layout.pannel_poster_11);
        sResourceIdMap.put(TemplateType.RECOMMEND_﻿TEMPLATE_02, R.layout.pannel_poster_1521);
        sResourceIdMap.put(TemplateType.RECOMMEND_﻿TEMPLATE_03, R.layout.pannel_poster_14);
        sResourceIdMap.put(TemplateType.RECOMMEND_﻿TEMPLATE_04, R.layout.pannel_poster_1125);
        sResourceIdMap.put(TemplateType.RECOMMEND_﻿TEMPLATE_05, R.layout.pannel_poster_112135);
        sResourceIdMap.put(TemplateType.RECOMMEND_TEMPLATE_06, R.layout.pannel_poster_scroll_112135);
        sResourceIdMap.put(TemplateType.RECOMMEND_TEMPLATE_07, R.layout.pannel_poster_l1_r4);
        sResourceIdMap.put(TemplateType.RECOMMEND_TEMPLATE_08, R.layout.pannel_poster_scroll_11);
        sResourceIdMap.put(TemplateType.RECOMMEND_﻿TEMPLATE_09, R.layout.pannel_poster_1321);
        sResourceIdMap.put(TemplateType.RECOMMEND_﻿TEMPLATE_10, R.layout.pannel_poster_132434);
        sResourceIdMap.put(TemplateType.RECOMMEND_﻿TEMPLATE_11, R.layout.pannel_poster_112434);
        sResourceIdMap.put(TemplateType.RECOMMEND_﻿TEMPLATE_12, R.layout.pannel_poster_142235);
        sResourceIdMap.put(TemplateType.RECOMMEND_﻿TEMPLATE_13, R.layout.pannel_poster_132335);
        sResourceIdMap.put(TemplateType.COMBIN_﻿TEMPLATE_01, R.layout.pannel_poster_1326);
        sResourceIdMap.put(TemplateType.COMBIN_﻿TEMPLATE_02, R.layout.pannel_poster_112531);
        sResourceIdMap.put(TemplateType.COMBIN_﻿TEMPLATE_03, R.layout.pannel_poster_1123);
        sResourceIdMap.put(TemplateType.BOOKMARK_OR_FAVORITE,R.layout.pannel_poster_my_list);
        sResourceIdMap.put(TemplateType.MIX_VIDEO_TEMPLATE,R.layout.pannel_poster_12_video);

        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_01, R.layout.pannel_poster_1524);
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_02, R.layout.pannel_poster_1324);
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_03, R.layout.pannel_poster_0_12);
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_04, R.layout.pannel_poster_0_13);
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_05, R.layout.pannel_poster_0_12_half);
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_06, R.layout.pannel_poster_0_222_13);
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_07, R.layout.pannel_poster_213_13);
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_08, R.layout.pannel_poster_123_13);
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_09, R.layout.pannel_poster_1113_14);
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_10, R.layout.pannel_poster_1122_14);
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_11, R.layout.pannel_poster_321_13);//<-----
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_12, R.layout.pannel_poster_21111_15);
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_13, R.layout.pannel_poster_horizontal_16);//横向滑动一行六列正方形
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_14, R.layout.pannel_poster_horizontal_z_16);//横向滑动一行六列长方形
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_15, R.layout.pannel_poster_live_l1_r4);

        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_16, R.layout.pannel_poster_m_0_11);//
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_17, R.layout.pannel_poster_0_11);//窄海报
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_18, R.layout.pannel_poster_3111_14);//
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_19, R.layout.pannel_poster_11211_15);//
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_20, R.layout.pannel_poster_horizontal_nor_16);//正常横滑资源位264*360
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_21, R.layout.pannel_poster_horizontal_sm_16);//

        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_22, R.layout.pannel_poster_0_14);//
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_23, R.layout.pannel_poster_12);//
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_24, R.layout.pannel_poster_13);//
        sResourceIdMap.put(TemplateType.ENTRANCE_NOTE_﻿TEMPLATE_25, R.layout.pannel_poster_13_video);//

        //儿童版
        sResourceIdMap.put(TemplateType.CHILDREN_﻿TEMPLATE_01, R.layout.pannel_poster_0_1422);//
        sResourceIdMap.put(TemplateType.CHILDREN_﻿TEMPLATE_02, R.layout.pannel_poster_children_13);//
        sResourceIdMap.put(TemplateType.CHILDREN_﻿TEMPLATE_03, R.layout.pannel_poster_children_16m);//

        //2.1版本新增两个模板（热播排行榜/时间轴）
        sResourceIdMap.put(TemplateType.RECOMMEND_TEMPLATE_14, R.layout.pannel_poster_vertiacal_hot);//热播排行榜
        sResourceIdMap.put(TemplateType.RECOMMEND_TEMPLATE_15, R.layout.pannel_poster_horizontal_timeline_16);//时间轴

        //首页智能推荐模板
        sResourceIdMap.put(TemplateType.RECOMMEND_TEMPLATE_16, R.layout.pannel_poster_recommend_16);//一行六列
        sResourceIdMap.put(TemplateType.RECOMMEND_TEMPLATE_17, R.layout.pannel_poster_recommend_11);//专题推荐
        sResourceIdMap.put(TemplateType.RECOMMEND_TEMPLATE_18, R.layout.pannel_poster_recommend_0_14);//活动推荐

        sResourceIdMap.put(TemplateType.RECOMMEND_TEMPLATE_19, R.layout.pannel_poster_my_list_profile);//多Profile观看记录单独处理，此类型不作为新增模板

        //pannel_poster_024_142231_video、pannel_poster_024_1422_video、pannel_poster_024_13_video
        sResourceIdMap.put(TemplateType.RECOMMEND_TEMPLATE_20, R.layout.pannel_poster_024_142231_video);
        sResourceIdMap.put(TemplateType.RECOMMEND_TEMPLATE_21, R.layout.pannel_poster_024_1422_video);
        sResourceIdMap.put(TemplateType.RECOMMEND_TEMPLATE_22, R.layout.pannel_poster_024_13_video);
    }
}
