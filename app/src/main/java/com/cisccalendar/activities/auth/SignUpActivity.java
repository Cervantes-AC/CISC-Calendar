package com.cisccalendar.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.cisccalendar.R;
import com.cisccalendar.firebase.FirebaseConstants;
import com.cisccalendar.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Spinner spRole;
    private Button btnSignUp;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        spRole = findViewById(R.id.spRole);
        btnSignUp = findViewById(R.id.btnSignUp);
        progressBar = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnSignUp.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String role = spRole.getSelectedItem().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(android.view.View.VISIBLE);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser != null) {
                        saveUserToFirestore(firebaseUser.getUid(), name, email, role);
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(android.view.View.GONE);
                    Toast.makeText(SignUpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveUserToFirestore(String uid, String name, String email, String role) {
        User user = new User(uid, name, email, role);

        db.collection(FirebaseConstants.COLLECTION_USERS)
                .document(uid)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(android.view.View.GONE);
                    Toast.makeText(SignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    redirectToDashboard(role);
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(android.view.View.GONE);
                    Toast.makeText(SignUpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void redirectToDashboard(String role) {
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
    }
}
