package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {
    private String id;
    private BigDecimal amount;
    private String category;
    private LocalDate date;
    private String description;

    public Expense() {}

    public Expense(String id, BigDecimal amount, String category, LocalDate date, String description) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return date + " | " + category + " | " + amount + " euro | " + description;
    }
}
