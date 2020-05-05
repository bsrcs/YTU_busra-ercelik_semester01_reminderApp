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
import com.busra.reminderapp.dao.ReminderDao;
import com.busra.reminderapp.model.ReminderModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Button addReminderBtn;
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
                openAddReminderActivity();
            }
        });
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ReminderDao.getAllReminders());
        listview.setAdapter(adapter);
    }

    public void openAddReminderActivity() {
        Intent i = new Intent(this, AddReminderActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        Log.d(REMINDER_APP_CONSTANTS.LOG_LABEL,"came back to list activity!");
    }
}
