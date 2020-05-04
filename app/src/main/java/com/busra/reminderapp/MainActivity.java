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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.busra.reminderapp.constants.REMINDER_APP_CONSTANTS;
import com.busra.reminderapp.model.ReminderModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Button addReminderBtn;
    private Dialog reminderMessageDialog;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private ArrayList<ReminderModel> reminders;
    private ListView listview;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addReminderBtn = findViewById(R.id.addReminderBtn);
        listview = findViewById(R.id.listview);
        addReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDatePicker();
            }
        });
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        reminders = new ArrayList<>();
        adapter =  new ArrayAdapter(this, android.R.layout.simple_list_item_1, reminders);
        listview.setAdapter(adapter);

    }

    public void callDatePicker(){
        final Calendar selectedDateAndTime = Calendar.getInstance();
        //create a calender object that has the current time.
        final Calendar defaultDateAndTimeForDatePicker = Calendar.getInstance();
        //set up the onclick listener, where we want to call the datepicker and timepicker.
        //create a datepicker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    //the code that executes after the user has selected the date.
                    @Override
                    //the parameters reflect the date the user has selected.
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        callTimePicker(defaultDateAndTimeForDatePicker,selectedDateAndTime,year,month,dayOfMonth);
                    }
                },
                //set the default selected time in the datepicker.
                defaultDateAndTimeForDatePicker.get(Calendar.YEAR),
                defaultDateAndTimeForDatePicker.get(Calendar.MONTH),
                defaultDateAndTimeForDatePicker.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void callTimePicker(final Calendar defaultDateAndTimeForDatePicker, final Calendar selectedDateAndTime, final int year, final int month, final int dayOfMonth){
        //after selecting and setting the date, we move on to selecting the time.
        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            // the method that gets called upon the successful selection of a time.
            @Override
            //the parameters reflect the hour and minute chosen by the user
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //set the dates selected by the user in the variable that we will use later
                selectedDateAndTime.set(year,month,dayOfMonth,hourOfDay,minute);
                //show a toast with the selected time and date
                Toast.makeText(MainActivity.this, selectedDateAndTime.getTime().toString(), Toast.LENGTH_SHORT).show();
                //show dialog to get the reminder message from the user
                reminderMessageDialog(selectedDateAndTime);
            }
        },defaultDateAndTimeForDatePicker.get(Calendar.HOUR_OF_DAY),
                defaultDateAndTimeForDatePicker.get(Calendar.MINUTE),true);
        timePickerDialog.show();
    }

    public void reminderMessageDialog(final Calendar selectedDateAndTime){
        reminderMessageDialog = new Dialog(MainActivity.this);
        //set up a dialog to get the reminder message from the user.
        reminderMessageDialog.setContentView(R.layout.dialog_layout);
        //associate the elements in the dialog with variables
        final EditText dialogEditText = reminderMessageDialog.findViewById(R.id.message);
        TextView selectedDate = reminderMessageDialog.findViewById(R.id.selectedDate);
        Button addButton = reminderMessageDialog.findViewById(R.id.addButton);
        //set the selected date in the dialog to show the user what he/she has selected
        selectedDate.setText(selectedDateAndTime.getTime().toString());
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the reminder message from the user
                String reminderMessageFromUser = dialogEditText.getText().toString();
                //show the selected message
                Toast.makeText(MainActivity.this, reminderMessageFromUser, Toast.LENGTH_SHORT).show();
                //close the dialog
                reminderMessageDialog.dismiss();
                //create a reminder object from the input gathered by the dialogs
                ReminderModel reminderModel= new ReminderModel(
                        UUID.randomUUID().toString(),
                        reminderMessageFromUser,
                        selectedDateAndTime.getTimeInMillis(), REMINDER_APP_CONSTANTS.REMINDER_TYPE_ONCE);
                //printing a single reminder from the user
                //Log.d(REMINDER_APP_CONSTANTS.LOG_LABEL,reminderModel.toString());
                //print all the reminds
                //Log.d(REMINDER_APP_CONSTANTS.LOG_LABEL,reminders.toString());

                //add the reminder to the list of all reminders
                reminders.add(reminderModel);
                //refresh the adapter so that it can update the listview
                adapter.notifyDataSetChanged();
                //pass the reminderModel to the setReminder method to actually set the reminder
                setReminder(reminderModel);
            }
        });
        reminderMessageDialog.show();
    }


    //actually set the reminder
    private void setReminder(ReminderModel reminderModel){
        //create an intent to call the broadcastreciever
        Intent alarmIntent = new Intent(MainActivity.this, MyBroadcastReceiver.class);
        //set the reminder message in the intent
        alarmIntent.putExtra(REMINDER_APP_CONSTANTS.INTENT_DATA_KEY_NAME,reminderModel.getReminderMessage());
        //create an pending intent from the normal intent above
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,alarmIntent,0);
        //set the alarm with the pending intent
        //use selectedDateAndTime to get the interval after which the alarm will be triggered
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderModel.getRemindDateTime(), pendingIntent);
    }
}
