package com.example.neighborhood;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button register, login;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
      //  super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button register = (Button) findViewById(R.id.register);
        Button login = (Button) findViewById(R.id.login);
        register.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                toRegister();

            }
        });
    }

    private void toRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);

        startActivity(intent);

    }

    private void toLogin() {

    }
}
