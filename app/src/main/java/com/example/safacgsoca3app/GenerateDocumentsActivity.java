package com.example.safacgsoca3app;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

/*import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;*/


import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;



public class GenerateDocumentsActivity extends AppCompatActivity implements RecyclerViewInterface{

    /*private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private static Font tableHeader = new Font(Font.FontFamily.TIMES_ROMAN, 6,
            Font.NORMAL);
    private static Font tableContent = new Font(Font.FontFamily.TIMES_ROMAN, 4,
            Font.NORMAL);*/

    private static final String TAG_AMMO_NAME = "ammo_name";
    private static final String TAG_PERSONNEL_NAME = "personnel_name";
    private static final String TAG_TD_ISSUED = "td_issued";
    private static final String TAG_TD_RETURNED = "td_returned";
    private static final String TAG_TD_EXPENDED = "td_expended";
    private static final String TAG_TD_SPOILED = "td_spoiled";
    private static final String TAG_ISSUE_DATETIME = "td_issuedatetime";
    private static final String TAG_RETURN_DATETIME = "td_returndatetime";
    private static final String TAG_ISSUE_SIGNATURE = "td_issuesignaturerecieving";
    private static final String TAG_RETURN_SIGNATURE = "td_returnsignaturerecieving";
    private static final String TAG_TD_ID = "td_id";

    private static final String TAG_D_NAME = "d_name";
    private static final String TAG_O_NAME = "o_name";
    private static final String TAG_O_UNIT = "o_unit";
    private static final String TAG_PAST_DOC = "past_doc";

    private static final String TAG_DOC_NUMBER = "doc_id";

    // Define some String variables, initialized with empty string
    String filepath;
    private File file;

    private static final String TAG = GenerateDocumentsActivity.class.getSimpleName();

    // variables for our buttons.

    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;

    adapter_generate_documents rvAdapter;

    ArrayList<ContentValues> data;
    ArrayList<HashMap<String,String>> dispData;
    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;
    private String close;

    String doc_id;
    String regenerated;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        close = "1";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_documents);

        Intent i = getIntent();
        doc_id = i.getStringExtra(TAG_DOC_NUMBER);
        String pastdoc = i.getStringExtra(TAG_PAST_DOC);
        Log.i(pastdoc, "dock");
        Log.i("doc id", doc_id);
        Button btnGenerate = findViewById(R.id.btnGenerate);
        SignaturePad signaturePad = findViewById(R.id.gen_docSignature_Pad);
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE,}, PackageManager.PERMISSION_GRANTED);

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
        HashMap<String,String> doc_data = new HashMap<>();
        Cursor c1;
        c1 = db.rawQuery("SELECT d_name, o_name, o_unit FROM document WHERE doc_id = ?", new String[]{String.valueOf(doc_id)});
        if(c1.moveToFirst())
        {
            doc_data.put(TAG_D_NAME, c1.getString(0));
            doc_data.put(TAG_O_NAME, c1.getString(1));
            doc_data.put(TAG_O_UNIT, c1.getString(2));
        }

        data = new ArrayList<ContentValues>();
        dispData = new ArrayList<HashMap<String,String>>();

        //get db data
        String td_exported_query = "0";
        if(pastdoc.equals("1"))
        {
            c1 = db.rawQuery("SELECT td_a_name, td_p_name, td_issued, td_returned, td_expended, td_spoiled, td_issuedatetime, td_issuesignature ,td_returndatetime, td_returnsignature, td_id from transaction_data where doc_id = ?", new String[]{doc_id});

        }
        else {
            c1 = db.rawQuery("SELECT td_a_name, td_p_name, td_issued, td_returned, td_expended, td_spoiled, td_issuedatetime, td_issuesignature ,td_returndatetime, td_returnsignature, td_id from transaction_data where doc_id = ? and td_exported = ?", new String[]{doc_id, td_exported_query});
        }
        ByteArrayInputStream imageStream;

        while(c1.moveToNext())
        {

            ContentValues line_data = new ContentValues();
            String line_ammo_name = c1.getString(0);
            String line_personnel_name = c1.getString(1);
            String td_issued = c1.getString(2);
            String td_returned = c1.getString(3);
            String td_expended = c1.getString(4);
            String td_spoiled = c1.getString(5);
            String td_issuedatetime = c1.getString(6);
            byte[] td_issuesignatureblob = c1.getBlob(7);
            String td_returndatetime = c1.getString(8);
            byte[] td_returnsignatureblob = c1.getBlob(9);
            String td_id = c1.getString(10);


            Log.i("Information | issuedatetime", td_issuedatetime);
            line_data.put(TAG_AMMO_NAME, line_ammo_name);
            line_data.put(TAG_PERSONNEL_NAME, line_personnel_name);
            line_data.put(TAG_TD_ISSUED, td_issued);
            line_data.put(TAG_TD_RETURNED, td_returned);
            line_data.put(TAG_TD_EXPENDED, td_expended);
            line_data.put(TAG_TD_SPOILED, td_spoiled);
            line_data.put(TAG_ISSUE_DATETIME, td_issuedatetime);
            line_data.put(TAG_ISSUE_SIGNATURE, td_issuesignatureblob);
            line_data.put(TAG_RETURN_DATETIME, td_returndatetime);
            line_data.put(TAG_RETURN_SIGNATURE, td_returnsignatureblob);
            line_data.put(TAG_TD_ID, td_id);

            HashMap<String,String>map = new HashMap<>();
            map.put(TAG_TD_ID, td_id);
            map.put(TAG_AMMO_NAME, line_ammo_name);
            map.put(TAG_PERSONNEL_NAME, line_personnel_name);
            map.put(TAG_TD_ISSUED, td_issued);
            map.put(TAG_TD_RETURNED, td_returned);
            map.put(TAG_TD_EXPENDED, td_expended);
            map.put(TAG_TD_SPOILED, td_spoiled);

            Log.i("Information | Line Data", String.valueOf(line_data));
            Log.i(line_data.toString(), "test");
            dispData.add(map);
            data.add(line_data);
        }


        RecyclerView rv_gen_doc = findViewById(R.id.lv_generate_documents);
        rvAdapter = new adapter_generate_documents(
                this,
                dispData,
                this
        );
        rv_gen_doc.setAdapter(rvAdapter);
        rv_gen_doc.setLayoutManager(new LinearLayoutManager(this));


        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCreatePDF(view, doc_id);
            }
        });

    }

    public void btnCreatePDF(View view, String doc_id) {

        SignaturePad Signature_Pad = findViewById(R.id.gen_docSignature_Pad);
        Bitmap bitmap = Signature_Pad.getSignatureBitmap();

        boolean _dataValidationPass = true;
        String _errorMessage = "there is no empty field, something went wrong";
        if(Signature_Pad.isEmpty())
        {
            _dataValidationPass = false;
            _errorMessage = "Please sign on the signature pad before continuing";
        }

        if(_dataValidationPass == false)
        {
            showErrorAlertDialog(view, _errorMessage);
            return;
        }
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
        HashMap<String,String> doc_data = new HashMap<>();
        Cursor c1;
        c1 = db.rawQuery("SELECT d_name, o_name, o_unit FROM document WHERE doc_id = ?", new String[]{doc_id});
        if(c1.moveToFirst())
        {
            doc_data.put(TAG_D_NAME, c1.getString(0));
            doc_data.put(TAG_O_NAME, c1.getString(1));
            doc_data.put(TAG_O_UNIT, c1.getString(2));
        }
        db.close();

        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault());
            SimpleDateFormat dateFileFormat = new SimpleDateFormat("ddMMYYYYHHmmssSSS", Locale.getDefault());

            String currentDateTime = dateFormat.format(new Date());
            String currentDateFile = dateFileFormat.format(new Date());
            String fileName = "SAF1386" + (doc_data.get(TAG_D_NAME)).replaceAll("[^\\p{L}\\d\\s_\\n]", "") +  doc_data.get(TAG_O_NAME).replaceAll("[^\\p{L}\\d\\s_\\n]", "") +  doc_data.get(TAG_O_UNIT).replaceAll("[^\\p{L}\\d\\s_\\n]", "") +currentDateFile +  ".pdf";

            filepath = Environment.getExternalStorageDirectory().getPath() + "/Download/" + fileName;
            file = new File(filepath);
            OutputStream outputStream = new FileOutputStream(file);

            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);


            pdfDocument.setDefaultPageSize(PageSize.A4);
            document.setMargins(5,0,0,0);
            Paragraph _documentTitle = new Paragraph("SAF 1386: Records of Ammunition Distribution to Individiual").setBold().setFontSize(12).setTextAlignment(TextAlignment.LEFT);
            document.add(_documentTitle);


            Paragraph _details = new Paragraph("UNIT/ECHELON: " + doc_data.get(TAG_O_UNIT) + "             DETAIL: " + doc_data.get(TAG_D_NAME) + "                    DATE: " + currentDateTime).setFontSize(12).setTextAlignment(TextAlignment.LEFT);
            document.add(_details);

            Log.i("Before Table", "yes");
            document.add(createTable(data));
            Log.i("After Table", "yes");

            Paragraph _certify = new Paragraph("Certified Correct By:");
            _certify.setMargin(10);
            document.add(_certify);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress((Bitmap.CompressFormat.PNG), 100, stream);
            byte[] bitmapData = stream.toByteArray();
            Image img = new Image(ImageDataFactory.create(bitmapData));
            img.setHeight(img.getImageHeight()/7);
            img.setWidth(img.getImageWidth()/7);
            document.add(img).setHorizontalAlignment(HorizontalAlignment.LEFT).setLeftMargin(5);


            EditText et_endorsedBy = findViewById(R.id.et_endorsedByName);


            Paragraph _endorsed = new Paragraph("Rank & Name \n" +et_endorsedBy.getText().toString());
            _endorsed.setMargin(10);
            document.add(_endorsed);

            document.close();
            showSuccessAlertDialog("Success! Data saved at: " + filepath);
            //show popup
        }
        catch(Exception e)
        {
            Log.i("execption", e.toString());
            showErrorAlertDialog(view, e.toString());
        }


        db = openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
        //look through data check if doc can close
        boolean checkclose = true;
        for(HashMap<String,String> map : dispData) {
            Log.i("Information", map.toString());
            if(map.get(TAG_TD_ISSUED) == null || map.get(TAG_TD_RETURNED) == null || map.get(TAG_TD_EXPENDED) == null || map.get(TAG_TD_SPOILED) == null)
            {
                checkclose = false;
            }
        }
        if(checkclose)
        {
            for(ContentValues line : data)
            {
                String td_id = (String) line.get(TAG_TD_ID);
                db.execSQL("UPDATE transaction_data SET td_exported = ? WHERE td_id = ?", new String[]{"1", td_id});
            }
            Log.i("closing doc", doc_id);
            db.execSQL("UPDATE document SET doc_closed = ? WHERE doc_id = ?", new String[]{String.valueOf(close), doc_id});
            db.close();
        }

    }

    public void showErrorAlertDialog(View v, String message)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Error");
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                dialogInterface.dismiss();
            }});
        alert.show();
    }

    private Table createTable(ArrayList<ContentValues> data) {
        Table table = new Table(10);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.setPadding(10);
        table.setMaxWidth(PageSize.A4.getWidth());
        table.setMaxHeight(PageSize.A4.getHeight());
        table.setFontSize(8);
        //SAF 1836 format
        Cell c1;
        c1 = new Cell().add(new Paragraph("S/N").setHorizontalAlignment(HorizontalAlignment.LEFT));
        table.addHeaderCell(c1);

        c1 = new Cell().add(new Paragraph("AMMUNITION DESCRIPTION").setHorizontalAlignment(HorizontalAlignment.LEFT));
        table.addHeaderCell(c1);

        c1 = new Cell().add(new Paragraph("RANK & NAME").setHorizontalAlignment(HorizontalAlignment.LEFT));
        table.addHeaderCell(c1);

        c1 = new Cell().add(new Paragraph("ISSUED QTY").setHorizontalAlignment(HorizontalAlignment.LEFT));
        table.addHeaderCell(c1);

        c1 = new Cell().add(new Paragraph("ISSUE DATE").setHorizontalAlignment(HorizontalAlignment.LEFT));
        table.addHeaderCell(c1);

        c1 = new Cell().add(new Paragraph("SIGNATURE").setHorizontalAlignment(HorizontalAlignment.LEFT));
        table.addHeaderCell(c1);

        c1 = new Cell().add(new Paragraph("EXPENDED QTY").setHorizontalAlignment(HorizontalAlignment.LEFT));
        table.addHeaderCell(c1);

        c1 = new Cell().add(new Paragraph("RETURNED QTY").setHorizontalAlignment(HorizontalAlignment.LEFT));
        table.addHeaderCell(c1);

        c1 = new Cell().add(new Paragraph("RETURN DATE").setHorizontalAlignment(HorizontalAlignment.LEFT));
        table.addHeaderCell(c1);

        c1 = new Cell().add(new Paragraph("SIGNATURE").setHorizontalAlignment(HorizontalAlignment.LEFT));
        table.addHeaderCell(c1);


        int sn = 0;
        Log.i(String.valueOf(data.size()), "why");
        for(ContentValues entry : data)
        {
            Log.i("Information", "Enter For Loop");

            Image _issuesignature = null;
            if(entry.containsKey(TAG_ISSUE_SIGNATURE)){
                Log.i("Information", "Image 1");
                if(entry.get(TAG_ISSUE_SIGNATURE) != null) {
                    _issuesignature = new Image(ImageDataFactory.create((byte[]) entry.get(TAG_ISSUE_SIGNATURE)));
                }
            }

            Image _returnsignature = null;
            if(entry.containsKey(TAG_RETURN_SIGNATURE)) {
                Log.i("Information", "Image 2");
                if(entry.get(TAG_RETURN_SIGNATURE) != null) {
                    _returnsignature = new Image(ImageDataFactory.create((byte[]) entry.get(TAG_RETURN_SIGNATURE)));
                }
            }

            Log.i("Information", "Past Image Processing");
            //S/N
            table.addCell(new Cell().add(new Paragraph(String.valueOf(sn))));
            //ammo desc
            if(!String.valueOf(entry.get(TAG_AMMO_NAME)).equals("null")) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(entry.get(TAG_AMMO_NAME)))));
            }else{
                table.addCell(new Cell());
            }
            //rank & name
            if(!String.valueOf(entry.get(TAG_PERSONNEL_NAME)).equals("null")) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(entry.get(TAG_PERSONNEL_NAME)))));
            }else{
                table.addCell(new Cell());
            }
            //issued qty
            if(!String.valueOf(entry.get(TAG_TD_ISSUED)).equals("null")) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(entry.get(TAG_TD_ISSUED)))));
            }else{
                table.addCell(new Cell());
            }
            //issue date
            Log.i("Information", String.valueOf(entry.get(TAG_ISSUE_DATETIME)));
            if(!String.valueOf(entry.get(TAG_ISSUE_DATETIME)).equals("null")) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(entry.get(TAG_ISSUE_DATETIME)))));
            }else{
                table.addCell(new Cell());
            }
            //signature
            if(_issuesignature != null) {
                table.addCell(new Cell().add(_issuesignature.setAutoScale(true)));
            }
            else{
                table.addCell(new Cell());
            }
            //expended qty
            if(!String.valueOf(entry.get(TAG_TD_EXPENDED)).equals("null")) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(entry.get(TAG_TD_EXPENDED)))));
            }else{
                table.addCell(new Cell());
            }
            //returned qty
            if(!String.valueOf(entry.get(TAG_TD_RETURNED)).equals("null")) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(entry.get(TAG_TD_RETURNED)))));
            }else{
                table.addCell(new Cell());
            }
            //return date
            if(!String.valueOf(entry.get(TAG_RETURN_DATETIME)).equals("null")) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(entry.get(TAG_RETURN_DATETIME)))));
            }else{
                table.addCell(new Cell());
            }
            //signature
            if(_returnsignature != null) {
                table.addCell(new Cell().add(_returnsignature.setAutoScale(true)));
            }
            else{
                table.addCell(new Cell());
            }
            sn++;

        }
        Log.i("Information","CREATE TABLE END");
        return table;

    }

    public void showSuccessAlertDialog(String message)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                finish();
            }});
        alert.show();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLongItemClick(int position) {
        showErrorAlertDialog("Do not authorize this entry?", position);
    }

    public void showErrorAlertDialog(String message, int position)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                close = "0";
                dispData.remove(position);
                data.remove(position);
                rvAdapter.notifyItemRemoved(position);
            }});
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }
}

