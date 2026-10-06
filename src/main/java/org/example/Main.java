package org.example;

import org.example.model.Expense;
import org.example.service.ExpenseService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpenseService service = new ExpenseService();

        System.out.println("Personal Expense Analyzer");

        while (true) {
            System.out.println("Select an action:");
            System.out.println("1. Add an expense");
            System.out.println("2. Show all expenses");
            System.out.println("3. Exit:");
            System.out.println("> ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addExpenseMenu(scanner, service);
                case "2" -> printAllExpenses(service);
                case "3" -> {
                    System.out.println("Bye");
                    return;
                }
                default  -> System.out.println("Invalid command. Try again.");
            }
        }
    }

    private static void addExpenseMenu(Scanner scanner, ExpenseService service) {
        System.out.print("Enter the amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        System.out.print("Enter the category (Food, transport and other): ");
        String category = scanner.nextLine();
        System.out.print("Enter the description: ");
        String description = scanner.nextLine();
        Expense expense = new Expense(
                UUID.randomUUID().toString(),
                amount,
                category,
                LocalDate.now(),
                description
        );
        service.addExpense(expense);
        System.out.println("Successfully added!");
    }

    private static void printAllExpenses(ExpenseService service) {
        var list = service.getAllExpenses();
        if (list.isEmpty()) {
            System.out.println("The list of expenses is currently empty.");
            return;
        }
        System.out.println("Your expenses");
        for (Expense e : list) {
            System.out.println(e);
        }
    }
}