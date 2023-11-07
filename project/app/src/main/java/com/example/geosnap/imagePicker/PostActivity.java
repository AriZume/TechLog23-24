package com.example.geosnap.imagePicker;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaMetadataRetriever;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.geosnap.R;

import java.io.IOException;

public class PostActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ImageView selectPhoto = findViewById(R.id.selectPhoto);

        selectPhoto.setOnClickListener(v -> openGallery());
        getSupportActionBar().setTitle("Upload...");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            if (selectedImage != null) {
                // Handle the selected image
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();

                try {
                    retriever.setDataSource(this, selectedImage);

                    String location = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION);
                    String date = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
                    String author = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);

                    Log.d("Metadata", "Location: " + location);
                    Log.d("Metadata", "Date: " + date);
                    Log.d("Metadata", "Author: " + author);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Metadata", "Error extracting metadata: " + e.getMessage());
                } finally {
                    try {
                        retriever.release();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                Log.e("Metadata", "Selected image URI is null.");
            }
        }
    }
}