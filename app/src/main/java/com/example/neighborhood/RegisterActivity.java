package com.example.neighborhood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText username, eMail, password;
    Button  btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        eMail = findViewById(R.id.eMail);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);
    }

    private void register(String username, String eMail, String password){

    }
}
