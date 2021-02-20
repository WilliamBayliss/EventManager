package com.williambayliss.eventmanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EventListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    EventListAdapter eventListAdapter;
    public String selectedDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_activity);

        Intent intent = getIntent();
        selectedDate = intent.getStringExtra("event date");

        recyclerView = findViewById(R.id.event_list_recycler_view);
        eventListAdapter = new EventListAdapter(getApplicationContext());
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(eventListAdapter);
        recyclerView.setLayoutManager(layoutManager);


    }
}
