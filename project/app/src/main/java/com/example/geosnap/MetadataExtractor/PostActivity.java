package com.example.geosnap.MetadataExtractor;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.geosnap.MainActivity;
import com.example.geosnap.R;
import com.example.geosnap.databases.DatabaseData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;

public class PostActivity extends AppCompatActivity {

    Uri imageUri;
    EditText etDescription;
    Button cancelBtn, uploadBtn, tagBtn;
    Bitmap imageBitmap;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
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
                        if (data != null && data.getData() != null) {
                            //Get image from gallery
                            imageUri = data.getData();
                            uploadImg.setImageURI(imageUri);
                            imageMetadataUtil.extractMetadata(PostActivity.this, imageUri,getContentResolver());
                        }
                        if (data.hasExtra("data")) {

                                //Capture picture from camera
                                imageBitmap = (Bitmap) data.getExtras().get("data");
                                imageUri = imageMetadataUtil.getImageUri(PostActivity.this, imageBitmap);
                                uploadImg.setImageURI(imageUri);
                            imageMetadataUtil.extractMetadata(PostActivity.this, imageUri,getContentResolver());
                        }
                    }
                    else {
                        Toast.makeText(PostActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        uploadImg.setOnClickListener(view -> {
            // Create an intent to show the dialog to pick an image
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            // Create an Intent to capture a photo from the camera
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Create a chooser dialog for the user to pick between gallery and camera
            Intent chooserIntent = Intent.createChooser(photoPickerIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});
            // Launch the chooser dialog
            activityResultLauncher.launch(chooserIntent);
            checkForCamPermission();
        });

        etDescription = findViewById(R.id.etDescription);
        cancelBtn = findViewById(R.id.cancelButton);
        uploadBtn = findViewById(R.id.uploadButton);
        tagBtn = findViewById(R.id.addTagBtn);

        tagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheet();
            }
        });

        getSupportActionBar().setTitle("Upload...");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF252526")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void saveData() {
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean checkForCamPermission(){
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
             requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
             return true;
        }
        return false;
    }

    private void openBottomSheet() {
        // Find the dimming view in the activity_post layout
        View dimmingView = findViewById(R.id.dimmingView);

        // Set the dimming view to visible
        if (dimmingView != null) {
            dimmingView.setVisibility(View.VISIBLE);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#47252526")));
        }

        // Create and show the Bottom Sheet Dialog
        final Dialog bottomSheetDialog = new Dialog(PostActivity.this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);
        bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

        // Add dismiss listener to handle the dimming reset when the BottomSheet is dismissed
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Set the dimming view back to gone
                if (dimmingView != null) {
                    dimmingView.setVisibility(View.GONE);
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF252526")));
                }
            }
        });

        // Initialize the RadioGroup in the Bottom Sheet Dialog
        RadioGroup bottomSheetRadioGroup = bottomSheetDialog.findViewById(R.id.radioGroup);

        // Set a listener on the RadioGroup in the Bottom Sheet Dialog
        bottomSheetRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Handle the checked change event for the RadioGroup in the Bottom Sheet
            }
        });

        // Find the "Save Tag" button inside the Bottom Sheet Dialog
        Button bottomSheetSaveTagBtn = bottomSheetDialog.findViewById(R.id.saveTagBtn);

        if (bottomSheetSaveTagBtn != null) {
            bottomSheetSaveTagBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle the click event for the "Save Tag" button inside the Bottom Sheet
                    int selectedId = bottomSheetRadioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = bottomSheetDialog.findViewById(selectedId);

                    // Get the background drawable of the selected radio button
                    Drawable backgroundDrawable = radioButton.getBackground();

                    String selectedTag = radioButton.getText().toString();
                    Toast.makeText(PostActivity.this, radioButton.getText(), Toast.LENGTH_SHORT).show();

                    if (tagBtn != null) {
                        // Set the background drawable for addTagBtn
                        tagBtn.setBackground(backgroundDrawable);
                        tagBtn.setText(selectedTag);
                    }

                    bottomSheetDialog.dismiss();  // Dismiss the Bottom Sheet Dialog

                    // Set the dimming view back to gone
                    if (dimmingView != null) {
                        dimmingView.setVisibility(View.GONE);
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF252526")));
                    }
                }
            });
        }
        // Show the Bottom Sheet Dialog
        bottomSheetDialog.show();
    }

}