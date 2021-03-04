    package com.williambayliss.eventmanager;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        String alertTypeStringText = holder.itemView.getContext().getString( R.string.alert_time_declaration) + " " +currentEvent.alertType;
        holder.eventAlertTypeTextView.setText(alertTypeStringText);
        holder.containerView.setTag(currentEvent);

//        Sets background color of item depending on alert type
        switch (currentEvent.alertType) {
            case "At time of event":
                holder.containerView.setBackgroundColor(Color.parseColor("#FF0000"));
                break;
            case "Five minutes before event":
                holder.containerView.setBackgroundColor(Color.parseColor("#FF8A00"));
                break;
            case "Thirty minutes before event":
                holder.containerView.setBackgroundColor(Color.parseColor("#1EDA00"));
                break;
            case "One hour before event":
                holder.containerView.setBackgroundColor(Color.parseColor("#0098DA"));
                break;
            case "One day before event":
                holder.containerView.setBackgroundColor(Color.parseColor("#9700FF"));
                break;
            case "One week before event":
                holder.containerView.setBackgroundColor(Color.parseColor("#FF00E8"));
                break;
            default:
                holder.containerView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

}
