package fr.ynov.schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TaskNotificationLayout extends AppCompatActivity {

    Task task_test;
    private ArrayList<Task> tasks_result;
    private String taskName;
    private String taskDescription;
    private Long taskDureeMinutes;
    private long taskTimestamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_notification_layout);

        tasks_result = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String docRefIntent = getIntent().getExtras().getString("idTask");
        com.google.android.gms.tasks.Task<DocumentSnapshot> docRef = db.collection("Task").document(docRefIntent).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
        duree_tache.setText("Durée de la tache : " + taskDureeMinutes + " minutes");
        
    }

//    public String heureFormat() {
//        //long heu = Long.parseLong(taskDureeMinutes.toString()) / 60;
//        //long min = Long.parseLong(taskDureeMinutes.toString()) % 60;
//
//        if (heu == 0) {
//            return min + "min";
//        } else {
//            return heu + "h" + min + "min";
//        }
//    }
    public String dateFormat() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM HH:mm");
        Date date = new Date(taskTimestamp);
        return dateFormat.format(date);
    }
}
