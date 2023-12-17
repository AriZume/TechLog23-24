package com.example.geosnap.fragments;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public interface OnLocationsLoadedListener {
    void onLocationsLoaded(String datetime, LatLng location, String tag);
}
