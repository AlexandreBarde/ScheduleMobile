package fr.ynov.schedule;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;

public class LoginPassword implements TextWatcher
{

    private Button login;

    public LoginPassword(Button login)
    {
        this.login = login;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {
        if(charSequence.length() == 5)
        {
            this.login.setEnabled(true);
            this.login.setAlpha(1f);
        }
        Log.i("xxxx", charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

}
