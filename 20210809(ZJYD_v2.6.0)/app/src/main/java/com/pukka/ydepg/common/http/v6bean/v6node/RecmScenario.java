package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: RecmScenario.java
 * @author: yh
 * @date: 2017-04-24 11:35
 */

public class RecmScenario {

    public static final String RECM_TYPE_CONTENT_SIMILARITY = "1";

    public static final String RECM_TYPE_USER_COLLABORATION = "3";

    public static final String VOD_TOP_PICKS_FOR_YOU = "VOD_Top_Picks_For_You";
    public static final String VOD_MORE_LIKE = "VOD_More_Like";
    public static final String VOD_BECAUSE_YOU_LIKE = "VOD_Because_You_Like";
    public static final String VOD_LIKE_MANY_OF_ITS_KIND = "VOD_Like_Many_of_its_Kind";
    /**
     * contentType :
     * businessType :
     * entrance :
     * recmType :
     * contentID :
     * userPreference :
     * count :
     * offset :
     * extensionFields :
     */

    /**
     *推荐内容类型，取值包括：

     VOD：点播内容
     CHANNEL：频道内容
     PROGRAM：节目单内容
     推荐业务类型为直播、回看时必填。如未传递，默认为VOD。

     其中，推荐业务类型为直播时：

     如推荐个性频道列表则推荐内容类型为CHANNEL；

     如推荐正在热播节目列表、即将播放节目列表则推荐内容类型为PROGRAM；

     */
    @SerializedName("contentType")
    private String contentType;

    /**
     *推荐业务类型，取值包括：

     VOD：点播
     BTV：直播
     CUTV：回看
     推荐业务类型为直播、回看时必填。如未传递，默认为点播。

     */
    @SerializedName("businessType")
    private String businessType;

    /**
     *推荐位/场景名称，具体名称由业务运营人员在视频大数据推荐系统管理页面配置。

     V6基线版本默认预制的推荐位/场景名称如下，如已预制的推荐位/场景名称不满足要求，业务运营人员可自行在视频大数据推荐系统管理页面新增：

     VOD_Top_Picks_For_You
     VOD_More_Like
     VOD_Because_You_Like
     VOD_Like_Many_of_its_Kind
     VOD_Viewers_also_Watched
     Channel_Preferred_LiveTV
     Program_Whats_On
     Program_Happy_Next
     Program_Similar_Recommendations
     Preferred_CatchupTV
     CatchupTV_Similar_Recommendations
     说明：
     推荐位/场景名称entrance、推荐类型recmType 2个字段必须传递1个字段，如2个都传递推荐系统默认按照推荐位/场景名称entrance匹配后台配置的推荐算法策略。
     V6基线版本默认需传递推荐位/场景名称entrance字段。
     */
    @SerializedName("entrance")
    private String entrance;

    /**
     *推荐类型，取值包括：

     0：内容协同推荐
     1：内容相似推荐
     2：用户偏好推荐
     3：用户协同推荐
     4：用户偏好收集
     5：正在热播节目
     说明：
     针对用户偏好收集场景，该字段需传递4：用户偏好收集，其他UI展示推荐位场景，如已传递推荐位/场景名称entrance字段，则该字段可选不用传递。

     */
    @SerializedName("recmType")
    private String recmType;

    /**
     *用户当前操作的内容ID。

     推荐类型为2、3、4和5时不填，0和1时必填。

     */
    @SerializedName("contentID")
    private String contentID;

    /**
     *1.用户偏好数据收集，当recType=4（用户偏好收集）时，此参数有效。

     2.用户偏好收集当前版本只支持VOD点播内容

     */
    @SerializedName("userPreference")
    private UserPreference userPreference;

    /**
     *一次查询的总条数，最大不超过30条。

     */
    @SerializedName("count")
    private String count;

    /**
     *询的起始位置。默认值为0，0表示从推荐结果的第1个开始获取。
     */
    @SerializedName("offset")
    private String offset;

    /**
     *扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public String getRecmType() {
        return recmType;
    }

    public void setRecmType(String recmType) {
        this.recmType = recmType;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public UserPreference getUserPreference() {
        return userPreference;
    }

    public void setUserPreference(UserPreference userPreference) {
        this.userPreference = userPreference;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
