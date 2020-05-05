package com.busra.reminderapp.dao;

import com.busra.reminderapp.model.ReminderModel;

import java.util.ArrayList;

public class ReminderDao {

    private static ArrayList<ReminderModel> reminders = new ArrayList<>();

    public static void addReminder(ReminderModel reminderModel){
        reminders.add(reminderModel);
    }
    public static ArrayList<ReminderModel> getAllReminders(){
        return reminders;
    }

}
