package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: QueryPlaybill.java
 * @author: yh
 * @date: 2017-04-25 09:48
 */

public class QueryPlaybill implements Serializable {


    /**
     * type :
     * startTime :
     * endTime :
     * mustIncluded :
     * count :
     * offset :
     * isFillProgram :
     * playbillFilter :
     * playbillExcluder :
     * sortType :
     * extensionFields :
     */

    /**
     *节目类型。

     取值范围：

     0：直播节目，包括过期的、当前的和未来的直播节目
     1：直播节目，包括当前的和未来的节目，不包括过期的节目
     2：直播节目，包括历史的成功录制的Catch-up TV节目、当前的直播节目和未来的直播节目
     3：Catch-up TV直播节目，只包含已录制成功的节目
     4：Catch-up TV直播节目，包括历史的Catch-up TV节目（含录制成功或者录制失败）、当前的Catch-up TV直播节目和未来的Catch-up TV直播节目
     */
    @SerializedName("type")
    private String type;

    /**
     *取节目单的开始时间，取值为距离1970年1月1号的毫秒数。

     根据终端要求，此值不填充，则平台自动填充为当前时间。

     */
    @SerializedName("startTime")
    private String startTime;

    /**
     *获取节目单的结束时间，取值为距离1970年1月1号的毫秒数。

     根据与终端协商，此值不填充，则平台默认为基于startTime延后24小时。

     */
    @SerializedName("endTime")
    private String endTime;

    /**
     *节目单是否全包含在startTime和 endTime区间内。

     0：否，允许节目跨startTime或endTime。

     1：是，只返回完全在startTime和 endTime区间内的节目，不返回跨startTime或endTime的节目。

     */
    @SerializedName("mustIncluded")
    private String mustIncluded;

    /**
     *一次查询的节目的总条数，不能设置为-1，调用者一定要指定获取数据的总条数，最大条数默认不超过100，最大条数可配置，超过最大条数返回错误。

     */
    @SerializedName("count")
    private String count;

    /**
     *查询的起始位置。默认值为0，表示从第一个节目开始查询。

     */
    @SerializedName("offset")
    private String offset;

    /**
     *是否自动填充节目。

     取值范围：

     0：不填充
     1：按照整点填充
     默认值为0。

     当type取值为0、1，且不支持playbillFilters和playbillExcluders时才有效。

     例如：isFillProgram=1，startTime=20120305080000

     如果7:40点到9点没有节目，则VSP构造两个节目（id规则为"@@"+channelID+"@@"+starttime(14位的年月日时分秒)），起止时间分别为7:40~8:00和8:00~900的两个节目。节目名称为""(即长度为0的字符串)。

     说明：
     1. 平台侧有配置项，如果配置为不允许返回高级别的内容，则节目单可能被过滤，此时如果终端要求填充，则填充类型为2。

     2. 如果平台填充节目单，则填充节目单也参与offset、count计算。

     */
    @SerializedName("isFillProgram")
    private String isFillProgram;

    /**
     *节目单搜索条件。

     */
    @SerializedName("playbillFilter")
    private PlaybillFilter playbillFilter;

    /**
     *节目单排他条件。

     说明：
     如果playbillFilter和playbillExcluder中包含相同的条件，则以playbillFilter中包含的为准，忽略playbillExcluder中的条件。

     */
    @SerializedName("playbillExcluder")
    private PlaybillExcluder playbillExcluder;

    /**
     *节目单排序方式。以下列方式指定排序方式：

     PLAYTIMES:ASC：按节目单点击率升序排序，PLAYTIMES:DESC：按节目单点击率降序排序
     NAME:ASC：按节目单名称升序排序，NAME:DESC：按节目单名称降序排序
     STARTTIME:ASC：按节目单开始时间升序排序，STARTTIME:DESC：按节目单开始时间降序排序，如果节目单开始时间相同，则按照节目单ID排列
     如果不指定，按平台默认实现处理，即按照STARTTIME:ASC处理。

     */
    @SerializedName("sortType")
    private String sortType;

    /**
     *扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMustIncluded() {
        return mustIncluded;
    }

    public void setMustIncluded(String mustIncluded) {
        this.mustIncluded = mustIncluded;
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

    public String getIsFillProgram() {
        return isFillProgram;
    }

    public void setIsFillProgram(String isFillProgram) {
        this.isFillProgram = isFillProgram;
    }

    public PlaybillFilter getPlaybillFilter() {
        return playbillFilter;
    }

    public void setPlaybillFilter(PlaybillFilter playbillFilter) {
        this.playbillFilter = playbillFilter;
    }

    public PlaybillExcluder getPlaybillExcluder() {
        return playbillExcluder;
    }

    public void setPlaybillExcluder(PlaybillExcluder playbillExcluder) {
        this.playbillExcluder = playbillExcluder;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
