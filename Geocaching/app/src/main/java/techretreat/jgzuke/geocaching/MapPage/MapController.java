package techretreat.jgzuke.geocaching.MapPage;

import android.content.Context;

import techretreat.jgzuke.geocaching.FoundPage.FoundCaches;
import techretreat.jgzuke.geocaching.R;
import techretreat.jgzuke.geocaching.Utilities.DataUtilities;
import techretreat.jgzuke.geocaching.Utilities.DataUtilities.Receiver;

public class MapController implements MapFragment.Callback {

    private MapFragment mapFragment;

    public MapController(Context context, MapFragment fragment) {
        this.mapFragment = fragment;

        DataUtilities.getResponse(context, MapCaches.class, R.raw.caches, new Receiver<MapCaches>() {
            @Override
            public void getResults(MapCaches results) {
                mapFragment.setMapCaches(results.caches);
            }
        });
        DataUtilities.getResponse(context, FoundCaches.class, R.raw.caches_found, new Receiver<FoundCaches>() {
            @Override
            public void getResults(FoundCaches results) {
                mapFragment.setFoundCaches(results.caches);
            }
        });
    }

    // MapFragment.Callback
    @Override
    public void setCacheFound(String cacheId) {

    }
}
