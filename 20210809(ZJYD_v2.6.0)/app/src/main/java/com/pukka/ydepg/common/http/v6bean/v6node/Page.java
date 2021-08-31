package com.pukka.ydepg.common.http.v6bean.v6node;


import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.utils.GlideUtil;

import java.util.List;

public class Page
{
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private List<Dialect> nameDialect;
    @SerializedName("description")
    private List<Dialect> descriptionDialect;
    @SerializedName("elementWidth")
    private String elementWidth;
    @SerializedName("elementHeight")
    private String elementHeight;
    @SerializedName("horizontal_space")
    private String horizontalSpace;
    @SerializedName("vertical_space")
    private String verticalSpace;
    @SerializedName("column")
    private String column;
    @SerializedName("row")
    private String row;
    @SerializedName("background")
    private String background;
    @SerializedName("elements")
    private List<Element> elementList;
    @SerializedName("index")
    private String index;

    public String getIndex()
    {
        return index;
    }

    public void setIndex(String index)
    {
        this.index = index;
    }

    public String getId() { return id;}

    public void setId(String id) { this.id = id;}

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

    public String getHorizontalSpace()
    {
        return horizontalSpace;
    }

    public void setHorizontalSpace(String horizontalSpace)
    {
        this.horizontalSpace = horizontalSpace;
    }

    public String getVerticalSpace()
    {
        return verticalSpace;
    }

    public void setVerticalSpace(String verticalSpace)
    {
        this.verticalSpace = verticalSpace;
    }

    public String getElementWidth() { return elementWidth;}

    public void setElementWidth(String elementWidth) { this.elementWidth = elementWidth;}

    public String getElementHeight() { return elementHeight;}

    public void setElementHeight(String elementHeight)
    {
        this.elementHeight = elementHeight;
    }

    public String getColumn() { return column;}

    public void setColumn(String column) { this.column = column;}

    public String getRow() { return row;}

    public void setRow(String row) { this.row = row;}

    public String getBackground() {
        return GlideUtil.getUrl(background);
    }

    public void setBackground(String background) { this.background = background;}

    public List<Element> getElementList() { return elementList;}

    public void setElementList(List<Element> elementList) { this.elementList = elementList;}
}