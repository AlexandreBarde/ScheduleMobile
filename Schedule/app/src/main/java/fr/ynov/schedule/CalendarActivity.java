package fr.ynov.schedule;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.ynov.schedule.stats.StatsTask;

public class CalendarActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener, OnCompleteListener<QuerySnapshot> {

    public static Calendar calendar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<fr.ynov.schedule.Task> list_task;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendar = Calendar.getInstance();

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

        CalendarActivity.calendar = getCalendarDay(Integer.parseInt(newDay), Integer.parseInt(newMonth), year);

        // Récupération des tâches en fonction du jour sur lequel on a cliqué

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Task<QuerySnapshot> docRef = db.collection("Task").get();
        docRef.addOnCompleteListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task)
    {
        QuerySnapshot querySnap = (QuerySnapshot) task.getResult();
        List<DocumentSnapshot> documents = querySnap.getDocuments();
        list_task = new ArrayList<fr.ynov.schedule.Task>();
        long minTimestamp = getMinTimestamp(CalendarActivity.calendar);
        long maxTimestamp = getMaxTimestamp(CalendarActivity.calendar);

        for(DocumentSnapshot doc : documents)
        {
            if(Long.parseLong(doc.get("timestamp").toString()) >= minTimestamp && Long.parseLong(doc.get("timestamp").toString()) <= maxTimestamp)
            {
                long timestamp = Long.parseLong(doc.get("timestamp").toString());
                Date date = new Date(timestamp);
                Timestamp ts = new Timestamp(date.getTime());
                Long durré =  Long.parseLong(doc.get("durée_minutes").toString());
                list_task.add(new fr.ynov.schedule.Task(doc.get("name").toString(), doc.get("description").toString(), timestamp,doc.get("state").toString(), 1, durré ));
            }
        }
        recyclerView = findViewById(R.id.recycler_tasks_calendar);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new TaskAdapterCalendar(list_task);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    public Calendar getCalendarDay(int day, int month, int year)
    {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0);
        return c;
    }

    public long getMinTimestamp(Calendar c)
    {
        Calendar cCopy = c;
        cCopy.set(Calendar.HOUR_OF_DAY, 0);
        cCopy.set(Calendar.MINUTE, 0);
        cCopy.set(Calendar.SECOND, 0);
        cCopy.set(Calendar.MILLISECOND, 1);
        return cCopy.getTimeInMillis();
    }

    public long getMaxTimestamp(Calendar c)
    {
        Calendar cCopy = c;
        cCopy.set(Calendar.HOUR_OF_DAY, 23);
        cCopy.set(Calendar.MINUTE, 59);
        cCopy.set(Calendar.SECOND, 59);
        cCopy.set(Calendar.MILLISECOND, 59);
        return cCopy.getTimeInMillis();
    }

}
