    package com.williambayliss.eventmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder> {
    public static class EventListViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout containerView;
        public TextView eventTitleTextView;
        public TextView eventLocationTextView;
        public TextView eventStartTimeTextView;
        public TextView eventEndTimeTextView;
        public TextView eventDateTextView;
        public TextView eventAlertTypeTextView;

        EventListViewHolder(View view) {
            super(view);
//            Assigning container and textViews to layout elements
            containerView = view.findViewById(R.id.event_row);
            eventTitleTextView = view.findViewById(R.id.event_title_text_view);
            eventLocationTextView = view.findViewById(R.id.event_location_text_view);
            eventStartTimeTextView = view.findViewById(R.id.event_start_time_text_view);
            eventEndTimeTextView = view.findViewById(R.id.event_until_time_text_view);
            eventDateTextView = view.findViewById(R.id.event_date_text_view);
            eventAlertTypeTextView = view.findViewById(R.id.event_alert_type_text_view);
        }


    }
//    List of Events
    public List<Event> eventList = new ArrayList<>();

//    Initializes EventListAdapter
    EventListAdapter(String selectedDate) {
        loadDayEvents(selectedDate);
    }

//    Loads all events that have date value matching selectedDate into List
    public void loadDayEvents(String selectedDate) {
        eventList = MainActivity.eventDatabase.eventDao().getDaysEvents(selectedDate);
    }

//    Reloads eventList, for use on event add/delete to update Recyclerview
    public void updateDayEventList(List<Event> eventList, String selectedDay) {
        eventList.clear();
        loadDayEvents(selectedDay);
    }

//    Gets event id, for user in deleting items from Recyclerview
    public int getEventID(int position) {
        Event current = eventList.get(position);
        return current.id;
    }



    @NonNull
    @Override
    public EventListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_row, parent, false);
        return new EventListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventListAdapter.EventListViewHolder holder, int position) {
        Event currentEvent = eventList.get(position);
        holder.eventTitleTextView.setText(currentEvent.title);
        holder.eventLocationTextView.setText(currentEvent.location);
        holder.eventDateTextView.setText(currentEvent.date);
        holder.eventStartTimeTextView.setText(currentEvent.startTime);
        holder.eventEndTimeTextView.setText(currentEvent.endTime);
        holder.eventAlertTypeTextView.setText(currentEvent.alertType);
        holder.containerView.setTag(currentEvent);

//        Sets background color of item depending on alert type
        if (currentEvent.alertType.equals("At time of event")) {
            holder.containerView.setBackgroundColor(Color.parseColor("#FF0000"));
        } else if (currentEvent.alertType.equals("Five minutes before event")) {
            holder.containerView.setBackgroundColor(Color.parseColor("#FF8A00"));
        } else if (currentEvent.alertType.equals("Thirty minutes before event")) {
            holder.containerView.setBackgroundColor(Color.parseColor("#1EDA00"));
        } else if (currentEvent.alertType.equals("One hour before event")) {
            holder.containerView.setBackgroundColor(Color.parseColor("#0098DA"));
        } else if (currentEvent.alertType.equals("One day before event")) {
            holder.containerView.setBackgroundColor(Color.parseColor("#9700FF"));
        } else if (currentEvent.alertType.equals("One week before event")) {
            holder.containerView.setBackgroundColor(Color.parseColor("#FF00E8"));
        } else {
            holder.containerView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    private int getEventId(int position) {
        Event current = eventList.get(position);
        return current.id;
    }
}
