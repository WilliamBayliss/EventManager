package com.williambayliss.eventmanager;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventTemplateDao {
    @Query("INSERT INTO EventTemplates (Title, Location, `Start Time`, `End Time`, `Alert Type`) VALUES (:title, :location, :startTime, :endTime, :alertType)")
    void create(String title, String location, String startTime, String endTime, String alertType);

    @Query("UPDATE EventTemplates SET Title = :title, Location = :location, `Start Time` = :startTime, `End Time` = :endTime, `Alert Type` = :alertType WHERE id = :id")
    void update(int id, String title, String location, String startTime, String endTime, String alertType);

    @Query("DELETE FROM EventTemplates WHERE id = :id")
    void delete(int id);

    @Query("SELECT * FROM EventTemplates")
    List<EventTemplate> getAllTemplates();
}
