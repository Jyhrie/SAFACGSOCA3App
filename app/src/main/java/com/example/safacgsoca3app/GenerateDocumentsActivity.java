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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


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



public class GenerateDocumentsActivity extends AppCompatActivity {

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
    private static final String TAG_RETURN_DATETIME = "td_issuedatetime";
    private static final String TAG_ISSUE_SIGNATURE = "td_issuesignaturerecieving";
    private static final String TAG_RETURN_SIGNATURE = "td_returnsignaturerecieving";

    private static final String TAG_D_NAME = "d_name";
    private static final String TAG_O_NAME = "o_name";
    private static final String TAG_O_UNIT = "o_unit";

    private static final String TAG_DOC_NUMBER = "doc_id";

    // Define some String variables, initialized with empty string
    String filepath = Environment.getExternalStorageDirectory().getPath() + "/Download/Test.pdf";
    String fileContent = "ASDAD";
    private File file = new File(filepath);

    private static final String TAG = GenerateDocumentsActivity.class.getSimpleName();

    // variables for our buttons.

    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;


    ArrayList<ContentValues> data;
    ArrayList<HashMap<String,String>> dispData;
    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    String doc_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_documents);

        Intent i = getIntent();
        doc_id = i.getStringExtra(TAG_DOC_NUMBER);
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
        c1 = db.rawQuery("SELECT td_a_name, td_p_name, td_issued, td_returned, td_expended, td_spoiled, td_issuedatetime, td_issuesignature ,td_returndatetime, td_returnsignature from transaction_data where doc_id = ?", new String[]{doc_id});

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

            HashMap<String,String>map = new HashMap<>();
            map.put(TAG_AMMO_NAME, line_ammo_name);
            map.put(TAG_PERSONNEL_NAME, line_personnel_name);
            map.put(TAG_TD_ISSUED, td_issued);
            map.put(TAG_TD_RETURNED, td_returned);
            map.put(TAG_TD_EXPENDED, td_expended);
            map.put(TAG_TD_SPOILED, td_spoiled);

            Log.i(line_data.toString(), "test");
            dispData.add(map);
            data.add(line_data);
        }


        ListView lv_gen_doc = findViewById(R.id.lv_generate_documents);
        SimpleAdapter lvAdapter = new SimpleAdapter(
                this, //context
                dispData, //hashmapdata
                R.layout.list_generate_document_check, //layout of list
                new String[] {TAG_AMMO_NAME, TAG_PERSONNEL_NAME, TAG_TD_ISSUED, TAG_TD_RETURNED,TAG_TD_EXPENDED}, //from array
                new int[] {R.id.tv_gen_doc_ammo, R.id.tv_gen_doc_personnel, R.id.tv_gen_doc_issued, R.id.tv_gen_doc_returned, R.id.tv_gen_doc_expended});  //toarray

        lv_gen_doc.setAdapter(lvAdapter);

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
            OutputStream outputStream = new FileOutputStream(file);

            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            pdfDocument.setDefaultPageSize(PageSize.A4);
            document.setMargins(0,0,0,0);
            Paragraph _documentTitle = new Paragraph("SAF 1386: Records of Ammunition Distribution to Individiual").setBold().setFontSize(12).setTextAlignment(TextAlignment.LEFT);
            document.add(_documentTitle);

            Paragraph _details = new Paragraph("UNIT/ECHELON: " + doc_data.get(TAG_O_UNIT) + "             DETAIL: " + doc_data.get(TAG_D_NAME) + "                    DATE: TBD").setFontSize(12).setTextAlignment(TextAlignment.LEFT);
            document.add(_details);

            document.add(createTable(data));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress((Bitmap.CompressFormat.PNG), 100, stream);
            byte[] bitmapData = stream.toByteArray();
            Image img = new Image(ImageDataFactory.create(bitmapData));
            img.setHeight(img.getImageHeight()/7);
            img.setWidth(img.getImageWidth()/7);
            document.add(img).setHorizontalAlignment(HorizontalAlignment.LEFT).setLeftMargin(5);

            Paragraph _endorsed = new Paragraph("Endorsed By: Name");
            _endorsed.setMargin(10);
            document.add(_endorsed);

            db = openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
            Log.i("closing doc", doc_id);
            db.execSQL("UPDATE document SET doc_closed = true WHERE doc_id = ?", new String[]{doc_id});
            db.close();
            document.close();
        }
        catch(Exception e)
        {

        }

        finish();
        /*try {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream(file));
            doc.open();
            addMetaData(doc);
            addContent(doc, data, doc_data);

            doc.close();
            //set doc closed = true
        } catch (Exception e) {
            e.printStackTrace();
        }*/

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

    /*
    private static void addMetaData(Document document) {
        document.addTitle("SAF 1386");
        document.addSubject("GENERATED USING A3 APP");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("GSAB/PYAD");
        document.addCreator("SAFAC");
    }*/

    /*
    private static void addTitlePage(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Title of the document", catFont));

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph(
                "Report generated by: " + System.getProperty("user.name") + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                smallBold));
        addEmptyLine(preface, 3);
        preface.add(new Paragraph(
                "This document describes something which is very important ",
                smallBold));

        addEmptyLine(preface, 8);

        preface.add(new Paragraph(
                "This document is a preliminary version and not subject to your license agreement or any other agreement with vogella.com ;-).",
                redFont));

        document.add(preface);
        // Start a new page
        document.newPage();
    }*/


    /*private static void addContent(Document document, ArrayList<ContentValues> data, HashMap<String,String> doc_data) throws DocumentException, IOException {


        Paragraph _documentTitle = new Paragraph("SAF 1386: Records of Ammunition Distribution to Individiual");
        /*Anchor anchor = new Anchor("SAF 1386: Records of Ammunition Distribution to Individiual", catFont);
        anchor.setName("SAF 1386");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph details = new Paragraph("LESSON/EXERCISE: " + doc_data.get(TAG_O_NAME), subFont);
        Section subCatPart = catPart.addSection(details);
        subCatPart.add(new Paragraph("UNIT/ECHELON: " + doc_data.get(TAG_O_UNIT) + "             DETAIL: " + doc_data.get(TAG_D_NAME) + "                    DATE: TBD"));

        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph,2);
        subCatPart.add(paragraph);
        /*Paragraph subPara = new Paragraph("Subcategory 1", subFont);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Hello"));

        // add a table
        createTable(subCatPart, data);



        addEmptyLine(paragraph, 5);

        Paragraph sign_off_details = new Paragraph("Endorsed By: ");
        Section sign_off = catPart.addSection(sign_off_details);

        // now add all this to the document
        document.add(catPart);*/



        /*
        // Next section
        anchor = new Anchor("Second Chapter", catFont);
        anchor.setName("Second Chapter");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 1);

        subPara = new Paragraph("Subcategory", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("This is a very important message"));

        // now add all this to the document
        document.add(catPart);
        }*/



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

        for(ContentValues entry : data)
        {

            Image _issuesignature = new Image(ImageDataFactory.create((byte[]) entry.get(TAG_ISSUE_SIGNATURE)));
            Image _returnsignature = new Image(ImageDataFactory.create((byte[]) entry.get(TAG_RETURN_SIGNATURE)));

            //S/N
            table.addCell(new Cell().add(new Paragraph(String.valueOf(sn))));
            //ammo desc
            table.addCell(new Cell().add(new Paragraph(String.valueOf(entry.get(TAG_AMMO_NAME)))));
            //rank & name
            table.addCell(new Cell().add(new Paragraph(String.valueOf(entry.get(TAG_PERSONNEL_NAME)))));
            //issued qty
            table.addCell(new Cell().add(new Paragraph(String.valueOf(entry.get(TAG_TD_ISSUED)))));
            //issue date
            table.addCell(new Cell().add(new Paragraph(String.valueOf(entry.get(TAG_ISSUE_DATETIME)))));
            //signature
            table.addCell(new Cell().add(_issuesignature.setAutoScale(true)));
            //expended qty
            table.addCell(new Cell().add(new Paragraph(String.valueOf(entry.get(TAG_TD_EXPENDED)))));
            //returned qty
            table.addCell(new Cell().add(new Paragraph(String.valueOf(entry.get(TAG_TD_RETURNED)))));
            //return date
            table.addCell(new Cell().add(new Paragraph(String.valueOf(entry.get(TAG_RETURN_DATETIME)))));
            //signature
            table.addCell(new Cell().add(_returnsignature.setAutoScale(true)));

            sn++;

        }
        return table;
    }

    /*
    private static void createList(Section subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }*/




    /*
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }


    }*/
}

