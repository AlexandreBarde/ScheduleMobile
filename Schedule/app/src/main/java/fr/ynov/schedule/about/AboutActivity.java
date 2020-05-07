package fr.ynov.schedule.about;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import fr.ynov.schedule.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_propos);

        ImageView alexandre = findViewById(R.id.image_alexandre);
        ImageView hugo = findViewById(R.id.image_hugo);
        ImageView adel = findViewById(R.id.image_adel);

        alexandre.setClipToOutline(true);
        hugo.setClipToOutline(true);
        adel.setClipToOutline(true);


    }
}
