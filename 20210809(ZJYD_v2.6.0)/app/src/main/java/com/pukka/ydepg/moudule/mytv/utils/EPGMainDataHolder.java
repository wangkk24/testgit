package com.pukka.ydepg.moudule.mytv.utils;

import com.pukka.ydepg.common.http.v6bean.v6response.SuperScript;
import java.util.List;

public class EPGMainDataHolder {

    //角标信息集合
    public static List<SuperScript> superScript = null;

    public static List<SuperScript> getSuperScript() {
        return superScript;
    }

    public static void setSuperScript(List<SuperScript> superScript1) {
        superScript = superScript1;
    }
}
