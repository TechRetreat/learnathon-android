package com.example.jzukewich.geocaching;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jgzuke on 16-03-25.
 */
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private Map<String, MapCaches.Cache> mapCaches;
    private Map<String, FoundCaches.Cache> foundCaches;

    // We will use this Object to control the map shown on the screen
    private GoogleMap googleMap;

    // onCreate is called when the Fragment is created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // This requests access to the GoogleMap, we will talk about what Async means later
        getMapAsync(this);
    }

    // Called when the map is ready to use
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // Save this object to use later
        this.googleMap = googleMap;

        zoomToUserLocation();

        DataUtilities.getFoundCaches(getContext(), new DataUtilities.FoundCachesReceiver() {
            @Override
            public void onResults(FoundCaches results) {
                foundCaches = results.caches;
            }
        });
        DataUtilities.getMapCaches(getContext(), new DataUtilities.MapCachesReceiver() {
            @Override
            public void onResults(MapCaches results) {
                mapCaches = results.caches;
                makeMarkers();
            }
        });
    }

    private void makeMarkers() {
        for (MapCaches.Cache cache : mapCaches.values()) {
            LatLng position = new LatLng(cache.location.latitude, cache.location.longitude);
            boolean hasBeenFound = foundCaches.containsKey(cache.name);
            float iconColor = hasBeenFound? BitmapDescriptorFactory.HUE_AZURE : BitmapDescriptorFactory.HUE_RED;
            googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .icon(BitmapDescriptorFactory.defaultMarker(iconColor))
                    .title(cache.name));
        }
    }

    private void zoomToUserLocation() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
        if (location != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
        } else {
            // Default to a location in Waterloo
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.38224, -80.32682), 13));
        }
    }
}
