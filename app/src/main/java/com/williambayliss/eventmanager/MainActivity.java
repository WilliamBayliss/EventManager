package com.williambayliss.eventmanager;


import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton newEventButton;
    CalendarView calendarView;
    public static EventDatabase eventDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Creates Event Database
        eventDatabase = Room.databaseBuilder(getApplicationContext(), EventDatabase.class, "Events")
                .allowMainThreadQueries()
                .build();

//        Selects calendar and button from layout
        calendarView = findViewById(R.id.calendar_view);
        newEventButton = findViewById(R.id.new_event_button);

//        Launches NewEventActivity
        newEventButton.setOnClickListener(v -> launchNewEventActivity());

//        Will launch EventListActivity for selected Date on click
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            month = month + 1;
            String selectedDate = String.format(Locale.CANADA, "%02d/%02d/%04d" , dayOfMonth, month, year);
            launchEventListActivity(selectedDate);
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