package com.example.fersonaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
//        view.startAnimation(animation);

    }
}