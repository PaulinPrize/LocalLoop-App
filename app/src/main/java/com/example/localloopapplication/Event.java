package com.example.localloopapplication;

public class Event {
    private String eventId;
    private String organizerId;
    private String name;
    private String description;
    private String category;
    private double fee;
    private String dateTime;
    private String imageUrl;



    public Event() {
        // here will hold and empty constructor for Firebase
    }

    public Event(String eventId, String organizerId, String name, String description,
                 String category, double fee, String dateTime, String imageUrl) {
        this.eventId = eventId;
        this.organizerId = organizerId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.fee = fee;
        this.dateTime = dateTime;
        this.imageUrl = imageUrl;
    }

    // Getters and setters for all fields, I will test it and see how it will work

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getOrganizerId() { return organizerId; }
    public void setOrganizerId(String organizerId) { this.organizerId = organizerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

}