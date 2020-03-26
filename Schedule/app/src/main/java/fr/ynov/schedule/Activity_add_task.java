package fr.ynov.schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Activity_add_task extends AppCompatActivity implements OnSuccessListener<DocumentReference>, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        TimePicker picker=(TimePicker)findViewById(R.id.timePicker1);
        picker.setIs24HourView(true);
        Button addtask = findViewById(R.id.button_ajouter_la_tache);
        addtask.setOnClickListener(this);
    }

    @Override
    public void onSuccess(DocumentReference documentReference) {
        Log.d("xxxx","Insertion Ok");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_ajouter_la_tache) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            EditText edit_name = findViewById(R.id.name_new_task);
            String  new_name_text = edit_name.getText().toString();
            EditText edit_description = findViewById(R.id.description_new_task);
            String  new_description_text = edit_description.getText().toString();
            Task new_task = new Task(new_name_text, new_description_text, "9H00", 1);
            db.collection("Task").add(new_task).addOnSuccessListener(this);


        }
    }
}
