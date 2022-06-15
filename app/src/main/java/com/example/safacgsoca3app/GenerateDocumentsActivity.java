package com.example.safacgsoca3app;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;


import androidx.annotation.NonNull;

import androidx.core.content.ContextCompat;


import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class GenerateDocumentsActivity extends AppCompatActivity {

    // Define some String variables, initialized with empty string
    String filepath = Environment.getExternalStorageDirectory().getPath() + "/Download/Test.pdf";
    String fileContent = "ASDAD";
    private File file = new File(filepath);

    // variables for our buttons.

    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;

    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_documents);

        Button btnGenerate = findViewById(R.id.btnGenerate);

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE,},PackageManager.PERMISSION_GRANTED);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCreatePDF(view);
            }
        });

    }
    public void btnCreatePDF(View view){
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Paint paint = new Paint();
        String content = "HEHEXD\nPEEPEEPOOPOO";
        int x = 10, y = 25;

        for(String line: content.split("\n"))
        {
            page.getCanvas().drawText(line, x,y,paint);

            y+=paint.descent()-paint.ascent();
        }
        pdfDocument.finishPage(page);

        try{
            pdfDocument.writeTo(new FileOutputStream(file));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        pdfDocument.close();


    }

}