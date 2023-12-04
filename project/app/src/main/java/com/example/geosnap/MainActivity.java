package com.example.geosnap;

import com.example.geosnap.databinding.ActivityMainBinding;
import com.example.geosnap.fragments.MapFragment;
import com.example.geosnap.fragments.SearchFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.content.Intent;

import com.example.geosnap.Metadata.PostActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Fragment fragment = new MapFragment();
        replaceFragment(fragment);
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.frame_layout,fragment)
                .commit();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF252526")));

        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.home){
                replaceFragment(new MapFragment());
            } else if (id == R.id.search) {
                replaceFragment(new SearchFragment());
            }
            return false;
        });

        FloatingActionButton postFab = findViewById(R.id.postFab);
        postFab.setOnClickListener(v -> openPostActivity());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void openPostActivity(){
        Intent intent = new Intent(MainActivity.this, PostActivity.class);
        startActivity(intent);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }



}