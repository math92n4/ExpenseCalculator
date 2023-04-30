package com.example.expensecalculator.dtos;

public class TransferDTO {

    private UserExpenseDTO sender;
    private UserExpenseDTO receiver;
    private double amount;

    public TransferDTO(UserExpenseDTO sender, UserExpenseDTO receiver, double amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public String getSender() {
        return sender.getUserName();
    }

    public void setSender(UserExpenseDTO sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver.getUserName();
    }

    public void setReceiver(UserExpenseDTO receiver) {
        this.receiver = receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
