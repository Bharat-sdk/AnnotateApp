package com.sippitechnologes.annotateapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
Button getAnnotate,getCancel;
ImageButton annotate,show_cropping_area,red_box,blue_box,green_box,yellow_box,red_txt,blue_txt,green_txt,yellow_txt;
ImageButton save,refresh;
ImageView imageView;
TextView label;
AlertDialog alertDialog;
IconCropView iconCropView;
Uri gallery_image;

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
       String name= i.getStringExtra("label");
        Log.d("TAG", "onCreate: "+name);

       if (name.equals("labelfromgallery")) {
           gallery_image = i.getParcelableExtra("path");
           imageView.setImageURI(gallery_image);
       }




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
                iconCropView.setVisibility(View.INVISIBLE);
                imageView.setImageBitmap(bm);

            }


        }
    }

    public void createTextView(String notation)
    {
     iconCropView.setText(notation);
     iconCropView.invalidate();
    }
}