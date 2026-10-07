package org.example.service;

import org.example.model.Expense;
import java.util.List;

public class ExpenseService {
    private final JsonStorageService storageService;
    private final List<Expense> expenses;

    public ExpenseService() {
        this.storageService = new JsonStorageService();
        this.expenses = storageService.Load();
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        storageService.save(expenses);
    }

    public List<Expense> getAllExpenses() {
        return expenses;
    }
}
