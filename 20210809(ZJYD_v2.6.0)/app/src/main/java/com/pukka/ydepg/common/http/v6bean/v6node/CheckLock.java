package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: CheckLock.java
 * @author: yh
 * @date: 2017-04-21 16:27
 */

public class CheckLock {


    /**
     * checkType : 123
     * password :
     * type : 123
     */

    /**
     *检查类型，取值包括：

     0：检查父母字和加锁
     1：检查解锁密码
     */
    @SerializedName("checkType")
    private String checkType;

    /**
     *如果checkType=1，此字段必填，表示用户输入的解锁密码，所传递的密码以明文方式传递。

     */
    @SerializedName("password")
    private String password;

    /**
     *如果checkType=1，此字段必填，表示解密密码的密码类型，取值包括：

     0：Profile密码
     1：订购密码
     2：订户密码
     3：Admin Profile密码
     4：父母控制密码
     */
    @SerializedName("type")
    private String type;

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
