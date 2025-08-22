package com.cisccalendar.activities.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cisccalendar.R;
import com.cisccalendar.adapters.EventAdapter;
import com.cisccalendar.firebase.FirebaseConstants;
import com.cisccalendar.models.Event;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminManageEventsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEvents;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private ProgressBar progressBar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_events);

        recyclerViewEvents = findViewById(R.id.recyclerViewEvents);
        progressBar = findViewById(R.id.progressBar);

        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList, true); // true = admin mode
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEvents.setAdapter(eventAdapter);

        db = FirebaseFirestore.getInstance();

        loadAllEvents();
    }

    private void loadAllEvents() {
        progressBar.setVisibility(View.VISIBLE);

        db.collection(FirebaseConstants.COLLECTION_EVENTS)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        eventList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Event event = doc.toObject(Event.class);
                            eventList.add(event);
                        }
                        eventAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AdminManageEventsActivity.this,
                                "Error loading events: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
