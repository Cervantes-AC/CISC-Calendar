package com.cisccalendar.activities.student;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cisccalendar.R;
import com.cisccalendar.adapters.ReminderAdapter;
import com.cisccalendar.firebase.FirebaseConstants;
import com.cisccalendar.models.Reminder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentRemindersActivity extends AppCompatActivity {

    private RecyclerView recyclerViewReminders;
    private ReminderAdapter reminderAdapter;
    private List<Reminder> reminderList;
    private ProgressBar progressBar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_reminder);

        recyclerViewReminders = findViewById(R.id.recyclerViewReminders);
        progressBar = findViewById(R.id.progressBar);

        reminderList = new ArrayList<>();
        reminderAdapter = new ReminderAdapter(reminderList);
        recyclerViewReminders.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReminders.setAdapter(reminderAdapter);

        db = FirebaseFirestore.getInstance();

        loadStudentReminders();
    }

    private void loadStudentReminders() {
        progressBar.setVisibility(View.VISIBLE);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection(FirebaseConstants.COLLECTION_REMINDERS)
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        reminderList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Reminder reminder = doc.toObject(Reminder.class);
                            reminderList.add(reminder);
                        }
                        reminderAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(StudentReminderActivity.this,
                                "Error loading reminders: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
