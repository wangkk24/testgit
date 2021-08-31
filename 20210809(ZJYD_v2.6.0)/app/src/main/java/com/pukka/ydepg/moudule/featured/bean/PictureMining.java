package com.pukka.ydepg.moudule.featured.bean;





import com.pukka.ydepg.common.http.v6bean.v6node.Picture;

import java.util.List;

/**
 * The class is mining the data from Picture for UI
 */
public class PictureMining
{
    /**
     *
     * @param picture
     * @return
     */
    public static String getPoster(Picture picture)
    {
        if(null == picture)
        {
            return null;
        }
        List<String> posterList = picture.getPosters();
        if(null == posterList || posterList.isEmpty())
        {
            return  null;
        }
        return posterList.get(0);
    }

    public static List<String> getHCSSlaveAddressList(Picture picture)
    {
        if(null == picture)
        {
            return null;
        }
       return  picture.getHcsSlaveAddrs();
    }

    public static String getChannelPicture(Picture picture)
    {
        if(null == picture)
        {
            return null;
        }
        List<String> channelPictureList = picture.getIcons();
        if(null == channelPictureList || channelPictureList.isEmpty())
        {
            return null;
        }
        return channelPictureList.get(0);
    }
}