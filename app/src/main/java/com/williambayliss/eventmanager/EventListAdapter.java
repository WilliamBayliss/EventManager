package com.williambayliss.eventmanager;

import android.content.Context;
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
    public static class EventListViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout containerView;
        public TextView eventTitleTextView;
        public TextView eventLocationTextView;
        public TextView eventStartTimeTextView;
        public TextView eventEndTimeTextView;
        public TextView eventDateTextView;

        EventListViewHolder(View view) {
            super(view);
            containerView = view.findViewById(R.id.event_row);
            eventTitleTextView = view.findViewById(R.id.event_title_text_view);
            eventLocationTextView = view.findViewById(R.id.event_location_text_view);
            eventStartTimeTextView = view.findViewById(R.id.event_start_time_text_view);
            eventEndTimeTextView = view.findViewById(R.id.event_until_time_text_view);
            eventDateTextView = view.findViewById(R.id.event_date_text_view);

            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    TODO create popup menu to either edit or delete event
                }
            });
        }
    }

    private List<Event> eventList = new ArrayList<>();

    EventListAdapter(Context context) {
        loadDayEvents();
    }

    public void loadDayEvents() {
        eventList = MainActivity.eventDatabase.eventDao().getAllEvents();
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
        holder.containerView.setTag(currentEvent);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
