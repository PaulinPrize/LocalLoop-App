package com.example.localloopapplication;

public class Category {
    private String id;
    private String name;
    private String description;
    private boolean isActive;

    public Category() {}

    public Category(String id, String name, String description, boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isActive() { return isActive; }


    public void setId(String id) {
        this.id = id;
    }
}
