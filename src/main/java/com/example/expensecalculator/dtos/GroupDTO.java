package com.example.expensecalculator.dtos;

public class GroupDTO {

    private int groupID;
    private String name;
    private String desc;
    private String members;

    public GroupDTO(int groupID, String name, String desc, String members) {
        this.groupID = groupID;
        this.name = name;
        this.desc = desc;
        this.members = members;
    }

    public GroupDTO() {

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

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }
}
