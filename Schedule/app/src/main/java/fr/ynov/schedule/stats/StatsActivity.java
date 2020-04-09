package fr.ynov.schedule.stats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import fr.ynov.schedule.R;
import fr.ynov.schedule.Activity_no_data_graph;

public class StatsActivity extends AppCompatActivity implements OnCompleteListener<QuerySnapshot>
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        getTasks();
    }

    /**
     * Récupère toutes les tâches de la base de données
     */
    public void getTasks()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Task<QuerySnapshot> docRef = db.collection("Task").get();
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
            if(Long.parseLong(doc.get("timestamp").toString()) > timestampFirstDayWeek) // si le timestamp du document (tâche) est plus grand que le timestamp du premier jour de la semaine
            {
                StatsTask statsTask = new StatsTask(
                        doc.get("name").toString(),
                        doc.get("description").toString(),
                        Long.parseLong(doc.get("timestamp").toString()),
                        doc.get("state").toString());
                tasks.add(statsTask);
                Log.i("xxxx", statsTask.toString());
            }
            else
            {
                Log.i("xxxx", Long.parseLong(doc.get("timestamp").toString()) + " > " + timestampFirstDayWeek);
                Log.i("xxxx", doc.toString());
            }
        }
        Log.i("xxxx","tasks : ");
        Log.i("xxxx", tasks.toString());
        if(tasks.size() <= 1)
        {
            Intent noDataView = new Intent(getApplicationContext(), Activity_no_data_graph.class);
            startActivity(noDataView);
        }
        else sortTasks(tasks);
    }

    /**
     * Retourne un timestamp qui correspond au premier jour de la semaine à 00h00
     * @return
     */
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

    /**
     *
     * @param listTasks
     */
    public void sortTasks(ArrayList<StatsTask> listTasks)
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

        Log.i("xxxx", "size : " + tasksWeek.size());

        if(tasksWeek.size() == 0)
        {
            Intent noDataView = new Intent(getApplicationContext(), Activity_no_data_graph.class);
            startActivity(noDataView);
        }

        DataPoint[] points = new DataPoint[tasksWeek.size()];
        Integer[] percents = new Integer[tasksWeek.size()];
        String[] days = new String[tasksWeek.size()];

        int posTMP = 0; // tmp -> TO REMOVE
        for(Integer i : tasksWeek.keySet())
        {
            text = text + getDayFr(i) + ":" + "\n";
            int nbTasks = tasksWeek.get(i).size(); // nombre de tâches par jour
            int nbTaskLate = 0; // nombre de tâches en retard par jour
            if(tasksWeek.get(i).size() > 1)
            {
                ArrayList<StatsTask> tasksTmp = new ArrayList<>();
                for(StatsTask ts : tasksWeek.get(i))
                {
                    if(ts.getState().equals("late")) nbTaskLate ++;
                    text = text + ts.getName() + ": " + ts.getDescription() + "\n";
                }
            }
            else
            {
                text = text + tasksWeek.get(i).get(0).getName() + ": " + tasksWeek.get(i).get(0).getDescription() + "\n";
            }
            int percent;
            if (nbTaskLate == 0) percent = 0;
            else percent = ((nbTaskLate * 100 ) / nbTasks);
            text = text + percent + "% de retard.\n";
            Log.i("xxxx", percent + "% de retard.");
            //points[i] = new DataPoint(i, percent);
            percents[posTMP] = percent;
            days[posTMP] = getDayFr(i);
            posTMP++;
        }

        Log.i("xxxx", Arrays.toString(percents));

        for(int i = 0; i < percents.length; i++)
        {
            points[i] = new DataPoint(i, percents[i]);
        }

        txt.setText(text);
        final GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setMaxY(100);
        graph.getViewport().setYAxisBoundsManual(true);
        LineGraphSeries <DataPoint> series = new LineGraphSeries<>(points);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        try
        {
            staticLabelsFormatter.setHorizontalLabels(days);
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.addSeries(series);
        }
        catch(IllegalStateException e)
        {
            Intent noDataView = new Intent(getApplicationContext(), Activity_no_data_graph.class);
            startActivity(noDataView);
        }
    }

    /**
     * Retourne le jour de la semaine en fonction du numéro de jour
     * @param day
     * @return
     */
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
