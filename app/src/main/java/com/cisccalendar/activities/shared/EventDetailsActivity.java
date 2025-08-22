package com.cisccalendar.activities.shared;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.cisccalendar.R;
import com.cisccalendar.models.Event;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class EventDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT = "extra_event";

    private TextView tvTitle, tvDescription, tvDate, tvType, tvOrganizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvDate = findViewById(R.id.tvDate);
        tvType = findViewById(R.id.tvType);
        tvOrganizer = findViewById(R.id.tvOrganizer);

        Event event = (Event) getIntent().getSerializableExtra(EXTRA_EVENT);
        if (event != null) {
            displayEventDetails(event);
        }
    }

    private void displayEventDetails(Event event) {
        tvTitle.setText(event.getTitle());
        tvDescription.setText(event.getDescription());
        tvType.setText(event.getEventType());
        tvOrganizer.setText(event.getOrganizer());

        Timestamp timestamp = event.getStartDate();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
        tvDate.setText(sdf.format(timestamp.toDate()));
    }
}
