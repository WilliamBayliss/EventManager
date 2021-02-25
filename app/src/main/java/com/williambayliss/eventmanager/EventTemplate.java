package com.williambayliss.eventmanager;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Comparator;

@Entity(tableName = "EventTemplates")
public class EventTemplate{
    @PrimaryKey
    public int id;
    @ColumnInfo(name = "Title")
    public String title;
    @ColumnInfo(name = "Location")
    public String location;
    @ColumnInfo(name = "Start Time")
    public String startTime;
    @ColumnInfo(name = "End Time")
    public String endTime;
    @ColumnInfo(name = "Alert Type")
    public String alertType;
}
