package com.example.geosnap.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.geosnap.Manifest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Locale;

class UserLocation {
    FusedLocationProviderClient fusedLocationProviderClient;
    private final int REQUEST_CODE=100;

    double latitude , longitude

    public getLocation(){
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        getLocation.setOnClickListener(new View.OnClickListener(){
            getLastLocation();
        });


    }
    private void getLastLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if(location != null){
                                Geocoder geocoder= new Geocoder(UserLocation.this, Locale.getDefault());
                                latitude.getLatitude();
                                longitude.getLongitude();
                            }
                        }
                    });
        }else{

            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(UserLocation.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
    }
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permission , @NonNull String[] grantResults){
        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else{
                Toast.makeText(this, "R", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


