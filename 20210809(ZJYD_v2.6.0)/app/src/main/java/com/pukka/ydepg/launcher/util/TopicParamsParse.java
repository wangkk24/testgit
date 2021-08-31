package com.pukka.ydepg.launcher.util;

import android.text.TextUtils;

import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.bean.node.NamedParameter;

import java.util.List;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.util.ParseTopic.java
 * @date: 2018-03-13 21:54
 * @version: V1.0 描述当前版本功能
 */


public class TopicParamsParse {
    public static String getTopicParam(List<NamedParameter> parameters, String param) {
        if (!CollectionUtil.isEmpty(parameters)) {
            List<String> valueList;
            for (NamedParameter parameter : parameters) {
                if (param.equals(parameter.getKey())) {
                    valueList = parameter.getValueList();
                    if (!CollectionUtil.isEmpty(valueList)) {
                        for (String tmpValue : valueList) {
                            if (!TextUtils.isEmpty(tmpValue)) {
                                return tmpValue;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
