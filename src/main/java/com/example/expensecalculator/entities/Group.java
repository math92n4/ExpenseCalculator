package com.example.expensecalculator.entities;

public class Group {

    private int groupID;
    private String name;
    private String desc;

    public Group(int groupID, String name, String desc) {
        this.groupID = groupID;
        this.name = name;
        this.desc = desc;
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
}
