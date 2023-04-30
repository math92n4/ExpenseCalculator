package com.example.expensecalculator.entities;

public class Expense {

    private int expenseID;
    private double expense;


    public Expense(int expenseID, double expense) {
        this.expenseID = expenseID;
        this.expense = expense;
    }

    public Expense() {

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
}
