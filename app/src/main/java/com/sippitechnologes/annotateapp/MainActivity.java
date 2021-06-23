package com.sippitechnologes.annotateapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button getAnnotate,getCancel,getSave;
    ImageButton annotate,show_cropping_area,red_box,blue_box,green_box,yellow_box,red_txt,blue_txt,green_txt,yellow_txt;
    ImageButton save,reset;
    ImageView imageView;
    EditText label,folderName;
    AlertDialog annotationAlertDialog,folderAlertDialog;
    com.sippitechnologes.annotateapp.IconCropView iconCropView1;
    Uri uri_image;
    FileOutputStream outputStream;
    String path;
     StorageReference mStorageRef;
ConstraintLayout view;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mStorageRef = FirebaseStorage.getInstance().getReference();

        annotationAlertDialog = createAnnotateDialog();
        folderAlertDialog = createFolderDialog();

        annotate = findViewById(R.id.annotatebtn);
        show_cropping_area = findViewById(R.id.show_cropping_area);
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
        reset = findViewById(R.id.reset);
        //imageview
        imageView = findViewById(R.id.crop_image);

         view = findViewById(R.id.pic_taker);

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
        reset.setOnClickListener(this::onClick);

        //Intent getting the image and setting it from camera or gallery
        Intent i = getIntent();
        uri_image=i.getParcelableExtra("image_uri");
        imageView.setImageURI(uri_image);

    }


    AlertDialog createAnnotateDialog()
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
    AlertDialog createFolderDialog()
    {
        View view = LayoutInflater.from(this).inflate(R.layout.save_to_folder_dialog,null, false);
        getSave = view.findViewById(R.id.btn_save);
        getCancel = view.findViewById(R.id.btn_cancel);
        folderName = view.findViewById(R.id.folder_name);
        getSave.setOnClickListener(this);
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
                annotationAlertDialog.dismiss();
                createTextView(label.getText().toString());

            }
            break;
            case  R.id.btn_cancel:
            {
                annotationAlertDialog.dismiss();
            }
            break;
            case R.id.show_cropping_area:
            {
                iconCropView1 = (IconCropView) View.inflate(this, R.layout.rectangular_crop_voew, null);
                view.addView(iconCropView1);

            }
            break;
            case R.id.annotatebtn:
            {
                annotationAlertDialog.show();
            }
            break;
            case R.id.red_box:
            {
                iconCropView1.changeEdgeColor(getResources().getColor(R.color.red));
                iconCropView1.invalidate();

            }
            break;
            case R.id.blue_box:
            {
                iconCropView1.changeEdgeColor(getResources().getColor(R.color.blue));
                iconCropView1.invalidate();

            }
            break;
            case R.id.green_box:
            {
                iconCropView1.changeEdgeColor(getResources().getColor(R.color.green));
                iconCropView1.invalidate();
            }
            break;
            case R.id.yellow_box:
            {
                iconCropView1.changeEdgeColor(getResources().getColor(R.color.yellow));
                iconCropView1.invalidate();

            }
            break;
            case R.id.red_color_txt:
            {
                iconCropView1.changetextColor(getResources().getColor(R.color.red));
                iconCropView1.invalidate();

            }
            break;
            case R.id.blue_color_txt:
            {
                iconCropView1.changetextColor(getResources().getColor(R.color.blue));
                iconCropView1.invalidate();

            }
            break;
            case R.id.green_color_txt:
            {
                iconCropView1.changetextColor(getResources().getColor(R.color.green));
                iconCropView1.invalidate();
            }
            break;
            case R.id.yellow_color_txt:
            {
                iconCropView1.changetextColor(getResources().getColor(R.color.yellow));
                iconCropView1.invalidate();
            }
            break;
            case R.id.save:
            {
                folderAlertDialog.show();
            }
            break;
            case R.id.reset:
            {
               // view.removeView(iconCropView1);
                view.removeViewInLayout(iconCropView1);
            }
            break;
            case R.id.btn_save:
            {

                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                Bitmap bm = view.getDrawingCache();
                String folderNameString = folderName.getText().toString();
                folderAlertDialog.dismiss();
                if (folderNameString.isEmpty())
                {

                    Toast.makeText(getApplicationContext(),"Enter The Folder Name",Toast.LENGTH_SHORT).show();
                }
                else {

                    folderName.setText("");
                    saveImageInFirebase(bm, folderNameString);
                    createDirectoryAndSaveFile(bm,folderNameString);


                }
            }


        }
    }

    public void createTextView(String notation)
    {
        iconCropView1.setText(notation);
        iconCropView1.invalidate();
    }

    public void  saveImageInFirebase(Bitmap bitmapImage , String fileName)
    {

       final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image....");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] data = baos.toByteArray();
        StorageReference mountainImagesRef = mStorageRef.child("ImageAnnotation/"+fileName+"/"+fileName+System.currentTimeMillis()+".jpg");
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(),"Upload Failed Please Retry",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_SHORT).show();
                Intent intentt = new Intent(getApplicationContext(),Home.class);
                startActivity(intentt);
            }
        })
          .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
              @Override
              public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                   pd.setMessage(" On Progress....... ");
              }
          });
    }
    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {
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