package com.sippitechnologes.annotateapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
Button getAnnotate,getCancel;
ImageButton annotate,show_cropping_area,red_box,blue_box,green_box,yellow_box,red_txt,blue_txt,green_txt,yellow_txt;
TextView label;
AlertDialog alertDialog;
IconCropView iconCropView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        alertDialog = createDialog();

        annotate = findViewById(R.id.annotatebtn);
        show_cropping_area = findViewById(R.id.show_cropping_area);
        iconCropView = findViewById(R.id.marker);
        red_box = findViewById(R.id.red_box);
        blue_box = findViewById(R.id.blue_box);
        green_box = findViewById(R.id.green_box);
        yellow_box = findViewById(R.id.yellow_box);

        annotate.setOnClickListener(this);
        show_cropping_area.setOnClickListener(this);
        red_box.setOnClickListener(this::onClick);
        green_box.setOnClickListener(this::onClick);
        yellow_box.setOnClickListener(this::onClick);
        blue_box.setOnClickListener(this::onClick);




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

        }
    }

    public void createTextView(String notation)
    {
     iconCropView.setText(notation);
     iconCropView.invalidate();
    }
}