package fr.ynov.schedule.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import fr.ynov.schedule.MainActivity;
import fr.ynov.schedule.R;

public class SettingsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activitty);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.prefs.getContext());
        Log.d("xxxx", "onCreate: " + prefs.getString("alarm_song",null));

        Switch sMode = findViewById(R.id.switch_mode);
        RadioGroup rg = (RadioGroup) findViewById(R.id.alarm_radio_button_group);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.prefs.getContext());
                SharedPreferences.Editor editor = prefs.edit();
                switch(checkedId)
                {
                    case R.id.base_alarm:
                        Log.d("xxxx", "onCheckedChanged: Base");
                        String path_alarm_base = "android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.fantasy_alarm_clock;
                        editor.putString("alarm_song", path_alarm_base);
                        editor.apply();
                        break;
                    case R.id.custom_alarm:
                        Log.d("xxxx", "onCheckedChanged: custom");
                        String path_alarm = "android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.arouf;
                        editor.putString("alarm_song", path_alarm);
                        editor.apply();
                        break;
                }
            }
        });
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        {
            sMode.setChecked(true);
        }
        else sMode.setChecked(false);

        sMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveNightMode(true);
                    recreate();
                }
                else
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveNightMode(false);
                    recreate();
                }
            }
        });
    }

    private void saveNightMode(boolean state)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.prefs.getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("nightMode", state);
        editor.apply();
    }

}
