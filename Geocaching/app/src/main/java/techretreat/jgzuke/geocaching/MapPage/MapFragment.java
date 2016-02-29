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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import techretreat.jgzuke.geocaching.MainActivity;
import techretreat.jgzuke.geocaching.R;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private static final String KEY_USER_ID = "user_id";

    private GoogleMap map;
    private String userId;

    public static MapFragment newInstance(String userId) {
        Bundle args = new Bundle();
        MapFragment fragment = new MapFragment();
        args.putString(KEY_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        tryZoomToCurrentLocation();
    }

    public void updateLocationPermissions() {
        tryZoomToCurrentLocation();
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
    }
}