package fr.ynov.schedule.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fr.ynov.schedule.R;
import fr.ynov.schedule.task.TaskAdapterCalendar;

public class CalendarActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener, OnCompleteListener<QuerySnapshot>, CompactCalendarView.CompactCalendarViewListener {

    public static Calendar calendar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<fr.ynov.schedule.task.Task> list_task;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendar = Calendar.getInstance();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        TextView textView = findViewById(R.id.calendar_textview);
        textView.setText(dateFormat.format(date));

        CompactCalendarView compactCalendarView = findViewById(R.id.compactcalendar_view);
        compactCalendarView.setListener(this);

        // Affichage du jour
        String[] dateSplitted = dateFormat.format(date).split("/");
        int day =  Integer.parseInt(dateSplitted[0]);
        int month =  Integer.parseInt(dateSplitted[1]);
        String newMonth = "";
        String newDay = "";
        if(month < 10) newMonth = "0" + month;
        else newMonth = String.valueOf(month);
        if(day < 10) newDay = "0" + day;
        else newDay = String.valueOf(day);
        int year = Integer.parseInt(dateSplitted[2]);

        // Affichage du mois courant
        TextView calendarMonth = findViewById(R.id.calendar_month);
        calendarMonth.setText(new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.getTime()));

        getTasks(Integer.parseInt(newDay), Integer.parseInt(newMonth) - 1, year);
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

        getTasks(Integer.parseInt(newDay), Integer.parseInt(newMonth), year);
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task)
    {
        CompactCalendarView compactCalendarView = findViewById(R.id.compactcalendar_view);

        QuerySnapshot querySnap = (QuerySnapshot) task.getResult();
        List<DocumentSnapshot> documents = querySnap.getDocuments();
        list_task = new ArrayList<fr.ynov.schedule.task.Task>();
        long minTimestamp = getMinTimestamp(CalendarActivity.calendar);
        long maxTimestamp = getMaxTimestamp(CalendarActivity.calendar);
        compactCalendarView.removeAllEvents();
        for(DocumentSnapshot doc : documents)
        {
            if(Long.parseLong(doc.get("timestamp").toString()) >= minTimestamp && Long.parseLong(doc.get("timestamp").toString()) <= maxTimestamp)
            {
                long timestamp = Long.parseLong(doc.get("timestamp").toString());
                Date date = new Date(timestamp);
                Timestamp ts = new Timestamp(date.getTime());
                Long duration =  Long.parseLong(doc.get("durée_minutes").toString());
                list_task.add(new fr.ynov.schedule.task.Task(doc.get("name").toString(), doc.get("description").toString(), timestamp,doc.get("state").toString(), 1, duration));

            }
            Event ev = new Event(Color.RED, Long.parseLong(doc.get("timestamp").toString()));
            compactCalendarView.addEvent(ev);
        }
        recyclerView = findViewById(R.id.recycler_tasks_calendar);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new TaskAdapterCalendar(list_task);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void getTasks(int day, int month, int year)
    {
        CalendarActivity.calendar = getCalendarDay(day, month, year);

        // Récupération des tâches en fonction du jour sur lequel on a cliqué

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Task<QuerySnapshot> docRef = db.collection("Task").orderBy("timestamp", Query.Direction.ASCENDING).get();
        docRef.addOnCompleteListener(this);
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

    @Override
    public void onDayClick(Date dateClicked)
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String[] dateSplitted = dateFormat.format(dateClicked).split("/");
        int day =  Integer.parseInt(dateSplitted[0]);
        int month =  Integer.parseInt(dateSplitted[1]);
        String newMonth = "";
        String newDay = "";
        if(month < 10) newMonth = "0" + month;
        else newMonth = String.valueOf(month);
        if(day < 10) newDay = "0" + day;
        else newDay = String.valueOf(day);
        int year = Integer.parseInt(dateSplitted[2]);
        TextView textView = findViewById(R.id.calendar_textview);
        textView.setText(newDay + "/" + newMonth + "/" + year);
        getTasks(Integer.parseInt(newDay), Integer.parseInt(newMonth) - 1, year);
    }

    @Override
    public void onMonthScroll(Date firstDayOfNewMonth)
    {
        SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        TextView textView = findViewById(R.id.calendar_month);
        textView.setText(dateFormatForMonth.format(firstDayOfNewMonth));
    }
}
