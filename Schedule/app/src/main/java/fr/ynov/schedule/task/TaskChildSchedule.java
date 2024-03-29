package fr.ynov.schedule.task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import fr.ynov.schedule.R;

public class TaskChildSchedule extends AppCompatActivity  implements OnCompleteListener<QuerySnapshot> {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<fr.ynov.schedule.task.Task> list_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_schedule);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        com.google.android.gms.tasks.Task<QuerySnapshot> docRef = db.collection("Task").orderBy("timestamp").get();
        docRef.addOnCompleteListener(this);
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
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        QuerySnapshot querySnap = (QuerySnapshot) task.getResult();
        List<DocumentSnapshot> documents = querySnap.getDocuments();

        list_task = new ArrayList<fr.ynov.schedule.task.Task>();
        for(DocumentSnapshot doc : documents) {
            Map<String, Object> map = doc.getData();
            int image_task;
            switch (doc.get("state").toString()) {
                case "done" :
                    image_task = R.drawable.image_task_green;
                    break;
                case "todo" :
                    image_task = R.drawable.image_task_grey;
                    break;
                case "late" :
                    image_task = R.drawable.image_task_red;
                    break;
                default :
                    image_task = R.drawable.image_task_green;
            }
            long timestamp = Long.parseLong(doc.get("timestamp").toString());
            Date date = new Date(timestamp);
            Timestamp ts = new Timestamp(date.getTime());
            Long durré =  Long.parseLong(doc.get("durée_minutes").toString());
            list_task.add(new fr.ynov.schedule.task.Task(doc.get("name").toString(), doc.get("description").toString(), timestamp,doc.get("state").toString(), image_task, durré ));
        }
        recyclerView = findViewById(R.id.taches_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new TaskAdapterChild(list_task);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}
