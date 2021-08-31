package com.pukka.ydepg.launcher.util;

import android.text.TextUtils;

import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.utils.OTTFormat;

import java.util.Comparator;

/**
 * ElementList从小到大，根据left排序
 */

public class ComparatorElementLeft implements Comparator<Element> {
    @Override
    public int compare(Element element1, Element element2) {
        return ascendIndex(element1.getLeft(),element2.getLeft());
    }


    private int ascendIndex(String indexOne, String indexSecond)
    {
        if (TextUtils.isEmpty(indexOne) && TextUtils.isEmpty(indexSecond)) {
            return 0;
        }
        if (TextUtils.isEmpty(indexOne) && !TextUtils.isEmpty(indexSecond)) {
            return 1;
        }
        if (!TextUtils.isEmpty(indexOne) && TextUtils.isEmpty(indexSecond)) {
            return -1;
        }

        int index1 = OTTFormat.convertInt(indexOne);
        int index2 = OTTFormat.convertInt(indexSecond);
        if (index1 > index2)
        {
            return 1;
        }
        if (index1 < index2)
        {
            return -1;
        }
        return 0;
    }
}
