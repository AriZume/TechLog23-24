package com.example.geosnap.fragments;

import android.content.Context;
import com.example.geosnap.fragments.MyItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class MarkerClusterRenderer extends DefaultClusterRenderer<MyItem> {
    public MarkerClusterRenderer(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
    }
    @Override
    protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
        // use this to make your change to the marker option
        // for the marker before it gets render on the map
        if (item.getTitle() != null && item.getSnippet() != null) {
            markerOptions.title(item.getTitle());
            markerOptions.snippet(item.getSnippet());
        } else if (item.getTitle() != null) {
            markerOptions.title(item.getTitle());
        } else if (item.getSnippet() != null) {
            markerOptions.title(item.getSnippet());
        }
        if(item.getTitle().matches("None")){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        } else if (item.getTitle().matches("Tag 1")) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        } else if (item.getTitle().matches("Tag 2")) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else if (item.getTitle().matches("Tag 3")) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        } else if (item.getTitle().matches("Tag 4")) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        } else if (item.getTitle().matches("Tag 5")) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

    }
}