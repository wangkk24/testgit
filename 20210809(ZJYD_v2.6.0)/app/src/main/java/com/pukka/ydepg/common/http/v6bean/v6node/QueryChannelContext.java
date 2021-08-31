package com.pukka.ydepg.common.http.v6bean.v6node;

import java.util.List;

/**
 * Created by YuanChao on 2017/5/16 0016.
 */

public class QueryChannelContext {
    private String channelNO;
    private String preNumber;
    private String nextNumber;
    private String contentType;
    private String channelNamespace;
    private ChannelFilter channelFilter;
    private List<NamedParameter> extensionFields;

    public String getChannelNO() {
        return channelNO;
    }

    public void setChannelNO(String channelNO) {
        this.channelNO = channelNO;
    }

    public String getPreNumber() {
        return preNumber;
    }

    public void setPreNumber(String preNumber) {
        this.preNumber = preNumber;
    }

    public String getNextNumber() {
        return nextNumber;
    }

    public void setNextNumber(String nextNumber) {
        this.nextNumber = nextNumber;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getChannelNamespace() {
        return channelNamespace;
    }

    public void setChannelNamespace(String channelNamespace) {
        this.channelNamespace = channelNamespace;
    }

    public ChannelFilter getChannelFilter() {
        return channelFilter;
    }

    public void setChannelFilter(ChannelFilter channelFilter) {
        this.channelFilter = channelFilter;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
