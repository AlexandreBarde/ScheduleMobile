package fr.ynov.schedule;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParentsSetAlarmClock extends AppCompatActivity implements View.OnClickListener {
    Map clickedButtons = new HashMap<String, Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_set_alarm_clock);

        clickedButtons.put("Lundi", false);
        clickedButtons.put("Mardi", false);
        clickedButtons.put("Mercredi", false);
        clickedButtons.put("Jeudi", false);
        clickedButtons.put("Vendredi", false);

        Button setAlarm = findViewById(R.id.setAlarm);
        setAlarm.setOnClickListener(this);

        TimePicker picker=(TimePicker)findViewById(R.id.alarmTimePicker);
        picker.setIs24HourView(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.setAlarm)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SET_ALARM}, 0);
            
            TimePicker picker=(TimePicker)findViewById(R.id.alarmTimePicker);
            picker.setIs24HourView(true);

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Iterator iterator = clickedButtons.entrySet().iterator();
            while (iterator.hasNext()) {
                Map<String, Object> alarms = new HashMap<>();

                Map.Entry val = (Map.Entry) iterator.next();
                if((Boolean) val.getValue()) {
                    alarms.put("day", val.getKey());
                    alarms.put("hour", picker.getHour() + ":" + picker.getMinute());
                    alarms.put("activation", true);
                    db.collection("alarms")
                            .add(alarms)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override public void onSuccess(DocumentReference documentReference) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "L'alarme a bien été ajoutée", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override public void onFailure(@NonNull Exception e) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Erreur.", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            });
                }
            }
        }
        if(view.getId() == R.id.lundi) {
            clickedButtons.put("Lundi", !(Boolean)clickedButtons.get("Lundi"));
            if((Boolean)clickedButtons.get("Lundi") == true) {
                view.setBackgroundColor(Color.parseColor("#03DAC5"));
            } else {
                view.setBackgroundColor(Color.parseColor("#FFCACACA"));
            }
        }
        if(view.getId() == R.id.mardi) {
            clickedButtons.put("Mardi", !(Boolean)clickedButtons.get("Mardi"));
            if((Boolean)clickedButtons.get("Mardi") == true) {
                view.setBackgroundColor(Color.parseColor("#03DAC5"));
            } else {
                view.setBackgroundColor(Color.parseColor("#FFCACACA"));
            }
        }
        if(view.getId() == R.id.mercredi) {
            clickedButtons.put("Mercredi", !(Boolean)clickedButtons.get("Mercredi"));
            if((Boolean)clickedButtons.get("Mercredi") == true) {
                view.setBackgroundColor(Color.parseColor("#03DAC5"));
            } else {
                view.setBackgroundColor(Color.parseColor("#FFCACACA"));
            }
        }
        if(view.getId() == R.id.jeudi) {
            clickedButtons.put("Jeudi", !(Boolean)clickedButtons.get("Jeudi"));
            if((Boolean)clickedButtons.get("Jeudi") == true) {
                view.setBackgroundColor(Color.parseColor("#03DAC5"));
            } else {
                view.setBackgroundColor(Color.parseColor("#FFCACACA"));
            }
        }
        if(view.getId() == R.id.vendredi) {
            clickedButtons.put("Vendredi", !(Boolean)clickedButtons.get("Vendredi"));
            if((Boolean)clickedButtons.get("Vendredi") == true) {
                view.setBackgroundColor(Color.parseColor("#03DAC5"));
            } else {
                view.setBackgroundColor(Color.parseColor("#FFCACACA"));
            }
        }
    }
}
