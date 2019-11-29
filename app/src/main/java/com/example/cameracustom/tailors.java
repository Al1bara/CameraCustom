package com.example.cameracustom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class tailors extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailors);


    }

    public void onClick(View view){


        switch (view.getId()){
            case R.id.b1:

                Toast.makeText(this," Your Order has been sent to Tailor 1 ",Toast.LENGTH_LONG).show();
                break;
            case R.id.b2:
                Toast.makeText(this," Your Order has been sent to Tailor 2 ",Toast.LENGTH_LONG).show();
                break;
            case R.id.b3:
                Toast.makeText(this," Your Order has been sent to Tailor 3 ",Toast.LENGTH_LONG).show();
                break;
            case R.id.b4:
                Toast.makeText(this," Your Order has been sent to Tailor 4 ",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
