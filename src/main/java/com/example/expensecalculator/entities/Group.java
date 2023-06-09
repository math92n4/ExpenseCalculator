package com.example.expensecalculator.entities;

import java.util.List;

public class Group {

    private int groupID;
    private String name;
    private String desc;
    private List<String> members;

    public Group(int groupID, String name, String desc, List<String> members) {
        this.groupID = groupID;
        this.name = name;
        this.desc = desc;
        this.members = members;
    }

    public Group(int groupID, String name, String desc) {
        this.groupID = groupID;
        this.name = name;
        this.desc = desc;
    }

    public Group() {
    }


    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
