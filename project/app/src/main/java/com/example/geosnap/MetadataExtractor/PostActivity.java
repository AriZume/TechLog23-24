package com.example.geosnap.MetadataExtractor;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.foundation.gestures.TapGestureDetectorKt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.GestureDetector;
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

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {

    Uri imageUri;
    EditText etDescription;
    Button cancelBtn, uploadBtn, tagBtn, btnSelectImages;
    Bitmap imageBitmap;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private final int REQUEST_PERMISSION_CODE=35;
    private ArrayList<Uri> imagesUri;
    private ViewPager uploadImg;
    ImageMetadataUtil imageMetadataUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        uploadImg = findViewById(R.id.uploadImage);
        btnSelectImages = findViewById(R.id.btnSelectImages);
        imagesUri= new ArrayList<>();
        imageMetadataUtil= new ImageMetadataUtil();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            if (data.getClipData() != null) {
                                // Get image from gallery
                                int count = data.getClipData().getItemCount();

                                for (int i = 0; i < count; i++) {
                                    Uri uri = data.getClipData().getItemAt(i).getUri();
                                    imagesUri.add(uri);
                                    imageMetadataUtil.extractMetadata(PostActivity.this, uri, getContentResolver());
                                }
                            } else if (data.hasExtra("data")) {
                                // Capture picture from camera
                                imageBitmap = (Bitmap) data.getExtras().get("data");
                                imageUri = imageMetadataUtil.getImageUri(PostActivity.this, imageBitmap);
                                imagesUri.add(imageUri);
                                imageMetadataUtil.extractMetadata(PostActivity.this, imageUri, getContentResolver());
                            } else if (data.getData() != null) {
                                // Handle the case where a single image is selected from the gallery
                                imagesUri.add(data.getData());
                                imageMetadataUtil.extractMetadata(PostActivity.this, data.getData(), getContentResolver());
                            }

                            mySetAdapter();
                        } else {
                            Toast.makeText(PostActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


        /*uploadImg.setOnClickListener(view -> {
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
        */

        etDescription = findViewById(R.id.etDescription);
        cancelBtn = findViewById(R.id.cancelButton);
        uploadBtn = findViewById(R.id.uploadButton);
        tagBtn = findViewById(R.id.addTagBtn);

        btnSelectImages.setOnClickListener(view -> {
                checkUserPermission();
                // Create an intent to show the dialog to pick an image
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                // Create an Intent to capture a photo from the camera
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //gia polles fwtografies apo thn kamera:
                //Intent cameraIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
                //alla prepei na oristei to limit mesa sto activity result

                // Create a chooser dialog for the user to pick between gallery and camera
                Intent chooserIntent = Intent.createChooser(photoPickerIntent, "Select Images");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});
                // Launch the chooser dialog
                activityResultLauncher.launch(chooserIntent);
                checkForCamPermission();
        });

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

    private void mySetAdapter(){
        //edw mpainoyn oi fwto (imagesUri) sto viewpager
        ImagesAdapter imagesAdapter= new ImagesAdapter(this, imagesUri);
        uploadImg.setAdapter(imagesAdapter);
    }

    public void saveData() {
        for (int i=0; i<imagesUri.size(); i++) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images")
                    .child(imagesUri.get(i).getLastPathSegment());

            AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            storageReference.putFile(imagesUri.get(i)).addOnSuccessListener(taskSnapshot -> {
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

        DatabaseData dataClass = new DatabaseData(imageMetadataUtil.getLatitude(),
                imageMetadataUtil.getLongitude(),
                imageMetadataUtil.getImgSize(),
                imageMetadataUtil.getImgHeight(),
                imageMetadataUtil.getImgWidth(),
                imageURL, imageMetadataUtil.getDateTime(), tag, description);

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

        /*if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
         */
    }

    public boolean checkForCamPermission(){
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
             requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
             return true;
        }
        return false;
    }

    private void checkUserPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE);
        }
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

                    if (radioButton != null) {
                        Toast.makeText(PostActivity.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
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