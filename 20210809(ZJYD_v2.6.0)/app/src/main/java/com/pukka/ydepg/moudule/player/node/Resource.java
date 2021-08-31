package com.pukka.ydepg.moudule.player.node;



import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Resource
{
    /**
     * color : [{"name":"C3","description":"White","value":"#FFFFFF"},{"name":"C2",
     * "description":"Black","value":"#1E1E1E"}]
     */
    @SerializedName("color")
    private List<ColorEntity> color;

    public void setColor(List<ColorEntity> color)
    {
        this.color = color;
    }

    public List<ColorEntity> getColor()
    {
        return color;
    }

    public static class ColorEntity
    {
        /**
         * name : C3
         * description : White
         * value : #FFFFFF
         */
        @SerializedName("name")
        private String name;

        @SerializedName("description")
        private String description;

        @SerializedName("value")
        private String value;

        public void setName(String name)
        {
            this.name = name;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        public void setValue(String value)
        {
            this.value = value;
        }

        public String getName()
        {
            return name;
        }

        public String getDescription()
        {
            return description;
        }

        public String getValue()
        {
            return value;
        }
    }
}
