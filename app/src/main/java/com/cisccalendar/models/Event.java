package com.cisccalendar.models;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Event implements Serializable {

    private String id;
    private String title;
    private String description;
    private Timestamp startDate;
    private String eventType; // "academic", "organization", "announcement"
    private String organizer;
    private boolean approved;
    private String layer; // Optional: "academic", "organization", "announcement"

    public Event() {}

    public Event(String id, String title, String description, Timestamp startDate, String eventType, String organizer, boolean approved, String layer) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.eventType = eventType;
        this.organizer = organizer;
        this.approved = approved;
        this.layer = layer;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getStartDate() { return startDate; }
    public void setStartDate(Timestamp startDate) { this.startDate = startDate; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getOrganizer() { return organizer; }
    public void setOrganizer(String organizer) { this.organizer = organizer; }

    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }

    public String getLayer() { return layer; }
    public void setLayer(String layer) { this.layer = layer; }
}
