package com.williambayliss.eventmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton newEventButton;
    CalendarView calendarView;
    public static EventTemplateDatabase eventTemplateDatabase;
    public static EventDatabase eventDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventTemplateDatabase = Room.databaseBuilder(getApplicationContext(), EventTemplateDatabase.class, "EventTemplates")
                .allowMainThreadQueries()
                .build();

        eventDatabase = Room.databaseBuilder(getApplicationContext(), EventDatabase.class, "Events")
                .allowMainThreadQueries()
                .build();

        calendarView = findViewById(R.id.calendar_view);
        newEventButton = findViewById(R.id.new_event_button);

        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNewEventActivity();
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String selectedDate = dayOfMonth + "/" + month + "/" + year;
                launchEventListActivity(selectedDate);
            }
        });
    }

    private void launchNewEventActivity() {
        Intent intent = new Intent(this, NewEventActivity.class);
        startActivity(intent);
    }

    private void launchEventListActivity(String date) {
        Intent intent = new Intent(this, EventListActivity.class);
        intent.putExtra("selectedDate", date);
        startActivity(intent);
    }
}