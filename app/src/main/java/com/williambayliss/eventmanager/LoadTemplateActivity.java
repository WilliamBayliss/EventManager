package com.williambayliss.eventmanager;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LoadTemplateActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LoadTemplateAdapter loadTemplateAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_template_activity);

        recyclerView = findViewById(R.id.template_list_recycler_view);
        loadTemplateAdapter = new LoadTemplateAdapter(getApplicationContext());
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(loadTemplateAdapter);
        recyclerView.setLayoutManager(layoutManager);

    }
}
