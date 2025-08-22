package com.cisccalendar.activities.student;

import android.os.Bundle;
import android.widget.CalendarView;
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
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StudentCalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView recyclerViewEvents;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_calendar);

        calendarView = findViewById(R.id.calendarView);
        recyclerViewEvents = findViewById(R.id.recyclerViewEvents);

        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEvents.setAdapter(eventAdapter);

        db = FirebaseFirestore.getInstance();

        loadEventsForDate(new Date(calendarView.getDate()));

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, dayOfMonth);
            loadEventsForDate(cal.getTime());
        });
    }

    private void loadEventsForDate(Date date) {
        eventList.clear();

        db.collection(FirebaseConstants.COLLECTION_EVENTS)
                .whereEqualTo(FirebaseConstants.FIELD_APPROVED, true)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Event event = doc.toObject(Event.class);
                            Timestamp ts = event.getStartDate();
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(ts.toDate());
                            Calendar selected = Calendar.getInstance();
                            selected.setTime(date);

                            if (cal.get(Calendar.YEAR) == selected.get(Calendar.YEAR) &&
                                    cal.get(Calendar.MONTH) == selected.get(Calendar.MONTH) &&
                                    cal.get(Calendar.DAY_OF_MONTH) == selected.get(Calendar.DAY_OF_MONTH)) {
                                eventList.add(event);
                            }
                        }
                        eventAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(StudentCalendarActivity.this,
                                "Error loading events: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
