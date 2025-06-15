package com.example.localloopapplication;

// This class represents an event category with an ID, name, and description.
public class Category {
    private String id;
    private String name;
    private String description;
    private boolean active = true; //I want to track if the category is active

    public Category() {} // Firebase requires an empty constructor

    // Constructor to initialize a category with given values
    public Category(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = true;//Default to active when creating a new category
    }

    // Getter methods
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isActive() { return active; }//I want to ge a getter active status


    // Setter methods
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setActive(boolean active) { this.active = active; } // setter for the activity
}
