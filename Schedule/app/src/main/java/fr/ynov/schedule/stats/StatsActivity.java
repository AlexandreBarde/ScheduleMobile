package fr.ynov.schedule.stats;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
        Integer[] tasks = new Integer[tasksWeek.size()];
        Integer[] tasksLate = new Integer[tasksWeek.size()];

        int posTMP = 0; // tmp -> TO REMOVE
        for(Integer i : tasksWeek.keySet())
        {
            Log.i("xxxx", "tmp : " + i);
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
            Log.i("xxxx", "i " + i + " tmp " + posTMP + " nbTasks " + nbTasks + " nbTasksLate " + nbTaskLate);
            tasks[posTMP] = nbTasks;
            tasksLate[posTMP] = nbTaskLate;
            if (nbTaskLate == 0) percent = 0;
            else percent = 100 - ((nbTaskLate * 100 ) / nbTasks); // 100 % - (nombre de tâches en retard du jour * 100) / nombre de tâches total du jour
            text = text + percent + "% de retard.\n";
            Log.i("xxxx", percent + "% de retard.");
            percents[posTMP] = percent;
            days[posTMP] = getDayFr(i);
            posTMP++;
        }

        Log.i("xxxx", Arrays.toString(percents));
        Log.i("xxxx", Arrays.toString(days));

        for(int i = 0; i < percents.length; i++)
        {
            points[i] = new DataPoint(i, percents[i]);
        }

        TableLayout table = findViewById(R.id.stats_table);

        for (int i = 0; i < percents.length; i++)
        {
            Log.i("xxxx", "i " + i);
            Log.i("xxxx", "task : " + tasks[i]);
            Log.i("xxxx", "tasklate : " + tasksLate[i]);
            Log.i("xxxx", "point : " + days[i]);
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            row.setBackgroundResource(R.drawable.border);
            row.setLayoutParams(lp);

            TextView day = new TextView(this);
            TextView tasksLateTv = new TextView(this);
            TextView tasksTv = new TextView(this);
            TextView percent = new TextView(this);

            day.setText(days[i] + "");
            tasksLateTv.setText(tasksLate[i] + "");
            tasksTv.setText(tasks[i] + "");
            percent.setText(percents[i] + "");

            day.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tasksLateTv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tasksTv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            percent.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            day.setTextAppearance(R.style.border_table);
            day.setBackgroundResource(R.drawable.border);
            day.setLayoutParams(new TableRow.LayoutParams((int) (100 * getBaseContext().getResources().getDisplayMetrics().density), TableRow.LayoutParams.WRAP_CONTENT));

            tasksLateTv.setTextAppearance(R.style.border_table);
            tasksLateTv.setBackgroundResource(R.drawable.border);
            tasksLateTv.setLayoutParams(new TableRow.LayoutParams((int) (150 * getBaseContext().getResources().getDisplayMetrics().density), TableRow.LayoutParams.WRAP_CONTENT));

            tasksTv.setTextAppearance(R.style.border_table);
            tasksTv.setBackgroundResource(R.drawable.border);
            tasksLateTv.setLayoutParams(new TableRow.LayoutParams((int) (100 * getBaseContext().getResources().getDisplayMetrics().density), TableRow.LayoutParams.WRAP_CONTENT));

            percent.setTextAppearance(R.style.border_table);
            percent.setBackgroundResource(R.drawable.border);
            percent.setLayoutParams(new TableRow.LayoutParams((int) (61 * getBaseContext().getResources().getDisplayMetrics().density), TableRow.LayoutParams.WRAP_CONTENT));

            row.addView(day, 0);
            row.addView(tasksLateTv, 1);
            row.addView(tasksTv, 2);
            row.addView(percent, 3);

            table.addView(row);
        }

        final GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setMaxY(100);
        graph.getViewport().setYAxisBoundsManual(true);
        LineGraphSeries <DataPoint> series = new LineGraphSeries<>(points);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        try
        {
            staticLabelsFormatter.setHorizontalLabels(days);
            series.setAnimated(true);
            series.setColor(Color.argb(100, 98, 0, 238));
            series.setBackgroundColor(Color.argb(20, 98, 0, 238));
            series.setDrawBackground(true);
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
            case 0:
                dayFr = "Lundi";
                break;
            case 1:
                dayFr = "Mardi";
                break;
            case 2:
                dayFr = "Mercredi";
                break;
            case 3:
                dayFr = "Jeudi";
                break;
            case 4:
                dayFr = "Vendredi";
                break;
            case 5:
                dayFr = "Samedi";
                break;
            case 6:
                dayFr = "Dimanche";
                break;
        }
        return dayFr;
    }

}
