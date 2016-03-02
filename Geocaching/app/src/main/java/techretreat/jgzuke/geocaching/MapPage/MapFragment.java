package techretreat.jgzuke.geocaching.MapPage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
        makeMarkers();
    }

    private void makeMarkers() {
        if(map == null || mapCaches == null) {
            return;
        }
        markerToCacheId = new HashMap<>(mapCaches.size());
        for(Map.Entry<String, MapCaches.Cache> entry : mapCaches.entrySet()) {
            MapCaches.Cache cache = entry.getValue();
            LatLng position = new LatLng(cache.location.latitude, cache.location.longitude);
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(position)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
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
        tryZoomToCurrentLocation();
        setMapInfoWindowAdapter();
        makeMarkers();
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
                View v = getLayoutInflater(null).inflate(R.layout.map_item_cache, null);

                String cacheId = markerToCacheId.get(marker);
                MapCaches.Cache cache = mapCaches.get(cacheId);

                TextView name = (TextView) v.findViewById(R.id.name);
                TextView description = (TextView) v.findViewById(R.id.description);

                name.setText(cache.name);
                description.setText(cache.description);
                return v;
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
}