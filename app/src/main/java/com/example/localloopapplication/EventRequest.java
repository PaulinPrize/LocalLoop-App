package com.example.localloopapplication;

public class EventRequest {
    private String id;         // 🔹 Clé Firebase (ajoutée)
    private String userId;
    private String eventId;
    private String status;     // ex : "pending", "approved", "rejected"

    // Constructeur vide requis pour Firebase
    public EventRequest() {}

    // Constructeur avec paramètres
    public EventRequest(String userId, String eventId, String status) {
        this.userId = userId;
        this.eventId = eventId;
        this.status = status;
    }

    // Getter et Setter pour id
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    // Getters et Setters classiques
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
