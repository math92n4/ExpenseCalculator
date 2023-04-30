package com.example.expensecalculator.dtos;

public class UserExpenseDTO {

    private int userID;
    private String userName;
    private double expense;


    public UserExpenseDTO(int userID, String userName, double expense) {
        this.userName = userName;
        this.expense = expense;
    }

    public UserExpenseDTO() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public void deductExpense(double expense) {
        this.expense -= expense;
    }

    public void addExpense(double expense) {
        this.expense += expense;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
