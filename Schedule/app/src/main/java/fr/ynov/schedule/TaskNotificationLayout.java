package fr.ynov.schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TaskNotificationLayout extends AppCompatActivity {

    Task task_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_notification_layout);
        task_test = new Task("Test","Description test",1588265280231L, "todo", 1, 5L);
        TextView name_task = findViewById(R.id.name_task);
        name_task.setText(task_test.getName());
        TextView description_task = findViewById(R.id.description_task);
        description_task.setText(task_test.getDescription());
        TextView heure_tache = findViewById(R.id.heure_tache);
        heure_tache.setText("Date de la tache : " + task_test.dateFormat());
        TextView duree_tache = findViewById(R.id.durée_tache);
        duree_tache.setText("Durée de la tache : " + task_test.heureFormat());
    }
}
