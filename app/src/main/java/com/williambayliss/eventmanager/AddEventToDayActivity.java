package com.williambayliss.eventmanager;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Locale;

public class AddEventToDayActivity extends NewEventActivity {
    private EditText eventTitleEditText;
    private EditText eventLocationEditText;
    private TextView setDateTextView;
    private TextView startTimeTextView;
    private TextView endTimeTextView;
    private ToggleButton saveTemplateToggle;

    private String eventTitle;
    private String eventLocation;
    private String eventDate;
    private String startTime;
    private String endTime;
    private Boolean saveTemplateToggleState;

    private String alertType = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event_activity);
        Intent intent = getIntent();

//        Gets selected calendar date from previous activity
        eventDate = intent.getExtras().getString("currentDate");

//        Assigning vars to layout items
        eventTitleEditText = findViewById(R.id.event_title);
        eventLocationEditText = findViewById(R.id.event_location);
        Button setDateButton = findViewById(R.id.date_button);
        setDateButton.setText("Date: ");
        setDateButton.setClickable(false);
        setDateButton.setFocusable(false);
        setDateTextView = findViewById(R.id.set_date_text_view);
        Button startTimeButton = findViewById(R.id.start_time_button);
        startTimeTextView = findViewById(R.id.start_time_text_view);
        Button endTimeButton = findViewById(R.id.end_time_button);
        endTimeTextView = findViewById(R.id.end_time_text_view);
        Button alertTypeButton = findViewById(R.id.alert_type_button);
        saveTemplateToggle = findViewById(R.id.save_template_button);
        Button loadFromTemplates = findViewById(R.id.load_from_template_button);
        Button addToCalendarButton = findViewById(R.id.add_to_calendar_button);

        setDateTextView.setText(eventDate);

        //            This function brings up a timePicker and sets the StartTimeTextView
//                  text to the user's selection
        startTimeButton.setOnClickListener(v -> {
                        Calendar currentTime = Calendar.getInstance();
                        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = currentTime.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog;
                        timePickerDialog = new TimePickerDialog(
                                AddEventToDayActivity.this,
                                (timePicker, selectedHour, selectedMinute) ->
                                        startTimeTextView.setText(
                                                String.format(Locale.CANADA,
                                                        "%02d:%02d",
                                                        selectedHour,
                                                        selectedMinute)),
                                hour,
                                minute,
                                true);
                        timePickerDialog.setTitle("Select Start Time");
                        timePickerDialog
                                .getWindow();
                        timePickerDialog.show();
                    });

        //            This function brings up a timePicker and sets the endTimeTextView
//                  text to the user's selection
        endTimeButton.setOnClickListener(v -> {
        //
                        Calendar currentTime = Calendar.getInstance();
                        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = currentTime.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog;
                        timePickerDialog = new TimePickerDialog(
                                AddEventToDayActivity.this,
                                (timePicker, selectedHour, selectedMinute) ->
                                        endTimeTextView.setText(
                                                String.format(
                                                        Locale.CANADA,
                                                        "%02d:%02d",
                                                        selectedHour,
                                                        selectedMinute)),
                                hour,
                                minute,
                                true);
                        timePickerDialog.setTitle("Select End Time");

                        timePickerDialog
                                .getWindow();
                        timePickerDialog.show();
                    });

        //        This function creates a popupmenu on alertTypeButton press to
//            save selection to String alertType
        alertTypeButton.setOnClickListener(v -> {
                        PopupMenu menu = new PopupMenu(getApplicationContext(), v);
                        menu.setOnMenuItemClickListener(item -> {
                            onMenuItemOptionClick(item);
                            return false;
                        });
                        menu.inflate(R.menu.alert_popup_menu);
                        menu.show();
                    });

        //            This function launches LoadTemplateActivity on loadFromTemplates butn click
        loadFromTemplates.setOnClickListener(v -> loadTemplate());

        addToCalendarButton.setOnClickListener(v -> {
//                Saves data to variables for columninfo entries
            eventTitle = eventTitleEditText.getText().toString();
            eventLocation = eventLocationEditText.getText().toString();
            saveTemplateToggleState = saveTemplateToggle.isChecked();
            eventDate = setDateTextView.getText().toString();
            startTime = startTimeTextView.getText().toString();
            endTime = endTimeTextView.getText().toString();

            if (eventTitle.length() == 0) {
                Toast.makeText(getApplicationContext(), "Error: Title field empty", Toast.LENGTH_SHORT).show();
            }
            else if (eventLocation.length() == 0) {
                Toast.makeText(getApplicationContext(), "Error: Location field empty", Toast.LENGTH_SHORT).show();
            }
            else if (eventDate.length() == 0) {
                Toast.makeText(getApplicationContext(), "Error: No date", Toast.LENGTH_SHORT).show();
            }
            else if (startTime.length() == 0) {
                Toast.makeText(getApplicationContext(), "Error: No start time", Toast.LENGTH_SHORT).show();
            }
            else if (endTime.length() == 0) {
                Toast.makeText(getApplicationContext(), "Error: No end time", Toast.LENGTH_SHORT).show();
            }
            else if (alertType.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Error: No alert type selected", Toast.LENGTH_SHORT).show();
            }
            else {


//                    Gather time info for event
                String eventTimeAndDate = startTime + ", " + eventDate;
                String eventEndTimeAndDate = endTime + ", " + eventDate;
                long notificationDelay = eventDateConverter(eventTimeAndDate);
                long endTimeInMillis = eventDateConverter(eventEndTimeAndDate);
//                    Checks that event is scheduled in the future
//                    if eventDateConverter returns a negative, it means that the start time is
//                    before the current time in millis
                if (notificationDelay < 0) {
                    Toast.makeText(getApplicationContext(), "Error: Event scheduled in the past", Toast.LENGTH_SHORT).show();
//                    If event end time value is smaller than start time value raises error
                } else if (notificationDelay > endTimeInMillis) {
                    Toast.makeText(getApplicationContext(), "Error: Event ends before it begins", Toast.LENGTH_SHORT).show();
                } else {
                    switch (alertType) {
                        case "At time of event":
                            scheduleNotification(
                                    buildNotification(
                                            eventTitle,
                                            eventLocation,
                                            eventTimeAndDate),
                                    notificationDelay);
                            break;
                        case "Five minutes before event":
                            notificationDelay = notificationDelay - (5 * 60 * 1000);
                            scheduleNotification(
                                    buildNotification(
                                            eventTitle,
                                            eventLocation,
                                            eventTimeAndDate),
                                    notificationDelay);
                            break;
                        case "Thirty minutes before event":
                            notificationDelay = notificationDelay - (30 * 60 * 1000);
                            scheduleNotification(
                                    buildNotification(
                                            eventTitle,
                                            eventLocation,
                                            eventTimeAndDate),
                                    notificationDelay);
                            break;
                        case "One hour before event":
                            notificationDelay = notificationDelay - (60 * 60 * 1000);
                            scheduleNotification(
                                    buildNotification(
                                            eventTitle,
                                            eventLocation,
                                            eventTimeAndDate),
                                    notificationDelay);
                            break;
                        case "One day before event":
                            notificationDelay = notificationDelay - ((60 * 24) * 60 * 1000);
                            scheduleNotification(
                                    buildNotification(
                                            eventTitle,
                                            eventLocation,
                                            eventTimeAndDate),
                                    notificationDelay);
                            break;
                        case "One week before event":
                            notificationDelay = notificationDelay - (((60 * 24) * 7) * 60 * 1000);
                            scheduleNotification(
                                    buildNotification(
                                            eventTitle,
                                            eventLocation,
                                            eventTimeAndDate),
                                    notificationDelay);
                            break;
                    }

                    //                Adds event to database and ends activity
                    saveEvent();

                    finish();
                }
            }
        });
    }

    private void onMenuItemOptionClick(MenuItem item) {
//        Popup text to show which alert type has been saved
        Toast.makeText(this, "Selected item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
//        Will assign selected menu option to String alertType
        switch (item.getItemId()) {
            case R.id.timeOfEvent:
                alertType = "At time of event";
                break;
            case R.id.fiveMinsBeforeEvent:
                alertType = "Five minutes before event";
                break;
            case R.id.halfHourBeforeEvent:
                alertType = "Thirty minutes before event";
                break;
            case R.id.hourBeforeEvent:
                alertType = "One hour before event";
                break;
            case R.id.oneDayBeforeEvent:
                alertType = "One day before event";
                break;
            case R.id.oneWeekBeforeEvent:
                alertType = "One week before event";
                break;
        }
    }

    private void saveEvent() {
//        Adds entry to Event Database
        MainActivity.eventDatabase.eventDao().create(eventTitle, eventLocation, eventDate, startTime, endTime, alertType, saveTemplateToggleState);
    }

    private void loadTemplate() {
//        Launches LoadTemplateActivity
        Intent intent = new Intent(getApplicationContext(), LoadTemplateActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                eventTitleEditText.setText(data.getStringExtra("TemplateTitle"));
                eventLocationEditText.setText(data.getStringExtra("TemplateLocation"));
                startTimeTextView.setText(data.getStringExtra("TemplateStartTime"));
                endTimeTextView.setText(data.getStringExtra("TemplateEndTime"));
                alertType = data.getStringExtra("TemplateAlertType");
                Log.e("Error", "Template Title: " + data.getStringExtra("TemplateTitle"));
            }
    }

}
