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
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import static android.content.Context.POWER_SERVICE;

public class TaskReceiver extends BroadcastReceiver {
    private AlarmManager alarmMgr;
    private Context c_context;
    public static Vibrator alarmReceiverVibrator;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager)context.getSystemService(POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if(!isScreenOn)
        {
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");

            wl_cpu.acquire(10000);
        }
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        c_context = context;
        alarmReceiverVibrator = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
        alarmReceiverVibrator.vibrate(200);

        ReveilService.setNewAlarm.setNewTask();

        Intent n_intent =new Intent(c_context,TaskNotificationLayout.class);
        n_intent.putExtra("idTask", ReveilService.docIdTask);

        NotificationChannel notificationChannel= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("blbl","tache", NotificationManager.IMPORTANCE_HIGH);
        }
        PendingIntent pendingIntent=PendingIntent.getActivity(c_context,1,n_intent,PendingIntent.FLAG_ONE_SHOT);
        Notification notification= null;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(c_context,"blbl")
                    .setContentText(ReveilService.docIdTask)
                    .setContentTitle(ReveilService.docObjectTask.getName())
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .addAction(R.drawable.common_google_signin_btn_icon_dark,"Ouvrir",pendingIntent)
                    .setChannelId("blbl")
                    .setSmallIcon(android.R.drawable.sym_action_chat)
                    .build();
        }

        NotificationManager notificationManager=(NotificationManager) c_context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify((int) ReveilService.docObjectTask.getTimestamp(),notification);


    }
}
