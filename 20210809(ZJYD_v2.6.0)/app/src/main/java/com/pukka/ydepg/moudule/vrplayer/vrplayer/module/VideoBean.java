package com.pukka.ydepg.moudule.vrplayer.vrplayer.module;

import java.util.List;

/**
 * 功能描述
 *
 * @author l00477311
 * @since 2020-07-23
 */
public class VideoBean {
    /**
     * /**
     * 视频的名称
     */
    private String ResourceName;

    /**
     * 视频的编号
     */
    private String ContentID;

    /**
     * 视频的播放链接
     */
    private List<String> ResourcePlayURL;

    /**
     * 视频类型
     * 3: VR180 的 2D 视频;
     * 4: VR360 的 2D 视频;
     * 5: VR180 的 3D 视频;
     * 6: VR360 的 3D 视频;
     */
    private int ResourceType = 4;

    /**
     * 3D视频的类型对应3Dtype
     * 0:默认不区分
     * 1:3D 上下格式；
     * 2:3D 左右格式；
     */
    private int Type3D = 0;

    /**
     * 播放完 VR 视频后，实际返回的 URL 链接。默认直接退出播放器
     */
    private String ReturnURL;

    /**
     * 节目类型
     * 1:直播
     * 2:点播
     * 默认按照直播
     */
    private int MediaType = 1;

    /**
     * 播放书签
     */
    private long Bookmark;

    /**
     * 配置图片
     */
    private Picture picture;



    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public String getResourceName() {
        return ResourceName;
    }

    public void setResourceName(String resourceName) {
        this.ResourceName = resourceName;
    }

    public String getContentID() {
        return ContentID;
    }

    public void setContentID(String contentID) {
        this.ContentID = contentID;
    }

    public List<String> getResourcePlayURL() {
        return ResourcePlayURL;
    }

    public void setResourcePlayURL(List<String> resourcePlayURL) {
        this.ResourcePlayURL = resourcePlayURL;
    }

    public int getResourceType() {
        return ResourceType;
    }

    public void setResourceType(int resourceType) {
        this.ResourceType = resourceType;
    }

    public int getType3D() {
        return Type3D;
    }

    public void setType3D(int type3D) {
        this.Type3D = type3D;
    }

    public String getReturnURL() {
        return ReturnURL;
    }

    public void setReturnURL(String returnURL) {
        this.ReturnURL = returnURL;
    }

    public int getMediaType() {
        return MediaType;
    }

    public void setMediaType(int mediaType) {
        this.MediaType = mediaType;
    }

    public long getBookmark() {
        return Bookmark;
    }

    public void setBookmark(long bookmark) {
        this.Bookmark = bookmark;
    }


    @Override
    public String toString() {
        return "VideoBean{" +
                "ResourceName='" + ResourceName + '\'' +
                ", ContentID='" + ContentID + '\'' +
                ", ResourcePlayURL=" + ResourcePlayURL +
                ", ResourceType=" + ResourceType +
                ", Type3D=" + Type3D +
                ", ReturnURL='" + ReturnURL + '\'' +
                ", MediaType=" + MediaType +
                ", Bookmark=" + Bookmark +
                ", picture=" + picture +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VideoBean)) return false;
        VideoBean videoBean = (VideoBean) o;
        return ResourcePlayURL.equals(videoBean.ResourcePlayURL);
    }

    public String Url() {
        return "ContentID='" + ContentID + '\'' +
                "?Bookmark=" + Bookmark +
                "?ReturnURL='" + ReturnURL;
    }
}

