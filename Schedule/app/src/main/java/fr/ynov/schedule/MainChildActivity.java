package fr.ynov.schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainChildActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_child);
        Button button_emploi_du_temp = findViewById(R.id.button_tache);
        button_emploi_du_temp.setOnClickListener(this);
        Button button_listing_reveils_enfant = findViewById(R.id.button_voir_les_reveils);
        button_listing_reveils_enfant.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_tache) {
            Intent emploi_du_temps_enfant_vew = new Intent(getApplicationContext(), Activity_emploi_du_temps_enfant.class);
            startActivity(emploi_du_temps_enfant_vew);
        } else if(v.getId() == R.id.button_voir_les_reveils) {
            Intent emploi_du_temps_enfant_vew = new Intent(getApplicationContext(), ListAlarmClockChild.class);
            startActivity(emploi_du_temps_enfant_vew);
        }
    }
}
