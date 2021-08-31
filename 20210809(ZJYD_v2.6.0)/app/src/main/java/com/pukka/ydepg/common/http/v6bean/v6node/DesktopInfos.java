package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.utils.CollectionUtil;

import java.io.Serializable;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.huawei.chfjydvideo.common.http.v6bean.v6node.NamedParameter.java
 * @author: yh
 * @date: 2017-04-20 12:15
 */

public class DesktopInfos implements Serializable{

    /*桌面描述文件相对下载链接，终端需要拼接LoginRoute返回的vspURL拼接在前面得到描述文件访问的全路径。对于国内EPG模板获取不到vspURL，可以采用当前访问的VSC的地址进行拼接。
    如果该返回值相对于前一次的返回没有变化，说明对应的描述文件没有变化。
    成功匹配到时，该参数必填。*/
    @SerializedName("launcherLink")
    private String launcherLink;

    //桌面的ID，用于后续终端检查该桌面的最新数据。
    @SerializedName("desktopID")
    private String desktopID;

    public String getLauncherLink() {
        return launcherLink;
    }

    public void setLauncherLink(String launcherLink) {
        this.launcherLink = launcherLink;
    }

    public String getDesktopID() {
        return desktopID;
    }

    public void setDesktopID(String desktopID) {
        this.desktopID = desktopID;
    }
}
