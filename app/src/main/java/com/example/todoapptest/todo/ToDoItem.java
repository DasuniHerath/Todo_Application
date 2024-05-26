package com.example.todoapptest.todo;

public class ToDoItem {
    private int username;
    private String title;
    private String description;
    private String dueDate;

    public ToDoItem(int username, String title, String description, String dueDate) {
        this.username = username;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    // Getters and setters
    public int getUsername() {
        return username;
    }

    public void setUsername(int username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}