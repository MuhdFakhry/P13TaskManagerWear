package com.example.p13_taskmanagerwear;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    EditText etName, etDescription , etReminder;
    Button btnConfirmAdd, btnCancel;
    int reqCode = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etReminder = findViewById(R.id.etReminder);

        btnConfirmAdd = findViewById(R.id.btnConfirmAdd);
        btnCancel = findViewById(R.id.btnCancel);

        btnConfirmAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String description = etDescription.getText().toString();
                String reminder = etReminder.getText().toString();
                if (name.length() == 0) {
                    Toast.makeText(getBaseContext(), "Name cannot be empty", Toast.LENGTH_LONG).show();
                } else if (description.length() == 0) {
                    Toast.makeText(getBaseContext(), "Description cannot be empty", Toast.LENGTH_LONG).show();
                } else if (reminder.length() == 0) {
                    Toast.makeText(getBaseContext(), "Reminder cannot be empty", Toast.LENGTH_LONG).show();
                }else {
                    DBHelper db = new DBHelper(AddActivity.this);
                    db.insertTask(name, description);
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.SECOND, Integer.parseInt(reminder));

                    Intent intent = new Intent(AddActivity.this,
                            ScheduledNotificationReceiver.class);
                    intent.putExtra("name", name);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            AddActivity.this, reqCode,
                            intent, PendingIntent.FLAG_CANCEL_CURRENT);

                    AlarmManager am = (AlarmManager)
                            getSystemService(Activity.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                            pendingIntent);
                    Toast.makeText(getBaseContext(), "Task Inserted", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
