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
            System.out.println("3. Delete an expense");
            System.out.println("4. Edit an expense");
            System.out.println("5. Exit:");
            System.out.print("> ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addExpenseMenu(scanner, service);
                case "2" -> printAllExpenses(service);
                case "3" -> deleteExpenseMenu(scanner, service);
                case "4" -> editExpenseMenu(scanner, service);
                case "5" -> {
                    System.out.println("Bye");
                    return;
                }
                default -> System.out.println("Invalid command. Try again.");
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

        service.addExpense(new Expense(UUID.randomUUID().toString(), amount, category, LocalDate.now(), description));
        System.out.println("Successfully added!");
    }

    private static void printAllExpenses(ExpenseService service) {
        var list = service.getAllExpenses();
        if (list.isEmpty()) {
            System.out.println("The list of expenses is currently empty.");
            return;
        }
        System.out.println("Your expenses");
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ". " + list.get(i));
        }
    }

    private static void deleteExpenseMenu(Scanner scanner, ExpenseService service) {
        printAllExpenses(service);
        if (service.getAllExpenses().isEmpty()) return;

        System.out.print("Enter number to delete: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (service.deleteExpense(index)) {
            System.out.println("Successfully deleted!");
        } else {
            System.out.println("Invalid number.");
        }
    }

    private static void editExpenseMenu(Scanner scanner, ExpenseService service) {
        printAllExpenses(service);
        var list = service.getAllExpenses();
        if (list.isEmpty()) return;

        System.out.print("Enter number to edit: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index < 0 || index >= list.size()) {
            System.out.println("Invalid number.");
            return;
        }

        Expense old = list.get(index);
        System.out.print("Enter amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        service.updateExpense(index, new Expense(old.getId(), amount, category, old.getDate(), description));
        System.out.println("Successfully updated!");
    }
}