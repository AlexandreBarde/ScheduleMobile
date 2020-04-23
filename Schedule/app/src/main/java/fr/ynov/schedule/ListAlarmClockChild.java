package fr.ynov.schedule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

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
import java.util.List;
import java.util.Map;

import fr.ynov.schedule.login.LoginActivity;

public class ListAlarmClockChild extends AppCompatActivity implements OnCompleteListener<QuerySnapshot> {

    private RecyclerView recyclerViewChild;
    private RecyclerView.Adapter recyclerViewChildAdapter;
    private RecyclerView.LayoutManager recyclerViewChildLManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_listing_alarm_clock);

        ArrayList<AlarmClock> alarmClockList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Date currentTime = Calendar.getInstance().getTime();
        com.google.android.gms.tasks.Task<QuerySnapshot> docRef = db.collection("alarms").orderBy("timestamp").get();
        docRef.addOnCompleteListener(this);

        recyclerViewChild = findViewById(R.id.alarmClock_recyclerView_child);
        recyclerViewChild.setHasFixedSize(true);
        recyclerViewChildLManager = new LinearLayoutManager(this);
        recyclerViewChildAdapter = new AlarmClockAdapter(alarmClockList);

        recyclerViewChild.setLayoutManager(recyclerViewChildLManager);
        recyclerViewChild.setAdapter(recyclerViewChildAdapter);
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
    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> alarms) {
        QuerySnapshot querySnap = (QuerySnapshot) alarms.getResult();
        List<DocumentSnapshot> documents = querySnap.getDocuments();
        ArrayList<fr.ynov.schedule.AlarmClock> list_alarm_clock = new ArrayList<AlarmClock>();
        for(DocumentSnapshot doc : documents) {
            list_alarm_clock.add(new fr.ynov.schedule.AlarmClock(doc.get("hour").toString(),(Boolean) doc.get("activation") ,doc.get("day").toString(), (long) doc.get("timestamp")));
        }

        recyclerViewChild = findViewById(R.id.alarmClock_recyclerView_child);
        recyclerViewChild.setHasFixedSize(true);
        recyclerViewChildLManager = new LinearLayoutManager(this);
        recyclerViewChildAdapter = new ListAlarmClockChildAdapter(list_alarm_clock);
        recyclerViewChild.setLayoutManager(recyclerViewChildLManager);
        recyclerViewChild.setAdapter(recyclerViewChildAdapter);
    }
}
