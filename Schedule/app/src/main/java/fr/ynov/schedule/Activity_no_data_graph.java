package fr.ynov.schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity_no_data_graph extends AppCompatActivity implements View.OnClickListener
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
        Intent home = new Intent(getApplicationContext(), Activity_main_parent.class);
        startActivity(home);
    }

}
