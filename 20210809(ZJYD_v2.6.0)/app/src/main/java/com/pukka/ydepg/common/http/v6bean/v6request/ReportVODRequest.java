package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: ReportVODRequest.java
 * @author: yh
 * @date: 2017-04-25 10:58
 */

public class ReportVODRequest {


    public static final int START_PLAY=0;

    public static final int FINISH_PLAY=1;

    /**
     * action : 1
     * VODID :
     * mediaID :
     * isDownload : 12
     * subjectID :
     * productID :
     * extensionFields : [""]
     */

    /**
     *播放行为，取值包括：

     0：开始播放
     1：退出播放

     */
    @SerializedName("action")
    private int action;

    /**
     *VOD的ID。

     */
    @SerializedName("VODID")
    private String VODID;

    /**
     *播放的媒资ID。

     */
    @SerializedName("mediaID")
    private String mediaID;

    /**
     *是否播放本地已下载的VOD，取值包括：

     0：否
     1：是
     默认值是0。

     */
    @SerializedName("isDownload")
    private String isDownload;

    /**
     *如果从指定的栏目进入VOD，携带栏目ID，用于Reporter按栏目统计用户行为。

     */
    @SerializedName("subjectID")
    private String subjectID;

    /**
     *鉴权通过的产品ID
     */
    @SerializedName("productID")
    private String productID;

    /**
     *扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public int  getAction() {
        return action;
    }

    public void setAction(int  action) {
        this.action = action;
    }

    public String getVODID() {
        return VODID;
    }

    public void setVODID(String VODID) {
        this.VODID = VODID;
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }

    public String getIsDownload() {
        return isDownload;
    }

    public void setIsDownload(String isDownload) {
        this.isDownload = isDownload;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
