package techretreat.jgzuke.geocaching.MapPage;

import android.content.Context;

import java.util.Map;

import techretreat.jgzuke.geocaching.Utilities.DataUtilities;
import techretreat.jgzuke.geocaching.FoundPage.FoundCaches;
import techretreat.jgzuke.geocaching.R;

public class MapDataInteractor {

    // System objects
    private Context context;

    private DataReceiver reciever;
    public interface DataReceiver {
        void getCaches(Map<String, MapCaches.Cache> mapCaches,
                       Map<String, FoundCaches.Cache> foundCaches);
    }

    public MapDataInteractor(Context context, DataReceiver reciever) {
        this.context = context;
        this.reciever = reciever;

        getCaches();
    }

    public void getCaches() {
        MapCaches mapCaches = DataUtilities.getResponse(context, MapCaches.class, R.raw.caches);
        FoundCaches foundCaches = DataUtilities.getResponse(context, FoundCaches.class, R.raw.caches_found);
        reciever.getCaches(mapCaches.caches, foundCaches.caches);
    }

    public void setCacheFound(String cacheId) {
        //TODO when theres a server
    }
}