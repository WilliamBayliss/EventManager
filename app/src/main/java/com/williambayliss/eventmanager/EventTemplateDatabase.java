package com.williambayliss.eventmanager;

import androidx.room.Database;

@Database(entities = {Event.class}, version = 1)
public abstract class EventTemplateDatabase {
    public abstract EventTemplateDAO eventTemplateDAO();
}
