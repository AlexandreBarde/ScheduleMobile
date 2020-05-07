package fr.ynov.schedule.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import fr.ynov.schedule.R;
import fr.ynov.schedule.alarm.AlarmReceiver;

public class StopRingingAlarm extends AppCompatActivity{
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_ringing_alarm);
        context = getApplicationContext();
        AlarmReceiver.r.stop();
    }
}
