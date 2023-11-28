package com.example.geosnap.MetadataExtractor;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.geosnap.MainActivity;
import com.example.geosnap.R;
import com.example.geosnap.databases.DatabaseData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostActivity extends AppCompatActivity {

    Uri imageUri;
    EditText etDescription;
    Button cancelBtn, uploadBtn, tagBtn;
    ImageMetadataUtil imageMetadataUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ImageView uploadImg = findViewById(R.id.uploadImage);
        imageMetadataUtil= new ImageMetadataUtil();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null ) {
                            //Get image from gallery
                            imageUri = data.getData();
                            imageMetadataUtil.extractMetadata(PostActivity.this, imageUri,getContentResolver());
                            if (imageMetadataUtil.metadataValidator() ){
                                //MetaData Validation
                                Toast.makeText(PostActivity.this, "Please enable 'location tags' on camera settings", Toast.LENGTH_LONG).show();
                            }
                            else{uploadImg.setImageURI(imageUri);}
                        }
                    }
                    else {
                        Toast.makeText(PostActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        uploadImg.setOnClickListener(view -> {
            // Create an intent to pick an image
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            activityResultLauncher.launch(photoPickerIntent);
        });

        etDescription = findViewById(R.id.etDescription);
        cancelBtn = findViewById(R.id.cancelButton);
        uploadBtn = findViewById(R.id.uploadButton);
        tagBtn = findViewById(R.id.addTagBtn);

        getSupportActionBar().setTitle("Upload...");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF252526")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void saveData() {
        if ( imageMetadataUtil.metadataValidator() ){
            //MetaData Validation
            Toast.makeText(PostActivity.this, "You need to select and image first", Toast.LENGTH_SHORT).show();
        }
        else {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images")
                    .child(imageUri.getLastPathSegment());
            AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                String imageURL = urlImage.toString();
                uploadData(imageURL);
                dialog.dismiss();
            }).addOnFailureListener(e -> {
                Toast.makeText(PostActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        }
    }
    public void uploadData(String imageURL) {
        String tag = tagBtn.getText().toString();
        if(tag.equals("+ add a tag"))
            tag = "no tag";
        String description = etDescription.getText().toString();

        DatabaseData dataClass = new DatabaseData(imageMetadataUtil.getLatitude(), imageMetadataUtil.getLongitude(), imageMetadataUtil.getImgSize(), imageMetadataUtil.getImgHeight(), imageMetadataUtil.getImgWidth(), imageURL, imageMetadataUtil.getDateTime(), tag, description);

        FirebaseDatabase.getInstance().getReference("info").child(imageMetadataUtil.getDateTime())
                .setValue(dataClass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(PostActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PostActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(e -> Toast.makeText(PostActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show());
    }

    public void saveOnClick(View view){
        saveData();
    }

    public void cancelOnClick(View view){
        finish();
    }

}