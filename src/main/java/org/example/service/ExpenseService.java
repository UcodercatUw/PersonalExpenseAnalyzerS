package org.example.service;

import org.example.model.Expense;
import java.util.ArrayList;
import java.util.List;

public class ExpenseService {
    private final List<Expense> expenses = new ArrayList<>();

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public List<Expense> getAllExpenses() {
        return expenses;
    }
}
