package fr.ynov.schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_gestion_des_taches extends AppCompatActivity implements View.OnClickListener, OnCompleteListener<QuerySnapshot> {
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private   ArrayList<Task> list_task;
    private HashMap<String,DocumentSnapshot> map_task_references;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_des_taches);
        getTask();
        Button button_add_task = findViewById(R.id.button_ajouter_des_taches);
        button_add_task.setOnClickListener(this);
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_ajouter_des_taches) {
            Intent add_task_view = new Intent(getApplicationContext(), Activity_add_task.class);
            startActivity(add_task_view);
        }

    }

    @Override
    public void onComplete(@NonNull final com.google.android.gms.tasks.Task<QuerySnapshot> task) {
        QuerySnapshot querySnap = (QuerySnapshot) task.getResult();
        List<DocumentSnapshot> documents = querySnap.getDocuments();
        map_task_references = new  HashMap<String, DocumentSnapshot>();
        createTaskList(documents);
        recyclerView = findViewById(R.id.taches_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new TaskAdapter(list_task);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });

    }
    public void removeItem(int position) {
        Task task_to_remove = list_task.get(position);
        Log.i("xxxx", task_to_remove.getName());
        DocumentSnapshot doc =  map_task_references.get(task_to_remove.getName() + position);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        list_task.remove(position);
        adapter.notifyItemRemoved(position);
        db.collection("Task").document(doc.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                Log.i("xxxx", "suppr ok");
                getTask();
            }
        });
    }

    public  void createTaskList(List<DocumentSnapshot> documents) {
        list_task = new ArrayList<Task>();
        int nb_task = 0;
        for (DocumentSnapshot doc : documents) {
            map_task_references.put(doc.get("name").toString() + nb_task, doc);
            Map<String, Object> map = doc.getData();
            int image_task;
            switch (doc.get("state").toString()) {
                case "done":
                    image_task = R.drawable.image_task_green;
                    break;
                case "todo":
                    image_task = R.drawable.image_task_grey;
                    break;
                case "late":
                    image_task = R.drawable.image_task_red;
                    break;
                default:
                    image_task = R.drawable.image_task_green;
            }
            long timestamp = Long.parseLong(doc.get("timestamp").toString());
            Date date = new Date(timestamp);
            Timestamp ts = new Timestamp(date.getTime());
            Long durré =  Long.parseLong(doc.get("durée_minutes").toString());
            list_task.add(new Task(doc.get("name").toString(), doc.get("description").toString(), timestamp,doc.get("state").toString(), image_task, durré));
            nb_task ++;
        }
    }
    public void getTask() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        com.google.android.gms.tasks.Task<QuerySnapshot> docRef = db.collection("Task").orderBy("timestamp").get();
        docRef.addOnCompleteListener(this);
    }
}
