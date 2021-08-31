package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: PriceObjectDetail.java
 * @author: yh
 * @date: 2017-04-24 15:31
 */

public class PriceObjectDetail {

    /**
     * ID :
     * type : 12
     * contentType :
     * VOD :
     * channel :
     * playbill :
     */


    /**
     * the ranges of field type
     */
    public interface Type
    {
        /**
         * 1:content&media file
         */
        String CONTENT_MEDIA_FILE = "1";

        /**
         * 2:content
         */
        String CONTENT = "2";

        /**
         * 3:category
         */
        String CATEGORY = "3";

        /**
         * 4:media file
         */
        String MEDIA_FILE = "4";

        /**
         * 5:whole feature
         */
        String WHOLE_FEATURE = "5";
    }

    /**
     * the range of field contentType
     */
    public interface ContentType
    {
        /**
         * VOD
         */
        String VOD = "VOD";

        /**
         * CHANNEL
         */
        String CHANNEL = "CHANNEL";

        /**
         * PROGRAM
         */
        String PROGRAM = "PROGRAM";

        /**
         * VAS
         */
        String VAS = "VAS";
    }


    /**
     *定价对象ID。

     */
    @SerializedName("ID")
    private String ID;

    /**
     *定价对象类型。

     取值范围：

     1：内容&媒资特性[预留]
     2：内容
     3：栏目[预留]
     4：媒资[预留]
     5：全局特性[预留]
     */
    @SerializedName("type")
    private String type;

    /**
     *type=2时，表示内容对应的内容类型，具体取值包括：

     VOD：点播
     CHANNEL：频道
     PROGRAM：节目单

     */
    @SerializedName("contentType")
    private String contentType;

    /**
     *如果contentType= VOD，返回VOD对象。

     */
    @SerializedName("VOD")
    private VOD VOD;

    /**
     *如果contentType=CHANNEL，返回Channel对象。

     */
    @SerializedName("channel")
    private Channel channel;

    /**
     *如果contentType= PROGRAM，返回节目单对象。

     */
    @SerializedName("playbill")
    private Playbill playbill;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public com.pukka.ydepg.common.http.v6bean.v6node.VOD getVOD() {
        return VOD;
    }

    public void setVOD(com.pukka.ydepg.common.http.v6bean.v6node.VOD VOD) {
        this.VOD = VOD;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Playbill getPlaybill() {
        return playbill;
    }

    public void setPlaybill(Playbill playbill) {
        this.playbill = playbill;
    }
}
