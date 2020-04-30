package fr.ynov.schedule;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class ReveilService extends Service {
    public int counter = 0;
    public static AlarmManager alarmMgr;
    public static PendingIntent alarmIntent;
    private static String[] joursSemaines;
    public static Context serviceContext;
    private static int hour;
    private static int minutes;
    public static List<Long> sortedAlarms;
    public static List<Long> sortedTasks;
    public static String docIdTask;
    public static Task docObjectTask;
    public static class setNewAlarm {
        public static void setNewAlarm() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Date currentTime = Calendar.getInstance().getTime();
            com.google.android.gms.tasks.Task<QuerySnapshot> docRef = db.collection("alarms").orderBy("timestamp").get();
            docRef.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                    ReveilService.setAlarmNotification((QuerySnapshot) task.getResult());
                }
            });
        }

        public static void setNewTask() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Date currentTime = Calendar.getInstance().getTime();
            com.google.android.gms.tasks.Task<QuerySnapshot> docRef = db.collection("Task").orderBy("timestamp", Query.Direction.ASCENDING).get();
            docRef.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                    ReveilService.setAlarmTask((QuerySnapshot) task.getResult());
                }
            });
        }
    }

    @Override
    public void onCreate() {
        alarmMgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        serviceContext = getApplicationContext();
        joursSemaines = new String[]{" ", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        super.onCreate();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Task").orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                setAlarmTask(queryDocumentSnapshots);
            }
        });
        db.collection("alarms").whereEqualTo("activation", true).addSnapshotListener(new EventListener<QuerySnapshot>() {
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                setAlarmNotification(queryDocumentSnapshots);
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void setAlarmNotification(QuerySnapshot queryDocumentSnapshots) {
        sortedAlarms = new ArrayList<Long>();
        ComponentName receiver = new ComponentName(serviceContext, AlarmReceiver.class);
        PackageManager pm = serviceContext.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        QuerySnapshot querySnap = queryDocumentSnapshots;
        List<DocumentSnapshot> documents = querySnap.getDocuments();
        ArrayList<AlarmClock> list_alarm_clock = new ArrayList<AlarmClock>();
        for (DocumentSnapshot doc : documents) {
            Switch mSwitch = new Switch(serviceContext);
            mSwitch.setChecked((Boolean) doc.get("activation"));
            list_alarm_clock.add(new fr.ynov.schedule.AlarmClock(doc.get("hour").toString(), (Boolean) doc.get("activation"), doc.get("day").toString(), (long) doc.get("timestamp")));
        }

        for (AlarmClock alarm_clock : list_alarm_clock) {
            hour = Integer.parseInt(alarm_clock.getHourAlarmClock().split(":")[0]);
            minutes = Integer.parseInt(alarm_clock.getHourAlarmClock().split(":")[1]);
            Date currentTime = Calendar.getInstance().getTime();
            if (alarm_clock.getDay().equals(joursSemaines[currentTime.getDay()])) {
                if (currentTime.getHours() < hour || (currentTime.getHours() == hour && currentTime.getMinutes() < minutes)) {
                    sortedAlarms.add(alarm_clock.getTimestamp());
                    Intent intent = new Intent(serviceContext, AlarmReceiver.class);
                    alarmIntent = PendingIntent.getBroadcast(serviceContext, 0, intent, 0);
                }
            }
        }
        Collections.sort(sortedAlarms);
        if (!sortedAlarms.isEmpty()) {
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, sortedAlarms.get(0), alarmIntent);

        }
    }

    public static void setAlarmTask(QuerySnapshot queryDocumentSnapshots) {
        sortedTasks = new ArrayList<Long>();
        ComponentName receiver = new ComponentName(serviceContext, TaskReceiver.class);
        PackageManager pm = serviceContext.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        QuerySnapshot querySnap = queryDocumentSnapshots;
        List<DocumentSnapshot> documents = querySnap.getDocuments();
        LinkedHashMap<String, Task> list_tasks = new LinkedHashMap<>();
        LinkedHashMap<String, Task> daily_tasks = new LinkedHashMap<>();
        long millis = System.currentTimeMillis();
        for (DocumentSnapshot doc : documents) {
            list_tasks.put(doc.getId(), new Task(doc.get("name").toString(), doc.get("description").toString(), Long.parseLong(doc.get("timestamp").toString()), doc.get("state").toString(), Integer.parseInt(doc.get("image_status").toString()), Long.parseLong(doc.get("durée_minutes").toString())));
        }
        Iterator it = list_tasks.entrySet().iterator();

        for (Map.Entry current_task : list_tasks.entrySet()) {
            Task current = (Task) current_task.getValue();
            if (current.getTimestamp() > millis) {
                daily_tasks.put((String) current_task.getKey(), current);
                Long alarmMilis = daily_tasks.entrySet().iterator().next().getValue().getTimestamp();
            }
        }

        if (!daily_tasks.isEmpty()) {
            Long alarmMilis = daily_tasks.entrySet().iterator().next().getValue().getTimestamp();

            Intent intent = new Intent(serviceContext, TaskReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(serviceContext, 0, intent, 0);
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, alarmMilis, alarmIntent);
            docIdTask = daily_tasks.entrySet().iterator().next().getKey();
            docObjectTask = daily_tasks.entrySet().iterator().next().getValue();
        }
    }
}