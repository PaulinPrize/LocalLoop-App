package com.example.localloopapplication;

public class Category {
    // Unique identifier for the category
    private String id;
    // Name of the category
    private String name;
    // Description of the category
    private String description;
    // Flag indicating whether the category is active or not
    private boolean isActive;

    // Default constructor required for some frameworks (e.g., Firebase)
    public Category() {}

    // Parameterized constructor to create a category with all fields
    public Category(String id, String name, String description, boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
    }

    // Getter method for id
    public String getId() { return id; }
    // Getter method for name
    public String getName() { return name; }
    // Getter method for description
    public String getDescription() { return description; }
    // Getter method for isActive flag
    public boolean isActive() { return isActive; }

    // Setter method for id
    public void setId(String id) {
        this.id = id;
    }
}
