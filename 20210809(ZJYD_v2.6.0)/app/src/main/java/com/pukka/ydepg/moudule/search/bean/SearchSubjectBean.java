package com.pukka.ydepg.moudule.search.bean;

import java.io.Serializable;
import java.util.List;

public class SearchSubjectBean implements Serializable {

    private String id;

    private String subjectName;

    private String subjectIDs;

    private int subjectType;

    private List<String> filterSubjectIDs;

    //栏目类型
    public interface SubjectType{
        Integer COMMON = 0; //0: 普通
        Integer MIGU   = 1; //1: 咪咕
        Integer ACTOR  = 2; //2: 演员
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(int subjectType) {
        this.subjectType = subjectType;
    }

    public String getSubjectID() {
        return subjectIDs;
    }

    public void setSubjectID(String subjectID) {
        this.subjectIDs = subjectID;
    }

    public List<String> getFilterSubjectIDs() {
        return filterSubjectIDs;
    }

    public void setFilterSubjectIDs(List<String> filterSubjectIDs) {
        this.filterSubjectIDs = filterSubjectIDs;
    }
}
