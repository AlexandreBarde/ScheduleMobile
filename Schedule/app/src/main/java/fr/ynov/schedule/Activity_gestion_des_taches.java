package fr.ynov.schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Activity_gestion_des_taches extends AppCompatActivity implements View.OnClickListener, OnCompleteListener<QuerySnapshot> {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_des_taches);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        com.google.android.gms.tasks.Task<QuerySnapshot> docRef = db.collection("Task").get();
        docRef.addOnCompleteListener(this);
        Button button_add_task = findViewById(R.id.button_ajouter_des_taches);
        button_add_task.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_ajouter_des_taches) {
            Intent add_task_view = new Intent(getApplicationContext(), Activity_add_task.class);
            startActivity(add_task_view);

        }
    }

    @Override
    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
        QuerySnapshot querySnap = (QuerySnapshot) task.getResult();
        List<DocumentSnapshot> documents = querySnap.getDocuments();
        ArrayList<Task> list_task = new ArrayList<Task>();
        for(DocumentSnapshot doc : documents) {
            Map<String, Object> map = doc.getData();
            int image_task;
            switch (doc.get("image_status").toString()) {
                case "1" :
                    Log.i("xxxx", "image task : ok");
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
            list_task.add(new Task(doc.get("name").toString(), doc.get("description").toString(), doc.get("date").toString(), image_task,"[]" ));
        }

        recyclerView = findViewById(R.id.taches_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new TaskAdapter(list_task);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}

