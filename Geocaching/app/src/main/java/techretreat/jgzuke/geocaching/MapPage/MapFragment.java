package techretreat.jgzuke.geocaching.MapPage;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import techretreat.jgzuke.geocaching.FoundPage.FoundCaches;
import techretreat.jgzuke.geocaching.Activity.GeocachingActivity;
import techretreat.jgzuke.geocaching.R;
import techretreat.jgzuke.geocaching.Utilities.PreferenceUtilities;
import techretreat.jgzuke.geocaching.Utilities.UiUtilities;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    // Data
    private Map<String, MapCaches.Cache> mapCaches;
    private Map<String, FoundCaches.Cache> foundCaches;

    // View
    private GoogleMap googleMap;

    // Callback
    private Callback callback;

    public interface Callback {
        void setCacheFound(String cacheId);
    }

    public static MapFragment newInstance() {
        Bundle args = new Bundle();
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback = new MapController(getContext(), this);
    }

    public void setMapCaches(Map<String, MapCaches.Cache> mapCaches) {
        this.mapCaches = mapCaches;
        if (googleMap != null && foundCaches != null) {
            makeMarkers();
        }
    }

    public void setFoundCaches(Map<String, FoundCaches.Cache> foundCaches) {
        this.foundCaches = foundCaches;
        if (googleMap != null && mapCaches != null) {
            makeMarkers();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setMapSettings();
        setMarkerPopupAdapter();
        zoomToUserLocation();
        if (mapCaches != null && foundCaches != null) {
            makeMarkers();
        }
    }

    private void setMapSettings() {
        boolean compassEnabled = PreferenceUtilities.getCompassEnabled(getContext());
        boolean locationEnabled = PreferenceUtilities.getLocationEnabled(getContext());
        boolean zoomControlsEnabled = PreferenceUtilities.getZoomButtonsEnabled(getContext());
        boolean toolbarEnabled = PreferenceUtilities.getToolbarEnabled(getContext());
        UiSettings settings = googleMap.getUiSettings();
        settings.setCompassEnabled(compassEnabled);
        settings.setMyLocationButtonEnabled(locationEnabled);
        settings.setZoomControlsEnabled(zoomControlsEnabled);
        settings.setMapToolbarEnabled(toolbarEnabled);
    }

    private void setMarkerPopupAdapter() {
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoView = getLayoutInflater(null).inflate(R.layout.map_item_cache, null);
                String cacheId = marker.getTitle();
                MapCaches.Cache mapCache = mapCaches.get(cacheId);

                TextView name = (TextView) infoView.findViewById(R.id.name);
                TextView description = (TextView) infoView.findViewById(R.id.difficulty);

                name.setText(mapCache.name);
                description.setText(mapCache.description);
                return infoView;
            }
        });
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String cacheId = marker.getTitle();
                //openViewDetailsDialog(cacheId);
            }
        });
    }

    private void makeMarkers() {
        for (Map.Entry<String, MapCaches.Cache> entry : mapCaches.entrySet()) {
            boolean found = foundCaches.containsKey(entry.getKey());
            MapCaches.Cache cache = entry.getValue();
            LatLng position = new LatLng(cache.location.latitude, cache.location.longitude);
            float iconColor = found ? BitmapDescriptorFactory.HUE_AZURE : BitmapDescriptorFactory.HUE_RED;
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .icon(BitmapDescriptorFactory.defaultMarker(iconColor))
                    .title(entry.getKey()));
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