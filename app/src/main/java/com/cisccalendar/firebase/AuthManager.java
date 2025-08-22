package com.cisccalendar.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class AuthManager {

    private final FirebaseAuth auth;

    public AuthManager() {
        auth = FirebaseAuth.getInstance();
    }

    // Sign Up
    public Task<AuthResult> signUp(String email, String password) {
        return auth.createUserWithEmailAndPassword(email, password);
    }

    // Sign In
    public Task<AuthResult> signIn(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }

    // Sign Out
    public void signOut() {
        auth.signOut();
    }

    // Get current user
    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    // Check if user is signed in
    public boolean isUserSignedIn() {
        return getCurrentUser() != null;
    }
}
