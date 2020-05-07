package fr.ynov.schedule.task;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fr.ynov.schedule.R;

public class TaskAddNew extends AppCompatActivity implements OnSuccessListener<DocumentReference>, View.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        TimePicker picker=(TimePicker)findViewById(R.id.timePicker1);
        picker.setIs24HourView(true);
        TimePicker picker_durée=(TimePicker)findViewById(R.id.timePickerDurée);
        picker_durée.setIs24HourView(true);
        picker_durée.setHour(0);
        picker_durée.setMinute(0);
        Button addtask = findViewById(R.id.button_ajouter_la_tache);
        addtask.setOnClickListener(this);
        Calendar current = Calendar.getInstance();
        for(int i = 0; i < 7 ; i ++ ) {
            current.set(Calendar.DAY_OF_WEEK, i);
            long timestamp = current.getTimeInMillis();
            Date date = new Date(timestamp);
            Timestamp ts = new Timestamp(date.getTime());
            Log.d("xxxx","i : " + i + "Date : " + ts + "");

        }

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

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    @Override
    public void onSuccess(DocumentReference documentReference) {
        Context context = getApplicationContext();
        CharSequence text = "La tache a bien été ajouté ";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        Log.d("xxxx","Insertion Ok");
        Intent gestion_task_view = new Intent(getApplicationContext(), TaskManagement.class);
        startActivity(gestion_task_view);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_ajouter_la_tache) {

            ArrayList<Integer> list_recurrence = setListRecurrence();
            createNewTask(list_recurrence);
        }
    }

    public int checkChecked(CheckBox checkBox) {
        if (checkBox.isChecked()) {
            return 1;
        }else{
            return 0;
        }



    }
    public ArrayList<Integer> setListRecurrence() {
        ArrayList<Integer> list_recurrence = new ArrayList<Integer>();
        CheckBox checkBox_samedi = (CheckBox) findViewById(R.id.checkbox_samedi);
        list_recurrence.add(checkChecked(checkBox_samedi));
        CheckBox checkBox_dimanche= (CheckBox) findViewById(R.id.checkbox_dimanche);
        list_recurrence.add(checkChecked(checkBox_dimanche));
        CheckBox checkBox_lundi = (CheckBox) findViewById(R.id.checkbox_lundi);
        list_recurrence.add(checkChecked(checkBox_lundi));
        CheckBox checkBox_mardi = (CheckBox) findViewById(R.id.checkbox_mardi);
        list_recurrence.add(checkChecked(checkBox_mardi));
        CheckBox checkBox_mercredi = (CheckBox) findViewById(R.id.checkbox_mercredi);
        list_recurrence.add(checkChecked(checkBox_mercredi));
        CheckBox checkBox_jeudi = (CheckBox) findViewById(R.id.checkbox_jeudi);
        list_recurrence.add(checkChecked(checkBox_jeudi));
        CheckBox checkBox_vendredi = (CheckBox) findViewById(R.id.checkbox_vendredi);
        list_recurrence.add(checkChecked(checkBox_vendredi));
        Log.d("xxxx","Liste : " + list_recurrence.toString());
        return  list_recurrence;

    }

    public void createNewTask(ArrayList<Integer> listRecurrence) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<Task> list_new_task = new ArrayList<Task>();
        EditText edit_name = findViewById(R.id.name_new_task);
        String  new_name_text = edit_name.getText().toString();
        EditText edit_description = findViewById(R.id.description_new_task);
        String  new_description_text = edit_description.getText().toString();
        TimePicker picker=(TimePicker)findViewById(R.id.timePicker1);
       // String hour_task = String.format("%02dH%02d", picker.getHour(), picker.getMinute());
        TimePicker picker_durée=(TimePicker)findViewById(R.id.timePickerDurée);
        long durée_minutes = picker_durée.getHour()*60 + picker_durée.getMinute();
        for(int i =0; i < listRecurrence.size(); i ++) {
            if(listRecurrence.get(i) == 1 ) {
                Calendar calendar_task = Calendar.getInstance();
                calendar_task.set(Calendar.DAY_OF_WEEK, i);
                calendar_task.set(Calendar.HOUR_OF_DAY, picker.getHour());
                calendar_task.set(Calendar.MINUTE, picker.getMinute());
                calendar_task.set(Calendar.SECOND, 0);

                long timestamp = calendar_task.getTimeInMillis();
                Log.d("xxxx","Long  : " + timestamp);
                list_new_task.add(new Task(new_name_text, new_description_text, timestamp , "todo", 1,durée_minutes));
            }
        }
        boolean nbDays = false;
        for(Integer i : listRecurrence)
        {
            if(i == 1) nbDays = true;
        }
        if(!nbDays)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Vous devez choisir au moins un jour.")
                    .setIcon(android.R.drawable.ic_delete)
                    .setTitle("Erreur !")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else
        {
            for(int i = 0; i < list_new_task.size(); i ++ ) {
                if(i == list_new_task.size() -1 ) {
                    db.collection("Task").add(list_new_task.get(i)).addOnSuccessListener(this);
                } else {
                    db.collection("Task").add(list_new_task.get(i));
                }
            }
        }

    }

}
