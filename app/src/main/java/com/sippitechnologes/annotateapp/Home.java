package com.sippitechnologes.annotateapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;


public class Home extends AppCompatActivity implements View.OnClickListener {

    public static int REQUEST_IMAGE_CAPTURE = 300;
    ImageButton btn_camera, btn_gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        btn_camera = findViewById(R.id.camera);
        btn_gallery = findViewById(R.id.gallery);
        btn_camera.setOnClickListener(this);
        btn_gallery.setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK)
        {
            Uri uri = data.getData();
            Intent camera_intent = new Intent(getApplicationContext(), MainActivity.class);
            camera_intent.putExtra("image_uri", uri);
            startActivity(camera_intent);

        }
        else if(resultCode==ImagePicker.RESULT_ERROR)
        {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera:
                ImagePicker.with(this)
                        .cameraOnly()
                        .start();

                break;
            case R.id.gallery:
                ImagePicker.with(this)
                        .galleryOnly()
                        .start();
                break;
        }
    }
}

