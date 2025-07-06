package com.example.localloopapplication;

public class Event {
    private String organizerId;
    private String name;
    private String description;
    private String categoryId;
    private double fee;
    private String dateTime;
    private String eventId;


    public Event() {
        // here will hold and empty constructor for Firebase
    }

    public Event(String organizerId, String name, String description,
                 String categoryId, double fee, String dateTime, String eventId) {
        this.organizerId = organizerId;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.fee = fee;
        this.dateTime = dateTime;
        this.eventId = eventId;
    }

    // Getters and setters for all fields, I will test it and see how it will work

    public String getOrganizerId() { return organizerId; }
    public void setOrganizerId(String organizerId) { this.organizerId = organizerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return categoryId; }
    public void setCategory(String category) { this.categoryId = categoryId; }

    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    
    public String getId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
}