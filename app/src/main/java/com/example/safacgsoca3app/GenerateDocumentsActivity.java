package com.example.safacgsoca3app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;

import com.itextpdf.text.Anchor;
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
import com.itextpdf.text.pdf.PdfWriter;


import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;



public class GenerateDocumentsActivity extends AppCompatActivity {

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
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
            Font.NORMAL);

    private static final String TAG_AMMO_NAME = "ammo_name";
    private static final String TAG_PERSONNEL_NAME = "personnel_name";
    private static final String TAG_TD_ISSUED = "td_issued";
    private static final String TAG_TD_RETURNED = "td_returned";
    private static final String TAG_TD_EXPENDED = "td_expended";
    private static final String TAG_TD_SPOILED = "td_spoiled";
    private static final String TAG_ISSUE_DATETIME = "td_issuedatetime";
    private static final String TAG_RETURN_DATETIME = "td_issuedatetime";
    private static final String TAG_ISSUE_SIGNATURE_REC = "td_issuesignaturerecieving";
    private static final String TAG_RETURN_SIGNATURE_REC = "td_returnsignaturerecieving";


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
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS transaction_dataTest (td_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, td_ammo_name text NOT NULL, td_personnel_name NOT NULL, td_issued number, td_returned number, td_expended number, td_spoiled number, td_issuedatetime text, td_issuesignature image, td_returndatetime text, td_returnsignature image)");

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE,}, PackageManager.PERMISSION_GRANTED);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCreatePDF(view);
            }
        });

    }

    public void btnCreatePDF(View view) {
        try {

            ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
            //get db data
            SQLiteDatabase db;
            db = openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
            Cursor c1 = db.rawQuery("SELECT td_ammo_name, td_personnel_name, td_issued, td_returned, td_expended, td_spoiled, td_issuedatetime ,td_returndatetime from transaction_dataTest", null);

            ByteArrayInputStream imageStream;

            while(c1.moveToNext())
            {
                HashMap<String, String> line_data = new HashMap<>();
                String line_ammo_name = c1.getString(0);
                String line_personnel_name = c1.getString(1);
                String td_issued = c1.getString(2);
                String td_returned = c1.getString(3);
                String td_expended = c1.getString(4);
                String td_spoiled = c1.getString(5);
                String td_issuedatetime = c1.getString(6);
                Log.i("stringy", td_issuedatetime);

                /*Bitmap td_issuesignaturerecieving = null;
                byte[] td_issuesignaturerecievingblob = c1.getBlob(7);
                if(td_issuesignaturerecievingblob.equals(null)) {
                    imageStream = new ByteArrayInputStream(td_issuesignaturerecievingblob);
                    td_issuesignaturerecieving = BitmapFactory.decodeStream(imageStream);
                }*/

                String td_returndatetime = c1.getString(7);

                /*Bitmap td_returnsignaturerecieving = null;
                byte[] td_returnsignaturerecievingblob = c1.getBlob(9);
                if(!td_returnsignaturerecievingblob.equals(null)) {
                    imageStream = new ByteArrayInputStream(td_issuesignaturerecievingblob);
                    td_returnsignaturerecieving = BitmapFactory.decodeStream(imageStream);
                }*/

                line_data.put(TAG_AMMO_NAME, line_ammo_name);
                line_data.put(TAG_PERSONNEL_NAME, line_personnel_name);
                line_data.put(TAG_TD_ISSUED, td_issued);
                line_data.put(TAG_TD_RETURNED, td_returned);
                line_data.put(TAG_TD_EXPENDED, td_expended);
                line_data.put(TAG_TD_SPOILED, td_spoiled);
                line_data.put(TAG_ISSUE_DATETIME, td_issuedatetime);
                line_data.put(TAG_RETURN_DATETIME, td_returndatetime);

                Log.i(line_data.toString(), "test");
                data.add(line_data);
            }

            //pdfDocument.writeTo(new FileOutputStream(file));
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream(file));
            doc.open();
            addMetaData(doc);
            //addTitlePage(doc);
            addContent(doc, data);
            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addMetaData(Document document) {
        document.addTitle("My first PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }


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
    }


    private static void addContent(Document document, ArrayList<HashMap<String, String>> data) throws DocumentException {
        Anchor anchor = new Anchor("SAF 1386: Records of Ammunition Distribution to Individiual", catFont);
        anchor.setName("SAF 1386");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph details = new Paragraph("LESSON/EXERCISE: TBD", subFont);
        Section subCatPart = catPart.addSection(details);
        subCatPart.add(new Paragraph("UNIT/ECHELON: TBD             DETAIL: TBD                 DATE: TBD"));

        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph,2);
        subCatPart.add(paragraph);
        /*Paragraph subPara = new Paragraph("Subcategory 1", subFont);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Hello"));*/


        // add a list
        //createList(subCatPart);
        //Paragraph paragraph = new Paragraph();
        //addEmptyLine(paragraph, 5);
        //subCatPart.add(paragraph);

        // add a table
        createTable(subCatPart, data);

        // now add all this to the document
        document.add(catPart);

        // Next section
        anchor = new Anchor("Second Chapter", catFont);
        anchor.setName("Second Chapter");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 1);

        /*subPara = new Paragraph("Subcategory", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("This is a very important message"));*/

        // now add all this to the document
        document.add(catPart);

    }

    private static void createTable(Section subCatPart, ArrayList<HashMap<String, String>> data)
            throws DocumentException {
        PdfPTable table = new PdfPTable(10);
        table.setWidthPercentage(100);
        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);
        float[] columnWidths = new float[]{5f, 30f,15f, 12f, 15f, 25f, 12f, 12f, 15f, 25f};
        table.setWidths(columnWidths);

        //SAF 1836 format
        PdfPCell c1;
        c1 = new PdfPCell(new Phrase("S/N", tableHeader));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("AMMUNITION DESCRIPTION", tableHeader));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("RANK & NAME", tableHeader));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("ISSUED QTY", tableHeader));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("ISSUE DATE", tableHeader));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("SIGNATURE", tableHeader));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("EXPENDED QTY", tableHeader));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("RETURNED QTY", tableHeader));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("RETURN DATE", tableHeader));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("SIGNATURE", tableHeader));
        c1.setColspan(2);
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);
        table.setHeaderRows(1);




        int sn = 0;



        for(HashMap<String, String> entry : data)
        {

            //S/N
            table.addCell(new PdfPCell(new Phrase(String.valueOf(sn), tableContent)));
            //ammo desc
            table.addCell(new PdfPCell(new Phrase(entry.get(TAG_AMMO_NAME), tableContent)));
            //rank & name
            table.addCell(new PdfPCell(new Phrase(entry.get(TAG_PERSONNEL_NAME), tableContent)));
            //issued qty
            table.addCell(new PdfPCell(new Phrase(entry.get(TAG_TD_ISSUED), tableContent)));
            //issue date
            table.addCell(new PdfPCell(new Phrase(entry.get(TAG_ISSUE_DATETIME), tableContent)));
            //signature
            table.addCell((Image) null);
            //expended qty
            table.addCell(new PdfPCell(new Phrase(entry.get(TAG_TD_EXPENDED), tableContent)));
            //returned qty
            table.addCell(new PdfPCell(new Phrase(entry.get(TAG_TD_RETURNED), tableContent)));
            //return date
            table.addCell(new PdfPCell(new Phrase(entry.get(TAG_RETURN_DATETIME), tableContent)));
            //signature
            table.addCell((Image) null);

            sn++;

        }


        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");

        subCatPart.add(table);

    }

    private static void createList(Section subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }


    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }


    }
}

