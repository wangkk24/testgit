package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: CastRole.java
 * @author: yh
 * @date: 2017-04-21 17:27
 */

public class CastRole implements Serializable {


    /**
     * roleType : 11
     * casts : [""]
     */

    /**
     * 角色类型，取值包括：

     0：演员
     1：导演
     2：词曲作者
     3：演唱者
     4：出品人
     5：编剧
     6：解说员
     7：主办人
     8：化妆师
     9：音响师
     100：其他
     说明：
     对于MV类的视频VOD，Cast只支持singer，而且singer对应的roleType为0。
     对于音频VOD的Cast也只支持singer，singer对应的roleType为3，终端在使用该参数时需要注意。
     roleType为7，8和9目前仅节目单支持。

     */
    @SerializedName("roleType")
    private String roleType;

    /**
     * 艺术家信息。

     */
    @SerializedName("casts")
    private List<Cast> casts;

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public List<Cast> getCasts() {
        return casts;
    }

    public void setCasts(List<Cast> casts) {
        this.casts = casts;
    }

    public interface RoleType
    {
        String ACTOR = "0";

        String DIRECTOR = "1";

        String SONGWRITER = "2";

        String SINGER = "3";

        String PRODUCER = "4";

        String SCREENWRITER = "5";

        String NARRATOR = "6";

        String ORGANISER = "7";

        String MAKEUP_ARTIST = "8";

        String SOUND_ENGINEER = "9";

        String OTHER = "100";
    }
}
