package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: PhysicalChannel.java
 * @author: yh
 * @date: 2017-04-24 14:25
 */

public class PhysicalChannel  implements Serializable {


    /**
     * ID : 12
     * mediaName : 12
     * code : 12
     * videoCodec : 12
     * bitrate : 12
     * fps : 12
     * definition : 12
     * dimension : 12
     * formatOf3D : 12
     * channelEncrypt : 12
     * btvCR : 12
     * pltvCR : 12
     * cpltvCR : 12
     * cutvCR : 12
     * npvrRecordCR : 12
     * cpvrRecordCR : 12
     * irCR : 12
     * fileFormat : 12
     * isSupportPIP : 12
     * previewLength : 12
     * previewCount : 12
     * fccEnable : 12
     * fecEnable : 12
     * maxBitrate : 12
     * multiBitrates : 12
     * blockedNetworks : 12
     * customFields : 12
     * isVRContent
     * viewDegree
     */

    /**
     *媒体ID。

     */
    @SerializedName("ID")
    private String ID;

    /**
     *媒资名称。

     */
    @SerializedName("mediaName")
    private String mediaName;

    /**
     *第三方系统分配的媒资Code。

     */
    @SerializedName("code")
    private String code;

    /**
     *视频编码格式，取值包括：

     H.263
     H.264
     H.265
     */
    @SerializedName("videoCodec")
    private String videoCodec;

    /**
     *频道带宽。

     单位：kbps。

     */
    @SerializedName("bitrate")
    private String bitrate;


    /**
     *内容帧率，取值范围(0,256]。

     */
    @SerializedName("fps")
    private String fps;

    /**
     *高清标清标识。

     取值范围：

     0：SD
     1：HD
     2：4K
     */
    @SerializedName("definition")
    private String definition;

    /**
     *视频类型。取值范围：

     2：2D
     3：3D
     4： 2D VR
     5： 3D VR
     默认值为2。

     */
    @SerializedName("dimension")
    private String dimension;

    /**
     *3D格式，该字段仅对3D内容（即dimension参数值为3）有效。取值范围：

     1：side-by-side
     2：top-and-bottom
     默认值为1。

     */
    @SerializedName("formatOf3D")
    private String formatOf3D;

    /**
     *频道加密信息。

     */
    @SerializedName("channelEncrypt")
    private ChannelEncrypt channelEncrypt;

    /**
     *BTV业务的内容权限。

     */
    @SerializedName("btvCR")
    private ContentRight btvCR;

    /**
     *网络时移业务的内容权限。

     */
    @SerializedName("pltvCR")
    private ContentRight pltvCR;

    /**
     *本地时移业务的内容权限

     */
    @SerializedName("cpltvCR")
    private ContentRight cpltvCR;

    /**
     *CUTV业务的内容权限。

     */
    @SerializedName("cutvCR")
    private ContentRight cutvCR;

    /**
     *网络录制业务的录制权限。

     */
    @SerializedName("npvrRecordCR")
    private ContentRight npvrRecordCR;

    /**
     *本地录制业务的录制权限。

     */
    @SerializedName("cpvrRecordCR")
    private ContentRight cpvrRecordCR;

    /**
     *即时重播业务的内容权限

     */
    @SerializedName("irCR")
    private ContentRight irCR;

    /**
     *媒体文件的内容格式。

     取值范围：

     1：TS
     2：HLS
     3：HSS
     4：DASH
     5：MPEG DASH over Flute
     7：Flute
     8：DASH-M-ABR
     默认值为1。

     */
    @SerializedName("fileFormat")
    private String fileFormat;

    /**
     *是否支持PIP小流。

     针对IPTV频道，表示当前物理媒资为PIP流；
     针对OTT频道，表示取当前物理媒资的最低码率作为PIP流。
     */
    @SerializedName("isSupportPIP")
    private String isSupportPIP;

    /**
     *预览时长。

     如果不支持取值为0。

     单位：秒。

     */
    @SerializedName("previewLength")
    private String previewLength;

    /**
     *预览次数。

     如果不支持取值为0。

     */
    @SerializedName("previewCount")
    private String previewCount;

    /**
     *频道是否支持FCC和RET。

     取值范围：

     0：不支持FCC和RET
     1：支持FCC和RET
     2：仅支持FCC
     3：仅支持RET

     */
    @SerializedName("fccEnable")
    private String fccEnable;

    /**
     *频道是否支持FEC属性，取值包括：

     0：不支持
     1：支持
     */
    @SerializedName("fecEnable")
    private String fecEnable;

    /**
     *频道对应的峰值码率，只有频道（Capped VBR、DT Capped VBR）才有峰值码率。

     */
    @SerializedName("maxBitrate")
    private String maxBitrate;

    /**
     *对于OTT多码率媒资，返回多个码率信息。

     码率单位为kbps，同时多个码率会按照码率从大到小排序，例如一个媒资有10M、9.5M、9M、8M四种码率，则multiBitrates[]数字的取值顺序为：10000、9500、9000、8000。

     */
    @SerializedName("multiBitrates")
    private List<String> multiBitrates;

    /**
     *不允许访问的网络类型，取值包括：

     1：wlan网络
     2：cellular网络
     如果没有值，表示wlan和cellular网络都可以访问。

     */
    @SerializedName("blockedNetworks")
    private List<String> blockedNetworks;

    /**
     *频道媒资的扩展属性，其中扩展属性的Key由局点CMS定制
     */
    @SerializedName("customFields")
    private List<NamedParameter> customFields;

    @SerializedName("isVRContent")
    private String isVRContent;

    @SerializedName("viewDegree")
    private String viewDegree;

    public String getViewDegree() {
        return viewDegree;
    }

    public void setViewDegree(String viewDegree) {
        this.viewDegree = viewDegree;
    }

    public String getIsVRContent() {
        return isVRContent;
    }

    public void setIsVRContent(String isVRContent) {
        this.isVRContent = isVRContent;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    public void setVideoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public String getFps() {
        return fps;
    }

    public void setFps(String fps) {
        this.fps = fps;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getFormatOf3D() {
        return formatOf3D;
    }

    public void setFormatOf3D(String formatOf3D) {
        this.formatOf3D = formatOf3D;
    }

    public ChannelEncrypt getChannelEncrypt() {
        return channelEncrypt;
    }

    public void setChannelEncrypt(ChannelEncrypt channelEncrypt) {
        this.channelEncrypt = channelEncrypt;
    }

    public ContentRight getBtvCR() {
        return btvCR;
    }

    public void setBtvCR(ContentRight btvCR) {
        this.btvCR = btvCR;
    }

    public ContentRight getPltvCR() {
        return pltvCR;
    }

    public void setPltvCR(ContentRight pltvCR) {
        this.pltvCR = pltvCR;
    }

    public ContentRight getCpltvCR() {
        return cpltvCR;
    }

    public void setCpltvCR(ContentRight cpltvCR) {
        this.cpltvCR = cpltvCR;
    }

    public ContentRight getCutvCR() {
        return cutvCR;
    }

    public void setCutvCR(ContentRight cutvCR) {
        this.cutvCR = cutvCR;
    }

    public ContentRight getNpvrRecordCR() {
        return npvrRecordCR;
    }

    public void setNpvrRecordCR(ContentRight npvrRecordCR) {
        this.npvrRecordCR = npvrRecordCR;
    }

    public ContentRight getCpvrRecordCR() {
        return cpvrRecordCR;
    }

    public void setCpvrRecordCR(ContentRight cpvrRecordCR) {
        this.cpvrRecordCR = cpvrRecordCR;
    }

    public ContentRight getIrCR() {
        return irCR;
    }

    public void setIrCR(ContentRight irCR) {
        this.irCR = irCR;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getIsSupportPIP() {
        return isSupportPIP;
    }

    public void setIsSupportPIP(String isSupportPIP) {
        this.isSupportPIP = isSupportPIP;
    }

    public String getPreviewLength() {
        return previewLength;
    }

    public void setPreviewLength(String previewLength) {
        this.previewLength = previewLength;
    }

    public String getPreviewCount() {
        return previewCount;
    }

    public void setPreviewCount(String previewCount) {
        this.previewCount = previewCount;
    }

    public String getFccEnable() {
        return fccEnable;
    }

    public void setFccEnable(String fccEnable) {
        this.fccEnable = fccEnable;
    }

    public String getFecEnable() {
        return fecEnable;
    }

    public void setFecEnable(String fecEnable) {
        this.fecEnable = fecEnable;
    }

    public String getMaxBitrate() {
        return maxBitrate;
    }

    public void setMaxBitrate(String maxBitrate) {
        this.maxBitrate = maxBitrate;
    }

    public List<String> getMultiBitrates() {
        return multiBitrates;
    }

    public void setMultiBitrates(List<String> multiBitrates) {
        this.multiBitrates = multiBitrates;
    }

    public List<String> getBlockedNetworks() {
        return blockedNetworks;
    }

    public void setBlockedNetworks(List<String> blockedNetworks) {
        this.blockedNetworks = blockedNetworks;
    }

    public List<NamedParameter> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<NamedParameter> customFields) {
        this.customFields = customFields;
    }
}
