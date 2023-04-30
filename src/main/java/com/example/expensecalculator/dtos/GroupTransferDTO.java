package com.example.expensecalculator.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GroupTransferDTO {

    private double amountToShare;
    private Set<UserExpenseDTO> usersToShare;

    public GroupTransferDTO(double amountToShare, Set<UserExpenseDTO> usersToShare) {
        this.amountToShare = amountToShare;
        this.usersToShare = usersToShare;
    }

    public GroupTransferDTO() {

    }

    public double getAmountToBeShared() {
        return amountToShare / usersToShare.size();
    }

    public List<UserExpenseDTO> getUsersWithAmountUnder() {
        var amountToBeShared = getAmountToBeShared();
        return usersToShare.stream()
                .filter(user -> user.getExpense() < amountToBeShared)
                .toList();
    }

    public List<UserExpenseDTO> getUsersWithAmountOver() {
        var amountToBeShared = getAmountToBeShared();
        return usersToShare.stream()
                .filter(user -> user.getExpense() > amountToBeShared)
                .toList();
    }

    public List<TransferDTO> calculateTransfers() {
        var transfers = new ArrayList<TransferDTO>();
        var amountToBeShared = getAmountToBeShared();

        for(var userOver : getUsersWithAmountOver()) {
            var maxTransferAmount = userOver.getExpense() - amountToBeShared;

            for(var userUnder : getUsersWithAmountUnder()) {
                if(userUnder.getExpense() != amountToBeShared) {
                    var toTransfer = amountToBeShared - userUnder.getExpense();
                    if (toTransfer > maxTransferAmount) {
                        toTransfer = maxTransferAmount;
                    }
                    userUnder.addExpense(toTransfer);
                    userOver.deductExpense(toTransfer);
                    transfers.add(new TransferDTO(userUnder, userOver, toTransfer));
                }
            }
        }
        return transfers;
    }

    public double getAmountToShare() {
        return amountToShare;
    }

    public void setAmountToShare(double amountToShare) {
        this.amountToShare = amountToShare;
    }

    public Set<UserExpenseDTO> getUsersToShare() {
        return usersToShare;
    }

    public void setUsersToShare(Set<UserExpenseDTO> usersToShare) {
        this.usersToShare = usersToShare;
    }


}
