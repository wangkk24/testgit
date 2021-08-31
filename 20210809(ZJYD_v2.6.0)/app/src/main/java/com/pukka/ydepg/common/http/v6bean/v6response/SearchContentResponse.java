package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public class SearchContentResponse extends BaseResponse{

    /**
     * result :
     * correction :
     * correctKey :
     * total :
     * contents :
     * extensionFields :
     */

    /**
     * 是否对关键词进行了纠错：
     * <p>
     * 1：纠错，使用纠错词汇搜索；
     * 0：未纠错，原词搜索。
     * 默认值为0
     */
    @SerializedName("correction")
    private String correction;

    /**
     * 纠错后关键词，当correction取值1时必填
     */
    @SerializedName("correctKey")
    private String correctKey;

    /**
     * 搜索记录总数。总数计算不准确时不返回该参数
     */
    @SerializedName("total")
    private String total;

    /**
     * 如果搜索成功，返回搜索内容。
     * <p>
     * 搜索请求参数contentTypes取值范围为：
     * <p>
     * VOD：点播内容，包括音频和视频内容
     * AUDIO_VOD：点播内容，仅限于音频内容
     * VIDEO_VOD：点播内容，仅限于视频内容
     * 对应返回Content中vod字段。
     * <p>
     * 搜索请求参数contentTypes取值范围为：
     * <p>
     * CHANNEL：直播频道
     * AUDIO_CHANNEL：直播频道，仅限于音频
     * VIDEO_CHANNEL：直播频道，仅限于视频
     * FILECAST_CHANNEL：[字段预留]
     * 对应返回Content中channel字段。
     * <p>
     * 搜索请求参数contentTypes取值范围为：
     * <p>
     * PROGRAM：正在播放和未开始的直播节目单
     * TVOD：录制成功的过期节目单
     * 对应返回Content中的playbill字段。
     * <p>
     * 搜索请求参数contentTypes取值范围为VAS对应返回Content中的vas字段
     */
    @SerializedName("contents")
    private List<Content> contents;

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getCorrection() {
        return correction;
    }

    public void setCorrection(String correction) {
        this.correction = correction;
    }

    public String getCorrectKey() {
        return correctKey;
    }

    public void setCorrectKey(String correctKey) {
        this.correctKey = correctKey;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
