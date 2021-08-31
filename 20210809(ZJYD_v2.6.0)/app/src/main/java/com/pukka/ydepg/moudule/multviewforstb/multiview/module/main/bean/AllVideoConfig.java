package com.pukka.ydepg.moudule.multviewforstb.multiview.module.main.bean;

import com.huawei.ott.sdk.log.DebugLog;

import java.util.List;

public class AllVideoConfig {

    /**
     * 视频的播放链接
     */
    private List<String> ResourcePlayURL;

    /**
     * 视频的编号
     */
    private String ContentID;

    /**
     * 视频名称
     */
    private String ResourceName;

    /**
     * 1：直播
     * 2：点播
     */
    private int MediaType;

    /**
     * 主机位ID
     */
    private int mainCameraId;

    /**
     * 当前片源可旋转度数
     */
    private int CameraDegree;

    /**
     * 当前片源机位总数
     */
    private int cameraNum;

    /**
     * 0：一大三小的布局
     * 1：四等分的布局
     */
    private int Viewtype;

    /**
     * 播放书签
     */
    private long Bookmark;

    /**
     * 播放完实际返回的URL链接
     * （未传递，默认直接退出播放器apk）
     */
    private String ReturnURL;

    /**
     * 是否强制逐行扫描，当且仅当=1时有效
     */
    private String enforceProgressiveScanning;

    /**
     * 传入的图片（背景，海报，播放背景等等）
     */
    private Picture picture;

    private List<ViewSize> viewSizes;

    /**
     * 是否循环播放
     */
    private boolean isRecyclePlay;

    public long getBookmark() {
        return Bookmark;
    }

    public void setBookmark(long bookmark) {
        Bookmark = bookmark;
    }

    public String getReturnURL() {
        return ReturnURL;
    }

    public void setReturnURL(String returnURL) {
        ReturnURL = returnURL;
    }

    public int getViewtype() {
        return Viewtype;
    }

    public void setViewtype(int viewtype) {
        this.Viewtype = viewtype;
    }

    public String getContentID() {
        return ContentID;
    }

    public void setContentID(String contentID) {
        ContentID = contentID;
    }

    public String getResourceName() {
        return ResourceName;
    }

    public void setResourceName(String resourceName) {
        this.ResourceName = resourceName;
    }

    public List<String> getResourcePlayURL() {
        return ResourcePlayURL;
    }

    public void setResourcePlayURL(List<String> resourcePlayURL) {
        ResourcePlayURL = resourcePlayURL;
        DebugLog.info("AllVideoConfig","[setResourcePlayURL] size="+resourcePlayURL.size());
    }

    public int getMediaType() {
        return MediaType;
    }

    public void setMediaType(int mediaType) {
        MediaType = mediaType;
    }

    public int getMainCameraId() {
        return mainCameraId;
    }

    public void setMainCameraId(int mainCameraId) {
        this.mainCameraId = mainCameraId;
    }

    public int getCameraDegree() {
        return CameraDegree;
    }

    public void setCameraDegree(int cameraDegree) {
        CameraDegree = cameraDegree;
    }

    public String getEnforceProgressiveScanning() {
        return enforceProgressiveScanning;
    }

    public void setEnforceProgressiveScanning(String enforceProgressiveScanning) {
        this.enforceProgressiveScanning = enforceProgressiveScanning;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public List<ViewSize> getViewSizes() {
        return viewSizes;
    }

    public void setViewSizes(List<ViewSize> viewSizes) {
        this.viewSizes = viewSizes;
    }

    public boolean isRecyclePlay() {
        return isRecyclePlay;
    }

    public void setRecyclePlay(boolean recyclePlay) {
        isRecyclePlay = recyclePlay;
    }

    public int getCameraNum() {
        return cameraNum;
    }

    public void setCameraNum(int cameraNum) {
        this.cameraNum = cameraNum;
    }

    @Override
    public String toString() {
        return "AllVideoConfig{" +
                "ResourcePlayURL=" + ResourcePlayURL +
                ", ContentID='" + ContentID + '\'' +
                ", ResourceName='" + ResourceName + '\'' +
                ", MediaType=" + MediaType +
                ", mainCameraId=" + mainCameraId +
                ", CameraDegree=" + CameraDegree +
                ", cameraNum=" + cameraNum +
                ", Viewtype=" + Viewtype +
                ", Bookmark=" + Bookmark +
                ", ReturnURL='" + ReturnURL + '\'' +
                ", enforceProgressiveScanning='" + enforceProgressiveScanning + '\'' +
                ", picture=" + picture +
                ", viewSizes=" + viewSizes +
                ", isRecyclePlay=" + isRecyclePlay +
                '}';
    }

    public String toJson(){
        return "ContentID='" + ContentID + '\'' +
                "?Bookmark=" + Bookmark +
                "?ReturnURL='" + ReturnURL;
    }
}
