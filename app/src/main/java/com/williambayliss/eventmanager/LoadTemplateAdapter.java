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

public class LoadTemplateAdapter extends RecyclerView.Adapter<LoadTemplateAdapter.LoadTemplateViewHolder> {
    public static class LoadTemplateViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout containerView;
        public TextView templateTitleTextView;
        public TextView templateLocationTextView;
        public TextView templateStartTimeTextView;
        public TextView templateEndTimeTextView;

        LoadTemplateViewHolder(View view) {
            super(view);
            containerView = view.findViewById(R.id.template_row);
            templateTitleTextView = view.findViewById(R.id.template_title_text_view);
            templateLocationTextView = view.findViewById(R.id.template_location_text_view);
            templateStartTimeTextView = view.findViewById(R.id.template_start_time_text_view);
            templateEndTimeTextView = view.findViewById(R.id.template_until_time_text_view);

            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    TODO: load from template
                }
            });
        }
    }
    public List<EventTemplate> templateList = new ArrayList<>();

    LoadTemplateAdapter(Context context) {
        loadTemplates();
    }

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
}
