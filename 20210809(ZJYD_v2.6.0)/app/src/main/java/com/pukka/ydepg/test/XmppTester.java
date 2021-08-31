package com.pukka.ydepg.test;

import com.pukka.ydepg.common.pushmessage.presenter.PushMessagePresenter;
import com.pukka.ydepg.xmpp.XmppManager;

class XmppTester {
    static void sendPushMessage(){
        String body = "{\"mode\":\"5\",\"domain\":\"0\",\"type\":\"0\",\"content\":{\"Command\":\"appmessage\",\"actionType\":\"1\",\"extensionInfo\":\"{\\\"postURL\\\":\\\"%2fCPS%2fimages%2funiversal%2ffilm%2fposter%2f201911%2f20191129%2f20191129091603459nz4_op.png\\\",\\\"cancelBtnText\\\":\\\"1\\\",\\\"messageId\\\":\\\"Gift-20200426102200-001\\\",\\\"confirmBtnText\\\":\\\"1\\\",\\\"sendTime\\\":\\\"20200426102200\\\"}\",\"linkURL\":\"http%3A%2F%2Faikanvod.miguvideo.com%3A8858%2Fpvideo%2Fp%2FchannelMiddle.jsp%3FproductId%3D600000526400\",\"messageIntroduce\":\"您已订购电影VIP包和电视剧VIP包，升级到影视VIP包更优惠！升级后系统会自动为您退订电影VIP包和电视剧VIP包，如需升级请点击前往。（已升级请忽略）\",\"messageTitle\":\"友情提示（20200426）11\",\"pushType\":\"TOPIC\"},\"validTime\":\"20220426072014\"}";

        body = "{\"mode\":\"6\",\"domain\":\"0\",\"type\":\"0\",\"content\":\"22222222222222\",\"validTime\":\"20210903052926\"}";
        //body = "{\"mode\":\"5\",\"domain\":\"0\",\"type\":\"0\",\"content\":{\"Command\":\"appmessage\",\"actionType\":\"0\",\"extensionInfo\":\"{\\\"postURL\\\":\\\"%2fCPS%2fimages%2funiversal%2ffilm%2fposter%2f201911%2f20191129%2f20191129091603459nz4_op.png\\\",\\\"cancelBtnText\\\":\\\"1\\\",\\\"messageId\\\":\\\"Gift-20200426102200-001\\\",\\\"confirmBtnText\\\":\\\"1\\\",\\\"sendTime\\\":\\\"20201009091000\\\",\\\"messagetype\\\":\\\"1\\\",\\\"Messagelabel\\\":\\\"宽带电视全网用户\\\",\\\"location\\\":\\\"1\\\",\\\"opaque\\\":\\\"100\\\",\\\"theretime\\\":\\\"1\\\",\\\"messagenum\\\":\\\"4\\\",\\\"messageurl\\\":\\\"wwww.baidu.com\\\"}\",\"linkURL\":\"http%3A%2F%2Faikanvod.miguvideo.com%3A8858%2Fpvideo%2Fp%2FchannelMiddle.jsp%3FproductId%3D600000526400\",\"messageIntroduce\":\"pic您已订购电影VIP包和电视剧VIP包，升级到影视VIP包更优惠！升级后系统会自动为您退订电影VIP包和电视剧VIP包，如需升级请点击前往。（已升级请忽略）\",\"messageTitle\":\"友情提示（20200426）11\",\"pushType\":\"TOPIC\"},\"validTime\":\"20220426072014\"}";
        //XmppManager.handlePushMessage(body);
        PushMessagePresenter.pushMessage(body);
    }
}