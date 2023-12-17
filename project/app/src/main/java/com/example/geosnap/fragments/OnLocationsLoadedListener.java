package com.example.geosnap.fragments;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public interface OnLocationsLoadedListener {
    void onLocationsLoaded(String datetime, LatLng location, String tag); // gia tin foto, Bitmap image);
}
