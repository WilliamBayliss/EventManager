package com.williambayliss.eventmanager;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NewEventActivity extends AppCompatActivity {
    Button setDateButton;
    Button startTimeButton;
    Button endTimeButton;
    Button alertTypeButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setDateButton = findViewById(R.id.date_button);
        startTimeButton = findViewById(R.id.start_time_button);
        endTimeButton = findViewById(R.id.end_time_button);
        alertTypeButton = findViewById(R.id.alert_type_button);
    }
}
