package com.pukka.ydepg.common.http.v6bean.v6node;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertVideo;

/**
 * Created by ld on 2017/8/25.
 */

public class PlayVodBean implements Parcelable {
    //是否是甩屏0否，1是
    private String isXmppPush="0";
    private String isXiri = "0";
    private String lastPlayUrl;
    private String lastPlayID;
    //甩屏指示
    private String xmppFrom;
    //vod名字
    private String vodName;
    //播放url
    private String playUrl;
    //书签rangeTime
    private String bookmark;
    //vodID
    private String vodId;
    //传"VOD"
    private String vodType;
    //子集是连续剧的第几集
    private String sitcomNO;
    //子集VOD的id；
    private String episodeId;
    //是否是电影，0为电影，1为电视剧
    private String isFilm="0";
    /***********用于银河普通VOD添加书签 *************/
    // 因为银河普通VOD也是添加在某个父集底下
    private String fatherSitcomNO;
    private String fatherVODId;

    private int tryToSeeFlag;

    private String elapseTime;

    private String authResult;

    private String detailStr;



    //是否时连续剧
    private int isSeries;

    private int isReverse;


    private String mediaId;

    private String subjectID;

    private String productId;

    private String recommendType;

    private AdvertVideo mAdvertVideo;

    public AdvertVideo getAdvertVideo() {
        return mAdvertVideo;
    }

    public void setAdvertVideo(AdvertVideo mAdvertVideo) {
        this.mAdvertVideo = mAdvertVideo;
    }

    public String getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(String recommendType) {
        this.recommendType = recommendType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public int getIsSeries() {
        return isSeries;
    }

    public void setIsSeries(int isSeries) {
        this.isSeries = isSeries;
    }

    public int getIsReverse() {
        return isReverse;
    }

    public void setIsReverse(int isReverse) {
        this.isReverse = isReverse;
    }

    public String getDetailStr() {
        return detailStr;
    }

    public void setDetailStr(String detailStr) {
        this.detailStr = detailStr;
    }

    public String getAuthResult() {
        return authResult;
    }

    public void setAuthResult(String authResult) {
        this.authResult = authResult;
    }

    public String getElapseTime() {
        return elapseTime;
    }

    public void setElapseTime(String elapseTime) {
        this.elapseTime = elapseTime;
    }

    public int getTryToSeeFlag() {
        return tryToSeeFlag;
    }

    public void setTryToSeeFlag(int tryToSeeFlag) {
        this.tryToSeeFlag = tryToSeeFlag;
    }

    public String getFatherVODId() {
        return fatherVODId;
    }

    public void setFatherVODId(String fatherVODId) {
        this.fatherVODId = fatherVODId;
    }

    public String getFatherSitcomNO() {
        return fatherSitcomNO;
    }

    public void setFatherSitcomNO(String fatherSitcomNO) {
        this.fatherSitcomNO = fatherSitcomNO;
    }

    /***********用于银河普通VOD添加书签 *************/



    public String getIsFilm() {
        return isFilm;
    }

    public void setIsFilm(String isFilm) {
        this.isFilm = isFilm;
    }

    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }

    public String getSitcomNO() {
        return sitcomNO;
    }

    public void setSitcomNO(String sitcomNO) {
        this.sitcomNO = sitcomNO;
    }

    public String getVodName() {
        return vodName;
    }

    public void setVodName(String vodName) {
        this.vodName = vodName;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getBookmark() {
        if(!TextUtils.isEmpty(bookmark)&&bookmark.contains(".")){
            bookmark=bookmark.substring(0,bookmark.indexOf("."));
        }
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public String getVodId() {
        return vodId;
    }

    public void setVodId(String vodId) {
        this.vodId = vodId;
    }

    public String getVodType() {
        return vodType;
    }

    public void setVodType(String vodType) {
        this.vodType = vodType;
    }

    public String isXmppPush() {
        return isXmppPush;
    }

    public void setXmppPush(String xmppPush) {
        isXmppPush = xmppPush;
    }

    public void setIsXiri(String isXiri) {
        this.isXiri = isXiri;
    }

    public String getIsXiri() {
        return isXiri;
    }

    public String getLastPlayUrl() {
        return lastPlayUrl;
    }

    public void setLastPlayUrl(String lastPlayUrl) {
        this.lastPlayUrl = lastPlayUrl;
    }

    public String getLastPlayID() {
        return lastPlayID;
    }

    public void setLastPlayID(String lastPlayID) {
        this.lastPlayID = lastPlayID;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public PlayVodBean() {
    }

    protected PlayVodBean(Parcel in) {
        this.isXmppPush=in.readString();
        this.xmppFrom=in.readString();
        this.vodName = in.readString();
        this.playUrl = in.readString();
        this.bookmark = in.readString();
        this.vodId = in.readString();
        this.vodType = in.readString();
        this.sitcomNO = in.readString();
        this.episodeId = in.readString();
        this.isFilm = in.readString();
        this.fatherSitcomNO = in.readString();
        this.fatherVODId = in.readString();
        this.tryToSeeFlag=in.readInt();
        this.elapseTime=in.readString();
        this.authResult=in.readString();
        this.detailStr=in.readString();
        this.isSeries=in.readInt();
        this.mediaId=in.readString();
        this.subjectID=in.readString();
        this.productId=in.readString();
        this.recommendType=in.readString();
        this.isXiri=in.readString();
        this.lastPlayUrl=in.readString();
        this.lastPlayID=in.readString();
        this.isReverse=in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.isXmppPush);
        dest.writeString(this.xmppFrom);
        dest.writeString(this.vodName);
        dest.writeString(this.playUrl);
        dest.writeString(this.bookmark);
        dest.writeString(this.vodId);
        dest.writeString(this.vodType);
        dest.writeString(this.sitcomNO);
        dest.writeString(this.episodeId);
        dest.writeString(this.isFilm);
        dest.writeString(this.fatherSitcomNO);
        dest.writeString(this.fatherVODId);
        dest.writeInt(this.tryToSeeFlag);
        dest.writeString(this.elapseTime);
        dest.writeString(this.authResult);
        dest.writeString(this.detailStr);
        dest.writeInt(isSeries);
        dest.writeString(this.mediaId);
        dest.writeString(this.subjectID);
        dest.writeString(this.productId);
        dest.writeString(this.recommendType);
        dest.writeString(this.isXiri);
        dest.writeString(this.lastPlayUrl);
        dest.writeString(this.lastPlayID);
        dest.writeInt(isReverse);
    }


    public static final Creator<PlayVodBean> CREATOR = new Creator<PlayVodBean>() {
        @Override
        public PlayVodBean createFromParcel(Parcel source) {
            return new PlayVodBean(source);
        }

        @Override
        public PlayVodBean[] newArray(int size) {
            return new PlayVodBean[size];
        }
    };

    public String getXmppFrom() {
        return xmppFrom;
    }

    public void setXmppFrom(String xmppFrom) {
        this.xmppFrom = xmppFrom;
    }


    public void clearDate(){
        //是否是甩屏0否，1是
       isXmppPush="0";
       isXiri = "0";
        lastPlayUrl = null;
        lastPlayID = null;
        //甩屏指示
        xmppFrom=null;
        //vod名字
        vodName=null;
        //播放url
        playUrl=null;
        //书签rangeTime
        bookmark=null;
        //vodID
        vodId=null;
        //传"VOD"
        vodType=null;
        //子集是连续剧的第几集
        sitcomNO=null;
        //子集VOD的id；
        episodeId=null;
        //是否是电影，0为电影，1为电视剧
        isFilm="0";
        /***********用于银河普通VOD添加书签 *************/
        // 因为银河普通VOD也是添加在某个父集底下
       fatherSitcomNO=null;

       fatherVODId=null;

       tryToSeeFlag=0;

        elapseTime=null;

        authResult=null;

        detailStr=null;



        //是否时连续剧
         isSeries=0;
         isReverse=0;


         mediaId=null;

        subjectID=null;

        productId=null;

        recommendType=null;

    }

}
