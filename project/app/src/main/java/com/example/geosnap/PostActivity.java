package com.example.geosnap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        getSupportActionBar().setTitle("Upload...");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}