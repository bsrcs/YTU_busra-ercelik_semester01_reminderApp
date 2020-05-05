package com.busra.reminderapp.model;

import com.busra.reminderapp.constants.REMINDER_APP_CONSTANTS;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ReminderModel {

    private String id;
    private String reminderMessage;
    private long remindDateTime;
    private String typeOfReminder;
    private String notes;

    public ReminderModel(){}

    public ReminderModel(String id, String reminderMessage, long remindDateTime, String typeOfReminder, String notes) {
        this.id = id;
        this.reminderMessage = reminderMessage;
        this.remindDateTime = remindDateTime;
        this.typeOfReminder = typeOfReminder;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReminderMessage() {
        return reminderMessage;
    }

    public void setReminderMessage(String reminderMessage) {
        this.reminderMessage = reminderMessage;
    }

    public long getRemindDateTime() {
        return remindDateTime;
    }

    public void setRemindDateTime(long remindDateTime) {
        this.remindDateTime = remindDateTime;
    }

    public String getTypeOfReminder() {
        return typeOfReminder;
    }

    public void setTypeOfReminder(String typeOfReminder) {
        this.typeOfReminder = typeOfReminder;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReminderModel)) return false;
        ReminderModel that = (ReminderModel) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(REMINDER_APP_CONSTANTS.DATE_FORMAT_TO_DISPLAY);
        String dateTime = simpleDateFormat.format(new Date(this.remindDateTime));
        return "Message: "+this.reminderMessage+"\nTime: "+dateTime;
    }
}
