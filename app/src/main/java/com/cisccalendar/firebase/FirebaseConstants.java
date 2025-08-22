package com.cisccalendar.firebase;

public class FirebaseConstants {

    // Collections
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_EVENTS = "events";
    public static final String COLLECTION_REMINDERS = "reminders";

    // User fields
    public static final String FIELD_NAME = "name";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_ROLE = "role";

    // Event fields
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_START_DATE = "startDate";
    public static final String FIELD_EVENT_TYPE = "eventType";
    public static final String FIELD_ORGANIZER = "organizer";
    public static final String FIELD_APPROVED = "approved";
    public static final String FIELD_LAYER = "layer";

    // Reminder fields
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_REMINDER_DATE = "reminderDate";
}
