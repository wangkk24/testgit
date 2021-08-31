package com.pukka.ydepg.launcher.util;

import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.utils.OTTFormat;

import java.util.Comparator;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.util.ComparatorIndex.java
 * @date: 2018-02-26 18:12
 * @version: V1.0 描述当前版本功能
 */


public class ComparatorIndex implements Comparator<Element> {
    @Override
    public int compare(Element element1, Element element2) {
        return ascendIndex(element1.getIndex(),element2.getIndex());
    }


    private int ascendIndex(String indexOne, String indexSecond)
    {
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
