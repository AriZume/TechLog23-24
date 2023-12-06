package com.example.geosnap.fragments;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.geosnap.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback, OnLocationsLoadedListener {

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REFRESH_INTERVAL = 10000; // (10000 for 10 seconds)
    private Handler handler = new Handler();
    private Runnable refreshRunnable;
    private boolean isInitialLocationUpdate = true;
    private String dateTimeKey, tag;
    private LatLng loc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        createLocationCallback();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        updateLocationUI();
        startLocationUpdates();


        retrieveData(this);
        setLocationsOnMap();
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    updateMapLocation(location.getLatitude(), location.getLongitude());
                }
            }
        };
    }

    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000);

        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void updateMapLocation(double latitude, double longitude) {
        LatLng currentLocation = new LatLng(latitude, longitude);
        // Center the map on the user's location only during the initial update
        if (isInitialLocationUpdate) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            isInitialLocationUpdate = false;
        }

        // Place marker every reset.
        // googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
    }

    @Override
    public void onResume() {
        super.onResume();
        startAutoRefresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
        stopAutoRefresh();
    }

    private void startAutoRefresh() {
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                // Refresh the map by retrieving data from Firebase
                retrieveData(MapFragment.this);
                // Schedule the next refresh
                handler.postDelayed(this, REFRESH_INTERVAL);
            }
        };

        // Start the initial refresh
        handler.post(refreshRunnable);
    }

    private void stopAutoRefresh() {
        // Remove the refresh callback
        handler.removeCallbacks(refreshRunnable);
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    // Add onRequestPermissionsResult method to handle location permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLocationUI();
                startLocationUpdates();
            }
        }
    }

    @Override
    public void onLocationsLoaded(String datetime, LatLng location, String tag) {  //String URL gia tin photo
        // Call the method to set locations on the map
        setLocationsOnMap();
    }

    private void setLocationsOnMap() {
        if(loc != null){
            googleMap.addMarker(new MarkerOptions().position(loc).title(dateTimeKey).snippet(tag));
        }
        loc=null;
        tag="";
        dateTimeKey="";
    }



    // Display of an upload on the map
    private void retrieveData(OnLocationsLoadedListener listener) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("info");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                String dateTimeKey = dataSnapshot.getKey(); //Gets the outer child key (DateTime)
                tag = dataSnapshot.child("tag").getValue().toString();

                for (DataSnapshot uniqueIdSnapshot : dataSnapshot.getChildren()) {
                    String imageID = uniqueIdSnapshot.getKey(); //Gets the inner child key (UniqueImageID)
                    Double latitude = uniqueIdSnapshot.child("latitude").getValue(Double.class);
                    Double longitude = uniqueIdSnapshot.child("longitude").getValue(Double.class);


                    if (latitude != null && longitude != null) {
                        Log.d("imageID", "onChildAdded: " + imageID);
                        Log.d("lat", "onChildAdded: " + latitude);
                        Log.d("lng", "onChildAdded: " + longitude);

                        loc = new LatLng(latitude, longitude);
                    }
                    break;
                }
                Log.d("key", "onChildAdded: " + dateTimeKey);
                if (listener != null) {
                    listener.onLocationsLoaded(MapFragment.this.dateTimeKey, loc, tag);
                }
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
