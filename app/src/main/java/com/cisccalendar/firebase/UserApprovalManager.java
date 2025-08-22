package com.cisccalendar.firebase;

import com.cisccalendar.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserApprovalManager {

    private final FirebaseFirestore db;

    public UserApprovalManager() {
        db = FirebaseFirestore.getInstance();
    }

    // Flag a user as pending approval
    public Task<Void> submitForApproval(User user) {
        user.setRole("pending"); // Set role as pending until approved
        return db.collection(FirebaseConstants.COLLECTION_USERS)
                .document(user.getId())
                .set(user);
    }

    // Approve user
    public Task<Void> approveUser(String userId, String role) {
        DocumentReference docRef = db.collection(FirebaseConstants.COLLECTION_USERS)
                .document(userId);
        return docRef.update(FirebaseConstants.FIELD_ROLE, role);
    }

    // Reject user
    public Task<Void> rejectUser(String userId) {
        return db.collection(FirebaseConstants.COLLECTION_USERS)
                .document(userId)
                .delete();
    }
}
