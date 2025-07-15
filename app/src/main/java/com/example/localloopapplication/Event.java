package com.example.localloopapplication;

/**
 * Event class represents an event entity in the application.
 * Stores event details such as organizer, name, description, category, fee, and date/time.
 */
public class Event {
    private String id; // Unique event ID (can be set after creation)
    private String organizerId; // Organizer's user ID
    private String name; // Event name
    private String description; // Event description
    private String categoryId; // Category identifier or name
    private double fee; // Participation fee
    private String dateTime; // Date and time of the event

    /**
     * Default constructor required for Firebase data mapping.
     * Do not remove!
     */
    public Event() {
        // Empty constructor for Firebase
    }

    /**
     * Constructs an Event with the given details (except ID, which can be set later).
     * @param organizerId ID of the event organizer
     * @param name Name of the event
     * @param description Description of the event
     * @param categoryId Event category (or category ID)
     * @param fee Participation fee
     * @param dateTime Date and time of the event
     */
    public Event(String organizerId, String name, String description,
                 String categoryId, double fee, String dateTime) {
        this.organizerId = organizerId;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.fee = fee;
        this.dateTime = dateTime;
    }

    // Getters and setters for all fields

    /** Gets the unique event ID */
    public String getId() { return id; }

    /** Sets the unique event ID */
    public void setId(String id) { this.id = id; }

    /** Gets the organizer's user ID */
    public String getOrganizerId() { return organizerId; }

    /** Sets the organizer's user ID */
    public void setOrganizerId(String organizerId) { this.organizerId = organizerId; }

    /** Gets the event name */
    public String getName() { return name; }

    /** Sets the event name */
    public void setName(String name) { this.name = name; }

    /** Gets the event description */
    public String getDescription() { return description; }

    /** Sets the event description */
    public void setDescription(String description) { this.description = description; }

    /** Gets the event category or category ID */
    public String getCategory() { return categoryId; }

    /** Sets the event category or category ID */
    public void setCategory(String category) { this.categoryId = category; }

    /** Gets the event participation fee */
    public double getFee() { return fee; }

    /** Sets the event participation fee */
    public void setFee(double fee) { this.fee = fee; }

    /** Gets the date and time of the event */
    public String getDateTime() { return dateTime; }

    /** Sets the date and time of the event */
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
}
