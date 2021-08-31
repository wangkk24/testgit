package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: PhysicalChannelSet.java
 * @author: yh
 * @date: 2017-04-21 16:33
 */

public class PhysicalChannelSet implements Serializable{


    /**
     * isSupportDefinitionSD : 123
     * isSupportDefinitionHD : 123
     * isSupportDefinition4K : 123
     */

    /**
     *是否支持标清。

     0：不支持
     1：支持
     默认值为0。

     */
    @SerializedName("isSupportDefinitionSD")
    private String isSupportDefinitionSD;

    /**
     *是否支持高清。

     0：不支持
     1：支持
     默认值为0。

     */
    @SerializedName("isSupportDefinitionHD")
    private String isSupportDefinitionHD;

    /**
     *是否支持4K。

     0：不支持
     1：支持
     默认值为0。

     */
    @SerializedName("isSupportDefinition4K")
    private String isSupportDefinition4K;

    public String getIsSupportDefinitionSD() {
        return isSupportDefinitionSD;
    }

    public void setIsSupportDefinitionSD(String isSupportDefinitionSD) {
        this.isSupportDefinitionSD = isSupportDefinitionSD;
    }

    public String getIsSupportDefinitionHD() {
        return isSupportDefinitionHD;
    }

    public void setIsSupportDefinitionHD(String isSupportDefinitionHD) {
        this.isSupportDefinitionHD = isSupportDefinitionHD;
    }

    public String getIsSupportDefinition4K() {
        return isSupportDefinition4K;
    }

    public void setIsSupportDefinition4K(String isSupportDefinition4K) {
        this.isSupportDefinition4K = isSupportDefinition4K;
    }
}
