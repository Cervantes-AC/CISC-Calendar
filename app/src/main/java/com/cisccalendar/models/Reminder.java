package com.cisccalendar.models;

import com.google.firebase.Timestamp;
import java.io.Serializable;

public class Reminder implements Serializable {

    private String id;
    private String userId;
    private String title;
    private Timestamp reminderDate;

    public Reminder() {}

    public Reminder(String id, String userId, String title, Timestamp reminderDate) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.reminderDate = reminderDate;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Timestamp getReminderDate() { return reminderDate; }
    public void setReminderDate(Timestamp reminderDate) { this.reminderDate = reminderDate; }
}
