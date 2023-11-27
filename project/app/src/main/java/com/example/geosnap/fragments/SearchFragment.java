package com.example.geosnap.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.geosnap.R;
import com.example.geosnap.databases.DatabaseData;
import com.example.geosnap.databases.MyAdapter;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ArrayList<DatabaseData> dataList;
    private MyAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        //initializeRecyclerView();
        initializeSearchView();
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.searchRecyclerView);
        searchView = view.findViewById(R.id.searchView);
        dataList = new ArrayList<>();
    }

    private void initializeRecyclerView() {
      //  adapter = new MyAdapter(requireContext(), dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
       // recyclerView.setAdapter(adapter);
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
      // adapter.searchDataList(searchList);
    }
}
