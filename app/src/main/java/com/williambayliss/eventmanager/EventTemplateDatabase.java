package com.williambayliss.eventmanager;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Event.class}, version = 1)
public abstract class EventTemplateDatabase extends RoomDatabase {
    public abstract EventTemplateDAO eventTemplateDAO();
}
