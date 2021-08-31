package com.pukka.ydepg.launcher.bean;

import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.launcher.bean.node.SubjectVodsList;

import java.util.List;

/**
 * 首页fragment数据结构bean
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.bean.GroupElement.java
 * @date: 2017-12-15 15:01
 * @version: V1.0 首页fragment数据结构bean
 */


public class GroupElement {
    private int dataIndex;
    private List<Element> element;
    private Group group;
    private String name;

    private String type;//==-10,爱看界面的数据

    private List<SubjectVodsList> subjectVODLists;

    public List<SubjectVodsList> getSubjectVODLists() {
        return subjectVODLists;
    }

    public void setSubjectVODLists(List<SubjectVodsList> subjectVODLists) {
        this.subjectVODLists = subjectVODLists;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(int dataIndex) {
        this.dataIndex = dataIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupElement(List<Element> element, Group group) {
        this.element = element;
        this.group = group;
    }

    public GroupElement() {
    }



    public List<Element> getElement() {
        return element;
    }

    public void setElement(List<Element> element) {
        this.element = element;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

}
