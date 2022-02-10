package com.example.pdfviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Objects;

public class CreatePdfFileActivity extends AppCompatActivity {
    ImageView backButton;
    EditText pdfTitle,pdfDescription;
    Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException ignored) {
        }
        setContentView(R.layout.activity_create_pdf_file);
        initViews();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pdfTitle.getText().toString().equals("")){
                    pdfTitle.setError("Enter the title...");
                }else if(pdfDescription.getText().toString().equals("")){
                    pdfDescription.setError("Give the short description about title..");
                }else{
                    savePdfFileInPhone(pdfTitle.getText().toString(),pdfDescription.getText().toString());
                }
            }
        });
    }

    private void savePdfFileInPhone(String pdf_Title, String pdf_Description) {
        Document document=new Document();
        String filePath= Environment.getExternalStorageDirectory()+"/"+"Pdf Viewer"+pdf_Title+".pdf";
        try {
            PdfWriter.getInstance(document,new FileOutputStream(filePath));
            document.open();
            document.add(new Paragraph(pdf_Description));
            document.addAuthor("By Pdf Viewer");
            document.close();
            Toast.makeText(getApplicationContext(),"File saved...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CreatePdfFileActivity.this,MainActivity.class));
            finish();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        backButton=findViewById(R.id.backButton);
        pdfDescription=findViewById(R.id.pdfDescription);
        pdfTitle=findViewById(R.id.pdfTitle);
        saveButton=findViewById(R.id.savePdfButton);
    }
}