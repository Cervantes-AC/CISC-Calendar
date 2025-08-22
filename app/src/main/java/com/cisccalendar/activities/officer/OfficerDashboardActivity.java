package com.cisccalendar.activities.officer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cisccalendar.R;
import com.cisccalendar.adapters.EventAdapter;
import com.cisccalendar.firebase.FirebaseConstants;
import com.cisccalendar.models.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OfficerDashboardActivity extends AppCompatActivity {

    private RecyclerView rvEvents;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private ProgressBar progressBar;

    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_dashboard);

        rvEvents = findViewById(R.id.rvEvents);
        progressBar = findViewById(R.id.progressBar);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);

        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        rvEvents.setAdapter(eventAdapter);

        fetchOfficerEvents();
    }

    private void fetchOfficerEvents() {
        progressBar.setVisibility(View.VISIBLE);
        eventList.clear();

        // Fetch organization events for all approved events
        db.collection(FirebaseConstants.COLLECTION_EVENTS)
                .whereEqualTo(FirebaseConstants.FIELD_EVENT_TYPE, "organization")
                .whereEqualTo(FirebaseConstants.FIELD_APPROVED, true)
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
                        Toast.makeText(OfficerDashboardActivity.this, "Error fetching events", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
