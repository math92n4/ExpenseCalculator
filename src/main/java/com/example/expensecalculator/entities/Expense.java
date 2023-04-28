package com.example.expensecalculator.entities;

public class Expense {

    private int expenseID;
    private double expense;
    private String desc;
    private String date;


    public Expense(int expenseID, double expense, String desc, String date) {
        this.expenseID = expenseID;
        this.expense = expense;
        this.desc = desc;
        this.date = date;
    }

    public int getExpenseID() {
        return expenseID;
    }

    public void setExpenseID(int expenseID) {
        this.expenseID = expenseID;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
