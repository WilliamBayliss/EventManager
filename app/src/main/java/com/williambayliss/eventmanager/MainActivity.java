package com.williambayliss.eventmanager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton newEventButton;
    CalendarView calendarView;
    public static EventTemplateDatabase eventTemplateDatabase;
    public static EventDatabase eventDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Creates Event Template Database
        eventTemplateDatabase = Room.databaseBuilder(getApplicationContext(), EventTemplateDatabase.class, "EventTemplates")
                .allowMainThreadQueries()
                .build();

//        Creates Event Database
        eventDatabase = Room.databaseBuilder(getApplicationContext(), EventDatabase.class, "Events")
                .allowMainThreadQueries()
                .build();

//        Selects calendar and button from layout
        calendarView = findViewById(R.id.calendar_view);
        newEventButton = findViewById(R.id.new_event_button);

//        Launches NewEventActivity
        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNewEventActivity();
            }
        });

//        Will launch EventListActivity for selected Date on click
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String selectedDate = dayOfMonth + "/" + month + "/" + year;
                launchEventListActivity(selectedDate);
            }
        });
    }


//    Launches NewEventActivity
    private void launchNewEventActivity() {
        Intent intent = new Intent(this, NewEventActivity.class);
        startActivity(intent);
    }

//    Launches EventListActivity
    private void launchEventListActivity(String date) {
        Intent intent = new Intent(this, EventListActivity.class);
        intent.putExtra("selectedDate", date);
        startActivity(intent);
    }
}