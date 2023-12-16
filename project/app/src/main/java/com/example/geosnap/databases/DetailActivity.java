package com.example.geosnap.databases;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.geosnap.R;

public class DetailActivity extends AppCompatActivity {

    TextView detailDesc,detailDate, detailTag;
    ImageView detailImage;
    String imageUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailDate = findViewById(R.id.detailDate);
        detailDesc = findViewById(R.id.detailDesc);
        detailImage = findViewById(R.id.detailImage);
        detailTag = findViewById(R.id.detailTag);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailDesc.setText(bundle.getString("Description"));
            detailTag.setText(bundle.getString("Tag"));
            detailDate.setText(bundle.getString("Date"));
            imageUrl= bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }
        getSupportActionBar().setTitle("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}


