package com.cisccalendar.activities.faculty;

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

public class FacultyDashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList;
    private ProgressBar progressBar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_dashboard);

        recyclerView = findViewById(R.id.recyclerViewEvents);
        progressBar = findViewById(R.id.progressBar);

        eventList = new ArrayList<>();
        adapter = new EventAdapter(eventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadAcademicEvents();
    }

    private void loadAcademicEvents() {
        progressBar.setVisibility(View.VISIBLE);

        db.collection(FirebaseConstants.COLLECTION_EVENTS)
                .whereEqualTo(FirebaseConstants.FIELD_EVENT_TYPE, "academic")
                .whereEqualTo(FirebaseConstants.FIELD_APPROVED, true)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        eventList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Event event = doc.toObject(Event.class);
                            eventList.add(event);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(FacultyDashboardActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
