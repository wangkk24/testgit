package com.pukka.ydepg.common.http.bean.node;

import com.pukka.ydepg.launcher.bean.node.NamedParameter;

import java.util.List;

/**
 * Created by liudo on 2018/9/17.
 */

public class User
{
    private int type=1;

    private String id;

    private List<NamedParameter> extensionInfo;

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public List<NamedParameter> getExtensionInfo()
    {
        return extensionInfo;
    }

    public void setExtensionInfo(List<NamedParameter> extensionInfo)
    {
        this.extensionInfo = extensionInfo;
    }
}
