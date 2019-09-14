package com.example.cameracustom;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Camera camera;
    FrameLayout frameLayout;
    TextView textViewX, textViewY, textViewZ;
    Button button;
    Show show;
    final private int MY_REQUEST_CODE = 123;

    /* TBA
    1\ Create to pages
        - First for instructions
            * Please make sure your phone in 90 degree
            * The distant between you and the Object must be 1 meter
        - Second for Camera preview
            * the button show when device in 90 degree
    2\ Save pictures in the app to send it to the server
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = findViewById(R.id.camera_preview);
        textViewX = findViewById(R.id.textViewX);
        textViewY = findViewById(R.id.textViewY);
        textViewZ = findViewById(R.id.textViewZ);
        button = findViewById(R.id.button);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);



        // Create an instance of camera
        camera = getCameraInstance();
        camera.setDisplayOrientation(90);
        // Create Camera preview to set our activity
        show = new Show(this,camera);
        frameLayout.addView(show);
        CheckUserPermissions();

        // to autofocus
        Camera.Parameters params = camera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        camera.setParameters(params);

        //to register sensor
        sensorManager.registerListener(rvListener, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);



    }

    // Create a listener
    SensorEventListener rvListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            // More code goes here
            float[] rotationMatrix = new float[16];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);
            float[] remappedRotationMatrix = new float[16];
            SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, remappedRotationMatrix);
            // Convert to orientations
            float[] orientations = new float[3];
            SensorManager.getOrientation(remappedRotationMatrix, orientations);
            // convert rad to degree
            for(int i = 0; i < 3; i++) {
                orientations[i] = (float)(Math.toDegrees(orientations[i]));
            }

//            textViewX.setText("X: " + orientations[0]);
            textViewY.setText("Y: " +orientations[1]);
            textViewZ.setText("Z: "+orientations[2]);

            // take pic in 90 degree
            if ((orientations[2] < 1 & orientations[2] > -1) && (orientations[1] < 1 & orientations[1] > -1) ){
                button.setVisibility(View.VISIBLE);
            }else
                button.setVisibility(View.GONE);
//            textViewX.setText("X: " + sensorEvent.values[0]);
//            textViewY.setText("Y: " +sensorEvent.values[1]);
//            textViewZ.setText("Z: "+sensorEvent.values[2]);
    }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };


    public void onClick(View view){

    }





    void CheckUserPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            // Camera permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                Manifest.permission.CAMERA},
                        MY_REQUEST_CODE);
                return;
            }
            // Storage permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE);
                return;
            }
        }
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }


}
