package com.example.localloopapplication;

// This class represents an event category with an ID, name, and description.
public class Category {
    private String id;
    private String name;
    private String description;

    public Category() {} // Firebase requires an empty constructor

    // Constructor to initialize a category with given values
    public Category(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getter methods
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    // Setter methods
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
}
