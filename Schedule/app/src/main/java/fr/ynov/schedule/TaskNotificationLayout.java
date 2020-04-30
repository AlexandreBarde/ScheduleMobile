package fr.ynov.schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TaskNotificationLayout extends AppCompatActivity {

    Task task_test;
    private ArrayList<Task> tasks_result;
    private String taskName;
    private String taskDescription;
    private Long taskDureeMinutes;
    private long taskTimestamp;
    private com.google.android.gms.tasks.Task<DocumentSnapshot> docRef;
    private String docRefIntent;
    private String task_state;
    private long taskDureeMinutesToMillis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_notification_layout);
        tasks_result = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        docRefIntent = getIntent().getExtras().getString("idTask");
        docRef = db.collection("Task").document(docRefIntent).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                getTaskContent((DocumentSnapshot) task.getResult());
            }
        });
    }

    public void getTaskContent(DocumentSnapshot queryDocumentSnapshots) {
        DocumentSnapshot querySnap = queryDocumentSnapshots;
        taskName = (String) querySnap.get("name");
        taskDescription = (String) querySnap.get("description");
        taskDureeMinutes = Long.parseLong(Objects.requireNonNull(querySnap.get("durée_minutes")).toString());
        taskTimestamp = (long) querySnap.get("timestamp");

        Log.i("Servicestatus", taskName + " " + taskDescription + " " + taskDureeMinutes + " " + taskTimestamp);

        TextView name_task = findViewById(R.id.name_task);
        name_task.setText(taskName);
        TextView description_task = findViewById(R.id.description_task);
        description_task.setText(taskDescription);
        TextView heure_tache = findViewById(R.id.heure_tache);
        heure_tache.setText("Date de la tache : " + dateFormat());
        TextView duree_tache = findViewById(R.id.durée_tache);
        duree_tache.setText("Durée de la tache : " + heureFormat());

        Button ok_tache = findViewById(R.id.ok_tache);
        final TextView timer = findViewById(R.id.timer);
        taskDureeMinutesToMillis = TimeUnit.MINUTES.toMillis(taskDureeMinutes);

        CountDownTimer cdt = new CountDownTimer(taskDureeMinutesToMillis, 1000) {
            public void onTick(long taskTimestamp) {
                long h = TimeUnit.MILLISECONDS.toHours((taskTimestamp));
                long m = TimeUnit.MILLISECONDS.toMinutes(taskTimestamp - h*3600*1000);
                long s = TimeUnit.MILLISECONDS.toSeconds(taskTimestamp - (h*3600*1000 + m*60*1000));
                timer.setText(""+String.format("%02d:%02d:%02d",h,m,s) + " restants");
            }

            public void onFinish() {
                Log.i("oui", "oui");
            }
        }.start();

        ok_tache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.ok_tache) {
                    taskDureeMinutesToMillis = TimeUnit.MINUTES.toMillis(taskDureeMinutes);
                    long currentTimeMillis = System.currentTimeMillis();
                    if((taskTimestamp + taskDureeMinutesToMillis) - currentTimeMillis >= 0) {
                        task_state = "done";
                    } else {
                        task_state = "late";
                    }
                    Task task_new = new Task(taskName, taskDescription, taskTimestamp ,task_state, 1, taskDureeMinutes);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("Task").document(docRefIntent).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            Log.i("ouistiti", "suppr ok");
                        }
                    });

                    db.collection("Task").document(docRefIntent).set(task_new).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            Intent add_task_view = new Intent(getApplicationContext(), Activity_emploi_du_temps_enfant.class);
                            startActivity(add_task_view);
                        }
                    });
                }
            }
        });
    }

    public String heureFormat() {
        long heu = Long.parseLong(taskDureeMinutes.toString()) / 60;
        long min = Long.parseLong(taskDureeMinutes.toString()) % 60;

        if (heu == 0) {
            return min + "min";
        } else {
            return heu + "h" + min + "min";
        }
    }
    public String dateFormat() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM HH:mm");
        Date date = new Date(taskTimestamp);
        return dateFormat.format(date);
    }
}
