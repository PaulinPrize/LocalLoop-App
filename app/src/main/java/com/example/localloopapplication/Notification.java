package com.example.localloopapplication;

/**
 * Event class represents an event entity in the application.
 * Stores event details such as organizer, name, description, category, fee, and date/time.
 */
public class Notification {
    private String notifId; // Unique notification ID (can be set after creation)
    private String organizerId; // Organizer's user ID
    private String participantId; // Participant's user ID
    private String text; // Notification text 
    private Event event;

    /**
     * Default constructor required for Firebase data mapping.
     * Do not remove!
     */
    public Notification() {
        // Empty constructor for Firebase
    }

    /**
     * Constructs an Event with the given details (except ID, which can be set later).
     * @param organizerId ID of the event organizer
     * @param name Name of the event
     * @param description Description of the event
     */
    public Notification(String notifId, String organizerId, String participantId, String text, Event event) {
        this.notifId=notifId;
        this.organizerId = organizerId;
        this.participantId = participantId;
        this.text = text;
        this.event=event;
    }

    // Getters and setters for all fields

    /** Gets the unique event ID */
    public String getNotifId() { return notifId; }

    /** Sets the unique event ID */
    public void setNotifId(String notifId) { this.notifId = notifId; }

    /** Gets the organizer's user ID */
    public String getOrganizerId() { return organizerId; }

    /** Sets the organizer's user ID */
    public void setOrganizerId(String organizerId) { this.organizerId = organizerId; }

    /** Gets the event name */
    public String getParticipantId() { return participantId; }

    /** Sets the event name */
    public void setParticipantId(String participantId) { this.participantId = participantId; }

    /** Gets the event description */
    public String getText() { return text; }

    /** Sets the event description */
    public void setText(String text) { this.text = text; }
    
    /** Gets the event */
    public String getEvent() { return text; }

    /** Sets the event */
    public void setEvent(Event event) { this.event=event; }

}