package fr.ynov.schedule;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private AlarmManager alarmMgr;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Toast.makeText(context, "ALARM....", Toast.LENGTH_LONG).show();
        Vibrator v = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
        v.vibrate(10000);
        ReveilService.setNewAlarm.startActivityFromBackground();
        ReveilService.setNewAlarm.setNewAlarm();
    }

}
