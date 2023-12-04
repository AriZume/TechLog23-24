package com.example.geosnap.MetadataExtractor;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.geosnap.MainActivity;
import com.example.geosnap.R;
import com.example.geosnap.databases.DatabaseData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {
    EditText etDescription;
    Button cancelBtn, uploadBtn, tagBtn, btnSelectImages;
    private ArrayList<Uri> imagesUri;
    private ViewPager uploadImg;
    ImageMetadataUtil imageMetadataUtil;
    private int count = 0;

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
                                    imageMetadataUtil.extractMetadata(PostActivity.this, uri, getContentResolver());
                                    if (imageMetadataUtil.metadataValidator() ){
                                        //MetaData Validation
                                        Toast.makeText(PostActivity.this, "Please enable 'location tags' on the camera settings", Toast.LENGTH_LONG).show();
                                    }else {
                                        imagesUri.add(uri);
                                    }
                                }
                            } else if (data.getData() != null) {
                                // Handle the case where a single image is selected from the gallery
                                imageMetadataUtil.extractMetadata(PostActivity.this, data.getData(), getContentResolver());
                                if (imageMetadataUtil.metadataValidator() ){
                                    //MetaData Validation
                                    Toast.makeText(PostActivity.this, "Please enable 'location tags' on the camera settings", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    imagesUri.add(data.getData());
                                }
                            }
                            mySetAdapter();
                        } else {
                            Toast.makeText(PostActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        etDescription = findViewById(R.id.etDescription);
        cancelBtn = findViewById(R.id.cancelButton);
        uploadBtn = findViewById(R.id.uploadButton);

        btnSelectImages.setOnClickListener(view -> {
                //checkUserPermission();
                // Create an intent to show the dialog to pick an image
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                // Create a chooser dialog for the user to pick between gallery and camera
                Intent chooserIntent = Intent.createChooser(photoPickerIntent, "Select Images");
                // Launch the chooser dialog
                activityResultLauncher.launch(chooserIntent);
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
            if (imageMetadataUtil.metadataValidator()) {
                //MetaData Validation
                Toast.makeText(PostActivity.this, "You need to select and image first", Toast.LENGTH_SHORT).show();
            } else {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                        .child("images").child(imagesUri.get(i).getLastPathSegment());

                AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                builder.setCancelable(false);
                builder.setView(R.layout.progress_layout);
                AlertDialog dialog = builder.create();
                dialog.show();

                storageReference.putFile(imagesUri.get(i)).addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    taskSnapshot.getMetadata();
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
    }

    public void uploadData(String imageURL) {
        String tag = tagBtn.getText().toString();
        if(tag.equals("+ add a tag"))
            tag = "none";
        String description = etDescription.getText().toString();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("info")
                .child(imageMetadataUtil.getDateTime());

        dbRef.child("tag").setValue(tag);
        dbRef.child("description").setValue(description);

        DatabaseData dataClass = new DatabaseData(imageMetadataUtil.getLatitude(),
                imageMetadataUtil.getLongitude(),
                imageMetadataUtil.getImgSize(),
                imageMetadataUtil.getImgHeight(),
                imageMetadataUtil.getImgWidth(),
                imageURL, imageMetadataUtil.getDateTime());

        dbRef.push().setValue(dataClass).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                count+=1;
                if (count==imagesUri.size()){
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

                    String selectedTag = radioButton.getText().toString();
                    Toast.makeText(PostActivity.this, radioButton.getText(), Toast.LENGTH_SHORT).show();

                    if (tagBtn != null) {
                        // Set the background drawable for addTagBtn
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