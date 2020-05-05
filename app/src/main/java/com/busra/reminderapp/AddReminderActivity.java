package com.busra.reminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.busra.reminderapp.constants.REMINDER_APP_CONSTANTS;
import com.busra.reminderapp.dao.ReminderDao;
import com.busra.reminderapp.model.ReminderModel;

import java.util.Calendar;
import java.util.UUID;

public class AddReminderActivity extends AppCompatActivity {

    private EditText editTextMessage, editTextNotes;
    private Button btnSelectDate, btnSelectTime, btnSaveReminder;
    private TextView textViewSelectedDate, textViewSelectedTime;
    //dropdown list
    private Spinner spinnerFrequencyOptions;
    //values for dropdown
    private String[] FREQUENCY_OPTIONS = {
            REMINDER_APP_CONSTANTS.REMINDER_TYPE_ONCE,
            REMINDER_APP_CONSTANTS.REMINDER_TYPE_MONTHLY,
            REMINDER_APP_CONSTANTS.REMINDER_TYPE_WEEKLY,
            REMINDER_APP_CONSTANTS.REMINDER_TYPE_YEARLY
    };
    private ReminderModel reminderModel;
    private Dialog reminderMessageDialog;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private int year,month,dayOfMonth,hourOfDay,minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        editTextMessage = findViewById(R.id.editTextMessage);
        editTextNotes = findViewById(R.id.editTextNotes);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        btnSaveReminder = findViewById(R.id.btnSaveReminder);
        textViewSelectedDate = findViewById(R.id.textViewSelectedDate);
        textViewSelectedTime = findViewById(R.id.textViewSelectedTime);
        spinnerFrequencyOptions = findViewById(R.id.spinnerFrequencyOptions);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,FREQUENCY_OPTIONS);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerFrequencyOptions.setAdapter(spinnerAdapter);
        reminderModel = new ReminderModel();
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDatePicker();
            }
        });
        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callTimePicker();
            }
        });
        btnSaveReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createReminder();
            }
        });
    }

    private void createReminder() {
        final Calendar selectedDateAndTime = Calendar.getInstance();
        selectedDateAndTime.set(this.year,this.month,this.dayOfMonth,this.hourOfDay,this.minute);
        reminderModel.setId(UUID.randomUUID().toString());
        reminderModel.setNotes(editTextNotes.getText().toString());
        reminderModel.setReminderMessage(editTextMessage.getText().toString());
        reminderModel.setTypeOfReminder((String)spinnerFrequencyOptions.getSelectedItem());
        reminderModel.setRemindDateTime(selectedDateAndTime.getTimeInMillis());
        Log.d(REMINDER_APP_CONSTANTS.LOG_LABEL,reminderModel.toString());
        //add this reminder to the static list
        ReminderDao.addReminder(reminderModel);
        //close this activity
        finish();
    }


    public void callDatePicker() {
        //create a calender object that has the current time.
        final Calendar defaultDateAndTimeForDatePicker = Calendar.getInstance();
        //set up the onclick listener, where we want to call the datepicker and timepicker.
        //create a datepicker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddReminderActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    //the code that executes after the user has selected the date.
                    @Override
                    //the parameters reflect the date the user has selected.
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        AddReminderActivity.this.year=year;
                        AddReminderActivity.this.month=month;
                        AddReminderActivity.this.dayOfMonth=dayOfMonth;
                    }
                },
                //set the default selected time in the datepicker.
                defaultDateAndTimeForDatePicker.get(Calendar.YEAR),
                defaultDateAndTimeForDatePicker.get(Calendar.MONTH),
                defaultDateAndTimeForDatePicker.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void callTimePicker() {
        final Calendar defaultDateAndTimeForDatePicker = Calendar.getInstance();
        //after selecting and setting the date, we move on to selecting the time.
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
            // the method that gets called upon the successful selection of a time.
            @Override
            //the parameters reflect the hour and minute chosen by the user
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //set the dates selected by the user in the variable that we will use later
                AddReminderActivity.this.hourOfDay=hourOfDay;
                AddReminderActivity.this.minute=minute;
            }
        }, defaultDateAndTimeForDatePicker.get(Calendar.HOUR_OF_DAY),
                defaultDateAndTimeForDatePicker.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    //actually set the reminder
    private void setReminder(ReminderModel reminderModel) {
        //create an intent to call the broadcastreciever
        Intent alarmIntent = new Intent(AddReminderActivity.this, MyBroadcastReceiver.class);
        //set the reminder message in the intent
        alarmIntent.putExtra(REMINDER_APP_CONSTANTS.INTENT_DATA_KEY_NAME, reminderModel.getReminderMessage());
        //create an pending intent from the normal intent above
        pendingIntent = PendingIntent.getBroadcast(AddReminderActivity.this, 0, alarmIntent, 0);
        //set the alarm with the pending intent
        //use selectedDateAndTime to get the interval after which the alarm will be triggered
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderModel.getRemindDateTime(), pendingIntent);
    }

}
