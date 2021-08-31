package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: VODMediaFile.java
 * @author: yh
 * @date: 2017-04-21 17:00
 */

public class VODMediaFile implements Serializable{


    /**
     * ID :
     * elapseTime : 11
     * bitrate : 11
     * isDownload : 11
     * definition : 11
     * HDCPEnable : 11
     * macrovision : 11
     * dimension : 11
     * formatOf3D :
     * supportTerminals : [""]
     * fileFormat : 11
     * encrypt : 11
     * CGMSA : 11
     * analogOutputEnable : 11
     * videoCodec :
     * customFields : ["NamedParameter"]
     * audioType :
     * code :
     * multiBitrates : [""]
     * fps : 111
     * maxBitrate : 11
     * picture : Picture
     * preview : 22
     * previewStartTime :
     * previewEndTime :
     * extensionFields : [""]
     */

    /**
     *媒体文件的ID。

     客户端获取媒体文件的播放地址的时候需要用到该参数。

     */
    @SerializedName("ID")
    private String ID;

    /**
     *媒体播放时长。

     单位：秒。

     */
    @SerializedName("elapseTime")
    private String elapseTime;

    /**
     *媒体文件码率。

     单位：kbit/s。

     */
    @SerializedName("bitrate")
    private String bitrate;

    /**
     *媒体是否支持下载。

     取值范围：

     0：不支持
     1：支持
     默认值为0。

     */
    @SerializedName("isDownload")
    private String isDownload;

    /**
     *高清标清标识，取值范围：

     0：SD
     1：HD
     2：4K
     */
    @SerializedName("definition")
    private String definition;

    /**
     *点播媒体文件是否支持HDCP。

     取值范围：

     0：不支持
     1：支持
     默认值为0。

     */
    @SerializedName("HDCPEnable")
    private String HDCPEnable;

    /**
     *点播内容的Macrovision属性。

     取值范围：

     0：Macrovision off
     1：AGC
     2：AGC + 2-stripe
     3：AGC + 4-stripe
     默认值为0

     */
    @SerializedName("macrovision")
    private String macrovision;

    /**
     *视频类型。

     取值范围：

     2：2D
     3：3D
     4： 2D VR
     5： 3D VR
     默认值为2。

     */
    @SerializedName("dimension")
    private String dimension;

    /**
     *3D格式，该字段仅对3D内容（即dimension参数值为3）有效。

     取值范围：

     1：side-by-side
     2：top-and-bottom
     默认值为1。

     */
    @SerializedName("formatOf3D")
    private String formatOf3D;

    /**
     *媒体文件的内容格式。

     取值范围：

     1：TS
     2：HLS
     3：HSS
     4：DASH
     默认值为1。

     */
    @SerializedName("fileFormat")
    private String fileFormat;

    /**
     *是否CA加密。

     取值范围：

     0：未加密
     1：加密
     */
    @SerializedName("encrypt")
    private String encrypt;

    /**
     *内容CGMS-A属性。

     取值范围：

     0：Copy Freely
     1：Copy No More
     2：Copy Once
     3：CopyNever
     */
    @SerializedName("CGMSA")
    private String CGMSA;

    /**
     *是否支持模拟端口输出。

     0：不支持
     1：支持
     */
    @SerializedName("analogOutputEnable")
    private String analogOutputEnable;

    /**
     *视频编码格式，取值包括：

     H.263
     H.264
     H.265
     */
    @SerializedName("videoCodec")
    private String videoCodec;

    /**
     *标识该媒资的声场技术，常见的声场技术有Dolby ProLogic，Dolby Digital，Stereo，Mono
     */
    @SerializedName("audioType")
    private String audioType;

    /**
     *第三方系统分配的媒资Code。

     */
    @SerializedName("code")
    private String code;

    /**
     *内容帧率，取值范围(0,256]。

     */
    @SerializedName("fps")
    private String fps;

    /**
     *点播对应的峰值码率，只有VBR的点播才有峰值码率。

     */
    @SerializedName("maxBitrate")
    private String maxBitrate;

    /**
     *海报路径。

     说明：
     目前只有片花有海报，正片不支持。

     */
    @SerializedName("picture")
    private Picture picture;

    /**
     *可否预览，取值包括：

     0：否
     1：是
     */
    @SerializedName("preview")
    private String preview;

    /**
     *如果支持预览，返回预览开始时间，相对于节目开始的时间，格式为HHmmss。

     */
    @SerializedName("previewStartTime")
    private String previewStartTime;

    /**
     *如果支持预览，返回预览结束时间，相对于节目开始的时间，格式为HHmmss。

     */
    @SerializedName("previewEndTime")
    private String previewEndTime;

    /**
     *媒体文件支持的终端类型。

     TV屏的内容取值范围：

     1：STB
     2：PC
     3：LG-TV
     Web屏内容取值范围：

     1：Browser
     2：iPhone/iPad
     3：Android
     */
    @SerializedName("supportTerminals")
    private List<String> supportTerminals;

    /**
     *VOD媒资的扩展属性，其中扩展属性的Key由局点CMS定制。

     */
    @SerializedName("customFields")
    private List<NamedParameter> customFields;

    /**
     *对于OTT多码率媒资，返回多个码率信息。

     码率单位为kbps，同时多个码率会按照码率从大到小排序，例如一个媒资有10M、9.5M、9M、8M四种码率，则multiBitrates[]数字的取值顺序为：10000、9500、9000、8000。

     */
    @SerializedName("multiBitrates")
    private List<String> multiBitrates;

    /**
     *局点定制的扩展字段。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    /**
     *片头时长。
     *
     * 单位：秒。
     */
    @SerializedName("headDuration")
    private int headDuration;

    /**
     *片尾时长。
     *
     * 单位：秒。
     */
    @SerializedName("tailDuration")
    private int tailDuration;

   /*是否VR内容。

    取值包括：
            •
            0: 非VR内容

•
        1: 普通VR

•
        2: FOV VR*/


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

    public String getElapseTime() {
        return elapseTime;
    }

    public void setElapseTime(String elapseTime) {
        this.elapseTime = elapseTime;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public String getIsDownload() {
        return isDownload;
    }

    public void setIsDownload(String isDownload) {
        this.isDownload = isDownload;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getHDCPEnable() {
        return HDCPEnable;
    }

    public void setHDCPEnable(String HDCPEnable) {
        this.HDCPEnable = HDCPEnable;
    }

    public String getMacrovision() {
        return macrovision;
    }

    public void setMacrovision(String macrovision) {
        this.macrovision = macrovision;
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

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

    public String getCGMSA() {
        return CGMSA;
    }

    public void setCGMSA(String CGMSA) {
        this.CGMSA = CGMSA;
    }

    public String getAnalogOutputEnable() {
        return analogOutputEnable;
    }

    public void setAnalogOutputEnable(String analogOutputEnable) {
        this.analogOutputEnable = analogOutputEnable;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    public void setVideoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
    }

    public String getAudioType() {
        return audioType;
    }

    public void setAudioType(String audioType) {
        this.audioType = audioType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFps() {
        return fps;
    }

    public void setFps(String fps) {
        this.fps = fps;
    }

    public String getMaxBitrate() {
        return maxBitrate;
    }

    public void setMaxBitrate(String maxBitrate) {
        this.maxBitrate = maxBitrate;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getPreviewStartTime() {
        return previewStartTime;
    }

    public void setPreviewStartTime(String previewStartTime) {
        this.previewStartTime = previewStartTime;
    }

    public String getPreviewEndTime() {
        return previewEndTime;
    }

    public void setPreviewEndTime(String previewEndTime) {
        this.previewEndTime = previewEndTime;
    }

    public List<String> getSupportTerminals() {
        return supportTerminals;
    }

    public void setSupportTerminals(List<String> supportTerminals) {
        this.supportTerminals = supportTerminals;
    }

    public List<String> getMultiBitrates() {
        return multiBitrates;
    }

    public void setMultiBitrates(List<String> multiBitrates) {
        this.multiBitrates = multiBitrates;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public List<NamedParameter> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<NamedParameter> customFields) {
        this.customFields = customFields;
    }

    public interface Definition
    {
        String SD = "0";

        String HD = "1";

        String FOUR_K = "2";
    }

    public int getHeadDuration() {
        return headDuration;
    }

    public void setHeadDuration(int headDuration) {
        this.headDuration = headDuration;
    }

    public int getTailDuration() {
        return tailDuration;
    }

    public void setTailDuration(int tailDuration) {
        this.tailDuration = tailDuration;
    }
}
