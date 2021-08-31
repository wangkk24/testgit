package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xj on 2017/5/19.
 */

public class GlobalFilter {
    /**
     * 频道命名空间
     */
    @SerializedName("channelNamespace")
    private String channelNamespace;

    /**
     * 如果取值为空，表示definitions不进行过滤。
     0：SD
     1：HD
     2：4K
     */
    @SerializedName("definitions")
    private List<String> definitions;

    /**
     * 内容帧率，表示获取媒质帧率小于等于此帧率的内容。
     */
    @SerializedName("fps")
    private String fps;

    /**
     视频编码格式，取值包括：
     H.263
     H.264
     H.265
     如果取值为空，表示videoCodecs不进行过滤。
     */
    @SerializedName("videoCodecs")
    private List<String> videoCodecs;


      /**
       视频类型。
       取值范围：
       2：2D
       3：3D
       4： 2D VR
       5： 3D VR
       */
      @SerializedName("dimension")
    private List<String> dimension;

    public String getChannelNamespace() {
        return channelNamespace;
    }

    public List<String> getDefinitions() {
        return definitions;
    }

    public String getFps() {
        return fps;
    }

    public List<String> getVideoCodecs() {
        return videoCodecs;
    }

    public List<String> getDimension() {
        return dimension;
    }

    public void setChannelNamespace(String channelNamespace) {
        this.channelNamespace = channelNamespace;
    }

    public void setDefinitions(List<String> definitions) {
        this.definitions = definitions;
    }

    public void setFps(String fps) {
        this.fps = fps;
    }

    public void setVideoCodecs(List<String> videoCodecs) {
        this.videoCodecs = videoCodecs;
    }

    public void setDimension(List<String> dimension) {
        this.dimension = dimension;
    }
}
