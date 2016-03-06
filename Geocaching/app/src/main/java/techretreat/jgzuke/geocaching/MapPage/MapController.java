package techretreat.jgzuke.geocaching.MapPage;

import android.content.Context;

import techretreat.jgzuke.geocaching.FoundPage.FoundCaches;
import techretreat.jgzuke.geocaching.Utilities.DataUtilities;
import techretreat.jgzuke.geocaching.Utilities.DataUtilities.Receiver;

public class MapController implements MapFragment.Callback {

    private Context context;
    private MapFragment mapFragment;

    public MapController(Context context, MapFragment fragment) {
        this.context = context;
        this.mapFragment = fragment;

        DataUtilities.getMapCaches(context, new Receiver<MapCaches>() {
            @Override
            public void onResults(MapCaches results) {
                mapFragment.setMapCaches(results.caches);
            }

            @Override
            public void onError() {

            }
        });
        DataUtilities.getFoundCaches(context, new Receiver<FoundCaches>() {
            @Override
            public void onResults(FoundCaches results) {
                mapFragment.setFoundCaches(results.caches);
            }

            @Override
            public void onError() {

            }
        });
    }

    // MapFragment.Callback
    @Override
    public void setCacheFound(String cacheId) {

    }
}
