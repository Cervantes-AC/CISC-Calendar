package com.cisccalendar.activities.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.cisccalendar.R;
import com.cisccalendar.adapters.EventAdapter;
import com.cisccalendar.firebase.FirebaseConstants;
import com.cisccalendar.models.Event;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class StudentDashboardActivity extends AppCompatActivity {

    private RecyclerView rvEvents;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private ProgressBar progressBar;
    private Spinner spinnerFilter;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        rvEvents = findViewById(R.id.rvEvents);
        progressBar = findViewById(R.id.progressBar);
        spinnerFilter = findViewById(R.id.spinnerFilter);

        db = FirebaseFirestore.getInstance();
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);

        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        rvEvents.setAdapter(eventAdapter);

        setupSpinner();
        fetchEvents(null); // fetch all events by default
    }

    private void setupSpinner() {
        String[] filters = {"All", "academic", "organization", "announcement"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filters);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter);

        spinnerFilter.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selected = filters[position];
                if (selected.equals("All")) selected = null;
                fetchEvents(selected);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                fetchEvents(null);
            }
        });
    }

    private void fetchEvents(String eventType) {
        progressBar.setVisibility(View.VISIBLE);
        eventList.clear();

        db.collection(FirebaseConstants.COLLECTION_EVENTS)
                .whereEqualTo(FirebaseConstants.FIELD_APPROVED, true)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Event event = doc.toObject(Event.class);
                            if (eventType == null || event.getEventType().equalsIgnoreCase(eventType)) {
                                eventList.add(event);
                            }
                        }
                        eventAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(StudentDashboardActivity.this, "Error fetching events", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
