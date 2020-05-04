package com.busra.reminderapp;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private Dialog reminderMessageDialog;
    private TextView messageToRemind;
    private Button closeBtn;
    @Override
    public void onReceive(Context context, Intent intent) {
        String message="Reminder alert! :"+intent.getStringExtra("REMINDER_MESSAGE");
        Log.d("REMINDER",message);
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
        /*reminderMessageDialog = new Dialog(context.getApplicationContext());
        //set up a dialog to get the reminder message from the user.
        reminderMessageDialog.setContentView(R.layout.reminder_message_popup);
        messageToRemind = reminderMessageDialog.findViewById(R.id.messageToRemind);
        closeBtn = reminderMessageDialog.findViewById(R.id.closeBtn);
        messageToRemind.setText(message);
        reminderMessageDialog.show();
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderMessageDialog.dismiss();
            }
        });*/
    }
}
