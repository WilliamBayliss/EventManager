package com.williambayliss.eventmanager;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventDao {
    @Query("INSERT INTO Events (Title, Location, Date, `Start Time`, `End Time`, `Alert Type`) VALUES (:title, :location, :date, :startTime, :endTime, :alertType)")
    void create(String title, String location, String date, String startTime, String endTime, String alertType);

    @Query("UPDATE Events SET Title = :title, Location = :location, Date = :date, `Start Time` = :startTime, `End Time` = :endTime, `Alert Type` = :alertType WHERE id = :id")
    void update(int id, String title, String location, String date, String startTime, String endTime, String alertType);

    @Query("DELETE FROM Events WHERE id = :id")
    void delete(int id);

    @Query("SELECT * FROM Events WHERE Date = :date ORDER BY `Start Time` ASC")
    List<Event> getDaysEvents(String date);

    @Query("SELECT * FROM Events")
    List<Event> getAllEvents();

    @Query("SELECT id FROM EVENTS WHERE Title = :title AND Date = :date AND `Start Time` = :startTime")
    int getEventID(String title, String date, String startTime);
}
