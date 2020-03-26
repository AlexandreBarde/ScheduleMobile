package fr.ynov.schedule;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity  
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("name", "enfant");
        user.put("password", "test123");

        Button login = findViewById(R.id.login);
        login.setEnabled(false);
        login.setAlpha(.5f);
        login.setOnClickListener(new LoginButton());

        EditText password = findViewById(R.id.password);
        password.addTextChangedListener(new LoginPassword(login));

        EditText username = findViewById(R.id.username);
        username.addTextChangedListener(new LoginUsername(login));

        /**
         db.collection("users")
         .add(user)
         .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
        @Override public void onSuccess(DocumentReference documentReference) {
        Log.d("xxxx", "DocumentSnapshot added with ID: " + documentReference.getId());
        }
        })
         .addOnFailureListener(new OnFailureListener() {
        @Override public void onFailure(@NonNull Exception e) {
        Log.w("xxxx", "Error adding document", e);
        }
        }); **/

    }

}
