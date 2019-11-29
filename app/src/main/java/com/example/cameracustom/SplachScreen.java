package com.example.cameracustom;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public class SplachScreen extends AppCompatActivity {
    private static int SPLACH_TIME_OUT=3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent home = new Intent(SplachScreen.this, Declaration.class);
                startActivity(home);
                finish();
            }
        },SPLACH_TIME_OUT);



    }
}