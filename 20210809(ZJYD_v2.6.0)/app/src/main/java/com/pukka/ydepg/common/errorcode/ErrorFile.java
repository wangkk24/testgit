package com.pukka.ydepg.common.errorcode;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * read error code list from file
 *
 * @author wangqing 00188401
 */
public class ErrorFile {
    @SerializedName("error_code_list")
    private ArrayList<ErrorNode> errorNodeList;

    public ErrorFile() { }

    public ArrayList<ErrorNode> getErrorNodeList()
    {
        return errorNodeList;
    }

    public void setErrorNodeList(ArrayList<ErrorNode> errorNodeList) {
        this.errorNodeList = errorNodeList;
    }
}