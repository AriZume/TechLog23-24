package com.example.geosnap.databases;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.geosnap.MainActivity;
import com.example.geosnap.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PicturesDB {

    private EditText authorText, dateText, locationText, nameText, tagText, urlText, descText;
    private Button searchBtn;
    private DatabaseReference dbRef;
    private String author, date, location, name, tag, url, description;
    private MainActivity mainActivity;
    public PicturesDB(String author, String date, String location, String name, String tag, String url, String description) {
        this.author = author;
        this.date = date;
        this.location = location;
        this.name = name;
        this.tag = tag;
        this.url = url;
        this.description=description;
    }

    public void initPic(MainActivity mainActivity){
        this.authorText=mainActivity.findViewById(R.id.authorText);
        this.dateText=mainActivity.findViewById(R.id.dateText);
        this.locationText=mainActivity.findViewById(R.id.locationText);
        this.nameText=mainActivity.findViewById(R.id.nameText);
        this.tagText=mainActivity.findViewById(R.id.tagText);
        this.urlText=mainActivity.findViewById(R.id.urlText);
        this.descText=mainActivity.findViewById(R.id.descText);
        this.searchBtn=mainActivity.findViewById(R.id.searchBtn);
        this.dbRef= FirebaseDatabase.getInstance().getReference();
        this.author= authorText.getText().toString();
        this.date= dateText.getText().toString();
        this.location= locationText.getText().toString();
        this.name= nameText.getText().toString();
        this.tag= tagText.getText().toString();
        this.url= urlText.getText().toString();
        this.description= descText.getText().toString();
    }


    public void searchOnClick(View v){
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot shot: snapshot.getChildren()) {
                    //author= shot.child("author").getValue().toString();
                    //date= shot.child("date").getValue().toString();
                    //location= shot.child("location").getValue().toString();
                    //name= shot.child("name").getValue().toString();
                    //tag= shot.child("tag").getValue().toString();
                    //url= shot.child("url").getValue().toString();
                    //description= shot.child("description").getValue().toString();
                    PicturesDB post= shot.child(shot.getKey()).getValue(PicturesDB.class);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });
    }
    public String getAuthor() {
        return this.author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getLocation() {
        return this.location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTag() {
        return this.tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
