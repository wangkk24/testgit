package com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.bean;

public class SrvResponse {
    private Result result;

    public SrvResponse() {
    }

    public Result getResult() {
        return this.result;
    }

    public void setResult(Result result) {
        this.result = result;
    }


    public static class Result {
        private String retCode;
        private String retMsg;

        public Result() {
        }

        public String getRetCode() {
            return this.retCode;
        }

        public void setRetCode(String retCode) {
            this.retCode = retCode;
        }

        public String getRetMsg() {
            return this.retMsg;
        }

        public void setRetMsg(String retMsg) {
            this.retMsg = retMsg;
        }
    }
}