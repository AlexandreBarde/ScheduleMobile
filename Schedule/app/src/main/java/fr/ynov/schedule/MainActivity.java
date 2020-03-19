package fr.ynov.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Button btn_parents = findViewById(R.id.btn_parents);
        btn_parents.setOnClickListener(this);

        Button btn_childs = findViewById(R.id.btn_childs);
        btn_childs.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.btn_parents)
        {
            Intent parentsView = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(parentsView);
        }
        else if(view.getId() == R.id.btn_childs) {
            Intent childView = new Intent(getApplicationContext(), MainChildActivity.class);
            startActivity(childView);
        }
    }
}
