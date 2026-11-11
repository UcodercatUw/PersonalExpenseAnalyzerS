package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.model.Expense;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonStorageService {
    // Using an absolute path to the project root directory
    private final String filePath = System.getProperty("user.dir") + File.separator + "expenses.json";
    private final ObjectMapper objectMapper;

    public JsonStorageService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void saveExpenses(List<Expense> expenses) {
        try {
            File file = new File(filePath);
            System.out.println(">>> SAVING FILE TO: " + file.getAbsolutePath());
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, expenses);
            System.out.println(">>> FILE SAVED SUCCESSFULLY! Items count: " + expenses.size());
        } catch (IOException e) {
            System.out.println(">>> ERROR SAVING JSON:");
            e.printStackTrace();
        }
    }

    public List<Expense> loadExpenses() {
        File file = new File(filePath);
        System.out.println(">>> LOADING FROM FILE: " + file.getAbsolutePath() + " (Exists: " + file.exists() + ")");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            List<Expense> loaded = objectMapper.readValue(file, new TypeReference<List<Expense>>() {});
            System.out.println(">>> SUCCESSFULLY LOADED items: " + loaded.size());
            return loaded;
        } catch (IOException e) {
            System.out.println(">>> ERROR READING FILE:");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}