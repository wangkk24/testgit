package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: BookmarkItem.java
 * @author: yh
 * @date: 2017-04-24 15:05
 */

public class BookmarkItem {


    /**
     * bookmarkType :
     * VOD : 12
     * playbill : 12
     * npvrBookmark : 12
     * cpvrBookmark : 12
     */

    /**
     *书签类型，取值包括：

     VOD：点播书签
     PROGRAM：回看节目单书签
     NPVR：nPVR书签
     CPVR：CPVR书签
     */
    @SerializedName("bookmarkType")
    private String bookmarkType;

    /**
     *VOD内容描述，bookmarkType等于VOD时返回。

     */
    @SerializedName("VOD")
    private VOD VOD;

    /**
     *program内容描述，

     bookmarkType等于PROGRAM时返回。

     */
    @SerializedName("playbill")
    private Playbill playbill;

    /**
     *NPVR书签，则返回Bookmark对象，具体参数参见对象的定义。

     */
    @SerializedName("npvrBookmark")
    private Bookmark npvrBookmark;

    /**
     *CPVR书签，则返回Bookmark对象，具体参数参见对象的定义。

     */
    @SerializedName("cpvrBookmark")
    private Bookmark cpvrBookmark;

    public String getBookmarkType() {
        return bookmarkType;
    }

    public void setBookmarkType(String bookmarkType) {
        this.bookmarkType = bookmarkType;
    }

    public com.pukka.ydepg.common.http.v6bean.v6node.VOD getVOD() {
        return VOD;
    }

    public void setVOD(com.pukka.ydepg.common.http.v6bean.v6node.VOD VOD) {
        this.VOD = VOD;
    }

    public Playbill getPlaybill() {
        return playbill;
    }

    public void setPlaybill(Playbill playbill) {
        this.playbill = playbill;
    }

    public Bookmark getNpvrBookmark() {
        return npvrBookmark;
    }

    public void setNpvrBookmark(Bookmark npvrBookmark) {
        this.npvrBookmark = npvrBookmark;
    }

    public Bookmark getCpvrBookmark() {
        return cpvrBookmark;
    }

    public void setCpvrBookmark(Bookmark cpvrBookmark) {
        this.cpvrBookmark = cpvrBookmark;
    }
    private String elapseTime;

    public String getElapseTime() {
        return elapseTime;
    }

    public void setElapseTime(String elapseTime) {
        this.elapseTime = elapseTime;
    }

    public interface BookmarkType
    {
        String VOD = "VOD";
        String PROGRAM = "PROGRAM";
    }

}
