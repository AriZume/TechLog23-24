package com.example.geosnap.databases;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.geosnap.Metadata.ImagesAdapter;
import com.example.geosnap.R;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    TextView detailDesc,detailDate, detailTag;
    ViewPager detailImage;
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

            //Glide.with(this).load(bundle.getString("Image")).into(detailImage);
            ArrayList<String> imagesUrl = bundle.getStringArrayList("Images");
            ArrayList<Uri> imagesUri = new ArrayList<>();

            if (imagesUrl != null) {
                for (String imageUrl : imagesUrl) {
                    imagesUri.add(Uri.parse(imageUrl));
                }
            }

            ImagesAdapter adapter = new ImagesAdapter(this, imagesUri);
            detailImage.setAdapter(adapter);
        }
        getSupportActionBar().setTitle("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}


