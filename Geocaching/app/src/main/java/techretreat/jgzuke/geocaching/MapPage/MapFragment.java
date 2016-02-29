package techretreat.jgzuke.geocaching.MapPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

import techretreat.jgzuke.geocaching.R;

public class MapFragment extends SupportMapFragment {

    private static final String KEY_USER_ID = "user_id";

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
    public void onCreate(Bundle savedInstanceState) {

    }
}