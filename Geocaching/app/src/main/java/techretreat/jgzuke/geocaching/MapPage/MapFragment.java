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
import techretreat.jgzuke.geocaching.MainActivity.MainActivity;
import techretreat.jgzuke.geocaching.R;
import techretreat.jgzuke.geocaching.Utilities.UiUtilities;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    // Data
    private Map<Marker, String> markerToCacheId;
    private Map<String, MapCaches.Cache> mapCaches;
    private Map<String, FoundCaches.Cache> foundCaches;
    private String startingCacheId;

    // View
    private GoogleMap map;

    // Callback
    private Callback callback;

    public interface Callback {
        void setCacheFound(String cacheId);
    }

    public static MapFragment newInstance(Callback callback) {
        Bundle args = new Bundle();
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        fragment.setCallBack(callback);
        return fragment;
    }

    public void setCallBack(Callback callback) {
        this.callback = callback;
    }

    public void setCaches(Map<String, MapCaches.Cache> mapCaches, Map<String, FoundCaches.Cache> foundCaches) {
        this.mapCaches = mapCaches;
        this.foundCaches = foundCaches;
        if (map != null) {
            makeMarkers();
            if (startingCacheId != null) {
                tryZoomToStartingLocation();
            }
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
        if (mapCaches != null) {
            makeMarkers();
        }
        if (startingCacheId == null || mapCaches != null) {
            tryZoomToStartingLocation();
        }
    }

    private void setMapSettings() {
        //TODO: put these in setings to allow user to change
        UiSettings settings = map.getUiSettings();
        settings.setCompassEnabled(true);
        settings.setMyLocationButtonEnabled(true);
        settings.setZoomControlsEnabled(true);
        settings.setMapToolbarEnabled(false);
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
                String cacheId = markerToCacheId.get(marker);
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
                String cacheId = markerToCacheId.get(marker);
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
        markerToCacheId = new HashMap<>(mapCaches.size());
        for (Map.Entry<String, MapCaches.Cache> entry : mapCaches.entrySet()) {
            boolean found = foundCaches.containsKey(entry.getKey());
            MapCaches.Cache cache = entry.getValue();
            LatLng position = new LatLng(cache.location.latitude, cache.location.longitude);
            float iconColor = found ? BitmapDescriptorFactory.HUE_AZURE : BitmapDescriptorFactory.HUE_RED;
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(position)
                    .icon(BitmapDescriptorFactory.defaultMarker(iconColor)));
            markerToCacheId.put(marker, entry.getKey());
        }
    }

    public void updateLocationPermissions() {
        tryZoomToStartingLocation();
    }

    private void tryZoomToStartingLocation() {
        if (startingCacheId != null) {
            MapCaches.Location location = mapCaches.get(startingCacheId).location;
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude, location.longitude), 13));
        } else {
            Location location = getLocationOrRequestPermissions();
            if (location != null) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
            }
        }
    }

    private Location getLocationOrRequestPermissions() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M &&
                getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            getActivity().requestPermissions(permissions, MainActivity.MAPS_PAGE_LOCATION_PERMISSIONS_REQUEST_CODE);
            return null;
        }
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        return locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
    }

    public void selectCache(String cacheId) {
        if (map == null || mapCaches == null) {
            startingCacheId = cacheId;
        } else {
            MapCaches.Location location = mapCaches.get(cacheId).location;
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude, location.longitude), 13));
        }
    }
}