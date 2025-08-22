package com.cisccalendar.activities.shared;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cisccalendar.R;
import com.cisccalendar.firebase.FirebaseConstants;
import com.cisccalendar.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private EditText etName, etEmail, etRole;
    private Button btnUpdate;
    private ProgressBar progressBar;

    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etRole = findViewById(R.id.etRole);
        btnUpdate = findViewById(R.id.btnUpdate);
        progressBar = findViewById(R.id.progressBar);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        loadProfile();

        btnUpdate.setOnClickListener(v -> updateProfile());
    }

    private void loadProfile() {
        progressBar.setVisibility(android.view.View.VISIBLE);
        DocumentReference docRef = db.collection(FirebaseConstants.COLLECTION_USERS)
                .document(currentUser.getUid());

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            progressBar.setVisibility(android.view.View.GONE);
            if (documentSnapshot.exists()) {
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    etName.setText(user.getName());
                    etEmail.setText(user.getEmail());
                    etRole.setText(user.getRole());
                }
            } else {
                Toast.makeText(this, "Profile not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            progressBar.setVisibility(android.view.View.GONE);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void updateProfile() {
        String name = etName.getText().toString().trim();
        if (name.isEmpty()) {
            etName.setError("Name cannot be empty");
            return;
        }

        progressBar.setVisibility(android.view.View.VISIBLE);

        DocumentReference docRef = db.collection(FirebaseConstants.COLLECTION_USERS)
                .document(currentUser.getUid());

        docRef.update(FirebaseConstants.FIELD_NAME, name)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(android.view.View.GONE);
                    Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(android.view.View.GONE);
                    Toast.makeText(ProfileActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
