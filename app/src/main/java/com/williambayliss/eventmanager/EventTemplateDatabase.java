package com.williambayliss.eventmanager;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {EventTemplate.class}, version = 1, exportSchema = true)
public abstract class EventTemplateDatabase extends RoomDatabase {
    public abstract EventTemplateDao eventTemplateDao();
}
