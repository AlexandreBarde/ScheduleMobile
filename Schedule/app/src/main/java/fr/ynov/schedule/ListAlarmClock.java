package fr.ynov.schedule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.ynov.schedule.login.LoginActivity;

public class ListAlarmClock extends AppCompatActivity implements View.OnClickListener, OnCompleteListener<QuerySnapshot> {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_show_list_alarm_clock);

        Button ajouter_reveil = this.findViewById(R.id.button_ajouter_un_reveil);
        ajouter_reveil.setOnClickListener(this);
        ArrayList<AlarmClock> alarmClockList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        com.google.android.gms.tasks.Task<QuerySnapshot> docRef = db.collection("alarms").get();
        docRef.addOnCompleteListener(this);

        recyclerView = findViewById(R.id.alarmClock_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerViewLManager = new LinearLayoutManager(this);
        recyclerViewAdapter = new AlarmClockAdapter(alarmClockList);

        recyclerView.setLayoutManager(recyclerViewLManager);
        recyclerView.setAdapter(recyclerViewAdapter);
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
        ArrayList<fr.ynov.schedule.AlarmClock> list_alarm_clock = new ArrayList<AlarmClock>();
        for(DocumentSnapshot doc : documents) {
            Switch mSwitch = new Switch(this);
            mSwitch.setChecked((Boolean) doc.get("activation"));
            list_alarm_clock.add(new fr.ynov.schedule.AlarmClock(doc.get("day").toString() + ": " +  doc.get("hour").toString() ,mSwitch, new Button(this)));
        }

        recyclerView = findViewById(R.id.alarmClock_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerViewLManager = new LinearLayoutManager(this);
        recyclerViewAdapter = new AlarmClockAdapter(list_alarm_clock);
        recyclerView.setLayoutManager(recyclerViewLManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}