package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liudo on 2018/12/10.
 */

public class CastDetail implements Serializable
{

    /**
     * 演职员编号。
     */
    @SerializedName("castID")
    private String castID;
    /**
     * 演职员名称。
     */
    @SerializedName("castName")
    private String castName;
    /**
     * 第三方系统分配的Code。
     */
    @SerializedName("castCode")
    private String castCode;

    /**
     *头衔
     */
    @SerializedName("title")
    private String  title;

    /**
     *演员描述。
     */
    @SerializedName("introduce")
    private String introduce;

    /**
     *演职员图片
     */
    @SerializedName("picture")
    private Picture picture;


    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getCastID()
    {
        return castID;
    }

    public void setCastID(String castID)
    {
        this.castID = castID;
    }

    public String getCastName()
    {
        return castName;
    }

    public void setCastName(String castName)
    {
        this.castName = castName;
    }

    public String getCastCode()
    {
        return castCode;
    }

    public void setCastCode(String castCode)
    {
        this.castCode = castCode;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getIntroduce()
    {
        return introduce;
    }

    public void setIntroduce(String introduce)
    {
        this.introduce = introduce;
    }

    public Picture getPicture()
    {
        return picture;
    }

    public void setPicture(Picture picture)
    {
        this.picture = picture;
    }

    public List<NamedParameter> getExtensionFields()
    {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields)
    {
        this.extensionFields = extensionFields;
    }
}
