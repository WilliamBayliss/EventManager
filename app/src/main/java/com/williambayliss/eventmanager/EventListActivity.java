package com.williambayliss.eventmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EventListActivity extends AppCompatActivity {
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

        RecyclerView recyclerView = findViewById(R.id.event_list_recycler_view);
        eventListAdapter = new EventListAdapter(selectedDate);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(eventListAdapter);
        recyclerView.setLayoutManager(layoutManager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallBack);
        itemTouchHelper.attachToRecyclerView(recyclerView);

//        Selects FloatingActionButton and adds onclick Listener
//                to launch AddEventToDay Activity
        newEventButton = findViewById(R.id.add_event_to_day_button);
        newEventButton.setOnClickListener(v -> addEventToDay());


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

    ItemTouchHelper.SimpleCallback simpleItemTouchCallBack =
            new ItemTouchHelper.SimpleCallback(
                    0,
                    ItemTouchHelper.LEFT |
                            ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//           Show popup text saying event deleted
            Toast.makeText(
                    getApplicationContext(),
                    "Event Deleted",
                    Toast.LENGTH_SHORT).show();
//           Get id of selected Event from list and delete it from DB
            int position = viewHolder.getAdapterPosition();
            int id = eventListAdapter.getEventID(position);
//            Checks whether event is set as template
            int templateStatus = MainActivity.eventDatabase.eventDao().getTemplateStatus(id);
            if (templateStatus == 1) {
//                If yes, on deletion sets date to null to delete from EventList but save template data
                MainActivity.eventDatabase.eventDao().deleteEventSaveTemplate(id);
            }
            else {
//                Else, deletes event
                MainActivity.eventDatabase.eventDao().delete(id);
            }
//            Update List of Events, update RecyclerView
            eventListAdapter.updateDayEventList(eventListAdapter.eventList, selectedDate);
            eventListAdapter.notifyDataSetChanged();
        }

    };

}
