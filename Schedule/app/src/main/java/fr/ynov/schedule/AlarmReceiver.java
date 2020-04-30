package fr.ynov.schedule;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static android.content.Context.POWER_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "reveil";
    private AlarmManager alarmMgr;
    private Context c_context;
    public static Ringtone r;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.prefs.getContext());
        PowerManager pm = (PowerManager)context.getSystemService(POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if(!isScreenOn)
        {
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");
            wl.acquire(10000);
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");

            wl_cpu.acquire(10000);
        }

        WakeLocker.acquire(context);
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        c_context = context;
        Toast.makeText(context, "ALARM....", Toast.LENGTH_LONG).show();
        Uri alert;
        if(null  != prefs.getString("alarm_song",null) ) {
             alert = Uri.parse(prefs.getString("alarm_song",null));
        }else {
             alert = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.arouf);
        }
        Log.d("xxxx", "onReceive: " + alert);
        r = RingtoneManager.getRingtone(context, alert);

        if(r == null){

            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            r = RingtoneManager.getRingtone(context, alert);

            if(r == null){
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                r = RingtoneManager.getRingtone(context, alert);
            }
        }
        if(r != null)
            r.play();
        ReveilService.setNewAlarm.setNewAlarm();

        Intent n_intent =new Intent(c_context,stopRingingAlarm.class);
        String CHANNEL_ID="reveil";
        NotificationChannel notificationChannel= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID,"reveil", NotificationManager.IMPORTANCE_LOW);
        }
        PendingIntent pendingIntent=PendingIntent.getActivity(c_context,1,n_intent,PendingIntent.FLAG_ONE_SHOT);
        Notification notification= null;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("xxxx", "onReceive: LA NOTIF");
            notification = new Notification.Builder(c_context,CHANNEL_ID)
                    .setContentText("Cliquez pour arrêter le réveil")
                    .setContentTitle("REVEILLE TOI")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.sym_action_chat)
                    .build();
        }

        NotificationManager notificationManager=(NotificationManager) c_context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(1,notification);
    }
}
