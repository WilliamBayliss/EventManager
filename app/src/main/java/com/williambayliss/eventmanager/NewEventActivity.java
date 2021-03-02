package com.williambayliss.eventmanager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class NewEventActivity extends AppCompatActivity {
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

//        Selects layout, attaches layout items to variables
        setContentView(R.layout.new_event_activity);
        eventTitleEditText = findViewById(R.id.event_title);
        eventLocationEditText = findViewById(R.id.event_location);
        Button setDateButton = findViewById(R.id.date_button);
        setDateTextView = findViewById(R.id.set_date_text_view);
        Button startTimeButton = findViewById(R.id.start_time_button);
        startTimeTextView = findViewById(R.id.start_time_text_view);
        Button endTimeButton = findViewById(R.id.end_time_button);
        endTimeTextView = findViewById(R.id.end_time_text_view);
        Button alertTypeButton = findViewById(R.id.alert_type_button);
        saveTemplateToggle = findViewById(R.id.save_template_button);
        Button loadTemplateButton = findViewById(R.id.load_from_template_button);
        Button addToCalendarButton = findViewById(R.id.add_to_calendar_button);

//        Launches DatePicker on button press
        setDateButton.setOnClickListener(v -> {
            Calendar currentDate = Calendar.getInstance();
            int year = currentDate.get(Calendar.YEAR);
            int month = currentDate.get(Calendar.MONTH);
            int day = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker;
            datePicker = new DatePickerDialog(
                    NewEventActivity.this,
                    (datePicker1, selectedYear, selectedMonth, selectedDayOfMonth) -> {
//                        Selected month increased by 1 because starts counting at 0
//                        & need correct month value for DB retrieval elsewhere
                selectedMonth = selectedMonth + 1;
                setDateTextView.setText(
                        String.format(
                                Locale.CANADA,
                                "%02d/%02d/%04d",
                                selectedDayOfMonth,
                                selectedMonth,
                                selectedYear));
            }, year, month, day);
            datePicker.setTitle("Select Date");
            datePicker.show();
        });

//        Launches Timepicker on button press, sets user selection as Textview text
        startTimeButton.setOnClickListener(v -> {
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog;
            timePickerDialog = new TimePickerDialog(
                    NewEventActivity.this,
                    (timePicker, selectedHour, selectedMinute) -> startTimeTextView.setText(
                        String.format(
                            Locale.CANADA,
                            "%02d:%02d",
                            selectedHour,
                            selectedMinute)), hour, minute, true);
            timePickerDialog.setTitle("Select Start Time");
            timePickerDialog.show();
        });

//        Launches TimePicker on button press, sets user selection as Textview text
        endTimeButton.setOnClickListener(v -> {
//
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog;
            timePickerDialog = new TimePickerDialog(
                    NewEventActivity.this,
                    (timePicker, selectedHour, selectedMinute) -> endTimeTextView.setText(
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

//        Launches popup menu to save selection as String alertType
        alertTypeButton.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(getApplicationContext(), v);
            menu.setOnMenuItemClickListener(item -> {
                onMenuItemOptionClick(item);
                return false;
            });
            menu.inflate(R.menu.alert_popup_menu);
            menu.show();
        });

//        Launches LoadTemplateActivity on button press
        loadTemplateButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                                getApplicationContext(),
                                LoadTemplateActivity.class);
            startActivityForResult(intent, 1);
        });

        addToCalendarButton.setOnClickListener(v -> {

//                assigns entered values to variables
            assignEventVariables();


//                This logic ensures that no fields left blank
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
            } else {


//                    Get time info for event
                String eventTimeAndDate = startTime + ", " + eventDate;
                String eventEndTimeAndDate = endTime + ", " + eventDate;
                long notificationDelay = eventDateConverter(eventTimeAndDate);
                long endTimeInMillis = eventDateConverter(eventEndTimeAndDate);
//                    Checks that event is scheduled in the future
//                    if eventDateConverter returns a negative, it means that the start time is
//                    before the current time in millis
                if (notificationDelay < 0) {
                    Toast.makeText(getApplicationContext(),
                            "Error: Event scheduled in the past",
                            Toast.LENGTH_SHORT)
                            .show();
//                    If event end time value is smaller than start time value raises error
                } else if (notificationDelay > endTimeInMillis) {
                    Toast.makeText(getApplicationContext(),
                            "Error: Event ends before it begins",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
//                    Schedules notification on a delay based on alertType value
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
//                    Checks whether an event with the exact same data already exists
                    int doesEventExistCheck = MainActivity.eventDatabase.eventDao().getEventID(eventTitle, eventLocation, eventDate, startTime, endTime, alertType);
//                    Checks whether an event with the exact same data but with null date value
                    int doesEventExistNullDateCheck = MainActivity.eventDatabase.eventDao().checkNullDate(eventTitle, eventLocation,startTime, endTime, alertType);
                    int duplicateTemplateCheck = MainActivity.eventDatabase.eventDao().checkDuplicateTemplate(eventTitle, eventLocation,startTime, endTime, alertType);
//                    If event already exists as entered, raise error saying event already exists
                    if (doesEventExistCheck != 0) {
                        Toast.makeText(getApplicationContext(),
                                "Event already exists",
                                Toast.LENGTH_SHORT)
                                .show();
//                        If Event exists but with null date entry, update date entry to selected date
                    } else if (doesEventExistNullDateCheck != 0) {
                        MainActivity.eventDatabase.eventDao().update(doesEventExistNullDateCheck, eventTitle, eventLocation, eventDate, startTime, endTime, alertType, saveTemplateToggleState);

                        finish();
                    } else if (duplicateTemplateCheck == 1) {
                        Toast.makeText(this, "Template already exists", Toast.LENGTH_SHORT).show();
                    } else {
//                        Saves Event to DB
                        saveEvent();
                        finish();
                    }
                    finish();
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public long eventDateConverter(String eventDate) {
//      Creates DateTimeFormatter to take in time/date from String
        DateTimeFormatter dateTimeFormatter =
                DateTimeFormatter.ofPattern(
                        "HH:mm, dd/MM/yyyy",
                        Locale.CANADA);
//      Parses the eventDate
        LocalDateTime date = LocalDateTime.parse(eventDate, dateTimeFormatter);
//      Gets exact time of event from epoch in millis
        long eventTimeInMillis =
                date.atZone(
                        ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli();
//      gets current time from epoch in millis
        long currentTime = System.currentTimeMillis();
//      returns the difference, which will be the required delay for notification
        return eventTimeInMillis - currentTime;
    }

    public void assignEventVariables() {
//        Gets data from view and saves user entries to variables
        eventTitle = eventTitleEditText.getText().toString();
        eventLocation = eventLocationEditText.getText().toString();
        saveTemplateToggleState = saveTemplateToggle.isChecked();
        eventDate = setDateTextView.getText().toString();
        startTime = startTimeTextView.getText().toString();
        endTime = endTimeTextView.getText().toString();
    }


    private void onMenuItemOptionClick(MenuItem item) {
//        Popup text to show which item selected
        Toast.makeText(this,
                "Selected item: " + item.getTitle(),
                Toast.LENGTH_SHORT)
                .show();
//        Handles item selection, saves selection as String alertType
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

//        Saves Event to event db
    private void saveEvent() {
        MainActivity
                .eventDatabase
                .eventDao()
                .create(
                        eventTitle,
                        eventLocation,
                        eventDate,
                        startTime,
                        endTime,
                        alertType,
                        saveTemplateToggleState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
//                After LoadTemplateActivity finishes, loads data from return Intent
//                into textViews and variables
                assert data != null;
                eventTitleEditText.setText(
                        data.getStringExtra("TemplateTitle")
                );
                eventLocationEditText.setText(
                        data.getStringExtra("TemplateLocation")
                );
                startTimeTextView.setText(
                        data.getStringExtra("TemplateStartTime")
                );
                endTimeTextView.setText(
                        data.getStringExtra("TemplateEndTime")
                );
                alertType = data.getStringExtra(
                            "TemplateAlertType"
                );
            }
    }

//      Builds Notification
    public Notification buildNotification(String title, String location, String eventDate) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        this,
                        NotificationChannelBuilder.CHANNEL_1_ID);
        builder.setContentTitle(title);
        builder.setContentText(location + ", " + eventDate);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NotificationChannelBuilder.CHANNEL_1_ID);
        return builder.build();
    }

//     Schedules notification
    public void scheduleNotification (Notification notification , long delay) {
//        Creates intent for NotificationPublisher
        Intent notificationIntent =
                new Intent(
                        this,
                        NotificationPublisher.class );
//        Puts intent data into intent
        notificationIntent
                .putExtra(
                        NotificationPublisher.NOTIFICATION_ID ,
                        1 );
//        Puts notification into intent
        notificationIntent
                .putExtra(
                        NotificationPublisher.NOTIFICATION ,
                        notification) ;
//        Gets pendingIntent
        PendingIntent pendingIntent =
                PendingIntent
                        .getBroadcast (
                                this,
                                0, notificationIntent,
                                PendingIntent.
                                        FLAG_UPDATE_CURRENT);
//        Adds delay to current time to schedule notification
        long futureInMillis = SystemClock.elapsedRealtime () + delay ;
//        Creates AlarmManager and sets alarm for notification
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.set(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                futureInMillis,
                pendingIntent);
    }
}
