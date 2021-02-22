package com.williambayliss.eventmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EventListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    EventListAdapter eventListAdapter;
    private String selectedDate;
    FloatingActionButton newEventButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_activity);

        Intent intent = getIntent();
        selectedDate = intent.getExtras().getString("selectedDate");

        recyclerView = findViewById(R.id.event_list_recycler_view);
        eventListAdapter = new EventListAdapter(getApplicationContext(), selectedDate);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(eventListAdapter);
        recyclerView.setLayoutManager(layoutManager);

        newEventButton = findViewById(R.id.add_event_to_day_button);

        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEventToDay();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        eventListAdapter.updateDayEventList(eventListAdapter.eventList, selectedDate);
        eventListAdapter.notifyDataSetChanged();
    }

    private void addEventToDay() {
        Intent i = new Intent(getApplicationContext(), AddEventToDayActivity.class);
        i.putExtra("currentDate", selectedDate);
        startActivity(i);
    }

}
