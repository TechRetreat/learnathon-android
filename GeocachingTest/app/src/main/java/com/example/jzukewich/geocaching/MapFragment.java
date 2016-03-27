package com.example.jzukewich.geocaching;

import android.app.AlertDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
<<<<<<< HEAD
import android.support.v4.app.Fragment;
=======
>>>>>>> origin/master
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by jgzuke on 16-03-25.
 */
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private Map<String, MapCache> mapCaches;
    private Map<String, FoundCache> foundCaches;

    // We will use this Object to control the map shown on the screen
    private GoogleMap googleMap;

    // onCreate is called when the Fragment is created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            public void onResults(Map<String, FoundCache> results) {
                foundCaches = results;
            }
        });
        DataUtilities.getMapCaches(getContext(), new DataUtilities.MapCachesReceiver() {
            @Override
            public void onResults(Map<String, MapCache> results) {
                mapCaches = results;
                makeMarkers();
                setMarkerPopupAdapter();
            }
        });
    }

    private void makeMarkers() {
        for (MapCache cache : mapCaches.values()) {
            LatLng position = new LatLng(cache.location.latitude, cache.location.longitude);
            boolean hasBeenFound = foundCaches.containsKey(cache.name);
            float iconColor = hasBeenFound? BitmapDescriptorFactory.HUE_AZURE : BitmapDescriptorFactory.HUE_RED;
            googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .icon(BitmapDescriptorFactory.defaultMarker(iconColor))
                    .title(cache.name));
        }
    }

    private void setMarkerPopupAdapter() {
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoView = getLayoutInflater(null).inflate(R.layout.marker_click_popup, null);
                TextView name = (TextView) infoView.findViewById(R.id.name);
                TextView description = (TextView) infoView.findViewById(R.id.difficulty);

                final String cacheName = marker.getTitle();
                MapCache cache = mapCaches.get(cacheName);
                name.setText(cache.name);
                description.setText(FormattingUtilities.getDifficultyString(cache.difficulty, getContext()));
                return infoView;
            }
        });
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String cacheName = marker.getTitle();
                openViewDetailsDialog(cacheName);
            }
        });
    }

    private void openViewDetailsDialog(String cacheName) {
        View dialogView = getLayoutInflater(null).inflate(R.layout.see_more_dialog, null);
        TextView name = (TextView) dialogView.findViewById(R.id.cache_name);
        TextView difficulty = (TextView) dialogView.findViewById(R.id.cache_difficulty);
        TextView description = (TextView) dialogView.findViewById(R.id.cache_description);
        Button found = (Button) dialogView.findViewById(R.id.cache_found);

        MapCache cache = mapCaches.get(cacheName);
        name.setText(cache.name);
        difficulty.setText(FormattingUtilities.getDifficultyString(cache.difficulty, getContext()));
        description.setText(cache.description);

        if (foundCaches.containsKey(cacheName)) {
            FoundCache foundCache = foundCaches.get(cacheName);
            found.setClickable(false);
            found.setText(FormattingUtilities.getTimeAgoString(foundCache.found, getContext()));
        } else {
            found.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Set this cache to found
                }
            });
        }

        new AlertDialog.Builder(getContext()).setView(dialogView).create().show();
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
