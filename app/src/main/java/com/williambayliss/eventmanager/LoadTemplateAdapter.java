package com.williambayliss.eventmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LoadTemplateAdapter extends RecyclerView.Adapter<LoadTemplateAdapter.LoadTemplateViewHolder> {
    public static class LoadTemplateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public LinearLayout containerView;
        public TextView templateTitleTextView;
        public TextView templateLocationTextView;
        public TextView templateStartTimeTextView;
        public TextView templateEndTimeTextView;
        public TextView templateAlertTypeTextView;

        LoadTemplateViewHolder(View view) {
            super(view);
//            Sets containerView, selects TextViews from layout
            containerView = view.findViewById(R.id.template_row);
            templateTitleTextView = view.findViewById(R.id.template_title_text_view);
            templateLocationTextView = view.findViewById(R.id.template_location_text_view);
            templateStartTimeTextView = view.findViewById(R.id.template_start_time_text_view);
            templateEndTimeTextView = view.findViewById(R.id.template_until_time_text_view);
            templateAlertTypeTextView = view.findViewById(R.id.template_alert_type_text_view);

            containerView.setOnClickListener(this);
            containerView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(containerView.getContext(), "Template Selected", Toast.LENGTH_SHORT).show();
//            Gets View context
            Context context = v.getContext();
//            Creates intent and loads EventTemplate data into it
            Intent intent = new Intent(context, NewEventActivity.class);
            intent.putExtra("TemplateTitle", templateTitleTextView.getText());
            intent.putExtra("TemplateLocation", templateLocationTextView.getText());
            intent.putExtra("TemplateStartTime", templateStartTimeTextView.getText());
            intent.putExtra("TemplateEndTime", templateEndTimeTextView.getText());
            intent.putExtra("TemplateAlertType", templateAlertTypeTextView.getText());
//            Sets resultcode to OK and finishes Activity, passing data back to NewEventList activity
            ((Activity)context).setResult(Activity.RESULT_OK, intent);
            ((Activity)context).finish();

        }

        @Override
        public boolean onLongClick(View v) {
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
        holder.templateAlertTypeTextView.setText(currentTemplate.alertType);

        holder.containerView.setTag(currentTemplate);
        //        Sets background color of item depending on alert type
        if (currentTemplate.alertType.equals("At time of event")) {
            holder.containerView.setBackgroundColor(Color.parseColor("#FF7567"));
        } else if (currentTemplate.alertType.equals("Five minutes before event")) {
            holder.containerView.setBackgroundColor(Color.parseColor("#FAFF98"));
        } else if (currentTemplate.alertType.equals("Thirty minutes before event")) {
            holder.containerView.setBackgroundColor(Color.parseColor("#8AE4FF"));
        } else if (currentTemplate.alertType.equals("One hour before event")) {
            holder.containerView.setBackgroundColor(Color.parseColor("#ADFF8A"));
        } else if (currentTemplate.alertType.equals("One day before event")) {
            holder.containerView.setBackgroundColor(Color.parseColor("#D18AFF"));
        } else if (currentTemplate.alertType.equals("One week before event")) {
            holder.containerView.setBackgroundColor(Color.parseColor("#FFBF61"));
        } else {
            holder.containerView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
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
