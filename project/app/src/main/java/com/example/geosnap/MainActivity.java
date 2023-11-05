package com.example.geosnap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.geosnap.databases.DatabaseAdapter;
import com.example.geosnap.databases.DatabaseData;
import com.example.geosnap.databases.UploadActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    ArrayList<DatabaseData> dataList;
    DatabaseAdapter adapter;
    private final DatabaseReference dbRef= FirebaseDatabase
            .getInstance().getReference("pictures");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        fab= findViewById(R.id.fab);
        recyclerView= findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataList= new ArrayList<>();
        adapter= new DatabaseAdapter(dataList, this);
        recyclerView.setAdapter(adapter);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot shot: snapshot.getChildren()){
                    DatabaseData data= shot.getValue(DatabaseData.class);
                    dataList.add(data);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}