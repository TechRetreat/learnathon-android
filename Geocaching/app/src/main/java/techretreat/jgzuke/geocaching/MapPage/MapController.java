package techretreat.jgzuke.geocaching.MapPage;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.Map;

public class MapController implements MapDataInteractor.DataReceiver {

    private String userId;
    private Context context;
    private MapFragment mapFragment;
    private MapDataInteractor mapDataInteractor;

    public MapController(String userId, Context context) {
        this.userId = userId;

        mapFragment = MapFragment.newInstance(userId);
        mapDataInteractor = new MapDataInteractor(userId, context, this);

        mapDataInteractor.getCaches();
    }

    public Fragment getFragment() {
        return mapFragment;
    }

    @Override
    public void getCaches(Map<String, MapCaches.Cache> cacheIdToCache) {
        mapFragment.setCaches(cacheIdToCache);
    }
}
