package fr.ynov.schedule.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import fr.ynov.schedule.R;
import fr.ynov.schedule.alarm.ListAlarmClock;
import fr.ynov.schedule.stats.StatsActivity;
import fr.ynov.schedule.task.TaskManagement;


public class MainParent extends AppCompatActivity implements View.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_parent);
        Button button_ajouter_une_tache = findViewById(R.id.button_gestion_des_taches);
        Button button_ajouter_un_reveil =  findViewById(R.id.button_ajouter_un_reveil);
        Button buttonStats = findViewById(R.id.button_show_stats);
        buttonStats.setOnClickListener(this);
        button_ajouter_une_tache.setOnClickListener(this);
        button_ajouter_un_reveil.setOnClickListener(this);
        Log.d("test", "build: build");
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case(R.id.button_gestion_des_taches):
                Intent gestion_des_taches_view = new Intent(getApplicationContext(), TaskManagement.class);
                startActivity(gestion_des_taches_view);
                break;
            case(R.id.button_ajouter_un_reveil):
                Intent ajout_reveil_view = new Intent(getApplicationContext(), ListAlarmClock.class);
                startActivity(ajout_reveil_view);
                break;
            case(R.id.button_show_stats):
                Intent showStats = new Intent(getApplicationContext(), StatsActivity.class);
                startActivity(showStats);
                break;
            default:
        }
    }
}
