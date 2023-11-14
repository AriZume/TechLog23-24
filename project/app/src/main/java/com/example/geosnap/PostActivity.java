package com.example.geosnap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.widget.Button;

import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;


public class PostActivity extends AppCompatActivity {

    private Button addTagBtn;
   // private RadioGroup radioGroup;
    //private RadioButton radioButton;
   // private Button saveTag;
    //Button bottomSheet;
    private static final String PREF_SELECTED_OPTION = "selected_option";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        getSupportActionBar().setTitle("Upload...");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF252526")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*bottomSheet = findViewById(R.id.addTagBtn);

        bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {showDialog();}
        }); */

        addTagBtn = findViewById(R.id.addTagBtn);

        addTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheet();
            }
        });

    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void openBottomSheet() {
        // Create and show the Bottom Sheet Dialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PostActivity.this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);

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
                }
            });
        }

        View bottomSheetView = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
       if (bottomSheetView != null) {
           bottomSheetView.setBackgroundColor(Color.TRANSPARENT);

        }

        // Show the Bottom Sheet Dialog
        bottomSheetDialog.show();

    }

}