package com.example.pdfviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PdfViewActivity extends AppCompatActivity {
    PDFView pdfView;
    TextView pdfName;
    ImageView sharePdfButton;
    String filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        initViews();

        sharePdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                File fileWithinMyDir = new File(filePath);

                if(fileWithinMyDir.exists()) {
                    intentShareFile.setType("application/pdf");
                    intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+filePath));
                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,"Sharing File...");
                    intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                    startActivity(Intent.createChooser(intentShareFile, "Share File"));
                }
            }
        });
    }

    private void initViews() {
        pdfView=findViewById(R.id.pdfView);
        sharePdfButton=findViewById(R.id.sharePdfButton);
        pdfName=findViewById(R.id.pdfViewNameTextView);
        pdfView.fromFile((File) getIntent().getExtras().get("pdf")).load();
        pdfName.setText(getIntent().getStringExtra("name"));
        filePath=getIntent().getStringExtra("path");
    }
}