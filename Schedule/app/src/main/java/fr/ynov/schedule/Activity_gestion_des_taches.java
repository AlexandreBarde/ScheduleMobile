package fr.ynov.schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentReference;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Activity_gestion_des_taches extends AppCompatActivity implements View.OnClickListener {
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
}
