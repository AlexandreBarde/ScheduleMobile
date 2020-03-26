package fr.ynov.schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class Activity_main_parent extends AppCompatActivity implements View.OnClickListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_parent);
        Button button_ajouter_une_tache = findViewById(R.id.button_gestion_des_taches);
        Button button_ajouter_un_reveil =  findViewById(R.id.button_ajouter_un_reveil);
        button_ajouter_une_tache.setOnClickListener(this);
        button_ajouter_un_reveil.setOnClickListener(this);
        Log.d("test", "build: build");

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_gestion_des_taches) {
            Intent gestion_des_taches_view = new Intent(getApplicationContext(), Activity_gestion_des_taches.class);
            startActivity(gestion_des_taches_view);
        }
        if(v.getId() == R.id.button_ajouter_un_reveil) {
            Intent ajout_reveil_view = new Intent(getApplicationContext(), ParentsSetAlarmClock.class);
            startActivity(ajout_reveil_view);
        }
    }
}
