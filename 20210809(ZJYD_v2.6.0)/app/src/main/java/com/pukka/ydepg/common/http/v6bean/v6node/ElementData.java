package com.pukka.ydepg.common.http.v6bean.v6node;


import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 */

public class ElementData
{
    /**
     * name : 001_1
     * description :
     * contentURL : ${jmpurl}/ContentCode=${code}&ContentType=VOD&Action=SmallScreen
     * textContent :
     * elementAction : {"actionType":"0",
     * "actionURL":"${jmpurl}/ContentCode=${code}&ContentType=VOD&Action
     * =FullScreen","appParam":""}
     */
    @SerializedName("name")
    private List<Dialect> nameDialect;
    @SerializedName("description")
    private List<Dialect> descriptionDialect;
    @SerializedName("contentURL")
    private String contentURL;
    @SerializedName("textContent")
    private String textContent;
    @SerializedName("elementAction")
    private ElementAction elementAction;
    @SerializedName("extraData")
    private HashMap<String, String> extraDataMap;



    public HashMap<String, String> getExtraDataMap()
    {
        return extraDataMap;
    }

    public void setExtraDataMap(HashMap<String, String> extraDataMap)
    {
        this.extraDataMap = extraDataMap;
    }

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

    public String getContentURL() { return contentURL;}

    public void setContentURL(String contentURL) { this.contentURL = contentURL;}

    public String getTextContent() { return textContent;}

    public void setTextContent(String textContent)
    {
        this.textContent = textContent;
    }

    public ElementAction getElementAction() { return elementAction;}

    public void setElementAction(ElementAction elementAction)
    {
        this.elementAction = elementAction;
    }
}