package fr.ynov.schedule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

import fr.ynov.schedule.login.LoginActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    public static Preference prefs;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        MainActivity.prefs = new Preference(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Button btn_parents = findViewById(R.id.btn_parents);
        btn_parents.setOnClickListener(this);

        Button btn_childs = findViewById(R.id.btn_childs);
        btn_childs.setOnClickListener(this);

        TextView aPropos = findViewById(R.id.aPropos);
        aPropos.setOnClickListener(this);

        TextView settings = findViewById(R.id.button_settings);
        settings.setOnClickListener(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.prefs.getContext());
        if(prefs.getBoolean("nightMode", false))
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.btn_parents)
        {
            Intent parentsView = new Intent(getApplicationContext(), LoginActivity.class);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.prefs.getContext());
            final String login = prefs.getString("login", "");
            final String password = prefs.getString("password", "");
            Log.i("xxxx", "login:" + login + " password:" + password);
            if(login.length() == 0 || password.length() == 0) startActivity(parentsView);

            // Affichage du toast de chargement
            Context contextLoading = getApplicationContext();
            CharSequence loading = "Chargement des paramètres...";
            Toast toast = Toast.makeText(contextLoading, loading, Toast.LENGTH_LONG);
            toast.show();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Task<QuerySnapshot> docRef = db.collection("users").get();

            docRef.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    QuerySnapshot querySnap = (QuerySnapshot) task.getResult();
                    List<DocumentSnapshot> documents = querySnap.getDocuments();
                    boolean isFound = false;
                    boolean correctPassword = false;
                    for(DocumentSnapshot doc : documents)
                    {
                        Map<String, Object> map = doc.getData();
                        Log.i("xxxx", "doc :: " + doc.get("name"));
                        if(doc.get("name").equals(login.toString()))
                        {
                            isFound = true;
                            if(doc.get("name").equals(login.toString()) && doc.get("password").equals(password.toString()))
                            {
                                correctPassword = true;
                            }
                        }
                    }
                    CharSequence text = "";
                    if(!isFound)
                    {
                        text = "Utilisateur non trouvé !";
                    }
                    else if(isFound && !correctPassword)
                    {
                        text = "Le mot de passe n'est pas correct !";
                    }
                    else if(isFound && correctPassword)
                    {
                        text = "Re-bienvenue " + login;
                        Intent childView = new Intent(getApplicationContext(), Activity_main_parent.class);
                        startActivity(childView);
                    }
                    else
                    {
                        text = "Erreur";
                    }
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                    toast.show();
                }
            });
        }
        else if(view.getId() == R.id.btn_childs) {
            Intent childView = new Intent(getApplicationContext(), MainChildActivity.class);
            startActivity(childView);
        }
        else if(view.getId() == R.id.aPropos) {
            Intent aProposView = new Intent(getApplicationContext(), Activity_a_propos.class);
            startActivity(aProposView);
        }
        else if(view.getId() == R.id.button_settings)
        {
            Intent settingsView = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingsView);
        }
    }


}
