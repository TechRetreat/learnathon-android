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
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import techretreat.jgzuke.geocaching.FoundPage.FoundCaches;
import techretreat.jgzuke.geocaching.MainActivity;
import techretreat.jgzuke.geocaching.R;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private static final String KEY_USER_ID = "user_id";

    private GoogleMap map;
    private String userId;
    private Map<Marker, String> markerToCacheId;
    private Map<String, MapCaches.Cache> mapCaches;
    private Map<String, FoundCaches.Cache> foundCaches;

    public static MapFragment newInstance(String userId) {
        Bundle args = new Bundle();
        MapFragment fragment = new MapFragment();
        args.putString(KEY_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() {
    }

    public void setCaches(Map<String, MapCaches.Cache> mapCaches, Map<String, FoundCaches.Cache> foundCaches) {
        this.mapCaches = mapCaches;
        this.foundCaches = foundCaches;
        makeMarkers();
    }

    private void makeMarkers() {
        if (map == null || mapCaches == null) {
            return;
        }
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setMapSettings();
        tryZoomToCurrentLocation();
        setMapInfoWindowAdapter();
        makeMarkers();
    }

    private void setMapSettings() {
        //TODO: put these in setings to allow user to change
        UiSettings settings = map.getUiSettings();
        settings.setCompassEnabled(true);
        settings.setMyLocationButtonEnabled(true);
        settings.setZoomControlsEnabled(true);
        settings.setMapToolbarEnabled(false);
    }

    public void updateLocationPermissions() {
        tryZoomToCurrentLocation();
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
                final MapCaches.Cache mapCache = mapCaches.get(cacheId);
                final FoundCaches.Cache foundCache = foundCaches.get(cacheId);

                TextView name = (TextView) infoView.findViewById(R.id.name);
                TextView description = (TextView) infoView.findViewById(R.id.description);
                View viewDetailsButton = infoView.findViewById(R.id.view_details);

                name.setText(mapCache.name);
                description.setText(mapCache.description);
                viewDetailsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openViewDetailsDialog(mapCache, foundCache);
                    }
                });
                return infoView;
            }
        });
    }

    private void tryZoomToCurrentLocation() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M &&
                getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            getActivity().requestPermissions(permissions, MainActivity.MAPS_PAGE_LOCATION_PERMISSIONS_REQUEST_CODE);
        } else {
            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
            }
        }

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.38224, -80.32382), 13));
    }

    private void openViewDetailsDialog(MapCaches.Cache mapCache, FoundCaches.Cache foundCache) {
        View dialogBody = getLayoutInflater(null).inflate(R.layout.map_item_cache, null);
        TextView name = (TextView) dialogBody.findViewById(R.id.cache_name);
        TextView description = (TextView) dialogBody.findViewById(R.id.cache_description);
        TextView cacheFound = (TextView) dialogBody.findViewById(R.id.cache_found);

        name.setText(mapCache.name);
        description.setText(mapCache.description);
        boolean hasBeenFound = foundCache != null;
        if (hasBeenFound) {
            cacheFound.setBackgroundResource(R.color.button_inactive);
            cacheFound.setText(DateUtils.getRelativeTimeSpanString(foundCache.found));
        } else {
            cacheFound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO set as found with current time
                }
            });
        }

        new AlertDialog.Builder(getContext())
                .setView(dialogBody).create().show();
    }
}