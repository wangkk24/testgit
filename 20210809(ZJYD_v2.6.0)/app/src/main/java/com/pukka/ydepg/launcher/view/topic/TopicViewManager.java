package com.pukka.ydepg.launcher.view.topic;

import android.content.Context;

import com.pukka.ydepg.common.report.ubd.scene.UBDSwitch;
import com.pukka.ydepg.launcher.bean.node.Topic;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.view.topic.TopicViewFactory.java
 * @date: 2018-03-13 22:56
 * @version: V1.0 描述当前版本功能
 */
public class TopicViewManager {
    private static final String TOPIC_STYLE_01 = "topicStyle01";
    private static final String TOPIC_STYLE_02 = "topicStyle02";
    private static final String TOPIC_STYLE_03 = "topicStyle03";
    private static final String TOPIC_STYLE_04 = "topicStyle04";

    public static BaseTopicStyleView getTopicView(String styleID, Topic topic, Context context) {
        UBDSwitch.getInstance().setTopicStyleId(styleID);
        switch (styleID) {
            case TOPIC_STYLE_01:
                return new TopicStyleOneView(context, topic);
            case TOPIC_STYLE_02:
                return new TopicStyleTwoView(context, topic);
            case TOPIC_STYLE_03:
                return new TopicStyleThreeView(context, topic);
            case TOPIC_STYLE_04:
                return new TopicStyleFourView(context, topic);
            default:
                return null;
        }
    }

    public static boolean isStyleIdValid(String styleID){
        if(styleID == null){
            return false;
        }
        switch (styleID) {
            case TOPIC_STYLE_01:
            case TOPIC_STYLE_02:
            case TOPIC_STYLE_03:
            case TOPIC_STYLE_04:
                return true;
            default:
                return false;
        }
    }
}
