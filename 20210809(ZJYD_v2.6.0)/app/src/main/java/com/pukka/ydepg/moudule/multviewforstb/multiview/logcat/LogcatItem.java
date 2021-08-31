package com.pukka.ydepg.moudule.multviewforstb.multiview.logcat;

public class LogcatItem {
    private String logcat;
    private boolean isError;

    public LogcatItem(String logcat) {
        this.logcat = logcat;
    }

    public LogcatItem(String logcat, boolean isError) {
        this.logcat = logcat;
        this.isError = isError;
    }

    public String getLogcat() {
        return logcat;
    }

    public void setLogcat(String logcat) {
        this.logcat = logcat;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }
}
