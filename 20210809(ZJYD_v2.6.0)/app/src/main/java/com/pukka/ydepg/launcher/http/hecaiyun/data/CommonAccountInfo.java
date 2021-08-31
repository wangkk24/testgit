package com.pukka.ydepg.launcher.http.hecaiyun.data;

public class CommonAccountInfo {

    private String account;
    private String accountType;

    public CommonAccountInfo(String account, String accountType) {
        this.account     = account;
        this.accountType = accountType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
