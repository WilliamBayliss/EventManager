package com.williambayliss.eventmanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MotionEvent;
import android.view.View;
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

//        Create recyclerView and layout manager
        recyclerView = findViewById(R.id.template_list_recycler_view);
        loadTemplateAdapter = new LoadTemplateAdapter();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(loadTemplateAdapter);
        recyclerView.setLayoutManager(layoutManager);


//        Allows for swipe to delete and attaches to recyclerView
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
//            Popup text to show item deleted
            Toast.makeText(getApplicationContext(), "Template deleted", Toast.LENGTH_SHORT).show();
//            Gets selected Template, gets corresponding ID and deletes correct Template from DB
//            updates recyclerView
            int position = viewHolder.getAdapterPosition();
            int id = loadTemplateAdapter.getItemID(position);
//            Checks whether event date has been set to NULL in DB
            String eventDeletedCheck = MainActivity.eventDatabase.eventDao().getEventDate(id);
            if (eventDeletedCheck == null ) {
//                If yes, deletes event on template deletion
                MainActivity.eventDatabase.eventDao().delete(id);
            } else {
//                Else, sets template value to false in DB
                MainActivity.eventDatabase.eventDao().toggleTemplate(id);
            }
//            Updates recyclerView
            loadTemplateAdapter.updateTemplates();
            loadTemplateAdapter.notifyDataSetChanged();
        }
    };

}
