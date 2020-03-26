package fr.ynov.schedule;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginButton extends AppCompatActivity implements View.OnClickListener, OnCompleteListener
{

    private View parent;

    @Override
    public void onClick(View view)
    {
        this.parent = (View) view.getParent();
        TextView error = parent.findViewById(R.id.error_login);
        error.setText("");
        Log.i("xxxx", "azeaze");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Task<QuerySnapshot> docRef = db.collection("users").get();
        docRef.addOnCompleteListener(this);
    }

    @Override
    public void onComplete(@NonNull Task task)
    {
        Log.i("xxxx", "Data : Complete");
        TextView username = this.parent.findViewById(R.id.username);
        TextView password = this.parent.findViewById(R.id.password);
        QuerySnapshot querySnap = (QuerySnapshot) task.getResult();
        List<DocumentSnapshot> documents = querySnap.getDocuments();
        boolean isFound = false;
        boolean correctPassword = false;
        for(DocumentSnapshot doc : documents)
        {
            Map<String, Object> map = doc.getData();
            Log.i("xxxx", "doc :: " + doc.get("name"));
            if(doc.get("name").equals(username.getText().toString()))
            {
                isFound = true;
                if(doc.get("name").equals(username.getText().toString()) && doc.get("password").equals(password.getText().toString()))
                {
                    correctPassword = true;
                }
            }
        }
        TextView error = this.parent.findViewById(R.id.error_login);
        if(!isFound)
        {
            error.setText("Utilisateur non trouvé !");
        }
        else if(isFound && !correctPassword)
        {
            error.setText("Le mot de passe n'est pas correct !");
        }
        else if(isFound && correctPassword)
        {
            Intent childView = new Intent(parent.getContext(), Activity_main_parent.class);
            parent.getContext().startActivity(childView);
        }
        else
        {
            error.setText("Autre...");
        }
    }


}

