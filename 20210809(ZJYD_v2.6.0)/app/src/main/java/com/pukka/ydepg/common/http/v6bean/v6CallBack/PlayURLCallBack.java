package com.pukka.ydepg.common.http.v6bean.v6CallBack;

import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: PlayURLCallBack.java
 * @author: yh
 * @date: 2017-04-25 16:53
 */

public interface PlayURLCallBack {

    /**
     * VOD鉴权回调
     *
     * @param url      播放地址 （鉴权成功返回）
     * @param bookmark 书签
     * @param productId 产品ID
     */
    void getVODPlayUrlSuccess(String url, String bookmark,String productId,String elapseTime);

    /**
     * 频道相关鉴权回调
     *
     * @param url             播放地址
     * @param attachedPlayURL 由于直播和网络时移业务，用户可以自由切换，所以为了提升用户体验，
     *                        当终端在请求直播或网络时移的播放地址时，平台可能返回网络时移或直播的播放地址，原则如下：
     *                        如果businessType是BTV且频道支持网络时移且用户可以使用网络时移业务，返回网络时移的播放地址。
     *                        如果businessType是PLTV且用户可以使用直播业务，返回直播的播放地址。
     * @param bookmark        书签
     */
    void getChannelPlayUrlSuccess(String url, String attachedPlayURL, String bookmark);

    void playFail();

    void playFail(PlayVODResponse playVODResponse);

    void onPlaycancel();

    void getVODDownloadUrlSuccess(String vodID, String url, String postURL, String switchNum, String name);

    void getVODDownloadUrlFailed(String vodID, String episodeID);

}
