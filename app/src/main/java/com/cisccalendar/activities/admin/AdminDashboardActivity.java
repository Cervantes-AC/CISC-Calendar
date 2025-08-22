package com.cisccalendar.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cisccalendar.R;
import com.cisccalendar.adapters.EventAdapter;
import com.cisccalendar.firebase.FirebaseConstants;
import com.cisccalendar.models.Event;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private RecyclerView rvEvents;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private ProgressBar progressBar;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        rvEvents = findViewById(R.id.rvEvents);
        progressBar = findViewById(R.id.progressBar);

        db = FirebaseFirestore.getInstance();
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);

        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        rvEvents.setAdapter(eventAdapter);

        fetchAllEvents();
    }

    private void fetchAllEvents() {
        progressBar.setVisibility(View.VISIBLE);
        eventList.clear();

        db.collection(FirebaseConstants.COLLECTION_EVENTS)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Event event = doc.toObject(Event.class);
                            eventList.add(event);
                        }
                        eventAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AdminDashboardActivity.this, "Error fetching events", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Approve event
    private void approveEvent(Event event) {
        DocumentReference docRef = db.collection(FirebaseConstants.COLLECTION_EVENTS).document(event.getId());
        docRef.update(FirebaseConstants.FIELD_APPROVED, true)
                .addOnSuccessListener(aVoid -> Toast.makeText(AdminDashboardActivity.this, "Event approved", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(AdminDashboardActivity.this, "Approval failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Reject event
    private void rejectEvent(Event event) {
        DocumentReference docRef = db.collection(FirebaseConstants.COLLECTION_EVENTS).document(event.getId());
        docRef.delete()
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(AdminDashboardActivity.this, "Event rejected", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(AdminDashboardActivity.this, "Rejection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
