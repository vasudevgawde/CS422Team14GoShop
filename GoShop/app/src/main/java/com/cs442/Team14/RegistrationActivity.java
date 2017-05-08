package com.cs442.Team14;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Button button = (Button) findViewById(R.id.registrationButtonSubmit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText userName = (EditText) findViewById(R.id.registrationUserName);
                EditText phone = (EditText) findViewById(R.id.registrationPhone);
                EditText address = (EditText) findViewById(R.id.registrationAddress);
                EditText pwd = (EditText) findViewById(R.id.registrationPwd);
                EditText confPwd = (EditText) findViewById(R.id.registrationConfPwd);
                EditText email = (EditText) findViewById(R.id.registrationEmail);

                if(userName.getText().toString().equals(""))
                    userName.setError("Enter user name");

                else if(phone.getText().toString().equals(""))
                    phone.setError("Enter phone number");

                else if(pwd.getText().toString().equals(""))
                    pwd.setError("Enter password");

                else if(email.getText().toString().equals(""))
                    pwd.setError("Enter e-mail");

                else if(!pwd.getText().toString().equals(confPwd.getText().toString()))
                {confPwd.setError("Passwords do not match");
                    pwd.setError("");}

                else
                {
                    //Save the user details in shared preference
                    SharedPreferences preferences = getSharedPreferences(GoShopApplicationData.GO_SHOP_SHARED_PREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString(GoShopApplicationData.USER_NAME, userName.getText().toString());
                    editor.putString(GoShopApplicationData.USER_PHONE, phone.getText().toString());
                    editor.putString(GoShopApplicationData.USER_EMAIL, email.getText().toString());
                    editor.putString(GoShopApplicationData.USER_ADDRESS, address.getText().toString());
                    editor.putString(GoShopApplicationData.USER_PASSWORD, pwd.getText().toString());
                    editor.putBoolean(GoShopApplicationData.USER_LOGGED_IN_STATUS, true);
                    editor.putBoolean(GoShopApplicationData.USER_CREDENTIAL_EXISTS, true);
                    editor.commit();

                    Intent intent = new Intent(RegistrationActivity.this, MenuActivity.class);
                    startActivity(intent);

                }


            }
        });

    }
}
