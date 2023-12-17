package com.example.geosnap.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;


import com.example.geosnap.R;
import com.example.geosnap.databases.DatabaseData;
import com.example.geosnap.databases.MyAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;


public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ArrayList<DatabaseData> dataList;
    private MyAdapter adapter;
    Context thiscontext;
    private String desc, tag, dateTimeKey;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        thiscontext = container.getContext();
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        initializeRecyclerView();
        initializeSearchView();
        retrieveData();
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.searchRecyclerView);
        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        dataList = new ArrayList<>();
    }

    private void initializeRecyclerView() {
        adapter = new MyAdapter(thiscontext, dataList);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(thiscontext, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initializeSearchView() {
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
    }

    private void searchList(String text) {
        ArrayList<DatabaseData> searchList = new ArrayList<>();
        for (DatabaseData dataClass : dataList) {
            if (dataClass.getTag().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }


    private void retrieveData() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("info");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                dateTimeKey = dataSnapshot.getKey(); //Gets the outer child key (DateTime)
                tag = dataSnapshot.child("tag").getValue().toString();
                desc = dataSnapshot.child("description").getValue().toString();
                ArrayList<String> imagesUrl = new ArrayList<>();
                for (DataSnapshot uniqueIdSnapshot : dataSnapshot.getChildren()) {
                    if (uniqueIdSnapshot.child("imageURL").exists()){
                        String imageUrl = uniqueIdSnapshot.child("imageURL").getValue().toString();
                        imagesUrl.add(imageUrl);
                    }
                }

                DatabaseData databaseData = new DatabaseData();
                databaseData.setDesc(desc);
                databaseData.setDateTime(dateTimeKey);
                databaseData.setTag(tag);
                databaseData.setImagesUrl(imagesUrl);
                dataList.add(databaseData);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
