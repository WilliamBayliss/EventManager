package com.williambayliss.eventmanager;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddEventToDayActivity extends AppCompatActivity {
    private EditText eventTitleEditText;
    private EditText eventLocationEditText;
    private Button setDateButton;
    private TextView setDateTextView;
    private Button startTimeButton;
    private TextView startTimeTextView;
    private Button endTimeButton;
    private TextView endTimeTextView;
    private Button alertTypeButton;
    private ToggleButton saveTemplateToggle;
    private Button loadFromTemplates;
    private Button addToCalendarButton;

    private String eventTitle;
    private String eventLocation;
    private String eventDate;
    private String startTime;
    private String endTime;
    private Boolean saveTemplateToggleState;

    private String alertType;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event_activity);
        Intent intent = getIntent();
        eventDate = intent.getExtras().getString("currentDate");

        eventTitleEditText = findViewById(R.id.event_title);
        eventLocationEditText = findViewById(R.id.event_location);
        setDateButton = findViewById(R.id.date_button);
        setDateButton.setText("Date: ");
        setDateButton.setClickable(false);
        setDateButton.setFocusable(false);
        setDateTextView = findViewById(R.id.set_date_text_view);
        startTimeButton = findViewById(R.id.start_time_button);
        startTimeTextView = findViewById(R.id.start_time_text_view);
        endTimeButton = findViewById(R.id.end_time_button);
        endTimeTextView = findViewById(R.id.end_time_text_view);
        alertTypeButton = findViewById(R.id.alert_type_button);
        saveTemplateToggle = findViewById(R.id.save_template_button);
        loadFromTemplates = findViewById(R.id.load_from_template_button);
        addToCalendarButton = findViewById(R.id.add_to_calendar_button);

        setDateTextView.setText(eventDate);

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(AddEventToDayActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTimeTextView.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                timePickerDialog.setTitle("Select Start Time");
                timePickerDialog.show();
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(AddEventToDayActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endTimeTextView.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                timePickerDialog.setTitle("Select End Time");
                timePickerDialog.show();
            }
        });

        alertTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(getApplicationContext(), v);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        onMenuItemOptionClick(item);
                        return false;
                    }
                });
                menu.inflate(R.menu.alert_popup_menu);
                menu.show();
            }
        });

        loadFromTemplates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTemplate();
            }
        });

        addToCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventTitle = eventTitleEditText.getText().toString();
                eventLocation = eventLocationEditText.getText().toString();
                saveTemplateToggleState = saveTemplateToggle.isChecked();
                eventDate = setDateTextView.getText().toString();
                startTime = startTimeTextView.getText().toString();
                endTime = endTimeTextView.getText().toString();
                if (saveTemplateToggleState.equals(true)) {
                    saveEventTemplate();
                }
                saveEvent();
                finish();
            }
        });
    }

    private boolean onMenuItemOptionClick(MenuItem item) {
        Toast.makeText(this, "Selected item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.timeOfEvent:
                alertType = "At time of event";
                return true;
            case R.id.fiveMinsBeforeEvent:
                alertType = "Five minutes before event";
                return true;
            case R.id.halfHourBeforeEvent:
                alertType = "Thirty minutes before event";
                return true;
            case R.id.hourBeforeEvent:
                alertType = "One hour before event";
                return true;
            case R.id.oneDayBeforeEvent:
                alertType = "One day before event";
                return true;
            case R.id.oneWeekBeforeEvent:
                alertType = "One week before event";
                return true;
            default:
                return false;
        }
    }

    private void saveEventTemplate() {
        MainActivity.eventTemplateDatabase.eventTemplateDao().create(eventTitle, eventLocation, startTime, endTime, alertType);
    }
    private void saveEvent() {
        MainActivity.eventDatabase.eventDao().create(eventTitle, eventLocation, eventDate, startTime, endTime, alertType);
    }

    private void loadTemplate() {
        Intent intent = new Intent(getApplicationContext(), LoadTemplateActivity.class);
        startActivity(intent);
    }

}