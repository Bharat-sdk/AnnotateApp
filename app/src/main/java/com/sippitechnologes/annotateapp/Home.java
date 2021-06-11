package com.sippitechnologes.annotateapp;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Home extends AppCompatActivity implements View.OnClickListener {

    public static int REQUEST_IMAGE_CAPTURE=300;
    ImageButton btn_camera,btn_gallery;

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(this,"Permission Not Granted",Toast.LENGTH_LONG).show();
                }
            });

    ActivityResultLauncher galleryActivityLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            Intent galleryintent = new Intent(getApplicationContext(), MainActivity.class);
            galleryintent.putExtra("path",result);
            galleryintent.putExtra("label","labelfromgallery");
            startActivity(galleryintent);
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        btn_camera = findViewById(R.id.camera);
        btn_gallery = findViewById(R.id.gallery);
        btn_camera.setOnClickListener(this);
        btn_gallery.setOnClickListener(this);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
         /*   Intent intent = new Intent();
            intent.putExtra("camearImage",intent.getExtras());*/

            Bitmap imageBitmap = (Bitmap) extras.get("data");
          Intent camera_intent = new Intent(getApplicationContext(),MainActivity.class);
            camera_intent.putExtra("bundle",extras);
            camera_intent.putExtra("label","labelfromcamera");
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void  onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.camera:
                checkCameraPermission();

                break;
            case  R.id.gallery:
                galleryActivityLauncher.launch("image/*");
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    void askForPermission()
    {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA);
    }
    void showDialog(String title,String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(Home.this)
                .setTitle(title)
                .setMessage(message).setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        askForPermission();

                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();


                    }
                }).create();
        alertDialog.show();
    }





    @RequiresApi(api = Build.VERSION_CODES.M)
    void checkCameraPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {
            dispatchTakePictureIntent();
        }
        else
        {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                askForPermission();
            } else {

                showDialog("Camera Permission Required","App request this permission to Capture Images");
            }


        }
    }
}
