package com.williambayliss.eventmanager;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NewEventActivity extends AppCompatActivity {
    Button setDateButton;
    Button startTimeButton;
    Button endTimeButton;
    Button alertTypeButton;
    ToggleButton saveTemplateToggle;
    Button addToCalendarButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setDateButton = findViewById(R.id.date_button);
        startTimeButton = findViewById(R.id.start_time_button);
        endTimeButton = findViewById(R.id.end_time_button);
        alertTypeButton = findViewById(R.id.alert_type_button);
        saveTemplateToggle = findViewById(R.id.save_template_button);
        addToCalendarButton = findViewById(R.id.add_to_calendar_button);
    }
}
