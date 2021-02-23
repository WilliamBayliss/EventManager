package com.williambayliss.eventmanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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
        loadTemplateAdapter = new LoadTemplateAdapter(getApplicationContext(), LoadTemplateActivity.this);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(loadTemplateAdapter);
        recyclerView.setLayoutManager(layoutManager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallBack);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Toast.makeText(getApplicationContext(), "Template deleted", Toast.LENGTH_SHORT).show();
            int position = viewHolder.getAdapterPosition();
            int id = loadTemplateAdapter.getItemID(position);
            MainActivity.eventTemplateDatabase.eventTemplateDao().delete(id);
            loadTemplateAdapter.updateTemplates();
        }
    };

}
