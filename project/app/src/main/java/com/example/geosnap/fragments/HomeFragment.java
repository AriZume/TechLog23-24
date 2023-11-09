package com.example.geosnap.fragments;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.geosnap.MainActivity;
import com.example.geosnap.Manifest;
import com.example.geosnap.R;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class HomeFragment extends Fragment {

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE=101;



        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Initialize view
        View view=inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize map fragment
        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);







        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // When map is loaded

                UserLocation userobj = new UserLocation();
                //Location loc = userobj.getLocation();
                userobj.getLastLocation();
                double latitude = userobj.getLat();//loc.getLatitude();
                double longitude = userobj.getLon();//loc.getLongitude();
                // Create a LatLng object with the initial position
                LatLng initialPosition = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());latitude,longitude);//41.0749, 23.5555);//latitude,longitude);
                // Initialize marker options
                MarkerOptions markerOptions = new MarkerOptions();
                // Set position of marker
                markerOptions.position(initialPosition);
                // Set title of marker
                markerOptions.title(initialPosition.latitude + " : " + initialPosition.longitude);
                // Add marker on map
                googleMap.addMarker(markerOptions);
                // Zoom to the initial marker position
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 10));




                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        // When clicked on map
                        // Initialize marker options
                        MarkerOptions markerOptions=new MarkerOptions();
                        // Set position of marker
                        markerOptions.position(latLng);
                        // Set title of marker
                        markerOptions.title(latLng.latitude+" : "+latLng.longitude);
                        // Remove all marker
                        googleMap.clear();
                        // Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                        // Add marker on map
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });
        // Return view
        return view;
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , REQUEST_CODE);
            return;
        }

        Task<Location> task= fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){

                    currentLocation= location;
                    Toast.makeText(getApplicationContext(),currentLocation.getLatitude(),Toast.LENGTH_LONG)
                            .show();
                    SupportMapFragment supportMapFragment=(SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }
}

