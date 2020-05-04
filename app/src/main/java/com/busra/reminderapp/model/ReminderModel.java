package com.busra.reminderapp.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ReminderModel {

    String id;
    String reminderMessage;
    long remindDateTime;
    //ONE_TIME, REPEAT_WEEKLY,REPEAT_MONTHLY,REPEAT_YEARLY
    String typeOfReminder;

    public ReminderModel(String id, String reminderMessage, long remindDateTime, String typeOfReminder) {
        this.id = id;
        this.reminderMessage = reminderMessage;
        this.remindDateTime = remindDateTime;
        this.typeOfReminder = typeOfReminder;
    }

    public ReminderModel() {

    }

    public String getId() {
        return id;
    }

    public String getReminderMessage() {
        return reminderMessage;
    }

    public long getRemindDateTime() {
        return remindDateTime;
    }

    public String getTypeOfReminder() {
        return typeOfReminder;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setReminderMessage(String reminderMessage) {
        this.reminderMessage = reminderMessage;
    }

    public void setRemindDateTime(long remindDateTime) {
        this.remindDateTime = remindDateTime;
    }

    public void setTypeOfReminder(String typeOfReminder) {
        this.typeOfReminder = typeOfReminder;
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
        return Objects.hash(getId(), getReminderMessage(), getRemindDateTime(), getTypeOfReminder());
    }

    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, E hh:MM a");
        String dateTime = simpleDateFormat.format(new Date(this.remindDateTime));
        return "Message: "+this.reminderMessage+"\nTime: "+dateTime;
    }
}
