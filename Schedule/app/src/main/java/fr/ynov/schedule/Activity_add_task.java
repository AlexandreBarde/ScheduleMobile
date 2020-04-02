package fr.ynov.schedule;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Activity_add_task extends AppCompatActivity implements OnSuccessListener<DocumentReference>, View.OnClickListener
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
        Button addtask = findViewById(R.id.button_ajouter_la_tache);
        addtask.setOnClickListener(this);
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
        Intent gestion_task_view = new Intent(getApplicationContext(), Activity_gestion_des_taches.class);
        startActivity(gestion_task_view);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_ajouter_la_tache) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            EditText edit_name = findViewById(R.id.name_new_task);
            String  new_name_text = edit_name.getText().toString();
            EditText edit_description = findViewById(R.id.description_new_task);
            String  new_description_text = edit_description.getText().toString();
            TimePicker picker=(TimePicker)findViewById(R.id.timePicker1);
            String hour_task = String.format("%02dH%02d", picker.getHour(), picker.getMinute());
            ArrayList<Integer> list_recurrence = new ArrayList<Integer>();
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
            CheckBox checkBox_samedi = (CheckBox) findViewById(R.id.checkbox_samedi);
            list_recurrence.add(checkChecked(checkBox_samedi));
            CheckBox checkBox_dimanche= (CheckBox) findViewById(R.id.checkbox_dimanche);
            list_recurrence.add(checkChecked(checkBox_dimanche));
            Log.d("xxxx","Liste : " + list_recurrence.toString());
            Task new_task = new Task(new_name_text, new_description_text, hour_task, 1,list_recurrence.toString());

            db.collection("Task").add(new_task).addOnSuccessListener(this);
        }
    }

    public int checkChecked(CheckBox checkBox) {
        if (checkBox.isChecked()) {
            return 1;
        }else{
            return 0;
        }

    }
}
