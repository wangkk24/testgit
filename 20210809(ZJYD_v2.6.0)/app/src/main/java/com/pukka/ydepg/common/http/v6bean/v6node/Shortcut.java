package com.pukka.ydepg.common.http.v6bean.v6node;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 */

public class Shortcut
{
    /**
     * id :
     * type :
     * canFocus :
     * name :
     * img :
     * focusImg :
     * appID :
     * elementAction : {"actionType":"","actionURL":"","appParam":""}
     */
    @SerializedName("id")
    private String id;
    @SerializedName("type")
    private String type;
    @SerializedName("canFocus")
    private String canFocus;
    @SerializedName("name")
    private List<Dialect> nameDialect;
    @SerializedName("description")
    private List<Dialect> descriptionDialect;
    @SerializedName("img")
    private String image;
    @SerializedName("focusImg")
    private String focusImage;
    @SerializedName("appID")
    private String appID;
    @SerializedName("elementAction")
    private ElementAction elementAction;

    public String getId() { return id;}

    public void setId(String id) { this.id = id;}

    public String getType() { return type;}

    public void setType(String type) { this.type = type;}

    public String getCanFocus() { return canFocus;}

    public void setCanFocus(String canFocus) { this.canFocus = canFocus;}

    public List<Dialect> getNameDialect()
    {
        return nameDialect;
    }

    public void setNameDialect(List<Dialect> nameDialect)
    {
        this.nameDialect = nameDialect;
    }

    public List<Dialect> getDescriptionDialect()
    {
        return descriptionDialect;
    }

    public void setDescriptionDialect(List<Dialect> descriptionDialect)
    {
        this.descriptionDialect = descriptionDialect;
    }

    public String getImage() { return image;}

    public void setImage(String image) { this.image = image;}

    public String getFocusImage() { return focusImage;}

    public void setFocusImage(String focusImage) { this.focusImage = focusImage;}

    public String getAppId() { return appID;}

    public void setAppId(String appID) { this.appID = appID;}

    public ElementAction getElementAction() { return elementAction;}

    public void setElementAction(ElementAction elementAction)
    {
        this.elementAction = elementAction;
    }
}