package org.example.service;

import org.example.model.Expense;
import java.util.List;

public class ExpenseService {
    private final JsonStorageService storageService;
    private final List<Expense> expenses;

    public ExpenseService() {
        this.storageService = new JsonStorageService();
        this.expenses = storageService.Load(); // если метод называется с заглавной, укажи Load()
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        storageService.save(expenses);
    }

    public List<Expense> getAllExpenses() {
        return expenses;
    }

    public boolean deleteExpense(int index) {
        if (index >= 0 && index < expenses.size()) {
            expenses.remove(index);
            storageService.save(expenses);
            return true;
        }
        return false;
    }

    public boolean updateExpense(int index, Expense updatedExpense) {
        if (index >= 0 && index < expenses.size()) {
            expenses.set(index, updatedExpense);
            storageService.save(expenses);
        }
        return false;
    }
}
