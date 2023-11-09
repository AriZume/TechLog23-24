package com.example.geosnap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.geosnap.databases.DatabaseData;

import com.example.geosnap.databases.UploadActivity;

import com.example.geosnap.databases.MyAdapter;

import com.example.geosnap.databases.UploadActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;

    ArrayList<DatabaseData> dataList;
    MyAdapter adapter;
    SearchView searchView;

    private final DatabaseReference dbRef = FirebaseDatabase
            .getInstance().getReference("pictures");

    List<DatabaseData> dataList;
    DatabaseReference dbRef;
    ValueEventListener valueEventListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);



        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataList = new ArrayList<>();
        adapter = new MyAdapter(dataList, this);
        fab= findViewById(R.id.fab);
        recyclerView= findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager= new GridLayoutManager(MainActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog= builder.create();
        dialog.show();

        dataList= new ArrayList<>();
        MyAdapter adapter= new MyAdapter(MainActivity.this, dataList);

        recyclerView.setAdapter(adapter);
        dbRef= FirebaseDatabase.getInstance().getReference("info");
        dialog.show();
        valueEventListener= dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot shot : snapshot.getChildren()) {
                    DatabaseData data = shot.getValue(DatabaseData.class);
                    dataList.add(data);

                dataList.clear();
                for (DataSnapshot shot: snapshot.getChildren()){
                    DatabaseData dataClass= shot.getValue(DatabaseData.class);
                    dataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });
    }
    public void searchList(String text){
        ArrayList<DatabaseData> searchList = new ArrayList<>();
        for (DatabaseData dataClass: dataList ){
            if (dataClass.getDate().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }

}
}
