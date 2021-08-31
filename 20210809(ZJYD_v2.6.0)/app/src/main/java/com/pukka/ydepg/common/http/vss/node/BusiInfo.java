package com.pukka.ydepg.common.http.vss.node;

import com.google.gson.annotations.SerializedName;

public class BusiInfo {

    @SerializedName("BILL_ID")
    private String billId;

    //查询话费余额时选填
    @SerializedName("ACCT_ID")
    private String acctId;

    //查询话费余额时选填
    @SerializedName("BANK_ACCOUNT")
    private String bankAccount;

    //查询积分余额时选填
    @SerializedName("START_DATE")
    private String START_DATE;

    //查询积分余额时选填
    @SerializedName("END_DATE")
    private String END_DATE;

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getSTART_DATE() {
        return START_DATE;
    }

    public void setSTART_DATE(String START_DATE) {
        this.START_DATE = START_DATE;
    }

    public String getEND_DATE() {
        return END_DATE;
    }

    public void setEND_DATE(String END_DATE) {
        this.END_DATE = END_DATE;
    }
}
