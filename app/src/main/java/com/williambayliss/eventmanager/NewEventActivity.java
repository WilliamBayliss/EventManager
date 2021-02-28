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
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewEventActivity extends AppCompatActivity {
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
    private Button loadTemplateButton;
    private Button addToCalendarButton;

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
        setDateButton = findViewById(R.id.date_button);
        setDateTextView = findViewById(R.id.set_date_text_view);
        startTimeButton = findViewById(R.id.start_time_button);
        startTimeTextView = findViewById(R.id.start_time_text_view);
        endTimeButton = findViewById(R.id.end_time_button);
        endTimeTextView = findViewById(R.id.end_time_text_view);
        alertTypeButton = findViewById(R.id.alert_type_button);
        saveTemplateToggle = findViewById(R.id.save_template_button);
        loadTemplateButton = findViewById(R.id.load_from_template_button);
        addToCalendarButton = findViewById(R.id.add_to_calendar_button);

//        Launches DatePicker on button press
        setDateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar currentDate = Calendar.getInstance();
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker;
                datePicker = new DatePickerDialog(NewEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
//                        Selected month increased by 1 because starts counting at 0
//                        & need correct month value for DB retreival elsewhere
                        selectedMonth = selectedMonth + 1;
                        setDateTextView.setText(String.format("%02d/%02d/%04d", selectedDayOfMonth, selectedMonth, selectedYear));
                    }
                }, year, month, day);
                datePicker.setTitle("Select Date");
                datePicker.show();
            }
        });

//        Launches Timepicker on button press, sets user selection as Textview text
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(NewEventActivity.this, android.R.style.Theme_Holo_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTimeTextView.setText(String.format("%02d:%02d", selectedHour,  selectedMinute));
                    }
                }, hour, minute, true);
                timePickerDialog.setTitle("Select Start Time");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });

//        Launches TimePicker on button press, sets user selection as Textview text
        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(NewEventActivity.this, android.R.style.Theme_Holo_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endTimeTextView.setText(String.format("%02d:%02d", selectedHour,  selectedMinute));
                    }
                }, hour, minute, true);
                timePickerDialog.setTitle("Select End Time");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });

//        Launches popup menu to save selection as String alertType
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

//        Launches LoadTemplateActivity on button press
        loadTemplateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoadTemplateActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        addToCalendarButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
//                Gets data DB entry
                assignEventVariables();
//                This logic ensures that no fields left blank
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
//                    Saves Event to DB
                    saveEvent();

//                    Saves template to DB if toggled
                    if (saveTemplateToggleState.equals(true)) {
                        saveEventTemplate();
                    }

//                    Gather data from notification
                    String eventTimeAndDate = startTime + ", " + eventDate;
                    long notificationDelay = eventDateConverter(eventTimeAndDate);

                    if (alertType.equals("At time of event")) {
                        scheduleNotification(buildNotification(eventTitle, eventLocation, eventTimeAndDate), notificationDelay);
                        Log.e("notification delay: ", "" +notificationDelay);
                    } else if (alertType == "Five minutes before event") {
                        notificationDelay = notificationDelay - (5 * 60 * 1000);
                        scheduleNotification(buildNotification(eventTitle, eventLocation, eventTimeAndDate), notificationDelay);
                    } else if (alertType == "Thirty minutes before event") {
                        notificationDelay = notificationDelay - (30 * 60 * 1000);
                        scheduleNotification(buildNotification(eventTitle, eventLocation, eventTimeAndDate), notificationDelay);
                    } else if (alertType == "One hour before event") {
                        notificationDelay = notificationDelay - (60 * 60 * 1000);
                        scheduleNotification(buildNotification(eventTitle, eventLocation, eventTimeAndDate), notificationDelay);
                    } else if (alertType == "One day before event") {
                        notificationDelay = notificationDelay - ((60 * 24) * 60 * 1000);
                        scheduleNotification(buildNotification(eventTitle, eventLocation, eventTimeAndDate), notificationDelay);
                    } else if (alertType == "One week before event") {
                        notificationDelay = notificationDelay - (((60 * 24) * 7) * 60 * 1000);
                        scheduleNotification(buildNotification(eventTitle, eventLocation, eventTimeAndDate), notificationDelay);
                    }
//                     Ends activity
                    finish();
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private long eventDateConverter(String eventDate) {
        Log.e("Date given", "" + eventDate);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy", Locale.CANADA);
        LocalDateTime date = LocalDateTime.parse(eventDate, dateTimeFormatter);
        long eventTimeInMillis = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Log.e("event time in milis", "" + eventTimeInMillis);
        long currentTime = System.currentTimeMillis();
        Log.e("current time in millis", "" + currentTime);
        long delay = eventTimeInMillis - currentTime;
        Log.e("delay", "" + delay);
        return delay;
    }

    private void assignEventVariables() {
        eventTitle = eventTitleEditText.getText().toString();
        eventLocation = eventLocationEditText.getText().toString();
        saveTemplateToggleState = saveTemplateToggle.isChecked();
        eventDate = setDateTextView.getText().toString();
        startTime = startTimeTextView.getText().toString();
        endTime = endTimeTextView.getText().toString();
    }


    private boolean onMenuItemOptionClick(MenuItem item) {
//        Popup text to show which item selected
        Toast.makeText(this, "Selected item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
//        Handles item selection, saves selection as String alertType
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

//    Saves EventTemplate to DB
    private void saveEventTemplate() {
            MainActivity.eventTemplateDatabase.eventTemplateDao().create(eventTitle, eventLocation, startTime, endTime, alertType);
        }
//        Saves Event to event db
    private void saveEvent() {
        MainActivity.eventDatabase.eventDao().create(eventTitle, eventLocation, eventDate, startTime, endTime, alertType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                eventTitleEditText.setText(data.getStringExtra("TemplateTitle"));
                eventLocationEditText.setText(data.getStringExtra("TemplateLocation"));
                startTimeTextView.setText(data.getStringExtra("TemplateStartTime"));
                endTimeTextView.setText(data.getStringExtra("TemplateEndTime"));
                alertType = data.getStringExtra("TemplateAlertType");
            }
    }


    private Notification buildNotification(String title, String location, String eventDate) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationChannelBuilder.CHANNEL_1_ID);
        builder.setContentTitle(title);
        builder.setContentText(location + ", " + eventDate);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NotificationChannelBuilder.CHANNEL_1_ID);
        return builder.build();
    }


    private void scheduleNotification (Notification notification , long delay) {
        Intent notificationIntent = new Intent( this, NotificationPublisher.class ) ;
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0, notificationIntent, PendingIntent. FLAG_UPDATE_CURRENT ) ;
        long futureInMillis = SystemClock.elapsedRealtime () + delay ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent) ;
    }
}
