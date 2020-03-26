package fr.ynov.schedule;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginUsername extends AppCompatActivity implements TextWatcher
{

    private Button login;

    public LoginUsername(Button login)
    {
        this.login = login;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

}
