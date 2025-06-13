package com.example.localloopapplication;

public class Category {
    private static String id;
    private static String name;
    private static String description;
    private boolean active;

    public Category() {}

    public Category(String id, String name, String description, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
    }

    public static String getId() { return id; }
    public static String getName() { return name; }
    public static String getDescription() { return description; }
    public boolean isActive() { return active; }
}
