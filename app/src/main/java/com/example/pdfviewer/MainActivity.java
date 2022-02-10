package com.example.pdfviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton createPdfFile;
    PdfAdapter pdfAdapter;
    private static final int PERMISSON_READ_STORAGE=123;
    public static final int PERMISSION_WRITE_STORAGE=111;
    String[] manifest={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedEditor;
    ArrayList<File> fileList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setPdfFilesInGridRecyclerView();
        createPdfFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CreatePdfFileActivity.class));
            }
        });
    }

    private void initViews()
    {
        createPdfFile=findViewById(R.id.createPdfFile);
        sharedPreferences=getSharedPreferences("MyData",MODE_PRIVATE);
        sharedEditor=sharedPreferences.edit();
    }

    private ArrayList<File> getPdfFiles(File file){
        ArrayList<File> arrayList=new ArrayList<>();
        File[] pdfFiles=file.listFiles();
        if(pdfFiles!=null){
            for (File singleFile : pdfFiles){
                if(singleFile.isDirectory() && !singleFile.isHidden()){
                    arrayList.addAll(getPdfFiles(singleFile));
                }else{
                    if(singleFile.getName().endsWith(".pdf")){
                        arrayList.add(singleFile);
                    }
                }
            }
        }
        return arrayList;
    }

    private void setPdfFilesInGridRecyclerView()
    {
        String layout="grid";
        recyclerView=findViewById(R.id.pdfRecyclerView);;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        fileList=new ArrayList<>();
        fileList.addAll(getPdfFiles(Environment.getExternalStorageDirectory()));
        pdfAdapter=new PdfAdapter(MainActivity.this,fileList,layout);
        recyclerView.setAdapter(pdfAdapter);
    }

    private void setPdfFilesInLinearRecyclerView()
    {
        String layout="linear";
        recyclerView=findViewById(R.id.pdfRecyclerView);;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fileList=new ArrayList<>();
        fileList.addAll(getPdfFiles(Environment.getExternalStorageDirectory()));
        pdfAdapter=new PdfAdapter(MainActivity.this,fileList,layout);
        recyclerView.setAdapter(pdfAdapter);
    }

    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,manifest, MainActivity.PERMISSON_READ_STORAGE);
        }else if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,manifest, MainActivity.PERMISSION_WRITE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSON_READ_STORAGE && grantResults[0]==PackageManager.PERMISSION_DENIED
             || requestCode==PERMISSION_WRITE_STORAGE && grantResults[0]==PackageManager.PERMISSION_DENIED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
       checkPermission();
       String layout=sharedPreferences.getString("layout","grid");
       if(layout.equalsIgnoreCase("grid")){
           setPdfFilesInGridRecyclerView();
       }else if (layout.equalsIgnoreCase("linear")){
           setPdfFilesInLinearRecyclerView();
       }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pdf_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.gridView){
            setPdfFilesInGridRecyclerView();
            sharedEditor.putString("edit","yes");
            sharedEditor.putString("layout","grid");
            sharedEditor.apply();
            return true;
        }else if(item.getItemId()==R.id.linearView){
            setPdfFilesInLinearRecyclerView();
            sharedEditor.putString("edit","yes");
            sharedEditor.putString("layout","linear");
            sharedEditor.apply();
            return true;
        }
        return false;
    }
}