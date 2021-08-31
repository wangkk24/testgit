package com.pukka.ydepg.moudule.player.node;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jWX234536 on 2015/8/4.
 */
public class Episode  implements Serializable
{
    @SerializedName("ID")
    private String id;

    @SerializedName("index")
    private int index;

    @SerializedName("mediaFiles")
    private List<VODMediaFile> mediaFiles;

    public List<VODMediaFile> getMediaFiles()
    {
        return mediaFiles;
    }

    public void setMediaFiles(List<VODMediaFile> mediaFiles)
    {
        this.mediaFiles = mediaFiles;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

}
