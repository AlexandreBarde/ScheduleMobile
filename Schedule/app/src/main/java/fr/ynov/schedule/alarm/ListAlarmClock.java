package fr.ynov.schedule.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fr.ynov.schedule.R;

public class ListAlarmClock extends AppCompatActivity implements View.OnClickListener, OnCompleteListener<QuerySnapshot> {
    private RecyclerView recyclerView;
    private AlarmClockAdapter adapter;
    private RecyclerView.LayoutManager recyclerViewLManager;
    private ArrayList<AlarmClock> list_alarm_clock;
    private HashMap<String,DocumentSnapshot> map_task_references;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_show_list_alarm_clock);
        Button ajouter_reveil = this.findViewById(R.id.button_ajouter_un_reveil);
        ajouter_reveil.setOnClickListener(this);
        ArrayList<AlarmClock> alarmClockList = new ArrayList<>();
        Date currentTime = Calendar.getInstance().getTime();
        getAlarms();
    }

    public void removeItem(int position) {
        AlarmClock alarm_to_remove = list_alarm_clock.get(position);
        DocumentSnapshot doc =  map_task_references.get(alarm_to_remove.getDay() + alarm_to_remove.getHourAlarmClock() + position);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        list_alarm_clock.remove(position);
        adapter.notifyItemRemoved(position);
        db.collection("alarms").document(doc.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("xxxx", "onComplete: suppr ok");
                getAlarms();
            }
        });
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
    public void onClick(View view)
    {
        if(view.getId() == R.id.button_ajouter_un_reveil) {
            Intent ajouter_reveil = new Intent(getApplicationContext(), ParentsSetAlarmClock.class);
            startActivity(ajouter_reveil);
        }
    }

    @Override
    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> alarms) {
        QuerySnapshot querySnap = (QuerySnapshot) alarms.getResult();
        List<DocumentSnapshot> documents = querySnap.getDocuments();
        list_alarm_clock = new ArrayList<AlarmClock>();
        map_task_references = new  HashMap<String, DocumentSnapshot>();
        int nb_alarm = 0;

        for(DocumentSnapshot doc : documents) {
            Switch mSwitch = new Switch(this);
            mSwitch.setChecked((Boolean) doc.get("activation"));
            map_task_references.put(doc.get("day").toString() + doc.get("hour").toString() + nb_alarm, doc);
            list_alarm_clock.add(new AlarmClock(doc.get("hour").toString(),(Boolean) doc.get("activation") ,doc.get("day").toString(), (long) doc.get("timestamp")));
            nb_alarm ++;
        }
        recyclerView = findViewById(R.id.alarmClock_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerViewLManager = new LinearLayoutManager(this);
        adapter = new AlarmClockAdapter(list_alarm_clock);
        recyclerView.setLayoutManager(recyclerViewLManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AlarmClockAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                Log.d("xxxx", "onDeleteClick: " + position);
                removeItem(position);
            }
        });
    }

    public void getAlarms() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        com.google.android.gms.tasks.Task<QuerySnapshot> docRef = db.collection("alarms").orderBy("timestamp").get();
        docRef.addOnCompleteListener(this);
    }
}
