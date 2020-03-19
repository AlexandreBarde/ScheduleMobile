package fr.ynov.schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class ParentsSetAlarmClock extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_set_alarm_clock);

        Button setAlarm = findViewById(R.id.setAlarm);
        setAlarm.setOnClickListener(this);

        TimePicker picker=(TimePicker)findViewById(R.id.alarmTimePicker);
        picker.setIs24HourView(true);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.setAlarm)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SET_ALARM}, 0);


            TimePicker picker=(TimePicker)findViewById(R.id.alarmTimePicker);
            picker.setIs24HourView(true);

            Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
            intent.putExtra(AlarmClock.EXTRA_HOUR,picker.getHour());
            intent.putExtra(AlarmClock.EXTRA_MINUTES,picker.getMinute());

            startActivity(intent);
        }
    }
}
