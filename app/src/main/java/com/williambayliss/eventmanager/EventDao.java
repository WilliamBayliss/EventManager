package com.williambayliss.eventmanager;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventDao {
    @Query("INSERT INTO Events (Title, Location, Date, `Start Time`, `End Time`, `Alert Type`, Template) VALUES (:title, :location, :date, :startTime, :endTime, :alertType, :template)")
    void create(String title, String location, String date, String startTime, String endTime, String alertType, boolean template);

    @Query("UPDATE Events SET Title = :title, Location = :location, Date = :date, `Start Time` = :startTime, `End Time` = :endTime, `Alert Type` = :alertType, Template = :templateToggle WHERE id = :id")
    void update(int id, String title, String location, String date, String startTime, String endTime, String alertType, boolean templateToggle);

    @Query("SELECT Template FROM Events WHERE id = :id")
    int getTemplateStatus(int id);

    @Query("UPDATE Events SET template = '0' WHERE id = :id")
    void toggleTemplate(int id);

    @Query("UPDATE Events SET Date = NULL WHERE id = :id AND Template = '1'")
    void deleteEventSaveTemplate(int id);

    @Query("SELECT Date FROM Events WHERE id = :id")
    String getEventDate(int id);

    @Query("UPDATE Events SET Date = :date WHERE id = :id")
    void updateEventDate(int id, String date);

    @Query("DELETE FROM Events WHERE id = :id")
    void delete(int id);

    @Query("SELECT * FROM Events WHERE Date = :date ORDER BY `Start Time` ASC")
    List<Event> getDaysEvents(String date);

    @Query("SELECT * FROM Events ORDER BY `Start Time` ASC")
    List<Event> getAllEvents();

    @Query("SELECT * FROM Events WHERE Template == '1'")
    List<Event> getAllTemplates();

    @Query("SELECT id FROM Events WHERE Title = :title AND Location = :location AND Date = :date AND `Start Time` = :startTime AND `End Time` = :endTime AND `Alert Type` = :alertType")
    int getEventID(String title, String location, String date, String startTime, String endTime, String alertType);

    @Query("SELECT id FROM Events WHERE Title = :title AND Location = :location AND Date IS NULL AND `Start Time` = :startTime AND `End Time` = :endTime AND `Alert Type` = :alertType")
    int checkNullDate(String title, String location, String startTime, String endTime, String alertType);
}
