package com.example.neighborhood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    Animation splash_anim;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Animations
        splash_anim = AnimationUtils.loadAnimation(this, R.anim.splash_screen);

        //Hooks
        logo = findViewById(R.id.logo_iv);

        logo.setAnimation(splash_anim);
    }
}