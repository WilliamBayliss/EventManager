package com.williambayliss.eventmanager;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
        setDateButton.setText(getString(R.string.add_event_date_button_text));
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
                                this,
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
                                this,
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

        //            This function launches LoadTemplateActivity on loadFromTemplates button click
        loadFromTemplates.setOnClickListener(v -> loadTemplate());

        addToCalendarButton.setOnClickListener(v -> {
//                Saves data to variables for columnInfo entries
            eventTitle = eventTitleEditText.getText().toString();
            eventLocation = eventLocationEditText.getText().toString();
            saveTemplateToggleState = saveTemplateToggle.isChecked();
            eventDate = setDateTextView.getText().toString();
            startTime = startTimeTextView.getText().toString();
            endTime = endTimeTextView.getText().toString();

            if (eventTitle.length() == 0) {
                Toast.makeText(getApplicationContext(),
                        "Title field empty",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else if (eventLocation.length() == 0) {
                Toast.makeText(getApplicationContext(),
                        "Location field empty",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else if (eventDate.length() == 0) {
                Toast.makeText(getApplicationContext(),
                        "No date",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else if (startTime.length() == 0) {
                Toast.makeText(getApplicationContext(),
                        "No start time",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else if (endTime.length() == 0) {
                Toast.makeText(getApplicationContext(),
                        "No end time",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else if (alertType.isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        "No alert type selected",
                        Toast.LENGTH_SHORT)
                        .show();
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
                    Toast.makeText(getApplicationContext(),
                            "Event scheduled in the past",
                            Toast.LENGTH_SHORT)
                            .show();
//                    If event end time value is smaller than start time value raises error
                } else if (notificationDelay > endTimeInMillis) {
                    Toast.makeText(getApplicationContext(),
                            "Event ends before it begins",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    switch (alertType) {
//                        Case statement will subtract the # of milliseconds represented by the alert type
//                                (i.e 5 minutes is 300000 milliseconds)
//                        from the timing of the event, and then schedule a notification at that time
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

//                    Checks whether an event with the exact same data already exists
                    int doesEventExistCheck = MainActivity.eventDatabase.eventDao()
                            .getEventID(
                                    eventTitle,
                                    eventLocation,
                                    eventDate,
                                    startTime,
                                    endTime,
                                    alertType);
//                    Checks whether an event with the exact same data but with null date value
                    int doesEventExistNullDateCheck = MainActivity.eventDatabase.eventDao()
                            .checkNullDate(
                                    eventTitle,
                                    eventLocation,
                                    startTime,
                                    endTime,
                                    alertType);
//                    Checks whether a template with the same data already exists
                    int duplicateTemplateCheck = MainActivity.eventDatabase.eventDao()
                            .checkDuplicateTemplate(
                                    eventTitle,
                                    eventLocation,
                                    startTime,
                                    endTime,
                                    alertType);
//                    If event already exists as entered, raise error saying event already exists
                    if (doesEventExistCheck != 0) {
                        Toast.makeText(getApplicationContext(),
                                "Event already exists",
                                Toast.LENGTH_SHORT)
                                .show();
//                        If Event exists but with null date entry, update date entry to selected date
                    } else if (doesEventExistNullDateCheck != 0) {
                        MainActivity.eventDatabase.eventDao()
                                .update(
                                        doesEventExistNullDateCheck,
                                        eventTitle,
                                        eventLocation,
                                        eventDate,
                                        startTime,
                                        endTime,
                                        alertType,
                                        saveTemplateToggleState);

                        finish();
                    } else if (duplicateTemplateCheck == 1) {
//                        Notifies user that template not saved
                        Toast.makeText(this,
                                "Template already exists",
                                Toast.LENGTH_SHORT).show();
//                        Inserts new event into database with template value of false
                        MainActivity.eventDatabase.eventDao()
                                .create(
                                        eventTitle,
                                        eventLocation,
                                        eventDate,
                                        startTime,
                                        endTime,
                                        alertType,
                                        false);
//                        ends activity
                        finish();
                    } else {
//                        Saves Event to DB
                        saveEvent();
//                        ends activity
                        finish();
                    }

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
                eventTitleEditText.setText(
                        data.getStringExtra("TemplateTitle"));
                eventLocationEditText.setText(
                        data.getStringExtra("TemplateLocation"));
                startTimeTextView.setText(
                        data.getStringExtra("TemplateStartTime"));
                endTimeTextView.setText(
                        data.getStringExtra("TemplateEndTime"));
                alertType = data.getStringExtra("TemplateAlertType");
            }
    }

}
