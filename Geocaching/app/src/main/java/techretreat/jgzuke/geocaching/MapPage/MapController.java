package techretreat.jgzuke.geocaching.MapPage;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.Map;

import techretreat.jgzuke.geocaching.FoundPage.FoundCaches;

public class MapController implements MapDataInteractor.DataReceiver, MapFragment.Callback {

    private MapFragment mapFragment;
    private MapDataInteractor mapDataInteractor;

    public MapController(Context context) {
        mapFragment = MapFragment.newInstance(this);
        mapDataInteractor = new MapDataInteractor(context, this);
    }

    public Fragment getFragment() {
        return mapFragment;
    }

    // MapDataInteractor.DataReceiver
    @Override
    public void getCaches(Map<String, MapCaches.Cache> mapCaches, Map<String, FoundCaches.Cache> foundCaches) {
        mapFragment.setCaches(mapCaches, foundCaches);
    }

    // MapFragment.Callback
    @Override
    public void setCacheFound(String cacheId) {

    }
}
