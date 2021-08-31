package com.pukka.ydepg.launcher.bean;

/**
 * 用户在机顶盒中的信息
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.bean.UserInfo.java
 * @date: 2018-01-09 17:01
 * @version: V1.0 描述当前版本功能
 */


public class UserInfo {
    //用户id
    private String userId;
    //vsp ip
    private String IP;
    //vsp port
    private String port;

    public UserInfo(String userId, String IP, String port) {
        this.userId = userId;
        this.IP     = IP;
        this.port   = port;
    }

    public UserInfo() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}