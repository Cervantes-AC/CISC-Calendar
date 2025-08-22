package com.cisccalendar.firebase;

import com.cisccalendar.models.Event;
import com.cisccalendar.models.Reminder;
import com.cisccalendar.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirestoreManager {

    private final FirebaseFirestore db;

    public FirestoreManager() {
        db = FirebaseFirestore.getInstance();
    }

    // Users
    public Task<Void> addUser(User user) {
        return db.collection(FirebaseConstants.COLLECTION_USERS)
                .document(user.getId())
                .set(user);
    }

    public Task<QuerySnapshot> getAllUsers() {
        return db.collection(FirebaseConstants.COLLECTION_USERS).get();
    }

    // Events
    public Task<Void> addEvent(Event event) {
        return db.collection(FirebaseConstants.COLLECTION_EVENTS)
                .document(event.getId())
                .set(event);
    }

    public Task<QuerySnapshot> getAllEvents() {
        return db.collection(FirebaseConstants.COLLECTION_EVENTS).get();
    }

    public Task<Void> updateEventApproval(String eventId, boolean approved) {
        return db.collection(FirebaseConstants.COLLECTION_EVENTS)
                .document(eventId)
                .update(FirebaseConstants.FIELD_APPROVED, approved);
    }

    public Task<Void> deleteEvent(String eventId) {
        return db.collection(FirebaseConstants.COLLECTION_EVENTS)
                .document(eventId)
                .delete();
    }

    // Reminders
    public Task<Void> addReminder(Reminder reminder) {
        return db.collection(FirebaseConstants.COLLECTION_REMINDERS)
                .document(reminder.getId())
                .set(reminder);
    }

    public Task<QuerySnapshot> getUserReminders(String userId) {
        return db.collection(FirebaseConstants.COLLECTION_REMINDERS)
                .whereEqualTo(FirebaseConstants.FIELD_USER_ID, userId)
                .get();
    }
}
