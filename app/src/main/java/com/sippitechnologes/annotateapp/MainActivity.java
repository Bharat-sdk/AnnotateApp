package com.sippitechnologes.annotateapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button getAnnotate,getCancel;
    ImageButton annotate,show_cropping_area,red_box,blue_box,green_box,yellow_box,red_txt,blue_txt,green_txt,yellow_txt;
    ImageButton save,refresh;
    ImageView imageView;
    TextView label;
    AlertDialog alertDialog;
    IconCropView iconCropView;
    Uri uri_image;
    FileOutputStream outputStream;


    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public final int EXTERNAL_REQUEST = 138;

    public boolean requestForExternalStoragePermission() {

        boolean isPermissionOn = true;
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            if (!canAccessExternalSd()) {
                isPermissionOn = false;
                requestPermissions(EXTERNAL_PERMS, EXTERNAL_REQUEST);
            }
        }

        return isPermissionOn;
    }

    public boolean canAccessExternalSd() {
        return (hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        alertDialog = createDialog();

        annotate = findViewById(R.id.annotatebtn);
        show_cropping_area = findViewById(R.id.show_cropping_area);
        iconCropView = findViewById(R.id.marker);
        //color for the rectangle
        red_box = findViewById(R.id.red_box);
        blue_box = findViewById(R.id.blue_box);
        green_box = findViewById(R.id.green_box);
        yellow_box = findViewById(R.id.yellow_box);
        //color for the text
        red_txt = findViewById(R.id.red_color_txt);
        blue_txt= findViewById(R.id.blue_color_txt);
        green_txt = findViewById(R.id.green_color_txt);
        yellow_txt = findViewById(R.id.yellow_color_txt);
        //Image refresh and save
        save = findViewById(R.id.save);
        refresh = findViewById(R.id.refresh);
        //imageview
        imageView = findViewById(R.id.crop_image);

        //textbox

        annotate.setOnClickListener(this);
        show_cropping_area.setOnClickListener(this);
        red_box.setOnClickListener(this::onClick);
        green_box.setOnClickListener(this::onClick);
        yellow_box.setOnClickListener(this::onClick);
        blue_box.setOnClickListener(this::onClick);
        red_txt.setOnClickListener(this::onClick);
        green_txt.setOnClickListener(this::onClick);
        yellow_txt.setOnClickListener(this::onClick);
        blue_txt.setOnClickListener(this::onClick);
        save.setOnClickListener(this::onClick);
        refresh.setOnClickListener(this::onClick);

        //Intent getting the image and setting it from camera or gallery
        Intent i = getIntent();
        uri_image=i.getParcelableExtra("image_uri");
        imageView.setImageURI(uri_image);

    }


    AlertDialog createDialog()
    {

        View view = LayoutInflater.from(this).inflate(R.layout.annotate_dialog,null, false);
        getAnnotate = view.findViewById(R.id.btn_annotate);
        getCancel = view.findViewById(R.id.btn_cancel);
        label = view.findViewById(R.id.SwapName1);
        getAnnotate.setOnClickListener(this);
        getCancel.setOnClickListener(this);
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        return alertDialog;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_annotate:
            {
                alertDialog.dismiss();
                createTextView(label.getText().toString());

            }
            break;
            case  R.id.btn_cancel:
            {
                alertDialog.dismiss();
            }
            break;
            case R.id.show_cropping_area:
            {
                iconCropView.setVisibility(View.VISIBLE);
            }
            break;
            case R.id.annotatebtn:
            {
                alertDialog.show();
            }
            break;
            case R.id.red_box:
            {
                iconCropView.changeEdgeColor(getResources().getColor(R.color.red));
                iconCropView.invalidate();

            }
            break;
            case R.id.blue_box:
            {
                iconCropView.changeEdgeColor(getResources().getColor(R.color.blue));
                iconCropView.invalidate();

            }
            break;
            case R.id.green_box:
            {
                iconCropView.changeEdgeColor(getResources().getColor(R.color.green));
                iconCropView.invalidate();
            }
            break;
            case R.id.yellow_box:
            {
                iconCropView.changeEdgeColor(getResources().getColor(R.color.yellow));
                iconCropView.invalidate();
            }
            break;
            case R.id.red_color_txt:
            {
                iconCropView.changetextColor(getResources().getColor(R.color.red));
                iconCropView.invalidate();

            }
            break;
            case R.id.blue_color_txt:
            {
                iconCropView.changetextColor(getResources().getColor(R.color.blue));
                iconCropView.invalidate();

            }
            break;
            case R.id.green_color_txt:
            {
                iconCropView.changetextColor(getResources().getColor(R.color.green));
                iconCropView.invalidate();
            }
            break;
            case R.id.yellow_color_txt:
            {
                iconCropView.changetextColor(getResources().getColor(R.color.yellow));
                iconCropView.invalidate();
            }
            break;
            case R.id.save:
            {
                ConstraintLayout view = (ConstraintLayout) findViewById(R.id.pic_taker);
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                Bitmap bm = view.getDrawingCache();
               String name = iconCropView.getText();
             createDirectoryAndSaveFile(bm,name);

            }


        }
    }

    public void createTextView(String notation)
    {
        iconCropView.setText(notation);
        iconCropView.invalidate();
    }


    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {
        requestForExternalStoragePermission();
if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.R)
{
    Toast.makeText(getApplicationContext(),"Your are in Version R",Toast.LENGTH_SHORT).show();
    String path1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Image_Annotation_App";
    createFolder(path1);
 if (createFolder(path1).exists()) {
        File file = new File(path1+"/"+fileName);
        if (file.exists()) {
        } else {
            createFolder(path1+"/"+fileName);
        }
    }
    saveImage(imageToSave,fileName,path1+"/"+fileName);
}
else {
    String path = Environment.getExternalStorageDirectory() + "/Image_Annotation_App";
    createFolder(path);
    if (createFolder(path).exists()) {
        File file = new File(path+"/"+fileName);
            if (file.exists()) {
        } else {
                createFolder(path+"/"+fileName);
            }
        }
    saveImage(imageToSave,fileName,path+"/"+fileName);
    }
    }



    public File createFolder(String dirpath)
    {
        File dir = new File(dirpath);
        if(dir.isDirectory())
        {

        }
        else {
            dir.mkdirs();
        }
        return dir;
    }




    public void saveImage(Bitmap bms, String imagename, String path)
    {
       File file = new File(path,imagename+System.currentTimeMillis()+".png");
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bms.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        Toast.makeText(getApplicationContext(),"File Saved",Toast.LENGTH_SHORT).show();
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}