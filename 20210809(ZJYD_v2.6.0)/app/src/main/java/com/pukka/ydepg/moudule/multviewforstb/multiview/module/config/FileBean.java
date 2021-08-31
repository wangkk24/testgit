package com.pukka.ydepg.moudule.multviewforstb.multiview.module.config;

public class FileBean {
    private String name;
    private String path;
    private boolean isFile;

    public FileBean() {
    }

    public FileBean(String name, String path, boolean isFile) {
        this.name = name;
        this.path = path;
        this.isFile = isFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }
}
