package com.example.cameracustom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class results extends AppCompatActivity {

    TextView neck,shoulders,waist,hieght,sleeve;
    String resultResponse;
    Button cus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        neck=findViewById(R.id.neck);
        shoulders=findViewById(R.id.shoulders);
        waist=findViewById(R.id.waist);
        hieght=findViewById(R.id.height);
       sleeve=findViewById(R.id.sleeve);
        cus = findViewById(R.id.cus);
            neck.setText(UploadPic.neck);
            hieght.setText(UploadPic.height);
            shoulders.setText(UploadPic.shoulders);
            waist.setText(UploadPic.waist);
            sleeve.setText(UploadPic.sleeve);

            cus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(results.this,customize.class);
                    startActivity(intent);
                }
            });
    }
}
//       resultResponse="{\n" +
//               "    \"height\": 56,\n" +
//               "    \"shoulders\": 18,\n" +
//               "    \"neck\": 10,\n" +
//               "    \"waist\": 18,\n" +
//               "    \"sleeve\": 23\n" +
//               "}";
//