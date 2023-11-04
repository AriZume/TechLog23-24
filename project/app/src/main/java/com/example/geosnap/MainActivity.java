package com.example.geosnap;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import com.example.geosnap.databases.PicturesDB;

public class MainActivity extends AppCompatActivity {
    PicturesDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //if () {
         db.initPic(this);
        //}
    }
}