package com.example.geosnap.databases;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.geosnap.R;

import com.bumptech.glide.Glide;
import com.example.geosnap.MainActivity;
import com.example.geosnap.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


    public class DetailActivity extends AppCompatActivity {

//        TextView detailDesc, detailAuthor, detailDate, detailName;
//        ImageView detailImage;
//        FloatingActionButton deleteButton, editButton;
//        String key = "";
//        String imageUrl = "";
//
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_detail);
//
//            detailAuthor = findViewById(R.id.detailAuthor);
//            detailDate = findViewById(R.id.detailDate);
//            detailDesc = findViewById(R.id.detailDesc);
//            detailImage = findViewById(R.id.detailImage);
//            detailName = findViewById(R.id.detailName);
//            deleteButton = findViewById(R.id.deleteButton);
//            editButton = findViewById(R.id.editButton);
//
//            Bundle bundle = getIntent().getExtras();
//            if (bundle != null) {
//                detailDesc.setText(bundle.getString("Description"));
//                detailName.setText(bundle.getString("Name"));
//                detailAuthor.setText(bundle.getString("Author"));
//                detailDate.setText(bundle.getString("Date"));
//                key= bundle.getString("Key");
//                imageUrl= bundle.getString("Image");
//                Glide.with(this).load(bundle.getString("Image")).into(detailImage);
//            }
//            deleteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("info");
//                    FirebaseStorage storage = FirebaseStorage.getInstance();
//
//                    StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
//                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            reference.child(key).removeValue();
//                            Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                            finish();
//                        }
//                    });
//                }
//            });
//            editButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(DetailActivity.this, updateActivity.class)
//                            .putExtra("Name", detailName.getText().toString())
//                            .putExtra("Description", detailDesc.getText().toString())
//                            .putExtra("Date", detailDate.getText().toString())
//                            .putExtra("Author", detailAuthor.getText().toString())
//                            .putExtra("Image", imageUrl)
//                            .putExtra("Key", key);
//                    startActivity(intent);
//                }
//            });
//
//        }
    }

