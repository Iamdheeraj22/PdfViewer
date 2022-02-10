package com.example.pdfviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import java.io.File;
import java.util.Objects;

public class StartActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException ignored) {
        }
        setContentView(R.layout.activity_start);
        sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        String edited = sharedPreferences.getString("edit", "no");
        if (edited.equalsIgnoreCase("no")) {
            sharedPreferencesEditor.putString("layout", "grid");
        }
        sharedPreferencesEditor.apply();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(StartActivity.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        }, 1500);
    }

    @Override
    protected void onStart() {
        super.onStart();
        File myDirectory = new File(Environment.getExternalStorageDirectory(), "/Pdf Viewer");
        myDirectory.mkdir();
    }
}