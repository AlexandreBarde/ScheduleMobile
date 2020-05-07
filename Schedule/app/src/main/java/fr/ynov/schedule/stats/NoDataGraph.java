package fr.ynov.schedule.stats;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import fr.ynov.schedule.main.MainParent;
import fr.ynov.schedule.R;

public class NoDataGraph extends AppCompatActivity implements View.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_data_graph);
        Button back = findViewById(R.id.btn_back);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent home = new Intent(getApplicationContext(), MainParent.class);
        startActivity(home);
    }

}
