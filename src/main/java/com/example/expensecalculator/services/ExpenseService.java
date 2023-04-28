package com.example.expensecalculator.services;

import com.example.expensecalculator.entities.User;
import com.example.expensecalculator.repositories.ExpenseRepository;
import com.example.expensecalculator.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ExpenseService {

    private ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }



}