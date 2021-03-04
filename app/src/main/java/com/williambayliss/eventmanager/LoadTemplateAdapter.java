package com.williambayliss.eventmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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



//            Sets resultCode to OK and finishes Activity, passing data back to NewEventList activity
            ((Activity)context).setResult(Activity.RESULT_OK, intent);
            ((Activity)context).finish();

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
//    List of EventTemplates
    public List<Event> templateList = new ArrayList<>();


//    Initializes RecyclerView.Adapter
    LoadTemplateAdapter() {
        loadTemplates();
    }

//    Updates List of EventTemplates
    public void updateTemplates() {
        templateList.clear();
        loadTemplates();
    }

    public void loadTemplates() {
        templateList = MainActivity.eventDatabase.eventDao().getAllTemplates();
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
        Event currentTemplate = templateList.get(position);
        holder.templateTitleTextView.setText(currentTemplate.title);
        holder.templateLocationTextView.setText(currentTemplate.location);
        holder.templateStartTimeTextView.setText(currentTemplate.startTime);
        holder.templateEndTimeTextView.setText(currentTemplate.endTime);
        holder.templateAlertTypeTextView.setText(currentTemplate.alertType);

        holder.containerView.setTag(currentTemplate);
        //        Sets background color of item depending on alert type
        switch (currentTemplate.alertType) {
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
        return templateList.size();
    }

//    Gets current Template ID, for use in swipe to delete
    public int getItemID(int position) {
        Event current = templateList.get(position);
        return current.id;
    }


}
