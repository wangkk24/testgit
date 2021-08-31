package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.CheckLock;

import java.util.List;

/**
 * 作者：panjw on 2021/6/10 10:09
 * 点播批量媒资播放鉴权
 * 邮箱：panjw@easier.cn
 */
public class PlayMultiMediaVODRequest {
    //播放的VOD的编号，如果播放子集，传子集的VOD编号。如果IDType为0时，表示VOD的ID，为1时，表示VOD的code。
    //如果传入的VODID是连续剧父集编号，mediaID只能是父集片花的媒资编号。
    @SerializedName("VODID")
    private String VODID;
    //播放的VOD媒资编号列表，可以是正片媒资编号或者是片花媒资编号。如果IDType为0时，表示VOD媒资的ID，为1时，表示VOD媒资的code。
    @SerializedName("mediaIDs")
    private List<String> mediaIDs;
    //父集编号，如果IDType为0时，表示VOD父集的ID，为1时，表示VOD父集的code。
    //
    //该参数用于连续剧/季播剧书签的返回。
    //
    //终端UI需要根据连续剧/季播剧书签继续播放的场景，需要传入该参数，如果不传则按VOD点播书签返回，不会返回连续剧/季播剧书签。
    //
    //如果播放的是连续剧，传入连续剧父集编号。如果播放的是季播剧，传入季播剧父集编号。
    @SerializedName("seriesID")
    private String seriesID;
    //内容编号的类型。
    //取值包括：
    //0: 内容内键，取值由VSP生成。
    //1: 内容Code，取值由第三方系统生成。
    @SerializedName("IDType")
    private int IDType;
    //检查VOD是否被加锁和父母字控制，或者如果加锁或父母字控制，需要进行密码校验。
    //接口默认对鉴权请求进行童锁和父母控制字的检查，包括主Profile和子Profile的鉴权请求，如果终端不传，VSP平台会默认进行强制检查。
    @SerializedName("checkLock")
    private CheckLock checkLock;
    //指定返回的URL格式。
    //取值包括：
    //0: 默认格式
    //1: TS转换成HLS协议
    //2: 转换为RTSP协议
    @SerializedName("URLFormat")
    private int URLFormat;
    //如果内容未订购是否返回内容定价的产品。
    //    取值包括：
    //            0: 不返回
    //        1: 返回
    @SerializedName("isReturnProduct")
    private int isReturnProduct;
    //isReturnProduct为1时，本参数有意义。表示返回产品列表的排序方式。
    //取值包括：
    //PRICE:ASC: 按产品价格升序排序
    //PRICE:DESC: 按产品价格降序排
    @SerializedName("sortType")
    private String sortType;
    //需要中断的播放会话键值。
// 说明： 如果因为播放会话数超限导致播放失败，用户可以选择中止其他会话的播放行为。
    @SerializedName("playSessionKey")
    private String playSessionKey;
    //是否返回HTTPS的URL。
    //取值包括：
    //0: 否
    //1: 是
    @SerializedName("isHTTPS")
    private int isHTTPS;

    public String getVODID() {
        return VODID;
    }

    public void setVODID(String VODID) {
        this.VODID = VODID;
    }

    public List<String> getMediaIDs() {
        return mediaIDs;
    }

    public void setMediaIDs(List<String> mediaIDs) {
        this.mediaIDs = mediaIDs;
    }

    public String getSeriesID() {
        return seriesID;
    }

    public void setSeriesID(String seriesID) {
        this.seriesID = seriesID;
    }

    public int getIDType() {
        return IDType;
    }

    public void setIDType(int IDType) {
        this.IDType = IDType;
    }

    public CheckLock getCheckLock() {
        return checkLock;
    }

    public void setCheckLock(CheckLock checkLock) {
        this.checkLock = checkLock;
    }

    public int getURLFormat() {
        return URLFormat;
    }

    public void setURLFormat(int URLFormat) {
        this.URLFormat = URLFormat;
    }

    public int getIsReturnProduct() {
        return isReturnProduct;
    }

    public void setIsReturnProduct(int isReturnProduct) {
        this.isReturnProduct = isReturnProduct;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getPlaySessionKey() {
        return playSessionKey;
    }

    public void setPlaySessionKey(String playSessionKey) {
        this.playSessionKey = playSessionKey;
    }

    public int getIsHTTPS() {
        return isHTTPS;
    }

    public void setIsHTTPS(int isHTTPS) {
        this.isHTTPS = isHTTPS;
    }
}
