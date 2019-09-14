package com.example.cameracustom;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class Show extends SurfaceView implements SurfaceHolder.Callback {

    Camera camera;
    SurfaceHolder mholder;


    public Show(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        mholder = getHolder();
        mholder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        }catch (IOException e){
            Log.d(VIEW_LOG_TAG, "Error setting camera preview");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mholder.getSurface() == null){
            return;
        }
        try {
            camera.stopPreview();
        }catch (Exception e){}


        try{
            camera.setPreviewDisplay(mholder);
            camera.startPreview();
        }catch (IOException e ){
            Log.d(VIEW_LOG_TAG, "Error setting camera preview");
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
