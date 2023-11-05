package com.example.geosnap.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.geosnap.R;

public class DetailActivity extends AppCompatActivity {

    TextView detailDesc, detailAuthor, detailDate, detailName;
    ImageView detailImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailDesc = findViewById(R.id.detailDesc);
        detailName = findViewById(R.id.detailName);
        detailAuthor = findViewById(R.id.detailAuthor);
        detailDate = findViewById(R.id.detailDate);
        detailImage = findViewById(R.id.detailImage);

        Bundle bundle= getIntent().getExtras();
        if (bundle!=null){
            detailDesc.setText(bundle.getString("Description"));
            detailName.setText(bundle.getString("Name"));
            detailAuthor.setText(bundle.getString("Author"));
            detailDate.setText(bundle.getString("Date"));
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }



    }
}