package com.example.expensecalculator.dtos;

import java.util.List;

public class GroupDTO {

    private int groupID;
    private String name;
    private String desc;
    private List<Integer> listOfUsers;

    public GroupDTO(int groupID, String name, String desc, List<Integer> listOfUsers) {
        this.groupID = groupID;
        this.name = name;
        this.desc = desc;
        this.listOfUsers = listOfUsers;
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

    public List<Integer> getListOfUsers() {
        return listOfUsers;
    }

    public void setListOfUsers(List<Integer> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }
}
