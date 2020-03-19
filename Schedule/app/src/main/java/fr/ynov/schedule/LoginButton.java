package fr.ynov.schedule;


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
    };

    @Override
    public void onComplete(@NonNull Task task)
    {
        TextView username = this.parent.findViewById(R.id.username);
        QuerySnapshot querySnap = (QuerySnapshot) task.getResult();
        List<DocumentSnapshot> documents = querySnap.getDocuments();
        boolean isFound = false;
        for(DocumentSnapshot doc : documents)
        {
            Map<String, Object> map = doc.getData();
            for(Map.Entry<String, Object> entry: map.entrySet())
            {
                Log.i("xxxx", "entrée : " + entry.getValue().toString());
                Log.i("xxxx", "egal : " + entry.getValue().toString() + " " + username.getText());
                if(entry.getValue().toString().equals(username.getText().toString()))
                {
                    Log.i("xxxx", "oui");
                    isFound = true;
                }
            }
        }
        TextView error = this.parent.findViewById(R.id.error_login);

        if(!isFound)
        {
            error.setText("Utilisateur non trouvé !");
        }
        else
        {
            error.setText("Yes");
        }
    }


    /**
        ApiFuture<DocumentSnapshot> future = (ApiFuture<DocumentSnapshot>) docRef.get();
        DocumentSnapshot document = null;
        try
        {
            document = future.get();
            if(document.exists())
            {

            }
            else
            {
                error.setText("L'utilisateur n'a pas été trouvé !");
            }
        }
        catch (ExecutionException | InterruptedException e)
        {
            e.printStackTrace();
            error.setText("Erreur !");
        }

         **/


}

