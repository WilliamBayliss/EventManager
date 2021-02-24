package com.williambayliss.eventmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LoadTemplateAdapter extends RecyclerView.Adapter<LoadTemplateAdapter.LoadTemplateViewHolder> {
    public static class LoadTemplateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public LinearLayout containerView;
        public TextView templateTitleTextView;
        public TextView templateLocationTextView;
        public TextView templateStartTimeTextView;
        public TextView templateEndTimeTextView;
        LoadTemplateViewHolder(View view) {
            super(view);
//            Sets containerView, selects TextViews from layout
            containerView = view.findViewById(R.id.template_row);
            templateTitleTextView = view.findViewById(R.id.template_title_text_view);
            templateLocationTextView = view.findViewById(R.id.template_location_text_view);
            templateStartTimeTextView = view.findViewById(R.id.template_start_time_text_view);
            templateEndTimeTextView = view.findViewById(R.id.template_until_time_text_view);

            containerView.setOnClickListener(this);
            containerView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(containerView.getContext(), "It is absolute happening bro", Toast.LENGTH_SHORT).show();

        }

        @Override
        public boolean onLongClick(View v) {
            Toast.makeText(containerView.getContext(), "IT's happening bro", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
//    List of EventTemplates
    public List<EventTemplate> templateList = new ArrayList<>();

//    Initializes RecyclerView.Adapter
    LoadTemplateAdapter(Context context, Activity activity) {
        loadTemplates();
    }

//    Updates List of EventTemplates
    public void updateTemplates() {
        templateList.clear();
        loadTemplates();
    }

    public void loadTemplates() {
        templateList = MainActivity.eventTemplateDatabase.eventTemplateDao().getAllTemplates();
    }

    @NonNull
    @Override
    public LoadTemplateAdapter.LoadTemplateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_row, parent, false);
        return new LoadTemplateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoadTemplateAdapter.LoadTemplateViewHolder holder, int position) {
//        Gets current template from position in List
//        sets textViews in Row to corresponding data
        EventTemplate currentTemplate = templateList.get(position);
        holder.templateTitleTextView.setText(currentTemplate.title);
        holder.templateLocationTextView.setText(currentTemplate.location);
        holder.templateStartTimeTextView.setText(currentTemplate.startTime);
        holder.templateEndTimeTextView.setText(currentTemplate.endTime);
        holder.containerView.setTag(currentTemplate);
    }

    @Override
    public int getItemCount() {
        return templateList.size();
    }

//    Gets current Template ID, for use in swipe to delete
    public int getItemID(int position) {
        EventTemplate current = templateList.get(position);
        return current.id;
    }

    public EventTemplate getTemplateData(int position) {
        EventTemplate current = templateList.get(position);
        return current;
    }

}
