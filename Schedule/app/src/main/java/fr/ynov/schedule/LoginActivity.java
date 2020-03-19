package fr.ynov.schedule;

import android.os.Bundle;

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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("name", "enfant");
        user.put("password", "test123");

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
