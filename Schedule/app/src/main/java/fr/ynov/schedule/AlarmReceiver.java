package fr.ynov.schedule;

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
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "monchannel";
    private AlarmManager alarmMgr;
    private Context c_context;
    public static Vibrator alarmReceiverVibrator;
    public static Ringtone r;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        c_context = context;
        Toast.makeText(context, "ALARM....", Toast.LENGTH_LONG).show();
        alarmReceiverVibrator = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
        alarmReceiverVibrator.vibrate(5000);

        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
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
        ReveilService.setNewAlarm.startActivityFromBackground();
        ReveilService.setNewAlarm.setNewAlarm();

        Intent n_intent =new Intent(c_context,stopRingingAlarm.class);
        String CHANNEL_ID="MYCHANNEL";
        NotificationChannel notificationChannel= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID,"name", NotificationManager.IMPORTANCE_LOW);
        }
        PendingIntent pendingIntent=PendingIntent.getActivity(c_context,1,n_intent,PendingIntent.FLAG_ONE_SHOT);
        Notification notification= null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(c_context,CHANNEL_ID)
                    .setContentText("Cliquez pour arrêter le réveil")
                    .setContentTitle("REVEILLE TOI")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .addAction(R.drawable.common_google_signin_btn_icon_dark,"Stop alarm",pendingIntent)
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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = c_context.getString(R.string.channel_name);
            String description = c_context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "nouveau_channel_id", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = c_context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
