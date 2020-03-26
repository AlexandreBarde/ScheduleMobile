package fr.ynov.schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Activity_gestion_des_taches extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_des_taches);
        ArrayList<Task> list_task = new ArrayList<Task>();
        for (int i = 0; i < 30; i ++ ) {
            int date = 9 + i;
            int image_task = R.drawable.image_task_green;
            if(i == 4) {
                image_task = R.drawable.image_task_red;
            } else if (i > 4 ) {
                image_task = R.drawable.image_task_grey;
            }

            list_task.add(new Task("Task " + i, "description of the task" + i,Integer.toString(date) + "H00", image_task ));
        }

        recyclerView = findViewById(R.id.taches_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new TaskAdapter(list_task);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);



    }
}
