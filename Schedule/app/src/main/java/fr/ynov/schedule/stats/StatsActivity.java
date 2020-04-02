package fr.ynov.schedule.stats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import fr.ynov.schedule.R;

public class StatsActivity extends AppCompatActivity implements OnCompleteListener<QuerySnapshot>
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        getTasks();
    }

    public void getTasks()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Task<QuerySnapshot> docRef = db.collection("tasks_new").get();
        docRef.addOnCompleteListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task)
    {
        QuerySnapshot querySnap = (QuerySnapshot) task.getResult();
        List<DocumentSnapshot> documents = querySnap.getDocuments();
        ArrayList<StatsTask> tasks = new ArrayList<>();
        long timestampFirstDayWeek = getTimestampFirstDayWeek();
        for(DocumentSnapshot doc : documents)
        {
            if(Long.parseLong(doc.get("timestamp").toString()) > timestampFirstDayWeek)
            {
                StatsTask statsTask = new StatsTask(doc.get("name").toString(), doc.get("description").toString(), Long.parseLong(doc.get("timestamp").toString()));
                tasks.add(statsTask);
                Log.i("xxxx", statsTask.toString());
            }
            else
            {
                Log.i("xxxx", Long.parseLong(doc.get("timestamp").toString()) + " > " + timestampFirstDayWeek);
                Log.i("xxxx", doc.toString());
            }
        }
        jenesaispasencore(tasks);
    }

    public Long getTimestampFirstDayWeek()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        Log.i("xxxx", "day : " + day);
        while(calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
        {
            calendar.set(Calendar.DAY_OF_YEAR, --day);
            Log.i("xxxx", calendar.getTime() + "");
        }
        Log.i("xxxx", "date fin : " + calendar.getTime());
        Log.i("xxxx", "timestamp : " + calendar.getTimeInMillis());
        return(calendar.getTimeInMillis());
    }

    public void jenesaispasencore(ArrayList<StatsTask> listTasks)
    {
        Calendar calendar = Calendar.getInstance();
        HashMap<Integer, ArrayList<StatsTask>> tasksWeek = new HashMap<>(); // numéro du jour de la semaine : liste de tâches
        for(StatsTask st : listTasks)
        {
            long timestamp = st.getTimestamp();
            Date date = new Date(timestamp);
            Log.i("xxxx", date.getDay() + "");
            if(tasksWeek.containsKey(date.getDay()))
            {
                ArrayList<StatsTask> taskTmp = tasksWeek.get(date.getDay());
                taskTmp.add(st);
                tasksWeek.put(date.getDay(), taskTmp);
            }
            else // si le jour n'est pas encore dans la liste
            {
                ArrayList<StatsTask> taskTmp = new ArrayList<>();
                taskTmp.add(st);
                tasksWeek.put(date.getDay(), taskTmp);
            }
        }
        Log.i("xxxx", "Jours de la semaine :");
        Log.i("xxxx", tasksWeek.toString());
        TextView txt = findViewById(R.id.stats_text);
        String text = "";
        for(Integer i : tasksWeek.keySet())
        {
            if(tasksWeek.get(i).size() > 1)
            {
                ArrayList<StatsTask> tasksTmp = new ArrayList<>();
                for(StatsTask ts : tasksWeek.get(i))
                {
                    text = text + getDayFr(i) + " " + ts.getName() + ": " + ts.getDescription() + "\n";
                }
            }
            else
            {
                text = text + getDayFr(i) + " " + tasksWeek.get(i).get(0).getName() + ": " + tasksWeek.get(i).get(0).getDescription() + "\n";
            }
        }
        txt.setText(text);
    }

    public String getDayFr(int day)
    {
        String dayFr = "";
        switch(day)
        {
            case 1:
                dayFr = "Lundi";
                break;
            case 2:
                dayFr = "Mardi";
                break;
            case 3:
                dayFr = "Mercredi";
                break;
            case 4:
                dayFr = "Jeudi";
                break;
            case 5:
                dayFr = "Vendredi";
                break;
            case 6:
                dayFr = "Samedi";
                break;
            case 7:
                dayFr = "Dimanche";
                break;
        }
        return dayFr;
    }

}
