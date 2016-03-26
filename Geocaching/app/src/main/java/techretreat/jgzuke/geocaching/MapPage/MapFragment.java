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
    private GoogleMap map;

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
        if (map != null && foundCaches != null) {
            makeMarkers();
        }
    }

    public void setFoundCaches(Map<String, FoundCaches.Cache> foundCaches) {
        this.foundCaches = foundCaches;
        if (map != null && mapCaches != null) {
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
        map = googleMap;
        setMapSettings();
        setMapInfoWindowAdapter();
        tryZoomToStartingLocation();
        if (mapCaches != null && foundCaches != null) {
            makeMarkers();
        }
    }

    private void setMapSettings() {
        boolean compassEnabled = PreferenceUtilities.getCompassEnabled(getContext());
        boolean locationEnabled = PreferenceUtilities.getLocationEnabled(getContext());
        boolean zoomControlsEnabled = PreferenceUtilities.getZoomButtonsEnabled(getContext());
        boolean toolbarEnabled = PreferenceUtilities.getToolbarEnabled(getContext());
        UiSettings settings = map.getUiSettings();
        settings.setCompassEnabled(compassEnabled);
        settings.setMyLocationButtonEnabled(locationEnabled);
        settings.setZoomControlsEnabled(zoomControlsEnabled);
        settings.setMapToolbarEnabled(toolbarEnabled);
    }

    private void setMapInfoWindowAdapter() {
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
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
                TextView description = (TextView) infoView.findViewById(R.id.description);

                name.setText(mapCache.name);
                description.setText(mapCache.description);
                return infoView;
            }
        });
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String cacheId = marker.getTitle();
                MapCaches.Cache mapCache = mapCaches.get(cacheId);
                FoundCaches.Cache foundCache = foundCaches.get(cacheId);
                openViewDetailsDialog(cacheId, mapCache, foundCache);
            }
        });
    }

    private void openViewDetailsDialog(final String cacheId, MapCaches.Cache mapCache, FoundCaches.Cache foundCache) {
        View dialogBody = getLayoutInflater(null).inflate(R.layout.dialog_view_cache_details, null);
        TextView name = (TextView) dialogBody.findViewById(R.id.cache_name);
        TextView description = (TextView) dialogBody.findViewById(R.id.cache_description);
        TextView cacheFound = (TextView) dialogBody.findViewById(R.id.cache_found);

        name.setText(mapCache.name);
        description.setText(mapCache.description);
        boolean hasBeenFound = foundCache != null;
        if (hasBeenFound) {
            cacheFound.setBackgroundResource(R.color.button_inactive);
            cacheFound.setText(UiUtilities.getTimeAgoString(foundCache.found, getContext()));
        } else {
            cacheFound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.setCacheFound(cacheId);
                }
            });
        }

        new AlertDialog.Builder(getContext()).setView(dialogBody).create().show();
    }

    private void makeMarkers() {
        for (Map.Entry<String, MapCaches.Cache> entry : mapCaches.entrySet()) {
            boolean found = foundCaches.containsKey(entry.getKey());
            MapCaches.Cache cache = entry.getValue();
            LatLng position = new LatLng(cache.location.latitude, cache.location.longitude);
            float iconColor = found ? BitmapDescriptorFactory.HUE_AZURE : BitmapDescriptorFactory.HUE_RED;
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(position)
                    .icon(BitmapDescriptorFactory.defaultMarker(iconColor))
                    .title(entry.getKey()));
        }
    }

    public void updateLocationPermissions() {
        tryZoomToStartingLocation();
    }

    private void tryZoomToStartingLocation() {
        Location location = getLocationOrRequestPermissions();
        if (location != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
        }
    }

    private Location getLocationOrRequestPermissions() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M &&
                getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            getActivity().requestPermissions(permissions, GeocachingActivity.MAPS_PAGE_LOCATION_PERMISSIONS_REQUEST_CODE);
            return null;
        }
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        return locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
    }
}