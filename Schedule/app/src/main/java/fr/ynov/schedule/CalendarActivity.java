package fr.ynov.schedule;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        TextView textView = findViewById(R.id.calendar_textview);
        textView.setText(dateFormat.format(date));

        CalendarView calendarButton = findViewById(R.id.calendar);
        calendarButton.setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
    {
        String newMonth = "";
        String newDay = "";

        if(month < 10) newMonth = "0" + month;
        else newMonth = String.valueOf(month);

        if(dayOfMonth < 10) newDay = "0" + dayOfMonth;
        else newDay = String.valueOf(dayOfMonth);

        String date = newDay + "/" + newMonth + "/" + year;
        TextView textView = findViewById(R.id.calendar_textview);
        textView.setText(date);
    }
}
