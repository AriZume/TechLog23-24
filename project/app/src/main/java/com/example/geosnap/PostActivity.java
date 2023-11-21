package com.example.geosnap;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        getSupportActionBar().setTitle("Upload...");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF252526")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}