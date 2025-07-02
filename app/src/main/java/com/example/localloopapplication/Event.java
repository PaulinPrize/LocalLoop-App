package com.example.localloopapplication;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Event {
    
    /**
     * Instance variables
     **/
    
    // Unique identifier for the event
    private String id;
    // Name of the event
    private String name; 
    // Description of the event
    private String description; 
    // Category of the event 
    private String category; 
    // Participation of the event (equal to 0 if the event is free)
    private float participationFee;
    // Year of the event
    private int year;
    // Month of the event
    private int month;
    // Day of the event
    private int day;
    // Hour  of the event
    private int hour;
    // Minute of the event
    private int minute;
    
    /**
     * Constructors
     **/
    
    // Default constructor required for some frameworks (e.g., Firebase)
    public Event() {}

    // Parameterized constructor without fee as argument to create an event with all fields that is for free by default
    public Event(String id, String name, String description, String category, int year, int month, int day, int hour, int minute) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.participationFee = 0;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }
    // Parameterized constructor to create an event with all fields
    public Event(String id, String name, String description, String category, float participationFee, int year, int month, int day, int hour, int minute) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.participationFee = participationFee;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }
    
    /**
     * Getters and setters
     **/
    
    // Getter method for id
    public String getId() { return id; }
    // Getter method for name
    public String getName() { return name; }
    // Getter method for description
    public String getDescription() { return description; }
    // Getter method for Category
    public String getCategory() { return category; }
    // getter method for participationFee
    public float getParticipationFee() { return participationFee; }
    

    // Setter method for id
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Class methods
     **/
    
    public String StringDateAndTime(){
        LocalDateTime dt = LocalDateTime.of(year, month, day, hour, minute);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String formatted = dt.format(formatter);
        return formatted; // returns something of the form yyy/MM/dd HH:mm
    }
    
    
}