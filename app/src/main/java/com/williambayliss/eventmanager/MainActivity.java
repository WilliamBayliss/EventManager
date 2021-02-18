package com.williambayliss.eventmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton newEventButton;
    CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                launchEventListActivity();
            }
        });
    }

    private void launchNewEventActivity() {
        Intent intent = new Intent(this, NewEventActivity.class);
        startActivity(intent);
    }

    private void launchEventListActivity() {
        Intent intent = new Intent(this, EventListActivity.class);
        startActivity(intent);
    }
}