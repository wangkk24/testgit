package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: ChannelEncrypt.java
 * @author: yh
 * @date: 2017-04-21 16:05
 */

public class ChannelEncrypt  implements Serializable {


    /**
     * encrypt : 123
     * hdcpEnable : 123
     * macrovision : 123
     * CGMSA : 123
     */

    /**
     *频道是否加密。

     取值范围：

     0：未加密
     1：加密
     */
    @SerializedName("encrypt")
    private String encrypt;

    /**
     *内容是否支持HDCP。

     取值范围：

     0：不支持
     1：支持
     */
    @SerializedName("hdcpEnable")
    private String hdcpEnable;

    /**
     *频道的Macrovision属性。

     取值范围：

     0：Macrovision off
     1：AGC
     2：AGC + 2-stripe
     3：AGC + 4-stripe
     默认值为0。

     */
    @SerializedName("macrovision")
    private String macrovision;

    /**
     *内容CGMS-A属性，取值包括：

     0：Copy Freely
     1：Copy No More
     2：Copy Once
     3：Copy Never
     */
    @SerializedName("CGMSA")
    private String CGMSA;

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

    public String getHdcpEnable() {
        return hdcpEnable;
    }

    public void setHdcpEnable(String hdcpEnable) {
        this.hdcpEnable = hdcpEnable;
    }

    public String getMacrovision() {
        return macrovision;
    }

    public void setMacrovision(String macrovision) {
        this.macrovision = macrovision;
    }

    public String getCGMSA() {
        return CGMSA;
    }

    public void setCGMSA(String CGMSA) {
        this.CGMSA = CGMSA;
    }

    public ChannelEncrypt() {
    }
}
