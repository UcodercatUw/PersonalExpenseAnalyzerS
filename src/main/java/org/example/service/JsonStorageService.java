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
    private final String filePath = "expenses.json";
    private final ObjectMapper objectMapper;

    public JsonStorageService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void save(List<Expense> expenses) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), expenses);
        } catch (IOException e) {
                System.out.println("Error saving expenses to file: " + e.getMessage());
        }
    }

    public List<Expense> Load() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(file, new TypeReference<List<Expense>>() {});
        } catch (IOException e) {
            System.out.println("Error reading file, creating a new list: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
