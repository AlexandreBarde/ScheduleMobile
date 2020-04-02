package fr.ynov.schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_emploi_du_temps_enfant extends AppCompatActivity  implements OnCompleteListener<QuerySnapshot> {
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<fr.ynov.schedule.Task> list_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emploi_du_temps_enfant);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        com.google.android.gms.tasks.Task<QuerySnapshot> docRef = db.collection("Task").get();
        docRef.addOnCompleteListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        QuerySnapshot querySnap = (QuerySnapshot) task.getResult();
        List<DocumentSnapshot> documents = querySnap.getDocuments();

        list_task = new ArrayList<fr.ynov.schedule.Task>();
        for(DocumentSnapshot doc : documents) {
            Map<String, Object> map = doc.getData();
            int image_task;
            switch (doc.get("image_status").toString()) {
                case "1" :
                    image_task = R.drawable.image_task_green;
                    break;
                case "3" :
                    image_task = R.drawable.image_task_grey;
                    break;
                case "2" :
                    image_task = R.drawable.image_task_red;
                    break;
                default :
                    image_task = R.drawable.image_task_green;
            }
            list_task.add(new fr.ynov.schedule.Task(doc.get("name").toString(), doc.get("description").toString(), doc.get("date").toString(), image_task,"[]" ));
        }
        recyclerView = findViewById(R.id.taches_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new TaskAdapter(list_task);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}
