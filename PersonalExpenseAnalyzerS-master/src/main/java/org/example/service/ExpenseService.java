package org.example.service;

import org.example.model.Expense;
import java.util.List;

public class ExpenseService {
    private final JsonStorageService storageService;
    private List<Expense> expenses;

    public ExpenseService() {
        this.storageService = new JsonStorageService();
        this.expenses = storageService.loadExpenses(); // Загружаем при старте
    }

    public List<Expense> getAllExpenses() {
        return expenses;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        storageService.saveExpenses(expenses); // <--- ВАЖНО: сразу сохраняем в JSON!
    }

    public void updateExpense(int index, Expense expense) {
        if (index >= 0 && index < expenses.size()) {
            expenses.set(index, expense);
            storageService.saveExpenses(expenses); // <--- И здесь тоже!
        }
    }

    public boolean deleteExpense(int index) {
        if (index >= 0 && index < expenses.size()) {
            expenses.remove(index);
            storageService.saveExpenses(expenses); // <--- И здесь!
        }
        return false;
    }
}

