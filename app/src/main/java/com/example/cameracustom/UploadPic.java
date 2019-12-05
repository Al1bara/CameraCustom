package com.example.cameracustom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.cameracustom.CameraPic.bitmap;

public class UploadPic extends AppCompatActivity {

    private ImageView camera_preview;
    private Button button;
    Button BackButton;
    private String url = "http://192.168.8.101:3000/upload";
    private static final String IMAGE_DIRECTORY = "/CustomImage";
    private String pathFile;
    private static final int RGB_MASK = 0x00FFFFFF;
    public static String resultResponse;
    public static String neck;
    public static String height;
    public static String waist;
    public static String shoulders;
    public static String sleeve;
    Bitmap correctedBitmap, negativeBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
        findViewById(R.id.progressBarCircularIndeterminate).setVisibility(View.GONE);
        camera_preview = findViewById(R.id.camera_preview);
        button = findViewById(R.id.upload);
        BackButton = findViewById(R.id.backButton);


        correctedBitmap = RotateBitmap(bitmap, (90 * 3));
        negativeBitmap = RotateBitmap(bitmap, (90 * 3));
        negativeBitmap = invert(correctedBitmap);
        camera_preview.setImageBitmap(negativeBitmap);

        saveImage(correctedBitmap);

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadPic.this, CameraPic.class);
                startActivity(intent);
            }
        });
    }


    public static byte[] getFileDataFromDrawable(Context context, Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        pathFile = wallpaperDirectory.getAbsolutePath();

        try {
            rotateImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs());
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();   //give read write permission
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }

    public void onClick(View view) {
        findViewById(R.id.progressBarCircularIndeterminate).setVisibility(View.VISIBLE);
        findViewById(R.id.upload).setEnabled(false);

        VolleyMultipartRequest stringRequest = new VolleyMultipartRequest(Request.Method.POST,
                url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        findViewById(R.id.progressBarCircularIndeterminate).setVisibility(View.INVISIBLE);
                        findViewById(R.id.upload).setEnabled(true);
                        resultResponse = new String(response.data);
                        Toast.makeText(getApplicationContext(),
                                resultResponse, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject object = new JSONObject(resultResponse);
                            height = object.getString("height");
                            neck = object.getString("neck");
                            sleeve = object.getString("sleeve");
                            waist = object.getString("waist");
                            shoulders = object.getString("shoulders");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(UploadPic.this, results.class);
                        startActivity(intent);


                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                findViewById(R.id.progressBarCircularIndeterminate).setVisibility(View.INVISIBLE);
                findViewById(R.id.upload).setEnabled(true);
                Toast.makeText(getApplicationContext(), "error: " +
                        error.toString(), Toast.LENGTH_LONG).show();
            }

        }) {
            //Http header
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                Drawable b = new BitmapDrawable(getResources(), correctedBitmap);
                params.put("avatar", new DataPart("file_avatar.jpg",
                        getFileDataFromDrawable(getBaseContext(),
                                b), "image/jpeg"));

                return params;
            }

            private final String boundary = "apiclient-" + System.currentTimeMillis();

            @Override
            public String getBodyContentType() {
                return "multipart/form-data;boundary=" + boundary;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue = Volley.newRequestQueue(UploadPic.this);
        requestQueue.add(stringRequest);


    }

    public String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    public void rotateImage() throws Exception {
        ExifInterface ei = new ExifInterface(pathFile);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();

        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;

            default:
        }
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);

    }


    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    public Bitmap invert(Bitmap original) {
        // Create mutable Bitmap to invert, argument true makes it mutable
        Bitmap inversion = original.copy(Bitmap.Config.ARGB_8888, true);

        // Get info about Bitmap
        int width = inversion.getWidth();
        int height = inversion.getHeight();
        int pixels = width * height;

        // Get original pixels
        int[] pixel = new int[pixels];
        inversion.getPixels(pixel, 0, width, 0, 0, width, height);

        // Modify pixels
        for (int i = 0; i < pixels; i++)
            pixel[i] ^= RGB_MASK;
        inversion.setPixels(pixel, 0, width, 0, 0, width, height);

        // Return inverted Bitmap
        return inversion;
    }
}
