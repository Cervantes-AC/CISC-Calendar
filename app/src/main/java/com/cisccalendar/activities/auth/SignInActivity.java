package com.cisccalendar.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cisccalendar.R;
import com.cisccalendar.firebase.FirebaseConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class SignInActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnSignIn;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        progressBar = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnSignIn.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(android.view.View.VISIBLE);

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser != null) {
                        redirectToDashboard(firebaseUser.getUid());
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(android.view.View.GONE);
                    Toast.makeText(SignInActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void redirectToDashboard(String uid) {
        db.collection(FirebaseConstants.COLLECTION_USERS)
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    progressBar.setVisibility(android.view.View.GONE);
                    if (documentSnapshot.exists()) {
                        String role = documentSnapshot.getString(FirebaseConstants.FIELD_ROLE);
                        Intent intent;
                        switch (role.toLowerCase()) {
                            case "student":
                                intent = new Intent(this, com.cisccalendar.activities.student.StudentDashboardActivity.class);
                                break;
                            case "faculty":
                                intent = new Intent(this, com.cisccalendar.activities.faculty.FacultyDashboardActivity.class);
                                break;
                            case "officer":
                                intent = new Intent(this, com.cisccalendar.activities.officer.OfficerDashboardActivity.class);
                                break;
                            case "admin":
                                intent = new Intent(this, com.cisccalendar.activities.admin.AdminDashboardActivity.class);
                                break;
                            default:
                                intent = new Intent(this, com.cisccalendar.activities.student.StudentDashboardActivity.class);
                        }
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "User profile not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(android.view.View.GONE);
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
