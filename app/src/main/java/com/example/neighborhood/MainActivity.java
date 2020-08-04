package com.example.neighborhood;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button register, login;
    private static int SPLASH_SCREEN = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent (MainActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);

        super.onCreate(savedInstanceState);
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

    /*new Handler().postDelayed(new Runnable()

    private void toRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);

        startActivity(intent);

    }*/

    private void toLogin() {

    }


}
