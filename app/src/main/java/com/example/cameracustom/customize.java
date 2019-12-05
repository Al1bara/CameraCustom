package com.example.cameracustom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class customize extends AppCompatActivity {

    ImageView collar1, collar2, cuff1, cuff2, Mbutton1, Mbutton2, fabric1, fabric2;
    Button browsingTailors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);

        collar1 = findViewById(R.id.Collar1);
        collar2 = findViewById(R.id.Collar2);
        cuff1 = findViewById(R.id.Cuff1);
        cuff2 = findViewById(R.id.Cuff2);
        Mbutton1 = findViewById(R.id.Mainbutton1);
        Mbutton2 = findViewById(R.id.Mainbutton2);
        fabric1 = findViewById(R.id.Fabric1);
        fabric2 = findViewById(R.id.Fabric2);
        browsingTailors = findViewById(R.id.browsing_tailors);

        browsingTailors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(customize.this, tailors.class);
                startActivity(intent);
            }

        });
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bco1:
                collar1.setVisibility(View.VISIBLE);
                collar2.setVisibility(View.INVISIBLE);
                break;
            case R.id.bco2:
                collar2.setVisibility(View.VISIBLE);
                collar1.setVisibility(View.INVISIBLE);
                break;
            case R.id.bcu1:
                cuff1.setVisibility(View.VISIBLE);
                cuff2.setVisibility(View.INVISIBLE);
                break;
            case R.id.bcu2:
                cuff2.setVisibility(View.VISIBLE);
                cuff1.setVisibility(View.INVISIBLE);
                break;
            case R.id.mb1:
                Mbutton1.setVisibility(View.VISIBLE);
                Mbutton2.setVisibility(View.INVISIBLE);
                break;
            case R.id.mb2:
                Mbutton2.setVisibility(View.VISIBLE);
                Mbutton1.setVisibility(View.INVISIBLE);
                break;
            case R.id.bf1:
                fabric1.setVisibility(View.VISIBLE);
                fabric2.setVisibility(View.INVISIBLE);
                break;
            case R.id.bf2:
                fabric2.setVisibility(View.VISIBLE);
                fabric1.setVisibility(View.INVISIBLE);
                break;

        }
    }
}
