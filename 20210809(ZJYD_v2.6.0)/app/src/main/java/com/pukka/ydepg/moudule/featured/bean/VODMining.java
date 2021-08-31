package com.pukka.ydepg.moudule.featured.bean;



import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.Sitcom;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.OTTFormat;

import java.util.List;

/**
 * The class is mining the data from VOD for UI
 */
public class VODMining
{
    /**
     * get the vod 's poster picture
     * it will get the first url of poster list
     *
     * @param vod
     * @return
     */
    public static String getPoster(VOD vod)
    {
        Picture picture = getPicture(vod);
        if (null == picture)
        {
            return null;
        }
        List<String> posterList = picture.getPosters();
        if (CollectionUtil.isEmpty(posterList))
        {
            return null;
        }
        return posterList.get(0);
    }

    /**
     * get the vod 's poster picture
     * it will get the first url of poster list
     *
     * @param vod
     * @return
     */
    public static String getMiguPoster(VOD vod)
    {
        Picture picture = getPicture(vod);
        if (null == picture)
        {
            return null;
        }
        List<String> iconList = picture.getIcons();
        if (!CollectionUtil.isEmpty(iconList))
        {
            return iconList.get(0);
        }
        else
        {
            List<String> draftList = picture.getDrafts();
            if (!CollectionUtil.isEmpty(draftList))
            {
                return draftList.get(0);
            }
            else
            {
                List<String> titleList = picture.getTitles();
                if (!CollectionUtil.isEmpty(titleList))
                {
                    return titleList.get(0);
                }
            }
        }
        return null;
    }

    /**
     * get the banner 's poster picture
     * it will get the first url of poster list
     *
     * @param vod
     * @return
     */
    public static String getBannerPoster(VOD vod)
    {
        List<String> posterList = getDraftList(vod);
        if (CollectionUtil.isEmpty(posterList))
        {
            return null;
        }
        return posterList.get(0);
    }

    public static List<String> getDraftList(VOD vod)
    {
        Picture picture = getPicture(vod);
        if (null == picture)
        {
            return null;
        }
        return picture.getDrafts();
    }

    /**
     * get the hor poster picture
     * it will get the first url of poster list
     *
     * @param vod
     * @return
     */
    public static String getAdsPoster(VOD vod)
    {
        Picture picture = getPicture(vod);
        if (null == picture)
        {
            return null;
        }
        List<String> posterList = picture.getAds();
        if (CollectionUtil.isEmpty(posterList))
        {
            return null;
        }
        return posterList.get(0);
    }

    /**
     * get the ver poster picture
     * it will get the first url of poster list
     *
     * @param vod
     * @return
     */
    public static String getTitlePoster(VOD vod)
    {
        Picture picture = getPicture(vod);
        if (null == picture)
        {
            return null;
        }
        List<String> posterList = picture.getTitles();
        if (CollectionUtil.isEmpty(posterList))
        {
            return null;
        }
        return posterList.get(0);
    }


    /**
     * get the ver poster picture
     * it will get the first url of poster list
     *
     * @param vod
     * @return
     */
    public static String getIcon(VOD vod)
    {
        Picture picture = getPicture(vod);
        if (null == picture)
        {
            return null;
        }
        List<String> posterList = picture.getIcons();
        if (CollectionUtil.isEmpty(posterList))
        {
            return null;
        }
        return posterList.get(0);
    }

    /**
     * get the HCSSlaveAddressList of VOD 's picture
     *
     * @param vod
     * @return
     */
    public static List<String> getHCSSlaveAddressList(VOD vod)
    {
        return PictureMining.getHCSSlaveAddressList(getPicture(vod));
    }

    /**
     * get the picture of VOD
     *
     * @param vod
     * @return
     */
    public static Picture getPicture(VOD vod)
    {
        if (null == vod)
        {
            return null;
        }
        return vod.getPicture();
    }

    public static Bookmark getBookmark(VOD vod)
    {
        if (null == vod)
        {
            return null;
        }
        return vod.getBookmark();
    }

    /**
     * get the bookmark 's range time of vod
     *
     * @param vod
     * @return
     */
    public static String getRangeTime(VOD vod)
    {
        Bookmark bookmark = getBookmark(vod);
        if (null == bookmark)
        {
            return null;
        }
        return bookmark.getRangeTime();
    }

    /**
     * get the elapse time of media file
     * the media file is the sub object of vod
     * if get the time failed ,it will return -1;
     *
     * @param vod
     * @return
     */
    public static int getElapseTime(VOD vod)
    {
        VODMediaFile vodMediaFile = getVODMediaFile(vod);
        if (null == vodMediaFile)
        {
            return -1;
        }
        return OTTFormat.convertInt(vodMediaFile.getElapseTime());
    }

    public static VODMediaFile getVODMediaFile(VOD vod)
    {
        VODMediaFile vodMediaFile = null;
        if (null == vod)
        {
            return null;
        }
        List<VODMediaFile> mediaFileList = vod.getMediaFiles();
        if (!CollectionUtil.isEmpty(mediaFileList))
        {
            vodMediaFile = mediaFileList.get(0);
        }
        return vodMediaFile;
    }

    private static Sitcom getSitcom(VOD vod)
    {
        Sitcom sitcom = null;
        if (null == vod)
        {
            return null;
        }
        List<Sitcom> sitcomList = vod.getSeries();
        if (!CollectionUtil.isEmpty(sitcomList))
        {
            sitcom = sitcomList.get(0);
        }
        return sitcom;
    }

    public static int getSitcomNo(VOD vod)
    {
        Sitcom sitcom = getSitcom(vod);
        if (null == sitcom)
        {
            return 0;
        }
        return OTTFormat.convertInt(sitcom.getSitcomNO());
    }


    public static String getDefinition(VOD vod)
    {
        VODMediaFile vodMediaFile = getVODMediaFile(vod);
        if (null == vodMediaFile)
        {
            return "-1";
        }
        return vodMediaFile.getDefinition();
    }

    public static String getSitcomNoFromBookMark(VOD vod)
    {
        Bookmark bookmark = getBookmark(vod);
        if (null == bookmark)
        {
            return null;
        }
        return bookmark.getSitcomNO();
    }
}