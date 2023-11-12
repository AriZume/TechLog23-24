package com.example.geosnap.MetadataExtractor;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.GpsDirectory;
import com.example.geosnap.MainActivity;
import com.example.geosnap.R;
import com.example.geosnap.databases.DatabaseData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PostActivity extends AppCompatActivity {

    //METADATA
    double latitude, longitude, imgSize =0;
    String imgHeight, imgWidth, dateTime ="0";
    Uri imageUri;
    EditText etDescription;
    Button cancelBtn, uploadBtn, tagBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ImageView uploadImg = findViewById(R.id.uploadImage);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            imageUri = data.getData();
                            uploadImg.setImageURI(imageUri);
                            extractMetadata(PostActivity.this, imageUri);
                        } else {
                            Toast.makeText(PostActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        uploadImg.setOnClickListener(view -> {
            Intent photoPicker = new Intent(Intent.ACTION_PICK);
            photoPicker.setType("image/*");
            activityResultLauncher.launch(photoPicker);
        });

        etDescription = findViewById(R.id.etDescription);
        cancelBtn = findViewById(R.id.cancelButton);
        uploadBtn = findViewById(R.id.uploadButton);
        tagBtn = findViewById(R.id.addTagBtn);

        getSupportActionBar().setTitle("Upload...");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF252526")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void extractMetadata(Context context, Uri photoUri) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(photoUri);

            if (inputStream != null) {
                Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
                if (!containsMetadata(metadata)) {
                    throw new RuntimeException("Selected photo does not contain metadata. Please choose another photo.");
                }
                File file = new File(getRealPathFromURI(photoUri));
                imgSize = file.length();
                Log.d("PhotoMetadata", "File Size: " + imgSize + " bytes");
                for (Directory directory : metadata.getDirectories()) {
                    for (Tag tag : directory.getTags()) {
                        if (tag.getTagName().equals("Image Height")) {
                            imgHeight =tag.getDescription();
                        }else if (tag.getTagName().equals("Image Width")) {
                            imgWidth = tag.getDescription();
                        } else if (tag.getTagName().equals("Date/Time")) {
                            dateTime =tag.getDescription();
                        }
                    }
                    if (directory instanceof GpsDirectory) {
                        GpsDirectory gpsDirectory = (GpsDirectory) directory;
                        // Get GPS information
                        longitude = gpsDirectory.getGeoLocation().getLongitude();
                        latitude = gpsDirectory.getGeoLocation().getLatitude();
                        Log.d("Longitude", "extractMetadata: " + longitude);
                        Log.d("Latitude", "extractMetadata: " + latitude);
                    }
                }
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ImageProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private boolean containsMetadata(Metadata metadata) {
        if (metadata == null) {
            return false;
        }
        Iterable<Directory> directories = metadata.getDirectories();
        return directories != null && directories.iterator().hasNext();
    }

    public void saveData() {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images")
                .child(imageUri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                String imageURL = urlImage.toString();
                uploadData(imageURL);
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }
    public void uploadData(String imageURL) {
        String tag = tagBtn.getText().toString();
        if(tag.equals("+ add a tag"))
            tag = "no tag";
        String description = etDescription.getText().toString();

        DatabaseData dataClass = new DatabaseData(latitude, longitude, imgSize, imgHeight, imgWidth, imageURL, dateTime, tag, description);

        FirebaseDatabase.getInstance().getReference("info").child(dateTime)
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PostActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PostActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void saveOnClick(View view){
        saveData();
    }

    public void cancelOnClick(View view){
        finish();
    }
}